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


public class DetailPersonnel implements Initializable {

    @FXML
    private Label nbEMpLabel;
    @FXML
    private Label nbMaint;
    @FXML
    private Label nbCaissier;
    @FXML
    private Label nbOpérateurs;
    @FXML
    private Label nbSécurité;
    @FXML
    private Label prochFinCont;
    @FXML
    private Label prochMaint;
    @FXML
    private Label nbManSusp;
    public Connection cnx;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            nbMaint.setText(String.valueOf(nbEmpMaintSéc("Maintenance")+"/"+nbEmpTot("Maintenance")));
            nbSécurité.setText(String.valueOf(nbEmpMaintSéc("Sécurité")+"/"+nbEmpTot("Sécurité")));
            nbCaissier.setText(String.valueOf(nbEmpCaissier()+"/"+nbEmpTot("Caissier")));
            nbOpérateurs.setText(String.valueOf(nbEmpOper()+"/"+nbEmpTot("Opérateur")));
            
            prochFinCont.setText(prochFinCont());
            prochMaint.setText(prochMaint());
            nbManSusp.setText(String.valueOf(nbrManSusp()));
        } catch (Exception ex) {
            Logger.getLogger(DetailPersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }  
    
    //retourne le nombre du personnel de sécurité et maintenance présents actuellement
    private int nbEmpMaintSéc(String poste)
    {
        int nb=0;
        
        String sql = "SELECT COUNT(pointage.IdPersonnel) AS nbEmp FROM pointage,personnel,poste "
                + "WHERE Date=DATE(NOW()) AND pointage.IdPersonnel=personnel.NumContrat AND personnel.IdPoste=poste.IdPoste "
                + "AND poste.Libelle='"+poste+"'";
       
        try {
            cnx = new ConnexionBDD().ConnexionDB();
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
    
    //retourne le nombre de caissier présents actuellement
    private int nbEmpCaissier()
    {   
        int nb=0;
        String sql = "SELECT COUNT(DISTINCT gérecaisse.Caissier) AS nbEmp FROM gérecaisse WHERE "
                + "gérecaisse.Date=DATE(NOW()) AND Exist='0'";
                 
       
        try {cnx = new ConnexionBDD().ConnexionDB();
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
    
    //retourne le nombre d'opérateur disponible actuellement
    private int nbEmpOper()
    {
        int nb = 0;
        
        String sql= "SELECT COUNT(géremanége.NumContrat) AS nbOper FROM géremanége WHERE géremanége.Date=DATE(NOW())";
        
        try {
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
                nb = rs.getInt("nbOper");
            cnx.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nb;
    }
    
    //retourne le nombre total d'employés dans un seul poste
    private int nbEmpTot (String poste)
        {
            int nb=0;
            String sql="SELECT COUNT(personnel.NumContrat) AS nbTotEmp "
                    + "FROM personnel,poste "
                    + "WHERE ADDDATE(DateDebCon, INTERVAL Durée MONTH) >= DATE(NOW()) AND personnel.IdPoste=poste.IdPoste AND poste.Libelle='"+poste+"'";
            try {
                cnx = new ConnexionBDD().ConnexionDB();
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
    
    //retourne date de prochaine fin de contrat
    private String prochFinCont()
    {
        String date=null;
        String sql = "SELECT MIN(ADDDATE(DateDebCon,INTERVAL Durée MONTH)) AS dateFin FROM personnel "
                + "WHERE (SELECT(MIN(ADDDATE(DateDebCon,INTERVAL Durée MONTH))))>=DATE(NOW())";
        
        try
        {   
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                date=rs.getString("dateFin");
            }
            cnx.close();
            
        }catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return date;
        
    }
    
    private String prochMaint()
    {
        String date=null;
        String sql = "SELECT MIN(ADDDATE(Date,INTERVAL PerMaintenance DAY)) AS dateMaint FROM manége,rapportmaintenance WHERE IdManége=IdManege";
        
        try
        {   
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                date=rs.getString("dateMaint");
            }
            cnx.close();
        }catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return date;
        
    }
    
    private int nbrManSusp() throws SQLException
    {
        int nb=0;
        String sql="SELECT COUNT(IdManége) AS nbManSusp FROM manége WHERE Etat='0'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            nb=rs.getInt("nbManSusp");
        }
        cnx.close();
        return nb;
    }
}

    

