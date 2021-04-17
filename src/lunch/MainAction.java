package lunch;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.mysql.cj.Constants;

import GUI.ProgramGUI;
import Login.Join;
import Login.Login;
import dao.UserDao;
import data.Room;
import room.AddRoom;
import room.MakeRoom;

public class MainAction extends JFrame implements ActionListener, Runnable {
	private Socket socket;
	private BufferedReader bReader;
	private PrintWriter pWriter;

	Login login;
	Join join;
	ProgramGUI programGUI;
	MakeRoom makeRoom;
	Server server;

	public MainAction() {
		connection();// 서버 연결

		// 일단 각 프레임들을 모두 실행 시키고 버튼이 반응할 때 보이기
		login = new Login();
		login.textField_Id.requestFocus();
		join = new Join();
		programGUI = new ProgramGUI(bReader, pWriter);
		makeRoom = new MakeRoom();

		InetAddress local;
		try {
			local = InetAddress.getLocalHost();
			login.txtField_Ip.setText(local.getHostAddress());// 클라이언트의 ip주소 가져와서 입력
			login.txtField_Port.setText(String.valueOf(server.getPort()));// 서버의 포트 가져와서 입력
		} catch (UnknownHostException e) {
			System.out.println("IP주소와 포트를 확인 할 수 없습니다.");
		}

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
		join.btn_Join.addActionListener(this);
		login.btn_Login.addActionListener(this);// 로그인 버튼
		login.btn_Join.addActionListener(this);// 회원가입 버튼
		login.btn_FindId.addActionListener(this);// 아이디 찾기 버튼
		login.btn_find.addActionListener(this);
		login.comboBox_email.addActionListener(this);
		login.btn_FindPw.addActionListener(this);// 비번 찾기 버튼
		login.comboBoxPw.addActionListener(this);
		login.btn_emailChkPw.addActionListener(this);
		programGUI.btn_addChat.addActionListener(this);
		programGUI.btn_Chanel.addActionListener(this);
		makeRoom.btn_done.addActionListener(this);
		makeRoom.chckbxNewCheckBox.addActionListener(this);
		makeRoom.btn_Cancle.addActionListener(this);

		join.txtField_ID.addFocusListener(new FocusAdapter() {// 아이디 중복 체크
			@Override
			public void focusLost(FocusEvent e) {
				if (join.txtField_ID.getText().isEmpty()) {
					join.lblNewLabel_6.setText("필수사항입니다");
					join.lblNewLabel_6.setForeground(Color.red);
				} else {
					String inputId = join.txtField_ID.getText();
					pWriter.println(Protocol.IDSEARCHCHECK + ">" + inputId);// 중복확인 프로토콜과 아이디를 보냄
					pWriter.flush();
				}
			}
		});

		join.pwField_PW.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (join.getPW().isEmpty()) {
					join.lbl_CheckPw.setText("필수사항입니다");
					join.lbl_CheckPw.setForeground(Color.red);
				} else {
					String pwAndPwCheck = join.getPW() + "@" + join.getPWCheck();// 이렇게하면 더 좋을거같음!
					pWriter.println(Protocol.PWCHECK + ">" + pwAndPwCheck);// 비밀번호확인요청 보냄
					pWriter.flush();
				}
			}
		});

		join.pwField_CheckPw.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (join.getPWCheck().isEmpty()) {
					join.lbl_CheckPw.setText("필수사항입니다");
					join.lbl_CheckPw.setForeground(Color.red);
				} else {
					String pwAndPwCheck = join.getPW() + "@" + join.getPWCheck();// 이렇게하면 더 좋을거같음!
					pWriter.println(Protocol.PWCHECK + ">" + pwAndPwCheck);// 비밀번호확인요청 보냄
					pWriter.flush();
				}
			}
		});

		join.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.setVisible(true);
				join.txtField_ID.setText("");
				join.lblNewLabel_6.setText("");
				join.pwField_PW.setText("");
				join.pwField_CheckPw.setText("");
				join.lbl_CheckPw.setText("");
				join.txtField_Email.setText("");
			}
		});

		login.findID.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				join.txtField_ID.setText("");
				join.lblNewLabel_6.setText("");
				join.pwField_PW.setText("");
				join.pwField_CheckPw.setText("");
				join.lbl_CheckPw.setText("");
				join.txtField_Email.setText("");
			}
		});

		login.comboBox_email.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (login.comboBox_email.getSelectedItem().toString().equals("직접입력")) {
					login.txtField_Email.setText("");
				} else {
					login.txtField_Email.setText(login.comboBox_email.getSelectedItem().toString());
				}
			}
		});
		
		login.comboBoxPw.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (login.comboBoxPw.getSelectedItem().toString().equals("직접입력")) {
					login.txtField_pWEmail.setText("");
				} else {
					login.txtField_pWEmail.setText(login.comboBoxPw.getSelectedItem().toString());
				}
			}
		});

		programGUI.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (programGUI.pnl_ChatRoom.isVisible()) {
					int result = JOptionPane.showConfirmDialog(null, "퇴장하시겠습니까?", "퇴장", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						pWriter.println(Protocol.EXITROOM + ">");
						pWriter.flush();
//						programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
						programGUI.pnl_Parent.removeAll();
						programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
						programGUI.pnl_Parent.repaint();
						programGUI.pnl_Parent.revalidate();
					}
				}
				programGUI.pnl_ChatRoom.setVisible(false);
				programGUI.setVisible(false);
				login.setVisible(true);
				pWriter.println(Protocol.LOGOUT + ">" + "message");
				pWriter.flush();
			}
		});

		makeRoom.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				programGUI.setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {// 클라이언트들이 행동을 하여 명령을 서버로 보내는 곳
		if (e.getSource() == login.btn_Join) {// 회원가입 버튼 click
			login.setVisible(false);// 로그인 프레임 안보이기
			join.setVisible(true);// 회원가입 프레임 보이기

		} else if (e.getSource() == join.btn_CheckPw) {// 비밀번호 같은지 확인
			String pwAndPwCheck = join.getPW() + "@" + join.getPWCheck();// 이렇게하면 더 좋을거같음!
			pWriter.println(Protocol.PWCHECK + ">" + pwAndPwCheck);// 비밀번호확인요청 보냄
			pWriter.flush();

		} else if (e.getSource() == join.btn_Join) {// 가입버튼 click
			if (!join.lblNewLabel_6.getText().equals("사용가능한 아이디입니다.")) {// 아이디 중복체크여부
				JOptionPane.showMessageDialog(null, "아이디를 확인해 주세요");
			} else if (!join.lbl_CheckPw.getText().equals("비밀번호가 일치합니다.")) {// 비번확인 여부
				JOptionPane.showMessageDialog(null, "비밀번호 확인을 해주세요");
			} else {
				String joinInfo = join.txtField_ID.getText() + "@" + join.getPW() + "@" + join.txtField_Email.getText();
				pWriter.println(Protocol.REGISTER + ">" + joinInfo);
				pWriter.flush();
			}

		} else if (e.getSource() == login.btn_Login) {// 로그인 버튼 click
			String id = login.textField_Id.getText();
			String password = getPassword(login.pwField_Pw);
			String ip = login.txtField_Ip.getText();
			String port = login.txtField_Port.getText();

			if (id.length() == 0) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요");
			} else if (password.length() == 0) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요");
			} else {
				String line = id + "@" + password + "@" + ip;
				pWriter.println(Protocol.LOGIN + ">" + line);
				pWriter.flush();
			}
			login.textField_Id.setText("");
			login.pwField_Pw.setText("");
			login.txtField_Ip.setText("");
			login.txtField_Port.setText("");

		} else if (e.getSource() == login.btn_FindId) { // 아이디 찾기 버튼 click
			login.findID.setVisible(true);
			login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");

		} else if (e.getSource() == login.btn_find) { // 아이디 찾기 버튼 click
			String getEmail = login.txtField_emailId.getText();
			String[] emailSplit = getEmail.split("");
			int count = emailSplit.length;
			for (String chr : emailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".") || chr.equals("?")) {
					login.lbl_hint.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
					login.lbl_hint.setForeground(Color.red);
					count--;
				} else {
					login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
					login.lbl_hint.setForeground(Color.GRAY);
				}
			}
			if (count == emailSplit.length) {
				if (login.txtField_emailId.getText().isEmpty()) {
					login.lbl_hint.setText("이메일을 입력해주세요");
					login.lbl_hint.setForeground(Color.red);
					login.txtField_emailId.requestFocus();
				} else if (login.comboBox_email.getSelectedItem().equals("이메일 선택")) {
					login.lbl_hint.setText("이메일을 선택해주세요");
					login.lbl_hint.setForeground(Color.red);

				} else if (login.comboBox_email.getSelectedItem().equals("직접입력")) {
					if (!login.txtField_Email.getText().isEmpty()) {
						login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
						login.lbl_hint.setForeground(Color.GRAY);
						String findIdForEmail = login.txtField_emailId.getText() + "@" + login.txtField_Email.getText();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
					} else {
						login.lbl_hint.setText("이메일을 직접 입력해주세요");
						login.lbl_hint.setForeground(Color.red);
					}
				} else {
					if (login.txtField_Email.getText().isEmpty()) {
						login.lbl_hint.setText("이메일을 선택해주세요");
						login.lbl_hint.setForeground(Color.red);
					} else {
						login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
						login.lbl_hint.setForeground(Color.GRAY);
						String findIdForEmail = login.txtField_emailId.getText() + "@"
								+ login.comboBox_email.getSelectedItem();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
					}
				}
			}

		} else if (e.getSource() == login.btn_FindPw) { // 비번 찾기 버튼 click
			login.findPw.setVisible(true);
			login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
			
		} else if (e.getSource() == login.btn_emailChkPw) { // 아이디, 이메일 확인 버튼 click
			String getId = login.txtField_IdPw.getText();
			String getEmail = login.txtField_pWEmailId.getText();
			String[] emailSplit = getEmail.split("");
			int count = emailSplit.length;
			for (String chr : emailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".") || chr.equals("?")) {
					login.lbl_hint_idPwCon.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
					login.lbl_hint_idPwCon.setForeground(Color.red);
					count--;
				} else {
					login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.GRAY);
				}
			}
			if (count == emailSplit.length) {
				if (login.txtField_pWEmailId.getText().isEmpty()) {
					login.lbl_hint_idPwCon.setText("이메일을 입력해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.red);
					login.txtField_pWEmailId.requestFocus();
				} else if (login.comboBoxPw.getSelectedItem().equals("이메일 선택")) {
					login.lbl_hint_idPwCon.setText("이메일을 선택해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else if (login.comboBoxPw.getSelectedItem().equals("직접입력")) {
					if (!login.txtField_pWEmailId.getText().isEmpty()) {
						login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
						login.lbl_hint_idPwCon.setForeground(Color.GRAY);
						String findIdForEmail = login.txtField_pWEmailId.getText() + "@" + login.txtField_pWEmailId.getText();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
					} else {
						login.lbl_hint_idPwCon.setText("이메일을 직접 입력해주세요");
						login.lbl_hint_idPwCon.setForeground(Color.red);
					}
				} else {
					if (login.txtField_pWEmailId.getText().isEmpty()) {
						login.lbl_hint_idPwCon.setText("이메일을 선택해주세요");
						login.lbl_hint_idPwCon.setForeground(Color.red);
					} else {
						login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
						login.lbl_hint_idPwCon.setForeground(Color.GRAY);
						String findIdForEmail = login.txtField_pWEmailId.getText() + "@"
								+ login.comboBoxPw.getSelectedItem();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
					}
				}
			}
			
		} else if (e.getSource() == programGUI.btn_addChat) { // 방만들기 버튼 click
			programGUI.setVisible(false);
			makeRoom.setVisible(true);

		} else if (e.getSource() == makeRoom.chckbxNewCheckBox) { // 비밀번호 체크 click
			if (makeRoom.chckbxNewCheckBox.isSelected()) {
				makeRoom.passwordField_RoomPw.setEditable(true);
			} else {
				makeRoom.passwordField_RoomPw.setEditable(false);
			}

		} else if (e.getSource() == makeRoom.btn_done) { // 만들기 버튼 click
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
				} else if (priv == 0) {// 공개방
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
			programGUI.setVisible(true);
		} else if (e.getSource() == makeRoom.btn_Cancle) {
			makeRoom.setVisible(false);
			makeRoom.txtField_RoomName.setText("");
			makeRoom.passwordField_RoomPw.setText("");
			makeRoom.cb_Persons.setSelectedIndex(0);
			makeRoom.chckbxNewCheckBox.setSelected(false);

		} else if (e.getSource() == programGUI.btn_Chanel) {
			if (programGUI.pnl_ChatRoom.isVisible()) {
				int result = JOptionPane.showConfirmDialog(this, "퇴장하시겠습니까?", "퇴장", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					pWriter.println(Protocol.EXITROOM + ">");
					pWriter.flush();
					programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
				}
			} else if (programGUI.pnl_Chanel.isVisible()) {
				programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
				programGUI.pnl_Parent.removeAll();
				programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
				programGUI.pnl_Parent.repaint();
				programGUI.pnl_Parent.revalidate();
			}
		}
	}

	@Override
	public void run() {// 클라이언트들이 서버로 부터 받은 응답을 받는곳
		String line[] = null;
		while (true) {
			try {
				line = bReader.readLine().split(">");
				if (line == null) {
					bReader.close();
					pWriter.close();
					socket.close();
					System.exit(0);

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) {// 중복이라면
					join.lblNewLabel_6.setText(line[1]);
					join.lblNewLabel_6.setForeground(Color.red);

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) {// 중복이 아니라면
					join.lblNewLabel_6.setText(line[1]);
					join.lblNewLabel_6.setForeground(Color.BLUE);

				} else if (line[0].compareTo(Protocol.PWCHECK_OK) == 0) {// 비번 일치한다면
					join.lbl_CheckPw.setText(line[1]);
					join.lbl_CheckPw.setForeground(Color.BLUE);

				} else if (line[0].compareTo(Protocol.PWCHECK_NO) == 0) {// 비번 불일치한다면
					join.lbl_CheckPw.setText(line[1]);
					join.lbl_CheckPw.setForeground(Color.red);

				} else if (line[0].compareTo(Protocol.REGISTER_OK) == 0) {// 회원가입 성공이라면
					JOptionPane.showMessageDialog(this, line[1]);
					join.setVisible(false);
					login.setVisible(true);

				} else if (line[0].compareTo(Protocol.REGISTER_NO) == 0) {// 회원가입 실패라면
					JOptionPane.showMessageDialog(this, line[1]);

				} else if (line[0].compareTo(Protocol.LOGIN_NO) == 0) {// 로그인 실패
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("[로그인 실패]");

				} else if (line[0].compareTo(Protocol.LOGIN_OK) == 0) {// 로그인 성공
					login.setVisible(false);
					programGUI.setVisible(true);
					String[] userInfo = line[1].split("@");
					String id = userInfo[0];
					String email = "";
					if (userInfo.length == 3) {
						email = userInfo[1] + "@" + userInfo[2];
					} else {
						email = userInfo[1];
					}
					programGUI.lbl_getId.setText(id);
					programGUI.lbl_getEmail.setText(email);

				} else if (line[0].compareTo(Protocol.SEARCHID_OK) == 0) {// 아이디찾기 성공
					if (line.length > 1) {
						login.findID.setVisible(false);
						login.searchID.setVisible(true);
						String[] idsStr = line[1].split("@");
						for (String ids : idsStr) {
							login.ta_SearchId.append(ids);
						}
					}
					
				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					programGUI.setVisible(false);
					login.setVisible(true);
					pWriter.println(Protocol.LOGOUT + ">" + "message");
					pWriter.flush();

				} else if (line[0].compareTo(Protocol.MAKEROOM_OK) == 0) {// 방 생성 성공
					// 방 별로 자른것
					String allRoom[] = line[1].split("-");// 존재하는 방을 모두 보여줌
					// 방 세부 정보별로 자른것
					String roomInfo[];
					programGUI.pnlClear();// 새로고침(1)
					for (int i = 0; i < allRoom.length; i++) {
						String userCount = "";
						programGUI.addRoom[i].init();// 생성되어있는 방 새로고침(2)
						roomInfo = allRoom[i].split("@");
						// 비밀방 - 0: 방번호, 1: 방제목, 2: 방비번, 3: 인원수, 4: 방장, 5: 비밀방/공개방, 6: 참가인원
						// 공개방 - 0: 방번호, 1: 방제목, 2: 인원수, 3: 방장, 4: 비밀방/공개방, 5: 참가인원

						if (roomInfo.length == 7) {
							userCount += (roomInfo[6] + "/" + roomInfo[3]);// 참가자 인원수
							programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// 방 번호
							programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// 방 제목
							programGUI.addRoom[i].labelArr[3].setFont(new Font("맑은 고딕", Font.BOLD, 18));
							programGUI.addRoom[i].labelArr[5].setText("\t" + userCount);// 방 인원
							programGUI.addRoom[i].labelArr[6].setText("방장: " + roomInfo[4]);// 방장
						} else if (roomInfo.length == 6) {
							userCount += (roomInfo[5] + "/" + roomInfo[2]);// 참가자 인원수
							programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// 방 번호
							programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// 방 제목
							programGUI.addRoom[i].labelArr[5].setText(userCount);// 방 인원
							programGUI.addRoom[i].labelArr[6].setText("방장: " + roomInfo[3]);// 방장
						}
						programGUI.addRoom[i].revalidate();
						programGUI.addRoom[i].repaint();
					}
					makeRoom.setVisible(false);

				} else if (line[0].compareTo(Protocol.MAKEROOM_ADMIN_OK) == 0) {// 방을 만든 방장
					makeRoom.setVisible(false);
					programGUI.pnl_ChatRoom.setVisible(true);
//					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();

				} else if (line[0].compareTo(Protocol.ENTER_User) == 0) {
					// 대화방으로 화면 전환
					programGUI.pnl_ChatRoom.setVisible(true);
					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();

				} else if (line[0].compareTo(Protocol.EXITROOM) == 0) {
					programGUI.pnl_ChatRoom.setVisible(false);

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
