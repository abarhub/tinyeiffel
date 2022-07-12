package tinyeiffel.interpreteur2;

public class EType {

	public EType(boolean expended,String nom,EType param[])
	{
		this.expended=expended;
		this.nom=nom;
		this.param=param;
	}
	
	public String getNom()
	{
		return nom;
	}
	
	public EType getParam(int i)
	{
		assert(param!=null);
		assert(param.length>0);
		assert(i>=0);
		assert(i<param.length);
		return param[i];
	}
	
	public int getParamSize()
	{
		if(param==null||param.length==0)
			return 0;
		else
			return param.length;
	}
	
	public boolean getExpended()
	{
		return expended;
	}
	
	private boolean expended;
	private String nom;
	private EType param[];
}
