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
public class Room extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;  

    public String description; 

    public Long building_id;
  
    public static int byBuildingId(int buildingId) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Building, Room WHERE Room.building_id = "+buildingId+"");
		rs.next();
		int amount = rs.getInt("amount");
		stmt.close();
		con.close();
		return amount;
    }    

    /**
     * liefert eine Liste von Räumen für ein Gebäude
     * @param id: Building-ID
     * @return
     * @throws SQLException
     */
    public static List<Room> byBuildingId(long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name FROM Room WHERE building_id = "+id+"");
		List<Room> list = new ArrayList<Room>();
		while(rs.next()) {
			Room e = new Room();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			list.add(e);
		}
		stmt.close();
		con.close();
		return list;
    }
    
    
    public static List<Room> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name,description,building_id FROM Room");
		List<Room> list = new ArrayList<Room>();
		while(rs.next()) {
			Room e = new Room();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			e.description = rs.getString("description");
			e.building_id = rs.getLong("building_id");
			list.add(e);
		}
		stmt.close();
		con.close();
		return list;
    }
    
    public static void add(String name, String description, Long building_id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        
        String insert = String.format("INSERT INTO Room (name,description,building_id) "
				 +"VALUES ('%s','%s',%s)",name,description,building_id );
        
        
       stmt.executeUpdate(insert);
       stmt.close(); 
        // iwo muss das statement aber noch geschlossen werden!!!!
        //   stmt.close();
        
       con.close();
       
    }
    
    public static void update(Long id, String name, String description) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        String update = String.format("UPDATE Room SET "
        		+ "name='%s',description='%s' WHERE id=%s;" ,name,description,id );
        
        
       stmt.executeUpdate(update);
       stmt.close();
       
       con.close();
        // iwo muss das statement aber noch geschlossen werden!!!!
        //   stmt.close();
        
    }
    
    public static void delete(Long id) throws SQLException{
    	
    	
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        
        stmt.execute("SET FOREIGN_KEY_CHECKS=0");
		stmt.close();
		
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String delete = String.format("DELETE FROM Room WHERE id=%s;",id);
        stmt.executeUpdate(delete);
        stmt.close();
        
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmt.execute("SET FOREIGN_KEY_CHECKS=1");
		stmt.close();
		
        con.close();
        
        
    	
    }
    
    public static boolean checkRoomInUse(Long id) throws SQLException{
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        
        int dateInt = Integer.parseInt(dateFormat.format(date).split(" ")[0].replace("-", ""));
        int timeInt = Integer.parseInt(dateFormat.format(date).split(" ")[1].replace(":",""));
        
        List<Integer> sessionsDate = new ArrayList<Integer>();
        List<Integer> sessionsTime = new ArrayList<Integer>();
        
        
        String checkRoomInUse = String.format("SELECT Session.id,Session.datetime,Session.room_id FROM Session WHERE room_id=%s"
        		+ " AND Session.datetime > NOW(); ",id);
        
        ResultSet rs = stmt.executeQuery(checkRoomInUse);
        while(rs.next()){
        	// FEHLT HIER NOCH 
        }
        
        stmt.close();
        con.close();
        
        boolean roomsInUse= true;
        if(sessionsDate.size() == 0)
        roomsInUse= false;
        
        
        if(roomsInUse)
        	Logger.error("ROOM IS STILL IN USE!");
        else{
        	Logger.info("ROOM IS NOT IN USE!");
        }
        
        return roomsInUse;
    	
    }

    
//    ÜBERFLÜSSIG: GEBÄUDE WERDEN NICHT GELÖSCHT SONDERN INS ARCHIV VERSCHOBEN
    
//    public static void deleteRooms(List<Integer> roomIds) throws SQLException{
//    	
//    	Connection con = DB.getConnection();
//        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//       
//        /*DISABLE FOREIGN KEY CHECKS*/
//        stmt.execute("SET FOREIGN_KEY_CHECKS=0");
//		stmt.close();
//		
//		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        
//		
//		/*DELETE ROOMS NO LONGER USED*/
//        for (int i = 0; i < roomIds.size(); i++) {
//        	String delete = String.format("DELETE FROM Room WHERE id=%s;",roomIds.get(i));
//            stmt.executeUpdate(delete);
//		}
//       
//        stmt.close();
//        
//        /*ENABLE FOREIGN KEY CHECKS */
//        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        stmt.execute("SET FOREIGN_KEY_CHECKS=1");
//		stmt.close();
//        
//        con.close();
//        
//        String message="ALL ROOMS HAVE BEEN DELETED";
//        Logger.info(message);
//    	
//    }
    
}
