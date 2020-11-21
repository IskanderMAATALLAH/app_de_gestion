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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class InfoCaisseDG implements Initializable {
Connection cnx ;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private Text Id;
    @FXML
    private Circle OnOff;
    @FXML
    private Text Libelle;
    @FXML
    private Text type;
String Etat;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    try {
        getInfo();
    } catch (SQLException ex) {
        Logger.getLogger(InfoCaisseDG.class.getName()).log(Level.SEVERE, null, ex);
    }
    }    

         public void getInfo() throws SQLException
    {
        String sql="select * from caisse where IdCaisse='"+Caisses.IdCaisse+"'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            Id.setText(rs.getString("IdCaisse"));
            Libelle.setText(rs.getString("Libelle"));
            type.setText(rs.getString("TypeCaisse"));
             Etat=rs.getString("Etat");
             if(Etat.equals("1")) OnOff.setFill(Color.GREEN);
             else if(Etat.equals("0"))OnOff.setFill(Color.RED);
             else if(Etat.equals("2"))OnOff.setFill(Color.ORANGE);
    }
    cnx.close();
    }
    
    
}
