package tableau;

public class events
{
	public int ID;
	public String Intitulé;
	public String Debut;
        public String Fin;
	

	public events(int ID, String Intitulé, String Debut,String Fin)
	{
		super();
		this.ID = ID;
		this.Intitulé = Intitulé;
		this.Debut =Debut;
                this.Fin=Fin;
             
               
	}

	public int getID() {
		return ID;
	}

	public String getIntitulé() {
		return Intitulé;
	}

	public String getDebut() {
		return Debut;
	}
        public String getFin() {
		return Fin;
	}


       

	
	
	
	

}
