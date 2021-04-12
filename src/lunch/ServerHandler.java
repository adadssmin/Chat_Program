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

import data.Room;
import data.UserData;

public class ServerHandler extends Thread {
	private UserData user;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Room UserRoom; // 사용자가 있는 방
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
		this.UserRoom = new Room();
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

					System.out.println("아이디: " + userContent[0] + ", 비번: " + userContent[1]);

					for (int i = 0; i < standbyUserList.size(); i++) {
						if ((standbyUserList.get(i).user.getId()).compareTo(userContent[0]) == 0) {
							isLogin = false;
						}
					}
					System.out.println(isLogin);

					if (isLogin) {
						String query = "SELECT * FROM user WHERE id = '" + userContent[0] + "' AND password = '"
								+ userContent[1] + "';";
						int count = 0;
						try {
							pstmt = conn.prepareStatement(query);
							rs = pstmt.executeQuery();
							while (rs.next()) {
								user.setId(rs.getString("id"));
								user.setPassword(rs.getString("password"));
								count++;
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						System.out.println(count);

						if (count == 0) {// 로그인 실패
							pWriter.println(Protocol.LOGIN_NO + ">" + "[로그인 실패]");
							pWriter.flush();

							user.setId("");
							user.setPassword("");
						} else {// 로그인 성공
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
							System.out.println("[대기 인원] :" + standbyUserList.size());

							System.out.println("[방 정보]");
							for (Room room : totalRoomList) {
								System.out.println(room.toString() + "현재방 인원수: " + room.roomInUserList.size());
							}

							System.out.println("[전체 방 갯수]" + totalRoomList.size());
							String roomListMessage = "";

							for (int i = 0; i < totalRoomList.size(); i++) {
								roomListMessage += (totalRoomList.get(i).getRoom_Num() + "@"
										+ totalRoomList.get(i).getTitle() + "@"
										+ totalRoomList.get(i).getRoom_Password() + "@"
										+ totalRoomList.get(i).getUser_Count() + "@" + totalRoomList.get(i).getAdmin()
										+ "@" + totalRoomList.get(i).getPriv() + "@"
										+ totalRoomList.get(i).roomInUserList.size() + "-");
							}

							System.out.println(roomListMessage);

							if (roomListMessage.length() != 0) {
								for (int i = 0; i < standbyUserList.size(); i++) {
									standbyUserList.get(i).pWriter
											.println(Protocol.MAKEROOM_OK + ">" + roomListMessage);
									standbyUserList.get(i).pWriter.flush();
								}
							}
						}
						System.out.println(user.toString());
					} else {
						pWriter.println(Protocol.LOGIN_NO + ">" + "로그인 중입니다.");
						pWriter.flush();
					}
				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					String thisId = standbyUserList.get(standbyUserList.indexOf(this)).user.getId();
					logoutId = thisId;
					standbyUserList.remove(this);
					System.out.println("[접속 인원수]: " + standbyUserList.size());

					String userLine = "";
					for (int i = 0; i < standbyUserList.size(); i++) {
						userLine += (standbyUserList.get(i).user.getId() + "$");
					}
					System.out.println("[접속 중인 사용자]: " + userLine);

					user.setId("");
					user.setPassword("");
				} else if (line[0].compareTo(Protocol.MAKEROOM) == 0) {
					String userContent[] = line[1].split("@");

					String qurey = null;
					Room roomTemp = new Room();
					if (userContent.length == 4) {
						qurey = "INSERT INTO room (room_title, room_password, usercount, master_id, private_room)"
								+ "VALUE (?, ?, ?, ?, ?);";
						pstmt = conn.prepareStatement(qurey);
						pstmt.setString(1, userContent[0]);
						pstmt.setString(2, userContent[1]);
						pstmt.setString(3, userContent[2]);
						pstmt.setString(4, user.getId());
						pstmt.setString(5, userContent[3]);

						roomTemp.setTitle(userContent[0]);
						roomTemp.setRoom_Password(userContent[1]);
						roomTemp.setUser_Count(userContent[2]);
						roomTemp.setAdmin(user.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[3]));

						//방을 확인하기 위하여 qurey문 교체
						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
								+ userContent[2] + "' AND admin = '" + user.getId() + "';";
					} else {
						qurey = "INSERT INTO room (room_title, usercount, master_id, private_room)"
								+ "VALUE (?, ?, ?, ?);";
						pstmt = conn.prepareStatement(qurey);
						pstmt.setString(1, userContent[0]);
						pstmt.setString(2, userContent[1]);
						pstmt.setString(4, user.getId());
						pstmt.setString(3, userContent[2]);

						roomTemp.setTitle(userContent[0]);
						roomTemp.setUser_Count(userContent[1]);
						roomTemp.setAdmin(user.getId());
						roomTemp.setPriv(Integer.valueOf(userContent[2]));

						//방을 확인하기 위하여 qurey문 교체
						qurey = "SELECT * FROM room WHERE room_title = '" + userContent[0] + "' AND usercount = '"
								+ userContent[2] + "' AND admin = '" + user.getId() + "';";
					}
					
					//만들어진 방 확인 
					pstmt = conn.prepareStatement(qurey);
					ResultSet rs = pstmt.executeQuery();
					int priNumber = 0;//방 고유번호 설정
					int count = 0;
					while (rs.next()) {//방 정보가 있다면?
						count++;
						priNumber = rs.getInt("room_Num");
					}
					if (count != 0) {
						roomTemp.setRoom_Num(priNumber);//방 고유번호 설
						roomTemp.roomInUserList.add(this);
						totalRoomList.add(roomTemp);
						UserRoom = roomTemp;
					}
					
				}
			}
			bReader.close();
			pWriter.close();
			socket.close();
		} catch (SocketException se) {
			System.out.println("[사용자 (" + logoutId + ") 접속종료]");
			logoutId = "";
		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}