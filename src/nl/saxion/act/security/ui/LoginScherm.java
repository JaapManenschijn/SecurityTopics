package nl.saxion.act.security.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginScherm extends JPanel {
	private JTextField textField;
	private JTextField textField_1;

	public LoginScherm() {
		setLayout(null);

		JLabel lblLoginCijfersysteem = new JLabel("Login Cijfersysteem");
		lblLoginCijfersysteem.setBounds(152, 13, 154, 22);
		lblLoginCijfersysteem.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblLoginCijfersysteem);

		textField = new JTextField();
		textField.setBounds(235, 83, 154, 22);
		add(textField);
		textField.setColumns(10);

		JLabel lblUserid = new JLabel("User (id)");
		lblUserid.setBounds(60, 86, 56, 16);
		add(lblUserid);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(235, 118, 154, 22);
		add(textField_1);

		JLabel lblWachtwoord = new JLabel("Wachtwoord");
		lblWachtwoord.setBounds(60, 121, 103, 16);
		add(lblWachtwoord);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(292, 189, 97, 25);
		add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Valideer input en ga naar volgende scherm,
				// geef melding bij foute gegevens
			}

		});

	}
}
