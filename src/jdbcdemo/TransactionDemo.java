package jdbcdemo;

import java.sql.*;
import java.util.Scanner;

public class TransactionDemo {
	/**
	 * Prompts the user, to save or not to save. Return true if they Enter "yes", false otherwise
	 * 
	 * @return
	 */
	private static boolean askUserIfOkToSave(){
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Is it okay to save changes? yes/no: ");
		String input = scanner.nextLine();
		
		scanner.close();
		return input.equalsIgnoreCase("yes");
	}
	
	private static void showSalaries(Connection conConn, String theDepartment)
			throws SQLException {
		
		PreparedStatement myStmt = null;
		ResultSet rs = null;
		
		System.out.println("Show Salaries for Department: " + theDepartment);
		
		try {
			// Prepare Statement
			myStmt = conConn
					.prepareStatement("SELECT * FROM employees WHERE department=?;", 
							  ResultSet.TYPE_SCROLL_INSENSITIVE, 
							  ResultSet.CONCUR_READ_ONLY);
			
			myStmt.setString(1, theDepartment);
			
			// Execute SQL query
			rs = myStmt.executeQuery();
			
			// Process result set
			while ( rs.next() ){
				String lastName = rs.getString("last_name");
				String firstName = rs.getString("first_name");
				double salary = rs.getDouble("salary");
				String department = rs.getString("department");
				
				System.out.printf("%s, %s, %s, %.2f\n", 
							lastName, firstName, department, salary);
			}
			rs.last();
			int count = rs.getRow();
			//rs.beforeFirst();

			System.out.println( "--- Found " + count + " row(s) ---");
		} catch ( Exception e ){
			e.printStackTrace();
			
		} finally {
			close(myStmt, rs);
		}
		// extra space after Print of 1 Department
		System.out.println();
	}
	
	private static void close ( Connection myConn, Statement myStmt, 
			ResultSet myRs) throws SQLException {
		if ( myRs != null ){
			myRs.close();
		}
		
		if ( myStmt != null ){
			myStmt.close();
		}
		
		if ( myConn != null ) {
			myConn.close();
		}
		
	}
	
	private static void close( Statement myStmt, ResultSet myRs )
		throws SQLException {
		
		close(null, myStmt, myRs);
	}
	
	
	public static void main(String[] args) throws SQLException {
		
		Connection conConn = null;
		Statement myStmt = null;
		
		try {
			// 1. Get a connection to database
			conConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");
			
			
			conConn.setAutoCommit(false);
			
			// Show Salaries before changes
			System.out.println("Salaries BEFORE\n");
			showSalaries(conConn, "HR");
			showSalaries(conConn, "Engineering");
			
			// Transaction Step 1: Delete all HR employees
			myStmt = conConn.createStatement();
			myStmt.executeUpdate("DELETE FROM employees WHERE department='HR';");
			
			
			// Transaction Step 2: Set salaries to 300000 for all Engineering Employees
			myStmt.executeUpdate("UPDATE employees set salary=300000"
							    +" WHERE department='engineering';");
			
			System.out.println("\n>> Transaction steps are ready. \n");
			
			//Ask user if it is okay to save
			boolean ok = askUserIfOkToSave();
			
			if (ok) {
				// store in database
				conConn.commit();
				System.out.println("\n>> Transaction COMMITTED. \n");
			} else {
				// discard
				conConn.rollback();
				System.out.println("\n>> Transaction ROLLED BACK. \n");
			}
			
			// Show salaries AFTER
			System.out.println("Salaries AFTER\n");
			showSalaries(conConn, "HR");
			showSalaries(conConn, "Engineering");
			
			
		} catch ( Exception e ){
			e.printStackTrace();
		} finally{
			close(conConn, myStmt, null);
		}

	}

}

/*
 * NOTE:
 * When getting "Lock wait timeout exceeded; try restarting transaction" On Update or Delete, run 
 * "SHOW ENGINE INNODB STATUS"
 * 
 */


