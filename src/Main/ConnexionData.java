package Main;


import java.sql.*;



public class ConnexionData {


  
   public static void main(String[] args) throws Exception{ 
       Connection cn =ConnecterDB();    
   }

   public static Connection ConnecterDB()  {
	   try
	   {
	   Class.forName("com.mysql.jdbc.Driver");
	   System.out.println("Driver OK !");
	   String url="jdbc:mysql://localhost:3306/parcdattractions";
	   String user = "root";
	   String Password="root";
	   Connection cnx = DriverManager.getConnection(url,user,Password);
	   System.out.print("Connection bien etablie ");
	   return cnx;

	   }catch (Exception e) {e.printStackTrace();
		return null;   
	   }
	   
	   
	  
   }
   
   
   
   
   
   
   
}