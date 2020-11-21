package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tableau.TabPointage;
import tableau.TabPointage_2;





public class Pointagee implements Initializable {
   private String selectedItem1 = null;
    ToggleGroup tg1 = null;
    private String selectedItem2 = null;
    ToggleGroup tg2 = null;
    ObservableList<TabPointage> data = FXCollections.observableArrayList();
    ObservableList<TabPointage_2> data_2 = FXCollections.observableArrayList();    
    FilteredList<TabPointage> filtredData = new FilteredList<>(data,e->true);
    FilteredList<TabPointage_2> filtredData_2 = new FilteredList<>(data_2,e1->true);
    
    
    @FXML
    private JFXRadioButton rbSécurité;
    @FXML
    private JFXRadioButton rbOperateur;
    @FXML
    private JFXRadioButton rbMaintenance;
    @FXML
    private JFXRadioButton rbCaissier;
    @FXML
    private JFXComboBox<String> choixEmpPointage;
    @FXML
    private JFXComboBox<String> choixLieuPointage;
    @FXML
    private TableView<TabPointage> tabCaissierOp;
    @FXML
    private TableColumn<?, ?> NContrat;
    @FXML
    private TableColumn<?, ?> Prénom;
    @FXML
    private TableColumn<?, ?> Nom;
    @FXML
    private TableColumn<?, ?> DateTravaill;
    @FXML
    private TableColumn<?, ?> LieuTravail;
    @FXML
    private TableColumn<?, ?> NContrat_2;
    @FXML
    private TableColumn<?, ?> Prénom_2;
    @FXML
    private TableColumn<?, ?> Nom_2;
    @FXML
    private TableColumn<?, ?> Date_de_travail_2;
    @FXML
    private TableView<TabPointage_2> tabMainSec;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
     int click = 0;
    ArrayList<String> numContratCaissierRech = new ArrayList<String>();
    ArrayList<String> numContratOpérateurRech = new ArrayList<String>();
    ArrayList<String> numContratMaintRech = new ArrayList<String>();
    ArrayList<String> numContratSécRech = new ArrayList<String>();
    TabPointage tp = null ;
    TabPointage_2 tp_2 = null ;
    public Connection cnx;
    String rechQuery = null;
    
    @FXML
    private JFXRadioButton rbCaissierRech;
    @FXML
    private JFXComboBox<String> choixEmpRech;
    @FXML
    private JFXComboBox<String> choixLieuRech;
    @FXML
    private JFXRadioButton rbRechOp;
    @FXML
    private JFXRadioButton rbMaintRech;
    @FXML
    private JFXRadioButton rbSécRech;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    Paint value0=Paint.valueOf("F9C7CC");
    @FXML
    private JFXButton Supp;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tg1 = new ToggleGroup();
        tg2 = new ToggleGroup();
        
        rbSécurité.setToggleGroup(tg1);
        rbOperateur.setToggleGroup(tg1);
        rbMaintenance.setToggleGroup(tg1);
        rbCaissier.setToggleGroup(tg1);
        
        rbCaissierRech.setToggleGroup(tg2);
        rbRechOp.setToggleGroup(tg2);
        rbMaintRech.setToggleGroup(tg2);
        rbSécRech.setToggleGroup(tg2);
        
