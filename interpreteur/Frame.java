/*
 * Created on 20 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

import java.util.*;
import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Frame {

	/**
	 * 
	 */
	public Frame(String nom_classe,NomFeature nom_fonction,
					Interpreteur interp,Frame precedant,
					Map param,Donnee objet_courant) {
		assert(nom_classe!=null);
		assert(nom_fonction!=null);
		assert(interp!=null);
		assert(objet_courant!=null);
		this.nom_classe=nom_classe;
		this.nom_fonction=nom_fonction;
		this.interpreteur=interp;
		this.precedant=precedant;
		this.param=param;
		this.objet_courant=objet_courant;
	}

	public void set_local(FeatureRoutine f)
	{
		assert(f!=null);
		int i;
		DeclareVar dv;
		Donnee d;
		this.f=f;
		local=new HashMap();
		if(f.local!=null)
		{
			for(i=0;i<f.local.length;i++)
			{
				dv=f.local[i];
				d=interpreteur.defaut_var(dv.type);
				local.put(dv.nom,d);
			}
		}
	}

	public void set_var(String nom,Donnee d)
	{
		assert(nom!=null);
		assert(d!=null);
		if(local!=null&&local.containsKey(nom))
			local.put(nom,d);
		else if(param!=null&&param.containsKey(nom))
			param.put(nom,d);
		else
		{
			objet_courant.set(nom,d);
		}
	}

	public Donnee get_var(String nom)
	{
		assert(nom!=null);
		Donnee res;
		if(local!=null&&local.containsKey(nom))
		{
			res=(Donnee)local.get(nom);
		}
		else
		{
			if(param!=null&&param.containsKey(nom))
			{
				res=(Donnee)param.get(nom);
			}
			else
			{
				res=(Donnee)objet_courant.get(nom);
			}
		}
		return res;
	}

	public Classe get_classe()
	{
		Classe cl=interpreteur.cherche_classe(nom_classe);
		assert(cl!=null);
		return cl;
	}

	public String nom_classe;
	public NomFeature nom_fonction;
	public Feature f;
	public Map local;
	public Interpreteur interpreteur;
	public Frame precedant;
	public Map param;
	public Donnee objet_courant;
}
