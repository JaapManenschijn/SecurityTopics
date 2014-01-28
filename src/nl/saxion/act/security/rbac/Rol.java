package nl.saxion.act.security.rbac;

import java.util.ArrayList;
import java.util.List;

public class Rol {
	private long id;
	private String naam;
	private List<Permissie> permissies;

	public Rol(long id, String naam) {
		this.id = id;
		this.naam = naam;
		this.permissies = new ArrayList<Permissie>();
	}

	public void addPermissie(Permissie permissie) {
		if (!permissies.contains(permissie)) {
			permissies.add(permissie);
		}
	}

	public void removePermissie(Permissie permissie) {
		permissies.remove(permissie);
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public List<Permissie> getPermissies() {
		return permissies;
	}

	public String toString() {
		return naam;
	}
}
