package tableau;

public class rendezvs
{
	public int Num;
	public String Date;
	public String Heure;
        public String avec;
	public String lieu;

	public rendezvs(int Num, String Date, String Heure,String avec, String lieu)
	{
		super();
		this.Num = Num;
		this.Date = Date;
		this.Heure =Heure;
                this.avec=avec;
                this.lieu=lieu;
               
	}

	public int getNum() {
		return Num;
	}

	public String getDate() {
		return Date;
	}

	public String getHeure() {
		return Heure;
	}
        public String getAvec() {
		return avec;
	}

	public String getLieu() {
		return lieu;
	}
       

	
	
	
	

}
