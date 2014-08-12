package models;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import play.db.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity 
public class Experiment extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    public String description;

    public float duration;

    public float proband_hours;

    public Boolean finished;

    public String email_notifications;

    public int experiment_type_id;
    
    
    /**
     * Generic query helper for entity User with id
     */
    public static Finder<Long,Experiment> find = new Finder<Long,Experiment>(Long.class, Experiment.class); 

    public static List<Experiment> all() throws SQLException {
    	Connection con = DB.getConnection();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt
				.executeQuery("SELECT id, name FROM Experiment");
		List<Experiment> list = new ArrayList<Experiment>();
		while(rs.next()) {
			Experiment e = new Experiment();
			e.id = rs.getLong("id");
			e.name = rs.getString("name");
			list.add(e);
		}
		return list;
    }
    
//    public static List<Experiment> byUserId() {
//        return find.where().eq(arg0, arg1)
//    }
    
    // /**
    //  * Return a page of computer
    //  *
    //  * @param page Page to display
    //  * @param pageSize Number of computers per page
    //  * @param sortBy Computer property used for sorting
    //  * @param order Sort order (either or asc or desc)
    //  * @param filter Filter applied on the name column
    //  */
    // public static Page<Computer> page(int page, int pageSize, String sortBy, String order, String filter) {
    //     return 
    //         find.where()
    //             .ilike("name", "%" + filter + "%")
    //             .orderBy(sortBy + " " + order)
    //             .fetch("company")
    //             .findPagingList(pageSize)
    //             .setFetchAhead(false)
    //             .getPage(page);
    // }
    
}

