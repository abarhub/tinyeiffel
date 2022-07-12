package tinyeiffel.compiler;

import tinyeiffel.ast.*;
import java.util.*;
import tinyeiffel.erreur.*;

public class Context
{
	public Context(Logging log,Compiler_Eiffel comp)
	{
		assert(log!=null);
		assert(comp!=null);
		logging=log;
		compiler=comp;
	}

	public void construit_classes(Vector liste_classe,Vector liste_type)
	{
		int i,classes_traite;
		Type t;
		Classe c;

		this.liste_type=liste_type;
		this.liste_classe=liste_classe;
		/*for(i=0;i<liste_type.size();i++)
		{
			t=(Type)liste_type.elementAt(i);
			if(t.nom.compareToIgnoreCase("any")!=0&&
				t.nom.compareToIgnoreCase("none")!=0)
			{
				c=parse_file(t.nom+".e");
				if(c!=null)
				{
					liste_classe.addElement(c);
					ajoute_type(c.liste_classe);
				}
			}
		}
		System.out.println("Classes chargees:");
		affiche_classe();*/
		table_heritage_directe=new boolean[liste_classe.size()][liste_classe.size()];

		for(i=0;i<liste_classe.size();i++)
		{
			HashSet set=new HashSet();
			Classe cl=((Classe)liste_classe.elementAt(i));
			if(cl.nom.compareToIgnoreCase("none")==0)
			{
				int j;
				Table_Symbol table;
				for(j=0;j<table_heritage_directe.length;j++)
				{
					if(j!=i)
						table_heritage_directe[i][j]=true;
				}
				table=new Table_Symbol(cl,this);
				ajoute_table(table);
				//cl.attributs=new Vector();
				//cl.attributs_ancetre=new Vector();
				cl.attributs_traite=true;
			}
			else
			{
				System.out.println("calcul de :"+
						((Classe)liste_classe.elementAt(i)).nom+"("+i+")"+
						((Classe)liste_classe.elementAt(0)).nom);
				calcul_attributs((Classe)liste_classe.elementAt(i),set);
			}
			classes_traite=i;
		}
		table_heritage=calcul_heritage(table_heritage_directe);
		for(i=0;i<table_symbol.length;i++)
		{// verifier suivant l'ordre hierarchique
			//&é"'(
			compiler.env.entre_classe(table_symbol[i].classe,table_symbol[i]);
			table_symbol[i].termine_verification();
			compiler.env.sort_classe();
		}
	}

	public void affiche_classe()
	{
		int i;
		Classe cl;
		for(i=0;i<liste_classe.size();i++)
		{
			cl=(Classe)liste_classe.elementAt(i);
			System.out.println(i+":"+cl.nom);
		}
	}

	public void ajoute_table(Table_Symbol table)
	{
		assert(table!=null);
		assert(no_table_symbol(table.classe)==-1):
				"ajout *2 de "+table.classe.nom;
		if(table_symbol==null)
		{
			table_symbol=new Table_Symbol[1];
			table_symbol[0]=table;
		}
		else
		{
			int i,len;
			Table_Symbol t[];
			len=table_symbol.length;
			t=new Table_Symbol[len+1];
			for(i=0;i<table_symbol.length;i++)
				t[i]=table_symbol[i];
			t[i]=table;
			table_symbol=t;
		}
	}

	public int no_table_symbol(Classe c)
	{
		assert(c!=null);
		if(table_symbol==null)
			return -1;
		for(int i=0;i<table_symbol.length;i++)
		{
			Classe c2=table_symbol[i].classe;
			assert(c2!=null);
			if(c2.nom.compareToIgnoreCase(c.nom)==0)
				return i;
		}
		return -1;
	}

	public Table_Symbol donne_table_symbol(Classe c)
	{
		assert(c!=null);
		int no=no_table_symbol(c);
		assert(no!=-1);
		return table_symbol[no];
	}

	public Table_Symbol donne_table_symbol(Type t)
	{
		assert(t!=null);
		Classe c=this.cherche_classe(t);
		assert(c!=null):"t="+t.toString2();
		int no=no_table_symbol(c);
		assert(no!=-1):"type="+t;
		Table_Symbol table=table_symbol[no];
		assert(table!=null);
		assert(table.classe.nom.compareToIgnoreCase(t.nom)==0);
		return table;
	}

	public Table_Symbol table_symbol[];

	public void ajoute_heritage(Classe classe,Heritage herit,
			HashSet classe_descendante,Table_Symbol table)
	{
		//int k,j;
		Type t=herit.type;
		Classe cl=cherche_classe(t);
		assert(cl!=null);
		System.out.println("herite de "+cl.nom);
		assert(!classe_descendante.contains(cl));
		HashSet set=new HashSet();
		set.addAll(classe_descendante);
		set.add(classe);
		calcul_attributs(cl,set);
		//Rename rename[]=herit.rename;
		int no_table,i,j;
		no_table=no_table_symbol(cl);
		assert(no_table!=-1);
		Table_Symbol tbl=table_symbol[no_table];
		if(herit.redefine!=null)
		{
			NomFeature nom1,nom2;
			for(i=0;i<herit.redefine.length;i++)
			{
				nom1=herit.redefine[i];
				if(tbl.donne_attribut(nom1)==null)
				{// attribut non present dans l'ancetre
					erreur(new ErreurVDRS1(classe,herit,nom1));
				}
				for(j=0;j<i;j++)
				{
					nom2=herit.redefine[j];
					if(nom1.equals(nom2))
					{// Erreur VDRS3 : plusieurs fois l'attr 
						// dans une liste de redefine
						erreur(new ErreurVDRS3(classe,herit,
										nom1,nom2));
					}
				}
			}
		}
		if(herit.undefine!=null)
		{
			NomFeature nom1,nom2;
			for(i=0;i<herit.undefine.length;i++)
			{
				nom1=herit.undefine[i];
				if(tbl.donne_attribut(nom1)==null)
				{// attribut non present dans l'ancetre
					erreur(new ErreurVDUS1(classe,herit,nom1));
				}
				for(j=0;j<i;j++)
				{
					nom2=herit.undefine[j];
					if(nom1.equals(nom2))
					{// Erreur VDUS4 : plusieurs fois l'attr 
						// dans une liste de undefine
						erreur(new ErreurVDUS4(classe,herit,
										nom1,nom2));
					}
				}
			}
		}
		
		table.ajoute_heritage(herit,tbl);
	}

