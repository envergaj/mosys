package simplegui;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.RunScript;

public class TestDatabase {
	public Connection loadDatabase() throws Exception {
	    Class.forName("org.h2.Driver");
	    Connection conn = null;
	    try {
	        // throws an exception if the database does not exists
	        conn = DriverManager.getConnection("jdbc:h2:./mosys.db;ifexists=true");
	    } catch (SQLException e) {
	        // if the database does not exists it will be created
	        conn = DriverManager.getConnection("jdbc:h2:./mosys.db");
	        // init the db
	        initDatabase(conn);
	    }

	    return conn;
	}

	private void initDatabase(Connection con) throws Exception {
	    // load the init.sql script from JAR
	    InputStreamReader isr = new InputStreamReader(
	        getClass().getResourceAsStream("createDatabase.sql"));
	    // run it on the database to create the whole structure, tables and so on
	    RunScript.execute(con, isr);
	    isr.close();
	}
}
