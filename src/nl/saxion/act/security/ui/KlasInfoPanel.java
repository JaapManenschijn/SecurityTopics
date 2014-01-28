package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.saxion.act.security.rbac.User;

public class KlasInfoPanel extends JPanel {
	private DefaultListModel<User> leerlingLijst = new DefaultListModel<User>();
	private JList<User> list;

	public KlasInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 0, 10));

		list = new JList();
		list.setModel(leerlingLijst);
		add(list, BorderLayout.CENTER);

	}

	public void setLeerlingenLijst(List<User> leerlingen) {
		leerlingLijst.clear();
		for (User leerling : leerlingen) {
			leerlingLijst.addElement(leerling);
		}
	}

	public User getSelectedLeerling() {
		int index = list.getSelectedIndex();
		return leerlingLijst.get(index);
	}
}
