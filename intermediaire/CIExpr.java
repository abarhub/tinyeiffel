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
public abstract class CIExpr {

	/**
	 * 
	 */
	/*public CIExpr() {
	}*/

	public abstract void toXML(PrintStream out);

	public abstract void check_egal(CIExpr e);

	public CIType type;

}
