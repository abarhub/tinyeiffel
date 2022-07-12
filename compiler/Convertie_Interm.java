/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;
import tinyeiffel.intermediaire.*;

import java.util.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Convertie_Interm {

	/**
	 * @param routines_internes 
	 * 
	 */
	public Convertie_Interm(Compiler_Eiffel compiler, 
			CIProgramme routines_internes) {
		assert(compiler!=null);
		assert(compiler.context!=null);
		this.compiler=compiler;
		this.context=compiler.context;
		this.routines_internes=routines_internes;
	}

	public CIProgramme convertie()
	{
		CIProgramme p;
		boolean heritage_directe[][],heritage[][];
		int len,i,j;
		CIClasse liste_classe[];
		
		p=new CIProgramme();
		p.init();
		len=compiler.liste_classe.size();
		// convertion des relations d'heritage
		heritage_directe=new boolean[len][len];
		heritage=new boolean[len][len];
		for(i=0;i<len;i++)
		{
			tinyeiffel.ast.Classe cl;
			boolean trouve=false;
			if(((tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i)).nom.equalsIgnoreCase("any"))
			{
				cl=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
				assert(!context.table_heritage_directe[i][i]):"type="+cl.nom;
			}
			for(j=0;j<len;j++)
			{
				heritage[i][j]=context.table_heritage[i][j];
				heritage_directe[i][j]=context.table_heritage_directe[i][j];
				trouve=trouve||heritage_directe[i][j];
			}
			if(!trouve)
			{// pas d'ancetre
				cl=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
				if(cl.nom.equalsIgnoreCase("any")||
					cl.nom.equalsIgnoreCase("general"))
				{
					
				}
				else
				{
					for(j=0;j<len;j++)
					{
						cl=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(j);
						if(cl.nom.equalsIgnoreCase("any"))
						{
							heritage_directe[i][j]=true;
							heritage[i][j]=true;
							break;
						}
					}
					assert(j<len):"classe ANY non presente";
					//context..no_classe()
				}
			}
		}
		p.heritage_directe=heritage_directe;
		p.heritage=CIInputXML.calcul_heritage(heritage_directe);
		// convertions des attributs de chaque classe
		liste_classe=new CIClasse[len];
		for(i=0;i<len;i++)
		{
			liste_classe[i]=convertie_classe((tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i),p);
		}
		p.liste_classe=liste_classe;
		termine_convertion(p);
		
		return p;
	}

	protected void termine_convertion(CIProgramme p)
	{
		CIClasse classe;
		tinyeiffel.ast.Classe cl;
		int i,j,no;
		CIAttribut attr,attr2;
		Table_Symbol table;
		Attribut_Complet ac;
		Attribut_Heritage ah;
		CIAttribut_Ascendant asc;
		assert(p!=null);
		for(i=0;i<p.liste_classe.length;i++)
		{
			classe=p.liste_classe[i];
			cl=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
			table=context.donne_table_symbol(cl);
			if(classe.liste_attribut!=null)
			{
				for(j=0;j<classe.liste_attribut.length
						&&table.liste_attribut!=null
						&&j<table.liste_attribut.length // TODO:pour eviter de traiter les attributs speciaux. A enlever
						;j++)
				{
					attr=classe.liste_attribut[j];
					no=p.no_classe(attr.type);
					assert(i==no||p.heritage_directe[i][no]):"classe:"+classe.nom.nom;
					ac=table.liste_attribut[j];
					if(ac.attribut_ancetre!=null)
					{
						for(int k=0;k<ac.attribut_ancetre.length;k++)
						{
							int l,no1=-1,m;
							tinyeiffel.ast.Heritage h;
							CINom_Attribut n1=null;
							ah=ac.attribut_ancetre[k];
							attr2=donne_attribut(p,ah);
							if(!attr2.type.nom.equalsIgnoreCase(classe.nom.nom))
							{// c'est pas le meme attribut
								CIAttribut_Descendant ad;
								//assert(classe.nom.nom.equalsIgnoreCase("a")&&
								//		attr.nom.nom.equalsIgnoreCase("toto")):
								//	"cl:"+classe.nom+";attr2:"+attr2.nom+";attr="+attr.nom;
								ad=new CIAttribut_Descendant(attr.nom,classe.nom,convertie_source((Token)null));
								attr2.ajoute_descendant(ad);
								//if(!attr.type.nom.equalsIgnoreCase(attr2.type.nom))
								{
								if(cl.heritage!=null)
								{
								for(m=0;m<cl.heritage.length;m++)
								{
									if(ah.heritage==cl.heritage[m])
									{
										no1=m;
										break;
									}
								}
								}
								else
								{// heritage implicite vers ANY
									no1=0;
								}
								assert(no1!=-1);
								h=ah.heritage;
								if(h.rename!=null)
								{
									for(m=0;m<h.rename.length;m++)
									{
										if(!h.rename[m].nom_dest.meme_nom(ac.nom))
										{
											n1=convertie_nom_feature(h.rename[m].nom_src);
											break;
										}
									}
								}
								asc=new CIAttribut_Ascendant(no1,n1,convertie_source((Token)null));
								attr.ajoute_ascendant(asc);
								/*}
								else
								{
									assert(false):"attr="+attr.nom+";k="+k+
													";ah="+ah.heritage.type+";classe="+classe.nom;
								}*/
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param object
	 * @return
	 */
	protected CISource convertie_source(Token pos) {
		// TODO Auto-generated method stub
		return null;
	}


	protected CISource convertie_source(Position pos) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected CIAttribut donne_attribut(CIProgramme p,Attribut_Heritage ah)
	{
		CIType type;
		String nom;
		CIClasse cl;
		CIAttribut attr;
		CINom_Attribut n;
		assert(p!=null);
		assert(ah!=null);
		type=convertie_type(ah.heritage.type);
		cl=p.donne_classe(type);
		n=convertie_nom_feature(ah.nom);
		return cl.donne_attribut(n);
	}

	protected CIClasse convertie_classe(tinyeiffel.ast.Classe classe,CIProgramme p)
	{
		CIClasse cl=null;
		CIAttribut attr;
		Table_Symbol table;
		Attribut_Complet ac;
		tinyeiffel.ast.Attribut attr1;
		CIAttribut attr2,liste_attr[]=null;
		int no;
		CITypeSimple heritage[];
		CICreation liste_creation[]=null;
		CIAssertion inv=null;
		
		assert(p!=null);
		assert(classe!=null);
		if(classe.heritage!=null)
		{
			heritage=new CITypeSimple[classe.heritage.length];
			for(int i=0;i<classe.heritage.length;i++)
			{
				heritage[i]=(CITypeSimple)convertie_type(classe.heritage[i].type);
			}
		}
		else
		{
			if(classe.nom.equalsIgnoreCase("general")||
				classe.nom.equalsIgnoreCase("any"))
			{
				heritage=null;
			}
			else if(classe.nom.equalsIgnoreCase("none"))
			{
				int i,len,j;
				tinyeiffel.ast.Classe cl0;
				len=compiler.liste_classe.size()-1;
				heritage=new CITypeSimple[len];
				j=0;
				for(i=0;i<len+1;i++)
				{
					cl0=(tinyeiffel.ast.Classe)compiler.liste_classe.elementAt(i);
					if(!cl0.nom.equalsIgnoreCase("none"))
					{
						assert(j>=0);
						assert(j<len);
						heritage[j]=(CITypeSimple)convertie_type(cl0.type);
						j++;
					}
				}
				assert(j==len);
			}
			else
			{
				heritage=new CITypeSimple[1];
				heritage[0]=(CITypeSimple)convertie_type(new tinyeiffel.ast.TypeSimple(false,"any",new Vector()));
			}
		}
		cl=new CIClasse((CITypeSimple)convertie_type(classe.type),p,heritage,
				convertie_source(classe.tclass));
		if(classe.expanded)
			cl.nom.expanded=true;
		table=context.donne_table_symbol(classe);
		assert(table!=null):"classe="+classe.nom;
		compiler.env.entre_classe(classe,table);
		if(table.liste_attribut!=null)
		{
			int len_plus=0,i=0;
			Vector liste;
			liste=new Vector();
			//liste_attr=new Attribut[table.liste_attribut.length+len_plus];
			for(i=0;i<table.liste_attribut.length;i++)
			{
				ac=table.liste_attribut[i];
				assert(ac!=null);
				attr2=convertie_attribut(ac,false,cl,liste);
				//liste_attr[i]=attr2;
				liste.addElement(attr2);
			}
			//ajout_attr_speciaux(classe,liste);
			ajout_attr_speciaux2(classe,liste);
			liste_attr=new CIAttribut[liste.size()];
			liste.copyInto(liste_attr);
		}
		else
		{
			Vector liste;
			liste=new Vector();
			//liste_attr=new Attribut[table.liste_attribut.length+len_plus];
			ajout_attr_speciaux2(classe,liste);
			if(liste.size()>0)
			{
				liste_attr=new CIAttribut[liste.size()];
				liste.copyInto(liste_attr);
			}
		}
		if(classe.creation!=null)
		{
			int i;
			liste_creation=new CICreation[classe.creation.length];
			for(i=0;i<classe.creation.length;i++)
			{
				liste_creation[i]=convertie_creation(classe.creation[i]);
			}
		}
		if(classe.invariant!=null)
			inv=convertie_assert(cl,classe.invariant);
		cl.invariant=inv;
		cl.liste_attribut=liste_attr;
		cl.creation=liste_creation;
		compiler.env.sort_classe();
		return cl;
	}

	/**
	 * ajoute des attributs spéciaux suivant la classe
	 */	
	protected void ajout_attr_speciaux(tinyeiffel.ast.Classe classe,Vector liste)
	{
		CIAttribut attr2;
		if(classe.nom.equalsIgnoreCase("integer"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_int",null),null);
			attr2.retour=new CITypeSimple(false,"$integer",null,null,null);
			attr2.type=new CITypeSimple(false,"integer",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("boolean"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_bool",null),null);
			attr2.retour=new CITypeSimple(false,"$boolean",null,null,null);
			attr2.type=new CITypeSimple(false,"boolean",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("real"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_real",null),null);
			attr2.retour=new CITypeSimple(false,"$real",null,null,null);
			attr2.type=new CITypeSimple(false,"real",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("double"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_double",null),null);
			attr2.retour=new CITypeSimple(false,"$double",null,null,null);
			attr2.type=new CITypeSimple(false,"double",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("character"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_char",null),null);
			attr2.retour=new CITypeSimple(false,"$character",null,null,null);
			attr2.type=new CITypeSimple(false,"character",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("array"))
		{
			CIType liste2[];
			int i;
			//assert(false):"Array !";
			attr2=new CIAttribut(new CINom_Attribut("$tab",null),null);
			assert(classe.type.generique!=null);
			assert(classe.type.generique.length>0);
			liste2=new CIType[classe.type.generique.length];
			for(i=0;i<classe.type.generique.length;i++)
			{
				liste2[i]=convertie_type(classe.type.generique[i]);
			}
			attr2.retour=new CITypeSimple(false,"$array",liste2,null,null);
			attr2.type=new CITypeSimple(false,"array",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
		else if(classe.nom.equalsIgnoreCase("string"))
		{
			attr2=new CIAttribut(new CINom_Attribut("$val_string",null),null);
			attr2.retour=new CITypeSimple(false,"$string",null,null,null);
			attr2.type=new CITypeSimple(false,"string",null,null,null);
			//liste_attr[i]=attr2;
			liste.addElement(attr2);
		}
	}
	
	/**
	 * ajoute des attributs spéciaux suivant la classe
	 */	
	protected void ajout_attr_speciaux2(Classe classe,Vector liste)
	{
		Type t;
		int i,j;
		CIClasse cl;
		CIAttribut attr,attr2;
		assert(classe!=null);
		assert(liste!=null);
		t=classe.type;
		assert(t!=null);
		assert(t.nom!=null);
		for(i=0;i<routines_internes.liste_classe.length;i++)
		{
			cl=routines_internes.liste_classe[i];
			if(cl!=null&&cl.nom.nom.equalsIgnoreCase(t.nom))
			{
				for(j=0;j<cl.liste_attribut.length;j++)
				{
					attr=cl.liste_attribut[j];
					if(!attr.est_descendant())
					{// c'est une variable
						attr2=new CIAttribut(attr);
						liste.add(attr2);
					}
				}
			}
		}
	}
	
	protected CICreation convertie_creation(tinyeiffel.ast.Creation create)
	{
		CICreation t=null;
		CINom_Attribut attr[]=null;
		CIType type[]=null;
		int i;
		assert(create!=null);
		attr=new CINom_Attribut[create.nom_fonction.length];
		for(i=0;i<create.nom_fonction.length;i++)
		{
			attr[i]=convertie_nom_feature(create.nom_fonction[i]);
		}
		if(create.liste_type!=null)
		{
			type=new CIType[create.liste_type.length];
			for(i=0;i<create.liste_type.length;i++)
			{
				type[i]=convertie_type(create.liste_type[i]);
			}
		}
		//create.nom_fonction
		t=new CICreation(type,attr,convertie_source(create.tcreation));
		return t;
		/*if(type.is_like)
		{
			t=new Type(((tinyeiffel.ast.Expr_Var)type.like).nom);
		}
		else
		{
			if(type.generique!=null)
			{
				t2=new Type[type.generique.length];
				for(i=0;i<t2.length;i++)
				{
					t2[i]=convertie_type(type.generique[i]);
				}
			}
			t=new Type(type.expanded,type.nom,t2);
		}
		return t;*/
	}

	protected CIType convertie_type(tinyeiffel.ast.Type type)
	{
		CIType t=null;
		CIType t2[]=null;
		int i;
		assert(type!=null);
		if(type.is_like)
		{
			t=new CITypeAncre(((tinyeiffel.ast.Expr_Var)type.like).nom,
					convertie_source(type.tlike));
		}
		else
		{
			if(type.generique!=null)
			{
				t2=new CIType[type.generique.length];
				for(i=0;i<t2.length;i++)
				{
					t2[i]=convertie_type(type.generique[i]);
				}
			}
			// TODO: voir pour les contraintes
			t=new CITypeSimple(type.expanded,type.nom,t2,null,convertie_source(type.tnom));
		}
		return t;
	}

	protected CIAttribut convertie_attribut(Attribut_Complet attr,boolean renomage,
														CIClasse cl, 
														Vector liste)
	{
		CIAttribut t=null;
		CINom_Attribut n;
		assert(attr!=null);
		assert(cl!=null);
		n=convertie_nom_feature(attr.getAttribut().nom);
		t=new CIAttribut(n,n.source);
		// affectation du type au resultat
		if(attr.no_attribut_reel==-1)
		{// c'est un attribut directe
			t.type=(CITypeSimple)convertie_type(attr.getAttribut().classe.type);
			CIRoutine routine;
			Feature f=attr.getFeature();
			if(f instanceof FeatureRoutine||
				f instanceof FeatureExternal||
				f instanceof FeatureDeferred)
			{
				compiler.env.entre_feature(f);
				compiler.env.entre_do();
				routine=convertie_feature(cl,f);
				t.routine=routine;
				if(routine.retour!=null)
					t.retour=routine.retour.type;
				compiler.env.sort_do();
				compiler.env.sort_feature();
				if(f instanceof FeatureRoutine&&((FeatureRoutine)f).once)
				{// routine once
					CIAttribut attr2;
					attr2=new CIAttribut(new CINom_Attribut(
							this.nom_once_traite(cl,f.liste_nom[0]),null),null);
					attr2.retour=new CITypeSimple(false,"$boolean",null,null,null);
					attr2.type=new CITypeSimple(false,cl.nom.nom,null,null,null);
					attr2.var_static=true;
					liste.addElement(attr2);
					if(routine.retour!=null)
					{
						attr2=new CIAttribut(
								new CINom_Attribut(nom_once_result(cl,f.liste_nom[0]),null),null);
						// TODO: a voir que faire pour un type ancre
						attr2.retour=new CITypeSimple(false,((CITypeSimple)routine.retour.type).nom,null,null,null);
						attr2.type=new CITypeSimple(false,cl.nom.nom,null,null,null);
						attr2.var_static=true;
						liste.addElement(attr2);
					}
				}
			}
			else if(f instanceof FeatureExpr)
			{
				FeatureExpr f2=(FeatureExpr)f;
				if(f2.type_retour!=null)
					t.retour=convertie_type(f2.type_retour);
				t.cst=convertie_expr_simple(f2.expr);
			}
			else if(f instanceof FeatureUnique)
			{// TODO : prendre en compte le numero
				if(f.type_retour!=null)
					t.retour=convertie_type(f.type_retour);
			}
			else if(f instanceof FeatureAttr)
			{
				if(f.type_retour!=null)
					t.retour=convertie_type(f.type_retour);
			}
		}
		else
		{// c'est un attribut qui vient d'un ancetre
			Feature f=attr.getFeature();
			t.type=(CITypeSimple)convertie_type(attr.attribut_ancetre[attr.no_attribut_reel].heritage.type);
			if(f.type_retour!=null)
				t.retour=convertie_type(f.type_retour);
		}
		return t;
	}

	protected CIRoutine convertie_feature(CIClasse cl,Feature f)
	{
		CIRoutine r;
		Signature sig;
		DeclareVar d;
		CIDeclare_Var d2,type_retour=null;
		CIListe_Var param=null;
		int i,j;
		CIAssertion pre=null,post=null;
		assert(cl!=null);
		assert(f!=null);
		assert(f instanceof FeatureRoutine||
				f instanceof FeatureExternal||
				f instanceof FeatureDeferred);
		no_temp0=0;
		if(f.param!=null)
		{
			//param=new Declare_Var[f.param.length];
			param=new CIListe_Var();
			for(i=0;i<f.param.length;i++)
			{
				d=f.param[i];
				d2=new CIDeclare_Var(d.nom,convertie_type(d.type),
						convertie_source(d.tnom));
				//param[i]=d2;
				param.ajoute(d2);
			}
		}
		if(f.type_retour!=null)
		{
			d2=new CIDeclare_Var("Result",convertie_type(f.type_retour),null);
			type_retour=d2;
		}
		if(f.require!=null)
			pre=convertie_assert(cl,f.require);
		if(f instanceof FeatureRoutine)
		{
			FeatureRoutine f2=(FeatureRoutine)f;
			CIListe_Var local=null;
			CIInstruction instr[]=null,rescue[]=null;
			int len,pos;
			CIListe_Instr ins;
			CIExpr val;
			CITypeSimple t;
			r=new CIRoutine(cl,convertie_source(f.debut()));
			if(f2.local!=null)
			{
				//local=new Declare_Var[f2.local.length];
				local=new CIListe_Var();
				for(i=0;i<f2.local.length;i++)
				{
					d=f2.local[i];
					d2=new CIDeclare_Var(d.nom,convertie_type(d.type),
							convertie_source(d.tnom));
					//local[i]=d2;
					local.ajoute(d2);
				}
			}
			r.local=local;
			r.parametre=param;
			r.retour=type_retour;
			if(f2.liste_instr!=null)
				instr=convertie_instr(cl,r,f2.liste_instr);
			if(f2.rescue!=null)
				rescue=convertie_instr(cl,r,f2.rescue);
			len=0;
			if(r.local!=null)
				len+=r.local.size();
			if(instr!=null)
				len+=instr.length;
			if(rescue!=null)
				len+=rescue.length;
			//if(f2.once)
			//	len+=1;
			//ins=new Instruction[len+4];
			ins=new CIListe_Instr();
			pos=0;
			if(f.type_retour!=null)
			{
				d2=type_retour;
				t=trouve_vrai_type(d2.type,cl,f2);
				assert(t!=null):"type "+d2.type+" invalide";
				val=valeur_defaut(t);
				ins.ajoute(new CIInstruction_Affect(new CIExpr_Var(d2.nom,d2.source),
						val,false,null));
			}
			if(r.local!=null)
			{// initialisation des variables
				for(i=0;i<r.local.size();i++)
				{
					d2=r.local.elt(i);
					t=trouve_vrai_type(d2.type, cl, f2);
					assert(t!=null):"type "+d2.type+" invalide";
					val=valeur_defaut(t);
					//ins[i+pos]=new Instruction_Affect(new Expr_Var(d2.nom),val);
					ins.ajoute(new CIInstruction_Affect(new CIExpr_Var(d2.nom,d2.source),
							val,false,null));
				}
				pos+=i;
			}
			//ins[pos]=new Instruction_Label("debut_routine");
			ins.ajoute(new CIInstruction_Label("debut_routine",convertie_source(f.debut())));
			pos+=1;
			if(f2.once)
			{// TODO: prendre en compte les routines once avec plusieurs noms
				String nom_var_test,label_suite;
				CIDeclare_Var decl;
				CIExpr_Binaire expb;
				CIExpr_Scalaire exp1,exp2;
				CIExpr_Var expr_var;
				String once_traite=nom_once_traite(cl,f.liste_nom[0]);
				String once_result=nom_once_result(cl,f.liste_nom[0]);
				label_suite=temp("label_once");
				exp1=new CIExpr_Var(once_traite,null);
				ins.ajoute(new CIInstruction_Si_Non(exp1,label_suite,null));
				if(f.type_retour!=null)
				{
					expr_var=new CIExpr_Var("result",null);
					exp1=new CIExpr_Var(once_result,null);
					ins.ajoute(new CIInstruction_Affect(expr_var,exp1,false,null));
				}
				ins.ajoute(new CIInstruction_Return(null));
				ins.ajoute(new CIInstruction_Label(label_suite,null));
				expr_var=new CIExpr_Var(once_traite,null);
				exp1=new CIExpr_Bool(true,null);
				ins.ajoute(new CIInstruction_Affect(expr_var,exp1,false,null));
			}
			if(instr!=null)
			{
				for(j=0;j<instr.length;j++)
				{
					//ins[pos+j]=instr[j];
					ins.ajoute(instr[j]);
				}
				pos+=j;
			}
			if(f2.once&&f.type_retour!=null)
			{
				CIExpr_Var expr_var;
				CIExpr_Scalaire exp1;
				String once_result=nom_once_result(cl,f.liste_nom[0]);
				expr_var=new CIExpr_Var(once_result,null);
				exp1=new CIExpr_Var("result",null);
				ins.ajoute(new CIInstruction_Affect(expr_var,exp1,false,null));
			}
			//ins[pos]=new Instruction_Return();
			ins.ajoute(new CIInstruction_Return(null));
			//ins[pos+1]=new Instruction_Label("debut_rescue");
			ins.ajoute(new CIInstruction_Label("debut_rescue",null));
			pos+=2;
			if(rescue!=null)
			{
				for(j=0;j<rescue.length;j++)
				{
					//ins[pos+j]=rescue[j];
					ins.ajoute(rescue[j]);
				}
				pos+=j;
			}
			//ins[pos]=new Instruction_Raise(new Expr_Entier(4));
			ins.ajoute(new CIInstruction_Raise(new CIExpr_Entier(4,null),null));
			pos+=1;
			//assert(pos==ins.length):"pos="+pos+";len="+ins.length;
			//instr=ins;
			//r.liste_instruction=instr;
			r.liste_instruction=ins;
		}
		else if(f instanceof FeatureExternal)
		{
			FeatureExternal fe=(FeatureExternal)f;
			String s;
			CIListe_Instr ins;
			s=fe.str.chaine_unique();
			ins=new CIListe_Instr();
			ins.ajoute(new CIInstruction_Label("debut_routine",convertie_source(f.debut())));
			if(s.equalsIgnoreCase("\"tinyeiffel\""))
			{
				CIExpr_Var ev,ev2,ev3;
				CIExpr e;
				int type=0;
				r=new CIRoutine(cl,convertie_source(f.debut()));
				r.parametre=param;
				r.retour=type_retour;
				/*type=conv_type_op(cl.nom.nom,fe);
				assert(type>0);
				instr_extern(ins,type,r);*/
				instr_extern2(ins,cl.nom,fe,r);
			}
			else if(s.substring(0,4).equalsIgnoreCase("\"jvm"))
			{
				CIExpr_Var ev,ev2,ev3;
				CIExpr e;
				int type=0;
				r=new CIRoutine(cl,convertie_source(f.debut()));
				r.parametre=param;
				r.retour=type_retour;
				/*type=conv_type_op(cl.nom.nom,fe);
				assert(type>0);
				instr_extern(ins,type,r);*/
				instr_extern3(ins,cl.nom,fe,r);
			}
			else
			{
				r=new CIRoutine(cl,convertie_source(f.debut()));
				r.parametre=param;
				r.retour=type_retour;
				assert(false):"s="+s;
			}
			ins.ajoute(new CIInstruction_Label("debut_rescue",null));
			ins.ajoute(new CIInstruction_Raise(new CIExpr_Entier(4,null),null));
			r.liste_instruction=ins;
		}
		else
		{
			r=new CIRoutine(cl,convertie_source(f.debut()));
			r.parametre=param;
			r.retour=type_retour;
		}
		if(f.ensure!=null)
			post=convertie_assert(cl,f.ensure);
		r.precondition=pre;
		r.postcondition=post;
		return r;
	}

	private CITypeSimple trouve_vrai_type(CIType type, CIClasse cl,
			FeatureRoutine f2) {
		if(type!=null)
		{
			if(type instanceof CITypeSimple)
			{
				return (CITypeSimple)type;
			}
			else
			{
				CITypeAncre t;
				String nom;
				t=(CITypeAncre)type;
				nom=t.nom;
				if(f2!=null)
				{
					DeclareVar d,tab[];
					int i;
					tab=f2.param;
					if(tab!=null)
					{
						for(i=0;i<tab.length;i++)
						{
							d=tab[i];
							if(d.nom.equalsIgnoreCase(nom))
							{
								return (CITypeSimple)convertie_type(d.type);
							}
						}
					}
					tab=f2.local;
					if(tab!=null)
					{
						for(i=0;i<tab.length;i++)
						{
							d=tab[i];
							if(d.nom.equalsIgnoreCase(nom))
							{
								return (CITypeSimple)convertie_type(d.type);
							}
						}
					}
				}
				if(cl!=null)
				{
					CIAttribut attr,tab[];
					int i;
					if(nom.equals("Current"))
					{
						return cl.nom;
					}
					tab=cl.liste_attribut;
					if(tab!=null)
					{
						for(i=0;i<tab.length;i++)
						{
							attr=tab[i];
							if(attr.nom.nom.equalsIgnoreCase(nom))
							{
								return (CITypeSimple)attr.retour;
							}
						}
					}
				}
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	protected HashMap liste_operation=null;
	
	protected Map liste_op()
	{
		if(liste_operation==null)
		{
			liste_operation=new HashMap();
			// integer
			liste_operation.put("integer \"+\" 1",new Integer(extern_plus_int));
			liste_operation.put("integer \"-\" 1",new Integer(extern_moins_int));
			liste_operation.put("integer \"*\" 1",new Integer(extern_fois_int));
			liste_operation.put("integer \"/\" 1",new Integer(extern_divr_int));
			liste_operation.put("integer \"//\" 1",new Integer(extern_dive_int));
			liste_operation.put("integer \"^\" 1",new Integer(extern_puiss_int));
			liste_operation.put("integer \"\\\\\" 1",new Integer(extern_mod_int));
			liste_operation.put("integer \">=\" 1",new Integer(extern_sup_int));
			liste_operation.put("integer \">\" 1",new Integer(extern_sups_int));
			liste_operation.put("integer \"<\" 1",new Integer(extern_infs_int));
			liste_operation.put("integer \"<=\" 1",new Integer(extern_inf_int));
			liste_operation.put("integer \"+\" 0",new Integer(extern_plusu_int));
			liste_operation.put("integer \"-\" 0",new Integer(extern_moinsu_int));
			// real
			liste_operation.put("real \"+\" 1",new Integer(extern_plus_real));
			liste_operation.put("real \"-\" 1",new Integer(extern_moins_real));
			liste_operation.put("real \"*\" 1",new Integer(extern_fois_real));
			liste_operation.put("real \"/\" 1",new Integer(extern_divr_real));
			liste_operation.put("real \"^\" 1",new Integer(extern_puiss_real));
			liste_operation.put("real \">=\" 1",new Integer(extern_sup_real));
			liste_operation.put("real \">\" 1",new Integer(extern_sups_real));
			liste_operation.put("real \"<\" 1",new Integer(extern_infs_real));
			liste_operation.put("real \"<=\" 1",new Integer(extern_inf_real));
			liste_operation.put("real \"+\" 0",new Integer(extern_plusu_real));
			liste_operation.put("real \"-\" 0",new Integer(extern_moinsu_real));
			// double
			liste_operation.put("double \"+\" 1",new Integer(extern_plus_double));
			liste_operation.put("double \"-\" 1",new Integer(extern_moins_double));
			liste_operation.put("double \"*\" 1",new Integer(extern_fois_double));
			liste_operation.put("double \"/\" 1",new Integer(extern_divr_double));
			liste_operation.put("double \"^\" 1",new Integer(extern_puiss_double));
			liste_operation.put("double \">=\" 1",new Integer(extern_sup_double));
			liste_operation.put("double \">\" 1",new Integer(extern_sups_double));
			liste_operation.put("double \"<\" 1",new Integer(extern_infs_double));
			liste_operation.put("double \"<=\" 1",new Integer(extern_inf_double));
			liste_operation.put("double \"+\" 0",new Integer(extern_plusu_double));
			liste_operation.put("double \"-\" 0",new Integer(extern_moinsu_double));
			// boolean
			liste_operation.put("boolean \"and\" 1",new Integer(extern_and_bool));
			liste_operation.put("boolean \"or\" 1",new Integer(extern_or_bool));
			liste_operation.put("boolean \"xor\" 1",new Integer(extern_xor_bool));
			liste_operation.put("boolean \"implies\" 1",new Integer(extern_implies_bool));
			liste_operation.put("boolean \"not\" 0",new Integer(extern_not_bool));
			// array
			liste_operation.put("array put_ 2",new Integer(extern_put_array));
			liste_operation.put("array item_ 1",new Integer(extern_item_array));
			liste_operation.put("array resize_ 1",new Integer(extern_resize_array));
			liste_operation.put("array build_ 1",new Integer(extern_build_array));
			// string
			liste_operation.put("string \"+\" 1",new Integer(extern_plus_string));
			liste_operation.put("string put_ 2",new Integer(extern_put_string));
			liste_operation.put("string item_ 1",new Integer(extern_item_string));
			liste_operation.put("string resize_ 1",new Integer(extern_resize_string));
			liste_operation.put("string build_ 1",new Integer(extern_build_string));
			liste_operation.put("string count_ 0",new Integer(extern_count_string));
			// character
			liste_operation.put("character \">=\" 1",new Integer(extern_sup_character));
			liste_operation.put("character \">\" 1",new Integer(extern_sups_character));
			liste_operation.put("character \"<\" 1",new Integer(extern_infs_character));
			liste_operation.put("character \"<=\" 1",new Integer(extern_inf_character));
		}
		assert(liste_operation!=null);
		return liste_operation;
	}
	
	protected int conv_type_op(String nom_classe,FeatureExternal f)
	{
		assert(nom_classe!=null);
		assert(f!=null);
		Map m;
		NomFeature n;
		String s;
		int i,nb_param;
		
		m=liste_op();
		if(f.param==null)
			nb_param=0;
		else
			nb_param=f.param.length;
		for(i=0;i<f.liste_nom.length;i++)
		{
			n=f.liste_nom[i];
			if(n.infix||n.prefix)
			{
				s=nom_classe.toLowerCase()+" "+n.nom2.chaine_unique()+" "+nb_param;
				if(m.containsKey(s))
				{
					Integer j;
					j=(Integer)m.get(s);
					return j.intValue();
				}
			}
			else
			{
				s=nom_classe.toLowerCase()+" "+n.nom+" "+nb_param;
				if(m.containsKey(s))
				{
					Integer j;
					j=(Integer)m.get(s);
					return j.intValue();
				}
			}
		}
		assert(false):"classe="+nom_classe+",feature="+f.toString();
		return 0;
	}
		
	protected static final int debut=100;
	protected static final int type_int=1,type_real=2,type_double=3,
		type_char=4,type_bool=5,type_array=6,type_string=7;
	
	protected static final int put_=1,resize_=put_+1,item_=put_+2,
		build_=put_+3,count_=put_+4;
	
	protected static final int 
		extern_plus_int=debut*type_int+CIExpr_Binaire.Plus,
		extern_moins_int=debut*type_int+CIExpr_Binaire.Moins,
		extern_fois_int=debut*type_int+CIExpr_Binaire.Fois,
		extern_divr_int=debut*type_int+CIExpr_Binaire.Div_reel,
		extern_dive_int=debut*type_int+CIExpr_Binaire.Div_entier,
		extern_puiss_int=debut*type_int+CIExpr_Binaire.Puiss,
		extern_mod_int=debut*type_int+CIExpr_Binaire.Mod,
		extern_sup_int=debut*type_int+CIExpr_Binaire.Sup,
		extern_sups_int=debut*type_int+CIExpr_Binaire.SupS,
		//extern_egal_int=debut*type_int+Expr_Binaire.Egal,
		//extern_diff_int=debut*type_int+Expr_Binaire.Diff,
		extern_inf_int=debut*type_int+CIExpr_Binaire.Inf,
		extern_infs_int=debut*type_int+CIExpr_Binaire.InfS,
		extern_plusu_int=debut*type_int+CIExpr_Unaire.Plus,
		extern_moinsu_int=debut*type_int+CIExpr_Unaire.Moins;
	
	protected static final int 
		extern_plus_real=debut*type_real+CIExpr_Binaire.Plus,
		extern_moins_real=debut*type_real+CIExpr_Binaire.Moins,
		extern_fois_real=debut*type_real+CIExpr_Binaire.Fois,
		extern_divr_real=debut*type_real+CIExpr_Binaire.Div_reel,
		//extern_dive_real=debut*type_real+Expr_Binaire.Div_entier,
		extern_puiss_real=debut*type_real+CIExpr_Binaire.Puiss,
		//extern_mod_real=debut*type_real+Expr_Binaire.Mod,
		extern_sup_real=debut*type_real+CIExpr_Binaire.Sup,
		extern_sups_real=debut*type_real+CIExpr_Binaire.SupS,
		//extern_egal_int=debut*type_real+Expr_Binaire.Egal,
		//extern_diff_int=debut*type_real+Expr_Binaire.Diff,
		extern_inf_real=debut*type_real+CIExpr_Binaire.Inf,
		extern_infs_real=debut*type_real+CIExpr_Binaire.InfS,
		extern_plusu_real=debut*type_real+CIExpr_Unaire.Plus,
		extern_moinsu_real=debut*type_real+CIExpr_Unaire.Moins;
	
	protected static final int 
		extern_plus_double=debut*type_double+CIExpr_Binaire.Plus,
		extern_moins_double=debut*type_double+CIExpr_Binaire.Moins,
		extern_fois_double=debut*type_double+CIExpr_Binaire.Fois,
		extern_divr_double=debut*type_double+CIExpr_Binaire.Div_reel,
		//extern_dive_double=debut*type_double+Expr_Binaire.Div_entier,
		extern_puiss_double=debut*type_double+CIExpr_Binaire.Puiss,
		//extern_mod_double=debut*type_double+Expr_Binaire.Mod,
		extern_sup_double=debut*type_double+CIExpr_Binaire.Sup,
		extern_sups_double=debut*type_double+CIExpr_Binaire.SupS,
		//extern_egal_double=debut*type_double+Expr_Binaire.Egal,
		//extern_diff_double=debut*type_double+Expr_Binaire.Diff,
		extern_inf_double=debut*type_double+CIExpr_Binaire.Inf,
		extern_infs_double=debut*type_double+CIExpr_Binaire.InfS,
		extern_plusu_double=debut*type_double+CIExpr_Unaire.Plus,
		extern_moinsu_double=debut*type_double+CIExpr_Unaire.Moins;
	
	protected static final int 
		extern_and_bool=debut*type_bool+CIExpr_Binaire.And,
		extern_or_bool=debut*type_bool+CIExpr_Binaire.Or,
		extern_xor_bool=debut*type_bool+CIExpr_Binaire.Xor,
		extern_implies_bool=debut*type_bool+CIExpr_Binaire.Implies,
		extern_not_bool=debut*type_bool+CIExpr_Unaire.Not;

	protected static final int 
		extern_put_array=debut*type_array+put_,
		extern_resize_array=debut*type_array+resize_,
		extern_item_array=debut*type_array+item_,
		extern_build_array=debut*type_array+build_;

	protected static final int 
		extern_plus_string=debut*type_string+CIExpr_Binaire.Plus,
		extern_put_string=debut*type_string+put_+1,
		extern_resize_string=debut*type_string+resize_+1,
		extern_item_string=debut*type_string+item_+1,
		extern_build_string=debut*type_string+build_+1,
		extern_count_string=debut*type_string+count_+1;

	protected static final int 
		extern_sup_character=debut*type_char+CIExpr_Binaire.Sup,
		extern_sups_character=debut*type_char+CIExpr_Binaire.SupS,
		//extern_egal_double=debut*type_double+Expr_Binaire.Egal,
		//extern_diff_double=debut*type_double+Expr_Binaire.Diff,
		extern_inf_character=debut*type_char+CIExpr_Binaire.Inf,
		extern_infs_character=debut*type_char+CIExpr_Binaire.InfS;
	
	protected void instr_extern(CIListe_Instr instr,int type,CIRoutine routine)
	{// TODO:prendre en compte le nombre de parametres
		// TODO:prendre en compte les operateurs semi-stricts et spéciaux (olds,$,etc...) 
		CIExpr_Var ev,ev2,ev3;
		CIExpr e;
		CIExpr_Scalaire es,es2,param[];
		String valp1,valp2,valr=null;
		CIType t1,t2;
		CIListe_Var local;
		CIDeclare_Var d,d2;
		CINom_Attribut nom;		
		assert(instr!=null);
		assert(type>0);
		assert(type>=extern_plus_int);
		assert(type<=extern_count_string);
		assert(routine!=null);
		switch(type)
		{
		case extern_plus_int:
		case extern_moins_int:
		case extern_fois_int:
		case extern_divr_int:
		case extern_dive_int:
		case extern_puiss_int:
		case extern_mod_int:
		case extern_sup_int:
		case extern_sups_int:
		case extern_inf_int:
		case extern_infs_int:
		case extern_plus_real:
		case extern_moins_real:
		case extern_fois_real:
		case extern_divr_real:
		//case extern_dive_real:
		case extern_puiss_real:
		//case extern_mod_real:
		case extern_sup_real:
		case extern_sups_real:
		case extern_inf_real:
		case extern_infs_real:
		case extern_plus_double:
		case extern_moins_double:
		case extern_fois_double:
		case extern_divr_double:
		//case extern_dive_double:
		case extern_puiss_double:
		//case extern_mod_double:
		case extern_sup_double:
		case extern_sups_double:
		case extern_inf_double:
		case extern_infs_double:
		case extern_and_bool:
		case extern_or_bool:
		case extern_xor_bool:
		case extern_implies_bool:
		case extern_sup_character:
		case extern_sups_character:
		case extern_inf_character:
		case extern_infs_character:
			assert(routine.parametre!=null);
			assert(routine.parametre.size()==1);
			//instr.ajoute(new Instruction_Label("ajout"));
			valp1=attr_interne(type/debut);
			// TODO: voir pour type ancré
			valp2=attr_interne(((CITypeSimple)routine.parametre.elt(0).type).nom);
			valr=attr_interne(((CITypeSimple)routine.retour.type).nom);
			ev=new CIExpr_Var(valr,null);
			ev.set_src("Result");
			ev2=new CIExpr_Var(valp1,null);
			ev3=new CIExpr_Var(valp2,null);
			ev3.set_src(routine.parametre.elt(0).nom);
			e=new CIExpr_Binaire(type%debut,ev2,ev3,null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_plusu_int:
		case extern_moinsu_int:
		case extern_plusu_real:
		case extern_moinsu_real:
		case extern_plusu_double:
		case extern_moinsu_double:
		case extern_not_bool:
			assert(routine.parametre==null||routine.parametre.size()==0);
			valp1=attr_interne(type/debut);
			// TODO: voir pour type ancré
			valr=attr_interne(((CITypeSimple)routine.retour.type).nom);
			ev=new CIExpr_Var(valr,null);
			ev.set_src("Result");
			ev2=new CIExpr_Var(valp1,null);
			e=new CIExpr_Unaire(type%debut,ev2,null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_put_array:
			assert(routine.parametre!=null||routine.parametre.size()==2);
			// $tab[i]:=v
			ev2=new CIExpr_Var("i",null);
			ev3=new CIExpr_Var("v",null);
			ev=new CIExpr_Var("$tab",ev2,null);
			instr.ajoute(new CIInstruction_Affect(ev,ev3,false,null));
			break;
		case extern_item_array:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// Result:=$tab[i]
			ev2=new CIExpr_Var("i",null);
			ev=new CIExpr_Var("$tab",ev2,null);
			ev3=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev3,ev,false,null));
			break;
		case extern_build_array:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// $tab:=new E[i]
			ev2=new CIExpr_Var("i",null);
			t1=new CITypeSimple(false,"E",null,null,null);
			e=new CIExpr_Creation(t1,ev2,null);
			ev=new CIExpr_Var("$tab",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_resize_array:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// TODO: a faire
			// $tab_local:=new E[i]
			// copie de $tab vers $tab_local
			// $tab=$tab_local
			{
				CITypeSimple t3[]={new CITypeSimple(false,"E",null,null,null)};
				local=new CIListe_Var();
				routine.local=local;
				t2=new CITypeSimple(false,"$array",t3,null,null);
				d=new CIDeclare_Var("$tab_local",t2,null);
				local.ajoute(d);
				t2=new CITypeSimple(false,"$integer",null,null,null);
				d=new CIDeclare_Var("$i",t2,null);
				local.ajoute(d);
				ev2=new CIExpr_Var("i",null);
				t1=new CITypeSimple(false,"E",null,null,null);
				e=new CIExpr_Creation(t1,ev2,null);
				ev=new CIExpr_Var("$tab_local",null);
				instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
				ev=new CIExpr_Var("$i",null);
				e=new CIExpr_Entier(0,null);
				instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
				
				//Instr_Loop instr=(Instr_Loop)inst;
				CIInstruction instruction;
				String l1,l2,l3;
				l1=temp("test");
				l2=temp("fin");
				l3=temp("debut_boucle");
				instruction=new CIInstruction_Goto(l1,null);
				instr.ajoute(instruction);
				instruction=new CIInstruction_Label(l3,null);
				instr.ajoute(instruction);
				
				//ajoute_instr(v,cl,r,instr.loop);
				ev2=new CIExpr_Var("$i",null);
				ev3=new CIExpr_Var("$tab",ev2,null);
				ev2=new CIExpr_Var("$i",null);
				ev=new CIExpr_Var("$tab_local",ev2,null);
				instr.ajoute(new CIInstruction_Affect(ev,ev3,false,null));
				
				ev2=new CIExpr_Var("$i",null);
				es2=new CIExpr_Entier(1,null);
				e=new CIExpr_Binaire(CIExpr_Binaire.Plus,ev2,es2,null);
				ev=new CIExpr_Var("$i",null);
				instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
				
				instruction=new CIInstruction_Label(l1,null);
				instr.ajoute(instruction);
				
				ev2=new CIExpr_Var("$i",null);
				ev3=new CIExpr_Var("i",null);
				e=new CIExpr_Binaire(CIExpr_Binaire.InfS,ev2,ev3,null);
				instruction=new CIInstruction_Si_Non(e,l2,null);
				instr.ajoute(instruction);
				
				ev2=new CIExpr_Var("$i",null);
				ev3=new CIExpr_Var("count",null);
				e=new CIExpr_Binaire(CIExpr_Binaire.InfS,ev2,ev3,null);
				instruction=new CIInstruction_Si(e,l3,null);
				instr.ajoute(instruction);
				
				instruction=new CIInstruction_Label(l2,null);
				instr.ajoute(instruction);
				
				ev2=new CIExpr_Var("$tab_local",null);
				ev=new CIExpr_Var("$tab",null);
				instr.ajoute(new CIInstruction_Affect(ev,ev2,false,null));
			}
			break;
		case extern_plus_string:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// Result:=$add_string($val_string,other.$val_string)
			nom=new CINom_Attribut("$add_string",null);
			param=new CIExpr_Scalaire[2];
			param[0]=new CIExpr_Var("$val_string",null);
			ev2=new CIExpr_Var("$val_string",null);
			ev2.set_src("other");
			param[1]=ev2;
			e=new CIExpr_Appel(null,nom,param,null);
			ev=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_put_string:
			assert(routine.parametre!=null||routine.parametre.size()==2);
			// $put_string($val_string,c,i)
			nom=new CINom_Attribut("$put_string",null);
			param=new CIExpr_Scalaire[3];
			param[0]=new CIExpr_Var("$val_string",null);
			param[1]=new CIExpr_Var("c",null);
			param[2]=new CIExpr_Var("i",null);
			instr.ajoute(new CIInstruction_Appel(null,nom,param,null));
			break;
		case extern_item_string:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// Result:=$item_string($val_string,i)
			nom=new CINom_Attribut("$item_string",null);
			param=new CIExpr_Scalaire[1];
			param[0]=new CIExpr_Var("i",null);
			e=new CIExpr_Appel(null,nom,param,null);
			ev=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_resize_string:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// $val_string:=$resize_string($val_string,i)
			nom=new CINom_Attribut("$resize_string",null);
			param=new CIExpr_Scalaire[2];
			param[0]=new CIExpr_Var("$val_string",null);
			param[1]=new CIExpr_Var("i",null);
			ev=new CIExpr_Var("$val_string",null);
			e=new CIExpr_Appel(null,nom,param,null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_build_string:
			assert(routine.parametre!=null||routine.parametre.size()==1);
			// $val_string:=$build_string(i)
			nom=new CINom_Attribut("$build_string",null);
			param=new CIExpr_Scalaire[1];
			param[0]=new CIExpr_Var("$i",null);
			ev=new CIExpr_Var("$val_string",null);
			e=new CIExpr_Appel(null,nom,param,null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		case extern_count_string:
			assert(routine.parametre==null||routine.parametre.size()==0);
			// Result:=$count_string($val_string)
			nom=new CINom_Attribut("$count_string",null);
			param=new CIExpr_Scalaire[1];
			param[0]=new CIExpr_Var("$val_string",null);
			e=new CIExpr_Appel(null,nom,param,null);
			ev=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
			break;
		default:
			assert(false):"type="+type;
		}
	}
	
	// les routines extern tinyeiffel
	protected void instr_extern2(CIListe_Instr instr,
			CITypeSimple nom_classe,FeatureExternal fe,CIRoutine routine)
	{
		assert(instr!=null);
		assert(nom_classe!=null);
		assert(fe!=null);
		assert(routine!=null);
		CIExpr_Scalaire param[];
		CIExpr_Var v1,v2;
		int i;
		CINom_Attribut nom;
		String nom_routine;
		CIExpr_Appel e;
		CIExpr_Var ev;
		//assert(routine.parametre!=null||routine.parametre.size()==1);
		// Result:=$add_string($val_string,other.$val_string)
		/*nom=new CINom_Attribut("$add_string",null);
		param=new CIExpr_Scalaire[2];
		param[0]=new CIExpr_Var("$val_string",null);
		ev2=new CIExpr_Var("$val_string",null);
		ev2.set_src("other");
		param[1]=ev2;
		e=new CIExpr_Appel(null,nom,param,null);
		ev=new CIExpr_Var("Result",null);
		instr.ajoute(new CIInstruction_Affect(ev,e,null));*/
		if(fe.param!=null&&fe.param.length>0)
		{
			param=new CIExpr_Scalaire[fe.param.length];
			for(i=0;i<param.length;i++)
			{
				v1=new CIExpr_Var(fe.param[i].nom,null);
				param[i]=v1;
			}
		}
		else
		{
			param=null;
		}
		if(fe.alias!=null&&!fe.alias.toString().equals(""))
		{
			nom_routine=fe.alias.toString();
		}
		else
		{
			nom_routine="$"+fe.liste_nom[0].nom;
		}
		nom=new CINom_Attribut(nom_routine,null);
		if(fe.type_retour!=null)
		{
			e=new CIExpr_Appel(null,nom,param,null);
			ev=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
		}
		else
		{
			instr.ajoute(new CIInstruction_Appel(null,nom,param,null));
		}
	}
	
	// les routines externs java
	protected void instr_extern3(CIListe_Instr instr,
			CITypeSimple nom_classe,FeatureExternal fe,CIRoutine routine)
	{
		assert(instr!=null);
		assert(nom_classe!=null);
		assert(fe!=null);
		assert(routine!=null);
		CIExpr_Scalaire param[];
		CIExpr_Var v1,v2;
		int i;
		CINom_Attribut nom;
		String nom_routine;
		CIExpr_Extern e;
		CIExpr_Var ev;
		//assert(routine.parametre!=null||routine.parametre.size()==1);
		// Result:=$add_string($val_string,other.$val_string)
		/*nom=new CINom_Attribut("$add_string",null);
		param=new CIExpr_Scalaire[2];
		param[0]=new CIExpr_Var("$val_string",null);
		ev2=new CIExpr_Var("$val_string",null);
		ev2.set_src("other");
		param[1]=ev2;
		e=new CIExpr_Appel(null,nom,param,null);
		ev=new CIExpr_Var("Result",null);
		instr.ajoute(new CIInstruction_Affect(ev,e,null));*/
		if(fe.param!=null&&fe.param.length>0)
		{
			param=new CIExpr_Scalaire[fe.param.length];
			for(i=0;i<param.length;i++)
			{
				v1=null;
				if(!fe.param[i].type.is_like)
				{
					String nom_type;
					nom_type=fe.param[i].type.nom;
					if(nom_type.equalsIgnoreCase("integer"))
					{
						v1=new CIExpr_Var("$val_int",null);
						v1.set_src(fe.param[i].nom);
					}
					else if(nom_type.equalsIgnoreCase("boolean"))
					{
						v1=new CIExpr_Var("$val_bool",null);
						v1.set_src(fe.param[i].nom);
					}
					else if(nom_type.equalsIgnoreCase("real"))
					{
						v1=new CIExpr_Var("$val_real",null);
						v1.set_src(fe.param[i].nom);
					}
					else if(nom_type.equalsIgnoreCase("double"))
					{
						v1=new CIExpr_Var("$val_double",null);
						v1.set_src(fe.param[i].nom);
					}
					else if(nom_type.equalsIgnoreCase("string"))
					{
						v1=new CIExpr_Var("$val_string",null);
						v1.set_src(fe.param[i].nom);
					}
					else if(nom_type.equalsIgnoreCase("character"))
					{
						v1=new CIExpr_Var("$val_char",null);
						v1.set_src(fe.param[i].nom);
					}
				}
				if(v1==null)
					v1=new CIExpr_Var(fe.param[i].nom,null);
				/*if(v1!=null&&!v1.type.is_like)
				{
					CITypeSimple t;
					String type_primitif;
					routine.local.ajoute("local"+i,type_primitif);
					t=(CITypeSimple)v1.type;
					if(t.nom.equalsIgnoreCase("INTEGER"))
					{
						e=new CIExpr_Extern
						instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
					}
				}*/
				param[i]=v1;
			}
		}
		else
		{
			param=null;
		}
		if(fe.alias!=null&&!fe.alias.toString().equals(""))
		{
			nom_routine=fe.alias.toString();
		}
		else
		{
			nom_routine=fe.liste_nom[0].nom;
		}
		nom_routine=nom_routine.replaceAll("\"","");
		nom=new CINom_Attribut(nom_routine,null);
		if(fe.type_retour!=null)
		{
			e=new CIExpr_Extern(convertie_type(fe.type_retour),
				fe.str.toString(),(fe.alias!=null)?fe.alias.toString():null,
				null,nom,param,null);
			ev=new CIExpr_Var("Result",null);
			instr.ajoute(new CIInstruction_Affect(ev,e,false,null));
		}
		else
		{
			instr.ajoute(new CIInstruction_Extern(
				fe.str.toString().replaceAll("\"",""),
				(fe.alias!=null)?fe.alias.toString().replaceAll("\"",""):null,
				null,nom,param,null));
		}
	}
	
	protected String attr_interne(String classe)
	{
		String valt=null;
		if(classe.equalsIgnoreCase("integer"))
		{
			valt="$val_int";
		}
		else if(classe.equalsIgnoreCase("boolean"))
		{
			valt="$val_bool";
		}
		else if(classe.equalsIgnoreCase("real"))
		{
			valt="$val_real";
		}
		else if(classe.equalsIgnoreCase("double"))
		{
			valt="$val_double";
		}
		else if(classe.equalsIgnoreCase("character"))
		{
			valt="$val_char";
		}
		assert(valt!=null);
		return valt;
	}
	
	protected String attr_interne(int no_classe_kernel)
	{
		String valt=null;
		switch(no_classe_kernel)
		{
			case type_int:
				valt="$val_int";
				break;
			case type_real:
				valt="$val_real";
				break;
			case type_double:
				valt="$val_double";
				break;
			case type_bool:
				valt="$val_bool";
				break;
			case type_char:
				valt="$val_char";
				break;
			default:
				assert(false);
				break;
		}
		return valt;
	}
	
	protected CIExpr valeur_defaut(CIType type)
	{
		CIExpr val=null;
		assert(type!=null);
		if(type instanceof CITypeSimple)
		{
			CITypeSimple type2=(CITypeSimple)type;
			if(type2.nom.equalsIgnoreCase("INTEGER"))
				val=new CIExpr_Entier(0,null);
			else if(type2.nom.equalsIgnoreCase("BOOLEAN"))
				val=new CIExpr_Bool(false,null);
			else if(type2.nom.equalsIgnoreCase("CHARACTER"))
				val=new CIExpr_Char('\0',null);
			else if(type2.nom.equalsIgnoreCase("REAL"))
				val=new CIExpr_Real(0.0,null);
			else if(type2.nom.equalsIgnoreCase("DOUBLE"))
				val=new CIExpr_Real(0.0,null);
			else
				val=new CIExpr_Void(null);
		}
		else
		{// TODO: a voir pour type ancré
			//val=new CIExpr_Void(null);
			assert(false):"type="+type;
		}
		assert(val!=null);
		return val;
	}
	
	protected String nom_once_traite(CIClasse cl,NomFeature n)
	{
		assert(cl!=null);
		assert(n!=null);
		return "$once_traite_"+cl.nom+"$"+n.nom;
	}

	protected String nom_once_result(CIClasse cl,NomFeature n)
	{
		assert(cl!=null);
		assert(n!=null);
		return "$once_result_"+cl.nom+"$"+n.nom;
	}
	
	protected boolean contient_nom_feature(String nom,
											boolean infix,
											boolean prefix,
											NomFeature liste_nom[])
	{
		assert(nom!=null);
		assert(liste_nom!=null);
		assert(liste_nom.length>0);
		for(int k=0;k<liste_nom.length;k++)
		{
			NomFeature n=liste_nom[k];
			if(infix&&n.infix&&nom.equalsIgnoreCase(n.nom2.chaine_unique()))
				return true;
			else if(prefix&&n.prefix&&nom.equalsIgnoreCase(n.nom2.chaine_unique()))
				return true;
			else if(!prefix&&!n.prefix&&!infix&&!n.infix&&
					nom.equalsIgnoreCase(n.nom))
				return true;
		}
		return false;
	}

	protected CIInstruction[] convertie_instr(CIClasse cl,
											CIRoutine r,Instr liste[])
	{
		CIInstruction linstr[]=null,instruction,linstr2[];
		Vector v=new Vector();
		Instr inst;
		int i,j;
		CIResult_Expr res,r1,r2,r3;
		assert(cl!=null);
		assert(r!=null);
		assert(liste!=null);
		for(i=0;i<liste.length;i++)
		{
			inst=liste[i];
			if(inst instanceof Instr_Affect)
			{
				Instr_Affect instr=(Instr_Affect)inst;
				CIExpr_Var nom;
				res=convertie_expr(r.local,instr.expr);
				if(res!=null)
				{
				if(res.a_instr())
					v.addAll(res.instr);
				nom=new CIExpr_Var(instr.nom,convertie_source(instr.tid));
				v.addElement(new CIInstruction_Affect(nom,res.expr,false,
						convertie_source(instr.debut())));
				}
			}
			else if(inst instanceof Instr_If)
			{
				Instr_If instr=(Instr_If)inst;
				Instr ins,lins[];
				String nom;
				CIDeclare_Var v1,v2;
				String l1,l2,l3;
				CIExpr e;
				CIExpr_Scalaire es;
				Instr_ElseIf ei;
				Instr_Else els;
				l1=temp("label");
				l2=temp("fin");
				ajoute_if(r,instr.expr,instr.liste_instr,l1,l2,v);
				instruction=new CIInstruction_Label(l1,null);
				v.addElement(instruction);
				ins=instr.getSuivant();
				while(ins!=null&&ins instanceof Instr_ElseIf)
				{
					ei=(Instr_ElseIf)ins;
					l1=temp("label");
					ajoute_if(r,ei.expr,ei.liste_instr,l1,l2,v);
					instruction=new CIInstruction_Label(l1,null);
					v.addElement(instruction);
					ins=ei.getSuivant();
				}
				if(ins!=null)
				{// else
					assert(ins instanceof Instr_Else);
					els=(Instr_Else)ins;
					ajoute_instr(v,r.classe,r,els.liste_instr);
				}
				//ajoute_instr(v,cl,t,instr.liste_instr);
				// fin
				instruction=new CIInstruction_Label(l2,null);
				v.addElement(instruction);
				//}
			}
			else if(inst instanceof Instr_Loop)
			{
				Instr_Loop instr=(Instr_Loop)inst;
				Instr ins,lins[];
				String l1,l2,l3;
				ajoute_instr(v,cl,r,instr.from);
				l1=temp("test");
				l2=temp("fin");
				l3=temp("debut_boucle");
				instruction=new CIInstruction_Goto(l1,null);
				v.addElement(instruction);
				instruction=new CIInstruction_Label(l3,
						convertie_source(instr.tloop));
				v.addElement(instruction);
				ajoute_instr(v,cl,r,instr.loop);
				instruction=new CIInstruction_Label(l1,
						convertie_source(instr.tuntil));
				v.addElement(instruction);
				ajoute_test(r.local,l3,v,instr.expr,false);
				instruction=new CIInstruction_Label(l2,
						convertie_source(instr.tend));
				v.addElement(instruction);
			}
			else if(inst instanceof Instr_Appel)
			{// TODO : a terminer
				Instr_Appel instr=(Instr_Appel)inst,ins;
				Instr ins2,lins[];
				CIAttribut attr;
				CIDeclare_Var var,var2,var3;//&é"'(-è
				Feature f;
				Attribut_Complet ac;
				CIType type;
				NomFeature nf;
				tinyeiffel.ast.Classe cl3;
				tinyeiffel.ast.Type type3;
				//assert(false):"liste="+liste.length;
				//f=compiler.env.feature;
				//ac=compiler.env.table.donne_attribut(new NomFeature(instr.nom));//cl.donne_attribut(instr.nom);
				type=convertie_type(compiler.env.classe.type);
				nf=new NomFeature(instr.nom);
				var2=donne_attr(nf,type/*ac.getType().nom*/);
				//if(ac!=null&&ac.getFeature().est_routine())
				assert(var2!=null):"nom="+instr.nom+";type="+type;
				if(var2!=null&&est_routine(nf,type))
				{
					var=ajoute_routine(r.local,
							new CINom_Attribut(var2.nom,var2.source),
							var2.type,v,null,instr.parametre);
					//assert(var!=null);
				}
				else
				{
					if(var2==null)
						var=r.cherche_var_routine(instr.nom);
					else
					{
						var=new CIDeclare_Var(var2.nom,var2.type,
								var2.source);
					}
					assert(var!=null):"nom="+instr.nom+";"+var2;
				}
				//cl3=compiler.context.cherche_classe(new tinyeiffel.ast.Type(false,var.type.nom,new Vector()));
				//assert(cl3!=null);
				ins=(Instr_Appel)instr.getSuivant();
				//i=0;
				while(ins!=null)
				{
					CIClasse cl2;
					Table_Symbol table;
					assert(var!=null);
					//cl2=cl.programme.donne_classe(var.type);
					//attr=cl2.donne_attribut(instr.nom);
					/*table=context.donne_table_symbol(new tinyeiffel.ast.Type(false,var.type.nom,new Vector()));
					assert(table!=null);
					ac=table.donne_attribut(new NomFeature(ins.nom));
					assert(ac!=null);*/
					nf=new NomFeature(ins.nom);
					var2=donne_attr(nf,var.type);
					//assert(i<0):"var2="+var2+";i="+i+";param="+ins.parametre.length+
					//		";"+est_routine(ins.nom,var.type.nom);
					//if(ac.getFeature().est_routine())
					assert(var2!=null):"nf="+nf+";type="+var.type;
					if(var2!=null&&est_routine(nf,var.type))
					{
						var=ajoute_routine(r.local,
								new CINom_Attribut(var2.nom,var2.source),
								var2.type,v,
								new CIExpr_Var(var.nom,var.source),
								ins.parametre);
					}
					else
					{
						//var=r.cherche_var(ins.nom);
						CIExpr_Appel expr;
						CIDeclare_Var v1,v2;
						CIInstruction ins3;
						/*if(var2==null)
						{// il faut chercher dans la classe du resultat precedant au lieu de la routine
							Type type2;
							Declare_entite entite;
							//var2=r.cherche_var_routine(ins.nom); //&"'(è-"
							entite=compiler.env.donne_entite(ins.nom);
							assert(entite!=null):"nom="+ins.nom;
							type2=convertie_type(entite.getType());
							//if(var2==null)
							{
								var2=new Declare_Var(ins.nom,type2);
							}
							assert(var2!=null):"var2="+ins.nom+";pos="+ins.debut();
						}*/
						/*else
						{
							var2=new Declare_Var(var2.nom,var2.type);
						}*/
						assert(var!=null);
						assert(var2!=null);
						expr=new CIExpr_Appel(
								new CIExpr_Var(var.nom,var.source),
								new CINom_Attribut(ins.nom,convertie_source(ins.tid)),
								null,convertie_source(ins.debut()));
						v1=r.ajoute_var_local(temp("tmp"),var2.type);
						ins3=new CIInstruction_Affect(
								new CIExpr_Var(v1.nom,v1.source),expr,false,null);
						v.addElement(ins3);
						var=v1;
						assert(var!=null);
					}
					ins=(Instr_Appel)ins.getSuivant();
					//i++;
				}
			}
			else if(inst instanceof Instr_Creation)
			{// TODO : a terminer
				Instr_Creation ins=(Instr_Creation)inst;
				CIAttribut attr;
				CIDeclare_Var var,var2,var3,nom;//&é"'(-è
				Feature f;
				Attribut_Complet ac;
				CIType type;
				NomFeature nf;
				CIInstruction_Affect instr;
				CIExpr e,e2;
				CIExpr_Appel ea;
				CIExpr_Scalaire p[];
				CIResult_Expr r01,r02;
				if(ins.type!=null)
					type=convertie_type(ins.type);
				else
				{
					Declare_entite entite;
					entite=compiler.env.donne_entite(ins.nom);
					assert(entite!=null);
					type=convertie_type(entite.getType());
				}
				assert(type!=null);
				nom=r.ajoute_var_local(ins.nom,type);
				p=null;
				if(ins.nom2!=null)
				{// il y a appel
					if(ins.parametre!=null)
					{
						CIExpr_Scalaire es;
						p=new CIExpr_Scalaire[ins.parametre.length];
						for(int k=0;k<ins.parametre.length;k++)
						{
							r01=this.convertie_expr(r.local,ins.parametre[k]);
							if(r01!=null)
							{
								if(r01.a_instr())
									v.addAll(r01.instr);
								r02=this.convertie_scalaire(r.local,r01.expr);
								if(r02.a_instr())
									v.addAll(r02.instr);
								p[k]=(CIExpr_Scalaire)r02.expr;
							}
						}
					}
					ea=new CIExpr_Appel(null,
						new CINom_Attribut(ins.nom2,convertie_source(ins.tid2)),
						p,convertie_source(ins.debut()));
				}
				else
				{
					ea=null;
				}
				e=new CIExpr_Creation(type,ea,convertie_source(ins.debut()));
				instr=new CIInstruction_Affect(
						new CIExpr_Var(nom.nom,nom.source),e,false,null);
				v.addElement(instr);
			}
			else if(inst instanceof Instr_TentAffect)
			{// TODO : a terminer
				Instr_TentAffect instr=(Instr_TentAffect)inst;
				Instr ins,lins[];
				String nom;
				CIDeclare_Var v1,v2;
				String l1,fin;
				CIExpr e,e2,e3;
				CIExpr_Scalaire es;
				CIExpr_Var ev;
				CIResult_Expr r01,r02;
				CIType type;
				l1=temp("label");
				fin=temp("fin");
				res=convertie_expr(r.local,instr.expr);
				if(res!=null)
				{
				if(res.a_instr())
				{
					v.addAll(res.instr);
				}
				type=res.expr.type;
				ev=new CIExpr_Var(instr.nom,convertie_source(instr.tid));
				e=new CIExpr_Type(ev,type,
						convertie_source((Token)null/*TODO: instr.expr.debut() ?*/));
				instruction=new CIInstruction_Si(e,l1,
						convertie_source(instr.debut()));
				v.addElement(instruction);
				instruction=new CIInstruction_Affect(ev,new CIExpr_Void(null),false,null);
				v.addElement(instruction);
				instruction=new CIInstruction_Goto(fin,null);
				v.addElement(instruction);
				instruction=new CIInstruction_Label(l1,null);
				v.addElement(instruction);
				instruction=new CIInstruction_Affect(ev,res.expr,true,null);
				v.addElement(instruction);
				instruction=new CIInstruction_Label(fin,null);
				v.addElement(instruction);
				}
			}
			else if(inst instanceof Instr_Inspect)
			{// TODO : a terminer
				Instr_Inspect instr=(Instr_Inspect)inst;
				Instr ins,lins[];
				String nom,var_test;
				CIDeclare_Var v1,v2;
				String l1,fin,suite,suite_else=null;
				CIExpr e,e2,e3;
				CIExpr_Scalaire es;
				CIExpr_Var ev;
				CIResult_Expr r01,r02;
				CIType type;
				Vector v3,v4;
				boolean else_trouve=false;
				Token token=new Token(-1,-1,"","");
				tinyeiffel.ast.Expr_Var ev2;
				r01=this.convertie_expr(r.local,instr.expr);
				if(r01.a_instr())
					v.addAll(r01.instr);
				e=r01.expr;
				r01=this.convertie_scalaire(r.local,e);
				if(r01.a_instr())
					v.addAll(r01.instr);
				es=(CIExpr_Scalaire)r01.expr;
				fin=temp("label");
				var_test=temp("var_test");
				r.ajoute_var_local(var_test,es.type);
				instruction=new CIInstruction_Affect(
						new CIExpr_Var(var_test,null),
						es,false,null);
				v.addElement(instruction);
				ev2=new tinyeiffel.ast.Expr_Var(var_test,token);
				l1=null;
				while(instr!=null)
				{
					l1=temp("label");
					if(instr.when!=null&&instr.when.size()>0)
					{// les testes
						tinyeiffel.ast.Expr e1,e4;
						int k;
						v4=instr.when;
						if(v4.size()>0)
						{
							suite=temp("suite");
							for(k=0;k<v4.size();k++)
							{
								tinyeiffel.ast.Expr_Binaire exp_test;
								v3=(Vector)v4.elementAt(k);
								if(v3.size()==2)
								{
									String suite2,suite3;
									CIExpr_Scalaire es1,es2;
									CIExpr_Binaire eb;
									suite2=temp("test_intervalle");
									suite3=temp("fin_test_intervalle");
									e1=(tinyeiffel.ast.Expr)v3.elementAt(0);
									r01=convertie_expr(r.local,e1);
									if(r01.a_instr())
										v.addAll(r01.instr);
									r01=convertie_scalaire(r.local,r01.expr);
									if(r01.a_instr())
										v.addAll(r01.instr);
									es1=(CIExpr_Scalaire)r01.expr;
									eb=new CIExpr_Binaire(CIExpr_Binaire.Sup,es,es1,
											convertie_source(((tinyeiffel.ast.Expr)v3.elementAt(0)).debut()));
									//exp_test=new tinyeiffel.ast.Expr_Binaire(tinyeiffel.ast.Expr.Sup,ev2,e1,token);
									ajoute_test(r.local,suite2,v,/*exp_test*/eb,false);
									instruction=new CIInstruction_Goto(suite3,null);
									v.addElement(instruction);
									instruction=new CIInstruction_Label(suite2,null);
									v.addElement(instruction);
									e4=(tinyeiffel.ast.Expr)v3.elementAt(1);
									r01=convertie_expr(r.local,e4);
									if(r01.a_instr())
										v.addAll(r01.instr);
									r01=convertie_scalaire(r.local,r01.expr);
									if(r01.a_instr())
										v.addAll(r01.instr);
									es1=(CIExpr_Scalaire)r01.expr;
									eb=new CIExpr_Binaire(CIExpr_Binaire.Inf,es,es1,null);
									//exp_test=new tinyeiffel.ast.Expr_Binaire(tinyeiffel.ast.Expr.Inf,ev2,e4,token);
									ajoute_test(r.local,suite,v,/*exp_test*/eb,false);
									instruction=new CIInstruction_Label(suite3,null);
									v.addElement(instruction);
								}
								else
								{
									assert(v3.size()==1);
									e1=(tinyeiffel.ast.Expr)v3.elementAt(0);
									exp_test=new tinyeiffel.ast.Expr_Binaire(tinyeiffel.ast.Expr.Egal,ev2,e1,new Token(-1,-1,"",""));
									ajoute_test(r.local,suite,v,exp_test,false);
								}
							}
							instruction=new CIInstruction_Goto(l1,null);
							v.addElement(instruction);
							instruction=new CIInstruction_Label(suite,null);
							v.addElement(instruction);
						}
					}
					else
					{
						suite_else=temp("label_else");
						instruction=new CIInstruction_Goto(l1,null);
						v.addElement(instruction);
						instruction=new CIInstruction_Label(suite_else,null);
						v.addElement(instruction);
					}
					//r01=convertie_instr(cl,r,instr.liste_instr);
					ajoute_instr(v,cl,r,instr.liste_instr);
					instruction=new CIInstruction_Goto(fin,null);
					v.addElement(instruction);
					instruction=new CIInstruction_Label(l1,null);
					v.addElement(instruction);
					instr=(Instr_Inspect)instr.getSuivant();
				}
				// instruction si aucun choix
				instruction=new CIInstruction_Raise(new CIExpr_Entier(7,null),null);
				v.addElement(instruction);
				if(suite_else!=null)
				{
					instruction=new CIInstruction_Goto(suite_else,null);
					v.addElement(instruction);
				}
				// fin de l'inspect
				instruction=new CIInstruction_Label(fin,null);
				v.addElement(instruction);
			}
			else if(inst instanceof Instr_Retry)
			{
				Instr_Retry instr=(Instr_Retry)inst;
				Instr ins,lins[];
				String nom,var_test;
				instruction=new CIInstruction_Goto("debut_routine",
						convertie_source(instr.debut()));
				v.addElement(instruction);
			}
			else if(inst instanceof Instr_Check)
			{
				Instr_Check instr=(Instr_Check)inst;
				Instr ins,lins[];
				String nom,var_test,suite,suite2;
				CIAssertion as;
				CIDeclare_Var var;
				CIExpr_Var_Syst ev;
				CIExpr_Entier ee;
				CIExpr_Scalaire tab[];
				//Instruction instr0;
				suite=temp("suite_check");
				//suite2=temp("debut_check");
				// TODO: verifier pour le code d'exception
				as=convertie_assert(cl,instr.liste_expr,
						new CIExpr_Entier(4,null));
				//instruction=new Instruction_Goto("debut_routine");
				//v.addElement(instruction);
				//TODO: trouver la valeur pour les niveaus de verif
				ee=new CIExpr_Entier(-1,null);
				tab=new CIExpr_Scalaire[1];
				tab[0]=ee;
				ev=new CIExpr_Var_Syst("$niveau_verif",tab,null);
				//e=new Expr_Type(ev,type);
				// TODO: a optimiser (ajouter l'instruction Si_Non)
				instruction=new CIInstruction_Si_Non(ev,suite,
						convertie_source(instr.debut()));
				v.addElement(instruction);
				/*instruction=new Instruction_Goto(suite);
				v.addElement(instruction);
				instruction=new Instruction_Label(suite2);
				v.addElement(instruction);*/
				for(i=0;i<as.liste.size();i++)
				{
					var=as.liste.elt(i);
					r.local.ajoute(var.nom,var.type);
				}
				for(i=0;i<as.liste_instr.size();i++)
				{
					instruction=as.liste_instr.elt(i);
					//r.local.ajoute(var.nom,var.type);
					v.addElement(instruction);
				}
				instruction=new CIInstruction_Label(suite,null);
				v.addElement(instruction);
			}
			else if(inst instanceof Instr_Debug)
			{
				Instr_Debug instr=(Instr_Debug)inst;
				Instr ins,lins[];
				String nom,var_test,suite,suite2;
				CIAssertion as;
				CIDeclare_Var var;
				CIExpr_Var_Syst ev;
				CIExpr_String es;
				CIExpr_Scalaire tab[];
				//Instruction instr0;
				suite=temp("suite_debug");
				//suite2=temp("debut_check");
				// TODO: verifier pour le code d'exception
				//as=convertie_assert(cl,instr.liste_expr,new Expr_Entier(4));
				//instruction=new Instruction_Goto("debut_routine");
				//v.addElement(instruction);
				//TODO: trouver la valeur pour les niveaus de verif
				//ee=new Expr_Entier(-1);
				tab=new CIExpr_Scalaire[instr.chaine.length];
				for(i=0;i<tab.length;i++)
				{
					String str,str2;
					str=instr.chaine[i].chaine_unique();
					if(str.startsWith("\"")&&str.endsWith("\""))
					{
						str2=str.substring(1,str.length()-1);
					}
					else
					{
						str2=str;
					}
					
					es=new CIExpr_String(str2,null);
					tab[i]=es;
				}
				ev=new CIExpr_Var_Syst("$niveau_verif_debug",tab,null);
				//e=new Expr_Type(ev,type);
				// TODO: a optimiser (ajouter l'instruction Si_Non)
				instruction=new CIInstruction_Si_Non(ev,suite,null);
				v.addElement(instruction);
				/*instruction=new Instruction_Goto(suite);
				v.addElement(instruction);
				instruction=new Instruction_Label(suite2);
				v.addElement(instruction);*/
				ajoute_instr(v,cl,r,instr.instr);
				/*for(i=0;i<as.liste.size();i++)
				{
					var=as.liste.elt(i);
					r.local.ajoute(var.nom,var.type);
				}
				for(i=0;i<as.liste_instr.size();i++)
				{
					instruction=as.liste_instr.elt(i);
					//r.local.ajoute(var.nom,var.type);
					v.addElement(instruction);
				}*/
				instruction=new CIInstruction_Label(suite,null);
				v.addElement(instruction);
			}
		}
		linstr=new CIInstruction[v.size()];
		v.copyInto(linstr);
		return linstr;
	}


	protected CIAssertion convertie_assert(CIClasse cl,
											Assert liste[])
	{
		return convertie_assert(cl,liste,new CIExpr_Entier(4,null));
	}

	protected CIAssertion convertie_assert(CIClasse cl,
											Assert liste[],
											CIExpr_Scalaire except)
	{// TODO: mettre en parametre l'exception levée
		Vector v=new Vector();
		Assert ass,liste_assert[];
		int i,j;
		CIResult_Expr res,r1,r2,r3;
		CIListe_Var liste_var;
		CIAssertion assert2;
		CIInstruction instr;
		CIListe_Instr liste_instr;
		String label;
		assert(cl!=null);
		assert(liste!=null);
		assert(except!=null);
		assert2=new CIAssertion(null);
		for(i=0;i<liste.length;i++)
		{
			ass=liste[i];
			if(ass.expr!=null)
			{
				label=temp("assert");
				ajoute_test(assert2.liste,label,v,ass.expr,false);
				instr=new CIInstruction_Raise(except,null);
				v.addElement(instr);
				instr=new CIInstruction_Label(label,null);
				v.addElement(instr);
			}
		}
		if(v.size()>0)
		{
			liste_instr=new CIListe_Instr();
			//v.copyInto(liste_instr);
			liste_instr.ajoute(v);
			assert2.liste_instr=liste_instr;
			return assert2;
		}
		else
		{
			return null;
		}
	}

	protected CIDeclare_Var ajoute_routine(CIListe_Var liste,CINom_Attribut nom,CIType type,/*Attribut_Complet ac,//Attribut attr,
									/*Instr_Appel instr,*/Vector v,CIExpr_Var cible,
									tinyeiffel.ast.Expr parametre[])
	{
		CIResult_Expr res1,res2;
		tinyeiffel.ast.Expr e;
		CIExpr_Scalaire param[]=null,e1,e2;
		int i,j;
		CIInstruction ins;
		CIDeclare_Var res;
		//Expr_Var ev;
		assert(liste!=null);
		assert(nom!=null);
		//assert(type!=null);
		//assert(instr!=null);
		assert(v!=null);
		//assert(!var.nom.equalsIgnoreCase("f3")):"var="+var;
		if(parametre!=null)
		{
			param=new CIExpr_Scalaire[parametre.length];
			for(i=0;i<parametre.length;i++)
			{
				e=parametre[i];
				res1=convertie_expr(liste,e);
				if(res1!=null)
				{
				if(res1.instr!=null)
					v.addAll(res1.instr);
				res2=convertie_scalaire(liste,res1.expr);
				if(res2.instr!=null)
					v.addAll(res2.instr);
				param[i]=(CIExpr_Scalaire)res2.expr;
				}
			}
		}
		// TODO: a continuer
		/*if(cible!=null)
			ev=new Expr_Var(cible);
		else
			ev=null;*/
		/*if(var.nom.compareToIgnoreCase("f1")==0)
		{
			assert(var.nom.compareToIgnoreCase("f1")==0);
			assert(var.nom.compareToIgnoreCase("f3")!=0);
			//assert(var.nom.compareToIgnoreCase("f1")!=0):"var="+var;
			//assert(false):"var="+var;
		}*/
		//assert(var.nom.compareToIgnoreCase("f3")!=0):"f3="+var+";"+param+"#"+cible+";p="+parametre;
		if(type!=null)
		{
			CIExpr_Appel expr;
			CIDeclare_Var v1,v2;
			if(cible!=null)
			{
				CIExpr_Binaire exp;
				String label;
				label=temp("tmp");
				exp=new CIExpr_Binaire(CIExpr_Binaire.Diff,new CIExpr_Void(null),cible,null);
				ins=new CIInstruction_Si(exp,label,null);
				v.addElement(ins);
				ins=new CIInstruction_Raise(new CIExpr_Entier(6,null),null);
				v.addElement(ins);
				ins=new CIInstruction_Label(label,null);
				v.addElement(ins);
			}
			expr=new CIExpr_Appel(cible,nom,param,null);
			//v1=r.ajoute_var_local(temp("tmp"),type);
			v1=liste.ajoute(temp("tmp"),type);
			ins=new CIInstruction_Affect(new CIExpr_Var(v1.nom,null),expr,false,null);
			v.addElement(ins);
			return v1;
		}
		else
		{
			if(cible!=null)
			{
				CIExpr_Binaire exp;
				String label;
				label=temp("tmp");
				exp=new CIExpr_Binaire(CIExpr_Binaire.Diff,new CIExpr_Void(null),cible,null);
				ins=new CIInstruction_Si(exp,label,null);
				v.addElement(ins);
				ins=new CIInstruction_Raise(new CIExpr_Entier(6,null),null);
				v.addElement(ins);
				ins=new CIInstruction_Label(label,null);
				v.addElement(ins);
			}
			ins=new CIInstruction_Appel(cible,nom,param,null);
			v.addElement(ins);
			return null;
		}
	}

	/*protected Type trouve_vrai_type(Routine r,Type type)
	{
		Declare_Var v;
		assert(type!=null);
		if(type.is_like)
		{
			if(r!=null)
			{
				v=r.cherche_var(type.nom);
				assert(v!=null);
			}
		}
		else
			return type;
	}*/

	protected Attribut_Complet donne_attribut(NomFeature nom,CIType type)
	{
		Table_Symbol table;
		Attribut_Complet ac;
		tinyeiffel.ast.Type t,t3;
		CIType t2;
		assert(nom!=null);
		assert(type!=null);
		//t2=trouve_vrai_type(type);
		//assert(t2!=null);
		t=convertie_type2(type);
		assert(t!=null);
		t3=compiler.env.table.trouve_vrai_type(t);
		assert(t3!=null);
		assert(!t3.is_like);
		table=context.donne_table_symbol(t3);
		assert(table!=null);
		ac=table.donne_attribut(nom);
		return ac;
	}

	protected tinyeiffel.ast.Type convertie_type2(CIType type)
	{
		Vector gen=new Vector();
		int i;
		tinyeiffel.ast.Type res;
		assert(type!=null);
		if(type.is_like)
		{
			res=new tinyeiffel.ast.TypeAncre(
					new tinyeiffel.ast.Expr_Var(((CITypeAncre)type).nom,
							new Token(-1,-1,"","")));
		}
		else
		{
			CITypeSimple type2=(CITypeSimple)type;
			if(type2.generique!=null)
			{
				for(i=0;i<type2.generique.length;i++)
				{
					gen.addElement(convertie_type2(type2.generique[i]));
				}
			}
			res=new tinyeiffel.ast.TypeSimple(type2.expanded,type2.nom,gen);
		}
		return res;
	}

	protected boolean est_routine(NomFeature nom,CIType type)
	{
		Table_Symbol table;
		Attribut_Complet ac;
		boolean res=false;
		assert(nom!=null);
		assert(type!=null);
		//assert(!nom.equalsIgnoreCase("f3"));
		ac=donne_attribut(nom,type);
		//assert(!nom.equalsIgnoreCase("f3"));
		if(ac==null)
			res=false;
		//assert(!nom.equalsIgnoreCase("f3"));
		if(res!=true&&ac!=null)
		{
			res=ac.getFeature().est_routine();
			//assert(!nom.equalsIgnoreCase("f3")):"f3="+nom+";"+res+
			//		";ac="+ac+";type="+
			//		(ac.getFeature() instanceof FeatureRoutine);
		}
		//assert(!nom.equalsIgnoreCase("f3")):"f3="+nom+";"+res+";ac="+ac;
		return res;
	}

	protected CIDeclare_Var donne_attr(NomFeature nom,CIType type)
	{// TODO: mettre le numéro de ligne pour la variable
		Table_Symbol table;
		Attribut_Complet ac;
		assert(nom!=null);
		assert(type!=null);
		ac=donne_attribut(nom,type);
		if(ac==null)
		{
			// TODO: voir pour un type ancré
			table=context.donne_table_symbol(new tinyeiffel.ast.TypeSimple(false,((CITypeSimple)type).nom,new Vector()));
			assert(table!=null);
			ac=table.donne_attribut(nom);
			if(ac==null)
			{
				Declare_entite entite;
				entite=compiler.env.donne_entite(nom.nom);
				if(entite!=null)
					return new CIDeclare_Var(entite.getNom(),
							convertie_type(entite.getType()),null);
				else
					return null;
			}
		}
		if(ac.getType()!=null)
			return new CIDeclare_Var(ac.nom.nom,convertie_type(ac.getType()),null);
		else
			return new CIDeclare_Var(ac.nom.nom,null,null);
	}

	protected void ajoute_test(CIListe_Var liste,String suite,
								Vector v,tinyeiffel.ast.Expr expr,boolean inv)
	{
		CIResult_Expr res,r1;
		CIDeclare_Var v1,v2;
		CIExpr_Scalaire es;
		CIExpr e,e2;
		CIInstruction instruction;
		assert(liste!=null);
		assert(expr!=null);
		assert(suite!=null);
		assert(v!=null);
		res=convertie_expr(liste,expr);
		if(res!=null)
		{
		if(res.a_instr())
		{
			v.addAll(res.instr);
		}
		if(inv)
		{
			//v1=r.ajoute_var_local(temp("tmp"),res.expr.type);
			v1=liste.ajoute(temp("tmp"),res.expr.type);
			// simplifier res en es
			r1=convertie_scalaire(liste,res.expr);
			es=(CIExpr_Scalaire)r1.expr;
			v.addAll(r1.instr);
			e=new CIExpr_Unaire(CIExpr_Unaire.Not,es,null);
			instruction=new CIInstruction_Affect(convertie_expr(v1),e,false,null);
			v.addElement(instruction);
			e2=convertie_expr(v1);
		}
		else
		{
			e2=res.expr;
		}
		//l1=temp("label");
		instruction=new CIInstruction_Si(e2,suite,null);
		v.addElement(instruction);
		// if
		//ajoute_instr(v,r.classe,r,liste);
		//l2=temp("fin");
		//instruction=new Instruction_Goto(fin);
		//v.addElement(instruction);
		//instruction=new Instruction_Label(l1);
		//v.addElement(instruction);
		//ins=ei.getSuivant();
		}
		else
		{
			//ins=null;
		}
	}

	protected void ajoute_test(CIListe_Var liste,String suite,
								Vector v,CIExpr expr,boolean inv)
	{
		CIResult_Expr res,r1;
		CIDeclare_Var v1,v2;
		CIExpr_Scalaire es;
		CIExpr e,e2;
		CIInstruction instruction;
		assert(liste!=null);
		assert(expr!=null);
		assert(suite!=null);
		assert(v!=null);
		if(inv)
		{
			//v1=r.ajoute_var_local(temp("tmp"),expr.type);
			v1=liste.ajoute(temp("tmp"),expr.type);
			// simplifier res en es
			r1=convertie_scalaire(liste,expr);
			es=(CIExpr_Scalaire)r1.expr;
			v.addAll(r1.instr);
			e=new CIExpr_Unaire(CIExpr_Unaire.Not,es,null);
			instruction=new CIInstruction_Affect(convertie_expr(v1),e,false,null);
			v.addElement(instruction);
			e2=convertie_expr(v1);
		}
		else
		{
			e2=expr;
		}
		//l1=temp("label");
		instruction=new CIInstruction_Si(e2,suite,null);
		v.addElement(instruction);
	}

	protected void ajoute_if(CIRoutine r,tinyeiffel.ast.Expr expr,
								Instr liste[],String suite,
								String fin,Vector v)
	{// a effacer en remplacant par ajoute_test
		CIResult_Expr res,r1;
		CIDeclare_Var v1,v2;
		CIExpr_Scalaire es;
		CIExpr e;
		CIInstruction instruction;
		assert(r!=null);
		assert(expr!=null);
		assert(liste!=null);
		assert(suite!=null);
		assert(fin!=null);
		assert(v!=null);
		res=convertie_expr(r.local,expr);
		if(res!=null)
		{
		if(res.a_instr())
		{
			v.addAll(res.instr);
		}
		v1=r.ajoute_var_local(temp("tmp"),res.expr.type);
		// simplifier res en es
		r1=convertie_scalaire(r.local,res.expr);
		es=(CIExpr_Scalaire)r1.expr;
		v.addAll(r1.instr);
		e=new CIExpr_Unaire(CIExpr_Unaire.Not,es,null);
		instruction=new CIInstruction_Affect(convertie_expr(v1),e,false,null);
		v.addElement(instruction);
		//l1=temp("label");
		instruction=new CIInstruction_Si(convertie_expr(v1),suite,null);
		v.addElement(instruction);
		// if
		ajoute_instr(v,r.classe,r,liste);
		//l2=temp("fin");
		instruction=new CIInstruction_Goto(fin,null);
		v.addElement(instruction);
		//instruction=new Instruction_Label(l1);
		//v.addElement(instruction);
		//ins=ei.getSuivant();
		}
		else
		{
			//ins=null;
		}
	}

	protected void ajoute_instr(Vector v,CIClasse cl,
								CIRoutine r,Instr liste[])
	{
		CIInstruction linstr2[];
		int j;
		assert(v!=null);
		assert(cl!=null);
		assert(r!=null);
		assert(liste!=null);
		linstr2=convertie_instr(cl,r,liste);
		if(linstr2!=null)
		{
			for(j=0;j<linstr2.length;j++)
			{
				v.addElement(linstr2[j]);
			}
		}
	}

	protected CIResult_Expr convertie_scalaire(CIListe_Var liste,CIExpr expr)
	{
		CIResult_Expr res;
		Vector v=new Vector();
		CIExpr_Scalaire es;
		CIDeclare_Var v2;
		CIInstruction instruction;
		assert(expr!=null);
		assert(liste!=null);
		if(expr instanceof CIExpr_Scalaire)
		{
			es=(CIExpr_Scalaire)expr;
		}
		else
		{
			//v2=r.ajoute_var_local(temp("tmp"),expr.type);
			v2=liste.ajoute(temp("tmp"),expr.type);
			es=convertie_expr(v2);
			instruction=new CIInstruction_Affect((CIExpr_Var)es,expr,false,null);
			v.addElement(instruction);
		}
		res=new CIResult_Expr(v,es);
		return res;
	}

	protected CIExpr_Var convertie_expr(CIDeclare_Var v)
	{
		CIExpr_Var res;
		assert(v!=null);
		res=new CIExpr_Var(v.nom,v.source);
		res.type=v.type;
		return res;
	}

	protected String temp(String nom)
	{
		String res;
		if(nom==null)
			nom="temp";
		res=nom+no_temp0;
		no_temp0++;
		return res;
	}
	
	protected int no_temp0;

	protected String temp_global(String nom)
	{
		String res;
		if(nom==null)
			nom="_global_temp";
		res=nom+no_temp0;
		no_temp_global0++;
		return res;
	}
	
	protected int no_temp_global0;

	protected CIResult_Expr convertie_expr(CIListe_Var liste,tinyeiffel.ast.Expr e)
	{
		CIResult_Expr res=null;
		CIExpr exp;
		assert(liste!=null);
		assert(e!=null);
		if(e instanceof tinyeiffel.ast.Expr_Var)
		{
			tinyeiffel.ast.Expr_Var ex=(tinyeiffel.ast.Expr_Var)e;
			tinyeiffel.ast.Type type;
			CIDeclare_Var v;
			Declare_entite entite;
			exp=new CIExpr_Var(ex.nom,convertie_source(ex.debut())); 
			res=new CIResult_Expr(new Vector(),exp);
			//v=r.cherche_var(ex.nom);
			//assert(v!=null):"nom="+ex.nom;
			//exp.type=v.type;
			entite=compiler.env.donne_entite(ex.nom);
			//assert(entite!=null):"nom="+ex.nom;
			if(entite!=null)
			{
				exp.type=convertie_type(entite.getType());
			}
			else
			{
				//v=r.cherche_var(ex.nom);
				v=liste.donne_var(ex.nom);
				assert(v!=null):"nom="+ex.nom;
				exp.type=v.type;
			}
		}
		else if(e instanceof tinyeiffel.ast.Expr_Vrai||
				e instanceof tinyeiffel.ast.Expr_Faux||
				e instanceof tinyeiffel.ast.Expr_Entier||
				e instanceof tinyeiffel.ast.Expr_Chaine||
				e instanceof tinyeiffel.ast.Expr_Reel||
				e instanceof tinyeiffel.ast.Expr_Car)
		{
			exp=convertie_expr_simple(e);
			res=new CIResult_Expr(new Vector(),exp);
		}
		else if(e instanceof tinyeiffel.ast.Expr_Appel)
		{
			CIAttribut attr;
			CIDeclare_Var var,var2,var3;//&é"'(-è
			Feature f;
			Attribut_Complet ac;
			CIType type;
			NomFeature nf;
			tinyeiffel.ast.Expr_Appel ex=(tinyeiffel.ast.Expr_Appel)e;
			Vector v=new Vector();
			type=convertie_type(compiler.env.classe.type);
			nf=new NomFeature(ex.nom);
			var2=donne_attr(nf,type/*ac.getType().nom*/);
			//if(ac!=null&&ac.getFeature().est_routine())
			if(var2!=null&&est_routine(nf,type))
			{
				tinyeiffel.ast.Expr param[];
				if(ex.parametre==null)
					param=null;
				else
				{
					param=new tinyeiffel.ast.Expr[ex.parametre.size()];
					ex.parametre.copyInto(param);
				}
				var=ajoute_routine(liste,new CINom_Attribut(var2.nom,var2.source),
						var2.type,v,null,param);
				assert(var!=null);
			}
			else
			{
				if(var2==null)
					//var=r.cherche_var_routine(ex.nom);
					var=liste.donne_var(ex.nom);
				else
				{
					var=new CIDeclare_Var(var2.nom,var2.type,var2.source);
				}
				assert(var!=null):"e="+e;
			}
			exp=new CIExpr_Var(var.nom,var.source);
			exp.type=var.type;
			res=new CIResult_Expr(v,exp);
		}
		else if(e instanceof tinyeiffel.ast.Expr_Binaire)
		{
			CIAttribut attr;
			CIDeclare_Var var,var2,var3;//&é"'(-è
			Feature f;
			Attribut_Complet ac;
			CIType type;
			NomFeature nf;
			tinyeiffel.ast.Expr_Binaire ex=(tinyeiffel.ast.Expr_Binaire)e;
			Vector v=new Vector();
			CIExpr_Var cible;
			CIResult_Expr res2,res3,res4;
			if(ex.op==tinyeiffel.ast.Expr.Egal||ex.op==tinyeiffel.ast.Expr.Diff)
			{// operation = et /=
				int op;
				op=(ex.op==tinyeiffel.ast.Expr.Egal)?CIExpr_Binaire.Egal:CIExpr_Binaire.Diff;
				res2=convertie_expr(liste,ex.expr1);
				//assert(res2!=null);
				if(res2!=null)
				{
				if(res2.a_instr())
					v.addAll(res2.instr);
				res3=convertie_expr(liste,ex.expr2);
				if(res3!=null)
				{
				if(res3.a_instr())
					v.addAll(res3.instr);
				res2=convertie_scalaire(liste,res2.expr);
				if(res2.a_instr())
					v.addAll(res2.instr);
				res3=convertie_scalaire(liste,res3.expr);
				if(res3.a_instr())
					v.addAll(res3.instr);
				exp=new CIExpr_Binaire(op,(CIExpr_Scalaire)res2.expr,
						(CIExpr_Scalaire)res3.expr,
						convertie_source(ex.debut()));
				exp.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
				res=new CIResult_Expr(v,exp);
				}
				}
			}
			else if(ex.op==tinyeiffel.ast.Expr.And_Then||ex.op==tinyeiffel.ast.Expr.Or_Else)
			{// operation and then et or else
				int op;
				boolean type_op;
				String suite1,fin;
				CIExpr exp2;
				CIExpr_Var v1,v2;
				type_op=ex.op==tinyeiffel.ast.Expr.And_Then;
				op=(type_op)?CIExpr_Binaire.And:CIExpr_Binaire.Or;
				suite1=temp("label");
				fin=temp("label");
				//var2=r.ajoute_var_local(temp("var"),new Type(false,"BOOLEAN",null));
				var2=liste.ajoute(temp("var"),new CITypeSimple(false,"BOOLEAN",null,null,null));
				ajoute_test(liste,suite1,v,ex.expr1,type_op);
				exp=null;
				res3=convertie_expr(liste,ex.expr2);
				if(res3!=null)
				{
				if(res3.a_instr())
					v.addAll(res3.instr);
				res3=convertie_scalaire(liste,res3.expr);
				if(res3.a_instr())
					v.addAll(res3.instr);
				exp2=new CIExpr_Binaire(op,
						(CIExpr_Scalaire)new CIExpr_Var(var2.nom,var2.source),
						(CIExpr_Scalaire)res3.expr,
						null);
				exp2.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
				v1=new CIExpr_Var(var2.nom,null);
				v.addElement(new CIInstruction_Affect(v1,exp2,false,null));
				// TODO : c'est pas plutot une instruction si
				v.addElement(new CIInstruction_Goto(fin,null));
				}
				v.addElement(new CIInstruction_Label(suite1,null));
				exp2=new CIExpr_Bool(!type_op,null);
				exp2.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
				v1=new CIExpr_Var(var2.nom,null);
				v.addElement(new CIInstruction_Affect(v1,exp2,false,null));
				v.addElement(new CIInstruction_Label(fin,null));
				exp=new CIExpr_Var(var2.nom,null);
				exp.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
				res=new CIResult_Expr(v,exp);
				//}
			}
			else if(ex.op==tinyeiffel.ast.Expr.Point)
			{// operation point
				tinyeiffel.ast.Expr param[];
				res2=convertie_expr(liste,ex.expr1);
				if(res2!=null)
				{
				if(res2.a_instr())
					v.addAll(res2.instr);
				type=res2.expr.type;
				assert(type!=null);
				if(res2.expr instanceof CIExpr_Var)
				{
					cible=(CIExpr_Var)res2.expr;
				}
				else
				{
					CIDeclare_Var v2;
					CIExpr_Var es;
					CIInstruction instruction;
					//v2=r.ajoute_var_local(temp("tmp"),res2.expr.type);
					v2=liste.ajoute(temp("tmp"),res2.expr.type);
					es=convertie_expr(v2);
					instruction=new CIInstruction_Affect((CIExpr_Var)es,
							res2.expr,false,null);
					v.addElement(instruction);
					cible=es;
				}
				assert(ex.expr2 instanceof tinyeiffel.ast.Expr_Var||
						ex.expr2 instanceof tinyeiffel.ast.Expr_Appel);
				if(ex.expr2 instanceof tinyeiffel.ast.Expr_Appel)
				{
					
					tinyeiffel.ast.Expr_Appel ea=(tinyeiffel.ast.Expr_Appel)ex.expr2;
					nf=new NomFeature(ea.nom);
					var2=donne_attr(nf,type);
					if(ea.parametre==null)
						param=null;
					else
					{
						param=new tinyeiffel.ast.Expr[ea.parametre.size()];
						ea.parametre.copyInto(param);
					}
				}
				else
				{
					tinyeiffel.ast.Expr_Var ev=(tinyeiffel.ast.Expr_Var)ex.expr2;
					param=null;
					nf=new NomFeature(ev.nom);
					var2=donne_attr(nf,type);
				}
				var=ajoute_routine(liste,new CINom_Attribut(var2.nom,var2.source),
						var2.type,v,cible,param);
				assert(var!=null);
				exp=new CIExpr_Var(var.nom,var.source);
				exp.type=var.type;
				res=new CIResult_Expr(v,exp);
				}
			}
			else
			{// les autres operations
				//assert(false);
				res2=convertie_expr(liste,ex.expr1);
				//assert(res2!=null);
				if(res2!=null)
				{
				if(res2.a_instr())
					v.addAll(res2.instr);
				type=res2.expr.type;
				nf=ex.donne_nom_feature();
				var2=donne_attr(nf,type/*ac.getType().nom*/);
				//if(ac!=null&&ac.getFeature().est_routine())
				assert(var2!=null):"nf="+nf+";type="+type;
				assert(est_routine(nf,type));
				tinyeiffel.ast.Expr param[];
				//cible=(Expr_Var)convertie_scalaire(r,res2.expr);
				if(res2.expr instanceof CIExpr_Var)
				{
					cible=(CIExpr_Var)res2.expr;
				}
				else
				{
					CIDeclare_Var v2;
					CIExpr_Var es;
					CIInstruction instruction;
					//v2=r.ajoute_var_local(temp("tmp"),res2.expr.type);
					v2=liste.ajoute(temp("tmp"),res2.expr.type);
					es=convertie_expr(v2);
					instruction=new CIInstruction_Affect((CIExpr_Var)es,
							res2.expr,false,null);
					v.addElement(instruction);
					cible=es;
				}
				param=new tinyeiffel.ast.Expr[1];
				param[0]=ex.expr2;
				var=ajoute_routine(liste,convertie_nom_feature(nf),var2.type,v,cible,param);
				assert(var!=null);
				exp=new CIExpr_Var(var.nom,var.source);
				exp.type=var.type;
				res=new CIResult_Expr(v,exp);
				}
			}
		}
		else if(e instanceof tinyeiffel.ast.Expr_Unaire)
		{
			CIAttribut attr;
			CIDeclare_Var var,var2,var3;//&é"'(-è
			Feature f;
			Attribut_Complet ac;
			CIType type;
			NomFeature nf;
			tinyeiffel.ast.Expr_Unaire ex=(tinyeiffel.ast.Expr_Unaire)e;
			Vector v=new Vector();
			CIExpr_Var cible;
			CIResult_Expr res2;
			if(ex.op!=tinyeiffel.ast.Expr.Old)
			{
				res2=convertie_expr(liste,ex.expr1);
				//assert(res2!=null);
				if(res2!=null)
				{
				if(res2.a_instr())
					v.addAll(res2.instr);
				type=res2.expr.type;
				nf=ex.donne_nom_feature();
				var2=donne_attr(nf,type/*ac.getType().nom*/);
				//if(ac!=null&&ac.getFeature().est_routine())
				assert(var2!=null):"nf="+nf+";type="+type;
				assert(est_routine(nf,type));
				tinyeiffel.ast.Expr param[];
				param=null;
				//cible=(Expr_Var)convertie_scalaire(r,res2.expr);
				if(res2.expr instanceof CIExpr_Var)
				{
					cible=(CIExpr_Var)res2.expr;
				}
				else
				{
					CIDeclare_Var v2;
					CIExpr_Var es;
					CIInstruction instruction;
					//v2=r.ajoute_var_local(temp("tmp"),res2.expr.type);
					v2=liste.ajoute(temp("tmp"),res2.expr.type);
					es=convertie_expr(v2);
					instruction=new CIInstruction_Affect((CIExpr_Var)es,
							res2.expr,false,null);
					v.addElement(instruction);
					cible=es;
				}
				var=ajoute_routine(liste,convertie_nom_feature(nf),var2.type,v,cible,param);
				assert(var!=null);
				exp=new CIExpr_Var(var.nom,var.source);
				exp.type=var.type;
				res=new CIResult_Expr(v,exp);
				}
			}
			else
			{// operation old
				
			}
		}
		else if(e instanceof tinyeiffel.ast.Expr_Tableau)
		{// TODO : a faire
			
		}
		return res;
	}

	protected CIExpr convertie_expr_simple(tinyeiffel.ast.Expr expr)
	{
		CIExpr res=null;
		assert(expr!=null);
		if(expr instanceof tinyeiffel.ast.Expr_Car)
		{
			tinyeiffel.ast.Expr_Car car=(tinyeiffel.ast.Expr_Car)expr;
			char c;
			if(car.car.length()==3)
			{
				c=car.car.charAt(1);
			}
			else
			{
				c=' ';
				assert(false);
			}
			res=new CIExpr_Char(c,convertie_source(car.debut()));
			res.type=new CITypeSimple(false,"CHARACTER",null,null,null);
		}
		else if(expr instanceof tinyeiffel.ast.Expr_Chaine)
		{
			tinyeiffel.ast.Expr_Chaine ch=(tinyeiffel.ast.Expr_Chaine)expr;
			String s;
			s=ch.simplifie();
			res=new CIExpr_String(s,convertie_source(ch.debut()));
			res.type=new CITypeSimple(false,"STRING",null,null,null);
		}
		else if(expr instanceof tinyeiffel.ast.Expr_Entier)
		{
			tinyeiffel.ast.Expr_Entier e=(tinyeiffel.ast.Expr_Entier)expr;
			int i;
			try {
				i=Integer.parseInt(e.str);
			}
			catch(NumberFormatException ex)
			{
				ex.printStackTrace();
				i=0;
				assert(false);
			}
			res=new CIExpr_Entier(i,convertie_source(e.debut()));
			res.type=new CITypeSimple(false,"INTEGER",null,null,null);
		}
		else if(expr instanceof tinyeiffel.ast.Expr_Faux)
		{
			res=new CIExpr_Bool(false,convertie_source(expr.debut()));
			res.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
		}
		else if(expr instanceof tinyeiffel.ast.Expr_Vrai)
		{
			res=new CIExpr_Bool(true,convertie_source(expr.debut()));
			res.type=new CITypeSimple(false,"BOOLEAN",null,null,null);
		}
		else if(expr instanceof tinyeiffel.ast.Expr_Reel)
		{// TODO voir pour les nombres doubles et reels
			tinyeiffel.ast.Expr_Reel car=(tinyeiffel.ast.Expr_Reel)expr;
			double d;
			try {
				d=Double.parseDouble(car.str);
			}
			catch(NumberFormatException ex)
			{
				ex.printStackTrace();
				d=0.0;
				assert(false);
			}
			res=new CIExpr_Real(d,convertie_source(expr.debut()));
			// TODO : prendre en compte le type double
			res.type=new CITypeSimple(false,"REAL",null,null,null);
		}
		else
		{
			assert(false);
		}
		return res;
	}

	protected CINom_Attribut convertie_nom_feature(NomFeature nom)
	{
		CINom_Attribut t=null;
		int i;
		String n;
		assert(nom!=null);
		if(nom.infix)
		{
			if(nom.nom2!=null)
				n=nom.nom2.chaine_unique();
			else
				n=nom.nom;
			t=new CINom_Attribut(n,true,null);
		}	
		else if(nom.prefix)
		{
			//assert(nom.nom!=null):"nom="+nom;
			//assert(nom.nom2!=null):"nom="+nom;
			if(nom.nom2!=null)
				n=nom.nom2.chaine_unique();
			else
				n=nom.nom;
			t=new CINom_Attribut(n,false,convertie_source(nom.debut));
		}
		else
			t=new CINom_Attribut(nom.nom,convertie_source(nom.debut));
		return t;
	}

	Compiler_Eiffel compiler;
	Context context;
	CIProgramme routines_internes;

}
