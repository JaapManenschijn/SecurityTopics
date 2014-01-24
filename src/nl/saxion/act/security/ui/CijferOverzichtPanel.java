package nl.saxion.act.security.ui;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

public class CijferOverzichtPanel extends JPanel {

	public CijferOverzichtPanel() {
		setLayout(new BorderLayout(0, 0));

		JList list = new JList();
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);

		JComboBox comboBox = new JComboBox();
		add(comboBox, BorderLayout.NORTH);

	}

}
