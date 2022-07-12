/*
 * Created on 11 févr. 2004
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
public class CIAttribut_Descendant {

	/**
	 * 
	 */
	public CIAttribut_Descendant(CINom_Attribut nom,CIType type,
			CISource source)
	{
		assert(type!=null);
		assert(nom!=null);
		this.type=type;
		this.nom=nom;
		this.source=source;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<descendant>");
		nom.toXML(out);
		type.toXML(out);
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</descendant>");
	}

	public void check_egal(CIAttribut_Descendant a)
	{
		assert(a!=null);
		assert(type!=null);
		assert(a.type!=null);
		assert(nom!=null);
		assert(a.nom!=null);
		nom.check_egal(a.nom);
		type.check_egal(a.type);
		if(source!=null)
		{
			source.check_egal(a.source);
		}
		else
		{
			assert(a.source==null);
		}
	}
	
	public final CIType type;
	public final CINom_Attribut nom;
	public CISource source;


}
