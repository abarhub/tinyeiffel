/*
 * Created on 23 déc. 2003
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
public class ErreurVOMB5 extends Erreur {

	public ErreurVOMB5(Classe classe,Instr_Inspect instr,
						Expr expr)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(expr!=null);
		this.classe=classe;
		this.instr=instr;
		this.expr=expr;
	}
	
	Classe classe;
	Instr_Inspect instr;
	Expr expr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'instruction "+
				"d'inspection a au moins une constante "+
				"d'inspection unique et une constante positive "+
				"a la ligne "+expr.debut();
	}

}
