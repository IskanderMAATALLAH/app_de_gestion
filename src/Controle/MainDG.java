/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class MainDG implements Initializable {
    Connection cnx =new ConnexionBDD().ConnexionDB();
    @FXML
    private Pane BigPane;
    @FXML
    private VBox pane3;
    @FXML
    private Pane PaneDep;
    @FXML
    private Pane PaneLittle;
    @FXML
    private Label admin;
    @FXML
    private Label maint;
    @FXML
    private Label Opera;
    @FXML
    private Label caissier;
    @FXML
    private Label secur;
    @FXML
    private Label RdvTot;
    @FXML
    private Label DateProch;
    @FXML
    private Label Tot;
    @FXML
    private HBox hBoxEntete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            hBoxEntete.setStyle("-fx-background-color: linear-gradient( #454960, #EAEDF1);");
            maint.setText(String.valueOf(nbEmp("Maintenance")+"/"+nbEmpTot("Maintenance")));
            secur.setText(String.valueOf(nbEmp("Sécurité")+"/"+nbEmpTot("Sécurité")));
            caissier.setText(String.valueOf(nbEmp("Caissier")+"/"+nbEmpTot("Caissier")));
            Opera.setText(String.valueOf(nbEmp("Operateur")+"/"+nbEmpTot("Opérateur")));
            admin.setText(String.valueOf(nbEmp("Administration")+"/"+nbEmpTot("Administration")));
            int nbr = nbEmp("Maintenance")+nbEmp("Sécurité")+nbEmp("Caissier")+nbEmp("Operateur")+nbEmp("Administration");
            int tot =nbEmpTot("Maintenance")+nbEmpTot("Sécurité")+nbEmpTot("Caissier")+nbEmpTot("Opérateur")+nbEmpTot("Administration");
             Tot.setText(String.valueOf(nbr+"/"+tot));
             info();
    }    

     @FXML
    private void add(ActionEvent event) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/MainDG.fxml"));
            BigPane.getChildren().clear();
            BigPane.getChildren().add(root);
    }

    @FXML
    private void Personnel(ActionEvent event) throws IOException {
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/EnTetePersonnel.fxml"));
        PaneDep.getChildren().clear();
      PaneDep.getChildren().add(root);
    }

    @FXML
    private void sertech(ActionEvent event) throws IOException {
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/EnTeteSerTech.fxml"));
      BigPane.getChildren().clear();
       BigPane.getChildren().add(root); 
    }

    @FXML
    private void Finance(ActionEvent event) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/EnteteFinance.fxml"));
        BigPane.getChildren().clear();
        BigPane.getChildren().add(root);   
    }

    @FXML
    private void ouvrirComptes(ActionEvent event) throws IOException {
             Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/Comptes.fxml"));
      PaneLittle.getChildren().clear();
      PaneLittle.getChildren().add(root);
    }

    @FXML
    private void DocsetRDV(ActionEvent event) throws IOException {
      Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/ConsulteRDVetDoc.fxml"));
      PaneLittle.getChildren().clear();
      PaneLittle.getChildren().add(root);
    }

    @FXML
    private void MissionCongé(ActionEvent event) throws IOException {
       Pane root = FXMLLoader.load(getClass().getResource("/Interfaces/MissionEtCongé.fxml"));
      PaneLittle.getChildren().clear();
      PaneLittle.getChildren().add(root);
    }

    @FXML
    private void change(MouseEvent event) throws IOException {
     
      
    }

    @FXML
    private void sendtounder(MouseEvent event) throws IOException {
      
        
    }
    private int nbEmpTot (String poste)
        {
            int nb=0;
            String sql1="SELECT COUNT(personnel.NumContrat) AS nbTotEmp FROM personnel,poste WHERE personnel.IdPoste=poste.IdPoste AND poste.Libelle='"+poste+"'";
            String sql2="SELECT COUNT(personnel.NumContrat) AS nbTotEmp FROM personnel,poste WHERE personnel.IdPoste=poste.IdPoste AND poste.Libelle!='Caissier' AND poste.Libelle!='Opérateur' AND poste.Libelle!='Maintenance' AND poste.Libelle!='Sécurité'";
            String sql=null;
            if(poste.equals("Administration")) sql=sql2;
            else sql=sql1;

            try {
                cnx =new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next())
                {
                    nb=rs.getInt("nbTotEmp");
                }
                cnx.close();
            } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
            return nb;
        }
     private int nbEmp(String poste)
    {
        int nb=0;

        String sql1 = "SELECT COUNT(pointage.IdPersonnel) AS nbEmp FROM pointage,personnel,poste WHERE pointage.Date=DATE(NOW()) AND pointage.IdPersonnel=personnel.NumContrat AND personnel.IdPoste=poste.IdPoste AND poste.Libelle='"+poste+"'";
        String sql2 = "SELECT COUNT(DISTINCT gérecaisse.Caissier) AS nbEmp FROM gérecaisse WHERE Date=DATE(NOW())"; 
        String sql3 = "SELECT COUNT(assistantmanege.NumContrat) AS nbEmp FROM assistantmanege WHERE date=DATE(NOW())";
        String sql4 = "SELECT COUNT(pointage.IdPersonnel) AS nbEmp FROM pointage,personnel,poste WHERE pointage.Date=DATE(NOW()) AND pointage.IdPersonnel=personnel.NumContrat AND personnel.IdPoste=poste.IdPoste AND poste.Libelle!='Securité' and poste.Libelle!='Maintenance'" ;
        String sql=null;
        if(poste.equals("Caissier")) sql=sql2; 
        else if(poste.equals("Operateur")) sql =sql3;
        else if(poste.equals("Maintenance") || poste.equals("Securité")) sql=sql1;
        else sql=sql4;
        
        try {
            cnx =new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {   
                nb=rs.getInt("nbEmp");
            }
            cnx.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
      
      return nb;
    }
      private void info()
    {
        try {
        String nbr_rdv = "SELECT count(*) as nbr FROM `rdv` where Date > CURRENT_TIMESTAMP()";
        String proch_date="select Min(Date) as proche from rdv where Date > CURRENT_TIMESTAMP()";
        cnx=new ConnexionBDD().ConnexionDB();
        PreparedStatement ps = cnx.prepareStatement(nbr_rdv);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String result = rs.getString("nbr");
            RdvTot.setText(result);
        }
          ps = cnx.prepareStatement(proch_date);
        rs = ps.executeQuery();
        while(rs.next())
        {
            String result = rs.getString("proche");
            DateProch.setText(result);
        }
        cnx.close();
    } catch (SQLException ex) {
        Logger.getLogger(SecretaireController.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
}
