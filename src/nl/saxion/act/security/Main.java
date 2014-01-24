package nl.saxion.act.security;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import nl.saxion.act.security.rbac.User;
import nl.saxion.act.security.ui.TabbedPanel;

public class Main {

	private JFrame frame;
	
	public Main(){
		frame = new JFrame();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocus();
	}
	
	public void start(){
		frame.getContentPane().add(new TabbedPanel(new User(1, "Testaccount")),BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}

}
