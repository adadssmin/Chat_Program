package lunch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.UserDao;
import data.Room;
import data.UserData;
import dbutil.MyConnectionProvider;

public class ServerHandler extends Thread {
	private UserData userData;
	private UserDao userdao;
	private Socket socket;
	private Connection conn;
	private ResultSet rs;
	private Room thisRoom; // 사용자가 있는 방
	private Room roomTemp;
	private BufferedReader bReader;
	private PrintWriter pWriter;

	private ArrayList<ServerHandler> allUserList; // 사용자 전체
	private ArrayList<ServerHandler> standbyUserList; // 대기중인 사용자
	private ArrayList<Room> totalRoomList; // 전체 방 리스트
	private String logoutId;

	public ServerHandler(Socket socket, ArrayList<ServerHandler> allUserList, ArrayList<ServerHandler> standbyUserList,
			ArrayList<Room> totalRoomList, Connection conn) {
		this.userData = new UserData();
		this.thisRoom = new Room();
		this.roomTemp = new Room();
		this.socket = socket;
		this.allUserList = allUserList;
		this.standbyUserList = standbyUserList;
		this.totalRoomList = totalRoomList;
		this.conn = conn;
		this.logoutId = "";
		userdao = new UserDao();
		try {
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String[] line = null;
			while (true) {// 서버가 받는곳
				line = bReader.readLine().split(">");

				if (line == null) {
					break;
				}
				if (line[0].compareTo(Protocol.IDSEARCHCHECK) == 0) {
					String inputId = line[1];
					// 아이디를 DAO에 넣어 중복을 확인한다.
					if (UserDao.findExistId(inputId)) {// 중복되는 아이디가 존재
						pWriter.println(Protocol.IDSEARCHCHECK_OK + ">" + "사용중인 아이디입니다." + ">");
						pWriter.flush();
					} else {// 중복 아닐때
						pWriter.println(Protocol.IDSEARCHCHECK_NO + ">" + "사용가능한 아이디입니다." + ">");
						pWriter.flush();
					}
				} else if (line[0].compareTo(Protocol.PWCHECK) == 0) {
					String[] pw = line[1].split("@");
					if (pw.length == 2) {
						if (pw[0].equals(pw[1])) {// 두 비밀번호 비교
							pWriter.println(Protocol.PWCHECK_OK + ">" + "비밀번호가 일치합니다.");
							pWriter.flush();
						} else {
							pWriter.println(Protocol.PWCHECK_NO + ">" + "비밀번호가 틀립니다.");
							pWriter.flush();
						}
					}
				} else if (line[0].compareTo(Protocol.REGISTER) == 0) {// 가입버튼눌러서 오면
					String[] joinInfo = line[1].split("@");
					String id = joinInfo[0];
					String pw = joinInfo[1];
					String email = joinInfo[2] + "@" + joinInfo[3];
					System.out.println(email);
					userdao.addUser(id, pw, email);

					if (userdao.findExistId(id)) {
						pWriter.println(Protocol.REGISTER_OK + ">" + "회원가입 완료");
						pWriter.flush();
					} else {
						pWriter.println(Protocol.REGISTER_NO + ">" + "회원가입 실패");
						pWriter.flush();
					}
				} else if (line[0].compareTo(Protocol.LOGIN) == 0) {
					// --------------------로그인--------------------
					String userContent[] = line[1].split("@");

					boolean isLogin = true; // 로그인 되어있는지 확인(중복로그인 막기)
					for (int i = 0; i < standbyUserList.size(); i++) {
						if ((standbyUserList.get(i).userData.getId()).compareTo(userContent[0]) == 0) {
							isLogin = false;
						}
					}
					int count = 0;
					if (isLogin) {
						if (UserDao.loginUser(userContent[0], userContent[1])) {
							this.userData = userdao.getUserById(userContent[0]);
							count++;
						}

						if (count == 0) {// 로그인 실패
							pWriter.println(Protocol.LOGIN_NO + ">" + "[로그인 실패]");
							pWriter.flush();

							userData.setId("");
							userData.setPassword("");

						} else {// 로그인 성공
							this.userData.setIp(userContent[2]);
							String userInfo = this.userData.getId() + "@" + this.userData.getEmail();
							pWriter.println(Protocol.LOGIN_OK + ">" + userInfo);
							pWriter.flush();
							standbyUserList.add(this);// 대기중인 인원 추가
							String userListLine = "";
							for (int i = 0; i < standbyUserList.size(); i++) {
								userListLine += ("(" + standbyUserList.get(i).userData.getId() + ")/");
							}
							System.out.println("[접속한 회원 정보] 아이디: " + userContent[0] + ", 비번: " + userContent[1]
									+ ", ip: " + userContent[2]);
							System.out.println("[접속 인원수] " + standbyUserList.size() + " ,[접속 중인 사용자]" + userListLine);
							System.out.println("[전체 방 갯수] " + totalRoomList.size());

							refreshRoomlist();// 만들어져 있는 방 보여주기
						}
					} else {
						pWriter.println(Protocol.LOGIN_NO + ">" + "로그인 중입니다.");
						pWriter.flush();
					}
				} else if (line[0].compareTo(Protocol.SEARCHID) == 0) {// 아이디 찾기
					String email = line[1];
					List<String> ids = new ArrayList<>();
					ids.addAll(UserDao.getUserByEmail(email));
					String idsStr = "";
					if (ids.size() != 0) {
						for (String id : ids) {
							idsStr += (id + "@");
						}
						pWriter.println(Protocol.SEARCHID_OK + ">" + idsStr);
						pWriter.flush();
					} else {
						pWriter.println(Protocol.SEARCHID_NO + ">" + "찾으시는 아이디가 없습니다.");
						pWriter.flush();
					}

				} else if (line[0].compareTo(Protocol.SEARCHPASSWORD) == 0) {// 비번 찾기
					String id = line[1];
					String email = line[2];
					if (UserDao.findPassword(id, email)) {
						pWriter.println(Protocol.SEARCHPASSWORD_OK + ">" + "새로운 비밀번호 설정");
						pWriter.flush();
					} else {
						pWriter.println(Protocol.SEARCHPASSWORD_NO + ">" + "찾으시는 계정이 없습니다.");
						pWriter.flush();
					}

				} else if (line[0].compareTo(Protocol.CHANGEPASSWORD) == 0) {
					String id = line[1];
					String password = line[2];
					if (UserDao.updatePW(password, id)) {
						pWriter.println(Protocol.CHANGEPASSWORD_OK + ">" + "비밀번호를 변경완료");
						pWriter.flush();
					} else {
						pWriter.println(Protocol.CHANGEPASSWORD_NO + ">" + "비밀번호를 변경실패");
						pWriter.flush();
					}

				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					String thisId = standbyUserList.get(standbyUserList.indexOf(this)).userData.getId();
					logoutId = thisId;
					standbyUserList.remove(this);
					System.out.println("[접속 인원수] " + standbyUserList.size());

					String userListLine = "";
					for (int i = 0; i < standbyUserList.size(); i++) {
						userListLine += ("(" + standbyUserList.get(i).userData.getId() + ") ");
					}
					System.out.println("[접속 중인 사용자]" + userListLine);

					if (!logoutId.isEmpty()) {
						System.out.println("[사용자  접속종료] " + logoutId);
						logoutId = "";
					}

					userData.setId("");
					userData.setPassword("");
				} else if (line[0].compareTo(Protocol.MAKEROOM) == 0) {// 방만들기
					String userContent[] = line[1].split("@");

					String qurey = "";
					roomTemp = new Room();
					if (userContent.length == 4) {// 비공개방
						roomTemp.setTitle(userContent[0]);
						roomTemp.setRoom_Password(userContent[1]);
						roomTemp.setUser_Count(userContent[2]);
						roomTemp.setAdmin(userData.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[3]));
					} else if (userContent.length == 3) {// 공개방
						roomTemp.setTitle(userContent[0]);
						roomTemp.setUser_Count(userContent[1]);
						roomTemp.setAdmin(userData.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[2]));
					}
					totalRoomList.add(roomTemp);// 방 리스트에 만들어진 방 추가

					int priNumber = 0;// 방 고유번호 설정
					priNumber = totalRoomList.size();
					roomTemp.setRoom_Num(priNumber);// 방 고유번호 설
					roomTemp.userListInRoom.add(this);// 방에 있는 사용자 리스트에 추가
					roomTemp.setUserCountInRoom(roomTemp.userListInRoom.size());
					thisRoom = roomTemp;// 현재 방으로 지정

					String roomListLine = "";
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getPriv() == 1) {
							roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
									+ totalRoomList.get(i).getTitle() + "@"
									+ totalRoomList.get(i).getRoom_Password() + "@"
									+ totalRoomList.get(i).getUser_Count() + "@" 
									+ totalRoomList.get(i).getAdmin() + "@"
									+ totalRoomList.get(i).getPriv() + "@" 
									+ totalRoomList.get(i).getUserCountInRoom()
									+ "-");
						} else if (totalRoomList.get(i).getPriv() == 0) {
							roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
									+ totalRoomList.get(i).getTitle() + "@" 
									+ totalRoomList.get(i).getUser_Count() + "@" 
									+ totalRoomList.get(i).getAdmin() + "@" 
									+ totalRoomList.get(i).getPriv() + "@"
									+ totalRoomList.get(i).getUserCountInRoom()
									+ "-");
						}
					}
					for (int i = 0; i < standbyUserList.size(); i++) {
						if (standbyUserList.get(i).userData.getId().compareTo(roomTemp.getAdmin()) == 0) {
							// 사용자가 방만들면 바로 채팅방들어가도록
							standbyUserList.get(i).pWriter
									.println(Protocol.MAKEROOM_ADMIN_OK + ">" + roomListLine + ">"  + roomTemp.getAdmin());
							standbyUserList.get(i).pWriter.flush();
						} else {
							// 다른사용자들이 만들어진 방을 볼 수 있도록
							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
							standbyUserList.get(i).pWriter.flush();
						}
					}
					System.out.println(roomTemp.getAdmin() + "님이 " + roomTemp.getRoom_Num() + "번방을 생성하셨습니다.");
					standbyUserList.remove(this);
					System.out.println("[새로운 방 생성 후 대기자] " + standbyUserList.size());

				} else if (line[0].compareTo(Protocol.ENTER_PASSWORD) == 0) { 
					String thisId = standbyUserList.get(standbyUserList.indexOf(this)).userData.getId();
					int enterRoom_num = Integer.valueOf(line[1]);
					int index = 0;
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == enterRoom_num) {
							if (!totalRoomList.get(i).getRoom_Password().equals(line[2])) {
								pWriter.println(Protocol.ENTER_NO + ">" + "비밀번호가 일치하지 않습니다.");
								pWriter.flush();
							} else {
								System.out.println("입장?");
								enterRoom(line[1]);
							}
						}
					}
				} else if (line[0].compareTo(Protocol.ENTER) == 0) {
					enterRoom(line[1]);
					
				} else if (line[0].compareTo(Protocol.EXITROOM) == 0) {
					int thisRoomNum = 0;
					String thisRoomUserCount = "";
					boolean isUserIn = true;// 사용자가 남아있는지 확인
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == thisRoom.getRoom_Num()) {// 현재방 선택
							if (totalRoomList.get(i).userListInRoom.size() == 1) {
								// 마지막 사람이 퇴장할 때
								System.out.println("[마지막 사람 대화방 퇴장] " + thisRoom.getRoom_Num() + "번 방");
								totalRoomList.remove(thisRoom);// 전체 방 리스트에서 삭제
								thisRoom = new Room();// 현재방을 나타내는 장치 초기화
								isUserIn = false;
								
							} else {
								System.out.println("[대화방 퇴장]");
								totalRoomList.get(i).userListInRoom.remove(this);
								totalRoomList.get(i).setUserCountInRoom(totalRoomList.get(i).userListInRoom.size());
								thisRoomUserCount = thisRoom.getUserCountInRoom() + " / " + thisRoom.getUser_Count();
								thisRoom = new Room();
								thisRoomNum = i;// 현재방 기억
							}
						}
					}
					if (isUserIn) {
						String userInRoom = ""; // 모든 방을 검색하여 지금의 사용자가 들어있는지 확인하기 위해
						for (int i = 0; i < totalRoomList.get(thisRoomNum).userListInRoom.size(); i++) {
							userInRoom += (totalRoomList.get(thisRoomNum).userListInRoom.get(i).userData.getId() + "@");
						}
						System.out.println(totalRoomList.get(thisRoomNum).getRoom_Num() + "번방에 있는 사람 :" + userInRoom);
						System.out.println("[방 사용자 수] " + totalRoomList.get(thisRoomNum).userListInRoom.size());
					}
					
					standbyUserList.add(this);
					System.out.println("퇴장 후 totoalRoomList의 수 " + totalRoomList.size());
					System.out.println("[대기중인 사용자수] " + standbyUserList.size());
					
					String roomListLine = "";
					String userIdInRoom = "";
					if (totalRoomList.size() > 0) {
						for (int i = 0; i < totalRoomList.size(); i++) {
							roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
									+ totalRoomList.get(i).getTitle() + "@"
									+ totalRoomList.get(i).getRoom_Password() + "@" 
									+ totalRoomList.get(i).getUser_Count() + "@"
									+ totalRoomList.get(i).getAdmin() + "@" 
									+ totalRoomList.get(i).getPriv() + "@"
									+ totalRoomList.get(i).getUserCountInRoom() 
									+ "-");
						}
						if (totalRoomList.get(thisRoomNum).userListInRoom.size() > 0) {
							for (int j = 0; j < totalRoomList.get(thisRoomNum).userListInRoom.size(); j++) {
								userIdInRoom += (totalRoomList.get(thisRoomNum).userListInRoom.get(j).userData
										.getId() + "@");
							}
						}

						System.out.println(thisRoomUserCount);
						for (int j = 0; j < totalRoomList.get(thisRoomNum).userListInRoom.size(); j++) {
							totalRoomList.get(thisRoomNum).userListInRoom.get(j).pWriter.println(Protocol.CHATROOMSUBINFO + ">" 
									+ thisRoomUserCount + ">" 
									+ userIdInRoom);
							totalRoomList.get(thisRoomNum).userListInRoom.get(j).pWriter.flush();
						}
					} else {
						roomListLine = "-";
					}
					
					for (int i = 0; i < standbyUserList.size(); i++) {
						standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
						standbyUserList.get(i).pWriter.flush();
					}

					
					
				} else if (line[0].compareTo(Protocol.CHATMESSAGE) == 0) {
					int userInRoomSize = totalRoomList.get(totalRoomList.indexOf(thisRoom)).userListInRoom.size();
					for (int i = 0; i < userInRoomSize; i++) {
						totalRoomList.get(totalRoomList.indexOf(thisRoom)).userListInRoom.get(i)
							.pWriter.println(Protocol.CHATMESSAGE_OK + ">"
									+ userData.getId() + ">" + line[1]);
						totalRoomList.get(totalRoomList.indexOf(thisRoom)).userListInRoom.get(i)
							.pWriter.flush();
					}
				}
			}
			
			bReader.close();
			pWriter.close();
			socket.close();
		} catch (SocketException se) {
			if (this.userData.getIp() != null) {
				System.out.println("[클라이언트 접속 종료] ip: " + this.userData.getIp());
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public void refreshRoomlist() {
		String roomListLine = "";
		if (totalRoomList.size() != 0) {
			for (int i = 0; i < totalRoomList.size(); i++) {
				if (totalRoomList.get(i).getPriv() == 1) {
					roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
							+ totalRoomList.get(i).getTitle() + "@"
							+ totalRoomList.get(i).getRoom_Password() + "@" 
							+ totalRoomList.get(i).getUser_Count() + "@"
							+ totalRoomList.get(i).getAdmin() + "@" 
							+ totalRoomList.get(i).getPriv() + "@"
							+ totalRoomList.get(i).getUserCountInRoom() 
							+ "-");
				} else if (totalRoomList.get(i).getPriv() == 0) {
					roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
							+ totalRoomList.get(i).getTitle() + "@"
							+ totalRoomList.get(i).getUser_Count() + "@" 
							+ totalRoomList.get(i).getAdmin() + "@"
							+ totalRoomList.get(i).getPriv() + "@" 
							+ totalRoomList.get(i).getUserCountInRoom() 
							+ "-");
				}
			}
//			for (int i = 0; i < standbyUserList.size(); i++) {
//				// 다른사용자들이 만들어진 방을 볼 수 있도록
//				standbyUserList.get(i).pWriter.println(Protocol.CHATROOMSUBINFO + ">" + roomListLine);
//				standbyUserList.get(i).pWriter.flush();
//			}
		} else {
			roomListLine = "-";
		}
		for (int i = 0; i < standbyUserList.size(); i++) {
			// 다른사용자들이 만들어진 방을 볼 수 있도록
			standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
			standbyUserList.get(i).pWriter.flush();
		}
	}
	
	private void enterRoom(String line) {
		String thisId = standbyUserList.get(standbyUserList.indexOf(this)).userData.getId();
		int enterRoom_num = Integer.valueOf(line);
		
		int index = 0;
		for (int i = 0; i < totalRoomList.size(); i++) {
			if (totalRoomList.get(i).getRoom_Num() == enterRoom_num) {
				totalRoomList.get(i).userListInRoom.add(this); // 방에 유저 넣고
				totalRoomList.get(i).setUserCountInRoom(totalRoomList.get(i).userListInRoom.size());
				thisRoom = totalRoomList.get(i);
				index = i;
			}
		}
		System.out.println("[대화방 입장] (" + this.userData.getId() + ")-" + enterRoom_num + "번 방");
		
		String roomListLine = "";
		for (int i = 0; i < totalRoomList.size(); i++) {
			roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
					+ totalRoomList.get(i).getTitle() + "@"
					+ totalRoomList.get(i).getRoom_Password() + "@" 
					+ totalRoomList.get(i).getUser_Count() + "@"
					+ totalRoomList.get(i).getAdmin() + "@" 
					+ totalRoomList.get(i).getPriv() + "@"
					+ totalRoomList.get(i).getUserCountInRoom() 
					+ "-");
		}
		
		for (int i = 0; i < standbyUserList.size(); i++) {
			if (standbyUserList.get(i).userData.getId().compareTo(thisId) == 0) {
				standbyUserList.get(i).pWriter.println(Protocol.ENTER_OK + ">");
				standbyUserList.get(i).pWriter.flush();
			} else {
				standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
				standbyUserList.get(i).pWriter.flush();
			}
		}
		
		String userIdInRoom = "";
		for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
			userIdInRoom += (totalRoomList.get(index).userListInRoom.get(i).userData
					.getId() + "@");
		}
		System.out.println(thisRoom.getUserCountInRoom()+ " / " + thisRoom.getUser_Count());
		for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
			totalRoomList.get(index).userListInRoom.get(i).pWriter.println(Protocol.CHATROOMSUBINFO + ">" 
					+ (thisRoom.getUserCountInRoom() + " / " + thisRoom.getUser_Count()) + ">" 
					+ userIdInRoom);
			totalRoomList.get(index).userListInRoom.get(i).pWriter.flush();
		}
		
		standbyUserList.remove(this);
		System.out.println("[방입장 후 대기자 수] " + standbyUserList.size());
	}
}
