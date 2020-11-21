package Controle;

import Main.ConnexionBDD;
import Reports.ImpOutii;
import Reports.ImpPieceDem;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import java.util.regex.Pattern;
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
import org.controlsfx.control.Notifications;
import tableau.TabOutillage;
import tableau.TabPieces;





public class MagasinPieces implements Initializable {
    
    ObservableList<TabOutillage> data = FXCollections.observableArrayList();    
    FilteredList<TabOutillage> filtredData = new FilteredList<>(data,e->true);
    
    ObservableList<TabPieces> data_2 = FXCollections.observableArrayList();    
    FilteredList<TabPieces> filtredData_2 = new FilteredList<>(data_2,e->true);

    @FXML
    private JFXComboBox<String> cbPiece;
    @FXML
    private JFXComboBox<String> cbPreneur;
    @FXML
    private JFXComboBox<String> cbOutillage;
    @FXML
    private JFXComboBox<String> cbUtilisateur;
    @FXML
    private JFXDatePicker dateRest;
    @FXML
    private TableColumn<?, ?> outillage;
    @FXML
    private TableColumn<?, ?> utilisateur;
    @FXML
    private TableColumn<?, ?> dateRemise;
    @FXML
    private TableColumn<?, ?> dateRestit;
    @FXML
    private TableView<TabOutillage> tabOutillage;
    @FXML
    private JFXTextField tfQte;

    TabOutillage to = null;
    TabPieces tp = null;
    @FXML
    private JFXButton btnActualiser;
    @FXML
    private JFXTextField tfQte_P;
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
    public Connection cnx;
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
    @FXML
    private JFXDatePicker dateDu2;
    @FXML
    private JFXDatePicker dateAu2;
    Paint value0=Paint.valueOf("F9C7CC");
    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
       
        try {
            listerPiece();
            listerPreneurUtilisateur();
            listerOutillage();
            
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
            System.out.println(ex);
        }
        
    }
