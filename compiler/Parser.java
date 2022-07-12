package tinyeiffel.compiler;

import java.io.*;
import tinyeiffel.ast.*;
import java.util.*;
import tinyeiffel.erreur.*;

public class Parser extends AbstractParser //Lexer_Constantes
{
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
		if(args!=null&&args.length==1)
		{
			try{
			Parser p=new Parser(args[0]);
			if(p.etatOk())
			{
				System.out.println("Contenu={"+p.lexer.contenu+"}");
				p.parse();
			}
			} catch(FileNotFoundException e)
			{
				System.out.println("Erreur:Impossible d'ouvir le fichier "+args[0]);
				assert(false);
			}
		}
	}

	public Parser(Lexer l)
	{
		assert(l!=null);
		type_utilisee=new Vector();
		lexer=l;
	}
	
	public Parser(String nom) throws FileNotFoundException
	{
		type_utilisee=new Vector();
		lexer=new Lexer();
		lexer.ouvre(nom);
	}
	
	public void parse()
	{
		assert(etatOk());
		//lexer.parcourt();
		parse_classe();
	}

	Classe parse_classe()
	{
		Token t_classe,t_expanded,t_deferred,t_indexing;
		Token texp_defer=null,t_invariant=null,t_end=null;
		Type type;
		Classe cl=null;
		boolean erreur=false,est_expanded=false,est_deferred=false;
		t_indexing=lexer.lit(TK_INDEXING);
		if(t_indexing!=null)
		{// parcour des indexing (TODO:les mettre dans un vector)
			Token t_id,t_pointv;
			int tab[]={ID,PointVirgule,STRING,REAL,INT,DoublePoint,Virgule},n;
			boolean fin=false;
			while(!fin)
			{
				n=lexer.test_prochain3(tab);
				if(n>=0&&n<tab.length)
				{
					lexer.lit(tab[n]);
				}
				else
				{
					fin=true;
				}
			}
			if(erreur||!etatParserOk()||!etatOk())
			{
				erreur_parse2("Erreur dans l'index");
			}
		}
		t_expanded=lexer.lit(TK_EXPANDED);
		if(t_expanded!=null)
		{
			est_expanded=true;
			texp_defer=t_expanded;
		}
		t_deferred=lexer.lit(TK_DEFERRED);
		if(t_deferred!=null)
		{
			est_deferred=true;
			texp_defer=t_deferred;
		}
		t_classe=lexer.lit2();
		if(t_classe==null||t_classe.get_type_token()!=TK_CLASS)
		{
			erreur_parse("Il faut une classe");
			erreur=true;
			assert(false);
		}
		else
		{
			type=parse_type(true);
			if(type==null)
			{
				erreur_parse("Il faut un type pour cette classe");
				erreur=true;
			}
			else
			{
				Vector heritage=null,liste_feature=null,creation=null,invariant=null;
				Token t_obsolete=null,t_feature,t_accoladeO,t_accoladeF;
				Chaine obsolete=null;
				Feature feature;
				Vector export=null;
				
				t_obsolete=lexer.lit(TK_OBSOLETE);
				if(t_obsolete!=null)
				{
					obsolete=parse_chaine();
				}
				heritage=parse_heritage();
				creation=parse_creation();
				t_feature=lexer.lit(TK_FEATURE);
				if(t_feature!=null)
				{
					Type t1;
					Token t_virgule;
					t_accoladeO=lexer.lit(AcoladeO);
					if(t_accoladeO!=null)
					{
						export=new Vector();
						if(lexer.test_prochain(debut_type))
						{
							t1=parse_type(false);
							while(t1!=null)
							{
								export.addElement(t1);
								t_virgule=lexer.lit(Virgule);
								if(t_virgule!=null)
								{
									t1=parse_type(false);
								}
								else
								{
									t_accoladeF=lexer.lit(AcoladeF);
									if(t_accoladeF==null)
									{
										erreur_parse("Il faut une accolade fermante");
										erreur=true;
									}
									t1=null;
								}
							}
						}
						else
						{
							t_accoladeF=lexer.lit(AcoladeF);
							if(t_accoladeF==null)
							{
								erreur_parse("Il faut une accolade fermante");
								erreur=true;
							}
						}
					}
					int i=0;
					liste_feature=new Vector();
					feature=parse_feature(t_feature,export);
					export=null;
					while(feature!=null)
					{
						liste_feature.addElement(feature);
						parse_point_virgule();
						feature=parse_feature(t_feature,export);
						i++;
					}
					//assert(liste_feature.size()==7):liste_feature.size()+"("+i+")";
				}
				t_invariant=lexer.lit(TK_INVARIANT);
				if(t_invariant!=null)
				{
					invariant=parse_assert();
				}
				parse_point_virgule();
				t_end=lexer.lit(TK_END);
				if(t_end==null)
				{
					erreur_parse("Il faut un end");
					erreur=true;
				}
				if(!erreur)
				{
					System.out.println("Ok");
				}
				else
				{
					System.out.println("Il y a des erreurs");
				}
				if(!erreur&&etatParserOk()&&etatOk())
				{
					cl=new Classe(est_deferred,est_expanded,type,liste_feature,
						heritage,invariant,creation,obsolete,null,null);
					ajoute_type(type);
					// TODO: a remettre apres affecter le t_end
					cl.set_token(t_classe,texp_defer,t_obsolete,t_invariant,t_end);
				}
				else
				{
					erreur=true;
				}
			}
		}
		assert((cl!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans la classe");
		}
		return cl;
	}

	Feature parse_feature(Token t_feature_init,Vector liste_export_init)
	{
		Feature res=null;
		Expr exp;
		Token t_feature,t_virgule,t_frozen,t_accoladeO,t_accoladeF;
		Vector liste_export=liste_export_init,liste_nom;
		NomFeature n;
		boolean debut=true,passage_feature=false;
		boolean erreur=false;
		t_feature=lexer.lit(TK_FEATURE);
		while(t_feature!=null)
		{
			Type t1;
			passage_feature=true;
			t_accoladeO=lexer.lit(AcoladeO);
			if(t_accoladeO!=null)
			{
				liste_export=new Vector();
				if(lexer.test_prochain(debut_type))
				{
					t1=parse_type(false);
					while(t1!=null)
					{
						liste_export.addElement(t1);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule!=null)
						{
							t1=parse_type(false);
						}
						else
						{
							t_accoladeF=lexer.lit(AcoladeF);
							if(t_accoladeF==null)
							{
								erreur_parse("Il faut une accolade fermante");
								erreur=true;
							}
							t1=null;
						}
					}
				}
				else
				{
					t_accoladeF=lexer.lit(AcoladeF);
					if(t_accoladeF==null)
					{
						erreur_parse("Il faut une accolade fermante");
						erreur=true;
					}
				}
			}
			t_feature=lexer.lit(TK_FEATURE);
			if(t_feature!=null)
			{
				liste_export=null;
			}
		}
		if(debut&&!passage_feature)
		{
			t_feature=t_feature_init;
			liste_export=liste_export_init;
		}
		debut=false;
		liste_nom=new Vector();
		n=parse_nom_feature();
		while(n!=null)
		{
			liste_nom.addElement(n);
			t_virgule=lexer.lit(Virgule);
			if(t_virgule!=null)
			{
				n=parse_nom_feature();
			}
			else
			{
				n=null;
			}
		}
		//System.out.println("nb="+liste_nom.size());
		if(liste_nom.size()>0)
		{
			Token t_parentheseO,t_parentheseF,t_double_point,t_is;
			DeclareVar declare;
			Vector liste_param=null;
			Type type_retour=null;

			t_parentheseO=lexer.lit(ParentO);
			if(t_parentheseO!=null)
			{
				liste_param=parse_declare_var();
				t_parentheseF=lexer.lit(ParentF);
				if(t_parentheseF==null)
				{
					erreur_parse("Il faut une accolade fermante");
					erreur=true;
				}
			}
			t_double_point=lexer.lit(DoublePoint);
			if(t_double_point!=null)
			{
				type_retour=parse_type(false);
				if(type_retour==null)
				{
					erreur_parse("Il faut un type de retour");
					erreur=true;
				}
			}
			t_is=lexer.lit(TK_IS);
			if(t_is==null)
			{
				res=new FeatureAttr();
			}
			else
			{
				res=parse_corps();
			}
			if(res!=null)
			{
				res.set_nom_param(liste_nom,liste_param,liste_export,null);
				res.type_retour=type_retour;
			}
			ajoute_type(liste_export);
			ajoute_type(type_retour);
		}
		else
		{
			//erreur_parse("Test nb");
			res=null;
		}
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'attribut");
		}
		return res;
	}

	Feature parse_corps()
	{
		Vector require=new Vector(),ensure=new Vector(),rescue=new Vector();
		Token t_obsolete,t_require,t_ensure,t_end,t_rescue,t_unique;
		Chaine obsolete;
		boolean erreur=false;
		Feature res=null;
		Expr expr;
		t_unique=lexer.lit(TK_UNIQUE);
		if(t_unique!=null)
		{
			res=new FeatureUnique();
			res.set_token(t_unique); 
		}
		else
		{
			int tab[]={TK_OBSOLETE,TK_REQUIRE,TK_DO,TK_ONCE,
				TK_LOCAL,TK_DEFERRED,TK_EXTERNAL,TK_UNIQUE};
			if(!lexer.test_prochain(tab))
			{
				//assert(false);
				expr=parse_expr();
				if(expr!=null)
				{
					int tab2[]={INT};
					assert(!lexer.test_prochain(tab2));
					res=new FeatureExpr();
					((FeatureExpr)res).expr=expr;
				}
				else
				{
					erreur_parse("Il faut une expression");
					erreur=true;
				}
			}
			else
			{
				obsolete=null;
				t_obsolete=lexer.lit(TK_OBSOLETE);
				if(t_obsolete!=null)
				{
					obsolete=parse_chaine();
					if(obsolete==null)
					{
						erreur_parse("Il faut une chaine de caractère");
						erreur=true;
					}
				}
				t_require=lexer.lit(TK_REQUIRE);
				if(t_require!=null)
				{
					Token t_else;
					t_else=lexer.lit(TK_ELSE);
					require=parse_assert();
				}
				res=parse_corps2();
				t_ensure=lexer.lit(TK_ENSURE);
				if(t_ensure!=null)
				{
					Token t_then;
					t_then=lexer.lit(TK_THEN);
					ensure=parse_assert();
				}
				t_rescue=lexer.lit(TK_RESCUE);
				if(t_rescue!=null)
				{
					rescue=parse_instr();
				}
				t_end=lexer.lit(TK_END);
				if(t_end==null)
				{
					erreur_parse("Il faut end");
					erreur=true;
				}
				if(!erreur&&res!=null)
				{// todo: prendre en compte les commentaires
					res.set_require_ensure(require,ensure,rescue,
						obsolete,new Vector(),null);
					res.set_token(t_obsolete,t_require,
						t_ensure,t_rescue);
				}
				/*exp=parse_expr();
				while(exp!=null)
				{
					res.addElement(exp);
					parse_point_virgule();
						exp=parse_expr();
				}*/
			}
		}
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'attribut");
		}
		return res;
	}

	Vector parse_declare_var()
	{
		Vector res=new Vector(),liste_nom;
		Type type;
		Token t_nom,t_double_point,t_virgule,t_pvirgule;
		boolean fin=false,erreur=false;
		int i;
		DeclareVar declare;
		t_nom=lexer.lit(ID);
		while(t_nom!=null&&!fin)
		{
			liste_nom=new Vector();
			while(t_nom!=null)
			{
				liste_nom.addElement(t_nom);
				t_virgule=lexer.lit(Virgule);
				if(t_virgule!=null)
				{
					t_nom=lexer.lit(ID);
				}
				else
				{
					t_nom=null;
				}
			}
			t_double_point=lexer.lit(DoublePoint);
			if(t_double_point==null)
			{
				erreur_parse("Il faut un double point");
				erreur=true;
				fin=true;
			}
			else
			{
				type=parse_type(false);
				for(i=0;i<liste_nom.size();i++)
				{
					t_nom=(Token)liste_nom.elementAt(i);
					declare=new DeclareVar(t_nom.text,type);
					declare.set_token(t_nom);
					res.addElement(declare);
				}
				t_pvirgule=lexer.lit(PointVirgule);
				if(t_pvirgule!=null)
				{
					while(t_pvirgule!=null)
					{
						t_pvirgule=lexer.lit(PointVirgule);
					}
					t_nom=lexer.lit(ID);
				}
				else
				{
					//fin=true;
					t_nom=lexer.lit(ID);
				}
			}
		}
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans la declaration des parametres");
		}
		return res;
	}

	Feature parse_corps2()
	{// TODO: prendre en compte : 
	// test_important: a>0
		Feature res=null;
		Expr exp;
		boolean erreur=false,est_once;
		Token t_local,t_do,t_external,t_once=null,t_deferred,t_alias=null;
		Vector var_local=new Vector(),liste_instr=new Vector();
		t_deferred=lexer.lit(TK_DEFERRED);
		if(t_deferred!=null)
		{// deferred
			res=new FeatureDeferred();res.tdeferred=t_deferred;
		}
		else
		{
			t_external=lexer.lit(TK_EXTERNAL);
			if(t_external!=null)
			{// external
				Chaine ch,ch2=null;
				ch=parse_chaine();
				if(ch==null)
				{
					erreur_parse("Il faut une chaine de caractère");
					erreur=true;
				}
				else
				{
					t_alias=lexer.lit(TK_ALIAS);
					if(t_alias!=null)
					{
						ch2=parse_chaine();
						if(ch2==null)
						{
							erreur_parse("Il faut une chaine de caractère");
							erreur=true;
						}
					}
				}
				if(!erreur)
				{
					res=new FeatureExternal(ch,ch2);
					res.texternal=t_external;
					res.talias=t_alias;
				}
			}
			else
			{// do ou once
				t_local=lexer.lit(TK_LOCAL);
				if(t_local!=null)
				{
					var_local=parse_declare_var();
				}
				est_once=false;
				t_do=lexer.lit(TK_DO);
				if(t_do==null)
				{
					est_once=true;
					t_once=lexer.lit(TK_ONCE);
					if(t_once==null)
					{
						erreur_parse("Il faut soit do, soit once");
						erreur=true;
					}
				}
				liste_instr=parse_instr();
				if(!erreur)
				{
					res=new FeatureRoutine(est_once,liste_instr,var_local);
					if(est_once)
					{
						res.tdo=t_do;
					}
					else
					{
						res.tdo=t_once;
					}
				}
			}
		}
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans le corps de l'attribut");
		}
		return res;
	}

	Vector parse_instr()
	{// TODO: prendre en compte : 
	// test_important: a>0
		Vector res=new Vector();
		Expr exp;
		boolean erreur=false,fin=false;
		int tab1[]={ID},tab2[]={Affect},tab3[]={ParentO,Point,PointVirgule};
		Token t_from,t_if,t_debug,t_check,t_retry,t_excl,t_id,t_inspect;
		int tab_debut[]={TK_FROM,TK_IF,TK_RETRY,TK_DEBUG,TK_CHECK,TK_INSPECT,
			Exclamation,ID,ParentO,INT,REAL,STRING,CHAR};
		Instr_Appel instr_appel=null;
		do
		{
			switch(lexer.test_prochain3(tab_debut))
			{
			case 0:
				{// from
				t_from=lexer.lit(TK_FROM);
				assert(t_from!=null);
				Vector liste_instr1,liste_instr2;
				Vector liste_assert=new Vector();
				Expr expr,expr_variant=null;
				Token t_until,t_loop,t_end,t_variant,t_invariant;
				Instr_Loop instr;
				liste_instr1=parse_instr();
				t_invariant=lexer.lit(TK_INVARIANT);
				if(t_invariant!=null)
				{// invariant
					liste_assert=parse_assert();
				}
				t_variant=lexer.lit(TK_VARIANT);
				if(t_variant!=null)
				{// variant
					expr_variant=parse_expr();
				}
				t_until=lexer.lit(TK_UNTIL);
				if(t_until==null)
				{
					erreur_parse("Il faut until");
					erreur=true;
				}
				else
				{// until
					expr=parse_expr();
					if(expr==null)
					{
						erreur_parse("Il faut une expression");
						erreur=true;
					}
					else
					{
						ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
						t_loop=lexer.lit(TK_LOOP);
						if(t_loop==null)
						{
							erreur_parse("Il faut loop");
							erreur=true;
						}
						else
						{// loop
							liste_instr2=parse_instr();
							t_end=lexer.lit(TK_END);
							if(t_end==null)
							{
								erreur_parse("Il faut end");
								erreur=true;
							}
							instr=new Instr_Loop(expr,liste_instr1,liste_instr2,
							liste_assert,expr_variant);
							instr.set_token(t_from,t_until,t_invariant,t_variant,
								t_loop,t_end);
							res.addElement(instr);
						}
					}
				}
				}
				break;
			case 1:
				{// if
				t_if=lexer.lit(TK_IF);
				assert(t_if!=null);
				Vector liste_instr;
				Expr expr;
				Token t_then,t_elseif,t_end,t_else;
				Instr_If instr_if=null;
				Instr_ElseIf instr_elseif=null;
				Instr_Else instr_else=null;
				Instr derniere_instr;
				expr=parse_expr();
				if(expr==null)
				{
					erreur_parse("Il faut une expression");
					erreur=true;
				}
				else
				{
					t_then=lexer.lit(TK_THEN);
					if(t_then==null)
					{
						erreur_parse("Il faut then");
						erreur=true;
					}
					else
					{
						ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
						liste_instr=parse_instr();
						instr_if=new Instr_If(expr,liste_instr);
						instr_if.tif=t_if;
						instr_if.tthen=t_then;
						res.addElement(instr_if);
					}
				}
				if(!erreur)
				{
					//boolean terminer;
					assert(instr_if!=null);
					derniere_instr=instr_if;
					t_elseif=lexer.lit(TK_ELSEIF);
					while(t_elseif!=null&&!erreur)
					{
						expr=parse_expr();
						if(expr==null)
						{
							erreur_parse("Il faut une expression");
							erreur=true;
						}
						else
						{
							t_then=lexer.lit(TK_THEN);
							if(t_then==null)
							{
								erreur_parse("Il faut then");
								erreur=true;
							}
							else
							{
								liste_instr=parse_instr();
								instr_elseif=new Instr_ElseIf(expr,liste_instr);
								instr_elseif.telseif=t_elseif;
								instr_elseif.tthen=t_then;
								((Suite)derniere_instr).setSuivant(instr_elseif);
								derniere_instr=instr_elseif;
							}
						}
						t_elseif=lexer.lit(TK_ELSEIF);
					}
					assert(derniere_instr!=null);
					t_else=lexer.lit(TK_ELSE);
					if(t_else!=null&&!erreur)
					{
						liste_instr=parse_instr();
						instr_else=new Instr_Else(liste_instr);
						instr_else.telse=t_else;
						((Suite)derniere_instr).setSuivant(instr_else);
						derniere_instr=instr_elseif;
					}
				}
				t_end=lexer.lit(TK_END);
				if(t_end==null)
				{
					erreur_parse("Il faut end");
					erreur=true;
				}
				else
				{// end
					
				}
				}
				break;
			case 2: // TK_RETRY
				{// retry
				t_retry=lexer.lit(TK_RETRY);
				assert(t_retry!=null);
				Instr_Retry instr_retry;
				instr_retry=new Instr_Retry();
				instr_retry.tretry=t_retry;
				res.addElement(instr_retry);
				}
				break;
			case 3: // TK_DEBUG
				{// debug
				t_debug=lexer.lit(TK_DEBUG);
				assert(t_debug!=null);
				Vector liste_instr,liste_chaine;
				Token t_end,t_virgule,t_parentO,t_parentF;
				Instr_Debug instr_debug;
				Chaine chaine;ajoute_type(new TypeSimple(false,"STRING",new Vector()));
				liste_chaine=new Vector();
				t_parentO=lexer.lit(ParentO);
				if(t_parentO!=null)
				{
					chaine=parse_chaine();
					while(chaine!=null&&!erreur)
					{
						liste_chaine.addElement(chaine);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule!=null)
						{
							chaine=parse_chaine();
							if(chaine==null)
							{
								erreur_parse("Il faut une chaine");
								erreur=true;
							}
						}
						else
						{
							chaine=null;
						}
					}
					t_parentF=lexer.lit(ParentF);
					if(t_parentF==null)
					{
						erreur_parse("Il faut une parenthese fermante");
						erreur=true;
					}
				}
				if(!erreur)
				{
					liste_instr=parse_instr();
					t_end=lexer.lit(TK_END);
					if(t_end==null)
					{
						erreur_parse("Il faut end");
						erreur=true;
					}
					instr_debug=new Instr_Debug(liste_chaine,liste_instr);
					instr_debug.tdebug=t_debug;
					instr_debug.tend=t_end;
					res.addElement(instr_debug);
				}
				}
				break;
			case 4: // TK_CHECK
				{// check
				t_check=lexer.lit(TK_CHECK);
				assert(t_check!=null);
				Vector liste_assert,liste_chaine;
				Token t_end,t_virgule,t_parentO,t_parentF;
				Instr_Check instr_check;
				Chaine chaine;
				ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
				//System.out.println("AAA");
				liste_assert=parse_assert();
				//System.out.println("BBB");
				t_end=lexer.lit(TK_END);
				if(t_end==null)
				{
					erreur_parse("Il faut end");
					erreur=true;
				}
				instr_check=new Instr_Check(liste_assert);
				instr_check.tcheck=t_check;
				instr_check.tend=t_end;
				res.addElement(instr_check);
				}
				break;
			case 5: // TK_INSPECT
				{// inspect
				t_inspect=lexer.lit(TK_INSPECT);
				assert(t_inspect!=null);
				Vector liste_expr,liste_expr2,liste_instr;
				Token t_end,t_virgule,t_when,t_then=null,t_double_point,t_else;
				Instr_Inspect instr_inspect;
				Instr derniere_instr=null;
				Chaine chaine;
				Expr expr,expr2,expr3;
				// TODO: a faire
				//assert(false);
				expr=parse_expr();
				if(expr==null)
				{
					erreur_parse("Il faut une expression");
					erreur=true;
				}
				else
				{
					t_when=lexer.lit(TK_WHEN);
					while(t_when!=null&&!erreur)
					{
						liste_expr=new Vector();
						expr2=parse_expr();
						while(expr2!=null&&!erreur)
						{
							liste_expr2=new Vector();
							liste_expr2.addElement(expr2);
							t_double_point=lexer.lit(DeuxPoint);
							if(t_double_point!=null)
							{
								expr3=parse_expr();
								if(expr3!=null)
								{
									liste_expr2.addElement(expr3);
								}
								else
								{
									erreur_parse("Il faut une expression");
									erreur=true;
								}
							}
							liste_expr.addElement(liste_expr2);
							t_virgule=lexer.lit(Virgule);
							if(t_virgule!=null)
							{
								expr2=parse_expr();
							}
							else
							{
								expr2=null;
							}
						}
						t_then=lexer.lit(TK_THEN);
						if(t_then==null)
						{
							erreur_parse("Il faut then");
							erreur=true;
						}
						liste_instr=parse_instr();
						instr_inspect=new Instr_Inspect(expr,liste_expr,liste_instr);
						instr_inspect.tinspect=t_inspect;
						instr_inspect.twhen=t_when;
						instr_inspect.tthen=t_then;
						if(derniere_instr==null)
						{
							derniere_instr=instr_inspect;
							res.addElement(instr_inspect);
						}
						else
						{
							((Suite)derniere_instr).setSuivant(instr_inspect);
							derniere_instr=instr_inspect;
						}
						t_when=lexer.lit(TK_WHEN);
					}
					t_else=lexer.lit(TK_ELSE);
					if(t_else!=null)
					{
						liste_instr=parse_instr();
						instr_inspect=new Instr_Inspect(expr,new Vector(),liste_instr);
						instr_inspect.tinspect=t_inspect;
						instr_inspect.twhen=t_when;
						instr_inspect.tthen=t_then;
						if(derniere_instr==null)
						{
							derniere_instr=instr_inspect;
							res.addElement(instr_inspect);
						}
						else
						{
							((Suite)derniere_instr).setSuivant(instr_inspect);
							derniere_instr=instr_inspect;
						}
					}
					t_end=lexer.lit(TK_END);
					if(t_end==null)
					{
						erreur_parse("Il faut end");
						erreur=true;
					}
				}
				}
				break;
			case 6: //Exclamation
				{// creation
				t_excl=lexer.lit(Exclamation);
				assert(t_excl!=null);
				Vector liste_param=null;
				Token t_excl2,t_point,t_id2=null;
				Instr_Creation instr_creation;
				Type type;
				if(lexer.test_prochain(debut_type))
				{
					type=parse_type(false);
				}
				else
				{
					type=null;
				}
				t_excl2=lexer.lit(Exclamation);
				if(t_excl2==null)
				{
					erreur_parse("Il faut !");
					erreur=true;
				}
				else
				{
					t_id=lexer.lit(ID);
					if(t_id==null)
					{
						erreur_parse("Il faut un id");
						erreur=true;
					}
					else
					{
						t_point=lexer.lit(Point);
						liste_param=new Vector();
						if(t_point!=null)
						{
							t_id2=lexer.lit(ID);
							if(t_id2==null)
							{
								erreur_parse("Il faut un id");
								erreur=true;
							}
							else
							{
								liste_param=parse_param();
							}
						}
						if(t_id2!=null)
							instr_creation=new Instr_Creation(type,t_id.text,t_id2.text,liste_param);
						else
							instr_creation=new Instr_Creation(type,t_id.text,null,liste_param);
						ajoute_type(type);
						instr_creation.texcl1=t_excl;
						instr_creation.texcl2=t_excl2;
						instr_creation.tid=t_id;
						instr_creation.tid2=t_id2;
						instr_creation.tpoint=t_point;
						res.addElement(instr_creation);
					}
				}
				}
				break;
			case 7:// ID
				{
					int n,tab[][]={{ID,Point,ID,Affect},
								{ID,Affect},
								{ID,Point,ID,TentativeAffect},
								{ID,TentativeAffect}};
					n=lexer.test_prochain4(tab);
					if(n!=-1)
					{// c'est un affect
						Token t_id2,t_point,t_affect;
						Expr expr;
						Instr_Affect instr_affect;
						Instr_TentAffect instr_tent_affect;
						switch(n)
						{
							case 0:
								t_id=lexer.lit(ID);
								assert(t_id!=null);
								t_point=lexer.lit(Point);
								assert(t_point!=null);
								t_id2=lexer.lit(ID);
								assert(t_id2!=null);
								t_affect=lexer.lit(Affect);
								assert(t_affect!=null);
								expr=parse_expr();
								instr_affect=new Instr_Affect(t_id2.text,expr);
								instr_affect.set_token(t_id2,t_affect);
								instr_affect.tcurrent=t_id;
								instr_affect.var_current=t_id.text;
								res.addElement(instr_affect);
								break;
							case 1:
								t_id2=lexer.lit(ID);
								assert(t_id2!=null);
								t_affect=lexer.lit(Affect);
								assert(t_affect!=null);
								expr=parse_expr();
								instr_affect=new Instr_Affect(t_id2.text,expr);
								instr_affect.set_token(t_id2,t_affect);
								res.addElement(instr_affect);
								break;
							case 2:
								t_id=lexer.lit(ID);
								assert(t_id!=null);
								t_point=lexer.lit(Point);
								assert(t_point!=null);
								t_id2=lexer.lit(ID);
								assert(t_id2!=null);
								t_affect=lexer.lit(TentativeAffect);
								assert(t_affect!=null);
								expr=parse_expr();
								instr_tent_affect=new Instr_TentAffect(t_id2.text,expr);
								instr_tent_affect.set_token(t_id2,t_affect);
								instr_tent_affect.tcurrent=t_id;
								instr_tent_affect.var_current=t_id.text;
								res.addElement(instr_tent_affect);
								break;
							case 3:
								t_id2=lexer.lit(ID);
								assert(t_id2!=null);
								t_affect=lexer.lit(TentativeAffect);
								assert(t_affect!=null);
								expr=parse_expr();
								instr_tent_affect=new Instr_TentAffect(t_id2.text,expr);
								instr_tent_affect.set_token(t_id2,t_affect);
								res.addElement(instr_tent_affect);
								break;
							default:
								assert(false);
						}
						break;
					}
					else
					{// on continue
						Vector liste_param;
						t_id=lexer.lit(ID);
						assert(t_id!=null);
						liste_param=parse_param();
						instr_appel=new Instr_Appel(t_id.text,liste_param);
						instr_appel.tid=t_id;
						res.addElement(instr_appel);
					}
				}
			case 8:// ParentO
				if(instr_appel==null)
				{
					Token t_parentO,t_parentF;
					Expr expr;
					t_parentO=lexer.lit(ParentO);
					assert(t_parentO!=null);
					expr=parse_expr();
					if(expr==null)
					{
						erreur_parse("Il faut une expression");
						erreur=true;
					}
					else
					{
						t_parentF=lexer.lit(ParentF);
						if(t_parentF==null)
						{
							erreur_parse("Il faut )");
							erreur=true;
						}
						instr_appel=new Instr_Appel(expr);
						res.addElement(instr_appel);
					}
				}
			case 9:// INT
				if(instr_appel==null)
				{
					Token t_int;
					Expr expr;
					t_int=lexer.lit(INT);
					assert(t_int!=null);
					expr=new Expr_Entier(t_int.text,t_int);
					instr_appel=new Instr_Appel(expr);
					res.addElement(instr_appel);
					ajoute_type(new TypeSimple(false,"INTEGER",new Vector()));
				}
			case 10:// REAL
				if(instr_appel==null)
				{
					Token t_real;
					Expr expr;
					t_real=lexer.lit(REAL);
					assert(t_real!=null);
					expr=new Expr_Reel(t_real.text,t_real);
					instr_appel=new Instr_Appel(expr);
					res.addElement(instr_appel);
					ajoute_type(new TypeSimple(false,"REAL",new Vector()));
				}
			case 11:// STRING
				if(instr_appel==null)
				{
					Chaine chaine;
					Expr expr;
					chaine=parse_chaine();
					assert(chaine!=null);
					expr=new Expr_Chaine(chaine);
					instr_appel=new Instr_Appel(expr);
					res.addElement(instr_appel);
					ajoute_type(new TypeSimple(false,"STRING",new Vector()));
				}
			case 12:// CHAR
				if(instr_appel==null)
				{
					Token t_char;
					Expr expr;
					t_char=lexer.lit(CHAR);
					assert(t_char!=null);
					expr=new Expr_Car(t_char.text,t_char);
					instr_appel=new Instr_Appel(expr);
					res.addElement(instr_appel);
					ajoute_type(new TypeSimple(false,"CHAR",new Vector()));
				}
				{
				Instr derniere_instr;
				Token t_point;
				Vector liste_param;
				assert(instr_appel!=null);
				derniere_instr=instr_appel;
				t_point=lexer.lit(Point);
				while(t_point!=null&&!erreur)
				{
					t_id=lexer.lit(ID);
					if(t_id==null)
					{
						erreur_parse("Il faut un id");
						erreur=true;
					}
					else
					{
						liste_param=parse_param();
						instr_appel=new Instr_Appel(t_id.text,liste_param);
						instr_appel.tid=t_id;
						instr_appel.tpoint=t_point;
						((Suite)derniere_instr).setSuivant(instr_appel);
						derniere_instr=instr_appel;
					}
					t_point=lexer.lit(Point);
				}
				instr_appel=null;
				}
				break;
			default:
				{
				//erreur_parse("Default");
				int tab[]={PointVirgule};
				if(!lexer.test_prochain(tab))
					fin=true;
				}
				break;
			}
			parse_point_virgule();
		} while(!fin&&!erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'instruction");
		}
		return res;
	}

	Vector parse_param()
	{
		Vector res=new Vector();
		Expr exp;
		boolean erreur=false;
		Token t_virgule,t_parentO,t_parentF;
		t_parentO=lexer.lit(ParentO);
		if(t_parentO!=null)
		{
			exp=parse_expr();
			while(exp!=null)
			{
				res.addElement(exp);
				t_virgule=lexer.lit(Virgule);
				if(t_virgule!=null)
				{
					exp=parse_expr();
				}
				else
				{
					exp=null;
				}
			}
			t_parentF=lexer.lit(ParentF);
			if(t_parentF==null)
			{
				erreur_parse("Il faut )");
				erreur=true;
			}
		}
		return res;
	}

	Vector parse_assert()
	{// TODO: prendre en compte : 
	// test_important: a>0
		Vector res=new Vector();
		Expr exp;
		Assert as;
		String nom=null;
		boolean erreur=false;
		int tab1[]={ID},tab2[]={DoublePoint},i;
		//System.out.println("Deb");
		if(lexer.test_prochain2(tab1,tab2))
		{
			Token t_nom,t_dpoint;
			//assert(false);
			t_nom=lexer.lit(ID);
			assert(t_nom!=null);
			t_dpoint=lexer.lit(DoublePoint);
			assert(t_dpoint!=null);
			nom=t_nom.text;
		}
		else
		{
			nom=null;
		}
		//System.out.println("Deb2");
		//assert(false);
		//System.out.println("Deb2");
		exp=parse_expr();
		//System.out.println("Deb3");
		if(exp!=null)
		{
			if(nom==null)
			{
				as=new Assert(exp);
			}
			else
			{
				as=new Assert(nom,exp);
			}
		}
		else
		{
			as=null;
		}
		//i=0;
		//System.out.println("Mil");
		while(as!=null)
		{
			//System.out.println(i);i++;
			res.addElement(as);
			parse_point_virgule();
			if(lexer.test_prochain2(tab1,tab2))
			{
				Token t_nom=lexer.lit(ID),t_dpoint;
				assert(t_nom!=null);
				t_dpoint=lexer.lit(DoublePoint);
				assert(t_dpoint!=null);
				nom=t_nom.text;
			}
			else
			{
				nom=null;
			}
			exp=parse_expr();
			if(exp!=null)
			{
				if(nom==null)
				{
					as=new Assert(exp);
				}
				else
				{
					as=new Assert(nom,exp);
				}
			}
			else
			{
				as=null;
			}
		}
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans la liste d'assertion");
		}
		//System.out.println("End");
		return res;
	}

	Vector parse_heritage()
	{
		Token t_herit,t_expanded,t_deferred;
		Type type;
		Heritage h=null;
		boolean erreur=false,est_expanded=false,est_deferred=false;
		Vector liste_heritage=null;
		t_herit=lexer.lit(TK_INHERIT);
		if(t_herit!=null)
		{
			liste_heritage=new Vector();
			type=parse_type(false);
			while(type!=null)
			{
				Vector rename=new Vector(),export=new Vector(),
					undefine=new Vector(),redefine=new Vector(),
					select=new Vector();
				Token therit=null,t_rename=null,t_export=null,
					t_undefine=null,t_redefine=null,t_select=null,t_end=null;
				NomFeature n1,n2;
				// rename
				t_rename=lexer.lit(TK_RENAME);
				if(t_rename!=null)
				{
					Token t_id,t_id2,t_as,t_virgule;
					boolean fin=false;
					Rename ren;
					do
					{
						n1=parse_nom_feature();
						if(n1!=null)
						{
							t_as=lexer.lit(TK_AS);
							if(t_as==null)
							{
								erreur_parse("Il faut as");
								erreur=true;
								fin=true;
							}
							else
							{
								n2=parse_nom_feature();
								if(n2!=null)
								{
									ren=new Rename(n1,n2);
									rename.addElement(ren);
									t_virgule=lexer.lit(Virgule);
									if(t_virgule==null)
									{
										fin=true;
									}
								}
								else
								{
									erreur_parse("Il faut un nom de feature");
									erreur=true;
									fin=true;
								}
							}
						}
						else
						{
							fin=true;
						}
						
					} while(!fin);
				}
				parse_point_virgule();
				// export
				t_export=lexer.lit(TK_EXPORT);
				if(t_export!=null)
				{
					Token t_accolO,t_accolF,t_as,t_virgule,t_all;
					boolean fin=false;
					Export exp;
					Type t1,t2;
					Vector liste_type=null,liste_nom_feature;
					do
					{
						liste_type=null;
						liste_nom_feature=new Vector();
						t_accolO=lexer.lit(AcoladeO);
						if(t_accolO!=null)
						{
							liste_type=new Vector();
							if(lexer.test_prochain(debut_type))
							{
								t1=parse_type(false);
								while(t1!=null)
								{
									liste_type.addElement(t1);
									t_virgule=lexer.lit(Virgule);
									if(t_virgule==null)
									{
										t1=null;
									}
									else
									{
										t1=parse_type(false);
									}
								}
							}
							t_accolF=lexer.lit(AcoladeF);
							if(t_accolF==null)
							{
								erreur_parse("Il faut une accolade fermante");
								erreur=true;
								fin=true;
							}
							t_all=lexer.lit(TK_ALL);
							if(t_all!=null)
							{
	
							}
							else
							{
								n1=parse_nom_feature();
								while(n1!=null)
								{
									liste_nom_feature.addElement(n1);
									t_virgule=lexer.lit(Virgule);
									if(t_virgule==null)
									{
										n1=null;
									}
									else
									{
										n1=parse_nom_feature();
									}
								}
							}
							exp=new Export(liste_nom_feature,liste_type);
							export.addElement(exp);
							parse_point_virgule();
						}
						else
						{
							fin=true;
						}
					} while(!fin);
				}
				parse_point_virgule();
				// undefine
				t_undefine=lexer.lit(TK_UNDEFINE);
				if(t_undefine!=null)
				{
					Token t_virgule;
					n1=parse_nom_feature();
					while(n1!=null)
					{
						undefine.addElement(n1);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule==null)
						{
							n1=null;
						}
						else
						{
							n1=parse_nom_feature();
						}
					}
				}
				parse_point_virgule();
				// redefine
				t_redefine=lexer.lit(TK_REDEFINE);
				if(t_redefine!=null)
				{
					Token t_virgule;
					n1=parse_nom_feature();
					while(n1!=null)
					{
						redefine.addElement(n1);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule==null)
						{
							n1=null;
						}
						else
						{
							n1=parse_nom_feature();
						}
					}
				}
				parse_point_virgule();
				// select
				t_select=lexer.lit(TK_SELECT);
				if(t_select!=null)
				{
					Token t_virgule;
					n1=parse_nom_feature();
					while(n1!=null)
					{
						select.addElement(n1);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule==null)
						{
							n1=null;
						}
						else
						{
							n1=parse_nom_feature();
						}
					}
				}
				parse_point_virgule();
				t_end=lexer.lit(TK_END);
				parse_point_virgule();
				h=new Heritage(type,rename,export,undefine,redefine,select);
				h.set_token(t_herit,t_rename,t_export,t_undefine,t_redefine,
						t_select,t_end);
				liste_heritage.addElement(h);
				parse_point_virgule();
				if(lexer.test_prochain(debut_type))
				{
					type=parse_type(false);
				}
				else
				{
					type=null;
				}
			}
		}
		//assert((cl!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'heritage");
		}
		return liste_heritage;
	}

	int debut_type[]={TK_EXPANDED,ID,TK_LIKE};

	void parse_point_virgule()
	{
		Token t_virgule;
		t_virgule=lexer.lit(PointVirgule);
		while(t_virgule!=null)
		{
			t_virgule=lexer.lit(PointVirgule);
		}
	}

	Vector parse_creation()
	{
		Token t_creation,t_accoladeO,t_accoladeF,t_id,t_virgule;
		Type type;
		Creation c=null;
		boolean erreur=false,est_expanded=false,est_deferred=false;
		Vector liste_creation=new Vector(),liste_nom_feature,liste_type;
		NomFeature n;
		t_creation=lexer.lit(TK_CREATION);
		while(t_creation!=null)
		{
			liste_nom_feature=new Vector();
			t_accoladeO=lexer.lit(AcoladeO);
			if(t_accoladeO!=null)
			{
				liste_type=new Vector();
				if(lexer.test_prochain(debut_type))
				{
					type=parse_type(false);
					while(type!=null)
					{
						liste_type.addElement(type);
						t_virgule=lexer.lit(Virgule);
						if(t_virgule!=null)
						{
							type=parse_type(false);
						}
						else
						{
							type=null;
						}
					}
				}
				t_accoladeF=lexer.lit(AcoladeF);
				if(t_accoladeF==null)
				{
					erreur_parse("Il manque une accolade fermante");
				}
			}
			else
			{
				liste_type=null;
			}
			n=parse_nom_feature();
			while(n!=null)
			{
				liste_nom_feature.addElement(n);
				t_virgule=lexer.lit(Virgule);
				if(t_virgule!=null)
				{
					n=parse_nom_feature();
				}
				else
				{
					n=null;
				}
			}
			c=new Creation(liste_type,liste_nom_feature);
			c.set_token(t_creation);
			liste_creation.addElement(c);
			t_creation=lexer.lit(TK_CREATION);
		}
		//assert((cl!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans la creation");
		}
		return liste_creation;
	}

	// parse un type
	Type parse_type(boolean type_classe)
	{
		Token t_type,t_expand,t_like;
		Type type=null,type2,type3;
		boolean erreur=false,expand=false;
		Token crochetO=null,crochetF=null,vers;
		t_expand=lexer.lit(TK_EXPANDED);
		if(t_expand!=null)
		{
			expand=true;
		}
		t_type=lexer.lit(ID);
		if(t_type==null)
		{
			t_like=lexer.lit(TK_LIKE);
			if(t_like==null)
			{
				erreur_parse("Il faut un type");
				erreur=true;
			}
			else
			{
				Expr expr;
				expr=parse_expr();
				if(expr!=null)
				{
					type=new TypeAncre(expr);
					type.set_token(t_like);
				}
				else
				{
					erreur_parse("Il faut une expression");
					erreur=true;
				}
			}
		}
		else
		{
			Token virgule;
			Vector param=new Vector();
			boolean fin=false;
			crochetO=lexer.lit(CrochetO);
			if(crochetO!=null)
			{
				crochetF=lexer.lit(CrochetF);
				if(crochetF==null)
				{
					int i=0;
					while(!fin)
					{
						//erreur_parse("no="+i);
						if(type_classe)
						{
							type2=parse_type2();
						}
						else
						{
							type2=parse_type(type_classe);
						}
						if(type2!=null)
						{
							param.addElement(type2);
							virgule=lexer.lit(Virgule);
							if(virgule==null)
							{
								fin=true;
							}
							else
							{
	
							}
						}
						else
						{
							fin=true;
						}
						i++;
					}
					crochetF=lexer.lit(CrochetF);
					if(crochetF==null)
					{
						erreur_parse("Il faut ]");
						erreur=true;
					}
				}
			}
			type=new TypeSimple(expand,t_type.text,param);
			type.set_token(t_type,crochetO,crochetF);
			ajoute_type(type);
		}
		assert((type!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans le type");
		}
		return type;
	}

	
	// parse un type
	Type parse_type2()
	{
		Token t_type,t_expand,t_like;
		Type type=null,type2,type3;
		boolean erreur=false,expand=false;
		Token crochetO=null,crochetF=null,vers;
		t_expand=lexer.lit(TK_EXPANDED);
		if(t_expand!=null)
		{
			expand=true;
		}
		t_type=lexer.lit(ID);
		if(t_type==null)
		{
			erreur_parse("Il faut un type");
			erreur=true;
		}
		else
		{
			Token virgule;
			Vector param=new Vector();
			boolean fin=false;
			vers=lexer.lit(Fleche);
			if(vers!=null)
			{
				type3=parse_type(true);
				param.addElement(type3);
			}
			type=new TypeSimple(expand,t_type.text,param);
			type.is_from=true;
			type.set_token(t_type,vers);
			ajoute_type(type);
		}
		assert((type!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans le type");
		}
		return type;
	}


	Expr parse_expr()
	{
		Expr e=parse_expr17();		
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return e;
	}

	// les operateurs binaires xor,implies
	Expr parse_expr17()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		int tab[]={TK_XOR,TK_IMPLIES},n;
		boolean erreur=false;
		expr1=parse_expr18();
		res=expr1;
		n=lexer.test_prochain3(tab);
		while(n!=-1&&!erreur)
		{
			t_exp=lexer.lit2();
			assert(t_exp!=null);
			expr2=parse_expr18();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				switch(t_exp.get_type_token())
				{
					case TK_XOR:
						n=Expr.Xor;
						break;
					case TK_IMPLIES:
						n=Expr.Implies;
						break;
					default:
						assert(false);
				}
				res=new Expr_Binaire(n,res,expr2,t_exp);
				n=lexer.test_prochain3(tab);
			}
		}
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs binaires or, or else
	Expr parse_expr18()
	{
		Token t_exp,t_exp2,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		int n;
		boolean erreur=false;
		expr1=parse_expr19();
		res=expr1;
		t_exp=lexer.lit(TK_OR);
		//System.out.println("AAAA"+t_exp);
		while(t_exp!=null&&!erreur)
		{
			//System.out.println("AAAA3");
			t_exp2=lexer.lit(TK_ELSE);
			expr2=parse_expr19();
			//System.out.println("AAAA4");
			assert(expr2!=null);
			//System.out.println("AAAA5");
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				if(t_exp2!=null)
					n=Expr.Or_Else;
				else
					n=Expr.Or;
				res=new Expr_Binaire(n,res,expr2,t_exp);
				t_exp=lexer.lit(TK_OR);
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		//System.out.println("AAAA2");
		return res;
	}

	// les operateurs binaires and, and then
	Expr parse_expr19()
	{
		Token t_exp,t_exp2,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		boolean erreur=false;
		int n;
		expr1=parse_expr20();
		res=expr1;
		t_exp=lexer.lit(TK_AND);
		while(t_exp!=null&&!erreur)
		{
			t_exp2=lexer.lit(TK_THEN);
			expr2=parse_expr20();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				if(t_exp2!=null)
					n=Expr.And_Then;
				else
					n=Expr.And;
				res=new Expr_Binaire(n,res,expr2,t_exp);
				t_exp=lexer.lit(TK_AND);
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs binaires =,/=,<,<=,>,>=
	Expr parse_expr20()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		int tab[]={Egal,Diff,InfS,Inf,SupS,Sup},n;
		boolean erreur=false;
		expr1=parse_expr21();
		res=expr1;
		n=lexer.test_prochain3(tab);
		while(n!=-1&&!erreur)
		{
			//System.out.println("OOO");
			t_exp=lexer.lit2();
			assert(t_exp!=null);
			expr2=parse_expr21();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				switch(t_exp.get_type_token())
				{
					case Egal:
						n=Expr.Egal;
						break;
					case Diff:
						n=Expr.Diff;
						break;
					case InfS:
						n=Expr.Infs;
						break;
					case Inf:
						n=Expr.Inf;
						break;
					case SupS:
						n=Expr.Sups;
						break;
					case Sup:
						n=Expr.Sup;
						break;
					default:
						assert(false);
				}
				res=new Expr_Binaire(n,res,expr2,t_exp);
				n=lexer.test_prochain3(tab);
				ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs binaires +,-
	Expr parse_expr21()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		int tab[]={Plus,Moins},n;
		boolean erreur=false;
		expr1=parse_expr22();
		res=expr1;
		n=lexer.test_prochain3(tab);
		while(n!=-1&&!erreur)
		{
			t_exp=lexer.lit2();
			assert(t_exp!=null);
			expr2=parse_expr22();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				switch(t_exp.get_type_token())
				{
					case Plus:
						n=Expr.Plus;
						break;
					case Moins:
						n=Expr.Moins;
						break;
					default:
						assert(false);
				}
				res=new Expr_Binaire(n,res,expr2,t_exp);
				n=lexer.test_prochain3(tab);
				/*if(n==-1)
				{
					erreur_parse("Test plus");
				}*/
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs binaires *,/,//,\\
	Expr parse_expr22()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		int tab[]={Fois,Div,DivEntier,Mod},n;
		boolean erreur=false;
		expr1=parse_expr23();
		res=expr1;
		n=lexer.test_prochain3(tab);
		while(n!=-1&&!erreur)
		{
			t_exp=lexer.lit2();
			assert(t_exp!=null);
			expr2=parse_expr23();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				switch(t_exp.get_type_token())
				{
					case Fois:
						n=Expr.Fois;
						break;
					case Div:
						n=Expr.Div;
						break;
					case DivEntier:
						n=Expr.Div_entier;
						break;
					case Mod:
						n=Expr.Mod;
						break;
					default:
						assert(false);
				}
				res=new Expr_Binaire(n,res,expr2,t_exp);
				n=lexer.test_prochain3(tab);
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// l'operateur puissance
	Expr parse_expr23()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		boolean erreur=false;
		expr1=parse_expr24();
		res=expr1;
		t_exp=lexer.lit(Puiss);
		if(t_exp!=null&&!erreur)
		{
			expr2=parse_expr23();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				res=new Expr_Binaire(Expr.Puiss,res,expr2,t_exp);
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs binaires libres
	Expr parse_expr24()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		boolean erreur=false;
		expr1=parse_expr24_2();
		res=expr1;
		t_exp=lexer.lit(FreeOp);
		while(t_exp!=null&&!erreur)
		{
			expr2=parse_expr24_2();
			if(expr2==null)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
			else
			{
				res=new Expr_Binaire(t_exp.text,res,expr2,t_exp);
				t_exp=lexer.lit(FreeOp);
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs unaires libres
	Expr parse_expr24_2()
	{
		Token t_exp,t_op,t_id;
		Expr expr1,expr2,res=null;
		Vector liste_op;
		boolean erreur=false;
		int n,i;
		liste_op=new Vector();
		t_op=lexer.lit(FreeOp);
		while(t_op!=null)
		{
			liste_op.addElement(t_op);
			t_op=lexer.lit(FreeOp);
		}
		expr1=parse_expr25();
		if(expr1!=null)
		{
			for(i=liste_op.size()-1;i>=0;i--)
			{
				t_op=(Token)liste_op.elementAt(i);
				expr1=new Expr_Unaire(t_op.text,expr1,t_op);
			}
			res=expr1;
		}
		else
		{
			if(liste_op.size()>0)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les operateurs unaires old,not,+,-,$
	Expr parse_expr25()
	{
		Token t_exp,t_op,t_id;
		Expr expr1,expr2,res=null;
		Vector liste_op;
		boolean erreur=false;
		int tab[]={TK_OLD,TK_NOT,Plus,Moins,Dollard},n,i;
		liste_op=new Vector();
		n=lexer.test_prochain3(tab);
		while(n!=-1&&!erreur)
		{
			t_op=lexer.lit2();
			liste_op.addElement(t_op);
			n=lexer.test_prochain3(tab);
		}
		expr1=parse_expr26();
		if(expr1!=null&&!erreur)
		{
			for(i=liste_op.size()-1;i>=0;i--)
			{
				t_op=(Token)liste_op.elementAt(i);
				switch(t_op.get_type_token())
				{
					case TK_OLD:
						n=Expr.Old;break;
					case TK_NOT:
						n=Expr.Not;break;
					case Plus:
						n=Expr.PlusU;break;
					case Moins:
						n=Expr.MoinsU;break;
					case Dollard:
						n=Expr.Dollard;
						ajoute_type(new TypeSimple(false,"POINTER",new Vector()));
						break;
					default:
						assert(false);
				}
				expr1=new Expr_Unaire(n,expr1,t_op);
			}
			res=expr1;
		}
		else
		{
			if(liste_op.size()>0)
			{
				erreur_parse("Il faut une expression");
				erreur=true;
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// le .
	Expr parse_expr26()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		boolean erreur=false;
		expr1=parse_expr27();
		res=expr1;
		if(res!=null)
		{
			t_exp=lexer.lit(Point);
			while(t_exp!=null&&!erreur)
			{
				expr2=parse_expr27();
				if(expr2==null)
				{
					erreur_parse("Il faut une expression");
					erreur=true;
				}
				else
				{
					res=new Expr_Binaire(Expr.Point,res,expr2,t_exp);
					t_exp=lexer.lit(Point);
				}
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}

	// les feuilles de l'expression (entier,chaine,variable,tableau,...)
	Expr parse_expr27()
	{
		Token t_exp,t_debut_tab,t_id;
		Expr expr1,expr2,res=null;
		Vector param;
		boolean erreur=false;
		int tab[][]={{ID,DoublePoint},{ID,Affect},{ID,TentativeAffect}};
		if(lexer.test_prochain4(tab)!=-1)
		{
			int tab2[]={ID};
			assert(lexer.test_prochain(tab2));
		}
		else
		{
			t_exp=lexer.lit2();
			if(t_exp==null)
			{
				
			}
			else
			{
				switch(t_exp.get_type_token())
				{
					case ID:
						//assert(false);
						t_id=t_exp;
						param=parse_param();
						if(param==null||param.size()==0)
							res=new Expr_Var(t_id.text,t_id);
						else
							res=new Expr_Appel(t_id.text,param,t_id);
						break;
					case INT:
						res=new Expr_Entier(t_exp.text,t_exp);
						ajoute_type(new TypeSimple(false,"INTEGER",new Vector()));
						break;
					case REAL:
						res=new Expr_Reel(t_exp.text,t_exp);
						ajoute_type(new TypeSimple(false,"REAL",new Vector()));
						break;
					case STRING:
						lexer.undo();
						Chaine chaine=parse_chaine();
						//erreur_parse("chaine");
						assert(chaine!=null);
						res=new Expr_Chaine(chaine);
						ajoute_type(new TypeSimple(false,"STRING",new Vector()));
						break;
					case CHAR:
						res=new Expr_Car(t_exp.text,t_exp);
						ajoute_type(new TypeSimple(false,"CHARACTER",new Vector()));
						break;
					case TK_TRUE:
						res=new Expr_Vrai(t_exp);
						ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
						break;
					case TK_FALSE:
						res=new Expr_Faux(t_exp);
						ajoute_type(new TypeSimple(false,"BOOLEAN",new Vector()));
						break;
					case ParentO:
						expr1=parse_expr();
						if(expr1==null)
						{
							erreur_parse("Il faut une expression");
							erreur=true;
						}
						else
						{
							t_exp=lexer.lit(ParentF);
							if(t_exp==null)
							{
								erreur_parse("Il faut )");
								erreur=true;
							}
							res=expr1;
						}
						break;
					case ArrayO:
						param=new Vector();
						t_debut_tab=t_exp;
						expr1=parse_expr();
						while(expr1!=null&&!erreur)
						{
							param.addElement(expr1);
							t_exp=lexer.lit(Virgule);
							if(t_exp==null)
							{
								expr1=null;
							}
							else
							{
								expr1=parse_expr();
							}
						}
						t_exp=lexer.lit(ArrayF);
						if(t_exp==null)
						{
							erreur_parse("Il faut >>");
							erreur=true;
						}
						res=new Expr_Tableau(param,t_debut_tab);
						ajoute_type(new TypeSimple(false,"ARRAY",new Vector()));
						break;
					default:
						//assert(false):"text="+t_exp.text+"("+t_exp.get_type_token()+")";
						lexer.undo();
				}
			}
		}	
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans l'expression");
		}
		return res;
	}


}
