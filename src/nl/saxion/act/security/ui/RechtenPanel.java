package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.rbac.Permissie;
import nl.saxion.act.security.rbac.Rol;

public class RechtenPanel extends RefreshPanel {
	private DefaultListModel<Rol> rolLijst = new DefaultListModel<Rol>();
	private RechtenInfoPanel rechtenInfoPanel;

	public RechtenPanel() {
		setLayout(new BorderLayout(0, 0));

		final JList list = new JList(rolLijst);
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

		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		rechtenInfoPanel = new RechtenInfoPanel();
		add(rechtenInfoPanel, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		JButton addPermissie = new JButton("Permissie toevoegen");
		JButton verwijderPermissie = new JButton("Permisse verwijderen");
		buttons.add(addPermissie);
		buttons.add(verwijderPermissie);
		add(buttons, BorderLayout.SOUTH);

		addPermissie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Rol selectedRol = rolLijst.get(list.getSelectedIndex());
				JLabel permissie = new JLabel("Selecteer de juiste permissie");
				JComboBox permissies = new JComboBox();
				for (Permissie p : Dao.getInstance().getPermissies()) {
					permissies.addItem(p);
				}

				final JComponent[] inputs = new JComponent[] { permissie,
						permissies };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Permissie toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Permissie selectedPerm = (Permissie) permissies
							.getSelectedItem();
					if (selectedRol.getPermissies().contains(selectedPerm)) {
						JOptionPane.showMessageDialog(null,
								"De rol heeft deze permissie al!", "Fout",
								JOptionPane.ERROR_MESSAGE);
					} else {
						Dao.getInstance().setPermissieBijRol(
								selectedRol.getId(), selectedPerm.getId());
						refreshPanel();
					}
				}
			}

		});

		verwijderPermissie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Rol selectedRol = rolLijst.get(list.getSelectedIndex());
				Permissie selectedPermissie = rechtenInfoPanel
						.getSelectedPermissie();
				int result = JOptionPane.showConfirmDialog(null, "Wil je "
						+ selectedPermissie.getNaam() + " verwijderen uit "
						+ selectedRol.getNaam() + "?", null,
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().verwijderPermissieBijRol(
							selectedPermissie.getId(), selectedRol.getId());

				}
			}

		});
	}

	public void refreshPanel() {
		rolLijst.clear();
		List<Rol> rollen = Dao.getInstance().getAlleRollen();
		for (Rol rol : rollen) {
			rolLijst.addElement(rol);
		}
		rechtenInfoPanel.clear();
	}
}
