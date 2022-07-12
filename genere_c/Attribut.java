package tinyeiffel.genere_c;

import tinyeiffel.ast.*;

public class Attribut implements Traite
{
	public Attribut(NomFeature nom,Type type)
	{
		assert(nom!=null);
		assert(type!=null);
		this.nom=nom;
		this.type=type;
	}

	public void ajoute_ancetre(NomFeature nom,Type type,int no)
	{
		assert(nom!=null);
		assert(type!=null);
		assert(no>=0);
		AttrAncetre attr=new AttrAncetre(nom,type,no);
		if(ancetre==null)
		{
			ancetre=new AttrAncetre[1];
			ancetre[0]=attr;
		}
		else
		{
			AttrAncetre tab[];
			tab=new AttrAncetre[ancetre.length+1];
			int i;
			for(i=0;i<ancetre.length;i++)
				tab[i]=ancetre[i];
			tab[i]=attr;
			ancetre=tab;
		}
	}

	public void ajoute_descendant(NomFeature nom,Type type,int no)
	{
		assert(nom!=null);
		assert(type!=null);
		assert(no>=0);
		AttrAncetre attr=new AttrAncetre(nom,type,no);
		if(descendant==null)
		{
			descendant=new AttrAncetre[1];
			descendant[0]=attr;
		}
		else
		{
			AttrAncetre tab[];
			tab=new AttrAncetre[descendant.length+1];
			int i;
			for(i=0;i<descendant.length;i++)
				tab[i]=descendant[i];
			tab[i]=attr;
			descendant=tab;
		}
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
	public AttrAncetre ancetre[],descendant[];
	public Feature feature_directe,feature_reel;
}
