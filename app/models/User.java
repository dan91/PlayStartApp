package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.persistence.*;

import play.db.DB;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String email;

    public String language;

    public int privilege_id;

    public int chair_id;

    public String phone;

    public Date birthday;

    public String handedness;

    public String enrollment_date;

    public String course;

    public int proband_pools_id;
    
    
    public static String nameById(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT name FROM User WHERE id = "+id+"");
		rs.next();
			stmt.close();

		return rs.getString("name");
    }
    

       public static int allUsers() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt
                .executeQuery("SELECT COUNT(*) AS Amount FROM User ");
        rs.next();
        int amount = rs.getInt("Amount");
        stmt.close();
        return amount;
    }
       
       public static List<User> TenUser() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT name, id, enrollment_date FROM User LIMIT 10 ");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				e.enrollment_date = rs.getString("enrollment_date");
   				list.add(e);
   		}
   			stmt.close();
   		return list;
       } 
       
       
       
       
       
       public static int allMen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='male' ");
           rs.next();
  			stmt.close();
           return rs.getInt("Amount");
       }
    
       public static int allWomen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='female' ");
           rs.next();
  			stmt.close();
           return rs.getInt("Amount");
       }
       
}

