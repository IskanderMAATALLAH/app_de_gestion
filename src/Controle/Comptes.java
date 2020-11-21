/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
//import org.dom4j.rule.Action;
import tableau.Compte;
import tableau.TabPointage;
import org.controlsfx.control.Notifications;
/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class Comptes implements Initializable  {
  Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
      ObservableList<Compte> data = FXCollections.observableArrayList();

    @FXML
    private JFXTextField Util;
    @FXML
    private JFXPasswordField MotDpasse;
    @FXML
    private JFXPasswordField Confirme;
    @FXML
    private JFXComboBox<String> Employe;
    @FXML
    private JFXTextField RechUtil;
    @FXML
    private TableView<tableau.Compte> Compte;
    @FXML
    private TableColumn<?, ?> Contrat;
    @FXML
    private TableColumn<?, ?> poste;
    @FXML
    private TableColumn<?, ?> Nom;
    @FXML
    private TableColumn<?, ?> MDP;
    @FXML
    private JFXButton add;
    @FXML
    private JFXButton del;
    tableau.Compte cpt;
    @FXML
    private JFXButton modif;
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         del.setDisable(true);
         modif.setDisable(true);
        Contrat.setCellValueFactory(new PropertyValueFactory<>("Contrat"));
        poste.setCellValueFactory(new PropertyValueFactory<>("poste"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("numutil"));
        MDP.setCellValueFactory(new PropertyValueFactory<>("motdepasse"));
        remplircombo();
      try {
          getData();
      } catch (SQLException ex) {
          Logger.getLogger(Comptes.class.getName()).log(Level.SEVERE, null, ex);
      }
    }    

    @FXML
    private void ajouter(ActionEvent event) throws SQLException  {
        String NOM = Util.getText();
        String pasS = MotDpasse.getText();
        String Conf =Confirme.getText();
        if(VerificationComp(1))
        {
        String Cont[] = Employe.getSelectionModel().getSelectedItem().split("  ");
        String contrat =Cont[0];
        String sql ="insert into compte (NU,MP,	NumUtil)values('"+NOM+"','"+pasS+"','"+contrat+"')";        
          cnx=new ConnexionBDD().ConnexionDB();
          ps = cnx.prepareStatement(sql);
          ps.executeUpdate();
          cnx.close();
          act(1);
        }
         
          
    }

    @FXML
    private void supprimer(ActionEvent event) throws SQLException {
        if(VerificationComp(3))
        {
        String sql="delete from compte where NU='"+cpt.getNumutil()+"'";
        cnx=new ConnexionBDD().ConnexionDB();
         ps = cnx.prepareStatement(sql);
          ps.executeUpdate();
          cnx.close();
          act(3);
        }
         afterSuppUpda();
          Employe.setDisable(false);
    }

    @FXML
    private void Rechercher(ActionEvent event) {
    }
    
    public void remplircombo()
    {
         try {
            String sql ="select * from personnel where IdPoste in (select IdPoste from poste where Libelle !='Caissier' and Libelle !='Opérateur' and Libelle !='Maintenance' and Libelle !='Sécurité')";
           cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                Employe.getItems().add(rs.getString("NumContrat")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom"));
                
            }
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(PointageAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void show(MouseEvent event) {
       cpt = Compte.getSelectionModel().getSelectedItem();
        Util.setText(cpt.getNumutil());
        MotDpasse.setText(cpt.getMotdepasse());
        Employe.setDisable(true);
        Employe.setValue(cpt.getContrat());
        add.setDisable(true);
        del.setDisable(false);
        modif.setDisable(false);
        
    }
    
    public void getData() throws SQLException
    {
       
         String sql="SELECT NU,MP,Libelle,NumUtil,Nom,Prenom FROM compte,personnel,poste WHERE NumUtil=NumContrat and personnel.IdPoste=poste.IdPoste";
        insertdata(sql);
    }

    @FXML
    private void modifier(ActionEvent event) {
        String tab[]= cpt.getContrat().split("  ");
        if(VerificationComp(2))
        {
            try {
                String sql = "UPDATE  compte set NU='"+Util.getText()+"' ,MP='"+MotDpasse.getText()+"' where NumUtil='"+tab[0]+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                ps = cnx.prepareStatement(sql);
                ps.executeUpdate();
                cnx.close();
                act(2);
                afterSuppUpda();
            } catch (SQLException ex) {
             act(4);  
            }
}
          
    }
    
    public void afterSuppUpda() throws SQLException
    {
          Util.setText("");
          MotDpasse.setText("");
          Confirme.setText("");
          Confirme.setDisable(false);
          Employe.setValue(null);        
          add.setDisable(false);
          del.setDisable(true); 
          getData();
    }
  public void act(int i)
  {
      Notifications nt = Notifications.create()
              .title("Compte")
              .graphic(null)
              .hideAfter(Duration.seconds(3))
              .position(Pos.TOP_RIGHT);
      if(i==1){nt.text("Compte ajouté");}
      else if(i==2){nt.text("Compte modifié");}
      else if(i==3){nt.text("Compte supprimé");}
      else if(i==4){nt.text("Compte non modifié");}
      nt.showConfirm();
              
  }
 
  public boolean confirmation(int i)
  {
    
    
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      if(i==3){alert.setTitle("Compte");alert.setContentText("Supprimer compte?");}
      else if(i==1){alert.setTitle("Compte");alert.setContentText("Ajouter compte ?");}
        else if(i==2){alert.setTitle("Compte");alert.setContentText("Modifier compte ?");}
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }

   
        
    public void insertdata(String sql) throws SQLException
    { data.clear();
      cnx=new ConnexionBDD().ConnexionDB();
         PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                data.add(new Compte(                       // recuperation 
                    rs.getString("NumUtil")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom"),                //   des 
                    rs.getString("Libelle"),                       //   info 
                    rs.getString("NU"),                    // et les mettre
                    rs.getString("MP")                    //dans  // un tableau
                )); 
                
		this.Compte.setItems(data);                        
            }cnx.close();  
    }
    
        

    @FXML
    private void Cherche(KeyEvent event) {
         String begins =RechUtil.getText();
      String sql="SELECT NU,MP,Libelle,NumUtil,Nom,Prenom FROM compte,personnel,poste WHERE NumUtil=NumContrat and personnel.IdPoste=poste.IdPoste and NU LIKE '"+begins+"%'";
      try {  
          insertdata(sql);
      } catch (SQLException ex) {
          Logger.getLogger(Comptes.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public boolean verifieChampsAjoutModif(int i)
    {
        boolean b = true;
         Paint value0=Paint.valueOf("F9C7CC");
        if(Util.getText().equals(""))
        {
            Util.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
        }
        if(MotDpasse.getText().equals(""))
        {
            MotDpasse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
        }
        if(Confirme.getText().equals("") && (i==1|| i== 2))
        {
          Confirme.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;  
        }
        if(!Confirme.getText().equals(MotDpasse.getText()))
        {
         Confirme.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;  
         MotDpasse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
 
        }
        if(Employe.getSelectionModel().isEmpty() && i==1)
        {
           Employe.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false; 
        }
        return b;
    }
    public boolean VerificationComp(int i)
    {  boolean b = false;
        if(verifieChampsAjoutModif(i))
        {
            if(confirmation(i))
            {
               b=true; 
            }
        }
        return b;
    }
    
} 
