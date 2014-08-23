package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.db.DB;

public class CompletedMapper {

	public String experiment_name;
	
	public String date;
	
	public String time;
	
	public String end_time;
	
	public float proband_hours;
	
	public float duration;
	
	public int was_present;
	
	public float experiment_proband_hours;
	
	
	public static List<CompletedMapper> byUserId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
        
        
        ResultSet rs = stmt
				.executeQuery(
						"SELECT Experiment.name,Experiment.proband_hours AS exp_proband_hours, Session.datetime,Experiment.duration,Participation.proband_hours, "+
						" Participation.was_present FROM Participation JOIN Session ON Participation.session_id = Session.id "+
						" JOIN Experiment ON Session.experiment_id = Experiment.id "+
						" WHERE Participation.user_id = 1 AND FROM_UNIXTIME(Session.datetime/1000) < NOW() ;"
						);
        
        List<CompletedMapper> list = new ArrayList<CompletedMapper>();
        while(rs.next()){
        	CompletedMapper c = new CompletedMapper();
        	
        	c.experiment_name = rs.getString("name");
        	c.experiment_proband_hours = rs.getFloat("exp_proband_hours");
        	c.date = new SimpleDateFormat("dd.MM.YYYY", new Locale("de", "DE")).format(new Date(rs.getLong("datetime")));
        	c.time = new SimpleDateFormat("HH:mm", new Locale("de", "DE")).format(new Date(rs.getLong("datetime")));
        	c.duration = rs.getFloat("duration");
        	c.end_time = getEndTime(c.date, c.time, c.duration);
        	c.proband_hours = rs.getFloat("proband_hours");
        	c.was_present = rs.getInt("was_present");
        	
        	
        	list.add(c);
        }
        stmt.close();
        con.close();
        
        return list;
        
        
	}
	
	public static int getCompletedAmount(Long id) throws SQLException{
		
		Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
        ResultSet rs = stmt
				.executeQuery("SELECT Count(Participation.id) AS amount FROM "
						+ "Participation JOIN Session ON Participation.session_id = Session.id WHERE "
						+ "Participation.user_id="+id+" AND FROM_UNIXTIME(Session.datetime/1000) < NOW();");
        
        int amount = 0;
        
        while(rs.next()) {
			amount = rs.getInt("amount");
			
        }
        stmt.close();
        con.close();
        
		return amount;
	}
	
	
	/**
	 * Erfolgsbalken bei Completed
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static float getBar(Long id) throws SQLException{
		
		Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
        ResultSet rs = stmt
				.executeQuery(
						"SELECT SUM(Participation.proband_hours) AS amount FROM "+
						" Participation JOIN Session ON Participation.session_id = Session.id WHERE "+
						" Participation.user_id="+id+" AND Participation.was_present=1  "+
						" AND FROM_UNIXTIME(Session.datetime/1000) < NOW();");
        
        float amount = 0;
        
        while(rs.next()) {
			amount = rs.getFloat("amount");
			
        }
        stmt.close();
        con.close();
        
        // Weil 25 Probandenstunden zu absolvieren sind
//        float barPercentage = amount/25;
        
		return amount;
	}
	
	
	/**
	 * EXISTIERT SCHON IN REGISTERED MAPPER
	 * 
	 * @param date
	 * @param time
	 * @param duration
	 * @return
	 */
	public static String getEndTime(String date, String time, float duration) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(date+" "+time);
		
		String pattern = "dd.MM.yyyy HH:mm";
		
		int minutes = (int) (duration%60);
		int hours = (int) ((duration - minutes)/60);
		

		
		DateTime dt = DateTime.parse(sb.toString(),DateTimeFormat.forPattern(pattern));
		DateTime endTime = dt.plusHours(hours).plusMinutes(minutes);
		
		return endTime.toString("HH:mm");
	}
	
}
