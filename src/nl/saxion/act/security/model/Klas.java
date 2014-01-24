package nl.saxion.act.security.model;

import java.util.List;

import nl.saxion.act.security.rbac.User;

public class Klas {
	private long id;
	private String naam;
	private List<User> leerlingen;
	private List<Vak> vakken;
	
	public Klas(long id, String naam){
		this.id = id; 
		this.naam = naam;
	}
	
	public void addStudent(User user){
		if(user.isStudent()){
			if(!leerlingen.contains(user)){
				leerlingen.add(user);
			}
		}
	}
	
	public void addVak(Vak vak){
		if(!vakken.contains(vak)){
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

}
