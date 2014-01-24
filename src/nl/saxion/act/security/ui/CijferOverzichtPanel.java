package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.Sessie;

public class CijferOverzichtPanel extends JPanel {
	private DefaultListModel vakLijst = new DefaultListModel();

	public CijferOverzichtPanel() {
		setLayout(new BorderLayout(0, 0));

		JList list = new JList(vakLijst);
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);

		if (Sessie.getIngelogdeGebruiker().isStudent()) {
			List<Vak> vakken = Dao.getInstance().getVakkenVanStudent(
					Sessie.getIngelogdeGebruiker().getId());
			for (Vak vak : vakken) {
				vakLijst.addElement(vak);
			}
		}

		// TODO permissies
		if (Sessie.getIngelogdeGebruiker().isDocent()
				|| Sessie.getIngelogdeGebruiker().isSuperUser()) {
			JComboBox comboBox = new JComboBox();
			add(comboBox, BorderLayout.NORTH);
		}

	}
}
