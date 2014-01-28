package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.PermissieHelper;
import nl.saxion.act.security.rbac.Sessie;
import nl.saxion.act.security.rbac.User;

public class UserPanel extends JPanel {
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
				} else{
					userLijst.clear();
					userInfoPanel.clear();
				}
			}
		});
		add(comboBox, BorderLayout.NORTH);
		comboBox.addItem("Selecteer een gebruikersgroep...");
		comboBox.addItem("Docenten");
		comboBox.addItem("Studenten");
	}
}
