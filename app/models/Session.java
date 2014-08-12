package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.DB;
import play.db.ebean.Model;

@Entity 
public class Session extends Model {

    private static final long serialVersionUID = 2L;

    @Id
    public Long id;
    
    public Date datetime;

    public int max_probands;

    public Long room_id;

    public Long experiment_id;
  
   
    
    
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
