/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class AjoutMissionCongé implements Initializable {
     Connection cnx;
  PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private VBox PaneChange;
    @FXML
    private JFXRadioButton Cong;
    @FXML
    private JFXRadioButton mission;
    @FXML
    private JFXTextField Num;
    @FXML
    private JFXComboBox<String> Personnel;
    @FXML
    private JFXTextField Adresse;
    @FXML
    private JFXDatePicker DateDeb;
    @FXML
    private JFXDatePicker DateFin;
    @FXML
    private JFXTextField Titre;
    @FXML
    private JFXTextField Objet;
    @FXML
    private JFXTextField Moyen;
    @FXML
    private JFXButton ButtValide;
    @FXML
    private JFXButton ButtSupp;

    ToggleGroup groupe = new ToggleGroup();
    Paint value0=Paint.valueOf("F9C7CC");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Cong.setToggleGroup(groupe);
         mission.setToggleGroup(groupe);
          remplirCombo();
    }    


      private void ValideCongé() {
          LocalDate da=DateDeb.getValue();
    String NumCongé= Num.getText();
    String pers[] = new String[2];
    pers=Personnel.getSelectionModel().getSelectedItem().split("  ");
    String lieu = Adresse.getText();
    String date=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
    String titre = Titre.getText();
    
    LocalDate dat= DateFin.getValue();
    String date1=Integer.toString(dat.getYear())+"-"+Integer.toString(dat.getMonthValue())+"-"+Integer.toString(dat.getDayOfMonth());


   
     String sql ="Insert into congé (NumCongé,Adresse,DateDeb,Durée,IdPerso,Titre,DateRet)values(?,?,?,?,?,?)";
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
            ps.setString(1, NumCongé);
            ps.setString(2, lieu);
            ps.setString(3, date);
           
            ps.setString(4, pers[0]);
            ps.setString(5, titre);
            ps.setString(6, date1);
            ps.executeUpdate();
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(MissionEtCongé.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    
     }
     
    private void ValideMission() {
    LocalDate da=DateDeb.getValue();
    String mission= Num.getText();
    String pers[] = new String[2];
    pers=Personnel.getSelectionModel().getSelectedItem().split("  ");
    String lieu = Adresse.getText();
    String date=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
    String obj = Objet.getText();
    String moyen = Moyen.getText();
    LocalDate dat= DateFin.getValue();
    String date1=Integer.toString(dat.getYear())+"-"+Integer.toString(dat.getMonthValue())+"-"+Integer.toString(dat.getDayOfMonth());


   
     String sql ="Insert into mission (NumMission,Lieu,DateDeb,datefin,Objet,Moyen,IdPerso)values(?,?,?,?,?,?,?)";
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
            ps.setString(1, mission);
            ps.setString(2, lieu);
            ps.setString(3, date);
            ps.setString(4, date1);
            ps.setString(5, obj);
            ps.setString(6, moyen);
            ps.setString(7, pers[0]);
            ps.executeUpdate();
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(MissionEtCongé.class.getName()).log(Level.SEVERE, null, ex);
        }
   
 
    }
    @FXML
    private void Valide(ActionEvent event) {
        if(mission.isSelected())
        {
            if(this.verifieSelection())
            {
                if(this.verifieChampMission())
                    if(confirmation(2))
                ValideMission();
            }
        }        
        else if(Cong.isSelected())
            {
            if(this.verifieSelection())
            {
                if(this.verifieChampCongé())
                      if(confirmation(2))
                ValideCongé();
            }
        }  
            

    }
    
      
    public void remplirCombo()
    {
        String sql ="select * from personnel where IdPoste in (select IdPoste from poste where Libelle ='Secrétaire' or Libelle ='Agent Administratif' or Libelle ='Chauffeur' or Libelle ='Comptable' or Libelle ='Aide Comptable' or Libelle ='Trésorier' )";
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            ps =cnx.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                String empl=rs.getString("NumContrat")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom");
                Personnel.getItems().add(empl);
               
            }
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(MissionEtCongé.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     @FXML
    private void Congé(ActionEvent event) {
         Num.setPromptText("Numéro Congé");
        Adresse.setPromptText("Lieu congé");
        Objet.setDisable(true);
        Moyen.setDisable(true);
      
        Titre.setDisable(false);
        ButtValide.setDisable(false); 

    }
    @FXML
    private void Mission(ActionEvent event) {
        Num.setPromptText("Numéro Mission");
        Adresse.setPromptText("Lieu mission");
    
        Titre.setDisable(true);
        Objet.setDisable(false);
        Moyen.setDisable(false);  
        ButtValide.setDisable(false); 
    }
    
    @FXML
    private void SuppCongé(ActionEvent event) {
    }
    
    public boolean verifieChampCongé()
    {
        boolean b = true;
        if(Num.getText().equals(""))
        {
          b=false;
          Num.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));   
        }
        if(Personnel.getSelectionModel().isEmpty())
        {
          b=false;
          Personnel.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
        }
        if(Adresse.getText().equals(""))
        {
          b=false;
          Adresse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); 
        }
        if(DateDeb.equals(""))
        {
            b=false;
        }
        if(DateFin.equals(""))
        {
            b=false;
        }
        if(Titre.getText().equals(""))
        {
          b=false;
          Titre.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));             
        }
        
        return b;
    }
    
     public boolean verifieChampMission()
    {
        boolean b = true;
        if(Num.getText().equals(""))
        {
          b=false;
          Num.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));   
        }
        if(Personnel.getSelectionModel().isEmpty())
        {
          b=false;
          Personnel.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
        }
        if(Adresse.getText().equals(""))
        {
          b=false;
          Adresse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); 
        }
        if(DateDeb.equals(""))
        {
            b=false;
        }
        if(DateFin.equals(""))
        {
            b=false;
        }
        if(Objet.getText().equals(""))
        {
          b=false;
          Objet.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));             
        }
          if(Moyen.getText().equals(""))
        {
          b=false;
          Moyen.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));             
        }
        
        return b;
    }
    
    public boolean verifieSelection()
    {
        boolean b = true;
        if(!mission.isSelected()&& !Cong.isSelected())
        {
            b=false;
        }
        return b;
    }

      public boolean confirmation(int i)
  {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      if(i==1){alert.setTitle("Suppression");alert.setContentText("etes vous sure de supprimer ?");}
      else if(i==2){alert.setTitle("Ajout");alert.setContentText("etes vous sure d'ajouter ?");}
      else if(i==3){alert.setTitle("Modification");alert.setContentText("etes vous sure de modifier ?");}
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }
    @FXML
    private void NumContrainte(KeyEvent event) {
             if(!Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }
}
