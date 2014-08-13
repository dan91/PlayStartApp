package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.DB;

@Entity 
public class Session {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    public Date datetime;

    public Long room_id;

    public Long experiment_id;
   
    public static int availableAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Session, Experiment WHERE Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND Session.datetime > NOW()");
		rs.next();
		return rs.getInt("amount");
    }
    
    public static List<Session> byExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, datetime FROM Session WHERE experiment_id = "+id+"");
		List<Session> list = new ArrayList<Session>();
		while(rs.next()) {
			Session s = new Session();
			s.id = rs.getLong("id");
			s.datetime = rs.getDate("datetime");
			System.out.println(rs.getDate("datetime"));
			list.add(s);
		}
		return list;
    }
    
    public static void main(String args[]) throws SQLException {
    	List<Session> list = new ArrayList<Session>();
    	Long id = (long) 1;
    	list = Session.byExperimentId(id);
    }
    
}
