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
		connection();// ?????? ??????

		// ?????? ??? ??????????????? ?????? ?????? ????????? ????????? ????????? ??? ?????????
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
			login.txtField_Ip.setText(local.getHostAddress());// ?????????????????? ip?????? ???????????? ??????
			login.txtField_Port.setText(String.valueOf(server.getPort()));// ????????? ?????? ???????????? ??????
		} catch (UnknownHostException e) {
			System.out.println("IP????????? ????????? ?????? ??? ??? ????????????.");
		}

		totalRoomList = new ArrayList<>();
		thisRoom = new Room();
		groupOfListener();// ????????? ??????
	}

	public void connection() {
		try {
			socket = new Socket("localhost", 9999);
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));
			pWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC-KR"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("????????? ?????? ??? ????????????");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("?????? ?????? ??????");
			System.exit(0);
		}

		Thread t = new Thread(this);
		t.start();
	}

	public void groupOfListener() {
		join.btn_Join.addActionListener(this);
		login.btn_Login.addActionListener(this);// ????????? ??????
		login.btn_Join.addActionListener(this);// ???????????? ??????
		login.btn_FindId.addActionListener(this);// ????????? ?????? ??????
		login.btn_find.addActionListener(this);
		login.comboBox_email.addActionListener(this);
		login.btn_FindPw.addActionListener(this);// ?????? ?????? ??????
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
				if (join.comboBox_email.getSelectedItem().toString().equals("????????????")) {
					join.txtField_Email.setText("");
				} else if (join.comboBox_email.getSelectedItem().equals("????????? ??????")) {
					join.lbl_Check_email.setText("???????????? ??????????????????");
					join.lbl_Check_email.setForeground(Color.red);
					join.txtField_Email.setText("????????? ??????");
				} else {
					join.txtField_Email.setText(join.comboBox_email.getSelectedItem().toString());
				}
			}
		});

		login.comboBox_email.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (login.comboBox_email.getSelectedItem().toString().equals("????????????")) {
					login.txtField_Email.setText("");
				} else {
					login.txtField_Email.setText(login.comboBox_email.getSelectedItem().toString());
				}
			}
		});

		login.comboBoxPw.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (login.comboBoxPw.getSelectedItem().toString().equals("????????????")) {
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
						join.lblNewLabel_6.setText("?????????????????????");
						join.lblNewLabel_6.setForeground(Color.red);
					} else {
						String inputId = join.txtField_ID.getText();
						String[] idSplit = inputId.split("");
						int count = idSplit.length;
						for (String chr : idSplit) {
							if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".")
									|| chr.equals("?")) {
								join.txtField_ID.setText("");
								join.lblNewLabel_6.setText("@, /, >, ., ? ???????????? ??? ????????????.");
								join.lblNewLabel_6.setForeground(Color.red);
								count--;
							}
						}
						if (count == idSplit.length) {
							pWriter.println(Protocol.IDSEARCHCHECK + ">" + inputId);// ???????????? ??????????????? ???????????? ??????
							pWriter.flush();
						}
					}
				} else if (e.getSource() == join.pwField_PW || e.getSource() == join.pwField_CheckPw) {
					if (join.getPW().isEmpty() || join.getPWCheck().isEmpty()) {
						join.lbl_CheckPw.setText("?????????????????????");
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
								join.lbl_CheckPw.setText("@, /, >, ., ? ???????????? ??? ????????????.");
								join.lbl_CheckPw.setForeground(Color.red);
								count--;
							}
						}
						if (count == pwSplit.length) {
							String pwAndPwCheck = join.getPW() + "@" + join.getPWCheck();// ??????????????? ??? ???????????????!
							pWriter.println(Protocol.PWCHECK + ">" + pwAndPwCheck);// ???????????????????????? ??????
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
							join.lbl_Check_email.setText("@, /, >, ., ? ??? ???????????? ??? ????????????.");
							join.lbl_Check_email.setForeground(Color.red);
							count--;
						}
					}
					if (count == emailSplit.length) {
						if (join.txtField_EmailID.getText().isEmpty()) {
							join.lbl_Check_email.setText("???????????? ??????????????????");
							join.lbl_Check_email.setForeground(Color.red);

						} else {
							if (join.comboBox_email.getSelectedItem().equals("????????????")) {
								if (join.txtField_Email.getText().isEmpty()) {
									join.lbl_Check_email.setText("???????????? ?????? ??????????????????");
									join.lbl_Check_email.setForeground(Color.red);
								} else {
									join.lbl_Check_email.setText("????????? ????????????");
									join.lbl_Check_email.setForeground(Color.gray);
								}
							} else if (join.txtField_Email.getText().equals("????????? ??????")) {
								join.lbl_Check_email.setText("???????????? ??????????????????");
								join.lbl_Check_email.setForeground(Color.red);
							} else {
								join.lbl_Check_email.setText("????????? ????????????");
								join.lbl_Check_email.setForeground(Color.gray);
							}
						}
					}
				} else if (e.getSource() == join.comboBox_email) {
					if (join.txtField_Email.getText().isEmpty() || join.txtField_Email.getText().equals("????????? ??????")) {
						join.lbl_Check_email.setText("???????????? ??????????????????");
						join.lbl_Check_email.setForeground(Color.red);
					} else {
						join.lbl_Check_email.setText("????????? ????????????");
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
					join.comboBox_email.setSelectedItem("????????? ??????");

				} else if (e.getSource() == login.findID) {
					login.txtField_Email.setText("");
					login.txtField_emailId.setText("");
					login.comboBox_email.setSelectedItem("????????? ??????");
					login.lbl_hint.setText("?????? ??? ????????? ???????????? ????????? ????????? ????????????.");
					login.lbl_hint.setForeground(Color.GRAY);

				} else if (e.getSource() == login.searchID) {
					login.comboBox_email.setSelectedItem("????????? ??????");
					login.ta_SearchId.removeAll();
					login.ta_SearchId.revalidate();
					login.ta_SearchId.repaint();

				} else if (e.getSource() == login.findPw) {
					login.txtField_IdPw.setText("");
					login.txtField_pWEmail.setText("");
					login.txtField_pWEmailId.setText("");
					login.lbl_hint_idPwCon.setText("???????????? ???????????? ??????????????????");
					login.lbl_hint_idPwCon.setForeground(Color.GRAY);

				} else if (e.getSource() == programGUI) {
					if (programGUI.pnl_ChatRoom.isVisible()) {
						programGUI.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
						int result = JOptionPane.showConfirmDialog(null, "?????????????????????????", "??????",
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
	public void actionPerformed(ActionEvent e) {// ????????????????????? ????????? ?????? ????????? ????????? ????????? ???
		if (e.getSource() == login.btn_Join) {// ???????????? ?????? click
			login.setVisible(false);// ????????? ????????? ????????????
			join.setVisible(true);// ???????????? ????????? ?????????
			join.lblNewLabel_6.setText("");
			join.lbl_CheckPw.setText("");
			join.lbl_Check_email.setText("");

		} else if (e.getSource() == join.btn_Join) {// ???????????? click
			if (!join.lblNewLabel_6.getText().equals("??????????????? ??????????????????.")) {// ????????? ??????????????????
				JOptionPane.showMessageDialog(null, "???????????? ????????? ?????????");
			} else if (!join.lbl_CheckPw.getText().equals("??????????????? ???????????????.")) {// ???????????? ??????
				JOptionPane.showMessageDialog(null, "??????????????? ?????? ????????????");
			} else if (!join.lbl_Check_email.getText().equals("????????? ????????????")) {// ????????? ?????? ??????
				JOptionPane.showMessageDialog(null, "???????????? ?????? ????????????");
			} else {
				String joinInfo = join.txtField_ID.getText() + "@" + join.getPW() + "@"
						+ join.txtField_EmailID.getText() + "@" + join.txtField_Email.getText();
				pWriter.println(Protocol.REGISTER + ">" + joinInfo);
				pWriter.flush();
			}

		} else if (e.getSource() == login.btn_Login) {// ????????? ?????? click
			String id = login.textField_Id.getText();
			String password = getPassword(login.pwField_Pw);
			String ip = login.txtField_Ip.getText();
			String port = login.txtField_Port.getText();

			if (id.length() == 0) {
				JOptionPane.showMessageDialog(null, "???????????? ??????????????????");
			} else if (password.length() == 0) {
				JOptionPane.showMessageDialog(null, "??????????????? ??????????????????");
			} else {
				String line = id + "@" + password + "@" + ip;
				pWriter.println(Protocol.LOGIN + ">" + line);
				pWriter.flush();
			}
			login.textField_Id.setText("");
			login.pwField_Pw.setText("");

		} else if (e.getSource() == login.btn_FindId) { // ????????? ?????? ?????? click
			login.findID.setVisible(true);

		} else if (e.getSource() == login.btn_find) { // ????????? ?????? ?????? click
			String getEmail = login.txtField_emailId.getText();
			String[] emailSplit = getEmail.split("");
			int count = emailSplit.length;
			for (String chr : emailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(">") || chr.equals(".") || chr.equals("?")) {
					login.txtField_emailId.setText("");
					login.lbl_hint.setText("@, /, >, ., ? ??? ???????????? ??? ????????????.");
					login.lbl_hint.setForeground(Color.red);
					count--;
				}
			}
			if (count == emailSplit.length) {
				if (login.txtField_emailId.getText().isEmpty()) {
					login.lbl_hint.setText("???????????? ??????????????????");
					login.lbl_hint.setForeground(Color.red);
					login.txtField_emailId.requestFocus();
				} else if (login.comboBox_email.getSelectedItem().equals("????????? ??????")) {
					login.lbl_hint.setText("???????????? ??????????????????");
					login.lbl_hint.setForeground(Color.red);

				} else {
					if (login.comboBox_email.getSelectedItem().equals("????????????")) {
						if (login.txtField_Email.getText().isEmpty()) {
							login.lbl_hint.setText("???????????? ?????? ??????????????????");
							login.lbl_hint.setForeground(Color.red);
						} else {
							String findIdForEmail = login.txtField_emailId.getText() + "@"
									+ login.txtField_Email.getText();
							pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
							pWriter.flush();
							login.lbl_hint.setText("?????? ??? ????????? ???????????? ????????? ????????? ????????????.");
							login.lbl_hint.setForeground(Color.GRAY);
						}
					} else {
						String findIdForEmail = login.txtField_emailId.getText() + "@"
								+ login.comboBox_email.getSelectedItem();
						pWriter.println(Protocol.SEARCHID + ">" + findIdForEmail);
						pWriter.flush();
						login.lbl_hint.setText("?????? ??? ????????? ???????????? ????????? ????????? ????????????.");
						login.lbl_hint.setForeground(Color.GRAY);
					}
				}
			}
		} else if (e.getSource() == login.btn_FindPw) { // ?????? ?????? ?????? click
			login.findPw.setVisible(true);

		} else if (e.getSource() == login.btn_emailChkPw) { // ?????????, ????????? ?????? ?????? click
			String getId = login.txtField_IdPw.getText();
			String getEmail = login.txtField_pWEmailId.getText();
			String[] idEmailSplit = (getId + getEmail).split("");
			int count = idEmailSplit.length;
			for (String chr : idEmailSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(".") || chr.equals(">") || chr.equals("?")) {
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");
					login.lbl_hint_idPwCon.setText("@, /, >, ., ? ??? ???????????? ??? ????????????.");
					login.lbl_hint_idPwCon.setForeground(Color.red);
					count--;
				}
			}
			if (count == idEmailSplit.length) {
				if (login.txtField_IdPw.getText().isEmpty()) {
					login.txtField_IdPw.requestFocus();
					login.lbl_hint_idPwCon.setText("???????????? ??????????????????");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else if (login.txtField_pWEmailId.getText().isEmpty()) {
					login.txtField_pWEmailId.requestFocus();
					login.lbl_hint_idPwCon.setText("???????????? ??????????????????");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else if (login.comboBoxPw.getSelectedItem().equals("????????? ??????")) {
					login.lbl_hint_idPwCon.setText("???????????? ??????????????????");
					login.lbl_hint_idPwCon.setForeground(Color.red);

				} else {
					if (login.comboBoxPw.getSelectedItem().equals("????????????")) {
						if (login.txtField_pWEmailId.getText().isEmpty()) {
							login.lbl_hint_idPwCon.setText("???????????? ?????? ??????????????????");
							login.lbl_hint_idPwCon.setForeground(Color.red);
						} else {
							String findPwForIdEmail = login.txtField_IdPw.getText() + ">"
									+ login.txtField_pWEmailId.getText() + "@" + login.txtField_pWEmail.getText();
							pWriter.println(Protocol.SEARCHPASSWORD + ">" + findPwForIdEmail);
							pWriter.flush();
							login.lbl_hint_idPwCon.setText("????????? ??????????????? ??????????????????.");
							login.lbl_hint_idPwCon.setForeground(Color.GRAY);
						}
					} else {
						String findPwForIdEmail = login.txtField_IdPw.getText() + ">"
								+ login.txtField_pWEmailId.getText() + "@" + login.txtField_pWEmail.getText();
						pWriter.println(Protocol.SEARCHPASSWORD + ">" + findPwForIdEmail);
						pWriter.flush();
						login.lbl_hint_idPwCon.setText("????????? ??????????????? ??????????????????.");
						login.lbl_hint_idPwCon.setForeground(Color.GRAY);
					}
				}
			}

		} else if (e.getSource() == login.btn_newPwChk) { // ?????? ?????? ?????? ?????? ?????? click
			String getPW = login.getNewPW() + login.getNewPWCheck();
			String[] pwSplit = getPW.split("");
			int count = pwSplit.length;
			for (String chr : pwSplit) {
				if (chr.equals("@") || chr.equals("/") || chr.equals(".") || chr.equals(">") || chr.equals("?")) {
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
					login.lbl_hint_PwCon.setText("@, /, >, ., ? ??? ???????????? ??? ????????????.");
					login.lbl_hint_PwCon.setForeground(Color.red);
					count--;
				}
			}
			if (count == pwSplit.length) {
				if (!login.getNewPW().equals(login.getNewPWCheck())) {
					login.lbl_hint_PwCon.setText("????????? ??????????????? ???????????? ????????????.");
					login.lbl_hint_PwCon.setForeground(Color.red);
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
				} else {
					login.lbl_hint_PwCon.setText("????????? ??????????????? ???????????????.");
					login.lbl_hint_PwCon.setForeground(Color.GRAY);
				}
			}
		} else if (e.getSource() == login.btn_ChangPw) { // ?????? ?????? ?????? click
			if (!login.lbl_hint_idPwCon.getText().equals("????????? ???????????? ??????")) {
				JOptionPane.showMessageDialog(this, "?????????, ???????????? ??????????????????");
			} else if (!login.lbl_hint_PwCon.getText().equals("????????? ??????????????? ???????????????.")) {
				JOptionPane.showMessageDialog(this, "????????? ???????????? ????????? ????????????");
			} else {
				String idPassword = login.txtField_IdPw.getText() + ">" + login.getNewPW();
				pWriter.println(Protocol.CHANGEPASSWORD + ">" + idPassword);
				pWriter.flush();
			}

		} else if (e.getSource() == programGUI.btn_addChat) { // ???????????? ?????? click
			programGUI.setVisible(false);
			makeRoom.setLocation(programGUI.getX(), programGUI.getY());
			makeRoom.setVisible(true);
			EventQueue.invokeLater(new Runnable() {
			   @Override
			     public void run() {
			         makeRoom.txtField_RoomName.requestFocus();
			     }
			});

		} else if (e.getSource() == makeRoom.chckbxNewCheckBox) { // ???????????? ?????? click
			if (makeRoom.chckbxNewCheckBox.isSelected()) {
				makeRoom.passwordField_RoomPw.setEditable(true);
			} else {
				makeRoom.passwordField_RoomPw.setEditable(false);
			}

		} else if (e.getSource() == makeRoom.btn_done) { // ????????? ?????? click
			String room_title = makeRoom.txtField_RoomName.getText();
			String room_Password = getPassword(makeRoom.passwordField_RoomPw);
			String userCount = (String) makeRoom.cb_Persons.getSelectedItem();
			int priv = makeRoom.chckbxNewCheckBox.isSelected() ? 1 : 0;// ???????????? 1(?????????)

			if (room_title.length() == 0) {
				JOptionPane.showMessageDialog(this, "????????? ??????????????????");
			} else {
				if (priv == 1 && room_Password.length() == 0) {
					JOptionPane.showMessageDialog(this, "??????????????? ??????????????????");
				} else if (priv == 1 && room_Password.length() != 0) {// ????????? ??????
					String line = "";
					line += (room_title + "@" + room_Password + "@" + userCount + "@" + priv);
					pWriter.println(Protocol.MAKEROOM + ">" + line);
					pWriter.flush();

					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
				} else if (priv == 0) {// ?????????
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
				int result = JOptionPane.showConfirmDialog(this, "?????????????????????????", "??????", JOptionPane.OK_CANCEL_OPTION);
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
				int result = JOptionPane.showConfirmDialog(this, "?????????????????????????", "??????", JOptionPane.OK_CANCEL_OPTION);
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
				int result = JOptionPane.showConfirmDialog(this, "?????????????????????????", "??????", JOptionPane.OK_CANCEL_OPTION);
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
	public void run() {// ????????????????????? ????????? ?????? ?????? ????????? ?????????
		String line[] = null;
		while (true) {
			try {
				line = bReader.readLine().split(">");
				if (line == null) {
					bReader.close();
					pWriter.close();
					socket.close();
					System.exit(0);

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) {// ???????????????
					join.lblNewLabel_6.setText(line[1]);
					join.lblNewLabel_6.setForeground(Color.red);

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) {// ????????? ????????????
					join.lblNewLabel_6.setText(line[1]);
					join.lblNewLabel_6.setForeground(Color.BLUE);

				} else if (line[0].compareTo(Protocol.PWCHECK_OK) == 0) {// ?????? ???????????????
					join.lbl_CheckPw.setText(line[1]);
					join.lbl_CheckPw.setForeground(Color.BLUE);

				} else if (line[0].compareTo(Protocol.PWCHECK_NO) == 0) {// ?????? ??????????????????
					join.lbl_CheckPw.setText(line[1]);
					join.lbl_CheckPw.setForeground(Color.red);

				} else if (line[0].compareTo(Protocol.REGISTER_OK) == 0) {// ???????????? ???????????????
					JOptionPane.showMessageDialog(this, line[1]);
					join.setVisible(false);
					login.setVisible(true);

				} else if (line[0].compareTo(Protocol.REGISTER_NO) == 0) {// ???????????? ????????????
					JOptionPane.showMessageDialog(this, line[1]);

				} else if (line[0].compareTo(Protocol.LOGIN_NO) == 0) {// ????????? ??????
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("[????????? ??????]");

				} else if (line[0].compareTo(Protocol.LOGIN_OK) == 0) {// ????????? ??????
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

				} else if (line[0].compareTo(Protocol.SEARCHID_OK) == 0) {// ??????????????? ??????
					login.findID.setVisible(false);
					String[] idsStr = line[1].split("@");
					for (String ids : idsStr) {
						JPanel pnl = new JPanel();
						pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
						pnl.setBackground(SystemColor.inactiveCaptionBorder);
						JLabel lbl = new JLabel(ids);
						lbl.setFont(new Font("??????", Font.BOLD, 20));
						JButton btn = new JButton("?????????");
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

				} else if (line[0].compareTo(Protocol.SEARCHID_NO) == 0) {// ??????????????? ??????
					JOptionPane.showMessageDialog(this, line[1]);
					login.txtField_emailId.setText("");
					login.txtField_Email.setText("");

				} else if (line[0].compareTo(Protocol.SEARCHPASSWORD_OK) == 0) {// ???????????? ??????
					login.lbl_hint_idPwCon.setText(line[1]);

				} else if (line[0].compareTo(Protocol.SEARCHPASSWORD_NO) == 0) {// ???????????? ??????
					login.lbl_hint_idPwCon.setText(line[1]);
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");

				} else if (line[0].compareTo(Protocol.CHANGEPASSWORD_OK) == 0) {// ???????????? ??????
					JOptionPane.showMessageDialog(this, line[1]);
					login.findPw.setVisible(false);
					login.txtField_IdPw.setText("");
					login.txtField_pWEmailId.setText("");
					login.txtField_pWEmail.setText("");
					login.comboBoxPw.setSelectedItem("????????? ??????");
					login.pwField_newPw.setText("");
					login.pwField_newPwChk.setText("");
					login.lbl_hint_idPwCon.setText("???????????? ???????????? ??????????????????");
					login.lbl_hint_PwCon.setText("");

				} else if (line[0].compareTo(Protocol.LOGOUT) == 0) {// ????????????
					programGUI.setVisible(false);
					login.setVisible(true);
					pWriter.println(Protocol.LOGOUT + ">" + "message");
					pWriter.flush();

				} else if (line[0].compareTo(Protocol.MAKEROOM_OK) == 0) {// ??? ?????? ??????
					refrashRoom(line[1]);
					makeRoom.setVisible(false);
					makeRoom.txtField_RoomName.setText("");
					makeRoom.passwordField_RoomPw.setText("");
					makeRoom.cb_Persons.setSelectedIndex(0);
					makeRoom.chckbxNewCheckBox.setSelected(false);
					
				} else if (line[0].compareTo(Protocol.MAKEROOM_ADMIN_OK) == 0) {// ?????? ?????? ??????
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
		String allUser[] = Userline.split("@");// ???????????? ????????? ?????? ?????????
		String userId = "";
		programGUI.pnlUserClear();// ????????????(1)
		
		for (int i = 0; i < allUser.length; i++) {
			userId = allUser[i];
			programGUI.addUser[i].init();
			programGUI.addUser[i].setText(userId);//?????? ?????????
			programGUI.addUser[i].setFont(new Font("?????? ??????", Font.BOLD, 18));
			programGUI.addUser[i].revalidate();
			programGUI.addUser[i].repaint();
		}
	}
	
	public void refrashRoom(String Roomline) {
		String allRoom[] = Roomline.split("-");// ???????????? ?????? ?????? ?????????
		String roomInfo[];// ??? ?????? ???????????? ?????????
		programGUI.pnlRoomClear();// ????????????(1)
		for (int i = 0; i < allRoom.length; i++) {
			String userCount = "";
			programGUI.addRoom[i].init();// ?????????????????? ??? ????????????(2)
			roomInfo = allRoom[i].split("@");
			// ????????? - 0: ?????????, 1: ?????????, 2: ?????????, 3: ?????????, 4: ??????, 5: ?????????/?????????, 6: ????????????
			// ????????? - 0: ?????????, 1: ?????????, 2: ?????????, 3: ??????, 4: ?????????/?????????, 5: ????????????
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
				userCount += (roomInfo[6] + " / " + roomInfo[3]);// ????????? ?????????
				programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// ??? ??????
				programGUI.addRoom[i].labelArr[2].setText("[?????????]");// ??? ??????
				programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// ??? ??????
				programGUI.addRoom[i].labelArr[3].setFont(new Font("?????? ??????", Font.BOLD, 18));
				programGUI.addRoom[i].labelArr[5].setText("\t" + userCount);// ??? ??????
				if (roomInfo[6].equals(roomInfo[3])) {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.red);
				} else {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.black);
				}
				programGUI.addRoom[i].labelArr[6].setText("??????: " + roomInfo[4]);// ??????
				
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
				userCount += (roomInfo[5] + " / " + roomInfo[2]);// ????????? ?????????
				programGUI.addRoom[i].labelArr[0].setText(roomInfo[0]);// ??? ??????
				programGUI.addRoom[i].labelArr[3].setText(roomInfo[1]);// ??? ??????
				programGUI.addRoom[i].labelArr[3].setFont(new Font("?????? ??????", Font.BOLD, 18));
				programGUI.addRoom[i].labelArr[5].setText(userCount);// ??? ??????
				if (roomInfo[5].equals(roomInfo[2])) {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.red);
				} else {
					programGUI.addRoom[i].labelArr[5].setForeground(Color.black);
				}
				programGUI.addRoom[i].labelArr[6].setText("??????: " + roomInfo[3]);// ??????
				
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
