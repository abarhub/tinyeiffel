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
public class CIExpr_Bool extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Bool(boolean bool,CISource source) {
		this.bool=bool;
		this.source=source;
	}

	public String toString()
	{
		return ""+bool;
	}
	
	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"booleen\" text=\""+bool+"\" ");
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
		assert(e instanceof CIExpr_Bool);
		CIExpr_Bool e2=(CIExpr_Bool)e;
		assert(bool==e2.bool);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final boolean bool;
	public CISource source;

}
