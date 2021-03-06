package room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import lunch.Protocol;

public class AddRoom extends JButton implements ActionListener{
	public String label[] = { "", "", "", "", "", "", "" };
	public JLabel labelArr[];
	public JDialog passwordCon;
	private JPasswordField pf_passwordConfirm;
	private JButton btn_enter;
	
	private BufferedReader bReader;
	private PrintWriter pWriter;

	public AddRoom(BufferedReader bReader, PrintWriter pWriter) {
		this.bReader = bReader;
		this.pWriter = pWriter;
		
		passwordCon = new JDialog();
		passwordCon.setVisible(false);
		passwordCon.setSize(486, 300);
		passwordCon.setLocationRelativeTo(null);
		passwordCon.setResizable(false);
		passwordCon.setLayout(null);
		passwordCon.setModal(true);
		passwordCon();
	}

	public void init() {
		setLayout(new GridLayout(3, 3, 5, 5));
		setPreferredSize(new Dimension(661, 100));
		setBorderPainted(false);
		setContentAreaFilled(false);
		addActionListener(this);
		labelArr = new JLabel[label.length];
		btn_enter.addActionListener(this);
		for (int i = 0; i < label.length; i++) {
			labelArr[i] = new JLabel(label[i]);
			add(labelArr[i]);
		}
	}
	
	public void passwordCon() {
		JPanel pnl = new JPanel();
		pnl.setBackground(new Color(173, 216, 230));
		pnl.setBounds(0, 0, 486, 273);
		passwordCon.getContentPane().add(pnl);
		pnl.setLayout(null);
		
		JLabel lbl_PasswordLogo = new JLabel("Password");
		lbl_PasswordLogo.setBounds(25, 6, 177, 78);
		pnl.add(lbl_PasswordLogo);
		lbl_PasswordLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_PasswordLogo.setForeground(Color.WHITE);
		lbl_PasswordLogo.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lbl_password = new JLabel("????????????");
		lbl_password.setFont(new Font("??????", Font.BOLD, 15));
		lbl_password.setBounds(62, 122, 67, 27);
		pnl.add(lbl_password);
		
		pf_passwordConfirm = new JPasswordField();
		pf_passwordConfirm.setBounds(144, 122, 232, 27);
		pnl.add(pf_passwordConfirm);
		pf_passwordConfirm.setColumns(10);
		
		btn_enter = new JButton("??????");
		btn_enter.setBackground(new Color(173, 216, 230));
		btn_enter.setBounds(184, 201, 111, 27);
		
		pnl.add(btn_enter);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this) {
			int isOk = JOptionPane.showConfirmDialog(this, labelArr[0].getText() 
					+ "??? ?????? ?????????????????????????", "", JOptionPane.OK_CANCEL_OPTION);
			if (isOk == JOptionPane.OK_OPTION) {
				String userCount[] = labelArr[5].getText().split(" / ");
				System.out.println(labelArr[5].getText());
				System.out.println(userCount[0]);
				System.out.println(userCount[1]);
				if (userCount[0].compareTo(userCount[1]) == 0) {
					JOptionPane.showMessageDialog(null, "????????? ??????");
				} else if (labelArr[2].getText().equals("[?????????]")) {
					passwordCon.setVisible(true);
					EventQueue.invokeLater(new Runnable() {
						   @Override
						     public void run() {
						        pf_passwordConfirm.requestFocus();
						     }
						});
				} else {
					pWriter.println(Protocol.ENTER + ">" + labelArr[0].getText());
					pWriter.flush();
				}
			} 
		} else if (e.getSource() == btn_enter) {
			pWriter.println(Protocol.ENTER_PASSWORD + ">" + labelArr[0].getText() 
					+ ">" + getPW());
			pWriter.flush();
			passwordCon.setVisible(false);
			pf_passwordConfirm.setText("");
		}
	}
	
	public String getPW() {
		String pw = "";
		char[] secret_pw = pf_passwordConfirm.getPassword();
		for(char cha : secret_pw) {
			Character.toString(cha);
			pw += (pw.equals("")) ? "" + cha + "" : "" + cha + "";		
		}
		return pw;
	}
}
