/*
 * Created on 20 nov. 2003
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
public class ErreurVDRS1 extends Erreur {

	public ErreurVDRS1(Classe classe,Heritage heritage,
						NomFeature nom)
	{
		assert(classe!=null);
		assert(heritage!=null);
		assert(nom!=null);
		this.classe=classe;
		this.heritage=heritage;
		this.nom=nom;
	}

	Classe classe;
	Heritage heritage;
	NomFeature nom;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'attribut "+
				nom+" herité de "+heritage.type+" n'existe pas "+
				"a la ligne "+nom.debut();
	}

}
