package Controle;



import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author user
 */
public class DetailManège implements Initializable {


    @FXML
     private  Text etat;
    @FXML
     private Text derniere;
    @FXML
     private Text periode;
    @FXML
     private Text prochaine;
    @FXML
     private Text zone;
    @FXML
     private Text remarque;
    

    public String datee;
    public String per;
    String result;
    @FXML
    private JFXButton suspendre;
    @FXML
    private JFXButton reprendre;
    @FXML
    private Label NomManége;
    @FXML
    private JFXButton rapport;
    public Connection cnx;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
       try {
     
        String sql ="select * from manége where manége.IdManége='"+ListeManège.numManege+"'";
        String sql2 ="select * from manége,rapportmaintenance where manége.IdManége=rapportmaintenance.IdManege and manége.IdManége='"+ListeManège.numManege+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {  NomManége.setText(rs.getString("Libelle"));
            per=rs.getString("PerMaintenance");
          
             if(rs.getString("Etat").equals("1"))
             {etat.setText("Actif");suspendre.setDisable(false);}
             else {etat.setText("Suspendu");reprendre.setDisable(false);}
             zone.setText(rs.getString("Zone"));
            periode.setText(rs.getString("manége.PerMaintenance"));
        }
        st =cnx.createStatement();
        ResultSet rs2 = st.executeQuery(sql2);
        while(rs2.next())
        {
         datee = rs2.getString("Date");
         result = rs2.getString("Resultat");
        }
         if(datee!=null)
        {
       String sql1="select ADDDATE('"+datee+"',interval '"+per+"' day) as dateMaint";
         st =cnx.createStatement();
         ResultSet rs1 = st.executeQuery(sql1);
         derniere.setText(datee);
         while(rs1.next())
         {
            prochaine.setText(rs1.getString("dateMaint"));
            
         }
        cnx.close();
        } else {prochaine.setText("-");
        derniere.setText("-");}
        
        
    } catch (SQLException ex) {
        Logger.getLogger(InfoPersonnel.class.getName()).log(Level.SEVERE, null, ex);
    }
    }    

    @FXML
    private void susp(ActionEvent event) {
        String sql ="update manége set Etat =0 where IdManége ='"+ListeManège.numManege+"'";
          try
            {
                cnx = new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                cnx.close();
                suspendre.setDisable(true);
                reprendre.setDisable(false);
                etat.setText("Suspendu");
            }catch(SQLException e) {System.out.print(e);}
        
    }

    @FXML
    private void repren(ActionEvent event) {
          String sql ="update manége set Etat =1 where IdManége ='"+ListeManège.numManege+"'";
          try
            {   
                cnx = new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                cnx.close();
                suspendre.setDisable(false);
                reprendre.setDisable(true);
                etat.setText("Actif");
            }catch(SQLException e) {System.out.print(e);}
         
         
    }

    @FXML
    private void Rapport(ActionEvent event) throws IOException {
        AjoutModifPersonnel.ModAjout=0;
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/rapport.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
