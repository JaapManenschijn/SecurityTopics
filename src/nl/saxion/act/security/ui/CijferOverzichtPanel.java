package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.Sessie;

public class CijferOverzichtPanel extends JPanel {
	private DefaultListModel<Vak> vakLijst = new DefaultListModel<Vak>();
	private CijferPanel cijferPanel;

	public CijferOverzichtPanel() {
		setLayout(new BorderLayout(0, 0));

		JList list = new JList(vakLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < vakLijst.size() && index >= 0) {
						Vak vak = vakLijst.get(index);
						double cijfer = Dao.getInstance()
								.getCijferVanStudentVanVak(vak.getId(),
										Sessie.getIngelogdeGebruiker().getId());
						cijferPanel.setVak(vak.getNaam());
						cijferPanel.setCijfer("" + cijfer);
						cijferPanel.setDocent(vak.getDocent().getNaam());
					}
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		cijferPanel = new CijferPanel();
		add(cijferPanel, BorderLayout.CENTER);

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
