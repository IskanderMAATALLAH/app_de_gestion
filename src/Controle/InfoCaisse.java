/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import static Controle.InfoManége.n;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class InfoCaisse implements Initializable {
 Connection cnx =new ConnexionBDD().ConnexionDB();
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private Text Id;
    @FXML
    private Text Libelle;
    @FXML
    private Text nom;
    @FXML
    private Text type;
   public static String Etat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     try {
         getInfo();
     } catch (SQLException ex) {
         Logger.getLogger(InfoCaisse.class.getName()).log(Level.SEVERE, null, ex);
     }
    }    

    @FXML
    private void show(MouseEvent event) {
        Caisses.IdCaisse=Id.getText();
        Caisses.Libelle=Libelle.getText();
        Caisses.Type=type.getText();
    }
    public void getInfo() throws SQLException
    {
        String sql="select * from caisse where IdCaisse='"+Caisses.IdCaisse+"'";
        
        cnx =new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            Id.setText(rs.getString("IdCaisse"));
            Libelle.setText(rs.getString("Libelle"));
            type.setText(rs.getString("TypeCaisse"));
             Etat=rs.getString("Etat");
             System.out.print(Etat);
        }
        cnx.close();
    }
}
