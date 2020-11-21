package Controle;

import Main.ConnexionBDD;
import Reports.Suite;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.controlsfx.control.Notifications;



public class AjoutModifPersonnel implements Initializable {
   Connection cnx =null;
     PreparedStatement ps;
  ResultSet rs=null;
    @FXML
    private JFXTextField numContrat;
    @FXML
    private JFXTextField prénom;
    @FXML
    private JFXTextField nom;
    @FXML
    private JFXDatePicker dateNaiss;
    @FXML
    private JFXTextField numTel ;
    @FXML
    private JFXTextField adresse;
    @FXML
    private JFXDatePicker dateDebCont;
    @FXML
    private JFXComboBox<Integer> duréeCont;
    @FXML
    private JFXTextField salaire;
    @FXML
    private JFXComboBox<String> poste;
    public static int ModAjout=1;
    @FXML
    private ImageView ImagePers;
    private FileChooser fileChoose;
    private File file;
    private final Desktop desk = Desktop.getDesktop();
    public String UrlImg="";

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
     affDuréeCont();
     affListePoste();
       try {
           GenerateurNumCont();
       } catch (SQLException ex) {
           Logger.getLogger(AjoutModifPersonnel.class.getName()).log(Level.SEVERE, null, ex);
       }
     if(ModAjout==1)
     {
         try {
             String sql = "Select * from personnel,poste where NumContrat='"+ListePersonnel.num+"' AND personnel.IdPoste=poste.IdPoste";
             cnx = new ConnexionBDD().ConnexionDB();
             Statement st =cnx.createStatement();
             ResultSet rs = st.executeQuery(sql);
             while(rs.next())
             {
               numContrat.setText(rs.getString("NumContrat"));
               numContrat.setDisable(true);
               prénom.setText(rs.getString("Prenom"));
               nom.setText(rs.getString("Nom"));
               
               LocalDate ld = LocalDate.parse(rs.getString("DateNaiss"));
               dateNaiss.setValue(ld);
               numTel.setText(rs.getString("Téléphone"));
               adresse.setText(rs.getString("Adresse"));
               ld = LocalDate.parse(rs.getString("DateDebCon"));
               dateDebCont.setValue(ld);
               duréeCont.setValue(Integer.getInteger(rs.getString("Durée")));
               salaire.setText(rs.getString("Salaire"));
               duréeCont.setValue(rs.getInt("Durée"));
               poste.setValue(rs.getString("Libelle"));
             }
             cnx.close();
         } catch (SQLException ex) {
             Logger.getLogger(AjoutModifPersonnel.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
        
    }  

    @FXML
    private void ajouterEmployé(ActionEvent event) 
    {   
        try {
            String sql=null;
            
            
            if (ModAjout==0)
            {   
                if(this.VerificationComplete(2))
                {
                sql = "INSERT INTO personnel(NumContrat,Nom,Prenom,DateNaiss,Adresse,Téléphone,DateDebCon,Durée,Salaire,IdPoste,image) VALUES (?,?,?,?,?,?,?,?,?,?,?)"; 
                cnx = new ConnexionBDD().ConnexionDB();
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setString(1,numContrat.getText());
                ps.setString(2,nom.getText().toUpperCase());
                ps.setString(3,prénom.getText());
                ps.setString(4,changeFormatDate(dateNaiss));
                ps.setString(5,adresse.getText());
                ps.setString(6,numTel.getText());
                ps.setString(7,changeFormatDate(dateDebCont));
                ps.setInt(8,duréeCont.getSelectionModel().getSelectedIndex()+1);
                ps.setInt(9,Integer.valueOf(salaire.getText()));
                ps.setInt(10,codePoste(poste.getSelectionModel().getSelectedItem())+1);
                ps.setString(11, UrlImg);

                ps.executeUpdate();
                ListePersonnel.stage_nvlEmp.close();
                String date =changeFormatDate(dateDebCont);
                String dur =Integer.toString(duréeCont.getSelectionModel().getSelectedIndex());
                 
                if(poste.getSelectionModel().getSelectedItem().equals("Opérateur"))
                {
                Reports.ContratOP o = new Reports.ContratOP();
                o.showReport(numContrat.getText(),date,dur); 
                Suite s = new Suite();
                s.showReport(numContrat.getText(),date,dur);
                
                }
                else if(poste.getSelectionModel().getSelectedItem().equals("Caissier"))
                {
                    Reports.ContratCaissier o = new Reports.ContratCaissier();
                    o.showReport(numContrat.getText(),date,dur);
                    Suite s = new Suite();
                    s.showReport(numContrat.getText(),date,dur);
                }             
            }
                cnx.close();
            }
            else if(ModAjout==1)
            {   
                if(this.VerificationComplete(3))
                {
                    sql = "UPDATE personnel SET Nom='"+nom.getText().toUpperCase()+"', Prenom='"+prénom.getText()+"', DateNaiss='"+changeFormatDate(dateNaiss)+"', Adresse='"+adresse.getText()+"', Téléphone='"+numTel.getText()+"', DateDebCon='"+changeFormatDate(dateDebCont)+"', Durée='"+(duréeCont.getSelectionModel().getSelectedIndex()+1)+"', Salaire='"+Integer.valueOf(salaire.getText())+"', IdPoste='"+codePoste(poste.getSelectionModel().getSelectedItem())+"',image='"+UrlImg+"' WHERE NumContrat='"+numContrat.getText()+"'";
                    cnx = new ConnexionBDD().ConnexionDB();
                    PreparedStatement ps2 = cnx.prepareStatement(sql);
                    ps2.executeUpdate();
                    cnx.close();
                    ListePersonnel.stage_nvlEmp.close();
                }
            }
         }catch (SQLException ex) {
          System.out.println(ex.getMessage());
        }
        
    }

    private void affDuréeCont() {
         duréeCont.getItems().clear();
          for (int i=1; i<=12; i++)
            duréeCont.getItems().add(i);
    }

    private void affListePoste() {
       poste.getItems().clear();
           String sql = "SELECT * FROM poste WHERE Libelle='Caissier' OR Libelle='Opérateur' OR Libelle='Maintenance' OR Libelle='Sécurité'";
        Statement st;
        try {
            cnx = new ConnexionBDD().ConnexionDB();
            st = cnx.createStatement();
            ResultSet rs= st.executeQuery(sql);
            while (rs.next())
            {
               poste.getItems().add(rs.getString("Libelle")); 
            }
            cnx.close();
        } catch (SQLException ex) {
            
        }
    }
    
    
    private int codePoste(String LibPoste) throws SQLException
    {
        int code=0;
        String sql="SELECT IdPoste FROM poste WHERE Libelle='"+LibPoste+"'";
        cnx = new ConnexionBDD().ConnexionDB();;
        Statement st=cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
           code=rs.getInt("IdPoste"); 
        }
        cnx.close();
        return code;
    }
    public String changeFormatDate(DatePicker date)
    {   
        String dateNvFormat=null;
        try
        {
            LocalDate ld = date.getValue();
            dateNvFormat=ld.getYear()+"-"+ld.getMonthValue()+"-"+ld.getDayOfMonth();
        }catch(Exception ex)
        {
            
        }
        return dateNvFormat;
        
    }
    
    public void act()
  {
      Notifications nt = Notifications.create()
              .title("Ajout terminé")
              .text("Vous avez bien ajouté un nouveau compte")
              .graphic(null)
              .hideAfter(Duration.seconds(3))
              .position(Pos.TOP_RIGHT);
      nt.showConfirm();
              
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
          alert.setTitle("Suppression");
          alert.setContentText("Supprimer employer ?");
      }
      else if(i==2)
      {
          alert.setGraphic(new ImageView(im));
          alert.setTitle("Ajout");
          alert.setContentText("Ajouter employer ?");
      }
      else if(i==3)
      {
          alert.setGraphic(new ImageView(im2));
          alert.setTitle("Modification");
          alert.setContentText("Modifier employer ?");
      }
 
      Optional <ButtonType> action = alert.showAndWait();
      if(action.get()==ButtonType.OK)
      {
          return true; 
      }
      else return false;
  }

