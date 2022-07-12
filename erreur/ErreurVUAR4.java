/*
 * Created on 15 mai 2004
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
public class ErreurVUAR4 extends Erreur {

	public ErreurVUAR4(Expr expr,Classe classe1,Position pos1)
	{
		assert(classe1!=null);
		assert(expr!=null);
		assert(pos1!=null);
		this.classe=classe1;
		this.expr=expr;
		this.pos=pos1;
	}

	public Classe classe;
	public Expr expr;
	Position pos;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Tentative de prendre l'adresse d'une expression "+
				"qui n'est pas un attribut "+
				"a la ligne "+pos+" dans la classe "+classe.nom;
	}

}
