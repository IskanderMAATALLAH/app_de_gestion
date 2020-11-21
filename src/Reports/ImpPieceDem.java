/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;


import Main.ConnexionBDD;
import java.awt.BorderLayout;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author OFFICE
 */
public class ImpPieceDem extends JFrame{
     Connection cn =null; 
    PreparedStatement pst = null;
    ResultSet rs = null;
    public ImpPieceDem()
    {
        
    }
    
      public void showReport(String num,String date,int piece){
         try {
             
             //JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\OFFICE\\Documents\\NetBeansProjects\\ParcdAttractions\\src\\Reports\\PrendPiece.jrxml");
             InputStream input = getClass().getResourceAsStream("PrendPiece.jrxml");
             JasperDesign jasperDesign = JRXmlLoader.load(input);
             cn=new ConnexionBDD().ConnexionDB();
             String query = "select * from personnel,prend,piece where IdPreneur=NumContrat and Ref=IdPiece and IdPreneur='"+num+"' and IdPiece ='"+piece+"'and Date='"+date+"'";
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
