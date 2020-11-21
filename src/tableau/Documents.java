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
public class Documents {
    
    String NDoc;
    String Descreption;
    String Date;
    public Documents(String NDoc,String Descreption,String Date)
    {
        super();
        this.NDoc=NDoc;
        this.Descreption=Descreption;
        this.Date=Date;
    }
    public String getNDoc()
    {
        return NDoc;
    }
    
     public String getDescreption()
    {
        return Descreption;
    }
      public String getDate()
    {
        return Date;
    }
}
