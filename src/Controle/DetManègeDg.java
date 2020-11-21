/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class DetManègeDg implements Initializable {
 Connection cnx ;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private Label NomManége;
    @FXML
    private Text etat;
    @FXML
    private Text derniere;
    @FXML
    private Text periode;
    @FXML
    private Text prochaine;
    @FXML
    private Text zone;
 public String datee;
    public String per;
     String result;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        getDet();
    }    
    
    public void getDet()
    {
          try {
     
        String sql ="select * from manége where manége.IdManége='"+ManègeDG.numManege+"'";
        String sql2 ="select * from manége,rapportmaintenance where manége.IdManége=rapportmaintenance.IdManege and manége.IdManége='"+ManègeDG.numManege+"'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {  NomManége.setText(rs.getString("Libelle"));
            per=rs.getString("PerMaintenance");
          
             if(rs.getString("Etat").equals("1"))
             {etat.setText("Actif");}
             else {etat.setText("Suspendu");}
             zone.setText(rs.getString("Zone"));
            periode.setText(rs.getString("manége.PerMaintenance"));
        }
        st =cnx.createStatement();
        ResultSet rs2 = st.executeQuery(sql2);
        while(rs2.next())
        {
         datee = rs2.getString("Date");
         result = rs2.getString("Resultat");
        }
         if(datee!=null)
        {
       String sql1="select ADDDATE('"+datee+"',interval '"+per+"' day) as dateMaint";
         st =cnx.createStatement();
         ResultSet rs1 = st.executeQuery(sql1);
         
         derniere.setText(datee);
         while(rs1.next())
         {
            prochaine.setText(rs1.getString("dateMaint"));
            
         }} else {prochaine.setText("-");derniere.setText("-");}
        
        cnx.close();
    } catch (SQLException ex) {
        Logger.getLogger(InfoPersonnel.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
}
