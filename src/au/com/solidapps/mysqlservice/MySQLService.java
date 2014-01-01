package au.com.solidapps.mysqlservice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
	
public class MySQLService {
	
	private static Map<String, MySQLConnectionPool> sPools = null;
	
	public static synchronized void createConnectionPool(MySQLIdentity ident) throws SQLException {
		if(sPools == null) {
			sPools = new HashMap<String, MySQLConnectionPool>();
		}
		MySQLConnectionPool pool = new MySQLConnectionPool();
		pool.init(ident,  10);
		sPools.put(ident.databaseName, pool);
	}
	
	public static synchronized IMySQLConnection getConnection(String databaseName) throws MySQLException, SQLException {
		
		if(sPools == null) { throw new MySQLException("No pool for database: " + databaseName); }

		MySQLConnectionPool pool = sPools.get(databaseName);
		if(pool == null) { throw new MySQLException("No pool for database: " + databaseName); }

		IMySQLConnection conn = pool.getConnection();
		MySQLReusableConnection mySQLReusableConnection = new MySQLReusableConnection();
		if(mySQLReusableConnection.accept(conn))
		{
			return mySQLReusableConnection;
		}
		else {
			throw new MySQLException("No connections available");
		}
	}

	public static MySQLIdentity createMyIdentity(
			String host,
			String databaseName,
			String user,
			String password
			) {
		MySQLIdentity ident = new MySQLIdentity();
		ident.host = host;
		ident.databaseName = databaseName;
		ident.user = user;
		ident.password = password;
		return ident;
	}
	
	public static void createLocalDatabaseAndUser(MySQLIdentity root, MySQLIdentity newIdentity) throws SQLException, MySQLException {
		if((root.host == null)  || (newIdentity.host == null)) { throw new MySQLException("Host not specified");}
		if(root.host.equals(newIdentity.host) == false) { throw new MySQLException("Hosts must not be different");}
		MySQLService.quickExec(root, "CREATE DATABASE " + newIdentity.databaseName);
		MySQLService.quickExec(root, "CREATE USER '" + newIdentity.user + "'@'" + newIdentity.host + "' IDENTIFIED BY '" + newIdentity.password + "';");

		MySQLIdentity rootAtNewDatabase = MySQLService.createMyIdentity(root.host, newIdentity.databaseName, root.user, root.password);
		MySQLService.quickExec(rootAtNewDatabase, "GRANT ALL PRIVILEGES ON " + newIdentity.databaseName + " .* TO '" + newIdentity.user + "'@'" + newIdentity.host + "';");
	}

	public static void dropLocalDatabaseAndUser(MySQLIdentity root, MySQLIdentity newIdentity) throws SQLException, MySQLException {
		if((root.host == null)  || (newIdentity.host == null)) { throw new MySQLException("Host not specified");}
		if(root.host.equals(newIdentity.host) == false) { throw new MySQLException("Hosts must not be different");}

		MySQLIdentity rootAtNewDatabase = MySQLService.createMyIdentity(root.host, newIdentity.databaseName, root.user, root.password);
		MySQLService.quickExec(rootAtNewDatabase, "DROP USER '" + newIdentity.user + "'@'" + newIdentity.host + "';");
		MySQLService.quickExec(rootAtNewDatabase, "DROP DATABASE " + newIdentity.databaseName + ";");
	}

	public static void quickExec(MySQLIdentity ident, String sql) throws SQLException {
		MySQLConnection conn = new MySQLConnection();
		conn.open(ident);
		conn.execute(sql);
		conn.close();
	}

	public static String quickQuery(MySQLIdentity ident, String sql, String columnName) throws SQLException {
		MySQLConnection conn = new MySQLConnection();
		conn.open(ident);
		conn.execute(sql);
		String result = conn.getResultSetString(columnName);
		conn.close();
		return result;
	}
}

	