package nl.saxion.act.security.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBBuilder {
	private DBManager manager = DBManager.getInstance();
	private static DBBuilder instance;

	private DBBuilder() {

	}

	/**
	 * Geeft een unieke instantie van deze DBBuilder terug
	 */
	public static DBBuilder getInstance() {
		if (instance == null) {
			instance = new DBBuilder();
		}
		return instance;
	}

	/**
	 * Dropt de tabellen partijen en personen in de datbase
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void cleanDatabase() {
		System.out.println("Dropping all tables");
		String[] statements = { "DROP TABLE klassen", "DROP TABLE toetsen",
				"DROP TABLE vakken", "DROP TABLE users", "DROP TABLE rol",
				"DROP TABLE permissie", "DROP TABLE toetsuitslag",
				"DROP TABLE vak_klas", "DROP TABLE leerling_klas",
				"DROP TABLE rol_permissie" };

		for (String statement : statements) {
			PreparedStatement prepareStatement = manager
					.prepareStatement(statement);
			try {
				prepareStatement.execute();
			} catch (SQLException e) {
				System.err.println("Failed to exectue query " + statement);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Maakt de tabellen partijen en personen aan in de database
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void createDatabase() {
		System.out.println("Creating all tables");

		String[] statements = {//
				"CREATE TABLE klassen ( "
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"naam VARCHAR(255) NOT NULL," + //
						"CONSTRAINT pk_klas PRIMARY KEY (id))",

				"CREATE TABLE toetsen ("
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"vak_id INT NOT NULL," + //
						"CONSTRAINT pk_toets PRIMARY KEY (id))",

				"CREATE TABLE vakken ("
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"naam VARCHAR(255) NOT NULL," + //
						"docent_id BIGINT NOT NULL," + //
						"CONSTRAINT pk_vak PRIMARY KEY (id))",

				"CREATE TABLE users ("
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"naam VARCHAR(255) NOT NULL," + //
						"wachtwoord VARCHAR(255) NOT NULL," + //
						"CONSTRAINT pk_user PRIMARY KEY (id))",

				"CREATE TABLE rol ("
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"naam VARCHAR(255) UNIQUE NOT NULL," + //
						"CONSTRAINT pk_rol PRIMARY KEY (id))",

				"CREATE TABLE permissie ("
						+ //
						"id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
						+ //
						"naam VARCHAR(255) UNIQUE NOT NULL," + //
						"CONSTRAINT pk_permissie PRIMARY KEY (id))",

				"CREATE TABLE toetsuitslag ("
						+ //
						"toets_id BIGINT NOT NULL,"
						+ //
						"leerling_id BIGINT NOT NULL,"
						+ //
						"cijfer FLOAT NOT NULL,"
						+ //
						"CONSTRAINT pk_uitslag PRIMARY KEY (toets_id, leerling_id),"
						+ //
						"CONSTRAINT fk_toets foreign key (toets_id) references toetsen (id),"
						+ //
						"CONSTRAINT fk_leerling foreign key (leerling_id) references users (id))",

				"CREATE TABLE vak_klas ("
						+ //
						"vak_id BIGINT NOT NULL,"
						+ //
						"klas_id BIGINT NOT NULL,"
						+ //
						"CONSTRAINT pk_vakklas PRIMARY KEY (vak_id, klas_id),"
						+ //
						"CONSTRAINT fk_vak foreign key (vak_id) references vakken (id),"
						+ //
						"CONSTRAINT fk_klas foreign key (klas_id) references klassen (id))",

				"CREATE TABLE leerling_klas ("
						+ //
						"leerling_id BIGINT NOT NULL,"
						+ //
						"klas_id BIGINT NOT NULL,"
						+ //
						"CONSTRAINT pk_leerlingklas PRIMARY KEY (leerling_id, klas_id),"
						+ //
						"CONSTRAINT leerling_fk foreign key (leerling_id) references users (id),"
						+ //
						"CONSTRAINT klas_fk foreign key (klas_id) references klassen (id))",

				"CREATE TABLE rol_permissie ("
						+ //
						"rol_id BIGINT NOT NULL,"
						+ //
						"permissie_id BIGINT NOT NULL,"
						+ //
						"CONSTRAINT pk_rolpermissie PRIMARY KEY (rol_id, permissie_id),"
						+ //
						"CONSTRAINT fk_rol foreign key (rol_id) references rol (id),"
						+ //
						"CONSTRAINT fk_permissie foreign key (permissie_id) references permissie (id))"

		};

		for (String statement : statements) {
			PreparedStatement prepareStatement = manager
					.prepareStatement(statement);
			try {
				prepareStatement.execute();
			} catch (SQLException e) {
				System.err.println("Failed to exectue query " + statement);
				throw new RuntimeException(e);
			}
		}
	}
}
