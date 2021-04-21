package room;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import GUI.ProgramGUI;

public class MakeRoom extends JFrame {

	public JDialog dl_addChat;
	public JPanel pnl;
	public JLabel lbl_Logo, lbl_RoomName, lbl_RoomPw, lbl_Persons;
	public JTextField txtField_RoomName;
	public JPasswordField passwordField_RoomPw;
	public JComboBox cb_Persons;
	public JCheckBox chckbxNewCheckBox;
	public JButton btn_done, btn_Cancle;

	public MakeRoom() {
		String[] combo = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
		
		dl_addChat = new JDialog();
		dl_addChat.setVisible(false);
		dl_addChat.setSize(585, 395);
		dl_addChat.setLocationRelativeTo(null);
		dl_addChat.setResizable(false);
		dl_addChat.setLayout(null);
		
		pnl = new JPanel();
		pnl.setBackground(new Color(173, 216, 230));
		pnl.setBounds(0, 0, 567, 347);
		dl_addChat.add(pnl);
		pnl.setLayout(null);
		
		lbl_Logo = new JLabel("New Chat");
		lbl_Logo.setBounds(25, 6, 186, 63);
		lbl_Logo.setForeground(Color.WHITE);
		lbl_Logo.setFont(new Font("Arial", Font.BOLD, 35));
		pnl.add(lbl_Logo);
		
		lbl_RoomName = new JLabel("방제목");
		lbl_RoomName.setBounds(84, 122, 99, 38);
		lbl_RoomName.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_RoomName.setFont(new Font("굴림", Font.BOLD, 20));
		pnl.add(lbl_RoomName);
		
		lbl_RoomPw = new JLabel("비밀번호");
		lbl_RoomPw.setBounds(84, 177, 99, 38);
		lbl_RoomPw.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_RoomPw.setFont(new Font("굴림", Font.BOLD, 20));
		pnl.add(lbl_RoomPw);
		
		lbl_Persons = new JLabel("인원");
		lbl_Persons.setBounds(363, 80, 72, 30);
		lbl_Persons.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Persons.setHorizontalAlignment(SwingConstants.CENTER);
		pnl.add(lbl_Persons);
		
		txtField_RoomName = new JTextField();
		txtField_RoomName.setBounds(184, 122, 294, 33);
		pnl.add(txtField_RoomName);
		txtField_RoomName.setColumns(10);
		
		passwordField_RoomPw = new JPasswordField();
		passwordField_RoomPw.setBounds(184, 177, 294, 33);
		passwordField_RoomPw.setEditable(false);
		passwordField_RoomPw.setColumns(10);
		pnl.add(passwordField_RoomPw);
		
		cb_Persons = new JComboBox(combo);
		cb_Persons.setBounds(433, 82, 45, 30);
		cb_Persons.setEditable(true);
		pnl.add(cb_Persons);
		
		chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setBounds(488, 168, 55, 47);
		chckbxNewCheckBox.setFont(new Font("굴림", Font.BOLD, 17));
		pnl.add(chckbxNewCheckBox);
		chckbxNewCheckBox.setBackground(new Color(173, 216, 230));
		
		btn_done = new JButton("방생성");
		btn_done.setBounds(121, 273, 155, 38);
		btn_done.setBackground(new Color(173, 216, 230));
		pnl.add(btn_done);
		
		btn_Cancle = new JButton("취소");
		btn_Cancle.setBounds(290, 273, 155, 38);
		btn_Cancle.setBackground(new Color(173, 216, 230));
		pnl.add(btn_Cancle);
		
		add(pnl);
		setVisible(false);
		setSize(585, 395);
		setResizable(false);
		setLayout(null);
	}
}