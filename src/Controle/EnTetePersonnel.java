/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class EnTetePersonnel implements Initializable {

    @FXML
    private Pane panee;
    @FXML
    private Pane PanePerso;
    @FXML
    private HBox hBoxEntete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            hBoxEntete.setStyle("-fx-background-color: linear-gradient( #454960, #EAEDF1);");
            Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/suiviPointage.fxml"));
            PanePerso.getChildren().clear(); 
            PanePerso.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(EnTetePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void Contrat(ActionEvent event) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/suiviPersonnel.fxml"));
      PanePerso.getChildren().clear();
       PanePerso.getChildren().add(root); 
    }
    

    @FXML
    private void pointage(ActionEvent event) throws IOException {
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/suiviPointage.fxml"));
      PanePerso.getChildren().clear();
       PanePerso.getChildren().add(root); 
    }
    
}