        NContrat.setCellValueFactory(new PropertyValueFactory<>("NContrat"));
        Prénom.setCellValueFactory(new PropertyValueFactory<>("Prénom"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        DateTravaill.setCellValueFactory(new PropertyValueFactory<>("Date_de_travail"));
        LieuTravail.setCellValueFactory(new PropertyValueFactory<>("Lieu_de_travail"));
        
        NContrat_2.setCellValueFactory(new PropertyValueFactory<>("NContrat_2"));
        Prénom_2.setCellValueFactory(new PropertyValueFactory<>("Prénom_2"));
        Nom_2.setCellValueFactory(new PropertyValueFactory<>("Nom_2"));
        Date_de_travail_2.setCellValueFactory(new PropertyValueFactory<>("Date_de_travail_2"));
        
        
        try {
            loadDatabaseData();
            loadDBData();
            listerPersonnelRech();
        } catch (SQLException ex) {
            Logger.getLogger(Pointagee.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void recupererValeurTG1() throws SQLException {
        
        RadioButton selectedRB = (RadioButton) tg1.getSelectedToggle();
        
        selectedItem1 = selectedRB.getText();
        choixEmpPointage.setDisable(false);
        
        if(selectedItem1.equals("Caissier"))
        {
            listerPersonnel("Caissier");
            listerLieux("Caisse");
        }
        else if(selectedItem1.equals("Opérateur"))
        {
            listerPersonnel("Opérateur");
            listerLieux("Manége");
        }
        else if(selectedItem1.equals("Sécurité"))
        {
            listerPersonnel("Sécurité");
            choixLieuPointage.setDisable(true);
        }
        else if(selectedItem1.equals("Maintenance")) 
        {
            listerPersonnel("Maintenance");
            choixLieuPointage.setDisable(true);
        }
    }
    
    
  private void listerPersonnel(String poste) throws SQLException
    {
        String sql = "SELECT NumContrat, Nom, Prenom FROM personnel,poste WHERE NumContrat NOT IN(SELECT IdPersonnel FROM pointage WHERE Date = DATE(Now()) UNION SELECT DISTINCT Caissier FROM gérecaisse WHERE Date = DATE(Now()) AND Exist='0' UNION SELECT géremanége.NumContrat FROM géremanége WHERE Date = DATE(Now()) ) AND personnel.IdPoste = poste.IdPoste AND poste.Libelle = '"+poste+"' AND ADDDATE(DateDebCon, INTERVAL Durée MONTH) >= DATE(NOW())";
        String itemEmp=null;
        
        choixEmpPointage.getItems().clear();
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {   
            itemEmp=rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom");
            choixEmpPointage.getItems().add(itemEmp);
        }
        cnx.close();
    }
    private void listerPersonnelRech() throws SQLException
    {
        String sql = "SELECT NumContrat, Nom, Prenom FROM personnel,poste WHERE ADDDATE(DateDebCon, INTERVAL Durée MONTH) >= DATE(NOW()) AND (personnel.IdPoste=poste.IdPoste) AND(Libelle='Caissier' OR Libelle='Sécurité' OR Libelle='Opérateur' OR Libelle='Maintenance')";
        String itemEmp=null;
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            System.out.println(rs.getString("NumContrat"));
            itemEmp=rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom");
            choixEmpRech.getItems().add(itemEmp);
        }
        cnx.close();
    }
    
   private void listerLieux(String lieu) throws SQLException
    {
        String sql = "SELECT Libelle FROM "+lieu;
        
        choixLieuPointage.setDisable(false);
        choixLieuPointage.getItems().clear();
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if(lieu.equals("Manége"))
        while (rs.next())
        {
            choixLieuPointage.getItems().add(rs.getString("Libelle")+"  ("+nbrOperateur(rs.getString("Libelle"))+" Opér.)");
        }
        else
        {   
            sql = "SELECT caisse.IdCaisse,Libelle FROM caisse WHERE IdCaisse NOT IN (SELECT gérecaisse.IdCaisse FROM gérecaisse,caisse WHERE caisse.IdCaisse=gérecaisse.IdCaisse AND Date=DATE(NOW()) AND Exist='0')";
            
            rs=st.executeQuery(sql);
            while (rs.next())
            {   
                choixLieuPointage.getItems().add(rs.getString("Libelle"));
            }
            
        }
        cnx.close();

    }
    
    
    private int nbrOperateur(String manège)
    {
        int nbr=0;
        
        
        String sql="SELECT COUNT(géremanége.NumContrat) AS nbrOpTitre FROM géremanége,manége WHERE manége.IdManége=géremanége.IdManége AND manége.Libelle='"+manège+"' AND Date=DATE(NOW())";
        
        try{
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st=cnx.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while (rs.next())
                nbr=rs.getInt("nbrOpTitre");
            cnx.close();
            }
        catch(Exception ex)
            {
               System.out.println(ex.getMessage()); 
            }
        return nbr;
    } 
    
      
    
    public void effectuerPointage(String numContrat) throws SQLException
    {
        
        Date date=null;
        String query = "SELECT MAX(DATE(Date)) AS dateAuj FROM journée";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        while(rs.next())
            date= rs.getDate("dateAuj");
        String sql = "INSERT INTO pointage(IdPersonnel,Date) VALUES (?,?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        
        ps.setString(1,numContrat);
        ps.setDate(2, (java.sql.Date) date);
        
        ps.executeUpdate();
        actualiser();act(1);
        cnx.close();
        
    }
    
    private void effectuerPointageCaissier(String numContrat,String caisse)
    {
        int IdCaisse=0;
       try
        {
            String sql2=null;
            String sql = "SELECT IdCaisse,TypeCaisse FROM caisse WHERE Libelle='"+caisse+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs3 = st.executeQuery(sql);
            
            while(rs3.next())
            {
                IdCaisse=rs3.getInt("IdCaisse");
                if(rs3.getString("TypeCaisse").equals("Entrée"))
                    sql2 = "SELECT Code FROM ticket";
                else
                    sql2 = "SELECT Code FROM ticket WHERE Code != 9";
            }
            
            sql="SELECT DISTINCT IdCaisse FROM gérecaisse WHERE Exist='1' AND Date=DATE(NOW()) AND Caissier='"+numContrat+"' AND IdCaisse='"+IdCaisse+"'";//Debut
            
            Statement st2 = cnx.createStatement();
            ResultSet rs4 = st2.executeQuery(sql);
            int IdCaisseGC=0;
            
            while(rs4.next())
            {   
                System.out.println("123");
                IdCaisseGC=rs4.getInt("IdCaisse");
            }
            if(IdCaisseGC!=0)
            {   
                sql="UPDATE gérecaisse SET Exist='0' WHERE Date=DATE(NOW())AND Caissier='"+numContrat+"' AND IdCaisse='"+IdCaisse+"'";
                PreparedStatement ps = cnx.prepareStatement(sql);
    
                ps.executeUpdate(sql);
            }
            else                                                                                                                                //Fin
            {   
               
            ResultSet rs2 = st.executeQuery(sql2);
            
            sql="INSERT INTO gérecaisse(Caissier,CodeTicket,IdCaisse,Date,NbrTicketDonné,Exist,Monnaie,Recette) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(sql);
            
            while (rs2.next())
            {   
                
                ps.setString(1, numContrat);
                ps.setInt(2, rs2.getInt("Code"));
                ps.setInt(3, IdCaisse);
                ps.setDate(4, (java.sql.Date) maxDate("journée","Date"));
                ps.setInt(5, 0);
                ps.setInt(6, 0);
                ps.setInt(7, 0);
                ps.setInt(8, 0);
                ps.executeUpdate();
   
            }
            }
            cnx.close();}
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
    }
    
    private void effectuerPointageOpérateur(String numContrat,String manège) throws SQLException
    {
        int IdManège=0;
        String tab[] = new String[2];
        tab=manège.split("  ");
        
        String query="SELECT IdManége FROM Manége WHERE Libelle='"+tab[0]+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);
        while(rs.next())
            IdManège=rs.getInt("IdManége");
        String sql = null;
        
        sql="INSERT INTO géremanége(NumContrat,IdManége,Date) VALUES (?,?,?)";
        
        
        PreparedStatement ps = cnx.prepareStatement(sql);
        
        ps.setString(1,numContrat);
        ps.setInt(2,IdManège);
        ps.setDate(3, (java.sql.Date) maxDate("journée","Date"));
        
        ps.executeUpdate();
        actualiser();act(1);
        cnx.close();
        
    }
 
    
    public Date maxDate(String table,String champ) throws SQLException
    {
        Date date=null;
        String query = "SELECT MAX(DATE("+champ+")) AS dateAuj FROM "+table;
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        while(rs.next())
                date= rs.getDate("dateAuj");
        cnx.close();
        return date;
    }

    @FXML
    private void buttValider(ActionEvent event)   {
        if(validation(2))
        {
  
        String tab[]=new String[2];
        if(hBox1.isDisable())
        {   
            int res=0;
           
            cnx = new ConnexionBDD().ConnexionDB();
            if(rbCaissier.isSelected())
            {
                try
                {
                String sql="SELECT IdCaisse FROM caisse WHERE Libelle='"+choixLieuPointage.getSelectionModel().getSelectedItem()+"'";
                Statement st= cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next())
                    res=rs.getInt("IdCaisse");
                sql="UPDATE caisse SET Etat='0' WHERE Libelle='"+tp.getLieu_de_travail()+"'";
                st.executeUpdate(sql);
                sql="UPDATE caisse SET Etat='1' WHERE IdCaisse='"+res+"'";
                st.executeUpdate(sql);
                sql = "UPDATE gérecaisse SET IdCaisse='"+res+"' WHERE Caissier='"+tp.getNContrat()+"' AND Date=DATE(NOW())";
                st.executeUpdate(sql);
                
        actualiser();act(1);
                }catch(SQLException ex){act(3);}
            }
            else
            {
                try
                {
                tab=choixLieuPointage.getSelectionModel().getSelectedItem().split("  ");
                String sql="SELECT IdManége FROM manége WHERE Libelle='"+tab[0]+"'";
                Statement st= cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next())
                    res=rs.getInt("IdManége");
                
                sql="UPDATE `géremanége` SET `IdManége` = '"+res+"' WHERE `NumContrat` = '"+tp.getNContrat()+"' AND `géremanége`.`Date` = DATE(NOW())";
                st.executeUpdate(sql);
              
                 actualiser();act(1);
            }catch(SQLException ex){act(3);}}
            
            
        }
        else{
            if(choixEmpPointage.getSelectionModel().getSelectedItem()!=null)
            {
                tab = choixEmpPointage.getSelectionModel().getSelectedItem().split("-");
                if ((selectedItem1.equals("Maintenance") || selectedItem1.equals("Sécurité")))
                    try{effectuerPointage(tab[0]);}catch(SQLException e){act(3);}
                else if (selectedItem1.equals("Caissier") && choixLieuPointage.getSelectionModel().getSelectedItem()!=null)
                    effectuerPointageCaissier(tab[0],choixLieuPointage.getSelectionModel().getSelectedItem());
                else if (selectedItem1.equals("Opérateur") && choixLieuPointage.getSelectionModel().getSelectedItem()!=null)
                    try{effectuerPointageOpérateur(tab[0],choixLieuPointage.getSelectionModel().getSelectedItem());}catch(SQLException e ){act(3);}
            }
            }
            try { 
                cnx.close();
                click=0;
            } catch (SQLException ex) {
                Logger.getLogger(Pointagee.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       
    }
    
    @FXML
    private void clickTab() throws SQLException
    {   
        try{
            String lib=null;
            String dateNow=null;
            tp=tabCaissierOp.getSelectionModel().getSelectedItem();
            String sql = "SELECT Libelle FROM personnel,poste WHERE NumContrat='"+tp.getNContrat()+"' AND personnel.IdPoste=poste.IdPoste";
            String sql1="SELECT DATE(NOW()) AS dateNow";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
                lib=rs.getString("Libelle");
            if(lib.equals("Opérateur"))
                {
                    rbOperateur.setSelected(true);
                  
                }
            else if(lib.equals("Caissier"))
                {
                    rbCaissier.setSelected(true);
                 
                }

            recupererValeurTG1();
            
            choixEmpPointage.setDisable(true);
            choixEmpPointage.setValue(tp.getNContrat()+"-"+tp.getNom()+" "+tp.getPrénom());
            rs=st.executeQuery(sql1);
            while(rs.next())
                dateNow=rs.getString("dateNow");
            
            if(tp.getDate_de_travail().equals(dateNow))
            { choixLieuPointage.setDisable(false); Supp.setDisable(false);}
            else
            {choixLieuPointage.setDisable(true);Supp.setDisable(true);}
            choixLieuPointage.setValue(tp.getLieu_de_travail());
            cnx.close();
            hBox1.setDisable(true);
            hBox2.setDisable(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        click=1;
    }
    
    @FXML
    private void clickTab_2() throws SQLException
    {    String dateNow=null;
        try{
        tp_2 = tabMainSec.getSelectionModel().getSelectedItem();
        
        choixLieuPointage.setValue(null);
        choixLieuPointage.setDisable(true);
        
        String sql = "SELECT Libelle FROM personnel,poste WHERE NumContrat='"+tp_2.getNContrat_2()+"' AND personnel.IdPoste=poste.IdPoste";
         String sql1="SELECT DATE(NOW()) AS dateNow";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql); 
        while(rs.next())
        {
            if(rs.getString("Libelle").equals("Maintenance"))
            {
                rbMaintenance.setSelected(true);
            }
            else if(rs.getString("Libelle").equals("Sécurité"))
            {
                rbSécurité.setSelected(true);
            }
            recupererValeurTG1();
        }
          rs=st.executeQuery(sql1);
            while(rs.next())
                dateNow=rs.getString("dateNow");
            
            if(tp_2.Date_de_travail_2.equals(dateNow))
            { Supp.setDisable(false);}
            else Supp.setDisable(true);
        cnx.close();
        
        choixEmpPointage.setDisable(true);
        choixEmpPointage.setValue(tp_2.getNContrat_2()+"-"+tp_2.getNom_2()+" "+tp_2.getPrénom_2());
        
        hBox1.setDisable(true);
        hBox2.setDisable(true);
        }
        catch(Exception ex)
        {
            
        }
        click =1;
    }
        
    public void loadDatabaseData()
    {
	String sql="select personnel.NumContrat,Nom,Prenom,Date,Libelle from manége ,personnel ,géremanége where (SELECT MONTH(Date))=(SELECT MONTH(DATE(NOW()))) AND géremanége.IdManége = manége.IdManége and personnel.NumContrat=géremanége.NumContrat";
	String sql1="select personnel.NumContrat,Nom,Prenom,Date,Libelle from caisse ,personnel ,gérecaisse where (SELECT MONTH(Date))=(SELECT MONTH(DATE(NOW()))) AND gérecaisse.IdCaisse= caisse.IdCaisse and personnel.NumContrat=gérecaisse.Caissier group by Caissier,Date"; 
        
        try
        {
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            
            while(rs.next())
            {
                data.add(new TabPointage(                     // recuperation 
		rs.getString("personnel.NumContrat"),          //   des 
		rs.getString("Nom"),      //   info 
		rs.getString("Prenom"),     // et les mettres 
                rs.getString("Date"),      //dans 
                rs.getString("Libelle")       // un tableau
                ));                       
		tabCaissierOp.setItems(data);      
            }
                        
            PreparedStatement ps1 = cnx.prepareStatement(sql1);
            ResultSet rs1=ps1.executeQuery();
            while(rs1.next())
            {
                data.add(new TabPointage(                     // recuperation 
                    rs1.getString("personnel.NumContrat"),          //   des 
                    rs1.getString("Nom"),      //   info 
                    rs1.getString("Prenom"),     // et les mettres 
                    rs1.getString("Date"),      //dans 
                    rs1.getString("Libelle")       // un tableau
                    ));
                tabCaissierOp.setItems(data);             
            }
         
          cnx.close();  
        }
        catch(Exception e)
	{
            System.err.println(e.getMessage());
	}
    }
    
    private void loadDBData() throws SQLException
    {
        String sql="SELECT NumContrat,Nom,Prenom,Date FROM pointage,personnel WHERE (SELECT MONTH(Date))=(SELECT MONTH(DATE(NOW()))) AND NumContrat=IdPersonnel";
        data_2.clear();
        cnx = new ConnexionBDD().ConnexionDB();
        PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                data_2.add(new TabPointage_2(                       // recuperation 
                    rs.getString("NumContrat"),                //   des 
                    rs.getString("Nom"),                       //   info 
                    rs.getString("Prenom"),                    // et les mettre
                    rs.getString("Date")                    //dans 
                                                              // un tableau
                ));                        
		tabMainSec.setItems(data_2);                        
            }
        cnx.close();
        
    }

    @FXML
    private void buttSupprimer(ActionEvent event) throws SQLException {
       cnx = new ConnexionBDD().ConnexionDB();
        if(validation(1))
        {
        try
        {   
            if(selectedItem1.equals("Caissier"))
            {   
                String sql = "SELECT Etat FROM caisse WHERE Libelle='"+tp.getLieu_de_travail()+"'";
                int etat=0;
               Statement st = cnx.createStatement();
                ResultSet rs=st.executeQuery(sql);
                while(rs.next())
                    etat=rs.getInt("Etat");
                if (etat==0)
                {
                    sql="DELETE FROM gérecaisse WHERE Caissier='"+tp.getNContrat()+"'AND Date=DATE(NOW())";
                    st.executeUpdate(sql);
                }
                else
                    act(4);
            }
            else if(selectedItem1.equals("Opérateur"))
            {
                String sql="DELETE FROM géremanége WHERE NumContrat='"+tp.getNContrat()+"'AND Date=DATE(NOW())";
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                 actualiser();
            act(2);
                
            }
            else
            {   
                String sql="DELETE FROM pointage WHERE IdPersonnel='"+tp_2.getNContrat_2()+"'AND Date=DATE(NOW())";
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                 actualiser();
            act(2);
            }
            cnx = new ConnexionBDD().ConnexionDB();
           
        }
        catch(Exception e)
        {
          System.out.print(e);
        }
        
        }
        click=0;
    }
    
    private void actualiser() throws SQLException
    {
        click=0;
        rbOperateur.setSelected(false);
        rbCaissier.setSelected(false);
        rbSécurité.setSelected(false);
        rbMaintenance.setSelected(false);
        rbMaintRech.setSelected(false);
        rbSécRech.setSelected(false);
        rbRechOp.setSelected(false);
        rbCaissierRech.setSelected(false);
        
        hBox1.setDisable(false);
        hBox2.setDisable(false);
        Supp.setDisable(true);
        
        /*choixEmpPointage.setDisable(false);
        choixLieuPointage.setDisable(false);*/
        choixLieuRech.setDisable(false);
        choixLieuPointage.setValue("");
        choixEmpPointage.setValue("");
        choixLieuRech.setValue(null);
        choixEmpRech.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        choixEmpPointage.setDisable(true);
        choixLieuPointage.setDisable(true);
        Paint value=Paint.valueOf("EAEDF1");
        choixEmpPointage.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
        choixLieuPointage.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
        
        
        loadDatabaseData();
        loadDBData();
    }
    @FXML
    private void buttActualiser(ActionEvent event) throws SQLException {
        actualiser();
        Supp.setDisable(false);
    }

    @FXML
    private void cherchEmp(KeyEvent event) throws SQLException 
    {
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='"+selectedItem1+"' AND ADDDate(DateDebCon,INTERVAL Durée MONTH) >= DATE(NOW()) AND (Nom LIKE '%"+choixEmpPointage.getValue()+"%' OR Prenom LIKE '%"+choixEmpPointage.getValue()+"%')";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixEmpPointage.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            choixEmpPointage.setVisibleRowCount(i);
            choixEmpPointage.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            choixEmpPointage.show();
        }
        cnx.close();
    }

    @FXML
    private void cherchLieu(KeyEvent event) throws SQLException 
    {
        cnx=new ConnexionBDD().ConnexionDB();
        if (selectedItem1.equals("Opérateur"))
        {   
            String sql = "SELECT IdManége,Libelle FROM Manége "
                + "WHERE  Libelle LIKE '%"+choixLieuPointage.getValue()+"%'";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixLieuPointage.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                choixLieuPointage.setVisibleRowCount(i);
                choixLieuPointage.getItems().add(rs.getInt("IdManége")+"|"+rs.getString("Libelle")+"  ("+nbrOperateur(rs.getString("Libelle"))+")");
                choixLieuPointage.show();
            }
        }else if(selectedItem1.equals("Caissier"))
        {
            String sql = "SELECT Libelle FROM Caisse "
                + "WHERE  Libelle LIKE '%"+choixLieuPointage.getValue()+"%'";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixLieuPointage.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                choixLieuPointage.setVisibleRowCount(i);
                choixLieuPointage.getItems().add(rs.getString("Libelle"));
                choixLieuPointage.show();
            }
        }
            
            cnx.close();
    }

