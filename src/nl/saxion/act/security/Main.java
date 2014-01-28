package nl.saxion.act.security;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import nl.saxion.act.security.db.DBBuilder;
import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.ui.LoginScherm;

public class Main {

	private JFrame frame;

	public Main() {
		frame = new JFrame();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocus();
	}

	public void start() {
		frame.getContentPane().add(new LoginScherm(frame), BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}

	public static void main(String[] args) {
		Main main = new Main();
		initApplication();
		main.start();
	}

	private static void initApplication() {
		try {
			DBBuilder.getInstance().createDatabase();
		} catch (Exception e) {
			DBBuilder.getInstance().cleanDatabase();
			DBBuilder.getInstance().createDatabase();
		}

		Dao dao = Dao.getInstance();
		dao.addUser("superuser", "superuser");
		dao.addUser("docent", "docent");
		dao.addUser("leerling", "leerling");

		dao.addRol("beheerder");
		dao.addRol("docent");
		dao.addRol("student");

		dao.setRolBijUser(1, 1);
		dao.setRolBijUser(2, 2);
		dao.setRolBijUser(3, 3);

		dao.addKlas("Klas1");
		dao.addLeerlingAanKlas(3, 1);
		dao.addVak("Minor Sec", 2);
		dao.addKlasAanVak(1, 1);

		dao.addPermissie("INZIENEIGENSTUDENTEN");
		dao.setPermissieBijRol(2, 1);

		dao.setPermissieMap();
	}

}
