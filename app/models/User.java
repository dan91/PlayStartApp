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

	public int anzahlStudien;

	public int vps;

	public int noshow;

	public int anzahlBesucht;
	
	public int privilige_id;

	public String gender;

	public String studystart;
	
	public int proband_pool_id;
	
	 public static Boolean update(String name, String email, int privilige_id, String phone, String birthday, String handedness, String course, int proband_pool_id, String gender, String studystart, int id) throws SQLException {
	    	Connection con = DB.getConnection();
	        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);       
	        String update = String.format("UPDATE User SET name='%s',email='%s',privilige_id='%s',phone='%s', birthday='%s', handedness='%s', course='%s', proband_pool_id='%s', gender='%s', studystart='%s' WHERE id=%s; name,  email,  privilige_id,  phone,  birthday,  handedness,  course,  proband_pool_id,  gender,  studystart,");
	        
	       try {
	       stmt.executeUpdate(update);
		   stmt.close();
	       con.close();
	       return true;
	       } catch(Exception e) {
	    	   stmt.close();
	           con.close();
	    	   return false;
	       }
	       
	    }
//	 public static User byId(Long id) throws SQLException {
//	    	Connection con = DB.getConnection();
//	        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			ResultSet rs = stmt
//					.executeQuery("SELECT * FROM Experiment WHERE id = "+id+"");
//			rs.next();
//			User e = new User();
//			e.id = rs.getLong("id");
//			e.name = rs.getString("name");
//			e.email = rs.getString("email");
//			e.privilige_id = rs.getInt("privilige_id");
//			e.phone = rs.getString("phone");
//			e.birthday = rs.getDate("birthday");
//			e.handedness = rs.getString("handedness");
//			e.course = rs.getString("course");
//			e.proband_pool_id = rs.getInt("proband_pool_id");
//			e.gender = rs.getString("gender");
//			e.studystart = rs.getString("studytart");
//			stmt.close();
//			con.close();
//			return e;
//	    }
    
    
    public static String nameById(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT name FROM User WHERE id = "+id+"");
		rs.next();
		String name = rs.getString("name");
			stmt.close();
			con.close();
		return name;
    }
    
    public static User byId(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name FROM User WHERE id = "+id+"");
		rs.next();
		User u = new User();
		u.id = rs.getLong("id");
		u.name = rs.getString("name");
			stmt.close();
		con.close();
		return u;
    }
    
    public static int vpsById(Long id) throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT SUM(Participation.proband_hours) AS Amount FROM Experiment, Session, Participation WHERE Participation.user_id = "+id+" AND Participation.session_id = Session.id AND Session.experiment_id = Experiment.id");
		rs.next();
		int vps = rs.getInt("Amount");
			stmt.close();
			con.close();
		return vps;
    }
    
    public static int allVps() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT SUM(Experiment.proband_hours) AS Amount FROM Experiment, Participation WHERE Participation.was_present = 1");
		rs.next();
		int vps = rs.getInt("Amount");
			stmt.close();
			con.close();
		return vps;
    }
    
    public static int allUser25Vps() throws SQLException {
    	int amount = 0;
    	
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id FROM User");
		while(rs.next()){
			long userid = rs.getInt("id");
			if (vpsById(userid) >= 25){
				amount ++;}		
		}
			stmt.close();
			con.close();
		return amount;
    }
    
    

       public static int allUsers() throws SQLException {
        Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt
                .executeQuery("SELECT COUNT(*) AS Amount FROM User ");
        rs.next();
        int amount = rs.getInt("Amount");
        stmt.close();
        con.close();
        return amount;
    }
       
       public static List<User> experimenterByName(String name) throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.id, User.name FROM User, Privilege "
                   		+ "WHERE User.name LIKE '%"+name+"%' AND User.privilege_id = Privilege.id AND Privilege.level > 1 "
                   				+ "LIMIT 3");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       } 
       
       public static List<User> byFilter(Long id) throws SQLException { //TODO
    	   Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.id, User.name FROM User "
                   		+ "WHERE User.name LIKE '%"+id+"%' LIMIT 3");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       }
       
       public static List<User> byName(String name) throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.id, User.name FROM User "
                   		+ "WHERE User.name LIKE '%"+name+"%' LIMIT 3");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
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
   			con.close();
   		return list;
       } 
       
       

       public static List<User> allAdminList() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.name, User.id, User.email From  User WHERE User.privilege_id=4");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				e.email = rs.getString("email");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       } 
       
       public static List<User> allVersuchsleiterList() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.name, User.id, User.email From  User WHERE User.privilege_id=3");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				e.email = rs.getString("email");
   			//	e.anzahlStudien = rs.getInt("Amount");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       } 
       public static List<User> allHilfList() throws SQLException {
    	   
 
    	   
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.name, User.id, User.email From  User WHERE User.privilege_id=2");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				e.email = rs.getString("email");
   		//		e.anzahlStudien = rs.getInt("Amount");
   		//		e.vps = rs.getInt("Amount2");
   		//		e.noshow = rs.getInt("Amount3");
   		//		e.anzahlBesucht = rs.getInt("Amount4");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       }     
       
       
public static List<User> allProbandList() throws SQLException {
    	   	   
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT User.name, User.id, User.email From  User WHERE User.privilege_id=1");
           		List<User> list = new ArrayList<User>();
   			while(rs.next()) {
   				User e = new User();
   				e.id = rs.getLong("id");
   				e.name = rs.getString("name");
   				e.email = rs.getString("email");
   		//		e.anzahlStudien = rs.getInt("Amount");
   		//		e.vps = rs.getInt("Amount2");
   		//		e.noshow = rs.getInt("Amount3");
   		//		e.anzahlBesucht = rs.getInt("Amount4");
   				list.add(e);
   		}
   			stmt.close();
   			con.close();
   		return list;
       }  
              
       
       
       public static int allMen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='male' ");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close();
  			con.close();
           return amount;
       }
    
       public static int allWomen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE gender='female' ");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close();
  			con.close();
           return amount;
       }
       
       public static int allProbands() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN Privilege ON User.privilege_id=Privilege.id AND Privilege.level=1");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int allVersuchsleiter() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN Privilege ON User.privilege_id=Privilege.id AND Privilege.level=3");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int allAdmins() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN Privilege ON User.privilege_id=Privilege.id AND Privilege.level=3");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       
       public static int allStudents() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN ProbandPool ON User.proband_pool_id=ProbandPool.id AND ProbandPool.name='Student' ");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int allExterne() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN ProbandPool ON User.proband_pool_id=ProbandPool.id AND ProbandPool.name='Extern'");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       
       public static int allMCS() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE course='MCS'");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();;
           return amount;
       }
       public static int allMK() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE course='MK'");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int allSonstige() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User WHERE course!='MCS' AND course!='MK' ");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }

       public static int allStudies() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM Experiment");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int aktuelleStudien() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM Experiment JOIN Session ON Experiment.id=Session.experiment_id WHERE Session.datetime > NOW() ");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int finishedStudies() throws SQLException {
    	   int amount = allStudies() - aktuelleStudien();
    	   return amount;
       }
       
       public static int allSessions() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM Session");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int allTeilnahmen() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM Participation WHERE was_present=true");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }
       public static int percentageNA() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM Participation WHERE was_present=false");
           rs.next();
           int zaehler = rs.getInt("Amount");
           int nenner = allTeilnahmen();
           int ergebniss = zaehler / 1 ;
           //TODO  nenner * 100
  			stmt.close(); con.close();
           return ergebniss;
       }
       public static int allVPS() throws SQLException {
           Connection con = DB.getConnection();
           Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet rs = stmt
                   .executeQuery("SELECT COUNT(*) AS Amount FROM User INNER JOIN Privilege ON User.privilege_id=Privilege.id AND Privilege.level=1");
           rs.next();
           int amount = rs.getInt("Amount");

  			stmt.close(); con.close();
           return amount;
       }


	public static void add(String name2, String email2, String sstart,
			String course2) {
		// TODO Auto-generated method stub
		
	}


	public static Object authenticate(String email2, String password) {
		// TODO Auto-generated method stub
		return null;
	}
       
       
}

