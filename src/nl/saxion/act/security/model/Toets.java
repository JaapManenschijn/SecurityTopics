package nl.saxion.act.security.model;

import java.util.HashMap;
import java.util.Map;

import nl.saxion.act.security.rbac.User;

public class Toets {
	private long id;
	private Vak vak;
	private Map<User, Double> uitslagen;

	public Toets(long id, Vak vak) {
		this.id = id;
		this.vak = vak;
		uitslagen = new HashMap<User, Double>();
	}

	public void addUitslag(User user, double cijfer) {
		assert (user.isStudent());
		assert (cijfer >= 1.0 && cijfer <= 10.0);
		uitslagen.put(user, cijfer);
	}

	public long getId() {
		return id;
	}

	public Vak getVak() {
		return vak;
	}

	public Map<User, Double> getUitslagen() {
		return uitslagen;
	}

	public String toString() {
		return "Toets " + id;
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
		Toets other = (Toets) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
