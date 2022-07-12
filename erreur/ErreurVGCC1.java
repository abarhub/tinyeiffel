/*
 * Created on 28 d�c. 2003
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
public class ErreurVGCC1 extends Erreur {

	public ErreurVGCC1(Classe classe,Instr_Creation instr,Type type)
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
				"cr�ation utilise le type g�n�rique "+type
				+" a la ligne "+instr.debut();
	}

}
