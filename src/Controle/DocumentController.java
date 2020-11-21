/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;
import Main.ConnexionBDD;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;
import tableau.Documents;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class DocumentController implements Initializable {
   Connection cnx;
   PreparedStatement ps;
     ObservableList<Documents> data=FXCollections.observableArrayList();
    @FXML
    private JFXTextField NumDoc;
    @FXML
    private TextArea desc;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton ajout;
    @FXML
    private JFXButton modif;
    @FXML
    private JFXButton supp;
    @FXML
    private TableView<Documents> tableau;
    @FXML
    private TableColumn<?, ?> num;
    @FXML
    private TableColumn<?, ?> descre;
    @FXML
    private TableColumn<?, ?> datee;
Documents doc;
    @FXML
    private JFXDatePicker dateDuDoc;
    @FXML
    private JFXDatePicker dateAuDoc;
    @FXML
    private JFXTextField descreption;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           num.setCellValueFactory(new PropertyValueFactory<>("NDoc"));
        descre.setCellValueFactory(new PropertyValueFactory<>("Descreption"));
        datee.setCellValueFactory(new PropertyValueFactory<>("Date"));
       loadDatabaseData();
       try {
           getnumDoc();
       } catch (SQLException ex) {
           Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }    

    @FXML
    private void ajouter(ActionEvent event) { 
        if(this.confirmationFinale(2))
        {
           LocalDate da=date.getValue();
           String ndate=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
           String sql = "insert into document (IdDoc,Descr,Date,IdSec) values ('"+NumDoc.getText()+"','"+desc.getText()+"','"+ndate+"','"+SecretaireController.numSec+"')";
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                   st.executeUpdate(sql);
                   cnx.close();
                   actualiser();
         	   act(1);
            }catch(SQLException e) {System.out.print(e);}
    }}

    @FXML
    private void modifier(ActionEvent event) {
       if(confirmation(3))
       {
         LocalDate da=date.getValue();
        String ndate=Integer.toString(da.getYear())+"-"+Integer.toString(da.getMonthValue())+"-"+Integer.toString(da.getDayOfMonth());
        
           String sql = "UPDATE  document set IdDoc='"+NumDoc.getText()+"',Descr='"+desc.getText()+"',Date='"+ndate+"' where IdDoc="+doc.getNDoc()+"";

            try
               {
                   cnx=new ConnexionBDD().ConnexionDB();
                   Statement st = cnx.createStatement();
                   st.executeUpdate(sql);
                   cnx.close();                 
         	   actualiser(); 
                   act(3);
            }catch(SQLException e) {}
    }
    }
     public void actualiser() throws SQLException
     { 
          loadDatabaseData();
           getnumDoc();
           supp.setDisable(true);
           ajout.setDisable(false);
           modif.setDisable(true);
           date.setValue(null);
           desc.setText("");
           NumDoc.setText("");
           descreption.setText("");
           dateDuDoc.setValue(null);
           dateAuDoc.setValue(null);
     }
    @FXML
    private void supprimer(ActionEvent event) {
            if(confirmationFinale(1))
              {
        String sql ="delete from document where IdDoc='"+doc.getNDoc()+"'"; 
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                cnx.close();
                act(2);
                actualiser();   
            }catch(SQLException e) {}
    }
    }
    
     public void loadDatabaseData()
	{
		String sql="select * from document where IdSec='"+SecretaireController.numSec+"'";
		
		try
		{
					
			data.clear();

                   cnx=new ConnexionBDD().ConnexionDB();
                    ps = cnx.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				data.add(new Documents(
						rs.getString("IdDoc"),
						rs.getString("Descr"),
						rs.getString("Date")
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
    
     @FXML
	public void showOnClick()
	{
            doc =(Documents)tableau.getSelectionModel().getSelectedItem();// click pour retourner les valeurs de tableau dans les champs
            NumDoc.setText(doc.getNDoc());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;          // convert from BDD to javafx values
            LocalDate localDate = LocalDate.parse(doc.getDate(), formatter);    // convert from BDD to javafx values
            date.setValue(localDate);  
            desc.setText(doc.getDescreption());//convert from BDD to javafx values
            //convert from BDD to javafx values                                           //convert from BDD to javafx values
            //convert from BDD to javafx values
            ajout.setDisable(true);     // disable and enable buttons
            modif.setDisable(false);
            supp.setDisable(false);// disable and enable buttons
	}
         public void act(int i)
    {
     Image im = new Image("/Icones/icons8-approbation-40.png"); 
     Image im1 = new Image("/Icones/icons8-haute-priorité-40.png");
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Document"); nt.text("Document ajouté");}
      else if(i==2){nt.title("Document"); nt.text("Document supprimé");}
       else if(i==3){nt.title("Document"); nt.text("Document modifié");}
       
      nt.show();


  }
 
  public boolean confirmation(int i)
  {
      
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-approbation-40.png"); 
       alert.setGraphic(new ImageView(im));
      if(i==1){alert.setTitle("Document");alert.setContentText("Supprimer Document ?");}
      else if(i==2){alert.setTitle("Rendez-vous");alert.setContentText("Confirmer Document ?");}
      else if(i==3){alert.setTitle("Rendez-vous");alert.setContentText("Modifier Document ?");}
      Optional <ButtonType> action = alert.showAndWait();     
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }

  public boolean verifierChamps()
  {  boolean b = true;
    Paint value0=Paint.valueOf("F9C7CC");
     if(date.equals(null))
     {
      date.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      b=false;
     }

     if(NumDoc.getText().equals(""))
     {
      NumDoc.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      b=false;
     }
     
      if(desc.getText().equals(""))
     {
      desc.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      b=false;
     }
  
     return b ;
  }
    public boolean confirmationFinale(int i)
    {
        boolean b = false;
        if(verifierChamps())
        {
            if(confirmation(i))
            {
             b=true;  
            }
        }
        return b;
    }
    
    public void getnumDoc() throws SQLException
    {
         int num=0;
        String sql = " select Max(IdDoc)as Num from document";
        cnx=new ConnexionBDD().ConnexionDB();
        ps = cnx.prepareStatement(sql);
	ResultSet rs=ps.executeQuery();
       
         ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
                        num=rs.getInt("Num");
                        }
                        num=num+1;
                  NumDoc.setText(Integer.toString(num));
                   NumDoc.setDisable(true);
                   cnx.close();
    }

  @FXML
    private void rechDocAction(ActionEvent event) throws SQLException 
    {
        if(!descreption.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document WHERE Descr LIKE '%"+descreption.getText()+"%' and IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(!descreption.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+descreption.getText()+"%' AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"' "
                    + "and IdSec='"+SecretaireController.numSec+"' ";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(!descreption.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+descreption.getText()+"%' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' "
                    + "and IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(!descreption.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Descr LIKE '%"+descreption.getText()+"%' "
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(descreption.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()==null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date>= '"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"' "
                    + "And IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
   
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(descreption.getText().equals("") && dateDuDoc.getValue()==null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' "
                    + "And IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }else if(descreption.getText().equals("") && dateDuDoc.getValue()!=null && dateAuDoc.getValue()!=null)
        {
            String sql ="SELECT * FROM document "
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAuDoc)+"' "
                    + "AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDuDoc)+"' "
                    + "And IdSec='"+SecretaireController.numSec+"'";
            cnx=new ConnexionBDD().ConnexionDB();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            tableau.getItems().clear();
            while(rs.next())
            {
                data.add(new Documents(
                                            rs.getString("IdDoc"),
                                            rs.getString("Descr"),
                                            rs.getString("Date")
                                            ));
                tableau.setItems(data);
            }
            cnx.close();
		
        }

    }

    @FXML
    private void ActualiserDoc(ActionEvent event) throws SQLException {
        actualiser();
    }


}
