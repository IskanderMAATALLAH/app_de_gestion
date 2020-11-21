package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.io.IOException;
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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tableau.TabFacture;


public class InterDepenses implements Initializable {
    ObservableList<TabFacture> data = FXCollections.observableArrayList();    
    FilteredList<TabFacture> filtredData = new FilteredList<>(data,e->true);
    public static Stage stage_fact=null;
    public static Stage stage_avPaie=null;
    public static Stage stage_paie=null;

    @FXML
    private TableColumn<?, ?> nomF;
    @FXML
    private TableColumn<?, ?> nomP;
    @FXML
    private TableColumn<?, ?> date;
    @FXML
    private TableColumn<?, ?> prixUnit;
    @FXML
    private TableColumn<?, ?> qte;
    @FXML
    private TableColumn<?, ?> monTot;
    @FXML
    private TableColumn<?, ?> rem;
    @FXML
    private TableColumn<?, ?> montNet;
    @FXML
    private TableView<TabFacture> tabFacture;
    @FXML
    private TableColumn<?, ?> vers;
    @FXML
    private TableColumn<?, ?> montRest;
    @FXML
    private TableColumn<?, ?> autreCharge;
    public Connection cnx;
    @FXML
    private JFXComboBox<String> cbFourn;
    @FXML
    private JFXComboBox<String> cbProd;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    @FXML
    private JFXRadioButton payéRb;
    @FXML
    private ToggleGroup selectedRb;
    @FXML
    private JFXRadioButton nonPayéRb;
    TabFacture tf=null;
    @FXML
    private JFXButton Supp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nomF.setCellValueFactory(new PropertyValueFactory<>("Nom_Four"));
            nomP.setCellValueFactory(new PropertyValueFactory<>("Produit"));
            date.setCellValueFactory(new PropertyValueFactory<>("Date"));
            prixUnit.setCellValueFactory(new PropertyValueFactory<>("Prix_Unit"));
            qte.setCellValueFactory(new PropertyValueFactory<>("Qte"));
            monTot.setCellValueFactory(new PropertyValueFactory<>("Mont_Tot"));
            rem.setCellValueFactory(new PropertyValueFactory<>("Rem"));
            autreCharge.setCellValueFactory(new PropertyValueFactory<>("autreCharge"));
            montNet.setCellValueFactory(new PropertyValueFactory<>("Mont_Net"));
            vers.setCellValueFactory(new PropertyValueFactory<>("Vers"));
            montRest.setCellValueFactory(new PropertyValueFactory<>("Mont_Rest"));
            chargerFactures(data,tabFacture);
            new Facture().listerFournisseur(cbFourn);
            listerProduit();
        } catch (SQLException ex) {
            Logger.getLogger(InterDepenses.class.getName()).log(Level.SEVERE, null, ex);
        }
        Supp.setDisable(true);
    }    

    @FXML
    private void ouvrirAvancePaie(ActionEvent event) throws IOException {
        Supp.setDisable(true);
        stage_avPaie =new Stage();
        stage_avPaie.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/interAvancePaie.fxml"));
        Scene scene = new Scene(root);
        stage_avPaie.setScene(scene);
        stage_avPaie.show();
        
    }

    @FXML
    private void ouvrirNvlFact(ActionEvent event) throws IOException {
        Supp.setDisable(true);
        stage_fact =new Stage();
        stage_fact.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/facture.fxml"));
        Scene scene_fact = new Scene(root);
        stage_fact.setScene(scene_fact);
        stage_fact.show();
    }

    private void ouvrirModifFact(ActionEvent event) throws IOException {
        Supp.setDisable(true);
        Stage stage =new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/facture.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void chargerFactures(ObservableList<TabFacture> Data,TableView<TabFacture> Tab) throws SQLException
    {
        
        String sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit) "
                + "AND MONTH(Date)=MONTH(NOW())";
                
        Data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                Data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		Tab.setItems(Data);      
            }
            cnx.close();
    }
    
    private void listerProduit() throws SQLException
    {
        String sql ="SELECT IdProduit,Libelle FROM produit";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbProd.getItems().clear();
        while(rs.next())
        {
            cbProd.getItems().add(rs.getInt("IdProduit")+"-"+rs.getString("Libelle"));
        }
    }

    @FXML
    public void rechAction(ActionEvent event) throws SQLException 
    {
        String tab[] = new String[2];
        String tab2[] = new String[2];
        String  sql =null;
        Supp.setDisable(true);
        if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
        data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
           
            
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2 = cbProd.getValue().split("-");
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' AND Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date<= '"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
           
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab=cbFourn.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab=cbFourn.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab=cbFourn.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdFournisseur='"+tab[0]+"' "
                    + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab2=cbProd.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab2=cbProd.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()==null && cbProd.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && payéRb.isSelected() && !nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }else if (cbFourn.getValue()!=null && cbProd.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null && !payéRb.isSelected() && nonPayéRb.isSelected())
        {
            tab = cbFourn.getValue().split("-");
            tab2 = cbProd.getValue().split("-");
            sql="SELECT traceachat.IdFournisseur,NomFournisseur,traceachat.IdProduit,Libelle,Date,prixUnit,Remise,Autre,MontantVerse,traceachat.Qte, "
                + "prixUnit*traceachat.Qte AS montTot, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*(Remise/100) AS montAcRem, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre AS montantNet, "
                + "prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse AS montantRest "
                + "FROM traceachat,fournisseur,produit "
                    + "WHERE traceachat.IdFournisseur='"+tab[0]+"' AND traceachat.IdProduit='"+tab2[0]+"' "
                    + "AND prixUnit*traceachat.Qte-prixUnit*traceachat.Qte*Remise/100+Autre-MontantVerse!=0 "
                    + "AND (traceachat.IdFournisseur=fournisseur.IdFournisseur) AND (traceachat.IdProduit=produit.IdProduit)";
                
            data.clear();
            cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
			
            while(rs.next())
            {   
                
                data.add(new TabFacture(                      
                        rs.getString("IdFournisseur")+"|"+rs.getString("NomFournisseur"), 
                        rs.getString("IdProduit")+"|"+rs.getString("Libelle"),
                        rs.getString("Date"),
                        rs.getInt("prixUnit"),
                        rs.getInt("Qte"),
                        rs.getInt("montTot"),
                        rs.getFloat("Remise"),
                        rs.getInt("Autre"),
                        rs.getFloat("montantNet"),
                        rs.getInt("MontantVerse"),
                        rs.getInt("MontantRest")
		));  
                
		tabFacture.setItems(data);      
            }
            cnx.close();
        }
    }
    
    public void actualiser() throws SQLException
    {
        cbFourn.setValue(null);
        cbProd.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        payéRb.setSelected(false);
        nonPayéRb.setSelected(false);
        Supp.setDisable(true);
        chargerFactures(data,tabFacture);
        new Facture().listerFournisseur(cbFourn);
        listerProduit();
    }

    @FXML
    private void actualiserAction(ActionEvent event) throws SQLException {
        actualiser();
    }

    @FXML
    private void eventCherchFourn(KeyEvent event) throws SQLException {
        if(cbFourn.getValue().equals(""))
        {
            cbFourn.setValue(null);
            listerProduit();
        }
        else
        {String sql = "SELECT IdFournisseur,NomFournisseur FROM fournisseur "
                + "WHERE  NomFournisseur LIKE '%"+cbFourn.getValue()+"%'";
        cnx = new ConnexionBDD().ConnexionDB(); 
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbFourn.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            cbFourn.setVisibleRowCount(i);
            cbFourn.getItems().add(rs.getInt("IdFournisseur")+"|"+rs.getString("NomFournisseur"));
            cbFourn.show();
        }
        cnx.close();}
    }

    @FXML
    private void eventCherchProd(KeyEvent event) throws SQLException 
    {
        if(cbProd.getValue().equals(""))
        {
            cbProd.setValue(null);
            listerProduit();
        }
        else
        {String sql = "SELECT IdProduit,Libelle FROM produit "
                + "WHERE  Libelle LIKE '%"+cbProd.getValue()+"%'";
        cnx = new ConnexionBDD().ConnexionDB(); 
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbProd.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            cbProd.setVisibleRowCount(i);
            cbProd.getItems().add(rs.getInt("IdProduit")+"|"+rs.getString("Libelle"));
            cbProd.show();
        }
        cnx.close();}
    }

    @FXML
    private void supprimerFact(ActionEvent event) 
    {
        String tab[]=new String[2];
        String tab2[]=new String[2];
        if(confirmation())
        try
        {   
            tab=tf.getNom_Four().split("|");
            tab2=tf.getProduit().split("|");
            String sql = "DELETE FROM traceachat WHERE IdFournisseur='"+tab[0]+"' AND IdProduit='"+tab2[0]+"' AND Date='"+tf.getDate()+"'";
            cnx = new ConnexionBDD().ConnexionDB(); 
            Statement st = cnx.createStatement();
            st.executeUpdate(sql);
            actualiser();
            Supp.setDisable(true);
            act();    
        }catch(Exception e)
        {
            
        }
    }

    @FXML
    private void ouvrirPaiement(ActionEvent event) throws IOException {
        stage_paie =new Stage();
        stage_paie.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/paiement.fxml"));
        Scene scene = new Scene(root);
        stage_paie.setScene(scene);
        stage_paie.show();
    }
     public void act()
  {
      
     Image im = new Image("/Icones/icons8-approbation-40.png");   
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER); 
     nt.title("Facture"); nt.text(" suppression terminée"); 
      nt.show();
  }
   
   public boolean confirmation()
  {
      Image im1 = new Image("/Icones/icons8-effacer-40.png");
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setHeaderText(null);
          alert.setGraphic(new ImageView(im1));
          alert.setTitle("Facture");
          alert.setContentText("Supprimer facture ?");
          Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
  }

    @FXML
    private void selectRow(MouseEvent event) {
      tf=tabFacture.getSelectionModel().getSelectedItem();
      Supp.setDisable(false);
    }
   
    
} 

    
    

