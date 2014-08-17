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
public class ProbandPoolFilter extends Model {

    private static final long serialVersionUID = 1L;    
    
    public int proband_pool_id;
    
    public int filter_id;
    
    
}

