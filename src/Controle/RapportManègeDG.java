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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class RapportManègeDG implements Initializable {
Connection cnx;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private Label NomManége;
    @FXML
    private Text Date;
    @FXML
    private Text Agent;
    @FXML
    private Text Nature;
    @FXML
    private Text PDR;
    @FXML
    private Text Carb;
    @FXML
    private Text horaires;
    @FXML
    private ImageView next;
    @FXML
    private ImageView Previous;
    
    public static String suiv=null ;
     public static  String prece=null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(ManègeDG.numManege!=null)
        {
    try {
        getrapport();
    } catch (SQLException ex) {
        Logger.getLogger(RapportManègeDG.class.getName()).log(Level.SEVERE, null, ex);
    }}
    }    
    
    public void getrapport() throws SQLException
    {
          String sql ="select Libelle,Date,Nom,Prenom,Nature,PDR,Carb,Horaires from manége,rapportmaintenance,personnel where manége.IdManége=rapportmaintenance.IdManege and Agent=NumContrat and manége.IdManége='"+ManègeDG.numManege+"'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {  
         NomManége.setText(rs.getString("Libelle"));
         Date.setText(rs.getString("Date"));
         Agent.setText(rs.getString("Nom")+" "+rs.getString("Prenom"));
         Nature.setText(rs.getString("Nature"));
         PDR.setText(rs.getString("PDR"));
         Carb.setText(rs.getString("Carb"));
         horaires.setText(rs.getString("Horaires"));
        }
       getnextday();
       getpreviousday();
       cnx.close();
       
    }

    @FXML
    private void suiv(MouseEvent event) throws SQLException {
        
         String sql ="select Libelle,Date,Nom,Prenom,Nature,PDR,Carb,Horaires from manége,rapportmaintenance,personnel where manége.IdManége=rapportmaintenance.IdManege and Agent=NumContrat and manége.IdManége='"+ManègeDG.numManege+"' and Date ='"+suiv+"'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {  
         NomManége.setText(rs.getString("Libelle"));
         Date.setText(rs.getString("Date"));
         Agent.setText(rs.getString("Nom")+" "+rs.getString("Prenom"));
         Nature.setText(rs.getString("Nature"));
         PDR.setText(rs.getString("PDR"));
         Carb.setText(rs.getString("Carb"));
         horaires.setText(rs.getString("Horaires"));
        }
        this.suiv=null;
        this.prece=null; 
      getnextday();
      getpreviousday();
      cnx.close();
    }

    @FXML
    private void prec(MouseEvent event) throws SQLException {
         String sql ="select Libelle,Date,Nom,Prenom,Nature,PDR,Carb,Horaires from manége,rapportmaintenance,personnel where manége.IdManége=rapportmaintenance.IdManege and Agent=NumContrat and manége.IdManége='"+ManègeDG.numManege+"' and Date ='"+prece+"'";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {  
         NomManége.setText(rs.getString("Libelle"));
         Date.setText(rs.getString("Date"));
         Agent.setText(rs.getString("Nom")+" "+rs.getString("Prenom"));
         Nature.setText(rs.getString("Nature"));
         PDR.setText(rs.getString("PDR"));
         Carb.setText(rs.getString("Carb"));
         horaires.setText(rs.getString("Horaires"));
        }
        this.suiv=null;
        this.prece=null; 
      getnextday();
      getpreviousday();
      cnx.close();
    }
    
    public void getnextday() throws SQLException
    {
         String nextDate=null;
        String da ="SELECT Min(Date) as Date FROM `rapportmaintenance` where Date >'"+Date.getText()+"' and IdManege='"+ManègeDG.numManege+"' ";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs1 = st.executeQuery(da);
        while(rs1.next())
        {
          nextDate=rs1.getString("date");
        }
       
        String sql1 ="select Date from manége,rapportmaintenance where manége.IdManége=rapportmaintenance.IdManege and Date='"+nextDate+"' and manége.IdManége='"+ManègeDG.numManege+"'";
         st =cnx.createStatement();
        ResultSet rs2 = st.executeQuery(sql1);
        while(rs2.next())
        {
            suiv=rs2.getString("Date");
        }
        cnx.close();
        if(suiv==null)
        {
            next.setVisible(false);
        }
        else next.setVisible(true);
    }  
    public void getpreviousday() throws SQLException
    {
           String prevDate=null;
        String da ="SELECT Max(Date) as Date FROM `rapportmaintenance` where Date <'"+Date.getText()+"' and IdManege='"+ManègeDG.numManege+"' ";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st =cnx.createStatement();
        ResultSet rs1 = st.executeQuery(da);
        while(rs1.next())
        {
          prevDate=rs1.getString("date");
        }
       
        String sql1 ="select Date from manége,rapportmaintenance where manége.IdManége=rapportmaintenance.IdManege and Date='"+prevDate+"' and manége.IdManége='"+ManègeDG.numManege+"'";
         st =cnx.createStatement();
        ResultSet rs2 = st.executeQuery(sql1);
        while(rs2.next())
        {
            prece=rs2.getString("Date");
        }
        cnx.close();
        if(prece==null)
        {
            Previous.setVisible(false);
            
        }else Previous.setVisible(true);
    }
}
