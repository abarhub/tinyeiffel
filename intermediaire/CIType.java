/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

//import java.util.*;
import java.io.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class CIType {

	public boolean is_like;
	public CISource source;
	public abstract void check_egal(CIType t);
	public abstract void toXML(PrintStream out);
	public abstract boolean est_special();
	
	/*public CIType(String nom,CISource source) {
		assert(nom!=null);
		expanded=false;
		this.nom=nom;
		generique=null;
		is_like=true;
		this.source=source;
	}

	/**
	 * 
	 * /
	public CIType(boolean expanded,String nom,CIType gene[],
			CISource source) {
		assert(nom!=null);
		this.expanded=expanded;
		this.nom=nom;
		this.generique=gene;
		is_like=false;
		this.source=source;
	}

	public void check_egal(CIType t)
	{
		assert(t!=null);
		assert(expanded==t.expanded);
		assert(is_like==t.is_like);
		assert(nom!=null);
		assert(t.nom!=null);
		assert(nom.equalsIgnoreCase(t.nom));
		if(generique==null||generique.length==0)
			assert(t.generique==null||t.generique.length==0);
		else
		{
			assert(t.generique!=null);
			assert(generique.length==t.generique.length);
			for(int i=0;i<generique.length;i++)
			{
				generique[i].check_egal(t.generique[i]);
			}
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

	public boolean expanded,is_like;
	public final String nom;
	public final CIType generique[];
	public CISource source;


	public String toString()
	{
		String res="";
		if(is_like)
		{
			res="like "+nom;
		}
		else
		{
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
				res+="]";
			}
		}
		return res;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		
		if(is_like)
		{
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
		else
		{
			out.print("<type nom=\""+nom+"\"");
			
			if(expanded)
				out.print(" expanded=\"oui\" ");
			
			if(generique!=null&&generique.length>0)
			{
				out.println(" >");
				for(int i=0;i<generique.length;i++)
				{
					generique[i].toXML(out);
				}
				if(source!=null)
				{
					source.toXML(out);
				}
				out.println("</type>");
			}
			else
			{
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
	}*/

}
