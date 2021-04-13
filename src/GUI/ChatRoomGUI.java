package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ChatRoomGUI extends JFrame{

	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomGUI window = new ChatRoomGUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChatRoomGUI() {
		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 1148, 748);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(173, 216, 230));
		panel.setBounds(0, 0, 142, 703);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(248, 248, 255));
		panel_1.setBounds(140, 0, 990, 703);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lbl_ChatName = new JLabel("get채팅방 이름()");
		lbl_ChatName.setFont(new Font("HY�︪��M", Font.BOLD, 25));
		lbl_ChatName.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_ChatName.setBounds(14, 12, 205, 38);
		panel_1.add(lbl_ChatName);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(14, 57, 677, 634);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(14, 12, 648, 454);
		panel_2.add(textArea);
		
		textField = new JTextField();
		textField.setBounds(14, 478, 484, 132);
		panel_2.add(textField);
		textField.setColumns(10);
		
		JButton btn_Send = new JButton("전송");
		btn_Send.setBackground(new Color(173, 216, 230));
		btn_Send.setBounds(509, 478, 153, 132);
		panel_2.add(btn_Send);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(705, 55, 271, 636);
		panel_1.add(panel_3);
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
		panel_1.add(lbl_Persons);
	}
}
