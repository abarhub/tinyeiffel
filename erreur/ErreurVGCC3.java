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
public class ErreurVGCC3 extends Erreur {

	public ErreurVGCC3(Classe classe,Instr_Creation instr,
						Type type)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(type!=null);
		this.classe=classe;
		this.instr=instr;
		this.type=type;
		type_erreur=non_reference;
	}

	public ErreurVGCC3(Classe classe,Instr_Creation instr,
						Type type,Type type2)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(type!=null);
		assert(type2!=null);
		this.classe=classe;
		this.instr=instr;
		this.type=type;
		this.type2=type2;
		type_erreur=non_conforme;
	}

	Classe classe;
	Instr_Creation instr;
	Type type,type2;
	int type_erreur;
	
	final int non_reference=1;
	final int non_conforme=2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(type_erreur==non_reference)
			return "Dans la classe "+classe.nom+", l'instruction de "+
				"création utilise le type expended "+type
				+" a la ligne "+instr.debut();
		else
			return "Dans la classe "+classe.nom+", l'instruction de "+
						"création utilise le type "+type
						+" qui n'est pas conforme a "+type2+
						" a la ligne "+instr.debut();
	}


}
