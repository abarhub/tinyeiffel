/*
 * Created on 30 nov. 2003
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
public class ErreurVFFD6 extends Erreur {

	public ErreurVFFD6(Classe classe,NomFeature nom,Feature feature)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(feature!=null);
		this.classe=classe;
		this.nom=nom;
		this.feature=feature;
	}

	Classe classe;
	NomFeature nom;
	Feature feature;

	public String toMsg() {
		if(feature.type_retour==null)
			return "Dans la classe "+classe.nom+", l'attribut "+nom+
				" doit avoir un type de retour a la "+
				"ligne "+feature.debut();
		else
			return "Dans la classe "+classe.nom+", l'attribut "+nom+
				" doit avoir un seul argument a la "+
				"ligne "+feature.debut();
	}


}
