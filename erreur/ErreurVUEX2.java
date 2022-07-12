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
public class ErreurVUEX2 extends Erreur {

	public ErreurVUEX2(Classe classe,NomFeature nom,Position pos)
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
