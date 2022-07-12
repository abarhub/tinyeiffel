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
public class ErreurVGCP4 extends Erreur {

	public ErreurVGCP4(Classe classe,NomFeature nom,
						Feature feature)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(feature!=null);
		assert(feature instanceof FeatureRoutine);
		assert(((FeatureRoutine)feature).once);
		this.classe=classe;
		this.nom=nom;
		this.feature=feature;
	}

	Classe classe;
	NomFeature nom;
	Feature feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", la routine "+
				"de creation "+nom+" est déclaré once a la "+
				"ligne "+feature.debut();
	}

}
