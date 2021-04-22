package lunch;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
	
	private List<Room> totalRoomList;
	private Room thisRoom;

	Login login;
	Join join;
	ProgramGUI programGUI;
	MakeRoom makeRoom;
	Server server;
	
	public MainAction() {
		connection();// 서버 연결

		// 일단 각 프레임들을 모두 실행 시키고 버튼이 반응할 때 보이기
		login = new Login();
		join = new Join();
		programGUI = new ProgramGUI(bReader, pWriter);
		makeRoom = new MakeRoom();
		EventQueue.invokeLater(new Runnable() {
		   @Override
		     public void run() {
		         login.textField_Id.requestFocus();
		     }
		});
		
		InetAddress local;
		try {
			local = InetAddress.getLocalHost();
			login.txtField_Ip.setText(local.getHostAddress());// 클라이언트의 ip주소 가져와서 입력
			login.txtField_Port.setText(String.valueOf(server.getPort()));// 서버의 포트 가져와서 입력
		} catch (UnknownHostException e) {
			System.out.println("IP주소와 포트를 확인 할 수 없습니다.");
		}

		totalRoomList = new ArrayList<>();
		thisRoom = new Room();
		groupOfListener();// 리스너 실행
	}

	public void connection() {
		try {
			socket = new Socket("localhost", 9999);
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));
			pWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC-KR"));
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
		login.btn_newPwChk.addActionListener(this);
		login.btn_ChangPw.addActionListener(this);
		programGUI.btn_addChat.addActionListener(this);
		programGUI.btn_Friend.addActionListener(this);
		programGUI.btn_Profile.addActionListener(this);
		programGUI.btn_Chanel.addActionListener(this);
		programGUI.btn_Send.addActionListener(this);
		makeRoom.btn_done.addActionListener(this);
		makeRoom.chckbxNewCheckBox.addActionListener(this);
		makeRoom.btn_Cancle.addActionListener(this);
		
		programGUI.txtField_chat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					programGUI.btn_Send.doClick();
				}
			}
		});
		
		join.comboBox_email.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (join.comboBox_email.getSelectedItem().toString().equals("직접입력")) {
					join.txtField_Email.setText("");
				} else if (join.comboBox_email.getSelectedItem().equals("이메일 선택")) {
					join.lbl_Check_email.setText("이메일을 선택해주세요");
					join.lbl_Check_email.setForeground(Color.red);
					join.txtField_Email.setText("이메일 선택");
				} else {
					join.txtField_Email.setText(join.comboBox_email.getSelectedItem().toString());
				}
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

		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (e.getSource() == join.txtField_ID) {
					if (join.txtField_ID.getText().isEmpty()) {
						join.lblNewLabel_6.setText("필수사항입니다");
						join.lblNewLabel_6.setForeground(Color.red);
					} else {
						String inputId = join.txtField_ID.getText();
						String[] idSplit = inputId.split("");
						int count = idSplit.length;
						for (String chr : idSplit) {
							if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".")
									|| chr.equals("?")) {
								join.txtField_ID.setText("");
								join.lblNewLabel_6.setText("@, /, >, ., ? 사용하실 수 없습니다.");
								join.lblNewLabel_6.setForeground(Color.red);
								count--;
							}
						}
						if (count == idSplit.length) {
							pWriter.println(Protocol.IDSEARCHCHECK + ">" + inputId);// 중복확인 프로토콜과 아이디를 보냄
							pWriter.flush();
						}
					}
				} else if (e.getSource() == join.pwField_PW || e.getSource() == join.pwField_CheckPw) {
					if (join.getPW().isEmpty() || join.getPWCheck().isEmpty()) {
						join.lbl_CheckPw.setText("필수사항입니다");
						join.lbl_CheckPw.setForeground(Color.red);
					} else {
						String inputPW = join.getPW();
						String inputPWCheck = join.getPWCheck();
						String[] pwSplit = (inputPW + inputPWCheck).split("");
						int count = pwSplit.length;
						for (String chr : pwSplit) {
							if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".")
									|| chr.equals("?")) {
								join.pwField_PW.setText("");
								join.pwField_CheckPw.setText("");
								join.lbl_CheckPw.setText("@, /, >, ., ? 사용하실 수 없습니다.");
								join.lbl_CheckPw.setForeground(Color.red);
								count--;
							}
						}
						if (count == pwSplit.length) {
							String pwAndPwCheck = join.getPW() + "@" + join.getPWCheck();// 이렇게하면 더 좋을거같음!
							pWriter.println(Protocol.PWCHECK + ">" + pwAndPwCheck);// 비밀번호확인요청 보냄
							pWriter.flush();
						}
					}
				} else if (e.getSource() == join.txtField_EmailID) {
					String getEmail = join.txtField_EmailID.getText();
					String[] emailSplit = getEmail.split("");
					int count = emailSplit.length;
					for (String chr : emailSplit) {
						if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".")
								|| chr.equals("?")) {
							join.txtField_EmailID.setText("");
							join.lbl_Check_email.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
							join.lbl_Check_email.setForeground(Color.red);
							count--;
						}
					}
					if (count == emailSplit.length) {
						if (join.txtField_EmailID.getText().isEmpty()) {
							join.lbl_Check_email.setText("이메일을 입력해주세요");
							join.lbl_Check_email.setForeground(Color.red);

						} else {
							if (join.comboBox_email.getSelectedItem().equals("직접입력")) {
								if (join.txtField_Email.getText().isEmpty()) {
									join.lbl_Check_email.setText("이메일을 직접 입력해주세요");
									join.lbl_Check_email.setForeground(Color.red);
								} else {
									join.lbl_Check_email.setText("이메일 입력완료");
									join.lbl_Check_email.setForeground(Color.gray);
								}
							} else if (join.txtField_Email.getText().equals("이메일 선택")) {
								join.lbl_Check_email.setText("이메일을 선택해주세요");
								join.lbl_Check_email.setForeground(Color.red);
							} else {
								join.lbl_Check_email.setText("이메일 입력완료");
								join.lbl_Check_email.setForeground(Color.gray);
							}
						}
					}
				} else if (e.getSource() == join.comboBox_email) {
					if (join.txtField_Email.getText().isEmpty() || join.txtField_Email.getText().equals("이메일 선택")) {
						join.lbl_Check_email.setText("이메일을 선택해주세요");
						join.lbl_Check_email.setForeground(Color.red);
					} else {
						join.lbl_Check_email.setText("이메일 입력완료");
						join.lbl_Check_email.setForeground(Color.gray);
					}
				}
			}
		};

		join.txtField_ID.addFocusListener(focusListener);
		join.pwField_PW.addFocusListener(focusListener);
		join.pwField_CheckPw.addFocusListener(focusListener);
		join.txtField_EmailID.addFocusListener(focusListener);
		join.comboBox_email.addFocusListener(focusListener);

		WindowListener winListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (e.getSource() == join) {
					login.setVisible(true);
					join.txtField_ID.setText("");
					join.pwField_PW.setText("");
					join.pwField_CheckPw.setText("");
					join.txtField_EmailID.setText("");
					join.txtField_Email.setText("");
					join.tfphoneNum.setText("");
					join.lblNewLabel_6.setText("");
					join.lbl_CheckPw.setText("");
					join.lbl_Check_email.setText("");
					join.comboBox_email.setSelectedItem("이메일 선택");

				} else if (e.getSource() == login.findID) {
					login.txtField_Email.setText("");
					login.txtField_emailId.setText("");
					login.comboBox_email.setSelectedItem("이메일 선택");
					login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
					login.lbl_hint.setForeground(Color.GRAY);

				} else if (e.getSource() == login.searchID) {
					login.comboBox_email.setSelectedItem("이메일 선택");
					login.ta_SearchId.removeAll();
					login.ta_SearchId.revalidate();
					login.ta_SearchId.repaint();

				} else if (e.getSource() == login.findPw) {
					login.txtField_IdPw.setText("");
					login.txtField_pWEmail.setText("");
					login.txtField_pWEmailId.setText("");
					login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.GRAY);

				} else if (e.getSource() == programGUI) {
					if (programGUI.pnl_ChatRoom.isVisible()) {
						programGUI.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
						int result = JOptionPane.showConfirmDialog(null, "퇴장하시겠습니까?", "퇴장",
								JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							pWriter.println(Protocol.EXITROOM + ">");
							pWriter.flush();
							
							programGUI.pnl_ChatRoom.setVisible(false);
							programGUI.pnl_Parent.removeAll();
							programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
							programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
							programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
							programGUI.pnl_Parent.repaint();
							programGUI.pnl_Parent.revalidate();
						}
					} else {
						pWriter.println(Protocol.LOGOUT + ">");
						pWriter.flush();
						programGUI.setVisible(false);
						login.setVisible(true);
					}

				} else if (e.getSource() == makeRoom) {
					programGUI.setVisible(true);
				}
			}
		};

		join.addWindowListener(winListener);
		login.findID.addWindowListener(winListener);
		login.searchID.addWindowListener(winListener);
		login.findPw.addWindowListener(winListener);
		programGUI.addWindowListener(winListener);
		makeRoom.addWindowListener(winListener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {// 클라이언트들이 행동을 하여 명령을 서버로 보내는 곳
		if (e.getSource() == login.btn_Join) {// 회원가입 버튼 click
			login.setVisible(false);// 로그인 프레임 안보이기
			join.setVisible(true);// 회원가입 프레임 보이기
			join.lblNewLabel_6.setText("");
			join.lbl_CheckPw.setText("");
			join.lbl_Check_email.setText("");

		} else if (e.getSource() == join.btn_Join) {// 가입버튼 click
			if (!join.lblNewLabel_6.getText().equals("사용가능한 아이디입니다.")) {// 아이디 중복체크여부
				JOptionPane.showMessageDialog(null, "아이디를 확인해 주세요");
			} else if (!join.lbl_CheckPw.getText().equals("비밀번호가 일치합니다.")) {// 비번확인 여부
				JOptionPane.showMessageDialog(null, "비밀번호를 확인 해주세요");
			} else if (!join.lbl_Check_email.getText().equals("이메일 입력완료")) {// 이메일 입력 여부
				JOptionPane.showMessageDialog(null, "이메일을 확인 해주세요");
			} else {
				String joinInfo = join.txtField_ID.getText() + "@" + join.getPW() + "@"
						+ join.txtField_EmailID.getText() + "@" + join.txtField_Email.getText();
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

		} else if (e.getSource() == login.btn_FindId) { // 아이디 찾기 버튼 click
			login.findID.setVisible(true);

		} else if (e.getSource() == login.btn_find) { // 아이디 찾기 버튼 click
			String getEmail = login.txtField_emailId.getText();
			String[] emailSplit = getEmail.split("");
			int count = emailSplit.length;
			for (String chr : emailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".") || chr.equals("?")) {
					login.txtField_emailId.setText("");
					login.lbl_hint.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
					login.lbl_hint.setForeground(Color.red);
					count--;
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

				} else {
					if (login.comboBox_email.getSelectedItem().equals("직접입력")) {
						if (login.txtField_Email.getText().isEmpty()) {
							login.lbl_hint.setText("이메일을 직접 입력해주세요");
							login.lbl_hint.setForeground(Color.red);
						} else {
							String findIdForEmail = login.txtField_emailId.getText() + "@"
									+ login.txtField_Email.getText();
							pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
							pWriter.flush();
							login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
							login.lbl_hint.setForeground(Color.GRAY);
						}
					} else {
						String findIdForEmail = login.txtField_emailId.getText() + "@"
								+ login.comboBox_email.getSelectedItem();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
						login.lbl_hint.setText("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
						login.lbl_hint.setForeground(Color.GRAY);
					}
				}
			}
		} else if (e.getSource() == login.btn_FindPw) { // 비번 찾기 버튼 click
			login.findPw.setVisible(true);

		} else if (e.getSource() == login.btn_emailChkPw) { // 아이디, 이메일 확인 버튼 click
			String getId = login.txtField_IdPw.getText();
			String getEmail = login.txtField_pWEmailId.getText();
			String[] idEmailSplit = (getId + getEmail).split("");
			int count = idEmailSplit.length;
			for (String chr : idEmailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(".") || chr.equals(">") || chr.equals("?")) {
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");
					login.lbl_hint_idPwCon.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
					login.lbl_hint_idPwCon.setForeground(Color.red);
					count--;
				}
			}
			if (count == idEmailSplit.length) {
				if (login.txtField_IdPw.getText().isEmpty()) {
					login.txtField_IdPw.requestFocus();
					login.lbl_hint_idPwCon.setText("아이디를 입력해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else if (login.txtField_pWEmailId.getText().isEmpty()) {
					login.txtField_pWEmailId.requestFocus();
					login.lbl_hint_idPwCon.setText("이메일을 입력해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else if (login.comboBoxPw.getSelectedItem().equals("이메일 선택")) {
					login.lbl_hint_idPwCon.setText("이메일을 선택해주세요");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else {
					if (login.comboBoxPw.getSelectedItem().equals("직접입력")) {
						if (login.txtField_pWEmailId.getText().isEmpty()) {
							login.lbl_hint_idPwCon.setText("이메일을 직접 입력해주세요");
							login.lbl_hint_idPwCon.setForeground(Color.red);
						} else {
							String findPwForIdEmail = login.txtField_IdPw.getText() + ">"
									+ login.txtField_pWEmailId.getText() + "@" + login.txtField_pWEmail.getText();
							pWriter.println(Protocol.SEARCHPASSWORD + ">" + findPwForIdEmail);
							pWriter.flush();
							login.lbl_hint_idPwCon.setText("새로운 비밀번호를 설정해주세요.");
							login.lbl_hint_idPwCon.setForeground(Color.GRAY);
						}
					} else {
						String findPwForIdEmail = login.txtField_IdPw.getText() + ">"
								+ login.txtField_pWEmailId.getText() + "@" + login.txtField_pWEmail.getText();
						pWriter.println(Protocol.SEARCHPASSWORD + ">" + findPwForIdEmail);
						pWriter.flush();
						login.lbl_hint_idPwCon.setText("새로운 비밀번호를 설정해주세요.");
						login.lbl_hint_idPwCon.setForeground(Color.GRAY);
					}
				}
			}

		} else if (e.getSource() == login.btn_newPwChk) { // 비번 찾기 비번 확인 버튼 click
			String getPW = login.getNewPW() + login.getNewPWCheck();
			String[] pwSplit = getPW.split("");
			int count = pwSplit.length;
			for (String chr : pwSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(".") || chr.equals(">") || chr.equals("?")) {
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
					login.lbl_hint_PwCon.setText("@, /, >, ., ? 를 사용하실 수 없습니다.");
					login.lbl_hint_PwCon.setForeground(Color.red);
					count--;
				}
			}
			if (count == pwSplit.length) {
				if (!login.getNewPW().equals(login.getNewPWCheck())) {
					login.lbl_hint_PwCon.setText("새로운 비밀번호가 일치하지 않습니다.");
					login.lbl_hint_PwCon.setForeground(Color.red);
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
				} else {
					login.lbl_hint_PwCon.setText("새로운 비밀번호가 일치합니다.");
					login.lbl_hint_PwCon.setForeground(Color.GRAY);
				}
			}
		} else if (e.getSource() == login.btn_ChangPw) { // 비번 변경 버튼 click
			if (!login.lbl_hint_idPwCon.getText().equals("새로운 비밀번호 설정")) {
				JOptionPane.showMessageDialog(this, "아이디, 이메일을 확인해주세요");
			} else if (!login.lbl_hint_PwCon.getText().equals("새로운 비밀번호가 일치합니다.")) {
				JOptionPane.showMessageDialog(this, "새로운 비밀번호 확인을 해주세요");
			} else {
				String idPassword = login.txtField_IdPw.getText() + ">" + login.getNewPW();
				pWriter.println(Protocol.CHANGEPASSWORD + ">" + idPassword);
				pWriter.flush();
			}

		} else if (e.getSource() == programGUI.btn_addChat) { // 방만들기 버튼 click
			programGUI.setVisible(false);
			makeRoom.setLocation(programGUI.getX(), programGUI.getY());
			makeRoom.setVisible(true);
			EventQueue.invokeLater(new Runnable() {
			   @Override
			     public void run() {
			         makeRoom.txtField_RoomName.requestFocus();
			     }
			});

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
			makeRoom.txtField_RoomName.setText("");
			makeRoom.passwordField_RoomPw.setText("");
			makeRoom.cb_Persons.setSelectedIndex(0);
			makeRoom.chckbxNewCheckBox.setSelected(false);
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
				if (result == JOptionPane.OK_OPTION) {
					pWriter.println(Protocol.EXITROOM + ">");
					pWriter.flush();
					
					programGUI.pnl_ChatRoom.setVisible(false);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
				}
				
			} else if (programGUI.pnl_Chanel.isVisible()) {
				programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
				programGUI.pnl_Parent.removeAll();
				programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
				programGUI.pnl_Parent.repaint();
				programGUI.pnl_Parent.revalidate();
				programGUI.revalidate();
				programGUI.repaint();
			}
		} else if (e.getSource() == programGUI.btn_Profile) {
			if (programGUI.pnl_ChatRoom.isVisible()) {
				int result = JOptionPane.showConfirmDialog(this, "퇴장하시겠습니까?", "퇴장", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					pWriter.println(Protocol.EXITROOM + ">");
					pWriter.flush();
					
					programGUI.pnl_ChatRoom.setVisible(false);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Profile);
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
				}
			} else {
				programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
				programGUI.pnl_Parent.removeAll();
				programGUI.pnl_Parent.add(programGUI.pnl_Profile);
				programGUI.pnl_Parent.repaint();
				programGUI.pnl_Parent.revalidate();
				programGUI.revalidate();
				programGUI.repaint();
			}
		} else if (e.getSource() == programGUI.btn_Friend) {
			if (programGUI.pnl_ChatRoom.isVisible()) {
				int result = JOptionPane.showConfirmDialog(this, "퇴장하시겠습니까?", "퇴장", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					pWriter.println(Protocol.EXITROOM + ">");
					pWriter.flush();
					
					programGUI.pnl_ChatRoom.setVisible(false);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Friend);
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
				}
			} else {
				programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
				programGUI.pnl_Parent.removeAll();
				programGUI.pnl_Parent.add(programGUI.pnl_Friend);
				programGUI.pnl_Parent.repaint();
				programGUI.pnl_Parent.revalidate();
				programGUI.revalidate();
				programGUI.repaint();
			}
		} else if (e.getSource() == programGUI.btn_Send) {
			if (!programGUI.txtField_chat.getText().equals("")) {
				pWriter.println(Protocol.CHATMESSAGE + ">" + programGUI.txtField_chat.getText());
				pWriter.flush();
				programGUI.txtField_chat.setText("");
				programGUI.txtField_chat.requestFocus();
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
					login.findID.setVisible(false);
					String[] idsStr = line[1].split("@");
					for (String ids : idsStr) {
						JPanel pnl = new JPanel();
						pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
						pnl.setBackground(SystemColor.inactiveCaptionBorder);
						JLabel lbl = new JLabel(ids);
						lbl.setFont(new Font("굴림", Font.BOLD, 20));
						JButton btn = new JButton("로그인");
						btn.setPreferredSize(new Dimension(100, 40));
						btn.setBackground(Color.LIGHT_GRAY);
						btn.setForeground(Color.DARK_GRAY);
						btn.setFocusPainted(false);
						btn.setBorderPainted(false);
						btn.setHorizontalAlignment(SwingConstants.CENTER);
						btn.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								login.textField_Id.setText(lbl.getText());
								login.searchID.setVisible(false);
							}
						});

						pnl.add(Box.createHorizontalGlue());
						pnl.add(lbl);
						pnl.add(Box.createHorizontalGlue());
						pnl.add(btn);

						login.ta_SearchId.add(pnl);
						login.ta_SearchId.add(Box.createVerticalStrut(10));
						login.ta_SearchId.revalidate();
						login.ta_SearchId.repaint();
					}
					login.searchID.setVisible(true);
					login.txtField_emailId.setText("");
					login.txtField_Email.setText("");

				} else if (line[0].compareTo(Protocol.SEARCHID_NO) == 0) {// 아이디찾기 실패
					JOptionPane.showMessageDialog(this, line[1]);
					login.txtField_emailId.setText("");
					login.txtField_Email.setText("");

				} else if (line[0].compareTo(Protocol.SEARCHPASSWORD_OK) == 0) {// 비번찾기 성공
					login.lbl_hint_idPwCon.setText(line[1]);

				} else if (line[0].compareTo(Protocol.SEARCHPASSWORD_NO) == 0) {// 비번찾기 실패
					login.lbl_hint_idPwCon.setText(line[1]);
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");

				} else if (line[0].compareTo(Protocol.CHANGEPASSWORD_OK) == 0) {// 비번변경 성공
					JOptionPane.showMessageDialog(this, line[1]);
					login.findPw.setVisible(false);
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");
					login.comboBoxPw.setSelectedItem("이메일 선택");
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
					login.lbl_hint_idPwCon.setText("아이디와 이메일을 확인해주세요");
					login.lbl_hint_PwCon.setText("");

				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// 로그아웃
					programGUI.setVisible(false);
					login.setVisible(true);
					pWriter.println(Protocol.LOGOUT + ">" + "message");
					pWriter.flush();

				} else if (line[0].compareTo(Protocol.MAKEROOM_OK) == 0) {// 방 생성 성공
					refrashRoom(line[1]);
					makeRoom.setVisible(false);
					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
					
				} else if (line[0].compareTo(Protocol.MAKEROOM_ADMIN_OK) == 0) {// 방을 만든 방장
					refrashRoom(line[1]);
					makeRoom.setVisible(false);
					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
					EventQueue.invokeLater(new Runnable() {
						   @Override
						     public void run() {
						         programGUI.txtField_chat.requestFocus();
						     }
						});
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 1148, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 1002, 719);
					programGUI.pnl_ChatRoom.setVisible(true);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();

				} else if (line[0].compareTo(Protocol.ENTER_OK) == 0) {
					EventQueue.invokeLater(new Runnable() {
						   @Override
						     public void run() {
						         programGUI.txtField_chat.requestFocus();
						     }
						});
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 1148, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 1002, 719);
					programGUI.pnl_ChatRoom.setVisible(true);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_ChatRoom);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();

				} else if (line[0].compareTo(Protocol.ENTER_NO) == 0) {
					JOptionPane.showMessageDialog(this, line[1]);
					
				} else if (line[0].compareTo(Protocol.EXITROOM_OK) == 0) {
					programGUI.pnl_ChatRoom.setVisible(false);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
					
				} else if (line[0].compareTo(Protocol.EXITROOM_OK_ALL) == 0) {
					programGUI.pnl_ChatRoom.setVisible(false);
					programGUI.setBounds(programGUI.getX(), programGUI.getY(), 910, 748);
					programGUI.pnl_Parent.setBounds(140, 0, 754, 719);
					programGUI.pnl_Parent.removeAll();
					programGUI.pnl_Parent.add(programGUI.pnl_Chanel);
					programGUI.pnl_Parent.repaint();
					programGUI.pnl_Parent.revalidate();
					
				} else if (line[0].compareTo(Protocol.CHATROOMSUBINFO) == 0) {
					refrashUser(line[2]);
					programGUI.lbl_Persons.setText("   " + line[1]);
					
				} else if (line[0].compareTo(Protocol.CHATMESSAGE_OK) == 0) {
					SimpleDateFormat fm = null;
					Date today = new Date();
					fm = new SimpleDateFormat("a h:mm");
					programGUI.textArea.append("[" + line[1] + "]  " + line[2]);
					programGUI.textArea.append("\t\t" + fm.format(today) + "\n\n");
					programGUI.scroll.getVerticalScrollBar().setValue(programGUI.textArea.getHeight());
					programGUI.revalidate();
					
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
	
	public void refrashUser(String Userline) {
		String allUser[] = Userline.split("@");// 존재하는 유저를 모두 보여줌
		String userId = "";
		programGUI.pnlUserClear();// 새로고침(1)
		
		for (int i = 0; i < allUser.length; i++) {
			userId = allUser[i];
			programGUI.addUser[i].init();
			programGUI.addUser[i].setText(userId);//유저 아이디
			programGUI.addUser[i].setFont(new Font("맑은 고딕", Font.BOLD, 18));
			programGUI.addUser[i].revalidate();
			programGUI.addUser[i].repaint();
		}
	}
	
	public void refrashRoom(String Roomline) {
		String allRoom[] = Roomline.split("-");// 존재하는 방을 모두 보여줌
		String roomInfo[];// 방 세부 정보별로 자른것
		programGUI.pnlRoomClear();// 새로고침(1)
		for (int i = 0; i < allRoom.length; i++) {
			String userCount = "";
			programGUI.addRoom[i].init();// 생성되어있는 방 새로고침(2)
			roomInfo = allRoom[i].split("@");
			// 비밀방 - 0: 방번호, 1: 방제목, 2: 방비번, 3: 인원수, 4: 방장, 5: 비밀방/공개방, 6: 참가인원
			// 공개방 - 0: 방번호, 1: 방제목, 2: 인원수, 3: 방장, 4: 비밀방/공개방, 5: 참가인원
			if (roomInfo.length == 7) {
				int room_Num = Integer.valueOf(roomInfo[0]);
				String title = roomInfo[1];
				String room_Password = roomInfo[2];
				String user_Count = roomInfo[3];
				String admin = roomInfo[4];
				int private_room = Integer.valueOf(roomInfo[5]);
				int userCountInRoom = Integer.valueOf(roomInfo[6]);
				
				thisRoom = new Room(room_Num, title, room_Password, user_Count, admin, private_room, userCountInRoom);
				totalRoomList.add(new Room(room_Num, title, room_Password, user_Count, admin, private_room, userCountInRoom));
				userCount += (roomInfo[6] + " / " + roomInfo[3]);// 참가자 인원수
				programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// 방 번호
				programGUI.addRoom[i].labelArr[2].setText("[비밀방]");// 방 번호
				programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// 방 제목
				programGUI.addRoom[i].labelArr[3].setFont(new Font("맑은 고딕", Font.BOLD, 18));
				programGUI.addRoom[i].labelArr[5].setText("\t" + userCount);// 방 인원
				if (roomInfo[6].equals(roomInfo[3])) {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.red);
				} else {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.black);
				}
				programGUI.addRoom[i].labelArr[6].setText("방장: " + roomInfo[4]);// 방장
				
				programGUI.lbl_ChatName.setText("   " + roomInfo[1]);
				programGUI.lbl_Persons.setText("   " + userCount);
				
			} else if (roomInfo.length == 6) {
				int room_Num = Integer.valueOf(roomInfo[0]);
				String title = roomInfo[1];
				String user_Count = roomInfo[2];
				String admin = roomInfo[3];
				int private_room = Integer.valueOf(roomInfo[4]);
				int userCountInRoom = Integer.valueOf(roomInfo[5]);
				
				thisRoom = new Room(room_Num, title, "", user_Count, admin, private_room, userCountInRoom);
				totalRoomList.add(new Room(room_Num, title, "", user_Count, admin, private_room, userCountInRoom));
				userCount += (roomInfo[5] + " / " + roomInfo[2]);// 참가자 인원수
				programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// 방 번호
				programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// 방 제목
				programGUI.addRoom[i].labelArr[3].setFont(new Font("맑은 고딕", Font.BOLD, 18));
				programGUI.addRoom[i].labelArr[5].setText(userCount);// 방 인원
				if (roomInfo[5].equals(roomInfo[2])) {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.red);
				} else {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.black);
				}
				programGUI.addRoom[i].labelArr[6].setText("방장: " + roomInfo[3]);// 방장
				
				programGUI.lbl_ChatName.setText("  " + roomInfo[1]);
				programGUI.lbl_Persons.setText("  " + userCount);
			}
			programGUI.lbl_ChatName.revalidate();
			programGUI.lbl_ChatName.repaint();
			programGUI.lbl_Persons.revalidate();
			programGUI.lbl_Persons.repaint();
			
			programGUI.addRoom[i].revalidate();
			programGUI.addRoom[i].repaint();
		}
	}
	
}
