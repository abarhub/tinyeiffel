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
public class CIExpr_Entier extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Entier(int entier,CISource source) {
		this.entier=entier;
		this.source=source;
	}

	public String toString()
	{
		return ""+entier;
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"entier\" text=\""+entier+"\" ");
		if(source!=null)
		{
			out.println("/>");
			source.toXML(out);
			out.println("</expression_scalaire>");
		}
		else
		{
			out.println("/>");
		}
	}

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Entier);
		CIExpr_Entier e2=(CIExpr_Entier)e;
		assert(entier==e2.entier);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final int entier;
	public CISource source;

}
