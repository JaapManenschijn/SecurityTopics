package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.saxion.act.security.rbac.Sessie;

public class NaamPanel extends JPanel {
	public NaamPanel(final JFrame frame) {
		setLayout(new BorderLayout());
		JLabel naamLabel = new JLabel("Welkom "
				+ Sessie.getIngelogdeGebruiker().getNaam(),
				SwingConstants.RIGHT);
		add(naamLabel, BorderLayout.CENTER);

		JButton btnUitloggen = new JButton("Uitloggen");
		btnUitloggen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				Sessie.setIngelogdeGebruiker(null);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(new LoginScherm(frame));
				frame.revalidate();
				frame.repaint();
			}

		});
		add(btnUitloggen, BorderLayout.EAST);
	}

}
