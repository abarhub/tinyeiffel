/*
 * Created on 8 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ElementCar extends Elt {

	/**
	 * 
	 */
	public ElementCar(Expr nom,char v) {
		super();
		this.nom=nom;
		this.valeur=v;
	}

	char valeur;

}
