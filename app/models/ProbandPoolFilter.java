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

	public static Boolean create(int filter_id, String proband_pool_id) throws SQLException {
		Connection con = DB.getConnection();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		String insert = String.format(
				"INSERT INTO ProbandPoolFilter (filter_id, probandPool_id) "
						+ "VALUES (%s, %s)", filter_id, proband_pool_id);

		try {
			stmt.executeUpdate(insert);
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			stmt.close();
			con.close();
			return false;
		}
	}
}
