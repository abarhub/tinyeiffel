package tinyeiffel.genere_c;

import java.io.*;
import java.util.*;
import tinyeiffel.compiler.*;
import tinyeiffel.ast.*;

public class GenC
{
	public GenC(Compiler_Eiffel compiler)
	{
		assert(compiler!=null);
		//PrintStreamTest p;
		PrintStream p;
		String nom="genere_c\\programme.c",nom2="genere_c\\programme.h";
		this.compiler=compiler;
		nom_classe_racine=compiler.get_configuration().classe_racine();
		nom_routine_racine=compiler.get_configuration().routine_racine();
		if(nom_routine_racine==null)
			nom_routine_racine="make";
		System.out.println("Generation du C :"+nom_classe_racine+"("+nom_routine_racine+")");
		classe_racine=cherche_classe(nom_classe_racine);
		assert(classe_racine!=null);
		System.out.println("classe="+classe_racine.nom);
		reference_struct();
		EClasse.affiche_temp_total();
		try
		{
			Date d1,d2;
			d1=new Date();
			long t;
			boolean genere1=true,genere2=true;
			if(genere1)
			{
			p=new PrintStream(new FileOutputStream(nom));
			//p=new PrintStreamTest(nom);
			//p.println("#ifndef _PROGRAMME_");
			//p.println("#define _PROGRAMME_");
			p.println("#include\"memoire.h\"");
			p.println("#include<stdio.h>");
			p.println("#include \"type_eiffel.h\"");
			p.println("#include \"programme.h\"");
			p.println("#include \"extern_fun.h\"");
			p.println();
			genere_struct(EToken,p);
			genere_struct(EPosition,p);
			genere_struct(ECharT,p);
			genere_struct(EType,p);
			genere_struct(ETypeT,p);
			genere_struct(EChaine,p);
			genere_struct(EChaineT,p);
			genere_struct(EIndexing,p);
			genere_struct(EIndexingT,p);
			genere_struct(EExpr,p);
			genere_struct(EExprT,p);
			genere_struct(EAssert,p);
			genere_struct(EAssertT,p);
			genere_struct(EInstr,p);
			genere_struct(EInstrT,p);
			genere_struct(ENomFeature,p);
			genere_struct(ENomFeatureT,p);
			genere_struct(EDeclareVar,p);
			genere_struct(EDeclareVarT,p);
			genere_struct(ECommentaire,p);
			genere_struct(ECommentaireT,p);
			genere_struct(EFeature,p);
			genere_struct(EFeatureT,p);
			genere_struct(EHeritage,p); // dernier test
			genere_struct(EHeritageT,p);
			genere_struct(ECreation,p);
			genere_struct(ECreationT,p);
			genere_struct(EAttrAncetre,p); // dernier
			genere_struct(EAttrAncetreT,p);
			genere_struct(EAttribut,p);
			genere_struct(EAttributT,p);
			genere_struct(EClasse,p);
			p.println();
			genere_heritage(p);
			p.println();
			p.println("const int nb_classe="+EClasse.size()+";");
			p.println("const char *nom_classe_racine=\""+nom_classe_racine+"\";");
			p.println("const char *nom_routine_racine=\""+nom_routine_racine+"\";");
			///	ETypeT,EChaineT/*,EIndexT*/,EExprT,
			// EAssertT,EInstrT,ENomFeatureT,
			// EDeclareVarT,ECommentaireT,EFeatureT,
			// EHeritageT,ECreationT,EIndexingT,ECharT
			//genere_struct(EType,p);
			p.println();
			/*p.println("struct TEIF_Classe programme[]={");
			genere_classe(classe_racine,p);
			p.println("}");*/
			genere_init(EInit,p);
			p.println();
			/*p.println("/*void main()");
			p.println("{");
			p.println("\tprintf(\"Hello world!\\n\");");
			p.println("}* /");
			p.println();*/
			//p.println("#endif");
			p.close();
			}
			if(genere2)
			{
			p=new PrintStream(new FileOutputStream(nom2));
			//p=new PrintStreamTest(nom2);
			p.println("#ifndef _PROGRAMME_");
			p.println("#define _PROGRAMME_");
			p.println("#include<stdio.h>");
			p.println("#include \"type_eiffel.h\"");
			p.println();
			p.println(EClasse.toString3());
			//p.println("#ifndef _NB_CLASSE_");
			//p.println("#ifndef _NB_CLASSE_");
			p.println("extern const int nb_classe;");
			p.println("extern const char *nom_classe_racine;");
			p.println("extern const char *nom_routine_racine;");
			genere_heritageh(p);
			p.println("void initialisation(void);");
			p.println();
			p.println("#endif");
			p.close();
			}
			d2=new Date();
			t=d2.getTime()-d1.getTime();
			System.out.println("Temp total genere c="+t+" ms");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Impossible de trouver le fichier "+nom+":\n"+e);
			e.printStackTrace();
			assert(false);
		}
		catch (SecurityException e)
		{
			System.out.println("Probl?me de s?curit? :\n"+e);
			e.printStackTrace();
			assert(false);
		}
		
	}

	public void reference_struct()
	{
		int i;
		EToken=new ListeStruct("global_token",false,"struct TEIF_Token");
		EPosition=new ListeStruct("global_position",false,"struct TEIF_Position");
		EType=new ListeStruct("global_type",false,"struct TEIF_Type");
		EChaine=new ListeStruct("global_chaine",false,"struct TEIF_Chaine");
		//EIndex=new ListeStruct("global_inde",false);
		EExpr=new ListeStruct("global_expr",false,"struct TEIF_Expr");
		EAssert=new ListeStruct("global_assert",false,"struct TEIF_Assert");
		EInstr=new ListeStruct("global_instr",false,"struct TEIF_Instr");
		ENomFeature=new ListeStruct("global_nomfeature",false,"struct TEIF_NomFeature");
		EDeclareVar=new ListeStruct("global_declarevar",false,"struct TEIF_DeclareVar");
		ECommentaire=new ListeStruct("global_commentaire",false,"struct TEIF_Commentaire");
		EFeature=new ListeStruct("global_feature",false,"struct TEIF_Feature");
		EHeritage=new ListeStruct("global_heritage",false,"struct TEIF_Heritage");
		ECreation=new ListeStruct("global_creation",false,"struct TEIF_Creation");
		EClasse=new ListeStruct("global_classe",false,"struct TEIF_Classe");
		EIndexing=new ListeStruct("global_indexing",false,"struct TEIF_Indexing");
		EAttribut=new ListeStruct("global_attribut",false,"struct TEIF_Attribut");
		EAttrAncetre=new ListeStruct("global_attrancetre",false,"struct TEIF_AttrAncetre");
		ETypeT=new ListeStruct("global_type_tab",true,"struct TEIF_Type");
		EChaineT=new ListeStruct("global_chaine_tab",true,"struct TEIF_Chaine");
		//EIndexT=new ListeStruct("global_");
		EExprT=new ListeStruct("global_expr_tab",true,"struct TEIF_Expr");
		EAssertT=new ListeStruct("global_assert_tab",true,"struct TEIF_Assert");
		EInstrT=new ListeStruct("global_instr_tab",true,"struct TEIF_Instr");
		ENomFeatureT=new ListeStruct("global_nomfeature_tab",true,"struct TEIF_NomFeature");
		EDeclareVarT=new ListeStruct("global_declarevar_tab",true,"struct TEIF_DeclareVar");
		ECommentaireT=new ListeStruct("global_commentaire_tab",true,"struct TEIF_Commentaire");
		EFeatureT=new ListeStruct("global_feature_tab",true,"struct TEIF_Feature");
		EHeritageT=new ListeStruct("global_heritage_tab",true,"struct TEIF_Heritage");
		ECreationT=new ListeStruct("global_creation_tab",true,"struct TEIF_Creation");
		EIndexingT=new ListeStruct("global_indexing_tab",true,"struct TEIF_Indexing");
		ECharT=new ListeStruct("global_char_tab",true,"char");
		EAttributT=new ListeStruct("global_attribut_tab",true,"struct TEIF_Attribut");
		EAttrAncetreT=new ListeStruct("global_attrancetre_tab",true,"struct TEIF_AttrAncetre");
		EInit=new Vector();
		liste_table=new Vector();
		
		construit_table_attr();
		reference_objet(classe_racine);
		for(i=0;i<compiler.liste_classe.size();i++)
		{
			Classe cl;
			cl=(Classe)compiler.liste_classe.elementAt(i);
			if(cl!=classe_racine)
			{
				reference_objet(cl);
			}
		}
	}

