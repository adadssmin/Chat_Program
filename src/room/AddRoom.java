package room;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import lunch.Protocol;

public class AddRoom extends JButton {
	public String label[] = { "0방번호", "", "", "3방제목", "", "5인원수", "6개설자" };
	public JLabel labelArr[];

	private PrintWriter pWriter;

	public AddRoom(PrintWriter pWriter) {
		this.pWriter = pWriter;
	}

	public void init() {
		this.setLayout(new GridLayout(3, 3, 1, 1));
		labelArr = new JLabel[label.length];

		for (int i = 0; i < label.length; i++) {
			labelArr[i] = new JLabel(label[i]);
			this.add(labelArr[i]);
		}

		ActionListener roomListener = new ActionListener() {
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
		};
		this.addActionListener(roomListener);
	}
}
