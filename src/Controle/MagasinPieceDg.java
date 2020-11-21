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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import tableau.TabOutillage;
import tableau.TabPieces;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class MagasinPieceDg implements Initializable {
        ObservableList<TabOutillage> data = FXCollections.observableArrayList();    
    FilteredList<TabOutillage> filtredData = new FilteredList<>(data,e->true);
    
    ObservableList<TabPieces> data_2 = FXCollections.observableArrayList();    
    FilteredList<TabPieces> filtredData_2 = new FilteredList<>(data_2,e->true);
public Connection cnx;
    @FXML
    private TableView<TabPieces> tabPieces;
    @FXML
    private TableColumn<?, ?> pièce;
    @FXML
    private TableColumn<?, ?> qte;
    @FXML
    private TableColumn<?, ?> date;
    @FXML
    private TableColumn<?, ?> preneur;
    @FXML
    private JFXButton btnActualiser;
    @FXML
    private TableView<TabOutillage> tabOutillage;
    @FXML
    private TableColumn<?, ?> outillage;
    @FXML
    private TableColumn<?, ?> utilisateur;
    @FXML
    private TableColumn<?, ?> dateRemise;
    @FXML
    private TableColumn<?, ?> dateRestit;
    @FXML
    private JFXDatePicker dateDu2;
    @FXML
    private JFXDatePicker dateAu2;
    @FXML
    private JFXComboBox<String> choixOutilRech;
    @FXML
    private JFXComboBox<String> choixUtilRech;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    @FXML
    private JFXComboBox<String> choixPieceRech;
    @FXML
    private JFXComboBox<String> choixPreneurRech;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {

            
            outillage.setCellValueFactory(new PropertyValueFactory<>("Outillage"));
            utilisateur.setCellValueFactory(new PropertyValueFactory<>("Utilisateur"));
            dateRemise.setCellValueFactory(new PropertyValueFactory<>("Date_remise"));
            dateRestit.setCellValueFactory(new PropertyValueFactory<>("Date_restitution"));
            
            pièce.setCellValueFactory(new PropertyValueFactory<>("Pièce"));
            qte.setCellValueFactory(new PropertyValueFactory<>("Qte"));
            date.setCellValueFactory(new PropertyValueFactory<>("Date"));
            preneur.setCellValueFactory(new PropertyValueFactory<>("Preneur"));
            

            chargerDonnéesPièces();
            chargerDonnéesOutillage();
            listerPieceRech();
            listerPreneurRech();
            listerOutilRech();
            listerUtilRech();
        } catch (SQLException ex) {
            
        }
        
    }    

  
  

 
 

    @FXML
    private void rechOutilAction(ActionEvent event) throws SQLException 
    {
        String tab[] =new String [2];
        String tab2[] =new String [2];
        String sql = null;
        
        if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab=choixOutilRech.getValue().split("-");
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab=choixOutilRech.getValue().split("-");
            tab2=choixUtilRech.getValue().split("-");
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' AND NumUtili='"+tab2[0]+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixOutilRech.getValue().split("-");
            tab2=choixUtilRech.getValue().split("-");
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' AND NumUtili='"+tab2[0]+"' "
                    + "AND dateRest>='"+dateDu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            tab=choixOutilRech.getValue().split("-");
            tab2=choixUtilRech.getValue().split("-");
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' AND NumUtili='"+tab2[0]+"' "
                    + "AND dateRest>='"+dateDu.getValue()+"' AND dateRest<='"+dateAu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab=choixOutilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' "
                    + "AND dateRest>='"+dateDu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            tab=choixOutilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' "
                    + "AND dateRest>='"+dateDu.getValue()+"' AND dateRest<= '"+dateAu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab=choixOutilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' "
                    + "AND dateRest<='"+dateAu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()!=null && choixUtilRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab=choixOutilRech.getValue().split("-");
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE IdOutillage= '"+tab[0]+"' AND NumUtili='"+tab2[0]+"' "
                    + "AND dateRest<='"+dateAu.getValue()+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE NumUtili='"+tab2[0]+"' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE NumUtili='"+tab2[0]+"' AND dateRest>='"+dateDu.getValue()+"'"
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()!=null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE NumUtili='"+tab2[0]+"' AND dateRest<='"+dateAu.getValue()+"'"
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()!=null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE NumUtili='"+tab2[0]+"' AND dateRest >= '"+dateDu.getValue()+ "' "
                            + "AND dateRest<='"+dateAu.getValue()+"'"
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE dateRest >= '"+dateDu.getValue()+ "' "
                            + "AND dateRest<='"+dateAu.getValue()+"'"
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()==null && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE dateRest >= '"+dateDu.getValue()+ "' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        }else if (choixOutilRech.getValue()==null && choixUtilRech.getValue()==null && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            tab2=choixUtilRech.getValue().split("-");
            
            sql = "SELECT DatePrise,IdOutillage,Libelle,NumUtili,Nom,Prenom,DateRest "
                    +"FROM utilise,piece,personnel "
                    + "WHERE dateRest <= '"+dateAu.getValue()+ "' "
                    + "AND IdOutillage=Ref AND NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabOutillage.getItems().clear();
            while (rs.next())
                        {
                            data.add(new TabOutillage(                     
                                    rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),
                                    rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),     
                                    rs.getString("DatePrise"),     
                                    rs.getString("DateRest")     
                                    ));
                                    tabOutillage.setItems(data);
                        }
                        cnx.close();
        } 
    }

    @FXML
    private void rechPieceAction(ActionEvent event) throws SQLException 
    {
        String tab[] =new String [2];
        String tab2[] =new String [2];
        String sql = null;
        
        if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()==null && dateDu2.getValue()==null && dateAu2.getValue()==null)
        {
            tab=choixPieceRech.getValue().split("-");
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    +"FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()!=null && dateDu2.getValue()==null && dateAu2.getValue()==null)
        {
            tab=choixPieceRech.getValue().split("-");
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    +"FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' AND IdPreneur= '"+tab2[0]+"' AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()!=null && dateDu2.getValue()!=null && dateAu2.getValue()==null)
        {
            tab=choixPieceRech.getValue().split("-");
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    +"FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' AND IdPreneur= '"+tab2[0]+"' AND "
                    + "Date >= '"+dateDu2.getValue()+"' AND "
                    + "IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()!=null && dateDu2.getValue()!=null && dateAu2.getValue()!=null)
        {
            tab=choixPieceRech.getValue().split("-");
            tab2=choixPreneurRech.getValue().split("-");
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    +"FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' AND IdPreneur= '"+tab2[0]+"' AND "
                    + "Date >= '"+dateDu2.getValue()+"' AND Date <= '"+dateAu2.getValue()+"'"
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()==null && dateDu2.getValue()!=null && dateAu2.getValue()==null)
        {
            tab=choixPieceRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' "
                    + "AND Date >= '"+dateDu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()==null && dateDu2.getValue()!=null && dateAu2.getValue()!=null)
        {
            tab=choixPieceRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' "
                    + "AND Date >= '"+dateDu2.getValue()+"' AND Date <= '"+dateAu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()==null && dateDu2.getValue()==null && dateAu2.getValue()!=null)
        {
            tab=choixPieceRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' "
                    + "AND Date <= '"+dateAu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()!=null && choixPreneurRech.getValue()!=null && dateDu2.getValue()==null && dateAu2.getValue()!=null)
        {
            tab=choixPieceRech.getValue().split("-");
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPiece= '"+tab[0]+"' AND IdPreneur= '"+tab2[0]+"' "
                    + "AND Date <= '"+dateAu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()!=null && dateDu2.getValue()==null && dateAu2.getValue()==null)
        {
            
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPreneur= '"+tab2[0]+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()!=null && dateDu2.getValue()!=null && dateAu2.getValue()==null)
        {
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPreneur= '"+tab2[0]+"' "
                    + "AND Date>= '"+dateDu2.getValue()+"'"
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()!=null && dateDu2.getValue()==null && dateAu2.getValue()!=null)
        {
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPreneur= '"+tab2[0]+"' "
                    + "AND Date<= '"+dateAu2.getValue()+"'"
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()!=null && dateDu2.getValue()!=null && dateAu2.getValue()!=null)
        {
            tab2=choixPreneurRech.getValue().split("-");
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE IdPreneur= '"+tab2[0]+"' "
                    + "AND Date>= '"+dateDu2.getValue()+"' AND Date<= '"+dateAu2.getValue()+"'"
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()==null && dateDu2.getValue()!=null && dateAu2.getValue()!=null)
        {
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE Date>= '"+dateDu2.getValue()+"' AND Date<= '"+dateAu2.getValue()+"'"
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()==null && dateDu2.getValue()!=null && dateAu2.getValue()==null)
        {
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE Date>= '"+dateDu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }else if (choixPieceRech.getValue()==null && choixPreneurRech.getValue()==null && dateDu2.getValue()==null && dateAu2.getValue()!=null)
        {
            
            sql = "SELECT Date,IdPiece,Libelle,IdPreneur,Nom,Prenom,prend.Qte "
                    + "FROM prend,piece,personnel "
                    + "WHERE Date<= '"+dateAu2.getValue()+"' "
                    + "AND IdPiece=Ref AND IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tabPieces.getItems().clear();
            while (rs.next())
                        {
                            data_2.add(new TabPieces(                     
                                    rs.getString("IdPiece")+"|"+rs.getString("Libelle"),
                                    rs.getInt("Qte"),
                                    rs.getString("Date"),    
                                    rs.getString("IdPreneur")+"|"+rs.getString("Nom")+" "+rs.getString("Prenom")     
                                    ));
                                    tabPieces.setItems(data_2);
                        }
                        cnx.close();
        }
        
    }
    

    @FXML
    private void cherchPiece(KeyEvent event) throws SQLException {
        try
        {
        {
            String sql = "SELECT IdPiece,Libelle FROM piece,prend "
                    + "WHERE IdPiece=Ref";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPieceRech.getItems().clear();
            int i=0;
            while (rs.next())
            {   
                i++;
                choixPieceRech.setVisibleRowCount(i);
                choixPieceRech.getItems().add(rs.getInt("IdPiece")+"-"+rs.getString("Libelle"));
                choixPieceRech.show();
            }
        }
        }catch(Exception ex)
        {
            
        }
        cnx.close();
    }
    private void chargerDonnéesPièces() throws SQLException
    {
        String sql="SELECT IdPiece,IdPreneur,Date,prend.Qte,Nom,Prenom,Libelle FROM prend,personnel,piece WHERE prend.IdPreneur=personnel.NumContrat AND prend.IdPiece=Ref";
        
        data_2.clear();
        cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            tabPieces.getItems().clear();
            while(rs.next())
            {
                data_2.add(new TabPieces(                      
		rs.getString("IdPiece")+"|"+rs.getString("Libelle"), 
                rs.getInt("Qte"),
                rs.getString("Date"), 
		rs.getString("IdPreneur")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom")         
                ));                       
		tabPieces.setItems(data_2);      
            }
            cnx.close();
    }
    
    private void chargerDonnéesOutillage() throws SQLException
    {
        String sql="SELECT IdOutillage,Libelle,NumUtili,Nom,Prenom,DatePrise,DateRest FROM utilise,personnel,piece WHERE utilise.NumUtili=personnel.NumContrat AND utilise.IdOutillage=Ref";
        
        data.clear();
        cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            
            while(rs.next())
            {
                data.add(new TabOutillage(                      
		rs.getString("IdOutillage")+"|"+rs.getString("Libelle"),          
		rs.getString("NumUtili")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"),      
		rs.getString("DatePrise"),  
                rs.getString("DateRest")         
                ));                       
		tabOutillage.setItems(data); 
            }
            cnx.close();
    }

    @FXML
    private void cherchPreneur(KeyEvent event) throws SQLException {
        try
        {
       
        {
            String sql = "SELECT NumContrat,Nom,Prenom FROM personnel,prend "
                    + "WHERE IdPreneur=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPreneurRech.getItems().clear();
            int i=0;
            while (rs.next())
            {   
                i++;
                choixPreneurRech.setVisibleRowCount(i);
                choixPreneurRech.getItems().add(rs.getInt("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
                choixPreneurRech.show();
            }
            
        }
        }catch(Exception ex)
        {
            
        }
        cnx.close();
    }

    @FXML
    private void cherchOutilEvent(KeyEvent event) throws SQLException {
        try
        {
        {
            String sql = "SELECT Ref,Libelle FROM piece,utilise "
                    + "WHERE IdOutillage=Ref";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPieceRech.getItems().clear();
            int i=0;
            while (rs.next())
            {   
                i++;
                choixOutilRech.setVisibleRowCount(i);
                choixOutilRech.getItems().add(rs.getInt("Ref")+"-"+rs.getString("Libelle"));
                choixOutilRech.show();
            }
            
        }
        }catch(Exception ex)
        {
            
        }
        cnx.close();
    }

    @FXML
    private void cherchUtilEvent(KeyEvent event) throws SQLException {
        try
        {
        {
            String sql = "SELECT NumContrat,Nom,Prenom FROM personnel,utilise "
                    + "WHERE NumUtili=NumContrat";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixUtilRech.getItems().clear();
            int i=0;
            while (rs.next())
            {   
                i++;
                choixUtilRech.setVisibleRowCount(i);
                choixUtilRech.getItems().add(rs.getInt("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
                choixUtilRech.show();
            }
            
        }
        }catch(Exception ex)
        {
            
        }
        cnx.close();
    }
    
    public void listerUtilRech() throws SQLException
    {   
        try
        {
            String sql="SELECT DISTINCT NumContrat,Prenom,Nom FROM utilise,personnel "
                    + "WHERE NumContrat=NumUtili";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixUtilRech.getItems().clear();
            while (rs.next())
                choixUtilRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }catch(Exception ex)
        {
            
        }
        cnx.close();
    }
    
    public void listerPreneurRech() throws SQLException
        {
            try
            {
            String sql="SELECT DISTINCT NumContrat,Prenom,Nom FROM prend,personnel "
                    + "WHERE NumContrat=IdPreneur";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPreneurRech.getItems().clear();
            while (rs.next())
                choixPreneurRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
            }catch(Exception ex)
            {
                
            }
            cnx.close();
        }
    
    public void listerOutilRech() throws SQLException
        {
            
            String sql="SELECT DISTINCT Ref,Libelle FROM utilise,piece "
                    + "WHERE Ref=IdOutillage";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixOutilRech.getItems().clear();
            while (rs.next())
                choixOutilRech.getItems().add(rs.getString("Ref")+"-"+rs.getString("Libelle"));
            cnx.close();
        }
    
    public void listerPieceRech() throws SQLException
        {
            String sql="SELECT DISTINCT Ref,Libelle FROM prend,piece "
                    + "WHERE Ref=IdPiece";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPieceRech.getItems().clear();
            while (rs.next())
                choixPieceRech.getItems().add(rs.getString("Ref")+"-"+rs.getString("Libelle"));
            cnx.close();
        }

    @FXML
    private void actPieces(ActionEvent event) throws SQLException {
        choixPieceRech.setValue(null);
        choixPreneurRech.setValue(null);
        dateDu2.setValue(null);
        dateAu2.setValue(null);
            
        chargerDonnéesPièces();
        listerPieceRech();
        listerPreneurRech();
        //new MagasinPieces().listerOutilRech();
        //new MagasinPieces().listerUtilRech();
    }

    @FXML
    private void clickTabPieces(MouseEvent event) {
    }

    @FXML
    private void actOutillage(ActionEvent event) throws SQLException {
        choixOutilRech.setValue(null);
        choixOutilRech.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        
        chargerDonnéesOutillage();
        listerOutilRech();
        listerUtilRech();
    }

    @FXML
    private void clickTabOutillage(MouseEvent event) {
    }

}
