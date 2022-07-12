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
public class ErreurVDRD7 extends Erreur {

	public ErreurVDRD7(Classe classe,
							Feature feature_directe,
							Heritage heritage,
							NomFeature nom)
	{
		this.feature_directe=feature_directe;
		this.classe=classe;
		this.heritage=heritage;
		this.nom=nom;
	}

	Feature feature_directe;
	Classe classe;
	Heritage heritage;
	NomFeature nom;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(feature_directe.est_external())
			return "Impossible de redefinir l'attribut non external "+
						nom+" a la ligne "+heritage.debut()+
						" en un attribut external a la ligne "+
						feature_directe.debut();
		else
			return "Impossible de redefinir l'attribut external "+
				nom+" a la ligne "+heritage.debut()+
				" en un attribut non external a la ligne "+
				feature_directe.debut();
	}


}
