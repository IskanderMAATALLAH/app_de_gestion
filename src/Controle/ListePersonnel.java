package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ListePersonnel implements Initializable {
    public static String num;
    public static Stage stage_nvlEmp =null;
    public static Scene scene_nvlEmp =null;
    
    @FXML
    private ScrollPane listePersonnel;
    @FXML
    private VBox vBox;
    @FXML
    private Pane panee;
    public Connection cnx;
    @FXML
    private JFXComboBox<String> choixEmp;
    @FXML
    private JFXComboBox<String> choixPoste;
    @FXML
    private JFXDatePicker dateAu1;
    @FXML
    private JFXDatePicker dateAu2;
    @FXML
    private VBox vBoxInfo;
    public static int choixDep=0;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try
        {
            listerPersonnel();
            affPersonnel();
            affPoste();
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void listerPersonnel() throws IOException
    {
        String SqlAgent = "SELECT NumContrat FROM personnel,poste WHERE personnel.IdPoste=poste.IdPoste AND (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur')";        
        String sql="SELECT NumContrat FROM personnel,poste WHERE personnel.IdPoste=poste.IdPoste AND (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur')";
         if(choixDep==1) sql = SqlAgent;
        try {
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {  
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    @FXML
    private void nouvelEmployé(ActionEvent event) throws IOException {
        AjoutModifPersonnel.ModAjout=0;
        stage_nvlEmp = new Stage();
        stage_nvlEmp.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/ajoutModifPersonnel.fxml"));
        Scene scene_nvlEmp = new Scene(root);
        stage_nvlEmp.setScene(scene_nvlEmp);
        stage_nvlEmp.show();
    }
    
    @FXML
    private void clickeed(MouseEvent event) throws IOException {
     Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/DetPersonnel.fxml"));
     panee.getChildren().clear();
     panee.getChildren().add(root);

    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException, IOException 
    {   
        String tab[] = new String[2];
        String tab2[] = new String[2];
        String sql=null;
        
        if(choixEmp.getValue()!=null && choixPoste.getValue()==null && dateAu1.getValue()==null && dateAu2.getValue()==null)
        {   
            tab=choixEmp.getValue().split("-");
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel WHERE NumContrat='"+tab[0]+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()!=null && dateAu1.getValue()==null && dateAu2.getValue()==null)
        {   
            tab=choixEmp.getValue().split("-");
            tab2=choixPoste.getValue().split("-");
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' AND IdPoste='"+tab2[0]+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()!=null && dateAu1.getValue()!=null && dateAu2.getValue()==null)
        {   
            tab=choixEmp.getValue().split("-");
            tab2=choixPoste.getValue().split("-");
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' AND IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }
        else if(choixEmp.getValue()!=null && choixPoste.getValue()!=null && dateAu1.getValue()!=null && dateAu2.getValue()==null)
        {   
            tab=choixEmp.getValue().split("-");
            tab2=choixPoste.getValue().split("-");
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' AND IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()!=null && dateAu1.getValue()==null && dateAu2.getValue()!=null)
        {   
            tab=choixEmp.getValue().split("-");
            tab2=choixPoste.getValue().split("-");
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' AND IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()==null && dateAu1.getValue()!=null && dateAu2.getValue()!=null)
        {   
            tab=choixEmp.getValue().split("-");
           
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'"
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()==null && dateAu1.getValue()!=null && dateAu2.getValue()==null)
        {   
            tab=choixEmp.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'"
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()!=null && choixPoste.getValue()==null && dateAu1.getValue()==null && dateAu2.getValue()!=null)
        {   
            tab=choixEmp.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE NumContrat='"+tab[0]+"'"
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()!=null && dateAu1.getValue()==null && dateAu2.getValue()==null)
        {   
            tab2=choixPoste.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE IdPoste='"+tab2[0]+"' ";
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()!=null && dateAu1.getValue()==null && dateAu2.getValue()==null)
        {   
            tab2=choixPoste.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'";;
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()!=null && dateAu1.getValue()==null && dateAu2.getValue()!=null)
        {   
            tab2=choixPoste.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";;
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()!=null && dateAu1.getValue()!=null && dateAu2.getValue()!=null)
        {   
            tab2=choixPoste.getValue().split("-");
            
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE IdPoste='"+tab2[0]+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()==null && dateAu1.getValue()!=null && dateAu2.getValue()==null)
        {   
            String SqlAdmin = "SELECT NumContrat,Prenom,Nom, Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur')"                    
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'";
            
            sql = "SELECT NumContrat,Prenom,Nom ,Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur')"
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"'";
            if(choixDep==1)
                sql=SqlAdmin;
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()==null && dateAu1.getValue()==null && dateAu2.getValue()!=null)
        {   
            String SqlAdmin = "SELECT NumContrat,Prenom,Nom ,Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur')"                    
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"' ";
            
            sql = "SELECT NumContrat,Prenom,Nom, Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur')"                
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"' ";
            if(choixDep==1)
                sql=SqlAdmin;
            
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }else if(choixEmp.getValue()==null && choixPoste.getValue()==null && dateAu1.getValue()!=null && dateAu2.getValue()!=null)
        {   
            String SqlAdmin = "SELECT NumContrat,Prenom,Nom , Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur')"                    
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"' "
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";
             
            sql = "SELECT NumContrat,Prenom,Nom,Libelle FROM personnel,poste "
                    + "WHERE personnel.IdPoste =poste.IdPoste"
                    + " AND (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur')"                    
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"' "
                    + " AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"'";

              if(choixDep==1)
                  sql=SqlAdmin;
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vBox.getChildren().clear();
            while (rs.next())
            {
                InfoPersonnel.n=rs.getString("NumContrat");
                Pane pane = FXMLLoader.load(getClass().getResource("/Interfaces/infoPersonnel.fxml"));
                vBox.getChildren().add(pane);
            }
            cnx.close();
        }  

    }
    
    
    private void actualiser() throws IOException, SQLException
    {
        vBox.getChildren().clear();
        listerPersonnel();
        choixEmp.setValue(null);
        choixPoste.setValue(null);
        dateAu1.setValue(null);
        dateAu2.setValue(null);
        affPersonnel();
        affPoste();
    }

    @FXML
    private void actualiserAction(ActionEvent event) throws IOException, SQLException 
    {
        actualiser();
    }
    
    private void affPersonnel() throws SQLException
    {
        String sqlADmin= "SELECT NumContrat,Prenom,Nom FROM personnel,poste  WHERE personnel.IdPoste=poste.IdPoste AND (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur') ";
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste  WHERE personnel.IdPoste=poste.IdPoste AND (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur') ";
        if(choixDep==1) sql =sqlADmin;
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixEmp.getItems().clear();
        while(rs.next())
            choixEmp.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Prenom")+" "+rs.getString("Nom"));
        cnx.close();
    }
    
    private void affPoste() throws SQLException
    {
        String sqlAdmin="SELECT IdPoste,Libelle FROM poste WHERE (Libelle!='Maintenance' and Libelle!='Sécurité' and Libelle!='Caissier' and Libelle!='Opérateur') ";
        String sql = "SELECT IdPoste,Libelle FROM poste WHERE (Libelle='Maintenance' or Libelle='Sécurité' or Libelle='Caissier' or Libelle='Opérateur') ";
        if(choixDep==1) sql=sqlAdmin;
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixPoste.getItems().clear();
        while(rs.next())
            choixPoste.getItems().add(rs.getString("IdPoste")+"-"+rs.getString("Libelle"));
        cnx.close();
    }

    @FXML
    private void cherchEmp(KeyEvent event) {
    }

    @FXML
    private void cherchPoste(KeyEvent event) {
    }

    @FXML
    private void eventDateDu(KeyEvent event) {
    }

    @FXML
    private void eventDateAu(KeyEvent event) {
    }
}
    

