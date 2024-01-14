package lambda;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;




public class dbHelper {
	
	public Context context;
	
	public static final String driver = "org.mariadb.jdbc.Driver";
	public static final String db_url = "jdbc:mariadb://test.cricuz6yn94x.us-east-2.rds.amazonaws.com/";
	//public static final String db_url = "jdbc:mariadb://127.0.0.1/";
	public static final String db_name = "test";
	
	public static final String user = "tbot";
	public static final String pass = "Tbot_298268";
	//public static final String pass = "tbot";
	
	public Statement stmt;
	public Connection init_conn;
	
	public void closeConn() {
		try {
			if(init_conn!=null)
				init_conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getConn() {
		try {
			//Register JDBC driver
			//context.getClass().forName(driver);
			Class.forName(driver);
			//DriverManager.getDriver(driver);
			//System.out.println("Trying to connect to selected database");
			try {
				System.out.println(" getConn Inside inner try");
				if(init_conn == null || init_conn.isClosed()) {
					System.out.println(" getConn Inside if loop");
					init_conn = DriverManager.getConnection(db_url+db_name, user, pass);
				}
			}
			catch(Exception ex) {
				System.out.println("SQL Exception caught in the init_conn check "+ex.toString());
			}
			//System.out.println("Connected database successfully");
			//return init_conn;
		}
		catch(Exception ex) {
			System.out.println("Debug Log: SQL Exception in "+ dbHelper.class.getCanonicalName()+".getConn()");
			System.out.println(ex.toString());
			//return init_conn;
		}
	}
	
	public boolean doesTableExist(String table) {
		getConn();
		Connection conn = init_conn;
		DatabaseMetaData meta;
		if(conn == null)
			System.out.println(" doesTableExist conn is null");
		try {
			meta = conn.getMetaData();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("SQL Connection unsuccessful");
			return false;
		}
		  ResultSet res;
		try {
			res = meta.getTables(null, null, table, 
			     new String[] {"TABLE"});
			if(res.next()) {
				//System.out.println(res.getString("TABLE_NAME"));
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		  
		     
	}
	
	public String createSelectQuery(String table, String columns, String limit, String cond) {
		String query = "select";
		String stmt_columns = "";
		String stmt_limit = "";
		String stmt_cond = "";
		
		String stmt_table = " from "+table;
		
		if(columns != null)
			stmt_columns = " "+columns;
		else
			stmt_columns = " *";
		
		if(cond != null)
			stmt_cond = " where "+cond;
		
		if(!limit.equals("0"))
			stmt_limit = " limit "+limit;
		else
			stmt_limit = "";
				
		query = query+stmt_columns+stmt_table+stmt_cond+stmt_limit;
		return query;
	}
	
	public ResultSet selectQuery(String sql) {
		ResultSet res_Set = null;
		try {
			getConn();
			Statement stmt = init_conn.createStatement();
		res_Set = stmt.executeQuery(sql);
		stmt.closeOnCompletion();
		return res_Set;
		}
		catch(Exception ex) {
			System.out.println("SQL Exception here "+ex.toString());
			//Possible results
			//No such column: xxxx
			return res_Set;
		}
	}
}

