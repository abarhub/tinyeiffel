/*
 * Created on 4 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ErreurVSRC1 extends Erreur {

	public ErreurVSRC1(Classe classe)
	{
		assert(classe!=null);
		assert(classe.type.generique!=null&&
				classe.type.generique.length>0);
		this.classe=classe;
	}

	Classe classe;
	
	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "La classe racine "+classe.nom+" est generique "+
				"a la ligne "+classe.type.debut();
	}

}
