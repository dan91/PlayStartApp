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
public class ExcludedExperiment extends Model {

    private static final long serialVersionUID = 1L;    
    
    public int experiment_id;
    
    public int filter_id;

    public static void create(int filter_id, String experiment_id) throws SQLException {
		Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		String insert = String.format(
				"INSERT INTO ExcludedExperiment (filter_id, experiment_id) "
						+ "VALUES (%s, %s)", filter_id, Integer.valueOf(experiment_id));

			stmt.executeUpdate(insert);
			stmt.close();
			con.close();
		
	}
    
    
}