    @FXML
    private void NuMcOntraint(KeyEvent event) {
       String c = event.getCharacter();
        System.out.print(c);
        String regex="[^0-9&&[^/]]";
        Pattern p = Pattern.compile(regex); 
       if (c.matches(regex))
       {
                event.consume();
       }
    }

    @FXML
    private void PrenomContraint(KeyEvent event) {
       String c = event.getCharacter();
        String regex="[^a-z&&[^ ]]";
        Pattern p = Pattern.compile(regex);
        if(c.matches(regex))
       {
           event.consume();
       } 

        
    }

    @FXML
    private void NomContrainte(KeyEvent event) {
       String c = event.getCharacter();
         String regex="[^a-z]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }

    }

    @FXML
    private void SalaireContrainte(KeyEvent event) {
        String c = event.getCharacter();       
         String regex="[^0-9]";
        Pattern p = Pattern.compile(regex);
         if(c.matches(regex))
       {
           event.consume();
       }

      
    }

    @FXML
    private void NtElContrainte(KeyEvent event) {
        String c = event.getCharacter();
        System.out.print(c);
        String text = numTel.getText();
        String regex="[^0-9&&[^-]]";
        Pattern p = Pattern.compile(regex);
       if (c.matches(regex))
       {
        event.consume();
       }
      textedit(text);

      
    }
    
    public void textedit(String text)
    {
        if(text.length()>12)
        {
           String txt = numTel.getText(0, 12);
           numTel.setText(txt);
           numTel.positionCaret(12);
           
        }   
    }
            
