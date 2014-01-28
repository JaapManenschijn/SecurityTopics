package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nl.saxion.act.security.model.Toets;

public class ToetsInfoPanel extends JPanel {
	private DefaultListModel<Toets> toetsLijst = new DefaultListModel<Toets>();

	public ToetsInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 10, 10));

		JList<Toets> list = new JList<Toets>();
		list.setModel(toetsLijst);
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

	}

	public void setToetsenLijst(List<Toets> toetsen) {
		toetsLijst.clear();
		for (Toets toets : toetsen) {
			toetsLijst.addElement(toets);
		}
	}

}
