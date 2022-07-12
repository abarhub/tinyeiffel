/*
 * Created on 27 déc. 2003
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
public class ErreurVKCN2 extends Erreur {

	public ErreurVKCN2(Classe classe,Instr_Appel instr)
	{
		assert(classe!=null);
		assert(instr!=null);
		this.classe=classe;
		this.instr=instr;
	}

	Classe classe;
	Instr_Appel instr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'appel n'est "+
				"pas une instruction a la ligne "+instr.debut();
	}

}
