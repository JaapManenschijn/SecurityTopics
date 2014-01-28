package nl.saxion.act.security.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserInfoPanel extends JPanel {

	private JLabel naam;
	private JLabel rol;

	public UserInfoPanel() {
		setLayout(null);

		JLabel lblCijfer = new JLabel("Naam: ");
		lblCijfer.setBounds(159, 23, 61, 16);
		add(lblCijfer);

		naam = new JLabel();
		naam.setBounds(232, 23, 179, 16);
		add(naam);

		JLabel lblNewLabel = new JLabel("Rol(len):");
		lblNewLabel.setBounds(159, 84, 61, 16);
		add(lblNewLabel);

		rol = new JLabel();
		rol.setBounds(232, 84, 179, 16);
		add(rol);
	}

	public void setNaam(String naam) {
		this.naam.setText(naam);
	}

	public void setRol(String rol) {
		this.rol.setText(rol);
	}

	public void clear() {
		this.naam.setText("");
		this.rol.setText("");
	}
}
