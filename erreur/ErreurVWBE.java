/*
 * Created on 6 déc. 2003
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
public class ErreurVWBE extends Erreur {

	public ErreurVWBE(Classe classe,Expr e,int type)
	{
		assert(classe!=null);
		assert(e!=null);
		assert(type>=type_if&&type<=type_loop);
		this.classe=classe;
		this.expr=e;
		this.type=type;
	}

	Classe classe;
	Expr expr;
	int type;

	public static final int type_if=1;
	public static final int type_elseif=2;
	public static final int type_check=3;
	public static final int type_invariant=4;
	public static final int type_require=5;
	public static final int type_ensure=6;
	public static final int type_loop_inv=7;
	public static final int type_loop=8;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		switch(type)
		{
			case type_if:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour le if";
			case type_elseif:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour le elseif";
			case type_check:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour le check";
			case type_invariant:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour l'invariant";
			case type_require:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour la précondition";
			case type_ensure:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour la postcondition";
			case type_loop_inv:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour l'invariant de boucle";
			case type_loop:
				return "Dans la classe "+classe.nom+
						", l'expression a la ligne "+
						expr.debut()+" doit etre de type "+
						"BOOLEAN pour la boucle";
			default:
				assert(false);
				return null;
		}
		
	}

}
