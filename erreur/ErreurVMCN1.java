/*
 * Created on 18 nov. 2003
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
public class ErreurVMCN1 extends Erreur {

	
	public ErreurVMCN1(Classe cl, Heritage herit_anc1,
						Heritage herit_anc2,NomFeature nom)
	{
		assert(cl!=null);
		assert(herit_anc1!=null);
		assert(herit_anc2!=null);
		assert(nom!=null);
		classe=cl;
		heritage1=herit_anc1;
		heritage2=herit_anc2;
		this.nom=nom;
	}

	Classe classe;
	Heritage heritage1,heritage2;
	NomFeature nom;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "conflit pour l'attribut deferred "+nom+" dans la "+
				"classe "+classe.nom+"entre les heritages "+
				heritage1.type.nom+" et "+heritage2.type.nom+
				" aux lignes "+heritage1.debut()+
				" et "+heritage2.debut();
	}

}
