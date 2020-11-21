package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;
import tableau.AdminPointage;


/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class PointageAdmin implements Initializable {
    Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
    ObservableList<AdminPointage> data = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox<String> ListeAgent;
    @FXML
    private JFXButton valider;
    @FXML
    private TableView<AdminPointage> AdminTable;
    @FXML
    private TableColumn<?, ?> NContrat;
    @FXML
    private TableColumn<?, ?> Prenom;
    @FXML
    private TableColumn<?, ?> Nom;
    @FXML
    private TableColumn<?, ?> Date;
    @FXML
    private JFXButton supp;
     AdminPointage AdPo;
     Date date;
    @FXML
    private JFXComboBox<String> choixEmpRech;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            NContrat.setCellValueFactory(new PropertyValueFactory<>("NContrat"));
            Prenom.setCellValueFactory(new PropertyValueFactory<>("Prénom"));
            Nom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
            Date.setCellValueFactory(new PropertyValueFactory<>("Date_de_travail"));
            loadDatabaseData();
            listerEmpRech();
            rempCombo(); 
        try {
            listerPers();
        } catch (SQLException ex) {
            Logger.getLogger(PointageAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
    }    
    
    public void loadDatabaseData()
	{
		String sql="select IdPersonnel,Date,Nom,Prenom from pointage,personnel,poste where IdPersonnel=NumContrat "
                        + "AND personnel.IdPoste=poste.IdPoste AND (Libelle!='Caissier' AND Libelle!='Opérateur' AND Libelle!='Maintenance' AND Libelle!='Sécurité')";
		
		try
		{
					
                    data.clear();

                    cnx=new ConnexionBDD().ConnexionDB();
                    ps = cnx.prepareStatement(sql);
                    rs=ps.executeQuery();

                    while(rs.next())
                    {
                            data.add(new AdminPointage(
                                            rs.getString("IdPersonnel"),
                                            rs.getString("Prenom"),
                                            rs.getString("Nom"),
                                            rs.getString("Date")
                                            ));
                            AdminTable.setItems(data);


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

    @FXML
    private void ShowEmp(MouseEvent event) throws SQLException {
        String dateNow=null;
        AdPo = (AdminPointage)AdminTable.getSelectionModel().getSelectedItem();
        ListeAgent.setDisable(true);
        ListeAgent.setValue(AdPo.getNContrat()+" "+AdPo.getNom()+" "+AdPo.getPrénom());
        valider.setDisable(true);
        String sql1="SELECT DATE(NOW()) AS dateNow";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql1); 
         while(rs.next())
                dateNow=rs.getString("dateNow");
         
         if(AdPo.Date_de_travail.equals(dateNow))
         {
           supp.setDisable(false);
         }
         else supp.setDisable(true);
         valider.setDisable(true);
    }
    
    private void listerEmpRech()
    {
        String sql = "SELECT NumContrat,Nom,Prenom FROM poste,personnel "
                + "WHERE poste.IdPoste=personnel.IdPoste AND (Libelle!='Caissier' AND Libelle!='Opérateur' AND Libelle!='Maintenance' AND Libelle!='Sécurité') "
                + "AND NumContrat NOT IN (SELECT IdPersonnel FROM pointage,journée WHERE pointage.Date=DATE(NOW()))";
        try
        {
            cnx= new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixEmpRech.getItems().clear();
            while (rs.next())
            {
                choixEmpRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
            }
            cnx.close();
        }catch(Exception e)
        {
            
        }
    }

    @FXML
    private void valide(ActionEvent event) {
        if(confirmation(2))
        {
        try {
            String numCon[] = new String[2];
            numCon=ListeAgent.getSelectionModel().getSelectedItem().split("-");
            String query = "SELECT MAX(DATE(Date)) AS dateAuj FROM journée";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next())
                date= rs.getDate("dateAuj");
            String sql = "INSERT INTO pointage(IdPersonnel,Date) VALUES (?,?)";
            ps = cnx.prepareStatement(sql);
            
            ps.setString(1,numCon[0]);
            ps.setDate(2, (java.sql.Date) date);
            ps.executeUpdate();
            cnx.close();
            act(1);
            actualiser();
        } catch (SQLException ex) {
            Logger.getLogger(PointageAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadDatabaseData();}
    }

    @FXML
    private void supprimer(ActionEvent event) throws SQLException {
        if(confirmation(1))
        {
        String sql ="delete from pointage where IdPersonnel='"+AdPo.getNContrat()+"' and Date='"+AdPo.Date_de_travail+"'";
        cnx=new ConnexionBDD().ConnexionDB(); 
        ps = cnx.prepareStatement(sql);
        ps.executeUpdate();
        act(2);
        cnx.close();
        valider.setDisable(false);
        supp.setDisable(true);
        ListeAgent.setDisable(false);
        ListeAgent.setValue(null);
        loadDatabaseData();
        actualiser();
    }}
    
    public void rempCombo() 
    {
        try {
            String sql ="select * from personnel where IdPoste in "
                    + "(select IdPoste from poste where Libelle !='Caissier' "
                    + "and Libelle !='Opérateur' and Libelle !='Maintenance' "
                    + "and Libelle !='Sécurité') "
                    + "and NumContrat not in "
                    + "(select IdPersonnel as NumContrat from pointage) ";
            cnx=new ConnexionBDD().ConnexionDB();
            ps = cnx.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next())
            {
                ListeAgent.getItems().add(rs.getString("NumContrat")+"  "+rs.getString("Nom")+" "+rs.getString("Prenom"));
                
            }
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(PointageAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
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
 
      nt.show();


  }
 
  public boolean confirmation(int i)
  {  boolean b = false;
      if(verifieChamp())
      {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-approbation-40.png"); 
      alert.setGraphic(new ImageView(im));
      if(i==1){alert.setTitle("Pointage");alert.setContentText("Supprimer poinage ?");}
      else if(i==2){alert.setTitle("Pointage");alert.setContentText("Ajouter pointage?");}
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          b=true;
      }
      }
      return b;
  }
  
  public boolean verifieChamp()
  { Paint value0=Paint.valueOf("F9C7CC");
      boolean b=true;
    if(ListeAgent.getSelectionModel().isEmpty() && !ListeAgent.isDisable())
    {
       ListeAgent.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
       b=false; 
    }
    return b;
  }
    
    private void listerPers() throws SQLException
    {
        String sql = "SELECT NumContrat,Nom,Prenom FROM poste,personnel "
                + "WHERE poste.IdPoste=personnel.IdPoste AND (Libelle!='Caissier' AND Libelle!='Opérateur' AND Libelle!='Maintenance' AND Libelle!='Sécurité') "
                + "AND NumContrat NOT IN (SELECT IdPersonnel FROM pointage,journée WHERE pointage.Date=DATE(NOW()))";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ListeAgent.getItems().clear();
        while (rs.next())
        {
            ListeAgent.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
        cnx.close();
    }

    @FXML
    private void buttActualiser(ActionEvent event) throws SQLException {
        actualiser();
    }
    
    private void actualiser() throws SQLException
    {
        ListeAgent.setDisable(false);
        ListeAgent.setValue(null);
        valider.setDisable(false);
        supp.setDisable(true);
        
        choixEmpRech.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        listerPers();
        listerEmpRech();
    }

    @FXML
    private void cherchEmpRech(KeyEvent event) {
    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException 
    {
        String tab[]=new String[2];
        String sql = null;
        if (choixEmpRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab=choixEmpRech.getValue().split("-");
            sql="SELECT * FROM pointage,personnel WHERE IdPersonnel='"+tab[0]+"' AND IdPersonnel=NumContrat";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            AdminTable.getItems().clear();
            while(rs.next())
            {
                data.add(new AdminPointage(
						rs.getString("IdPersonnel"),
						rs.getString("Prenom"),
						rs.getString("Nom"),
                                                rs.getString("Date")
						));
				AdminTable.setItems(data);
            }
        }else if (choixEmpRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixEmpRech.getValue().split("-");
            sql="SELECT * FROM pointage,personnel WHERE IdPersonnel='"+tab[0]+"' AND IdPersonnel=NumContrat "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            AdminTable.getItems().clear();
            while(rs.next())
            {
                data.add(new AdminPointage(
						rs.getString("IdPersonnel"),
						rs.getString("Prenom"),
						rs.getString("Nom"),
                                                rs.getString("Date")
						));
				AdminTable.setItems(data);
            }
        }else if (choixEmpRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null)
            {
                tab=choixEmpRech.getValue().split("-");
                sql="SELECT * FROM pointage,personnel WHERE IdPersonnel='"+tab[0]+"' AND IdPersonnel=NumContrat "
                        + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                        + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                AdminTable.getItems().clear();
                while(rs.next())
                {
                    data.add(new AdminPointage(
                                                    rs.getString("IdPersonnel"),
                                                    rs.getString("Prenom"),
                                                    rs.getString("Nom"),
                                                    rs.getString("Date")
                                                    ));
                                    AdminTable.setItems(data);
                }
            }else if (choixEmpRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null)
            {
                tab=choixEmpRech.getValue().split("-");
                sql="SELECT * FROM pointage,personnel WHERE IdPersonnel='"+tab[0]+"' AND IdPersonnel=NumContrat "
                        + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                AdminTable.getItems().clear();
                while(rs.next())
                {
                    data.add(new AdminPointage(
                                                    rs.getString("IdPersonnel"),
                                                    rs.getString("Prenom"),
                                                    rs.getString("Nom"),
                                                    rs.getString("Date")
                                                    ));
                                    AdminTable.setItems(data);
                }
            }else if (choixEmpRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null)
            {
                
                sql="SELECT * FROM pointage,personnel WHERE IdPersonnel=NumContrat "
                        + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                AdminTable.getItems().clear();
                while(rs.next())
                {
                    data.add(new AdminPointage(
                                                    rs.getString("IdPersonnel"),
                                                    rs.getString("Prenom"),
                                                    rs.getString("Nom"),
                                                    rs.getString("Date")
                                                    ));
                                    AdminTable.setItems(data);
                }
            }else if (choixEmpRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null)
            {
                
                sql="SELECT * FROM pointage,personnel WHERE IdPersonnel=NumContrat "
                        + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                AdminTable.getItems().clear();
                while(rs.next())
                {
                    data.add(new AdminPointage(
                                                    rs.getString("IdPersonnel"),
                                                    rs.getString("Prenom"),
                                                    rs.getString("Nom"),
                                                    rs.getString("Date")
                                                    ));
                                    AdminTable.setItems(data);
                }
            }else if (choixEmpRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null)
            {
                
                sql="SELECT * FROM pointage,personnel WHERE IdPersonnel=NumContrat "
                        + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                        + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                AdminTable.getItems().clear();
                while(rs.next())
                {
                    data.add(new AdminPointage(
                                                    rs.getString("IdPersonnel"),
                                                    rs.getString("Prenom"),
                                                    rs.getString("Nom"),
                                                    rs.getString("Date")
                                                    ));
                                    AdminTable.setItems(data);
                }
            }
    
    
    }
}