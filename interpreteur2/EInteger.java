package tinyeiffel.interpreteur2;

public class EInteger extends EObjetSpecial {

	public EInteger(EType t,int i) {
		super(t);
		this.val=i;
	}

	public EObjet action(String nom, EObjet[] param) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getValeur()
	{
		return val;
	}
	
	public void setValeur(int i)
	{
		this.val=i;
	}
	
	private int val;
}
