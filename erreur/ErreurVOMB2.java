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
public class ErreurVOMB2 extends Erreur {

	public ErreurVOMB2(Classe classe,Expr expr,boolean entier)
	{
		assert(classe!=null);
		assert(expr!=null);
		this.classe=classe;
		this.expr=expr;
		this.entier=entier;
		constant=false;
	}

	public ErreurVOMB2(Classe classe,Expr expr)
	{
		assert(classe!=null);
		assert(expr!=null);
		this.classe=classe;
		this.expr=expr;
		constant=true;
	}

	Classe classe;
	Expr expr;
	boolean entier,constant;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(constant)
		{
			return "Dans la classe "+classe.nom+", la constante"+
						" d'inspection n'est pas constante a la "+
						"ligne "+expr.debut();
		}
		else
		{
			if(entier)
				return "Dans la classe "+classe.nom+", la constante"+
						" d'inspection n'est pas un INTEGER a la "+
						"ligne "+expr.debut();
			else
				return "Dans la classe "+classe.nom+", la constante"+
						" d'inspection n'est pas un CHARACTER a la "+
						"ligne "+expr.debut();
		}
	}

}
