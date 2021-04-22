package room;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;

public class AddUser extends JButton{
	
	private BufferedReader bReader;
	private PrintWriter pWriter;
	
	public AddUser(BufferedReader bReader, PrintWriter pWriter) {
		this.bReader = bReader;
		this.pWriter = pWriter;
	}
	
	public void init() {
		this.setBackground(new Color(245, 245, 245));
		this.setPreferredSize(new Dimension(243, 50));
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
	}
}
