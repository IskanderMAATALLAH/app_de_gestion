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
public class Compte {
    String Numutil;
    String Motdepasse;
    String Contrat;
    String Poste;
    public Compte(String Contrat, String Poste,String Numutil,String Motdepasse)
    {
        this.Numutil=Numutil;
        this.Motdepasse=Motdepasse;
        this.Contrat=Contrat;
        this.Poste=Poste;
    }
    
    public String getNumutil()
    {
        return Numutil;
    }
     public String getMotdepasse()
    {
        return Motdepasse;
    }
      public String getContrat()
    {
        return Contrat;
    }
       public String getPoste()
    {
        return Poste;
    }
    
}
