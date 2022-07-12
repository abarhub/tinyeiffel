package tinyeiffel.ast;

public class Attribut implements Cloneable,Traite
{
	public Attribut(NomFeature nom)
	{
		this.nom=nom;
	}

	public Attribut(NomFeature nom,Classe classe,Feature feature)
	{
		this.nom=nom;
		this.classe=classe;
		this.feature=feature;
	}

	public Attribut(Attribut attr,Heritage heritage)
	{
		this.nom=attr.nom;
		this.heritage=heritage;
		this.classe=attr.classe;
		this.feature=attr.feature;
	}

	public void renome(NomFeature nom)
	{
		nom_de=this.nom;
		this.nom=nom;
	}

	public boolean equals(Object o)
	{
		//System.out.println("test equals attribut");
		if(o instanceof Attribut)
		{
			Attribut a=(Attribut)o;
			return nom.equals(a.nom);
		}
		else if(o instanceof NomFeature)
		{
			NomFeature a=(NomFeature)o;
			return nom.equals(a);
		}
		else if(o instanceof String)
		{
			String a=(String)o;
			return nom.equals(a);
		}

		return false;
	}

	public String toString()
	{
		return nom.toString();
	}

	public Object clone()
	{
		Attribut a=new Attribut(nom);
		a.nom_de=nom_de;
		a.heritage=heritage;
		a.classe=classe;
		a.feature=feature;
		return a;
	}

	/*public boolean meme_attribut(Attribut a)
	{
		
	}*/

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	protected boolean traite=false;

	public NomFeature nom,nom_de;
	public Heritage heritage;
	public Classe classe;
	public Feature feature;
}