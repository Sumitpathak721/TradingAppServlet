package auth;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.getWriter().append("login Page");
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // Adjust allowed methods if needed
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Adjust allowed headers if needed
	    
	    
		Map<String,Object> data = new HashMap<>();
		try {
			Enumeration<String> attributeNames = request.getParameterNames();
			while (attributeNames.hasMoreElements()) {
			    String attributeName = attributeNames.nextElement();
			    Object attributeValue = request.getParameter(attributeName);

			    // Print the name and value
			    System.out.println(attributeName + " = " + attributeValue);
			}
	        String email = request.getParameter("email");
	        String pass = request.getParameter("password");
	        
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/tradingapp","root","root");
			Statement stmt=con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users where email='"+email+"' and password='"+pass+"';");
			
			if(rs.next()) {
				
//				Mapping user data to JSON
				Map<String,Object> userData = new HashMap<>();
				userData.put("userId", rs.getInt("id"));
				userData.put("name", rs.getString("name"));
				
				Gson GsonObj = new Gson();
				data.put("user", GsonObj.toJson(userData));
				data.put("status","200");
				
				
		        con.close();   
			}else {
				data.put("status","401");
			}
		}catch(Exception e) {
			System.out.print(e);
			data.put("status","500");
		}
		String jsonData = new Gson().toJson(data);
		PrintWriter out = response.getWriter();
        out.print(jsonData);
	}

}
