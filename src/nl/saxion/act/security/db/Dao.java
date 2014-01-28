package nl.saxion.act.security.db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.saxion.act.security.model.Klas;
import nl.saxion.act.security.model.Toets;
import nl.saxion.act.security.model.Vak;
import nl.saxion.act.security.rbac.Permissie;
import nl.saxion.act.security.rbac.PermissieHelper;
import nl.saxion.act.security.rbac.Rol;
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

	public void setPermissieMap() {
		Map<String, Permissie> permissies = new HashMap<String, Permissie>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM permissie");
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				permissies.put(
						resultSet.getString(2),
						new Permissie(resultSet.getLong(1), resultSet
								.getString(2)));
			}
			PermissieHelper.permissies = permissies;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Rol> getAlleRollen() {
		List<Rol> rollen = new ArrayList<Rol>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM rol");
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				Rol rol = new Rol(resultSet.getLong(1), resultSet.getString(2));
				for (Permissie p : getPermissiesVanRol(rol.getId())) {
					rol.addPermissie(p);
				}
				rollen.add(rol);
			}
			return rollen;
		} catch (SQLException e) {
			e.printStackTrace();
			return rollen;
		}
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

	public List<Rol> getRollen(long userId) {
		List<Rol> rollen = new ArrayList<Rol>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT rol_id FROM user_rol WHERE user_id = ?");
			prepareStatement.setLong(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				rollen.add(getRol(resultSet.getLong(1)));
			}
			return rollen;
		} catch (SQLException e) {
			e.printStackTrace();
			return rollen;
		}
	}

	public Rol getRol(long rolId) {
		Rol rol = null;
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM rol WHERE id = ?");
			prepareStatement.setLong(1, rolId);
			ResultSet resultSet = prepareStatement.executeQuery();

			if (resultSet.next()) {
				rol = new Rol(resultSet.getLong(1), resultSet.getString(2));
				for (Permissie p : getPermissiesVanRol(rolId)) {
					rol.addPermissie(p);
				}
			}
			return rol;
		} catch (SQLException e) {
			e.printStackTrace();
			return rol;
		}
	}

	public List<User> getStudenten() {
		List<User> studenten = new ArrayList<User>();
		try {
			PreparedStatement preparedStatement = manager
					.prepareStatement("SELECT user_id FROM user_rol WHERE rol_id = 3");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				studenten.add(getUser(resultSet.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studenten;
	}

	public List<User> getStudentenVanDocent(long docentId) {
		List<User> studenten = new ArrayList<User>();
		for (Klas k : getKlassenVanDocent(docentId)) {
			for (User student : k.getLeerlingen()) {
				studenten.add(student);
			}
		}
		return studenten;
	}

	public List<Permissie> getPermissiesVanRol(long rolId) {
		List<Permissie> permissies = new ArrayList<Permissie>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT permissie_id FROM rol_permissie WHERE rol_id = ?");
			prepareStatement.setLong(1, rolId);
			ResultSet resultSet = prepareStatement.executeQuery();
			PreparedStatement permissie = manager
					.prepareStatement("SELECT * FROM permissie WHERE id = ?");
			while (resultSet.next()) {
				permissie.setLong(1, resultSet.getLong(1));
				ResultSet permResult = permissie.executeQuery();
				if (permResult.next()) {
					Permissie perm = new Permissie(permResult.getLong(1),
							permResult.getString(2));
					permissies.add(perm);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return permissies;
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
				user.setRollen(getRollen(userId));
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return user;
		}
	}

	public List<Klas> getKlassenVanVak(long vakId) {
		List<Long> klasIds = new ArrayList<Long>();
		List<Klas> klassen = new ArrayList<Klas>();

		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT klas_id FROM vak_klas WHERE vak_id = ?");
			prepareStatement.setLong(1, vakId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				klasIds.add(resultSet.getLong(1));
			}

			for (Long klasId : klasIds) {
				klassen.add(getKlas(klasId));
			}
			return klassen;
		} catch (SQLException e) {
			e.printStackTrace();
			return klassen;
		}
	}

	public List<Toets> getToetsenVanVak(long vakId) {
		List<Toets> toetsen = new ArrayList<Toets>();

		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM toetsen WHERE vak_id = ?");
			prepareStatement.setLong(1, vakId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				Vak vak = getVak(resultSet.getLong(1));
				Toets toets = new Toets(resultSet.getLong(1), vak);
				toetsen.add(toets);
			}
			return toetsen;
		} catch (SQLException e) {
			e.printStackTrace();
			return toetsen;
		}
	}

	public List<User> getUsers(long rolId) {
		List<User> users = new ArrayList<User>();
		List<Long> userIds = new ArrayList<Long>();
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT user_id FROM user_rol WHERE rol_id = ?");
			prepareStatement.setLong(1, rolId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				userIds.add(resultSet.getLong(1));
			}
			for (Long userId : userIds) {
				users.add(getUser(userId));
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return users;
		}
	}

	public Vak getVak(long vakId) {
		Vak vak = null;
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM vakken WHERE id = ?");
			prepareStatement.setLong(1, vakId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				vak = new Vak(resultSet.getLong(1), resultSet.getString(2));
				User user = getUser(resultSet.getLong(3));
				vak.setDocent(user);
			}
			return vak;
		} catch (SQLException e) {
			e.printStackTrace();
			return vak;
		}
	}

	public List<Vak> getVakkenVanDocent(long docentId) {
		List<Vak> vakken = new ArrayList<Vak>();

		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("SELECT * FROM vakken WHERE docent_id = ?");
			prepareStatement.setLong(1, docentId);
			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				Vak vak = new Vak(resultSet.getLong(1), resultSet.getString(2));
				User user = getUser(resultSet.getLong(3));
				vak.setDocent(user);
				vakken.add(vak);
			}
			return vakken;
		} catch (SQLException e) {
			e.printStackTrace();
			return vakken;
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
			for (Long vakId : vakIds) {
				vakken.add(getVak(vakId));
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
					.prepareStatement("SELECT id FROM toetsen WHERE vak_id = ?");
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
					.prepareStatement("SELECT klas_id FROM vak_klas WHERE vak_id = ?");
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

	public void addLeerlingAanKlas(long leerling_id, long klas_id) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO leerling_klas VALUES (?,?)");
			prepareStatement.setLong(1, leerling_id);
			prepareStatement.setLong(2, klas_id);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addKlasAanVak(long klasId, long vakId) {
		try {
			PreparedStatement prepareStatement = manager
					.prepareStatement("INSERT INTO vak_klas VALUES (?,?)");
			prepareStatement.setLong(1, vakId);
			prepareStatement.setLong(2, klasId);
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
