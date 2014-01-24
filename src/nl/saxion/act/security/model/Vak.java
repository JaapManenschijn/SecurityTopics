package nl.saxion.act.security.model;

import nl.saxion.act.security.rbac.User;

public class Vak {
	private long id;
	private String naam;
	private User docent;

	public Vak(long id, String naam) {
		this.id = id;
		this.naam = naam;
	}

	public User getDocent() {
		return docent;
	}

	public void setDocent(User user) {
		if (user.isDocent()) {
			this.docent = user;
		}
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public String toString() {
		return naam;
	}
}
