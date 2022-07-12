/*
 * Created on 6 févr. 2004
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
public class CIExpr_Void extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Void(CISource source) {
		this.source=source;
	}

	public String toString()
	{
		return "Void";
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"void\" ");
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
		assert(e instanceof CIExpr_Void);
		if(source!=null)
		{
			source.check_egal(((CIExpr_Void)e).source);
		}
		else
		{
			assert(((CIExpr_Void)e).source==null);
		}
	}
	
	public CISource source;
	
}
