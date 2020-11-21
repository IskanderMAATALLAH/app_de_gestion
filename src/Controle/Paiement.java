package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tableau.TabPaiement;



public class Paiement implements Initializable {

    @FXML
    private TableView<TabPaiement> tabPaiement;
    @FXML
    private TableColumn<?, ?> numContrat;
    @FXML
    private TableColumn<?, ?> firstName;
    @FXML
    private TableColumn<?, ?> name;
    @FXML
    private TableColumn<?, ?> nbJr;
    @FXML
    private TableColumn<?, ?> avancePaie;
    ObservableList<TabPaiement> data = FXCollections.observableArrayList();
    FilteredList<TabPaiement> filtredData = new FilteredList<>(data,e->true);
    Connection cnx = null;
    @FXML
    private Label moisAnnée;
    @FXML
    private JFXComboBox<String> cbMoisAnnée;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numContrat.setCellValueFactory(new PropertyValueFactory<>("NContrat"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("Prénom"));
        name.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        nbJr.setCellValueFactory(new PropertyValueFactory<>("nbrJour"));
        avancePaie.setCellValueFactory(new PropertyValueFactory<>("avPaie"));
        
        try {
            
            trMoisAnnée();
            calculNbJr();
            calculNbJrCaissier();
            calculNbJrOpérateur();
            ajoutMoisAnnée();
        } catch (SQLException ex) {
            Logger.getLogger(Paiement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void trMoisAnnée() throws SQLException
    {
        String sql ="SELECT MONTH(DATE(NOW())) AS mois , YEAR(DATE(NOW())) AS année";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
            moisAnnée.setText(rs.getString("mois")+"/"+rs.getString("Année"));
        }
        cnx.close();
    }
    private void calculNbJr() throws SQLException
    {
        /*String sql = "SELECT IdPersonnel,COUNT(IdPersonnel) AS nbrJr,Nom,Prenom FROM pointage,personnel "
                + "WHERE MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW())) AND IdPersonnel=NumContrat "
                + "GROUP BY IdPersonnel";*/
        String sql = "SELECT IdPersonnel,COUNT(IdPersonnel) AS nbrJr,Nom,Prenom FROM pointage,personnel "
                + "WHERE IdPersonnel NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW()))) AND MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW())) AND IdPersonnel=NumContrat "
                + "GROUP BY IdPersonnel";
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("IdPersonnel"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql = "SELECT pointage.IdPersonnel,COUNT(pointage.IdPersonnel) AS nbrJr,Nom,Prenom,Montant FROM pointage,personnel,avancepaie "
                + "WHERE pointage.IdPersonnel=avancepaie.idPersonnel AND MONTH(avancepaie.Date)=MONTH(DATE(NOW())) AND YEAR(avancepaie.Date)=YEAR(DATE(NOW())) AND MONTH(pointage.Date)=MONTH(DATE(NOW())) AND YEAR(pointage.Date)=YEAR(DATE(NOW())) "
                + "AND pointage.IdPersonnel=personnel.NumContrat "
                + "GROUP BY pointage.IdPersonnel";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("IdPersonnel"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
    }
    
    private void calculNbJrCaissier() throws SQLException
    {
        /*String sql = "SELECT Caissier,COUNT(Caissier) AS nbrJr,Nom,Prenom "
                + "FROM (SELECT Caissier FROM gérecaisse WHERE MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW())) AND Exist='0' GROUP BY Caissier,Date) AS subQuery,personnel "
                + "WHERE Caissier=NumContrat "
                + "GROUP BY Caissier ";*/
        
        String sql="SELECT personnel.NumContrat,COUNT(subQuery.Caissier) AS nbrJr,Nom,Prenom \n" +
        "FROM (SELECT Caissier FROM gérecaisse WHERE (MONTH(gérecaisse.Date)=MONTH(DATE(NOW())) AND YEAR(gérecaisse.Date)=YEAR(DATE(NOW()))) AND Exist='0' GROUP BY Caissier,Date) AS subQuery,personnel \n" +
        "WHERE personnel.NumContrat NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(avancepaie.Date)=MONTH(DATE(NOW())) \n" +
        "AND YEAR(avancepaie.Date)=YEAR(DATE(NOW()))) AND (subQuery.Caissier=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
                
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql="SELECT personnel.NumContrat,COUNT(subQuery.Caissier) AS nbrJr,Nom,Prenom,Montant \n" +
        "FROM (SELECT Caissier,Montant FROM gérecaisse,avancepaie WHERE (MONTH(gérecaisse.Date)=MONTH(DATE(NOW())) AND YEAR(gérecaisse.Date)=YEAR(DATE(NOW()))"
                + "AND MONTH(avancepaie.Date)=MONTH(DATE(NOW())) AND YEAR(avancepaie.Date)=YEAR(DATE(NOW())) AND Caissier=idPersonnel) "
                + "AND Exist='0' GROUP BY Caissier,gérecaisse.Date) AS subQuery,personnel \n" +
        "WHERE (subQuery.Caissier=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
    }
    
    private void calculNbJrOpérateur() throws SQLException
    {   
        
        String sql="SELECT personnel.NumContrat,COUNT(subQuery.NumContrat) AS nbrJr,Nom,Prenom \n" +
        "FROM (SELECT NumContrat FROM géremanége WHERE (MONTH(géremanége.Date)=MONTH(DATE(NOW())) AND YEAR(géremanége.Date)=YEAR(DATE(NOW())))) AS subQuery,personnel \n" +
        "WHERE personnel.NumContrat NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(avancepaie.Date)=MONTH(DATE(NOW())) \n" +
        "AND YEAR(avancepaie.Date)=YEAR(DATE(NOW()))) AND (subQuery.NumContrat=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            
            data.add(new TabPaiement(                      
		rs.getString("NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql="SELECT personnel.NumContrat,COUNT(subQuery.NumContrat) AS nbrJr,Nom,Prenom,Montant \n" +
        "FROM (SELECT NumContrat,Montant FROM géremanége,avancepaie WHERE (MONTH(géremanége.Date)=MONTH(DATE(NOW())) AND YEAR(géremanége.Date)=YEAR(DATE(NOW()))"
                + "AND MONTH(avancepaie.Date)=MONTH(DATE(NOW())) AND YEAR(avancepaie.Date)=YEAR(DATE(NOW())) AND NumContrat=idPersonnel) "
                + ") AS subQuery,personnel \n" +
        "WHERE (subQuery.NumContrat=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
    }
    
    private void ajoutMoisAnnée() throws SQLException
    {
        String sql="SELECT DISTINCT MONTH(Date) AS mois,YEAR(Date) AS année FROM journée WHERE YEAR(Date)>=YEAR(DATE(NOW()))-1 ";
        cnx= new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbMoisAnnée.getItems().clear();
        while (rs.next())
        {
            cbMoisAnnée.getItems().add(rs.getString("mois")+"-"+rs.getString("année"));
        }
    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException 
    {
        String tabMoisAnnée[]=new String [2];
        tabMoisAnnée=cbMoisAnnée.getValue().split("-");
        tabPaiement.getItems().clear();
        moisAnnée.setText(tabMoisAnnée[0]+"/"+tabMoisAnnée[1]);
        String sql = "SELECT IdPersonnel,COUNT(IdPersonnel) AS nbrJr,Nom,Prenom FROM pointage,personnel "
                + "WHERE IdPersonnel NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(Date)='"+tabMoisAnnée[0]+"' AND YEAR(Date)='"+tabMoisAnnée[1]+"') AND IdPersonnel=NumContrat "
                + "GROUP BY IdPersonnel";
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("IdPersonnel"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql = "SELECT pointage.IdPersonnel,COUNT(pointage.IdPersonnel) AS nbrJr,Nom,Prenom,Montant FROM pointage,personnel,avancepaie "
                + "WHERE pointage.IdPersonnel=avancepaie.idPersonnel AND MONTH(pointage.Date)='"+tabMoisAnnée[0]+"' AND YEAR(pointage.Date)='"+tabMoisAnnée[1]+"' AND MONTH(avancepaie.Date)='"+tabMoisAnnée[0]+"' AND YEAR(avancepaie.Date)='"+tabMoisAnnée[1]+"' "
                + "AND pointage.IdPersonnel=personnel.NumContrat "
                + "GROUP BY pointage.IdPersonnel";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("IdPersonnel"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
        
        sql="SELECT personnel.NumContrat,COUNT(subQuery.Caissier) AS nbrJr,Nom,Prenom \n" +
        "FROM (SELECT Caissier FROM gérecaisse WHERE MONTH(gérecaisse.Date)='"+tabMoisAnnée[0]+"' AND YEAR(gérecaisse.Date)='"+tabMoisAnnée[1]+"' AND Exist='0' GROUP BY Caissier,Date) AS subQuery,personnel \n" +
        "WHERE personnel.NumContrat NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(avancepaie.Date)='"+tabMoisAnnée[0]+"' \n" +
        "AND YEAR(avancepaie.Date)='"+tabMoisAnnée[1]+"') AND (subQuery.Caissier=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
                
        cnx = new ConnexionBDD().ConnexionDB();
        st = cnx.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql="SELECT personnel.NumContrat,COUNT(subQuery.Caissier) AS nbrJr,Nom,Prenom,Montant \n" +
        "FROM (SELECT Caissier,Montant FROM gérecaisse,avancepaie WHERE (MONTH(gérecaisse.Date)='"+tabMoisAnnée[0]+"' AND YEAR(gérecaisse.Date)='"+tabMoisAnnée[1]+"'"
                + "AND MONTH(avancepaie.Date)='"+tabMoisAnnée[0]+"' AND YEAR(avancepaie.Date)='"+tabMoisAnnée[1]+"' AND Caissier=idPersonnel) "
                + "AND Exist='0' GROUP BY Caissier,gérecaisse.Date) AS subQuery,personnel \n" +
        "WHERE (subQuery.Caissier=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
        
        sql="SELECT personnel.NumContrat,COUNT(subQuery.NumContrat) AS nbrJr,Nom,Prenom \n" +
        "FROM (SELECT NumContrat FROM géremanége WHERE MONTH(géremanége.Date)='"+tabMoisAnnée[0]+"' AND YEAR(géremanége.Date)='"+tabMoisAnnée[1]+"') AS subQuery,personnel \n" +
        "WHERE personnel.NumContrat NOT IN (SELECT idPersonnel FROM avancepaie WHERE MONTH(avancepaie.Date)='"+tabMoisAnnée[0]+"' \n" +
        "AND YEAR(avancepaie.Date)='"+tabMoisAnnée[1]+"' ) AND (subQuery.NumContrat=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        cnx = new ConnexionBDD().ConnexionDB();
        st = cnx.createStatement();
        rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            
            data.add(new TabPaiement(                      
		rs.getString("NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                0       
                ));                       
		tabPaiement.setItems(data);
        }
        sql="SELECT personnel.NumContrat,COUNT(subQuery.NumContrat) AS nbrJr,Nom,Prenom,Montant \n" +
        "FROM (SELECT NumContrat,Montant FROM géremanége,avancepaie WHERE (MONTH(géremanége.Date)='"+tabMoisAnnée[0]+"') AND YEAR(géremanége.Date)='"+tabMoisAnnée[1]+"'"
                + "AND MONTH(avancepaie.Date)='"+tabMoisAnnée[0]+"' AND YEAR(avancepaie.Date)='"+tabMoisAnnée[1]+"' AND NumContrat=idPersonnel "
                + ") AS subQuery,personnel \n" +
        "WHERE (subQuery.NumContrat=personnel.NumContrat)\n" +
        "GROUP BY personnel.NumContrat ";
        rs = st.executeQuery(sql);
        while (rs.next())
        {
            data.add(new TabPaiement(                      
		rs.getString("personnel.NumContrat"),           
		rs.getString("Nom"),       
		rs.getString("Prenom"),      
                rs.getInt("nbrJr"),       
                rs.getInt("Montant")
                ));                       
		tabPaiement.setItems(data);
        }
        cnx.close();
    }

    @FXML
    private void actualiserAction(ActionEvent event) {
        try {
            tabPaiement.getItems().clear();
            trMoisAnnée();
            calculNbJr();
            calculNbJrCaissier();
            calculNbJrOpérateur();
            ajoutMoisAnnée();
            cbMoisAnnée.setValue(null);
        } catch (SQLException ex) {
            Logger.getLogger(Paiement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void openAfterClose(ActionEvent event) throws IOException
     {
      Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      stage.close();  
     }  

    @FXML
    private void fermer(MouseEvent event) 
    {
        InterDepenses.stage_paie.close();
    }
    
}
