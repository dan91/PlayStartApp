package models;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import play.db.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
public class Experiment extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    public String description;

    public int duration;

    public float proband_hours;

    public String email_notifications;

    public int experiment_type_id;
    
    public int max_probands;

    /**
     * Generic query helper for entity User with id
     */
    public static Experiment byId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM Experiment WHERE id = "+id+"");
		rs.next();
		Experiment e = new Experiment();
		e.id = rs.getLong("id");
		e.name = rs.getString("name");
		e.description = rs.getString("description");
		e.duration = rs.getInt("duration");
		e.proband_hours = rs.getFloat("proband_hours");
		e.email_notifications = rs.getString("email_notifications");
		e.experiment_type_id = rs.getInt("experiment_type_id");
		e.max_probands = rs.getInt("max_probands");
		stmt.close();
		return e;
    }
    
    public static List<Experiment> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name FROM Experiment");
		List<Experiment> list = new ArrayList<Experiment>();
		while(rs.next()) {
			Experiment e = new Experiment();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			list.add(e);
		}
		stmt.close();
		return list;
    }
    
    
}

