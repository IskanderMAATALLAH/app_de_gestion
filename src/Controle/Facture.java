 package Controle;

import Main.ConnexionBDD;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;



public class Facture implements Initializable {

    @FXML
    private JFXRadioButton rbNvF;
    @FXML
    private JFXRadioButton rbAnF;
    @FXML
    private VBox vBoxF;
    @FXML
    private JFXTextField idF;
    @FXML
    private JFXTextField nomF;
    @FXML
    private JFXTextField adrF;
    @FXML
    private JFXTextField telF;
    @FXML
    private JFXComboBox<String> nomAnF;
    @FXML
    private JFXRadioButton rbOutil;
    @FXML
    private JFXRadioButton rbPièce;
    @FXML
    private JFXRadioButton rbAutre;
    @FXML
    private HBox hBoxNvAnP;
    @FXML
    private JFXRadioButton rbNvP;
    @FXML
    private JFXRadioButton rbAnP;
    @FXML
    private VBox vBoxP;
    @FXML
    private JFXTextField idP;
    @FXML
    private JFXTextField nomP;
    @FXML
    private JFXComboBox<String> nomAnP;
    @FXML
    private JFXTextField qte;
    
    ToggleGroup tg1 =null;
    ToggleGroup tg2 = null;
    ToggleGroup tg3 = null;
    String selecItem1=null;
    String selecItem2=null;
    String selecItem3=null;
    @FXML
    private JFXTextField prixUnit;
    @FXML
    private JFXTextField remise;
    @FXML
    private JFXTextField montNet;
    @FXML
    private JFXTextField vers;
    @FXML
    private JFXTextField montRest;
    @FXML
    private JFXTextField montTotal;
    @FXML
    private JFXTextField autreCharge;
    public Connection cnx;
    Paint value0=Paint.valueOf("F9C7CC");

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        tg1= new ToggleGroup();
        tg2= new ToggleGroup();
        tg3= new ToggleGroup();
        
        rbNvF.setToggleGroup(tg1);
        rbAnF.setToggleGroup(tg1);
        
        rbNvP.setToggleGroup(tg2);
        rbAnP.setToggleGroup(tg2);
        
        rbOutil.setToggleGroup(tg3);
        rbPièce.setToggleGroup(tg3);
        rbAutre.setToggleGroup(tg3);
        
