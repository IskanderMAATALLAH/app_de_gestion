package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;


public class GraphiqueRevenus implements Initializable {

    Connection cnx=null;
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private JFXButton btnEtablir;
    @FXML
    private JFXComboBox<String> dateDu;
    @FXML
    private JFXComboBox<String> dateAu;
    @FXML
    private LineChart<?, ?> lineChart_1;
    @FXML
    private NumberAxis y1;
    @FXML
    private CategoryAxis x1;
    @FXML
    private JFXButton btnEtablir_1;
    @FXML
    private JFXComboBox<String> duMois;
    @FXML
    private JFXComboBox<String> auMois;
    @FXML
    private JFXButton btnReset;
    @FXML
    private JFXButton btnReset_1;
    @FXML
    private JFXButton btnEtablir_11;
    @FXML
    private JFXButton btnReset_11;
    @FXML
    private LineChart<?, ?> lineChart_Client;
    @FXML
    private NumberAxis y11;
    @FXML
    private CategoryAxis x11;
    @FXML
    private JFXComboBox<?> dateDu_Client;
    @FXML
    private JFXComboBox<?> dateAu_Client;
    @FXML
    private JFXComboBox<String> duMois_client;
    @FXML
    private JFXComboBox<String> auMois_Client;
    @FXML
    private JFXButton btnEtablir_111;
    @FXML
    private JFXButton btnReset_111;
    @FXML
    private LineChart<?, ?> lineChartMois_Client;
    @FXML
    private NumberAxis y111;
    @FXML
    private CategoryAxis x111;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        try {
            listerMois(duMois,auMois);
            listerDate(dateDu,dateAu);
            listerDate(dateDu_Client,dateAu_Client);
            listerMois(duMois_client,auMois_Client);
            initLineChart();
            initLineChart_1();
            initLineChart_Client();
            initLineChartMois_Client();
        } catch (SQLException ex) {
            Logger.getLogger(GraphiqueRevenus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
    
    @FXML
    private void initLineChart()
    {   
        dateDu.setValue(null);
        dateAu.setValue(null);
        String sql = "SELECT Date,SUM(Recette) AS somme "
                + "FROM(SELECT Date,IdCaisse,Recette FROM parcdattractions.gérecaisse "
                + "WHERE MONTH(Date)=MONTH(DATE(NOW())) AND YEAR(Date)=YEAR(DATE(NOW())) "
                + "GROUP BY Caissier,Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
        XYChart.Series series = new XYChart.Series();
        
        Statement st;
        try {
            cnx=new ConnexionBDD().ConnexionDB();
            st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            lineChart.getData().clear();
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("Date"),rs.getInt("somme")));
            }
        lineChart.getData().addAll(series);
        cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(GraphiqueRevenus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    @FXML
    private void etablirGraph(ActionEvent event) throws SQLException 
    {
        lineChart.getData().clear();
        String sql = "SELECT Date,SUM(Recette) AS somme "
                + "FROM(SELECT Date,IdCaisse,Recette FROM parcdattractions.gérecaisse "
                + "WHERE Date>='"+dateDu.getValue()+"' AND Date<='"+dateAu.getValue()+"' "
                + "GROUP BY Caissier,Date,IdCaisse,Recette) AS subQuery GROUP BY Date ";
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("Date"),rs.getInt("somme")));
            }
        lineChart.getData().addAll(series);
        
        
    }
    
    private void listerDate(JFXComboBox jcb1,JFXComboBox jcb2) throws SQLException
    {
        String sql = "SELECT Date FROM journée";
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                jcb1.getItems().add(rs.getString("Date"));
                jcb2.getItems().add(rs.getString("Date"));
            }
        cnx.close();
    }
    
    private void listerMois(JFXComboBox jcb1,JFXComboBox jcb2) throws SQLException
    {
        
        
        for(int i=0;i<12;i++)
            {
                jcb1.getItems().add("1-Janvier");
                jcb2.getItems().add("1-Janvier");
                
                jcb1.getItems().add("2-Fevrier");
                jcb2.getItems().add("2-Fevrier");
                
                jcb1.getItems().add("3-Mars");
                jcb2.getItems().add("3-Mars");
                
                jcb1.getItems().add("4-Avril");
                jcb2.getItems().add("4-Avril");
                
                jcb1.getItems().add("5-Mai");
                jcb2.getItems().add("5-Mai");
                
                jcb1.getItems().add("6-Juin");
                jcb2.getItems().add("6-Juin");
                
                jcb1.getItems().add("7-Juillet");
                jcb2.getItems().add("7-Juillet");
                
                jcb1.getItems().add("8-Aout");
                jcb2.getItems().add("8-Aout");
                
                jcb1.getItems().add("9-Septembre");
                jcb2.getItems().add("9-Septembre");
                
                jcb1.getItems().add("10-Octobre");
                jcb2.getItems().add("10-Octobre");
                
                jcb1.getItems().add("11-Novembre");
                jcb2.getItems().add("11-Novembre");
                
                jcb1.getItems().add("12-Decembre");
                jcb2.getItems().add("12-Decembre");
                
            }
        
    }
    
    @FXML
    private void initLineChart_1() throws SQLException
    {   
        duMois.setValue(null);
        auMois.setValue(null);
        lineChart_1.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        if(duMois.getValue()!=null && auMois.getValue()!=null)
        {
            tab=duMois.getValue().split("-");
            tab2=auMois.getValue().split("-");
        }
        String sql = "SELECT MONTH(Date) AS mois,SUM(Recette) AS somme "
                + "FROM(SELECT Date,IdCaisse,Recette FROM parcdattractions.gérecaisse "
                + "WHERE MONTH(Date)>=(MONTH(DATE(NOW()))-1) AND MONTH(Date)<=MONTH(DATE(NOW())) "
                + "GROUP BY Caissier,Date,IdCaisse,Recette) AS subQuery GROUP BY MONTH(Date) ";
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("mois"),rs.getInt("somme")));
            }
        lineChart_1.getData().addAll(series);
        cnx.close();
    }
    
    @FXML
    private void etablirGraph_1(ActionEvent event) throws SQLException {
        lineChart_1.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        if(duMois.getValue()!=null && auMois.getValue()!=null)
        {
            tab=duMois.getValue().split("-");
            tab2=auMois.getValue().split("-");
        }
        String sql = "SELECT MONTH(Date) AS mois,SUM(Recette) AS somme "
                + "FROM(SELECT Date,IdCaisse,Recette FROM parcdattractions.gérecaisse "
                + "WHERE MONTH(Date)>='"+Integer.valueOf(tab[0])+"' AND MONTH(Date)<='"+Integer.valueOf(tab2[0])+"' "
                + "GROUP BY Caissier,Date,IdCaisse,Recette) AS subQuery GROUP BY MONTH(Date) ";
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("mois"),rs.getInt("somme")));
            }
        lineChart_1.getData().addAll(series);
        cnx.close();
       
    }

    @FXML
    private void etablirGraph_Client(ActionEvent event) throws SQLException {
        
        lineChart_Client.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        
        String sql = "SELECT Date,SUM(NbrTicketDonné) AS somme "
                + "FROM gérecaisse WHERE CodeTicket='9' AND MONTH(Date)=MONTH(DATE(NOW())) AND Date>='"+dateDu_Client.getValue()+"' AND Date<='"+dateAu_Client.getValue()+"' "
                + "GROUP BY Date";
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("Date"),rs.getInt("somme")));
            }
        lineChart_Client.getData().addAll(series);
        cnx.close();
    }

    @FXML
    private void initLineChart_Client() throws SQLException {
        dateDu_Client.setValue(null);
        dateAu_Client.setValue(null);
        lineChart_Client.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        
        String sql = "SELECT Date,SUM(NbrTicketDonné) AS somme FROM gérecaisse WHERE CodeTicket='9' AND MONTH(Date)=MONTH(DATE(NOW())) GROUP BY Date";
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("Date"),rs.getInt("somme")));
            }
        lineChart_Client.getData().addAll(series);
        cnx.close();
    }

    @FXML
    private void fermer(MouseEvent event) 
    {
        Comptable.stage_graphique.close();
    }

    @FXML
    private void etablirGraphMois_Client() throws SQLException {
        lineChartMois_Client.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        
        if(duMois_client.getValue()!=null && auMois_Client.getValue()!=null)
        {
            tab=duMois_client.getValue().split("-");
            tab2=auMois_Client.getValue().split("-");
        }
        String sql = "SELECT MONTH(Date) AS mois,SUM(NbrTicketDonné) AS sommeJr "
                + "FROM gérecaisse "
                + "WHERE CodeTicket='9' AND (MONTH(Date)=MONTH(DATE(NOW())) OR MONTH(Date)=MONTH(DATE(NOW()))-1) "
                + "AND MONTH(Date)>='"+Integer.valueOf(tab[0])+"' AND MONTH(Date)<='"+Integer.valueOf(tab2[0])+"' "
                + "AND YEAR(Date)=YEAR(DATE(NOW())) "
                + "GROUP BY mois";
                
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("mois"),rs.getInt("sommeJr")));
            }
        lineChartMois_Client.getData().addAll(series);
        cnx.close();
    }

    @FXML
    private void initLineChartMois_Client() throws SQLException {
        duMois_client.setValue(null);
        auMois_Client.setValue(null);
        lineChartMois_Client.getData().clear();
        String tab[]=new String[2];
        String tab2[]=new String[2];
        
        String sql = "SELECT MONTH(Date) AS mois,SUM(NbrTicketDonné) AS sommeJr FROM gérecaisse WHERE CodeTicket='9' AND (MONTH(Date)=MONTH(DATE(NOW())) OR MONTH(Date)=MONTH(DATE(NOW()))-1) AND YEAR(Date)=YEAR(DATE(NOW())) GROUP BY mois";
                
        XYChart.Series series = new XYChart.Series();
        
        cnx=new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString("mois"),rs.getInt("sommeJr")));
            }
        lineChartMois_Client.getData().addAll(series);
        cnx.close();
    }
    
    
    
}
