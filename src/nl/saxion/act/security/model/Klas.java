package nl.saxion.act.security.model;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.act.security.rbac.User;

public class Klas {
	private long id;
	private String naam;
	private List<User> leerlingen;
	private List<Vak> vakken;

	public Klas(long id, String naam) {
		this.id = id;
		this.naam = naam;
		this.leerlingen = new ArrayList<User>();
		this.vakken = new ArrayList<Vak>();
	}

	public void addStudent(User user) {
		if (user.isStudent()) {
			if (!leerlingen.contains(user)) {
				leerlingen.add(user);
			}
		}
	}

	public void addVak(Vak vak) {
		if (!vakken.contains(vak)) {
			vakken.add(vak);
		}
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public List<User> getLeerlingen() {
		return leerlingen;
	}

	public List<Vak> getVakken() {
		return vakken;
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
		Klas other = (Klas) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
