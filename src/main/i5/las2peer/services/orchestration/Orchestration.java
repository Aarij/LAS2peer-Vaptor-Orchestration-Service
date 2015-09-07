package i5.las2peer.services.orchestration;

import i5.las2peer.api.Service;
import i5.las2peer.restMapper.HttpResponse;
//import i5.las2peer.restMapper.MediaType;
import i5.las2peer.restMapper.RESTMapper;
import i5.las2peer.restMapper.annotations.ContentParam;
import i5.las2peer.restMapper.annotations.GET;
//import i5.las2peer.restMapper.annotations.POST;
import i5.las2peer.restMapper.annotations.Consumes;
import i5.las2peer.restMapper.annotations.POST;
import i5.las2peer.restMapper.annotations.Path;
import i5.las2peer.restMapper.annotations.PathParam;
import i5.las2peer.restMapper.annotations.Produces;
import i5.las2peer.restMapper.annotations.QueryParam;
import i5.las2peer.restMapper.annotations.Version;
import i5.las2peer.restMapper.annotations.swagger.ApiInfo;
import i5.las2peer.restMapper.annotations.swagger.ApiResponse;
import i5.las2peer.restMapper.annotations.swagger.ApiResponses;
import i5.las2peer.restMapper.annotations.swagger.Summary;
import i5.las2peer.restMapper.tools.ValidationResult;
import i5.las2peer.restMapper.tools.XMLCheck;
import i5.las2peer.services.orchestration.database.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.arangodb.entity.GraphEntity;


















//import i5.las2peer.services.videoCompiler.idGenerateClient.IdGenerateClientClass;
//import org.junit.experimental.theories.ParametersSuppliedBy;
//import com.sun.jersey.multipart.FormDataParam;
//import com.sun.jersey.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * LAS2peer Service
 * 
 * 
 * 
 * 
 */
@Path("vaptor")
@Version("0.1")
@ApiInfo(title = "Orchestration Service", 
	description = "<p>A RESTful service for Segment analytics for Vaptor.</p>", 
	termsOfServiceUrl = "", 
	contact = "siddiqui@dbis.rwth-aachen.de", 
	license = "MIT", 
	licenseUrl = "") 
	
public class Orchestration extends Service {

	private String port;
	//private String host;
	private String username;
	private String password;
	private String database;
	private String databaseServer;
	private String driverName;
	private String hostName;
	private String useUniCode;
	private String charEncoding;
	private String charSet;
	private String collation;
	
	private String analyticsService;
	private String adapterService;
	private String userPreferenceService;
	
	

	//private DatabaseManager dbm;
	private String epUrl;
	
	GraphEntity graphNew;
	
	

	public Orchestration() {
		// read and set properties values
		setFieldValues();

		if (!epUrl.endsWith("/")) {
			epUrl += "/";
		}
		// instantiate a database manager to handle database connection pooling
		// and credentials
		// dbm = new DatabaseManager(username, password, host, port, database);
	}

	/*@GET
	@Path("analytics/weight")
	public HttpResponse getWeight(@QueryParam(name="edge" , defaultValue = "*") int edge){

		dbm = new DatabaseManager();
		dbm.init(driverName, databaseServer, port, database, this.username, password, hostName);
		
		int weight = dbm.getWeight(edge);
		
		HttpResponse r = new HttpResponse(Integer.toString(weight));
		r.setStatus(200);
		return r;
	}*/
	
	// Orchestration for Adapter Service
		
