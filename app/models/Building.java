package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.Logger;
import play.data.validation.Constraints;
import play.db.DB;
import play.db.ebean.Model;

@Entity 
public class Building extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    public String description;
    
    public double lat;
    
    public double lng;
    
    
    
    
    
    
    /**
     * Generic query helper for entity User with id
     */
    
    
    public static int count() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT count(*) AS amount FROM Building WHERE Building.archive=0 ");
		
		int amount = 0;
		
		while(rs.next()) {
			amount = rs.getInt("amount");
			
		}
		
		
		stmt.close();
		con.close();
		
		return amount;
    }
    
    
    public static List<Building> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name,description, lat,lng FROM Building WHERE Building.archive=0");
		List<Building> list = new ArrayList<Building>();
		while(rs.next()) {
			Building e = new Building();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			e.description = rs.getString("description");
			e.lat = rs.getDouble("lat");
			e.lng = rs.getDouble("lng");
			
			list.add(e);
		}
		
		
		stmt.close();
		con.close();
		
		return list;
    }
    
    public static void add(String name, String description, double lat, double lng) throws SQLException {
    	    	Connection con = DB.getConnection();
    	        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			
    	       
    	        
    	        String insert = String.format("INSERT INTO Building (name,description,lat,lng,archive) "
    					 +"VALUES ('%s','%s',%s,%s,%s)",name,description,lat,lng,0 );
    	        
    	        
    	       stmt.executeUpdate(insert);
    	       stmt.close(); 
    	        // iwo muss das statement aber noch geschlossen werden!!!!
    	        //   stmt.close();
    	        
    	       con.close();
    	       
    	    }
    
    
    public static void update(Long id, String name, String description, double lat, double lng) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        String update = String.format("UPDATE Building SET "
        		+ "name='%s',description='%s',lat='%s',lng='%s' WHERE id=%s;" ,name,description,lat,lng,id );
        
        
       stmt.executeUpdate(update);
       stmt.close();
       
       con.close();
        
    }
    
    
    public static void delete(Long id) throws SQLException{
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        String allRoomsToArchive = String.format("Update Room SET "
        		+ "archive=1 WHERE building_id=%s;",id);
        stmt.executeUpdate(allRoomsToArchive);
        
        
        String buildingToArchive = String.format("Update Building SET "
        		+ "archive=1 WHERE id=%s;",id);
        stmt.executeUpdate(buildingToArchive);
        stmt.close();
        
        con.close();
        
    }
    
    
    /**
     * Erstellt einen TIMESTAMP des heutigen Tages, der jetzigen Zeit und vergleicht diese
     * mit den Timestamps von Sessions welche einen Raum eines Gebäudes benutzen,
     * welches zum Löschen ausgewählt wurde.
     * 
     * Es wird also überprüft, ob noch Sessions in der Zukunft in diesem Gebäude stattfinden
     * oder ob es wirklich gelöscht werden kann.
     * 
     * Es gibt den boolean roomsInUse zurück, welches das obengenannte anzeigt.
     * 
     * 
     * @param id
     * @throws SQLException
     */
    
    public static boolean checkRoomsUsedInSession(Long id) throws SQLException{
    	
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        
        String checkIt = 
        		String.format(
        
        				"SELECT Count(Room.id) AS amount FROM Session Join Room ON Session.room_id = Room.id WHERE Room.building_id = %s"
        				+ " AND FROM_UNIXTIME(Session.datetime/1000) > NOW();",id);
        
        ResultSet rs = stmt.executeQuery(checkIt);
        
        int amount = 0;
        
        while(rs.next()) {
        		amount = rs.getInt("amount");
		}
        
        stmt.close();
        con.close();
        
        boolean hasRoomsInUse = true;
        if(amount == 0)
        	hasRoomsInUse = false;
        
        
        
        return hasRoomsInUse;
        
    }
    
}