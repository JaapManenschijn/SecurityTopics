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
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.Sessie;

public class VakPanel extends JPanel {
	private DefaultListModel<Vak> vakLijst = new DefaultListModel<Vak>();
	private VakInfoPanel vakInfoPanel;
	private JList<Vak> list;

	public VakPanel() {
		setLayout(new BorderLayout(0, 0));

		list = new JList<Vak>(vakLijst);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Vak> list = (JList<Vak>) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					if (index < vakLijst.size() && index >= 0) {
						Vak vak = vakLijst.get(index);
						List<Klas> klassen = Dao.getInstance()
								.getKlassenVanVak(vak.getId());
						vakInfoPanel.setKlassenLijst(klassen);
					}
				}
			}
		});
		add(list, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(250, 500));
		vakInfoPanel = new VakInfoPanel();
		add(vakInfoPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		JButton vakToevoegen = new JButton("Voeg vak toe");
		JButton verwijderVak = new JButton("Verwijder vak");
		JButton klasToevoegen = new JButton("Voeg klas toe aan vak");
		JButton klasVerwijderen = new JButton("Verwijder klas van vak");
		panel.add(vakToevoegen);
		panel.add(verwijderVak);
		panel.add(klasToevoegen);
		panel.add(klasVerwijderen);
		vulVakkenlijst();
		vakToevoegen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTextField naam = new JTextField();
				JLabel vulNaam = new JLabel("Hoe heet het vak?");
				final JComponent[] inputs = new JComponent[] { vulNaam, naam };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Vak toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().addVak(naam.getText(),
							Sessie.getIngelogdeGebruiker().getId());
					vulVakkenlijst();
				}
			}

		});
		verwijderVak.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Vak selected = vakLijst.get(list.getSelectedIndex());
				int result = JOptionPane.showConfirmDialog(null, "Wil je "
						+ selected.getNaam() + " verwijderen?", null,
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().verwijderVak(selected.getId());
					vulVakkenlijst();
				}
			}

		});

		klasToevoegen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox klassen = new JComboBox();
				for (Klas klas : Dao.getInstance().getKlassen()) {
					klassen.addItem(klas);
				}
				JLabel selecteer = new JLabel(
						"Selecteer een klas en klik op Ok");
				final JComponent[] inputs = new JComponent[] { selecteer,
						klassen };
				int result = JOptionPane.showConfirmDialog(null, inputs,
						"Klas toevoegen", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Klas selectedKlas = (Klas) klassen.getSelectedItem();
					int index = list.getSelectedIndex();
					if (index < vakLijst.size() && index >= 0) {
						Vak vakSelected = vakLijst.get(index);
						if (!selectedKlas.getVakken().contains(vakSelected)) {
							selectedKlas.getVakken().add(vakSelected);

							Dao.getInstance().addKlasAanVak(
									selectedKlas.getId(), vakSelected.getId());
							vakInfoPanel.setKlassenLijst(Dao.getInstance()
									.getKlassenVanVak(vakSelected.getId()));
						} else {
							JOptionPane.showMessageDialog(null,
									"Klas is al gekoppeld aan dit vak!",
									"Fout", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}

		});

		klasVerwijderen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Klas klas = vakInfoPanel.getSelectedKlas();
				Vak vak = vakLijst.get(list.getSelectedIndex());
				int result = JOptionPane.showConfirmDialog(null, "Wil je "
						+ klas.getNaam() + " verwijderen uit " + vak.getNaam()
						+ "?", null, JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Dao.getInstance().verwijderKlasVanVak(klas.getId(),
							vak.getId());
					klas.getVakken().remove(vak);
					vakInfoPanel.setKlassenLijst(Dao.getInstance()
							.getKlassenVanVak(vak.getId()));
				}
			}

		});
	}

	private void vulVakkenlijst() {
		if (Sessie.getIngelogdeGebruiker().isDocent()) {
			List<Vak> vakken = Dao.getInstance().getVakkenVanDocent(
					Sessie.getIngelogdeGebruiker().getId());
			vakLijst.clear();
			for (Vak vak : vakken) {
				vakLijst.addElement(vak);
			}
		}
	}
}