    public boolean VerifieNum(String txt)
    {
        String regx="[0][1-9]{3}([-][0-9]{2}){3}";
        Pattern p = Pattern.compile(regx);
        if(txt.matches(regx))
        {
            return true;
        }
        else return false;
    }  
    public boolean verifeInfo()
    {  boolean b = true;
         Paint value0=Paint.valueOf("F9C7CC");

         if(numContrat.getText().equals(""))
        {   
        numContrat.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(prénom.getText().equals(""))
        {prénom.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;} 
         if(nom.getText().equals(""))
        {nom.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(dateNaiss.equals(null))
        {dateNaiss.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
         if(numTel.getText().equals("")|| !VerifieNum(numTel.getText()))
        {numTel.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(adresse.getText().equals(""))
        {adresse.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
         if(dateDebCont.equals(null))
        {dateDebCont.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(salaire.getText().equals(""))
        {salaire.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(poste.getSelectionModel().isEmpty())
        {poste.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
        if(duréeCont.getSelectionModel().isEmpty())
        {duréeCont.setBackground(new Background(new BackgroundFill(value0, CornerRadii.EMPTY, Insets.EMPTY)));b=false;}
       return b;
    }

   
    
    public boolean VerificationComplete(int i)
    { boolean b=true;
        if(verifeInfo())
        {
          if(confirmation(i))
          {
            b=true; 
            act(i);
          }
          else b=false;
        }
        return b;
    }
        public void act(int i)
  {
      
     Image im = new Image("/Icones/icons8-approbation-40.png");   
      Notifications nt = Notifications.create()
           
              .graphic(new ImageView(im))
              .hideAfter(Duration.seconds(3))
              .position(Pos.CENTER);
      
      if(i==2){nt.title("Personnel"); nt.text("Ajout effectué");}
      else if(i==1) {nt.title("Personnel"); nt.text(" suppression terminée");}
      else if(i==3) {nt.title("Personnel"); nt.text(" Modification terminée");}
      nt.show();

  }

    @FXML
    private void AdresseCont(KeyEvent event) {
        String c = event.getCharacter();
        System.out.print(c);
        String text = numTel.getText();
        String regex="[[^a-z&&[^0-9&&[^ ]]]]";
        Pattern p = Pattern.compile(regex);
       if (c.matches(regex))
       {
        event.consume();
       } 
    }

       @FXML
    private void openImg(MouseEvent event) {
        Stage stage = new Stage();
        fileChoose= new FileChooser();
        fileChoose.getExtensionFilters().addAll(new ExtensionFilter("Image files ","* .jpeg","* .jpg","* .png"));
        file = fileChoose.showOpenDialog(stage);
        if(file!=null)
        {
         UrlImg=file.toURI().toString();
         Image image = new Image(UrlImg,150,150,true,true);
         ImagePers.setImage(image);
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
        ListePersonnel.stage_nvlEmp.close();
    }
    
    public void GenerateurNumCont() throws SQLException
     {
    int newyear = 1;
    SimpleDateFormat formater = null;
    Date aujourdhui = new Date();
    formater = new SimpleDateFormat("dd-MM-yy");
    String s = formater.format(aujourdhui);
    String tabd[] = new String[3];
    tabd=s.split("-");
    String tab[] = new String[2];
    String contrat="111";
    String sql="SELECT NumContrat from personnel";
    ArrayList<String> arrayList = new ArrayList<String>();
    cnx = new ConnexionBDD().ConnexionDB();
    ArrayList<String> arrayList1 = new ArrayList<String>();
    Statement st=cnx.createStatement();
    ResultSet rs = st.executeQuery(sql);
    int numbd =1;
        while (rs.next())
        {
            tab =rs.getString("NumContrat").split("/");
            arrayList1.add((tab[1]));
        }
       Collections.sort(arrayList1);
       String numr =arrayList1.get((arrayList1.size() - 1));
       int num = Integer.parseInt(numr);
       int a =Integer.parseInt(tab[1]);
        rs = st.executeQuery(sql);
         while (rs.next())
        {
            tab =rs.getString("NumContrat").split("/");
            if(a==num)
            {
              if(Integer.parseInt(tab[0])>numbd)
              {
                numbd=Integer.parseInt(tab[0]);
              }
            }
        }
         cnx.close();        
         if(num<Integer.parseInt(tabd[2]))
         {
             contrat ="001/"+Integer.parseInt(tabd[2]);
         }
         else
         {
             numbd=numbd+1;
            if(numbd>1 && numbd<10)
            {
              contrat="00"+numbd+"/"+num;
            }
            else if(numbd>9 && numbd<100)
            {
             contrat="0"+numbd+"/"+num;   
            }
            else 
            {
                contrat=numbd+"/"+num;
            }
         }
         numContrat.setText(contrat);
     }
    
}
    

