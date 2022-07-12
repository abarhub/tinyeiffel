/*
 * Created on 29 déc. 2003
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
public class ErreurVGCC6 extends Erreur {

	public ErreurVGCC6(Classe classe,Instr_Creation instr,
						Feature f,int type)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(f!=null);
		assert(type==retour||type==once);
		this.classe=classe;
		this.instr=instr;
		this.feature=f;
		this.type=type;
	}

	public ErreurVGCC6(Classe classe,Instr_Creation instr)
	{
		assert(classe!=null);
		assert(instr!=null);
		this.classe=classe;
		this.instr=instr;
		this.type=visible;
	}


	Classe classe;
	Instr_Creation instr;
	Feature feature;
	int type;
	public static final int retour=1;
	public static final int once=2;
	public static final int visible=3;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(type==retour)
			return "Dans la classe "+classe.nom+", l'instruction de "+
				"création appelle une routine avec un type de retour"+
				" a la ligne "+instr.debut();
		else if(type==once)
			return "Dans la classe "+classe.nom+", l'instruction de "+
					"création appelle une routine once"+
					" a la ligne "+instr.debut();
		else
			return "Dans la classe "+classe.nom+", l'instruction de "+
					"création appelle une routine non visible"+
					" a la ligne "+instr.debut();
	}

}
