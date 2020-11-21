/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;


import Main.MainProjet;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class DirecteurGénérale implements Initializable {

    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    public static Pane pne; 
    @FXML
    private  Pane BigPane;
    public static int ch=0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/MainDG.fxml"));
            BigPane.getChildren().clear();
            BigPane.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(DirecteurGénérale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void change(MouseEvent event) {
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
