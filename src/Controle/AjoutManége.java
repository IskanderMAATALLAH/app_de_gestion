package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class AjoutManége implements Initializable {
 Connection cnx;
  PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private JFXTextField NomManege;
    @FXML
    private JFXComboBox<String> type;
    @FXML
    private JFXTextField periode;
    @FXML
    private JFXComboBox<String> zone;
    private JFXTextField etat;
    @FXML
    private JFXComboBox<String> ticket;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ObservableList<String> data=FXCollections.observableArrayList();
       ObservableList<String> data1=FXCollections.observableArrayList();
       ObservableList<String> data2=FXCollections.observableArrayList();
       data.addAll("Enfants","Adulte","Mixte");
       type.setItems(data);
       data1.addAll("1","2","3","4");
       zone.setItems(data1);
       String sql = "select Prix from ticket";
     try {
         cnx=new ConnexionBDD().ConnexionDB();
         ps = cnx.prepareStatement(sql);
         rs=ps.executeQuery();
         while(rs.next())
         {
             data2.add(rs.getString("Prix"));
             
         }
         ticket.setItems(data2);
         cnx.close();
     } catch (SQLException ex) {
         Logger.getLogger(AjoutManége.class.getName()).log(Level.SEVERE, null, ex);
     }
			
    }    

    @FXML
    private void ajouter(ActionEvent event) {
        String libelle = NomManege.getText();
        String typee = type.getSelectionModel().getSelectedItem();
        String periodee = periode.getText();
       
        String zonee = zone.getSelectionModel().getSelectedItem();
        String tickett = zone.getSelectionModel().getSelectedItem();
       
        if(confirmation(2))
        {
     try {
         String sql ="insert into manége (IdManége,Libelle,Catégorie,PerMaintenance,Zone,CodeTicket,Etat) values(null,'"+libelle+"','"+typee+"','"+periodee+"','"+zonee+"','"+tickett+"',0)";
         cnx=new ConnexionBDD().ConnexionDB();
         Statement st;
         st = cnx.createStatement();
         st.executeUpdate(sql);
         cnx.close();
         act(); clean();
         fermer();
     } catch (SQLException ex) {
         ex.getMessage();
     }
        }        
}
    
    

    
    public boolean verification()
    {   boolean b = true;
    Paint value0=Paint.valueOf("F9C7CC");
        if(NomManege.getText().equals(""))
        {
            b=false;
            NomManege.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }
        if(type.getSelectionModel().isEmpty())
        {
            b=false;
            type.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }   
        if(periode.getText().equals(""))
        {
            b=false;
            periode.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }
        if(ticket.getSelectionModel().isEmpty())
        {
         b=false; 
         ticket.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }
         if(zone.getSelectionModel().isEmpty())
        {
            b=false;
            zone.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }     
      return b;
    }
    public void act()
  {
       Image im = new Image("/Icones/icons8-approbation-40.png"); 
      Notifications nt = Notifications.create()
              .title("Ajout terminé")
              .text("Vous avez ajouté un nouveau manège")
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.TOP_RIGHT);
      nt.showConfirm();
              
  }
 
  public boolean confirmation(int i)
  { Image im = new Image("/Icones/icons8-plus-40.png"); 
   Image im1 = new Image("/Icones/icons8-effacer-40.png"); 
    boolean b =false;
    if(verification())
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      
      if(i==1){alert.setGraphic(new ImageView(im1));alert.setTitle("Manège");alert.setContentText("Supprimer manège ?");}
      else if(i==2){alert.setGraphic(new ImageView(im));alert.setTitle("Manège");alert.setContentText("Ajouter manège ?");}
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
         b=true; 
      }
      else b=false;}
   return b;  
  }
  
  public void clean()
  {
     NomManege.setText("");
     type.setValue(null);
     periode.setText("");
     ticket.setValue(null);
     zone.setValue(null);
     
  }

    @FXML
    private void fermer() 
    {
        ListeManègeAdmin.stage_ajoutMan.close();
    }
        
}
