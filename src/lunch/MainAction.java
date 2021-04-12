package lunch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import GUI.ProgramGUI;
import Login.Join;
import Login.Login;
import data.Room;
import room.MakeRoom;

public class MainAction extends JFrame implements ActionListener, Runnable {

	private Socket socket;
	private BufferedReader bReader;
	private PrintWriter pWriter;

	Login login;
	Join join;
	ProgramGUI programGUI;
	MakeRoom makeRoom;

	public MainAction() {
		connection();// 서버 연결

		// 일단 각 프레임들을 모두 실행 시키고 버튼이 반응할 때 보이기
		login = new Login();
		join = new Join();
		programGUI = new ProgramGUI();
		makeRoom = new MakeRoom();

		groupOfListener();// 리스너 실행
	}

	public void connection() {
		try {
			socket = new Socket("localhost", 9999);
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("서버를 찾을 수 없습니다");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("서버 연결 실패");
			System.exit(0);
		}

		Thread t = new Thread(this);
		t.start();
	}

	public void groupOfListener() {
		login.btn_Login.addActionListener(this);// 로그인 버튼
		login.btn_Join.addActionListener(this);// 회원가입 버튼
		programGUI.btn_addChat.addActionListener(this);
		makeRoom.btn_done.addActionListener(this);
		makeRoom.chckbxNewCheckBox.addActionListener(this);
		makeRoom.btn_Cancle.addActionListener(this);

		join.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				join.setVisible(false);
				login.setVisible(true);
			}
		});

		programGUI.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				programGUI.setVisible(false);
				login.setVisible(true);
				pWriter.println(Protocol.LOGOUT + ">" + "message");
				pWriter.flush();
				try {
					bReader.close();
					pWriter.close();
					socket.close();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {// 클라이언트들이 행동을 하여 명령을 서버로 보내는 곳
		if (e.getSource() == login.btn_Join) {// 회원가입 버튼을 누르면
			login.setVisible(false);// 로그인 프레임 안보이기
			join.setVisible(true);// 회원가입 프레임 보이기
		} else if (e.getSource() == login.btn_Login) {// 로그인 버튼 누르면
			String id = login.textField_Id.getText();
			String password = getPassword(login.pwField_Pw);

			if (id.length() == 0) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요");
			} else if (password.length() == 0) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요");
			} else {
				String line = id + "@" + password;
				pWriter.println(Protocol.LOGIN + ">" + line);
				pWriter.flush();
				login.dispose();
			}
			login.textField_Id.setText("");
			login.pwField_Pw.setText("");
		} else if (e.getSource() == programGUI.btn_addChat) {
			makeRoom.setVisible(true);
		} else if (e.getSource() == makeRoom.chckbxNewCheckBox) {
			if (makeRoom.chckbxNewCheckBox.isSelected()) {
				makeRoom.passwordField_RoomPw.setEditable(true);
			} else {
				makeRoom.passwordField_RoomPw.setEditable(false);
			}
		} else if (e.getSource() == makeRoom.btn_done) {
			String room_title = makeRoom.txtField_RoomName.getText();
			String room_Password = getPassword(makeRoom.passwordField_RoomPw);
			String userCount = (String) makeRoom.cb_Persons.getSelectedItem();
			int priv = makeRoom.chckbxNewCheckBox.isSelected() ? 1 : 0;// 선택되면 1(비밀방)

			if (room_title.length() == 0) {
				JOptionPane.showMessageDialog(this, "제목을 입력해주세요");
			} else {
				if (priv == 1 && room_Password.length() == 0) {
					JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요");
				} else if (priv == 1 && room_Password.length() != 0) {// 비밀방 생성
					String line = "";
					line += (room_title + "@" + room_Password + "@" + userCount + "@" + priv);
					pWriter.println(Protocol.MAKEROOM + ">" + line);
					pWriter.flush();
					
					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
				} else if (priv == 0) {//공개방
					String line = "";
					line += (room_title + "@" + userCount + "@" + priv);
					pWriter.println(Protocol.MAKEROOM + ">" + line);
					pWriter.flush();
					
					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
				}
			}
			
			makeRoom.setVisible(false);
		} else if (e.getSource() == makeRoom.btn_Cancle) {
			makeRoom.setVisible(false);
			makeRoom.txtField_RoomName.setText("");
			makeRoom.passwordField_RoomPw.setText("");
			makeRoom.cb_Persons.setSelectedIndex(0);
			makeRoom.chckbxNewCheckBox.setSelected(false);
		}
	}

	@Override
	public void run() {// 클라이언트들이 서버로 부터 받은 응답을 보는곳
		String line[] = null;
		while (true) {
			try {
				line = bReader.readLine().split("\\>");
				if (line == null) {
					bReader.close();
					pWriter.close();
					socket.close();
					System.exit(0);
				} else if (line[0].compareTo(Protocol.LOGIN_NO) == 0) {// 로그인 실패
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("[로그인 실패]");
					bReader.close();
					pWriter.close();
					socket.close();
				} else if (line[0].compareTo(Protocol.LOGIN_OK) == 0) {// 로그인 성공
					login.setVisible(false);
					programGUI.setVisible(true);
				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					programGUI.setVisible(false);
					login.setVisible(true);
					pWriter.println(Protocol.LOGOUT + ">" + "message");
					pWriter.flush();
				} else if (line[0].compareTo(Protocol.MAKEROOM_OK) == 0) {
					//방 별로 자른것
					String roomListAll[] = line[1].split("-");//존재하는 방을 모두 보여줌
					for (int i = 0; i < roomListAll.length; i++) {
						System.out.println(roomListAll[i] + "/");
					}
					
					//방 세부 정보별로 자른것
					String roomListInfo[];
					programGUI.pnl_Chat.removeAll();//새로고침
					for (int i = 0; i < roomListAll.length; i++) {
						roomListInfo = roomListAll[i].split("@");
						
//						programGUI.addRoomPnl(roomListInfo[1]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getPassword(JPasswordField jpf) {
		String str = "";
		for (char c : jpf.getPassword()) {
			str += c;
		}
		return str;
	}
}
