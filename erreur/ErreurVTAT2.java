/*
 * Created on 5 déc. 2003
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
public class ErreurVTAT2 extends Erreur {

	public ErreurVTAT2(Classe classe,Type type1,
						Type type2,int err)
	{
		assert(classe!=null);
		assert(type1!=null);
		assert(type2!=null);
		assert(err>=Type_expanse&&err<=Type_ancre);
		this.classe=classe;
		this.type1=type1;
		this.type2=type2;
		no_erreur=err;
	}

	public ErreurVTAT2(Classe classe,Type type1)
	{
		assert(classe!=null);
		assert(type1!=null);
		this.classe=classe;
		this.type1=type1;
		this.type2=null;
		no_erreur=Type_inconnue;
	}

	Classe classe;
	Type type1,type2;
	int no_erreur;

	public static final int Type_expanse=1;
	public static final int Type_generique=2;
	public static final int Type_ancre=3;
	public static final int Type_inconnue=4; 

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		switch(no_erreur)
		{
			case Type_expanse:
				return "Dans la classe "+classe.nom+", le type "+
						"ancré "+type1+" a la ligne "+
						type1.debut()+" pointe vers le type "+
						"expanse "+type2+" a la ligne "+
						type2.debut();
			case Type_generique:
				return "Dans la classe "+classe.nom+", le type "+
						"ancré "+type1+" a la ligne "+
						type1.debut()+" pointe vers le type "+
						"générique "+type2+" a la ligne "+
						type2.debut();
			case Type_ancre:
				return "Dans la classe "+classe.nom+", le type "+
						"ancré "+type1+" a la ligne "+
						type1.debut()+" pointe vers le type "+
						"ancré "+type2+" a la ligne "+
						type2.debut();
			case Type_inconnue:
				return "Dans la classe "+classe.nom+", le type "+
						"ancré "+type1+" a la ligne "+
						type1.debut()+" pointe vers rien";
			default:
				assert(false);
		}
		assert(false);
		return null;
	}


}
