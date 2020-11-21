package tableau;



public class TabPointage {
   public  String NContrat;
   public  String Prénom;
   public  String Nom;
   public  String Date_de_travail;
   public String Lieu_de_travail;
    
    public TabPointage(String NContrat ,String Prénom,String Nom,String Date_de_travail,String Lieu_de_travail)
    {
        super();
        this.NContrat=NContrat;
        this.Prénom=Prénom;
        this.Nom=Nom;
        this.Date_de_travail=Date_de_travail;
        this.Lieu_de_travail=Lieu_de_travail;
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
    public String getLieu_de_travail()
    {
        return Lieu_de_travail;
    }
}
