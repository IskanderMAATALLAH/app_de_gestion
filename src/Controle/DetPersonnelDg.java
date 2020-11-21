/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class DetPersonnelDg implements Initializable {
  public Connection cnx;
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
       
            String datee;
            String per;
            public static String num;
    @FXML
    private ImageView ImagePers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
        String sql ="select * from personnel,poste where NumContrat='"+SuiviPersonnel.num+"' AND personnel.IdPoste=poste.IdPoste";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        { 
            datee=rs.getString("DateDebCon");
            per=rs.getString("Durée");
            NomPrenom.setText(rs.getString("Nom")+"  "+rs.getString("Prenom"));
            numCont.setText(ListePersonnel.num);
            dateNai.setText(rs.getString("DateNaiss"));
            adresse.setText(rs.getString("Adresse"));
            NumTel.setText(rs.getString("Téléphone"));
            poste.setText(rs.getString("Libelle"));
            DateDebCont.setText(rs.getString("DateDebCon"));
            Duree.setText(rs.getString("Durée"));
            Salaire.setText(rs.getString("Salaire"));
            if(!rs.getString("image").equals(""))
            {
            Image image = new Image(rs.getString("image"));
            ImagePers.setImage(image);}
        }
        String sql1="select ADDDATE('"+datee+"',interval '"+per+"' Month) as dateFin";
         st =cnx.createStatement();
         ResultSet rs1 = st.executeQuery(sql1);
         while(rs1.next())
         {
            DateFinCont.setText(rs1.getString("dateFin"));  
         }
         cnx.close();
  
        
    } catch (SQLException ex) {
        Logger.getLogger(InfoPersonnel.class.getName()).log(Level.SEVERE, null, ex);
    }
    }    
    
}
