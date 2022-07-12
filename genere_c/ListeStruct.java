package tinyeiffel.genere_c;

import java.util.*;
import tinyeiffel.ast.*;

public class ListeStruct
{
	public ListeStruct(String nom,boolean tableau,String type)
	{
		assert(nom!=null);
		this.nom=nom;
		liste=new Vector();
		liste_str=new Vector();
		this.tableau=tableau;
		this.type=type;
	}

	public Traite elementAt(int i)
	{
		assert(i>=0);
		assert(i<liste.size());
		return (Traite)liste.elementAt(i);
	}

	public int ajoute(Traite o)
	{
		assert(o!=null);
		assert(liste.size()==liste_str.size());
		int res;
		res=liste.size();
		liste.addElement(o);
		o.set_traite(true);
		liste_str.addElement("");
		return res;
	}

	public int associe(Traite o,String text)
	{
		assert(o!=null);
		assert(liste.size()==liste_str.size());
		int res;
		res=no_objet(o);
		assert(res>=0);
		assert(res<size());
		//liste.addElement(o);
		liste_str.setElementAt(text,res);
		return res;
	}

	public int addElement(Object o,String text)
	{
		assert(o!=null);
		assert(liste.size()==liste_str.size());
		int res;
		res=liste.size();
		liste.addElement(o);
		if(o instanceof Traite)
		{
			Traite t=(Traite)o;
			t.set_traite(true);
		}
		liste_str.addElement(text);
		return res;
	}

	public String toString1()
	{
		int i;
		StringBuffer res;
		if(liste_str.size()==0)
			return "";
		res=new StringBuffer(type);
		res.append(" ");
		res.append(nom);
		res.append("[");
		res.append(liste_str.size());
		res.append("]={");
		res.append(retour_ligne());
		for(i=0;i<liste_str.size();i++)
		{
			if(i>0)
			{
				res.append(",");
				res.append(retour_ligne());
			}
			res.append("{");
			res.append(liste_str.elementAt(i));
			res.append("}");
		}
		res.append("};");
		return res.toString();
	}

	public String toString2()
	{
		int i;
		StringBuffer res=new StringBuffer("");
		String n;
		if(liste_str.size()==0)
			return "";
		for(i=0;i<liste_str.size();i++)
		{
			//if(i>0)
			//	res+=",";
			n=nom;
			res.append(type);
			res.append(" *");
			res.append(n);
			res.append((i));
			res.append("[");
			res.append(/*liste_str.size()+*/"]={");
			res.append(liste_str.elementAt(i));
			res.append("};");
			res.append(retour_ligne());
		}
		//res+="}";
		return res.toString();
	}

	public String toString3()
	{
		int i;
		String res="extern "+type+" "+nom+"["+liste_str.size()+"];"+retour_ligne();
		return res;
	}

	public String getNom()
	{
		return nom;
	}

	public boolean est_tableau()
	{
		return tableau;
	}

	protected String retour_ligne()
	{
		return "\r\n";
	}

	public int size()
	{
		return liste.size();
	}

	public int no_objet(Traite o)
	{
		assert(o!=null);
		/*if(o instanceof Classe)
		{// a enlever
			Classe cl,cl2;
			cl=(Classe)o;
			for(int i=0;i<liste.size();i++)
			{
				cl2=(Classe)liste.elementAt(i);
				if(cl==cl2||cl.nom.compareToIgnoreCase(cl2.nom)==0)
					return i;
			}
		}
		else*/
		if(o.get_traite())
		{// il est deja referencé 
			Date d1,d2;
			boolean trouve;
			long t;
			int i;
			trouve=false;
			d1=new Date();
			for(i=0;i<liste.size();i++)
			{
				if(liste.elementAt(i)==o)
				{
					trouve=true;
					break;
				}
			}
			d2=new Date();
			t=d2.getTime()-d1.getTime();
			temp_total+=t;
			if(trouve)
				return i;
		}
		return -1;
	}

	public void affiche_temp_total()
	{
		System.out.println("Temp total recherche="+temp_total+" ms");
	}

	protected static int temp_total=0;
	protected Vector liste;
	protected Vector liste_str;
	protected String nom,type;
	protected boolean tableau;
}
