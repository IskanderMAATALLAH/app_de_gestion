/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import tableau.TabAvPaie;
import tableau.TabFacture;
import tableau.TabRecette;

/**
 * FXML Controller class
 *
 * @author user
 */
public class DepensesRevenusDG implements Initializable {
    ObservableList<TabFacture> data = FXCollections.observableArrayList();    
    FilteredList<TabFacture> filtredData = new FilteredList<>(data,e->true);
    @FXML
    private TableView<TabFacture> tabFacture;
    @FXML
    private TableColumn<?, ?> nomF;
    @FXML
    private TableColumn<?, ?> nomP;
    @FXML
    private TableColumn<?, ?> date_fact;
    @FXML
    private TableColumn<?, ?> prixUnit;
    @FXML
    private TableColumn<?, ?> qte;
    @FXML
    private TableColumn<?, ?> monTot;
    @FXML
    private TableColumn<?, ?> rem;
    @FXML
    private TableColumn<?, ?> autreCharge;
    @FXML
    private TableColumn<?, ?> montNet;
    @FXML
    private TableColumn<?, ?> vers;
    @FXML
    private TableColumn<?, ?> montRest;
    
    ObservableList<TabRecette> data_Rev = FXCollections.observableArrayList();    
    FilteredList<TabRecette> filtredData_Rev = new FilteredList<>(data_Rev,e->true);
    @FXML
    private TableView<TabRecette> tabRev;
    @FXML
    private TableColumn<?, ?> dateRev;
    @FXML
    private TableColumn<?, ?> recette;
    Connection cnx = null;
    @FXML
    private TableView<TabAvPaie> tabAvPaie;
    @FXML
    private TableColumn<?, ?> numContrat;
    @FXML
    private TableColumn<?, ?> prenom;
    @FXML
    private TableColumn<?, ?> nom;
    @FXML
    private TableColumn<?, ?> date;
    @FXML
    private TableColumn<?, ?> mont;
    ObservableList<TabAvPaie> data_avPaie = FXCollections.observableArrayList();    
    FilteredList<TabAvPaie> filtredData_AvPaie = new FilteredList<>(data_avPaie,e->true);
    @FXML
    private JFXComboBox<?> cbFourn;
    @FXML
    private JFXComboBox<?> cbProd;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    @FXML
    private JFXRadioButton payéRb;
    @FXML
    private ToggleGroup selectedItem;
    @FXML
    private JFXRadioButton nonPayéRb;
    @FXML
    private JFXDatePicker dateDuRev;
    @FXML
    private JFXDatePicker dateAuRev;
    /**
     * Initializes the controller class.
     */    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {nomF.setCellValueFactory(new PropertyValueFactory<>("Nom_Four"));
            nomP.setCellValueFactory(new PropertyValueFactory<>("Produit"));
            date_fact.setCellValueFactory(new PropertyValueFactory<>("Date"));
            prixUnit.setCellValueFactory(new PropertyValueFactory<>("Prix_Unit"));
            qte.setCellValueFactory(new PropertyValueFactory<>("Qte"));
            monTot.setCellValueFactory(new PropertyValueFactory<>("Mont_Tot"));
            rem.setCellValueFactory(new PropertyValueFactory<>("Rem"));
            autreCharge.setCellValueFactory(new PropertyValueFactory<>("autreCharge"));
            montNet.setCellValueFactory(new PropertyValueFactory<>("Mont_Net"));
            vers.setCellValueFactory(new PropertyValueFactory<>("Vers"));
            montRest.setCellValueFactory(new PropertyValueFactory<>("Mont_Rest"));
            new InterDepenses().chargerFactures(data, tabFacture);
            
            dateRev.setCellValueFactory(new PropertyValueFactory<>("dateRev"));
            recette.setCellValueFactory(new PropertyValueFactory<>("recette"));
            chargerDonnéesRev();
            
            numContrat.setCellValueFactory(new PropertyValueFactory<>("numeroContrat"));
            prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            mont.setCellValueFactory(new PropertyValueFactory<>("montant"));
            
            new InterAvancePaie().chargerDonnées(data_avPaie,tabAvPaie);
            
        } catch (SQLException ex) {
            Logger.getLogger(DepensesRevenusDG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void chargerDonnéesRev() throws SQLException
    {
        String sql = "SELECT Date,SUM(Recette) AS somme "
                + "FROM(SELECT Date,IdCaisse,Recette FROM parcdattractions.gérecaisse "
                + "WHERE MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW())) "
                + "GROUP BY Caissier,Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        tabRev.getItems().clear();
        while(rs.next())
        {
            data_Rev.add(new TabRecette(rs.getString("Date"),
                        rs.getInt("somme")
            ));
            tabRev.setItems(data_Rev); 
        }
        
        cnx.close();
    }

    @FXML
    private void eventListerFourn(KeyEvent event) {
    }

    @FXML
    private void eventListerProd(KeyEvent event) {
    }

    @FXML
    private void rechFacAction(ActionEvent event) {
    }

    @FXML
    private void actualiserRechFact(ActionEvent event) {
    }

    @FXML
    private void rechRevAction(ActionEvent event) {
    }

    @FXML
    private void actualiserRechRev(ActionEvent event) {
    }
    
}
