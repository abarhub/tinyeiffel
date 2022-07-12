/*
 * Created on 30 sept. 2003
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
public class Attribut_Complet extends Declare_entite {

	public Attribut_Complet(NomFeature nom,Table_Symbol table)
	{
		assert(nom!=null);
		assert(table!=null);
		this.table_symbol=table;
		this.log=table.log;
		this.nom=nom;
		this.classe=table.classe;
		//this.nb_param=-1;
	}

	public boolean meme_attribut(Attribut a)
	{
		assert(a!=null);
		assert(nom!=null);
		return a.nom.meme_nom(nom);
	}

	public Type donne_type_retour(Context context)
	{// TODO : a terminer
		assert(context!=null);
		/*if(attribut_reel!=null)
			return attribut_reel.feature.type_retour;
		if(attribut_deferred!=null)
			return attribut_deferred[0].feature.type_retour;*/
		//if()
		//èçàè4521-((-
		return signature.getTypeRetour();
/*		if(est_deferred())
		{
			Vector liste=new Vector();
			int i,j;
			Type t,t2;
			boolean trouve;
			if(feature_directe!=null)
				liste.addElement(feature_directe.type_retour);
			if(attribut_ancetre!=null)
			{
				for(i=0;i<attribut_ancetre.length;i++)
				{
					t=attribut_ancetre[i].attribut.donne_type_retour(context);
					assert(t!=null);
					trouve=false;
					for(j=0;j<liste.size();j++)
					{
						t2=(Type)liste.elementAt(j);
						if(t2.egaux(t))
						{// le type t est deja inclus
							trouve=true;
							break;
						}
						else if(context.type_compatible(t2,table_symbol,t,table_symbol))
						{// il y a deja un sous type de t
							trouve=true;
							break;
						}
						else if(context.type_compatible(t,table_symbol,t2,table_symbol))
						{// il y a un sur type de t
							//trouve=true;
							//liste.setElementAt(t,j);
							//break;
							liste.remove(j);
							j--;
						}
					}
					if(!trouve)
					{
						liste.addElement(t);
					}
				}
			}
			
			assert(liste.size()>0);
			assert(liste.size()==1):"Il y a plusieurs types pour "+
						nom+"("+classe.nom+"):"+liste;
			t=(Type)liste.elementAt(0);
			assert(t!=null);
			return t;
		}
		else
		{
			if(no_attribut_reel==-1)
				return feature_directe.type_retour;
			else
			{
				Type t=attribut_ancetre[no_attribut_reel].attribut.donne_type_retour(context);
				assert(t!=null);
				return t;
			}
		}
*/		//return null;
	}

	public String toString(Attribut a)
	{
		assert(a!=null);
		String res;
		res=a.toString()+"<"+a.classe.nom+">:"+a.feature.type_retour;
		return res;
	}

	public String toString(Attribut_Heritage a)
	{
		assert(a!=null);
		String res;
		res="("+a.attribut.nom+"<"+a.classe.nom+">:"+
				a.signature.retour+")";
		return res;
	}

	public String toString()
	{
		String res="Attribut_Complet "+nom+":{directe=";
		if(attribut_directe!=null)
			res+=toString(attribut_directe);
		else
			res+="Aucun";
		res+=",no_reel="+no_attribut_reel;
		res+=",deferred=";
		if(attribut_ancetre!=null)
		{
			for(int i=0;i<attribut_ancetre.length;i++)
			{
				if(i>0)
					res+=",";
				res+=toString(attribut_ancetre[i]);
			}
		}
		else
		{
			res+="null";
		}
		res+="}";
		return res;
	}

	/**
	 * Ajoute un attribut complet venant d'un anctre à 
	 * l'attribut courant 
	 * @param ac	l'attribut a ajouter
	 * @param nom	le nom de cet attribut apres renomage
	 * @param undefine	=true ssi il est undefine
	 * @param redefine	=true ssi il est redefine
	 * @param herit		le lien d'heritage
	 */
	public void ajoute_ancetre(Attribut_Complet ac,NomFeature nom,
					boolean undefine,boolean redefine,Heritage herit,
					Conversion conversion)
	{
		Attribut_Heritage ah;
		
		assert(ac!=null);
		assert(herit!=null);
		if(nom.equals("a")&&classe.nom.equalsIgnoreCase("test2"))
			System.out.println("coucou");
		ah=new Attribut_Heritage(table_symbol,nom,undefine,
									redefine,herit,ac,
									conversion);
		//ah.
		if(attribut_ancetre==null)
		{
			attribut_ancetre=new Attribut_Heritage[1];
			attribut_ancetre[0]=ah;
		}
		else
		{
			int i;
			Attribut_Heritage at[];
			at=new Attribut_Heritage[attribut_ancetre.length+1];
			for(i=0;i<attribut_ancetre.length;i++)
			{
				at[i]=attribut_ancetre[i];
			}
			at[i]=ah;
			attribut_ancetre=at;
		}
	}

	/**
	 * definit un attribut directe de la classe
	 * @param a		l'attribut a ajouter
	 * @param feature	la feature a ajouter
	 */
	public void ajoute_attribut_directe(Attribut a,Feature feature)
	{
		assert(feature!=null);
		assert(a!=null);
		if(feature_directe!=null)
		{
			table_symbol.log.erreur(new ErreurVFFD2(attribut_directe,a));
		}
		else
		{
			feature_directe=feature;
			attribut_directe=a;
			if(a.nom.prefix)
			{
				if(feature.param!=null&&feature.param.length>0)
				{// Erreur VFFD5 : pas bon parametre
					table_symbol.context.erreur(new ErreurVFFD5(classe,a.nom,feature));
				}
				if(feature.type_retour==null)
				{// Erreur VFFD5 : pas bon type retour
					table_symbol.context.erreur(new ErreurVFFD5(classe,a.nom,feature));
				}
			}
			if(a.nom.infix)
			{
				if(feature.param==null||feature.param.length!=1)
				{// Erreur VFFD6 : pas bon parametre
					table_symbol.context.erreur(new ErreurVFFD6(classe,a.nom,feature));
				}
				if(feature.type_retour==null)
				{// Erreur VFFD6 : pas bon type retour
					table_symbol.context.erreur(new ErreurVFFD6(classe,a.nom,feature));
				}
			}
			if(feature instanceof FeatureRoutine&&
				((FeatureRoutine)feature).once&&
				feature.type_retour!=null)
			{
				if(feature.type_retour.is_like||
					table_symbol.type_generique(feature.type_retour))
				{
					table_symbol.context.erreur(new ErreurVFFD7(classe,a.nom,feature));
				}
			}
		}
	}

	/**
	 * verifie que l'attribut est valide.
	 * @param context
	 */
	public void verifie_valide(Context context) {
		assert(feature_directe!=null||attribut_ancetre!=null);
		Attribut_Complet ac;
		Attribut a,attr=null;
		Attribut_Heritage ah,ah2;
		int i,j;
		boolean redefine=false;
		Feature f1,f2;
		Vector liste_deferred,liste_effectif,
				liste_deferred_redefine,liste_effectif_redefine;
		liste_deferred=new Vector();
		liste_effectif=new Vector();
		liste_deferred_redefine=new Vector();
		liste_effectif_redefine=new Vector();
		/*
		 * TODO : comparer les attributs entre les ancetres
		 * et la feature directe  
		 */
		assert(!verifie);//&&&é"'(-è_çà
		if(attribut_ancetre!=null)
		{// il y a des attributs ancetres
			for(i=0;i<attribut_ancetre.length;i++)
			{
				ah=attribut_ancetre[i];
				assert(ah!=null);
				redefine=redefine||ah.redefine;
				if(ah.est_deferred())
				{
					if(ah.undefine)
					{
						context.erreur(
								new ErreurVDUS3(classe,
										ah.attribut.getAttribut(),
										ah.heritage));
					}
					if(ah.redefine)
					{
						liste_deferred_redefine.addElement(ah);
					}
					else
					{
						liste_deferred.addElement(ah);
					}
				}
				else
				{
					if(ah.undefine&&(ah.frozen()||ah.getFeature().est_variable()))
					{
						context.erreur(new ErreurVDUS2(classe,ah.nom,
														ah.heritage,
														ah.getFeature()));
					}
					if(ah.redefine)
					{
						if(ah.frozen()||ah.getFeature().est_constant())
						{//	Erreur de redefinition d'attribut frozen
							// VDRS2
							//assert(false);
							context.erreur(
										 new ErreurVDRS2(classe,ah.nom,
														 ah.heritage,
														 ah.getFeature()));
						}
						if(ah.undefine)
							liste_deferred_redefine.addElement(ah);
						else
							liste_effectif_redefine.addElement(ah);
					}
					else
					{
						if(ah.undefine)
							liste_deferred.addElement(ah);
						else
							liste_effectif.addElement(ah);
					}
				}
			}
			if(liste_effectif.size()+liste_effectif_redefine.size()>0)
			{
				if(liste_effectif.size()>0)
				{
					if(feature_directe!=null)
					{
						//if(feature_directe.is_deferred())
						{// Erreur : effectif+directe deferred
						//	assert(false);
						}
						//else
						{// Erreur entre ancetre et directe (VMCN2)
							ah=(Attribut_Heritage)liste_effectif.elementAt(0);
							context.erreur(
									new ErreurVMCN2(classe,ah.heritage,
													feature_directe,nom));
						}
						no_attribut_reel=-1;
					}
					else
					{// pas de feature directe
						boolean trouve=false;
						for(i=0;i<attribut_ancetre.length;i++)
						{
							if(attribut_ancetre[i]==(Attribut_Heritage)liste_effectif.elementAt(0))
							{
								no_attribut_reel=i;
								feature_reel=attribut_ancetre[i].getFeature();
								trouve=true;
							}
						}
						assert(trouve);
					}
					if(liste_effectif.size()>1)
					{// Erreur entre ancetres
						//System.out.println("***************************************************************************************");
						//System.out.println(nom+":"+classe.nom+";"+liste_effectif);
						for(int k=0;k<liste_effectif.size()-1;k++)
						{
							assert(k+1<liste_effectif.size());
							ah=(Attribut_Heritage)liste_effectif.elementAt(k);
							ah2=(Attribut_Heritage)liste_effectif.elementAt(k+1);
							f1=ah2.getFeature();
							f2=ah.getFeature();
							if(f1!=f2)
							{// pas les memes feature => erreur
									context.erreur(
											new ErreurVMCN3(classe,
															ah2.heritage,
															ah.heritage,
															nom));
							}
						}
					}
				}
				else
				{// liste_effectif.size==0
					assert(liste_effectif_redefine.size()>0);
					if(feature_directe!=null)
					{
						if(feature_directe.is_deferred())
						{// Erreur VDRD5 : redefine d'effectif vers deferred
							for(int k=0;k<liste_effectif_redefine.size();k++)
							{
								Attribut_Heritage ah3;
								ah3=(Attribut_Heritage)liste_effectif_redefine.elementAt(k);
								context.erreur(
									new ErreurVDRD5(
											classe,feature_directe,
											ah3.heritage,nom));
							}
						}
						no_attribut_reel=-1;
					}
					else
					{// Erreur: liste_effectif_redefine>0 et
						// feature_directe==0 et liste_effectif=0
						for(j=0;j<attribut_ancetre.length;j++)
						{
							ah=attribut_ancetre[j];
							for(int k=0;k<liste_effectif_redefine.size();k++)
							{
								if(ah==(Attribut_Heritage)liste_effectif_redefine.elementAt(k))
								{
									context.erreur(
												new ErreurVDRS4(classe,ah.nom,
																ah.heritage));
								}
								if(feature_reel==null)
								{
									feature_reel=ah.getFeature();
									no_attribut_reel=k;
								}
							}
						}
						//assert(false):"feature:"+nom+",directe="+
						//		feature_directe+",classe="+
						//		classe.nom;
					}
				}
				if(liste_deferred_redefine.size()>0)
				{// Erreur: redefine de deferred et effectif
					Feature f;
					if(no_attribut_reel==-1)
						f=feature_directe;
					else
						f=attribut_ancetre[no_attribut_reel].getFeature();
					Attribut_Heritage ah3=(Attribut_Heritage)liste_deferred_redefine.elementAt(0);
					context.erreur(new ErreurVDRS4(classe,nom,ah3.heritage,f));
				}
			}
			else
			{// pas de liste_effectif
				if(feature_directe!=null)
				{//
					if(liste_deferred.size()==0)
					{
						if(liste_deferred_redefine.size()>0)
						{
							if(!feature_directe.is_deferred())
							{// Erreur: redefine deferred et
								// directe pas deferred
								context.erreur(
										new ErreurVDRS4(classe,
														nom,
								((Attribut_Heritage)liste_deferred_redefine.elementAt(0)).heritage,
														feature_directe));
								//assert(false);
							}
						}
					}
					else if(liste_deferred.size()>1)
					{// Erreur: plus de 1 deferred
						if(feature_directe.is_deferred())
						{// conflit entre deferred ancetre et deferred directe
							Attribut_Heritage ah3=(Attribut_Heritage)liste_deferred.elementAt(0);
							context.erreur(new ErreurVDRS4(classe,nom,ah3.heritage,feature_directe));
						}
					}
					else
					{// liste_deferred=1
						if(feature_directe.is_deferred())
						{// Erreur: ancetre deferred et
							// directe deferred (oublit de redefine)
							Attribut_Heritage ah3=(Attribut_Heritage)liste_deferred.elementAt(0);
							context.erreur(new ErreurVDRS4(classe,nom,ah3.heritage,feature_directe));
							//assert(false);
						}
					}
					no_attribut_reel=-1;
				}
				else
				{// pas de feature_directe
					if(liste_deferred.size()==0)
					{
						if(liste_deferred_redefine.size()>0)
						{// Erreur: redefine sans effectif
							//assert(false);
							for(int k=0;k<liste_deferred_redefine.size();k++)
							{
								Attribut_Heritage ah3;
								ah3=(Attribut_Heritage)liste_deferred_redefine.elementAt(k);
								context.erreur(new ErreurVDRS4(classe,nom,ah3.heritage));
							}
						}
					}
					else if(liste_deferred.size()>1)
					{// Erreur : plus de 1 deferred
						for(int k=1;k<liste_deferred.size();k++)
						{
							Attribut_Heritage ah3,ah4=null;
							boolean trouve=false;
							ah3=(Attribut_Heritage)liste_deferred.elementAt(k);
							for(int m=0;m<k;m++)
							{
								ah4=(Attribut_Heritage)liste_deferred.elementAt(m);
								if(ah3.getFeature()!=ah4.getFeature())
								{
									trouve=true;
									break;
								}
							}
							if(trouve)
							{
								assert(ah4!=null);
								context.erreur(new ErreurVMCN1(classe,
																ah3.heritage,
																ah4.heritage,nom));
							}
						}
						//assert(false);
					}
					//assert(liste_deferred.size()>0);
					boolean trouve=false;
					Attribut_Heritage ah3;
					if(liste_deferred.size()>0)
						ah3=(Attribut_Heritage)liste_deferred.elementAt(0);
					else
					{
						assert(liste_deferred_redefine.size()>0);
						ah3=(Attribut_Heritage)liste_deferred_redefine.elementAt(0);
					}
					assert(ah3!=null);
					for(i=0;i<attribut_ancetre.length;i++)
					{
						if(attribut_ancetre[i]==ah3)
						{
							no_attribut_reel=i;
							feature_reel=attribut_ancetre[i].getFeature();
							trouve=true;
						}
					}
					assert(trouve);
				}
			}
			assert((no_attribut_reel==-1&&feature_directe!=null)
					||(no_attribut_reel>=0&&feature_reel!=null));
			if(feature_directe!=null)
			{
				if(feature_reel!=null)
				{// Erreur entre ancetre et directe (VMCN2)
					//assert(false);
					ah=attribut_ancetre[no_attribut_reel];
					context.erreur(
						new ErreurVMCN2(classe,ah.heritage,
								feature_directe,nom));
				}
				else
				{
					no_attribut_reel=-1;
				}
			}
			if(redefine&&feature_reel==null&&feature_directe==null)
			{// redefine sans feature effective VDRS4
				//assert(false);
				for(j=0;j<attribut_ancetre.length;j++)
				{
					ah=attribut_ancetre[j];
					if(ah.redefine)
					{
						context.erreur(
									new ErreurVDRS4(classe,ah.nom,
												ah.heritage));
					}
				}
			}
			
		}
		else
		{// pas d'attribut herité
			if(feature_directe!=null)
				no_attribut_reel=-1;
		}
		attr=getAttribut();
		//if(attribut_directe!=null)
		//	attr=attribut_directe;
		//else
			//attr=
		assert(attr!=null);
		if(frozen()&&est_deferred()&&attr.classe==classe)
		{
			context.erreur(new ErreurVFFD4(attr));
		}
		assert(nom!=null);
		if(feature_directe!=null)
		{
			NomFeature nom2;
			nom2=feature_directe.cherche_nom(nom);
			assert(nom2!=null):"nom="+nom+";pos="+nom.debut()+
					";classe="+classe.nom;
			nom=nom2;
		}
		else
		{
			assert(feature_reel!=null);
			NomFeature nom2;
			nom2=feature_reel.cherche_nom(nom);
			// TODO: a remettre (pose problème si rename)
			//assert(nom2!=null):"nom="+nom+","+classe.nom+";";
			nom=nom2;
			
		}
		// construction de la signature de l'attribut
		construit_signature(context);
		//assert()
		assert(feature_directe!=null||feature_reel!=null||
				est_deferred()):
			"directe="+feature_directe+";reel="+feature_reel+
			"attr="+nom+"("+classe.nom+")";
		verifie=true;
	}

	public void verifie_signature(Context context)
	{
		Type t1,t2;
		if(signature.getTypeRetour()!=null)
			context.type_valide(table_symbol,signature.getTypeRetour());
		//if(parametre!=null)
		{
			for(int j=0;j<signature.nb_parametre()/*parametre.length*/;j++)
				context.type_valide(table_symbol,signature.getParametre(j));
		}
		// verification des signatures par rapport 
		// a la feature "principale"
		if(attribut_ancetre!=null)
		{
			for(int i=0;i<attribut_ancetre.length;i++)
			{
				int no,no2;
				Attribut_Complet ac;
				Attribut_Heritage ah;
				ah=attribut_ancetre[i];
				ac=ah.attribut;
				//no2=verifie_signature(parametre,type_retour,attribut_ancetre[i],context);
				no=ah.signature.verifie_signature(signature,context,table_symbol);
				//assert(no2==no):"diff:no="+no+"!="+no2;
				switch(no)
				{
					case -2:// pas le bon nombre de parametre
							assert(no_attribut_reel==-1);
							//assert(false):"nom="+nom.debut()+";directe="+
							//		feature_directe.liste_nom[0].debut();
							context.erreur(new ErreurVNCS1(nom,classe,
										ac.nom,
										ac.classe));
							/*assert(false):"Erreur de nombre de parametre:"+
										"principal="+nb_param+"!="+
										attribut_ancetre[i].attribut.nb_param+
										"("+i+")";*/
							break;
					case -1:// pas de pb
							if(!ac.getFeature().is_deferred()&&
								getFeature().est_external()!=
								ac.getFeature().est_external())
							{// Erreur: est_external() pas identique
								// TODO: a remetre ?
								// TODO: ne pas mettre d'erreur si l'external est tinyeiffel
								if(getFeature().est_external())
								{
									FeatureExternal fe=(FeatureExternal)getFeature();
									if(!fe.str.chaine_unique().equalsIgnoreCase("\"tinyeiffel\""))
									{// erreur VDRD7
										context.erreur(new ErreurVDRD7(classe,getFeature(),
																						attribut_ancetre[i].heritage,nom));
									}
								}
								else
								{

									FeatureExternal fe=(FeatureExternal)ac.getFeature();
									if(!fe.str.chaine_unique().equalsIgnoreCase("\"tinyeiffel\""))
									{// Erreur VDRD7
										context.erreur(new ErreurVDRD7(classe,getFeature(),
																						attribut_ancetre[i].heritage,nom));
									}
								}
								/*context.erreur(new ErreurVDRD7(classe,getFeature(),
												attribut_ancetre[i].heritage,nom));*/
							}
							if(ac.getFeature().est_variable()&&
										!getFeature().est_variable())
							{
								context.erreur(new ErreurVDRD6(classe,getFeature(),attribut_ancetre[i].heritage,
												nom,true,!ac.getFeature().est_variable()));
							}
							else if(ac.getFeature().est_variable()==
									getFeature().est_variable())
							{
								//assert(false):"coucou";
								if(getFeature().type_retour!=null&&
									ac.getFeature().type_retour!=null)
								{
									t1=getFeature().type_retour;
									t2=ac.getFeature().type_retour;
									if(t1.expanded!=t2.expanded)
									{// TODO: A ameliorer le teste
										context.erreur(new ErreurVDRD6(classe,getFeature(),attribut_ancetre[i].heritage,
													nom,false,!t2.expanded));
									}
								}
								//else
									//assert(false);
							}
							break;
					case -3:// pas le bon type de retour
							t1=signature.getTypeRetour();
							t2=ah.signature.getTypeRetour();
							context.erreur(new ErreurVNCS2(nom,classe,ac.nom,
											ac.classe,-1,t1,t2));
							break;
					default:// Erreur avec le no parametre
							t1=signature.getParametre(no);
							t2=ah.signature.getParametre(no);//ac.parametre[no][0];
							context.erreur(new ErreurVNCS2(nom,classe,ac.nom,
												ac.classe,no,t1,t2));
							//assert(false):"Erreur avec parametre no "+no+
							//		" pour "+nom+" dans "+classe.nom+
							//		parametre[no][0]+" non conforme avec "+
							//		attribut_ancetre[i].attribut.parametre[no][0];
							break;
				}
			}
		}
	}

	/**
	 * construit la signature
	 * @param context
	 */
	protected void construit_signature(Context context)
	{// TODO : a terminer
		assert(context!=null);
		DeclareVar liste[];
		//Feature feature;
		//return;
		//&é"'(-
		// recherche de la feature "principale"
		if(no_attribut_reel==-1)
		{// l'attribut est directe
			//feature=feature_directe;
			assert(feature_directe!=null);
			// construction de la signature
			Type p[];
			if(feature_directe.param==null||
			feature_directe.param.length==0)
			{
//				nb_param=0;
				//p=null;
				liste=null;
			}
			else
			{
				int nb_param=feature_directe.param.length;
				liste=feature_directe.param;
//				parametre=new Type[nb_param][];
				/*p=new Type[nb_param];
				for(int i=0;i<nb_param;i++)
				{
//					parametre[i]=new Type[1];
//					parametre[i][0]=feature.param[i].type;
					p[i]=feature_directe.param[i].type;
				}*/
			}
//			type_retour=feature.type_retour;
			//if(type_retour)
			signature=new Signature(liste,feature_directe.type_retour);
		}
		else
		{
			//feature=feature_reel;
			signature=attribut_ancetre[no_attribut_reel].signature;
		}
	}

	/**
	 * Ajoute le type type dans le tableau liste 
	 * et retourne ce tableau 
	 * @param liste
	 * @param type
	 * @return
	 */
	protected Type[] ajoute_type(Type liste[],Type type)
	{
		int i;
		Type liste2[],t1,t2;
		assert(liste!=null);
		assert(type!=null);
		for(i=0;i<liste.length;i++)
		{
			t1=liste[i];
			assert(t1!=null);
			if(t1.equals(type))
				return liste;
		}
		liste2=new Type[liste.length+1];
		for(i=0;i<liste.length;i++)
		{
			liste2[i]=liste[i];
		}
		liste2[i]=type;
		liste=liste2;
		assert(liste!=null);
		assert(liste.length>0);
		return liste;
	}

	/**
	 * Ne pas utiliser si elle est deferred
	 * @return la feature effective
	 */
	public Feature getFeature()
	{
		assert(verifie);
		assert(feature_directe!=null||feature_reel!=null):
				"directe="+feature_directe+";reel="+feature_reel+
				"attr="+nom+"("+classe.nom+")";
		/*if(feature_directe==null&&feature_reel==null)
		{
			assert(attribut_ancetre!=null);
			for(int i=0;i<attribut_ancetre.length;i++)
			{
				
			}
			assert(false);
			return null;
		}
		else*/ if(feature_directe!=null)
			return feature_directe;
		else
			return feature_reel;
	}

	public Attribut getAttribut()
	{
		if(attribut_directe!=null)
			return attribut_directe;
		assert(attribut_ancetre!=null);
		assert(attribut_ancetre.length>0);
		int no=0;
		if(no_attribut_reel>-1)
			no=no_attribut_reel;
		assert(no<attribut_ancetre.length);
		assert(attribut_ancetre[no]!=null);
		assert(attribut_ancetre[no].attribut!=null);
		return attribut_ancetre[no].attribut.getAttribut();
	}

	/**
	 * Est vrai ssi tous les attributs sont deferred
	 * @return =true ssi tous les attributs sont deferred
	 */
	public boolean est_deferred()
	{
		assert(feature_directe!=null||feature_reel!=null);
		if(feature_directe!=null)
		{
			return feature_directe.is_deferred();
		}
		else
		{
			return feature_reel.is_deferred();
			/*if(feature_reel!=null)
			{
				assert(!feature_reel.is_deferred());
				return false;
			}
			else
			{
				return true;
				//int i;
				//for(i=0;i<)
			}*/
		}
	}

	public boolean frozen()
	{
		boolean res=false;
		if(attribut_directe!=null)
		{
			res=attribut_directe.nom.frozen;
		}
		if(!res&&attribut_ancetre!=null)
		{
			int i;
			for(i=0;!res&&i<attribut_ancetre.length;i++)
			{
				if(attribut_ancetre[i].frozen())
				{
					res=true;
				}
			}
		}
		return res;
	}

	public String getNom()
	{
		assert(!nom.infix&&!nom.prefix);
		return nom.nom;
	}
	
	public Type getType()
	{// TODO: enlever le table_symbol.context
		return this.donne_type_retour(table_symbol.context);
	}

	//public Attribut attribut_reel;
	//public Attribut[] attribut_deferred;
	public Logging log;
	//public boolean undefine,redefine;
	public Table_Symbol table_symbol;
	
	// utilise
	//public int nb_param;
	//public Type parametre[][];
	//public Type type_retour;
	public NomFeature nom;
	public Classe classe;
	public Attribut attribut_directe;
	public Feature feature_directe,feature_reel;
	public Attribut_Heritage attribut_ancetre[];
	public int no_attribut_reel;
	protected boolean verifie;
	public Signature signature;
	
}
