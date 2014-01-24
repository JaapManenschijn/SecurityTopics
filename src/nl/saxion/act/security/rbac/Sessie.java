package nl.saxion.act.security.rbac;

public class Sessie {
	private static User ingelogdeGebruiker;

	public static void setIngelogdeGebruiker(User user) {
		ingelogdeGebruiker = user;
	}

	public static User getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}
}
