package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProgramGUI extends JFrame implements ActionListener {
	public JPanel pnl_Parent, pnl_Friend, pnl_ChatList, pnl_Menu, pnl_back, pnl_North, pnl_South, pnl_Profile,
			pnl_Chat;
	public JButton btn_Friend, btn_ChatList, btn_Profile, btn_FriendList, btn_Chat1, btn_Chat2, btn_Chat3, btn_Chat4,
			btn_Chat5, btn_Search, btn_addChat, btn_Setting, btn_ModifyId, btn_ModifyEmail, btn_ChgPw, btn_ChgPic,
			btn_Chanel;
	private JLabel lbl_myFriend, lbl_ChatList, lbl_myProfile, lbl_chgPw, lbl_Pic, lbl_Id, lbl_getId, lbl_Email,
			lbl_getPw;
	private ImageIcon icon_ChatList, changeIcon2, icon_Profile, changeIcon_Search, icon_addChat, changeIcon_addChat,
			changeIcon_Profile, icon_Chanel, changeIcon_Chanel, icon_Friend, changeIcon_Friend, icon_Setting,
			changeIcon_Setting, icon_Search;
	private Image img_ChatList, changeImg2, img_Profile, img_Search, changeImg_Search, img_addChat, changeImg_addChat,
			changeImg_Profile, img_Chanel, changeImg_Chanel, img_Friend, changeImg_Friend, img_Setting,
			changeImg_Setting;

	public ProgramGUI() {
		ProfileGUI();
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

	public void ProfileGUI() {
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

		// 채팅목록 패널
		pnl_ChatList = new JPanel();
		pnl_ChatList.setBackground(new Color(245, 245, 245));
		pnl_ChatList.setBounds(0, 0, 1002, 719);
		pnl_ChatList.setLayout(null);

		pnl_Parent.add(pnl_ChatList); // 첫 실행시 초기화면

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

		lbl_Id = new JLabel("ID");
		lbl_Id.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Id.setBounds(39, 36, 126, 37);
		pnl_North.add(lbl_Id);

		lbl_getId = new JLabel("겟아이디()");
		lbl_getId.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_getId.setBounds(39, 85, 226, 37);
		pnl_North.add(lbl_getId);

		lbl_Email = new JLabel("이메일");
		lbl_Email.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Email.setBounds(39, 147, 126, 37);
		pnl_North.add(lbl_Email);

		lbl_getPw = new JLabel("겟비번()");
		lbl_getPw.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_getPw.setBounds(39, 196, 194, 37);
		pnl_North.add(lbl_getPw);

		btn_ModifyId = new JButton("아이디 수정");
		btn_ModifyId.setBounds(665, 56, 141, 38);
		pnl_North.add(btn_ModifyId);

		btn_ModifyEmail = new JButton("이메일 수정");
		btn_ModifyEmail.setBounds(665, 172, 141, 38);
		pnl_North.add(btn_ModifyEmail);

		lbl_chgPw = new JLabel("비밀번호 변경");
		lbl_chgPw.setFont(new Font("굴림", Font.PLAIN, 20));
		lbl_chgPw.setBounds(36, 12, 175, 37);
		pnl_South.add(lbl_chgPw);

		btn_ChgPw = new JButton("비밀번호 변경");
		btn_ChgPw.setBounds(46, 61, 141, 38);
		pnl_South.add(btn_ChgPw);

		lbl_Pic = new JLabel("프사");
		lbl_Pic.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Pic.setOpaque(true);
		lbl_Pic.setBackground(new Color(60, 179, 113));
		lbl_Pic.setBounds(39, 12, 89, 88);
		pnl_back.add(lbl_Pic);

		btn_ChgPic = new JButton("프로필사진 변경");
		btn_ChgPic.setBounds(700, 43, 157, 38);
		pnl_back.add(btn_ChgPic);

		lbl_myProfile = new JLabel("내 프로필");
		lbl_myProfile.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_myProfile.setBounds(26, 20, 110, 43);
		pnl_Friend.add(lbl_myProfile);

		lbl_myFriend = new JLabel("친구");
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

		lbl_ChatList = new JLabel("채팅방");
		lbl_ChatList.setFont(new Font("굴림", Font.BOLD, 25));
		lbl_ChatList.setBounds(39, 24, 139, 51);
		pnl_ChatList.add(lbl_ChatList);

//		dp = new DetailPanel[100];
//		
//		pnl_Chat = new JPanel(new GridLayout(100, 2, 10, 10)); // 100개
//		pnl_Chat.setPreferredSize(new Dimension(921, 558));
//		for (int i = 0; i < 100; i++) {
//			dp[i] = new DetailPanel(br, pw);
//			pnl_Chat.add(dp[i]);
//		}
//		JScrollPane scrollRoomList = new JScrollPane(centerPanel);
//		scrollRoomList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scrollRoomList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		scrollRoomList.getVerticalScrollBar().setValue(scrollRoomList.getVerticalScrollBar().getMaximum());
		
		
		pnl_Chat = new JPanel();
		pnl_Chat.setLayout(new BoxLayout(pnl_Chat, BoxLayout.Y_AXIS));
		pnl_Chat.setPreferredSize(new Dimension(921, 558));
		pnl_ChatList.add(pnl_Chat);
		
		btn_Chat1 = new JButton("chat 1");
		btn_Chat1.setBackground(new Color(245, 245, 245));
		btn_Chat1.setBounds(39, 87, 921, 93);
		pnl_Chat.add(btn_Chat1);

		btn_Chat2 = new JButton("chat 2");
		btn_Chat2.setBackground(new Color(245, 245, 245));
		btn_Chat2.setBounds(39, 197, 921, 93);
		pnl_Chat.add(btn_Chat2);

		btn_Chat3 = new JButton("chat 3");
		btn_Chat3.setBackground(new Color(245, 245, 245));
		btn_Chat3.setBounds(39, 302, 921, 93);
		pnl_Chat.add(btn_Chat3);

		btn_Chat4 = new JButton("chat 4");
		btn_Chat4.setBackground(new Color(245, 245, 245));
		btn_Chat4.setBounds(39, 405, 921, 93);
		pnl_Chat.add(btn_Chat4);

		btn_Chat5 = new JButton("chat 5");
		btn_Chat5.setBackground(new Color(245, 245, 245));
		btn_Chat5.setBounds(39, 508, 921, 93);
		pnl_Chat.add(btn_Chat5);

		// 검색 버튼
		icon_Search = new ImageIcon(".\\icon\\search.png");
		img_Search = icon_Search.getImage();
		changeImg_Search = img_Search.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		changeIcon_Search = new ImageIcon(changeImg_Search);
		btn_Search = new JButton(changeIcon_Search);
		btn_Search.setBounds(835, 24, 50, 50);
		pnl_ChatList.add(btn_Search);
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
		pnl_ChatList.add(btn_addChat);
		btn_addChat.setBorderPainted(false);
		btn_addChat.setContentAreaFilled(false);
		btn_addChat.addActionListener(this);

		// 프로필 버튼 이미지 셋팅
		icon_Profile = new ImageIcon(".\\icon\\Profile.png");
		img_Profile = icon_Profile.getImage();
		changeImg_Profile = img_Profile.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		changeIcon_Profile = new ImageIcon(changeImg_Profile);

		btn_Profile = new JButton(changeIcon_Profile);
		btn_Profile.setBounds(23, 21, 97, 87);
		pnl_Menu.add(btn_Profile);
		btn_Profile.setBorderPainted(false);
		btn_Profile.setContentAreaFilled(false);
		btn_Profile.addActionListener(this);

		icon_Friend = new ImageIcon(".\\icon\\Friend.png");
		img_Friend = icon_Friend.getImage();
		changeImg_Friend = img_Friend.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		changeIcon_Friend = new ImageIcon(changeImg_Friend);

		btn_Friend = new JButton(changeIcon_Friend);
		btn_Friend.setBounds(23, 118, 97, 87);
		pnl_Menu.add(btn_Friend);
		btn_Friend.setBorderPainted(false);
		btn_Friend.setContentAreaFilled(false);
		btn_Friend.addActionListener(this);

		// 채널 버튼 이미지 셋팅
		icon_Chanel = new ImageIcon(".\\icon\\Chanel.png");
		img_Chanel = icon_Chanel.getImage();
		changeImg_Chanel = img_Chanel.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		changeIcon_Chanel = new ImageIcon(changeImg_Chanel);

		btn_Chanel = new JButton(changeIcon_Chanel);
		btn_Chanel.setBounds(23, 205, 97, 87);
		pnl_Menu.add(btn_Chanel);
		btn_Chanel.setBorderPainted(false);
		btn_Chanel.setContentAreaFilled(false);
//		btn_Profile.setFocusPainted(false);
		btn_Chanel.addActionListener(this);

		// 채팅목록 버튼 이미지 셋팅
		icon_ChatList = new ImageIcon(".\\icon\\Chat.png");
		img_ChatList = icon_ChatList.getImage();
		changeImg2 = img_ChatList.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		changeIcon2 = new ImageIcon(changeImg2);

		btn_ChatList = new JButton(changeIcon2);
		btn_ChatList.setBounds(23, 300, 97, 87);
		pnl_Menu.add(btn_ChatList);
		btn_ChatList.setBorderPainted(false);
		btn_ChatList.setContentAreaFilled(false);
//		btn_ChatList.setFocusPainted(false);
		btn_ChatList.addActionListener(this);

		// 셋팅 버튼 이미지 셋팅
		icon_Setting = new ImageIcon(".\\icon\\setting.png");
		img_Setting = icon_Setting.getImage();
		changeImg_Setting = img_Setting.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		changeIcon_Setting = new ImageIcon(changeImg_Setting);

		btn_Setting = new JButton(changeIcon_Setting);
		btn_Setting.setBounds(23, 600, 97, 87);
		pnl_Menu.add(btn_Setting);
		btn_Setting.setBorderPainted(false);
		btn_Setting.setContentAreaFilled(false);
//		btn_Profile.setFocusPainted(false);
		btn_Setting.addActionListener(this);

		setResizable(false);
		setBounds(100, 100, 1148, 748);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(false);
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
		} else if (ob == btn_ChatList) {
			pnl_Parent.add(pnl_ChatList);
			pnl_Parent.removeAll();
			pnl_Parent.add(pnl_ChatList);
			pnl_Parent.repaint();
			pnl_Parent.revalidate();
		}
	}
}
