package nl.saxion.act.security.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import nl.saxion.act.security.rbac.User;

public class TabbedPanel extends JPanel {

	public TabbedPanel(User user) {
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		CijferOverzichtPanel coPanel = new CijferOverzichtPanel();
		tabbedPane.addTab("Cijfer overzicht", coPanel);

		KlasPanel klasPanel = new KlasPanel();
		tabbedPane.addTab("Klas overzicht", klasPanel);

		VakPanel vakPanel = new VakPanel();
		tabbedPane.addTab("Vak overzicht", vakPanel);

		ToetsPanel toetsPanel = new ToetsPanel();
		tabbedPane.addTab("Toets overzicht", toetsPanel);

		DocentPanel docentPanel = new DocentPanel();
		tabbedPane.addTab("Docent overzicht", docentPanel);

		RechtenPanel rechtenPanel = new RechtenPanel();
		tabbedPane.addTab("Rechten & Permissies", rechtenPanel);

		tabbedPane.setUI(new BasicTabbedPaneUI() {
			@Override
			protected int calculateTabHeight(int tabPlacement, int tabIndex,
					int fontHeight) {
				return 35;
			}
		});
		JLabel naamLabel = new JLabel("Welkom " + user.getNaam(),
				SwingConstants.RIGHT);
		naamLabel.setBorder(new EmptyBorder(10, 5, 5, 20));
		add(naamLabel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
	}

}
