/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import Main.MainProjet;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class SecretaireController implements Initializable {
    Connection cnx ; 
    Statement pst ;
    ResultSet rs;
    @FXML
    private Pane paneSec;
    @FXML
    private AnchorPane sec;
    public static String numSec ;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    try {
        initi();
    } catch (IOException ex) {
        Logger.getLogger(SecretaireController.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }    

    public void initi() throws IOException
    {
          Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Rendez-vous.fxml"));
       paneSec.getChildren().clear();
        paneSec.getChildren().add(root); 
    }
    @FXML
    private void rendez(ActionEvent event) throws IOException {
         initi();
    }

    @FXML
    private void docs(ActionEvent event) throws IOException {
          Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Document.fxml"));
       paneSec.getChildren().clear();
        paneSec.getChildren().add(root); 
    }

   /* @FXML
    private void accueil(ActionEvent event) throws IOException {
             Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Secretaire.fxml"));
       sec.getChildren().clear();
        sec.getChildren().add(root);   
          }
    
    private void info()
    {
        try {
        String nbr_rdv = "SELECT count(*) as nbr FROM `rdv` where Date > CURRENT_TIMESTAMP()";
        String proch_date="select Min(Date) as proche from rdv where Date > CURRENT_TIMESTAMP()";
        cnx=new ConnexionBDD().ConnexionDB();
        PreparedStatement ps = cnx.prepareStatement(nbr_rdv);
        rs = ps.executeQuery();
        while(rs.next())
        {
            String result = rs.getString("nbr");
            rdvvenir.setText(result);
        }
          ps = cnx.prepareStatement(proch_date);
        rs = ps.executeQuery();
        while(rs.next())
        {
            String result = rs.getString("proche");
            prochdate.setText(result);
        }
        cnx.close();
    } catch (SQLException ex) {
        Logger.getLogger(SecretaireController.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }*/

    @FXML
    private void LogOut(MouseEvent event) throws Exception {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        MainProjet m = new MainProjet();
        Stage stagee = new Stage();
        m.start(stagee);
    }
    
}
