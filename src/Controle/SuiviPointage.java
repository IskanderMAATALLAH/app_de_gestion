package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import tableau.TabPointage;
import tableau.TabPointage_2;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class SuiviPointage implements Initializable {
  Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
    private String selectedItem1 = null;
    ToggleGroup tg1 = null;
    ObservableList<TabPointage> data = FXCollections.observableArrayList();
    ObservableList<TabPointage_2> data_2 = FXCollections.observableArrayList();    
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
    private TableView<TabPointage_2> tabMainSec;
    @FXML
    private TableColumn<?, ?> NContrat_2;
    @FXML
    private TableColumn<?, ?> Prénom_2;
    @FXML
    private TableColumn<?, ?> Nom_2;
    @FXML
    private TableColumn<?, ?> Date_de_travail_2;
    private JFXRadioButton rbMaintenance;
    private JFXRadioButton rbCaissier;
    private JFXRadioButton rbSécurité;
    private JFXRadioButton rbOperateur;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
    @FXML
    private JFXRadioButton rbMaintRech;
    @FXML
    private JFXRadioButton rbCaissierRech;
    @FXML
    private JFXRadioButton rbSécRech;
    @FXML
    private JFXRadioButton rbRechOp;
    @FXML
    private JFXComboBox<String> choixEmpRech;
    @FXML
    private JFXComboBox<String> choixLieuRech;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    String selectedItem2=null;
    @FXML
    private JFXRadioButton admin;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      ToggleGroup tg1 = new ToggleGroup();
        
        rbSécRech.setToggleGroup(tg1);
        rbRechOp.setToggleGroup(tg1);
        rbMaintRech.setToggleGroup(tg1);
        rbCaissierRech.setToggleGroup(tg1);
        admin.setToggleGroup(tg1);
        
        NContrat.setCellValueFactory(new PropertyValueFactory<>("NContrat"));
        Prénom.setCellValueFactory(new PropertyValueFactory<>("Prénom"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        DateTravaill.setCellValueFactory(new PropertyValueFactory<>("Date_de_travail"));
        LieuTravail.setCellValueFactory(new PropertyValueFactory<>("Lieu_de_travail"));
        
        NContrat_2.setCellValueFactory(new PropertyValueFactory<>("NContrat_2"));
        Prénom_2.setCellValueFactory(new PropertyValueFactory<>("Prénom_2"));
        Nom_2.setCellValueFactory(new PropertyValueFactory<>("Nom_2"));
        Date_de_travail_2.setCellValueFactory(new PropertyValueFactory<>("Date_de_travail_2"));
        loadDatabaseData();
      try {
          loadDBData();
          listerPersonnelRech();
      } catch (SQLException ex) {
          Logger.getLogger(SuiviPointage.class.getName()).log(Level.SEVERE, null, ex);
      }
    }    
     public void loadDatabaseData()
    {
	String sql="select personnel.NumContrat,Nom,Prenom,Date,Libelle from manége ,personnel ,géremanége where (SELECT MONTH(Date))=(SELECT MONTH(DATE(NOW()))) AND géremanége.IdManége = manége.IdManége and personnel.NumContrat=géremanége.NumContrat";
	String sql1="select personnel.NumContrat,Nom,Prenom,Date,Libelle from caisse ,personnel ,gérecaisse where (SELECT MONTH(Date))=(SELECT MONTH(DATE(NOW()))) AND gérecaisse.IdCaisse= caisse.IdCaisse and personnel.NumContrat=gérecaisse.Caissier group by Caissier,Date"; 
        
        try
        {
            data.clear();
            cnx=new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {
                data.add(new TabPointage(                     
		rs.getString("personnel.NumContrat"),         
		rs.getString("Nom"),      
		rs.getString("Prenom"),    
                rs.getString("Date"),       
                rs.getString("Libelle")       
                ));                       
		tabCaissierOp.setItems(data);      
            }
                        
            PreparedStatement ps1 = cnx.prepareStatement(sql1);
            ResultSet rs1=ps1.executeQuery();
            while(rs1.next())
            {
                data.add(new TabPointage(                     
                    rs1.getString("personnel.NumContrat"),           
                    rs1.getString("Nom"),     
                    rs1.getString("Prenom"),     
                    rs1.getString("Date"),       
                    rs1.getString("Libelle")      
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
       cnx=new ConnexionBDD().ConnexionDB();
        PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                data_2.add(new TabPointage_2(                        
                    rs.getString("NumContrat"),               
                    rs.getString("Nom"),                       
                    rs.getString("Prenom"),                    
                    rs.getString("Date")                  
                                                             
                ));                        
		tabMainSec.setItems(data_2);                        
            }
            cnx.close();
        
    }

    @FXML
    private void buttActualiser(ActionEvent event) throws SQLException, SQLException, SQLException {
        rbMaintRech.setSelected(false);
        rbSécRech.setSelected(false);
        rbRechOp.setSelected(false);
        rbCaissierRech.setSelected(false);
        admin.setSelected(false);
        
        choixLieuRech.setValue(null);
        choixEmpRech.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        
        loadDatabaseData();
        loadDBData();
    }

    @FXML
    private void rechMaintRB(ActionEvent event) {
        selectedItem2="Maintenance";
        choixLieuRech.setValue(null);
        choixLieuRech.setDisable(true);
    }

    @FXML
    private void rechCaissierRB(ActionEvent event) throws SQLException {
        selectedItem2="Caissier";
        listerLieux("caisse");
        choixLieuRech.setValue(null);
        choixLieuRech.setDisable(false);
    }

    @FXML
    private void rechSécRB(ActionEvent event) {
        selectedItem2="Sécurité";
        choixLieuRech.setValue(null);
        choixLieuRech.setDisable(true);
    }

    @FXML
    private void rechOpérateurRB(ActionEvent event) throws SQLException {
        selectedItem2="Opérateur";
        listerLieux("Manége");
        choixLieuRech.setValue(null);
        choixLieuRech.setDisable(false);
    }
    
    @FXML
    private void rechAdminRB(ActionEvent event) {
        selectedItem2="Administratif";
        choixLieuRech.setValue(null);
        choixLieuRech.setDisable(true);
    }

    @FXML
    private void cherchEmpRech(KeyEvent event) throws SQLException {
        
        String sql = "SELECT NumContrat, Nom, Prenom "
                + "FROM personnel,poste "
                + "WHERE (personnel.IdPoste=poste.IdPoste) AND (Nom LIKE '%"+choixEmpRech.getValue()+"%' OR Prenom LIKE '%"+choixEmpRech.getValue()+"%')";
        String itemEmp=null;
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int i=0;
        choixEmpRech.getItems().clear();
        while (rs.next())
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
        try
        {
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
                choixLieuRech.getItems().add(rs.getInt("IdManége")+"-"+rs.getString("Libelle")+"  ("+new Pointage().nbrOperateur(rs.getString("Libelle"))+")");
                choixLieuRech.show();
            }
        }else if(selectedItem2.equals("Caissier"))
        {
            String sql = "SELECT IdCaisse,Libelle FROM Caisse "
                + "WHERE  Libelle LIKE '%"+choixLieuRech.getValue()+"%'";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixLieuRech.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                choixLieuRech.setVisibleRowCount(i);
                choixLieuRech.getItems().add(rs.getInt("IdCaisse")+"-"+rs.getString("Libelle"));
                choixLieuRech.show();
            }
        }
        cnx.close();
        }catch(Exception ex)
        {
            
        }
    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException {
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
                    }else if(selectedItem2.equals("Administratif"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel= '"+tab[0]+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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
            }else if(selectedItem2.equals("Administratif"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité')"
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
        }else if (selectedItem2.equals("Administratif"))
        {
            sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE IdPersonnel='"+tab[0]+"' AND "
                        + "Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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
            }else if(selectedItem2.equals("Administratif"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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
            }else if (selectedItem2.equals("Administratif"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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
            }else if(selectedItem2.equals("Administratif"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel='"+tab[0]+"'"
                        + "AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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
            }else if (selectedItem2.equals("Administratif"))
            {
                sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité')"
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
                    }else if(selectedItem2.equals("Administratif"))
                    {
                        sql = "SELECT personnel.NumContrat,Nom,Prenom,pointage.Date "
                        + "FROM personnel,pointage,poste "
                        + "WHERE pointage.IdPersonnel=personnel.NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!= 'Caissier' AND Libelle!= 'Opérateur' AND Libelle!= 'Maintenance' AND Libelle!= 'Sécurité') "
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

   private void listerPersonnelRech() throws SQLException
    {
        String sql = "SELECT NumContrat, Nom, Prenom FROM personnel,poste WHERE (personnel.IdPoste=poste.IdPoste)";
        String itemEmp=null;
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            itemEmp=rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom");
            choixEmpRech.getItems().add(itemEmp);
        }
        cnx.close();
    }  
 
   private void listerLieux(String lieu) throws SQLException
    {
        String sql =null;
        if(lieu.equals("Manége"))
        {
            sql = "SELECT IdManége,Libelle FROM "+lieu+" WHERE Etat='1' ";
        
            choixLieuRech.setDisable(false);
            choixLieuRech.getItems().clear();
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                choixLieuRech.getItems().add(rs.getString("IdManége")+"-"+rs.getString("Libelle"));
            }
            cnx.close();
        }
        else
        {   
            sql = "SELECT caisse.IdCaisse,Libelle FROM caisse WHERE IdCaisse NOT IN (SELECT gérecaisse.IdCaisse FROM gérecaisse,caisse WHERE caisse.IdCaisse=gérecaisse.IdCaisse AND Date=DATE(NOW()) AND Exist='0')";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            rs=st.executeQuery(sql);
            while (rs.next())
            {   
                choixLieuRech.getItems().add(rs.getInt("IdCaisse")+"-"+rs.getString("Libelle"));
            }
            cnx.close();
        }
        

    }
    
}
