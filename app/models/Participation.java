package models;

import java.util.*;

import javax.persistence.*;

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
     * Generic query helper for entity User with id
     */
    public static Finder<Long,Participation> find = new Finder<Long,Participation>(Long.class, Participation.class); 

    public static List<Participation> all() {
    	return find.all();
    }

    public static List<Participation> byExperimentId(Long user_id) {
        return find.fetch("session").findList();
    }
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

