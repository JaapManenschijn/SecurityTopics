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

import nl.saxion.act.security.model.Klas;
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
					.prepareStatement("INSERT INTO vakken (naam, docent_id) VALUES (?, ?)");
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
					.prepareStatement("INSERT INTO toetsen (vak_id) VALUES (?)");
			prepareStatement.setLong(1, vakId);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCijferBijStudent(long toetsId, long studentId, double cijfer) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO toetsuitslag (toets_id, leerling_id, cijfer) VALUES (?,?,?)");
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

	public void addUser(String naam, String wachtwoord) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO users (naam, wachtwoord) VALUES (?,?)");
			prepareStatement.setString(1, naam);
			prepareStatement.setString(2, encryptPassword(wachtwoord));
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addRol(String naam) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO rol (naam) VALUES (?)");
			prepareStatement.setString(1, naam);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addPermissie(String naam) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO permissie (naam) VALUES (?)");
			prepareStatement.setString(1, naam);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPermissieBijRol(long rol_id, long permissie_id) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO rol_permissie VALUES (?,?)");
			prepareStatement.setLong(1, rol_id);
			prepareStatement.setLong(2, permissie_id);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setRolBijUser(long user_id, long rol_id) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO user_rol VALUES (?,?)");
			prepareStatement.setLong(1, user_id);
			prepareStatement.setLong(2, rol_id);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addKlas(String naam) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO klassen (naam) VALUES (?)");
			prepareStatement.setString(1, naam);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Klas> getKlassenVanDocent(long docent_id) {
		List<Klas> klassenVanDocent = new ArrayList<Klas>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT id FROM vakken WHERE docent_id = ?");
			PreparedStatement vakklasStatement = manager
					.prepareStatement("SELECT klas_id FROM klassen WHERE vak_id = ?");
			prepareStatement.setLong(1, docent_id);
			ResultSet resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				long vakId = resultSet.getLong(1);
				vakklasStatement.setLong(1, vakId);

				ResultSet klasIds = vakklasStatement.executeQuery();
				while (klasIds.next()) {
					long klasId = klasIds.getLong(1);
					klassenVanDocent.add(getKlas(klasId));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klassenVanDocent;
	}

	public Klas getKlas(long klas_id) {
		Klas klas = null;
		try {
			PreparedStatement klasStatement = manager
					.prepareStatement("SELECT * FROM klassen WHERE id = ?");
			klasStatement.setLong(1, klas_id);

			ResultSet klasResult = klasStatement.executeQuery();
			if (klasResult.next()) {
				klas = new Klas(klasResult.getLong(1), klasResult.getString(2));
				PreparedStatement leerlingenKlas = manager
						.prepareStatement("SELECT leerling_id FROM leerling_klas WHERE klas_id = ?");
				leerlingenKlas.setLong(1, klas_id);
				ResultSet leerlingIds = leerlingenKlas.executeQuery();
				while (leerlingIds.next()) {
					klas.addStudent(getUser(leerlingIds.getLong(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return klas;
	}
}
