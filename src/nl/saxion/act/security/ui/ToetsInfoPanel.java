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
	private JList<Toets> list;

	public ToetsInfoPanel() {
		setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 10, 0, 10));

		list = new JList<Toets>();
		list.setModel(toetsLijst);
		add(list, BorderLayout.CENTER);

	}

	public void setToetsenLijst(List<Toets> toetsen) {
		toetsLijst.clear();
		for (Toets toets : toetsen) {
			toetsLijst.addElement(toets);
		}
		repaint();
	}

	public Toets getSelectedToets() {
		return toetsLijst.get(list.getSelectedIndex());
	}

	public void clear() {
		toetsLijst.clear();
	}

}
