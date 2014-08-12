package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SQLConn {

	
	Connection conn = null;

	public SQLConn() {
		SQLConn instance = new SQLConn();
		try {
			instance.connect();
			instance.doStuff();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { instance.disconnect(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}

	protected void doStuff() throws SQLException{
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		// ------------------------------
		// your code goes here
		// ------------------------------
		ResultSet result = stmt.executeQuery("SELECT * FROM Building");
		while (result.next()){
			System.out.println(result.getString("name"));
			
			
		}
		result.close();
	}



	protected void connect() throws SQLException{
		disconnect();
		Properties connectionProps = new Properties();
		connectionProps.put("user", "d01b49f3");
		connectionProps.put("password", "QZFUeJbWBp85KX6T");

		conn = DriverManager.getConnection(
				"jdbc:mysql://webased.de:3306/" + "d01b49f3",    
				
				/** *****hier wird für "db" der db name eingefügt,
				klappt aber auch mit username ?! ****   */
				
				connectionProps);

		System.out.println("Connected to database");		
	}

	protected void disconnect() throws SQLException{
		if (conn != null){
			conn.close();
			conn = null;
			System.out.println("Disconnected from database");
		}
	}
}
