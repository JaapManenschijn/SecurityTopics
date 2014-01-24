package nl.saxion.act.security.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import nl.saxion.act.security.db.Dao;

public class LoginScherm extends JPanel {
	private JTextField textField;
	private JTextField textField_1;

	public LoginScherm(final JFrame frame) {
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

		textField_1 = new JPasswordField();
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
			public void actionPerformed(ActionEvent evt) {
				String sUserId = textField.getText().toString();
				String wachtwoord = textField_1.getText().toString();
				long userId = 0;
				try {
					userId = Long.parseLong(sUserId);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Een usernummer moet alleen uit cijfers bestaan!",
							"Ongeldige user", JOptionPane.ERROR_MESSAGE);
					return;					
				}
				if (Dao.getInstance().checkLogin(userId, wachtwoord)) {
					frame.getContentPane().removeAll();
					frame.getContentPane().add(
							new TabbedPanel(Dao.getInstance().getUser(userId),
									frame));
					frame.revalidate();
					frame.repaint();
				} else {
					JOptionPane.showMessageDialog(null,
							"De ingevoerde combinatie is onjuist.",
							"Ongeldige combinatie", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

	}
}
