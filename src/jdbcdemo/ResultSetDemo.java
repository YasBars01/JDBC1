/*
 * Java JDBC Tutorial – Part 9: JDBC ResultSet MetaData with MySQL 
 * https://www.youtube.com/watch?v=JyMkANs3vhI&list=PLEAQNNR8IlB4R7NfqBY1frapYo97L6fOQ&index=13
 */

package jdbcdemo;

import java.sql.*;

public class ResultSetDemo {

	public static void close ( Connection conConn, Statement myStmt, ResultSet rs) 
		throws SQLException {
		if ( conConn != null ){
			conConn.close();
		}
		
		if ( myStmt != null ){
			myStmt.close();
		}
		
		if ( rs != null ){
			rs.close();
		}
	}
	

	public static void main(String[] args)throws SQLException {

		Connection conConn = null;
		Statement myStmt = null;
		ResultSet rs = null;

		try {
			
			conConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");
			
			myStmt = conConn.createStatement();
			rs = myStmt.executeQuery("SELECT id, last_name, first_name, salary "
					+ "FROM employees;");
			
			// get ResultSet MetaData
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			// Display info
			int columnCount = rsMetaData.getColumnCount();
			System.out.println("Column count: " + columnCount + "\n" );
			
			for ( int column = 1; column <= columnCount; column++){
				System.out.println("Column name: " + rsMetaData.getColumnName(column));
				System.out.println("Column type name: " + rsMetaData.getColumnTypeName(column));
				System.out.println("Is Nullable: " + rsMetaData.isNullable(column) + "\n");
				System.out.println("Is Auto Increment: " + rsMetaData.isAutoIncrement(column) + "\n");
			}
			
		} catch ( Exception e ){
			e.printStackTrace();
		} finally {
			close(conConn, myStmt, rs);
		}
		

	}

}
