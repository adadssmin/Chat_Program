package Login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import lunch.Protocol;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class Join extends JFrame{

	public JPasswordField pwField_PW, pwField_CheckPw;
	public JTextField txtField_ID, txtField_EmailID, txtField_Email;
	public JButton btn_CheckID, btn_CheckPw, btn_Join; 
	public JLabel lbl_Logo, lblNewLabel_6, lbl_CheckPw, lbl_Check_email;
	public JComboBox comboBox_email;
	public JFormattedTextField tfphoneNum;
	private MaskFormatter mask;
	private ImageIcon iconLogo, changeIcon;
	private Image imgLogo, changeImg;


	public Join() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JoinGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void JoinGUI() {
getContentPane().setLayout(null);
		
		iconLogo = new ImageIcon(".\\icon\\logo.png");
		imgLogo = iconLogo.getImage();
		changeImg = imgLogo.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		changeIcon = new ImageIcon(changeImg);	
		
		lbl_Logo = new JLabel(changeIcon);
		lbl_Logo.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Logo.setForeground(new Color(255, 0, 0));
		lbl_Logo.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Logo.setBounds(243, 41, 125, 103);
		getContentPane().add(lbl_Logo);
		
		JLabel lbl_Join = new JLabel("JOIN");
		lbl_Join.setBounds(23, 10, 103, 74);
		lbl_Join.setFont(new Font("Dialog", Font.BOLD, 40));
		lbl_Join.setForeground(Color.WHITE);
		lbl_Join.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_Join);
		
		JLabel lbl_JoinID = new JLabel("\uC544\uC774\uB514");
		lbl_JoinID.setBounds(71, 156, 77, 40);
		lbl_JoinID.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_JoinID);
		
		JLabel lbl_JoinPw = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbl_JoinPw.setBounds(71, 222, 94, 40);
		lbl_JoinPw.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_JoinPw);
		
		JLabel lbl_JoinCheckPw = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778");
		lbl_JoinCheckPw.setBounds(71, 262, 115, 40);
		lbl_JoinCheckPw.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_JoinCheckPw);
		
		JLabel lbl_JoinEmail = new JLabel("\uC774\uBA54\uC77C");
		lbl_JoinEmail.setBounds(71, 331, 77, 40);
		lbl_JoinEmail.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_JoinEmail);
		
		JLabel lbl_JoinPhone = new JLabel("\uC804\uD654\uBC88\uD638");
		lbl_JoinPhone.setBounds(71, 397, 84, 40);
		lbl_JoinPhone.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lbl_JoinPhone);
		
		txtField_ID = new JTextField();
		txtField_ID.setBounds(160, 162, 270, 30);
		getContentPane().add(txtField_ID);
		txtField_ID.setColumns(10);
		
		pwField_PW = new JPasswordField();
		pwField_PW.setBounds(160, 228, 270, 30);
		getContentPane().add(pwField_PW);
		
		pwField_CheckPw = new JPasswordField();
		pwField_CheckPw.setBounds(160, 268, 270, 30);
		getContentPane().add(pwField_CheckPw);
		
		txtField_EmailID = new JTextField();
		txtField_EmailID.setBounds(160, 337, 125, 27);
		txtField_EmailID.setColumns(10);
		getContentPane().add(txtField_EmailID);
		
		String[] combo = {"이메일 선택", "naver.com","daum.com", "google.com", "java.com", "직접입력"};
		comboBox_email = new JComboBox(combo);
		comboBox_email.setBounds(444, 338, 110, 26);
		getContentPane().add(comboBox_email);
		
		JLabel lbl_anotation = new JLabel("@");
		lbl_anotation.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_anotation.setBounds(289, 338, 19, 27);
		getContentPane().add(lbl_anotation);
		
		lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(160, 196, 270, 27);
		lblNewLabel_6.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_6.setForeground(Color.GRAY);
		lblNewLabel_6.setBackground(Color.WHITE);
		getContentPane().add(lblNewLabel_6);
		
//		btn_CheckID = new JButton("\uC911\uBCF5\uD655\uC778");
//		btn_CheckID.setBounds(442, 186, 126, 30);
//		btn_CheckID.setBackground(new Color(173, 216, 230));
//		getContentPane().add(btn_CheckID);
		
//		btn_CheckPw = new JButton("비밀번호 확인");
//		btn_CheckPw.setBounds(442, 292, 126, 30);
//		btn_CheckPw.setBackground(new Color(173, 216, 230));
//		getContentPane().add(btn_CheckPw);
		
		btn_Join = new JButton("가입하기");
		btn_Join.setBounds(205, 480, 163, 30);
		btn_Join.setBackground(new Color(173, 216, 230));
		getContentPane().add(btn_Join);
		
		lbl_CheckPw = new JLabel("");
		lbl_CheckPw.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_CheckPw.setBounds(160, 302, 270, 28);
		lbl_CheckPw.setForeground(Color.GRAY);
		lbl_CheckPw.setFont(new Font("굴림", Font.BOLD, 15));
		getContentPane().add(lbl_CheckPw);

		
		mask = null;
		try {
			mask = new MaskFormatter("###-####-####");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tfphoneNum = new JFormattedTextField(mask);
		tfphoneNum.setBounds(160, 403, 270, 30);
		mask.setAllowsInvalid(false);
		mask.setCommitsOnValidEdit(true);
		mask.setPlaceholder("_");
		getContentPane().add(tfphoneNum);
		
//		setVisible(true);
		setBounds(100, 100, 617, 592);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(173, 216, 230));
		
		txtField_Email = new JTextField("이메일 선택");
		txtField_Email.setColumns(10);
		txtField_Email.setBounds(305, 337, 125, 27);
		getContentPane().add(txtField_Email);
		
		lbl_Check_email = new JLabel("");
		lbl_Check_email.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Check_email.setForeground(Color.GRAY);
		lbl_Check_email.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_Check_email.setBounds(162, 368, 268, 28);
		getContentPane().add(lbl_Check_email);
	}
	
	public String getPWCheck() {
		String pwcheck = "";
		char[] secret_pwcheck = pwField_CheckPw.getPassword();
		for(char cha : secret_pwcheck) {
			Character.toString(cha);
			pwcheck += (pwcheck.equals("")) ? "" + cha + "" : "" + cha + "";		
		}
		return pwcheck;
	}

	public String getPW() {
		String pw = "";
		char[] secret_pw = pwField_PW.getPassword();
		for(char cha : secret_pw) {
			Character.toString(cha);
			pw += (pw.equals("")) ? "" + cha + "" : "" + cha + "";		
		}
		return pw;
	}
}
