/*
 * Created on 30 déc. 2003
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
public class ErreurVXRT extends Erreur {

	public ErreurVXRT(Classe classe,Feature feature,
						Instr_Retry instr)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(instr!=null);
		this.classe=classe;
		this.feature=feature;
		this.instr=instr;
	}

	Classe classe;
	Feature feature;
	Instr_Retry instr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'instruction "+
				"retry n'est pas dans la section rescue "+
				"a la ligne "+instr.debut();
	}

}