    @FXML
    private void rechCaissierRB(ActionEvent event) throws SQLException 
    {   
        selectedItem2="Caissier";
        choixLieuRech.setDisable(false);
        
        String sql = "SELECT NumContrat,Prenom,Nom "
                + "FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='Caissier'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs =st.executeQuery(sql);
        choixEmpRech.getItems().clear();
        while (rs.next())
        {
            choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
        cnx.close();
        
        sql= "SELECT IdCaisse,Libelle FROM caisse";
        cnx=new ConnexionBDD().ConnexionDB();
        st = cnx.createStatement();
        rs =st.executeQuery(sql);
        choixLieuRech.getItems().clear();
        while (rs.next())
        {
            choixLieuRech.getItems().add(rs.getString("IdCaisse")+"-"+rs.getString("Libelle"));
        }
        cnx.close();   
    }
    
    @FXML
    private void rechOpérateurRB(ActionEvent event) throws SQLException 
    {   
        selectedItem2="Opérateur"; 
        choixLieuRech.setDisable(false);
        
        String sql = "SELECT NumContrat,Prenom,Nom "
                + "FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='Opérateur'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs =st.executeQuery(sql);
        choixEmpRech.getItems().clear();
        while (rs.next())
        {
            choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
        cnx.close();
        
        sql= "SELECT IdManége,Libelle FROM manége";
        cnx=new ConnexionBDD().ConnexionDB();
        st = cnx.createStatement();
        rs =st.executeQuery(sql);
        choixLieuRech.getItems().clear();
        while (rs.next())
        {
            choixLieuRech.getItems().add(rs.getString("IdManége")+"-"+rs.getString("Libelle"));
        }
        cnx.close();
    }

    @FXML
    private void rechMaintRB(ActionEvent event) throws SQLException 
    {   
        selectedItem2="Maintenance";
        choixLieuRech.setDisable(true);
        
        String sql = "SELECT NumContrat,Prenom,Nom "
                + "FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='Maintenance'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs =st.executeQuery(sql);
        choixEmpRech.getItems().clear();
        while (rs.next())
        {
            choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
        cnx.close();
    }


    @FXML
    private void rechSécRB(ActionEvent event) throws SQLException 
    {   
        selectedItem2="Sécurité";
        choixLieuRech.setDisable(true);
        
        String sql = "SELECT NumContrat,Prenom,Nom "
                + "FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='Sécurité'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs =st.executeQuery(sql);
        choixEmpRech.getItems().clear();
        while (rs.next())
        {
            choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
        cnx.close();
    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException 
    {   
        String tab[] = new String[2];
        String tab2[] = new String[2];
        String sql=null;
        
        if(choixEmpRech.getValue()!=null && choixLieuRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null)
        {   
            tab=choixEmpRech.getValue().split("-");
            if(selectedItem2.equals("Caissier"))
            {   
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier= '"+tab[0]+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat= '"+tab[0]+"' AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Maintenance"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel= '"+tab[0]+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
                    }
            else if(selectedItem2.equals("Sécurité"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel= '"+tab[0]+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
                    }
        }else if(choixEmpRech.getValue()!= null && choixLieuRech.getValue()!= null && dateDu.getValue()==null && dateAu.getValue()==null)
        {   
            tab=choixEmpRech.getValue().split("-");
            tab2=choixLieuRech.getValue().split("-");
            if (selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier= '"+tab[0]+"' AND gérecaisse.IdCaisse='"+tab2[0]+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB(); 
      
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {   
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat= '"+tab[0]+"' AND géremanége.IdManége='"+tab2[0]+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB(); 
                System.out.println(tab[0]);
                System.out.println(tab2[0]);
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {   
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }
        }else if(choixEmpRech.getValue()!=null && choixLieuRech.getValue()!=null && dateAu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixEmpRech.getValue().split("-");
            tab2=choixLieuRech.getValue().split("-");
            if (selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier= '"+tab[0]+"' AND gérecaisse.IdCaisse='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }else if (selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat= '"+tab[0]+"' AND géremanége.IdManége='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }
        }else if(choixLieuRech.getValue()!=null && choixEmpRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null)
        {   
            tab2=choixLieuRech.getValue().split("-");
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.IdCaisse= '"+tab2[0]+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }else if (selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.IdManége= '"+tab2[0]+"' "
                        + "AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }
        }else if(dateDu.getValue()!= null && choixEmpRech.getValue()==null && choixLieuRech.getValue()==null && dateAu.getValue()==null)
        {
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();       
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();      
            }else if(selectedItem2.equals("Maintenance"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }else if(selectedItem2.equals("Sécurité"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }
    }else if(choixEmpRech.getValue()!=null && dateDu.getValue()!=null && choixLieuRech.getValue()==null && dateAu.getValue()==null)
    {
        tab = choixEmpRech.getValue().split("-");
        if (selectedItem2.equals("Caissier"))
        {
            sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier= '"+tab[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
        }else if(selectedItem2.equals("Opérateur"))
        {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat= '"+tab[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
        }else if (selectedItem2.equals("Maintenance"))
        {
            sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel='"+tab[0]+"' AND "
                        + "Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
        }else if (selectedItem2.equals("Sécurité"))
        {
            sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel='"+tab[0]+"' AND "
                        + "Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
        }
    }else if(dateAu.getValue()!= null && choixEmpRech.getValue() == null && choixLieuRech.getValue()==null && dateDu.getValue()==null)
        {
            if (selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Maintenance"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }else if(selectedItem2.equals("Sécurité"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }
        }else if(choixEmpRech.getValue()!=null && dateAu.getValue()!=null && choixLieuRech.getValue()==null && dateDu.getValue()==null)
        {   
            tab=choixEmpRech.getValue().split("-");
            if(selectedItem2.equals("Caissier"))
            {
               sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier='"+tab[0]+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close(); 
            }else if (selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat='"+tab[0]+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close(); 
            }else if(selectedItem2.equals("Maintenance"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }else if (selectedItem2.equals("Sécurité"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }
        }else if(choixEmpRech.getValue()!=null && choixLieuRech.getValue()!=null && dateAu.getValue()!=null && dateDu.getValue()==null)
        {
            tab=choixEmpRech.getValue().split("-");
            tab2=choixLieuRech.getValue().split("-");
            if (selectedItem2.equals("caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier='"+tab[0]+"' "
                        + "AND gérecaisse.IdCaisse= '"+tab2[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat='"+tab[0]+"' "
                        + "AND géremanége.IdManége= '"+tab2[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();    
            }
        }else if(choixLieuRech.getValue()!=null && dateDu.getValue()!=null && choixEmpRech.getValue()==null && dateAu.getValue()==null)
        {
            tab2=choixLieuRech.getValue().split("-");
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.IdCaisse='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.IdManége='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }
        }else if(choixLieuRech.getValue()!=null && dateAu.getValue()!=null && choixEmpRech.getValue()==null && dateDu.getValue()==null)
        {
            tab2=choixLieuRech.getValue().split("-");
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.IdCaisse='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.IdManége='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' AND "
                        + "géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }
        }else if(choixLieuRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && choixEmpRech.getValue()==null)
        {   
            tab2=choixLieuRech.getValue().split("-");
            
            if (selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.IdCaisse='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if (selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.IdManége='"+tab2[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }
        }else if(choixEmpRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && choixLieuRech.getValue()==null)
        {   
            tab=choixEmpRech.getValue().split("-");
            
            if (selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE gérecaisse.Caissier='"+tab[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {   
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if (selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE géremanége.NumContrat='"+tab[0]+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Maintenance"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }else if(selectedItem2.equals("Sécurité"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }
        }else if(dateDu.getValue()!=null && dateAu.getValue()!=null && choixEmpRech.getValue()==null && choixLieuRech.getValue()==null)
        {
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                        + "FROM personnel,gérecaisse,caisse "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND gérecaisse.Caissier=personnel.NumContrat "
                        + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {   
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if(selectedItem2.equals("Opérateur"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                        + "FROM personnel,géremanége,manége "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND géremanége.NumContrat=personnel.NumContrat "
                        + "AND géremanége.IdManége=manége.IdManége "
                        + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                cnx=new ConnexionBDD().ConnexionDB();  
                Statement st = cnx.createStatement();
                ResultSet rs =st.executeQuery(sql);
                tabCaissierOp.getItems().clear();
                while (rs.next())
                {   
                    data.add(new TabPointage(                     
                            rs.getString("NumContrat"),
                            rs.getString("Prenom"),     
                            rs.getString("Nom"),     
                            rs.getString("Date"),      
                            rs.getString("Libelle")      
                            ));
                            tabCaissierOp.setItems(data);
                }
                cnx.close();
            }else if (selectedItem2.equals("Maintenance"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }else if (selectedItem2.equals("Sécurité"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
            }
        }else if (choixEmpRech.getValue() != null && choixLieuRech.getValue() != null && dateDu.getValue() != null && dateAu.getValue() != null)
        {
            tab=choixEmpRech.getValue().split("-");
            tab2=choixLieuRech.getValue().split("-");
            
            if(selectedItem2.equals("Caissier"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                            + "FROM personnel,gérecaisse,caisse "
                            + "WHERE gérecaisse.Caissier='"+tab[0]+"' "
                            + "AND gérecaisse.IdCaisse= '"+tab2[0]+"'"
                            + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                            + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                            + "AND gérecaisse.Caissier=personnel.NumContrat "
                            + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                            + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                    cnx=new ConnexionBDD().ConnexionDB();  
                    Statement st = cnx.createStatement();
                    ResultSet rs =st.executeQuery(sql);
                    tabCaissierOp.getItems().clear();
                    while (rs.next())
                    {   
                        data.add(new TabPointage(                     
                                rs.getString("NumContrat"),
                                rs.getString("Prenom"),     
                                rs.getString("Nom"),     
                                rs.getString("Date"),      
                                rs.getString("Libelle")      
                                ));
                                tabCaissierOp.setItems(data);
                    }
                    cnx.close();
            }else if (selectedItem2.equals("Opérateur"))
                {
                    sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                            + "FROM personnel,géremanége,manége "
                            + "WHERE géremanége.NumContrat='"+tab[0]+"' "
                            + "AND géremanége.IdManége= '"+tab2[0]+"'"
                            + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                            + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                            + "AND géremanége.NumContrat=personnel.NumContrat "
                            + "AND géremanége.IdManége=manége.IdManége "
                            + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                    cnx=new ConnexionBDD().ConnexionDB();  
                    Statement st = cnx.createStatement();
                    ResultSet rs =st.executeQuery(sql);
                    tabCaissierOp.getItems().clear();
                    while (rs.next())
                    {   
                        data.add(new TabPointage(                     
                                rs.getString("NumContrat"),
                                rs.getString("Prenom"),     
                                rs.getString("Nom"),     
                                rs.getString("Date"),      
                                rs.getString("Libelle")      
                                ));
                                tabCaissierOp.setItems(data);
                    }
                    cnx.close();
                }
            }else if (choixEmpRech.getValue() == null && choixLieuRech.getValue() == null && dateDu.getValue() == null && dateAu.getValue() == null)
            {
                if(selectedItem2.equals("Caissier"))
                {
                    sql = "SELECT personnel.NumContrat,Nom,Prenom,gérecaisse.Date,Libelle "
                            + "FROM personnel,gérecaisse,caisse "
                            + "WHERE gérecaisse.Caissier=personnel.NumContrat "
                            + "AND gérecaisse.IdCaisse=caisse.IdCaisse "
                            + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                    cnx=new ConnexionBDD().ConnexionDB();  
                    Statement st = cnx.createStatement();
                    ResultSet rs =st.executeQuery(sql);
                    tabCaissierOp.getItems().clear();
                    while (rs.next())
                    {   
                        data.add(new TabPointage(                     
                                rs.getString("NumContrat"),
                                rs.getString("Prenom"),     
                                rs.getString("Nom"),     
                                rs.getString("Date"),      
                                rs.getString("Libelle")      
                                ));
                                tabCaissierOp.setItems(data);
                    }
                    cnx.close();
                }else if(selectedItem2.equals("Opérateur"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,géremanége.Date,Libelle "
                                + "FROM personnel,géremanége,manége "
                                + "WHERE géremanége.NumContrat=personnel.NumContrat "
                                + "AND géremanége.IdManége=manége.IdManége "
                                + "GROUP BY NumContrat,Nom,Prenom,Date,Libelle";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabCaissierOp.getItems().clear();
                        while (rs.next())
                        {   
                            data.add(new TabPointage(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date"),      
                                    rs.getString("Libelle")      
                                    ));
                                    tabCaissierOp.setItems(data);
                        }
                        cnx.close();
                    }else if(selectedItem2.equals("Maintenance"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Maintenance'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
                    }else if(selectedItem2.equals("Sécurité"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND Libelle= 'Sécurité'"
                        + "GROUP BY NumContrat,Nom,Prenom,Date";
                        cnx=new ConnexionBDD().ConnexionDB();  
                        Statement st = cnx.createStatement();
                        ResultSet rs =st.executeQuery(sql);
                        tabMainSec.getItems().clear();
                        while (rs.next())
                        {
                            data_2.add(new TabPointage_2(                     
                                    rs.getString("NumContrat"),
                                    rs.getString("Prenom"),     
                                    rs.getString("Nom"),     
                                    rs.getString("Date")     
                                    ));
                                    tabMainSec.setItems(data_2);
                        }
                        cnx.close();
                    }
            }
    }

    @FXML
    private void cherchEmpRech(KeyEvent event) throws SQLException 
    {
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND Libelle='"+selectedItem2+"' AND (Nom LIKE '%"+choixEmpRech.getValue()+"%' OR Prenom LIKE '%"+choixEmpRech.getValue()+"%')";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixEmpRech.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            choixEmpRech.setVisibleRowCount(i);
            choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            choixEmpRech.show();
        }
        cnx.close();
    }

    @FXML
    private void cherchLieuRech(KeyEvent event) throws SQLException 
    {
        cnx=new ConnexionBDD().ConnexionDB();
        if (selectedItem2.equals("Opérateur"))
        {   
            String sql = "SELECT IdManége,Libelle FROM Manége "
                + "WHERE  Libelle LIKE '%"+choixLieuRech.getValue()+"%'";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixLieuRech.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                choixLieuRech.setVisibleRowCount(i);
                choixLieuRech.getItems().add(rs.getInt("IdManége")+"-"+rs.getString("Libelle"));
                choixLieuRech.show();
            }
        }else if(selectedItem2.equals("Caissier"))
        {
            String sql = "SELECT Libelle FROM Caisse "
                + "WHERE  Libelle LIKE '%"+choixLieuRech.getValue()+"%'";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixLieuRech.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                choixLieuRech.setVisibleRowCount(i);
                choixLieuRech.getItems().add(rs.getString("Libelle"));
                choixLieuRech.show();
            }
        }
            
            cnx.close();
    }
      public void act(int i)
  {
     Image im = new Image("/Icones/icons8-approbation-40.png"); 
     Image im1 = new Image("/Icones/icons8-haute-priorité-40.png");
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Pointage"); nt.text("Pointage effectué");}
      else if(i==2){nt.title("Pointage"); nt.text("Pointage supprimé");}
       else if(i==3){nt.graphic(new ImageView(im1));nt.title("Pointage"); nt.text("Ajout non effectué !");}
       else if(i==4){nt.graphic(new ImageView(im1));nt.title("Pointage"); nt.text("La Caisse est encore ouverte !");}
      nt.show();


  }
 
  public boolean confirmation(int i)
  {
      
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-approbation-40.png"); 
       alert.setGraphic(new ImageView(im));
      if(i==1){alert.setTitle("Pointage");alert.setContentText("Supprimer pointage ?");}
      else if(i==2){alert.setTitle("Pointage");alert.setContentText("Confirmer pointage ?");}
      Optional <ButtonType> action = alert.showAndWait();     
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }

    @FXML
    private void EmplCont(KeyEvent event) {
        if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }    
    }

    @FXML
    private void LieuCont(KeyEvent event) {
        if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }    
    }

    @FXML
    private void CherchEmpCont(KeyEvent event) {
        if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }    
    }

    @FXML
    private void ChercheLieuCont(KeyEvent event) {
         if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }   
    }
    
    public boolean verifieChampPointage()
    {
    boolean b=true;
    if((rbOperateur.isSelected()||rbCaissier.isSelected()) && click == 0)
    {
      if(choixEmpPointage.getSelectionModel().isEmpty())
      {
          b=false;
          choixEmpPointage.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      }
       if(choixLieuPointage.getSelectionModel().isEmpty())
      {
          b=false;
          choixLieuPointage.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      }        
    }
    else if((rbSécurité.isSelected()||rbMaintenance.isSelected()) && click == 0)
    {
          if(choixEmpPointage.getSelectionModel().isEmpty())
      {
          b=false;
          choixEmpPointage.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      }
    } 
    else if((rbOperateur.isSelected()||rbCaissier.isSelected()) && click == 1 && !choixEmpPointage.isDisable() )
    {
      
       if(choixLieuPointage.getSelectionModel().isEmpty())
      {
          System.out.print(click+" hna");
          b=false;
          choixLieuPointage.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      }        
    }
    else if((rbOperateur.isSelected()||rbCaissier.isSelected()) && click == 1 && !choixLieuPointage.isDisable() &&!choixEmpPointage.isDisable()  )
    {
     b=false;       
    }
    else  if(!rbSécurité.isSelected()&& !rbOperateur.isSelected() && !rbCaissier.isSelected() && !rbMaintenance.isSelected())
        {
           information(); 
           b= false;
        }
    return b;
    }
     public void information()
   {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       Image im = new Image("/Icones/icons8-information-40.png"); 
        alert.setGraphic(new ImageView(im));
       alert.setTitle("Pointage");alert.setContentText("Vérifier les champs ");
       Optional <ButtonType> action = alert.showAndWait();
       alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          alert.close();
      }   
   }
     
     public boolean validation(int i)
     {  boolean b=false;
         if(verifieChampPointage())
         {
             if(i==1)
             {if(confirmation(1))
             {
               b=true;  
             }}
             else if(i==2)
                 {if(confirmation(2))
             {
               b=true;  
             }}
             
             
         }
         return b;
     }
    
}
