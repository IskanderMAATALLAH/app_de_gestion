package tableau;


public class TabFacture 
{   
    String Nom_Four;
    String Produit;
    String Date;
    int Prix_Unit;
    int Qte;
    int Mont_Tot;
    float Rem;
    int autreCharge;
    float Mont_Net;
    int Vers;
    float Mont_Rest;
    
        public TabFacture(String nomFour,String nomProd,String dateAchat,int prixUnitaire,int quantité,int prixTot,float remise,int autre,float montantNet, int versement,float montantRest)
    {
        super();
        
        Nom_Four=nomFour;
        Produit=nomProd;
        Date=dateAchat;
        Prix_Unit=prixUnitaire;
        Qte=quantité;
        Mont_Tot=prixTot;
        Rem=remise;
        autreCharge=autre;
        Mont_Net=montantNet;
        Vers=versement;
        Mont_Rest=montantRest;
    }
    
    public String getNom_Four()
    {
        return Nom_Four;
    }
    
    public String getProduit()
    {
        return Produit;
    }
    public String getDate()
    {
        return Date;
    }
    public int getPrix_Unit()
    {
        return Prix_Unit;
    }
    public int getQte()
    {
        return Qte;
    }
    public int getMont_Tot()
    {
        return Mont_Tot;
    }
    public float getRem()
    {
        return Rem;
    }
    public int getAutreCharge()
    {
        return autreCharge;
    }
    public float getMont_Net()
    {
        return Mont_Net;
    }
    public int getVers()
    {
        return Vers;
    }
    public float getMont_Rest()
    {
        return Mont_Rest;
    }
    
}
