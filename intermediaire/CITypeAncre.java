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
public class CITypeAncre extends CIType {

	/**
	 * @param nom
	 * @param source
	 */
	public CITypeAncre(String nom, CISource source) {
		assert(nom!=null);
		this.nom=nom;
		is_like=true;
		this.source=source;
	}
	
	public boolean est_special()
	{
		return false;
	}
	
	public void check_egal(CIType t)
	{
		assert(t!=null);
		assert(is_like==t.is_like);
		assert(t instanceof CITypeAncre);
		CITypeAncre t2=(CITypeAncre)t;
		assert(nom!=null);
		assert(t2.nom!=null);
		assert(nom.equalsIgnoreCase(t2.nom));
		assert(t instanceof CITypeAncre);
		if(source!=null)
		{
			source.check_egal(t.source);
		}
		else
		{
			assert(t.source==null);
		}
	}

	public final String nom;
	//public CISource source;


	public String toString()
	{
		String res="";
		res="like "+nom;
		return res;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		
		out.print("<type est_like=\"oui\" nom=\""+nom+"\" ");
		if(source!=null)
		{
			out.println(">");
			source.toXML(out);
			out.println("</type>");
		}
		else
		{
			out.println("/>");
		}
	}

}
