package tableau;


public class TabRecette 
{
    String date;
    int recette;
    
    public TabRecette(String Date,int Recette)
    {
        date=Date;
        recette=Recette;
    }
    
    public String getDateRev()
    {
        return date;
    }
    public int getRecette()
    {
        return recette;
    }
}
