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
public class CIExpr_Real extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Real(double real,CISource source) {
		this.real=real;
		this.source=source;
	}

	public String toString()
	{
		return ""+real;
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"reel\" text=\""+real+"\" ");
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
		assert(e instanceof CIExpr_Real);
		CIExpr_Real e2=(CIExpr_Real)e;
		assert(real==e2.real);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final double real;
	public CISource source;

}
