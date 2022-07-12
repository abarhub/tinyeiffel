package tinyeiffel.genere_java;

import java.util.List;
import java.util.Vector;

import tinyeiffel.genere_java.generation.GenereJvm;
import tinyeiffel.intermediaire.CIAttribut;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CIDeclare_Var;
import tinyeiffel.intermediaire.CIListe_Var;
import tinyeiffel.intermediaire.CIRoutine;
import tinyeiffel.intermediaire.CITypeSimple;

public class Environement {

	
	public Environement(
			CIRoutine r,GenereJvm genere,
			CIClasse cl, CIAttribut attr)
	{
		routine=r;
		//this.liste_local=local;
		this.cl=cl;
		this.attr=attr;
		this.genere=genere;
	}
	
	public void contrsuit_local()
	{
		CIListe_Var local,param;
		CIDeclare_Var v;
		int lg;
		//List liste_local;
		int i;
		local=routine.local;
		//param=routine.parametre;
		liste_local=new Vector();
		/*if(param!=null&&param.size()>0)
		{
			for(i=0;i<param.size();i++)
			{
				v=param.elt(i);
				lg=methode_gen.addLocalVariable(v.nom,
					envg.conv_type(v.type),debut,null);
				liste_local.add(lg);
			}
		}*/
		if(local!=null&&local.size()>0)
		{
			for(i=0;i<local.size();i++)
			{
				v=local.elt(i);
				//lg=methode_gen.addLocalVariable(v.nom,
					//envg.conv_type(v.type),debut,null);
				lg=genere.ajoute_var_local(v.nom, v.type);
				liste_local.add(new Integer(lg));
			}
		}
		if(routine.retour!=null)
		{
			v=routine.retour;
			lg=genere.ajoute_var_local(v.nom, v.type);
			//lg=methode_gen.addLocalVariable(v.nom,
			//	envg.conv_type(v.type),debut,null);
			liste_local.add(new Integer(lg));
		}
	}
	
	public int type_var(String nom)
	{
		CIListe_Var liste;
		int i;
		CIDeclare_Var d;
		CIAttribut attr;
		liste=routine.local;
		if(liste!=null)
		{
			d=liste.donne_var(nom);
			if(d!=null)
				return VAR_LOCAL;
		}
		liste=routine.parametre;
		if(liste!=null)
		{
			d=liste.donne_var(nom);
			if(d!=null)
				return VAR_PARAM;
		}
		if(routine.retour!=null)
		{
			if(routine.retour.nom.equalsIgnoreCase(nom))
				return VAR_RETOUR;
		}
		attr=routine.classe.donne_attribut(nom);
		if(attr!=null)
		{
			return VAR_ATTR;
		}
		else
		{
			//Field f,tab[];
			if(genere.contient_attribut(nom))
			{
				return VAR_ATTR;
			}
			/*tab=class_gen.getFields();
			if(tab!=null)
			{
				for(i=0;i<tab.length;i++)
				{
					if(tab[i].getName().equals(nom))
						return VAR_ATTR;
				}
			}*/
			return VAR_INCONNUE;
		}
	}

	public CIDeclare_Var donne_var(String nom) {
		CIListe_Var liste;
		int i;
		CIDeclare_Var d;
		CIAttribut attr;
		liste=routine.local;
		if(liste!=null)
		{
			d=liste.donne_var(nom);
			if(d!=null)
				return d;
		}
		liste=routine.parametre;
		if(liste!=null)
		{
			d=liste.donne_var(nom);
			if(d!=null)
				return d;
		}
		if(routine.retour!=null)
		{
			if(routine.retour.nom.equalsIgnoreCase(nom))
				return routine.retour;
		}
		attr=routine.classe.donne_attribut(nom);
		if(attr!=null)
		{
			CIDeclare_Var d2;
			d2=new CIDeclare_Var(nom,attr.retour,null);
			return d2;
		}
		else
		{
			/*Field f,tab[];
			tab=class_gen.getFields();
			if(tab!=null)
			{
				for(i=0;i<tab.length;i++)
				{
					if(tab[i].getName().equals(nom))
					{
						CIDeclare_Var d2;
						d2=new CIDeclare_Var(nom,new CITypeSimple(false,
							((ObjectType)tab[i].getType()).getClassName(),
							null,null,null),null);
						return d2;
					}
				}
			}*/
			if(genere.contient_attribut(nom))
			{
				String type;
				CIDeclare_Var d2;
				type=genere.donne_type(nom);
				assert(type!=null);
				d2=new CIDeclare_Var(nom,new CITypeSimple(false,
					type,null,null,null),null);
				return d2;
			}
			return null;
		}
	}

	public int donne_no(String nom) {
		CIListe_Var liste;
		int i,no;
		CIDeclare_Var d;
		CIAttribut attr;
		int lg;
		no=1;
		liste=routine.parametre;
		if(liste!=null)
		{
			for(i=0;i<liste.size();i++)
			{
				if(liste.elt(i).nom.equals(nom))
					return no;
				no++;
			}
		}
		/*liste=routine.local;
		if(liste!=null)
		{
			for(i=0;i<liste.size();i++)
			{
				if(liste.elt(i).nom.equals(nom))
					return no;
				no++;
			}
		}*/
		/*for(i=0;i<liste_local.size();i++)
		{
			lg=((Integer) liste_local.get(i)).intValue();
			if(lg.getName().equalsIgnoreCase(nom))
				return lg;
		}*/
		lg=genere.donne_var_local(nom);
		if(lg>=0)
			return lg+no;
		return -1;
	}
	
	public boolean est_dans_classe(String nom)
	{
		if(routine!=null)
		{
			if(routine.classe.nom.nom.equalsIgnoreCase(nom))
				return true;
		}
		return false;
	}
	
	public boolean est_dans_routine(String nom)
	{
		if(routine!=null)
		{
			if(routine.classe.nom.nom.equalsIgnoreCase(nom))
				return true;
		}
		return false;
	}
	
	public String toString()
	{
		String res;
		res="["+cl.nom;
		if(attr!=null)
		{
			res+="->"+attr.nom;
		}
		res+="]";
		return res;
	}
	
	public CIRoutine routine;
	public List liste_local;
	public CIClasse cl;
	public CIAttribut attr;
	private GenereJvm genere;
	
	public static final int VAR_LOCAL = 1,VAR_PARAM=2,VAR_ATTR=3,
		VAR_INCONNUE=4,VAR_RETOUR=5;


}
