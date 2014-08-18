package models;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@Entity
public class Filter extends Model {

	private static final long serialVersionUID = 1L;

	public int id;

	public String gender;

	public int semesterFrom;

	public int semesterUntil;

	public static int create(int experimentId, String gender,
			int semesterFrom, int semesterUntil) throws SQLException {
		Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		String delete = String.format("DELETE Filter FROM Filter "
				+ " INNER JOIN Experiment ON Experiment.filter_id = Filter.id WHERE Filter.id=%s",
				experimentId);
		Logger.info(delete);
			stmt.executeUpdate(delete);
		
		

		String insert = String.format(
				"INSERT INTO Filter (gender, semesterFrom, semesterUntil) "
						+ "VALUES ('%s', %s, %s)", gender, semesterFrom,
				semesterUntil, experimentId);
			Logger.info(insert);
			stmt.executeUpdate(insert,
                    Statement.RETURN_GENERATED_KEYS);
			int filter_id = 0;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                filter_id = generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException();
	            }
	        }
			stmt.close();
			con.close();
			return filter_id;
		
	}

}
