package tinyeiffel.interpreteur2;

public class EReal extends EObjetSpecial {

	public EReal(EType t,float val) {
		super(t);
		this.val=val;
	}

	public EObjet action(String nom, EObjet[] param) {
		// TODO Auto-generated method stub
		return null;
	}

	public float getValeur()
	{
		return val;
	}
	
	public void setValeur(float f)
	{
		val=f;
	}
	
	private float val;
}
