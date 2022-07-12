/*
 * Created on 27 nov. 2003
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
public class Conversion {

	/**
	 * 
	 */
	public Conversion(Heritage heritage,Type type) {
		assert(heritage!=null);
		assert(type!=null);
		this.heritage=heritage;
		this.type=type;
		construit_table(type,heritage.type);
	}

	public Conversion(Type de,Type vers)
	{
		assert(de!=null);
		assert(vers!=null);
		assert(!de.is_like);
		assert(!vers.is_like);
		assert(de.nom.compareToIgnoreCase(vers.nom)==0);
		construit_table(de,vers);
	}

	public Conversion(Conversion conv)
	{// fait la concatenation des conversions
		//TODO
	}

	protected void construit_table(Type de,Type vers)
	{
		if(de.generique!=null&&de.generique.length>0&&
			vers.generique!=null&&
			vers.generique.length>0&&
			de.generique.length==vers.generique.length)
		{
			int i;
			assert(de.generique.length==vers.generique.length);
			table=new Type[de.generique.length][2];
			for(i=0;i<de.generique.length;i++)
			{
				table[i][0]=de.generique[i];
				table[i][1]=vers.generique[i];
			}
		}
	}

	/**
	 * convertie le type reel t suivant l'heritage, en
	 * convertissant suivant le type formel de t
	 * @param t le type reel de 
	 * @return
	 */
	public Type convertie2(Type t)
	{
		assert(t!=null);
		Type res=null;
		assert(t.nom.compareToIgnoreCase(type.nom)==0):t.nom+"!="+type.nom;
		res=Type.copieType(heritage.type);
		if(t.generique!=null&&t.generique.length>0)
		{
			assert(type.generique!=null);
			assert(type.generique.length==t.generique.length):
					"t="+t.toString2()+";type="+type.toString2();
			int i;
			Type tableau[][],t2;
			tableau=new Type[t.generique.length][2];
			for(i=0;i<t.generique.length;i++)
			{
				tableau[i][0]=type.generique[i];
				tableau[i][1]=t.generique[i];
			}
			System.out.println("res avant conv parametres="+res.toString2());
			for(i=0;i<res.generique.length;i++)
			{
				t2=conv(res.generique[i],tableau);
				System.out.println(i+")"+res.generique[i]+"->"+t2);
				res.generique[i]=t2;
			}
			System.out.println("res apres conv parametres="+res.toString2());
		}
		return res;
	}

	protected Type conv(Type t,Type tab[][])
	{
		assert(t!=null);
		assert(tab!=null);
		Type t1,t2,res=Type.copieType(t);
		int i,j;
		boolean modifie=false;
		if(t.is_like)
			return Type.copieType(t);
		if(tab!=null)
		{
			for(i=0;i<tab.length;i++)
			{
				t1=tab[i][0];
				t2=tab[i][1];
				if(t.nom.compareToIgnoreCase(t1.nom)==0)
				{
					res=Type.copieType(t2);
					assert(!modifie);
					modifie=true;
					assert(t.generique==null||
							t.generique.length==0);
					break;
				}
			}
			if(t.generique!=null&&!modifie)
			{
				//assert(res!=type);
				//assert(!modifie);
				for(i=0;i<t.generique.length;i++)
				{
					res.generique[i]=conv(res.generique[i],tab);
				}
			}
		}
		return res;
	}

	public Type convertie(Type type)
	{
		assert(type!=null);
		Type t1,t2,res=Type.copieType(type);
		int i,j;
		boolean modifie=false;
		if(type.is_like)
			return Type.copieType(type);
		if(table!=null)
		{
			for(i=0;i<table.length;i++)
			{
				t1=table[i][0];
				t2=table[i][1];
				if(type.nom.compareToIgnoreCase(t1.nom)==0)
				{// TODO: prendre en compte les paramètres
					res=Type.copieType(t2);
					assert(!modifie);
					modifie=true;
					//assert(type.generique==null||
					//		type.generique.length==0);
					break;
				}
			}
			if(type.generique!=null)
			{
				//assert(res!=type);
				assert(type.generique.length==0||!modifie);
				for(i=0;i<type.generique.length;i++)
				{
					res.generique[i]=convertie(res.generique[i]);
				}
			}
		}
		return res;
	}

	public Type convertie_inv(Type type)
	{
		assert(type!=null);
		Type t1,t2,res=Type.copieType(type);
		int i,j;
		boolean modifie=false;
		if(type.is_like)
			return Type.copieType(type);
		if(table!=null)
		{
			for(i=0;i<table.length;i++)
			{
				t1=table[i][1];
				t2=table[i][0];
				if(type.nom.compareToIgnoreCase(t1.nom)==0)
				{
					res=Type.copieType(t2);
					assert(!modifie);
					modifie=true;
					//assert(type.generique==null||
					//		type.generique.length==0);
					break;
				}
			}
			if(type.generique!=null&&!modifie)
			{
				//assert(res!=type);
				assert(!modifie);
				for(i=0;i<type.generique.length;i++)
				{
					res.generique[i]=convertie_inv(res.generique[i]);
				}
			}
		}
		return res;
	}

	public String toString()
	{
		String res="{";
		if(table!=null)
		{
			for(int i=0;i<table.length;i++)
			{
				if(i>0)
					res+="+";
				res+=table[i][0]+"->"+table[i][1];
			}
		}
		res+="}";
		return res;
	}

	Heritage heritage;
	Type type,table[][];
}
