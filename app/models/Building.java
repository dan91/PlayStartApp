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
		
       
//        String update = String.format("UPDATE Building SET "
//        		+ "name='%s',description='%s',lat='%s',lng='%s' WHERE id=%s;" ,name,description,lat,lng,id );
        
        
//       stmt.executeUpdate(update);
       stmt.close();
       
       con.close();
    	
    	
    	
    }
    
}