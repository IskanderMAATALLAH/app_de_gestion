package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import tableau.Documents;
import tableau.rendezvs;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class ConsulteRDVetDoc implements Initializable {
    Connection cnx;
  PreparedStatement ps;
  ResultSet rs=null;
  ObservableList<rendezvs> data=FXCollections.observableArrayList();
  ObservableList<Documents> data1=FXCollections.observableArrayList();

    @FXML
    private TableView<rendezvs> tableau;
    @FXML
    private TableColumn<?, ?> num;
    @FXML
    private TableColumn<?, ?> datee;
    @FXML
    private TableColumn<?, ?> heurre;
    @FXML
    private TableColumn<?, ?> llieu;
    @FXML
    private TableColumn<?, ?> avecc;
    @FXML
    private TableView<Documents> tableau1;
    @FXML
    private TableColumn<?, ?> num1;
    @FXML
    private TableColumn<?, ?> descre;
    @FXML
    private TableColumn<?, ?> datee1;
    @FXML
    private JFXTextField nomAvec;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    @FXML
    private JFXTextField desc;
    @FXML
    private JFXDatePicker dateDuDoc;
    @FXML
    private JFXDatePicker dateAuDoc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                num.setCellValueFactory(new PropertyValueFactory<>("Num")); /////////// initialisation du tableau
		datee.setCellValueFactory(new PropertyValueFactory<>("Date"));/////////// initialisation du tableau
		heurre.setCellValueFactory(new PropertyValueFactory<>("Heure"));/////////// initialisation du tableau
                avecc.setCellValueFactory(new PropertyValueFactory<>("Avec"));/////////// initialisation du tableau
                llieu.setCellValueFactory(new PropertyValueFactory<>("Lieu"));/////////// initialisation du tableau
               
                num1.setCellValueFactory(new PropertyValueFactory<>("NDoc"));
                descre.setCellValueFactory(new PropertyValueFactory<>("Descreption"));
                datee1.setCellValueFactory(new PropertyValueFactory<>("Date"));	
                loadDatabaseData1();
                 loadDatabaseData();
    }    
    
    public void loadDatabaseData()
	{
            String sql="select * from rdv";
		
            try
        	{
                    tableau.getItems().clear();

                    cnx=new ConnexionBDD().ConnexionDB();
                    ps = cnx.prepareStatement(sql);
                    rs=ps.executeQuery();

                        while(rs.next())
                        {
                                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
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
        
         public void loadDatabaseData1()
	{
		String sql="select * from document";
		
		try
		{
					
                    tableau1.getItems().clear();

                    cnx=new ConnexionBDD().ConnexionDB();
                    ps = cnx.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				data1.add(new Documents(
						rs.getString("IdDoc"),
						rs.getString("Descr"),
						rs.getString("Date")
						));
				tableau1.setItems(data1);
                                
                               
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
    
        

    @FXML
    private void showOnClick(MouseEvent event) {
    }

    @FXML
    private void rechAction(ActionEvent event) throws SQLException 
    {
        if(!nomAvec.getText().equals("") && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            String sql = "SELECT * FROM rdv WHERE Avec='"+nomAvec.getText()+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(!nomAvec.getText().equals("") && dateDu.getValue()!=null && dateAu.getValue()==null)
        {   
            String sql = "SELECT * FROM rdv "
                    + "WHERE Avec='"+nomAvec.getText()+"' AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(!nomAvec.getText().equals("") && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            String sql = "SELECT * FROM rdv "
                    + "WHERE Avec='"+nomAvec.getText()+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' ";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(!nomAvec.getText().equals("") && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            
            String sql = "SELECT * FROM rdv "
                    + "WHERE Avec='"+nomAvec.getText()+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' ";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(nomAvec.getText().equals("") && dateDu.getValue()!=null && dateAu.getValue()==null)
        {
            
            String sql = "SELECT * FROM rdv "
                    + "WHERE Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' ";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(nomAvec.getText().equals("") && dateDu.getValue()!=null && dateAu.getValue()!=null)
        {
            String sql = "SELECT * FROM rdv "
                    + "WHERE Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }else if(nomAvec.getText().equals("") && dateDu.getValue()==null && dateAu.getValue()!=null)
        {
            String sql = "SELECT * FROM rdv "
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"'";
            cnx = new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new rendezvs(                     
                                                rs.getInt("Num"),          
                                                rs.getString("Date"),      
                                                rs.getString("Heure"),     
                                                rs.getString("Avec"),      
                                                rs.getString("Lieu")       
                                                ));                       
                tableau.setItems(data);       
            }
            cnx.close();
        }    
    }

    @FXML
    private void rechDocAction(ActionEvent event) throws SQLException 
    {
        if(!desc.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document WHERE Descr LIKE '%"+desc.getText()+"%'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(!desc.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+desc.getText()+"%' AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(!desc.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+desc.getText()+"%' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' ";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(!desc.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+desc.getText()+"%' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' ";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(desc.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
   
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(desc.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' ";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }else if(desc.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau1.getItems().clear();
            while(rs.next())
            {
                data1.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau1.setItems(data1);
            }
            cnx.close();
		
        }

    }

    @FXML
    private void actualiserAction(ActionEvent event) {
        nomAvec.setText("");
        dateDu.setValue(null);
        dateAu.setValue(null);
        
        loadDatabaseData();
    }

    @FXML
    private void actualsierDocAction(ActionEvent event) {
        desc.setText("");
        dateDuDoc.setValue(null);
        dateAuDoc.setValue(null);
        
        loadDatabaseData1();
    }
            
}
    

