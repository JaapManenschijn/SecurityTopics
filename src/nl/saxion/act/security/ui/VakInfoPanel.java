package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.saxion.act.security.model.Klas;
import nl.saxion.act.security.rbac.User;

public class VakInfoPanel extends JPanel {
	private DefaultListModel<Klas> klasLijst = new DefaultListModel<Klas>();

	public VakInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 10, 10));

		JList<Klas> list = new JList();
		list.setModel(klasLijst);
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

	}

	public void setKlassenLijst(List<Klas> klassen) {
		for (Klas klas : klassen) {
			klasLijst.addElement(klas);
		}
	}

}
