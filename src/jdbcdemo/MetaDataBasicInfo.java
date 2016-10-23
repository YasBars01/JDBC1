package jdbcdemo;

import java.sql.*;

public class MetaDataBasicInfo {

	public static void close (Connection myConn) throws SQLException {
		if ( myConn != null){
			myConn.close();
		}
		
	}
	
	public static void main(String[] args) throws SQLException {
		
		Connection conConn = null;
		
		try{
			
			// Get connection to database
			conConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");
			
			// GET the METADATA
			DatabaseMetaData dbMetaData = conConn.getMetaData();
			
			// Display info about database
			System.out.println("Product name: " + dbMetaData.getDatabaseProductName());
			System.out.println("Product version: " + dbMetaData.getDatabaseProductVersion());
			System.out.println();
			
			// Display info about JDBC Drver
			System.out.println("JDBC Driver name: " + dbMetaData.getDriverName());
			System.out.println("JDBC Driver version: " + dbMetaData.getDriverVersion());
			
		} catch ( Exception e ){
			e.printStackTrace();
		} finally {
			close(conConn);
		}

	}

}
