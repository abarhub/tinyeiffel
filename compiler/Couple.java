/*
 * Created on 21 janv. 2004
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
public class Couple {

	/**
	 * 
	 */
	public Couple(Classe classe,int no,Conversion conv) {
		assert(classe!=null);
		assert(no>=0);
		this.classe=classe;
		this.no=no;
		conversion=conv;
	}

	Classe classe;
	Conversion conversion;
	int no;

}
