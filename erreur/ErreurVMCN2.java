/*
 * Created on 2 nov. 2003
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
public class ErreurVMCN2 extends Erreur {

	
	public ErreurVMCN2(Classe cl, Heritage herit_anc,
						Feature f,NomFeature nom)
	{
		assert(cl!=null);
		assert(herit_anc!=null);
		assert(f!=null);
		assert(nom!=null);
		classe=cl;
		heritage=herit_anc;
		feature=f;
		this.nom=nom;
	}

	Classe classe;
	Heritage heritage;
	NomFeature nom;
	Feature feature;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "conflit pour l'attribut "+nom+" dans la "+
				"classe "+classe.nom+" entre l'héritage "+
				heritage.type.nom+
				" à la ligne "+heritage.debut()+
				" et l'attribut à la ligne "+feature.debut();
	}

}
