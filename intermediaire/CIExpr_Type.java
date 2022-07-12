/*
 * Created on 12 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.PrintStream;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIExpr_Type extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Type(CIExpr_Var var,CIType type,CISource source) {
		assert(var!=null);
		assert(type!=null);
		this.var=var;
		this.type=type;
		this.source=source;
	}

	public String toString()
	{
		return "type_of("+var+","+type+")";
	}

	/* (non-Javadoc)
	 * @see intermediaire.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression_type>");
		var.toXML(out);
		type.toXML(out);
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expression_type>");
	}

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Type);
		CIExpr_Type e2=(CIExpr_Type)e;
		assert(var!=null);
		assert(e2.var!=null);
		assert(type!=null);
		assert(e2.type!=null);
		var.check_egal(e2.var);
		type.check_egal(e2.type);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public CIExpr_Var var;
	public CIType type;
	public CISource source;

}
