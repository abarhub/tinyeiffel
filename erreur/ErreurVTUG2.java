/*
 * Created on 23 nov. 2003
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
public class ErreurVTUG2 extends Erreur {

	public ErreurVTUG2(Type reel,Type base)
	{
		assert(reel!=null);
		assert(base!=null);
		type_reel=reel;
		type_base=base;
	}

	Type type_reel,type_base;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Le nombre de parametres generiques de "+type_reel+
				" a la ligne "+type_reel.debut()+" ne correspond "+
				"pas a sa classe de base a la ligne "+
				type_base.debut();
	}

}
