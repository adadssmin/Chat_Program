import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Test extends JDialog {

	private JLabel lbl_LoginLogo, lbl_Id, lbl_Pw;
	public JLabel lbl_Logo, lbl_hint_idPwCon, lbl_hint_PwCon;
	public JButton btn_FindId, btn_FindPw, btn_Join, btn_Login, btn_find
			, btn_emailChkPw, btn_newPwChk, btn_ChangPw;
	public JTextField txtField_Ip, txtField_Port, textField_Id, txtField_emailId
		, txtField_pWEmailId, txtField_pWEmail, txtField_IdPw;
	public JPanel ta_SearchId;
	public JPasswordField pwField_Pw, pwField_newPw, pwField_newPwChk;
	public JComboBox comboBoxPw;
	public JDialog findID, searchID, findPw;
	private ImageIcon iconLogo;
	private Image imgLogo;
	private Image changeImg;
	private ImageIcon changeIcon;
	
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Test dialog = new Test();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Test() {
		JPanel pnl = new JPanel();
		pnl.setBackground(new Color(173, 216, 230));
		pnl.setBounds(0, 0, 486, 273);
		findID.getContentPane().add(pnl);
		pnl.setLayout(null);
		
		JLabel lbl_LoginLogo = new JLabel("Password");
		lbl_LoginLogo.setBounds(25, 6, 177, 78);
		pnl.add(lbl_LoginLogo);
		lbl_LoginLogo.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_LoginLogo.setForeground(Color.WHITE);
		lbl_LoginLogo.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lbl_email = new JLabel("비밀번호");
		lbl_email.setFont(new Font("굴림", Font.BOLD, 15));
		lbl_email.setBounds(62, 122, 67, 27);
		pnl.add(lbl_email);
		
		txtField_emailId = new JTextField();
		txtField_emailId.setBounds(144, 122, 232, 27);
		pnl.add(txtField_emailId);
		txtField_emailId.setColumns(10);
		
		btn_find = new JButton("입장");
		btn_find.setBackground(new Color(173, 216, 230));
		btn_find.setBounds(184, 201, 111, 27);
		pnl.add(btn_find);
	}

}
