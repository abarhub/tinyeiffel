/*
 * Created on 10 avr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.util.Vector;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIEnvironnement {

	public CIEnvironnement(CIClasse cl,CIProgramme p)
	{
		assert(cl!=null);
		classe=cl;
		liste_attr=new Vector();
		no_instr=-1;
		programme=p;
		no_creation=-1;
		creation=null;
		heritage=null;
		no_heritage=-1;
		type_assertion=CRien;
	}
	
	public void entre_attribut(CINom_Attribut nom)
	{
		assert(nom!=null);
		assert(nom_attr==null);
		assert(no_instr==-1);
		nom_attr=nom;
		liste_var_local=new Vector();
		liste_var_local_temp=new Vector();
	}
	
	public void sort_attribut()
	{
		assert(nom_attr!=null);
		nom_attr=null;
		liste_var_local=null;
		liste_var_local_temp=null;
		no_instr=-1;
	}
	
	public void entre_instr()
	{
		assert(nom_attr!=null);
		if(no_instr<=0)
			no_instr=1;
		else
			no_instr++;
	}

	public void sort_instr()
	{
		assert(nom_attr!=null);
		//no_instr=-1;
	}

	public void init_instr()
	{
		//assert(nom_attr!=null);
		no_instr=-1;
	}
	
	public void entre_assertion(int type)
	{
		assert(type!=CRien);
		assert(type_assertion==CRien);
		switch(type)
		{
		case CInvariant:
		case CPrecondition:
		case CPostcondition:
		case CCheck:
			break;
		default:
			assert(false):type;
		}
		type_assertion=type;
	}
	
	public void sort_assertion()
	{
		type_assertion=CRien;
	}
	
	public void entre_creation(CICreation c,int no_liste) {
		assert(nom_attr==null);
		assert(c!=null);
		creation=c;
		no_creation_liste=no_liste;
		if(no_creation<=0)
			no_creation=1;
		else
			no_creation++;
	}

	public void sort_creation() {
		assert(nom_attr==null);
		assert(creation!=null);
		creation=null;
	}
	
	public void entre_heritage(CIType c) {
		assert(nom_attr==null);
		assert(c!=null);
		assert(creation==null);
		heritage=c;
		if(no_heritage<=0)
			no_heritage=1;
		else
			no_heritage++;
	}

	public void sort_heritage() {
		assert(nom_attr==null);
		assert(heritage!=null);
		assert(creation==null);
		heritage=null;
	}

	public void ajoute_var_local(CIDeclare_Var v)
	{
		assert(v!=null);
		liste_var_local.add(v);
	}
	
	public void ajoute_var_local_temporaire(CIDeclare_Var v)
	{
		assert(v!=null);
		liste_var_local_temp.add(v);
	}
	
	public void supprime_var_temp()
	{
		liste_var_local_temp=new Vector();
	}
	
	public boolean var_existe(String nom)
	{
		assert(nom!=null);
		int i;
		String s;
		CINom_Attribut attr;
		if(liste_var_local_temp!=null)
		{
			for(i=0;i<liste_var_local_temp.size();i++)
			{
				s=((CIDeclare_Var)liste_var_local_temp.elementAt(i)).nom;
				if(s.equalsIgnoreCase(nom))
					return true;
			}
		}
		if(liste_var_local!=null)
		{
			for(i=0;i<liste_var_local.size();i++)
			{
				s=((CIDeclare_Var)liste_var_local.elementAt(i)).nom;
				if(s.equalsIgnoreCase(nom))
					return true;
			}
		}
		if(classe.liste_attribut!=null&&classe.liste_attribut.length>0)
		{
			for(i=0;i<classe.liste_attribut.length;i++)
			{
				attr=classe.liste_attribut[i].nom;
				if(!attr.infix&&!attr.prefix)
				{
					if(attr.nom.equalsIgnoreCase(nom))
						return true;
				}
			}
		}
		return false;
	}
	
	public CIType var_type(String nom)
	{
		assert(nom!=null);
		int i;
		String s;
		CIDeclare_Var d;
		CINom_Attribut attr;
		if(liste_var_local_temp!=null)
		{
			for(i=0;i<liste_var_local_temp.size();i++)
			{
				d=(CIDeclare_Var)liste_var_local_temp.elementAt(i);
				s=d.nom;
				if(s.equalsIgnoreCase(nom))
					return d.type;
			}
		}
		if(liste_var_local!=null)
		{
			for(i=0;i<liste_var_local.size();i++)
			{
				d=(CIDeclare_Var)liste_var_local.elementAt(i);
				s=d.nom;
				if(s.equalsIgnoreCase(nom))
					return d.type;
			}
		}
		if(classe.liste_attribut!=null&&classe.liste_attribut.length>0)
		{
			for(i=0;i<classe.liste_attribut.length;i++)
			{
				attr=classe.liste_attribut[i].nom;
				if(!attr.infix&&!attr.prefix)
				{
					if(attr.nom.equalsIgnoreCase(nom))
					{
						if(classe.liste_attribut[i].est_descendant())
						{
							CIAttribut_Ascendant a1;
							CIAttribut a2;
							int no;
							assert(classe.liste_attribut[i].attribut_ascendant!=null);
							assert(classe.liste_attribut[i].attribut_ascendant.length==1);
							a1=classe.liste_attribut[i].attribut_ascendant[0];
							a2=a1.donne_attribut(attr,classe,programme);
							assert(a2!=null);
							assert(a2.retour!=null);
							return a2.retour;
						}
						else
						{
							return classe.liste_attribut[i].type_retour();
						}
					}
				}
			}
		}
		return null;
	}
	
	public String toString()
	{
		String res;
		if(classe==null)
			res="Aucune classe";
		else
		{
			res=classe.nom.nom.toUpperCase();
			if(nom_attr!=null)
			{
				res+="."+nom_attr.toString().toLowerCase();
				if(type_assertion==CPrecondition)
				{
					res+=" precondition";
				}
				else if(type_assertion==CPostcondition)
				{
					res+=" postcondition";
				}
				if(no_instr>0)
					res+=" instruction no "+no_instr;
			}
			else if(heritage!=null)
			{
				res+=" heritage "+heritage+"("+no_heritage+")";
			}
			else if(creation!=null)
			{
				res+=" creation "+creation.toString(no_creation_liste)+
					"("+no_creation+")";
			}
			else if(type_assertion==CInvariant)
			{
				res+=" invariant";
				if(no_instr>0)
					res+=" instruction no "+no_instr;
			}
		}
		return res;
	}
	
	public CIClasse classe;
	public CINom_Attribut nom_attr;
	public Vector liste_attr,liste_var_local,liste_var_local_temp;
	public int no_instr;
	public CIProgramme programme;
	public CICreation creation;
	public int no_creation,no_creation_liste;
	public CIType heritage;
	public int no_heritage;
	public int type_assertion;
	
	public static final int CInvariant=1,CPrecondition=2,
		CPostcondition=3,CCheck=4,CRien=5;
}
