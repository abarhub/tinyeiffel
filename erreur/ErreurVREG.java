/*
 * Created on 1 déc. 2003
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
public class ErreurVREG extends Erreur {

	public ErreurVREG(Classe classe,Feature feature,
						DeclareVar nom1,DeclareVar nom2,
						boolean local)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(nom1!=null);
		assert(nom2!=null);
		this.classe=classe;
		this.feature=feature;
		this.nom1=nom1;
		this.nom2=nom2;
		this.local=local;
	}

	Classe classe;
	Feature feature;
	DeclareVar nom1,nom2;
	boolean local;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(local)
			return "Dans la classe "+classe.nom+", le nom du parametre"+
				" formel "+nom1.nom+" est declaré plusieurs fois "+
				" aux lignes "+nom1.debut()+" et "+nom2.debut();
		else
			return "Dans la classe "+classe.nom+", le nom de la"+
				" variable locale "+nom1.nom+" est declaré plusieurs fois "+
				" aux lignes "+nom1.debut()+" et "+nom2.debut();
	}


}
