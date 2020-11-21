/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import static Controle.ListePersonnel.num;
import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author user
 */
public class SuiviPersonnel implements Initializable {
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
    private ScrollPane listePersonnel;
    @FXML
    private VBox vBox;
    @FXML
    private Pane panee;
     public static String num;
    @FXML
    private ImageView ImagePers;
    @FXML
    private Label NomPrenom;
    @FXML
    private Text numCont;
    @FXML
    private Text dateNai;
    @FXML
    private Text adresse;
    @FXML
    private Text NumTel;
    @FXML
    private Text poste;
    @FXML
    private Text DateDebCont;
    @FXML
    private Text Duree;
    @FXML
    private Text DateFinCont;
    @FXML
    private Text Salaire;
    private JFXComboBox<String> choixEmpRech;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            listerPersonnel();
            affPersonnel();
            affPoste();
        } catch (IOException ex) {
            Logger.getLogger(SuiviPersonnel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SuiviPersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void listerPersonnel() throws IOException
    {
          String sql="SELECT NumContrat FROM personnel";   
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
    private void rechAction(ActionEvent event) throws SQLException, IOException {
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
          
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"' ";
            
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
          
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"' ";
            
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
          
            sql = "SELECT NumContrat,Prenom,Nom FROM personnel "
                    + "WHERE ADDDATE(DateDebCon,INTERVAL Durée MONTH)>='"+new AjoutModifPersonnel().changeFormatDate(dateAu1)+"' "
                    + "AND ADDDATE(DateDebCon,INTERVAL Durée MONTH)<='"+new AjoutModifPersonnel().changeFormatDate(dateAu2)+"' ";
            
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

    @FXML
    private void actualiserAction(ActionEvent event) throws IOException {
        choixEmp.setValue(null);
        choixPoste.setValue(null);
        dateAu1.setValue(null);
        dateAu2.setValue(null);
        listerPersonnel();
        
    }

    @FXML
    private void clickeed(MouseEvent event) throws IOException {
     
     Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/DetPersonnelDg.fxml"));
     panee.getChildren().clear();
     panee.getChildren().add(root);   
    }
    
  private void affPersonnel() throws SQLException
    {
        String sql = "SELECT NumContrat,Prenom,Nom FROM personnel,poste  WHERE personnel.IdPoste=poste.IdPoste";
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
        String sql = "SELECT IdPoste,Libelle FROM poste";
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        choixPoste.getItems().clear();
        while(rs.next())
            choixPoste.getItems().add(rs.getString("IdPoste")+"-"+rs.getString("Libelle"));
        cnx.close();
    }

    @FXML
    private void cherchEmp(KeyEvent event) throws SQLException 
    {
        String sql = "SELECT NumContrat, Nom, Prenom "
                + "FROM personnel,poste "
                + "WHERE (personnel.IdPoste=poste.IdPoste) AND (Nom LIKE '%"+choixEmp.getValue()+"%' OR Prenom LIKE '%"+choixEmp.getValue()+"%')";
        
        
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int i=0;
        choixEmp.getItems().clear();
        while (rs.next())
        {   
            i++;
            choixEmp.setVisibleRowCount(i);
            choixEmp.getItems().add(rs.getString("NumContrat")+"-"+rs.getString("Nom")+" "+rs.getString("Prenom"));
            choixEmp.show();
        }
        cnx.close();
    }



} 