	public void construit_table_attr()
	{
		Classe classe;
		Table_Symbol table;
		Table tbl;
		int i,j;
		Attribut attr,attr2;
		Attribut_Complet ac;
		Attribut_Heritage ah;
		for(i=0;i<compiler.liste_classe.size();i++)
		{
			classe=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
			table=compiler.context.donne_table_symbol(classe);
			tbl=new Table(classe.nom);
			liste_table.addElement(tbl);
			if(table.liste_attribut!=null&&table.liste_attribut.length>0)
			{
				tbl.liste_attribut=new Attribut[table.liste_attribut.length];
				for(j=0;j<table.liste_attribut.length;j++)
				{
					ac=table.liste_attribut[j];
					attr=new Attribut(ac.nom,classe.type);
					attr.feature_directe=ac.feature_directe;
					attr.feature_reel=ac.feature_reel;
					tbl.liste_attribut[j]=attr;
				}
			}
			else
			{
				//assert(false):"classe:"+classe.nom;
			}
		}
		for(i=0;i<liste_table.size();i++)
		{
			classe=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
			table=compiler.context.donne_table_symbol(classe);
			tbl=(Table)liste_table.elementAt(i);
			assert(tbl!=null);
			assert(tbl.nom_classe.equalsIgnoreCase(classe.nom));
			if(tbl.liste_attribut!=null&&tbl.liste_attribut.length>0)
			{
				for(j=0;j<tbl.liste_attribut.length;j++)
				{
					ac=table.liste_attribut[j];
					attr=tbl.liste_attribut[j];
					if(ac.attribut_ancetre!=null)
					{
						for(int k=0;k<ac.attribut_ancetre.length;k++)
						{
							ah=ac.attribut_ancetre[k];
							attr2=donne_attribut(ah);
							if(!attr2.type.nom.equalsIgnoreCase(classe.nom))
							{// c'est pas le meme attribut
								AttrAncetre a1,a2;
								int no1=-1,m;
								if(classe.heritage!=null)
								{
									for(m=0;m<classe.heritage.length;m++)
									{
										if(ah.heritage==classe.heritage[m])
										{
											no1=m;
											break;
										}
									}
									assert(no1!=-1);
								}
								else
								{// heritage implicite vers ANY
									no1=0;
								}
								//a1=new AttrAncetre(attr.nom,classe.type,no1);
								attr2.ajoute_descendant(attr.nom,classe.type,no1);
								//a2=new AttrAncetre(attr2.nom,ah.heritage.type,no1);
								attr.ajoute_ancetre(attr2.nom,ah.heritage.type,no1);
							}
						}
					}
				}
			}
		}
	}

	public String convertie_chaine(String s)
	{// todo: convertir les \t en \\t, \n en \\n, etc...
		if(s==null)
		{
			return null;
		}
		else
		{
			StringBuffer s2=new StringBuffer();
			int i;
			char c;
			for(i=0;i<s.length();i++)
			{
				c=s.charAt(i);
				if(c=='\\')
					s2.append("\\\\");
				else
					s2.append(c);
			}
			return s2.toString();
		}
	}

	public Attribut donne_attribut(Attribut_Heritage ah)
	{
		assert(ah!=null);
		Table table;
		Attribut liste[];
		liste=trouve_attribut(ah.heritage.type.nom);
		if(liste==null)
			return null;
		for(int i=0;i<liste.length;i++)
		{
			if(liste[i].nom.meme_nom(ah.nom))
				return liste[i];
		}
		return null;
	}

