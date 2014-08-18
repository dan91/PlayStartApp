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

import controllers.SQLConn;
import play.Logger;
import play.data.validation.Constraints;
import play.db.DB;

@Entity 
public class Session {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    public String datetime;

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
				.executeQuery("SELECT COUNT(*) AS amount FROM Session, Experiment WHERE Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND Session.datetime > NOW()");
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
			s.datetime = rs.getString("datetime");
			list.add(s);
		}
		stmt.close();
		con.close();
		return list;
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
}
