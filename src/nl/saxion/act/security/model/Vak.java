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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vak other = (Vak) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
