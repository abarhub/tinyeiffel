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
public class ErreurVQMC extends Erreur {

	public ErreurVQMC(Classe classe,Feature feature)
	{
		assert(classe!=null);
		assert(feature!=null);
		assert(feature instanceof FeatureExpr);
		this.classe=classe;
		this.feature=(FeatureExpr)feature;
		simple=false;
	}

	public ErreurVQMC(Type type_classe,Feature feature)
	{
		assert(type_classe!=null);
		assert(feature!=null);
		assert(feature instanceof FeatureExpr);
		this.type_classe=type_classe;
		this.feature=(FeatureExpr)feature;
		simple=true;
	}
	
	Classe classe;
	FeatureExpr feature;
	Type type_classe;
	boolean simple;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(simple)
		{
			return "Dans la classe "+type_classe.nom+", l'attribut constant"+
			" n'est pas une constante "+
			"a la ligne "+feature.debut();
		}
		else
		{
			return "Dans la classe "+classe.nom+", l'attribut constant"+
				" n'est pas une constante "+
				"a la ligne "+feature.debut();
		}
	}

}
