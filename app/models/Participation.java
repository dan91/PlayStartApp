package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.DB;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
public class Participation extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Constraints.Required
    public Long user_id;
    
    @Constraints.Required
    public Long session_id;
    
    public Boolean was_present;

    public Boolean got_invitation;
    
 
 

    public static int completedAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Participation, Session, Experiment WHERE Participation.session_id = Session.id AND Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND was_present = 1");
		rs.next();
		stmt.close();
		con.close();
		return rs.getInt("amount");
    }
    
    public static int registeredAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Participation, Session, Experiment WHERE Participation.session_id = Session.id AND Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND Session.datetime > NOW()");
		rs.next();
		stmt.close();
		con.close();
		return rs.getInt("amount");
    }
    
    public static List<Participation> byExperimentId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT Participation.id, user_id FROM Participation, Session WHERE Participation.session_id = Session.id AND Session.experiment_id = "+id+" AND Session.datetime < NOW()");
		List<Participation> list = new ArrayList<Participation>();
		while(rs.next()) {
			Participation p = new Participation();
			p.id = rs.getLong("id");
			p.user_id = rs.getLong("user_id");
			list.add(p);
		}
		stmt.close();
		con.close();
		return list;
    }    
    
    
}

