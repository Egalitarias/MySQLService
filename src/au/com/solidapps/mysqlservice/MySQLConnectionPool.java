package au.com.solidapps.mysqlservice;

import java.sql.SQLException;
import java.util.Vector;

public class MySQLConnectionPool {

	private static final int CULL_SCHEDULE = 10;
	private MySQLIdentity ident = null;
	private Vector<IMySQLConnection> conns = null;
	private int cullTicker = 0;
	
	public MySQLConnectionPool() {
		
	}
	
	public void init(MySQLIdentity anIdent, int poolSize) throws SQLException
	{
		ident = anIdent;
		conns = new Vector<IMySQLConnection>();
		
		for(int index = 0; index < poolSize; ++index) {
			addNewConnection();
		}
	}
	
	private IMySQLConnection addNewConnection() throws SQLException {
		MySQLConnection conn = new MySQLConnection();
		conn.open(ident);
		conns.add(conn);
		return conn;
	}
	
	IMySQLConnection getConnection() throws SQLException {
		System.out.println("Connections: " + conns.size());

		++cullTicker;
		if(cullTicker > CULL_SCHEDULE) {
			cullTicker = 0;
			cullConnections();
		}
		
		for (IMySQLConnection conn : conns)
		{
			if(conn.isAvailable()) {
				return conn;
			}
		}
				
		return addNewConnection();
	}
	
	public void cullConnections() throws SQLException
	{
		int availableCount = 0;
		for (IMySQLConnection conn : conns)
		{
			if(conn.isAvailable()) {
				++availableCount;
			}
		}
		double check =  ((double)availableCount) / ((double)conns.size());
		
		if((conns.size() > 10) && (availableCount > 5) && (check > 0.3))
		{
			int cullCount = availableCount / 2;
			while(cullCount > 0) {
				for (IMySQLConnection conn : conns)
				{
					if(conn.isAvailable()) {
						conn.clean();
						conn.close();
						conns.remove(conn);
						break;
					}
				}
				--cullCount;
			}
		}
	}
}
