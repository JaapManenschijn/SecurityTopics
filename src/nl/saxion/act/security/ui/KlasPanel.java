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
import nl.saxion.act.security.rbac.PermissieHelper;
import nl.saxion.act.security.rbac.Sessie;
import nl.saxion.act.security.rbac.User;

public class KlasPanel extends RefreshPanel {
	private DefaultListModel<Klas> klasLijst = new DefaultListModel<Klas>();
	private KlasInfoPanel klasInfoPanel;

	public KlasPanel() {
		setLayout(new BorderLayout(0, 0));

		final JList<Klas> list = new JList<Klas>(klasLijst);
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

		JPanel panel = new JPanel();
		JButton addLeerling = new JButton("Voeg leerling toe aan klas");
		JButton verwijderLeerling = new JButton("Verwijder leerling uit klas");
		JButton voegKlasToe = new JButton("Voeg klas toe");
		panel.add(voegKlasToe);
		panel.add(addLeerling);
		panel.add(verwijderLeerling);
		verwijderLeerling.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Klas klas = klasLijst.get(list.getSelectedIndex());
				User student = klasInfoPanel.getSelectedLeerling();
				int result = JOptionPane.showConfirmDialog(null,
						"Wil je " + student.getNaam() + " verwijderen uit "
								+ klas.getNaam() + "?", null,
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().verwijderLeerlingUitKlas(student.getId(),
							klas.getId());
					klas.getLeerlingen().remove(student);
					int index = list.getSelectedIndex();
					if (index < klasLijst.size() && index >= 0) {
						Klas klasSelected = klasLijst.get(index);
						klasInfoPanel.setLeerlingenLijst(klasSelected
								.getLeerlingen());
						klasInfoPanel.repaint();
					}
				}
			}
		});

		addLeerling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox studenten = new JComboBox();
				for (User stud : Dao.getInstance().getStudenten()) {
					studenten.addItem(stud);
				}
				JLabel selecteer = new JLabel(
						"Selecteer een leerling en klik op Ok");
				final JComponent[] inputs = new JComponent[] { selecteer,
						studenten };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Leerling toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					User selectedStudent = (User) studenten.getSelectedItem();
					int index = list.getSelectedIndex();
					if (index < klasLijst.size() && index >= 0) {
						Klas klasSelected = klasLijst.get(index);
						if (!klasSelected.getLeerlingen().contains(
								selectedStudent)) {
							klasSelected.getLeerlingen().add(selectedStudent);
							klasInfoPanel.setLeerlingenLijst(klasSelected
									.getLeerlingen());
							klasInfoPanel.repaint();
							Dao.getInstance().addLeerlingAanKlas(
									selectedStudent.getId(),
									klasSelected.getId());
						} else {
							JOptionPane.showMessageDialog(null,
									"Leerling zit al in deze klas!", "Fout",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}

		});
		voegKlasToe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTextField naam = new JTextField();
				JLabel vulNaam = new JLabel("Vul de klasnaam in");
				final JComponent[] inputs = new JComponent[] { vulNaam, naam };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Klas toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().addKlas(naam.getText());
				}
			}

		});
		add(panel, BorderLayout.SOUTH);
	}

	public void refreshPanel() {
		klasLijst.clear();
		if (Sessie.getIngelogdeGebruiker().heeftPermissie(
				PermissieHelper.permissies.get("BEHEERALLEKLASSEN"))) {
			List<Klas> klassen = Dao.getInstance().getKlassen();
			for (Klas klas : klassen) {
				klasLijst.addElement(klas);
			}
		} else if (Sessie.getIngelogdeGebruiker().heeftPermissie(
				PermissieHelper.permissies.get("BEHEEREIGENKLASSEN"))) {
			List<Klas> klassen = Dao.getInstance().getKlassenVanDocent(
					Sessie.getIngelogdeGebruiker().getId());
			for (Klas klas : klassen) {
				klasLijst.addElement(klas);
			}
		}
		klasInfoPanel.clear();
	}
}
