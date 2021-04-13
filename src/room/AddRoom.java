package room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lunch.Protocol;

public class AddRoom extends JPanel implements ActionListener{
	public String label[] = { "0방번호", "", "", "3방제목", "", "5인원수", "6개설자" };
	public JLabel labelArr[];
	public JButton btnRoom;

	private BufferedReader bReader;
	private PrintWriter pWriter;

	public AddRoom(BufferedReader bReader, PrintWriter pWriter) {
		this.bReader = bReader;
		this.pWriter = pWriter;
	}

	public void init() {
		btnRoom = new JButton();
		btnRoom.setLayout(new GridLayout(3, 3, 5, 5));
		btnRoom.setPreferredSize(new Dimension(829, 100));
		btnRoom.setBorderPainted(false);
		btnRoom.addActionListener(this);
		labelArr = new JLabel[label.length];
		
		for (int i = 0; i < label.length; i++) {
			labelArr[i] = new JLabel(label[i]);
			btnRoom.add(labelArr[i]);
		}
		add(btnRoom);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this) {
			String userCount[] = labelArr[5].getText().split("/");

			if (userCount[0].compareTo(userCount[1]) == 0) {
				JOptionPane.showMessageDialog(null, "인원수 초과");
			} else {
				String line = "";
				line += (Protocol.ENTER + ">" + labelArr[0].getText());
				pWriter.println(line);
				pWriter.flush();
			}
		}				
	}
}
