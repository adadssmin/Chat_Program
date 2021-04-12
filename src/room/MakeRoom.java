package room;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MakeRoom extends JFrame {

	private JLabel title, subject, password, numberOfUser;
	public JTextField tfId;
	public JPasswordField pfPassword;
	public JCheckBox cb;
	public JButton makeBtn, cancelBtn;
	public JComboBox<String> combo1;

	public MakeRoom() {
		title = new JLabel("방이름");
		password = new JLabel("비밀번호");
		numberOfUser = new JLabel("인원");

		tfId = new JTextField(15);
		pfPassword = new JPasswordField(15);
		pfPassword.setEditable(false);

		cb = new JCheckBox();
		String[] com1 = { "3", "4", "5", "6", "7", "8", "9", };
		combo1 = new JComboBox<String>(com1);

		makeBtn = new JButton("만들기");
		cancelBtn = new JButton("취소");

		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(title);
		panel1.add(tfId);

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel2.add(password);
		panel2.add(pfPassword);
		panel2.add(cb);

		JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel4.add(numberOfUser);
		panel4.add(combo1);

		JPanel totpanel = new JPanel(new GridLayout(4, 1, 0, 0));
		totpanel.add(panel1);
		totpanel.add(panel2);
		totpanel.add(panel4);

		JPanel btpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btpanel.add(makeBtn);
		btpanel.add(cancelBtn);

		Container c = this.getContentPane();
		c.add("Center", totpanel);
		c.add("South", btpanel);

		setResizable(false);
		setBounds(400, 200, 400, 300);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(false);
	}
}