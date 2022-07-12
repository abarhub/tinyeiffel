/*
 * Created on 28 déc. 2003
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
public class ErreurVGCC4 extends Erreur {

	public ErreurVGCC4(Classe classe,Instr_Creation instr,
						Type type)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(type!=null);
		this.classe=classe;
		this.instr=instr;
		this.type=type;
	}


	Classe classe;
	Instr_Creation instr;
	Type type;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'instruction de "+
				"création a un appel, mais la classe "+type+
				" n'a pas de section de création "+
				" a la ligne "+instr.debut();
	}

}
