/*
 * Created on 10 janv. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.lace;

import tinyeiffel.ast.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NomSysteme {

	/**
	 * 
	 */
	public NomSysteme(String nom,Token token) {
		assert(nom!=null);
		assert(token!=null);
		this.nom=nom;
		this.token=token;
	}

	public String nom;
	Token token;

}
