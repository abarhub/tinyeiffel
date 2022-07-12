/*
 * Created on 20 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Donnee_Int extends Donnee {

	/**
	 * @param nom_classe
	 */
	public Donnee_Int() {
		super("INTEGER",true);
		valeur=0;
		type_special=true;
	}

	public void set(Donnee_Int d)
	{
		assert(d!=null);
		valeur=d.valeur;
	}

	public void set(int i)
	{
		valeur=i;
	}

	public int get()
	{
		return valeur;
	}

	public Donnee_Int ajoute(Donnee_Int v)
	{
		assert(v!=null);
		Donnee_Int v2=new Donnee_Int();
		v2.valeur=v.valeur+this.valeur;
		return v2;
	}

	public String toString()
	{
		return ""+valeur;
	}

	public Donnee fun_is_equal(Donnee d)
	{
		if(d instanceof Donnee_Int)
		{
			Donnee_Boolean res;
			res=new Donnee_Boolean();
			if(((Donnee_Int)d).valeur==valeur)
				res.valeur=true;
			return res;
		}
		else
		{
			return super.fun_is_equal(d);
		}
	}
	
	public Donnee fonction_externe(NomFeature nf,Donnee param[])
	{
		assert(nf!=null);
		//assert(nf.nom2!=null):"nom="+nf.nom;
		//assert(nf.nom2.chaine_unique()!=null);
		//if(nf.infix&&nf.nom2.chaine_unique().equalsIgnoreCase("\"+\""))
		if(nf.nom.equalsIgnoreCase("\"+\""))
		{
			assert(param!=null);
			assert(param.length==1);
			assert(param[0]!=null);
			assert(param[0]instanceof Donnee_Int);
			return ajoute((Donnee_Int)param[0]);
		}
		else if(nf.nom.equalsIgnoreCase("is_equal"))
		{
			assert(param!=null);
			assert(param.length==1);
			assert(param[0]!=null);
			//assert(param[0]instanceof Donnee_Int);
			return fun_is_equal((Donnee_Int)param[0]);
		}
		else
		{
			super.fonction_externe(nf,param);
		}
		return null;
	}
	
	protected int valeur;
}
