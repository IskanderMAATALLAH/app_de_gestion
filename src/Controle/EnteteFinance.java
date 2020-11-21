/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import static Controle.Caisses.IdCaisse;
import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class EnteteFinance implements Initializable {
 Connection cnx =new ConnexionBDD().ConnexionDB();
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private Pane panee;
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane pane;
    @FXML
    private HBox hBoxEntete;
    @FXML
    private JFXButton rev_dep;
    @FXML
    private JFXButton stat;
    @FXML
    private Pane paneFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      listecaisses();
 //     hBoxEntete.setStyle("-fx-background-color: linear-gradient( #454960, #EAEDF1);");
      
      Pane root;
     try {
         root = FXMLLoader.load(getClass().getResource("/Interfaces/DepensesRevenusDG.fxml"));
         panee.getChildren().clear();
         panee.getChildren().add(root);
     } catch (IOException ex) {
         Logger.getLogger(EnteteFinance.class.getName()).log(Level.SEVERE, null, ex);
     }
      
    }    
     public void listecaisses()
    { 
        vBox.getChildren().clear();
          String sql="SELECT IdCaisse FROM caisse";
     
        try {
            cnx =new ConnexionBDD().ConnexionDB();
            Statement st =cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next())
            {  
                IdCaisse=rs.getString("IdCaisse");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/InfoCaisseDG.fxml"));
                vBox.getChildren().add(pane);
                
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void ouvrirAccueil(ActionEvent event) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/MainDG.fxml"));
        pane.getChildren().clear();
        pane.getChildren().add(root);
    }

    @FXML
    private void rev_depAction(ActionEvent event) {
        
    }

    @FXML
    private void statAction(ActionEvent event) {
    }
}
