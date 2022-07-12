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
public class ErreurVOMB1 extends Erreur {

	public ErreurVOMB1(Classe classe,Instr_Inspect instr,Type type)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(type!=null);
		this.classe=classe;
		this.instr=instr;
		this.type=type;
	}

	Classe classe;
	Instr_Inspect instr;
	Type type;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'expression "+
				"d'inspection n'est ni de type INTEGER, ni "+
				"CHARACTER, a la ligne "+
				((instr.expr==null)?instr.tinspect.debut():instr.expr.debut());
	}

}
