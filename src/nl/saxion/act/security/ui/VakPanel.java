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
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.Sessie;

public class VakPanel extends JPanel {
	private DefaultListModel<Vak> vakLijst = new DefaultListModel<Vak>();
	private VakInfoPanel vakInfoPanel;

	public VakPanel() {
		setLayout(new BorderLayout(0, 0));

		JList<Vak> list = new JList<Vak>(vakLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Vak> list = (JList<Vak>) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < vakLijst.size() && index >= 0) {
						Vak vak = vakLijst.get(index);
//						List<Klas> klassen = Dao.getInstance().get
//						vakLijst.setLeerlingenLijst();
					}
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		vakInfoPanel = new VakInfoPanel();
		add(vakInfoPanel, BorderLayout.CENTER);

		if (Sessie.getIngelogdeGebruiker().isDocent()) {
//			List<Vak> vakken = Dao.getInstance().getVakkenVanDocent(Sessie.getIngelogdeGebruiker().getId());
//			for (Vak vak : vakken) {
//				vakLijst.addElement(vak);
//			}
		}
	}
}
