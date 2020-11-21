/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableau;

public class Mission {
    String NumM;
    String IdPer;
     String Lieu;
    String Date;
    String DateF;
     String Objet;
    String Moyen;
    public Mission(String NumM,String IdPer,String Lieu,String Date, String DateF,String Objet,String Moyen)
    {
      super();
      this.NumM=NumM;
      this.IdPer=IdPer;
      this.Lieu=Lieu;
      this.Date=Date;
      this.DateF=DateF;
      this.Objet=Objet;
      this.Moyen=Moyen;
    }
    public String getNumM()
    {
        return NumM;
    }
     public String getDate()
    {
        return Date;
    }
      public String getIdPer()
    {
        return IdPer;
    }
       public String getLieu()
    {
        return Lieu;
    }
        public String getObjet()
    {
        return Objet;
    }
         public String getMoyen()
    {
        return Moyen;
    }
         public String getDateF()
         {
             return DateF;
         } 
    
    
    
}
