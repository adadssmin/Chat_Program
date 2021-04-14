package lunch;

import java.io.BufferedReader;
import java.io.File;
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

import data.Room;
import data.UserData;

public class ServerHandler extends Thread {
	private UserData user;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Room thisRoom; // 사용자가 있는 방
	private BufferedReader bReader;
	private PrintWriter pWriter;

	private ArrayList<ServerHandler> allUserList; // 사용자 전체
	private ArrayList<ServerHandler> standbyUserList; // 대기중인 사용자
	private ArrayList<Room> totalRoomList; // 전체 방 리스트
	private String fileName;
	private String logoutId;

	public ServerHandler(Socket socket, ArrayList<ServerHandler> allUserList, ArrayList<ServerHandler> standbyUserList,
			ArrayList<Room> totalRoomList, Connection conn) {
		this.user = new UserData();
		this.thisRoom = new Room();
		this.socket = socket;
		this.allUserList = allUserList;
		this.standbyUserList = standbyUserList;
		this.totalRoomList = totalRoomList;
		this.conn = conn;
		this.fileName = "";
		this.logoutId = "";
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
			while (true) {
				line = bReader.readLine().split("\\>");

				if (line == null) {
					break;
				}

				if (line[0].compareTo(Protocol.REGISTER) == 0) {
					// --------------------회원가입--------------------
					String userInform[] = line[1].split("@");

					String query = "INSERT INTO user (id, password)" + " VALUE (?, ?);";
					try {
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, userInform[0]);
						pstmt.setString(2, userInform[1]);
						int result = pstmt.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (line[0].compareTo(Protocol.LOGIN) == 0) {
					// --------------------로그인--------------------
					boolean isLogin = true; // 로그인 되어있는지 확인(중복로그인 막기)
					System.out.println("[로그인]");
					String userContent[] = line[1].split("@");

					System.out.println("[접속한 회원 정보] 아이디: " + userContent[0] + ", 비번: " + userContent[1]);

					for (int i = 0; i < standbyUserList.size(); i++) {
						if ((standbyUserList.get(i).user.getId()).compareTo(userContent[0]) == 0) {
							isLogin = false;
						}
					}
//					System.out.println(isLogin);

					if (isLogin) {
						String queryU = "SELECT * FROM user WHERE id = '" + userContent[0] + "' AND password = '"
								+ userContent[1] + "';";
						int count = 0;
						try {
							pstmt = conn.prepareStatement(queryU);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								user.setId(rs.getString("id"));
								user.setPassword(rs.getString("password"));
								count++;
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

//						System.out.println(count);

						if (count == 0) {// 로그인 실패
							pWriter.println(Protocol.LOGIN_NO + ">" + "[로그인 실패]");
							pWriter.flush();

							user.setId("");
							user.setPassword("");
						} else {// 로그인 성공
							pWriter.println(Protocol.LOGIN_OK + ">" + this.user.getId());
							standbyUserList.add(this);// 대기중인 인원 추가
							String userLine = "";
							// 접속했을 때 접속해 있는 사람 보이기위해 userLine을 만듬
							for (int i = 0; i < standbyUserList.size(); i++) {
								userLine += (standbyUserList.get(i).user.getId() + ":");
							}

							for (int i = 0; i < standbyUserList.size(); i++) {
								standbyUserList.get(i).pWriter
										.println(Protocol.LOGIN_OK + ">" + user.getId() + ">님이 접속하셨습니다.>" + userLine);
								standbyUserList.get(i).pWriter.flush();
							}
							System.out.println("[접속 인원] " + standbyUserList.size());

							String userListLine = "";
							for (int i = 0; i < standbyUserList.size(); i++) {
								userListLine += ("(" + standbyUserList.get(i).user.getId() + ") ");
							}
							System.out.println("[접속 중인 사용자]" + userListLine);

							if (totalRoomList.size() != 0) {
								System.out.println("[방 정보]");
								for (Room room : totalRoomList) {
									System.out.println(room.toString() + "현재방 인원수: " + room.userListInRoom.size());
								}
							}
							
							//db에 있는 방 보기
							totalRoomList.clear();
							String qureyR = "SELECT * FROM room;";
							pstmt = conn.prepareStatement(qureyR);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								int room_num = rs.getInt("room_num");
								String room_title = rs.getString("room_title");
								String room_password = rs.getString("room_password");
								String usercount = rs.getString("usercount");
								String admin = rs.getString("admin");
								int private_room = rs.getInt("private_room");
								totalRoomList.add(new Room(room_num, room_title, room_password, 
										usercount, admin, private_room));
							}
							
							
							System.out.println("[전체 방 갯수] " + totalRoomList.size());
							String roomListMessage = "";

							for (int i = 0; i < totalRoomList.size(); i++) {
								roomListMessage += (totalRoomList.get(i).getRoom_Num() + "@"
										+ totalRoomList.get(i).getTitle() + "@"
										+ totalRoomList.get(i).getRoom_Password() + "@"
										+ totalRoomList.get(i).getUser_Count() + "@" 
										+ totalRoomList.get(i).getAdmin() + "@" 
										+ totalRoomList.get(i).getPriv() + "@"
										+ totalRoomList.get(i).userListInRoom.size() + "-");
							}

//							System.out.println(roomListMessage);

							if (roomListMessage.length() != 0) {
								for (int i = 0; i < standbyUserList.size(); i++) {
									System.out.println("로그인 했을 때 totoalRoomList의 수 " + totalRoomList.size());
									standbyUserList.get(i).pWriter
											.println(Protocol.MAKEROOM_OK + ">" + roomListMessage);
									standbyUserList.get(i).pWriter.flush();
								}
							}
							System.out.println(roomListMessage);
						}
//						System.out.println(user.toString());
					} else {
						pWriter.println(Protocol.LOGIN_NO + ">" + "로그인 중입니다.");
						pWriter.flush();
					}
				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					String thisId = standbyUserList.get(standbyUserList.indexOf(this)).user.getId();
					logoutId = thisId;
					standbyUserList.remove(this);
					System.out.println("[접속 인원수] " + standbyUserList.size());

					String userListLine = "";
					for (int i = 0; i < standbyUserList.size(); i++) {
						userListLine += ("(" + standbyUserList.get(i).user.getId() + ") ");
					}
					System.out.println("[접속 중인 사용자]" + userListLine);

					user.setId("");
					user.setPassword("");
				} else if (line[0].compareTo(Protocol.MAKEROOM) == 0) {// 방만들기
					String userContent[] = line[1].split("@");

					String qurey = "";
					Room roomTemp = new Room();
					// 방 생성 query -------------------------------------------------------
					if (userContent.length == 4) {
						qurey = "INSERT INTO room (room_title, room_password, usercount, admin, private_room)"
								+ "VALUE (?, ?, ?, ?, ?);";
						pstmt = conn.prepareStatement(qurey);
						pstmt.setString(1, userContent[0]);
						pstmt.setString(2, userContent[1]);
						pstmt.setString(3, userContent[2]);
						pstmt.setString(4, user.getId());
						pstmt.setString(5, userContent[3]);

						int result = pstmt.executeUpdate();
//						System.out.println(result);

						roomTemp.setTitle(userContent[0]);
						roomTemp.setRoom_Password(userContent[1]);
						roomTemp.setUser_Count(userContent[2]);
						roomTemp.setAdmin(user.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[3]));

						// 방을 확인하기 위하여 qurey문 교체
						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
								+ userContent[2] + "' AND admin = '" + user.getId() + "';";
					} else if (userContent.length == 3) {
						qurey = "INSERT INTO room (room_title, usercount, admin, private_room)" + "VALUE (?, ?, ?, ?);";
						pstmt = conn.prepareStatement(qurey);
						pstmt.setString(1, userContent[0]);
						pstmt.setString(2, userContent[1]);
						pstmt.setString(3, user.getId());
						pstmt.setString(4, userContent[2]);

						int result = pstmt.executeUpdate();
//						System.out.println(result);

						roomTemp.setTitle(userContent[0]);
						roomTemp.setUser_Count(userContent[1]);
						roomTemp.setAdmin(user.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[2]));

						// 방을 확인하기 위하여 qurey문 교체
						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
								+ userContent[1] + "' AND admin = '" + user.getId() + "';";
					}

					// 만들어진 방 확인 -------------------------------------------------------
					pstmt = conn.prepareStatement(qurey);
					ResultSet rs = pstmt.executeQuery(qurey);
					int priNumber = 0;// 방 고유번호 설정
					int count = 0;
					while (rs.next()) {// 방 정보가 있다면?
						count++;
						priNumber = rs.getInt("room_num");
					}
					if (count != 0) {
						roomTemp.setRoom_Num(priNumber);// 방 고유번호 설
						roomTemp.userListInRoom.add(this);// 방에 있는 사용자 리스트에 추가
						totalRoomList.add(roomTemp);// 방 리스트에 만들어진 방 추가
						thisRoom = roomTemp;// 현재 방으로 지정
					}

					if (totalRoomList.size() != 0) {
						System.out.println("[방 정보]");
						for (Room room : totalRoomList) {
							System.out.println(room.toString() + ", 현재방에 인원수: " + room.userListInRoom.size());
						}
					}
					System.out.println("[전체 방 갯수] " + totalRoomList.size());

					// 만들어져 있는 방 전체 보기 -------------------------------------------------
					String roomListLine = "";
					for (int i = 0; i < roomListLine.length(); i++) {
						roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
								+ totalRoomList.get(i).getTitle() + "@" 
								+ totalRoomList.get(i).getRoom_Password() + "@"
								+ totalRoomList.get(i).getUser_Count() + "@" 
								+ totalRoomList.get(i).getAdmin() + "@"
								+ totalRoomList.get(i).getPriv() + "@" 
								+ totalRoomList.get(i).userListInRoom.size()
								+ "-");
					}
//					System.out.println(roomListLine);

					for (int i = 0; i < standbyUserList.size(); i++) {
						System.out.println(standbyUserList.get(i).user.getId().compareTo(roomTemp.getAdmin()));
						if (standbyUserList.get(i).user.getId().compareTo(roomTemp.getAdmin()) == 0) {
							// 사용자가 방만들면 바로 채팅방들어가도록
							System.out.println("방 만들 때 totoalRoomList의 수 " + totalRoomList.size());
							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_ADMIN_OK + ">" + roomTemp.getAdmin());
							standbyUserList.get(i).pWriter.flush();
						} else {
							// 다른사용자들이 만들어진 방을 볼 수 있도록
							System.out.println("방 만들 때 totoalRoomList의 수 " + totalRoomList.size());
							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
							standbyUserList.get(i).pWriter.flush();
						}
					}
					standbyUserList.remove(this);
					System.out.println(roomListLine);
					System.out.println("[새로운 방 생성 후 대기자] " + standbyUserList.size());

