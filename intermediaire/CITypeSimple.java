/*
 * Created on 5 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.intermediaire;

import java.io.PrintStream;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CITypeSimple extends CIType {

	/**
	 * @param expanded
	 * @param nom
	 * @param gene
	 * @param source
	 */
	public CITypeSimple(boolean expanded, String nom, CIType[] gene,
			CITypeSimple contrainte,CISource source) {
		assert(nom!=null);
		//assert(gene instanceof CITypeSimple[]);
		this.expanded=expanded;
		this.nom=nom;
		this.generique=gene;
		is_like=false;
		this.source=source;
		this.contrainte=contrainte;
	}
	
	public boolean est_special()
	{
		if(nom.equals("$integer")
			||nom.equals("$boolean")
			||nom.equals("$character")
			||nom.equals("$double")
			||nom.equals("$real")
			||nom.equals("$string")
			||nom.equals("$array"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void check_egal(CIType t)
	{
		assert(t!=null);
		assert(t instanceof CITypeSimple);
		CITypeSimple t2=(CITypeSimple)t;
		assert(expanded==t2.expanded);
		assert(is_like==t.is_like);
		assert(nom!=null);
		assert(t2.nom!=null);
		assert(nom.equalsIgnoreCase(t2.nom));
		if(generique==null||generique.length==0)
			assert(t2.generique==null||t2.generique.length==0);
		else
		{
			assert(t2.generique!=null);
			assert(generique.length==t2.generique.length);
			for(int i=0;i<generique.length;i++)
			{
				generique[i].check_egal(t2.generique[i]);
			}
		}
		if(contrainte==null)
		{
			assert(t2.contrainte==null);
		}
		else
		{
			contrainte.check_egal(t2.contrainte);
		}
		if(source!=null)
		{
			source.check_egal(t.source);
		}
		else
		{
			assert(t.source==null);
		}
	}

	public boolean expanded;
	public final String nom;
	public final CIType generique[];
	public final CITypeSimple contrainte;
	//public CISource source;


	public String toString()
	{
		String res="";
		if(expanded)
			res+=" expanded ";
		res+=nom;
		if(generique!=null&&generique.length>0)
		{
			res+="[";
			for(int i=0;i<generique.length;i++)
			{
				if(i>0)
					res+=",";
				res+=generique[i];
			}
			if(contrainte!=null)
				res+="->"+contrainte.toString();
			res+="]";
		}
		return res;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		boolean a_fils=false;
		
		out.print("<type nom=\""+nom+"\"");
		
		if(expanded)
			out.print(" expanded=\"oui\" ");
		
		if(generique!=null&&generique.length>0)
		{
			if(!a_fils)
			{
				out.println(" >");
			}
			a_fils=true;
			for(int i=0;i<generique.length;i++)
			{
				generique[i].toXML(out);
			}
		}
		if(contrainte!=null)
		{
			if(!a_fils)
			{
				out.println(" >");
			}
			a_fils=true;
			out.println("<contrainte>");
			contrainte.toXML(out);
			out.println("</contrainte>");
		}
		if(source!=null)
		{
			if(!a_fils)
			{
				out.println(">");
			}
			source.toXML(out);
			a_fils=true;
		}
		if(a_fils)
		{
			out.println("</type>");
		}
		else
		{
			out.println(" />");
		}
	}

	/*public void toXML(PrintStream out, boolean b) {
		assert(out!=null);
		String n;
		CITypeSimple t;
		
		if(!b)
		{
			n="type_formel";
		}
		else
		{
			n="type_containt";
		}
		out.print("<"+n+" nom=\""+nom+"\"");
		
		if(expanded)
			out.print(" expanded=\"oui\" ");
		
		if(generique!=null&&generique.length>0)
		{
			out.println(" >");
			for(int i=0;i<generique.length;i++)
			{
				t=(CITypeSimple)generique[i];
				t.toXML(out,true);
			}
			if(source!=null)
			{
				source.toXML(out);
			}
			out.println("</"+n+">");
		}
		else
		{
			if(source!=null)
			{
				out.println(">");
				source.toXML(out);
				out.println("</"+n+">");
			}
			else
			{
				out.println("/>");
			}
		}
	}*/
}
