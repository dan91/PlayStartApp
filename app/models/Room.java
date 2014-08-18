package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
				.executeQuery("SELECT id, name FROM Room WHERE id = "+id+"");
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
       
        String delete = String.format("DELETE FROM Room WHERE id=%s;",id);
        stmt.executeUpdate(delete);
        stmt.close();
        
        con.close();
        
        String message="Deleted on server, row with id: "+id+"\n "
    			+"has been deleted.";
        Logger.info(message);
    	
    }
    
    public static void deleteRooms(List<Integer> roomIds) throws SQLException{
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       
        for (int i = 0; i < roomIds.size(); i++) {
        	String delete = String.format("DELETE FROM Room WHERE id=%s;",roomIds.get(i));
            stmt.executeUpdate(delete);
		}
        
        
        stmt.close();
        con.close();
        
        String message="ALL ROOMS HAVE BEEN DELETED";
        Logger.info(message);
    	
    }
    
}
