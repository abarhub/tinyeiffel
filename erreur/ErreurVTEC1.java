/*
 * Created on 6 déc. 2003
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
public class ErreurVTEC1 extends Erreur {

	public ErreurVTEC1(Classe classe,Type type,Classe classe2)
	{
		assert(classe!=null);
		assert(type!=null);
		assert(classe2!=null);
		this.classe=classe;
		this.type=type;
		this.classe2=classe2;
	}

	Classe classe,classe2;
	Type type;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", le type "+type+
				" a la ligne "+type.debut()+" est invalide car"+
				" la classe "+classe2.nom+" est generique"+
				" a la ligne "+classe2.type.debut();
	}

}
