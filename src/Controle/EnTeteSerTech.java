/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import static Controle.InfoManége.pane;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class EnTeteSerTech implements Initializable {

      public  Pane root ;
    @FXML
    private Pane panee;
    @FXML
    private AnchorPane pane;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          try {
              // TODO
              root = FXMLLoader.load(getClass().getResource("/Interfaces/ManégeDG.fxml"));
              panee.getChildren().clear();
              panee.getChildren().add(root);
          } catch (IOException ex) {
              Logger.getLogger(EnTeteSerTech.class.getName()).log(Level.SEVERE, null, ex);
          }
    }    

  

    @FXML
    private void ouvrirMaintenance(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("/Interfaces/ManégeDG.fxml"));
        panee.getChildren().clear();
        panee.getChildren().add(root);
    }

    @FXML
    private void ouvrirMagasin(ActionEvent event) throws IOException {
       // MagasinPieces.disable=1;
         root = FXMLLoader.load(getClass().getResource("/Interfaces/MagasinPieceDg.fxml"));
        panee.getChildren().clear();
        panee.getChildren().add(root);
    }

    @FXML
    private void ouvrirAccueil(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Interfaces/MainDG.fxml"));
        pane.getChildren().clear();
        pane.getChildren().add(root);
    }
    
}
