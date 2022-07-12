package tinyeiffel.interpreteur2;

public abstract class EObjet {

	public EObjet(EType t)
	{
		assert(t!=null);
		this.type=t;
	}
	
	public EType getType()
	{
		return type;
	}
	
	protected EType type;
}
