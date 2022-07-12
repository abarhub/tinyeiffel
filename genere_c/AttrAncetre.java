package tinyeiffel.genere_c;

import tinyeiffel.ast.*;

public class AttrAncetre implements Traite
{
	public AttrAncetre(NomFeature nom,Type type,int no)
	{
		assert(nom!=null);
		assert(type!=null);
		assert(no>=0);
		this.nom=nom;
		this.type=type;
		this.no=no;
	}

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	protected boolean traite=false;

	public NomFeature nom;
	public Type type;
	public int no;
}
