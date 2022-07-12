/*
 * Created on 30 nov. 2003
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
public class ErreurVCCH2 extends Erreur {

	public ErreurVCCH2(Classe classe)
	{
		assert(classe!=null);
		assert(classe.deferred);
		this.classe=classe;
	}

	Classe classe;

	public String toMsg() {
		return "La classe abstraite "+classe.nom+" n'a pas d'"+
				"attributs deferred";
	}


}
