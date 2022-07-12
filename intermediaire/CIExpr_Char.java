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
public class CIExpr_Char extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Char(char car,CISource source) {
		this.car=car;
		this.source=source;
	}

	public String toString()
	{
		return "'"+car+"'";
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"car\" text=\""+car+"\" ");
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
		assert(e instanceof CIExpr_Char);
		CIExpr_Char e2=(CIExpr_Char)e;
		assert(car==e2.car);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final char car;
	public CISource source;

}
