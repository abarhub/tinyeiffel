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
public class ErreurVDUS2 extends Erreur {

	public ErreurVDUS2(Classe classe,NomFeature nom,
						Heritage heritage,Feature feature)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(heritage!=null);
		assert(feature!=null);
		this.classe=classe;
		this.nom=nom;
		this.heritage=heritage;
		this.feature=feature;
	}

	Classe classe;
	NomFeature nom;
	Heritage heritage;
	Feature feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(feature.est_variable())
			return "Dans la classe "+classe.nom+" "+
				"l'attribut "+nom+" herite de "+heritage.type.nom+
				" est undefine a la ligne "+heritage.debut()+
				" alors qu'il est une variable a la ligne "+feature.debut();
		else
			return "Dans la classe "+classe.nom+" "+
				"l'attribut "+nom+" herite de "+heritage.type.nom+
				" est undefine a la ligne "+heritage.debut()+
				" alors qu'il est frozen a la ligne "+feature.debut();
	}


}
