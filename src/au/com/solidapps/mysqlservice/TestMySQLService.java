package au.com.solidapps.mysqlservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TestMySQLService {

	private static void testCreateDB()
	{
		
		MySQLIdentity ident = MySQLService.createMyIdentity("localhost","", "root", "");
		try {
			MySQLService.quickExec(ident,"CREATE DATABASE mytest");
			MySQLService.quickExec(ident,"DROP DATABASE mytest");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void setUp()
	{
		try {
			MySQLIdentity rootAtMysql = MySQLService.createMyIdentity("localhost","", "root", "");
			MySQLService.quickExec(rootAtMysql, "CREATE DATABASE mytest");
			MySQLService.quickExec(rootAtMysql, "CREATE USER 'mytest'@'localhost' IDENTIFIED BY 'password';");

			MySQLIdentity rootAtMytest = MySQLService.createMyIdentity("localhost","mytest", "root", "");
			MySQLService.quickExec(rootAtMytest, "GRANT ALL PRIVILEGES ON mytest.* TO 'mytest'@'localhost';");
			
			MySQLService.quickExec(rootAtMytest, "CREATE TABLE location (id int, name VARCHAR(100));");
			MySQLService.quickExec(rootAtMytest, "INSERT INTO location VALUES (1, 'Armadale');");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void tearDown()
	{
		try {
			MySQLIdentity rootAtMysql = MySQLService.createMyIdentity("localhost","", "root", "");			
			MySQLService.quickExec(rootAtMysql, "DROP USER 'mytest'@'127.0.0.1';");
			MySQLService.quickExec(rootAtMysql, "DROP USER 'mytest'@'localhost';");
			MySQLService.quickExec(rootAtMysql, "DROP DATABASE mytest;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test1()
	{
		try {
			String databaseName = "mytest";
			MySQLIdentity mytestAtMytest = MySQLService.createMyIdentity("localhost",databaseName, "mytest", "password");
			MySQLService.createConnectionPool(mytestAtMytest);
			for(int i = 0; i < 1000; ++i) {
				IMySQLConnection conn = MySQLService.getConnection(databaseName);
				conn.executeQuery("SELECT * FROM location");
				conn.resultSetNext();
				String result = conn.getResultSetString("name");
				conn = null;
				System.out.println(result);
				for(int j = 0; j < 1000000000; ++j) {
					for(int k = 0; k < 1000000; ++k) {
						
					};
					
				};
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MySQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test2()
	{
		String databaseName = "test2";
		MySQLIdentity root = MySQLService.createMyIdentity("localhost", "", "root", "");
		MySQLIdentity mytest = MySQLService.createMyIdentity("localhost", databaseName, "myuser", "password");
		MySQLIdentity rootAtMysql = MySQLService.createMyIdentity("localhost", "mysql", "root", "");
		
		try {
			MySQLService.quickExec(root, "DROP DATABASE IF EXISTS " + databaseName);
			MySQLService.createLocalDatabaseAndUser(root, mytest);
			
			String user = MySQLService.quickQuery(rootAtMysql, "SELECT user FROM user WHERE user = 'myuser'", "user");
			System.out.println("user: " + user);
			MySQLService.dropLocalDatabaseAndUser(root, mytest);
		} catch (SQLException | MySQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test2 complete");
	}
	
	public static void test3()
	{
		String databaseName = "mytest";
		MySQLIdentity mytestAtMytest = MySQLService.createMyIdentity("localhost",databaseName, "mytest", "password");
		try {
			MySQLService.createConnectionPool(mytestAtMytest);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Vector<TestThread> threads = new Vector<TestThread>();
		for(int index = 0; index < 10; ++index) {
			TestThread t = new TestThread();
			t.start();
			threads.add(t);
		}
		
		// Long delay
		for(int j = 0; j < 100000000; ++j) {
			for(int k = 0; k < 1000000; ++k) {
				
			};
			
		};
		
		while(threads.size() > 0)
		{
			TestThread t = threads.lastElement();
			t.cmd_stop = true;
			while(t.state_stopped == false) {};
			threads.removeElement(t);			
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Test");
		
		testCreateDB();
		
		setUp();
		
		//test1();
		
		//test2();
		
		test3();
		
		tearDown();
	}

}
