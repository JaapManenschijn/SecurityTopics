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

	public boolean heeftPermissie(Permissie permissie) {
		for (Rol rol : rollen) {
			for (Permissie p : rol.getPermissies()) {
				if (p.equals(permissie)) {
					return true;
				}
			}
		}
		return false;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
