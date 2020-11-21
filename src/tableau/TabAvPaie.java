package tableau;


public class TabAvPaie 
{   
    String numeroContrat;
    String prenom;
    String nom;
    String date;
    int montant;
   
    public TabAvPaie(String numCont,String prenom,String nom,String date,int montant)
    {
        numeroContrat=numCont;
        this.prenom=prenom;
        this.nom=nom;
        this.date=date;
        this.montant=montant;
    }
    
    public String getNumeroContrat()
    {
        return numeroContrat;
    }
    
    public String getPrenom()
    {
        return prenom;
    }
    
    public String getNom()
    {
        return nom;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public int getMontant()
    {
        return montant;
    }
    
}
