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
 * Erreur VUAR no 1
 * Le nombre de parametre d'un attribut declaré n'est pas
 * conforme a l'appel de cet attribut 
 */
public class ErreurVUAR1 extends Erreur {

	public ErreurVUAR1(NomFeature nom,
							Classe classe1,int nb_param1,Position pos1,
							Classe classe2,int nb_param2,Position pos2)
	{
		assert(classe1!=null);
		assert(classe2!=null);
		assert(nom!=null);
		assert(nb_param1>=0);
		assert(nb_param2>=0);
		assert(nb_param1!=nb_param2);
		assert(pos1!=null);
		assert(pos2!=null);
		this.classe1=classe1;
		this.classe2=classe2;
		this.nb_param1=nb_param1;
		this.nb_param2=nb_param2;
		this.pos1=pos1;
		this.pos2=pos2;
		this.nom=nom;
		simple=false;
	}

	public ErreurVUAR1(NomFeature nom,
			String classe1,int nb_param1,Position pos1,
			String classe2,int nb_param2,Position pos2)
	{
		assert(classe1!=null);
		assert(classe2!=null);
		assert(nom!=null);
		assert(nb_param1>=-1);
		assert(nb_param2>=-1);
		assert(nb_param1!=nb_param2);
		assert(pos1!=null);
		assert(pos2!=null);
		this.nom_classe1=classe1;
		this.nom_classe2=classe2;
		this.nb_param1=nb_param1;
		this.nb_param2=nb_param2;
		this.pos1=pos1;
		this.pos2=pos2;
		this.nom=nom;
		simple=true;
	}
	
	public Classe classe1,classe2;
	public String nom_classe1,nom_classe2;
	public NomFeature nom;
	int nb_param1,nb_param2;
	Position pos1,pos2;
	boolean simple;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(simple)
		{
			if(nb_param1==-1||nb_param2==-1)
			{// difference dans le type de retour
				
				return "Les types de retour ne sont pas les memes entre l'appel "+
					"a la ligne "+pos1+" de la classe "+nom_classe1+
					" et la declaration a la ligne "+pos2+
					" de la classe "+nom_classe2+
					" pour la fonction "+nom;
			}
			else
			{// difference dans le nombre de parametres
				return "Nombre de parametre different entre l'appel "+
					"a la ligne "+pos1+" de la classe "+nom_classe1+
					" et la declaration a la ligne "+pos2+
					" de la classe "+nom_classe2+
					" pour la fonction "+nom;
			}
		}
		else
		{
			return "Nombre de parametre different entre l'appel "+
				"a la ligne "+pos1+" de la classe "+classe1.nom+
				" et la declaration a la ligne "+pos2+
				" de la classe "+classe2.nom+
				" pour la fonction "+nom;
		}
	}

}
