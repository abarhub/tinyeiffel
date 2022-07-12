/*
 * Created on 11 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * Erreur VWEQ
 * L'expression doit etre de type BOOLEAN
 */
public class ErreurVWEQ extends Erreur {

	public ErreurVWEQ(Classe classe,Expr_Binaire expr,Type t1,Type t2)
	{
		assert(t1!=null);
		assert(t2!=null);
		assert(classe!=null);
		assert(expr!=null);
		assert(expr.op==Expr.Egal||expr.op==Expr.Diff);
		this.expr=expr;
		this.t1=t1;
		this.t2=t2;
		this.classe=classe;
	}

	public Expr_Binaire expr;
	Type t1,t2;
	Classe classe;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Erreur dans la comparaison dans la classe "+classe.nom+
				" : les types "+t1+" et "+t2+" ne sont pas compatibles "+
				" a la ligne "+expr.debut();
	}

}
