package nl.saxion.act.security.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import nl.saxion.act.security.rbac.Sessie;

public class TabbedPanel extends JPanel {

	private JFrame frame;

	public TabbedPanel(final JFrame frame) {
		setLayout(new BorderLayout());
		this.frame = frame;

		final JTabbedPane tabbedPane = new JTabbedPane();

		CijferOverzichtPanel coPanel = new CijferOverzichtPanel();
		tabbedPane.addTab("Cijfer overzicht", coPanel);

		if (Sessie.getIngelogdeGebruiker().isDocent()
				|| Sessie.getIngelogdeGebruiker().isSuperUser()) {

			KlasPanel klasPanel = new KlasPanel();
			tabbedPane.addTab("Klas overzicht", klasPanel);

			VakPanel vakPanel = new VakPanel();
			tabbedPane.addTab("Vak overzicht", vakPanel);

			ToetsPanel toetsPanel = new ToetsPanel();
			tabbedPane.addTab("Toets overzicht", toetsPanel);
		}

		if (Sessie.getIngelogdeGebruiker().isSuperUser()) {
			UserPanel docentPanel = new UserPanel();
			tabbedPane.addTab("User overzicht", docentPanel);

			RechtenPanel rechtenPanel = new RechtenPanel();
			tabbedPane.addTab("Rechten & Permissies", rechtenPanel);
		}

		tabbedPane.setUI(new BasicTabbedPaneUI() {
			@Override
			protected int calculateTabHeight(int tabPlacement, int tabIndex,
					int fontHeight) {
				return 35;
			}
		});
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RefreshPanel panel = (RefreshPanel) tabbedPane
						.getSelectedComponent();
				panel.refreshPanel();
				if (Sessie.getIngelogdeGebruiker() == null) {
					JOptionPane
							.showMessageDialog(
									null,
									"De sessie is verlopen, u wordt teruggebracht naar het loginscherm.",
									"Sessie verlopen",
									JOptionPane.ERROR_MESSAGE);
					frame.removeAll();
					frame.getContentPane().add(new LoginScherm(frame));
					frame.revalidate();
					frame.repaint();
				}
			}
		});
		NaamPanel npanel = new NaamPanel(frame);
		npanel.setBorder(new EmptyBorder(10, 5, 5, 20));
		add(npanel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
	}

}
