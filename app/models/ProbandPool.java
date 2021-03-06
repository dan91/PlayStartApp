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
public class ProbandPool extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;   
  
    public static List<ProbandPool> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name FROM ProbandPool");
		List<ProbandPool> list = new ArrayList<ProbandPool>();
		while(rs.next()) {
			ProbandPool e = new ProbandPool();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			list.add(e);
		}
		stmt.close();
		con.close();
		return list;
    }
    
}
