/*
 * Created on 5 févr. 2004
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
public class CINom_Attribut {

	public CINom_Attribut(String nom,CISource source) {
		assert(nom!=null);
		assert(!nom.startsWith("\""));
		assert(!nom.endsWith("\""));
		this.nom=nom;
		infix=false;
		prefix=false;
		this.source=source;
	}

	public CINom_Attribut(String nom,boolean infix,CISource source) {
		assert(nom!=null);
		assert(nom.startsWith("\""));
		assert(nom.endsWith("\""));
		this.nom=nom;
		this.infix=infix;
		this.prefix=!infix;
		this.source=source;
	}

	public String toString()
	{
		String res;
	
		if(infix)
			res="infix "+nom;
		else if(prefix)
			res="prefix "+nom;
		else
			res=nom;
		
		return res;
	}

	public void toXML(PrintStream out)
	{
		String n;
		assert(out!=null);
		/*if(infix)
			n=nom.substring(1,nom.length()-2);
		else if(prefix)
			n=nom.substring(1,nom.length()-2);
		else*/
			n=nom;
		if(n.startsWith("\""))
			n=n.substring(1);
		if(n.endsWith("\""))
			n=n.substring(0,n.length()-1);
		assert(n!=null);
		assert(!n.equals(""));
		n=n.replaceAll("&","&amp;");
		n=n.replaceAll("<","&lt;");
		n=n.replaceAll(">","&gt;");
		out.print("<nom_attribut nom=\""+n+"\"");
		if(prefix)
			out.println(" type=\"prefix\" ");
		else if(infix)
			out.println(" type=\"infix\" ");
		else
			out.print(" ");
		if(source!=null)
		{
			out.println(" >");
			source.toXML(out);
			out.println("</nom_attribut>");
		}
		else
		{
			out.println(" />");
		}
	}
	
	public void check_egal(CINom_Attribut a)
	{
		assert(a!=null);
		assert(infix==a.infix);
		assert(prefix==a.prefix);
		assert(nom!=null);
		assert(a.nom!=null);
		assert(nom.equalsIgnoreCase(a.nom));
		if(source!=null)
		{
			source.check_egal(a.source);
		}
		else
		{
			assert(a.source==null);
		}
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof CINom_Attribut)
		{
			CINom_Attribut tmp;
			tmp=(CINom_Attribut)o;
			if(infix)
			{
				if(tmp.infix&&!tmp.prefix&&nom.equals(tmp.nom))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else if(prefix)
			{
				if(tmp.prefix&&!tmp.infix&&nom.equals(tmp.nom))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if(!tmp.prefix&&!tmp.infix&&nom.equals(tmp.nom))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}
	
	public final String nom;
	public final boolean infix,prefix;
	public CISource source;

}
