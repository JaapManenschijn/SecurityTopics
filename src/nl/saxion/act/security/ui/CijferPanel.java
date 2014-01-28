package nl.saxion.act.security.ui;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class CijferPanel extends JPanel{
	
	private JLabel cijfer;
	private JLabel vak;
	private JLabel docent;
	
	public CijferPanel(){
		setLayout(null);
		
		JLabel lblCijfer = new JLabel("Cijfer: ");
		lblCijfer.setBounds(159, 23, 40, 16);
		add(lblCijfer);
		
		cijfer = new JLabel();
		cijfer.setBounds(232, 23, 25, 16);
		add(cijfer);
		
		JLabel lblNewLabel = new JLabel("Vak:");
		lblNewLabel.setBounds(159, 84, 33, 16);
		add(lblNewLabel);
		
		vak = new JLabel();
		vak.setBounds(232, 84, 97, 16);
		add(vak);
		
		JLabel lblDocent = new JLabel("Docent:");
		lblDocent.setBounds(159, 145, 56, 16);
		add(lblDocent);
		
		docent = new JLabel();
		docent.setBounds(232, 145, 56, 16);
		add(docent);
	}

	public void setCijfer(String cijfer) {
		this.cijfer.setText(cijfer);
	}

	public void setVak(String vak) {
		this.vak.setText(vak);
	}

	public void setDocent(String docent) {
		this.docent.setText(docent);
	}
}
