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
public class CIInstruction_Affect extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Affect(CIExpr_Var nom,CIExpr expr,boolean force,CISource source) {
		assert(nom!=null);
		assert(expr!=null);
		this.nom=nom;
		this.expr=expr;
		this.source=source;
		this.force=force;
	}

	public String toString()
	{
		return nom+":="+expr;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		if(force)
		{
			out.println("<instr_affect force=\"oui\">");
		}
		else
		{
			out.println("<instr_affect>");
		}
		nom.toXML(out);
		out.println("<expression>");
		expr.toXML(out);
		out.println("</expression>");
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</instr_affect>");
	}

	public void check_egal(CIInstruction instr)
	{
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Affect);
		CIInstruction_Affect ins=(CIInstruction_Affect)instr;
		assert(nom!=null);
		assert(ins.nom!=null);
		assert(expr!=null);
		assert(ins.expr!=null);
		nom.check_egal(ins.nom);
		expr.check_egal(ins.expr);
		assert(force==ins.force);
		if(source!=null)
		{
			source.check_egal(ins.source);
		}
		else
		{
			assert(ins.source==null);
		}
	}
	
	public CIExpr_Var nom;
	public CIExpr expr;
	public CISource source;
	public boolean force;

}