					String userLine = "";
					for (int i = 0; i < standbyUserList.size(); i++) {
						userLine += (standbyUserList.get(i).user.getId() + ":");
					}
//					for (int i = 0; i < standbyUserList.size(); i++) {
//						// 프로그램을 다시한번 새로고침해주는 역할
//						standbyUserList.get(i).pWriter.println(Protocol.LOGIN_OK + ">" + roomTemp.getAdmin() + ">님이 "
//								+ roomTemp.getRoom_Num() + "번방을 생성하셨습니다.>");
//						standbyUserList.get(i).pWriter.flush();
//					}
					System.out.println(roomTemp.getAdmin() + "님이 " + roomTemp.getRoom_Num() + "번방을 생성하셨습니다.");

//					// 폴더를 만들어 방을 관리하기 위함
//					String folderPath = ".\\0409 Chatpro\\roomForder\\" + priNumber;
//					File folder = new File(folderPath);
//					if (folder.isDirectory()) {
//						try {
//							System.out.println("[폴더 존재]");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (!folder.isDirectory()) {
//						folder.mkdir();
//						System.out.println("[폴더 생성]");
//					}
				} else if (line[0].compareTo(Protocol.ENTER) == 0) {// 방입장
					String thisUser = standbyUserList.get(standbyUserList.indexOf(this)).user.getId();
					//AddRoom에서 온 내용
					int room_num = Integer.valueOf(line[1]);//방 고유번호
					int index = 0;
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == room_num) {
							totalRoomList.get(i).userListInRoom.add(this); // 지금 방에 유저추가
							thisRoom = totalRoomList.get(i); //들어온 방을 현재 방으로 설정
							index = i;//현재 번호 기억
						}
					}
					
					String roomListLine = "";
					for (int i = 0; i < roomListLine.length(); i++) {
						roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
								+ totalRoomList.get(i).getTitle() + "@" 
								+ totalRoomList.get(i).getRoom_Password() + "@"
								+ totalRoomList.get(i).getUser_Count() + "@" 
								+ totalRoomList.get(i).getAdmin() + "@"
								+ totalRoomList.get(i).getPriv() + "@" 
								+ totalRoomList.get(i).getUserListInRoom().size()
								+ "-");
					}
					System.out.println(roomListLine); 
					System.out.println(thisUser);
					System.out.println(room_num);
					
					String userInRoom = ""; //모든 방을 검색하여 지금의 사용자가 들어있는지 확하기 위해
					for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
						userInRoom += (totalRoomList.get(index).userListInRoom.get(i).user.getId() + "@");
					}
					for (int i = 0; i < standbyUserList.size(); i++) {
						if (standbyUserList.get(i).user.getId().compareTo(thisUser) == 0) {
							//방에 입장하는 사용자용
							standbyUserList.get(i).pWriter.println(Protocol.ENTER_User + ">" + "message");
							standbyUserList.get(i).pWriter.flush();
						} else { 
							//대화방 인원수 변경을 위한 대화방 채널 새로고침
							System.out.println("방에 입장할 때 totoalRoomList의 수 " + totalRoomList.size());
							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
							standbyUserList.get(i).pWriter.flush();
						}
					}
					standbyUserList.remove(this);

					//유저 입장 부분 넣기
					
					System.out.println("[대기중인 사용자수] " + standbyUserList.size());
				} else if (line[0].compareTo(Protocol.EXITROOM) == 0) {
					int index = 0;
					boolean isUserIn = true;//사용자가 남아있는지 확인
					standbyUserList.add(this);
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == thisRoom.getRoom_Num()) {
							if (totalRoomList.get(i).userListInRoom.size() == 1) {
								String queryD = "DELETE FROM room WHERE room_num = '" + thisRoom.getRoom_Num() + "';";
								//마지막 사람이 퇴장할 때
								System.out.println("[마지막 사람 대화방 퇴장]");
								totalRoomList.remove(thisRoom);
								thisRoom = new Room();//현재방을 나타내는 장치 초기화
								isUserIn = false;
								pstmt = conn.prepareStatement(queryD);
								pstmt.executeUpdate();
							} else {
								System.out.println("[대화방 퇴장]");
								totalRoomList.get(i).userListInRoom.remove(this);
								thisRoom = new Room();
								index = i;//현재방 기억
							}
						}
					}
					
					if (isUserIn) {
						String userInRoom = ""; //모든 방을 검색하여 지금의 사용자가 들어있는지 확하기 위해
						for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
							userInRoom += (totalRoomList.get(index).userListInRoom.get(i).user.getId() + "@");
						}
						
						System.out.println("[방 사용자 수] " + totalRoomList.get(index).userListInRoom.size());
						System.out.println(userInRoom);
					}
					
					String roomListLine = "";
					if (totalRoomList.size() > 0) {
						roomListLine = "";
						for (int i = 0; i < roomListLine.length(); i++) {
							roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" 
									+ totalRoomList.get(i).getTitle() + "@" 
									+ totalRoomList.get(i).getRoom_Password() + "@"
									+ totalRoomList.get(i).getUser_Count() + "@" 
									+ totalRoomList.get(i).getAdmin() + "@"
									+ totalRoomList.get(i).getPriv() + "@" 
									+ totalRoomList.get(i).userListInRoom.size()
									+ "-");
						}
					} else {
						roomListLine = "-";
					}
					System.out.println("퇴장 후 totoalRoomList의 수 " + totalRoomList.size());
//					for (int i = 0; i < standbyUserList.size(); i++) {
//						standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
//						standbyUserList.get(i).pWriter.flush();
//					}
					System.out.println("[대기중인 사용자수] " + standbyUserList.size());
				}
			}
			bReader.close();
			pWriter.close();
			socket.close();
		} catch (SocketException se) {
			System.out.println("[사용자  접속종료] " + logoutId);
			logoutId = "";
		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
