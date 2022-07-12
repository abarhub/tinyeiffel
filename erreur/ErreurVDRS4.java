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
public class ErreurVDRS4 extends Erreur {

	
	public ErreurVDRS4(Classe classe,NomFeature nom,
						Heritage heritage)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(heritage!=null);
		this.classe=classe;
		this.nom=nom;
		this.heritage=heritage;
	}


	public ErreurVDRS4(Classe classe,NomFeature nom,
						Heritage heritage,Feature feature_directe)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(heritage!=null);
		assert(feature_directe!=null);
		this.classe=classe;
		this.nom=nom;
		this.heritage=heritage;
		this.feature_directe=feature_directe;
	}

	Classe classe;
	NomFeature nom;
	Heritage heritage;
	Feature feature_directe;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(feature_directe!=null)
		{
			if(feature_directe.is_deferred())
			{
				return "Dans la classe "+classe.nom+" "+
					"l'attribut "+nom+" hérité de "+heritage.type.nom+
					" n'est pas redéclaré à la ligne "+heritage.debut();
			}
			else
			{
				return "Dans la classe "+classe.nom+" "+
					"l'attribut "+nom+" hérité de "+heritage.type.nom+
					" est déclaré redefini à la ligne "+heritage.debut()+
					" alors que c'est une concrétisation";
			}
		}
		else
		{
			return "Dans la classe "+classe.nom+" "+
				"l'attribut "+nom+" hérité de "+heritage.type.nom+
				" est déclaré redéfinie à la ligne "+heritage.debut()+
				" mais ne l'est pas effectivement";
		}
	}

}
