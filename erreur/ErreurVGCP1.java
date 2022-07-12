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
public class ErreurVGCP1 extends Erreur {

	public ErreurVGCP1(Classe classe)
	{
		assert(classe!=null);
		assert(classe.creation!=null&&classe.creation.length>0);
		this.classe=classe;
	}

	Classe classe;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "La classe abstraite "+classe.nom+" a une "+
				"section création a la ligne "+
				classe.creation[0].tcreation.debut();
	}

}
