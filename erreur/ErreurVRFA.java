/*
 * Created on 1 déc. 2003
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
public class ErreurVRFA extends Erreur {

	public ErreurVRFA(Classe classe,Feature feature,
						NomFeature nom1,DeclareVar nom2)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(nom1!=null);
		assert(nom2!=null);
		this.classe=classe;
		this.feature=feature;
		this.nom1=nom1;
		this.nom2=nom2;
	}

	Classe classe;
	Feature feature;
	NomFeature nom1;
	DeclareVar nom2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", le nom du parametre"+
				" formel "+nom2.nom+" a la ligne "+nom2.debut()+
				" est en conflit avec l'attribut "+nom1.nom+
				" a la ligne "+nom1.debut();
	}

}
