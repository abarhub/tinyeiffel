package tinyeiffel.interpreteur2;

public class EString extends EObjetSpecial {

	public EString(EType t,String s) {
		super(t);
		this.val=s;
	}

	public EObjet action(String nom, EObjet[] param) {
		return null;
	}

	public String getValeur()
	{
		return val;
	}
	
	public void setValeur(String s)
	{
		val=s;
	}
	
	private String val;
}
