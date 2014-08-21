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
    
    /**
     * liefert die Anzahl der abgeschlossenen Sessions für ein Experiment
     * @param id Experiment-ID
     * @return
     * @throws SQLException
     */
    public static int completedAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Participation, Session, Experiment WHERE Participation.session_id = Session.id AND Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND was_present = 1");
		rs.next();
		int amount = rs.getInt("amount");
		stmt.close();
		con.close();
		return amount;
    }
    
    /**
     * liefert die Anzahl der Anmeldungen für ein Experiment
     * @param id: Experiment-ID
     * @return
     * @throws SQLException
     */
    public static int registeredAmountByExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT COUNT(*) AS amount FROM Participation, Session, Experiment WHERE Participation.session_id = Session.id AND Session.experiment_id = Experiment.id AND Experiment.id = "+id+" AND Session.datetime > NOW()");
		rs.next();
		int amount = rs.getInt("amount");
		stmt.close();
		con.close();
		return amount;
    }
    
    /**
     * liefert eine Liste von Teilnahmen für eine Session
     * @param id: Session-ID
     * @return
     * @throws SQLException
     */
    public static List<Participation> bySessionId(Long id) throws SQLException {	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT Participation.id, user_id FROM Participation, Session WHERE Participation.session_id = Session.id AND Session.id = "+id+" AND Session.datetime < NOW()");
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

	public static void create(int session_id, int user_id) throws SQLException {
		Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		

		String insert = String.format(
				"INSERT INTO Participation (session_id, user_id) "
						+ "VALUES ('%s', '%s')", session_id,user_id);
			Logger.info(insert);
			stmt.executeUpdate(insert);
			
			stmt.close();
			con.close();
		
	}    
    
    
}

