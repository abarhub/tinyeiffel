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
public class ErreurVGCP2 extends Erreur {

	public ErreurVGCP2(Classe classe,NomFeature nom1,
		NomFeature nom2)
	{
		assert(classe!=null);
		assert(nom1!=null);
		assert(nom2!=null);
		this.classe=classe;
		this.nom1=nom1;
		this.nom2=nom2;
	}

	Classe classe;
	NomFeature nom1,nom2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'attribut "+
				nom1+" est déclaré plusieurs fois dans la "+
				"section création aux lignes "+nom2.debut()+
				" et "+nom1.debut();
	}

}
