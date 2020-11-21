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
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.swing.JOptionPane;
//import static org.codehaus.groovy.runtime.DefaultGroovyMethods.is;
import org.controlsfx.control.Notifications;

import tableau.rendezvs;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class RendezvousController implements Initializable {
  Connection cnx; 
  PreparedStatement ps;
  ResultSet rs=null;
  ObservableList<rendezvs> data=FXCollections.observableArrayList();
  FilteredList<rendezvs> filteredData=new FilteredList<>(data,e->true);

  int Id_Sec;
    @FXML
    private TableView<rendezvs> tableau;
    @FXML
    private TableColumn<?,?> num;
    @FXML
    private DatePicker date;
    @FXML
    private JFXTextField lieu;
    @FXML
    private JFXTextField avec;
    @FXML
    private JFXTimePicker hour;
    @FXML
    private TableColumn<?,?> datee;
    @FXML
    private TableColumn<?,?> heurre;
    @FXML
    private TableColumn<?,?> llieu;
    @FXML
    private TableColumn<?,?> avecc;
    @FXML
    private JFXButton ajout;
    @FXML
    private JFXButton modif;
     String ndate;
     String nhour ;
      rendezvs vs;
      String ndate1;
      String nhour2; 
    @FXML
    private JFXButton Supp;
    @FXML
    private JFXTextField nomAvec;
    @FXML
    private JFXDatePicker dateDu;
    @FXML
    private JFXDatePicker dateAu;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
                              
                 num.setCellValueFactory(new PropertyValueFactory<>("Num")); /////////// initialisation du tableau
		datee.setCellValueFactory(new PropertyValueFactory<>("Date"));/////////// initialisation du tableau
		heurre.setCellValueFactory(new PropertyValueFactory<>("Heure"));/////////// initialisation du tableau
                avecc.setCellValueFactory(new PropertyValueFactory<>("Avec"));/////////// initialisation du tableau
                llieu.setCellValueFactory(new PropertyValueFactory<>("Lieu"));/////////// initialisation du tableau
		loadDatabaseData();
        
    }    

    @FXML
    private void ajouter(ActionEvent event) {
        if(confirmationFinale(2))
        {   
        LocalDate da=date.getValue();//////////////////// recuperation de la date
        int year = da.getYear();/////// recuperation de l annee
        int  month = da.getMonthValue();/////// recuperation de mois
        int day = da.getDayOfMonth();/////// recuperation du jour
        String yeaar = Integer.toString(year);/////// convert l annee to string 
        String monnth = Integer.toString(month);/////// convert le mois  to string
        String daay = Integer.toString(day);/////// convert le jour to string
        String ho = hour.getValue().toString();/////// recuperation de l'heure
        LocalTime h = hour.getValue();/////// recuperation de l'heure
        int heur = h.getHour();/////// recuperation de l'heure exact
        int min = h.getMinute();/////// recuperation de min
        int sec = h.getSecond();/////// recuperation de seconde
        String ndate=yeaar+"-"+monnth+"-"+daay;/////// nouvelle date pour la BDD
        String nhour = Integer.toString(heur)+":"+Integer.toString(min)+":00";  /////// nouvelle heure pr la BDD
      
        String sql = "insert into rdv (Num,IdSec,NumContrat,Date,Heure,Avec,Lieu) values (null,'"+SecretaireController.numSec+"','002/18','"+ndate+"','"+nhour+"','"+avec.getText()+"','"+lieu.getText()+"')";

            try
            {   
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                   st.executeUpdate(sql);
                   cnx.close();
                   act(1);
                   loadDatabaseData();
         	   
            }catch(SQLException e) {System.out.print(e);}
          
    }}

    private void recherche(ActionEvent event) {
        loadDatabaseData();
    }
    	public void loadDatabaseData()
	{
		String sql="select * from rdv where IdSec='"+SecretaireController.numSec+"'";
		
		try
		{
					
			data.clear();

                        cnx=new ConnexionBDD().ConnexionDB();
                        ps = cnx.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				data.add(new rendezvs(                     // recuperation 
						rs.getInt("Num"),          //   des 
						rs.getString("Date"),      //   info 
						rs.getString("Heure"),     // et les mettres 
                                                rs.getString("Avec"),      //dans 
						rs.getString("Lieu")       // un tableau
						));                        //
				tableau.setItems(data);                    //
                                
                               
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
		try
		{
		     vs =(rendezvs)tableau.getSelectionModel().getSelectedItem();// click pour retourner les valeurs de tableau dans les champs
		     
		      DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;          // convert from BDD to javafx values 
                      LocalDate localDate = LocalDate.parse(vs.getDate(), formatter);    // convert from BDD to javafx values  
                       date.setValue(localDate);                                         //convert from BDD to javafx values
                       DateTimeFormatter time =DateTimeFormatter.ISO_TIME;               //convert from BDD to javafx values
                       LocalTime timee = LocalTime.parse(vs.getHeure(), time);           //convert from BDD to javafx values
			hour.setValue(timee);                                            //convert from BDD to javafx values
                         lieu.setText(vs.getLieu());                                     //convert from BDD to javafx values
                         avec.setText(vs.avec);                                          //convert from BDD to javafx values
			ps.close();
			rs.close();
                        ajout.setDisable(true);     // disable and enable buttons
                        modif.setDisable(false); 
                        Supp.setDisable(false); // disable and enable buttons
		}
		catch(SQLException e)
		{
			System.out.println();
		}
	}

    @FXML
    private void Modifier(ActionEvent event) {
        if(this.confirmationFinale(3))
        {
        LocalDate da = date.getValue();             //
        int year = da.getYear();                   //
        int  month = da.getMonthValue();           //       convert date et heure pour les mettres dans la 
        int day = da.getDayOfMonth();              //
        String yeaar = Integer.toString(year);     //
        String monnth = Integer.toString(month);   //                    BASE DE DONNEES
        String daay = Integer.toString(day);       //
        String ho = hour.getValue().toString();    //
        LocalTime h = hour.getValue();            //
        int heur = h.getHour();                  //
        int min = h.getMinute();                //
        int sec = h.getSecond();               //
       
        
        String ndate=yeaar+"-"+monnth+"-"+daay;//convert date et heure pour les mettres dans la bdd
        String nhour = Integer.toString(heur)+":"+Integer.toString(min)+":00"; //convert date et heure pour les mettres dans la bdd
        int NumRdv;
        rendezvs vs =(rendezvs)tableau.getSelectionModel().getSelectedItem(); // pour recuperer l identifiant
        
		     String query="select Num from rdv where IdSec='"+SecretaireController.numSec+"'";
                     NumRdv=vs.getNum();
         String sql = "UPDATE  rdv set Date='"+ndate+"',Heure='"+nhour+"',Avec='"+avec.getText()+"',Lieu='"+lieu.getText()+"' where Num="+NumRdv+"";
            
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                cnx.close();
                act(3);
                actualiser();   
            }catch(SQLException e) {System.out.print(e);}           
        }
    }
    public void actualiser()
    {
    ajout.setDisable(false);
    modif.setDisable(true);
    Supp.setDisable(true);
    date.setValue(null);
    hour.setValue(null);
    lieu.setText("");
    avec.setText("");
    nomAvec.setText("");
    dateDu.setValue(null);
    dateAu.setValue(null);
    loadDatabaseData();          
    }

    @FXML
    private void Supprimer(ActionEvent event) {
        if(confirmationFinale(1))
        {
        String sql ="delete from rdv where Num='"+vs.getNum()+"'"; 
            try
            {
                cnx=new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                st.executeUpdate(sql);
                cnx.close();
                act(2);
                actualiser();   
            }catch(SQLException e) {}
    }}
   public void act(int i)
    {
     Image im = new Image("/Icones/icons8-approbation-40.png"); 
     Image im1 = new Image("/Icones/icons8-haute-priorité-40.png");
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==1){nt.title("Rendez-vous"); nt.text("Rendez-vous ajouté");}
      else if(i==2){nt.title("Rendez-vous"); nt.text("Rendez-vous supprimé");}
       else if(i==3){nt.title("Rendez-vous"); nt.text("Rendez-vous supprimé");}
       else if(i==4){nt.graphic(new ImageView(im1));nt.title("Rendez-vous"); nt.text("Ajout rendez-vous non effectué !");}
      nt.show();


  }
 
  public boolean confirmation(int i)
  {
      
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-approbation-40.png"); 
       alert.setGraphic(new ImageView(im));
      if(i==1){alert.setTitle("Rendez-vous");alert.setContentText("Supprimer rendez-vous ?");}
      else if(i==2){alert.setTitle("Rendez-vous");alert.setContentText("Confirmer rendez-vous ?");}
      else if(i==3){alert.setTitle("Rendez-vous");alert.setContentText("Modifier rendez-vous ?");}
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
      b=false;
      information(2);
     }
     if(hour.equals(null))
     {
         information(1);
      b=false;   
     }
     if(lieu.getText().equals(""))
     {
      lieu.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
      b=false;
     }
     if(avec.getText().equals(""))
     {
      avec.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
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

    @FXML
    private void Contrainte(KeyEvent event) {
              if(Character.isDigit(event.getCharacter().charAt(0)))
       {
           event.consume();
       }
    }
    
     public void information(int i)
   {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       Image im = new Image("/Icones/icons8-information-40.png"); 
        alert.setGraphic(new ImageView(im));
        if(i==1)     {    alert.setTitle("Rendez-vous");alert.setContentText("Vérifier l'heure ");}
        else if(i==2 )alert.setContentText("Vérifier la date ");
       Optional <ButtonType> action = alert.showAndWait();
       alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          alert.close();
      }   
   }


    @FXML
   private void rechAction(ActionEvent event) throws SQLException 
    {
        if(!nomAvec.getText().equals("") && dateDu.getValue()==null && dateAu.getValue()==null)
        {
            String sql = "SELECT * FROM rdv WHERE Avec='"+nomAvec.getText()+"' AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "WHERE Avec='"+nomAvec.getText()+"' AND Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"'"
                    + " AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "WHERE Date>='"+new AjoutModifPersonnel().changeFormatDate(dateDu)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "AND Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
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
                    + "WHERE Date<='"+new AjoutModifPersonnel().changeFormatDate(dateAu)+"' "
                    + "AND IdSec='"+SecretaireController.numSec+"'";
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
    private void ActualiserRdv(ActionEvent event) {
       actualiser(); 
    }

}

          
    
    
    
    
    

