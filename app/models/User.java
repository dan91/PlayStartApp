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
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String email;

    public String language;

    public int rights_id;

    public int chair_id;

    public String phone;

    public Date birthday;

    public String handedness;

    public Date enrollment_date;

    public int proband_pools_id;
    
    
    /**
     * Generic query helper for entity User with id
     */
    public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
    
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

