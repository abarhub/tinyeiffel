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
public class ErreurVTEC2 extends Erreur {

	public ErreurVTEC2(Classe classe,Type type,Classe classe2)
	{
		assert(classe!=null);
		assert(type!=null);
		assert(classe2!=null);
		assert(classe2.creation!=null);
		assert(classe2.creation.length>0);
		this.classe=classe;
		this.type=type;
		this.classe2=classe2;
		this.creation=null;
	}

	public ErreurVTEC2(Classe classe,Type type,Classe classe2,
						Creation creation)
	{
		assert(classe!=null);
		assert(type!=null);
		assert(classe2!=null);
		assert(creation!=null);
		this.classe=classe;
		this.type=type;
		this.classe2=classe2;
		this.creation=creation;
	}

	Classe classe,classe2;
	Type type;
	Creation creation;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		// TODO : a ameliorer
		if(creation==null)
			return "Dans la classe "+classe.nom+", le type "+type+
				" a la ligne "+type.debut()+" est invalide car"+
				" la classe "+classe2.nom+" a plus d'une procedure "+
				"de creation a la ligne "+classe2.creation[0].debut();
		else
			return "Dans la classe "+classe.nom+", le type "+type+
				" a la ligne "+type.debut()+" est invalide car"+
				" la classe "+classe2.nom+" a une procedure "+
				"de creation avec des parametres a la ligne "+
				creation.debut();
	}

}