	/**
	 * retourne true ssi le type utilisé dans la classe
	 * table est correcte par rapport a son type de base
	 * @param table
	 * @param type
	 * @return
	 */
	public boolean type_valide(Table_Symbol table,Type type)
	{
		assert(table!=null);
		assert(type!=null);
		Classe classe_courante,classe_base;
		classe_courante=table.classe;
		if(type.is_like)
		{// type ancré
			//System.out.println("like--"+type.debut());
			//type.classe_env=classe_courante;
			if(type.like==null||!(type.like instanceof Expr_Var))
			{// Erreur VTAT1
				//assert(false);
				//System.out.println("VTAT1-4");
				Erreur e;
				if(compiler.env.dans_feature()/*feature_courante!=null*/)
					e=new ErreurVTAT2(table.classe,type);
				else
					e=new ErreurVTAT1(table.classe,type);
				erreur(e);
				return false;
			}
			else
			{
				Expr_Var var=(Expr_Var)type.like;
				Type t1=null;
				boolean regle2=false,regle3=false;
				if(var.nom.compareToIgnoreCase("current")==0)
				{
					return true;
				}
				else if(compiler.env.dans_feature())
				{
					/*DeclareVar d=cherche_var(var.nom,
									feature_courante.param);*/
					Declare_entite d;
					d=compiler.env.donne_entite_locale(var.nom);
					if(d==null)
					{// //Erreur VTAT2 : attribut non present
						//erreur(new ErreurVTAT2(table.classe,type));
						//return false;
					}
					else
					{// la variable est trouve
						regle2=true;
						if(d.getType()==null)
						{// Erreur VTAT2
							erreur(new ErreurVTAT2(table.classe,type));
							return false;
						}
						else
						{
							t1=d.getType();
						}
					}
				}
				if(t1==null)
				{// pas dans les attributs local
					Attribut_Complet ac=table.trouve_attribut(var.nom);
					if(ac!=null)
					{
//						System.out.println("VTAT1-2");
						t1=ac.donne_type_retour(this);
					}
					else
					{// Erreur VTAT1 : attribut non present
						//System.out.println("VTAT1-1");
						erreur(new ErreurVTAT1(table.classe,type));
						return false;
					}
				}
				if(t1==null)
				{
					Erreur e;
					//System.out.println("VTAT1-3");
					if(regle2)
						e=new ErreurVTAT2(table.classe,type);
					else
						e=new ErreurVTAT1(table.classe,type);
					erreur(e);
					return false;
				}
				else
				{
					if(t1.expanded)
					{// Erreur VTAT
						Erreur e;
						if(regle2)
							e=new ErreurVTAT2(table.classe,type,
										t1,ErreurVTAT1.Type_expanse);
						else
							e=new ErreurVTAT1(table.classe,type,
									t1,ErreurVTAT1.Type_expanse);
						erreur(e);
						return false;
					}
					else if(table.type_generique(t1))
					{// Erreur VTAT
						Erreur e;
						if(regle2)
							e=new ErreurVTAT2(table.classe,type,
									t1,ErreurVTAT1.Type_generique);
						else
							e=new ErreurVTAT1(table.classe,type,
									t1,ErreurVTAT1.Type_generique);
						erreur(e);
						return false;
					}
					else if(t1.is_like)
					{// Erreur VTAT
						Erreur e;
						if(regle2)
							e=new ErreurVTAT2(table.classe,type,
									t1,ErreurVTAT1.Type_ancre);
						else
							e=new ErreurVTAT1(table.classe,type,
									t1,ErreurVTAT1.Type_ancre);
						erreur(e);
						return false;
					}
					else
					{// Ok
						return true;
					}
				}
			}
		}
		else
		{
			Type type_reel=table.trouve_vrai_type(type);
			if(type_reel==null)
			{
				return false;
			}
			if(table.type_generique(type_reel))
			{
				Type contrainte=table.contrainte(type_reel);
				if(contrainte!=null)
				{
					classe_base=cherche_classe(contrainte);
					if(classe_base==null)
					{//	la classe n'existe pas
						System.out.println("Salut-----"+type+"="+
											type_reel+"->"+contrainte
											+":"+classe_courante.nom);
						erreur(new ErreurVTCT(type_reel,classe_courante));
						return false;
					}
				}
				else
				{
					classe_base=null;
				}
			}
			else
			{
				classe_base=cherche_classe(type_reel);
			}
			assert(classe_courante!=null);
			if(classe_base==null)
			{// la classe n'existe pas
				if(!table.type_generique(type_reel))
				{
					System.out.println("Coucou-----");
					erreur(new ErreurVTCT(type_reel,classe_courante));
					return false;
				}
				else
				{// c'est un type generique sans pb
					return true;
				}
			}
			Type t=classe_base.type;
			Table_Symbol table2=donne_table_symbol(t);
			assert(table2!=null);
			if(type_reel.expanded)
			{
				if(classe_base.deferred)
				{// Erreur VTEC1
					erreur(new ErreurVTEC1(classe_courante,
								type_reel,classe_base));
				}
				if(classe_base.creation!=null)
				{
					Creation c;
					int i;
					if(classe_base.creation.length>1||
						(classe_base.creation.length==1&&
						classe_base.creation[0].nom_fonction.length>1))
					{// Erreur VTEC2 : plusieurs procedures de creation
						erreur(new ErreurVTEC2(classe_courante,type_reel,
												classe_base));
					}
					else if(classe_base.creation.length==1)
					{
						c=classe_base.creation[0];
						Attribut_Complet ac;
						ac=table2.donne_attribut(c.nom_fonction[0]);
						if(ac.signature.parametre!=null&&
							ac.signature.parametre.length>0)
						{// Erreur VTEC2 : plusieurs parametres
							erreur(new ErreurVTEC2(classe_courante,
													type_reel,classe_base,c));
						}
					}
				}
			}
			if(t.generique==null||t.generique.length==0)
			{
				if(type_reel.generique!=null&&
					type_reel.generique.length>0)
				{// Erreur: pas le meme nombre de parametre
					erreur(new ErreurVTUG1(type_reel,t));
					return false;
				}
			}
			else
			{
				Type t1,t2;
				if(type_reel.generique==null||
					type_reel.generique.length!=t.generique.length)
				{// Erreur: pas le meme nombre de parametre
					erreur(new ErreurVTUG2(type_reel,t));
					return false;
				}
				for(int i=0;i<t.generique.length;i++)
				{
					Type contrainte=null,t3,t4;
					t3=t.generique[i];
					t4=type_reel.generique[i];
					assert(table2.type_generique(t3)):"classe:"+t+
							";param="+t3;
					if(table2.type_generique(t3))
						contrainte=table2.contrainte(t3);
					//System.out.println("coucou:");
					if(contrainte!=null&&!type_compatible(t4,table,
							contrainte,table2))
					{// Erreur pas compatible
						//System.out.println("Erreur,assert");
						//assert(false):"diff:"+t4+"!="+contrainte;
						erreur(new ErreurVTCG3(t4,contrainte));
						return false;
					}
					if(!type_valide(table,t4))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	public void affiche_table_symbol()
	{
		int i;
		System.out.println("Les tables des symboles");
		for(i=0;i<table_symbol.length;i++)
		{
			System.out.println(table_symbol[i]);
		}
		System.out.println("Fin des tables des symboles");
	}

	public boolean type_existe(Type type)
	{
		assert(type!=null);
		assert(!type.is_like);
		return compiler.classe_existe(type.nom);
	}

	public void calcul_attributs(Classe classe,HashSet classe_descendante)
	{// TODO : Terminer d'utiliser les Table_Symbol
		Heritage herit;
		Type t;
		int i,j,k;
		Classe cl;
		Vector attr_ancetre=new Vector();
		Table_Symbol table;

		System.out.println("attributs0 de la classe "+classe.nom);
		j=no_classe(classe);
		if(!classe.attributs_traite)
		{// classe non encore traite
			// TODO : eviter la recursivite pour TEST8
			assert(!classe_descendante.contains(classe));
			assert(no_table_symbol(classe)==-1):"Erreur:"+classe.nom;
			table=new Table_Symbol(classe,this);
			ajoute_table(table);
			//compiler.env.entre_classe(classe,table);
			if(classe.type.generique!=null&&
				classe.type.generique.length>0)
			{
				Type t1,t2;
				Vector liste=new Vector();
				for(i=0;i<classe.type.generique.length;i++)
				{
					t1=classe.type.generique[i];
					assert(t1!=null);
					if(type_existe(t1))
					{// Le type existe dans l'univers
						//assert()
						erreur(new ErreurVCFG1(classe,t1));
					}
					else
					{
						for(int k1=0;k1<liste.size();k1++)
						{
							t2=(Type)liste.elementAt(k1);
							assert(t2!=null);
							if(t1.egaux(t2))
							{// le type est deja declare dans la liste de
								// genericite
								//assert(false):"j="+j+";"+t1+"="+t2;
								erreur(new ErreurVCFG2(classe,t1,t2));
							}
						}
					}
					liste.addElement(t1);
				}
			}
			if(classe.heritage!=null)
			{// plusieurs heritage
				for(i=0;i<classe.heritage.length;i++)
				{
					herit=classe.heritage[i];
					t=herit.type;
					cl=cherche_classe(t);
					assert(cl!=null);
					assert(j==no_classe(classe));
					k=no_classe(cl);
					table_heritage_directe[j][k]=true;
					System.out.println("herit("+j+","+k+")");
					if(classe_descendante.contains(cl))
					{
						//erreur("Erreur: il y a un cycle d'heritage ("+
						//	cl.nom+"->...->"+classe.nom+"->"+cl.nom+")");
						erreur(new ErreurVHPR(cl,classe));
					}
					else
					{
						ajoute_heritage(classe,herit,classe_descendante,table);
					}
				}
			}
			else if((classe.heritage==null||classe.heritage.length==0)&&
					(classe.nom.compareToIgnoreCase("general")==0||
					classe.nom.compareToIgnoreCase("any")==0||
					classe.nom.compareToIgnoreCase("plateform")==0))
			{// classe general ou any ou plateform sans ancetre

			}
			else
			{// pas d'heritage => herite de ANY
				k=1;
				cl=(Classe)liste_classe.elementAt(k);
				assert(cl.nom.compareToIgnoreCase("any")==0);
				//System.out.println("("+j+","+k+")");
				assert(j==no_classe(classe));
				table_heritage_directe[j][k]=true;
				if(classe_descendante.contains(cl))
				{
					//erreur("Il y a un cycle d'heritage ("+
					//	cl.nom+"->...->"+classe.nom+"->"+cl.nom+")");
					erreur(new ErreurVHPR(cl,classe));
				}
				else
				{
					herit=new Heritage(new TypeSimple(false,"any",null),
							new Vector(),new Vector(),new Vector(),
							new Vector(),new Vector());
					ajoute_heritage(classe,herit,
									classe_descendante,table);
				}
			}
			// ajout dans la table de symbol de tous les attributs
			// directes de la classe
			if(classe.feature!=null&&classe.feature.length>0)
			{
				for(i=0;i<classe.feature.length;i++)
				{
					System.out.println("i="+i);
					//compiler.env.entre_feature(classe.feature[i]);
					table.ajoute_feature(classe.feature[i]);
					//compiler.env.sort_feature();
				}
			}
			table.verifie_valide();
			classe.attributs_traite=true;
			//compiler.env.sort_classe();
		}
		System.out.println("Fin des attributs de la classe "+classe.nom);
	}

	public Vector conflit_nom(Vector liste1,Vector liste2)
	{
		assert(liste1!=null);
		assert(liste2!=null);
		Vector tmp=intersection(liste1,liste2);
		
		Vector tmp2;
		for(int i=0;i<tmp.size();i++)
		{
			Attribut a0=(Attribut)tmp.elementAt(i);
			int j=liste2.indexOf(a0);
			assert(j!=-1);
			Attribut a2=(Attribut)liste2.elementAt(i);
			if(dans_meme_classe(a0.feature,a2.feature))
			{// c'est la meme feature 
				tmp.remove(i);
				i--;
			}
			if(a0.feature.is_deferred()||a2.feature.is_deferred())
			{
				tmp.remove(i);
				i--;
			}
		}
		return tmp;
	}

	public boolean dans_meme_classe(Feature f1,Feature f2)
	{
		assert(f1!=null);
		assert(f2!=null);
		return f1.classe.nom.equals(f2.classe.nom);
	}

	public boolean[][] calcul_heritage(boolean heritage_directe[][])
	{
		boolean heritage[][],modifier=true;
		int taille,i,j,no,k;

		taille=heritage_directe.length;
		heritage=new boolean[taille][taille];

		for(i=0;i<taille;i++)
			for(j=0;j<taille;j++)
				heritage[i][j]=heritage_directe[i][j];

		for(k=0;k<taille*taille&&modifier;k++)
		{
			modifier=false;
			for(i=0;i<taille;i++)
			{
				for(j=0;j<taille;j++)
				{
					if(heritage[j][i]==true)
					{
						for(k=0;k<taille;k++)
						{
							if(heritage[k][j]&&!heritage[k][i])
							{
								modifier=true;
								heritage[k][i]=true;
							}
						}
					}
				}
			}
		}

		return heritage;
	}

	public Vector intersection(Vector v1,Vector v2)
	{
		int i,j;
		Attribut n1,n2;
		Vector res=new Vector();

		if(v1==null||v2==null)
			return res;
		for(i=0;i<v1.size();i++)
		{
			n1=(Attribut)v1.elementAt(i);
			for(j=0;j<v2.size();j++)
			{
				n2=(Attribut)v2.elementAt(j);
				if(n1.equals(n2))
				{
					res.addElement(n1);
				}
			}
		}
		return res;
	}

	// retourne le plus petit type qui est conforme a t1 et t2 
	public Vector super_type(Type t1,Type t2)
	{
		assert(t1!=null);
		assert(t2!=null);
		if(t1==null)
			return null;
		if(t2==null)
			return null;
		Classe c1,c2;
		int no1,no2,i,len,res=-1,j;
		boolean table[];
		Type t;
		Vector liste;
		c1=cherche_classe(t1);
		no1=no_classe(c1);
		c2=cherche_classe(t2);
		no2=no_classe(c2);
		len=table_heritage.length;
		table=new boolean[len];
		System.out.println("super1");
		for(i=0;i<len;i++)
		{
			table[i]=table_heritage[no1][i] 
						&& table_heritage[no2][i];
		}
		table[no1]=true;
		table[no2]=true;
		System.out.println("super2");
		for(i=0;i<len;i++)
		{
			int nb_descendant=0,nb_total=0;
			if(table[i])
			{// un des ancetres
				System.out.println("super5:"+i);
				for(j=0;j<len;j++)
				{
					if(j!=i&&table[j])
					{//un autre des ancetres
						nb_total++;
						if(table_heritage[j][i])
						{
							nb_descendant++;
						}
					}
				}
				System.out.println("super6:"+i);
				if(nb_total>0&&nb_total==nb_descendant)
				{// la classe i est l'ancetre de toutes les classes
					table[i]=false;
					i=0;
				}
				System.out.println("super7:"+i);
			}
		}
		System.out.println("super3");
		liste=new Vector();
		for(i=0;i<len;i++)
		{
			if(table[i])
			{
				Classe c=(Classe)liste_classe.elementAt(i);
				liste.addElement(c.type);
				//assert(res==-1):"t="+res+"+"+i;// plusieurs type de retour non compris
				res=i;
			}
		}
		assert(res!=-1);
		System.out.println("super4");
		t=((Classe)liste_classe.elementAt(res)).type;
		return liste;
	}

	// retourne le plus petit type qui est conforme a liste et t2
	// TODO: a refaire 
	public Vector super_type(Vector liste,Type t2)
	{// TODO: a optimise (desfois plusieurs fois le meme type)
		// ex: <<1,2,3>> et ARRAY[INTEGER]
		Type t1;
		Vector res,l;
		assert(liste!=null);
		assert(t2!=null);
		res=new Vector();
		for(int i=0;i<liste.size();i++)
		{
			t1=(Type)liste.elementAt(i);
			l=super_type(t1,t2);
			for(int j=0;j<l.size();j++)
			{
				Type t3,t4;
				boolean deja_dedans=false;
				t3=(Type)l.elementAt(j);
				for(int k=0;!deja_dedans&&k<liste.size();k++)
				{// recherche si deja present
					t4=(Type)liste.elementAt(k);
					if(t3.egaux(t4))
					{
						deja_dedans=true;
					}
				}
				if(!deja_dedans)
				{
					res.addElement(t3);
				}
			}
			//res.addAll(l);
		}
		return res;
	}

	public boolean type_compatible(Expr de,Table_Symbol t1,
									Declare_entite ancetre,Table_Symbol t2)
	{
		assert(ancetre!=null);
		assert(de!=null);
		Type type1,type2;
		type1=de.type;
		type2=ancetre.getType();
		if(type1==null||type2==null)
			return false;
		if(de.op==Expr.Tableau)
		{
			Expr_Tableau expr_tab=(Expr_Tableau)de;
			Vector v=expr_tab.tableau,v2,liste;
			Type t3,t4=null;
			System.out.println("debut:");
			liste=new Vector();
			for(int i=0;i<v.size();i++)
			{
				t3=((Expr)v.elementAt(i)).type;
				System.out.println("type("+i+")="+t3+":"+t4);
				// TODO: a calculer le type
				//t4=super_type(t3,t4);
				//liste.addElement(t3);
				if(i>0)
					liste=super_type(liste,t3);
				else
					liste.addElement(t3);
			}
			System.out.println("fin type="+liste);
			for(int i=0;i<liste.size();i++)
			{// TODO: a optimiser le nombre d'objet creer
				t3=(Type)liste.elementAt(i);
				v=new Vector();
				v.addElement(t3);
				t3=new TypeSimple(false,"ARRAY",v);
				if(type_compatible(t3,t1,type2,t2))
					return true;
			}
			return false;
		}
		else if(!de.type.is_like&&ancetre.getType().is_like)
		{
			if(de instanceof Expr_Var&&
				!((Expr_Var)de).nom.equalsIgnoreCase("current"))
			{
				Expr_Var e1,e2;
				e1=(Expr_Var)de;
				e2=(Expr_Var)ancetre.getType().like;
				assert(e1!=null);
				assert(e2!=null);
				return meme_entite(e1.nom,t1,e2.nom,t2);
			}
		}
		return type_compatible(type1,t1,type2,t2);
	}

	public boolean type_compatible(Vector de,Table_Symbol t1,
								Signature ancetre,Table_Symbol t2)
	{// "de" dans la classe t1 est un sous type de 
		// "ancetre" de la classe t2
		// donc "de" est conforme a "ancetre"
		assert(de!=null);
		assert(ancetre!=null);
		if(ancetre.nb_parametre()!=de.size())
		{
			return false;
		}
		for(int i=0;i<ancetre.nb_parametre();i++)
		{
			Type type1,type2;
			type1=((Expr)de.elementAt(i)).type;
			type2=ancetre.getParametre(i);
			if(!type_compatible(type1,t1,type2,t2))
			{
				return false;
			}
		}
		return true;
	}

	/*public boolean type_compatible(Type de,Type a)
	{// TODO : a effacer
		if(de==null||a==null)
			return false;
		if(de==a)
			return true;
		if(de.equals(a))
			return true;
		int i,j;
		i=no_classe(cherche_classe(de));
		j=no_classe(cherche_classe(a));
		if(i==-1||j==-1)
			return false;
		return table_heritage[i][j];
	}*/

	/*
	 * determine si les types t1 et t2 venant des classes
	 * table1 et table2 sont egaux, sans prendre en compte
	 * s'ils sont expanded.
	 * seul le nom et les parametres generiques sont pris 
	 * en consideration
	 */
	public boolean type_egaux(Type t1,Table_Symbol table1,
								Type t2,Table_Symbol table2)
	{
		assert(t1!=null);
		assert(t2!=null);
		assert(table1!=null);
		assert(table2!=null);
		if(t1==t2)
			return true;
		//if(t1.is_like)
		if(t1.nom.compareToIgnoreCase(t2.nom)!=0)
			return false;
		/*if((t1.generique==null||t1.generique.length==0)&&
				(t2.generique!=null&&t2.generique.length>0))
			return false;
		if((t1.generique!=null&&t1.generique.length>0)&&
				(t2.generique==null||t2.generique.length>0))
			return false;*/
		if(!parametre_compatible(t1,table1,t2,table2))
			return false;
		if(table1.type_generique(t1)&&table2.type_generique(t2)&&
				table1!=table2)
		{
			t1=table1.contrainte(t1);
			if(t1==null)
				t1=new TypeSimple(false,"ANY",new Vector());
			t2=table1.contrainte(t2);
			if(t2==null)
				t2=new TypeSimple(false,"ANY",new Vector());
				
			return type_egaux(t1,table1,t2,table2);
		}
		return true;
	}

	// retourne true ssi de est un descendandt de ancetre,
	// sans prendre en compte la genericite, expanded et like
	public boolean type_ancetre(Type de,Table_Symbol t1,
								Type ancetre,Table_Symbol t2)
	{
		assert(de!=null);
		assert(t1!=null);
		assert(ancetre!=null);
		assert(t2!=null);
		int i,j;
		i=no_classe(cherche_classe(de));
		j=no_classe(cherche_classe(ancetre));
		if(i==-1||j==-1)
			return false;
		return table_heritage[i][j];
	}

	// retourne true ssi nom1 et nom2 sont la meme entite
	public boolean meme_entite(String nom1,Table_Symbol t1,
								String nom2,Table_Symbol t2)
	{// TODO: prendre en compte le renomage d'entite
		assert(nom1!=null);
		assert(t1!=null);
		assert(nom2!=null);
		assert(t2!=null);
		return t1==t2&&nom1.equalsIgnoreCase(nom2);
	}
	
	// retourne true ssi les parametres de de sont compatible 
	// avec les parametres de ancetre
	public boolean parametre_compatible(Type de,Table_Symbol t1,
										Type ancetre,Table_Symbol t2)
	{
		assert(de!=null);
		assert(t1!=null);
		assert(ancetre!=null);
		assert(t2!=null);
		//assert(type_ancetre(de,t1,ancetre,t2)||
		//		type_egaux(de,t1,ancetre,t2));
		boolean res=false;
		Type de_param[],ancetre_param[];
		System.out.println("Parametre type:"+
							donne_type(de)+"->"+
							donne_type(ancetre));
		de_param=de.generique;
		ancetre_param=ancetre.generique;
		if((de_param==null||de_param.length==0)
			&&(ancetre_param==null||ancetre_param.length==0))
		{// aucun n'a de parametre
			res=true;
		}
		else if((de_param==null||de_param.length==0)||
				(ancetre_param==null||ancetre_param.length==0))
		{
			res=false;
		}
		else
		{
			boolean tab[];
			int i,j,len,no1,no2;
			Vector liste,liste_tete,liste2,v,v2;
			Classe classe_de,classe_ancetre;
			Heritage liste_heritage[],heritage;
			// determination des classes pour le chemin
			// entre de et ancetre
			len=table_heritage.length;
			classe_de=cherche_classe(de);
			classe_ancetre=cherche_classe(ancetre);
			assert(classe_de!=null);
			assert(classe_ancetre!=null);
			tab=new boolean[len];
			no1=no_classe(classe_de);
			no2=no_classe(classe_ancetre);
			assert(no1!=-1);
			assert(no2!=-1);
			for(i=0;i<tab.length;i++)
			{
				tab[i]=table_heritage[no1][i]&&
						table_heritage[i][no2];
			}
			tab[no2]=true;
			liste=chemins_ancetre(no2,no1,tab);
			System.out.println("de="+donne_type(de)+";ancetre="+donne_type(ancetre));
			assert(liste.size()>0):"no1="+no1+";no2="+no2+";"+tab[5];
			for(i=0;i<liste.size();i++)
			{
				Couple c;
				v=(Vector)liste.elementAt(i);
				System.out.print(i+"={");
				for(j=0;j<v.size();j++)
				{
					c=(Couple)v.elementAt(j);
					if(j>0)
						System.out.print(",");
					System.out.print(c.classe.type+"(");
					System.out.print(((c.conversion!=null)?(c.conversion.toString()+"-"+c.classe.type):"null")+",");
					assert(c.classe!=null);
					//assert(c.classe.heritage!=null);
					//assert(c.classe.heritage[c.no].type!=null);
					System.out.print(((c.no!=-1&&c.classe.heritage!=null)?c.classe.heritage[c.no].type.toString():"null")+")");
				}
				System.out.println("}");
			}
			// construction des conversions en fonction des chemins
			liste2=new Vector();
			for(i=0;i<liste.size();i++)
			{
				Type t=null;
				Couple c;
				Conversion conv;
				v=(Vector)liste.elementAt(i);
				assert(v.size()>=1):"taille="+v.size()+";"+i;
				c=(Couple)v.elementAt(0);
				t=c.classe.type;
				t=de;
				//conv=new Conversion(de,t);//&é"'(-è
				System.out.println("conversion de "+donne_type(t));
				for(j=1;j<v.size();j++)
				{// TODO: utiliser la concatenation de conversion
					c=(Couple)v.elementAt(j);
					System.out.println("j="+j);
					//if(t==null)
					//	t=c.classe.type;
					//else
						//t=c.conversion.convertie_inv(t);
					t=c.conversion.convertie2(t);
					System.out.println("->"+donne_type(t)+
										"("+c.conversion+")");
				}
				assert(t!=null);
				liste2.addElement(t);
			}
			System.out.println("resultat=");
			for(i=0;i<liste2.size();i++)
			{
				Type t;
				t=(Type)liste2.elementAt(i);
				System.out.println(i+"={"+donne_type(t)+"}");
			}
			// verification si une des conversion donne ancetre
			if(liste2.size()==0)
				res=false;
			else
			{
				Type type1,type2,type3;
				boolean type_compatible=false;
				for(i=0;!type_compatible&&i<liste2.size();i++)
				{
					type1=(Type)liste2.elementAt(i);
					if(type1.generique!=null&&
							type1.generique.length==ancetre.generique.length)
					{
						boolean pb=false;
						for(j=0;!pb&&j<type1.generique.length;j++)
						{
							type2=type1.generique[j];
							type3=ancetre.generique[j];
							if(!type_compatible(type2,t2,type3,t2))
							{
								pb=true;
							}
						}
						if(!pb)
							type_compatible=true;
					}
				}
				System.out.println("Type parametre:"+res+":"+type_compatible);
				res=type_compatible;
			}
		}
		System.out.println("Fin parametre type:"+
							donne_type(de)+"->"+
							donne_type(ancetre)+":"+res);
		return res;
	}
	
	public String donne_type(Type t)
	{
		assert(t!=null);
		String res;
		if(t.is_like)
		{
			res=t.toString();
		}
		else
		{
			res=t.nom;
			if(t.generique!=null&&t.generique.length>0)
			{
				res+="[";
				for(int i=0;i<t.generique.length;i++)
				{
					if(i>0)
						res+=",";
					res+=donne_type(t.generique[i]);
				}
				res+="]";
			}
		}
		return res;
	}
	
	//	construction du chemin de de vers ancetre
	public Vector chemins_ancetre(int no2,int no1,
									boolean tab[])
	{
		Vector v,liste,liste_tete,liste2;
		int i,j,m=0;
		Classe classe_de;
		assert(no2>=0);
		assert(no1>=0);
		assert(tab!=null);
		v=new Vector();
		classe_de=(Classe)liste_classe.elementAt(no1);
		// table_symbol[no1].table_conversion[0]
		v.addElement(new Couple(classe_de,0,null));
		liste=new Vector();
		liste.addElement(v);
		liste_tete=liste_ancetre(classe_de,tab);
		while(true)
		{// tant que pas fini
			Classe cl;
			Vector l;
			int k;
			Couple couple;
			System.out.println("iteration "+m);
			for(int m0=0;m0<liste.size();m0++)
			{
				v=(Vector)liste.elementAt(m0);
				assert(v.size()>0);
				for(int m1=0;m1<v.size();m1++)
				{
					if(m1>0)
						System.out.println("->");
					couple=(Couple)v.elementAt(m1);
					System.out.print(couple.classe.type);
				}
				System.out.println("");
			}
			System.out.println("--------------------");
			liste2=new Vector();
			// construction de la portion suivante
			// des chemins
			for(i=0;i<liste.size();i++)
			{
				l=((Vector)liste.elementAt(i));
				cl=((Couple)l.lastElement()).classe;
				if(no_classe(cl)!=no2)
				{// le chemin n'est pas fini
					liste_tete=liste_ancetre(cl,tab);
					System.out.println(":::::"+liste_tete.size());
					// ajout dans liste2 des nouveaux chemins
					for(j=0;j<liste_tete.size();j++)
					{
						// copie du vecteur l
						v=new Vector();
						for(k=0;k<l.size();k++)
						{
							v.addElement(l.elementAt(k));
						}
						v.addElement(liste_tete.elementAt(j));
						liste2.addElement(v);
					}
				}
				else
				{// le chemin n'est pas fini
					liste2.addElement(l);
				}
			}
			liste=liste2;
			System.out.println("##################");
			if(liste.size()==0)
			{// plus de chemin
				break;
			}
			System.out.println("!!!!!!!!!!!!!!!!");
			// recherche si tous les chemins sont terminé 
			for(i=0;i<liste.size();i++)
			{
				v=(Vector)liste.elementAt(i);
				couple=(Couple)v.lastElement();
				if(no_classe(couple.classe)!=no2)
					break;
			}
			System.out.println("**************");
			if(i>=liste.size())
				break;
			System.out.println("++++++++++++");
		}
		return liste;
	}
	
	public Vector liste_ancetre(Classe classe,boolean tab[])
	{
		assert(classe!=null);
		Vector liste_tete;
		Heritage liste_heritage[],heritage;
		int i,j;
		Classe cl;
		Table_Symbol table;
		Conversion conv;
		assert(classe!=null);
		assert(tab!=null);
		liste_tete=new Vector();
		liste_heritage=classe.heritage;
		if(liste_heritage!=null)
		{
			for(i=0;i<liste_heritage.length;i++)
			{
				heritage=liste_heritage[i];
				assert(heritage!=null);
				cl=cherche_classe(heritage.type);
				assert(cl!=null);
				j=no_classe(cl);
				if(tab[j])
				{
					table=this.donne_table_symbol(classe);
					//conv=table.table_conversion[i];
					conv=new Conversion(heritage,classe.type);
					liste_tete.addElement(new Couple(cl,i,conv));
				}
			}
		}
		return liste_tete;
	}
	
	public boolean type_compatible(Type de,Table_Symbol t1,
									Type ancetre,Table_Symbol t2)
	{// "de" dans la classe t1 est un sous type de 
		// "ancetre" de la classe t2
		// donc "de" est conforme a "ancetre"
		assert(t1!=null);
		assert(t2!=null);
		assert(table_heritage!=null);
		System.out.println("conformite de:"+de.toString2()+
							" , ancetre:"+ancetre.toString2());
		if(de==null||ancetre==null)
			return false;
		if(de==ancetre)
			return true;
		/*if(de.equals(a))
			return true;*/
		int i,j;
		boolean res;
		/*de=t1.trouve_vrai_type(de);
		ancetre=t2.trouve_vrai_type(ancetre);
		assert(!de.is_like);
		assert(!ancetre.is_like);*/
		if(!type_expanded(ancetre,t2)&&!de.is_like&&de.nom.equalsIgnoreCase("none"))
		{// NONE est conforme a un type reference
			return true;
		}
		else if(ancetre.is_like)
		{
			System.out.println("like");
			if(!de.is_like)
			{
				if(((Expr_Var)ancetre.like).nom.compareToIgnoreCase("current")==0)
				{
					return type_compatible(de,t1,t2.classe.type,t2);
				}
				else
				{// TODO : verifier que c'est correcte dans tous les cas
					Type t;
					t=t2.trouve_vrai_type(ancetre);
					if(t.is_like)
					{// impossible de trouver le type reel
						return false;
					}
					else
					{
						return type_compatible(de,t1,t,t2);
					}
					//return false;
				}
			}
			else
			{
				if(ancetre.like ==null||
					!(ancetre.like instanceof Expr_Var)||
					de.like==null||
					!(de.like instanceof Expr_Var))
				{
					return false;
				}
				else
				{
					Expr_Var v1,v2;
					v1=(Expr_Var)ancetre.like;
					v2=(Expr_Var)de.like;
					return meme_entite(v1.nom,t1,v2.nom,t2);
				}
			}
		}
		else if(type_expanded(ancetre,t2))//||type_expanded(de,t1)
		{// la cible est expanded
			System.out.println("expanded");
			if(de.is_like)
			{
				de=t1.trouve_vrai_type(de);
				System.out.println("vrai type="+de+"("+t1.classe.nom+")");
			}
			if(t1.type_generique(de))
			{
				de=t1.contrainte(de);
				if(de==null)
					de=new TypeSimple(false,"ANY",new Vector());
			}
			if(type_expanded(de,t1))
			{
				if(ancetre.nom.compareToIgnoreCase("real")==0&&
					de.nom.compareToIgnoreCase("integer")==0)
					return true;
				if(ancetre.nom.compareToIgnoreCase("double")==0&&
					(de.nom.compareToIgnoreCase("real")==0||
					de.nom.compareToIgnoreCase("integer")==0))
					return true;
				res=type_egaux(de,t1,ancetre,t2);
				System.out.println("Exp:=Exp : "+res);
				return res;
			}
			else
			{
				res=type_egaux(de,t1,ancetre,t2);
				System.out.println("Exp:=Ref : "+res);
				return res;
			}
		}
		else if(!t2.type_generique(ancetre))
		{// la cible est une reference
			System.out.println("ref");
			if(de.is_like)
			{
				de=t1.trouve_vrai_type(de);
				assert(de!=null):"nom="+t1.classe.nom;
			}
			res=type_egaux(de,t1,ancetre,t2);
			if(!res)
			{
				System.out.println("type de="+de.toString2());
				if(t1.type_generique(de))
				{// de est un generique
					Type contrainte_de;
					de=t1.contrainte(de);
					if(de==null)
						de=new TypeSimple(false,"any",new Vector());
					res=type_egaux(de,t1,ancetre,t2);
				}
				if(!res)
				{
					res=type_ancetre(de,t1,ancetre,t2);//no_classe(cherche_classe(de))
					System.out.println("coucou2="+de.toString2()+"?="+ancetre.toString2()+"!" +
							no_classe(cherche_classe(de))+":"+
							no_classe(cherche_classe(ancetre)));
					if(res)
					{
						System.out.println("coucou");
						res=parametre_compatible(de,t1,ancetre,t2);
						System.out.println("res="+res);
					}
				}
			}
			System.out.println("Ref:=* : "+res);
			return res;
		}
		else if(t2.type_generique(ancetre))
		{// cible generique 
			// TODO: prendre en compte la contrainte ? 
			System.out.println("generique");
			assert(de.nom!=null):"pos="+de.debut();
			if(de.nom.compareToIgnoreCase(ancetre.nom)==0&&
				t1==t2)
			{// c'est le meme generique
				return true;
			}
			else
			{
				return false;
			}
		}
		assert(false);
		return false;
	}

	public Classe cherche_classe(Type t)
	{
		int i;
		Classe c;

		for(i=0;i<liste_classe.size();i++)
		{
			c=(Classe)liste_classe.elementAt(i);
			if(c.deType(t))
				return c;
		}
		return null;
	}

	public Type cherche_type(Classe c)
	{
	  int i;
	  Type t;

	  for(i=0;i<liste_type.size();i++)
	  {
	    t=(Type)liste_type.elementAt(i);
	    if(c.deType(t))
	      return t;
	  }
	  return null;
	}

  	public int no_classe(Classe c)
	{
		int i;

		for(i=0;i<liste_classe.size();i++)
		{
			if(liste_classe.elementAt(i).equals(c))
				return i;
		}

		return -1;
	}

	public Attribut donne_attribut(String nom)
	{
		int i,j;
		Attribut a1,a2;
		//Classe c=classe_courante;

		assert(false);

		return null;
	}

	/*public Attribut_Complet donne_attribut(String nom,ast.Classe c)
	{
		assert(nom!=null);
		assert(c!=null);
		return donne_attribut(new NomFeature(nom),c);
	}*/
	
	public Attribut_Complet donne_attribut(NomFeature nom,/*ast.*/Classe c)
	{
		int i,j;
		Attribut a1,a2;
		//Classe c=classe_courante;
		assert(c!=null);
		assert(nom!=null);
		//assert(false);
        //if(c==null)
		//	return null;
		Table_Symbol table=donne_table_symbol(c);
		i=table.no_attribut(nom);
		if(i==-1)
			return null;
		assert(i>=0&&i<table.liste_attribut.length):"i="+i+
					",len="+table.liste_attribut.length;
		assert(table.liste_attribut!=null);
		assert(i>=0&&i<table.liste_attribut.length):"i="+i;
		return table.liste_attribut[i];
	}

	public DeclareVar cherche_var(String nom,DeclareVar liste[])
	{
		int i;

		if(liste==null)
			return null;

		for(i=0;i<liste.length;i++)
		{
			if(liste[i].nom.compareToIgnoreCase(nom)==0)
				return liste[i];
		}

		return null;
	}

	public boolean est_constant(Table_Symbol table,Expr expr)
	{
		assert(table!=null);
		assert(expr!=null);
		if((expr.op==Expr.MoinsU||expr.op==Expr.PlusU)&&
			((Expr_Unaire)expr).expr1!=null)
		{
			Expr e=((Expr_Unaire)expr).expr1;
			if(e instanceof Expr_Entier||e instanceof Expr_Reel)
				return true;
		}
		if(expr instanceof Expr_Entier||expr instanceof Expr_Car||
			expr instanceof Expr_Vrai||expr instanceof Expr_Faux||
			expr instanceof Expr_Chaine||expr instanceof Expr_Reel||
			expr instanceof Expr_Tableau)
			return true;
		if(expr instanceof Expr_Var)
		{
			Attribut_Complet ac;
			Expr_Var e=(Expr_Var)expr;
			ac=table.trouve_attribut(e.nom);
			if(ac==null)
				return false;
			return ac.getFeature().est_constant();
			
		}
		return false;
	}

	public boolean type_expanded(Type type,Table_Symbol table)
	{
		assert(type!=null);
		assert(table!=null);
		type=table.trouve_vrai_type(type);
		if(type.expanded)
			return true;
		Classe c=cherche_classe(type);
		if(c==null)
		{
			return false;
		}
		if(c.expanded)
			return true;
		else
			return false;
	}
	
	// retourne true ssi e est une variable assignable
	public boolean variable_assignable(String nom,Position pos)
	{
		DeclareVar d[],d2;
		assert(nom!=null);
		assert(pos!=null);
		if(nom.compareToIgnoreCase("current")==0)
			return false;
		if(nom.compareToIgnoreCase("result")==0)
		{
			if(/*feature_courante==null*/!compiler.env.dans_feature()||
				(//feature_courante!=null&&
				compiler.env.feature.type_retour/*feature_courante.type_retour*/==null))
			{
				erreur(new ErreurVEEN(compiler.env.classe/*classe_courante*/,nom,pos));
				return false;
			}
			return true;
		}
		if(compiler.env.feature/*feature_courante*/ instanceof FeatureRoutine)
		{
			FeatureRoutine f;
			f=((FeatureRoutine)/*feature_courante*/compiler.env.feature);
			d2=cherche_var(nom,f.local);
			if(d2!=null)
				return true;
		}
		//d2=cherche_var(nom,feature_courante.param);
		//if(d2!=null)
		//	return true;
		Attribut_Complet a=donne_attribut(new NomFeature(nom),compiler.env.classe/*classe_courante*/);
		//assert(a!=null);
		if(a!=null)
		{
			if(a.getFeature() instanceof FeatureAttr)
				return true;
			else
				return false;
		}
		return false;
	}

	public boolean var_existe(String nom)
	{
		DeclareVar d[],d2;
		Classe c=compiler.env.classe;//classe_courante;
		Feature f=compiler.env.feature;//feature_courante;

		assert(nom!=null);
		if(nom.compareToIgnoreCase("result")==0)
		{// le mot result
			//assert(false);
			if(f.type_retour==null)
			{
				//erreur("Result dans une feature sans type de retour");
				//erreur(new ErreurVEEN(c,nom,pos));
				return false;
			}
			return true;
		}
		else if(nom.compareToIgnoreCase("current")==0)
		{// le mot current
		  return true;
		}
		else if(f!=null&&
				(f instanceof FeatureRoutine||
				f instanceof FeatureDeferred||
				f instanceof FeatureExternal))
		{
			if(f instanceof FeatureRoutine)
			{// recherche parmis les variables locales
				d=((FeatureRoutine)f).local;
				d2=cherche_var(nom,d);
				if(d2!=null)
					return true;
			}
			// recherche parmis les parametres
			d=f.param;
			d2=cherche_var(nom,d);
			if(d2!=null)
				return true;
			
			// recherche parmis les attributs de la classe
			Attribut_Complet a=donne_attribut(new NomFeature(nom),c);
			//assert(a!=null);
			if(a!=null)
			{
				return true;
			} 
		}
		//assert(false);
		//erreur(new ErreurVEEN(c,nom,pos));
		return false;
	}

	// =true ssi il a un type de retour
	public boolean est_variable(String nom)
	{
		DeclareVar d[],d2;
		Classe c=compiler.env.classe;//classe_courante;
		Feature f=compiler.env.feature;//feature_courante;

		assert(nom!=null);
		if(!var_existe(nom))
			return false;
		if(nom.compareToIgnoreCase("result")==0)
		{// le mot result
			//assert(false);
			if(f.type_retour==null)
			{
				//erreur("Result dans une feature sans type de retour");
				//erreur(new ErreurVEEN(c,nom,pos));
				return false;
			}
			return true;
		}
		else if(nom.compareToIgnoreCase("current")==0)
		{// le mot current
		  return true;
		}
		else if(f!=null&&
				(f instanceof FeatureRoutine||
				f instanceof FeatureDeferred||
				f instanceof FeatureExternal))
		{
			if(f instanceof FeatureRoutine)
			{// recherche parmis les variables locales
				d=((FeatureRoutine)f).local;
				d2=cherche_var(nom,d);
				if(d2!=null)
					return true;
			}
			// recherche parmis les parametres
			d=f.param;
			d2=cherche_var(nom,d);
			if(d2!=null)
				return true;
			
			// recherche parmis les attributs de la classe
			Attribut_Complet a=donne_attribut(new NomFeature(nom),c);
			//assert(a!=null);
			if(a!=null)
			{
				if(a.donne_type_retour(this)!=null)
					return true;
			} 
		}
		//assert(false);
		//erreur(new ErreurVEEN(c,nom,pos));
		return false;
	}

	public Declare_entite donne_entite(String nom,Position pos)
	{
		DeclareVar d[],d2;
		Classe c=compiler.env.classe;//classe_courante;
		Feature f=compiler.env.feature;//feature_courante;
		Declare_entite entite;

		assert(nom!=null);
		assert(pos!=null);
		assert(var_existe(nom));
	
		if(nom.compareToIgnoreCase("result")==0)
		{// le mot result
			//assert(false);
			if(f.type_retour==null)
			{
				//erreur("Result dans une feature sans type de retour");
				erreur(new ErreurVEEN(c,nom,pos));
				return null;
			}
			entite=new DeclareVar(nom,f.type_retour);
			return entite;
		}
		else if(nom.compareToIgnoreCase("current")==0)
		{// le mot current
			entite=new DeclareVar(nom,cherche_type(c/*classe_courante*/));
			return entite;
		}
		else if(f!=null&&
				(f instanceof FeatureRoutine||
				f instanceof FeatureDeferred||
				f instanceof FeatureExternal))
		{
			if(f instanceof FeatureRoutine)
			{// recherche parmis les variables locales
				d=((FeatureRoutine)f).local;
				d2=cherche_var(nom,d);
				if(d2!=null)
					return d2;
			}
			// recherche parmis les parametres
			d=f.param;
			d2=cherche_var(nom,d);
			if(d2!=null)
				return d2;
		
			// recherche parmis les attributs de la classe
			Attribut_Complet a=donne_attribut(new NomFeature(nom),c);
			//assert(a!=null);
			if(a!=null)
			{
				return a;
				//Type t=a.donne_type_retour(this);
				//assert(t!=null);
				//return t;
			} 
		}
		//assert(false);
		erreur(new ErreurVEEN(c,nom,pos));
		return null;
	}

	public Type donne_type_var(String nom,Position pos)
	{
		DeclareVar d[],d2;
		Classe c=compiler.env.classe;//classe_courante;
		Feature f=compiler.env.feature;//feature_courante;

		assert(nom!=null);
		assert(pos!=null);
		assert(var_existe(nom));
		
		if(nom.compareToIgnoreCase("result")==0)
		{// le mot result
			//assert(false);
			if(f.type_retour==null)
			{
				//erreur("Result dans une feature sans type de retour");
				erreur(new ErreurVEEN(c,nom,pos));
				return null;
			}
			return f.type_retour;
		}
		else if(nom.compareToIgnoreCase("current")==0)
		{// le mot current
		  return cherche_type(/*classe_courante*/c);
        }
		else if(f!=null&&
				(f instanceof FeatureRoutine||
				f instanceof FeatureDeferred||
				f instanceof FeatureExternal))
		{
			if(f instanceof FeatureRoutine)
			{// recherche parmis les variables locales
				d=((FeatureRoutine)f).local;
				d2=cherche_var(nom,d);
				if(d2!=null)
					return d2.type;
			}
			// recherche parmis les parametres
			d=f.param;
			d2=cherche_var(nom,d);
			if(d2!=null)
				return d2.type;
			
			// recherche parmis les attributs de la classe
			Attribut_Complet a=donne_attribut(new NomFeature(nom),c);
			//assert(a!=null);
			if(a!=null)
			{
				Type t=a.donne_type_retour(this);
				assert(t!=null);
				return t;
			} 
		}
		//assert(false);
		erreur(new ErreurVEEN(c,nom,pos));
		return null;
	}

	public void erreur(String t)
	{
		logging.erreurMsg(t);
	}

	public void erreur(Erreur t)
	{
		logging.erreur(t);
	}

	public void warning(String t)
	{
		logging.warningMsg(t);
	}

	public void info(String t)
	{
		logging.infoMsg(t);
	}

	public void fatal(String t)
	{
		logging.fatalMsg(t);
	}

	//public Classe classe_courante;
	//public Feature feature_courante;

	public Vector liste_classe=new Vector();
	public Vector liste_type;
	public boolean table_heritage_directe[][],table_heritage[][];
	public static Logging logging;
	public Compiler_Eiffel compiler;
	public boolean dans_rescue,dans_ensure;

}