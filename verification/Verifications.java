/*
 * Created on 23 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import tinyeiffel.ast.*;
import tinyeiffel.compiler.Compiler_Eiffel;
import tinyeiffel.compiler.Logging;
import tinyeiffel.erreur.*;
import tinyeiffel.verification.type_verif.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Verifications {

	public Type nom_classe,generique[];
	Heritage liste_heritage[];
	public Vector liste_verifications;
	Map liste_attribut;
	Logging logging;
	Logger profiler;
	protected int no_var_type;
	
	public Verifications(Type classe,Heritage heritage[],
			Logging logging)
	{
		assert(classe!=null);
		this.nom_classe=classe;
		liste_verifications=new Vector();
		this.generique=classe.generique;
		this.liste_heritage=heritage;
		liste_attribut=new HashMap();
		this.logging=logging;
		//this.profiler=profiler;
		this.profiler=Logger.getLogger("tinyeiffel.profiler.verification");
		this.profiler.setLevel(Level.OFF);
		no_var_type=0;
	}

	public String nom_classe()
	{
		return nom_classe.nom;
	}
	
	public void ajoute_attribut(NomFeature nom,DeclareVar param[],
			Type type_retour,boolean est_once)
	{
		VerifAttribut attr;
		attr=new VerifAttribut(nom,param,type_retour,est_once);
		liste_attribut.put(nom.toString(),attr);
	}
	

	public void ajoute_attribut(Feature f)
	{
		VerifAttribut attr;
		NomFeature nom,nom2;
		DeclareVar param[];
		Type type_retour;
		boolean est_once;
		int i;
		assert(f!=null);
		param=f.param;
		if(f instanceof FeatureRoutine&&
				((FeatureRoutine)f).once)
		{
			est_once=true;
		}
		else
		{
			est_once=false;
		}
		type_retour=f.type_retour;
		// verification si les attriobuts existent et
		// on les ajoutent
		for(i=0;i<f.liste_nom.length;i++)
		{
			nom=f.liste_nom[i];
			attr=cherche_attribut(nom);
			if(attr!=null)
			{
				Attribut attr1,attr2;
				attr1=new Attribut(nom,f.classe,f);
				attr2=new Attribut(attr.nom,f.classe,f);
				logging.erreur(new ErreurVFFD2(attr1,attr2));
			}
			else
			{
				attr=new VerifAttribut(nom,param,type_retour,est_once);
				liste_attribut.put(nom.toString(),attr);
			}
		}
		// verifications internes a la fetaure et
		// generation des tests hors de la feature
		verif_attribut(f);
	}
	
	public void verif_attribut(Feature f)
	{
		NomFeature nom,nom2;
		int i,j;
		DeclareVar d,liste_var0[];
		Map liste_var;
		boolean est_infix=false,est_prefix=false,est_frozen=false;
		NomFeature nom_prefix=null,nom_infix=null,nom_frozen=null;
		assert(f!=null);
		// vérification que les noms de la feature sont différents
		for(i=0;i<f.liste_nom.length;i++)
		{
			nom=f.liste_nom[i];
			for(j=0;j<i;j++)
			{
				nom2=f.liste_nom[j];
				if(nom.equals(nom2))
				{
					Attribut attr1,attr2;
					attr1=new Attribut(nom,f.classe,f);
					attr2=new Attribut(nom2,f.classe,f);
					logging.erreur(new ErreurVFFD2(attr1,attr2));
				}
			}
			if(!est_infix&&nom.infix)
			{
				est_infix=true;
				nom_infix=nom;
			}
			if(!est_prefix&&nom.prefix)
			{
				est_prefix=true;
				nom_prefix=nom;
			}
			if(!est_frozen&&nom.frozen)
			{
				est_frozen=true;
				nom_frozen=nom;
			}
			//verif_attribut_existe_pas(nom_classe.nom,nom,nom.debut);
		}
		if((f instanceof FeatureDeferred)&&!f.classe.deferred)
		{
			logging.erreur(new ErreurVCCH1(f.classe,f));
		}
		if(!(f instanceof FeatureDeferred)&&f.classe.deferred)
		{
			logging.erreur(new ErreurVCCH2(f.classe));
		}
		liste_var=new HashMap();
		if(f.param!=null&&f.param.length>0)
		{
			verifie_declare_var(f, f.param, liste_var,false);
		}
		if(est_frozen&&(f instanceof FeatureDeferred))
		{// erreur VFFD4
			Attribut attr;
			attr=new Attribut(nom_frozen,f.classe,f);
			logging.erreur(new ErreurVFFD4(attr));
		}
		if(est_prefix&&((f.param!=null&&f.param.length>0)||f.type_retour==null))
		{// Erreur VFFD5
			logging.erreur(new ErreurVFFD5(f.classe,nom_prefix,f));
		}
		if(est_infix&&(f.param==null||f.param.length!=1||f.type_retour==null))
		{// erreur VFFD6
			logging.erreur(new ErreurVFFD6(f.classe,nom_infix,f));
		}
		if(f.type_retour!=null)
		{
			DeclareVar d2;
			if(f.type_retour.is_like)
			{
				if(f instanceof FeatureRoutine&&((FeatureRoutine)f).once)
				{
					logging.erreur(new ErreurVFFD7(f.classe,f.liste_nom[0],f));
				}
			}
			else
			{
				verif_classe_existe(f.type_retour.nom,
					f.type_retour.debut());
			}
			d2=new DeclareVar("Result",f.type_retour);
			if(!liste_var.containsKey("Result"))
			{
				liste_var.put("Result",d2);
			}
			else
			{// TODO: Conflit Result et les parametres
				assert(false);
			}
		}
		if(f instanceof FeatureRoutine)
		{
			FeatureRoutine fr=(FeatureRoutine)f;
			if(fr.local!=null)
			{
				verifie_declare_var(f, fr.local, liste_var,true);
			}
			verifie_liste_instruction(fr.liste_instr,liste_var);
		}
		else if(f instanceof FeatureExpr)
		{
			FeatureExpr fe=(FeatureExpr)f;
			VerifType type_expr,type;
			if(fe.type_retour==null)
			{
				logging.erreur(new ErreurVQMC(nom_classe,f));
			}
			else
			{
				type=new VerifType(donne_var_type(),fe.type_retour);
				type_expr=verifie_expr(type_courant(),fe.expr,new HashMap());
				verif_type_conforme(type,type_expr,nom_classe);
			}
		}
		else if(f instanceof FeatureUnique)
		{
			
		}
	}
	
	/**
	 * @param liste_instr
	 * @param liste_var
	 */
	private void verifie_liste_instruction(Instr liste_instr[], Map liste_var) {
		int i;
		if(liste_instr!=null)
		{
			profiler.info("Debut Verif Instruction ..."+mem_info());
			for(i=0;i<liste_instr.length;i++)
			{
				profiler.info("Verif Instruction "+i+":"+liste_instr[i].debut()+" ..."+mem_info());
				verifie_instruction(liste_instr[i], liste_var);
				profiler.info("Verif Instruction fait "+mem_info());
			}
			profiler.info("Fin Verif Instruction "+mem_info());
		}
	}

	/**
	 * @return
	 */
	private String mem_info() {
		// TODO Auto-generated method stub
		return Compiler_Eiffel.info_mem();
	}

	/**
	 * @param instr
	 * @param liste_var
	 */
	private void verifie_instruction(Instr instr, Map liste_var) {
		assert(instr!=null);
		assert(liste_var!=null);
		if(instr instanceof Instr_Affect)
		{// Affectation
			Instr_Affect ins=(Instr_Affect)instr;
			VerifAttribut va;
			NomFeature nom;
			Type type_cible;
			VerifType type,type_expr,type_courant;
			DeclareVar d;
			VerifTypeConforme vc;
			profiler.info("Verif Affect "+mem_info());
			/*nom=new NomFeature(ins.nom);
			va=cherche_attribut(nom);
			if(va!=null)*/
			if(liste_var.containsKey(ins.nom))
			{
				//type_cible=
				d=(DeclareVar)liste_var.get(ins.nom);
				assert(d!=null);
				type=new VerifType(donne_var_type(),d.type);
			}
			else
			{
				VerifMethode vm;
				VerifType classe;
				NomFeature nom_source;
				classe=new VerifType(donne_var_type(),nom_classe);
				nom_source=new NomFeature(ins.nom);
				type=new VerifType(donne_var_type());
				/*vm=new VerifMethode(classe,nom_source,
						null,type,ins.tid.debut());
				liste_verifications.addElement(vm);*/
				verif_methode(classe,nom_source,
						null,type,ins.tid.debut());
			}
			type_expr=verifie_expr(type_courant(),ins.expr,liste_var);
			//vc=new VerifTypeConforme(type_courant(),type_expr,nom_classe);
			//liste_verifications.addElement(vc);
			verif_type_conforme(type,type_expr,nom_classe);
		}
		else if(instr instanceof Instr_Appel)
		{
			Instr_Appel ins=(Instr_Appel)instr;
			VerifType vt,vt2,param[],type_retour;
			VerifMethode vm;
			profiler.info("Verif Appel "+ins.debut()+":"+mem_info());
			vt=type_courant();
			type_retour=null;
			while(ins!=null)
			{
				if(ins.expr!=null)
				{
					type_retour=verifie_expr(type_courant(),ins.expr,liste_var);
					vt=type_retour;
				}
				else
				{
					if(ins.parametre!=null&&ins.parametre.length>0)
					{
						int j;
						param=new VerifType[ins.parametre.length];
						for(j=0;j<param.length;j++)
						{
							vt2=verifie_expr(type_courant(),ins.parametre[j],liste_var);
							param[j]=new VerifType(donne_var_type());
							verif_type_conforme(param[j],vt2,type_courant().get_type_reel());
						}
					}
					else
					{
						param=null;
					}
					type_retour=new VerifType(donne_var_type());
					verif_methode(vt,new NomFeature(ins.nom),
						param,type_retour,ins.debut());
					vt=type_retour;
				}
				if(ins.getSuivant()!=null)
				{
					ins=(Instr_Appel)ins.getSuivant();
				}
				else
				{
					ins=null;
				}
			}
			verif_pas_type(type_retour,type_courant().get_type_reel());
		}
		else if(instr instanceof Instr_Check)
		{
			Instr_Check ins=(Instr_Check)instr;
			profiler.info("Verif Check "+mem_info());
			if(ins.liste_expr!=null&&ins.liste_expr.length>0)
			{
				int j;
				VerifType type_expr;
				Assert a;
				for(j=0;j<ins.liste_expr.length;j++)
				{
					a=ins.liste_expr[j];
					if(a.expr!=null)
					{
						type_expr=verifie_expr(type_courant(),a.expr,liste_var);
						verif_type_conforme(type_boolean(),type_expr,nom_classe);
					}
				}
			}
			
		}
		else if(instr instanceof Instr_Creation)
		{// TODO: a terminer
			Instr_Creation ins=(Instr_Creation)instr;
			VerifAttribut va;
			NomFeature nom;
			Type type_cible;
			VerifType type,type_expr,type_courant,type_creation;
			DeclareVar d;
			VerifTypeConforme vc;
			profiler.info("Verif Creation "+mem_info());
			if(liste_var.containsKey(ins.nom))
			{
				//type_cible=
				d=(DeclareVar)liste_var.get(ins.nom);
				assert(d!=null);
				type=new VerifType(donne_var_type(),d.type);
			}
			else
			{
				VerifType classe;
				NomFeature nom_source;
				classe=new VerifType(donne_var_type(),nom_classe);
				nom_source=new NomFeature(ins.nom);
				type=new VerifType(donne_var_type());
				verif_methode(classe,nom_source,
						null,type,ins.tid.debut());
			}
			if(ins.type!=null)
			{
				type_creation=new VerifType(donne_var_type(),ins.type);
				verif_type_conforme(type,type_creation,nom_classe);
			}
			if(ins.nom2!=null)
			{
				VerifType param[],vt;
				if(ins.parametre!=null&&ins.parametre.length>0)
				{
					int j;
					param=new VerifType[ins.parametre.length];
					for(j=0;j<param.length;j++)
					{
						vt=verifie_expr(type,ins.parametre[j],liste_var);
						param[j]=new VerifType(donne_var_type());
						verif_type_conforme(param[j],vt,type_courant().get_type_reel());
					}
				}
				else
				{
					param=null;
				}
				verif_methode(type,new NomFeature(ins.nom),
					param,null,ins.debut());
				verif_methode_creation(type,new NomFeature(ins.nom),ins.debut());
			}
		}
		else if(instr instanceof Instr_Debug)
		{
			Instr_Debug ins=(Instr_Debug)instr;
			profiler.info("Verif Debug "+mem_info());
			if(ins.instr!=null&&ins.instr.length>0)
			{
				verifie_liste_instruction(ins.instr,liste_var);
			}
		}
		else if(instr instanceof Instr_If)
		{
			Instr_If ins=(Instr_If)instr;
			VerifType type_expr;
			Instr_ElseIf ins2;
			Instr_Else ins3;
			assert(ins.expr!=null);
			profiler.info("Verif If "+mem_info());
			type_expr=verifie_expr(type_courant(),ins.expr,liste_var);
			verif_type_conforme(type_boolean(),type_expr,nom_classe);
			if(ins.liste_instr!=null&&ins.liste_instr.length>0)
			{
				verifie_liste_instruction(ins.liste_instr,liste_var);
			}
			if(ins.getSuivant()!=null)
			{
				profiler.info("Verif If suivant "+mem_info());
				if(ins.getSuivant() instanceof Instr_ElseIf)
				{
					profiler.info("Verif ElseIf "+mem_info());
					ins2=(Instr_ElseIf)ins.getSuivant();
					ins3=null;
					while(ins2!=null)
					{
						profiler.info("Verif ElseIf boucle debut "+mem_info());
						assert(ins2.expr!=null);
						type_expr=verifie_expr(type_courant(),ins2.expr,liste_var);
						verif_type_conforme(type_boolean(),type_expr,nom_classe);
						if(ins2.liste_instr!=null&&ins2.liste_instr.length>0)
						{
							verifie_liste_instruction(ins2.liste_instr,liste_var);
						}
						if(ins2.getSuivant()!=null)
						{
							if(ins2.getSuivant() instanceof Instr_ElseIf)
							{
								ins2=(Instr_ElseIf)ins2.getSuivant();
							}
							else
							{
								ins3=(Instr_Else)ins2.getSuivant();
								ins2=null;
							}
						}
						else
						{
							ins2=null;
							ins3=null;
						}
						profiler.info("Verif ElseIf boucle fin "+mem_info());
					}
				}
				else
				{
					ins3=(Instr_Else)ins.getSuivant();
				}
				if(ins3!=null)
				{
					profiler.info("Verif Else "+mem_info());
					if(ins3.liste_instr!=null&&ins3.liste_instr.length>0)
					{
						verifie_liste_instruction(ins3.liste_instr,liste_var);
					}
				}
			}
			profiler.info("Fin Verif If "+mem_info());
		}
		else if(instr instanceof Instr_Inspect)
		{// TODO: a terminer
			Instr_Inspect ins=(Instr_Inspect)instr;
			VerifType type_expr;
			Vector liste_cas;
			Position debut;
			profiler.info("Verif Inspect "+mem_info());
			debut=ins.debut();
			liste_cas=new Vector();
			type_expr=verifie_expr(type_courant(),ins.expr,liste_var);
			while(ins!=null)
			{
				// TODO: voir les cas
				if(ins.when!=null)
				{
					liste_cas.addAll(ins.when);
				}
				verifie_liste_instruction(ins.liste_instr,liste_var);
				if(ins.getSuivant()!=null)
				{
					ins=(Instr_Inspect)ins.getSuivant();
				}
				else
				{
					ins=null;
				}
			}
			// verifier conformité du type de la variable avec les cas
			verif_inspect(type_expr,liste_cas,debut);
			//verif_type_conforme(type_integer(),type_expr,nom_classe);
			//verif_type_conforme(type_character(),type_expr,nom_classe);
		}
		else if(instr instanceof Instr_Loop)
		{
			Instr_Loop ins=(Instr_Loop)instr;
			VerifType type_expr;
			profiler.info("Verif Loop "+mem_info());
			if(ins.from!=null&&ins.from.length>0)
			{
				verifie_liste_instruction(ins.from,liste_var);
			}
			assert(ins.expr!=null);
			type_expr=verifie_expr(type_courant(),ins.expr,liste_var);
			verif_type_conforme(type_boolean(),type_expr,nom_classe);
			if(ins.variant!=null)
			{
				type_expr=verifie_expr(type_courant(),ins.variant,liste_var);
				verif_type_conforme(type_integer(),type_expr,nom_classe);
			}
			if(ins.invariant!=null&&ins.invariant.length>0)
			{
				int j;
				Assert a;
				for(j=0;j<ins.invariant.length;j++)
				{
					a=ins.invariant[j];
					if(a.expr!=null)
					{
						type_expr=verifie_expr(type_courant(),a.expr,liste_var);
						verif_type_conforme(type_boolean(),type_expr,nom_classe);
					}
				}
			}
			if(ins.loop!=null&&ins.loop.length>0)
			{
				verifie_liste_instruction(ins.loop,liste_var);
			}
		}
		else if(instr instanceof Instr_Retry)
		{
			Instr_Retry ins=(Instr_Retry)instr;
			profiler.info("Verif Retry "+mem_info());
		}
		else if(instr instanceof Instr_TentAffect)
		{
			Instr_TentAffect ins=(Instr_TentAffect)instr;
			VerifAttribut va;
			NomFeature nom;
			Type type_cible;
			VerifType type,type_expr,type_courant;
			DeclareVar d;
			VerifTypeConforme vc;
			profiler.info("Verif Tentative affectation "+mem_info());
			if(liste_var.containsKey(ins.nom))
			{
				profiler.info("Var existe "+mem_info());
				d=(DeclareVar)liste_var.get(ins.nom);
				assert(d!=null);
				type=new VerifType(donne_var_type(),d.type);
			}
			else
			{
				VerifMethode vm;
				VerifType classe;
				NomFeature nom_source;
				profiler.info("Var n'existe pas "+mem_info());
				classe=new VerifType(donne_var_type(),nom_classe);
				nom_source=new NomFeature(ins.nom);
				type=new VerifType(donne_var_type());
				vm=new VerifMethode(classe,nom_source,
						null,type,ins.tid.debut());
				liste_verifications.addElement(vm);
			}
			profiler.info("Verif expression "+mem_info());
			type_expr=verifie_expr(type_courant(),ins.expr,liste_var);
			profiler.info("Fin Verif Tentative affectation "+mem_info());
		}
		else
		{
			assert(false);
		}
	}

	public VerifType type_courant()
	{
		return new VerifType(donne_var_type(),nom_classe);
	}

	public VerifType type_integer()
	{
		Type type=new TypeSimple(false,"INTEGER",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_real()
	{
		Type type=new TypeSimple(false,"REAL",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_character()
	{
		Type type=new TypeSimple(false,"CHARACTER",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_boolean()
	{
		Type type=new TypeSimple(false,"BOOLEAN",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_string()
	{
		Type type=new TypeSimple(false,"STRING",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_double()
	{
		Type type=new TypeSimple(false,"DOUBLE",null);
		return new VerifType(donne_var_type(),type);
	}

	public VerifType type_any()
	{
		Type type=new TypeSimple(false,"ANY",null);
		return new VerifType(donne_var_type(),type);
	}
	
	public VerifType type_tab()
	{
		Type type,any;
		Vector v;
		any=type_any().get_type_reel();
		v=new Vector();
		v.addElement(any);
		type=new TypeSimple(false,"ARRAY",v);
		return new VerifType(donne_var_type(),type);
	}
	
	/**
	 * @param expr
	 * @param liste_var
	 * @return
	 */
	private VerifType verifie_expr(VerifType type_courant,
			Expr expr, Map liste_var) {
		VerifType res=null;
		assert(expr!=null);
		assert(liste_var!=null);
		//assert(type_courant.a_type_reel());
		if(expr instanceof Expr_Appel)
		{
			Expr_Appel exp=(Expr_Appel)expr;
			VerifType vt,vt2,param[],type_retour;
			VerifMethode vm;
			if(exp.parametre.size()>0)
			{
				int i;
				param=new VerifType[exp.parametre.size()];
				for(i=0;i<param.length;i++)
				{
					vt2=verifie_expr(type_courant,(Expr)exp.parametre.elementAt(i),liste_var);
					param[i]=new VerifType(donne_var_type());
					verif_type_conforme(param[i],vt2,type_courant.get_type_reel());
				}
			}
			else
			{
				param=null;
			}
			type_retour=new VerifType(donne_var_type());
			verif_methode(type_courant,new NomFeature(exp.nom),
					param,type_retour,exp.debut());
			res=type_retour;
		}
		else if(expr instanceof Expr_Binaire)
		{// TODO: prendre en compte les . et = et /=
			Expr_Binaire exp=(Expr_Binaire)expr;
			if(exp.op==Expr.Point)
			{
				VerifType vt,vt2;
				vt=verifie_expr(type_courant,exp.expr1,liste_var);
				vt2=verifie_expr(vt,exp.expr2,liste_var);
				res=vt2;
			}
			else if(exp.op==Expr.Egal||exp.op==Expr.Diff)
			{// TODO: verifier pour la conformité des 2 types
				VerifType vt,vt2;
				vt=verifie_expr(type_courant,exp.expr1,liste_var);
				vt2=verifie_expr(type_courant,exp.expr2,liste_var);
				verif_type_conforme(vt,vt2,type_courant.get_type_reel());
				res=type_boolean();
			}
			else
			{
				VerifType vt,vt2,param[],type_retour;
				VerifMethode vm;
				vt=verifie_expr(type_courant,exp.expr1,liste_var);
				vt2=verifie_expr(type_courant,exp.expr2,liste_var);
				param=new VerifType[1];
				param[0]=new VerifType(donne_var_type());
				type_retour=new VerifType(donne_var_type());
				verif_methode(vt,exp.donne_nom_feature(),param,type_retour,exp.debut());
				verif_type_conforme(param[0],vt2,type_courant.get_type_reel());
				res=type_retour;
			}
		}
		else if(expr instanceof Expr_Car)
		{
			Expr_Car exp=(Expr_Car)expr;
			res=new VerifType(donne_var_type(),type_character().get_type_reel());
		}
		else if(expr instanceof Expr_Chaine)
		{
			Expr_Chaine exp=(Expr_Chaine)expr;
			res=new VerifType(donne_var_type(),type_string().get_type_reel());
		}
		else if(expr instanceof Expr_Entier)
		{
			Expr_Entier exp=(Expr_Entier)expr;
			res=new VerifType(donne_var_type(),type_integer().get_type_reel());
		}
		else if(expr instanceof Expr_Faux)
		{
			Expr_Faux exp=(Expr_Faux)expr;
			res=new VerifType(donne_var_type(),type_boolean().get_type_reel());
		}
		else if(expr instanceof Expr_Reel)
		{
			Expr_Reel exp=(Expr_Reel)expr;
			res=new VerifType(donne_var_type(),type_real().get_type_reel());
		}
		else if(expr instanceof Expr_Tableau)
		{
			Expr_Tableau exp=(Expr_Tableau)expr;
			res=new VerifType(donne_var_type(),type_tab().get_type_reel());
		}
		else if(expr instanceof Expr_Unaire)
		{
			Expr_Unaire exp=(Expr_Unaire)expr;
			VerifType vt,type_retour;
			VerifMethode vm;
			vt=verifie_expr(type_courant,exp.expr1,liste_var);
			type_retour=new VerifType(donne_var_type());
			verif_methode(vt,exp.donne_nom_feature(),null,type_retour,exp.debut());
			res=type_retour;
		}
		else if(expr instanceof Expr_Var)
		{
			Expr_Var exp=(Expr_Var)expr;
			if(liste_var.containsKey(exp.nom))
			{
				DeclareVar d;
				d=(DeclareVar)liste_var.get(exp.nom);
				assert(d!=null);
				res=new VerifType(donne_var_type(),d.getType());
			}
			else
			{
				VerifType type_retour;
				type_retour=new VerifType(donne_var_type());
				verif_methode(type_courant,new NomFeature(exp.nom),null,
						type_retour,exp.debut());
				res=type_retour;
			}
		}
		else if(expr instanceof Expr_Vrai)
		{
			Expr_Vrai exp=(Expr_Vrai)expr;
			res=new VerifType(donne_var_type(),type_boolean().get_type_reel());
		}
		return res;
	}

	/**
	 * @return
	 */
	private String donne_var_type() {
		String res;
		res="$Var"+no_var_type;
		no_var_type++;
		return res;
	}

	/**
	 * @param f
	 * @param liste_var0
	 * @param liste_var
	 */
	private void verifie_declare_var(Feature f, DeclareVar[] liste_var0, 
			Map liste_var,boolean local) {
		NomFeature nom;
		int i;
		int j;
		DeclareVar d;
		for(i=0;i<liste_var0.length;i++)
		{
			if(liste_var.containsKey(liste_var0[i].nom))
			{// la variable no i est deja présente
				d=(DeclareVar)liste_var.get(liste_var0[i].nom);
				logging.erreur(new ErreurVREG(f.classe,f,
						d,liste_var0[i],local));
			}
			else
			{
				nom=new NomFeature(liste_var0[i].nom);
				nom.debut=liste_var0[i].debut();
				verif_attribut_existe_pas(nom_classe.nom,nom,nom.debut);
				liste_var.put(liste_var0[i].nom,liste_var0[i]);
			}
			if(!liste_var0[i].type.is_like)
			{// vérification si le type de la variable existe
				verif_classe_existe(liste_var0[i].type.nom,
						liste_var0[i].type.debut());
			}
		}
	}

	public VerifAttribut cherche_attribut(NomFeature nom)
	{
		assert(nom!=null);
		//int i;
		//VerifAttribut attribut;
		if(liste_attribut.containsKey(nom.toString()))
			return (VerifAttribut)liste_attribut.get(nom);
		else
			return null;
		/*for(i=0;i<liste_attribut.size();i++)
		{
			attribut=(VerifAttribut)liste_attribut.elementAt(i);
			if(attribut.nom.equals(nom))
				return attribut;
		}
		return null;*/
	}
	
	/* liste des vérifications hors de la classe */
	
	public void verif_classe_existe(String classe, Position pos)
	{
		assert(classe!=null);
		assert(pos!=null);
		VerifClasseExiste v=new VerifClasseExiste(classe,pos);
		if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}
	
	public void verif_attribut_existe(String classe,NomFeature nom_attribut,
			Position pos)
	{
		assert(classe!=null);
		assert(nom_attribut!=null);
		assert(pos!=null);
		VerifAttributExiste v=new VerifAttributExiste(classe,nom_attribut,pos);
		if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}

	public void verif_attribut_existe_pas(String classe,NomFeature nom_attribut,
			Position pos)
	{
		assert(classe!=null);
		assert(nom_attribut!=null);
		assert(pos!=null);
		VerifAttributConflit v=new VerifAttributConflit(classe,nom_attribut,pos);
		if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}
	
	public void verif_methode(VerifType classe,NomFeature nom_attribut,
			VerifType param[],VerifType type_retour,Position pos)
	{
		assert(classe!=null);
		assert(nom_attribut!=null);
		assert(pos!=null);
		VerifMethode v=new VerifMethode(classe,nom_attribut,param,type_retour,pos);
		//if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}

	public void verif_pas_type(VerifType type,Type classe)
	{
		assert(classe!=null);
		assert(type!=null);
		VerifTypeConforme v=new VerifTypeConforme(type,classe);
		//if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}
	
	public void verif_type_conforme(VerifType ancetre,VerifType descendant,Type classe)
	{
		assert(classe!=null);
		assert(ancetre!=null);
		assert(descendant!=null);
		VerifTypeConforme v=new VerifTypeConforme(ancetre,descendant,classe);
		//if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}

	public void verif_methode_creation(VerifType nom_classe,
			NomFeature nom,Position pos)
	{
		assert(nom_classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		VerifMethodeCreation v=new VerifMethodeCreation(nom_classe,nom,pos);
		if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}
	
	public void verif_inspect(VerifType type_expr,
			Vector liste_cas,Position pos)
	{
		assert(type_expr!=null);
		assert(liste_cas!=null);
		assert(pos!=null);
		VerifInspect v=new VerifInspect(type_expr,liste_cas,pos);
		//if(!liste_verifications.contains(v))
			liste_verifications.addElement(v);
	}
	
	public void toXML(PrintStream out)
	{
		int i;
		VerifClasseExiste v;
		VerifAttributExiste v2;
		VerifAttributConflit v3;
		VerifMethodeCreation v4;
		VerifTypeConforme v5;
		VerifMethode v6;
		assert(out!=null);
		out.println("<!DOCTYPE classe SYSTEM \"..\\..\\..\\test_unitaire\\verif.dtd\">");
		out.println("<classe nom=\""+nom_classe+"\">");
		out.println("<liste_verification>");
		for(i=0;i<liste_verifications.size();i++)
		{
			if(liste_verifications.elementAt(i) instanceof VerifClasseExiste)
			{
				v=(VerifClasseExiste)liste_verifications.elementAt(i);
				v.toXML(out);
			}
			else if(liste_verifications.elementAt(i) instanceof VerifAttributExiste)
			{
				v2=(VerifAttributExiste)liste_verifications.elementAt(i);
				v2.toXML(out);
			}
			else if(liste_verifications.elementAt(i) instanceof VerifAttributConflit)
			{
				v3=(VerifAttributConflit)liste_verifications.elementAt(i);
				v3.toXML(out);
			}
			else if(liste_verifications.elementAt(i) instanceof VerifMethodeCreation)
			{
				v4=(VerifMethodeCreation)liste_verifications.elementAt(i);
				v4.toXML(out);
			}
			else if(liste_verifications.elementAt(i) instanceof VerifTypeConforme)
			{
				v5=(VerifTypeConforme)liste_verifications.elementAt(i);
				v5.toXML(out);
			}
			else if(liste_verifications.elementAt(i) instanceof VerifMethode)
			{
				v6=(VerifMethode)liste_verifications.elementAt(i);
				v6.toXML(out);
			}
		}
		out.println("</liste_verification>");
		out.println("</classe>");
	}

	/**
	 * @param chemin_fichier
	 */
	public void genere_xml(String chemin_fichier) {
		PrintStream out2;
		OutputStream out;
		String chemin_fichier2;
		assert(chemin_fichier!=null);
		assert(chemin_fichier.length()>0);
		try {
			chemin_fichier2=chemin_fichier.replaceAll("\\.e",".xml");
			assert(!chemin_fichier.equals(chemin_fichier2)):chemin_fichier+"="+chemin_fichier2;
			out=new FileOutputStream(chemin_fichier2);
			out2=new PrintStream(out);
			toXML(out2);
			out2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assert(false):"Erreur:"+e;
		}
		
		
	}
	
}
