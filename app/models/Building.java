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
    
    public static void checkRoomsUsedInSession(Long id) throws SQLException{
    	
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        String checkIt = 
        		String.format(
        
        				"SELECT Session.room_id,Session.datetime,Room.name FROM Session LEFT Join Room ON Session.room_id = Room.id WHERE Room.building_id = %s;",id);
       
        ResultSet rs = stmt.executeQuery(checkIt);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
//        Logger.info(dateFormat.format(date)); //2014-08-06 15:59:48
        
        int dateInt = Integer.parseInt(dateFormat.format(date).split(" ")[0].replace("-", ""));
        int timeInt = Integer.parseInt(dateFormat.format(date).split(" ")[1].replace(":",""));
        
        
        
        List<String> sessions = new ArrayList<String>();
        
        while(rs.next()) {
			sessions.add(rs.getString("datetime").replace(".0", "").replace("-", "").replace(":",""));
		}
        
        stmt.close();
        con.close();
        
        
        for (int i = 0; i < sessions.size(); i++) {
			Logger.info(sessions.get(i));
		}
        
        
        boolean roomsInUse= true;
        /** WENN ES KLEINER IST SESSION ZUKÜNFTIG, RAUM ALSO IN ZUKUNFT NOCH BELEGT */
//        for (int i = 0; i < sessions.size(); i++) {
//			
//        	if(Long.parseLong(dateSt)     <      Long.parseLong(sessions.get(i).split(" ")[0])){
//        		roomsInUse = true;
//        		break;
//        	}	
//        	else if(Long.parseLong(dateSt)     ==      Long.parseLong(sessions.get(i).split(" ")[0]) &&
//        			Long.parseLong(timeSt)     <      Long.parseLong(sessions.get(i).split(" ")[1])){
//        		roomsInUse = true;
//        		break;
//        	}
//        	else if(Long.parseLong(dateSt)     >      Long.parseLong(sessions.get(i).split(" ")[0])){
//        		roomsInUse = false;
//        	}
//        	else if(Long.parseLong(dateSt.replace("-", ""))     ==      Long.parseLong(sessions.get(i).split(" ")[0]) &&
//        			Long.parseLong(timeSt.replace(":", ""))     >      Long.parseLong(sessions.get(i).split(" ")[1])){
//        		roomsInUse = false;
//        	} 
//        		
//		}
        
        
        if(roomsInUse)
        	Logger.error("ROOM IS STILL IN USE!!!");
       
        
    }
    
}