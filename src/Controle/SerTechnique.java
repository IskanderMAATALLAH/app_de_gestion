package Controle;

import Main.ConnexionBDD;
import Main.MainProjet;
import com.jfoenix.controls.JFXButton;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class SerTechnique implements Initializable {

    
    @FXML
    private Pane paneSerTech;
    @FXML
    private JFXButton btnPersonnel;
    public Connection cnx;
    public static String dep ="";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           if(dep.equals("STechnique"))
           {
             MaintPers.depchoix="STechnique";
             contrat();  
           }
           else
           {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/detailPersonnel.fxml"));
            paneSerTech.getChildren().clear();
            paneSerTech.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(SerTechnique.class.getName()).log(Level.SEVERE, null, ex);
        }}
        
        try {
            inserDateJournée();
        } catch (SQLException ex) {
            Logger.getLogger(Pointage.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    @FXML
    private void ouvrirPersonnel() throws IOException
    {
        contrat();  
    }
    
    @FXML
    private void ouvrirAccueil() throws IOException, SQLException
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/detailPersonnel.fxml"));
        paneSerTech.getChildren().clear();
        paneSerTech.getChildren().add(root);  
    }
    
    @FXML
    private void ouvrirMagasin() throws IOException
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/magasinPieces.fxml"));
        paneSerTech.getChildren().clear();
        paneSerTech.getChildren().add(root);  
    }
    
    @FXML
    private void ouvrirMaintenance() throws IOException
    {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/listeManèges.fxml"));
        paneSerTech.getChildren().clear();
        paneSerTech.getChildren().add(root);  
    } 
    
    public void inserDateJournée() throws SQLException
    {
        Date dateMax=null;
        Date dateNow=null;
        int heureNow=0;
        dateMax= new Pointage().maxDate("journée", "Date");
        String sql="SELECT DATE(NOW()) AS dateNow , HOUR(NOW()) AS heureNow";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs2=st.executeQuery(sql);
        while (rs2.next())
        {   
            dateNow=rs2.getDate("dateNow");
            heureNow=rs2.getInt("heureNow");
        }
        if (!dateNow.equals(dateMax) && heureNow>=7)
        {
            sql="INSERT INTO journée(Date) VALUES(?)";
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setDate(1, (java.sql.Date) dateNow);
            
            ps.executeUpdate();
        }
        cnx.close();
    }

    @FXML
    private void LogOut(MouseEvent event) throws Exception {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        MainProjet m = new MainProjet();
        Stage stagee = new Stage();
        m.start(stagee);
    }
    
    public void contrat()
    {
           try {
            Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/MaintPers.fxml"));
        paneSerTech.getChildren().clear();
        paneSerTech.getChildren().add(root);  
        } catch (IOException ex) {
            Logger.getLogger(SerTechnique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
