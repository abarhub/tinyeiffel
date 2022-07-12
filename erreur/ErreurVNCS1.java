/*
 * Created on 19 nov. 2003
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
public class ErreurVNCS1 extends Erreur {

	public ErreurVNCS1(NomFeature nom,Classe classe,
						NomFeature nom_ancetre,Classe classe_ancetre)
	{
		assert(nom!=null);
		assert(classe!=null);
		assert(nom_ancetre!=null);
		assert(classe_ancetre!=null);
		this.nom=nom;
		this.classe=classe;
		this.nom_ancetre=nom_ancetre;
		this.classe_ancetre=classe_ancetre;
	}

	NomFeature nom,nom_ancetre;
	Classe classe,classe_ancetre;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Le nombre de parametre de "+nom+" dans la classe "+
				classe.nom+
				" ne correspond pas a celui de l'ancetre "+
				classe_ancetre.nom+" aux lignes "+nom.debut()+" et "+
				nom_ancetre.debut();
	}

}
