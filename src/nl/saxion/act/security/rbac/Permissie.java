package nl.saxion.act.security.rbac;

public class Permissie {
	private long id;
	private String naam;

	public Permissie(long id, String naam) {
		this.id = id;
		this.naam = naam;
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
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
		Permissie other = (Permissie) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
