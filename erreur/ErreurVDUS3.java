/*
 * Created on 6 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * Erreur VDUS no 3
 * L'attribut est effectif plusieurs fois dans la classe
 */
public class ErreurVDUS3 extends Erreur {

	public ErreurVDUS3(Classe c,Attribut a,Heritage heritage)
	{
		assert(c!=null);
		assert(a!=null);
		assert(heritage!=null);
		assert(c.nom.compareToIgnoreCase(a.classe.nom)!=0);
		classe=c;
		attribut=a;
		this.heritage=heritage;
	}
	
	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Erreur dans la classe "+classe.nom+
				" a la ligne "+heritage.debut()+" : "+
				"l'attribut undefine "+attribut+" n'est pas"+
				" concret dans la classe "+attribut.classe.nom+
				" a la ligne "+attribut.nom.debut();
	}

	public Attribut attribut;
	public Classe classe;
	public Heritage heritage;

}
