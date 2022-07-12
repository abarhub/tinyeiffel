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
public abstract class Donnee {

	/**
	 * 
	 */
	public Donnee(String nom_classe,boolean expanded) {
		assert(nom_classe!=null);
		this.nom_classe=nom_classe;
		valeurs=new HashMap();
		type_special=false;
		est_expanded=expanded;
	}

	public Donnee get(String nom_attr)
	{
		assert(nom_attr!=null);
		Donnee data;
		data=(Donnee)valeurs.get(nom_attr);
		return data;
	}

	public void set(String nom,Donnee data)
	{
		assert(nom!=null);
		assert(valeurs.containsKey(nom));
		valeurs.put(nom,data);
	}

	public void add(String nom_attr,Donnee data)
	{
		assert(nom_attr!=null);
		assert(!valeurs.containsKey(nom_attr));
		valeurs.put(nom_attr,data);
	}

	public Donnee fun_is_equal(Donnee d)
	{
		String nom;
		Set set;
		Iterator iter;
		assert(d!=null);
		Donnee res=new Donnee_Boolean(),d1,d2;
		((Donnee_Boolean)res).valeur=false;
		set=valeurs.keySet();
		iter=set.iterator();
		((Donnee_Boolean)res).valeur=true;
		while(iter.hasNext())
		{
			nom=(String)iter.next();
			d1=(Donnee)valeurs.get(nom);
			if(d.valeurs.containsKey(nom))
			{
				d2=(Donnee)d.valeurs.get(nom);
				if(d2!=d1)
					return new Donnee_Boolean();
			}
		}
		return res;
	}

	public Donnee fonction_externe(NomFeature nf,Donnee param[])
	{
		if(nf.nom.equalsIgnoreCase("is_equal")||
			nf.nom.equalsIgnoreCase("standard_is_equal"))
		{
			assert(param!=null);
			assert(param.length==1);
			return fun_is_equal(param[1]);
		}
		else if(nf.nom.equalsIgnoreCase("copy")||
			nf.nom.equalsIgnoreCase("standard_copy"))
		{
			assert(false);
		}
		else if(nf.nom.equalsIgnoreCase("twin")||
			nf.nom.equalsIgnoreCase("standard_twin"))
		{
			assert(false);
		}
		else if(nf.nom.equalsIgnoreCase("clone")||
			nf.nom.equalsIgnoreCase("standard_clone"))
		{
			assert(false);
		}
		assert(false);
		return null;
	}

	public String nom_classe;
	public Map valeurs;
	public boolean type_special;
	public boolean est_expanded;

}
