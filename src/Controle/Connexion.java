package Controle;

import Main.ConnexionBDD;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Connexion implements Initializable {
 public Connection cnx;
    @FXML
    private TextField NomUtili;
    @FXML
    private ComboBox<String> Poste;
    @FXML
    private PasswordField MotDpasse;
    @FXML
    private Label Notif;
    private String num;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     try {   
         affPoste();
     } catch (SQLException ex) {
         Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
     }
    }    

    @FXML
    private void Connect(ActionEvent event) throws SQLException, IOException {
       
        if(verificationChamp())
        {
             String libelle [] = Poste.getValue().split("-");
        String poste="";
        cnx =cnx = new ConnexionBDD().ConnexionDB();
            String sql = "select Libelle,personnel.NumContrat from compte ,personnel,poste where"
                    + " NumUtil=NumContrat and personnel.IdPoste = poste.IdPoste and "
                    + "NU='"+NomUtili.getText()+"' and MP='"+MotDpasse.getText()+"'";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {  
                
             poste=rs.getString("Libelle");
             num=rs.getString("NumContrat");
            }
            cnx.close();
            String po = libelle[1];
            
                    
           if(!poste.equals(""))
            {
                if(poste.equals(po))
                {
               Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
               stage.close();
                if(po.equals("Comptable"))
                {
                 openPoste("Comptable") ;
                }
                else if(po.equals("Agent Administratif"))
                {
                    openPoste("AgentAdministratif") ;
                }
                else if(po.equals("Directeur générale"))
                {
                    openPoste("DirecteurGénérale") ;
                }
                else if(po.equals("Trésorier"))
                {
                    openPoste("SFinances") ;
                }
                else if(po.equals("Secrétaire"))
                {
                    openPoste("Secretaire") ;
                    SecretaireController.numSec=num;
                    System.out.print(SecretaireController.numSec);
                }
                else if(po.equals("Service Technique"))
                {
                    openPoste("STechnique") ;
                }
            }
                else
                {
                information();
                }
            }
           else
           {
               information();
           }     
        }
    }

    @FXML
    private void quitter(ActionEvent event) {
        
    }
    
    public boolean verificationChamp()
    {
        Paint value0=Paint.valueOf("F9C7CC");
        boolean b = true;
        if(NomUtili.getText().equals(""))
        {
            b=false;
            NomUtili.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(MotDpasse.getText().equals(""))
        {
            b=false;
            MotDpasse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(Poste.getSelectionModel().isEmpty())
        {
           b=false; 
           Poste.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        return b;
    }
  private void affPoste() throws SQLException
    {
        String sql = "SELECT IdPoste,Libelle FROM poste WHERE (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur')";
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        Poste.getItems().clear();
        while(rs.next())
            Poste.getItems().add(rs.getString("IdPoste")+"-"+rs.getString("Libelle"));
        cnx.close();
    }
  
  public void openPoste(String post) throws IOException
  {
      
                     Stage stage = new Stage();
                     stage.initStyle(StageStyle.UNDECORATED);
                     Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/"+post+".fxml"));
                     Scene scene = new Scene(root);
                     stage.setScene(scene);
                     stage.show();
  }
   public void information()
   {
     Notif.setText("Informations Invalides");
   }

    @FXML
    private void LabelVis(MouseEvent event) {
        Notif.setText("");
    }
}
