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
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;




public class Rapport implements Initializable {
     Connection cnx ;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private JFXComboBox<String> Agent;
    @FXML
    private JFXTextField Nature;
    @FXML
    private JFXTextField PDR;
    @FXML
    private JFXTextField Carb;
    @FXML
    private JFXTextField Horaires;
    @FXML
    private JFXTextField Resultat;
    Paint value0=Paint.valueOf("F9C7CC");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    String sql = "Select NumContrat,Nom,Prenom from personnel where IdPoste=3";
    Statement st;
        try {
            cnx = new ConnexionBDD().ConnexionDB();
            st = cnx.createStatement();
            ResultSet rs= st.executeQuery(sql);
            while (rs.next())
            {
               Agent.getItems().add(rs.getString("NumContrat")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom")); 
            }
            cnx.close();
        } catch (SQLException ex) {
            
        }
    }    

    @FXML
    private void ajouter(ActionEvent event) {
          if(this.verificationComplete())
          {
        String agent = Agent.getSelectionModel().getSelectedItem();
        String tab [] = new String [3];
        tab=agent.split("  ");
        String nature = Nature.getText();
        String Pdr = PDR.getText();
        String carb = Carb.getText();
        String horaire = Horaires.getText();
        String result = Resultat.getText();
        String sql ="insert into rapportmaintenance (NumRapport,Date,Agent,Nature,PDR,Carb,Horaires,Resultat,IdManege)values (null,CURRENT_DATE(),'"+tab[0]+"','"+nature+"','"+Pdr+"','"+carb+"','"+horaire+"','"+result+"','"+ListeManège.numManege+"')";
          Statement st;
        
            try {
            cnx = new ConnexionBDD().ConnexionDB();
            st = cnx.createStatement();
            st.executeUpdate(sql);
            actualiser();act(1);
            cnx.close();

            } 
            catch (SQLException ex) {act(2);}
         
            }
    }

    @FXML
    private void annuler(ActionEvent event) {
        actualiser();
    }
    public void actualiser()
    {
             Agent.getSelectionModel().clearSelection();
     Nature.setText("");
     PDR.setText("");
     Carb.setText("");
     Horaires.setText("");
     Resultat.setText("");
    }

    @FXML
    private void NatureConrainte(KeyEvent event) {
           if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

    @FXML
    private void PDRContrainte(KeyEvent event) {
           if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

    @FXML
    private void CarbContrainte(KeyEvent event) {
           if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

    @FXML
    private void HoraireContrainte(KeyEvent event) {
           if(!Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

    @FXML
    private void ResultatContrainte(KeyEvent event) {
           if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

  public boolean verifierChamps()
  { boolean b= true;
      if(Agent.getSelectionModel().isEmpty())
      {
          Agent.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
      }
      if(Nature.getText().equals(""))
      {
       Nature.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;  
      }
      if(PDR.getText().equals(""))
      {
          PDR.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
      }
      if(Carb.getText().equals(""))
      {
        Carb.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;  
      }
      if(Horaires.getText().equals("") || !VerValeur(Horaires.getText()))
      {
         Horaires.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false; 
      }
      if(Resultat.getText().equals(""))
      {
          Resultat.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
      }
      return b;
  }
  
   public boolean VerValeur(String txt)
    {
        String regx ="[0-9]*[1-9]+[0-9]*";
        Pattern p = Pattern.compile(regx);
        if(txt.matches(regx))
        {
            return true;
        }
        else return false;
    }
           public void act(int i)
  {
     Image im = new Image("/Icones/icons8-approbation-40.png");   
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Rapport"); nt.text("Ajout effectué");}
       else if(i==2){nt.title("Rapport"); nt.text("Ajout non effectué");}

      nt.show();
  }
           
             public boolean confirmation()
  {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
   
     alert.setTitle("Ajout");alert.setContentText("Ajouter Rapport?");

      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }
    
   public boolean verificationComplete()
   {  boolean b=false;
       if(verifierChamps())
       {
           if(confirmation())
           {
               b=true;
           }
       }
       return b;
   }
}

