package au.com.solidapps.mysqlservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLReusableConnection implements IMySQLConnection {

	private static Logger sLogger = Logger.getLogger(MySQLReusableConnection.class.getName());
	
	private IMySQLConnection conn = null;

	public MySQLReusableConnection() {
	}

	public boolean accept(IMySQLConnection myConn) throws MySQLException {
		if(myConn == null) { throw new MySQLException("No connections available"); }
		if(myConn.isAvailable() == true) {
			myConn.setAvailable(false);
			conn = myConn;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void finalize()
	{
		try{ conn.clean(); } catch(Exception ex) { }
		conn.setAvailable(true);
		conn = null;
		sLogger.log(Level.FINE, "Connection released");
	}


	@Override
	public void open(MySQLIdentity ident) throws SQLException {
		conn.open(ident);
	}


	@Override
	public boolean execute(String sql) throws SQLException {
		return conn.execute(sql);
	}


	@Override
	public boolean resultSetNext() throws SQLException {
		return conn.resultSetNext();
	}


	@Override
	public String getResultSetString(String name) throws SQLException {
		return conn.getResultSetString(name);
	}


	@Override
	public void clean() throws SQLException {
		conn.clean();
	}


	@Override
	public void close() throws SQLException {
		conn.close();
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException, MySQLException {
		return conn.executeQuery(sql);
	}

	@Override
	public void setAvailable(boolean availableState) {
		conn.setAvailable(availableState);
	}

	@Override
	public boolean isAvailable() {
		return conn.isAvailable();
	}
}
