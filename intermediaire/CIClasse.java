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
public class CIClasse {

	/**
	 * 
	 */
	public CIClasse(CITypeSimple nom,CIProgramme p,CITypeSimple heritage[],CISource source) {
		assert(nom!=null);
		assert(p!=null);
		assert(nom.nom.equalsIgnoreCase("general")||
				nom.nom.equalsIgnoreCase("any")||heritage!=null):nom.nom;
		this.nom=nom;
		this.programme=p;
		this.heritage=heritage;
		this.source=source;
	}

	public CIClasse()
	{
		
	}

	public void affiche(PrintStream out)
	{
		assert(out!=null);
		out.println(" * Classe "+nom+" :");
		if(liste_attribut!=null)
		{
			for(int i=0;i<liste_attribut.length;i++)
			{
				//out.println(liste_attribut[i].toString2());
				liste_attribut[i].affiche(out);
			}
		}
	}

	public void toXML(PrintStream out,boolean heritage0[])
	{
		assert(out!=null);
		out.println("<classe>");
		nom.toXML(out);
		out.println("<hierarchie>");
		if(heritage!=null)
		{
			for(int i=0;i<heritage.length;i++)
			{
				heritage[i].toXML(out);
			}
		}
		out.println("</hierarchie>");
		if(creation!=null)
		{
			for(int i=0;i<creation.length;i++)
			{
				creation[i].toXML(out);
			}
		}
		if(liste_attribut!=null)
		{
			for(int i=0;i<liste_attribut.length;i++)
			{
				//out.println(liste_attribut[i].toString2());
				liste_attribut[i].toXML(out);
			}
		}
		if(invariant!=null)
		{
			invariant.toXML(out,"invariant");
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</classe>");
	}

	public CIAttribut donne_attribut(CINom_Attribut attr)
	{
		assert(attr!=null);
		if(liste_attribut!=null)
		{
			CIAttribut a;
			for(int i=0;i<liste_attribut.length;i++)
			{
				a=liste_attribut[i];
				if(attr.nom.compareToIgnoreCase(a.nom.nom)==0&&
					attr.infix==a.nom.infix&&attr.prefix==a.nom.prefix)
				{// TODO: utiliser equal a la place de cela
					return a;
				}
			}
		}
		return null;
	}

	public CIAttribut donne_attribut(String nom)
	{
		assert(nom!=null);
		//assert(liste_attribut!=null):"classe"+this.nom+"("+nom+")";
		if(liste_attribut!=null)
		{
			CIAttribut a;
			for(int i=0;i<liste_attribut.length;i++)
			{
				a=liste_attribut[i];
				//assert(a.nom.nom.compareToIgnoreCase("f1")==0):"a="+a.nom;
				if(nom.compareToIgnoreCase(a.nom.nom)==0&&
					false==a.nom.infix&&false==a.nom.prefix)
				{
					return a;
				}
			}
		}
		return null;
	}
	
	public CIAttribut donne_attribut_defaut()
	{
		if(liste_attribut!=null)
		{
			CIAttribut a;
			for(int i=0;i<liste_attribut.length;i++)
			{
				a=liste_attribut[i];
				//assert(a.nom.nom.compareToIgnoreCase("f1")==0):"a="+a.nom;
				if(a.est_defaut())
				{
					return a;
				}
			}
		}
		return null;
	}

	public void check_egal(CIClasse c)
	{
		int i;
		assert(c!=null);
		assert(nom!=null);
		assert(c.nom!=null);
		nom.check_egal(c.nom);
		if(heritage==null||heritage.length==0)
			assert(c.heritage==null||c.heritage.length==0);
		else
		{
			assert(c.heritage!=null);
			assert(c.heritage.length==heritage.length);
			for(i=0;i<heritage.length;i++)
			{
				heritage[i].check_egal(c.heritage[i]);
			}
		}
		if(creation==null||creation.length==0)
			assert(c.creation==null||c.creation.length==0);
		else
		{
			assert(c.creation!=null);
			assert(c.creation.length==creation.length);
			for(i=0;i<creation.length;i++)
			{
				creation[i].check_egal(c.creation[i]);
			}
		}
		if(liste_attribut==null||liste_attribut.length==0)
			assert(c.liste_attribut==null||c.liste_attribut.length==0);
		else
		{
			boolean attr_defaut=false;
			assert(c.liste_attribut!=null);
			assert(c.liste_attribut.length==liste_attribut.length);
			for(i=0;i<liste_attribut.length;i++)
			{
				liste_attribut[i].check_egal(c.liste_attribut[i]);
				if(liste_attribut[i].est_defaut())
				{// il ne doit y avoir qu'un seul attribut par defaut
					assert(!attr_defaut);
					attr_defaut=true;
				}
			}
		}
		if(invariant!=null)
		{
			assert(c.invariant!=null);
			invariant.check_egal(c.invariant);
		}
		else
		{
			assert(c.invariant==null);
		}
		if(source!=null)
		{
			source.check_egal(c.source);
		}
		else
		{
			assert(c.source==null);
		}
	}

	public CITypeSimple nom;
	//public Routine liste_routine[];
	public CIAttribut liste_attribut[];
	//public Attribut creation[];
	public CIProgramme programme;
	public CITypeSimple heritage[];
	public CICreation creation[];
	public CIAssertion invariant;
	public CISource source;

}
