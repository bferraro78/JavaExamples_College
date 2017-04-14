import java.sql.*;
import java.util.Scanner;
import org.json.simple.*;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class JSONProcessing 
{
	public static void processJSON(String json) {
		System.out.println("-------- PostgreSQL " + "JDBC Connection Testing ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/flightsskewed","vagrant", "vagrant");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
            return;
        }


        System.out.println("Adding data from " + json + " into the database");
        
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(json);

			JSONObject jsonObject = (JSONObject) obj;

			JSONObject newCUSTorFligtinfo = (JSONObject) jsonObject.get("newcustomer");
			
			boolean custbool = true;
			boolean flightbool = true;
			String ff_id = "";

			if (newCUSTorFligtinfo != null) { // NEW CUSTOMER INFO
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;

				String custid = (String) newCUSTorFligtinfo.get("customerid");
			 	System.out.println("Customer ID: " + custid);
		        
		        // SEES IF CUSTOMER ID EXISTS in CUSTOMERS (BUG 1)
		        try {
		        	String query = "select * from customers where customerid = ?";
		            stmt = connection.prepareStatement(query);
					stmt.setString(1, custid);
		            ResultSet rs = stmt.executeQuery();
		            if (rs.next()) {
		            	custbool = false;
		            	throw new SQLException();
		            }
		            stmt.close();
		        } catch (SQLException e ) {
		            System.out.println(e);
		            System.out.println("CUST ID ALREADY EXIST in Customers");
		        }

		        // CHECKS TO SEE IF FREQUENT FLIER EXISTS (BUG 2)
		        String ff = (String) newCUSTorFligtinfo.get("frequentflieron");
			 	System.out.println("Airlines name: " + ff);
			 	try {
			 		String query2 = "select * from airlines where name = ?";
		            stmt2 = connection.prepareStatement(query2);
					stmt2.setString(1, ff);
		            ResultSet rs2 = stmt2.executeQuery();
		            if (!rs2.next()) {
		            	flightbool = false;
		                throw new SQLException();
		            } else {
		            	ff_id = (String) rs2.getString("airlineid");
		            	System.out.println("FF ID:" + ff_id);
		            }
		            stmt2.close();
		        } catch (SQLException e) {
		            System.out.println(e);
		            System.out.println(ff + " DOES NOT EXIST in Airline");
		        }

		       	String name = (String) newCUSTorFligtinfo.get("name");
		       	String birthdate = (String) newCUSTorFligtinfo.get("birthdate");

		        // IF NOT ERRORS INSERT newcustomer in Customers
		        if (custbool == true && flightbool == true) {
		        	String insertTableSQL = "INSERT INTO CUSTOMERS" + "(customerid, name, birthdate, frequentflieron) VALUES" + "(?,?,?,?)";
		        	try {
		        		PreparedStatement ps = connection.prepareStatement(insertTableSQL);
		        		ps.setString(1, custid);
			        	ps.setString(2, name);
			        	ps.setDate(3, java.sql.Date.valueOf(birthdate));
			        	ps.setString(4, ff_id);	
			        	ps.executeUpdate();
		        	} catch (SQLException e) {
		        		System.out.println(e);
		        	}
		    	}

			} else { // FLIGHT INFO
				newCUSTorFligtinfo = (JSONObject) jsonObject.get("flightinfo");
				PreparedStatement stmt = null;
				PreparedStatement stmtflewon = null;

				String flightid = (String) newCUSTorFligtinfo.get("flightid");
			 	System.out.println("Flightid: " + flightid);

				String flightdate = (String) newCUSTorFligtinfo.get("flightdate");
			 	System.out.println("Flight Date: " + flightdate);			 	

				JSONArray customers = (JSONArray) newCUSTorFligtinfo.get("customers");

				 for (int i = 0; i < customers.size(); i++) { // FOR ALL CUSTOMERS, SEE IF THEY EXIST in Customers table
  		 			JSONObject cust = (JSONObject) customers.get(i);
  		 			String custid = (String) cust.get("customer_id");
  		 			
  		 			try {
			        	String query = "select * from customers where customerid = ?";
			            stmt = connection.prepareStatement(query);
						stmt.setString(1, custid);
			            ResultSet rs = stmt.executeQuery();
			            if (!rs.next()) { // add to customers table
			            	String name = (String) cust.get("name");
		       				String birthdate = (String) cust.get("birthdate");
		       				System.out.println("Birthdate" + birthdate);
		       				ff_id = (String) cust.get("frequentflieron");

			            	String insertTableSQL = "INSERT INTO CUSTOMERS" + "(customerid, name, birthdate, frequentflieron) VALUES" + "(?,?,?,?)";
				        	try {
				        		PreparedStatement ps = connection.prepareStatement(insertTableSQL);
				        		ps.setString(1, custid);
					        	ps.setString(2, name);
					        	ps.setDate(3, java.sql.Date.valueOf(birthdate));
					        	ps.setString(4, ff_id);	
					        	ps.executeUpdate();
				        	} catch (SQLException e) {
				        		System.out.println(e);
				        	}
			            }
			            stmt.close();
		        	} catch (SQLException e) {
		            	System.out.println(e);
		        	}

		        	// ADD CUSTOMER WITH FLYER-INFO TO flewon TABLE
		         	String insertTableSQL = "INSERT INTO FLEWON" + "(flightid, customerid, flightdate) VALUES" + "(?,?,?)";
				     try {
				     	PreparedStatement ps1 = connection.prepareStatement(insertTableSQL);
				        ps1.setString(1, flightid);
					    ps1.setString(2, custid);
					    ps1.setDate(3, java.sql.Date.valueOf(flightdate));	
					    ps1.executeUpdate();
				     } catch (SQLException e) {
				         System.out.println(e);
				     }



				 } // end for

			}		

		} catch (ParseException e) {
			e.printStackTrace();
		} 




	}

	public static void main(String[] argv) {
		Scanner in_scanner = new Scanner(System.in);

		while(in_scanner.hasNext()) {
			String json = in_scanner.nextLine();
			processJSON(json);
		}
	}
}