        montTotal.setEditable(false);
        remise.setEditable(false);
        
        
    } 
    
    private void listerPieceOutil(String type) throws SQLException
    {
        nomAnP.getItems().clear();
        String sql = "SELECT Ref,Libelle FROM piece WHERE Type='"+type+"'";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        String itemRefLib=null;
        while (rs.next())
            {
                itemRefLib=rs.getInt("Ref")+"|"+rs.getString("Libelle");
                nomAnP.getItems().add(itemRefLib);
            }
        cnx.close();
    }
    
    private void listerAutreProd() throws SQLException
    {
        nomAnP.getItems().clear();
        String sql="SELECT IdProduit,Libelle FROM produit WHERE IdProduit NOT IN(SELECT Ref FROM piece)";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        String itemRefLib=null;
        while (rs.next())
            {
                itemRefLib=rs.getInt("IdProduit")+"|"+rs.getString("Libelle");
                nomAnP.getItems().add(itemRefLib);
            } 
        cnx.close();
    }
    
    public void listerFournisseur(JFXComboBox cbFourn) throws SQLException
    {   
        cbFourn.getItems().clear();
        String sql = "SELECT IdFournisseur,NomFournisseur FROM fournisseur";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        String itemIdNom=null;
        while (rs.next())
            {
                itemIdNom=rs.getInt("IdFournisseur")+"|"+rs.getString("NomFournisseur");
                cbFourn.getItems().add(itemIdNom);
            }
        cnx.close();
    }

    @FXML
    private void enableNvF(ActionEvent event) throws SQLException 
    {
        selecItem1="Nouveau";
        vBoxF.setDisable(false);
        nomAnF.setDisable(true);
        maxIdFourn();
        idF.setEditable(false);
    }

    @FXML
    private void enableAnF(ActionEvent event) throws SQLException {
        selecItem1="Ancien";
        vBoxF.setDisable(true);
        nomAnF.setDisable(false);
        listerFournisseur(nomAnF);
    }

    @FXML
    private void actProduit(ActionEvent event) throws SQLException {
        JFXRadioButton selecToggle=(JFXRadioButton) tg3.getSelectedToggle();
        selecItem3=selecToggle.getText();
        if(selecItem3.equals("Outillage"))
        {
            listerPieceOutil("Outillage");
        }else if(selecItem3.equals("Pièce"))
        {
            listerPieceOutil("Pièce");
        }else
            listerAutreProd();
            
    }

    @FXML
    private void enableNvP(ActionEvent event) throws SQLException {
        selecItem2="Nouveau";
        vBoxP.setDisable(false);
        nomAnP.setDisable(true);
        maxIdProd();
        idP.setEditable(false);
    }

    @FXML
    private void enableAnP(ActionEvent event) throws SQLException 
    {
        selecItem2="Ancien";
        vBoxP.setDisable(true);
        nomAnP.setDisable(false);
    }

    @FXML
    private void btnValider(ActionEvent event) throws IOException 
    {   
        int idFour=0;
        int idProd=0;
        
        if(verifieInformations())

        {
            try {
                if (selecItem1.equals("Nouveau"))//nouveau fournisseur
                {
                    try {
                        String sql = "INSERT INTO fournisseur(IdFournisseur,NomFournisseur,AdresseFourn,Ntel) VALUES (?,?,?,?) ";
                        cnx = new ConnexionBDD().ConnexionDB();
                        PreparedStatement ps = cnx.prepareStatement(sql);
                        ps.setInt(1,Integer.valueOf(idF.getText()));
                        ps.setString(2,nomF.getText());
                        ps.setString(3,adrF.getText());
                        ps.setString(4,telF.getText());
                        ps.executeUpdate();
                        cnx.close();
                        idFour=Integer.valueOf(idF.getText());
                    } catch (SQLException ex) {
                       information(4);
                    }
                }
                else if(selecItem1.equals("Ancien"))//fournisseur existe déja dans la bdd
                {
                    String  nomIdItem[] = new String[2];
                    nomIdItem=nomAnF.getSelectionModel().getSelectedItem().split("|");
                    
                    idFour=Integer.valueOf(nomIdItem[0]);
                }
                
                //Traitement des produits achetés
                if(selecItem2.equals("Nouveau")) //Nouveau produit acheté
                {
                    try {
                        String sql = "INSERT INTO produit(IdProduit,Libelle) VALUES (?,?)";
                        cnx = new ConnexionBDD().ConnexionDB();
                        PreparedStatement ps = cnx.prepareStatement(sql);
                        ps.setInt(1,Integer.valueOf(idP.getText()));
                        ps.setString(2,nomP.getText());
                        ps.executeUpdate();
                        cnx.close();
                        idProd=Integer.valueOf(idP.getText());
                        if(selecItem3.equals("Outillage") || selecItem3.equals("Pièce"))
                        {
                            sql = "INSERT INTO piece(Ref,Libelle,Qte,Type) VALUES (?,?,?,?)";
                            cnx = new ConnexionBDD().ConnexionDB();
                            ps = cnx.prepareStatement(sql);
                            ps.setInt(1,Integer.valueOf(idP.getText()));
                            ps.setString(2,nomP.getText());
                            ps.setString(3,qte.getText());
                            if(selecItem3.equals("Outillage"))
                                ps.setString(4,"Outillage");
                            else if(selecItem3.equals("Pièce"))
                                ps.setString(4,"Pièce");
                            ps.executeUpdate();
                            cnx.close();
                        }
                    } catch (SQLException ex) {
                        information(5);
                    }
                    
                    
                }
                else if(selecItem2.equals("Ancien")) // Produit existe déja dans la bdd
                {
                    String [] nomIdItem = new String [2];
                    nomIdItem=nomAnP.getSelectionModel().getSelectedItem().split("|");
                    idProd=Integer.valueOf(nomIdItem[0]);
                    if(selecItem3.equals("Outillage") || selecItem3.equals("Pièce"))
                    {
                        try {
                            String sql="UPDATE piece SET Qte=Qte+'"+Integer.valueOf(qte.getText())+"' WHERE Ref='"+idProd+"'";
                            cnx = new ConnexionBDD().ConnexionDB();
                            Statement st = cnx.createStatement();
                            st.executeUpdate(sql);
                            cnx.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Facture.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                String sql = "SELECT DATE(NOW()) AS dateNow";
                cnx = new ConnexionBDD().ConnexionDB();
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql);
                String dateNow=null;
                while(rs.next())
                {
                    dateNow=rs.getString("dateNow");
                }
                String query="INSERT INTO traceachat(IdFournisseur,IdProduit,Date,prixUnit,Remise,Autre,MontantVerse,Qte)VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement ps_q = null;
                ps_q=cnx.prepareStatement(query);
                ps_q.setInt(1, idFour);
                ps_q.setInt(2, idProd);
                ps_q.setString(3,dateNow);
                ps_q.setInt(4,Integer.valueOf(prixUnit.getText()));
                ps_q.setFloat(5,Float.valueOf(remise.getText()));
                ps_q.setInt(6,Integer.valueOf(autreCharge.getText()));
                ps_q.setInt(7,Integer.valueOf(vers.getText()));
                ps_q.setInt(8,Integer.valueOf(qte.getText()));
                ps_q.executeUpdate();
                cnx.close();
                openAfterClose(event);
                InterDepenses.stage_fact.close();
                new InterDepenses().actualiser();
            } catch (SQLException ex) {
                Logger.getLogger(Facture.class.getName()).log(Level.SEVERE, null, ex);
            }
           
}

        
        
    }
    private void maxIdProd() throws SQLException
    {
        String sql = "SELECT MAX(IdProduit) AS maxId FROM produit";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int maxId=0;
        while(rs.next())
        {
            maxId=rs.getInt("maxId");
        }
        cnx.close();
        idP.setText(String.valueOf(maxId+1));
    }
    
    private void maxIdFourn() throws SQLException
    {
        String sql = "SELECT MAX(IdFournisseur) AS maxId FROM fournisseur";
        cnx = new ConnexionBDD().ConnexionDB();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int maxId=0;
        while(rs.next())
        {   
            maxId=rs.getInt("maxId");
        }
        cnx.close();
        idF.setText(String.valueOf(maxId+1));
    } 

    @FXML
    private void montTotal(KeyEvent event) 
    {   
        try{
            if (!qte.getText().equals("") || !prixUnit.getText().equals(""))
            {
                int montTot=0;                
                montTot = Integer.valueOf(prixUnit.getText())*Integer.valueOf(qte.getText());        
                montTotal.setText(String.valueOf(montTot));
                remise.setEditable(true);
            }
            else if (qte.getText().equals("") && prixUnit.getText().equals(""))
            {
                montTotal.setText("");
                remise.setEditable(false);
            }
                
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void calculRemise(KeyEvent event) 
    {
        try
        {
            float montAvecRem=0;
            montAvecRem=Integer.valueOf(montTotal.getText())-Integer.valueOf(montTotal.getText())*(Float.valueOf(remise.getText())/100);
            montNet.setText(String.valueOf(montAvecRem));
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void calculAutre(KeyEvent event) 
    {
        try
        {
            float montAcRem=0;
            float montTotNet=0;
            montAcRem=Integer.valueOf(montTotal.getText())-Integer.valueOf(montTotal.getText())*(Float.valueOf(remise.getText())/100);
            
            montTotNet=montAcRem+Float.valueOf(autreCharge.getText());
            montNet.setText(String.valueOf(montTotNet));
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void calculMontRest(KeyEvent event) 
    {   
        try
        {
            if(vers.equals(""))
            {
                montRest.setText("0");
            }
            double montRestant=0;
            
            montRestant=Double.valueOf(montNet.getText())-Double.valueOf(vers.getText());

            montRest.setText(String.valueOf(montRestant));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cherchFournisseur(KeyEvent event) throws SQLException 
    {
        try
        {String sql = "SELECT IdFournisseur,NomFournisseur FROM fournisseur "
                + "WHERE  NomFournisseur LIKE '%"+nomAnF.getValue()+"%'";
        cnx = new ConnexionBDD().ConnexionDB(); 
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        nomAnF.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            nomAnF.setVisibleRowCount(i);
            nomAnF.getItems().add(rs.getInt("IdFournisseur")+"|"+rs.getString("NomFournisseur"));
            nomAnF.show();
        }
        cnx.close();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cherchProduit(KeyEvent event) throws SQLException 
    {   
        try
        {String sql = "SELECT IdProduit,Libelle FROM produit "
                + "WHERE  Libelle LIKE '%"+nomAnP.getValue()+"%'";
        cnx = new ConnexionBDD().ConnexionDB(); 
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        nomAnP.getItems().clear();
        int i=0;
        while(rs.next())
        {   
            i++;
            nomAnP.setVisibleRowCount(i);
            nomAnP.getItems().add(rs.getInt("IdProduit")+"|"+rs.getString("Libelle"));
            nomAnP.show();
        }
        cnx.close();}
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean verifieFournisseur()
    {    boolean b=true;
        if(rbNvF.isSelected())
        {
        if(nomF.getText().equals(""))
        {
          nomF.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;  
        }
        if(adrF.getText().equals(""))
        {
         adrF.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;  
         
        }
        if(telF.getText().equals("")||!VerifieNum(telF.getText())||telF.getText().length()<9)
        {
        telF.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;              
        }}
        
        else if(rbAnF.isSelected())
        {
            if(nomAnF.getSelectionModel().isEmpty())
            {
             nomAnF.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;                  
            }
        }
        else
        {
          information(1);  
        }
        return b;
    }
    
    public boolean verifieProduit()
    {    boolean b = true;
        if(rbOutil.isSelected()||rbPièce.isSelected()||rbAutre.isSelected())
        {
            if(rbNvP.isSelected())
            {
                if(nomP.getText().equals(""))
                {
                 nomP.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;   
                }                
            }
            else if(rbAnP.isSelected())
            {
                if(nomAnP.getSelectionModel().isEmpty())
                {
                 nomAnP.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;   
                }
            }
            else
            {
              b=false;
              information(3);  
            }
        }
        else
        { 
            b=false;
            information(2);
        }
        return b;
    }
    
    public boolean verifieDetailFacture()
    {   boolean b=true;
        if(prixUnit.getText().isEmpty()||!VerNumbers(prixUnit.getText()))
        {
          prixUnit.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;  
        }
        if(qte.getText().equals("")||!VerNumbers(qte.getText()))
        {
          qte.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;  
        }

        if(remise.getText().equals(""))
        {
        remise.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;
        }
        if(autreCharge.getText().equals(""))
        {
         autreCharge.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;
        }
        if(vers.getText().equals("")||!verifeVers())
        {
          vers.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY))); b=false;  
        }
        return b;
    }
    
     public boolean VerifieNum(String txt)
    {
        String regx="[0][0-9]{9}";
        Pattern p = Pattern.compile(regx);
        if(txt.matches(regx))
        {
            return true;
        }
        else return false;
    } 
     
       public void textedit(String text)
    {
        if(text.length()>9)
        {
           String txt = telF.getText(0, 9);
           telF.setText(txt);
           telF.positionCaret(9);
           
        }   
    }
       
        public boolean VerNumbers(String txt)
    {
        String regx ="[0-9]*[1-9]+[0-9]*";
        Pattern p = Pattern.compile(regx);
        if(txt.matches(regx))
        {
            return true;
        }
        else return false;
    }
    
   public boolean verifieInformations()
   {   boolean b = false;
       if(verifieFournisseur())
       {
        if(verifieProduit()) 
        {
            if(verifieDetailFacture())
            {
                if(confirmation())
                { b=true;information(6);}
                
            }
            else b = false;
        }
        else
        {
        b=false;
        verifieDetailFacture();
        }
       }
       
       else if(!verifieFournisseur())
       {
         verifieProduit();  
         verifieDetailFacture();
         b=false;
       }
       return b;
   }

    @FXML
    private void LettreContrainte(KeyEvent event) {
        String c = event.getCharacter();
         String regex="[^a-z]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }
    }

    @FXML
    private void ChiffresContrainte(KeyEvent event) {
        String c = event.getCharacter();       
         String regex="[^0-9]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }
    }

    @FXML
    private void TelContrainte(KeyEvent event) {
         String c = event.getCharacter();
        System.out.print(c);
        String text = telF.getText();
        String regex="[^0-9&&[^-]]";
        Pattern p = Pattern.compile(regex);
       if (c.matches(regex))
       {
        event.consume();
       }
      textedit(text);
    }
    
     public void information(int i)
   {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       Image im = new Image("/Icones/icons8-information-40.png"); 
       alert.setGraphic(new ImageView(im));
       if(i==1)
       {alert.setTitle("Fournisseur");alert.setContentText("Verifiez le fournisseur");}
       else if(i==2)
       {alert.setTitle("Produit");alert.setContentText("Choissiser le produit");}
        else if(i==3)
       {alert.setTitle("Produit");alert.setContentText("verfiez");}
       
        else if(i==4)
        {
         alert.setTitle("Produit");alert.setContentText("Fournisseur existe déja");   
        } 
        else if(i==5)
        {
         alert.setTitle("Produit");alert.setContentText("Produit existe déja");    
        }
        else if(i==6)
        { alert.setTitle("Produit");alert.setContentText("Produit Ajouté");   }
       
        Optional <ButtonType> action = alert.showAndWait();
      
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          alert.close();
      }
   }
     
      public boolean confirmation()
  {
      
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setHeaderText(null);
      Image im = new Image("/Icones/icons8-plus-40.png"); 
      alert.setGraphic(new ImageView(im));
      alert.setTitle("Produit");alert.setContentText("Ajouter nouveau produit ?");alert.setGraphic(new ImageView(im));
      
      Optional <ButtonType> action = alert.showAndWait();
      
      alert.setWidth(50);
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
     
  }
      
      public boolean verifeVers()
      {
          double a =Double.parseDouble(montNet.getText());
          double b =Double.parseDouble(vers.getText());

                  
          if(a<b)
          {
              return false;
          }
          else return true;
      }

    @FXML
    private void QteContrainte(KeyEvent event) {
         String c = event.getCharacter();       
         String regex="[^0-9]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }
    }
        public void openAfterClose(ActionEvent event) throws IOException
     {
      Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      stage.close();  
     }  

    @FXML
    private void fermer(MouseEvent event) 
    {
       InterDepenses.stage_fact.close();
    }
        
}
