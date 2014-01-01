package au.com.solidapps.mysqlservice;

public class MySQLIdentity {
	public String host;
	public String databaseName;
	public String user;
	public String password;
	
	public MySQLIdentity() {
		
	}
	
	public String getURL() {
	
		return "jdbc:mysql://" + host + "/" + databaseName;
	}	
	
}
