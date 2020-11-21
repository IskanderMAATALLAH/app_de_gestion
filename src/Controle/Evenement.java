/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Main.ConnexionBDD;
import static Main.ConnexionData.ConnecterDB;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import static mondrian.olap.fun.vba.Vba.date;
import tableau.events;
import tableau.rendezvs;


/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class Evenement implements Initializable {
     Connection cnx; 
  PreparedStatement ps;
  ResultSet rs=null;
  ObservableList<events> data=FXCollections.observableArrayList();
  FilteredList<events> filteredData=new FilteredList<>(data,e->true);
    @FXML
    private JFXTextField intitule;
    @FXML
    private JFXDatePicker datedeb;
    @FXML
    private JFXDatePicker datefin;
    @FXML
    private JFXTextField ideven;
    @FXML
    private JFXDatePicker date1;
    @FXML
    private JFXDatePicker date2;
    @FXML
    private TableView<events> tableau;
    @FXML
    private TableColumn<?, ?> ID;
    @FXML
    private TableColumn<?, ?> intitul;
    @FXML
    private TableColumn<?, ?> da1;
    @FXML
    private TableColumn<?, ?> da2;
    @FXML
    private JFXButton ajout;
    @FXML
    private JFXButton modif;
     events vs;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        intitul.setCellValueFactory(new PropertyValueFactory<>("Intitulé"));
        da1.setCellValueFactory(new PropertyValueFactory<>("Debut"));
        da2.setCellValueFactory(new PropertyValueFactory<>("Fin"));
        loadDatabaseData();
    }    
////////////////////////////// add events ////////////////////////////////////////////////////////////////////////
    @FXML
    private void ajouter(ActionEvent event) {
        
         LocalDate da=datedeb.getValue();
         LocalDate da1=datefin.getValue();

        String ndate=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
        String ndate1=Integer.toString(da1.getYear())+"-"+Integer.toString(da1.getMonthValue())+"-"+Integer.toString(da1.getDayOfMonth());
          String sql = "insert into evenements (IdEvent,IdAgent,Intitulé,Date_deb,Date_fin) values (null,'Agent01','"+intitule.getText()+"','"+ndate+"','"+ndate1+"')";
           if(datedeb.getValue()== null || datefin.getValue()==null || intitule.getText().equals(""))
           {
              JOptionPane.showMessageDialog(null, "Vérifier les champs !"); 
           }
           else
           {
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                   st.executeUpdate(sql);
                   cnx.close();
                   JOptionPane.showMessageDialog(null, "vous avez ajouter un nouveau evenement !");
         	   
            }catch(SQLException e) {System.out.print(e);}
          }
    }
////////////////////////////////////edit events//////////////////////////////////////////////////
    @FXML
    private void modifier(ActionEvent event) {
         LocalDate da=datedeb.getValue();
         LocalDate da1=datefin.getValue();

        String ndate=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
        String ndate1=Integer.toString(da1.getYear())+"-"+Integer.toString(da1.getMonthValue())+"-"+Integer.toString(da1.getDayOfMonth());
         String sql = "UPDATE  evenements set intitulé='"+intitule.getText()+"',Date_deb='"+ndate+"',Date_fin='"+ndate1+"' where IdEvent="+vs.getID()+"";
           if(datedeb.getValue()== null || datefin.getValue()==null || intitule.getText().equals(""))
           {
              JOptionPane.showMessageDialog(null, "Vérifier les champs !"); 
           }
           else
           {
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                   st.executeUpdate(sql);
                   cnx.close();
                   JOptionPane.showMessageDialog(null, "vous avez bien modifié evenement !");
         	   
            }catch(SQLException e) {System.out.print(e);}}
    }
////////////////// look for events ////////////////////////////////////////////////    
     @FXML
    private void recherche(ActionEvent event) {
        
    }
   //////////////// from table to fields /////////////////////////////////////////////// 
      @FXML
	public void showOnClick()
	{
             vs =(events)tableau.getSelectionModel().getSelectedItem();// click pour retourner les valeurs de tableau dans les champs
            intitule.setText(vs.getIntitulé());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;          // convert from BDD to javafx values
            LocalDate localDate = LocalDate.parse(vs.getDebut(), formatter);    // convert from BDD to javafx values
            datedeb.setValue(localDate);                                         //convert from BDD to javafx values
            //convert from BDD to javafx values
            LocalDate localDate2 = LocalDate.parse(vs.getFin(), formatter);           //convert from BDD to javafx values
            datefin.setValue(localDate2);                                            //convert from BDD to javafx values
            //convert from BDD to javafx values
            ajout.setDisable(true);     // disable and enable buttons
            modif.setDisable(false);   // disable and enable buttons
	}
    /////////////////////////////////////load from database ///////////////////
    public void loadDatabaseData()
	{
		String sql="select * from evenements";
		
		try
		{
					
			data.clear();

                   cnx=new ConnexionBDD().ConnexionDB();
                    ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				data.add(new events(
						rs.getInt("IdEvent"),
						rs.getString("Intitulé"),
						rs.getString("Date_deb"),
                                                rs.getString("Date_fin")
						));
				tableau.setItems(data);
                                
                               
			}
			ps.close();
			rs.close();
                        cnx.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
    
}