private void listerOutillage() throws SQLException 
    {
        cbOutillage.getItems().clear();
        String itemOutil=null;
        String sql="SELECT * FROM piece WHERE Type='Outillage'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            itemOutil=rs.getInt("Ref")+"| "+rs.getString("Libelle")+" ("+rs.getInt("Qte")+")";
            cbOutillage.getItems().add(itemOutil);
        }
        cnx.close();
    }    

    private void listerPiece() throws SQLException 
    {
        cbPiece.getItems().clear();
        String itemPiece=null;
        String sql="SELECT * FROM piece WHERE Type='Pièce'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next())
        {   
            itemPiece=rs.getInt("Ref")+"|"+rs.getString("Libelle")+" ("+rs.getInt("Qte")+")";
            cbPiece.getItems().add(itemPiece);
        }
        cnx.close();
    }
    
    private void listerPreneurUtilisateur() throws SQLException 
    {
        String itemPreneurUtili=null;
        String sql="SELECT NumContrat,Nom,Prenom FROM poste,personnel WHERE personnel.IdPoste=poste.IdPoste AND (Libelle='Maintenance' OR 'Libelle=Sécurité')";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbPreneur.getItems().clear();
        cbUtilisateur.getItems().clear();
        while (rs.next())
        {   
            itemPreneurUtili=rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom");
            cbPreneur.getItems().add(itemPreneurUtili);
            cbUtilisateur.getItems().add(itemPreneurUtili);
        }
        cnx.close();
    }
    
    @FXML
    private void clickTabOutillage()
    {
        try
        {
            to=tabOutillage.getSelectionModel().getSelectedItem();
            
            cbOutillage.setValue(to.getOutillage());
            cbUtilisateur.setValue(to.getUtilisateur());
            
            cbOutillage.setDisable(true);
            cbUtilisateur.setDisable(true);
            dateRest.setDisable(true);
            tfQte.setDisable(true);
            
            String [] tab2 = new String [2];
            tab2=cbUtilisateur.getSelectionModel().getSelectedItem().split("-");
            
            String [] tab = new String [2];
            int qte=0;
            tab=cbOutillage.getSelectionModel().getSelectedItem().split("|");
            String query="SELECT Qte FROM utilise WHERE IdOutillage='"+tab[0]+"' AND NumUtili='"+tab2[0]+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st= cnx.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
                qte=rs.getInt("Qte");
            cnx.close();
            tfQte.setText(String.valueOf(qte));
            
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDate ld = LocalDate.parse(to.getDate_restitution(),formatter);
            dateRest.setValue(ld);
        }catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    @FXML
    private void clickTabPieces()
    {
        try
        {
            tp=tabPieces.getSelectionModel().getSelectedItem();
            
            cbPiece.setValue(tp.getPièce());
            cbPreneur.setValue(tp.getPreneur());
            tfQte_P.setText(String.valueOf(tp.getQte()));
            
            cbPiece.setDisable(true);
            cbPreneur.setDisable(true);
            tfQte_P.setDisable(true);
            
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }


    @FXML
    private void fournirOutillage(ActionEvent event) throws SQLException 
    {
                 if(verificationCompOutil())
                 {
                 try{

            String tab[]=new String[2];
            String dateNow=null;
            int qteDispo=0;
            tab=cbOutillage.getSelectionModel().getSelectedItem().split("|");
            int idOutil=Integer.valueOf(tab[0]);
            
            String sql="SELECT Qte FROM piece WHERE Ref="+tab[0];
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
             if(VerQte(tfQte.getText()))
            {
            while(rs.next())
                qteDispo=rs.getInt("Qte");
            cnx.close();
            if(qteDispo >= Integer.valueOf(tfQte.getText()))
            {   
                qteDispo=qteDispo-Integer.valueOf(tfQte.getText());
                tab=cbUtilisateur.getSelectionModel().getSelectedItem().split("-");
                String idPreneur=tab[0];
                if(verifeDateRes(dateRest.getValue()))
                {
                sql="SELECT DATE(NOW()) AS dateNow";
                cnx = new ConnexionBDD().ConnexionDB();
                st = cnx.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                    dateNow=rs.getString("dateNow");
                
                String date_Rest = new AjoutModifPersonnel().changeFormatDate(dateRest);
                sql = "INSERT INTO utilise(DatePrise,IdOutillage,NumUtili,DateRest,Qte) VALUES(?,?,?,?,?);";
                       
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1,dateNow);
                ps.setInt(2, idOutil);
                ps.setString(3,idPreneur);
                ps.setString(4,date_Rest);
                ps.setInt(5,Integer.valueOf(tfQte.getText()));
                ps.executeUpdate();
                
                String query="UPDATE piece SET Qte='"+qteDispo+"' WHERE Ref='"+idOutil+"'";
                ps.executeUpdate(query);
                ImpOutii outil = new ImpOutii();
                outil.showReport(idPreneur, dateNow, idOutil);
                cnx.close();
                notif(2,1);
                
                actualiserOutillage();
                }
                else
                {information(1);}}

                  else
                  {
             information(2);
            }}}
        
        catch(Exception ex)
        {
        
        }}
        
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
    
    private void chargerDonnéesPièces() throws SQLException
    {
        String sql="SELECT IdPiece,IdPreneur,Date,prend.Qte,Nom,Prenom,Libelle FROM prend,personnel,piece WHERE prend.IdPreneur=personnel.NumContrat AND prend.IdPiece=Ref";
        
        data_2.clear();
        cnx = new ConnexionBDD().ConnexionDB();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            
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

    @FXML
    private void supprimerOutillage(ActionEvent event) 
    {    if(confirmation(2,2))
        {
        try
        {
            String tabOutil[] = new String [2];
            String tabUtil[] = new String [2];
            String date = null;
            int qteStock = 0,qte=0;
            
            tabOutil=cbOutillage.getSelectionModel().getSelectedItem().split("|");
            tabUtil=cbUtilisateur.getSelectionModel().getSelectedItem().split("-");
             
            to= tabOutillage.getSelectionModel().getSelectedItem();
            date=to.getDate_remise();
            
            qte=Integer.valueOf(tfQte.getText());

            String sql = "DELETE FROM utilise WHERE DatePrise='"+date+"' AND IdOutillage='"+tabOutil[0]+"' AND NumUtili='"+tabUtil[0]+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            st.executeUpdate(sql);
            
            sql = "SELECT Qte FROM piece WHERE Ref='"+tabOutil[0]+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
                qteStock=rs.getInt("Qte");
            qteStock=qteStock+qte;
            sql = "UPDATE piece SET Qte='"+qteStock+"' WHERE Ref='"+tabOutil[0]+"'";
            st.executeUpdate(sql);
            cnx.close();
            notif(2,2);
             actualiserOutillage();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }}
    else
        try {
            actualiserOutillage();
        } catch (SQLException ex) {
            Logger.getLogger(MagasinPieces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void supprimerPiece(ActionEvent event) 
    {
        if(confirmation(1,2))
        {
        try
        {
            String tabPiece[] = new String [2];
            String tabPreneur[] = new String [2];
            String date = null;
            int qteStock=0,qte=0;
            
            tabPiece=cbPiece.getSelectionModel().getSelectedItem().split("|");
            tabPreneur=cbPreneur.getSelectionModel().getSelectedItem().split("-");
             
            tp= tabPieces.getSelectionModel().getSelectedItem();
            date=tp.getDate();
            
            qte=Integer.valueOf(tfQte_P.getText());

            String sql = "DELETE FROM prend WHERE Date='"+date+"' AND IdPiece='"+tabPiece[0]+"' AND IdPreneur='"+tabPreneur[0]+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            st.executeUpdate(sql);
            
            sql = "SELECT Qte FROM piece WHERE Ref='"+tabPiece[0]+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
                qteStock=rs.getInt("Qte");
            qteStock=qteStock+qte;
            sql = "UPDATE piece SET Qte='"+qteStock+"' WHERE Ref='"+tabPiece[0]+"'";
            st.executeUpdate(sql);
            cnx.close();
            notif(1,2);
            actualiserPieces();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }}
        else
        try {
            actualiserPieces();
        } catch (SQLException ex) {
            Logger.getLogger(MagasinPieces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualiserOutillage() throws SQLException
    {   
        cbOutillage.setDisable(false);
        cbUtilisateur.setDisable(false);
        dateRest.setDisable(false);
        tfQte.setDisable(false);
            
        cbOutillage.setValue("");
        cbUtilisateur.setValue("");
        tfQte.setText("");
        dateRest.setValue(null);
        
        choixOutilRech.setValue(null);
        choixOutilRech.setValue(null);
        dateDu.setValue(null);
        dateAu.setValue(null);
        
        chargerDonnéesOutillage();
        listerOutillage();
        listerOutilRech();
        listerPreneurUtilisateur();
        listerUtilRech();
        
    }

    @FXML
    private void actOutillage(ActionEvent event) throws SQLException 
    {
        actualiserOutillage();
        actBackGround();
    }
    
    private void actualiserPieces() throws SQLException
    {   
        cbPiece.setDisable(false);
        cbPreneur.setDisable(false);
        tfQte_P.setDisable(false);
        
        choixPieceRech.setValue(null);
        choixPreneurRech.setValue(null);
        dateDu2.setValue(null);
        dateAu2.setValue(null);
            
        cbPiece.setValue("");
        cbPreneur.setValue("");
        tfQte_P.setText("");
          
        chargerDonnéesPièces();
        listerPiece();
        listerPieceRech();
        listerPreneurUtilisateur();
        listerPreneurRech();
    }
    
    @FXML
    private void actPieces(ActionEvent event) throws SQLException 
    {
        actualiserPieces();
        this.actBackGroundPiece();
    }
    
    public void actBackGround()
    {
        Paint value=Paint.valueOf("EAEDF1");
        dateRest.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY)));
        tfQte.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
        cbUtilisateur.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY)));
        cbOutillage.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    public void actBackGroundPiece()
    {    Paint value=Paint.valueOf("EAEDF1");
        tfQte_P.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
        cbPreneur.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
        cbPiece.setBackground(new Background(new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY))); 
    }

    @FXML
    private void fournirPiece(ActionEvent event) 
    {
          if(verificationCompPiece())
          {
        try{
            String tab[]=new String[2];
            String dateNow=null;
            int qteDispo=0;
            tab=cbPiece.getSelectionModel().getSelectedItem().split("|");
            int idPiece=Integer.valueOf(tab[0]);
            
            String sql="SELECT Qte FROM piece WHERE Ref="+tab[0];
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next())
                qteDispo=rs.getInt("Qte");
            if(qteDispo >= Integer.valueOf(tfQte_P.getText()))
            {   
                qteDispo=qteDispo-Integer.valueOf(tfQte_P.getText());
                tab=cbPreneur.getSelectionModel().getSelectedItem().split("-");
                String idPreneur=tab[0];

                sql="SELECT DATE(NOW()) AS dateNow";
                st = cnx.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                    dateNow=rs.getString("dateNow");
                

                sql = "INSERT INTO prend(IdPiece,IdPreneur,Date,Qte) VALUES(?,?,?,?)";
                        
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setInt(1,idPiece);
                ps.setString(2, idPreneur);
                ps.setString(3,dateNow);
                ps.setInt(4,Integer.valueOf(tfQte_P.getText())); 
                ps.executeUpdate();               
                String query="UPDATE piece SET Qte='"+qteDispo+"' WHERE Ref='"+idPiece+"'";
                ps.executeUpdate(query);
                notif(1, 1);
                actualiserPieces();
                 ImpPieceDem piece = new  ImpPieceDem();
                 piece.showReport(idPreneur, dateNow, idPiece);
                
            }
            else
            {
               information(3); 
            }
            cnx.close();
         
        }
    catch(Exception ex)
                {
           
                }

          }
     
}

    @FXML
    private void cherchOutil(KeyEvent event) throws SQLException 
    {
        String sql = "SELECT Ref,Libelle,Qte FROM piece WHERE Type='Outillage' AND Libelle LIKE '%"+cbOutillage.getValue()+"%'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbOutillage.getItems().clear();
        int i=0;
        while(rs.next())
        {
            i++;
            cbOutillage.getItems().add(rs.getInt("Ref")+"|"+rs.getString("Libelle")+" ("+rs.getInt("Qte")+")");
            cbOutillage.show();
        }
        cnx.close();
    }

    @FXML
    private void cherchUtil(KeyEvent event) throws SQLException 
    {   
       
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND (Libelle='Maintenance' OR Libelle='Sécurité') AND ADDDate(DateDebCon,INTERVAL Durée MONTH) >= DATE(NOW()) AND (Nom LIKE '%"+cbUtilisateur.getValue()+"%' OR Prenom LIKE '%"+cbUtilisateur.getValue()+"%')";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbUtilisateur.getItems().clear();
        int i=0;
        while(rs.next())
        {
            i++;
            cbUtilisateur.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            cbUtilisateur.show();
        }
        cnx.close();
    }

    @FXML
    private void cherchPièce(KeyEvent event) throws SQLException 
    {
        String sql = "SELECT Ref,Libelle,Qte FROM piece WHERE Type='Pièce' AND Libelle LIKE '%"+cbPiece.getValue()+"%'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbPiece.getItems().clear();
        int i=0;
        while(rs.next())
        {
            i++;
            cbPiece.getItems().add(rs.getInt("Ref")+"|"+rs.getString("Libelle")+" ("+rs.getInt("Qte")+")");
            cbPiece.show();
        }
        cnx.close();
    }

    @FXML
    private void cherchPren(KeyEvent event) throws SQLException 
    {   

        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste "
                + "WHERE personnel.IdPoste=poste.IdPoste AND (Libelle='Maintenance' OR Libelle='Sécurité') AND ADDDate(DateDebCon,INTERVAL Durée MONTH) >= DATE(NOW()) AND (Nom LIKE '%"+cbPreneur.getValue()+"%' OR Prenom LIKE '%"+cbPreneur.getValue()+"%')";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbPreneur.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            cbPreneur.setVisibleRowCount(i);
            cbPreneur.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            cbPreneur.show();
        }
        cnx.close();
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
    private void listerPieceRech() throws SQLException
        {
            String sql="SELECT DISTINCT Ref,Libelle FROM prend,piece "
                    + "WHERE Ref=IdPiece";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPieceRech.getItems().clear();
            while (rs.next())
                choixPieceRech.getItems().add(rs.getString("Ref")+"-"+rs.getString("Libelle"));
        }
    private void listerOutilRech() throws SQLException
        {
            String sql="SELECT DISTINCT Ref,Libelle FROM utilise,piece "
                    + "WHERE Ref=IdOutillage";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixOutilRech.getItems().clear();
            while (rs.next())
                choixOutilRech.getItems().add(rs.getString("Ref")+"-"+rs.getString("Libelle"));
        }
    private void listerPreneurRech() throws SQLException
        {
            String sql="SELECT DISTINCT NumContrat,Prenom,Nom FROM prend,personnel "
                    + "WHERE NumContrat=IdPreneur";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixPreneurRech.getItems().clear();
            while (rs.next())
                choixPreneurRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        }
    private void listerUtilRech() throws SQLException
    {
        String sql="SELECT DISTINCT NumContrat,Prenom,Nom FROM utilise,personnel "
                    + "WHERE NumContrat=NumUtili";
            
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            choixUtilRech.getItems().clear();
            while (rs.next())
                choixUtilRech.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
            cnx.close();
    }

    @FXML
    private void cherchPiece(KeyEvent event) throws SQLException {
        try
        {
            String sql = "SELECT DISTINCT IdPiece,Libelle FROM piece,prend "
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
            cnx.close();
        }catch(Exception ex)
        {
            
        }
    }

    @FXML
    private void cherchPreneur(KeyEvent event) throws SQLException {
        try
        {
            String sql = "SELECT DISTINCT NumContrat,Nom,Prenom FROM personnel,prend "
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
            cnx.close();
        }catch(Exception ex)
        {
            
        }
    }

    @FXML
    private void cherchOutilEvent(KeyEvent event) throws SQLException {
        try
        {
            String sql = "SELECT DISTINCT Ref,Libelle FROM piece,utilise "
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
            cnx.close();
        }catch(Exception ex)
        {
            
        }
    }

    @FXML
    private void cherchUtilEvent(KeyEvent event) throws SQLException {
        
        try
        {
            String sql = "SELECT DISTINCT NumContrat,Nom,Prenom FROM personnel,utilise "
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
            cnx.close();
        }catch(Exception ex)
        {
            
        }
    }

    private void QteCont(KeyEvent event) {
               if(!Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }
    
    public boolean VerQte(String txt)
    {
        String regx ="[0-9]*[1-9]+[0-9]*";
        Pattern p = Pattern.compile(regx);
        if(txt.matches(regx))
        {
            return true;
        }
        else return false;
    }


    public boolean verifeChampsPieces()
    {  boolean b = true;

        if(tfQte_P.getText().equals("")||!VerQte(tfQte_P.getText()))
        {
            if(!VerQte(tfQte_P.getText()) || !tfQte_P.getText().equals(""))
            {
                information(4);
            }
            tfQte_P.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;
        
        }   
       if(cbPiece.getSelectionModel().isEmpty())
       {
           cbPiece.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
       }
       if(cbPreneur.getSelectionModel().isEmpty())
       {
           cbPreneur.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
       }
       return b;
    }
    
    public boolean verifieChampsOuti()
    {   boolean b = true;
    LocalDate date = dateRest.getValue();
         if(tfQte.getText().equals("") || !VerQte(tfQte.getText()))
       {
           if(!VerQte(tfQte.getText()) && !tfQte.getText().equals(""))
           {
               information(5);
           }
           tfQte.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;
       }   
       if(cbOutillage.getSelectionModel().isEmpty())
       {
           cbOutillage.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
       }
       if(cbUtilisateur.getSelectionModel().isEmpty())
       {
           cbUtilisateur.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
       }
       if(dateRest.getValue()==null || !verifeDateRes(date))
           {
               if(!verifeDateRes(date) && !dateRest.equals(null))
                       {
                           information(1);
                       }
               dateRest.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;
           
           }

       return b;
    }
 
   public boolean verifeDateRes(LocalDate date)
   {   boolean b = false ;
       LocalDate today = LocalDate.now();
       if(!date.equals(null))
       {
       if(date.isEqual(today)||date.isAfter(today))
       {
        b= true;   
       }
       else b= false;}
       else {System.out.print("date");}
       return b;
   }
   
   public void notif(int i, int y)
   {
      
        Image im = new Image("/Icones/icons8-approbation-40.png");  
        Image im1 = new Image("/Icones/icons8-effacer-40.png");         
      Notifications nt = Notifications.create()
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1 && y==1)
      {
          nt.title("Piéces");
          nt.text("Prend des piéces effectué");
          nt.graphic(new ImageView(im));
      }
      else if(i==2&& y==1)
      {
          nt.title("Outillage"); 
          nt.text("fournire outillage effectué");
          nt.graphic(new ImageView(im));
      }
      else if(i==1 && y==2)
      {
          nt.title("Piéces");
          nt.text("Suppresion effectué");
          nt.graphic(new ImageView(im1));
      }
      else if(i==2&& y==2) 
      {
          nt.title("Outillage"); 
          nt.text("Suppresion effectué");
          nt.graphic(new ImageView(im1));
      }
      nt.show();
   }
   
   public boolean confirmation(int i, int y)
  {
      
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-plus-40.png"); 
      Image im1 = new Image("/Icones/icons8-haute-priorité-40.png"); 
      alert.setGraphic(new ImageView(im));
      if(i==1 && y==1)
      {
          alert.setTitle("Piéces");
          alert.setContentText("Prend piéce ?");
          alert.setGraphic(new ImageView(im));
      }
      else if(i==2 && y==1)
      {
          alert.setTitle("Outillage");
          alert.setContentText("Fournire outillage ?");
          alert.setGraphic(new ImageView(im));
      }
      
      if(i==1 && y==2)
      {
          alert.setTitle("Piéces");
          alert.setContentText("Supprimer Prend piéce ?");
          alert.setGraphic(new ImageView(im1));
      }
      else if(i==2 && y==2)
      {
          alert.setTitle("Outillage");
          alert.setContentText("Supprimer Fournire outillage ?");
          alert.setGraphic(new ImageView(im1));
      }
      Optional <ButtonType> action = alert.showAndWait();
      
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }
   
   public void information(int i)
   {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       Image im = new Image("/Icones/icons8-information-40.png"); 
       alert.setGraphic(new ImageView(im));
       if(i==1)
       {
        alert.setTitle("Outillage"); 
        alert.setContentText("verifier la date de restitution");
        dateRest.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));       
        }
       else if(i==2)
       {
         alert.setTitle("Outillage");
         alert.setContentText("quantité non disponible");  
          tfQte.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
       }
       
       else if(i==3 ){
            tfQte_P.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
           alert.setTitle("Piéces");alert.setContentText("quantité non disponible");
       } 
       
       else if(i==4)
       {
          tfQte_P.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
           alert.setTitle("Piéces");alert.setContentText("verifie la quantitée entrée ");  
       }
       
       else if(i==5)
       {
           alert.setTitle("Outillage");alert.setContentText("verifie la quantitée entrée ");  
          tfQte.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));  
       }
        Optional <ButtonType> action = alert.showAndWait();
      
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          alert.close();
      }
   }
   
   public boolean verificationCompOutil()
   {  boolean b = false;
       if(this.verifieChampsOuti())
       {
           if(confirmation(2,1))
           {
              b=true; 
           }
       }
       return b;
   }
   
    public boolean verificationCompPiece()
   {  boolean b = false;
       if(verifeChampsPieces())
       {
           if(confirmation(1,1))
           {
              b=true; 
           }
       }
       return b;
   }
   
}