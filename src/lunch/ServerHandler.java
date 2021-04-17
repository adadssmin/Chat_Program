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

import dao.UserDao;
import data.Room;
import data.UserData;
import dbutil.MyConnectionProvider;

public class ServerHandler extends Thread {
	private UserData userData;
	private UserDao userdao;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt, pstmt1, pstmt2, pstmt3;
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
					String email = joinInfo[2];
					userdao.addUser(id, pw, email);

					if (userdao.findExistId(id)) {// 어차피 아이디 중복 안되니까 아이디로만 찾으면 되겠네요!
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
							System.out.print("[접속 인원] " + standbyUserList.size() + " ,[접속 중인 사용자]" + userListLine);

							// db에 있는 방 보기
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
								totalRoomList.add(
										new Room(room_num, room_title, room_password, usercount, admin, private_room));
							}

							System.out.println("[전체 방 갯수] " + totalRoomList.size());
							String roomListMessage = "";

							for (int i = 0; i < totalRoomList.size(); i++) {
								roomListMessage += (totalRoomList.get(i).getRoom_Num() + "@"
										+ totalRoomList.get(i).getTitle() + "@"
										+ totalRoomList.get(i).getRoom_Password() + "@"
										+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin()
										+ "@" + totalRoomList.get(i).getPriv() + "@"
										+ totalRoomList.get(i).userListInRoom.size() + "-");
							}

//							System.out.println(roomListMessage);

							if (roomListMessage.length() != 0) {
								for (int i = 0; i < standbyUserList.size(); i++) {
									standbyUserList.get(i).pWriter
											.println(Protocol.MAKEROOM_OK + ">" + roomListMessage);
									standbyUserList.get(i).pWriter.flush();
								}
							}
							System.out.println(roomListMessage);
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
					for (String id : ids) {
						idsStr += (id + "@");
					}
					pWriter.println(Protocol.SEARCHID_OK + ">" + idsStr);
					pWriter.flush();
					
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
					// 방 생성 query -------------------------------------------------------
					if (userContent.length == 4) {
//						qurey = "INSERT INTO room (room_title, room_password, usercount, admin, private_room)"
//								+ "VALUE (?, ?, ?, ?, ?);";
//						synchronized (pstmt) {
//						pstmt = conn.prepareStatement(qurey);
//						pstmt.setString(1, userContent[0]);
//						pstmt.setString(2, userContent[1]);
//						pstmt.setString(3, userContent[2]);
//						pstmt.setString(4, userData.getId());
//						pstmt.setString(5, userContent[3]);
//
//						pstmt.executeUpdate();
//						}

						roomTemp.setTitle(userContent[0]);
						roomTemp.setRoom_Password(userContent[1]);
						roomTemp.setUser_Count(userContent[2]);
						roomTemp.setAdmin(userData.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[3]));

//						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
//								+ userContent[2] + "' AND admin = '" + userData.getId() + "';";

						// 방을 확인하기 위하여 qurey문 교체
					} else if (userContent.length == 3) {
//						qurey = "INSERT INTO room (room_title, usercount, admin, private_room)" + "VALUE (?, ?, ?, ?);";
//						synchronized (pstmt) {
//						pstmt = conn.prepareStatement(qurey);
//						pstmt.setString(1, userContent[0]);
//						pstmt.setString(2, userContent[1]);
//						pstmt.setString(3, userData.getId());
//						pstmt.setString(4, userContent[2]);
//						
//						pstmt.executeUpdate();
//						}

						roomTemp.setTitle(userContent[0]);
						roomTemp.setUser_Count(userContent[1]);
						roomTemp.setAdmin(userData.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[2]));

						// 방을 확인하기 위하여 qurey문 교체
