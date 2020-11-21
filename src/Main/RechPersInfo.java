package Main;

import tableau.TabPointage;



public class RechPersInfo extends TabPointage{
    
    public String poste=null;
    public RechPersInfo(String NContrat, String Prénom, String Nom, String Date_de_travail, String Lieu_de_travail,String poste) 
    {
        super(NContrat, Prénom, Nom, Date_de_travail, Lieu_de_travail);
        this.poste=poste;
    }
    
}
