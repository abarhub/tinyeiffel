/*
 * Created on 20 nov. 2003
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
public class ErreurVDUS4 extends Erreur {

	
	public ErreurVDUS4(Classe classe,Heritage heritage,
						NomFeature nom1,NomFeature nom2)
	{
		assert(classe!=null);
		assert(heritage!=null);
		assert(nom1!=null);
		assert(nom2!=null);
		this.classe=classe;
		this.heritage=heritage;
		this.nom1=nom1;
		this.nom2=nom2;
	}

	Classe classe;
	Heritage heritage;
	NomFeature nom1,nom2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Dans la classe "+classe.nom+", la primitive "+
				nom1+" de l'ancetre "+heritage.type+" est declaré "+
				" plusieurs fois undefine ("+nom1.debut()+" et "+
				nom2.debut()+")";
	}


}
