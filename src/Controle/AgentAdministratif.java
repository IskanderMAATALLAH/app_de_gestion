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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author OFFICE
 */
public class AgentAdministratif implements Initializable {
     Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
    private Label label;
    @FXML
    public AnchorPane pane;
    
    private void handleButtonAction(ActionEvent event) {
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try {
             new SerTechnique().inserDateJournée();
             ListePersonnel.choixDep=1;
             Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/listePersonnel.fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(root);
         } catch (SQLException ex) {
             Logger.getLogger(AgentAdministratif.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(AgentAdministratif.class.getName()).log(Level.SEVERE, null, ex);
         }
    }    

    @FXML
    private void pointaage(ActionEvent event) throws IOException {
         Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/PointageAdmin.fxml"));
       pane.getChildren().clear();
       pane.getChildren().add(root); 
    }

    @FXML
    private void personnel(ActionEvent event) throws IOException {
        ListePersonnel.choixDep=1;
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/listePersonnel.fxml"));
       pane.getChildren().clear();
       pane.getChildren().add(root); 
    }


    @FXML
    private void ouvrManège(ActionEvent event) throws IOException {
         Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/ListeManègeAdmin.fxml"));
         
       pane.getChildren().clear();
       pane.getChildren().add(root);
    }


    @FXML
    private void MissionEtCongé(ActionEvent event) throws IOException {
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Caisses.fxml"));   
       pane.getChildren().clear();
       pane.getChildren().add(root);
        
    }

    @FXML
    private void LogOut(MouseEvent event) throws Exception {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        MainProjet m = new MainProjet();
        Stage stagee = new Stage();
        m.start(stagee);
    }
    
}
