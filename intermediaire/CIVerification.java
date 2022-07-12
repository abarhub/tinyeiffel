/*
 * Created on 5 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.intermediaire;

import java.util.Vector;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CIVerification {

	/**
	 * 
	 */
	public CIVerification(CIProgramme p) {
		assert(p!=null);
		programme=p;
		erreur=false;
		msg_erreur=new Vector();
	}

	public boolean verification()
	{
		boolean res=true;
		int i;
		if(programme.liste_classe!=null&&
				programme.liste_classe.length>0)
		{
			for(i=0;i<programme.liste_classe.length;i++)
			{
				if(!verif_classe(programme,i))
				{// probleme avec la classe no i
					res=false;
					break;
				}
				
			}
		}
		assert(res!=erreur):erreur+"!="+res;
		return res;
	}
	
	protected boolean verif_classe(CIProgramme programme,int no_classe)
	{
		assert(programme!=null);
		boolean res=true;
		int i,j;
		String s,s2;
		Vector liste_attr;
		CIEnvironnement env=null;
		CIClasse cl;
		cl=programme.liste_classe[no_classe];
		assert(cl!=null);
		if(cl.nom==null)
		{// pas de nom de classe
			res=false;
			ajoute_erreur(env,"la classe no "+no_classe+" n'a pas de nom");
		}
		else
		{
			s=cl.nom.nom;
			if(cl.nom.contrainte!=null)
			{
				ajoute_erreur(env,"Le nom de classes "+cl.nom.nom+
						" ne peut avoir de contrainte");
				return false;
			}
			env=new CIEnvironnement(cl,programme);
			for(i=0;i<no_classe;i++)
			{
				if(s.equalsIgnoreCase(programme.liste_classe[i].nom.nom))
				{// une classe avec le meme nom existe deja
					ajoute_erreur(env,"Les classes no "+no_classe+" et "+i+
							" ont le meme nom");
					return false;
				}
			}
			if(cl.heritage!=null&&cl.heritage.length>0)
			{
				// verification des heritages
				for(i=0;i<cl.heritage.length;i++)
				{
					env.entre_heritage(cl.heritage[i]);
					if(!type_existe(cl.heritage[i]))
					{// le type n'existe pas
						ajoute_erreur(env,"Le type "+cl.heritage[i]+" n'existe pas");
						return false;
					}
					else if(!type_sans_contrainte(cl.heritage[i]))
					{// le type a des contraintes
						ajoute_erreur(env,"Le type avec contrainte "+
								cl.heritage[i]+" est interdit");
						return false;
					}
					env.sort_heritage();
				}
			}
			if(cl.creation!=null&&cl.creation.length>0)
			{// verification de la section creation
				CICreation c;
				CIType t;
				
				for(i=0;i<cl.creation.length;i++)
				{
					c=cl.creation[i];
					env.entre_creation(c,i);
					if(c.type!=null&&c.type.length>0)
					{
						for(j=0;j<c.type.length;j++)
						{
							t=c.type[j];
							if(!type_existe(t))
							{// le type n'existe pas
								ajoute_erreur(env,"Le type "+t+" n'existe pas");
								return false;
							}
						}
					}
					if(c.nom!=null&&c.nom.length>0)
					{
						int k,l;
						CINom_Attribut attr,attr2;
						CIAttribut attr3;
						for(j=0;j<c.nom.length;j++)
						{
							attr=c.nom[j];
							for(k=0;k<i;k++)
							{// Vérification de l'unicité du nom
								CINom_Attribut a1;
								CICreation c2;
								c2=cl.creation[k];
								for(l=0;l<c2.nom.length;l++)
								{
									//a1=c.nom;
									//a2.cl.creation[j].nom;
									if(attr.equals(c2.nom[l]))
									{
										ajoute_erreur(env,"Le nom de création est " +
												"en plusieurs exemplaires");
										return false;
									}
								}
							}
							for(k=0;k<j;k++)
							{
								if(attr.equals(c.nom[k]))
								{
									ajoute_erreur(env,"Le nom de création est " +
											"en plusieurs exemplaires");
									return false;
								}
							}
							attr3=cl.donne_attribut(attr);
							if(attr3==null)
							{// l'attribut n'existe pas
								ajoute_erreur(env,"L'attribut "+attr+" n'existe pas");
								return false;
							}
						}
					}
					env.sort_creation();
				}
			}
			if(cl.liste_attribut!=null&&cl.liste_attribut.length>0)
			{
				liste_attr=new Vector();
				for(i=0;i<cl.liste_attribut.length;i++)
				{
					env.entre_attribut(cl.liste_attribut[i].nom);
					s2=cl.liste_attribut[i].nom.toString();
					if(liste_attr.contains(s2))
					{// l'attribut est en double
						ajoute_erreur(env,"L'attribut "+s2+" est présent plusieurs fois"+
							" dans la classe "+s);
						return false;
					}
					else 
					{
						liste_attr.add(s2);
					}
					if(!verif_attribut(cl.liste_attribut[i],env))
					{
						return false;
					}
					env.sort_attribut();
				}
			}
			if(cl.invariant!=null)
			{
				env.init_instr();
				if(!assertion(env, cl.invariant,CIEnvironnement.CInvariant))
					return false;
			}
		}
		return res;
	}
	
	protected boolean verif_attribut(CIAttribut attr,CIEnvironnement env)
	{
		assert(attr!=null);
		assert(env!=null);
		if(attr.est_static())
		{
			if(attr.est_routine())
			{
				ajoute_erreur(env,"L'attribut static "+attr.nom+
						" ne peut pas être une routine");
				return false;
			}
			if(attr.attribut_ascendant!=null)
			{
				ajoute_erreur(env,"L'attribut static "+attr.nom+
					" ne peut pas être une redefinition");
				return false;
			}
			if(attr.attribut_descendant!=null)
			{
				ajoute_erreur(env,"L'attribut static "+attr.nom+
					" ne peut pas être redefinie");
				return false;
			}
		}
		if(attr.est_routine())
		{
			CIRoutine r;
			int i;
			CIDeclare_Var d;
			r=attr.routine;
			if(r.parametre!=null&&r.parametre.size()>0)
			{
				/*for(i=0;i<r.parametre.size();i++)
				{
					d=r.parametre.elt(i);
					if(!type_existe(d.type))
					{
						ajoute_erreur(env,"Le type "+d.type+" n'existe pas");
						return false;
					}
					else if(!type_sans_contrainte(d.type))
					{
						ajoute_erreur(env,"Le type avec contrainte "+
								d.type+" est interdit");
						return false;
					}
					env.ajoute_var_local(d);
				}*/
				if(!verifie_declaration(env, r.parametre,false))
				{
					return false;
				}
			}
			if(r.local!=null&&r.local.size()>0)
			{
				if(!verifie_declaration(env, r.local,false))
				{
					return false;
				}
			}
			if(r.retour!=null)
			{
				if(!type_existe(r.retour.type))
				{
					ajoute_erreur(env,"Le type "+r.retour.type+" n'existe pas");
					return false;
				}
				else if(!type_sans_contrainte(r.retour.type))
				{
					ajoute_erreur(env,"Le type avec contrainte "+
							r.retour.type+" est interdit");
					return false;
				}
				//d=new CIDeclare_Var("$Result",r.retour.type,null);
				env.ajoute_var_local(r.retour);
			}
			if(r.precondition!=null)
			{
				env.init_instr();
				if(!assertion(env, r.precondition,CIEnvironnement.CPrecondition))
					return false;
			}
			if(r.liste_instruction!=null)
			{
				env.init_instr();
				if(!verif_instr(r.liste_instruction,env))
				{
					return false;
				}
			}
			if(r.postcondition!=null)
			{
				env.init_instr();
				if(!assertion(env, r.postcondition,CIEnvironnement.CPostcondition))
					return false;
			}
		}
		else if(attr.est_descendant())
		{
			if(attr.attribut_ascendant!=null&&attr.attribut_ascendant.length>0)
			{
				CIClasse cl,cl2;
				int i;
				CIAttribut_Ascendant asc;
				CINom_Attribut nom;
				CITypeSimple h,param[],param2[];
				CIAttribut attr2;
				cl=env.classe;
				for(i=0;i<attr.attribut_ascendant.length;i++)
				{
					asc=attr.attribut_ascendant[i];
					if(asc==null)
					{
						ajoute_erreur(env,"L'attribut ascendant est vide");
						return false;
					}
					if(cl.heritage==null||
						asc.no<0||asc.no>=cl.heritage.length)
					{
						ajoute_erreur(env,"Il n'y a pas d'heritage no "+asc.no);
						return false;
					}
					h=cl.heritage[asc.no];
					if(h==null)
					{
						ajoute_erreur(env,"L'heritage no "+asc.no+" est vide !");
						return false;
					}
					cl2=donne_classe(h.nom);
					if(cl2==null)
					{
						ajoute_erreur(env,"La classe "+h.nom+" n'existe pas !");
						return false;
					}
					if(asc.nom!=null)
					{
						nom=asc.nom;
					}
					else
					{
						nom=attr.nom;
					}
					attr2=cl2.donne_attribut(nom);
					if(attr2==null)
					{
						ajoute_erreur(env,"L'attribut "+nom+" n'existe pas dans la classe "+h.nom+" !");
						return false;
					}
					else
					{// vérification des paramètres
						/*if(param!=null&&param.length>0)
						{
							for(j=0;j<param.length&&!erreur;j++)
							{
								type=attr.routine.parametre.elt(j).type;
								assert(type!=null);
								if(!type_compatible(type,param[j]))
								{
									ajoute_erreur(env,"type incompatible dans le parametre no "+j+" pour l'appel "+attr);
									return false;
								}
							}
						}*/
					}
				}
			}
			else
			{// erreur ce n'est pas un attribut descendant
				
			}
		}
		return true;
	}

	private boolean assertion(CIEnvironnement env, CIAssertion a,int type_assertion) {
		assert(a!=null);
		env.entre_assertion(type_assertion);
		if(a.liste!=null)
		{
			if(!verifie_declaration(env, a.liste,true))
			{
				env.supprime_var_temp();
				env.sort_assertion();
				return false;
			}
		}
		if(a.liste_instr!=null)
		{
			if(!verif_instr(a.liste_instr,env))
			{
				env.supprime_var_temp();
				env.sort_assertion();
				return false;
			}
		}
		env.supprime_var_temp();
		env.sort_assertion();
		return true;
	}

	private boolean verifie_declaration(CIEnvironnement env, 
			CIListe_Var liste,boolean temporaire) {
		int i;
		CIDeclare_Var d;
		for(i=0;i<liste.size();i++)
		{
			d=liste.elt(i);
			if(d.type instanceof CITypeSimple)
			{
				if(!type_existe(d.type))
				{
					ajoute_erreur(env,"Le type "+d.type+" n'existe pas");
					return false;
				}
				else if(!type_sans_contrainte(d.type))
				{
					ajoute_erreur(env,"Le type avec contrainte "+
							d.type+" est interdit");
					return false;
				}
			}
			if(temporaire)
				env.ajoute_var_local_temporaire(d);
			else
				env.ajoute_var_local(d);
		}
		return true;
	}
	
	/*private CITypeSimple trouve_vrai_type(CIType type, CIClasse cl,
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
	}*/
	
	/**
	 * @param liste_instruction
	 * @param env
	 * @return
	 */
	private boolean verif_instr(CIListe_Instr liste_instruction, CIEnvironnement env) 
	{// TODO: a faire
		assert(liste_instruction!=null);
		assert(env!=null);
		int i;
		CIInstruction ins;
		Vector liste_label,liste_goto;
		String s;
		liste_label=new Vector();
		liste_goto=new Vector();
		for(i=0;i<liste_instruction.size();i++)
		{
			ins=liste_instruction.elt(i);
			env.entre_instr();
			if(ins instanceof CIInstruction_Label)
			{
				CIInstruction_Label ins2=(CIInstruction_Label)ins;
				s=ins2.nom;
				if(liste_label.contains(s))
				{
					ajoute_erreur(env,"Le label "+s+" est declaré 2 fois");
					return false;
				}
				else
				{
					liste_label.add(s);
				}
			}
			else if(ins instanceof CIInstruction_Return)
			{
				CIInstruction_Return ins2=(CIInstruction_Return)ins;
			}
			else if(ins instanceof CIInstruction_Affect)
			{
				CIInstruction_Affect ins2=(CIInstruction_Affect)ins;
				CITypeSimple type,type2;
				if(!var_existe(ins2.nom.nom,env))
				{
					ajoute_erreur(env,"La variable "+ins2.nom.nom+" n'existe pas");
					return false;
				}
				type2=(CITypeSimple)env.var_type(ins2.nom.nom);
				if(type2==null)
				{
					ajoute_erreur(env,"La variable n'a pas de type");
					return false;
				}
				type=verif_expr(ins2.expr,env);
				if(type==null)
				{
					ajoute_erreur(env,"L'expression n'a pas de type");
					return false;
				}
				if(type2!=null&&type2.nom.equalsIgnoreCase("boolean")/*
					&&type!=null&&type.nom.equalsIgnoreCase("$boolean")*/)
				{
					System.out.println("Trouve!");
				}
				if(!ins2.force&&!type_compatible(type2,type))
				{
					ajoute_erreur(env,"L'expression de type "+type+" n'est pas compatible avec la variable "+ins2.nom.nom+ " de type "+type2);
					return false;
				}
			}
			else if(ins instanceof CIInstruction_Appel)
			{// TODO: a faire
				CIInstruction_Appel ins2=(CIInstruction_Appel)ins;
				CIType type,param[];
				CIClasse cl=null;
				int j;
				if(ins2.cible!=null)
				{
					type=verif_expr(ins2.cible,env);
					if(type==null)
					{
						ajoute_erreur(env,"Erreur dans la cible de l'appel");
						return false;
					}
					else
					{
						cl=env.programme.donne_classe(type);
						assert(cl!=null);
					}
				}
				else
				{
					cl=env.classe;
					assert(cl!=null);
				}
				if(ins2.param!=null&&ins2.param.length>0)
				{
					param=new CIType[ins2.param.length];
					for(j=0;j<param.length;j++)
					{
						param[j]=verif_expr(ins2.param[j],env);
					}
				}
				else
				{
					param=null;
				}
				// TODO: trouver l'attribut et verifier les parametres
				if(cl!=null)
				{
					CIAttribut attr;
					attr=cl.donne_attribut(ins2.nom);
					if(attr==null)
					{
						ajoute_erreur(env,"Impossible de trouver l'attribut "+ins2.nom+" dans la classe "+cl.nom.nom);
						return false;
					}
					else
					{
						if(attr.routine!=null&&attr.routine.parametre!=null&&
								attr.routine.parametre.size()>0)
						{
							if(param==null||
									param.length!=attr.routine.parametre.size())
							{
								ajoute_erreur(env,"Le nombre de parametres ne correspond pas pour l'appel "+attr);
								return false;
							}
						}
						else
						{
							if(param!=null&&
									param.length>0)
							{
								ajoute_erreur(env,"Le nombre de parametres ne correspond pas pour l'appel "+attr);
								return false;
							}
						}
						if(!erreur)
						{
							if(attr.est_descendant())
							{
								CIAttribut attr2;
								assert(attr.attribut_ascendant!=null);
								assert(attr.attribut_ascendant.length==1);
								attr2=attr.attribut_ascendant[0].donne_attribut(ins2.nom,cl,programme);
								assert(attr2!=null);
								if(attr2.retour!=null)
								{
									ajoute_erreur(env,"l'appel "+attr+" retourne quelque chose");
									return false;
								}
							}
							else
							{
								if(attr.retour!=null)
								{
									ajoute_erreur(env,"l'appel "+attr+" retourne quelque chose");
									return false;
								}
							}
							if(param!=null&&param.length>0)
							{
								for(j=0;j<param.length&&!erreur;j++)
								{
									type=attr.routine.parametre.elt(j).type;
									assert(type!=null);
									if(!type_compatible(type,param[j]))
									{
										ajoute_erreur(env,"type incompatible dans le parametre no "+j+" pour l'appel "+attr);
										return false;
									}
								}
							}
						}
					}
				}
			}
			else if(ins instanceof CIInstruction_Goto)
			{
				CIInstruction_Goto ins2=(CIInstruction_Goto)ins;
				s=ins2.nom;
				if(!liste_label.contains(s))
				{// on ne sais pas si il existe => on testera a la fin
					liste_goto.add(s);
				}
			}
			else if(ins instanceof CIInstruction_Raise)
			{
				CIInstruction_Raise ins2=(CIInstruction_Raise)ins;
				CITypeSimple type,type2;
				// Verifier que le type est correcte
				type=verif_expr(ins2.no,env);
				if(type==null)
				{
					return false;
				}
				if(!type_compatible(type_pinteger,type)
					&&!type_compatible(type_integer,type))
				{
					ajoute_erreur(env,"type non entier pour l'instruction raise");
					return false;
				}
				if(ins2.nom!=null)
				{
					type2=verif_expr(ins2.nom,env);
					if(type2==null)
						return false;
				}
			}
			else if(ins instanceof CIInstruction_Si_Non)
			{
				CIInstruction_Si_Non ins2=(CIInstruction_Si_Non)ins;
				CITypeSimple type;
				s=ins2.label;
				if(!liste_label.contains(s))
				{// on ne sais pas si il existe => on testera a la fin
					liste_goto.add(s);
				}
				type=verif_expr(ins2.expr,env);
				if(type==null)
				{
					return false;
				}
				if(!type_compatible(type_pboolean,type)
					&&!type_compatible(type_boolean,type))
				{
					ajoute_erreur(env,"type non booleen pour l'instruction si_non");
					return false;
				}
			}
			else if(ins instanceof CIInstruction_Si)
			{
				CIInstruction_Si ins2=(CIInstruction_Si)ins;
				CITypeSimple type;
				s=ins2.label;
				if(!liste_label.contains(s))
				{// on ne sais pas si il existe => on testera a la fin
					liste_goto.add(s);
				}
				type=verif_expr(ins2.expr,env);
				if(type==null)
				{
					return false;
				}
				if(!type_compatible(type_pboolean,type)
					&&!type_compatible(type_boolean,type))
				{
					ajoute_erreur(env,"type non booleen pour l'instruction si_non");
					return false;
				}
			}
			else if(ins instanceof CIInstruction_Extern)
			{// TODO: a faire
				CIInstruction_Extern ins2=(CIInstruction_Extern)ins;
				CIType type,param[];
				CIClasse cl=null;
				int j;
				if(ins2.cible!=null)
				{
					type=verif_expr(ins2.cible,env);
					if(type==null)
					{
						ajoute_erreur(env,"Erreur dans la cible de l'appel");
						return false;
					}
					else
					{
						cl=env.programme.donne_classe(type);
						assert(cl!=null);
					}
				}
				else
				{
					cl=env.classe;
					assert(cl!=null);
				}
				if(ins2.param!=null&&ins2.param.length>0)
				{
					param=new CIType[ins2.param.length];
					for(j=0;j<param.length;j++)
					{
						param[j]=verif_expr(ins2.param[j],env);
					}
				}
				else
				{
					param=null;
				}
			}
			else
			{
				assert(false);
			}
			env.sort_instr();
		}
		if(liste_goto.size()>0)
		{
			for(i=0;i<liste_goto.size();i++)
			{
				s=(String)liste_goto.elementAt(i);
				if(!liste_label.contains(s))
				{
					ajoute_erreur(env,"Le label "+s+" n'existe pas");
					return false;
				}
			}
		}
		return true;
	}

	public CITypeSimple verif_expr(CIExpr expr,CIEnvironnement env)
	{
		assert(expr!=null):env.toString();
		assert(env!=null);
		CITypeSimple res=null;
		int passe=0;
		if(expr instanceof CIExpr_Char)
		{
			res=type_pcharacter;
		}
		else if(expr instanceof CIExpr_Bool)
		{
			res=type_pboolean;
		}
		else if(expr instanceof CIExpr_Appel)
		{
			CIExpr_Appel e=(CIExpr_Appel)expr;
			CIType type,param[];
			CIClasse cl=null;
			int i;
			if(e.cible!=null)
			{
				type=verif_expr(e.cible,env);
				if(type==null)
				{
					ajoute_erreur(env,"Erreur dans la cible de la création");
				}
				else
				{
					cl=env.programme.donne_classe(type);
					assert(cl!=null);
				}
			}
			else
			{
				cl=env.classe;
				assert(cl!=null);
			}
			res = verif_appel(env, cl, e);
		}
		else if(expr instanceof CIExpr_Binaire)
		{
			CIExpr_Binaire e=(CIExpr_Binaire)expr;
			CIType type1,type2;
			type1=verif_expr(e.expr1,env);
			if(type1==null)
			{
				ajoute_erreur(env,"operation binaire impossible avec le 1er opérande");
			}
			else
			{
				type2=verif_expr(e.expr2,env);
				if(type2==null)
				{
					ajoute_erreur(env,"operation binaire impossible avec le 2eme opérande");
				}
				else
				{
					if(type_compatible(type1,type_pinteger)&&
						type_compatible(type2,type_pinteger))
					{
						switch(e.op)
						{
							case CIExpr_Binaire.Plus:
							case CIExpr_Binaire.Moins:
							case CIExpr_Binaire.Fois:
							case CIExpr_Binaire.Div_entier:
							case CIExpr_Binaire.Mod:
							case CIExpr_Binaire.Puiss:
								res=type_pinteger;
								break;
							case CIExpr_Binaire.Div_reel:
								res=type_pdouble;
								break;
							case CIExpr_Binaire.Diff:
							case CIExpr_Binaire.Egal:
							case CIExpr_Binaire.Inf:
							case CIExpr_Binaire.InfS:
							case CIExpr_Binaire.Sup:
							case CIExpr_Binaire.SupS:
								res=type_pboolean;
								break;
							default:
								ajoute_erreur(env,"operation binaire impossible avec cette opération");
						}
					}
					else if(type_compatible(type1,type_pboolean)&&
							type_compatible(type2,type_pboolean))
					{
						switch(e.op)
						{
							case CIExpr_Binaire.And:
							case CIExpr_Binaire.And_Then:
							case CIExpr_Binaire.Diff:
							case CIExpr_Binaire.Egal:
							case CIExpr_Binaire.Xor:
							case CIExpr_Binaire.Or:
							case CIExpr_Binaire.Or_Else:
							case CIExpr_Binaire.Implies:
								res=type_pboolean;
								break;
							default:
								ajoute_erreur(env,"operation binaire impossible avec cette opération");
						}
					}
					else if(type_compatible(type1,type_preal)&&
							type_compatible(type2,type_preal))
					{
						switch(e.op)
						{
							case CIExpr_Binaire.Plus:
							case CIExpr_Binaire.Moins:
							case CIExpr_Binaire.Fois:
							case CIExpr_Binaire.Div_reel:
								res=type_preal;
								break;
							case CIExpr_Binaire.Diff:
							case CIExpr_Binaire.Egal:
							case CIExpr_Binaire.Inf:
							case CIExpr_Binaire.InfS:
							case CIExpr_Binaire.Sup:
							case CIExpr_Binaire.SupS:
								res=type_pboolean;
								break;
							case CIExpr_Binaire.Puiss:
								res=type_pdouble;
								break;
							default:
									ajoute_erreur(env,"operation binaire impossible avec cette opération");
						}
					}
					else if(type_compatible(type1,type_pdouble)&&
							type_compatible(type2,type_pdouble))
					{
						switch(e.op)
						{
							case CIExpr_Binaire.Plus:
							case CIExpr_Binaire.Moins:
							case CIExpr_Binaire.Fois:
							case CIExpr_Binaire.Div_reel:
							case CIExpr_Binaire.Puiss:
								res=type_pdouble;
								break;
							case CIExpr_Binaire.Diff:
							case CIExpr_Binaire.Egal:
							case CIExpr_Binaire.Inf:
							case CIExpr_Binaire.InfS:
							case CIExpr_Binaire.Sup:
							case CIExpr_Binaire.SupS:
								res=type_pboolean;
								break;
							default:
									ajoute_erreur(env,"operation binaire impossible avec cette opération");
						}
					}
					else if(type_compatible(type1,type_pcharacter)&&
							type_compatible(type2,type_pcharacter))
					{
						switch(e.op)
						{
							case CIExpr_Binaire.Diff:
							case CIExpr_Binaire.Egal:
							case CIExpr_Binaire.Inf:
							case CIExpr_Binaire.InfS:
							case CIExpr_Binaire.Sup:
							case CIExpr_Binaire.SupS:
								res=type_pboolean;
								break;
							default:
								ajoute_erreur(env,"operation binaire impossible avec cette opération");
						}
					}
					else if(e.op==CIExpr_Binaire.Diff||e.op==CIExpr_Binaire.Egal)
					{// Difference ou egalité toujours possible si meme type
						if(type_compatible(type1,type2)||
								type_compatible(type2,type1))
						{
							res=type_pboolean;
						}
						else
						{
							ajoute_erreur(env,"operation binaire "+type1+"("+e.op+")"+type2+" impossible avec types différents");
						}
					}
					else if(e.op==CIExpr_Binaire.Puiss)
					{
						if(type_compatible(type1,type_pinteger))
						{
							if(type_compatible(type2,type_pinteger))
							{
								res=type_pinteger;
							}
							else if(type_compatible(type2,type_pdouble)
								||type_compatible(type2,type_preal))
							{
								res=type_pdouble;
							}
							else
							{
								ajoute_erreur(env,"operation binaire impossible avec ces opérandes");
							}
						}
						else if((type_compatible(type1,type_pdouble)
							||type_compatible(type1,type_preal))&&
							(type_compatible(type2,type_pinteger)
							||type_compatible(type2,type_pdouble)
							||type_compatible(type2,type_preal)))
						{
							res=type_pdouble;
						}
						else
						{
							ajoute_erreur(env,"operation binaire impossible avec ces opérandes");
						}
					}
					else
					{
						ajoute_erreur(env,"operation binaire "+type1+"("+e.op+")"+type2+" impossible");
					}
				}
			}
		}
		else if(expr instanceof CIExpr_Creation)
		{
			CIExpr_Creation e=(CIExpr_Creation)expr;
			CIType type1,type2;
			CIClasse cl=null;
			//type=verif_expr(e.index,env);
			if(e.index!=null)
			{
				type1=verif_expr(e.index,env);
				if(type1==null)
				{
					ajoute_erreur(env,"index invalide");
				}
				else if(!type_compatible(type1,type_pinteger))
				{
					ajoute_erreur(env,"index non entier");
				}
				else
				{
					if(!type_existe(e.type))
					{
						ajoute_erreur(env,"type invalide");
					}
				}
			}
			else
			{
				if(!type_existe(e.type))
				{
					ajoute_erreur(env,"type invalide");
				}
				else
				{
					if(e.appel!=null)
					{

						cl=env.programme.donne_classe(e.type);
						assert(cl!=null);
						type1 = verif_appel(env, cl, e.appel);
						//=verif_expr(e.appel,env);
						if(type1!=null)
						{// la routine de création ne doit rien retournée
							ajoute_erreur(env,"type invalide");
						}
						else
						{
							res=(CITypeSimple)e.type;
						}
					}
					else
					{
						res=(CITypeSimple)e.type;
					}
				}
			}
		}
		else if(expr instanceof CIExpr_Entier)
		{
			res=type_pinteger;
		}
		else if(expr instanceof CIExpr_Real)
		{
			res=type_preal;
		}
		else if(expr instanceof CIExpr_Double)
		{
			res=type_pdouble;
		}
		else if(expr instanceof CIExpr_String)
		{
			res=type_pstring;
		}
		else if(expr instanceof CIExpr_Type)
		{
			CIExpr_Type e=(CIExpr_Type)expr;
			CIType type;
			type=verif_expr(e.var,env);
			if(type==null)
			{
				ajoute_erreur(env,e.var+" n'a pas de type");
			}
			else
			{
				if(!type_existe(e.type))
				{
					ajoute_erreur(env,"type "+e.type+" invalide");
				}
				else
				{
					res=type_pboolean;
				}
			}
		}
		else if(expr instanceof CIExpr_Unaire)
		{
			CIExpr_Unaire e=(CIExpr_Unaire)expr;
			CIType type;
			type=verif_expr(e.expr,env);
			if(e.op==CIExpr_Unaire.Dollard)
			{
				res=type_none;
			}
			else if(type==null)
			{
				ajoute_erreur(env,"operation unaire impossible");
			}
			else
			{
				if(type_compatible(type,type_pinteger))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Plus:
						case CIExpr_Unaire.Moins:
							res=type_pinteger;
							break;
						case CIExpr_Unaire.Conv_E2D:
							res=type_pdouble;
							break;
						case CIExpr_Unaire.Conv_E2R:
							res=type_preal;
							break;
						case CIExpr_Unaire.Conv_E2C:
							res=type_pcharacter;
							break;
						case CIExpr_Unaire.Old:
							res=type_pinteger;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else if(type_compatible(type,type_pboolean))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Not:
							res=type_pboolean;
							break;
						case CIExpr_Unaire.Old:
							res=type_pboolean;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else if(type_compatible(type,type_preal))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Conv_R2D:
							res=type_pdouble;
							break;
						case CIExpr_Unaire.Conv_R2E:
							res=type_pinteger;
							break;
						case CIExpr_Unaire.Old:
							res=type_preal;
							break;
						case CIExpr_Unaire.Plus:
						case CIExpr_Unaire.Moins:
							res=type_preal;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else if(type_compatible(type,type_pdouble))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Conv_D2R:
							res=type_preal;
							break;
						case CIExpr_Unaire.Conv_D2E:
							res=type_pinteger;
							break;
						case CIExpr_Unaire.Old:
							res=type_pdouble;
							break;
						case CIExpr_Unaire.Plus:
						case CIExpr_Unaire.Moins:
							res=type_pdouble;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else if(type_compatible(type,type_pcharacter))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Conv_C2E:
							res=type_pinteger;
							break;
						case CIExpr_Unaire.Old:
							res=type_pcharacter;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else if(type_existe(type))
				{
					switch(e.op)
					{
						case CIExpr_Unaire.Old:
							res=(CITypeSimple)type;
							break;
						default:
							ajoute_erreur(env,"operation unaire impossible");
					}
				}
				else
				{
					assert(false);
				}
			}
		}
		else if(expr instanceof CIExpr_Var)
		{
			CIExpr_Var e=(CIExpr_Var)expr;
			CIType t1,t2,t3;
			passe=1;
			if(e.a_src())
			{
				passe=2;
				if(!env.var_existe(e.src))
				{// Erreur
					passe=3;
					ajoute_erreur(env,"la variable "+e.src+" n'existe pas");
				}
				else
				{
					passe=3;
					t1=env.var_type(e.src);
					if(t1==null)
					{// Erreur
						passe=4;
						res=null;
					}
					else
					{
						CIClasse cl;
						CIAttribut attr;
						passe=5;
						cl=programme.donne_classe(t1);
						attr=cl.donne_attribut(e.nom);
						if(attr==null)
						{// Erreur
							passe=6;
							ajoute_erreur(env,"la variable "+e.nom+" n'existe pas dans la classe "+cl.nom.nom);
						}
						else
						{
							passe=7;
							res=attr.type;
						}
					}
				}
			}
			else
			{
				passe=8;
				if(!env.var_existe(e.nom))
				{// Erreur
					assert(false):"nom="+e.nom;
					ajoute_erreur(env,"la variable "+e.nom+" n'existe pas");
				}
				else
				{
					CIType t;
					passe=9;
					t=env.var_type(e.nom);
					res=donne_vrai_type(env, t);
					if(res==null)
					{
						res=(CITypeSimple)env.var_type(e.nom);
					}
					assert(res!=null):e.nom+":"+env.toString();
				}
			}
			if(e.a_indice())
			{
				passe=10;
				t3=verif_expr(e.indice,env);
				if(t3==null)
				{
					if(!erreur)
					{
						ajoute_erreur(env,"l'indice n'est pas correcte");
					}
					return null;
				}
				else
				{
					if(!type_compatible(t3,type_pinteger))
					{
						ajoute_erreur(env,"l'indice n'est pas du type $integer");
					}
				}
			}
		}
		else if(expr instanceof CIExpr_Void)
		{
			res=type_none;
		}
		else if(expr instanceof CIExpr_Extern)
		{
			CIExpr_Extern e=(CIExpr_Extern)expr;
			CIType type,param[];
			CIClasse cl=null;
			int i;
			//assert(e.langage!=null)
			if(e.cible!=null)
			{
				type=verif_expr(e.cible,env);
				if(type==null)
				{
					ajoute_erreur(env,"Erreur dans la cible de l'appel extern");
				}
				else
				{
					cl=env.programme.donne_classe(type);
					assert(cl!=null);
				}
			}
			else
			{
				cl=env.classe;
				assert(cl!=null);
			}
			if(e.parametre!=null&&e.parametre.length>0)
			{
				param=new CIType[e.parametre.length];
				for(i=0;i<param.length;i++)
				{
					param[i]=verif_expr(e.parametre[i],env);
				}
			}
			else
			{
				param=null;
			}
			res=(CITypeSimple)e.type_retour;
			if(res==null)
			{
				ajoute_erreur(env,"Le type de retour de l'appel extern n'existe pas");
			}
		}
		else if(expr instanceof CIExpr_Var_Syst)
		{
			CIExpr_Var_Syst e=(CIExpr_Var_Syst)expr;
			CIType type,param[];
			CIClasse cl=null;
			int i;
			if(e.nom!=null&&e.nom.equals("$niveau_verif"))
			{
				res=type_boolean;
			}
			else if(e.nom!=null&&e.nom.equals("$niveau_verif_debug"))
			{
				res=type_boolean;
			}
			else
			{
				res=null;
				ajoute_erreur(env,"La variable system "+e+" n'existe pas");
			}
			//assert(e.langage!=null)
			/*if(e.cible!=null)
			{
				type=verif_expr(e.cible,env);
				if(type==null)
				{
					ajoute_erreur(env,"Erreur dans la cible de l'appel extern");
				}
				else
				{
					cl=env.programme.donne_classe(type);
					assert(cl!=null);
				}
			}
			else
			{
				cl=env.classe;
				assert(cl!=null);
			}
			if(e.parametre!=null&&e.parametre.length>0)
			{
				param=new CIType[e.parametre.length];
				for(i=0;i<param.length;i++)
				{
					param[i]=verif_expr(e.parametre[i],env);
				}
			}
			else
			{
				param=null;
			}
			res=(CITypeSimple)e.type_retour;
			if(res==null)
			{
				ajoute_erreur(env,"Le type de retour de l'appel extern n'existe pas");
			}*/
		}
		else
		{
			assert(false):env+";"+expr;
		}
		if(res==null)
		{
			//assert(erreur):env+":"+expr+(expr instanceof CIExpr_Var)+passe;
		}
		else
		{
			assert(type_sans_contrainte(res));
		}
		return res;
	}

	private CITypeSimple donne_vrai_type(CIEnvironnement env, CIType t) {
		if(t instanceof CITypeAncre)
		{
			CIDeclare_Var d;
			CIType t5;
			t5=env.var_type(((CITypeAncre)t).nom);
			assert(t5!=null);
			assert(t5 instanceof CITypeSimple);
			t=(CITypeSimple)t5;
			return (CITypeSimple)t;
		}
		else
		{
			return (CITypeSimple)t;
		}
		
	}

	private CITypeSimple verif_appel(CIEnvironnement env, CIClasse cl, CIExpr_Appel e) {
		CIType type;
		CIType[] param;
		int i;
		CITypeSimple res=null;
		if(e.parametre!=null&&e.parametre.length>0)
		{
			param=new CIType[e.parametre.length];
			for(i=0;i<param.length;i++)
			{
				param[i]=verif_expr(e.parametre[i],env);
			}
		}
		else
		{
			param=null;
		}
		// TODO: trouver l'attribut et verifier les parametres
		if(cl!=null)
		{
			CIAttribut attr;
			attr=cl.donne_attribut(e.nom);
			if(attr==null)
			{
				ajoute_erreur(env,"Impossible de trouver l'attribut "+attr+" dans la classe "+cl.nom.nom);
			}
			else
			{
				if(attr.routine!=null&&attr.routine.parametre!=null&&
						attr.routine.parametre.size()>0)
				{
					if(param==null||
							param.length!=attr.routine.parametre.size())
					{
						ajoute_erreur(env,"Le nombre de parametres ne correspond pas pour l'appel "+attr);
					}
				}
				else
				{
					if(param!=null&&
							param.length>0)
					{
						ajoute_erreur(env,"Le nombre de parametres ne correspond pas pour l'appel "+attr);
					}
				}
				if(!erreur)
				{
					if(attr.est_descendant())
					{
						CIAttribut attr2;
						assert(attr.attribut_ascendant!=null);
						assert(attr.attribut_ascendant.length==1);
						attr2=attr.attribut_ascendant[0].donne_attribut(e.nom,cl,programme);
						assert(attr2!=null);
						res=(CITypeSimple)attr2.retour;
					}
					else
					{
						res=(CITypeSimple)attr.retour;
					}
					if(param!=null&&param.length>0)
					{
						for(i=0;i<param.length&&!erreur;i++)
						{
							type=attr.routine.parametre.elt(i).type;
							assert(type!=null);
							if(!type_compatible(type,param[i]))
							{
								ajoute_erreur(env,"type incompatible dans le parametre no "+i+" pour l'appel "+attr);
								erreur=true;
							}
						}
					}
				}
			}
		}
		return res;
	}
	
	public boolean type_compatible(CIType ancetre,CIType descendant)
	{
		assert(ancetre!=null);
		assert(descendant!=null);
		assert(ancetre instanceof CITypeSimple);
		assert(descendant instanceof CITypeSimple);
		CITypeSimple a,d;
		
		CIClasse cl1,cl2;
		int no1,no2,i;
		boolean res;
		if(!type_existe(ancetre))
			return false;
		if(!type_existe(descendant))
			return false;
		a=(CITypeSimple)ancetre;
		d=(CITypeSimple)descendant;
		if(a.nom.equalsIgnoreCase(d.nom))
			return true;
		if(ancetre.est_special()||descendant.est_special())
		{// type special different
			if(ancetre.est_special()&&!descendant.est_special())
			{
				CIAttribut a1,a2;
				cl2=programme.donne_classe(d);
				assert(cl2!=null);
				a2=cl2.donne_attribut_defaut();
				if(a2!=null&&
					type_compatible(ancetre,a2.type_retour()))
				{
					return true;
				}
			}
			else if(!ancetre.est_special())
			{
				CIAttribut a1,a2;
				if(d.nom.equalsIgnoreCase("$boolean")&&a.nom.equalsIgnoreCase("boolean"))
				{
					System.out.println("Trouve");
				}
				cl1=programme.donne_classe(a);
				assert(cl1!=null);
				a1=cl1.donne_attribut_defaut();
				if(a1!=null&&
					type_compatible(a1.type_retour(),descendant))
				{
					return true;
				}
			}
			return false;
		}
		cl1=programme.donne_classe(a);
		assert(cl1!=null);
		cl2=programme.donne_classe(d);
		assert(cl2!=null);
		assert(cl1!=cl2):"2 classes ont le meme nom";
		if(cl2.heritage==null||cl2.heritage.length==0)
		{
			res=false;
		}
		else
		{// 
			res=lien_heritage(cl1,cl2);
		}
		if(!res)
		{
			CIAttribut a1,a2;
			a1=cl1.donne_attribut_defaut();
			a2=cl2.donne_attribut_defaut();
			if(a1!=null&&
				type_compatible(a1.type_retour(),descendant))
			{
				res=true;
			}
			else if(a2!=null&&
				type_compatible(ancetre,a2.type_retour()))
			{
				res=true;
			}
			else if(a1!=null&&a2!=null&&
				type_compatible(a1.type_retour(),a2.type_retour()))
			{
				res=true;
			}
		}
		return res;
	}
	
	// retourne true ssi ancetre est un ancetre de descendant
	protected boolean lien_heritage(CIClasse ancetre,CIClasse descendant)
	{
		CITypeSimple h,tabh[];
		int i;
		CIClasse cl;
		assert(ancetre!=null);
		assert(descendant!=null);
		assert(ancetre!=descendant);
		if(descendant.heritage==null||descendant.heritage.length==0)
		{
			return false;
		}
		else
		{
			for(i=0;i<descendant.heritage.length;i++)
			{
				cl=programme.donne_classe(descendant.heritage[i]);
				if(cl==ancetre)
				{
					return true;
				}
				else if(lien_heritage(ancetre,cl))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean var_existe(String nom,CIEnvironnement env)
	{
		assert(nom!=null);
		assert(env!=null);
		return env.var_existe(nom);
	}
	
	public boolean type_existe(CIType type)
	{// TODO: a faire
		assert(programme!=null);
		assert(type instanceof CITypeSimple);
		int i;
		CIClasse cl;
		CITypeSimple type2=(CITypeSimple)type;
		if(type2.nom.equalsIgnoreCase("$boolean")
			||type2.nom.equalsIgnoreCase("$character")
			||type2.nom.equalsIgnoreCase("$integer")
			||type2.nom.equalsIgnoreCase("$double")
			||type2.nom.equalsIgnoreCase("$real")
			||type2.nom.equalsIgnoreCase("$string"))
		{
			if(type2.generique==null||type2.generique.length==0)
				return true;
			else
				return false;
		}
		if(type2.nom.equalsIgnoreCase("$array"))
		{
			if((type2.generique!=null||type2.generique.length==1)&&
					type2.generique[0]!=null&&
					type_existe(type2.generique[0]))
				return true;
			else
				return false;
		}
		for(i=0;i<programme.liste_classe.length;i++)
		{
			cl=programme.liste_classe[i];
			if(cl.nom.nom.equalsIgnoreCase(type2.nom))
			{
				return true;
			}
		}
		return false;
	}
	
	public CIClasse donne_classe(String nom_classe)
	{
		int i;
		CIClasse cl;
		for(i=0;i<programme.liste_classe.length;i++)
		{
			cl=programme.liste_classe[i];
			if(cl.nom.nom.equalsIgnoreCase(nom_classe))
			{
				return cl;
			}
		}
		return null;
	}
	
	protected boolean type_sans_contrainte(CIType type)
	{
		assert(type!=null);
		CITypeSimple t;
		if(type instanceof CITypeAncre)
		{
			return true;
		}
		else
		{
			t=(CITypeSimple)type;
			if(t.contrainte!=null)
			{
				return false;
			}
			if(t.generique!=null&&t.generique.length>0)
			{
				for(int i=0;i<t.generique.length;i++)
				{
					if(!type_sans_contrainte(t.generique[i]))
					{
						return false; 
					}
				}
			}
			return true;
		}
	}
	
	public String[] message_erreur()
	{
		assert(msg_erreur!=null);
		assert(erreur);
		String s,tab[];
		int i;
		tab=new String[msg_erreur.size()];
		for(i=0;i<msg_erreur.size();i++)
		{
			assert(msg_erreur.elementAt(i) instanceof String):msg_erreur.elementAt(i);
			tab[i]=(String)msg_erreur.elementAt(i);
		}
		return tab;
		//return (String[])msg_erreur.toArray();
	}
	
	public Vector message_erreur2()
	{
		assert(msg_erreur!=null);
		assert(erreur);
		String s,tab[];
		int i;
		Vector v;
		tab=message_erreur();
		v=new Vector();
		for(i=0;i<tab.length;i++)
		{
			v.add(tab[i]);
		}
		return v;
		//return (String[])msg_erreur.toArray();
	}
	
	protected void ajoute_erreur(CIEnvironnement env,String msg)
	{
		assert(msg!=null);
		erreur=true;
		String m;
		if(env==null)
		{
			m="() ";
		}
		else
		{
			m="("+env.toString()+") ";
		}
		msg_erreur.add(m+msg);
	}
	
	protected CIProgramme programme;
	protected Vector msg_erreur;
	protected boolean erreur;

	protected static final CITypeSimple type_pinteger=new CITypeSimple(false,"$integer",null,null,null),
		type_integer=new CITypeSimple(false,"integer",null,null,null),
		type_pboolean=new CITypeSimple(false,"$boolean",null,null,null),
		type_boolean=new CITypeSimple(false,"boolean",null,null,null),
		type_pcharacter=new CITypeSimple(false,"$character",null,null,null),
		type_character=new CITypeSimple(false,"character",null,null,null),
		type_preal=new CITypeSimple(false,"$real",null,null,null),
		type_real=new CITypeSimple(false,"real",null,null,null),
		type_pdouble=new CITypeSimple(false,"$double",null,null,null),
		type_double=new CITypeSimple(false,"double",null,null,null),
		type_pstring=new CITypeSimple(false,"$string",null,null,null),
		type_string=new CITypeSimple(false,"string",null,null,null),
		type_none=new CITypeSimple(false,"none",null,null,null);

	/**
	 * @return
	 */
	/*public String[] liste_erreur() {
		if()
		return null;
	}*/

}

/*class CIEnv
{
	public CIEnv(CIClasse cl)
	{
		assert(cl!=null);
		classe=cl;
		liste_attr=new Vector();
		no_instr=-1;
	}
	
	public void entre_attribut(CINom_Attribut nom)
	{
		assert(nom!=null);
		assert(nom_attr==null);
		assert(no_instr==-1);
		nom_attr=nom;
		liste_var_local=new Vector();
	}
	
	public void sort_attribut()
	{
		assert(nom_attr!=null);
		assert(no_instr==-1);
		nom_attr=null;
		liste_var_local=null;
	}
	
	public void entre_instr()
	{
		assert(nom_attr!=null);
		if(no_instr<=0)
			no_instr=1;
		else
			no_instr++;
	}

	public void sort_instr()
	{
		assert(nom_attr!=null);
	}
	
	public void ajoute_var_local(CIDeclare_Var v)
	{
		assert(v!=null);
		liste_var_local.add(v);
	}
	
	public boolean var_existe(String nom)
	{
		assert(nom!=null);
		int i;
		String s;
		CINom_Attribut attr;
		if(liste_var_local!=null)
		{
			for(i=0;i<liste_var_local.size();i++)
			{
				s=((CIDeclare_Var)liste_var_local.elementAt(i)).nom;
				if(s.equalsIgnoreCase(nom))
					return true;
			}
		}
		if(classe.liste_attribut!=null&&classe.liste_attribut.length>0)
		{
			for(i=0;i<classe.liste_attribut.length;i++)
			{
				attr=classe.liste_attribut[i].nom;
				if(!attr.infix&&!attr.prefix)
				{
					if(attr.nom.equalsIgnoreCase(nom))
						return true;
				}
			}
		}
		return false;
	}
	
	public CIType var_type(String nom)
	{
		assert(nom!=null);
		int i;
		String s;
		CIDeclare_Var d;
		CINom_Attribut attr;
		if(liste_var_local!=null)
		{
			for(i=0;i<liste_var_local.size();i++)
			{
				d=(CIDeclare_Var)liste_var_local.elementAt(i);
				s=d.nom;
				if(s.equalsIgnoreCase(nom))
					return d.type;
			}
		}
		if(classe.liste_attribut!=null&&classe.liste_attribut.length>0)
		{
			for(i=0;i<classe.liste_attribut.length;i++)
			{
				attr=classe.liste_attribut[i].nom;
				if(!attr.infix&&!attr.prefix)
				{
					if(attr.nom.equalsIgnoreCase(nom))
						return classe.liste_attribut[i].retour;
				}
			}
		}
		return null;
	}
	
	public String toString()
	{
		String res;
		if(classe==null)
			res="Aucune classe";
		else
		{
			res="classe "+classe.nom.nom;
			if(nom_attr!=null)
			{
				res+=" attribut "+nom_attr.toString();
				if(no_instr>0)
					res+=" instruction no "+no_instr;
			}
		}
		return res;
	}
	
	public CIClasse classe;
	public CINom_Attribut nom_attr;
	public Vector liste_attr,liste_var_local;
	public int no_instr;
}*/
