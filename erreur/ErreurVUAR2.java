/*
 * Created on 11 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * Erreur VUAR no 2
 * Un des arguments réel lors d'un appel n'est pas 
 * conforme a sa declaration formelle
 */
public class ErreurVUAR2 extends Erreur {


	public ErreurVUAR2(NomFeature nom,int no_param,
							Classe classe1,Type type1,Position pos1,
							Classe classe2,Type type2,Position pos2)
	{
		assert(classe1!=null);
		assert(classe2!=null);
		assert(nom!=null);
		assert(no_param>=0);
		assert(type1!=null);
		assert(type2!=null);
		assert(pos1!=null);
		assert(pos2!=null):"type="+type2;
		assert(!type1.is_like);
		assert(!type2.is_like);
		this.classe1=classe1.nom;
		this.classe2=classe2.nom;
		this.no_param=no_param;
		this.type1=type1;
		this.type2=type2;
		this.pos1=pos1;
		this.pos2=pos2;
		this.nom=nom;
	}

	public ErreurVUAR2(NomFeature nom,int no_param,
			String classe1,Type type1,Position pos1,
			String classe2,Type type2,Position pos2)
 {
		assert (classe1 != null);
		assert (classe2 != null);
		assert (nom != null);
		assert (no_param >= 0);
		assert (type1 != null);
		assert (type2 != null);
		assert (pos1 != null);
		assert (pos2 != null) : "type=" + type2;
		assert (!type1.is_like);
		assert (!type2.is_like);
		this.classe1 = classe1;
		this.classe2 = classe2;
		this.no_param = no_param;
		this.type1 = type1;
		this.type2 = type2;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.nom = nom;
	}
	
	public String classe1,classe2;
	public NomFeature nom;
	int no_param;
	Position pos1,pos2;
	Type type1,type2;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return "Le parametre numero "+(no_param+1)+" de type "+
				type1+" n'est pas conforme avec le parametre formel de type "+
				type2+" pour l'appel "+
				" a la ligne "+pos1+" de la classe "+classe1+
				" et la declaration a la ligne "+pos2+
				" de la classe "+classe2+
				" pour la fonction "+nom;
	}


}
