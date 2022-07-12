/*
 * Created on 23 nov. 2003
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
public class ErreurVTCT extends Erreur {

	public ErreurVTCT(Type type,Classe classe)
	{
		assert(type!=null);
		assert(classe!=null);
		this.type=type;
		this.classe=classe;
		simple=false;
	}

	Type type;
	Classe classe;
	boolean simple;
	String nom_classe_inexistante,nom_classe;
	Position pos;

	public ErreurVTCT(String classe,Position pos)
	{
		assert(pos!=null);
		assert(classe!=null);
		this.pos=pos;
		this.nom_classe_inexistante=classe;
		nom_classe=pos.NomClasse();
		simple=true;
	}
	
	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(simple)
		{
			return "Dans la classe "+pos.fichier+" le type "+
			nom_classe_inexistante+" n'existe pas a la ligne "+pos;
		}
		else
		{
			return "Dans la classe "+classe.nom+" le type "+type+
				" n'existe pas a la ligne "+type.debut();
		}
	}

}
