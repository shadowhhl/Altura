package data;

import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author shadowhhl
 * DB is responsible for database connection, query, management, and other stuffs.
 */
public class Db {

	private Connection connection;
	private ResultSet resultSet;
	
	ArrayList< HashMap<String, String> > resultList;
	
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://dbw.cs.columbia.edu:3306/hldb", 
					"hailiang", "hailiang123");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void query(String sqlStmt) {
		try {
			if (connection.isValid(0)) {
				Statement stmt = connection.createStatement();
				resultSet = stmt.executeQuery(sqlStmt);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public int getCount() {
		try {
			resultSet.first();
			return resultSet.getInt("count(*)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public ArrayList< HashMap<String, String> > parseResult() {
		resultList = new ArrayList<HashMap<String,String>>();
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			resultSet.first();
			while (resultSet.next()) {
				HashMap<String, String> row = new HashMap<String, String>();
				for (int i=0;i<colCount;i++) {
					row.put(rsmd.getColumnName(i+1), resultSet.getString(i+1));
				}
				resultList.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	public boolean writeResultSet2Csv() {
		return writeResultSet2Csv(resultSet);
	}
	
	/**
	 * @param rs
	 * @return boolean
	 * writeCSV(ResultSet rs) writes the result into a CSV file
	 */
	public boolean writeResultSet2Csv(ResultSet rs) {
		SimpleDateFormat dateFileName = new SimpleDateFormat("yy_MM_dd_HHmmss");
			//Initialize file name
			//File is named after the time generated
		Date dNow = new Date();
		String fileName = dateFileName.format(dNow)+".csv";
		
		try {
			PrintWriter csvOut = new PrintWriter(new File(fileName).getAbsoluteFile());
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				String title="";
				for (int i=0;i<colCount-1;i++) {
					title = title.concat(rsmd.getColumnName(i+1)).concat(",");
				}
				title = title.concat(rsmd.getColumnName(colCount));
				csvOut.println(title);							//Write the first row, the header row, into the CSV file
				
				rs.first();
				while (rs.next()) {										//If the ResultSet has more records
					String rowStr = "";
					for (int i=0;i<colCount-1;i++) {
						rowStr = rowStr.concat(rs.getString(i+1)).concat(",");
					}
					rowStr=rowStr.concat(rs.getString(colCount));
					csvOut.println(rowStr);
				}
			} catch (Exception e) {
				e.printStackTrace();				//Handle exception
				throw(e);
			} finally {
				csvOut.close();						//Close the file
			}
		} catch (Exception e) {
			e.printStackTrace();					//Handle exception
			return false;							//Return false if any exception is generated
		} 
		return true;								//Return true if no exception is generated and the CSV file is good
	}
}
