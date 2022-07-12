/*
 * Created on 20 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

import tinyeiffel.compiler.*;
import tinyeiffel.ast.*;
import java.util.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Interpreteur {

	/**
	 * 
	 */
	public Interpreteur(Compiler_Eiffel comp) {
		assert(comp!=null);
		//super();
		// TODO Auto-generated constructor stub
		compiler=comp;
		nom_classe_racine=compiler.get_configuration().classe_racine();
		nom_routine_racine=compiler.get_configuration().routine_racine();
		if(nom_routine_racine==null)
			nom_routine_racine="make";
		System.out.println("Interpretation:"+nom_classe_racine+"("+nom_routine_racine+")");
		classe_racine=cherche_classe(nom_classe_racine);
		assert(classe_racine!=null);
		System.out.println("classe="+classe_racine.nom);
		routine_racine=cherche_feature(classe_racine,new NomFeature(nom_routine_racine));
		assert(routine_racine!=null);
		System.out.println("routine="+routine_racine);
		objet_racine=creer_var(classe_racine.type);
		lance_routine(objet_racine,routine_racine,new NomFeature(nom_routine_racine),null);
	}

	public Donnee lance_routine(Donnee objet,Feature f,NomFeature nf,Donnee param[])
	{
		assert(objet!=null);
		assert(f!=null);
		assert(nf!=null);
		Donnee d,result=null;
		Map local,param2;
		int i;
		DeclareVar dv;
		Instr instr,tab_instr[];
		if(param==null||param.length==0)
		{
			param2=null;
			assert(f.param==null||f.param.length==0);
		}
		else
		{
			assert(f.param!=null);
			assert(f.param.length==param.length);
			param2=new HashMap();
			for(i=0;i<param.length;i++)
			{
				dv=f.param[i];
				param2.put(dv.nom,param[i]);
			}
		}
		derniere_frame=new Frame(objet.nom_classe,nf,
			this,derniere_frame,param2,objet);
		if(f instanceof FeatureRoutine)
		{
			FeatureRoutine f2=(FeatureRoutine)f;
			Couple c;
			derniere_frame.set_local(f2);
			tab_instr=f2.liste_instr;
			if(tab_instr!=null)
			{
				for(i=0;i<tab_instr.length;i++)
				{
					instr=tab_instr[i];
					if(instr instanceof Instr_Affect)
					{
						Instr_Affect instr2=(Instr_Affect)instr;
			
						c=evalue(instr2.expr);
						assert(c!=null);
						d=c.donnee;
						//if(d!=null)
						derniere_frame.set_var(instr2.nom,d);
						System.out.println("affect:"+instr2.nom+":="+d);
					}
					else if(instr instanceof Instr_Appel)
					{
						Instr_Appel instr2=(Instr_Appel)instr;
						Donnee resultat=derniere_frame.objet_courant;
						while(instr2!=null)
						{
							if(instr2.nom!=null)
							{
								NomFeature nf2;
								Feature f3;
								nf2=new NomFeature(instr2.nom);
								Classe cl=cherche_classe(resultat.nom_classe);
								f3=cherche_feature(cl,nf2);
								if(f3==null||(f3 instanceof FeatureAttr||
									f3 instanceof FeatureExpr||
									f3 instanceof FeatureUnique))
								{
									resultat=derniere_frame.get_var(instr2.nom);
								}
								else if(f3 instanceof FeatureRoutine||
										f3 instanceof FeatureExternal)
								{
									resultat=appel_routine(resultat,f3,nf2,instr2.parametre);
								}
								else
								{
									assert(false);
								}
							}
							else
							{
								assert(instr2.expr!=null);
								c=evalue(instr2.expr);
								assert(c!=null);
								resultat=c.donnee;
							}
							instr2=(Instr_Appel)instr2.getSuivant();
						}
					}
					else if(instr instanceof Instr_Creation)
					{
						Instr_Creation instr2=(Instr_Creation)instr;
						Type t;
						
						t=instr2.type;
						assert(t!=null);
						d=creer_var(t);
						//if(d!=null)
						derniere_frame.set_var(instr2.nom,d);
						System.out.println("creation:!"+t+"!"+instr2.nom);
						if(instr2.nom2!=null)
						{
							NomFeature nf2;
							Feature f3;
							System.out.println("nom2="+instr2.nom2);
							nf2=new NomFeature(instr2.nom2);
							Classe cl=cherche_classe(d.nom_classe);
							f3=cherche_feature(cl,nf2);
							appel_routine(d,f3,nf2,instr2.parametre);
							System.out.println("Appel "+instr2.nom2);
						}
					}
					else
					{
			
					}
				}
			}
		}
		else if(f instanceof FeatureExternal)
		{
			FeatureExternal f2=(FeatureExternal)f;
			if(f2.str.chaine_unique().equalsIgnoreCase("\"tinyeiffel\""))
			{
				//if(objet.nom_classe.equalsIgnoreCase("integer"))
				{
					//if(nf.infix&&nf.nom2.chaine_unique().equalsIgnoreCase("\"+\""))
					//d2=evalue(expr2.expr2);
					//res=((Donnee_Int)d1).ajoute((Donnee_Int)d2);
					result=objet.fonction_externe(nf,param);
				}
			}
			else
			{
				assert(false):"extr:"+f2.str.chaine_unique();
			}
		}
		else
		{
			assert(false);
		}
		derniere_frame=derniere_frame.precedant;
		return result;
	}

	public Donnee appel_routine(Donnee objet,Feature f,NomFeature nf,Expr param[])
	{
		assert(f!=null);
		assert(f instanceof FeatureRoutine||
				f instanceof FeatureExternal);
		Donnee param3[],resultat;
		Couple c;
		if(param==null||param.length==0)
		{
			param3=null;
		}
		else
		{
			param3=new Donnee[param.length];
			for(int j=0;j<param.length;j++)
			{
				c=evalue(param[j]);
				assert(c!=null);
				param3[j]=c.donnee;
			}
		}
		resultat=lance_routine(objet,f,nf,param3);
		return resultat;
	}

	public Couple evalue(Expr expr)
	{
		assert(expr!=null);
		Donnee d;
		Couple res=null;
		if(expr instanceof Expr_Entier)
		{
			Expr_Entier expr2=(Expr_Entier)expr;
			int n=0;
			try{
				n=Integer.parseInt(expr2.str);
			}
			catch(NumberFormatException e)
			{
				System.out.println("Erreur de format entier:"+e);
				//e.printStackTrace();
				assert(false);
			}
			d=new Donnee_Int();
			((Donnee_Int)d).valeur=n;
			res=new Couple(d,new TypeSimple(false,"integer",null));
		}
		else if(expr instanceof Expr_Var)
		{
			Expr_Var expr2=(Expr_Var)expr;
			Donnee d2=derniere_frame.get_var(expr2.nom);
			assert(d2!=null):"var="+expr2.nom+"("+expr2.debut()+")";
			res=new Couple(d2,new TypeSimple(false,"Any",null));
			assert(res!=null);
		}
		else if(expr instanceof Expr_Vrai)
		{
			Donnee_Boolean e=new Donnee_Boolean();
			e.valeur=true;
			res=new Couple(e,new TypeSimple(false,"BOOLEAN",null));
			assert(res!=null);
		}
		else if(expr instanceof Expr_Faux)
		{
			Donnee_Boolean e=new Donnee_Boolean();
			res=new Couple(e,new TypeSimple(false,"BOOLEAN",null));
			assert(res!=null);
		}
		else if(expr instanceof Expr_Binaire)
		{
			Expr_Binaire expr2=(Expr_Binaire)expr;
			Donnee d1,d2;
			Couple c1,c2;
			switch(expr2.op)
			{
				case Expr.Plus:
					c1=evalue(expr2.expr1);
					d1=c1.donnee;
					c2=evalue(expr2.expr2);
					d2=c2.donnee;
					{
						Feature f;
						Donnee p[]={d2};
						NomFeature nf;
						Classe cl;
						nf=expr2.donne_nom_feature();
						nf=trouve_nom_feature(c1.type,c2.type,nf);
						cl=cherche_classe(d1.nom_classe);
						f=cherche_feature(cl,nf);
						//res=((Donnee_Int)d1).ajoute((Donnee_Int)d2);
						d1=this.lance_routine(d1,f,nf,p);
						res=new Couple(d1,f.type_retour);
					}
					break;
				default:
					assert(false);
			}
			assert(res!=null);
		}
		else
		{
			
		}
		return res;
	}

	public NomFeature trouve_nom_feature(Type t1,Type t2,NomFeature nf)
	{
		assert(t1!=null);
		assert(t2!=null);
		assert(nf!=null);
		Table_Symbol table,table2;
		//Attribut_Complet ac;
		NomFeature res=null,n2;
		boolean h1[],h2[];
		int i,j,no1,no2,n;
		Classe cl,cl2;
		Heritage h;
		
		table=compiler.context.donne_table_symbol(t1);
		assert(table!=null);
		//ac=table.donne_attribut(nf);
		//assert(ac!=null):"nf="+nf;
		h1=new boolean[compiler.liste_classe.size()];
		h2=new boolean[compiler.liste_classe.size()];
		no1=compiler.context.no_classe(compiler.context.cherche_classe(t1));
		no2=compiler.context.no_classe(compiler.context.cherche_classe(t2));
		for(i=0;i<compiler.liste_classe.size();i++)
		{
			if(compiler.context.table_heritage[i][no1]&&compiler.context.table_heritage[no2][i])
				h1[i]=true;
			else
				h1[i]=false;
		}
		n=no1;
		res=nf;
		for(i=0;i<compiler.liste_classe.size();i++)
		{
			cl2=(Classe)compiler.liste_classe.elementAt(i);
			for(j=0;j<compiler.liste_classe.size();j++)
			{
				if(compiler.context.table_heritage[n][j])
					break;
			}
			if(j>=compiler.liste_classe.size())
			// trouve
				break;
			cl=(Classe)compiler.liste_classe.elementAt(j);
			// la classe d'indice j est descendante directe de n
			table2=compiler.context.donne_table_symbol(cl);
			assert(table2!=null);
			// TODO: faire un renomage suivant l'heritage
			if(cl.heritage!=null&&cl.heritage.length>0)
			{
				h=null;
				for(int k=0;k<cl.heritage.length;k++)
				{
					h=cl.heritage[k];
					if(h.type.nom!=null&&
						h.type.nom.equalsIgnoreCase(cl.nom))
					{
						break;
					}
					h=null;
				}
				if(h!=null)
				{
					if(h.rename!=null&&h.rename.length>0)
					{
						for(int k=0;k<h.rename.length;k++)
						{
							if(h.rename[k].nom_src.meme_nom(res))
							{
								res=h.rename[k].nom_dest;
								break;
							}
						}
					}
				}
			}
			//ac=table.donne_attribut(res);
			//res=ac.
			n=j;
		}
		return res;
	}

	public Donnee defaut_var(Type t)
	{
		assert(t!=null);
		if(t.nom.equalsIgnoreCase("integer"))
		{
			Donnee_Int d=new Donnee_Int();
			d.set(0);
			return d;
		}
		else if(t.nom.equalsIgnoreCase("boolean"))
		{
			Donnee_Boolean d=new Donnee_Boolean();
			d.valeur=false;
			return d;
		}
		else
		{
			return null;
		}
		
	}

	public Donnee creer_var(Type t)
	{
		assert(t!=null);
		Donnee d;
		if(t.nom.equalsIgnoreCase("integer"))
			d=new Donnee_Int();
		else if(t.nom.equalsIgnoreCase("boolean"))
			d=new Donnee_Boolean();
		else
		{
			Table_Symbol table;
			Attribut_Complet ac;
			table=compiler.context.donne_table_symbol(t);
			assert(table!=null);
			d=new Donnee_Std(t.nom,est_expanded(t));
			for(int i=0;i<table.liste_attribut.length;i++)
			{
				ac=table.liste_attribut[i];
				if(!ac.est_deferred())
				{
					Feature f=ac.getFeature();
					if(f!=null&&(f instanceof FeatureAttr))
					{
						String nom=ac.nom.nom;
						Donnee d3=defaut_var(f.type_retour);
						d.add(nom,d3);
					}
				}
			}
		}
		return d;
	}

	public Classe cherche_classe(String nom)
	{
		assert(nom!=null);
		String nom_classe,tmp;
		Vector v;
		Classe cl;
		int i;
		v=compiler.liste_classe;
		if(v!=null)
		{
			for(i=0;i<v.size();i++)
			{
				cl=((Classe)v.elementAt(i));
				if(nom.equalsIgnoreCase(cl.nom))
				{
					return cl;
				}
			}
		}
		return null;
	}

	public Feature cherche_feature(Classe cl,NomFeature nom)
	{
		assert(cl!=null);
		assert(nom!=null);
		int i;
		Table_Symbol table;
		Attribut_Complet ac;
		Feature f;
		table=compiler.context.donne_table_symbol(cl);
		assert(table!=null);
		ac=table.donne_attribut(nom);
		if(ac==null)
			return null;
		assert(ac!=null):"nom="+nom+";classe="+cl.nom;
		f=ac.getFeature();
		assert(f!=null);
		return f;
	}

	public boolean est_expanded(Type t)
	{
		assert(t!=null);
		Classe cl;
		
		if(t.expanded)
			return true;
		cl=cherche_classe(t.nom);
		if(cl!=null)
		{
			if(cl.type.expanded)
				return true;
		}
		return false;
	}

	public String nom_classe_racine,nom_routine_racine;
	public Compiler_Eiffel compiler;
	public Classe classe_racine;
	public Feature routine_racine;
	public Frame derniere_frame;
	public Donnee objet_racine;
}