//						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
//								+ userContent[1] + "' AND admin = '" + userData.getId() + "';";
					}
					totalRoomList.add(roomTemp);// 방 리스트에 만들어진 방 추가
					int priNumber = 0;// 방 고유번호 설정
					priNumber = totalRoomList.size();
					roomTemp.setRoom_Num(priNumber);// 방 고유번호 설
					roomTemp.userListInRoom.add(this);// 방에 있는 사용자 리스트에 추가
					thisRoom = roomTemp;// 현재 방으로 지정

					String roomListLine = "";
					for (int i = 0; i < roomListLine.length(); i++) {
						roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" + totalRoomList.get(i).getTitle()
								+ "@" + totalRoomList.get(i).getRoom_Password() + "@"
								+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin() + "@"
								+ totalRoomList.get(i).getPriv() + "@" + totalRoomList.get(i).userListInRoom.size()
								+ "-");
					}

					for (int i = 0; i < standbyUserList.size(); i++) {
						System.out.println(standbyUserList.get(i).userData.getId().compareTo(roomTemp.getAdmin()));
						if (standbyUserList.get(i).userData.getId().compareTo(roomTemp.getAdmin()) == 0) {
							// 사용자가 방만들면 바로 채팅방들어가도록
							standbyUserList.get(i).pWriter
									.println(Protocol.MAKEROOM_ADMIN_OK + ">" + roomTemp.getAdmin());
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

					String userLine = "";
					for (int i = 0; i < standbyUserList.size(); i++) {
						userLine += (standbyUserList.get(i).userData.getId() + ":");
					}

				} else if (line[0].compareTo(Protocol.MAKEROOM_SHOW) == 0) {
					// 만들어져 있는 방 전체 보기 -------------------------------------------------
					if (totalRoomList.size() != 0) {
						System.out.println("[방 정보]");
						for (Room room : totalRoomList) {
							System.out.println(room.toString() + ", 현재방에 인원수: " + room.userListInRoom.size());
						}
					}
					System.out.println("[전체 방 갯수] " + totalRoomList.size());
					System.out.println("[접속 인원] " + standbyUserList.size());

//					String roomListLine = "";
//					for (int i = 0; i < roomListLine.length(); i++) {
//						roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" + totalRoomList.get(i).getTitle()
//								+ "@" + totalRoomList.get(i).getRoom_Password() + "@"
//								+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin() + "@"
//								+ totalRoomList.get(i).getPriv() + "@" + totalRoomList.get(i).userListInRoom.size()
//								+ "-");
//					}
////					System.out.println(roomListLine);
//
//					for (int i = 0; i < standbyUserList.size(); i++) {
//						System.out.println(standbyUserList.get(i).userData.getId().compareTo(roomTemp.getAdmin()));
//						if (standbyUserList.get(i).userData.getId().compareTo(roomTemp.getAdmin()) == 0) {
//							// 사용자가 방만들면 바로 채팅방들어가도록
//							standbyUserList.get(i).pWriter
//									.println(Protocol.MAKEROOM_ADMIN_OK + ">" + roomTemp.getAdmin());
//							standbyUserList.get(i).pWriter.flush();
//						} else {
//							// 다른사용자들이 만들어진 방을 볼 수 있도록
//							System.out.println("방 만들 때 totoalRoomList의 수 " + totalRoomList.size());
//							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
//							standbyUserList.get(i).pWriter.flush();
//						}
//					}
//					
//					System.out.println(roomListLine);

				} else if (line[0].compareTo(Protocol.ENTER) == 0) {// 방입장
					String thisUser = standbyUserList.get(standbyUserList.indexOf(this)).userData.getId();
					// AddRoom에서 온 내용
					int room_num = Integer.valueOf(line[1]);// 방 고유번호
					int index = 0;
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == room_num) {
							totalRoomList.get(i).userListInRoom.add(this); // 지금 방에 유저추가
							thisRoom = totalRoomList.get(i); // 들어온 방을 현재 방으로 설정
							index = i;// 현재 번호 기억
						}
					}

					String roomListLine = "";
					for (int i = 0; i < roomListLine.length(); i++) {
						roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" + totalRoomList.get(i).getTitle()
								+ "@" + totalRoomList.get(i).getRoom_Password() + "@"
								+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin() + "@"
								+ totalRoomList.get(i).getPriv() + "@" + totalRoomList.get(i).getUserListInRoom().size()
								+ "-");
					}
					System.out.println(roomListLine);
					System.out.println(thisUser);
					System.out.println(room_num);

					String userInRoom = ""; // 모든 방을 검색하여 지금의 사용자가 들어있는지 확하기 위해
					for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
						userInRoom += (totalRoomList.get(index).userListInRoom.get(i).userData.getId() + "@");
					}
					for (int i = 0; i < standbyUserList.size(); i++) {
						if (standbyUserList.get(i).userData.getId().compareTo(thisUser) == 0) {
							// 방에 입장하는 사용자용
							standbyUserList.get(i).pWriter.println(Protocol.ENTER_User + ">" + "message");
							standbyUserList.get(i).pWriter.flush();
						} else {
							// 대화방 인원수 변경을 위한 대화방 채널 새로고침
							System.out.println("방에 입장할 때 totoalRoomList의 수 " + totalRoomList.size());
							standbyUserList.get(i).pWriter.println(Protocol.MAKEROOM_OK + ">" + roomListLine);
							standbyUserList.get(i).pWriter.flush();
						}
					}
					standbyUserList.remove(this);

					// 유저 입장 부분 넣기

					System.out.println("[대기중인 사용자수] " + standbyUserList.size());

				} else if (line[0].compareTo(Protocol.EXITROOM) == 0) {
					int index = 0;
					boolean isUserIn = true;// 사용자가 남아있는지 확인
					for (int i = 0; i < totalRoomList.size(); i++) {
						if (totalRoomList.get(i).getRoom_Num() == thisRoom.getRoom_Num()) {
							if (totalRoomList.get(i).userListInRoom.size() == 1) {
								// 마지막 사람이 퇴장할 때
								String queryD = "DELETE FROM room WHERE room_num = '" + thisRoom.getRoom_Num() + "';";
								System.out.println("[마지막 사람 대화방 퇴장]");
								totalRoomList.remove(thisRoom);
								thisRoom = new Room();// 현재방을 나타내는 장치 초기화
								isUserIn = false;
								pstmt = conn.prepareStatement(queryD);
								pstmt.executeUpdate();
							} else {
								System.out.println("[대화방 퇴장]");
								totalRoomList.get(i).userListInRoom.remove(this);
								thisRoom = new Room();
								index = i;// 현재방 기억
							}
						}
					}

					if (isUserIn) {
						String userInRoom = ""; // 모든 방을 검색하여 지금의 사용자가 들어있는지 확하기 위해
						for (int i = 0; i < totalRoomList.get(index).userListInRoom.size(); i++) {
							userInRoom += (totalRoomList.get(index).userListInRoom.get(i).userData.getId() + "@");
						}
						System.out.println(userInRoom);
						System.out.println("[방 사용자 수] " + totalRoomList.get(index).userListInRoom.size());
					}

					standbyUserList.add(this);
					String roomListLine = "";
					if (totalRoomList.size() > 0) {
						roomListLine = "";
						for (int i = 0; i < roomListLine.length(); i++) {
							roomListLine += (totalRoomList.get(i).getRoom_Num() + "@" + totalRoomList.get(i).getTitle()
									+ "@" + totalRoomList.get(i).getRoom_Password() + "@"
									+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin() + "@"
									+ totalRoomList.get(i).getPriv() + "@" + totalRoomList.get(i).userListInRoom.size()
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
			if (this.userData.getIp() != null) {
				System.out.println("[클라이언트 접속 종료] ip: " + this.userData.getIp());
			}
		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void setDB_notUsed() {
//		totalRoomList.clear();
//		String qurey1 = "SELECT * FROM room;";
//		String qurey2 = "TRUNCATE room;";
//		String qurey3 = "INSERT INTO room (room_num, room_title, room_password, usercount, admin, private_room)"
//				+ " VALUE (?, ?, ?, ?, ?, ?)";
//		conn = null;
//		pstmt = null;
//		rs = null;
//		try {
//			conn = MyConnectionProvider.getConnection();
//			conn.setAutoCommit(false);
//			pstmt1 = conn.prepareStatement(qurey1);
//			rs = pstmt1.executeQuery();
//			while (rs.next()) {
//				int room_num = rs.getInt("room_num");
//				String room_title = rs.getString("room_title");
//				String room_password = rs.getString("room_password");
//				String usercount = rs.getString("usercount");
//				String admin = rs.getString("admin");
//				int private_room = rs.getInt("private_room");
//
//				totalRoomList.add(new Room(room_num, room_title, room_password, usercount, admin, private_room));
//			}
//			System.out.println("" + totalRoomList);
//			
//			pstmt2 = conn.prepareStatement(qurey2);
//			int result = pstmt2.executeUpdate();
//			System.out.println("TRUNCATE 한 부분 " + result);
//
//			pstmt3 = conn.prepareStatement(qurey3);
//			for (Room room : totalRoomList) {
//				pstmt3.setInt(1, room.getRoom_Num());
//				pstmt3.setString(2, room.getTitle());
//				pstmt3.setString(3, room.getRoom_Password());
//				pstmt3.setString(4, room.getUser_Count());
//				pstmt3.setString(5, room.getAdmin());
//				pstmt3.setInt(6, room.getPriv());
//
//				int result2 = pstmt3.executeUpdate();
//			}
//			totalRoomList.clear();
//			conn.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				conn.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}
//
	}
}
