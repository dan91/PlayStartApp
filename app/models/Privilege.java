package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity 
public class Privilege extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;  

    public int level; 
  
   
    /**
     * Generic query helper for entity User with id
     */
    public static Finder<Long,Privilege> find = new Finder<Long,Privilege>(Long.class, Privilege.class); 

    public static List<Privilege> all() {
        return find.all();
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
