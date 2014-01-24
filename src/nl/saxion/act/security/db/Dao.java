package nl.saxion.act.security.db;


public class Dao {
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
