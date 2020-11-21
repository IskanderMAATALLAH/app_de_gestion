/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import Reports.ContratOP;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class ReportController implements Initializable {
 Connection cnx;
    @FXML
    private JFXComboBox<String> Agent;
    @FXML
    private JFXTextField Nature;
    @FXML
    private JFXTextField PDR;
    @FXML
    private JFXTextField Carb;
    @FXML
    private JFXTextField Horaires;
    @FXML
    private JFXTextField Resultat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    String sql = "Select NumContrat,Nom,Prenom from personnel where IdPoste=3";
    Statement st;
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            st = cnx.createStatement();
            ResultSet rs= st.executeQuery(sql);
            while (rs.next())
            {
               Agent.getItems().add(rs.getString("NumContrat")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom")); 
            }
            cnx.close();
        } catch (SQLException ex) {
            
        }
    }    

    @FXML
    private void ajouter(ActionEvent event) {
        String agent = Agent.getSelectionModel().getSelectedItem();
        String tab [] = new String [3];
        tab=agent.split("  ");
        String nature = Nature.getText();
        String Pdr = PDR.getText();
        String carb = Carb.getText();
        String horaire = Horaires.getText();
        String result = Resultat.getText();
        String sql ="insert into rapportmaintenance (NumRapport,Date,Agent,Nature,PDR,Carb,Horaires,Resultat,IdManege)values (null,CURRENT_DATE(),'"+tab[0]+"','"+nature+"','"+Pdr+"','"+carb+"','"+horaire+"','"+result+"','"+ListeManège.numManege+"')";
          Statement st;
     try {
         cnx=new ConnexionBDD().ConnexionDB();
         st = cnx.createStatement();
         st.executeUpdate(sql);
         cnx.close();
     } catch (SQLException ex) {
         Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
     }
     Agent.getSelectionModel().clearSelection();
     Nature.setText("");
     PDR.setText("");
     Carb.setText("");
     Horaires.setText("");
     Resultat.setText("");
    }

    @FXML
    private void annuler(ActionEvent event) {
    }

  
    
}
