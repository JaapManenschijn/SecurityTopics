package nl.saxion.act.security.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nl.saxion.act.security.db.Dao;
import nl.saxion.act.security.rbac.User;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class NaamPanel extends JPanel {
	public NaamPanel(final JFrame frame, User user) {
		setLayout(new BorderLayout());
		JLabel naamLabel = new JLabel("Welkom " + user.getNaam(),
				SwingConstants.RIGHT);
		add(naamLabel, BorderLayout.CENTER);
		
		JButton btnUitloggen = new JButton("Uitloggen");
		btnUitloggen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				frame.getContentPane().removeAll();
				frame.getContentPane().add(new LoginScherm(frame));
				frame.revalidate();
				frame.repaint();
			}

		});
		add(btnUitloggen, BorderLayout.EAST);
	}

}
