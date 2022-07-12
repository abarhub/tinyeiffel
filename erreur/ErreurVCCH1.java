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
public class ErreurVCCH1 extends Erreur {

	public ErreurVCCH1(Classe classe,Feature feature)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(!classe.deferred);
		this.classe=classe;
		this.feature=feature;
	}

	Classe classe;
	Feature feature;

	public String toMsg() {
		return "La classe concrete "+classe.nom+" a des "+
				"attributs deferred a la ligne "+feature.debut();
	}

}
