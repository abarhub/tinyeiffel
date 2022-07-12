/*
 * Created on 23 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Couple {

	/**
	 * 
	 */
	public Couple(Donnee d,Type t) {
		assert(d!=null);
		assert(t!=null);
		this.donnee=d;
		this.type=t;
	}

	public Donnee donnee;
	public Type type;
}
