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
public class ErreurVGCC5 extends Erreur {

	public ErreurVGCC5(Classe classe,Instr_Creation instr,
						Type type,int t)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(type!=null);
		assert(t==pas_appel||t==pas_fonction);
		this.classe=classe;
		this.instr=instr;
		this.type=type;
		this.t=t;
	}


	Classe classe;
	Instr_Creation instr;
	Type type;
	int t;
	public static final int pas_fonction=1;
	public static final int pas_appel=2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(t==pas_appel)
			return "Dans la classe "+classe.nom+", l'instruction de "+
				"création n'a pas d'appel, mais la classe "+type+
				" a une section de création "+
				" a la ligne "+instr.debut();
		else
			return "Dans la classe "+classe.nom+", l'instruction de "+
				"création a un appel vers "+instr.nom2+", mais la classe "+type+
				" n'a pas déclaré cette routine dans la "+
				"section de création a la ligne "+instr.debut();
	}


}
