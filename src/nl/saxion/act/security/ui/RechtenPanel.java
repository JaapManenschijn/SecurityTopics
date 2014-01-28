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
import nl.saxion.act.security.rbac.Permissie;
import nl.saxion.act.security.rbac.Rol;

public class RechtenPanel extends JPanel {
	private DefaultListModel<Rol> rolLijst = new DefaultListModel<Rol>();
	private RechtenInfoPanel rechtenInfoPanel;

	public RechtenPanel() {
		setLayout(new BorderLayout(0, 0));

		JList list = new JList(rolLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < rolLijst.size() && index >= 0) {
						Rol rol = rolLijst.get(index);
						rechtenInfoPanel.setPermissieLijst(rol.getPermissies());
					}
				}
			}
		});
		List<Rol> rollen = Dao.getInstance().getAlleRollen();
		for(Rol rol : rollen){
			rolLijst.addElement(rol);
		}
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		rechtenInfoPanel = new RechtenInfoPanel();
		add(rechtenInfoPanel, BorderLayout.CENTER);
	}
}
