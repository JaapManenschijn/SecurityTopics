package nl.saxion.act.security.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import nl.saxion.act.security.rbac.User;

public class TabbedPanel extends JPanel {

	private JFrame frame;

	public TabbedPanel(User user, JFrame frame) {
		setLayout(new BorderLayout());
		this.frame = frame;

		JTabbedPane tabbedPane = new JTabbedPane();

		CijferOverzichtPanel coPanel = new CijferOverzichtPanel();
		tabbedPane.addTab("Cijfer overzicht", coPanel);

		if (user.isDocent() || user.isSuperUser()) {

			KlasPanel klasPanel = new KlasPanel();
			tabbedPane.addTab("Klas overzicht", klasPanel);

			VakPanel vakPanel = new VakPanel();
			tabbedPane.addTab("Vak overzicht", vakPanel);

			ToetsPanel toetsPanel = new ToetsPanel();
			tabbedPane.addTab("Toets overzicht", toetsPanel);
		}

		if (user.isSuperUser()) {
			DocentPanel docentPanel = new DocentPanel();
			tabbedPane.addTab("Docent overzicht", docentPanel);

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
		NaamPanel npanel = new NaamPanel(frame, user);
		npanel.setBorder(new EmptyBorder(10, 5, 5, 20));
		add(npanel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
	}

}
