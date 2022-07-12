/*
 * Created on 1 févr. 2004
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
public class CIDeclare_Var {

	/**
	 * 
	 */
	public CIDeclare_Var(String nom,CIType type,CISource source) {
		//assert(nom!=null);
		//assert(type!=null);
		this.nom=nom;
		this.type=type;
		this.expr=null;
		this.source=source;
	}

	public CIDeclare_Var(String nom,CIType type,CIExpr_Scalaire expr,CISource source) {
		assert(nom!=null);
		assert(type!=null);
		assert(expr!=null);
		this.nom=nom;
		this.type=type;
		this.expr=expr;
		this.source=source;
	}
	public String toString()
	{
		String res=nom+":"+type;
		if(expr!=null)
			res+="="+expr.toString();
		return res;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<declare_var nom=\""+nom+"\" >");
		type.toXML(out);
		if(expr!=null)
		{
			expr.toXML(out);
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</declare_var>");
	}

	public void check_egal(CIDeclare_Var d)
	{
		assert(d!=null);
		assert(nom!=null);
		assert(type!=null);
		assert(d.nom!=null);
		assert(d.type!=null);
		assert(nom.equalsIgnoreCase(d.nom));
		type.check_egal(d.type);
		if(source!=null)
		{
			source.check_egal(d.source);
		}
		else
		{
			assert(d.source==null);
		}
	}
	
	public final String nom;
	public final CIType type;
	public final CIExpr_Scalaire expr;
	public CISource source;

}
