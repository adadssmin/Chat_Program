package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import lunch.Protocol;
import room.AddRoom;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class ProgramGUI extends JFrame implements ActionListener {
	public JPanel pnl_Parent, pnl_Friend, pnl_ChatList, pnl_Menu, pnl_back, pnl_North, pnl_South, pnl_Profile, pnl_Chat,
			pnl_ChatList_set, pnl_Chanel, pnl_ChatRoom;
	public JButton btn_Friend, btn_ChatList, btn_Profile, btn_FriendList, btn_ModifyPhone, btn_Logout, btn_addFriend,
			btn_Search, btn_addChat, btn_Setting, btn_ModifyId, btn_ModifyEmail, btn_ChgPw, btn_ChgPic, btn_Chanel,
			btn_ChatRoom;
	public JScrollPane scrollRoomList;
	private JTextField txtField_chat;
	private JLabel lbl_ChatList;
	private ImageIcon changeIcon_Search, icon_addChat, changeIcon_addChat, icon_Search;
	private Image img_Search, changeImg_Search, img_addChat, changeImg_addChat;

	public AddRoom[] addRoom;
	public BufferedReader bReader;
	public PrintWriter pWriter;

	public ProgramGUI(BufferedReader bReader, PrintWriter pWriter) {
		this.pWriter = pWriter;
		ProfileGUI(bReader, pWriter);
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	public void ProfileGUI(BufferedReader bReader, PrintWriter pWriter) {
		//----------------------------addPanel()----------------------------
		pnl_Menu = new JPanel();
		pnl_Menu.setBounds(0, 0, 142, 719);
		pnl_Menu.setBackground(new Color(173, 216, 230));
		pnl_Menu.setLayout(null);
		getContentPane().add(pnl_Menu);
		getContentPane().setLayout(null);

		pnl_Parent = new JPanel();
		pnl_Parent.setBackground(new Color(245, 245, 245));
		pnl_Parent.setBounds(140, 0, 1002, 719);
		pnl_Parent.setLayout(null);
		getContentPane().add(pnl_Parent);

		// 프로필 패널
		pnl_Profile = new JPanel();
		pnl_Profile.setBackground(new Color(245, 245, 245));
		pnl_Profile.setBounds(0, 0, 1002, 719);
		pnl_Profile.setLayout(null);

		// 친구 패널
		pnl_Friend = new JPanel();
		pnl_Friend.setBackground(new Color(245, 245, 245));
		pnl_Friend.setBounds(0, 0, 1002, 719);
		pnl_Friend.setLayout(null);

		// 채널 패널
		pnl_Chanel = new JPanel();
		pnl_Chanel.setBackground(new Color(245, 245, 245));
		pnl_Chanel.setBounds(0, 0, 1002, 719);
		pnl_Chanel.setLayout(null);

		// 채팅방 패널
		pnl_ChatRoom = new JPanel();
		pnl_ChatRoom.setBackground(new Color(245, 245, 245));
		pnl_ChatRoom.setBounds(0, 0, 1002, 719);
		pnl_ChatRoom.setLayout(null);
		ChatRoomGUI();

		pnl_Parent.add(pnl_Chanel); // 첫 실행시 초기화면

		//----------------------------ProfileGUI()----------------------------
		pnl_back = new JPanel();
		pnl_back.setBackground(new Color(173, 216, 230));
		pnl_back.setBounds(48, 28, 894, 637);
		pnl_Profile.add(pnl_back);
		pnl_back.setLayout(null);

		pnl_North = new JPanel();
		pnl_North.setBackground(new Color(245, 245, 245));
		pnl_North.setBounds(37, 112, 820, 270);
		pnl_back.add(pnl_North);
		pnl_North.setLayout(null);

		pnl_South = new JPanel();
		pnl_South.setBackground(new Color(245, 245, 245));
		pnl_South.setBounds(37, 394, 820, 215);
		pnl_back.add(pnl_South);
		pnl_South.setLayout(null);

		JLabel lbl_Id = new JLabel("ID");
		lbl_Id.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Id.setBounds(39, 36, 126, 37);
		pnl_North.add(lbl_Id);

		JLabel lbl_getId = new JLabel("겟아이디()");
		lbl_getId.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_getId.setBounds(161, 36, 226, 37);
		pnl_North.add(lbl_getId);

		JLabel lbl_Email = new JLabel("이메일");
		lbl_Email.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Email.setBounds(39, 110, 126, 37);
		pnl_North.add(lbl_Email);

		JLabel lbl_getPw = new JLabel("get이메일()");
		lbl_getPw.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_getPw.setBounds(161, 110, 194, 37);
		pnl_North.add(lbl_getPw);

		JLabel lbl_Email_1 = new JLabel("전화번호");
		lbl_Email_1.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Email_1.setBounds(39, 175, 126, 37);
		pnl_North.add(lbl_Email_1);

		JLabel lbl_getPw_1 = new JLabel("get전화번호()");
		lbl_getPw_1.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_getPw_1.setBounds(161, 175, 194, 37);
		pnl_North.add(lbl_getPw_1);

		JLabel lbl_chgPw = new JLabel("비밀번호 관리");
		lbl_chgPw.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_chgPw.setBounds(36, 27, 175, 37);
		pnl_South.add(lbl_chgPw);

		btn_ModifyId = new JButton("아이디 수정");
		btn_ModifyId.setBackground(new Color(220, 220, 220));
		btn_ModifyId.setBounds(665, 37, 141, 38);
		pnl_North.add(btn_ModifyId);

		btn_ModifyEmail = new JButton("이메일 수정");
		btn_ModifyEmail.setBackground(new Color(220, 220, 220));
		btn_ModifyEmail.setBounds(665, 111, 141, 38);
		pnl_North.add(btn_ModifyEmail);

		btn_ModifyPhone = new JButton("전화번호 수정");
		btn_ModifyPhone.setBackground(new Color(220, 220, 220));
		btn_ModifyPhone.setBounds(665, 176, 141, 38);
		pnl_North.add(btn_ModifyPhone);

		btn_ChgPw = new JButton("비밀번호 변경");
		btn_ChgPw.setBackground(new Color(220, 220, 220));
		btn_ChgPw.setBounds(665, 27, 141, 38);
		pnl_South.add(btn_ChgPw);

		btn_Logout = new JButton("로그아웃");
		btn_Logout.setBackground(new Color(220, 220, 220));
		btn_Logout.setBounds(665, 137, 141, 38);
		pnl_South.add(btn_Logout);

		// 프로필 사진 라벨
		ImageIcon icon_profilePic = new ImageIcon(".\\icon\\profilePic.png");
		Image img_profilePic = icon_profilePic.getImage();
		Image changeImg_profilePic = img_profilePic.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_ProfilePic = new ImageIcon(changeImg_profilePic);
		JLabel lbl_Pic = new JLabel(changeIcon_ProfilePic);
		lbl_Pic.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Pic.setOpaque(true);
		lbl_Pic.setBounds(39, 12, 89, 88);
		pnl_back.add(lbl_Pic);

		//----------------------------FriendGUI()----------------------------
		btn_ChgPic = new JButton("프로필사진 변경");
		btn_ChgPic.setBackground(new Color(220, 220, 220));
		btn_ChgPic.setBounds(700, 43, 157, 38);
		pnl_back.add(btn_ChgPic);

		JLabel lbl_myProfile = new JLabel("내 프로필");
		lbl_myProfile.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_myProfile.setBounds(26, 20, 110, 43);
		pnl_Friend.add(lbl_myProfile);

		JLabel lbl_myFriend = new JLabel("친구");
		lbl_myFriend.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_myFriend.setBounds(26, 195, 110, 43);
		pnl_Friend.add(lbl_myFriend);

		btn_Profile = new JButton("New button");
		btn_Profile.setBackground(new Color(245, 245, 245));
		btn_Profile.setBounds(36, 73, 916, 93);
		pnl_Friend.add(btn_Profile);

		btn_FriendList = new JButton("New button");
		btn_FriendList.setBackground(new Color(245, 245, 245));
		btn_FriendList.setBounds(36, 246, 916, 93);
		pnl_Friend.add(btn_FriendList);

		ImageIcon icon_addFriend = new ImageIcon(".\\icon\\addFriend.png");
		Image img_addFriend = icon_addFriend.getImage();
		Image changeImg_addFriend = img_addFriend.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_addFriend = new ImageIcon(changeImg_addFriend);
		btn_addFriend = new JButton(changeIcon_addFriend);
		btn_addFriend.setBounds(900, 24, 50, 50);
		pnl_Friend.add(btn_addFriend);
		btn_addFriend.setBorderPainted(false);
		btn_addFriend.setContentAreaFilled(false);
		btn_addFriend.addActionListener(this);

		//----------------------------ChanelGUI()----------------------------
		lbl_ChatList = new JLabel("채팅방");
		lbl_ChatList.setFont(new Font("굴림", Font.BOLD, 25));
		lbl_ChatList.setBounds(39, 24, 139, 51);
		pnl_Chanel.add(lbl_ChatList);

		// 검색 버튼
		icon_Search = new ImageIcon(".\\icon\\search.png");
		img_Search = icon_Search.getImage();
		changeImg_Search = img_Search.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		changeIcon_Search = new ImageIcon(changeImg_Search);
		btn_Search = new JButton(changeIcon_Search);
		btn_Search.setBounds(835, 24, 50, 50);
		pnl_Chanel.add(btn_Search);
		btn_Search.setBorderPainted(false);
		btn_Search.setContentAreaFilled(false);
		btn_Search.addActionListener(this);

		// 채팅방 생성 버튼
		icon_addChat = new ImageIcon(".\\icon\\addChat.png");
		img_addChat = icon_addChat.getImage();
		changeImg_addChat = img_addChat.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		changeIcon_addChat = new ImageIcon(changeImg_addChat);
		btn_addChat = new JButton(changeIcon_addChat);
		btn_addChat.setBounds(900, 24, 50, 50);
		pnl_Chanel.add(btn_addChat);
		btn_addChat.setBorderPainted(false);
		btn_addChat.setContentAreaFilled(false);
		btn_addChat.addActionListener(this);
		
		//생성되어있는 대화방 불러오기
		addRoom = new AddRoom[15];
		pnl_Chat = new JPanel(); // 100개
		pnl_Chat.setBackground(new Color(245, 245, 245));
		pnl_Chat.setLayout(new GridLayout(15, 1));
		for (int i = 1; i < 15; i++) {
			addRoom[i] = new AddRoom(bReader, pWriter);
			addRoom[i].setBackground(new Color(245, 245, 245));
			pnl_Chat.add(addRoom[i]);
			pnl_Chat.revalidate();
			pnl_Chat.repaint();
		}
		scrollRoomList = new JScrollPane(pnl_Chat);
		scrollRoomList.setBackground(new Color(245, 245, 245));
		scrollRoomList.setBounds(39, 115, 926, 564);
		scrollRoomList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollRoomList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollRoomList.getVerticalScrollBar().setUnitIncrement(16);
		scrollRoomList.setViewportView(pnl_Chat);
		scrollRoomList.revalidate();
		scrollRoomList.repaint();
		pnl_Chanel.add(scrollRoomList);

		// 프로필 버튼 이미지 셋팅
		ImageIcon icon_Profile = new ImageIcon(".\\icon\\Profile.png");
		Image img_Profile = icon_Profile.getImage();
		Image changeImg_Profile = img_Profile.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_Profile = new ImageIcon(changeImg_Profile);

		btn_Profile = new JButton(changeIcon_Profile);
		btn_Profile.setBounds(23, 21, 97, 87);
		pnl_Menu.add(btn_Profile);
		btn_Profile.setBorderPainted(false);
		btn_Profile.setContentAreaFilled(false);
		btn_Profile.addActionListener(this);

		ImageIcon icon_Friend = new ImageIcon(".\\icon\\Friend.png");
		Image img_Friend = icon_Friend.getImage();
		Image changeImg_Friend = img_Friend.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_Friend = new ImageIcon(changeImg_Friend);

		btn_Friend = new JButton(changeIcon_Friend);
		btn_Friend.setBounds(23, 118, 97, 87);
		pnl_Menu.add(btn_Friend);
		btn_Friend.setBorderPainted(false);
		btn_Friend.setContentAreaFilled(false);
		btn_Friend.addActionListener(this);

		//----------------------------addButton()----------------------------
		// 채널 버튼 이미지 셋팅
		ImageIcon icon_Chanel = new ImageIcon(".\\icon\\Chanel.png");
		Image img_Chanel = icon_Chanel.getImage();
		Image changeImg_Chanel = img_Chanel.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_Chanel = new ImageIcon(changeImg_Chanel);

		btn_Chanel = new JButton(changeIcon_Chanel);
		btn_Chanel.setBounds(23, 205, 97, 87);
		pnl_Menu.add(btn_Chanel);
		btn_Chanel.setBorderPainted(false);
		btn_Chanel.setContentAreaFilled(false);
//				btn_Profile.setFocusPainted(false);
		btn_Chanel.addActionListener(this);

		// 채팅목록 버튼 이미지 셋팅
		ImageIcon icon_ChatList = new ImageIcon(".\\icon\\Chat.png");
		Image img_ChatList = icon_ChatList.getImage();
		Image changeImg2 = img_ChatList.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon2 = new ImageIcon(changeImg2);

		btn_ChatRoom = new JButton(changeIcon2);
		btn_ChatRoom.setBounds(23, 300, 97, 87);
		pnl_Menu.add(btn_ChatRoom);
		btn_ChatRoom.setBorderPainted(false);
		btn_ChatRoom.setContentAreaFilled(false);
//				btn_ChatList.setFocusPainted(false);
		btn_ChatRoom.addActionListener(this);

		// 셋팅 버튼 이미지 셋팅
		ImageIcon icon_Setting = new ImageIcon(".\\icon\\setting.png");
		Image img_Setting = icon_Setting.getImage();
		Image changeImg_Setting = img_Setting.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon changeIcon_Setting = new ImageIcon(changeImg_Setting);

		btn_Setting = new JButton(changeIcon_Setting);
		btn_Setting.setBounds(23, 600, 97, 87);
		pnl_Menu.add(btn_Setting);
		btn_Setting.setBorderPainted(false);
		btn_Setting.setContentAreaFilled(false);
//				btn_Profile.setFocusPainted(false);
		btn_Setting.addActionListener(this);

		setResizable(false);
		setBounds(100, 100, 1148, 748);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void ChatRoomGUI() {
		JLabel lbl_ChatName = new JLabel("get채팅방 이름()");
		lbl_ChatName.setFont(new Font("HY�︪��M", Font.BOLD, 25));
		lbl_ChatName.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_ChatName.setBounds(14, 12, 205, 38);
		pnl_ChatRoom.add(lbl_ChatName);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(14, 57, 677, 634);
		pnl_ChatRoom.add(panel_2);
		panel_2.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(14, 12, 648, 454);
		panel_2.add(textArea);

		txtField_chat = new JTextField();
		txtField_chat.setBounds(14, 478, 484, 132);
		panel_2.add(txtField_chat);
		txtField_chat.setColumns(10);

		JButton btn_Send = new JButton("전송");
		btn_Send.setBackground(new Color(173, 216, 230));
		btn_Send.setBounds(509, 478, 153, 132);
		panel_2.add(btn_Send);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(705, 55, 271, 636);
		pnl_ChatRoom.add(panel_3);
		panel_3.setLayout(null);

		JButton btn_user1 = new JButton("USER1");
		btn_user1.setBackground(new Color(211, 211, 211));
		btn_user1.setBounds(14, 12, 243, 50);
		panel_3.add(btn_user1);

		JButton btn_user2 = new JButton("USER2");
		btn_user2.setBackground(new Color(211, 211, 211));
		btn_user2.setBounds(14, 72, 243, 50);
		panel_3.add(btn_user2);

		JButton btn_user3 = new JButton("USER3");
		btn_user3.setBackground(new Color(211, 211, 211));
		btn_user3.setBounds(14, 134, 243, 50);
		panel_3.add(btn_user3);

		JButton btn_user4 = new JButton("USER4");
		btn_user4.setBackground(new Color(211, 211, 211));
		btn_user4.setBounds(14, 196, 243, 50);
		panel_3.add(btn_user4);

		JButton btn_user5 = new JButton("USER5");
		btn_user5.setBackground(new Color(211, 211, 211));
		btn_user5.setBounds(14, 258, 243, 50);
		panel_3.add(btn_user5);

		JButton btn_user6 = new JButton("USER6");
		btn_user6.setBackground(new Color(211, 211, 211));
		btn_user6.setBounds(14, 320, 243, 50);
		panel_3.add(btn_user6);

		JButton btn_user7 = new JButton("USER7");
		btn_user7.setBackground(new Color(211, 211, 211));
		btn_user7.setBounds(14, 382, 243, 50);
		panel_3.add(btn_user7);

		JButton btn_user8 = new JButton("USER8");
		btn_user8.setBackground(new Color(211, 211, 211));
		btn_user8.setBounds(14, 444, 243, 50);
		panel_3.add(btn_user8);

		JButton btn_user9 = new JButton("USER9");
		btn_user9.setBackground(new Color(211, 211, 211));
		btn_user9.setBounds(14, 506, 243, 50);
		panel_3.add(btn_user9);

		JButton btn_user10 = new JButton("USER 10");
		btn_user10.setBackground(new Color(211, 211, 211));
		btn_user10.setBounds(14, 568, 243, 50);
		panel_3.add(btn_user10);

		JLabel lbl_Persons = new JLabel("참여인원 get인원()");
		lbl_Persons.setFont(new Font("HY�︪��B", Font.BOLD, 25));
		lbl_Persons.setBounds(711, 12, 222, 38);
		pnl_ChatRoom.add(lbl_Persons);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 각 메뉴 클릭시
		Object ob = e.getSource();
		if (ob == btn_Profile) {
			pnl_Parent.removeAll();
			pnl_Parent.add(pnl_Profile);
			pnl_Parent.repaint();
			pnl_Parent.revalidate();
		} else if (ob == btn_Friend) {
			pnl_Parent.add(pnl_Friend);
			pnl_Parent.removeAll();
			pnl_Parent.add(pnl_Friend);
			pnl_Parent.repaint();
			pnl_Parent.revalidate();
		} else if (ob == btn_Chanel) {
			pnl_Parent.add(pnl_Chanel);
			pnl_Parent.removeAll();
			pnl_Parent.add(pnl_Chanel);
			pnl_Parent.repaint();
			pnl_Parent.revalidate();
		} else if (ob == btn_ChatRoom) {
			pnl_Parent.add(pnl_ChatRoom);
			pnl_Parent.removeAll();
			pnl_Parent.add(pnl_ChatRoom);
			pnl_Parent.repaint();
			pnl_Parent.revalidate();
		}

//		// 채팅방 생성버튼 클릭시
//		if (ob == btn_addChat) {
//			CreateChatRoom();
//		} else if (ob == btn_Search) {
//			System.out.println("검색창");
//		}
	}

	public void pnlClear() {
		pnl_Chat.removeAll();
		for (int i = 0; i < 15; i++) {
			addRoom[i] = new AddRoom(bReader, pWriter);
			addRoom[i].setBackground(new Color(245, 245, 245));
			pnl_Chat.add(addRoom[i]);
			pnl_Chat.revalidate();
			pnl_Chat.repaint();
		}
	}
}
