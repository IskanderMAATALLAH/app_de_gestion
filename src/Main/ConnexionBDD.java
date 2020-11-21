package Main;

import java.sql.Connection;
import java.sql.DriverManager;



public class ConnexionBDD {
    
    private Connection cnx;
    
    public ConnexionBDD()
    {
       
    }
    
    public Connection ConnexionDB()
    {
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            cnx=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/parcdattractions","root","");
            System.out.println("Connexion établie...");
            
        } catch (Exception ex) {
            
        }
        return cnx;
    }
    
}
    
