package jdbcdemo;

import java.sql.*;
import java.math.*;

public class GreetTheDepartment {

	public static void main( String[] args ) throws Exception{
		
		Connection conCon = null;
		CallableStatement myStmt = null;
		
		try{
			conCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "yas", "password");
			
			String theDepartment = "Engineering";

			// prepare the stored procedure call
			// ? for the placeholder
			myStmt = conCon.prepareCall("{call greet_the_department(?)}");
			
			// set the parameters
			myStmt.registerOutParameter(1, Types.VARCHAR);
			myStmt.setString(1, theDepartment);
			
			// Call Stored Procedure
			System.out.println("Calling Stored procedure. greet_the_department('" + theDepartment + "')");
			myStmt.execute();
			System.out.println("Finished calling stored procedure");
			
			// Get the value of the INOUT parameter
			String theResult = myStmt.getString(1);
			
			System.out.println("\nThe result = " + theResult);
			
		} catch ( Exception exc ){
			exc.printStackTrace();
		}
	}
	
}
