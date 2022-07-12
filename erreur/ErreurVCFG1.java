/*
 * Created on 22 nov. 2003
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
public class ErreurVCFG1 extends Erreur {

	public ErreurVCFG1(Classe classe,Type type)
	{
		assert(classe!=null);
		assert(type!=null);
		this.classe=classe;
		this.type=type;
	}

	Classe classe;
	Type type;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Le type générique "+type+" a la ligne "+type.debut()+
				" dans la classe "+classe.nom+
				" est deja présent dans l'univers";
	}

}
