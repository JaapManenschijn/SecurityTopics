package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.saxion.act.security.rbac.Permissie;

public class RechtenInfoPanel extends JPanel {
	private DefaultListModel<Permissie> permissieLijst = new DefaultListModel<Permissie>();

	public RechtenInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 10, 10));

		JList<Permissie> list = new JList<Permissie>();
		list.setModel(permissieLijst);
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

	}

	public void setPermissieLijst(List<Permissie> permissies) {
		permissieLijst.clear();
		for (Permissie permissie : permissies) {
			permissieLijst.addElement(permissie);
		}
	}

}
