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
public class ErreurVSRC2 extends Erreur {

	public ErreurVSRC2(Classe classe,NomFeature nom,
			Feature feature)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(feature!=null);
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
		return "La classe racine a l'attribut de création "+
				nom+" qui n'a pas le bon format de parametre"+
				" a la ligne "+feature.debut();
	}

}
