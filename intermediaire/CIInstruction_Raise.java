/*
 * Created on 24 févr. 2004
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
public class CIInstruction_Raise extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Raise(CIExpr no,CIExpr nom,
			CISource source) {
		assert(nom!=null);
		this.no=no;
		this.nom=nom;
		this.source=source;
	}

	/**
	 * no=
	 * 1 - loop_invariant
	 * 2 - loop_variant
	 * 3 - no_more_memory
	 * 4 - routine_failure
	 * 5 - Void_assigned_to_expanded
	 * 6 - Void_call_target
	 * 7 - incorrect_inspect_value
	 * @param no
	 */
	public CIInstruction_Raise(CIExpr no,CISource source) {
		this.no=no;
		this.nom=null;
		this.source=source;
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#affiche(java.io.PrintStream)
	 */
	public String toString() {
		String res;
		res="raise "+no;
		if(nom!=null)
			res+=","+nom;
		return res;
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instr_raise>");
		out.println("<expression>");
		no.toXML(out);
		out.println("</expression>");
		if(nom!=null)
		{
			out.println("<expression>");
			nom.toXML(out);
			out.println("</expression>");
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</instr_raise >");
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#check_egal(intermediaire.Instruction)
	 */
	public void check_egal(CIInstruction instr) {
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Raise);
		CIInstruction_Raise ins=(CIInstruction_Raise)instr;
		assert(no!=null);
		assert(ins.no!=null);
		no.check_egal(ins.no);
		if(nom==null)
		{
			assert(ins.nom==null);
		}
		else
		{
			assert(ins.nom!=null);
			nom.check_egal(ins.nom);
		}
		if(source!=null)
		{
			source.check_egal(ins.source);
		}
		else
		{
			assert(ins.source==null);
		}
	}

	public final CIExpr no;
	public final CIExpr nom;
	public CISource source;

}
