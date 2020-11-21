package Controle;

import static Controle.InfoManége.n;
import static Controle.ListePersonnel.num;
import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ListeManègeAdmin implements Initializable {
   Connection cnx ;
    @FXML
    private VBox vBox;
    public static String numManege;
    @FXML
    private VBox PaneManege;
    @FXML
    public static  Text etat;
    @FXML
    public static Text derniere;
    @FXML
    public static Text periode;
    @FXML
    public static Text prochaine;
    @FXML
    public static Text zone;
    @FXML
    public static Text remarque;
    @FXML
    public Pane panee;
    @FXML
    private JFXComboBox<String> choixManège;
    @FXML
    private JFXRadioButton rbMarche;
    @FXML
    private ToggleGroup selectedItem;
    @FXML
    private JFXRadioButton rbSusp;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    public static Stage stage_ajoutMan = null;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       try {
           listeManege();
           affManège();
       } catch (SQLException ex) {
           Logger.getLogger(ListeManègeAdmin.class.getName()).log(Level.SEVERE, null, ex);
       }
    }  
    
    public void listeManege()
    {
         String sql="SELECT IdManége FROM manége";
     
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st =cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next())
            {  
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
                
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

   
  

    @FXML
    private void clickeed(MouseEvent event) throws IOException {
        
     Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/DetManègeDg.fxml"));
     panee.getChildren().clear();
     panee.getChildren().add(root);

    }

    @FXML
    private void AjoutManège(ActionEvent event) throws IOException {
        stage_ajoutMan = new Stage();
        stage_ajoutMan.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/AjoutManége.fxml"));
        Scene scene = new Scene(root);
        stage_ajoutMan.setScene(scene);
        stage_ajoutMan.show();
        
    }

    @FXML
    private void cherchManège(KeyEvent event) throws SQLException {
        if(choixManège.getValue().equals("") || choixManège.getValue()==null)
        {
            choixManège.setValue(null);
            affManège();
        }else
        {
            String sql = "SELECT IdManége,Libelle FROM Manége "
                    + "WHERE Libelle LIKE '%"+choixManège.getValue()+"%'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st=cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixManège.getItems().clear();
            int i=0;
            while (rs.next())
            {   
                i++;
                choixManège.setVisibleRowCount(i);
                choixManège.getItems().add(rs.getInt("IdManége")+"-"+rs.getString("Libelle"));
                choixManège.show();
            }
        }
    }

    @FXML
    private void eventDateDu(KeyEvent event) {
        if(dateDu.getValue().equals(""))
            dateDu.setValue(null);
    }

    @FXML
    private void eventDateAu(KeyEvent event) {
        if(dateAu.getValue().equals(""))
            dateAu.setValue(null);
    }

    @FXML
      private void rechAction(ActionEvent event) throws SQLException, IOException 
    {
        String tab[] = new String[2];
        String sql=null;
        RadioButton selecRb = (RadioButton) selectedItem.getSelectedToggle();
        String textSelecRb=null;
        int etat=0;
        if (selecRb!=null)
        {
            textSelecRb=selecRb.getText();
            if(textSelecRb.equals("Marche"))
                etat=1;
            else if(textSelecRb.equals("Suspension"))
                etat=0;
        }
        
        if (choixManège.getValue()!=null && selecRb==null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab=choixManège.getValue().split("-");
            
            sql = "SELECT IdManége,Libelle FROM manége WHERE IdManége='"+tab[0]+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb !=null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle FROM manége "
                    + "WHERE IdManége='"+tab[0]+"' AND Etat='"+etat+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb !=null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb !=null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb ==null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb ==null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb ==null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()!=null && selecRb !=null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab=choixManège.getValue().split("-");
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége='"+tab[0]+"' AND Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege "
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb !=null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            sql = "SELECT IdManége,Libelle FROM manége "
                    + "WHERE Etat='"+etat+"'";
                    
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb !=null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege  AND "
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb !=null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege  AND "
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb !=null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            sql = "SELECT IdManége,Libelle , max(Date) FROM manége,rapportmaintenance "
                    + "WHERE Etat='"+etat+"' AND manége.IdManége=rapportmaintenance.IdManege AND ("
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"')";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb ==null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége=rapportmaintenance.IdManege  AND "
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb ==null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége=rapportmaintenance.IdManege  AND "
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
                    
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixManège.getValue()==null && selecRb ==null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            sql = "SELECT IdManége,Libelle, max(Date) FROM manége,rapportmaintenance "
                    + "WHERE manége.IdManége=rapportmaintenance.IdManege  AND "
                    + "ADDDate(Date,INTERVAL PerMaintenance DAY)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                    
                    
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoManége.n=rs.getString("IdManége");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoManége.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }   
        
        
    }

    private void affManège() throws SQLException
    {
        String sql = "SELECT IdManége,Libelle FROM Manége";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixManège.getItems().clear();
        while (rs.next())
        {
            choixManège.getItems().add(rs.getInt("IdManége")+"-"+rs.getString("Libelle"));
        }
        cnx.close();
    }

    @FXML
    private void actualiserAction(ActionEvent event) throws SQLException {
        choixManège.setValue(null);
        rbMarche.setSelected(false);
        rbSusp.setSelected(false);
        dateDu.setValue(null);
        dateAu.setValue(null);
        affManège();
        vBox.getChildren().clear();
        listeManege();
    }
    
}
