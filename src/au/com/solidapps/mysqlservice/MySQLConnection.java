package au.com.solidapps.mysqlservice;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection implements IMySQLConnection {

	private java.sql.Connection conn = null;
	private Statement statement = null;
	ResultSet resultSet = null;
	private boolean available = true;
	
	public MySQLConnection() {
	}
	
	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#open(au.com.solidapps.classymysql.MyIdentity)
	 */
	@Override
	public void open(MySQLIdentity ident) throws SQLException {
		// Allow this connection to be reopened, use existing connection
		if(conn == null) {
			conn = DriverManager.getConnection(ident.getURL(), ident.user, ident.password);
			statement = conn.createStatement();
		}
	}

	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#execute(java.lang.String)
	 */
	@Override
	public boolean execute(String sql) throws SQLException {
		if(resultSet != null) {
			resultSet.close();
		}
		return statement.execute(sql);
	}
	
	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String sql) throws SQLException, MySQLException {
		if((sql == null) || (sql.length() == 0)) { throw new MySQLException("MyConnection.executeQuery() appERROR sql == null or length 0");}
		if(statement == null) { throw new MySQLException("MyConnection.executeQuery() appERROR: statment == null");}

		if(resultSet != null) {
			resultSet.close();
		}
		resultSet = statement.executeQuery(sql);
		return resultSet;
	}

	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#resultSetNext()
	 */
	@Override
	public boolean resultSetNext() throws SQLException {
		return resultSet.next();
	}
	
	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#getResultSetString(java.lang.String)
	 */
	@Override
	public String getResultSetString(String name) throws SQLException {
		return resultSet.getString(name);
	}
	
	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#clean()
	 */
	@Override
	public void clean() throws SQLException {
		if(resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		
		if(statement != null) {
			statement.close();
			statement = null;
		}
		statement = conn.createStatement();
	}

	/* (non-Javadoc)
	 * @see au.com.solidapps.classymysql.IMyConnection#close()
	 */
	@Override
	public void close() throws SQLException {
		if(resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		
		if(statement != null) {
			statement.close();
			statement = null;
		}

		if(conn != null) {
			conn.close();
			conn = null;
		}
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean availableState) {
		available = availableState;
	}
}
