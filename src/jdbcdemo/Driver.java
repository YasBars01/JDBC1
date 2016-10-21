package jdbcdemo;

import java.sql.*;
import java.math.*;

public class Driver {

	private static String sDB2 = "";
	private static ResultSet myRs = null;
	private static Connection conConn = null;
	private static ResultSet rs = null;
	/**
	 * Create DB Connection using DriverManager JDBC
	 * 
	 * @param db		Database url
	 * @param root		username for DB Credentials
	 * @param password	password for DB Credentials
	 * @return	Returns Connection
	 */
	private static Connection setConnection(String db, String root, String password){
		
		try {
			conConn = DriverManager.getConnection(db, root, password); 			// Get a connection to DB
			
		} catch ( Exception exc ){
			System.out.println("DB Connection is broke!");
			exc.printStackTrace();
		}
		
		return conConn;
		
	}
	
	private static ResultSet getQuery(Connection conConn, String stringQuery, String stringQueryType){
		ResultSet rs 	 = null;
		Statement myStmt = null;
		
		//return myConn;
		if ( stringQueryType == "search" ){						// SEARCH
			//Driver.querySearch(conConn, stringQuery);
			
		} else if ( stringQueryType == "insert" ){				// INSERT
			Driver.queryInsert(conConn, stringQuery);
			
		} else if ( stringQueryType == "update" ){				// UPDATE
			Driver.queryUpdate(conConn, stringQuery);
			
		} else if ( stringQueryType == "delete" ){				// DELETE
			Driver.queryDelete(conConn, stringQuery);
		
		} else{}
		
		
		try {
			myStmt = conConn.createStatement();
			rs = myStmt.executeQuery(stringQuery);
			
		} catch ( Exception exc ){
			System.out.println("setStatement SQL is broke!");
			exc.printStackTrace();
		}
		
		return rs;
	}
	
	private static void queryInsert(Connection conMyConn, String sQuery){
		try {
			Statement myStmt = conMyConn.createStatement();
			myStmt.executeUpdate(sQuery);
			System.out.println("Insert complete");
			
		} catch ( Exception exc ){
			System.out.println("setInsert SQL is broke!");
			exc.printStackTrace();
		}
		
	}

	private static void queryUpdate(Connection conMyConn, String sQuery){
		try {
			Statement myStmt = conMyConn.createStatement();
			myStmt.executeUpdate(sQuery);
			System.out.println("Update \"" + sQuery
						 	+	"\" Complete");
			
		} catch ( Exception exc ){
			System.out.println("setInsert SQL is broke!");
			exc.printStackTrace();
		}
		
	}

	private static void queryDelete(Connection conMyConn, String sQuery){
		try {
			// Create a Statement
			Statement myStmt = conMyConn.createStatement();
			// Execute a SQL query
			int rowsAffected = myStmt.executeUpdate(sQuery);
			
			System.out.println("Rows affected: " + rowsAffected);
			System.out.println("Delete \"" + sQuery
						 	+	"\" Complete");
			
		} catch ( Exception exc ){
			System.out.println("DeleteEntry SQL is broke!");
			exc.printStackTrace();
		}
		
	
	}

	private static ResultSet prepState(Connection conMyConn){
		
		try {
			// Prepare Statement
			PreparedStatement myStmt = conMyConn.prepareStatement(""
					+ "SELECT * FROM employees "
					+ "WHERE salary > ?"
					+ " AND department=?;");
			
			// Set the parameters
			myStmt.setDouble(1, 80000);
			myStmt.setString(2, "Legal");
			
			// Execute a SQL query
			myRs = myStmt.executeQuery();
			
			
		} catch ( Exception exc ){
			System.out.println("Prepared Statement is caput!");
			exc.printStackTrace();
		}
		
		return myRs;
	}

	private static PreparedStatement prepStatement(Connection conMyConn, String psPrepStatement){
		PreparedStatement myStmt = null;
		try {
			// Prepare Statement
			myStmt = conMyConn.prepareStatement(psPrepStatement);
			
		} catch ( Exception exc ){
			System.out.println("Prepared Statement is caput!");
			exc.printStackTrace();
		}
		
		return myStmt;
	}
	
	private static ResultSet getResultsPrepStatement(PreparedStatement prepStmt){
		// Execute a SQL query
		try {
			myRs = prepStmt.executeQuery();
			
		} catch (SQLException exc) {
			System.out.println("Prepared Statement execute is caput!");
			exc.printStackTrace();
		}
		
		return myRs;
	}
	
	/********************************* Display Results ***/
	/**
	 * Sends SQL Statement and returns resultSet 
	 * 
	 * @param conMyConn	Connection from setConnection() return
	 * @param sQuery	String	SQL Query
	 * @return	ResultSet array? of results based from SQL Query
	 */
	private static ResultSet setResult(Connection conMyConn, String sQuery){
		try {
			Statement myStmt = conMyConn.createStatement();
			myRs = myStmt.executeQuery(sQuery);
			
		} catch ( Exception exc ){
			System.out.println("setStatement SQL is broke!");
			exc.printStackTrace();
		}
		
		return myRs;
		
	}
	
