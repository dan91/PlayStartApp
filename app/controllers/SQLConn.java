package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SQLConn {

	
	Connection conn = null;

	public static void main(String[] args) {
		SQLConn instance = new SQLConn();
		try {
			instance.connect("d01b49f3", "QZFUeJbWBp85KX6T", "d01b49f3");
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
		ResultSet result = stmt.executeQuery("SELECT * FROM Buildings");
		while (result.next()){
			System.out.println(result.getString("name"));
			
			
		}
		result.close();
	}



	protected void connect(String userName, String password, String db) throws SQLException{
		disconnect();
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		conn = DriverManager.getConnection(
				"jdbc:mysql://webased.de:3306/" + db,    
				
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
