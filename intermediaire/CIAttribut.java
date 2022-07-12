/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIAttribut {

	public CIAttribut(CINom_Attribut nom,CISource source) {
		assert(nom!=null);
		this.nom=nom;
		this.source=source;
	}
	
	public CIAttribut(CINom_Attribut nom,boolean var_static,CISource source) {
		assert(nom!=null);
		this.nom=nom;
		this.var_static=var_static;
		this.source=source;
	}
	
	public CIAttribut(CIAttribut attr)
	{// constructeur de copie
		this.nom=attr.nom;
		this.routine=attr.routine;
		this.type=attr.type;
		this.retour=attr.retour;
		this.attribut_descendant=attr.attribut_descendant;
		this.attribut_ascendant=attr.attribut_ascendant;
		this.cst=attr.cst;
		this.var_static=attr.var_static;
		this.source=attr.source;
		if(attr.nom.nom.equalsIgnoreCase("$val_bool"))
		{
			System.out.println("Trouve");
		}
		this.attribut_defaut=attr.attribut_defaut;
	}

	/*public Attribut(String nom) {
		assert(nom!=null);
		this.nom=nom;
		infix=false;
		prefix=false;
	}

	public Attribut(String nom,boolean infix) {
		assert(nom!=null);
		this.nom=nom;
		this.infix=infix;
		this.prefix=!infix;
	}*/

	public String toString()
	{
		String res;
		
		/*if(infix)
			res="infix "+nom;
		else if(prefix)
			res="prefix "+nom;
		else
			res=nom;*/
		res=nom.toString();
		
		if(type!=null)
			res+="{"+type+"}";
		else
			res+="{}";
		
		return res;
	}

	public String toString2()
	{
		String res=toString();
		res+=":";
		if(attribut_descendant!=null)
		{
			for(int i=0;i<attribut_descendant.length;i++)
			{
				if(i>0)
					res+=",";
				res+=attribut_descendant[i];
			}
		}
		return res;
	}

	public boolean est_routine()
	{
		return routine!=null;
	}

	public boolean est_static()
	{
		return var_static;		
	}
	
	public boolean est_defaut()
	{
		return attribut_defaut;
	}
	
	public void affiche(PrintStream out)
	{
		assert(out!=null);
		out.println(toString2());
		if(retour!=null)
		{
			out.println("type retour:"+retour.toString());
		}
		if(cst!=null)
		{
			out.println("cst="+cst.toString());
		}
		else if(routine!=null)
		{
			routine.affiche(out);
		}
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.print("<attribut");
		if(var_static)
			out.print(" est_static=\"oui\"");
		if(attribut_defaut)
			out.print(" est_defaut=\"oui\"");
		out.println(">");
		nom.toXML(out);
		type.toXML(out);
		if(retour!=null)
			retour.toXML(out);
		if(routine!=null)
			routine.toXML(out);
		else if(cst!=null)
		{
			out.println("<expression>");
			cst.toXML(out);
			out.println("</expression>");
		}
		if(attribut_ascendant!=null)
		{
			for(int i=0;i<attribut_ascendant.length;i++)
			{
				attribut_ascendant[i].toXML(out);
			}
		}
		if(attribut_descendant!=null)
		{
			for(int i=0;i<attribut_descendant.length;i++)
			{
				out.println("<descendant>");
				attribut_descendant[i].nom.toXML(out);
				attribut_descendant[i].type.toXML(out);
				out.println("</descendant>");
			}
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</attribut>");
		/*out.println(toString2());
		if(retour!=null)
		{
			out.println("type retour:"+retour.toString());
		}
		if(cst!=null)
		{
			out.println("cst="+cst.toString());
		}
		else if(routine!=null)
		{
			routine.affiche(out);
		}*/
	}
	public void ajoute_descendant(CIAttribut_Descendant attr)
	{
		assert(attr!=null);
		if(attribut_descendant==null)
		{
			attribut_descendant=new CIAttribut_Descendant[1];
			attribut_descendant[0]=attr;
		}
		else
		{
			CIAttribut_Descendant liste[];
			int i;
			liste=new CIAttribut_Descendant[attribut_descendant.length+1];
			for(i=0;i<attribut_descendant.length;i++)
			{
				liste[i]=attribut_descendant[i];
			}
			liste[i]=attr;
			attribut_descendant=liste;
		}
	}

	public void ajoute_ascendant(CIAttribut_Ascendant attr)
	{
		assert(attr!=null);
		if(attribut_ascendant==null)
		{
			attribut_ascendant=new CIAttribut_Ascendant[1];
			attribut_ascendant[0]=attr;
		}
		else
		{
			CIAttribut_Ascendant liste[];
			int i;
			liste=new CIAttribut_Ascendant[attribut_ascendant.length+1];
			for(i=0;i<attribut_ascendant.length;i++)
			{
				liste[i]=attribut_ascendant[i];
			}
			liste[i]=attr;
			attribut_ascendant=liste;
		}
	}

	public void check_egal(CIAttribut a)
	{
		int i;
		assert(a!=null);
		assert(type!=null);
		assert(a.type!=null);
		if(retour!=null)
		{
			assert(a.retour!=null);
			retour.check_egal(a.retour);
		}
		else
		{
			assert(a.retour==null);
		}
		if(routine!=null)
		{
			assert(a.routine!=null);
			assert(cst==null);
			assert(a.cst==null);
			routine.check_egal(a.routine);
		}
		else if(cst!=null)
		{
			assert(a.routine==null);
			assert(routine==null);
			assert(a.cst!=null);
			cst.check_egal(a.cst);
		}
		assert(nom!=null);
		assert(a.nom!=null);
		assert(a.var_static==var_static);
		assert(a.attribut_defaut==attribut_defaut);
		nom.check_egal(a.nom);
		if(attribut_descendant==null||attribut_descendant.length==0)
			assert(a.attribut_descendant==null||a.attribut_descendant.length==0);
		else
		{
			assert(a.attribut_descendant!=null);
			assert(a.attribut_descendant.length==attribut_descendant.length);
			for(i=0;i<attribut_descendant.length;i++)
			{
				attribut_descendant[i].check_egal(a.attribut_descendant[i]);
			}
		}
		if(attribut_ascendant==null||attribut_ascendant.length==0)
			assert(a.attribut_ascendant==null||a.attribut_ascendant.length==0);
		else
		{
			assert(a.attribut_ascendant!=null);
			assert(a.attribut_ascendant.length==attribut_ascendant.length);
			for(i=0;i<attribut_ascendant.length;i++)
			{
				attribut_ascendant[i].check_egal(a.attribut_ascendant[i]);
			}
		}
		if(source!=null)
		{
			source.check_egal(a.source);
		}
		else
		{
			assert(a.source==null);
		}
	}
	
	public boolean est_descendant()
	{
		if(routine==null&&retour==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//public final String nom;
	//public final boolean infix,prefix;
	public final CINom_Attribut nom;
	public CIRoutine routine;
	public CITypeSimple type;
	public CIType retour;
	public CIAttribut_Descendant attribut_descendant[];
	public CIAttribut_Ascendant attribut_ascendant[];
	public CIExpr cst;
	public boolean var_static;
	public CISource source;
	public boolean attribut_defaut;
	/**
	 * @return
	 */
	public CIType type_retour() {
		if(routine!=null)
		{
			if(routine.retour==null)
				return null;
			else
				return routine.retour.type;
		}
		else
		{
			return retour;
		}
	}

}
