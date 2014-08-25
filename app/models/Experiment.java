package models;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import play.Logger;
import play.db.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

import com.avaje.ebean.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    
    public int defaultRoom_id;
        
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
		e.defaultRoom_id = rs.getInt("defaultRoom_id");
		stmt.close();
		con.close();
		return e;
    }
    
    public static List<Experiment> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM Experiment");
		List<Experiment> list = new ArrayList<Experiment>();
		while(rs.next()) {
			Experiment e = new Experiment();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			e.description = rs.getString("description");
			e.duration = rs.getInt("duration");
			e.proband_hours = rs.getFloat("proband_hours");
			e.email_notifications = rs.getString("email_notifications");
			e.experiment_type_id = rs.getInt("experiment_type_id");
			e.max_probands = rs.getInt("max_probands");
			e.defaultRoom_id = rs.getInt("defaultRoom_id");
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
    
    public static Boolean update(int id, String name, String description, int duration, float proband_hours, int probandAmount, int expType, int defaultRoom_id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        String update = String.format("UPDATE Experiment SET "
        		+ "name='%s',description='%s',duration='%s',proband_hours='%s',max_probands='%s',experiment_type_id='%s', defaultRoom_id='%s' "
        		+ "WHERE id=%s;" ,name,description,duration,proband_hours,probandAmount, expType, defaultRoom_id, id);
        
      
       stmt.executeUpdate(update);
	   stmt.close();
       con.close();
       return true;
       
    }
    
    public static List<Experiment> LauFend() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt     .executeQuery("SELECT Experiment.name, Experiment.id, User.name FROM Experiment JOIN Assignment ON Experiment.id=Assignment.experiment_id JOIN User ON User.id=Assignment.user_id JOIN Session ON Experiment.id=Session.experiment_id WHERE FROM_UNIXTIME(Session.datetime) > NOW() ");
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
        ResultSet rs = stmt     .executeQuery("SELECT DISTINCT Experiment.name, Experiment.id, User.name FROM Experiment JOIN Assignment ON Experiment.id=Assignment.experiment_id JOIN User ON User.id=Assignment.user_id JOIN Session ON Experiment.id=Session.experiment_id WHERE FROM_UNIXTIME(Session.datetime) < NOW() AND NOT Session.datetime > NOW();");
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

	public static int create(String name, String description, int duration,
			float probandHours, int probandAmount, int expType,
			int defaultRoom_id) throws SQLException {
		Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
       
        String insert = String.format("INSERT INTO Experiment (name, description, duration, proband_hours, max_probands, experiment_type_id, defaultRoom_id) VALUES "
        		+ "('%s','%s','%s','%s','%s','%s','%s') " ,name,description,duration,probandHours,probandAmount, expType, defaultRoom_id);
        
      Logger.info(insert);
       stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
       int experiment_id = 0;
       try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
           if (generatedKeys.next()) {
           	experiment_id = generatedKeys.getInt(1);
           }
           else {
               throw new SQLException();
           }
       }
	   stmt.close();
       con.close();
       return experiment_id;
		
	}

	public static String jsonByString(String query) throws SQLException {
		List<Experiment> experiments = Experiment.all();
    	ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
    	for(Experiment e : experiments) {
    		ObjectNode event = Json.newObject();
    		event.put("id", e.id);
    		event.put("name", e.name);
    		result.add(event);
    	}
    	
    	return result.toString();
	} 
    
    
    
}

