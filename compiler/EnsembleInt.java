/*
 * Created on 12 déc. 2003
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
public class EnsembleInt implements Ensemble {

	
	/* (non-Javadoc)
	 * @see compiler.Ensemble#contient(compiler.Elt)
	 */
	/*public int contient(Elt e) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see compiler.Ensemble#contient(compiler.Elt, compiler.Elt)
	 */
	/*public int contient(Elt e1, Elt e2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see compiler.Ensemble#ajoute(compiler.Elt)
	 */
	/*public void ajoute(Elt e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see compiler.Ensemble#ajoute(compiler.Elt, compiler.Elt)
	 */
	/*public void ajoute(Elt e1, Elt e2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public EnsembleInt(Context context,Classe classe,
						Instr_Inspect instr) {
		super();
		assert(context!=null);
		assert(classe!=null);
		assert(instr!=null);
		this.context=context;
		this.classe=classe;
		this.instr=instr;
	}
	
	protected int dans_tab(ElementInt tab[],ElementInt e)
	{
		assert(e!=null);
		if(tab!=null)
		{
			for(int i=0;i<liste_elt.length;i++)
			{
				if(e.egal(liste_elt[i]))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	protected int dans_tab(ElementInt tab[][],ElementInt e)
	{
		assert(e!=null);
		if(tab!=null)
		{
			for(int i=0;i<tab.length;i++)
			{
				assert(tab[i].length==2);
				if(e.entre(tab[i][0],tab[i][1]))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	ElementInt elt,inter[];
	
	public int contient(Elt e)
	{
		assert(e!=null);
		assert(e instanceof ElementInt);
		ElementInt e2=(ElementInt)e,e3;
		//if(e2.est_unique)
		{
			
		}
		//else
		{
			int c,c1,c2,res;
			//c=e2.valeur;
			/*if(liste_elt!=null)
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
			}*/
			res=dans_tab(liste_elt,e2);
			if(res!=-1)
			{
				elt=liste_elt[res];
				inter=null;
				return dans_elt;
			}
			/*if(liste_couple!=null)
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
			}*/
			res=dans_tab(liste_couple,e2);
			System.out.println(((liste_couple==null)?"null":liste_couple[0][0].nom.toString()));
			System.out.println("coucou:"+res+":"+e2.nom+";"+
								e2.nom.debut()+"*******************");
			if(res!=-1)
			{
				elt=null;
				inter=liste_couple[res];
				return dans_inter;
			}
		}
		return dans_rien;
	}

	protected boolean intersection(ElementInt a1,ElementInt a2,
									ElementInt b1, ElementInt b2)
	{
		assert(a1!=null);
		assert(a2!=null);
		assert(b1!=null);
		assert(b2!=null);
		if(a1.supStrict(a2)||b1.supStrict(b2))
			return false;
		if(a1.entre(b1,b2)||a2.entre(b1,b2)||
			b1.entre(a1,a2)||b2.entre(a1,a2))
			return true;
		return false;
	}

	protected int dans_tab(ElementInt tab[],ElementInt e1,ElementInt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		if(e1.supStrict(e2))
			return -1;
		if(tab!=null)
		{
			for(int i=0;i<tab.length;i++)
			{
				if(tab[i].entre(e1,e2))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	protected int dans_tab(ElementInt tab[][],
							ElementInt e1,ElementInt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		if(e1.supStrict(e2))
			return -1;
		if(tab!=null)
		{
			for(int i=0;i<tab.length;i++)
			{
				assert(tab[i].length==2);
				//c2=tab[i][0].valeur;
				//c3=tab[i][1].valeur;
				//if(e2.infStrict(tab[i][0])||e1.supStrict(tab[i][1]))//(c1<c2||c0>c3)
				{
					
				}
				//else
				if(intersection(e1,e2,tab[i][0],tab[i][1]))
				{
					return i;
				}
			}
		}
		return -1;
	}

	public int contient(Elt e1,Elt e2)
	{
		assert(e1!=null);
		assert(e1 instanceof ElementInt);
		assert(e2!=null);
		assert(e2 instanceof ElementInt);
		ElementInt e=(ElementInt)e1;
		ElementInt e4=(ElementInt)e2;
		int c0,c1,c2,c3,res;
		//c0=e.valeur;
		//c1=e4.valeur;
		assert(e.infEgal(e4)):">:"+e.supStrict(e4)+":"+e.infEgal(e4);
		//assert(c0<=c1);
		/*if(liste_elt!=null)
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
		}*/
		res=dans_tab(liste_elt,e,e4);
		if(res!=-1)
		{
			elt=liste_elt[res];
			inter=null;
			return dans_elt;
		}
		/*if(liste_couple!=null)
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
		}*/
		res=dans_tab(liste_couple,e,e4);
		if(res!=-1)
		{
			inter=liste_couple[res];
			elt=null;
			return dans_inter;
		}
		return dans_rien;
	}
	
	protected Vector vecteur_inter(ElementInt inter[])
	{
		assert(inter!=null);
		assert(inter.length==2);
		Vector v=new Vector();
		v.addElement(inter[0].nom);
		v.addElement(inter[1].nom);
		return v;
	}
	
	protected void erreur(ElementInt e)
	{
		switch(contient(e))
		{
			case dans_elt:
				if(e.est_unique)
					context.erreur(new ErreurVOMB4(classe,instr,
											e.nom,elt.nom));
				else
					context.erreur(new ErreurVOMB3(classe,instr,
								e.nom,elt.nom));
				break;
			case dans_inter:
				if(e.est_unique)
					context.erreur(new ErreurVOMB4(classe,instr,
									e.nom,vecteur_inter(inter)));
				else
					context.erreur(new ErreurVOMB3(classe,instr,
									e.nom,vecteur_inter(inter)));
				break;
			default:
				assert(false);
		}
	}

	protected void erreur(ElementInt e1,ElementInt e2)
	{
		Vector v=new Vector();
		v.addElement(e1.nom);
		v.addElement(e2.nom);
		switch(contient(e1,e2))
		{
			case dans_elt:
				if(e1.est_unique||e2.est_unique)
					context.erreur(new ErreurVOMB4(classe,
							instr,elt.nom,v));
				else
					context.erreur(new ErreurVOMB3(classe,
							instr,elt.nom,v));
				break;
			case dans_inter:
				if(e1.est_unique||e2.est_unique)
					context.erreur(new ErreurVOMB4(classe,instr,v,
										vecteur_inter(inter)));
				else
					context.erreur(new ErreurVOMB3(classe,instr,v,
								vecteur_inter(inter)));
				break;
			default:
				assert(false);
		}
	}
	
	protected ElementInt[] add(ElementInt tab[],ElementInt e)
	{
		assert(e!=null);
		if(tab==null)
		{
			tab=new ElementInt[1];
			tab[0]=e;
		}
		else
		{
			ElementInt tmp[];
			int i;
			tmp=new ElementInt[tab.length+1];
			for(i=0;i<tab.length;i++)
				tmp[i]=tab[i];
			tmp[i]=e;
			tab=tmp;
		}
		return tab;
	}
	
	public void ajoute(Elt e)
	{
		assert(e!=null);
		assert(e instanceof ElementInt);
		//assert(contient(e)!=dans_rien);
		if(contient(e)!=dans_rien)
		{
			erreur((ElementInt)e);
			return;
		}
		ElementInt e1=(ElementInt)e;
		verifie_unique(e1);
		//if(e1.est_unique)
		{
			
		}
		//else
		{
			liste_elt=add(liste_elt,e1);
		}
		/*if(liste_elt==null)
		{
			liste_elt=new ElementInt[1];
			liste_elt[0]=e1;
		}
		else
		{
			ElementInt tmp[];
			int i;
			tmp=new ElementInt[liste_elt.length+1];
			for(i=0;i<liste_elt.length;i++)
				tmp[i]=liste_elt[i];
			tmp[i]=e1;
			liste_elt=tmp;
		}*/
	}

	protected ElementInt[][] add(ElementInt tab[][],
							ElementInt e1,ElementInt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		if(tab==null)
		{
			tab=new ElementInt[1][];
			tab[0]=new ElementInt[2];
			tab[0][0]=e1;
			tab[0][1]=e2;
		}
		else
		{
			ElementInt tmp[][];
			int i;
			tmp=new ElementInt[tab.length+1][];
			for(i=0;i<tab.length;i++)
				tmp[i]=tab[i];
			tmp[i]=new ElementInt[2];
			tmp[i][0]=e1;
			tmp[i][1]=e2;
			tab=tmp;
		}
		return tab;
	}

	public void ajoute(Elt e1,Elt e2)
	{
		assert(e1!=null);
		assert(e2!=null);
		assert(e1 instanceof ElementInt);
		assert(e2 instanceof ElementInt);
		//assert(contient(e1,e2)!=dans_rien);
		ElementInt e3=(ElementInt)e1;
		ElementInt e4=(ElementInt)e2;
		verifie_unique(e3);
		verifie_unique(e4);
		//if(e3.est_unique)
		{
			
		}
		//else
		{
			if(e3.supStrict(e4))//(e3.valeur>e4.valeur)
				return;
			if(contient(e1,e2)!=dans_rien)
			{
				erreur(e3,e4);
			}
			assert(e3.infEgal(e4));//(e3.valeur<=e4.valeur);
			liste_couple=add(liste_couple,e3,e4);
		}
		
		/*if(liste_couple==null)
		{
			liste_couple=new ElementInt[1][];
			liste_couple[0]=new ElementInt[2];
			liste_couple[0][0]=e3;
			liste_couple[0][1]=e4;
		}
		else
		{
			ElementInt tmp[][];
			int i;
			tmp=new ElementInt[liste_couple.length+1][];
			for(i=0;i<liste_couple.length;i++)
				tmp[i]=liste_couple[i];
			tmp[i]=new ElementInt[2];
			tmp[i][0]=e3;
			tmp[i][1]=e4;
			liste_couple=tmp;
		}*/
	}
	
	protected void verifie_unique(ElementInt e)
	{
		assert(e!=null);
		if(e.est_unique)
		{
			if(this.entier_positif)
			{
				context.erreur(new ErreurVOMB5(classe,instr,
												entier.nom));
			}
			else
			{
				entier_unique=true;
			}
		}
		else
		{
			if(e.valeur>0)
			{
				if(this.entier_unique)
				{
					context.erreur(new ErreurVOMB5(classe,instr,
													e.nom));
				}
				else
				{
					entier_positif=true;
					entier=e;
				}
			}
		}
	}
	
	protected ElementInt liste_elt[],liste_couple[][];
	//protected ElementInt liste_elt_unique[],liste_couple_unique[][];
	protected Context context;
	protected Classe classe;
	protected Instr_Inspect instr;
	protected boolean entier_positif,entier_unique;
	protected ElementInt entier;
	

}
