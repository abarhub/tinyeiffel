/*
 * Created on 10 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * Erreur VJAR
 * La source d'une affectation n'est pas conforme avec la cible
 */
public class ErreurVJAR extends Erreur {

	public ErreurVJAR(Classe cl,Instr_Affect instr,
						Type type_src,Type type_cible)
	{
		assert(cl!=null);
		assert(instr!=null);
		assert(type_src!=null);
		assert(type_cible!=null);
		classe=cl;
		this.instr=instr;
		this.type_src=type_src;
		this.type_cible=type_cible;
	}

	Classe classe;
	Instr_Affect instr;
	Type type_src,type_cible;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'affectation "+
				"a la ligne "+instr.debut()+" est incorrecte, car "+type_src+
				" n'est pas conforme a "+type_cible;
	}

}
