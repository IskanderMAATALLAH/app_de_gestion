package Controle;


import Main.ConnexionBDD;
import Main.MainProjet;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tableau.TabAvPaie;
import tableau.TabFacture;
import tableau.TabRecette;



public class Comptable implements Initializable {

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
    @FXML
    private TableView<TabAvPaie> tabAvPaie;
    ObservableList<TabAvPaie> data_avPaie = FXCollections.observableArrayList();    
    FilteredList<TabAvPaie> filtredData = new FilteredList<>(data_avPaie,e->true);
    public static Stage stage_graphique =null;
    
    @FXML
    private TableView<TabRecette> tabRev;
    @FXML
    private TableColumn<?, ?> dateRev;
    @FXML
    private TableColumn<?, ?> recette;
    ObservableList<TabRecette> data_Rev = FXCollections.observableArrayList();    
    FilteredList<TabRecette> filtredData_Rev = new FilteredList<>(data_Rev,e->true);
    
    
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
    private TableColumn<?, ?> montNet;
    @FXML
    private TableView<TabFacture> tabFacture;
    @FXML
    private TableColumn<?, ?> vers;
    @FXML
    private TableColumn<?, ?> montRest;
    @FXML
    private TableColumn<?, ?> autreCharge;
    ObservableList<TabFacture> data = FXCollections.observableArrayList();    
    FilteredList<TabFacture> filtredData_fact = new FilteredList<>(data,e->true);
    
    
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
    private ToggleGroup selectedItem;
    @FXML
    private JFXRadioButton nonPayéRb;
    @FXML
    private JFXDatePicker dateDuRev;
    @FXML
    private JFXDatePicker dateAuRev;
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        numContrat.setCellValueFactory(new PropertyValueFactory<>("numeroContrat"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        mont.setCellValueFactory(new PropertyValueFactory<>("montant")); 
        
        dateRev.setCellValueFactory(new PropertyValueFactory<>("dateRev"));
        recette.setCellValueFactory(new PropertyValueFactory<>("recette"));
        
        nomF.setCellValueFactory(new PropertyValueFactory<>("Nom_Four"));
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
        
        
        try {  
            new InterAvancePaie().chargerDonnées(data_avPaie,tabAvPaie);
            new InterDepenses().chargerFactures(data,tabFacture);
            chargerDonnéesRev();
            listerFournisseur();
            listerProduit();
        } catch (SQLException ex) {
            Logger.getLogger(Comptable.class.getName()).log(Level.SEVERE, null, ex);
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
    private void rechFacAction(ActionEvent event) throws SQLException {
        String tab[] = new String[2];
        String tab2[] = new String[2];
        String  sql =null;
        
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

    @FXML
    private void rechRevAction(ActionEvent event) throws SQLException 
    {
        String sql =null;
        if (dateDuRev.getValue()!=null && dateAuRev.getValue()==null)
        {
            sql = "SELECT Date,SUM(Recette) AS somme FROM(SELECT Date,IdCaisse,Recette "
                    + "FROM parcdattractions.gérecaisse WHERE Date>=' "+dateDuRev.getValue()+"' "
                    + "GROUP BY Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
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
        }else if (dateDuRev.getValue()==null && dateAuRev.getValue()!=null)
        {
            sql = "SELECT Date,SUM(Recette) AS somme FROM(SELECT Date,IdCaisse,Recette "
                    + "FROM parcdattractions.gérecaisse WHERE Date<=' "+dateAuRev.getValue()+"' "
                    + "GROUP BY Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
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
        }else if (dateDuRev.getValue()!=null && dateAuRev.getValue()!=null)
        {
            sql = "SELECT Date,SUM(Recette) AS somme FROM(SELECT Date,IdCaisse,Recette "
                    + "FROM parcdattractions.gérecaisse WHERE Date>=' "+dateDuRev.getValue()+"' AND Date<=' "+dateAuRev.getValue()+"' "
                    + "GROUP BY Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
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
    }

    @FXML
    private void actualiserRechFact(ActionEvent event) throws SQLException 
    {
        cbFourn.setValue(null);
        cbProd.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        payéRb.setSelected(false);
        nonPayéRb.setSelected(false);
        
        new InterDepenses().chargerFactures(data,tabFacture);
        listerFournisseur();
        listerProduit();
    }

    @FXML
    private void actualiserRechRev(ActionEvent event) throws SQLException {
        dateDuRev.setValue(null);
        dateAuRev.setValue(null);
        
        chargerDonnéesRev();
    }

    private void listerFournisseur() throws SQLException {
        String sql = "SELECT IdFournisseur,NomFournisseur FROM fournisseur";
        cnx= new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbFourn.getItems().clear();
        while(rs.next())
        {
            cbFourn.getItems().add(rs.getInt("IdFournisseur")+"-"+rs.getString("NomFournisseur"));
        }
    }
    
    private void listerProduit() throws SQLException {
        String sql = "SELECT IdProduit,Libelle FROM produit";
        cnx= new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbProd.getItems().clear();
        while(rs.next())
        {
            cbProd.getItems().add(rs.getInt("IdProduit")+"-"+rs.getString("Libelle"));
        }
    }

    @FXML
    private void eventListerFourn(KeyEvent event) {
        try
        {
            String sql = "SELECT IdFournisseur,NomFournisseur FROM fournisseur "
                + "WHERE NomFournisseur LIKE '%"+cbFourn.getValue()+"%'";
            cnx= new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbFourn.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                cbFourn.setVisibleRowCount(i);
                cbFourn.getItems().add(rs.getInt("IdFournisseur")+"-"+rs.getString("NomFournisseur"));
                cbFourn.show();
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void eventListerProd(KeyEvent event) {
       try{
            String sql = "SELECT IdProduit,Libelle FROM produit "
                + "WHERE Libelle LIKE '%"+cbProd.getValue()+"%'";
            cnx= new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbProd.getItems().clear();
            int i=0;
            while(rs.next())
            {   
                i++;
                cbProd.setVisibleRowCount(i);
                cbProd.getItems().add(rs.getInt("IdProduit")+"-"+rs.getString("Libelle"));
                cbProd.show();
            }
        }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
    }
    }


    @FXML
    private void ouvrirGraphiques(ActionEvent event) throws IOException 
    {
        stage_graphique =new Stage();
        stage_graphique.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/graphiqueRevenus.fxml"));
        Scene scene = new Scene(root);
        stage_graphique.setScene(scene);
        stage_graphique.show();
        
    }

    @FXML
    private void LogOut(MouseEvent event)  throws Exception {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        MainProjet m = new MainProjet();
        Stage stagee = new Stage();
        m.start(stagee);
    }
}

    
    

