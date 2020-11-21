package Controle;

import Main.ConnexionBDD;

import java.io.IOException;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author user
 */
public class InfoManége implements Initializable {

    @FXML
    private Text nom;
    @FXML
    private Text proche;
    @FXML
    private Text zone;

    public static String n;
    public String datee;
    public String per;
    public static String show;
    public static Pane pane;
    public Connection cnx;
    @FXML
    private Text num;
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getManege();
        
    }  
    public void getManege()
    {
        try {
      
        String sql ="select Libelle,Zone,PerMaintenance from manége where manége.IdManége='"+n+"'";
        String sql2="select Date from manége,rapportmaintenance where manége.IdManége=rapportmaintenance.IdManege and manége.IdManége='"+n+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            per=rs.getString("PerMaintenance");
            num.setText(n);
            nom.setText(rs.getString("Libelle"));
            zone.setText(rs.getString("Zone"));
        }
        st = cnx.createStatement();
        ResultSet rs2 = st.executeQuery(sql2);
        while(rs2.next())
        {
         datee =rs2.getString("Date");   
        }
        if(datee!=null)
        {
        String sql1="select ADDDATE('"+datee+"',interval '"+per+"' day) as dateMaint";
         st =cnx.createStatement();
         ResultSet rs1 = st.executeQuery(sql1);
         while(rs1.next())
         {
            proche.setText(rs1.getString("dateMaint"));  
         }}
        else proche.setText("-");
        cnx.close();
        
    } catch (SQLException ex) {
        Logger.getLogger(InfoPersonnel.class.getName()).log(Level.SEVERE, null, ex);
    }
    }    

    @FXML
    private void show(MouseEvent event) throws IOException {
        
        ListeManège.numManege=num.getText();
        ManègeDG.numManege=num.getText();
    }    
         
    }

