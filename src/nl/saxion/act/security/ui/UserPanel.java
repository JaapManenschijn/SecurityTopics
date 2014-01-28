package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.rbac.User;

public class UserPanel extends RefreshPanel {
	private DefaultListModel<User> userLijst = new DefaultListModel<User>();
	private UserInfoPanel userInfoPanel;

	public UserPanel() {
		setLayout(new BorderLayout(0, 0));

		JList list = new JList(userLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < userLijst.size() && index >= 0) {
						User user = userLijst.get(index);
						userInfoPanel.setNaam(user.getNaam());
						userInfoPanel.setRol(user.getRollen().toString());
					}
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		userInfoPanel = new UserInfoPanel();
		add(userInfoPanel, BorderLayout.CENTER);

		final JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedItem().equals("Docenten")) {
					userLijst.clear();
					List<User> users = Dao.getInstance().getUsers(2);
					for (User user : users) {
						userLijst.addElement(user);
					}
				} else if (comboBox.getSelectedItem().equals("Studenten")) {
					userLijst.clear();
					List<User> users = Dao.getInstance().getUsers(3);
					for (User user : users) {
						userLijst.addElement(user);
					}
				} else {
					userLijst.clear();
					userInfoPanel.clear();
				}
			}
		});
		add(comboBox, BorderLayout.NORTH);
		comboBox.addItem("Selecteer een gebruikersgroep...");
		comboBox.addItem("Docenten");
		comboBox.addItem("Studenten");

		JPanel buttons = new JPanel();
		add(buttons, BorderLayout.SOUTH);
		JButton addUser = new JButton("Gebruiker toevoegen");
		buttons.add(addUser);

		addUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JLabel selecteer = new JLabel(
						"Selecteer een rol, vul naam en wachtwoord in en klik op Ok");
				JLabel naam = new JLabel("Naam:");
				JTextField naamVeld = new JTextField();
				JLabel ww = new JLabel("Wachtwoord:");
				JPasswordField wwVeld = new JPasswordField();
				JComboBox rol = new JComboBox();
				rol.addItem("Docent");
				rol.addItem("Student");

				final JComponent[] inputs = new JComponent[] { selecteer, rol,
						naam, naamVeld, ww, wwVeld };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Gebruiker toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					String pass = new String(wwVeld.getPassword());
					long id = Dao.getInstance().addUser(naamVeld.getText(),
							pass);
					if (((String) rol.getSelectedItem()).equals("Docent")) {
						Dao.getInstance().setRolBijUser(id, 2);
					} else if (((String) rol.getSelectedItem())
							.equals("Student")) {
						Dao.getInstance().setRolBijUser(id, 3);
					}
					refreshPanel();
				}
			}

		});
	}

	public void refreshPanel() {
		userLijst.clear();
		userInfoPanel.clear();
	}

}
