package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;



public class DetailCaisseEntrée implements Initializable {
    
    public String libelleCaisse;
    @FXML
    private Label libCaisse;
    @FXML
    private JFXButton buttOuvFer;
    @FXML
    private Circle pointOuvFer;
    @FXML
    private Label msgErreur;
    private JFXTextField t100;
    @FXML
    private JFXTextField t120;
    @FXML
    private JFXTextField t150;
    @FXML
    private JFXTextField t240;
    @FXML
    private JFXTextField t300;
    @FXML
    private JFXTextField t400;
    @FXML
    private JFXTextField f1000;
    @FXML
    private JFXTextField f2000;
    @FXML
    private JFXTextField f3000;
    @FXML
    private JFXTextField monnaie;
    @FXML
    private JFXTextField recette;
    @FXML
    private JFXButton btnValider;
    public Connection cnx;
    String typeCaisse="";
    int tab[] = new int[9];
    @FXML
    private JFXButton ajouter;
    @FXML
    private JFXButton sosutraire;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try
        {   
            libCaisse.setText(InterCaisse.libelleCaisse);
            if (InterCaisse.etat==0)
            {
                buttOuvFer.setStyle("-fx-background-color: #18b24e");
                buttOuvFer.setText("Ouvrir");
                btnValider.setDisable(true);
            }
            else
            {   if (InterCaisse.etat==1)
                {
                    String préNom=null;
                    recette.setDisable(true);
                    String sql = "SELECT Nom,Prenom FROM personnel,caisse,gérecaisse WHERE '"+libCaisse.getText()+"'=caisse.Libelle AND caisse.IdCaisse = gérecaisse.IdCaisse AND Caissier=NumContrat AND Date=DATE(NOW()) AND Exist='0'";
                    cnx = new ConnexionBDD().ConnexionDB();
                    Statement st = cnx.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while (rs.next())
                        préNom=rs.getString("Prenom")+" "+rs.getString("Nom");
                    cnx.close();
                    if(préNom==null)
                    {
                        msgErreur.setText("Aucun caissier.");
                    }
                    else
                        msgErreur.setText(préNom);
                    buttOuvFer.setStyle("-fx-background-color: #F05757");
                    buttOuvFer.setText("Fermer");
                    pointOuvFer.setFill(Color.rgb(103, 192, 85));
                }  
                else
                {
                    pointOuvFer.setFill(Color.rgb(240, 176, 1));
                    buttOuvFer.setStyle("-fx-background-color: #18b24e");
                    buttOuvFer.setText("Ouvrir");
                    btnValider.setDisable(false);
                }
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    } 

    @FXML
    private void ouvFermer(ActionEvent event) throws SQLException, InterruptedException 
    {
        
        cnx = new ConnexionBDD().ConnexionDB();
        if(buttOuvFer.getText().equals("Ouvrir"))
        {
            String sql = "SELECT caisse.IdCaisse,Nom,Prenom FROM gérecaisse,caisse,personnel WHERE Exist='0' AND Libelle='"+libCaisse.getText()+"' AND gérecaisse.IdCaisse=caisse.IdCaisse AND Caissier=NumContrat AND gérecaisse.Date=(SELECT MAX(Date) FROM journée)";
            
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next())
            {
                sql = "UPDATE caisse SET Etat='1' WHERE Libelle='"+libCaisse.getText()+"'";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.executeUpdate();
                String nomPrénom =null;
                while (rs.next())
                    nomPrénom = rs.getString("Prenom")+" "+rs.getString("Nom");
                btnValider.setDisable(false);
                msgErreur.setText(nomPrénom);
                buttOuvFer.setText("Fermer");
                buttOuvFer.setStyle("-fx-background-color: #F05757");
                pointOuvFer.setFill(Color.rgb(103, 192, 85));
                recette.setDisable(true);
                monnaie.setDisable(false);
            }
            else
            {
               msgErreur.setText("Aucun caissier pour cette caisse.");
            }
        }
        else
        {
            String sql = "UPDATE caisse SET Etat='2' WHERE Libelle='"+libCaisse.getText()+"'";
            
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.executeUpdate();
            
            msgErreur.setText(null);
            buttOuvFer.setText("Ouvrir");
            buttOuvFer.setStyle("-fx-background-color: #63BF51");
            pointOuvFer.setFill(Color.rgb(240, 176, 1));
            recette.setDisable(false);
            monnaie.setDisable(true);
            
            for(int i=0;i<9;i++)
            {
                sql="SELECT Prix FROM ticket WHERE Code='"+(i+1)+"'";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next())
                    tab[i]=rs.getInt("Prix");
            }
        }
        cnx.close();
    }

    @FXML
    private void buttValider(ActionEvent event) throws SQLException 
    {
               
                String sql = "SELECT IdCaisse,TypeCaisse FROM caisse WHERE Libelle='"+libCaisse.getText()+"'";
                cnx = new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                int idCaisse=0,i=0;
                while (rs.next())
                {
                    idCaisse=rs.getInt("IdCaisse");
                    typeCaisse=rs.getString("TypeCaisse");
                }
                cnx.close();
            if(verification()) 
            {
                    ArrayList <Integer> ticket = new ArrayList <Integer>();
                    ticket.add(Integer.valueOf(t120.getText()));
                    ticket.add(Integer.valueOf(t150.getText()));
                    ticket.add(Integer.valueOf(t240.getText()));
                    ticket.add(Integer.valueOf(t300.getText()));
                    ticket.add(Integer.valueOf(t400.getText()));
                    ticket.add(Integer.valueOf(f1000.getText()));
                    ticket.add(Integer.valueOf(f2000.getText()));
                    ticket.add(Integer.valueOf(f3000.getText()));
                    if (typeCaisse.equals("Entrée"))
                       ticket.add(Integer.valueOf(t100.getText()));
                    
 
          PreparedStatement ps= null;

        cnx = new ConnexionBDD().ConnexionDB();
        if(buttOuvFer.getText().equals("Fermer"))
        {
            try
            {   
                
                for (i=1;i<=ticket.size();i++)
                {   
                    sql="UPDATE gérecaisse SET NbrTicketDonné=NbrTicketDonné+'"+ticket.get(i-1)+"',Monnaie=Monnaie+'"+Integer.valueOf(monnaie.getText())+"' WHERE IdCaisse='"+idCaisse+"' AND gérecaisse.Date=(SELECT MAX(Date) FROM journée) AND CodeTicket='"+i+"'";   
                    
                    ps = cnx.prepareStatement(sql);
                    ps.executeUpdate();
                    
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
        else if(buttOuvFer.getText().equals("Ouvrir"))
        {
            try
            {  
                btnValider.setDisable(true);
                
               for (i=1;i<=ticket.size();i++)
                {   
                    sql="UPDATE gérecaisse SET NbrTicketDonné=NbrTicketDonné-'"+ticket.get(i-1)+"', Recette='"+Integer.valueOf(recette.getText())+"' WHERE IdCaisse='"+idCaisse+"' AND gérecaisse.Date=(SELECT MAX(Date) FROM journée) AND CodeTicket='"+i+"' AND Exist='0'";   
                    
                    ps = cnx.prepareStatement(sql);
                    
                    ps.executeUpdate();
                }
                sql="UPDATE caisse SET Etat='0' WHERE IdCaisse='"+idCaisse+"'";
                
                ps = cnx.prepareStatement(sql);
                ps.executeUpdate();
                
                pointOuvFer.setFill(Color.rgb(242, 74, 74));
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
        cnx.close();
        if (t100!=null)
            t100.setText("");
        t120.setText("");
        t150.setText("");
        t240.setText("");
        t300.setText("");
        t400.setText("");
        f1000.setText("");
        f2000.setText("");
        f3000.setText("");
        recette.setText("");
        monnaie.setText("");
    }
    }

    @FXML
    private void MonnaieVerif(KeyEvent event) {
         String c = event.getCharacter();       
         String regex="[^0-9&&[^.]]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }
    }

    @FXML
    private void TicketVerif(KeyEvent event) {
        String c = event.getCharacter();       
        String regex="[^0-9]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }
    }
    
    public boolean verification()
    {
         boolean b = true;
         Paint value0=Paint.valueOf("F9C7CC");
      if(typeCaisse.equals("Entrée")) 
      {
         if(t100.getText().equals(""))
         {
          t100.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
         }
      }
         if(t120.getText().equals(""))
         {
           t120.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;  
         }
         if(t150.getText().equals(""))
         {
           t150.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;  
         }
         if(t240.getText().equals(""))
         {
           t240.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;  
         }
          if(t300.getText().equals(""))
         {
           t300.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;
         }
          if(t400.getText().equals(""))
         {
          t400.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;   
         }
         if(f1000.getText().equals(""))
         {
           f1000.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;  
         }
          if(f2000.getText().equals(""))
         {
          f2000.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;   
         }
         if(f3000.getText().equals(""))
         {
          f3000.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;   
         }
          if(buttOuvFer.getText().equals("Fermer") && recette.getText().equals(""))
         {
          recette.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;   
         }
         if(buttOuvFer.getText().equals("Ouvrir") && monnaie.getText().equals(""))
         {
          monnaie.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));
           b=false;   
         }
      return b;
    }
    public boolean confirmation(int i)
  {
     Image im = new Image("/Icones/icons8-plus-40.png");
      Image im1 = new Image("/Icones/icons8-effacer-40.png");
      Image im2 = new Image("/Icones/icons8-editer-le-fichier-48.png");
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      if(i==1)
      {
          alert.setGraphic(new ImageView(im1));
          alert.setTitle("Caisse");
          alert.setContentText("Retourner recette ?");
      }
      else if(i==2)
      {
          alert.setGraphic(new ImageView(im));
          alert.setTitle("Caisse");
          alert.setContentText("Ouvrir caisse?");
      }
      else if(i==3)
      {
          alert.setGraphic(new ImageView(im2));
          alert.setTitle("Caisse");
          alert.setContentText("Fermer caisse ?");
      }
 
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
  }

    @FXML
    private void buttAjouter(ActionEvent event) 
    {
        
    }

    @FXML
    private void buttSoust(ActionEvent event) {
    }

    

}
