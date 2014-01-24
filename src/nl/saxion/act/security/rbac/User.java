package nl.saxion.act.security.rbac;

public class User {
	private long id;
	private String naam;
	private Rol rol;

	public User(long id, String naam) {
		this.id = id;
		this.naam = naam;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}
}
