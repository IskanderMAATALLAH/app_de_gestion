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
public class AdminPointage {
    
      public  String NContrat;
   public  String Prénom;
   public  String Nom;
   public  String Date_de_travail;
  
    
    public AdminPointage(String NContrat ,String Prénom,String Nom,String Date_de_travail)
    {
        super();
        this.NContrat=NContrat;
        this.Prénom=Prénom;
        this.Nom=Nom;
        this.Date_de_travail=Date_de_travail;
       
    }
    
    public String getNContrat()
    {
     return NContrat;   
    }
    public String getPrénom()
    {
        return Prénom;
    }
    public String getNom()
    {
        return Nom;
    }
    public String getDate_de_travail()
    {
        return Date_de_travail;
    }
 
}
