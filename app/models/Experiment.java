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
public class Experiment extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    public String description;

    public String language;

    public float duration;

    public float proband_hours;

    public Boolean finished;

    public String email_notifications;

    public int experiment_type_id;
    
    
    /**
     * Generic query helper for entity User with id
     */
    public static Finder<Long,Experiment> find = new Finder<Long,Experiment>(Long.class, Experiment.class); 
    
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

