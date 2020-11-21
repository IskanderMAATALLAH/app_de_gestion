package Controle;


import Main.ConnexionBDD;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class InterCaisse implements Initializable {
    
    public static int etat=0;
    public static String libelleCaisse=null;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    public Connection cnx;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
           
            vBox.getChildren().add(table());
            
            } catch (Exception ex) 
            {
                System.out.println(ex);
            }
                
            }
    
    public GridPane table() throws IOException, SQLException{
        
        GridPane table = new GridPane();
        Pane root1;
        
        
        int i=0,j=0;
        table.setVgap(5);
        table.setHgap(5);
        String sql="SELECT Libelle,Etat FROM caisse WHERE TypeCaisse='Entrée'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
            libelleCaisse=rs.getString("Libelle");
            etat=rs.getInt("Etat");
            if (i<3)
            {  
               root1=FXMLLoader.load(getClass().getResource("/Interfaces/detailCaisseEntrée.fxml"));
               table.add(root1,i,j);
               i++;
            }
            else
            {
                i=0;
                j++;
                root1=FXMLLoader.load(getClass().getResource("/Interfaces/detailCaisseEntrée.fxml"));
                table.add(root1,i,j); 
                i++;  
            }
            
        }
        
        sql="SELECT Libelle,Etat FROM caisse WHERE TypeCaisse='Interne'";
        rs = st.executeQuery(sql);
        
        while (rs.next())
        {
            libelleCaisse=rs.getString("Libelle");
            etat=rs.getInt("Etat");
            if (i<3)
            {  
               root1=FXMLLoader.load(getClass().getResource("/Interfaces/detailCaisseInterne.fxml"));
               table.add(root1,i,j);
               i++;
            }
            else
            {
                i=0;
                j++;
                root1=FXMLLoader.load(getClass().getResource("/Interfaces/detailCaisseInterne.fxml"));
                table.add(root1,i,j); 
                i++;  
            }
            
        }
   
        table.setAlignment(Pos.CENTER);
        cnx.close();
        return table;

    }
    
}
            
  
