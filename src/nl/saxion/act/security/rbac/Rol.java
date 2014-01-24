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
	
	public boolean isStudent(){
		if(id == 1){
			return true;
		}
		return false;
	}
	
	public boolean isDocent(){
		if(id == 2){
			return true;
		}
		return false;
	}
	
	public boolean isSuperUser(){
		if(id == 3){
			return true;
		}
		return false;
	}

}