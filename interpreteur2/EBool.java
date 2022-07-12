package tinyeiffel.interpreteur2;

public class EBool extends EObjetSpecial {

	public EBool(EType t,boolean b) {
		super(t);
		this.val=b;
	}

	public EObjet action(String nom, EObjet[] param) {
		
		return null;
	}

	public boolean getValeur()
	{
		return val;
	}
	
	public void setValeur(boolean b)
	{
		this.val=b;
	}
	
	private boolean val;
}
