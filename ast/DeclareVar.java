package tinyeiffel.ast;

public class DeclareVar extends Declare_entite implements Traite
{
	public DeclareVar(String nom,Type type)
	{
		this.nom=nom;
		this.type=type;
	}

        public void set_token(Token tnom)
        {
          this.tnom=tnom;
        }

        public Position debut()
        {
          return tnom.debut();
        }

	public void check_egal(DeclareVar d)
	{
		assert(d!=null);
		assert(d instanceof DeclareVar);
		assert(nom.equals(d.nom));
		type.check_egal(d.type);
	}

	public String getNom()
	{
		return nom;
	}
	
	public Type getType()
	{
		return type;
	}

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	public String toString2()
	{
		return nom+":"+type.toString2();
	}

	public boolean equals(Object o)
	{
		if(o==null||!(o instanceof DeclareVar))
		{
			return false;
		}
		else
		{
			DeclareVar d;
			d=(DeclareVar)o;
			return nom.equalsIgnoreCase(d.nom);
		}
	}
	
	protected boolean traite=false;

	public String nom;
	public Type type;

    public Token tnom;
}