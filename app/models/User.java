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

    public Date enrollment_date;

    public String course;

    public int proband_pools_id;
    
    
    public static String nameById(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT name FROM User WHERE id = "+id+"");
		rs.next();
		return rs.getString("name");
    }
    

       public static int allUsers() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt
                .executeQuery("SELECT COUNT(*) AS Amount FROM User ");
        rs.next();
        return rs.getInt("Amount");
    }
       
       public static int allMen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='male' ");
           rs.next();
           return rs.getInt("Amount");
       }
    
       public static int allWomen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='female' ");
           rs.next();
           return rs.getInt("Amount");
       }
       
}

