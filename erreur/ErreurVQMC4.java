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
public class ErreurVQMC4 extends Erreur {

	public ErreurVQMC4(Classe classe,Feature feature)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(feature instanceof FeatureExpr);
		this.classe=classe;
		this.feature=(FeatureExpr)feature;
	}

	Classe classe;
	FeatureExpr feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'attribut constant"+
				" est une constante réelle mais n'est pas "+
				"déclaré REAL ou DOUBLE a la ligne "+feature.debut();
	}


}
