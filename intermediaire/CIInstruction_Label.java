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
public class CIInstruction_Label extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Label(String nom,CISource source) {
		assert(nom!=null);
		this.nom=nom;
		this.source=source;
	}

	public String toString()
	{
		return "Label "+nom;
	}

	public void toXML(PrintStream out)
	{
		out.print("<instr_label label=\""+nom+"\" ");
		if(source!=null)
		{
			out.println(">");
			source.toXML(out);
			out.println("</instr_label>");
		}
		else
		{
			out.println(" />");
		}
	}

	public void check_egal(CIInstruction instr)
	{
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Label);
		CIInstruction_Label ins=(CIInstruction_Label)instr;
		assert(nom!=null);
		assert(ins.nom!=null);
		assert(nom.equalsIgnoreCase(ins.nom));
		if(source!=null)
		{
			source.check_egal(ins.source);
		}
		else
		{
			assert(ins.source==null);
		}
	}
	
	public String nom;
	public CISource source;

}
