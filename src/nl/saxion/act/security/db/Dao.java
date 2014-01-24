package nl.saxion.act.security.db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.User;

public class Dao {

	private DBManager manager = DBManager.getInstance();
	private static Dao instance;

	private Dao() {
	}

	public static Dao getInstance() {
		if (instance == null) {
			instance = new Dao();
		}
		return instance;
	}

	public void addVak(String naam, long docentId) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO vakken VALUES (?, ?)");
			prepareStatement.setString(1, naam);
			prepareStatement.setLong(2, docentId);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User getUser(long userId) {
		User user = null;
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM users WHERE id = ?");
			prepareStatement.setLong(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getLong(1), resultSet.getString(2));
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return user;
		}
	}

	public List<Vak> getVakkenVanStudent(long studentId) {
		List<Vak> vakken = new ArrayList<Vak>();
		List<Long> klasIds = new ArrayList<Long>();
		List<Long> vakIds = new ArrayList<Long>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT klas_id FROM leerling_klas WHERE leerling_id = ?");
			prepareStatement.setLong(1, studentId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				klasIds.add(resultSet.getLong(1));
			}
			PreparedStatement prepareStatement2 = manager
					.prepareStatement("SELECT vak_id FROM vak_klas WHERE klas_id = ?");
			for (Long klasId : klasIds) {
				prepareStatement2.setLong(1, klasId);
				ResultSet resultSet2 = prepareStatement2.executeQuery();

				while (resultSet2.next()) {
					vakIds.add(resultSet2.getLong(1));
				}
			}

			PreparedStatement prepareStatement3 = manager
					.prepareStatement("SELECT * FROM vakken WHERE vak_id = ?");
			for (Long vakId : vakIds) {
				prepareStatement3.setLong(1, vakId);
				ResultSet resultSet3 = prepareStatement2.executeQuery();

				while (resultSet3.next()) {
					Vak vak = new Vak(resultSet3.getLong(1),
							resultSet3.getString(2));
					User user = getUser(resultSet3.getLong(3));
					vak.setDocent(user);
					vakken.add(vak);
				}
			}
			return vakken;
		} catch (SQLException e) {
			e.printStackTrace();
			return vakken;
		}
	}

	public void addToets(long vakId) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO toetsen VALUES (?)");
			prepareStatement.setLong(1, vakId);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCijferBijStudent(long toetsId, long studentId, double cijfer) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO toetsuitslag VALUES (?,?,?)");
			prepareStatement.setLong(1, toetsId);
			prepareStatement.setLong(2, studentId);
			prepareStatement.setDouble(3, cijfer);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public double getCijferVanStudentVanVak(long vakId, long studentId) {
		long toetsId = 0;
		double cijfer = 0.0;
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT toets_id FROM toetsen WHERE vak_id = ?");
			prepareStatement.setLong(1, vakId);
			ResultSet resultSet = prepareStatement.executeQuery();

			if (resultSet.next()) {
				toetsId = resultSet.getLong(1);
			}

			PreparedStatement prepareStatement2 = manager
					.prepareStatement("SELECT cijfer FROM toetsuitslag WHERE toets_id = ? AND leerling_id = ?");
			prepareStatement2.setLong(1, toetsId);
			prepareStatement2.setLong(2, studentId);
			ResultSet resultSet2 = prepareStatement2.executeQuery();

			if (resultSet2.next()) {
				cijfer = resultSet2.getDouble(1);
			}
			return cijfer;

		} catch (SQLException e) {
			e.printStackTrace();
			return cijfer;
		}
	}

	public boolean checkLogin(long userId, String wachtwoord) {
		String result = "";
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT wachtwoord FROM users WHERE id = ?");
			prepareStatement.setLong(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();

			if (resultSet.next()) {
				result = resultSet.getString(1);
			}
			return result.equals(encryptPassword(wachtwoord));
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	// private DBManager manager = DBManager.getInstance();
	//
	// private final static String PARTIJ_INSERT =
	// "INSERT INTO partijen VALUES (?,?,?)";
	// private final static String PERSOON_INSERT =
	// "INSERT INTO personen VALUES (?,?,?,?,?,?,?,?)";
	// private final static String NUMMER_INSERT =
	// "INSERT INTO stemnummers VALUES (?)";
	// private final static String REGISTREER_STEM =
	// "UPDATE personen SET aantal_stemmen = aantal_stemmen + 1 WHERE nummer = ? AND partij_id = ?";
	// private final static String GET_PERSOON =
	// "SELECT * FROM personen WHERE nummer = ? AND partij_id = ?";
	// private final static String PARTIJ_ALLES = "SELECT * FROM partijen";
	// private final static String PERSOON_UITSLAG =
	// "SELECT * FROM personen WHERE partij_id = ?";
	// private final static String PARTIJ_OP_NAAM =
	// "SELECT * FROM partijen WHERE naam = ?";
	// private final static String GET_PARTIJ =
	// "SELECT * FROM partijen WHERE id = ?";
	//
	// private static Dao instance = null;
	//
	// private Dao() {
	// }
	//
	// public static Dao getInstance() {
	// if (instance == null) {
	// instance = new Dao();
	// }
	// return instance;
	// }
	//
	// /**
	// * Voegt een nieuwe partij toe aan de database
	// *
	// * @param partij
	// * de toe te voegen partij
	// */
	// public void insertPartij(Partij partij) {
	//
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(PARTIJ_INSERT);
	// prepareStatement.setInt(1, partij.getNr());
	// prepareStatement.setString(2, partij.getNaam());
	// prepareStatement.setString(3, partij.getAfkorting());
	// prepareStatement.execute();
	// } catch (SQLException e) {
	// System.err.println("Failed to insert partij : " + partij);
	// }
	// }
	//
	// /**
	// * Voegt een nieuwe persoon toe aan de database
	// *
	// * @param persoon
	// * de toe te voegen partij
	// * @param partij
	// * de partij waar deze persoon aan gekoppeld moet worden
	// */
	// public void insertPersoon(Persoon persoon, Partij partij) {
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(PERSOON_INSERT);
	// prepareStatement.setInt(1, persoon.getNr());
	// prepareStatement.setString(2, persoon.getInitialen());
	// prepareStatement.setString(3, persoon.getRoepnaam());
	// prepareStatement.setString(4, persoon.getAchternaam());
	// prepareStatement.setString(5, persoon.getWoonplaats());
	// prepareStatement.setString(6, persoon.getGeslacht());
	// prepareStatement.setInt(7, partij.getNr());
	// prepareStatement.setInt(8, 0);
	//
	// prepareStatement.execute();
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to insert persoon : " + persoon);
	// }
	// }
	//
	// /**
	// * registreert een stem bij een specifieke persoon en partij
	// *
	// * @param persoonNummer
	// * het persoonnummer van de persoon waarop gestemd is
	// * @param partijNummer
	// * het partijnummer van de partij waar deze persoon bij hoort
	// */
	// public void registreerStem(Integer persoonNummer, Integer partijNummer) {
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(REGISTREER_STEM);
	// prepareStatement.setInt(1, persoonNummer);
	// prepareStatement.setInt(2, partijNummer);
	// prepareStatement.execute();
	// System.out.println("Stem geregistreerd: partij " + partijNummer
	// + ", persoon " + persoonNummer);
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to register vote for persoon: P"
	// + persoonNummer + "P" + partijNummer);
	// }
	// }
	//
	// public boolean checkStemnummer(Integer stemNummer) {
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(NUMMER_INSERT);
	// prepareStatement.setInt(1, stemNummer);
	// prepareStatement.execute();
	// System.out
	// .println("Uniek stemnummer " + stemNummer + " toegevoegd");
	// return true;
	// } catch (SQLException e) {
	// System.err.println("Stemnummer " + stemNummer
	// + " is niet uniek, stem niet toegestaan!");
	// return false;
	// }
	// }
	//
	// /**
	// * Geeft een Persoon terug aan de hand van zijn persoon en partij nummer
	// *
	// * @param persoonNummer
	// * het persoonnummer van deze persoon
	// * @param partijNummer
	// * het partijnummer van de partij waar deze persoon bij hoort
	// * @return De gevonden persoon of null als dit geen geldige persoon is
	// */
	// public Persoon getPersoonByUid(Integer persoonNummer, Integer
	// partijNummer) {
	// Persoon result = null;
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(GET_PERSOON);
	// prepareStatement.setInt(1, persoonNummer);
	// prepareStatement.setInt(2, partijNummer);
	//
	// ResultSet resultSet = prepareStatement.executeQuery();
	//
	// if (resultSet.next()) {
	// result = new Persoon();
	// result.setNr(resultSet.getInt(1));
	// result.setInitialen(resultSet.getString(2));
	// result.setRoepnaam(resultSet.getString(3));
	// result.setAchternaam(resultSet.getString(4));
	// result.setWoonplaats(resultSet.getString(5));
	// result.setGeslacht(resultSet.getString(6));
	//
	// }
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to retrieve Persoon object for id: P"
	// + persoonNummer + "P" + partijNummer);
	// }
	// return result;
	// }
	//
	// /**
	// * Verzamelt de uitslagen en maakt hier uitslagPartij en uitslagPersoon
	// * objecten van
	// *
	// * @return De lijst van partijen met hun uitslag
	// */
	// public List<PartijUitslag> getPartijUitslagen() {
	// List<PartijUitslag> result = new ArrayList<>();
	//
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(PARTIJ_ALLES);
	// ResultSet resultSet = prepareStatement.executeQuery();
	//
	// while (resultSet.next()) {
	// Partij partij = new Partij();
	// partij.setNr(resultSet.getInt(1));
	// partij.setNaam(resultSet.getString(2));
	// partij.setAfkorting(resultSet.getString(3));
	//
	// PartijUitslag uitslag = new PartijUitslag();
	// uitslag.setPartij(partij);
	// uitslag.setPersoonUitslagen(getPersoonUitslagenVoorPartij(partij));
	// result.add(uitslag);
	// }
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to retrieve Partijuitslagen");
	// }
	// return result;
	// }
	//
	// /**
	// * Geeft een partij aan de hand van de naam
	// *
	// * @param partijNaam
	// * de naam van deze partij
	// * @return de eerste partij met de meegegeven naam
	// */
	// public Partij getPartijOpNaam(String partijNaam) {
	// Partij partij = null;
	//
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(PARTIJ_OP_NAAM);
	// prepareStatement.setString(1, partijNaam);
	// ResultSet resultSet = prepareStatement.executeQuery();
	//
	// while (resultSet.next()) {
	// partij = new Partij();
	// partij.setNr(resultSet.getInt(1));
	// partij.setNaam(resultSet.getString(2));
	// partij.setAfkorting(resultSet.getString(3));
	// }
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to retrieve Partij");
	// }
	// return partij;
	// }
	//
	// /**
	// * Geeft een lijst van persoonuitslagen voor een specifieke partij
	// *
	// * @param partij
	// * de partij waarvoor we de uitslagen willen weten
	// * @return een lijst van alle persoonuitslagen van de meegegeven partij
	// */
	// public List<PersoonUitslag> getPersoonUitslagenVoorPartij(Partij partij)
	// {
	// List<PersoonUitslag> result = new ArrayList<>();
	//
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(PERSOON_UITSLAG);
	// prepareStatement.setInt(1, partij.getNr());
	// ResultSet resultSet = prepareStatement.executeQuery();
	//
	// while (resultSet.next()) {
	// Persoon persoon = new Persoon();
	// persoon.setNr(resultSet.getInt(1));
	// persoon.setInitialen(resultSet.getString(2));
	// persoon.setRoepnaam(resultSet.getString(3));
	// persoon.setAchternaam(resultSet.getString(4));
	// persoon.setWoonplaats(resultSet.getString(5));
	// persoon.setGeslacht(resultSet.getString(6));
	//
	// PersoonUitslag uitslag = new PersoonUitslag();
	// uitslag.setPersoon(persoon);
	// uitslag.setAantalStemmen(resultSet.getInt(8));
	//
	// result.add(uitslag);
	// }
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to retrieve PersoonUitslagen");
	// }
	// return result;
	// }
	//
	// public Partij getPartijByUID(Integer partijNummer) {
	// Partij partij = null;
	//
	// try {
	// PreparedStatement prepareStatement = manager
	// .prepareStatement(GET_PARTIJ);
	// prepareStatement.setInt(1, partijNummer);
	// ResultSet resultSet = prepareStatement.executeQuery();
	//
	// if (resultSet.next()) {
	// partij = new Partij();
	// partij.setNr(resultSet.getInt(1));
	// partij.setNaam(resultSet.getString(2));
	// partij.setAfkorting(resultSet.getString(3));
	// }
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to retrieve Partij");
	// }
	// return partij;
	// }
}
