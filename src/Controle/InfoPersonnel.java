package Controle;


import Main.ConnexionBDD;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;



public class InfoPersonnel implements Initializable {

    @FXML
    public Label numContrat;
    @FXML
    private Label prénom;
    @FXML
    private Label nom;

    public static String n;
    public Connection cnx;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    try {
       
        String sql ="select NumContrat,Nom,Prenom from personnel where NumContrat='"+n+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            numContrat.setText(rs.getString("NumContrat"));
            nom.setText(rs.getString("Nom"));
            prénom.setText(rs.getString("Prenom"));
        }
        cnx.close();
        
    } catch (SQLException ex) {
        Logger.getLogger(InfoPersonnel.class.getName()).log(Level.SEVERE, null, ex);
    }
    } 
      @FXML
    private void show(MouseEvent event) throws IOException 
    {
        ListePersonnel.num=numContrat.getText();
        SuiviPersonnel.num=numContrat.getText();
    }
}
      
        
    
        
  