package tinyeiffel.ast;

import java.util.*;

public class Indexing implements Traite
{
	public Indexing(String nom,Vector liste)
	{
		this.liste=new String[liste.size()];
		liste.copyInto(this.liste);
		this.nom=nom;
	}

	public void check_egal(Indexing index)
	{
		assert(index!=null);
		assert(nom==index.nom);
		int i;
		assert(liste.length==index.liste.length);
		for(i=0;i<liste.length;i++)
		{
			assert(liste[i]==index.liste[i]);
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

	public String liste[];
	public String nom;
}