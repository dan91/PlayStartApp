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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.SQLConn;
import play.Logger;
import play.data.validation.Constraints;
import play.db.DB;
import play.libs.Json;

@Entity 
public class Session {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    public long datetime;

    public Long room_id;

    public Long experiment_id;
   
    /**
     * liefert die Anzahl von verfügbaren Sessions für ein Experiment
     * @param id
     * @return
     * @throws SQLException
     */
    public static int availableAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Session, Experiment WHERE Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND FROM_UNIXTIME(Session.datetime/1000) > NOW()");
		rs.next();
		int amount = rs.getInt("amount");
		stmt.close();
		con.close();
		return amount;
    }
    
    /**
     * liefert eine Liste von Sessions für ein Experiment
     * @param id
     * @return
     * @throws SQLException
     */
    public static List<Session> byExperimentId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, datetime FROM Session WHERE experiment_id = "+id+"");
		List<Session> list = new ArrayList<Session>();
		while(rs.next()) {
			Session s = new Session();
			s.id = rs.getLong("id");
			s.datetime = rs.getLong("datetime");
			list.add(s);
		}
		stmt.close();
		con.close();
		return list;
    }   
    @JsonIgnore
    public static String jsonByExperimentId(long id) throws SQLException, JsonProcessingException {
    	List<Session> sessions = Session.byExperimentId(id);
    	ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
    	Experiment e = Experiment.byId(id);
    	int duration = e.duration;
    	int max_probands = e.max_probands;
    	for(Session s : sessions) {
    		ObjectNode event = Json.newObject();
    		DateTime dt = new DateTime(s.datetime, DateTimeZone.UTC);
    		event.put("title", "");
    		event.put("start", s.datetime);
    		event.put("end", dt.plusMinutes(duration).toString());
    		event.put("participations", Participation.bySessionId(s.id).size());
    		event.put("max_probands", max_probands);
    		event.put("session_id", s.id);
    		result.add(event);
    	}
    	
    	return result.toString();
    }
    
    public static List<Session> toConfirmByExperimentId(Long id, String filter) throws SQLException {
    	String add = "AND was_present IS NOT NULL";
    	if(filter.equals("new"))
    		add = "AND was_present IS NULL";
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String select = "SELECT Session.id, user_id, session_id, datetime, was_present FROM Participation, Session WHERE experiment_id = "+id+" AND "
						+ "Session.id = Participation.session_id AND FROM_UNIXTIME(Session.datetime/1000) < NOW() "+add+" GROUP BY Session.datetime";
		ResultSet rs = stmt
				.executeQuery(select);
		Logger.info(select);
		List<Session> list = new ArrayList<Session>();
		while(rs.next()) {
			Session p = new Session();
			p.id = rs.getLong("id");
			p.datetime = rs.getLong("datetime");
			
			list.add(p);
		}
		stmt.close();
		con.close();
		return list;
    }
    
    public static int amountToConfirmByExperimentId(Long id) throws SQLException {
    	return toConfirmByExperimentId(id, "new").size();
    }
    
    public static Session byId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT datetime FROM Session WHERE id = "+id+"");
		rs.next();
		Session s = new Session();
		s.datetime = rs.getLong("datetime");
		stmt.close();
		con.close();
		return s;
    }   
    
    
    
    public static int AmountByExperimentId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Session WHERE experiment_id = "+id+"");
		rs.next();
		int amount = rs.getInt("amount");
		stmt.close();
		con.close();
		return amount;
    }   
    
    
    
    public static List<Session> byUserId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        
        Logger.info("DATE AND TIME RIGHT NOW: "+dateFormat.format(date));
        
        ResultSet rs = stmt
				.executeQuery(
						"SELECT Participation.id, Participation.session_id, Session.experiment_id,"+ 
						"Session.datetime, Room.name AS room_name, Room.description,"+ 
						"Building.name AS building_name, Building.description,"+ 
						"Building.Lat, Building.Lng, Experiment.name AS experiment_name,"+ 
						"Experiment.duration, Experiment.proband_hours FROM Participation"+ 
						"JOIN Session ON Participation.session_id = Session.id"+ 
						"JOIN Experiment ON Session.experiment_id = Experiment.id"+ 
						"JOIN Room ON Session.room_id = Room.id"+ 
						"JOIN Building ON Room.building_id = Building.id"+ 
						"WHERE Participation.user_id ="+id+" AND Session.datetime = '2014-08-14 14:00:00'"
						
						);
        
//        Später wird dateFormat(date) hier eingetragen, um nur noch sessions anzuzeigen die, 
//        für die der Teilnehmer sich angemeldet hat, aber noch nicht abgeschlossen sind.
		
//		List<Session> list = new ArrayList<Session>();
//		while(rs.next()) {
//			
//			s.id = rs.getLong("id");
//			s.datetime = rs.getString("datetime");
//			list.add(s);
//		}
//		stmt.close();
//		con.close();
//		return list;
        
        return null;
    }

    public static void deleteByExperimentId(int experimentId) throws SQLException {
    	Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		String delete = String.format("DELETE Session FROM Session "
				+ " INNER JOIN Experiment ON Session.experiment_id = Experiment.id WHERE Experiment.id=%s",
				experimentId);
		Logger.info(delete);
		stmt.executeUpdate(delete);
		stmt.close();
		con.close();

    }
	public static int create(int id, long datetime, int room_id) throws SQLException {
		Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		

		String insert = String.format(
				"INSERT INTO Session (experiment_id, datetime, room_id) "
						+ "VALUES ('%s', '%s', '%s')", id, datetime, room_id);
			Logger.info(insert);
			stmt.executeUpdate(insert,
                    Statement.RETURN_GENERATED_KEYS);
			int session_id = 0;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	session_id = generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException();
	            }
	        }
			stmt.close();
			con.close();
			return session_id;
	} 
}
