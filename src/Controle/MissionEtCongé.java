/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import Reports.CongéImp;
import Reports.MissionImp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.Notifications;
import tableau.Congé;
import tableau.Mission;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class MissionEtCongé implements Initializable {
   Connection cnx;
  PreparedStatement ps;
  ResultSet rs=null;
  ObservableList<Congé> dataCon=FXCollections.observableArrayList();
  ObservableList<Mission> dataMission=FXCollections.observableArrayList();

    @FXML
    private TableView<Congé> TabCongé;
    @FXML
    private TableColumn<?, ?> NCongé;
    @FXML
    private TableColumn<?, ?> NContrat;
    @FXML
    private TableColumn<?, ?> AdresseC;
    @FXML
    private JFXDatePicker DateDeb;
   
      @FXML
    private TableColumn<?, ?> TitreCon;
    @FXML
    private TableColumn<?, ?> DateR;
    @FXML
    private TableView<Mission> TabMission;
    @FXML
    private TableColumn<?, ?> NMission;
    @FXML
    private TableColumn<?, ?> NMContrat;
    @FXML
    private TableColumn<?, ?> LieuM;
    @FXML
    private TableColumn<?, ?> Date;
    @FXML
    private TableColumn<?, ?> ObjetM;
    @FXML
    private TableColumn<?, ?> MoyenM;
  
    @FXML
    private JFXComboBox<String> Personnel;
    @FXML
    private JFXTextField Adresse;
    private JFXDatePicker DateCongé;
   
    @FXML
    private JFXTextField Objet;
    @FXML
    private JFXTextField Moyen;
    private JFXComboBox<String> PersonnelMission;
    private JFXTextField LieuMission;

   
    @FXML
    private JFXRadioButton Cong;
    @FXML
    private JFXRadioButton mission;
    @FXML
    private JFXTextField Num;
    @FXML
    private JFXDatePicker DateFin;
    @FXML
    private JFXTextField Titre;
    @FXML
    private TableColumn<?, ?> DateDebut;
    @FXML
    private JFXButton ButtValide;
    @FXML
    private TableColumn<?, ?> DateF;
    @FXML
    private VBox PaneChange;
    @FXML
    private Pane paneee;
    @FXML
    private JFXButton ButtSupp;
  
    ToggleGroup groupe = new ToggleGroup();
    Paint value0=Paint.valueOf("F9C7CC");
    Congé c ;
    Mission m ;
    int showNum = 0;
    @FXML
    private JFXButton Modif;
    @Override
   
    public void initialize(URL url, ResourceBundle rb) {
    Cong.setToggleGroup(groupe);
    mission.setToggleGroup(groupe);
    remplirCombo();
    NCongé.setCellValueFactory(new PropertyValueFactory<>("NCon"));
    NContrat.setCellValueFactory(new PropertyValueFactory<>("IdPer"));
    AdresseC.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
    DateDebut.setCellValueFactory(new PropertyValueFactory<>("DateDeb"));
    TitreCon.setCellValueFactory(new PropertyValueFactory<>("Titre"));
    DateR.setCellValueFactory(new PropertyValueFactory<>("Dater"));
    NMission.setCellValueFactory(new PropertyValueFactory<>("NumM"));
    NMContrat.setCellValueFactory(new PropertyValueFactory<>("IdPer"));
    LieuM.setCellValueFactory(new PropertyValueFactory<>("Lieu"));
    Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
    DateF.setCellValueFactory(new PropertyValueFactory<>("DateF"));
    ObjetM.setCellValueFactory(new PropertyValueFactory<>("Objet"));
    MoyenM.setCellValueFactory(new PropertyValueFactory<>("Moyen"));
    getListeCongé();
    getListeMission();

  
  
    }    

    @FXML
    private void GetCongé(MouseEvent event) throws SQLException {
        Congé();
        Cong.setSelected(true);
        c = TabCongé.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;          // convert from BDD to javafx values 
        LocalDate localDate = LocalDate.parse(c.getDateDeb(), formatter);
        showNum=1;
        Num.setText(c.getNCon());
        Adresse.setText(c.getAdresse());
        Personnel.setValue(c.getIdPer());
        DateDeb.setValue(localDate);
        Titre.setText(c.getTitre());  
       this.ButtValide.setDisable(true); this.Modif.setDisable(false);this.ButtSupp.setDisable(false);

        showNum=0;
        
    }

    @FXML
    private void GetMission(MouseEvent event) throws SQLException {
        Mission();
        mission.setSelected(true);
        m=TabMission.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;          // convert from BDD to javafx values 
        LocalDate localDate = LocalDate.parse(m.getDate(), formatter);
        LocalDate localDate1 = LocalDate.parse(m.getDateF(), formatter);
        showNum=1;
        Num.setText(m.getNumM());
        Adresse.setText(m.getLieu());
        Personnel.setValue(m.getIdPer());
        DateDeb.setValue(localDate);
        DateFin.setValue(localDate1);
        Objet.setText(m.getObjet());
        Moyen.setText(m.getMoyen());
       this.ButtValide.setDisable(true); this.Modif.setDisable(false);this.ButtSupp.setDisable(false);
        showNum=0;
       
    }


   

    
    public void getListeCongé()
    {
      String sql="select * from congé,personnel where IdPerso=NumContrat";
		
        try
        {

            dataCon.clear();

            cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
            rs=ps.executeQuery();

                while(rs.next())
                {
                    dataCon.add(new Congé(                     
                                    rs.getString("NumCongé"),    
                                    rs.getString("IdPerso")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom"),      //   info 
                                    rs.getString("Adresse"),     
                                    rs.getString("DateDeb"), 
                                    rs.getString("Titre"),
                                      rs.getString("DateRet")
                                    ));                       
                    TabCongé.setItems(dataCon);                   
                }
                ps.close();
                rs.close();
                cnx.close();
        }
        catch(Exception e)
        {
                System.err.println(e);
        }  

    }
    public void getListeMission()
    {
        String sql="select * from mission ,personnel where IdPerso=NumContrat";
		
        try
        {

            dataMission.clear();

            cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
                rs=ps.executeQuery();

                while(rs.next())
                {
                        dataMission.add(new Mission(                     // recuperation 
                                        rs.getString("NumMission"),          //   des 
                                        rs.getString("IdPerso")+"  "+rs.getString("Nom")+"  "+rs.getString("Prenom"),      //   info 
                                        rs.getString("Lieu"),     // et les mettres 
                                        rs.getString("DateDeb"),
                                        rs.getString("datefin"),      //dans 
                                        rs.getString("Objet"),      //dans 
                                        rs.getString("Moyen")        // un tableau
                                        ));                        //
                        TabMission.setItems(dataMission);                    //


                }
                ps.close();
                rs.close();
                cnx.close();
        }
        catch(Exception e)
        {
                System.err.println(e);
        }  
    }
  

   
    public void actualiser()
    {
          getListeMission();
          Num.setText("");
     DateDeb.setValue(null);
    Personnel.setValue(null);
    Adresse.setText("");
    Objet.setText("");
    Moyen.setText("");
    DateFin.setValue(null);
    this.mission.setSelected(false);
    Cong.setSelected(false);
     Titre.setDisable(false);
     DateDeb.setValue(null);
     Personnel.setValue(null);
     Adresse.setText("");
     Titre.setText("");
  
    DateFin.setValue(null);
    Objet.setDisable(false);
    Moyen.setDisable(false);
    getListeCongé();
    getListeMission();
    showNum=0;
    }

 
    

         private void ValideCongé() {
          LocalDate da=DateDeb.getValue();
    String NumCongé= Num.getText();
    String pers[] = new String[2];
    pers=Personnel.getSelectionModel().getSelectedItem().split("  ");
    String lieu = Adresse.getText();
    String date=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
    String titre = Titre.getText();
    
    LocalDate dat= DateDeb.getValue().plusDays(30);
    
    String date1=Integer.toString(dat.getYear())+"-"+Integer.toString(dat.getMonthValue())+"-"+Integer.toString(dat.getDayOfMonth());


   
     String sql ="Insert into congé (NumCongé,Adresse,DateDeb,IdPerso,Titre,DateRet)values(?,?,?,?,?,?)";
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
            act(2);
            actualiser();
            CongéImp Congé = new CongéImp();
            Congé.showReport(NumCongé);
            cnx.close();
        } catch (SQLException ex) {
           act(5);
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
            act(1);
            MissionImp Mission = new MissionImp();
            Mission.showReport(mission);
            cnx.close();
        } catch (SQLException ex) {
           act(7);
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
                    { ValideMission(); actualiser();}
                
            }
        }        
        else if(Cong.isSelected())
            {
            if(this.verifieSelection())
            {
                if(this.verifieChampCongé())
                      if(confirmation(2))
                      {ValideCongé();actualiser();}
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
    private void Congé() throws SQLException {
        
        Num.setPromptText("Numéro Congé");
        Adresse.setPromptText("Lieu congé");
        Adresse.setText("");
        Objet.setText("");
        Moyen.setText("");
        DateFin.setValue(null);
        Objet.setDisable(true);
        Moyen.setDisable(true);
        DateFin.setDisable(true);
        Titre.setDisable(false);
       
        ButtValide.setDisable(false); 
        if(showNum==0)
        {this.getNumCongé();this.ButtValide.setDisable(false);this.Modif.setDisable(true);this.ButtSupp.setDisable(true);}

    }
    @FXML
    private void Mission() throws SQLException {
     
        Num.setPromptText("Numéro Mission");
        Adresse.setPromptText("Lieu mission");
        Titre.setText("");
        Adresse.setText("");
        Titre.setDisable(true);
        Objet.setDisable(false);
        Moyen.setDisable(false);  
        DateFin.setDisable(false);
        ButtValide.setDisable(false); 
       if(showNum==0)
       {this.getNumMission();this.ButtValide.setDisable(false);this.Modif.setDisable(true);this.ButtSupp.setDisable(true);}
    }
    
    @FXML
    private void SuppCongé(ActionEvent event) throws SQLException {
        if(Cong.isSelected())
        {
            if(confirmation(1))
            {
            String sql="delete from congé where NumCongé='"+c.getNCon()+"'";
            cnx=new ConnexionBDD().ConnexionDB();
         ps = cnx.prepareStatement(sql);
          ps.executeUpdate();
          cnx.close();
          act(3);actualiser();}
            
        }
        else if(mission.isSelected())
        {
            if(confirmation(1)){
            String sql ="delete from mission where NumMission='"+m.getNumM()+"'";
            cnx=new ConnexionBDD().ConnexionDB();
         ps = cnx.prepareStatement(sql);
          ps.executeUpdate();
          cnx.close();
          act(4);
            actualiser();}
        }
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
            public void act(int i)
  {
     Image im = new Image("/Icones/icons8-approbation-40.png"); 
     Image im1 = new Image("/Icones/icons8-haute-priorité-40.png");
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Mission"); nt.text("Ajout effectué");}
      else if(i==2){nt.title("Congé"); nt.text("Ajout effectué");}
       else if(i==3){nt.title("Congé"); nt.text("Congé supprimé !");}
       else if(i==4){nt.title("Mission"); nt.text("Mission supprimé !");}
       else if(i==5 ){nt.graphic(new ImageView(im1));nt.title("Congé"); nt.text("Ajout non effectué!");}
      else if(i==6 ){nt.graphic(new ImageView(im1));nt.title("Congé"); nt.text("Suppresion non effectué!");}
      else if(i==7 ){nt.graphic(new ImageView(im1));nt.title("Mission"); nt.text("Ajout non effectué!");}
      else if(i==8 ){nt.graphic(new ImageView(im1));nt.title("Mission"); nt.text("Suppresion non effectué!");}
       else if(i==9){nt.title("Mission"); nt.text("Modification effectué");}
      else if(i==10){nt.title("Congé"); nt.text("Modification effectué");}
       else if(i==11){nt.title("Congé"); nt.text("Modification non effectué !");}
       else if(i==12){nt.title("Mission"); nt.text("Modification non effectué!");}
      
      nt.show();


  }
    @FXML
    private void NumContrainte(KeyEvent event) {
             if(!Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }

    
    public void getNumMission() throws SQLException
    {
       int num=0;
        cnx=new ConnexionBDD().ConnexionDB();
        String sql = " select Max(NumMission)as Num from mission";
         ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
                        num=rs.getInt("Num");
                        }
                     num=num+1;
                   Num.setText(Integer.toString(num));
                   Num.setDisable(true);
                   cnx.close();
    }
      public void getNumCongé() throws SQLException
    {
        int num=0;
        cnx=new ConnexionBDD().ConnexionDB();
        String sql = " select Max(NumCongé)as Num from congé";
         ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
                        num=rs.getInt("Num");
                        }
                        num=num+1;
                   Num.setText(Integer.toString(num));
                   Num.setDisable(true);
                   cnx.close();
    }

    @FXML
    private void Modifier(ActionEvent event) throws SQLException {
            if(mission.isSelected())
        {
            if(this.verifieSelection())
            {
                if(this.verifieChampMission())
                    if(confirmation(2))
                    { validModifMission(); actualiser();}
                
            }
        }        
        else if(Cong.isSelected())
            {
            if(this.verifieSelection())
            {
                if(this.verifieChampCongé())
                      if(confirmation(3))
                      {validModifCongé();actualiser();}
            }
        }  
           
    }
    
    public void validModifCongé() throws SQLException
    {
    cnx=new ConnexionBDD().ConnexionDB();
    Statement st= cnx.createStatement();
    LocalDate da=DateDeb.getValue();
    String NumCongé= Num.getText();
    String pers[] = new String[2];
    pers=Personnel.getSelectionModel().getSelectedItem().split("  ");
    String lieu = Adresse.getText();
    String date=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
    String titre = Titre.getText();    
    LocalDate dat= DateDeb.getValue().plusDays(30);   
    String date1=Integer.toString(dat.getYear())+"-"+Integer.toString(dat.getMonthValue())+"-"+Integer.toString(dat.getDayOfMonth());
    String sql="UPDATE `parcdattractions`.`congé` SET `Adresse` = '"+lieu+"', `DateDeb` = '"+date+"', `IdPerso` = '"+pers[0]+"', `Titre` = '"+titre+"', `DateRet` = '"+dat+"' WHERE `congé`.`NumCongé` = '"+NumCongé+"'";
                st.executeUpdate(sql);
                 actualiser();
                 act(10);
                 cnx.close();
    }
    public void validModifMission() throws SQLException
    {

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
    cnx=new ConnexionBDD().ConnexionDB();
    Statement st= cnx.createStatement();
    String sql ="UPDATE mission SET `Lieu` = '"+lieu+"', `Objet` = '"+obj+"', `Moyen` = '"+moyen+"', `DateDeb` = '"+date+"', `datefin` = '"+date1+"', `IdPerso` = '"+pers[0]+"' WHERE NumMission = "+mission+"";
    st.executeUpdate(sql);
    actualiser();
    act(9);
    cnx.close();
    }
}
