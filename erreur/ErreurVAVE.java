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
public class ErreurVAVE extends Erreur {

	public ErreurVAVE(Classe classe,Instr instr)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(instr instanceof Instr_Loop);
		assert(((Instr_Loop)instr).variant!=null);
		this.classe=classe;
		this.instr=(Instr_Loop)instr;
	}

	Classe classe;
	Instr_Loop instr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", le variant a la "+
				"ligne "+instr.variant.debut()+" n'est pas un entier";
	}

}
