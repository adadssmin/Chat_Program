package Login;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;

import javax.swing.SwingConstants;

import lunch.MainAction;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class Login extends JFrame {

	private JLabel lbl_LoginLogo, lbl_Id, lbl_Pw;
	public JLabel lbl_Logo, lbl_hint, lbl_hint_idPwCon, lbl_hint_PwCon;
	public JButton btn_FindId, btn_FindPw, btn_Join, btn_Login, btn_find
			, btn_emailChkPw, btn_newPwChk, btn_ChangPw;
	public JTextField txtField_Ip, txtField_Port, textField_Id, txtField_emailId, txtField_Email
		, txtField_pWEmailId, txtField_pWEmail, txtField_IdPw;
	public JPanel ta_SearchId;
	public JPasswordField pwField_Pw, pwField_newPw, pwField_newPwChk;
	public JComboBox comboBox_email, comboBoxPw;
	public JDialog findID, searchID, findPw;
	private ImageIcon iconLogo;
	private Image imgLogo;
	private Image changeImg;
	private ImageIcon changeIcon;
	
	public Login() {
		// 아이디찾기
		findID = new JDialog();
		findID.setVisible(false);
		findID.setSize(567, 347);
		findID.setLocationRelativeTo(null);
		findID.setResizable(false);
		findID.setLayout(null);
		findID.setModal(true);
		findIdGUI();
		
		// 찾은 아이디
		searchID = new JDialog();
		searchID.setVisible(false);
		searchID.setSize(580, 360);
		searchID.setLocationRelativeTo(null);
		searchID.setResizable(false);
		searchID.setLayout(null);
		searchId();
		
		// 비번 찾기
		findPw = new JDialog();
		findPw.setVisible(false);
		findPw.setSize(560, 420);
		findPw.setLocationRelativeTo(null);
		findPw.setResizable(false);
		findPw.getContentPane().setLayout(null);
		findPw.setModal(true);
		FindPwGUI();
		
		//로그인
		getContentPane().setLayout(null);
		lbl_LoginLogo = new JLabel("LOGIN");
		lbl_LoginLogo.setBounds(25, 6, 125, 63);
		getContentPane().add(lbl_LoginLogo);
		lbl_LoginLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_LoginLogo.setForeground(Color.WHITE);
		lbl_LoginLogo.setHorizontalAlignment(SwingConstants.LEFT);

		txtField_Ip = new JTextField();
		txtField_Ip.setBackground(new Color(173, 216, 230));
		txtField_Ip.setHorizontalAlignment(SwingConstants.CENTER);
		txtField_Ip.setBounds(80, 196, 202, 24);
		getContentPane().add(txtField_Ip);
		txtField_Ip.setColumns(10);
		txtField_Ip.setEditable(false);

		txtField_Port = new JTextField();
		txtField_Port.setBackground(new Color(173, 216, 230));
		txtField_Port.setHorizontalAlignment(SwingConstants.CENTER);
		txtField_Port.setColumns(10);
		txtField_Port.setBounds(294, 196, 115, 24);
		txtField_Port.setEditable(false);
		getContentPane().add(txtField_Port);

		textField_Id = new JTextField();
		textField_Id.setBounds(171, 230, 238, 33);
		getContentPane().add(textField_Id);
		textField_Id.setColumns(10);

		pwField_Pw = new JPasswordField();
		pwField_Pw.setBounds(171, 280, 238, 33);
		getContentPane().add(pwField_Pw);

		btn_Login = new JButton("로그인");
		btn_Login.setBackground(new Color(173, 216, 230));
		btn_Login.setBounds(80, 347, 153, 27);
		getContentPane().add(btn_Login);

		btn_Join = new JButton("회원가입");
		btn_Join.setBackground(new Color(173, 216, 230));
		btn_Join.setBounds(248, 347, 161, 27);
		getContentPane().add(btn_Join);

		lbl_Id = new JLabel("아이디 : ");
		lbl_Id.setBounds(80, 233, 75, 26);
		getContentPane().add(lbl_Id);

		lbl_Pw = new JLabel("비밀번호 : ");
		lbl_Pw.setBounds(80, 283, 75, 26);
		getContentPane().add(lbl_Pw);

		btn_FindId = new JButton("아이디 찾기");
		btn_FindId.setForeground(Color.GRAY);
		btn_FindId.setHorizontalAlignment(SwingConstants.RIGHT);
		btn_FindId.setFont(new Font("굴림", Font.PLAIN, 13));
		btn_FindId.setBounds(136, 424, 105, 27);
		btn_FindId.setBorderPainted(false);
		btn_FindId.setContentAreaFilled(false);
		getContentPane().add(btn_FindId);

		btn_FindPw = new JButton("비밀번호 찾기");
		btn_FindPw.setForeground(Color.GRAY);
		btn_FindPw.setHorizontalAlignment(SwingConstants.LEFT);
		btn_FindPw.setFont(new Font("굴림", Font.PLAIN, 13));
		btn_FindPw.setBounds(248, 425, 125, 24);
		btn_FindPw.setBorderPainted(false);
		btn_FindPw.setContentAreaFilled(false);
		getContentPane().add(btn_FindPw);
		
		iconLogo = new ImageIcon(".\\icon\\logo.png");
		imgLogo = iconLogo.getImage();
		changeImg = imgLogo.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		changeIcon = new ImageIcon(changeImg);
		
		lbl_Logo = new JLabel(changeIcon);
		lbl_Logo.setFont(new Font("굴림", Font.BOLD, 20));
		lbl_Logo.setForeground(new Color(255, 0, 0));
		lbl_Logo.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Logo.setBounds(189, 59, 125, 103);
		getContentPane().add(lbl_Logo);
		
		setVisible(true);
		setResizable(false);
		setBounds(100, 100, 512, 518);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(173, 216, 230));
		getContentPane().setLayout(null);
	}
	
	public void findIdGUI() {
		JPanel pnl = new JPanel();
		pnl.setBackground(new Color(173, 216, 230));
		pnl.setBounds(0, 0, 567, 347);
		findID.getContentPane().add(pnl);
		pnl.setLayout(null);
		
		JLabel lbl_LoginLogo = new JLabel("Find ID");
		lbl_LoginLogo.setBounds(25, 6, 140, 78);
		pnl.add(lbl_LoginLogo);
		lbl_LoginLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_LoginLogo.setForeground(Color.WHITE);
		lbl_LoginLogo.setHorizontalAlignment(SwingConstants.LEFT);
		
		lbl_hint = new JLabel("가입 시 입력한 이메일로 아이디 힌트를 얻으세요.");
		lbl_hint.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hint.setForeground(Color.GRAY);
		lbl_hint.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_hint.setBounds(84, 135, 383, 34);
		pnl.add(lbl_hint);
		
		JLabel lbl_email = new JLabel("이메일");
		lbl_email.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_email.setBounds(35, 96, 45, 27);
		pnl.add(lbl_email);
		
		txtField_emailId = new JTextField();
		txtField_emailId.setBounds(95, 96, 104, 27);
		pnl.add(txtField_emailId);
		txtField_emailId.setColumns(10);
		
		String[] combo = {"이메일 선택", "naver.com","daum.com", "google.com", "java.com", "직접입력"};
		comboBox_email = new JComboBox(combo);
		comboBox_email.setBounds(369, 96, 155, 26);
		pnl.add(comboBox_email);
		
		btn_find = new JButton("아이디 찾기");
		btn_find.setBackground(new Color(173, 216, 230));
		btn_find.setBounds(189, 181, 140, 27);
		pnl.add(btn_find);
		
		txtField_Email = new JTextField();
		txtField_Email.setColumns(10);
		txtField_Email.setBounds(241, 96, 104, 27);
		pnl.add(txtField_Email);
		
		JLabel lbl_anotation = new JLabel("@");
		lbl_anotation.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_anotation.setBounds(213, 96, 19, 27);
		pnl.add(lbl_anotation);
	}
	
	public void searchId() {
		JPanel pnl = new JPanel();
		pnl.setBackground(new Color(173, 216, 230));
		pnl.setBounds(0, 0, 561, 312);
		searchID.getContentPane().add(pnl);
		pnl.setLayout(null);
		
		JLabel lbl_LoginLogo = new JLabel("Find ID");
		lbl_LoginLogo.setBounds(25, 6, 140, 78);
		pnl.add(lbl_LoginLogo);
		lbl_LoginLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_LoginLogo.setForeground(Color.WHITE);
		lbl_LoginLogo.setHorizontalAlignment(SwingConstants.LEFT);
		
		ta_SearchId = new JPanel();
		ta_SearchId.setLayout(new BoxLayout(ta_SearchId, BoxLayout.Y_AXIS));
//		ta_SearchId.setEditable(false);
		ta_SearchId.setFont(new Font("굴림", Font.BOLD, 20));
		ta_SearchId.setBackground(SystemColor.inactiveCaptionBorder);
		ta_SearchId.setBounds(35, 118, 490, 163);
		pnl.add(ta_SearchId);
		
		JLabel lbl_LoginLogo_1 = new JLabel("찾은 아이디");
		lbl_LoginLogo_1.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_LoginLogo_1.setForeground(new Color(119, 136, 153));
		lbl_LoginLogo_1.setFont(new Font("굴림", Font.PLAIN, 16));
		lbl_LoginLogo_1.setBounds(35, 80, 140, 42);
		pnl.add(lbl_LoginLogo_1);
	}
	
	public void FindPwGUI() {
		JPanel pnl_findPw = new JPanel();
		pnl_findPw.setBackground(new Color(173, 216, 230));
		pnl_findPw.setBounds(0, 0, 560, 380);
		findPw.getContentPane().add(pnl_findPw);
		pnl_findPw.setLayout(null);

		JLabel lbl_FindPwLogo = new JLabel("Find Password");
		lbl_FindPwLogo.setBounds(12, 0, 278, 64);
		pnl_findPw.add(lbl_FindPwLogo);
		lbl_FindPwLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_FindPwLogo.setForeground(Color.WHITE);
		lbl_FindPwLogo.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lbl_hintPw = new JLabel("가입한 아이디와 이메일을 입력하여 비밀번호를 찾을 수 있습니다.");
		lbl_hintPw.setForeground(Color.GRAY);
		lbl_hintPw.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hintPw.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_hintPw.setBounds(39, 77, 440, 15);
		pnl_findPw.add(lbl_hintPw);

		JLabel lbl_IdPw = new JLabel("아이디");
		lbl_IdPw.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_IdPw.setBounds(39, 121, 59, 23);
		pnl_findPw.add(lbl_IdPw);

		JLabel lbl_emailPw = new JLabel("이메일");
		lbl_emailPw.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_emailPw.setBounds(39, 152, 59, 23);
		pnl_findPw.add(lbl_emailPw);

		JLabel lbl_NewPw = new JLabel("새비밀번호");
		lbl_NewPw.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_NewPw.setBounds(39, 218, 81, 23);
		pnl_findPw.add(lbl_NewPw);

		JLabel lbl_NewPwChk = new JLabel("새비밀번호 확인");
		lbl_NewPwChk.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_NewPwChk.setBounds(39, 251, 111, 23);
		pnl_findPw.add(lbl_NewPwChk);

		txtField_IdPw = new JTextField();
		txtField_IdPw.setBounds(162, 122, 210, 24);
		pnl_findPw.add(txtField_IdPw);
		txtField_IdPw.setColumns(10);

		txtField_pWEmailId = new JTextField();
		txtField_pWEmailId.setColumns(10);
		txtField_pWEmailId.setBounds(162, 153, 87, 24);
		pnl_findPw.add(txtField_pWEmailId);

		txtField_pWEmail = new JTextField();
		txtField_pWEmail.setColumns(10);
		txtField_pWEmail.setBounds(274, 153, 98, 24);
		pnl_findPw.add(txtField_pWEmail);
		
		String[] combo = {"이메일 선택", "naver.com","daum.com", "google.com", "java.com", "직접입력"};
		comboBoxPw = new JComboBox(combo);
		comboBoxPw.setBounds(384, 154, 120, 21);
		pnl_findPw.add(comboBoxPw);

		pwField_newPw = new JPasswordField();
		pwField_newPw.setBounds(162, 219, 210, 24);
		pnl_findPw.add(pwField_newPw);
		pwField_newPw.setColumns(10);

		pwField_newPwChk = new JPasswordField();
		pwField_newPwChk.setColumns(10);
		pwField_newPwChk.setBounds(162, 252, 210, 24);
		pnl_findPw.add(pwField_newPwChk);

		btn_emailChkPw = new JButton("확인");
		btn_emailChkPw.setBackground(new Color(173, 216, 230));
		btn_emailChkPw.setBounds(384, 186, 120, 23);
		pnl_findPw.add(btn_emailChkPw);

		btn_newPwChk = new JButton("비밀번호 확인");
		btn_newPwChk.setBackground(new Color(173, 216, 230));
		btn_newPwChk.setBounds(384, 253, 120, 23);
		pnl_findPw.add(btn_newPwChk);

		btn_ChangPw = new JButton("비밀번호 변경");
		btn_ChangPw.setBackground(new Color(173, 216, 230));
		btn_ChangPw.setBounds(198, 320, 133, 23);
		pnl_findPw.add(btn_ChangPw);
		
		lbl_hint_idPwCon = new JLabel("아이디와 이메일을 확인해주세요");
		lbl_hint_idPwCon.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hint_idPwCon.setForeground(Color.GRAY);
		lbl_hint_idPwCon.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_hint_idPwCon.setBounds(49, 190, 440, 15);
		pnl_findPw.add(lbl_hint_idPwCon);
		
		JLabel lbl_email_anotation = new JLabel("@");
		lbl_email_anotation.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_email_anotation.setBounds(255, 154, 23, 23);
		pnl_findPw.add(lbl_email_anotation);
		
		lbl_hint_PwCon = new JLabel("");
		lbl_hint_PwCon.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hint_PwCon.setForeground(Color.GRAY);
		lbl_hint_PwCon.setFont(new Font("굴림", Font.PLAIN, 15));
		lbl_hint_PwCon.setBounds(49, 288, 440, 15);
		pnl_findPw.add(lbl_hint_PwCon);
	}
	
	public String getNewPWCheck() {
		String pwcheck = "";
		char[] secret_pwcheck = pwField_newPwChk.getPassword();
		for(char cha : secret_pwcheck) {
			Character.toString(cha);
			pwcheck += (pwcheck.equals("")) ? "" + cha + "" : "" + cha + "";		
		}
		return pwcheck;
	}

	public String getNewPW() {
		String pw = "";
		char[] secret_pw = pwField_newPw.getPassword();
		for(char cha : secret_pw) {
			Character.toString(cha);
			pw += (pw.equals("")) ? "" + cha + "" : "" + cha + "";		
		}
		return pw;
	}
}
