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
 * Erreur VUEX no 1
 * L'attribut dans un chainage d'appel n'existe pas dans la classe
 * correspondante
 */
public class ErreurVUEX1 extends Erreur {

		public ErreurVUEX1(Classe classe,NomFeature nom,Position pos)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.classe=classe;
		this.nom=nom;
		this.pos=pos;
	}

	public Classe classe;
	public NomFeature nom;
	public Position pos;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "L'attribut "+nom+" n'est pas declaré dans la classe "+
				classe.nom+" à la ligne "+pos.toString();
	}


}
