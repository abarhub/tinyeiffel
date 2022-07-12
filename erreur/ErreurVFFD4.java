/*
 * Created on 9 nov. 2003
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
public class ErreurVFFD4 extends Erreur {

	public ErreurVFFD4(Attribut attribut)
	{
		assert(attribut!=null);
		this.attribut=attribut;
	}

	public Attribut attribut;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "L'attribut "+attribut+" est deferred, et frozen "+
				"dans la classe "+attribut.classe.nom+ "("+
				attribut.nom.debut()+")";
	}

}
