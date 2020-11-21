/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableau;

/**
 *
 * @author user
 */
public class TabPointage_2{
   public  String NContrat_2;
   public  String Prénom_2;
   public  String Nom_2;
   public  String Date_de_travail_2;
    
    public TabPointage_2(String NContrat_2 ,String Prénom_2,String Nom_2,String Date_de_travail_2)
    {
        super();
        this.NContrat_2=NContrat_2;
        this.Prénom_2=Prénom_2;
        this.Nom_2=Nom_2;
        this.Date_de_travail_2=Date_de_travail_2;
    }
    
    public String getNContrat_2()
    {
     return NContrat_2;   
    }
    public String getPrénom_2()
    {
        return Prénom_2;
    }
    public String getNom_2()
    {
        return Nom_2;
    }
    public String getDate_de_travail_2()
    {
        return Date_de_travail_2;
    }
}
