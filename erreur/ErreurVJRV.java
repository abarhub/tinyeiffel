/*
 * Created on 7 déc. 2003
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
public class ErreurVJRV extends Erreur {

	public ErreurVJRV(Classe cl,Instr_TentAffect instr,
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
	Instr_TentAffect instr;
	Type type_src,type_cible;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", la tentative d'affectation "+
				"a la ligne "+instr.debut()+" est incorrecte, car "+type_src+
				" n'est pas de type référence";
	}


}
