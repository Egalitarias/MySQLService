package au.com.solidapps.mysqlservice;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMySQLConnection {

	public abstract void open(MySQLIdentity ident) throws SQLException;

	public abstract boolean execute(String sql) throws SQLException;

	public abstract ResultSet executeQuery(String sql) throws SQLException, MySQLException;

	public abstract boolean resultSetNext() throws SQLException;

	public abstract String getResultSetString(String name) throws SQLException;

	public abstract void clean() throws SQLException;

	public abstract void close() throws SQLException;

	public void setAvailable(boolean availableState);

	public boolean isAvailable();

}