package Controle;

import static Controle.ListeManège.numManege;
import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class Caisses implements Initializable {
 Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private ScrollPane listecaisses;
    @FXML
    private VBox vBox;
    @FXML
    private JFXRadioButton tous;
    @FXML
    private JFXRadioButton ouvert;
    @FXML
    private JFXRadioButton ferme;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField libelle;
    @FXML
    private JFXComboBox<String> type;
    ToggleGroup groupe = new ToggleGroup();
    public static String IdCaisse;
    public static String Libelle;
    public static String Type;
    public static String Etat;
    @FXML
    private JFXButton ajout;
    @FXML
    private JFXButton supp;
    @FXML
    private JFXButton annu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tous.setToggleGroup(groupe);
        ouvert.setToggleGroup(groupe);
        ferme.setToggleGroup(groupe);
        ObservableList<String> data=FXCollections.observableArrayList();
        type.getItems().add("Interne");
        type.getItems().add("Entrée");
     try {
         getNumCaisse();
     } catch (SQLException ex) {
         Logger.getLogger(Caisses.class.getName()).log(Level.SEVERE, null, ex);
     }
         listecaisses();
    }    

    @FXML
    private void clickeed(MouseEvent event) {
      
      id.setText(IdCaisse);
      libelle.setText(Libelle);
      type.setValue(Type);
        if(InfoCaisse.Etat.equals("1")) {
            supp.setDisable(true);
     } else supp.setDisable(false);
      ajout.setDisable(true);
     
    }

    @FXML
    private void Ajouter(ActionEvent event) {
        if(confirmation(2))
        {
            try {
                String sql = "insert into caisse (IdCaisse,Libelle,TypeCaisse,Etat,EtatSupp) values(?,?,?,0,0)";
                cnx=new ConnexionBDD().ConnexionDB();
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1, id.getText());
                ps.setString(2, libelle.getText());
                ps.setString(3, type.getSelectionModel().getSelectedItem());
                ps.executeUpdate();
                cnx.close();
                listecaisses();
                action(1);
                tous.setSelected(true);
                actualiser();
            } catch (SQLException ex) {
                action(3);
            }
}
        
    }

    @FXML
    private void supprimer(ActionEvent event) {
        if(confirmation(1))
        {
            try {
                String sql ="update caisse set etatsupp=1 where IdCaisse='"+id.getText()+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.executeUpdate();
                cnx.close();
                action(2);
                actualiser();
            } catch (SQLException ex) {
                action(4);
            }
          
        }
     
        
    }
    public void listecaisses()
    { 
        vBox.getChildren().clear();
          String sql="SELECT IdCaisse FROM caisse where EtatSupp=0";
     
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st =cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next())
            {  
                IdCaisse=rs.getString("IdCaisse");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/InfoCaisse.fxml"));
                vBox.getChildren().add(pane);
                
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void showall(ActionEvent event) {
        vBox.getChildren().clear();
        listecaisses();
              }

    @FXML
    private void showouv(ActionEvent event) {
         supp.setDisable(true);
        vBox.getChildren().clear();
         String sql="SELECT IdCaisse FROM caisse where Etat=1";
     
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st =cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next())
            {  
                IdCaisse=rs.getString("IdCaisse");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/InfoCaisse.fxml"));
                vBox.getChildren().add(pane);
                
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void showfer(ActionEvent event) {
         supp.setDisable(false);
        vBox.getChildren().clear();
       
         String sql="SELECT IdCaisse FROM caisse where Etat=0";
     
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st =cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next())
            {  
                IdCaisse=rs.getString("IdCaisse");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/InfoCaisse.fxml"));
                vBox.getChildren().add(pane);
                
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean confirmation(int i)
    {
     Image im = new Image("/Icones/icons8-plus-40.png"); 
   Image im1 = new Image("/Icones/icons8-effacer-40.png"); 
    boolean b =false;
    if(verification())
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      
    if(i==1){alert.setGraphic(new ImageView(im1));alert.setTitle("Caisse");alert.setContentText("Supprimer Caisse ?");}
      else if(i==2){alert.setGraphic(new ImageView(im));alert.setTitle("Caisse");alert.setContentText("Ajouter Caisse ?");}
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
         b=true; 
      }
      else b=false;}
   return b;     
    }
    public boolean verification()
    {
         boolean b = true;
    Paint value0=Paint.valueOf("F9C7CC");
        if(id.getText().equals(""))
        {
            b=false;
            id.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }
        if(type.getSelectionModel().isEmpty())
        {
            b=false;
            type.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }   
        if(libelle.getText().equals(""))
        {
            b=false;
            libelle.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;

        }
           
      return b; 
    }
    public void action(int i)
    {
       Image im = new Image("/Icones/icons8-approbation-40.png"); 
     Image im1 = new Image("/Icones/icons8-haute-priorité-40.png");
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Caisse"); nt.text("Ajout caisse terminé");}
      else if(i==2){nt.title("Caisse"); nt.text("Suppression terminé");}
       else if(i==3){nt.graphic(new ImageView(im1));nt.title("Caisse"); nt.text("Ajout non effectué !");}
       else if(i==4){nt.graphic(new ImageView(im1));nt.title("Caisse"); nt.text("La caisse est encore ouverte !");}
      nt.show();  
    }
    
     public void getNumCaisse() throws SQLException
    {
       int num=0;
        cnx=new ConnexionBDD().ConnexionDB();
        String sql = " select Max(IdCaisse)as Num from caisse";
         ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
                        num=rs.getInt("Num");
                        }
                     num=num+1;
                   id.setText(Integer.toString(num));
                   id.setDisable(true);
                   cnx.close();
    }
 @FXML
    private void annuler(ActionEvent event) throws SQLException {
   actualiser();
    }
    public void actualiser() throws SQLException
    {
             getNumCaisse();
        libelle.setText("");
        type.setValue(null);
        supp.setDisable(true);
        ajout.setDisable(false);
       listecaisses();
    }
    
}
