package jdbcdemo;

import java.sql.*;

public class IncreaseSalariesForDepartment {

	private static ResultSet myRs = null;
	
	private static Connection setConnection (String db, String root, String password){
		Connection myConn = null;
		try{
			myConn = DriverManager.getConnection(db, root, password);
			
		} catch ( SQLException exc ){			
			System.out.println("Connection is caput!");
			exc.printStackTrace();
		}
		return myConn;
	}
	

	private static void printSalaries(Connection myConn, String theDepartment) throws SQLException {
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			// Prepare statement
			myStmt = myConn
					.prepareStatement("select * from employees where department=?");

			myStmt.setString(1, theDepartment);
			
			// Execute SQL query
			myRs = myStmt.executeQuery();

			// Process result set
			while (myRs.next()) {
				String lastName = myRs.getString("last_name");
				String firstName = myRs.getString("first_name");
				double salary = myRs.getDouble("salary");
				String department = myRs.getString("department");
				
				//System.out.printf("%s, %s, %s, %.2f\n", lastName, firstName, department, salary);
				System.out.println(lastName + ", " + firstName + ", " + department + ", " + salary );
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myStmt, myRs);
		}

	}

	private static void close(Connection myConn, Statement myStmt,
			ResultSet myRs) throws SQLException {
		if (myRs != null) {
			myRs.close();
		}

		if (myStmt != null) {
			myStmt.close();
		}

		if (myConn != null) {
			myConn.close();
		}
	}

	private static void close(Statement myStmt, ResultSet myRs)
			throws SQLException {

		close(null, myStmt, myRs);
	}

	
	public static void main(String[] args) throws SQLException {
		String db = "jdbc:mysql://localhost:3306/demo";
		String root = "yas";
		String password = "password";
		
		CallableStatement myStmt = null;
		Connection con = IncreaseSalariesForDepartment.setConnection(db, root, password);
				
		String theDepartment = "Engineering";
		int theIncreaseAmount = 10000;
		
		// Show salaries BEFORE
		System.out.println("Salaries BEFORE\n");
		IncreaseSalariesForDepartment.printSalaries(con, theDepartment);
		
		// prepare the stored procedure call
		myStmt = 
				con.prepareCall("{call increase_salaries_for_department(?, ?)}");
		
		// Set the parameters
		myStmt.setString(1, theDepartment);
		myStmt.setDouble(2, theIncreaseAmount);
		
		// Call Stored procedure
		System.out.println("\n\nCalling stored procedure.  increase_salaries_for_department('" + theDepartment + "', " + theIncreaseAmount + ")");
		myStmt.execute();
		System.out.println("Finished calling stored procedures");
		
		// Show Salaries AFTER
		System.out.println("\n\nSalaries AFTER");
		IncreaseSalariesForDepartment.printSalaries(con, theDepartment);
		
		close(con, myStmt, null);
		
	}
	
	

}
