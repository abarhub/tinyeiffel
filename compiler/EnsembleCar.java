/*
 * Created on 8 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;
import tinyeiffel.erreur.*;
import java.util.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EnsembleCar implements Ensemble {

	/**
	 * 
	 */
	public EnsembleCar(Context context,Classe classe,
						Instr_Inspect instr) {
		super();
		assert(context!=null);
		assert(classe!=null);
		assert(instr!=null);
		this.context=context;
		this.classe=classe;
		this.instr=instr;
	}
	
	ElementCar elt,inter[];
		
	public int contient(Elt e)
	{
		assert(e!=null);
		assert(e instanceof ElementCar);
		ElementCar e2=(ElementCar)e,e3;
		char c,c1,c2;
		c=e2.valeur;
		if(liste_elt!=null)
		{
			for(int i=0;i<liste_elt.length;i++)
			{
				c1=liste_elt[i].valeur;
				if(c1==c)
				{
					elt=liste_elt[i];
					inter=null;
					return dans_elt;
				}
			}
		}
		if(liste_couple!=null)
		{
			for(int i=0;i<liste_couple.length;i++)
			{
				assert(liste_couple[i].length==2);
				c1=liste_couple[i][0].valeur;
				c2=liste_couple[i][1].valeur;
				if(c>=c1&&c<=c2)
				{
					elt=null;
					inter=liste_couple[i];
					return dans_inter;
				}
			}
		}
		return dans_rien;
	}

	public int contient(Elt e1,Elt e2)
	{
		assert(e1!=null);
		assert(e1 instanceof ElementCar);
		assert(e2!=null);
		assert(e2 instanceof ElementCar);
		ElementCar e=(ElementCar)e1;
		ElementCar e4=(ElementCar)e2;
		char c0,c1,c2,c3;
		c0=e.valeur;
		c1=e4.valeur;
		assert(c0<=c1);
		if(liste_elt!=null)
		{
			for(int i=0;i<liste_elt.length;i++)
			{
				c2=liste_elt[i].valeur;
				if(c2>=c0&&c2<=c1)
				{
					elt=liste_elt[i];
					inter=null;
					return dans_elt;
				}
			}
		}
		if(liste_couple!=null)
		{
			for(int i=0;i<liste_couple.length;i++)
			{
				assert(liste_couple[i].length==2);
				c2=liste_couple[i][0].valeur;
				c3=liste_couple[i][1].valeur;
				if(c1<c2||c0>c3)
				{
					
				}
				else
				{
					inter=liste_couple[i];
					elt=null;
					return dans_inter;
				}
			}
		}
		return dans_rien;
	}
	
	protected Vector vecteur_inter(ElementCar inter[])
	{
		assert(inter!=null);
		assert(inter.length==2);
		Vector v=new Vector();
		v.addElement(inter[0].nom);
		v.addElement(inter[1].nom);
		return v;
	}
	
	protected void erreur(ElementCar e)
	{
		switch(contient(e))
		{
			case dans_elt:
				context.erreur(new ErreurVOMB3(classe,instr,e.nom,
								elt.nom));
				break;
			case dans_inter:
				context.erreur(new ErreurVOMB3(classe,instr,e.nom,
								vecteur_inter(inter)));
				break;
			default:
				assert(false);
		}
	}

	protected void erreur(ElementCar e1,ElementCar e2)
	{
		Vector v=new Vector();
		v.addElement(e1.nom);
		v.addElement(e2.nom);
		switch(contient(e1,e2))
		{
			case dans_elt:
				context.erreur(new ErreurVOMB3(classe,instr,elt.nom,
								v));
				break;
			case dans_inter:
				context.erreur(new ErreurVOMB3(classe,instr,v,
								vecteur_inter(inter)));
				break;
			default:
				assert(false);
		}
	}
	
	public void ajoute(Elt e)
	{
		assert(e!=null);
		assert(e instanceof ElementCar);
		//assert(contient(e)!=dans_rien);
		if(contient(e)!=dans_rien)
		{
			erreur((ElementCar)e);
			return;
		}
		ElementCar e1=(ElementCar)e;
		if(liste_elt==null)
		{
			liste_elt=new ElementCar[1];
			liste_elt[0]=e1;
		}
		else
		{
			ElementCar tmp[];
			int i;
			tmp=new ElementCar[liste_elt.length+1];
			for(i=0;i<liste_elt.length;i++)
				tmp[i]=liste_elt[i];
			tmp[i]=e1;
			liste_elt=tmp;
		}
	}

	public void ajoute(Elt e1,Elt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		assert(e1 instanceof ElementCar);
		assert(e2 instanceof ElementCar);
		//assert(contient(e1,e2)!=dans_rien);
		ElementCar e3=(ElementCar)e1;
		ElementCar e4=(ElementCar)e2;
		if(e3.valeur>e4.valeur)
			return;
		if(contient(e1,e2)!=dans_rien)
		{
			erreur(e3,e4);
		}
		assert(e3.valeur<=e4.valeur);
		if(liste_couple==null)
		{
			liste_couple=new ElementCar[1][];
			liste_couple[0]=new ElementCar[2];
			liste_couple[0][0]=e3;
			liste_couple[0][1]=e4;
		}
		else
		{
			ElementCar tmp[][];
			int i;
			tmp=new ElementCar[liste_couple.length+1][];
			for(i=0;i<liste_couple.length;i++)
				tmp[i]=liste_couple[i];
			tmp[i]=new ElementCar[2];
			tmp[i][0]=e3;
			tmp[i][1]=e4;
			liste_couple=tmp;
		}
	}
	
	protected ElementCar liste_elt[],liste_couple[][];
	protected Context context;
	protected Classe classe;
	protected Instr_Inspect instr; 

}
