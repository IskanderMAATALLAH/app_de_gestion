package Controle;

import Main.MainProjet;
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
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ServFinances implements Initializable {

    @FXML
    private Pane paneSerFin;
    @FXML
    private JFXButton buttCaisses;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Pane root;
        try {
            root = FXMLLoader.load(getClass().getResource("/Interfaces/interCaisse.fxml"));
            paneSerFin.getChildren().clear();
            paneSerFin.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(ServFinances.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void ouvrirAchats(ActionEvent event) throws IOException 
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/interDepenses.fxml"));
        paneSerFin.getChildren().clear();
        paneSerFin.getChildren().add(root);  
    }

    @FXML
    private void ouvrirCaisses(ActionEvent event) throws IOException 
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/interCaisse.fxml"));
        paneSerFin.getChildren().clear();
        paneSerFin.getChildren().add(root);
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
