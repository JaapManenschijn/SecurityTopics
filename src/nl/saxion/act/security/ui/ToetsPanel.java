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
import javax.swing.JTextField;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.model.Klas;
import nl.saxion.act.security.model.Toets;
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.PermissieHelper;
import nl.saxion.act.security.rbac.Sessie;
import nl.saxion.act.security.rbac.User;

public class ToetsPanel extends RefreshPanel {
	private DefaultListModel<Vak> vakLijst = new DefaultListModel<Vak>();
	private ToetsInfoPanel toetsInfoPanel;
	private final JList<Vak> list;

	public ToetsPanel() {
		setLayout(new BorderLayout(0, 0));

		list = new JList<Vak>(vakLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Vak> list = (JList<Vak>) evt.getSource();
				if (evt.getClickCount() == 1) {
					vulToetslijst();
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		toetsInfoPanel = new ToetsInfoPanel();
		add(toetsInfoPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		JButton toetsToevoegen = new JButton("Nieuwe toets toevoegen");
		JButton resultaat = new JButton("Toetsresultaat toevoegen");
		panel.add(toetsToevoegen);
		panel.add(resultaat);

		toetsToevoegen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vak selected = vakLijst.get(list.getSelectedIndex());
				JTextField naam = new JTextField();
				JLabel vulNaam = new JLabel("Vul de toetsnaam in");
				final JComponent[] inputs = new JComponent[] { vulNaam, naam };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Toets toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance()
							.addToets(selected.getId(), naam.getText());
					vulToetslijst();
				}

			}

		});

		resultaat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Toets selectedToets = toetsInfoPanel.getSelectedToets();
				Vak selectedVak = vakLijst.get(list.getSelectedIndex());

				JComboBox leerlingen = new JComboBox();
				for (Klas klas : Dao.getInstance().getKlassenVanVak(
						selectedVak.getId())) {
					for (User student : klas.getLeerlingen()) {
						leerlingen.addItem(student);
					}
				}
				JLabel selecteer = new JLabel(
						"Selecteer een leerling, vul een cijfer in en klik op Ok");
				JLabel cijfer = new JLabel("Cijfer");
				JComboBox cijferVeld = new JComboBox();
				for (int i = 1; i < 11; i++) {
					cijferVeld.addItem(i);
				}
				final JComponent[] inputs = new JComponent[] { selecteer,
						leerlingen, cijfer, cijferVeld };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Cijfer toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					User student = (User) leerlingen.getSelectedItem();
					int gegevenCijfer = (int) cijferVeld.getSelectedItem();
					if (selectedToets.heeftCijfer(student)) {
						Dao.getInstance().updateCijferBijStudent(
								selectedToets.getId(), student.getId(),
								gegevenCijfer);
					} else {
						Dao.getInstance().addCijferBijStudent(
								selectedToets.getId(), student.getId(),
								gegevenCijfer);
					}
					selectedToets.addUitslag(student, gegevenCijfer);

				}
			}

		});
	}

	private void vulToetslijst() {
		int index = list.getSelectedIndex();
		if (index < vakLijst.size() && index >= 0) {
			Vak vak = vakLijst.get(index);
			List<Toets> toetsen = Dao.getInstance().getToetsenVanVak(
					vak.getId());
			toetsInfoPanel.setToetsenLijst(toetsen);
		}
	}

	public void refreshPanel() {
		vakLijst.clear();
		if (Sessie.getIngelogdeGebruiker().heeftPermissie(
				PermissieHelper.permissies.get("BEHEERALLEKLASSEN"))) {
			List<Vak> vakken = Dao.getInstance().getVakken();
			for (Vak vak : vakken) {
				vakLijst.addElement(vak);
			}
		} else if (Sessie.getIngelogdeGebruiker().heeftPermissie(
				PermissieHelper.permissies.get("BEHEEREIGENKLASSEN"))) {
			List<Vak> vakken = Dao.getInstance().getVakkenVanDocent(
					Sessie.getIngelogdeGebruiker().getId());
			for (Vak vak : vakken) {
				vakLijst.addElement(vak);
			}
		}
		toetsInfoPanel.clear();
	}

}
