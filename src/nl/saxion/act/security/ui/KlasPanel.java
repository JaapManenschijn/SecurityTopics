package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.model.Klas;
import nl.saxion.act.security.rbac.Sessie;

public class KlasPanel extends JPanel {
	private DefaultListModel<Klas> klasLijst = new DefaultListModel<Klas>();
	private KlasInfoPanel klasInfoPanel;

	public KlasPanel() {
		setLayout(new BorderLayout(0, 0));

		JList<Klas> list = new JList<Klas>(klasLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Klas> list = (JList<Klas>) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < klasLijst.size() && index >= 0) {
						Klas klas = klasLijst.get(index);
						klasInfoPanel.setLeerlingenLijst(klas.getLeerlingen());
					}
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		klasInfoPanel = new KlasInfoPanel();
		add(klasInfoPanel, BorderLayout.CENTER);

		if (Sessie.getIngelogdeGebruiker().isDocent()) {
			List<Klas> klassen = Dao.getInstance().getKlassenVanDocent(
					Sessie.getIngelogdeGebruiker().getId());
			for (Klas klas : klassen) {
				klasLijst.addElement(klas);
			}
		}
	}
}
