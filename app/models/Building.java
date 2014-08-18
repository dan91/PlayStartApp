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
				.executeQuery("SELECT count(*) AS amount FROM Building");
		
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
				.executeQuery("SELECT id, name,description, lat,lng FROM Building");
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
    			
    	       
    	        
    	        String insert = String.format("INSERT INTO Building (name,description,lat,lng) "
    					 +"VALUES ('%s','%s',%s,%s)",name,description,lat,lng );
    	        
    	        
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
        // iwo muss das statement aber noch geschlossen werden!!!!
        //   stmt.close();
        
    }
    
    
    public static void delete(Long id) throws SQLException{
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        String delete = String.format("DELETE FROM Building WHERE id=%s;",id);
        stmt.executeUpdate(delete);
        stmt.close();
        
        con.close();
        
        String message="Deleted on server, row with id: "+id+"\n "
    			+"has been deleted.";
        Logger.info(message);
    	
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
        
        				"SELECT Session.room_id,Session.datetime FROM Session LEFT Join Room ON Session.room_id = Room.id WHERE Room.building_id = %s;",id);
       
        ResultSet rs = stmt.executeQuery(checkIt);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        
        int dateInt = Integer.parseInt(dateFormat.format(date).split(" ")[0].replace("-", ""));
        int timeInt = Integer.parseInt(dateFormat.format(date).split(" ")[1].replace(":",""));
        
        
        List<Integer> roomIds = new ArrayList<Integer>();
        
        List<Integer> sessionsDate = new ArrayList<Integer>();
        List<Integer> sessionsTime = new ArrayList<Integer>();
        
        while(rs.next()) {

        	
        	
        	if(!roomIds.contains(rs.getInt("room_id")))
        		roomIds.add(rs.getInt("room_id"));
        	
			sessionsDate.add(Integer.parseInt(rs.getString("datetime").split(" ")[0].replace("-", "")));
			sessionsTime.add(Integer.parseInt(rs.getString("datetime").split(" ")[1].replace(".0", "").replace(":","")));
		}
        
        stmt.close();
        con.close();
        
        Logger.debug(String.valueOf(roomIds.size()));

        boolean buildingHasNoRooms = false;
        boolean roomsInUse= true;
        		
        if(roomIds.size() == 0)
        buildingHasNoRooms = true;
        if(buildingHasNoRooms)
        	roomsInUse = !roomsInUse;
        
        
       
        /** WENN ES KLEINER IST SESSION ZUKÜNFTIG, RAUM ALSO IN ZUKUNFT NOCH BELEGT */
        for (int i = 0; i < sessionsDate.size(); i++) {
			
        	if(dateInt     <      sessionsDate.get(i)){
        		roomsInUse = true;
        		break;
        	}	
        	else if(dateInt     ==      sessionsDate.get(i) &&
        			timeInt     <       sessionsTime.get(i)){
        		roomsInUse = true;
        		break;
        	}
        	else if(dateInt     >      sessionsDate.get(i)){
        		roomsInUse = false;
        	}
        	else if(dateInt    ==      sessionsDate.get(i) &&
        			timeInt     >      sessionsTime.get(i)){
        		roomsInUse = false;
        	} 
        		
		}
        
        
        if(roomsInUse)
        	Logger.error("ROOM IS STILL IN USE!");
        else{
        	Logger.info("ROOM IS NOT IN USE!");
        	try{
        		Room.deleteRooms(roomIds);
        		Building.delete(id);
        	} catch(SQLException e){
        		Logger.error("Fehler beim Räume Löschen!\n "+e.toString());
        	}
        }
        
        
        return roomsInUse;
        
    }
    
}