/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableau;

/**
 *
 * @author OFFICE
 */
public class Congé {
     String NCon;
    String IdPer;
     String Adresse;
    String DateDeb;
    
     String Titre;
     String Dater;
    
    public Congé(String NCon,String IdPer,String Adresse,String DateDeb,String Titre,String Dater)
    {
      super();
      this.NCon=NCon;
      this.IdPer=IdPer;
      this.Adresse=Adresse;
      this.DateDeb=DateDeb;
    
      this.Titre=Titre;
      this.Dater=Dater;
     
    }
    public String getNCon()
    {
        return NCon;
    }
     public String getDateDeb()
    {
        return DateDeb;
    }
      public String getIdPer()
    {
        return IdPer;
    }
       public String getAdresse()
    {
        return Adresse;
    }

      public String getTitre()
    {
        return Titre;
    }
      public String getDater()
    {
        return Dater;
    }
    
}