	/**
	 * Prints Results based on the SQL Query provided on setResult()
	 * 
	 * @param rs	ResultSet from setStatement
	 * @throws SQLException
	 */
	private static void getResultsLastFristName(ResultSet rs) throws SQLException{
		System.out.println();
		while ( rs.next() ){
			System.out.println(rs.getString("last_name") + " " + rs.getString("first_name"));
		}
		System.out.println();
	}
	
	private static void getResultsIDLastFirstName(ResultSet rs) throws SQLException{
		System.out.println();
		while ( rs.next() ){
			System.out.println(rs.getInt("id") + "\t| " 
						+ rs.getString("last_name") + " " 
						+ rs.getString("first_name"));
		}
		System.out.println();
	}

	private static void getResultsIDLnFnEm(ResultSet rs) throws SQLException{
		System.out.println();
		while ( rs.next() ){
			System.out.println(rs.getInt("id") + "\t| " 
						+ rs.getString("last_name") + " " 
						+ rs.getString("first_name")+ "\t\t| " 
						+ rs.getString("email"));
		}
		System.out.println();
	}

	private static void getDisplay(ResultSet rs) throws SQLException{
		System.out.println();
		while ( rs.next() ){
			System.out.println(rs.getString("last_name") + " " 
						+ rs.getString("first_name") + "\t\t| " 
						+ rs.getBigDecimal("salary") + "\t| "
						+ rs.getString("department"));
		}
		System.out.println();
	}
	/********************************* End Display Results ***/
	
	public static void main(String[] args) throws SQLException {

		String sDB = "jdbc:mysql://localhost:3306/demo"; 
		String sUser ="yas";
		String sPass = "password";
		String sQuery = "SELECT * FROM employees;"; 
		String sQueryInsert = "INSERT INTO employees"
				+ " (last_name, first_name, email)"
				+ "values ('Brown', 'David', 'david.brown@foo.com');";
		String sQueryInsert2 = "INSERT INTO employees"
				+ " (last_name, first_name, email)"
				+ "values ('Brown', 'David2', 'david.brown@foo.com');";

		String sQueryUpdate = "UPDATE employees"
				+ " SET email='demo@luv2code.com'"
				+ " WHERE id=5;";

		String sQueryDelete = "DELETE FROM employees"
						   + " WHERE last_name='Brown';";

		String sQueryDelete2 = "DELETE FROM employees"
						   + " WHERE id=6;";
		
		// Create DB Connection
		Connection conCon = Driver.setConnection(sDB, sUser, sPass);
		
		ResultSet rsResult = Driver.setResult(conCon, sQuery);	// pass query
		Driver.getResultsLastFristName(rsResult);					// print result	
		
		rsResult = Driver.getQuery(conCon, sQuery, "search");	// pass query
		Driver.getResultsLastFristName(rsResult);					// print result	
		
		
		rsResult = Driver.setResult(conCon, sQuery);
		Driver.getResultsIDLnFnEm(rsResult);
		
		System.out.println("prepState");
		rsResult = Driver.prepState(conCon);
		Driver.getDisplay(rsResult);
		
		// ------------ Insert ---------------------- //
		//Driver.insertEntry(conCon, sQueryInsert2);
		
		// Pass SQL Statement - refresh results then Print
		//rsResult = Driver.setResult(conCon, sQuery);
		//Driver.getResultsLastFristName(rsResult);
		// ------------ Insert ---------------------- //
		
		// ------------ Update ---------------------- //
		/*
		Driver.updateEntry(conCon, sQueryUpdate);
		rsResult = Driver.setResult(conCon, sQuery);
		Driver.getResultsIDLnFnEm(rsResult);
		*/
		// ------------ Update ---------------------- //
		
		// ------------ Delete ---------------------- //
		/*
		Driver.deleteEntry(conCon, sQueryDelete);
		rsResult = Driver.setResult(conCon, sQuery);
		Driver.getResultsIDLnFnEm(rsResult);
		*/
		// ------------ Delete ---------------------- //
		
		// ------------ Prepared Statements ---------------------- //
		/*
		String sPrepStmt = "SELECT * FROM employees WHERE salary > ? AND department=?;";
		PreparedStatement psPrepStmt = null;
		psPrepStmt = Driver.prepStatement(conCon, sPrepStmt);
		// Set the parameters for the prepared statement
		psPrepStmt.setDouble(1, 80000);
		psPrepStmt.setString(2, "Legal");
		
		Driver.getResultsIDLnFnEm(Driver.getResultsPrepStatement(psPrepStmt));
		*/
		// ------------ Prepared Statements ---------------------- //
		
		// ------------ Stored Procedure ---------------------- //
		
		
		// ------------ Stored Procedure ---------------------- //
	}

}
