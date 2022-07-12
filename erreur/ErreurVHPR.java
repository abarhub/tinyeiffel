/*
 * Created on 9 nov. 2003
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
public class ErreurVHPR extends Erreur {

	public ErreurVHPR(Classe cl1,Classe cl2)
	{
		classe_enfant=cl1;
		classe_parent=cl2;
	}

	Classe classe_enfant,classe_parent;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Erreur: il y a un cycle d'heritage ("+
				classe_enfant.nom+"->...->"+classe_parent.nom+
				"->"+classe_enfant.nom+")";
	}

}
