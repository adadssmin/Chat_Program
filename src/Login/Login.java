package Login;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Login extends JFrame {

	private JLabel lbl_LoginLogo, lbl_Id, lbl_Pw;
	public JButton btn_FindId, btn_FindPw, btn_Join, btn_Login;
	public JTextField txtField_Ip, txtField_Port, textField_Id;
	public JPasswordField pwField_Pw;
	private JLabel lbl_Logo;
	private ImageIcon iconLogo;
	private Image imgLogo;
	private Image changeImg;
	private ImageIcon changeIcon;
	
	public Login() {
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
		txtField_Ip.setText("ip");
		txtField_Ip.setBounds(80, 196, 202, 24);
		getContentPane().add(txtField_Ip);
		txtField_Ip.setColumns(10);

		txtField_Port = new JTextField();
		txtField_Port.setBackground(new Color(173, 216, 230));
		txtField_Port.setHorizontalAlignment(SwingConstants.CENTER);
		txtField_Port.setText("port");
		txtField_Port.setColumns(10);
		txtField_Port.setBounds(294, 196, 115, 24);
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
}
