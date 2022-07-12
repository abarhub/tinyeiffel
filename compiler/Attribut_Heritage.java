/*
 * Created on 31 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Attribut_Heritage {

	public Attribut_Heritage(Table_Symbol table, NomFeature nom,
							boolean undefine,boolean redefine,
							Heritage herit,Attribut_Complet ac,
							Conversion conversion)
	{
		assert(table!=null);
		assert(nom!=null);
		assert(herit!=null):"nom="+nom+";classe="+table.classe.nom;
		assert(ac!=null);
		assert(conversion!=null);
		this.table=table;
		this.classe=ac.classe;
		//this.feature=feature;
		this.nom=nom;
		attribut=ac;
		this.redefine=redefine;
		this.undefine=undefine;
		heritage=herit;
		this.conversion=conversion;
		signature=new Signature(ac.signature,conversion);
	}

	public boolean est_deferred()
	{
		return attribut.est_deferred();
		/*Attribut_Complet ac;
		ac=attribut;
		if(ac.feature_directe!=null)
		{
			return ac.feature_directe.is_deferred();
		}
		else if(ac.attribut_reel==null)
		{
			return false;
		}
		else
		{
			return ac.feature_reel.is_deferred();
		}*/
	}

	public boolean frozen()
	{// TODO : a terminer
		//attribut.
		//assert(false);
		//return false;
		return attribut.frozen();
		
		//return getFeature().;
	}

	public Feature getFeature()
	{
		assert(attribut!=null);
		assert(attribut.verifie);
		//assert(!est_deferred());
		return attribut.getFeature();
	}

	public Table_Symbol table;
	public Classe classe;
	//public Feature feature;
	public NomFeature nom;
	public boolean redefine,undefine;
	public Heritage heritage;
	public Attribut_Complet attribut; 
	public Conversion conversion;
	public Signature signature;

}
