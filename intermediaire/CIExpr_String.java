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
public class CIExpr_String extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_String(String str,CISource source) {
		assert(str!=null);
		this.str=str;
		this.source=source;
	}

	public String toString()
	{
		return str;
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_scalaire type=\"chaine\" text=\""+str+"\" ");
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
		assert(e instanceof CIExpr_String);
		CIExpr_String e2=(CIExpr_String)e;
		assert(str!=null);
		assert(e2.str!=null);
		assert(str.equals(e2.str));
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final String str;
	public CISource source;

}
