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
public class ErreurVGCP3 extends Erreur {

	public ErreurVGCP3(Classe classe,NomFeature nom)
	{
		assert(classe!=null);
		assert(nom!=null);
		this.classe=classe;
		this.nom=nom;
	}

	Classe classe;
	NomFeature nom;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", la routine "+
				"de création "+nom+" a la ligne "+nom.debut()+
				" n'existe pas";
	}

}
