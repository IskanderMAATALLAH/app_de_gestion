package tableau;


public class TabPaiement {
   public  String NContrat;
   public  String Prénom;
   public  String Nom;
   public  int nbrJour;
   public  int avPaie;
   
   public TabPaiement(String NContrat ,String Prénom,String Nom,int nbrJour,int avPaie)
    {
        super();
        this.NContrat=NContrat;
        this.Prénom=Prénom;
        this.Nom=Nom;
        this.nbrJour=nbrJour;
        this.avPaie=avPaie;
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
    public int getNbrJour()
    {
        return nbrJour;
    }
    public int getAvPaie()
    {
        return avPaie;
    }
    
}
