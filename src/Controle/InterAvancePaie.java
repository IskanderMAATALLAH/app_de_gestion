package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tableau.TabAvPaie;


public class InterAvancePaie implements Initializable {

    @FXML
    private JFXComboBox<String> cbEmp;
    @FXML
    private JFXTextField montant;
    @FXML
    private TableView<TabAvPaie> tabAvPaie;
    @FXML
    private TableColumn<?, ?> numContrat;
    @FXML
    private TableColumn<?, ?> prenom;
    @FXML
    private TableColumn<?, ?> nom;
    TabAvPaie tap = null;
    ObservableList<TabAvPaie> data = FXCollections.observableArrayList();    
    FilteredList<TabAvPaie> filtredData = new FilteredList<>(data,e->true);
    @FXML
    private TableColumn<?, ?> mont;
    @FXML
    private TableColumn<?, ?> date;
    public Connection cnx;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {   
        try {
            listerEmployé();
            
        } catch (SQLException ex) {
            Logger.getLogger(InterAvancePaie.class.getName()).log(Level.SEVERE, null, ex);
        }
        numContrat.setCellValueFactory(new PropertyValueFactory<>("numeroContrat"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        mont.setCellValueFactory(new PropertyValueFactory<>("montant"));
        
        try {
            chargerDonnées(data,tabAvPaie);
        } catch (SQLException ex) {
            Logger.getLogger(InterAvancePaie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void listerEmployé() throws SQLException
    {
        cbEmp.getItems().clear();
        String sql = "SELECT NumContrat,Nom,Prenom FROM personnel WHERE NumContrat NOT IN(SELECT idPersonnel FROM avancepaie WHERE MONTH(Date) = MONTH(DATE(NOW())) AND YEAR(Date) = YEAR(DATE(NOW()))) AND ADDDate(DateDebCon,INTERVAL Durée MONTH) >= DATE(NOW())";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        String idNomPers=null;
        while(rs.next())
        {
            idNomPers=rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom");
            cbEmp.getItems().add(idNomPers);
        }
        cnx.close();
    }
    
    public void chargerDonnées(ObservableList<TabAvPaie> Data,TableView<TabAvPaie> Tab) throws SQLException
    {   
        Data.clear();
        String sql = "SELECT idPersonnel,Nom,Prenom,Montant,Date FROM avancepaie,personnel "
                + "WHERE idPersonnel=NumContrat";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            Data.add(new TabAvPaie(
                    rs.getString("IdPersonnel"),
                    rs.getString("Prenom"),
                    rs.getString("Nom"),
                    rs.getString("Date"),
                    rs.getInt("Montant")
                    
            ));
            Tab.setItems(Data);
        }
        cnx.close();
    }
    
    

    @FXML
    private void btnValider(ActionEvent event) throws SQLException, IOException 
    {
        if(verificationFinale(2))
        {   
        String  nomIdItem[] = new String[2];
        nomIdItem=cbEmp.getSelectionModel().getSelectedItem().split("-");
        
        cnx = new ConnexionBDD().ConnexionDB();
        String sql = "SELECT DATE(NOW()) AS dateNow";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        String dateNow=null;
        while(rs.next())
            dateNow=rs.getString("dateNow");
            
        sql = "INSERT INTO `parcdattractions`.`avancepaie` (`idPersonnel`, `Date`, `Montant`) VALUES (?, ?, ?);";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1,nomIdItem[0]);
        ps.setString(2,dateNow);
        ps.setInt(3,Integer.valueOf(montant.getText()));
        
        ps.executeUpdate();
        cnx.close();
        openAfterClose(event);
        actualiser();
        act(2);
        }
    }

    @FXML
    private void clickTab(MouseEvent event) 
    {
        try
        {   tap=tabAvPaie.getSelectionModel().getSelectedItem();
            cbEmp.setValue(tap.getNumeroContrat()+"-"+tap.getPrenom()+" "+tap.getNom());
            cbEmp.setDisable(true);
            montant.setText(String.valueOf(tap.getMontant()));
        }catch(Exception e)
        {
            
        }
    }

    @FXML
    private void btnModifier(ActionEvent event) throws SQLException 
    {
        if(verificationFinale(3))
        {
        tap=tabAvPaie.getSelectionModel().getSelectedItem();
        String sql ="UPDATE avancepaie SET Montant='"+Integer.valueOf(montant.getText())+"' WHERE idPersonnel='"+tap.getNumeroContrat()+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);
        cnx.close();
        actualiser();
        act(3);
        }
    }

    @FXML
    private void btnSupprimer(ActionEvent event) throws SQLException 
    {
        if(verificationFinale(1))
        {
        tap=tabAvPaie.getSelectionModel().getSelectedItem();
        String sql ="DELETE FROM avancepaie WHERE idPersonnel='"+tap.getNumeroContrat()+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);
        cnx.close();
        actualiser();
        act(1);
        }
    }
    
    private void actualiser() throws SQLException
    {
        cbEmp.setDisable(false);
        cbEmp.setValue("");
        montant.setText("");
        chargerDonnées(data,tabAvPaie);
        listerEmployé();
    }

    @FXML
    private void cherchEmp(KeyEvent event) throws SQLException 
    {
        try{
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                + "WHERE NumContrat NOT IN(SELECT idPersonnel FROM avancepaie WHERE MONTH(Date) = MONTH(DATE(NOW())) AND YEAR(Date) = YEAR(DATE(NOW())) AND (Nom LIKE '%"+cbEmp.getValue()+"%' OR Prenom LIKE '%"+cbEmp.getValue()+"%')) AND ADDDate(DateDebCon,INTERVAL Durée MONTH) >= DATE(NOW())";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        cbEmp.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            cbEmp.setVisibleRowCount(i);
            cbEmp.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            cbEmp.show();
        }
        cnx.close();
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        
    }
     
    public void openAfterClose(ActionEvent event) throws IOException
     {
      Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      stage.close();  
     } 
            
     public boolean verificationChamps(int i)
     {
          boolean b = true;
         Paint value0=Paint.valueOf("F9C7CC");

         if(cbEmp.getSelectionModel().isEmpty() && i==2)
        {   
        cbEmp.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
        b=false;
        }
        if(montant.getText().equals(""))
        {
        montant.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
        b=false;
        } 
       return b;
     }
     
       public boolean confirmation(int i)
  {
     Image im = new Image("/Icones/icons8-plus-40.png");
      Image im1 = new Image("/Icones/icons8-effacer-40.png");
      Image im2 = new Image("/Icones/icons8-editer-le-fichier-48.png");
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      if(i==1)
      {
          alert.setGraphic(new ImageView(im1));
          alert.setTitle("Avance paie");
          alert.setContentText("Supprimer montant ?");
      }
      else if(i==2)
      {
          alert.setGraphic(new ImageView(im));
          alert.setTitle("Avance paie");
          alert.setContentText("Ajouter montant ?");
      }
      else if(i==3)
      {
          alert.setGraphic(new ImageView(im2));
          alert.setTitle("Avance paie");
          alert.setContentText("Modifier montant ?");
      }
 
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
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      if(i==1) {nt.title("Avance paie"); nt.text(" suppression terminée");}
      else if(i==2){nt.title("Avance paie"); nt.text("Ajout effectué");}
      else if(i==3) {nt.title("Avance paie"); nt.text(" Modification terminée");}
      nt.show();

  }
               
    public boolean verificationFinale(int i)
    {
        boolean b = false;
        if(verificationChamps(i))
        {
            if(confirmation(i))
            {
                b=true;
            }
        }
        return b;
    }

    @FXML
    private void fermer(MouseEvent event) {
        InterDepenses.stage_avPaie.close();
    }

    @FXML
    private void Mont(KeyEvent event) {
    }
}
