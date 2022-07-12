/*
 * Created on 15 janv. 2004
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
public class Environnement {

	/**
	 * 
	 */
	public Environnement(Context c) {
		assert(c!=null);
		context=c;
	}

	/* classe */

	public void entre_classe(Classe c,Table_Symbol t)
	{
		assert(!d_classe);
		assert(c!=null);
		d_classe=true;
		classe=c;
		table=t;//context.donne_table_symbol(c);
	}

	public void sort_classe()
	{
		assert(d_classe);
		d_classe=false;
	}

	public boolean dans_classe()
	{
		return d_classe;
	}

	protected boolean d_classe;
	
	/* feature */
	
	public void entre_feature(Feature f)
	{
		assert(!d_feature);
		assert(f!=null);
		assert(d_classe);
		d_feature=true;
		feature=f;
	}

	public void sort_feature()
	{
		assert(d_feature);
		assert(d_classe);
		d_feature=false;
	}

	public boolean dans_feature()
	{
		return d_feature;
	}

	protected boolean d_feature;

	/* require */
	
	public void entre_require()
	{
		assert(!d_require);
		assert(d_classe);
		assert(d_feature);
		d_require=true;
	}

	public void sort_require()
	{
		assert(d_require);
		assert(d_feature);
		assert(d_classe);
		d_require=false;
	}

	public boolean dans_require()
	{
		return d_require;
	}

	protected boolean d_require;

	/* do */
	
	public void entre_do()
	{
		assert(!d_do);
		assert(d_feature);
		assert(d_classe);
		d_do=true;
	}

	public void sort_do()
	{
		assert(d_do);
		assert(d_feature);
		assert(d_classe);
		d_do=false;
	}

	public boolean dans_do()
	{
		return d_do;
	}

	protected boolean d_do;

	/* rescue */
	
	public void entre_rescue()
	{
		assert(!d_rescue);
		assert(d_feature);
		assert(d_classe);
		d_rescue=true;
	}

	public void sort_rescue()
	{
		assert(d_rescue);
		assert(d_feature);
		assert(d_classe);
		d_rescue=false;
	}

	public boolean dans_rescue()
	{
		return d_rescue;
	}

	protected boolean d_rescue;

	/* ensure */
	
	public void entre_ensure()
	{
		assert(!d_ensure);
		assert(d_feature);
		assert(d_classe);
		d_ensure=true;
	}

	public void sort_ensure()
	{
		assert(d_ensure);
		assert(d_feature);
		assert(d_classe);
		d_ensure=false;
	}

	public boolean dans_ensure()
	{
		return d_ensure;
	}

	protected boolean d_ensure;

	/* invariant */
	
	public void entre_invariant()
	{
		assert(!d_invariant);
		assert(!d_feature);
		assert(d_classe);
		d_invariant=true;
	}

	public void sort_invariant()
	{
		assert(d_invariant);
		assert(!d_feature);
		assert(d_classe);
		d_invariant=false;
	}

	public boolean dans_invariant()
	{
		return d_invariant;
	}

	protected boolean d_invariant;

	public Declare_entite donne_entite_locale(String nom)
	{
		DeclareVar liste[],var;
		assert(dans_feature());
		liste=feature.param;
		var=context.cherche_var(nom,liste);
		if(var!=null)
		{
			return var;
		}
		if(feature instanceof FeatureRoutine)
		{
			liste=((FeatureRoutine)feature).local;
			var=context.cherche_var(nom,liste);
			if(var!=null)
			{
				return var;
			}
			if(nom.compareToIgnoreCase("current")==0)
			{
				var=new DeclareVar(nom,classe.type);
				return var;
			}
			if(((FeatureRoutine)feature).type_retour!=null)
			{
				if(nom.compareToIgnoreCase("result")==0)
				{
					var=new DeclareVar(nom,feature.type_retour);
					return var;
				}
			
			}
		}
		return null;
	}

	public Declare_entite donne_entite(String nom)
	{
		assert(nom!=null);
		assert(dans_classe());
		Attribut_Complet ac;
		ac=table.donne_attribut(new NomFeature(nom));
		if(ac!=null)
		{
			return ac;
		}
		if(dans_feature())
		{
			return donne_entite_locale(nom);
			/*DeclareVar liste[],var;
			liste=feature.param;
			var=context.cherche_var(nom,liste);
			if(var!=null)
			{
				return var;
			}
			if(feature instanceof FeatureRoutine)
			{
				liste=((FeatureRoutine)feature).local;
				var=context.cherche_var(nom,liste);
				if(var!=null)
				{
					return var;
				}
				if(nom.compareToIgnoreCase("current")==0)
				{
					var=new DeclareVar(nom,classe.type);
					return var;
				}
				if(((FeatureRoutine)feature).type_retour!=null)
				{
					if(nom.compareToIgnoreCase("result")==0)
					{
						var=new DeclareVar(nom,feature.type_retour);
						return var;
					}
						
				}
			}*/
		}
		return null;
	}

	Classe classe;
	Feature feature;
	Context context;
	Table_Symbol table;

}
