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
public abstract class CIInstruction {

	/**
	 * 
	 */
	/*public CIInstruction() {
		super();
		// TODO Auto-generated constructor stub
	}*/

	public final void affiche(PrintStream out) {out.println(toString());};

	public abstract void toXML(PrintStream out);

	public abstract void check_egal(CIInstruction instr);

}
