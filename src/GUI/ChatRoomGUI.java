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

public class ChatRoomGUI {

	JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomGUI window = new ChatRoomGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatRoomGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1148, 748);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(173, 216, 230));
		panel.setBounds(0, 0, 142, 703);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(248, 248, 255));
		panel_1.setBounds(140, 0, 990, 703);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("get채팅방 이름()");
		lblNewLabel.setFont(new Font("HY�︪��M", Font.BOLD, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(14, 12, 205, 38);
		panel_1.add(lblNewLabel);
		
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
		
		JButton btnNewButton = new JButton("전송");
		btnNewButton.setBackground(new Color(173, 216, 230));
		btnNewButton.setBounds(509, 478, 153, 132);
		panel_2.add(btnNewButton);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(705, 55, 271, 636);
		panel_1.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("USER1");
		btnNewButton_1.setBackground(new Color(211, 211, 211));
		btnNewButton_1.setBounds(14, 12, 243, 50);
		panel_3.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("USER2");
		btnNewButton_2.setBackground(new Color(211, 211, 211));
		btnNewButton_2.setBounds(14, 72, 243, 50);
		panel_3.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("USER3");
		btnNewButton_3.setBackground(new Color(211, 211, 211));
		btnNewButton_3.setBounds(14, 134, 243, 50);
		panel_3.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("USER4");
		btnNewButton_4.setBackground(new Color(211, 211, 211));
		btnNewButton_4.setBounds(14, 196, 243, 50);
		panel_3.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("USER5");
		btnNewButton_5.setBackground(new Color(211, 211, 211));
		btnNewButton_5.setBounds(14, 258, 243, 50);
		panel_3.add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("USER6");
		btnNewButton_6.setBackground(new Color(211, 211, 211));
		btnNewButton_6.setBounds(14, 320, 243, 50);
		panel_3.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("USER7");
		btnNewButton_7.setBackground(new Color(211, 211, 211));
		btnNewButton_7.setBounds(14, 382, 243, 50);
		panel_3.add(btnNewButton_7);
		
		JButton btnNewButton_8 = new JButton("USER8");
		btnNewButton_8.setBackground(new Color(211, 211, 211));
		btnNewButton_8.setBounds(14, 444, 243, 50);
		panel_3.add(btnNewButton_8);
		
		JButton btnNewButton_9 = new JButton("USER9");
		btnNewButton_9.setBackground(new Color(211, 211, 211));
		btnNewButton_9.setBounds(14, 506, 243, 50);
		panel_3.add(btnNewButton_9);
		
		JButton btnNewButton_10 = new JButton("USER 10");
		btnNewButton_10.setBackground(new Color(211, 211, 211));
		btnNewButton_10.setBounds(14, 568, 243, 50);
		panel_3.add(btnNewButton_10);
		
		JLabel lblNewLabel_1 = new JLabel("참여인원 get인원()");
		lblNewLabel_1.setFont(new Font("HY�︪��B", Font.BOLD, 25));
		lblNewLabel_1.setBounds(711, 12, 222, 38);
		panel_1.add(lblNewLabel_1);
	}
}
