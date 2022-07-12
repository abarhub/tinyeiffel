/*
 * Created on 3 déc. 2003
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
public class ErreurVGCP5 extends Erreur {

	public ErreurVGCP5(Classe classe)
	{
		assert(classe!=null);
		assert(classe.expanded);
		assert(classe.creation!=null&&classe.creation.length>0);
		trop_procedure=true;
		this.classe=classe;
	}
	
	public ErreurVGCP5(Classe classe,NomFeature nom,
						Feature feature)
	{
		assert(classe!=null);
		assert(classe.expanded);
		assert(nom!=null);
		assert(feature!=null);
		trop_procedure=false;
		this.classe=classe;
		this.nom=nom;
		this.feature=feature;
	}
	
	boolean trop_procedure;
	Classe classe;
	NomFeature nom;
	Feature feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(trop_procedure)
			return "Dans la classe expended "+classe.nom+", "+
				"il y a plus de une procedure de création "+
				"a la ligne "+classe.creation[0].debut();
		else
			return "Dans la classe expended "+classe.nom+", "+
					"la procedure de création "+nom+" a des parametres "+
					"a la ligne "+feature.debut();
	}

}
