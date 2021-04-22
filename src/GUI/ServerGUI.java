package GUI;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import lunch.Server;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerGUI extends JFrame{
	public JButton btn_ServerStop, btn_ServerOpen;
	public Server server;
	public JTextArea txtArea_ServerState;
	private JTextField txtField_Port;
	private JTextField txtField_Ip;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI server1 = new ServerGUI();
					server1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerGUI() {
		serverGUI();
	}
	
	private void serverGUI() {
		setBounds(100, 100, 849, 597);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(173, 216, 230));
		panel.setBounds(0, 0, 831, 569);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		txtArea_ServerState = new JTextArea();
		txtArea_ServerState.setBounds(14, 55, 550, 434);
		panel.add(txtArea_ServerState);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				server = new Server();
			}
		});
		
		btn_ServerOpen = new JButton("\uC11C\uBC84 \uC624\uD508");
		btn_ServerOpen.setBackground(new Color(173, 216, 230));
		btn_ServerOpen.setBounds(14, 501, 105, 27);
		panel.add(btn_ServerOpen);
		btn_ServerOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.start();
				btn_ServerOpen.setEnabled(false);
			}
		});
		
		btn_ServerStop = new JButton("\uC11C\uBC84 \uC911\uC9C0");
		btn_ServerStop.setBackground(new Color(173, 216, 230));
		btn_ServerStop.setBounds(125, 501, 105, 27);
		panel.add(btn_ServerStop);
		btn_ServerStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lbl_port = new JLabel("PORT : ");
		lbl_port.setBounds(405, 25, 62, 18);
		panel.add(lbl_port);
		
		txtField_Port = new JTextField();
		txtField_Port.setBounds(459, 22, 105, 24);
		panel.add(txtField_Port);
		txtField_Port.setColumns(10);
		
		JLabel lbl_User = new JLabel("User List");
		lbl_User.setFont(new Font("����", Font.BOLD, 20));
		lbl_User.setBounds(589, 22, 105, 21);
		panel.add(lbl_User);
		
		JLabel lbl_Ip = new JLabel("IP : ");
		lbl_Ip.setBounds(198, 25, 38, 18);
		panel.add(lbl_Ip);
		
		txtField_Ip = new JTextField();
		txtField_Ip.setColumns(10);
		txtField_Ip.setBounds(226, 22, 160, 24);
		panel.add(txtField_Ip);
		
		JButton btn_UserSetting = new JButton("\uAD00\uB9AC");
		btn_UserSetting.setBackground(new Color(173, 216, 230));
		btn_UserSetting.setBounds(712, 501, 105, 27);
		panel.add(btn_UserSetting);
		
		JButton btn_SaveLog = new JButton("\uB85C\uADF8 \uC800\uC7A5");
		btn_SaveLog.setBackground(new Color(173, 216, 230));
		btn_SaveLog.setBounds(459, 501, 105, 27);
		panel.add(btn_SaveLog);
		
		JLabel lbl_Statement = new JLabel("Statement");
		lbl_Statement.setFont(new Font("����", Font.BOLD, 20));
		lbl_Statement.setBounds(14, 25, 123, 18);
		panel.add(lbl_Statement);
		
		JTextArea txtArea_UserList = new JTextArea();
		txtArea_UserList.setBounds(578, 55, 239, 434);
		panel.add(txtArea_UserList);
	}
}
