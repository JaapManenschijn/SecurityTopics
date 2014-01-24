package nl.saxion.act.security.rbac;

import java.util.ArrayList;
import java.util.List;

public class User {
	private long id;
	private String naam;
	private List<Rol> rollen;

	public User(long id, String naam) {
		this.id = id;
		this.naam = naam;
		rollen = new ArrayList<Rol>();
	}

	public List<Rol> getRollen() {
		return rollen;
	}

	public void setRollen(List<Rol> rollen) {
		this.rollen = rollen;
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public boolean isStudent() {
		for (Rol rol : rollen) {
			if (rol.getNaam().equals("student")) {
				return true;
			}
		}
		return false;
	}

	public boolean isDocent() {
		for (Rol rol : rollen) {
			if (rol.getNaam().equals("docent")) {
				return true;
			}
		}
		return false;
	}

	public boolean isSuperUser() {
		for (Rol rol : rollen) {
			if (rol.getNaam().equals("beheerder")) {
				return true;
			}
		}
		return false;
	}
}
