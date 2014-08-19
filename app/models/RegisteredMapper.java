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

import play.Logger;
import play.db.DB;

public class RegisteredMapper {

	public Long participation_id;
	
	public String experiment_name;
	
	public String date;
	
	public String time;
	
	public String building_name;
	
	public String building_description;
	
	public String room_name;
	
	public String room_description;
	
	public double Lat;
	
	public double Lng;
	
	public float proband_hours;
	
	public float duration;
	
	
	public static List<RegisteredMapper> byUserId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
        
        ResultSet rs = stmt
				.executeQuery(
						"SELECT Participation.id, Experiment.name AS experiment_name, Session.datetime, Experiment.duration,"+
						"Building.name AS building_name, Building.description AS building_description,"+
						"Building.Lat, Building.Lng,"+
						"Room.name AS room_name, Room.description AS room_description,"+
						"Experiment.proband_hours FROM Participation "+
						"JOIN Session ON Participation.session_id = Session.id "+
						"JOIN Experiment ON Session.experiment_id = Experiment.id "+
						"JOIN Room ON Session.room_id = Room.id "+
						"JOIN Building ON Room.building_id = Building.id "+

						"WHERE Participation.user_id="+id+" AND Session.datetime > NOW();"
						);
        
        
       
        StringBuilder sb = new StringBuilder();
		
		List<RegisteredMapper> list = new ArrayList<RegisteredMapper>();
		while(rs.next()) {
			RegisteredMapper u = new RegisteredMapper();
			
			u.participation_id = rs.getLong("id");
			u.experiment_name = rs.getString("experiment_name");
			
			sb.append(rs.getString("datetime").split(" ")[0].split("-")[2]+".");
			sb.append(rs.getString("datetime").split(" ")[0].split("-")[1]+".");
			sb.append(rs.getString("datetime").split(" ")[0].split("-")[0]);
			u.date = sb.toString();
			
			u.time = rs.getString("datetime").split(" ")[1].replace(":00.0","");
			u.duration = rs.getFloat("duration");
			u.building_name = rs.getString("building_name");
			u.building_description = rs.getString("building_description");
			u.Lat = rs.getDouble("Lat");
			u.Lng = rs.getDouble("Lng");
			u.room_name = rs.getString("room_name");
			u.room_description = rs.getString("room_description");
			u.proband_hours = rs.getFloat("proband_hours");
			
			
			sb = new StringBuilder();
			
			list.add(u);
		}
		stmt.close();
		con.close();
		
		return list;
        
    } 
	
	
	
}
