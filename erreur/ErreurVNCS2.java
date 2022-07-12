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
public class ErreurVNCS2 extends Erreur {

	public ErreurVNCS2(NomFeature nom,Classe classe,
						NomFeature nom_ancetre,Classe classe_ancetre,
						int no_param,Type type,Type type_ancetre)
	{
		assert(nom!=null);
		assert(classe!=null);
		assert(nom_ancetre!=null);
		assert(classe_ancetre!=null);
		assert(no_param>=-1);
		//assert(type!=null);
		//assert(type_ancetre!=null);
		this.nom=nom;
		this.classe=classe;
		this.nom_ancetre=nom_ancetre;
		this.classe_ancetre=classe_ancetre;
		this.no_param=no_param;
		this.type=type;
		this.type_ancetre=type_ancetre;
	}

	NomFeature nom,nom_ancetre;
	Classe classe,classe_ancetre;
	int no_param;
	Type type,type_ancetre;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(no_param==-1)
			return "Le type de retour de l'attribut "+nom+" dans la classe "+
						classe.nom+" de type "+type+
						" n'est pas conforme a "+type_ancetre+" de l'ancetre "+
						classe_ancetre.nom+" aux lignes "+nom.debut()+" et "+
						nom_ancetre.debut();
		else
			return "Le parametre numero "+(no_param+1)+" de l'attribut "+nom+" dans la classe "+
				classe.nom+" de type "+type+
				" n'est pas conforme a "+type_ancetre+" de l'ancetre "+
				classe_ancetre.nom+" aux lignes "+nom.debut()+" et "+
				nom_ancetre.debut();
	}

}