	@GET
	@Path("adapter/getPlaylist")
	public String getPlaylist(@QueryParam(name="sub" , defaultValue = "*") String subId, 
			@QueryParam(name="username" , defaultValue = "*") String username, 
			@QueryParam(name = "search", defaultValue = "*" ) String searchString){
		
		CloseableHttpResponse response = null;
		URI request = null;
		StringBuilder content=null;
		
		try {
			request = new URI(adapterService+"/getPlaylist?sub="+subId+"&username="+username+
					"&search="+searchString.replaceAll(" ", ","));
		
		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(request);
			
			response = httpClient.execute(get);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			content = new StringBuilder();
			String line;
			
			while (null != (line = rd.readLine())) {
			    content.append(line);
			}
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content.toString();
	}

	// Orchestration for User Preference Service 
	
	@POST
	@Path("preference")
	
	public String postPreferences(@ContentParam StringEntity jsonPreferences){
		
		CloseableHttpResponse response = null;
		URI request = null;
		StringBuilder content=null;
		
		try {
			request = new URI(userPreferenceService);
		
		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(request);
			
			post.setEntity(jsonPreferences);
			
			response = httpClient.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			content = new StringBuilder();
			String line;
			
			while (null != (line = rd.readLine())) {
			    content.append(line);
			}
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content.toString();
	}
	
	
	@GET
	@Path("preference")
	public String getPreferences(@QueryParam(name="username" , defaultValue = "*") String username){
		
		CloseableHttpResponse response = null;
		URI request = null;
		StringBuilder content=null;
		
		try {
			request = new URI(userPreferenceService+"?username="+username);
		
		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(request);
			
			response = httpClient.execute(get);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			content = new StringBuilder();
			String line;
			
			while (null != (line = rd.readLine())) {
			    content.append(line);
			}
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content.toString();
	}
	
	
	@GET
	@Path("analytics/weight")
	public CloseableHttpResponse getWeight(@QueryParam(name="edge" , defaultValue = "*") int edge){
		
		CloseableHttpResponse response = null;
		URI request = null;
		StringBuilder content=null;
		
		try {
			request = new URI(analyticsService+"?edge="+edge);
		
			
		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(request);
			
			response = httpClient.execute(get);
			
			//BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			//content = new StringBuilder();
			//String line;
			
			/*while (null != (line = rd.readLine())) {
			    content.append(line);
			}*/
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	@GET
	@Path("")
	public CloseableHttpResponse getAnalytics(@QueryParam(name="key" , defaultValue = "*") String key, 
			@QueryParam(name="value" , defaultValue = "0") int value){
		
		CloseableHttpResponse response = null;
		URI request = null;
		StringBuilder content=null;
		
		try {
			request = new URI(analyticsService+"?key="+key+"&value="+value);
		
		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(request);
			
			response = httpClient.execute(get);
			
			//BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			//content = new StringBuilder();
			//String line;
			
			/*while (null != (line = rd.readLine())) {
			    content.append(line);
			}*/
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	
	// ================= Swagger Resource Listing & API Declarations
	// =====================

	@GET
	@Path("api-docs")
	@Summary("retrieve Swagger 1.2 resource listing.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Swagger 1.2 compliant resource listing"),
			@ApiResponse(code = 404, message = "Swagger resource listing not available due to missing annotations."), })
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse getSwaggerResourceListing() {
		return RESTMapper.getSwaggerResourceListing(this.getClass());
	}

	@GET
	@Path("api-docs/{tlr}")
	@Produces(MediaType.APPLICATION_JSON)
	@Summary("retrieve Swagger 1.2 API declaration for given top-level resource.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Swagger 1.2 compliant API declaration"),
			@ApiResponse(code = 404, message = "Swagger API declaration not available due to missing annotations."), })
	public HttpResponse getSwaggerApiDeclaration(@PathParam("tlr") String tlr) {
		return RESTMapper.getSwaggerApiDeclaration(this.getClass(), tlr, epUrl);
	}

	/**
	 * Method for debugging purposes. Here the concept of restMapping validation
	 * is shown. It is important to check, if all annotations are correct and
	 * consistent. Otherwise the service will not be accessible by the
	 * WebConnector. Best to do it in the unit tests. To avoid being
	 * overlooked/ignored the method is implemented here and not in the test
	 * section.
	 * 
	 * @return true, if mapping correct
	 */
	public boolean debugMapping() {
		String XML_LOCATION = "./restMapping.xml";
		String xml = getRESTMapping();

		try {
			RESTMapper.writeFile(XML_LOCATION, xml);
		} catch (IOException e) {
			e.printStackTrace();
		}

		XMLCheck validator = new XMLCheck();
		ValidationResult result = validator.validate(xml);

		if (result.isValid())
			return true;
		return false;
	}

	/**
	 * This method is needed for every RESTful application in LAS2peer. There is
	 * no need to change!
	 * 
	 * @return the mapping
	 */
	public String getRESTMapping() {
		String result = "";
		try {
			result = RESTMapper.getMethodsAsXML(this.getClass());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

}
