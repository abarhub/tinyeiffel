/*
 * Created on 5 f�vr. 2004
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
public class CIInstruction_Si extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Si(CIExpr expr,String label,
			CISource source) {
		assert(expr!=null);
		assert(label!=null);
		this.expr=expr;
		this.label=label;
		this.source=source;
	}

	public String toString()
	{
		return "If "+expr+" then "+label;
	}

	public void toXML(PrintStream out)
	{
		out.println("<instr_si label=\""+label+"\">");
		out.println("<expression>");
		expr.toXML(out);
		out.println("</expression>");
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</instr_si>");
	}

	public void check_egal(CIInstruction instr)
	{
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Si);
		CIInstruction_Si ins=(CIInstruction_Si)instr;
		assert(label!=null);
		assert(ins.label!=null);
		assert(expr!=null);
		assert(ins.expr!=null);
		assert(label.equalsIgnoreCase(ins.label));
		expr.check_egal(ins.expr);
		if(source!=null)
		{
			source.check_egal(ins.source);
		}
		else
		{
			assert(ins.source==null);
		}
	}
	
	public String label;
	public CIExpr expr;
	public CISource source;

}
