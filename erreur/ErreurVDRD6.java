/*
 * Created on 20 nov. 2003
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
public class ErreurVDRD6 extends Erreur {


	public ErreurVDRD6(Classe classe,
							Feature feature_directe,
							Heritage heritage,
							NomFeature nom,
							boolean pb_variable,
							boolean pb_ancetre)
	{
		assert(classe!=null);
		assert(feature_directe!=null);
		assert(nom!=null);
		//assert(ancetre_variable||ancetre_expanded);
		this.feature_directe=feature_directe;
		this.classe=classe;
		this.heritage=heritage;
		this.nom=nom;
		this.pb_variable=pb_variable;
		this.pb_ancetre=pb_ancetre;
	}

	Feature feature_directe;
	Classe classe;
	Heritage heritage;
	NomFeature nom;
	boolean pb_variable;
	boolean pb_ancetre;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(pb_variable)
		{
			if(pb_ancetre)
			{
				return "Impossible de redefinir l'attribut non variable "+
						nom+" a la ligne "+heritage.debut()+
						" en un attribut variable a la ligne "+
						feature_directe.debut();
			}
			else
			{
				return "Impossible de redefinir l'attribut variable "+
						nom+" a la ligne "+heritage.debut()+
						" en un attribut non variable a la ligne "+
						feature_directe.debut();
			}
		}
		else
		{
			if(pb_ancetre)
			{
				return "Impossible de redefinir l'attribut non expanded "+
						nom+" a la ligne "+heritage.debut()+
						" en un attribut expanded a la ligne "+
						feature_directe.debut();
			}
			else
			{
				return "Impossible de redefinir l'attribut de type expanded "+
						nom+" a la ligne "+heritage.debut()+
						" en un attribut non expanded a la ligne "+
						feature_directe.debut();
			}
		}
			
	}


}
