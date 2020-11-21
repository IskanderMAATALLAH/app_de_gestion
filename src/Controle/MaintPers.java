package Controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class MaintPers implements Initializable {
        
    @FXML
    private Pane paneSousMenu;
    @FXML
    private HBox hBoxEntete;
    @FXML
    private JFXButton btnPointage;
    @FXML
    private JFXButton btnContrat;
    public static String depchoix="";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           if(depchoix.equals("STechnique"))
           {
             contrat();  
           }  

        try {
            hBoxEntete.setStyle("-fx-background-color: linear-gradient( #454960, #EAEDF1);");
            Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Pointage.fxml"));
            paneSousMenu.getChildren().clear();
            paneSousMenu.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MaintPers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void ouvrirPointage() throws IOException
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Pointage.fxml"));
        paneSousMenu.getChildren().clear();
        paneSousMenu.getChildren().add(root);  
    }
    
    @FXML
    private void ouvrirContrats() throws IOException
    {   
        contrat();
    }
    
    public void contrat()
    {
        try {
            ListePersonnel.choixDep=0;
            Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/listePersonnel.fxml"));
            paneSousMenu.getChildren().clear();
            paneSousMenu.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MaintPers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
