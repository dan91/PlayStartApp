package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.DB;
import play.db.ebean.Model;

@Entity 
public class Assignment extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    public Long experiment_id;    
 
    public Long user_id; 

    public Boolean notifications_only;    

   
    public static List<Assignment> byExperimentId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT Assignment.id, User.id, Assignment.notifications_only FROM User, Assignment WHERE Assignment.user_id = User.id AND Assignment.experiment_id = "+id+"");
		List<Assignment> list = new ArrayList<Assignment>();
		while(rs.next()) {
			Assignment a = new Assignment();
			a.id = rs.getLong("Assignment.id");
			a.user_id = rs.getLong("User.id");
			a.notifications_only = rs.getBoolean("notifications_only");
			list.add(a);
		}
		stmt.close();
		return list;
    }
    
}
