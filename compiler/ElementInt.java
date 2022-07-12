/*
 * Created on 12 déc. 2003
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
public class ElementInt extends Elt implements Compare {

	/**
		 * 
		 */
		public ElementInt(Expr nom,int v) {
			super();
			this.nom=nom;
			this.valeur=v;
			est_unique=false;
		}
		
	public ElementInt(Expr nom,Classe classe,FeatureUnique feature) {
				super();
				assert(nom instanceof Expr_Var);
				this.nom=nom;
				this.valeur=0;
				est_unique=true;
				this.classe=classe;
				this.feature=feature;
			}
	
	public boolean egal(Compare ec)
	{
		assert(ec!=null);
		ElementInt e=(ElementInt)ec;
		if(est_unique)
		{
			if(!e.est_unique)
				return false;
			assert(e.nom instanceof Expr_Var);
			String s1,s2;
			s1=((Expr_Var)nom).nom;
			s2=((Expr_Var)e.nom).nom;
			return s1.compareToIgnoreCase(s2)==0;
		}
		else
		{
			if(e.est_unique)
				return false;
			return valeur==e.valeur;
		}
	}
	
	protected int position()
	{
		assert(est_unique);
		int i;
		assert(feature!=null);
		NomFeature n,liste[];
		String n2;
		liste=feature.liste_nom;
		n2=((Expr_Var)nom).nom;
		//n=new No
		for(i=0;i<liste.length;i++)
		{
			n=liste[i];
			if(!n.infix&&!n.prefix&&n.nom.compareToIgnoreCase(n2)==0)
				return i;
		}
		assert(false);
		return -1;
	}
	
	public boolean entre(ElementInt e1,ElementInt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		if(est_unique)
		{
			if(e1.est_unique&&e2.est_unique)
			{
				if(e1.feature==e2.feature&&feature==e1.feature)
				{// TODO: a terminer
					int pos1,pos2,pos3;
					pos1=position();
					pos2=e1.position();
					pos3=e2.position();
					return pos1>=pos2&&pos1<=pos3;
				//assert(false);
				}
			}
		}
		else
		{
			if(!e1.est_unique&&!e2.est_unique)
			{
				return valeur>=e1.valeur&&valeur<=e2.valeur;
			}
		}
		return false;
	}
	
	public boolean infStrict(Compare ec)
	{
		assert(ec!=null);
		ElementInt e=(ElementInt)ec;
		if(est_unique)
		{
			if(feature==e.feature)
			{
				int pos1,pos2;
				pos1=position();
				pos2=e.position();
				return pos1<pos2;
				//assert(false);
			}
		}
		else
		{
			if(!e.est_unique)
			{
				return valeur<e.valeur;
			}
		}
		return false;
	}

	public boolean supStrict(Compare ec)
	{
		assert(ec!=null);
		ElementInt e=(ElementInt)ec;
		if(est_unique)
		{
			if(feature==e.feature)
			{
				int pos1,pos2;
				pos1=position();
				pos2=e.position();
				return pos1>pos2;
				//assert(false);
			}
		}
		else
		{
			if(!e.est_unique)
			{
				return valeur>e.valeur;
			}
		}
		return false;
	}
	
	public boolean infEgal(Compare elt)
	{
		assert(elt!=null);
		assert(elt instanceof ElementInt);
		ElementInt e=(ElementInt)elt;
		if(est_unique)
		{
			if(feature==e.feature)
			{
				int pos1,pos2;
				pos1=position();
				pos2=e.position();
				return pos1<=pos2;
				//assert(false);
			}
		}
		else
		{
			if(!e.est_unique)
			{
				return valeur<=e.valeur;
			}
		}
		return false;
	}

	public boolean supEgal(Compare elt)
	{
		assert(elt!=null);
		assert(elt instanceof ElementInt);
		ElementInt e=(ElementInt)elt;
		if(est_unique)
		{
			if(feature==e.feature)
			{
				int pos1,pos2;
				pos1=position();
				pos2=e.position();
				return pos1>=pos2;
				//assert(false);
			}
		}
		else
		{
			if(!e.est_unique)
			{
				return valeur>=e.valeur;
			}
		}
		return false;
	}

	public boolean diff(Compare elt)
	{
		assert(elt!=null);
		assert(elt instanceof ElementInt);
		ElementInt e=(ElementInt)elt;
		if(est_unique)
		{
			if(feature==e.feature)
			{
				int pos1,pos2;
				pos1=position();
				pos2=e.position();
				return pos1!=pos2;
				//assert(false);
			}
		}
		else
		{
			if(!e.est_unique)
			{
				return valeur!=e.valeur;
			}
		}
		return false;
	}
	
	int valeur;
	boolean est_unique;
	Classe classe;
	FeatureUnique feature;
}
