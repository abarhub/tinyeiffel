/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;
import java.util.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIProgramme {

	/**
	 * 
	 */
	public CIProgramme() {
		liste_var_global=new CIListe_Global();
	}

	public void init()
	{
		CIType type;
		type=new CITypeSimple(false,"INTEGER",null,null,null);
		/*1 - loop_invariant
	 * 2 - loop_variant
	 * 3 - no_more_memory
	 * 4 - routine_failure
	 * 5 - Void_assigned_to_expanded
	 * 6 - Void_call_target
	 * 7 - incorrect_inspect_value*/
		/*liste_var_global.ajoute(new Declare_Var("code_exception_loop_invariant",type,new Expr_Entier(1)));
		liste_var_global.ajoute(new Declare_Var("code_exception_loop_variant",type,new Expr_Entier(2)));
		liste_var_global.ajoute(new Declare_Var("code_exception_no_more_memory",type,new Expr_Entier(3)));
		liste_var_global.ajoute(new Declare_Var("code_exception_routine_failure",type,new Expr_Entier(4)));
		liste_var_global.ajoute(new Declare_Var("code_exception_Void_assigned_to_expanded",type,new Expr_Entier(5)));
		liste_var_global.ajoute(new Declare_Var("code_exception_Void_call_target",type,new Expr_Entier(6)));
		liste_var_global.ajoute(new Declare_Var("code_exception_incorrect_inspect_value",type,new Expr_Entier(7)));*/
	}

	public int no_classe(CITypeSimple t)
	{
		int i;
		CITypeSimple t2;
		assert(t!=null);
		for(i=0;i<liste_classe.length;i++)
		{
			t2=liste_classe[i].nom;
			if(t2.nom.compareToIgnoreCase(t.nom)==0)
				return i;
		}
		assert(false):"type="+t;
		return -1;
	}

	public CIClasse donne_classe(CIType t)
	{
		int i;
		assert(t!=null);
		i=no_classe((CITypeSimple)t);
		if(i>-1)
			return liste_classe[i];
		else
			return null;
	}

	public boolean run(String nom_classe,String routine)
	{
		assert(nom_classe!=null);
		assert(routine!=null);
		CIEnvironnement env;
		int no;
		CIClasse classe_racine;
		//env=new CIEnvironnement();
		classe_racine=donne_classe(new CITypeSimple(false,nom_classe,null,null,null));
		if(classe_racine==null)
			return false;
		
		return true;
	}

	public void affiche(PrintStream out)
	{
		int i;
		assert(out!=null);
		out.println("Programme:");
		out.println("Liste des classes:");
		for(i=0;i<liste_classe.length;i++)
		{
			out.println(i+")"+liste_classe[i].nom);
		}
		out.println("Heritage directe:");
		affiche(out,heritage_directe);
		out.println("Heritage:");
		affiche(out,heritage);
		out.println("Liste detaillé des classes:");
		for(i=0;i<liste_classe.length;i++)
		{
			liste_classe[i].affiche(out);
		}
		out.println("Fin du programme");
	}

	public void affiche(PrintStream out,boolean heritage[][])
	{
		int i,j,nb;
		assert(out!=null);
		assert(heritage!=null);
		for(i=0;i<heritage.length;i++)
		{
			out.print(liste_classe[i].nom+"->");
			nb=0;
			for(j=0;j<heritage[i].length;j++)
			{
				if(heritage[i][j])
				{
					if(nb>0)
						out.print(",");
					out.print(liste_classe[j].nom);
					nb++;
				}
			}
			out.println();
		}
	}

	public void toXML(PrintStream out)
	{
		int i;
		String date;
		assert(out!=null);
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<?xml-stylesheet type=\"text/xsl\" href=\"..\\..\\..\\test_unitaire\\programme.xsl\"?>");
		out.println("<!DOCTYPE programme SYSTEM \"..\\..\\..\\test_unitaire\\programme.dtd\">");
		{
			Calendar calendrier;
			int jour,mois,annee;
			int heure,minute,seconde;
			calendrier=Calendar.getInstance();
			jour=calendrier.get(Calendar.DAY_OF_MONTH);
			mois=calendrier.get(Calendar.MONTH);
			annee=calendrier.get(Calendar.YEAR);
			heure=calendrier.get(Calendar.HOUR);
			minute=calendrier.get(Calendar.MINUTE);
			seconde=calendrier.get(Calendar.SECOND);
			date=annee+"-"+mois+"-"+jour+"T"+heure+":"+minute+":"+seconde;
		}
		//out.print("<programme date=\""+date+"\" >");
		out.print("<programme >");
		/*out.println("Liste des classes:");
		for(i=0;i<liste_classe.length;i++)
		{
			out.println(i+")"+liste_classe[i].nom);
		}
		out.println("Heritage directe:");
		affiche(out,heritage_directe);
		out.println("Heritage:");
		affiche(out,heritage);
		out.println("Liste detaillé des classes:");*/
		liste_var_global.toXML(out);
		for(i=0;i<liste_classe.length;i++)
		{
			assert(liste_classe[i]!=null);
			liste_classe[i].toXML(out,heritage_directe[i]);
		}
		out.println("</programme>");
	}

	public void check_egal(CIProgramme p)
	{
		int i,j;
		assert(p!=null);
		assert(p.heritage!=null);
		assert(p.heritage_directe!=null);
		assert(p.heritage.length==heritage.length);
		assert(p.heritage_directe.length==heritage_directe.length);
		assert(heritage_directe.length==heritage.length);
		for(i=0;i<heritage.length;i++)
		{
			assert(heritage[i].length==p.heritage.length);
			assert(heritage_directe[i].length==p.heritage_directe.length);
			assert(heritage_directe[i].length==heritage[i].length);
			for(j=0;j<heritage[i].length;j++)
			{
				assert(heritage_directe[i][j]==p.heritage_directe[i][j]):"("+i+","+j+"):"+
						heritage[i][j]+"!="+p.heritage[i][j]+";"+
						liste_classe[i].nom+","+liste_classe[j].nom;
				assert(heritage[i][j]==p.heritage[i][j]):"("+i+","+j+"):"+
						heritage[i][j]+"!="+p.heritage[i][j]+";"+
						liste_classe[i].nom+","+liste_classe[j].nom;
			}
			assert(!heritage_directe[i][i]):"type="+liste_classe[i].nom;
		}
		if(liste_classe!=null)
		{
			assert(p.liste_classe!=null);
			assert(p.liste_classe.length==liste_classe.length);
			for(i=0;i<liste_classe.length;i++)
			{
				liste_classe[i].check_egal(p.liste_classe[i]);
			}
		}
		else
		{
			assert(p.liste_classe==null);
		}
		if(nom==null)
			assert(p.nom==null);
		else
			assert(nom.equalsIgnoreCase(p.nom));

		if(liste_var_global==null||liste_var_global.size()==0)
			assert(p.liste_var_global==null||p.liste_var_global.size()==0);
		else
			liste_var_global.check_egal(p.liste_var_global);
	}
	
	public boolean type_existe(CITypeSimple t) {
		int i;
		CITypeSimple t2;
		assert(t!=null);
		for(i=0;i<liste_classe.length;i++)
		{
			t2=liste_classe[i].nom;
			if(t2.nom.compareToIgnoreCase(t.nom)==0)
				return true;
		}
		return false;
	}

	public CIClasse liste_classe[];
	public boolean heritage_directe[][],heritage[][];
	public String nom;
	public CIListe_Global liste_var_global;
	//public 

}
