package Reports;

/**
 *
 * @author OFFICE
 */
import Main.ConnexionBDD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.layout.VBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author Ram
 */
public class ContratOP  extends JFrame{    
    
     
    PreparedStatement pst = null;
    ResultSet rs = null;   

    public ContratOP() throws HeadlessException {
    }
   
    public void showReport(String num,String date,String duree){
         try {
             Connection cn =null;
             //JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\user\\Documents\\NetBeansProjects\\ParcDAttractions\\src\\Reports\\Contrat.jrxml");
             InputStream input = getClass().getResourceAsStream("Contrat.jrxml");
             JasperDesign jasperDesign = JRXmlLoader.load(input);
             cn=new ConnexionBDD().ConnexionDB();
             String query = "select NumContrat,Nom,Prenom,Salaire ,DATE(DateNaiss) as DateNaiss ,Téléphone,IdPoste,Adresse,DATE(DateDebCon) as DateDebCon,Durée,ADDDATE('"+date+"',interval '"+duree+"' Month) as DateFin from personnel where NumContrat='"+num+"'";
             
             JRDesignQuery jrquery = new JRDesignQuery();
             jrquery.setText(query);
             jasperDesign.setQuery(jrquery); 
             JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
             JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, null, cn);
             
             JRViewer viewer = new JRViewer(JasperPrint);
             JPanel jPanel1 = new JPanel();
             jPanel1.setLayout(new BorderLayout());
             jPanel1.repaint();
             jPanel1.add(viewer);
             jPanel1.revalidate();
             viewer.setOpaque(true);
             viewer.setVisible(true);
             this.add(jPanel1);
             this.setSize(900,500); // JFrame size
             this.setVisible(true);
             cn.close();
           
            
         } catch (Exception e) {
             JOptionPane.showMessageDialog(rootPane, e.getMessage());
         }
   
}
}
