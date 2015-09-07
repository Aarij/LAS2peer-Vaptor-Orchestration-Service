package i5.las2peer.services.orchestration.database;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * Establishes connection to the MySql database.
 * Uses 'dbconnection.properties' file for configuration.
 *
 */
public class DatabaseManager
{
	private final static String INPUT_FILE = "etc/i5.las2peer.services.videoAdapter.dbconnection.properties";
	private static String url;
	private static String host;
	private static String port;
	private static String dbName;
	private static String driver;
	private static String userName;
	private static String password;
	private static String databaseServer;
	
	public void init(String driver, String databaseServer, String port, String dbName, String userName, String password, String host) {

		System.out.println("DB CHECK: ");
		
		this.driver = driver;
		this.databaseServer = databaseServer;
		this.port = port;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		this.host = host;
		
		url = "jdbc:" + this.databaseServer + "://" + this.host +":"+ this.port + "/";
		
		System.out.println("DB URL: "+url);
	}
	
	
	public int getWeight(int edgeId){
		
		int weight=0;
		ResultSet res = null;
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String query = "SELECT weight FROM weight WHERE edge_id=?";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, edgeId);
			res = pstmt.executeQuery();

			if(res.next()){
				weight = res.getInt("weight");
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weight;
	}
	
	public void updateWeight(int edgeId, int weight){
		
		int rowCount = 0;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "UPDATE weight SET weight=? WHERE edge_id=?";
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setInt(1, weight);
			pstmt.setInt(2, edgeId);
		
			rowCount = pstmt.executeUpdate();
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public JSONArray getAnalyticsByEdge(int edgeId){
		
		ResultSet res = null;
		JSONArray analytics = new JSONArray();
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String query = "SELECT * FROM analytics WHERE edge_id=?";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, edgeId);
			res = pstmt.executeQuery();

			
			while(res.next()){
				
				JSONObject analyticsObject = new JSONObject();
				//analyticsObject.put("edge",edgeId);
				
				analyticsObject.put("sub",res.getInt("sub"));
				analyticsObject.put("view duration",res.getInt("view_duration"));
				analyticsObject.put("no of views",res.getInt("no_of_views"));
				
				analytics.put(analyticsObject);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return analytics;
	}
	
	
	public JSONArray getAnalyticsByUser(int sub){
		
		ResultSet res = null;
		JSONArray analytics = new JSONArray();
		
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String query = "SELECT * FROM analytics WHERE sub=?";
			
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, sub);
			res = pstmt.executeQuery();

			
			while(res.next()){
				
				JSONObject analyticsObject = new JSONObject();
				
				analyticsObject.put("edge",res.getInt("edge_id"));
				analyticsObject.put("weight",getWeight(res.getInt("edge_id")));
				analyticsObject.put("view duration",res.getInt("view_duration"));
				analyticsObject.put("no of views",res.getInt("no_of_views"));
				
				analytics.put(analyticsObject);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return analytics;
	}
	

	/*public void saveSearch(String searchString, String result, String username){
		
		int rowCount = 0;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "insert into searches (query, result, user) values (?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, searchString);
			pstmt.setString(2, result);
			pstmt.setString(3, username);
			
			rowCount = pstmt.executeUpdate();
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	
	
	
	
	
}	