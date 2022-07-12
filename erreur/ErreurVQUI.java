/*
 * Created on 3 déc. 2003
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
public class ErreurVQUI extends Erreur {

	public ErreurVQUI(Classe classe,Feature feature)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(feature instanceof FeatureUnique);
		this.classe=classe;
		this.feature=feature;
	}

	Classe classe;
	Feature feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+
			", un attribut unique doit avoir un type "+
			"de retour integer, a la ligne "+
			((feature.type_retour==null)?feature.debut():
					feature.type_retour.debut());
	}

}
