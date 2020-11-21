package tableau;

   
public class TabOutillage {
   
    public  String Outillage;
    public  String Utilisateur;
    public  String Date_remise;
    public  String Date_restitution;
   
public TabOutillage(String outillage ,String utilisateur,String dateRemise,String dateRest)
    {
        super();
        this.Outillage=outillage;
        this.Utilisateur=utilisateur;
        this.Date_remise=dateRemise;
        this.Date_restitution=dateRest;
    }

    public String getOutillage()
    {
     return Outillage;   
    }
    public String getUtilisateur()
    {
        return Utilisateur;
    }
    public String getDate_remise()
    {
        return Date_remise;
    }
    public String getDate_restitution()
    {
        return Date_restitution;
    }
    
}
