package jdbcdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BlobHandler {
	
	/* To Do
	 * Improve by adding another constructor to sort file type, on DB add another field for file type
	 *  or create a method to check file type 
	 *  
	 *  Add field for file name
	 *  
	 */
	public static void ImportBlobPDF( String FileLocation, String email ) throws IOException, SQLException{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		FileInputStream input = null;
		String status = "";
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");

			// 2. Prepare statement
			//String sql = "update employees set resume=? where email='john.doe@foo.com'";
			//String sql = "update employees set resume=?, fileName=? where email='barrientos.yas@gmail.com'";
			String sql = "update employees set resume=? where email=?";

			myStmt = myConn.prepareStatement(sql);
			
			// 3. Set parameter for resume file name
			File theFile = new File(FileLocation);
			input = new FileInputStream(theFile);
			myStmt.setBinaryStream(1, input);
			myStmt.setString(2, email);
			//myStmt.setString(2, theFileName);
			
			System.out.println("Reading input file: " + theFile.getAbsolutePath());
			
			// 4. Execute statement
			System.out.println("\nStoring resume in database: " + theFile);
			System.out.println(sql);
			
			myStmt.executeUpdate();
			
			System.out.println("\nCompleted successfully!");
			
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {			
			if (input != null) {
				input.close();
			}
			
			close(myConn, myStmt);			
		}
		
	}
	
	private static void ExportBlobPDF ( String email ) throws IOException, SQLException {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		InputStream input = null;
		FileOutputStream output = null;

		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/demo", "yas", "password");

			// 2. Execute statement
			
			//String sql = "select resume from employees where email='john.doe@foo.com'";
			myStmt = myConn.createStatement();
			//String sql = "select resume from employees where email='john.doe@foo.com'";
			String sql = "select first_name, last_name, resume from employees where email='barrientos.yas@gmail.com'";
			myRs = myStmt.executeQuery(sql);
			
			
			// Create Date format
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
			String dateFormatted = sdf.format(cal.getTime());
						
			if (myRs.next()) {

				// 3. Set up a handle to the file	
				String parseFileName = "Exported_" + myRs.getString("first_name") + myRs.getString("last_name") + dateFormatted;
				File theFile = new File(parseFileName + ".pdf");
				output = new FileOutputStream(theFile);
				
				// export data
				input = myRs.getBinaryStream("resume"); 
				System.out.println("Reading resume from database...");
				System.out.println(sql);
				
				byte[] buffer = new byte[1024];
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}
				
				System.out.println("\nSaved to file: " + theFile.getAbsolutePath());
				
				System.out.println("\nCompleted successfully!");				
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (input != null) {
				input.close();
			}

			if (output != null) {
				output.close();
			}
			
			close(myConn, myStmt);
		}
	
	}
	
	
private static void close(Connection myConn, Statement myStmt)
		throws SQLException {

	if (myStmt != null) {
		myStmt.close();
	}
	
	if (myConn != null) {
		myConn.close();
	}
}

	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("Start method to import pdf resume");
		String userEmailToImportPDF = "barrientos.yas@gmail.com";
		BlobHandler.ImportBlobPDF("Yasmin_Barrientos_Dev2016.pdf", userEmailToImportPDF);
		
		System.out.println("Start Method to Export pdf resume");
		String userEmailToExportPDF = "barrientos.yas@gmail.com"; 
		BlobHandler.ExportBlobPDF(userEmailToExportPDF);
		
	}

}
