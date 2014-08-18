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
    
    public int filter_id;
    
    public String assignment;

	public String anzahlSessions;

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
		con.close();
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
		con.close();
		return list;
    }
    
    
    public static List<Experiment> TenExp() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt
                .executeQuery("SELECT name, id FROM Experiment LIMIT 10 ");
        		List<Experiment> list = new ArrayList<Experiment>();
			while(rs.next()) {
				Experiment e = new Experiment();
				e.id = rs.getLong("id");
				e.name = rs.getString("name");
				list.add(e);
		}
			stmt.close();
			con.close();
		return list;
    }  
    
    public static Boolean update(int id, String name, String description, int duration, float proband_hours, int probandAmount, int expType) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        String update = String.format("UPDATE Experiment SET "
        		+ "name='%s',description='%s',duration='%s',proband_hours='%s',max_probands='%s',experiment_type_id='%s' "
        		+ "WHERE id=%s;" ,name,description,duration,proband_hours,probandAmount, expType, id);
        
       try {
       stmt.executeUpdate(update);
	   stmt.close();
       con.close();
       return true;
       } catch(Exception e) {
    	   stmt.close();
           con.close();
    	   return false;
       }
       
    }
    
    public static List<Experiment> LauFend() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt     .executeQuery("SELECT Experiment.name, Experiment.id, User.name FROM Experiment JOIN Assignment ON Experiment.id=Assignment.experiment_id JOIN User ON User.id=Assignment.user_id JOIN Session ON Experiment.id=Session.experiment_id WHERE Session.datetime > NOW() ");
		List<Experiment> list = new ArrayList<Experiment>();
        while(rs.next()) {
			Experiment e = new Experiment();
			e.id = rs.getLong("id");
			e.name = rs.getString("Experiment.name");
			e.assignment = rs.getString("User.name");
			list.add(e);
	}
    
			stmt.close();
			con.close();
		return list;
    } 
    
    
    public static List<Experiment> finishedStudies() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt     .executeQuery("SELECT DISTINCT Experiment.name, Experiment.id, User.name FROM Experiment JOIN Assignment ON Experiment.id=Assignment.experiment_id JOIN User ON User.id=Assignment.user_id JOIN Session ON Experiment.id=Session.experiment_id WHERE NOT (Session.datetime > NOW());");
		List<Experiment> list = new ArrayList<Experiment>();
        while(rs.next()) {
			Experiment e = new Experiment();
			e.id = rs.getLong("id");
			e.name = rs.getString("Experiment.name");
			e.assignment = rs.getString("User.name");
			list.add(e);
	}
    
			stmt.close();
			con.close();
		return list;
    } 
    
    
    
}

