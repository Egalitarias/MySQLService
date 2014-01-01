package au.com.solidapps.mysqlservice;

import java.sql.SQLException;

public class TestThread extends Thread {

	public volatile boolean cmd_stop = false;
	public volatile boolean state_stopped = false;
	
	private static void doQuery()
	{
		try {
			String databaseName = "mytest";
			for(int i = 0; i < 100; ++i) {
				IMySQLConnection conn = MySQLService.getConnection(databaseName);
				
				if(conn == null) {
					System.out.println("conn == null");
				}
				for(int j = 0; j < 100; ++j) {
				
					conn.executeQuery("SELECT * FROM location");
					conn.resultSetNext();
					try { sleep(100); } catch(InterruptedException ex) {};
					/*String result = */ conn.getResultSetString("name");
					//System.out.println(result);
					/*
					for(int k = 0; k < 100000000; ++k) {
						for(int l = 0; l < 1000000; ++l) {
							
						}						
					}
					*/
				}
				conn = null;
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

	public void run() {
		
		cmd_stop = false;
		state_stopped = false;

		while(cmd_stop == false) {
			doQuery();
			try { sleep(1000); } catch(InterruptedException ex) {};
		}
		state_stopped = true;

	}
}
