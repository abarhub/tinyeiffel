/*
 * Created on 10 janv. 2004
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
public class ErreurVAOL1 extends Erreur {

	public ErreurVAOL1(Classe classe,Expr_Unaire expr)
	{
		assert(classe!=null);
		assert(expr!=null);
		this.classe=classe;
		this.expr=expr;
	}

	Classe classe;
	Expr_Unaire expr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'expression "+expr+
				" n'est pas utilisé dans une section ensure "+
				"a la ligne "+expr.debut();
	}

}
