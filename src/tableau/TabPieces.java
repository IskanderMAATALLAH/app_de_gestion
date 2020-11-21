package tableau;



public class TabPieces 
{
    public  String Pièce;
    public  int Qte;
    public  String Date;
    public  String Preneur;
    
    public TabPieces(String pièce,int qte,String date,String preneur)
    {
        super();
        this.Pièce=pièce;
        this.Qte=qte;
        this.Date=date;
        this.Preneur=preneur;
    }
        
    
    public String getPièce()
    {
     return Pièce;   
    }
    public int getQte()
    {
        return Qte;
    }
    public String getDate()
    {
        return Date;
    }
    public String getPreneur()
    {
        return Preneur;
    }
}