	public int reference_objet(Object objet)
	{
		assert(objet!=null);
		int i,j,res,no,no2,no3,no4;
		String text;
		if(objet instanceof Classe)
		{
			Classe classe=(Classe)objet;
			Attribut attr[];
			Table table;
			res=EClasse.no_objet(classe);
			if(res==-1)
			{
				EClasse.ajoute(classe);
				text="";
				/*if(EClasse.size()>0)
				{
					Object o=EClasse.elementAt(0);
					text="/* 0)"+o+","+(o==classe)+","+
						EClasse.no_objet(o)+","+EClasse.no_objet(classe)+" *//* ";
				}*/
				if(classe.deferred)
					text+="1,";
				else
					text+="0,";
				if(classe.expanded)
					text+="1,";
				else
					text+="0,";
				if (classe.type!=null)
				{
					no=reference_objet(classe.type);
					text+="&(global_type["+no+"]),";
				}
				else
					text+="NULL,";
				text+=ajoute_feature(classe.feature)+",";
				text+=ajoute_heritage(classe.heritage)+",";
				text+=ajoute_assert(classe.invariant)+",";
				text+=ajoute_creation(classe.creation)+",";
				if(classe.obsolete!=null)
				{
					no=reference_objet(classe.obsolete);
					text+="&(global_chaine["+no+"]),";
				}
				else
					text+="NULL,";
				text+=ajoute_indexing(classe.index)+",";
				text+="NULL,";
				attr=trouve_attribut(classe.nom);
				//assert(attr!=null):"classe="+classe.nom;
				//assert(attr.length>0);
				text+=ajoute_attribut(attr)+",";
				text+=taille(classe.feature)+","+taille(classe.heritage)+
					","+taille(classe.invariant)+","+taille(classe.creation)+
					","+taille(classe.index)+",0";
				text+=","+taille(attr)+",";
				if(classe.tclass!=null)
				{
					no=reference_objet(classe.tclass);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(classe.texp_defer!=null)
				{
					no=reference_objet(classe.texp_defer);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(classe.tobsolete!=null)
				{
					no=reference_objet(classe.tobsolete);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(classe.tinvariant!=null)
				{
					no=reference_objet(classe.tinvariant);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(classe.tend!=null)
				{
					no=reference_objet(classe.tend);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				/*if(EClasse.size()>0)
				{
					Object o=EClasse.elementAt(0);
					text="/* 1)"+o+","+(o==classe)+","+
						EClasse.no_objet(o)+","+EClasse.no_objet(classe)+" *//* "+text;
				}
				text=" /*("+classe.nom+","+classe+")*/// "+text;
				res=EClasse.associe(classe,text);
			}
		}
		else if(objet instanceof Creation)
		{
			Creation creation=(Creation)objet;
			res=ECreation.no_objet(creation);
			if(res==-1)
			{
				text=ajoute_type(creation.liste_type)+",";
				text+=ajoute_nomfeature(creation.nom_fonction)+",";
				text+=taille(creation.liste_type)+","+taille(creation.nom_fonction)+",";
				if(creation.tcreation!=null)
				{
					no=reference_objet(creation.tcreation);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(creation.debut!=null)
				{
					no=reference_objet(creation.debut);
					text+="&(global_position["+no+"]),";
				}
				else
					text+="NULL,";
				if(creation.fin!=null)
				{
					no=reference_objet(creation.fin);
					text+="&(global_position["+no+"])";
				}
				else
					text+="NULL";
				res=ECreation.addElement(creation,text);
			}
		}
		else if(objet instanceof Heritage)
		{
			Heritage heritage=(Heritage)objet;
			res=EHeritage.no_objet(heritage);
			if(res==-1)
			{
				if(heritage.type!=null)
				{
					no=reference_objet(heritage.type);
					text="&(global_type["+no+"]),";
				}
				else
					text="NULL,";
				if(heritage.export!=null&&heritage.export.length>0)
				{// TODO: a revoir
					//ENomFeatureT.
					text+="NULL,";
				}
				else
					text+="NULL,";
				if(heritage.rename!=null&&heritage.rename.length>0)
				{// TODO: a revoir
					//ENomFeatureT.
					text+="NULL,";
				}
				else
					text+="NULL,";
				text+=ajoute_nomfeature(heritage.undefine)+",";
				text+=ajoute_nomfeature(heritage.redefine)+",";
				text+=ajoute_nomfeature(heritage.select)+",";
				text+=taille(heritage.export)+","+taille(heritage.rename)+
					","+taille(heritage.undefine)+","+taille(heritage.redefine)+
					","+taille(heritage.select)+",";
				if(heritage.therit!=null)
				{
					no=reference_objet(heritage.therit);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.trename!=null)
				{
					no=reference_objet(heritage.trename);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.texport!=null)
				{
					no=reference_objet(heritage.texport);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.tundefine!=null)
				{
					no=reference_objet(heritage.tundefine);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.tredefine!=null)
				{
					no=reference_objet(heritage.tredefine);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.tselect!=null)
				{
					no=reference_objet(heritage.tselect);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.tend!=null)
				{
					no=reference_objet(heritage.tend);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.debut!=null)
				{
					no=reference_objet(heritage.debut);
					text+="&(global_position["+no+"]),";
				}
				else
					text+="NULL,";
				if(heritage.fin!=null)
				{
					no=reference_objet(heritage.fin);
					text+="&(global_position["+no+"]),";
				}
				else
					text+="NULL";
				res=EHeritage.addElement(heritage,text);
			}
		}
		else if(objet instanceof Feature)
		{
			Feature feature=(Feature)objet;
			res=EFeature.no_objet(feature);
			if(res==-1)
			{
				if(feature instanceof FeatureAttr)
					text="TEIF_FeatureAttr,";
				else if(feature instanceof FeatureDeferred)
					text="TEIF_FeatureDeferred,";
				else if(feature instanceof FeatureExpr)
					text="TEIF_FeatureExpr,";
				else if(feature instanceof FeatureExternal)
					text="TEIF_FeatureExternal,";
				else if(feature instanceof FeatureRoutine)
					text="TEIF_FeatureRoutine,";
				else if(feature instanceof FeatureUnique)
					text="TEIF_FeatureUnique,";
				else
				{
					text="";
					assert(false);
				}
				text+=ajoute_nomfeature(feature.liste_nom)+",";
				text+=ajoute_declarevar(feature.param)+",";
				if(feature instanceof FeatureRoutine&&
					((FeatureRoutine)feature).local!=null)
				{
					text+=ajoute_declarevar(((FeatureRoutine)feature).local)+",";
				}
				else
				{
					text+="NULL,";
				}
				text+=ajoute_assert(feature.require)+",";
				text+=ajoute_assert(feature.ensure)+",";
				if(feature.type_retour!=null)
				{
					no=reference_objet(feature.type_retour);
					text+="&(global_type["+no+"]),";
				}
				else
					text+="NULL,";
				text+=ajoute_type(feature.export)+",";
				text+=ajoute_instr(feature.rescue)+",";
				if(feature instanceof FeatureRoutine&&
					((FeatureRoutine)feature).local!=null)
				{
					text+=ajoute_instr(((FeatureRoutine)feature).liste_instr)+",";
				}
				else
				{
					text+="NULL,";
				}
				text+=ajoute_commentaire(feature.commentaire)+",";
				text+=ajoute_commentaire(feature.commentaire2)+",";
				text+=ajoute_commentaire(feature.commentaire3)+",";
				if(feature.obsolete!=null)
				{
					no=reference_objet(feature.obsolete);
					text+="(global_chaine["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature instanceof FeatureExpr)
				{
					FeatureExpr f=(FeatureExpr)feature;
					text+="NULL,NULL,NULL,";
					if(f.expr!=null)
					{
						no=reference_objet(f.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
				}
				else if(feature instanceof FeatureExternal)
				{
					FeatureExternal f=(FeatureExternal)feature;
					if(f.str!=null)
					{
						no=reference_objet(f.str);
						text+="&(global_chaine["+no+"]),";
					}
					else
						text+="NULL,";
					if(f.alias!=null)
					{
						no=reference_objet(f.alias);
						text+="&(global_chaine["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,0,";
					if(f.classe!=null&&f.classe.nom!=null)
					{
						if(f.classe.nom.equalsIgnoreCase("integer"))
						{
							text+="fun_extern_int,";
						}
						else if(f.classe.nom.equalsIgnoreCase("boolean"))
						{
							text+="fun_extern_bool,";
						}
						else if(f.classe.nom.equalsIgnoreCase("character"))
						{
							text+="fun_extern_char,";
						}
						else if(f.classe.nom.equalsIgnoreCase("real"))
						{
							text+="fun_extern_real,";
						}
						else if(f.classe.nom.equalsIgnoreCase("double"))
						{
							text+="fun_extern_double,";
						}
						else if(f.classe.nom.equalsIgnoreCase("general"))
						{
							text+="fun_extern_general,";
						}
						else
						{
							text+="NULL/*extern*/,";
						}
					}
					else
					{// todo: prendre en compte les extern qui ne sont pas tinyeiffel
						text+="NULL/*extern*/,";
					}
				}
				else if(feature instanceof FeatureRoutine)
				{
					FeatureRoutine f=(FeatureRoutine)feature;
					text+="NULL,NULL,NULL,NULL,";
					if(f.once)
						text+="1,NULL,";
					else
						text+="0,NULL,";
				}
				else //if(feature instanceof FeatureAttr)
				{
					//FeatureAttr f=(FeatureAttr)feature;
					text+="NULL,NULL,NULL,NULL,0,NULL,";
				}
				text+=taille(feature.liste_nom)+",";
				/*if(feature instanceof FeatureRoutine&&
					((FeatureRoutine)feature).local!=null)
				{
					
				}*/
				text+=taille(feature.param)+",";
				if(feature instanceof FeatureRoutine&&
					((FeatureRoutine)feature).local!=null)
				{
					text+=taille(((FeatureRoutine)feature).local)+",";
				}
				else
				{
					text+="0,";
				}
				text+=taille(feature.require)+",";
				text+=taille(feature.ensure)+",";
				text+=taille(feature.rescue)+",";
				if(feature instanceof FeatureRoutine&&
					((FeatureRoutine)feature).local!=null)
				{
					text+=taille(((FeatureRoutine)feature).liste_instr)+",";
				}
				else
				{
					text+="0,";
				}
				text+=taille(feature.commentaire)+",";
				text+=taille(feature.commentaire2)+",";
				text+=taille(feature.commentaire3)+",";
				if(feature.tfeature!=null)
				{
					no=reference_objet(feature.tfeature);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.tobsolete!=null)
				{
					no=reference_objet(feature.tobsolete);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.trequire!=null)
				{
					no=reference_objet(feature.trequire);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.tensure!=null)
				{
					no=reference_objet(feature.tensure);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.trescue!=null)
				{
					no=reference_objet(feature.trescue);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.tunique!=null)
				{
					no=reference_objet(feature.tunique);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.tdeferred!=null)
				{
					no=reference_objet(feature.tdeferred);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.texternal!=null)
				{
					no=reference_objet(feature.texternal);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.talias!=null)
				{
					no=reference_objet(feature.talias);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(feature.tdo!=null)
				{
					no=reference_objet(feature.tdo);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				res=EFeature.addElement(feature,text);
				if(feature.classe!=null)
				{
					no=reference_objet(feature.classe);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_feature["+res+"].classe=&(global_classe["+no+"]);");
				}
			}
		}
		else if(objet instanceof Commentaire)
		{
			Commentaire commentaire=(Commentaire)objet;
			res=ECommentaire.no_objet(commentaire);
			if(res==-1)
			{
				text="\""+commentaire.str+"\",";
				if(commentaire.debut!=null)
				{
					no=reference_objet(commentaire.debut);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				res=ECommentaire.addElement(commentaire,text);
			}
		}
		else if(objet instanceof DeclareVar)
		{
			DeclareVar declare_var=(DeclareVar)objet;
			res=EDeclareVar.no_objet(declare_var);
			if(res==-1)
			{
				text="\""+declare_var.nom+"\",";
				if(declare_var.type!=null)
				{
					no=reference_objet(declare_var.type);
					text+="&(global_type["+no+"]),";
				}
				else
					text+="NULL,";
				if(declare_var.tnom!=null)
				{
					no=reference_objet(declare_var.tnom);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				res=EDeclareVar.addElement(declare_var,text);
			}
		}
		else if(objet instanceof NomFeature)
		{
			NomFeature nom_feature=(NomFeature)objet;
			res=ENomFeature.no_objet(nom_feature);
			if(res==-1)
			{
				if(nom_feature.nom!=null)
				{
					text="TEIF_Nom_Normal,0,0,";
					if(nom_feature.frozen)
						text+="1,";
					else
						text+="0,";
					text+="\""+nom_feature.nom+"\",NULL,";
				}
				else
				{
					text="TEIF_Nom_Operateur,";
					if(nom_feature.prefix)
						text+="1,";
					else
						text+="0,";
					if(nom_feature.infix)
						text+="1,";
					else
						text+="0,";
					if(nom_feature.frozen)
						text+="1,";
					else
						text+="0,";
					text+="NULL,";
					no=reference_objet(nom_feature.nom2);
					text+="&(global_chaine["+no+"]),";
				}
				if(nom_feature.tnom!=null)
				{
					no=reference_objet(nom_feature.tnom);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(nom_feature.tpre_in!=null)
				{
					no=reference_objet(nom_feature.tpre_in);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(nom_feature.tfrozen!=null)
				{
					no=reference_objet(nom_feature.tfrozen);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(nom_feature.debut!=null)
				{
					no=reference_objet(nom_feature.debut);
					text+="&(global_position["+no+"]),";
				}
				else
					text+="NULL,";
				if(nom_feature.fin!=null)
				{
					no=reference_objet(nom_feature.fin);
					text+="&(global_position["+no+"])";
				}
				else
					text+="NULL";
				res=ENomFeature.addElement(nom_feature,text);
			}
		}
		else if(objet instanceof Instr)
		{
			Instr instr=(Instr)objet;
			res=EInstr.no_objet(instr);
			if(res==-1)
			{
				if(instr instanceof Instr_Affect)
				{
					Instr_Affect ins=(Instr_Affect)instr;
					text="TEIF_Affect,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,";
					text+="\""+ins.nom+"\",";
					text+="\""+ins.var_current+"\",";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,0,0,0,0,0,0,";
					if(ins.tid!=null)
					{
						no=reference_objet(ins.tid);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tegal!=null)
					{
						no=reference_objet(ins.tegal);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tcurrent!=null)
					{
						no=reference_objet(ins.tcurrent);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Appel)
				{
					Instr_Appel ins=(Instr_Appel)instr;
					text="TEIF_Appel,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+=ajoute_expr(ins.parametre)+",";
					text+="NULL,NULL,";
					text+="\""+ins.nom+"\",";
					text+="NULL,NULL,";
					if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					text+=taille(ins.parametre);
					text+=",0,0,0,0,0,0,0,0,";
					if(ins.tid!=null)
					{
						no=reference_objet(ins.tid);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tpoint!=null)
					{
						no=reference_objet(ins.tpoint);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Check)
				{
					Instr_Check ins=(Instr_Check)instr;
					text="TEIF_Check,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,"+
						"NULL,NULL,NULL,NULL,NULL,NULL,";
					text+=ajoute_assert(ins.liste_expr)+",";
					text+="NULL,NULL,NULL,";
					text+="0,0,0,0,0,0,";
					text+=taille(ins.liste_expr)+",0,0";
					if(ins.tcheck!=null)
					{
						no=reference_objet(ins.tcheck);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tend!=null)
					{
						no=reference_objet(ins.tend);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Creation)
				{
					Instr_Creation ins=(Instr_Creation)instr;
					//if(ins.type!=null)
					//	reference_objet(ins.type);
					//ajoute(EExprT,ins.parametre);
					text="TEIF_Creation,";
					text+="NULL,";
					text+=ajoute_expr(ins.parametre)+",";
					text+="NULL,NULL,";
					text+="\""+ins.nom+"\",";
					text+="NULL,";
					if(ins.nom2!=null)
					{
						//System.io.println("nom2="+ins.nom2);
						text+="\""+ins.nom2+"\",";
					}
					else
					{
						//System.io.println("nom2=null");
						text+="NULL,";
					}
					/*if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"])";
					}
					else*/
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					if(ins.type!=null)
					{
						no=reference_objet(ins.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,";
					text+=taille(ins.parametre);
					text+=",0,0,0,0,0,0,0,0,";
					if(ins.texcl1!=null)
					{
						no=reference_objet(ins.texcl1);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.texcl2!=null)
					{
						no=reference_objet(ins.texcl2);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tid!=null)
					{
						no=reference_objet(ins.tid);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tid2!=null)
					{
						no=reference_objet(ins.tid2);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tpoint!=null)
					{
						no=reference_objet(ins.tpoint);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL";
				}
				else if(instr instanceof Instr_Debug)
				{
					Instr_Debug ins=(Instr_Debug)instr;
					text="TEIF_Debug,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					//text+=ajoute_instr(ins.instr)+",";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+=ajoute_chaine(ins.chaine)+",";
					text+="0,0,"+taille(ins.instr)+",0,0,0,0,0,"+taille(ins.chaine)+",";
					if(ins.tdebug!=null)
					{
						no=reference_objet(ins.tdebug);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tend!=null)
					{
						no=reference_objet(ins.tend);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Else)
				{
					Instr_Else ins=(Instr_Else)instr;
					text="TEIF_Else,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,";
					//text+=ajoute_instr(ins.liste_instr)+",";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,"+taille(ins.liste_instr)+",0,0,0,0,0,";
					if(ins.telse!=null)
					{
						no=reference_objet(ins.telse);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_ElseIf)
				{
					Instr_ElseIf ins=(Instr_ElseIf)instr;
					text="TEIF_ElseIf,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,";
					//text+=ajoute_instr(ins.liste_instr)+",";
					//text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,"+taille(ins.liste_instr)+",0,0,0,0,0,";
					if(ins.telseif!=null)
					{
						no=reference_objet(ins.telseif);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tthen!=null)
					{
						no=reference_objet(ins.tthen);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_If)
				{
					Instr_If ins=(Instr_If)instr;
					text="TEIF_If,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,";
					//text+=ajoute_instr(ins.liste_instr)+",";
					//text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,"+taille(ins.liste_instr)+",0,0,0,0,0,";
					if(ins.tif!=null)
					{
						no=reference_objet(ins.tif);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tthen!=null)
					{
						no=reference_objet(ins.tthen);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Inspect)
				{// TODO: penser au when
					Instr_Inspect ins=(Instr_Inspect)instr;
					text="TEIF_Inspect,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,";
					//text+=ajoute_instr(ins.liste_instr)+",";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,"+taille(ins.liste_instr)+",0,0,0,0,0,";
					if(ins.tinspect!=null)
					{
						no=reference_objet(ins.tinspect);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.twhen!=null)
					{
						no=reference_objet(ins.twhen);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tthen!=null)
					{
						no=reference_objet(ins.tthen);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Loop)
				{
					Instr_Loop ins=(Instr_Loop)instr;
					text="TEIF_Loop,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,";
					if(ins.variant!=null)
					{
						no=reference_objet(ins.variant);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					//text+=ajoute_instr(ins.from)+",";
					//text+=ajoute_instr(ins.loop)+",";
					text+="NULL,";
					text+="NULL,";
					text+="NULL,";
					text+=ajoute_assert(ins.invariant)+",";
					text+="NULL,NULL,";
					text+="0,0,0,0,"+taille(ins.from)+","+
						taille(ins.loop)+",0,"+taille(ins.invariant)+",0,";
					if(ins.tfrom!=null)
					{
						no=reference_objet(ins.tfrom);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tuntil!=null)
					{
						no=reference_objet(ins.tuntil);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tinvariant!=null)
					{
						no=reference_objet(ins.tinvariant);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tvariant!=null)
					{
						no=reference_objet(ins.tvariant);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tloop!=null)
					{
						no=reference_objet(ins.tloop);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					if(ins.tend!=null)
					{
						no=reference_objet(ins.tend);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(instr instanceof Instr_TentAffect)
				{
					Instr_TentAffect ins=(Instr_TentAffect)instr;
					text="TEIF_TentAffect,";
					if(ins.expr!=null)
					{
						no=reference_objet(ins.expr);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,";
					text+="\""+ins.nom+"\",";
					text+="\""+ins.var_current+"\",";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,0,0,0,0,0,0,";
					if(ins.tid!=null)
					{
						no=reference_objet(ins.tid);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
					if(ins.tegal!=null)
					{
						no=reference_objet(ins.tegal);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
					if(ins.tcurrent!=null)
					{
						no=reference_objet(ins.tcurrent);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
					text+="NULL,NULL,NULL";
				}
				else if(instr instanceof Instr_Retry)
				{
					Instr_Retry ins=(Instr_Retry)instr;
					text="TEIF_Retry,";
					text+="NULL,";
					text+="NULL,NULL,NULL,";
					text+="NULL,";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,0,0,0,0,0,0,0,";
					if(ins.tretry!=null)
					{
						no=reference_objet(ins.tretry);
						text+="&(global_token["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,NULL,NULL,NULL";
				}
				else
				{
					text="";
					assert(false);
				}
				res=EInstr.addElement(instr,text);
				if(instr instanceof Instr_Debug&&
					((Instr_Debug)instr).instr!=null&&
					((Instr_Debug)instr).instr.length>0)
				{
					Instr_Debug ins=(Instr_Debug)instr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_instr["+res+"].instr="+ajoute_instr(ins.instr)+";");
					//EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
				else if(instr instanceof Instr_Else&&
					((Instr_Else)instr).liste_instr!=null&&
					((Instr_Else)instr).liste_instr.length>0)
				{
					Instr_Else ins=(Instr_Else)instr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_instr["+res+"].liste_instr="+ajoute_instr(ins.liste_instr)+";");
					//EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
				else if(instr instanceof Instr_ElseIf&&
					((Instr_ElseIf)instr).liste_instr!=null&&
					((Instr_ElseIf)instr).liste_instr.length>0)
				{
					Instr_ElseIf ins=(Instr_ElseIf)instr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_instr["+res+"].liste_instr="+ajoute_instr(ins.liste_instr)+";");
					//EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
				else if(instr instanceof Instr_If&&
					((Instr_If)instr).liste_instr!=null&&
					((Instr_If)instr).liste_instr.length>0)
				{
					Instr_If ins=(Instr_If)instr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_instr["+res+"].liste_instr="+ajoute_instr(ins.liste_instr)+";");
					//EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
				else if(instr instanceof Instr_Inspect&&
					((Instr_Inspect)instr).liste_instr!=null&&
					((Instr_Inspect)instr).liste_instr.length>0)
				{
					Instr_Inspect ins=(Instr_Inspect)instr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_instr["+res+"].liste_instr="+ajoute_instr(ins.liste_instr)+";");
					//EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
				else if(instr instanceof Instr_Loop)
				{
					if(((Instr_Loop)instr).from!=null&&
					((Instr_Loop)instr).from.length>0)
					{
						Instr_Loop ins=(Instr_Loop)instr;
						EInit.addElement("global_instr["+res+"].from="+ajoute_instr(ins.from)+";");
					}
					if(((Instr_Loop)instr).loop!=null&&
					((Instr_Loop)instr).loop.length>0)
					{
						Instr_Loop ins=(Instr_Loop)instr;
						EInit.addElement("global_instr["+res+"].loop="+ajoute_instr(ins.loop)+";");
					}
				}
				/*else if(instr instanceof Instr_Creation)
				{
					if(((Instr_Creation)instr).getSuivant!=null)
					{
						Instr_Creation ins=(Instr_Creation)instr;
						//EInit.addElement("global_instr["+res+"].suivant="+ajoute_instr(ins.getSuivant())+";");
					}
					/*if(ins.getSuivant()!=null)
					{
						no=reference_objet(ins.getSuivant());
						text+="&(global_instr["+no+"])";
					}
					else* /
					//	text+="NULL,";
				}*/
			}
		}
		else if(objet instanceof Assert)
		{
			Assert assertion=(Assert)objet;
			res=EAssert.no_objet(assertion);
			if(res==-1)
			{
				if(assertion.nom!=null)
				{
					text="\""+assertion.nom+"\",";
				}
				else
					text="NULL,";
				if(assertion.expr!=null)
				{
					no=reference_objet(assertion.expr);
					text+="&(global_expr["+no+"])";
				}
				else
					text+="NULL,";
				res=EAssert.addElement(assertion,text);
			}
		}
		else if(objet instanceof Expr)
		{
			Expr expr=(Expr)objet;
			res=EExpr.no_objet(expr);
			if(res==-1)
			{
				if(expr instanceof Expr_Appel)
				{
					Expr_Appel exp=(Expr_Appel)expr;
					text="Appel,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="\""+exp.nom+"\",";
					text+="NULL,NULL,";
					/*if(expr.parametre!=null&&expr.parametre.length>0)
					{
						no=reference_objet(expr.parametre);
						//text+="&(global_expr";
						//no2=EType.size();
						EInit.addElement("global_expr["+res+"].tableau="+ajoute_expr(exp.parametre)+";");
						EInit.addElement("global_expr["+res+"].taille_tableau="+expr.parametre.length+";");
					}*/
					text+="NULL,";
					//text+=ajoute_expr(exp.parametre)+",";
					//EInit.addElement("global_expr["+res+"].parametre="+ajoute_expr(exp.parametre)+";");
					text+="NULL,NULL,NULL,";
					text+=taille(exp.parametre)+",0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Binaire)
				{
					Expr_Binaire exp=(Expr_Binaire)expr;
					switch(exp.op)
					{// TODO: a continuer
						case Expr.Plus:
							text="Plus,";
							break;
						case Expr.Moins:
							text="Moins,";
							break;
						case Expr.Fois:
							text="Fois,";
							break;
						case Expr.Div:
							text="Div,";
							break;
						case Expr.Div_entier:
							text="Div_entier,";
							break;
						case Expr.Mod:
							text="Mod,";
							break;
						case Expr.Point:
							text="Point,";
							break;
						case Expr.Puiss:
							text="Puiss,";
							break;
						case Expr.Xor:
							text="Xor,";
							break;
						case Expr.Or:
							text="Or,";
							break;
						case Expr.And:
							text="And,";
							break;
						case Expr.And_Then:
							text="And_Then,";
							break;
						case Expr.Or_Else:
							text="Or_Else,";
							break;
						case Expr.Implies:
							text="Implies,";
							break;
						case Expr.Egal:
							text="Egal,";
							break;
						case Expr.Diff:
							text="Diff,";
							break;
						case Expr.Infs:
							text="Infs,";
							break;
						case Expr.Inf:
							text="Inf,";
							break;
						case Expr.Sup:
							text="Sup,";
							break;
						case Expr.Sups:
							text="Sups,";
							break;
						case Expr.Free_Op:
							text="Free_Op,";
							break;
						default:
							text="";
							assert(false);
					}
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,";
					text+="\""+exp.free_op+"\",";
					text+="NULL,NULL,";
					if(exp.expr1!=null)
					{
						no=reference_objet(exp.expr1);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					if(exp.expr2!=null)
					{
						no=reference_objet(exp.expr2);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Car)
				{
					Expr_Car exp=(Expr_Car)expr;
					text="Char,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="\""+exp.car+"\",";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Chaine)
				{
					Expr_Chaine exp=(Expr_Chaine)expr;
					
					text="Chaine,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					if(exp.str2!=null)
					{
						no=reference_objet(exp.str2);
						text+="&(global_chaine["+no+"]),";
					}
					else
						text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Entier)
				{
					Expr_Entier exp=(Expr_Entier)expr;
					text="Entier,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="\""+exp.str+"\",";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Faux)
				{
					Expr_Faux exp=(Expr_Faux)expr;
					text="Faux,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Reel)
				{
					Expr_Reel exp=(Expr_Reel)expr;
					text="Reel,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="\""+exp.str+"\",";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Tableau)
				{
					Expr_Tableau exp=(Expr_Tableau)expr;
					text="Tableau,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="NULL,";
					text+="NULL,NULL,NULL,";
					text+=ajoute_expr(exp.tableau)+",";
					text+="0,"+taille(exp.tableau)+",";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Unaire)
				{
					Expr_Unaire exp=(Expr_Unaire)expr;
					switch(exp.op)
					{// TODO: a continuer
						case Expr.Old:
							text="Old,";
							break;
						case Expr.Not:
							text="Not,";
							break;
						case Expr.PlusU:
							text="PlusU,";
							break;
						case Expr.MoinsU:
							text="MoinsU,";
							break;
						case Expr.Free_OpU:
							text="Free_OpU,";
							break;
						case Expr.Dollard:
							text="Dollard,";
							break;
						default:
							text="";
							assert(false);
					}
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,";
					text+="\""+exp.free_op+"\",";
					text+="NULL,NULL,";
					if(exp.expr1!=null)
					{
						no=reference_objet(exp.expr1);
						text+="&(global_expr["+no+"]),";
					}
					else
						text+="NULL,";
					text+="NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Vrai)
				{
					Expr_Vrai exp=(Expr_Vrai)expr;
					text="Vrai,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					text+="NULL,NULL,";
					text+="NULL,";
					text+="NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else if(expr instanceof Expr_Var)
				{
					Expr_Var exp=(Expr_Var)expr;
					text="Var,";
					if(exp.type!=null)
					{
						no=reference_objet(exp.type);
						text+="&(global_type["+no+"]),";
					}
					else
						text+="NULL,";
					text+="0,NULL,";
					if(exp.classe!=null)
					{// TODO: a revoir
						
					}
					text+="\""+exp.nom+"\",";
					text+="NULL,NULL,NULL,NULL,NULL,NULL,";
					text+="0,0,";
					text+="NULL,";
					if(exp.oper!=null)
					{
						no=reference_objet(exp.oper);
						text+="&(global_token["+no+"])";
					}
					else
						text+="NULL";
				}
				else
				{
					text="";
					assert(false);
				}
				res=EExpr.addElement(expr,text);
				if(expr.classe!=null)
				{
					no=reference_objet(expr.classe);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_expr["+res+"].classe=&(global_classe["+no+"]);");
				}
				if(expr instanceof Expr_Appel&&
					((Expr_Appel)expr).parametre!=null&&
					((Expr_Appel)expr).parametre.size()>0)
				{
					Expr_Appel exp=(Expr_Appel)expr;
					//no=reference_objet(expr.parametre);
					//text+="&(global_expr";
					//no2=EType.size();
					EInit.addElement("global_expr["+res+"].parametre="+ajoute_expr(exp.parametre)+";");
					EInit.addElement("global_expr["+res+"].taille_tableau="+exp.parametre.size()+";");
				}
			}
		}
		else if(objet instanceof Indexing)
		{// TODO: a revoir
			Indexing index=(Indexing)objet;
			res=EIndexing.no_objet(index);
			if(res==-1)
			{
				if(index.nom!=null)
				{
					text="\""+index.nom+"\",";
				}
				else
					text="NULL,";
				text+=ajoute_char(index.liste)+",";
				text+=taille(index.liste);
				res=EIndexing.addElement(index,text);
			}
		}
		else if(objet instanceof Chaine)
		{// TODO: a revoir
			Chaine chaine=(Chaine)objet;
			res=EChaine.no_objet(chaine);
			if(res==-1)
			{
				text=ajoute_char(chaine.liste_chaine);
				text+=","+taille(chaine.liste_chaine)+",";
				if(chaine.oper!=null)
				{
					no=reference_objet(chaine.oper);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				res=EChaine.addElement(chaine,text);
			}
		}
		else if(objet instanceof Type)
		{
			Type type=(Type)objet;
			res=EType.no_objet(type);
			if(res==-1)
			{
				if(type.nom!=null)
				{
					text="\""+type.nom+"\",";
				}
				else
					text="NULL,";
				if(type.is_like)
					text+="1,";
				else
					text+="0,";
				if(type.is_from)
					text+="1,";
				else
					text+="0,";
				if(type.expanded)
					text+="1,";
				else
					text+="0,";
				text+=ajoute_type(type.generique)+",";
				text+=taille(type.generique)+",";
				if(type.like!=null)
				{
					no=reference_objet(type.like);
					//text+="&(global_expr";
					no2=EType.size();
					EInit.addElement("global_type["+no2+"].like=&(global_expr["+no+"]);");
				}
				//else
				text+="NULL,";
				if(type.tnom!=null)
				{
					no=reference_objet(type.tnom);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.tcrochet_ouvr!=null)
				{
					no=reference_objet(type.tcrochet_ouvr);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.tcrochet_ferm!=null)
				{
					no=reference_objet(type.tcrochet_ferm);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.tlike!=null)
				{
					no=reference_objet(type.tlike);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.tfleche!=null)
				{
					no=reference_objet(type.tfleche);
					text+="&(global_token["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.debut_!=null)
				{
					no=reference_objet(type.debut_);
					text+="&(global_position["+no+"]),";
				}
				else
					text+="NULL,";
				if(type.fin!=null)
				{
					no=reference_objet(type.fin);
					text+="&(global_token["+no+"])";
				}
				else
					text+="NULL";
				res=EType.addElement(type,text);
			}
			else
			{

			}
		}
		else if(objet instanceof AttrAncetre)
		{
			AttrAncetre attr=(AttrAncetre)objet;
			res=EAttrAncetre.no_objet(attr);
			if(res==-1)
			{
				if(attr.type!=null)
				{
					no=reference_objet(attr.type);
					text="&(global_type["+no+"]),";
				}
				else
					text="NULL,";
				if(attr.nom!=null)
				{
					no=reference_objet(attr.nom);
					text+="&(global_nomfeature["+no+"]),";
				}
				else
					text+="NULL,";
				text+=""+attr.no;
				res=EAttrAncetre.addElement(attr,text);
			}
		}
		else if(objet instanceof Attribut)
		{
			Attribut attr=(Attribut)objet;
			res=EAttribut.no_objet(attr);
			if(res==-1)
			{
				if(attr.nom!=null)
				{
					no=reference_objet(attr.nom);
					text="&(global_nomfeature["+no+"]),";
				}
				else
					text="NULL,";
				if(attr.type!=null)
				{
					no=reference_objet(attr.type);
					text+="&(global_type["+no+"]),";
				}
				else
					text+="NULL,";
				if(attr.feature_directe!=null)
				{
					no=reference_objet(attr.feature_directe);
					text+="&(global_feature["+no+"]),";
				}
				else
					text+="NULL,";
				if(attr.feature_reel!=null)
				{
					no=reference_objet(attr.feature_reel);
					text+="&(global_feature["+no+"]),";
				}
				else
					text+="NULL,";
				text+=ajoute_attrancetre(attr.ancetre)+",";
				text+=ajoute_attrancetre(attr.descendant);
				text+=","+taille(attr.ancetre)+","+taille(attr.descendant);
				res=EAttribut.addElement(attr,text);
			}
		}
		else if(objet instanceof Token)
		{
			Token token=(Token)objet;
			res=EToken.no_objet(token);
			if(res==-1)
			{
				text=token.x+","+token.y+",";
				if(token.text!=null)
				{
					if(token.text.charAt(0)=='\"')
						text+=convertie_chaine(token.text);
					else
						text+="\""+convertie_chaine(token.text)+"\"";
				}
				text+=",\""+convertie_chaine(token.file)+"\"";
				res=EToken.addElement(token,text);
			}
		}
		else if(objet instanceof Position)
		{
			Position pos=(Position)objet;
			res=EPosition.no_objet(pos);
			if(res==-1)
			{
				text=pos.x+","+pos.y;
				res=EPosition.addElement(pos,text);
			}
		}
		/*else if(objet instanceof String)
		{// TODO: a revoir
			Chaine chaine=(Chaine)objet;
			res=EChaine.no_objet(chaine);
			if(res==-1)
			{
				text=ajoute_char(chaine.liste_chaine);
				res=EChaine.addElement(chaine,text);
			}
		}*/
		else
		{
			res=-1;
			assert(false):"objet="+objet;
		}
		assert(res!=-1);
		return res;
	}

	/*public int trouve_objet(ListeStruct liste,Objet o)
	{
		assert(liste!=null);
		assert(o!=null);
		return liste.
	}*/

	public Attribut[] trouve_attribut(String nom)
	{
		assert(nom!=null);
		int i;
		for(i=0;i<liste_table.size();i++)
		{
			Table table=(Table)liste_table.elementAt(i);
			if(table.nom_classe.equalsIgnoreCase(nom))
			{
				//assert(table.liste_attribut!=null):table.nom_classe+"="+nom;
				return table.liste_attribut;
			}
		}
		assert(false):"nom="+nom;
		return null;
	}

	public int taille(Object o[])
	{
		if(o==null)
			return 0;
		else
			return o.length;
	}

	public int taille(Vector o)
	{
		if(o==null)
			return 0;
		else
			return o.size();
	}

	public String ajoute_type(Type tableau[])
	{
		return ajoute(ETypeT,tableau,"global_type");
	}

	public String ajoute_expr(Expr tableau[])
	{
		return ajoute(EExprT,tableau,"global_expr");
	}

	public String ajoute_expr(Vector tableau)
	{
		return ajoute(EExprT,tableau,"global_expr");
	}

	public String ajoute_assert(Assert tableau[])
	{
		return ajoute(EAssertT,tableau,"global_assert");
	}

	public String ajoute_chaine(Chaine tableau[])
	{
		return ajoute(EChaineT,tableau,"global_chaine");
	}

	public String ajoute_instr(Instr tableau[])
	{
		return ajoute(EInstrT,tableau,"global_instr");
	}

	public String ajoute_nomfeature(NomFeature tableau[])
	{
		return ajoute(ENomFeatureT,tableau,"global_nomfeature");
	}

	public String ajoute_declarevar(DeclareVar tableau[])
	{
		return ajoute(EDeclareVarT,tableau,"global_declarevar");
	}

	public String ajoute_commentaire(Commentaire tableau[])
	{
		return ajoute(ECommentaireT,tableau,"global_commentaire");
	}

	public String ajoute_feature(Feature tableau[])
	{
		return ajoute(EFeatureT,tableau,"global_feature");
	}

	public String ajoute_heritage(Heritage tableau[])
	{
		return ajoute(EHeritageT,tableau,"global_heritage");
	}

	public String ajoute_creation(Creation tableau[])
	{
		return ajoute(ECreationT,tableau,"global_creation");
	}

	public String ajoute_indexing(Indexing tableau[])
	{
		return ajoute(EIndexingT,tableau,"global_indexing");
	}

	public String ajoute_attribut(Attribut tableau[])
	{
		return ajoute(EAttributT,tableau,"global_attribut");
	}

	public String ajoute_attrancetre(AttrAncetre tableau[])
	{
		return ajoute(EAttrAncetreT,tableau,"global_attrancetre");
	}

	public String ajoute_char(String tableau[])
	{
		return ajoute(ECharT,tableau,"global_char");
	}

	public String ajoute(ListeStruct liste,Vector tableau,String nom)
	{
		assert(liste!=null);
		if(tableau!=null&&tableau.size()>0)
		{
			Expr tab[];
			tab=new Expr[tableau.size()];
			tableau.copyInto(tab);
			return ajoute(liste,tab,nom);
		}
		else
			return "NULL";
	}

	public String ajoute(ListeStruct liste,Object tableau[],String nom)
	{
		assert(liste!=null);
		String res;
		if(tableau!=null&&tableau.length>0)
		{
			int i,no;
			res="";
			for(i=0;i<tableau.length;i++)
			{
				if(i>0)
					res+=",";
				if(tableau[i] instanceof String)
				{
					res+=tableau[i];
				}
				else
				{
					no=reference_objet(tableau[i]);
					res+="&("+nom+"["+no+"])";
				}
			}
			no=liste.addElement(tableau,res);
			return "&("+nom+"_tab"+no+")";
		}
		else
			return "NULL";
	}

	public Compiler_Eiffel compiler;
	public String nom_classe_racine,nom_routine_racine;
	public Classe classe_racine;
	public ListeStruct EType,EChaine,//EIndex,
		EExpr,EAssert,EInstr,ENomFeature,
		EDeclareVar,ECommentaire,EFeature,
		EHeritage,ECreation,EClasse,EIndexing,
		EAttribut,EAttrAncetre,EToken,EPosition;
	public ListeStruct ETypeT,EChaineT/*,EIndexT*/,EExprT,
		EAssertT,EInstrT,ENomFeatureT,
		EDeclareVarT,ECommentaireT,EFeatureT,
		EHeritageT,ECreationT,EIndexingT,ECharT,
		EAttributT,EAttrAncetreT;
	public Vector EInit;
	public Vector liste_table;

	public void genere_classe(Classe cl,PrintStream out)
	{
		assert(cl!=null);
		assert(out!=null);
		Type type;
		out.print("{");
		out.println("//Classe:"+cl.nom);
		type=cl.type;
		if(cl.deferred)
			out.print("1,");
		else
			out.print("0,");
		if(cl.expanded)
			out.print("1,");
		else
			out.print("0,");
		genere_type(cl.type,out);
		out.print(",");
		out.print("NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0");
		out.println("}");
	}

	public void genere_type(Type type,PrintStream out)
	{
		assert(type!=null);
		assert(out!=null);
		out.print("{");
		if(type.is_like)
		{
			out.print("NULL,1,1,0,NULL,0,");
			genere_expr(type.like,out);
		}
		else
		{
			genere_str(type.nom,out);
			out.print(",0,0,");
			if(type.expanded)
				out.print("1,");
			else
				out.print("0,");
			if(type.generique!=null&&type.generique.length>0)
			{
				out.print("{");
				for(int i=0;i<type.generique.length;i++)
				{
					if(i>0)
						out.print(",");
					genere_type(type.generique[i],out);
				}
				out.print("},"+type.generique.length);
			}
			else
			{
				out.print("NULL,0");
			}
			out.print(",NULL");
		}
		out.print("}");
	}
	
	public void genere_str(String str,PrintStream out)
	{
		assert(str!=null);
		assert(out!=null);
		out.print("\""+str+"\"");
	}

	public void genere_expr(Expr expr,PrintStream out)
	{
		assert(expr!=null);
		assert(out!=null);
		out.println("NULL");
	}

	public void genere_init(Vector init,PrintStream out)
	{
		assert(init!=null);
		assert(out!=null);
		int i;
		String s;
		if(init.size()>0)
		{
			out.println("void initialisation()");
			out.println("{");
			for(i=0;i<init.size();i++)
			{
				s=(String)init.elementAt(i);
				out.println("\t"+s);
			}
			out.println("}");
		}
	}

	public void genere_struct(ListeStruct liste,PrintStream out)
	{
		assert(out!=null);
		assert(liste!=null);
		String s;
		if(!liste.est_tableau())
			s=liste.toString1();
		else
			s=liste.toString2();
		if(!s.equals(""))
		{
			//out.println();
			out.println(s);
			out.println();
		}
	}

	public void genere_heritageh(PrintStream out)
	{
		assert(out!=null);
		boolean heritage_directe[][],heritage[][];
		int len;
		heritage_directe=compiler.table_heritage_directe;
		heritage=compiler.table_heritage;
		len=heritage.length;
		out.println("extern TEIF_Bool heritage_directe["+len+"]["+len+"];");
		out.println("extern TEIF_Bool heritage["+len+"]["+len+"];");
	}

	public void genere_heritage(PrintStream out)
	{
		assert(out!=null);
		boolean heritage_directe[][],heritage[][];
		heritage_directe=compiler.table_heritage_directe;
		heritage=compiler.table_heritage;
		genere_matrice(out,"TEIF_Bool","heritage_directe",heritage_directe);
		genere_matrice(out,"TEIF_Bool","heritage",heritage);
	}

	public void genere_matrice(PrintStream out,
		String type,String nom,boolean m[][])
	{
		assert(out!=null);
		assert(type!=null);
		assert(nom!=null);
		assert(m!=null);
		int i,j,len;
		len=m.length;
		out.println(type+" "+nom+"["+len+"]["+len+"]={");
		for(i=0;i<len;i++)
		{
			if(i>0)
				out.println(",");
			out.print("{");
			for(j=0;j<len;j++)
			{
				if(j>0)
					out.print(",");
				if(m[i][j])
					out.print("1");
				else
					out.print("0");
			}
			out.print("}");
		}
		out.println("};");
		out.println();
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
				cl=((tinyeiffel.ast.Classe)v.elementAt(i));
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


}