package jdbcdemo;

import java.sql.*;

public class SchemaInfo {

	public static void close ( Connection myConn ) throws SQLException {
		if ( myConn != null ){
			myConn.close();
		}
	}
	
	public static void main ( String[] args) throws SQLException{
		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;
		String columnNamePattern = null;
		String[] types = null;
		
		Connection conConn = null;
		ResultSet rs = null;
		
		try {
			
			conConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");
			
			DatabaseMetaData dbMetaData = conConn.getMetaData();
			
			System.out.println("List of Tables");
			System.out.println("--------------");
			
			rs = dbMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
			
			while ( rs.next() ){
				System.out.println(rs.getString("TABLE_NAME"));
			}
			
			System.out.println("\n\nList of Columns");
			System.out.println("----------------");
			
			rs = dbMetaData.getColumns(catalog, schemaPattern, "employees", columnNamePattern);
			
			while ( rs.next() ){
				System.out.println( rs.getString("COLUMN_NAME") );
			}
			
			
		} catch ( Exception e ){
			e.printStackTrace();
		} finally {
			close(conConn);
		}
		
	}
	
}
