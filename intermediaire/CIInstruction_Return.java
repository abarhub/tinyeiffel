/*
 * Created on 22 févr. 2004
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
public class CIInstruction_Return extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Return(CISource source) {
		this.source=source;
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#affiche(java.io.PrintStream)
	 */
	public String toString() {
		return "Return";
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.print("<instr_return ");
		if(source!=null)
		{
			out.println(">");
			source.toXML(out);
			out.println("</instr_return>");
		}
		else
		{
			out.println(" />");
		}
	}

	/* (non-Javadoc)
	 * @see intermediaire.Instruction#check_egal(intermediaire.Instruction)
	 */
	public void check_egal(CIInstruction instr) {
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Return);
		if(source!=null)
		{
			source.check_egal(((CIInstruction_Return)instr).source);
		}
		else
		{
			assert(((CIInstruction_Return)instr).source==null);
		}
	}

	public CISource source;
}
