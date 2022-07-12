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
public class ErreurVCFG2 extends Erreur {

	public ErreurVCFG2(Classe classe,Type type1,Type type2)
	{
		assert(classe!=null);
		assert(type1!=null);
		assert(type2!=null);
		this.classe=classe;
		this.type1=type1;
		this.type2=type2;
	}

	Classe classe;
	Type type1,type2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Le type generique "+type1+" a la ligne "+type1.debut()+
				" dans la classe "+classe.nom+
				" est deja présent dans la liste de générique "+
				" a la ligne "+type2.debut();
	}


}
