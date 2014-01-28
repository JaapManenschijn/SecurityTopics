package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.saxion.act.security.model.Klas;

public class VakInfoPanel extends JPanel {
	private DefaultListModel<Klas> klasLijst = new DefaultListModel<Klas>();
	private JList<Klas> list;

	public VakInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 10, 10));

		list = new JList<Klas>();
		list.setModel(klasLijst);
		add(list, BorderLayout.CENTER);
	}

	public void setKlassenLijst(List<Klas> klassen) {
		klasLijst.clear();
		for (Klas klas : klassen) {
			klasLijst.addElement(klas);
		}
	}

	public Klas getSelectedKlas() {
		return klasLijst.get(list.getSelectedIndex());
	}
}
