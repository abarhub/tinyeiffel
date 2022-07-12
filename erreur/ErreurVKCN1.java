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
public class ErreurVKCN1 extends Erreur {

	public ErreurVKCN1(Classe classe,Expr expr)
	{
		assert(classe!=null);
		assert(expr!=null);
		this.classe=classe;
		this.expr=expr;
	}

	Classe classe;
	Expr expr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", l'appel n'est "+
				"pas une expression a la ligne "+expr.debut();
	}

}
