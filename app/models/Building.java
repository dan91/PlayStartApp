package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    
    public int lat;
    
    public int lng;
    
    
    /**
     * Generic query helper for entity User with id
     */
    
    public static List<Building> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name, description, lat, lng FROM Building");
		List<Building> list = new ArrayList<Building>();
		while(rs.next()) {
			Building e = new Building();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			e.description = rs.getString("name");
			e.lat = rs.getInt("lat");
			e.lng = rs.getInt("lng");
			
			list.add(e);
		}
		return list;
    }
}