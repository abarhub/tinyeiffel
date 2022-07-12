/*
 * Created on 30 sept. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;
//import java.util.*;
import tinyeiffel.erreur.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Table_Symbol {
// TODO : Genere les erreurs
	public Table_Symbol(Classe c,Context context)
	{
		assert(c!=null);
		assert(context!=null);
		
		heritage=c.heritage;
		this.log=context.logging;
		classe=c;
		this.context=context;
		if(heritage!=null)
		{
			table_heritage=new Table_Symbol[heritage.length];
			table_conversion=new Conversion[heritage.length];
		}
		/*int i;
		for(i=0;i<heritage.length;i++)
			ajoute_heritage(heritage[i]);
		for(i=0;i<feature.length;i++)
			ajoute_feature(feature[i]);
		*/
	}
	
	/**
	 * Ajoute les attributs  d'une classe herité
	 * @param h	le lien d'heritage
	 * @param t	la table de symbol de la classe herité
	 */
	public void ajoute_heritage(Heritage h,Table_Symbol t)
	{
		assert(h!=null);
		assert(t!=null);
		//assert(heritage!=null);
		//assert(heritage.length>0);
		//assert(heritage_ajoute==false);
		int i,j;
		if(heritage==null)
		{
			heritage=new Heritage[1];
			heritage[0]=h;
			table_heritage=new Table_Symbol[1];
			table_conversion=new Conversion[1];
		}
		assert(heritage!=null);
		assert(heritage.length>0);
		assert(heritage.length==table_heritage.length);
		for(i=0;i<heritage.length;i++)
		{
			if(heritage[i]==h)
				break;
		}
		assert(i<heritage.length);
		assert(table_heritage[i]==null):"heritage refait:"+i+","
					+heritage.length;
		assert(table_conversion[i]==null);
		table_heritage[i]=t;
		Conversion conversion=new Conversion(h,t.classe.type);
		table_conversion[i]=conversion;
		//context.type_valide(this,h.type);
		if(t.liste_attribut!=null)
		{
			for(i=0;i<t.liste_attribut.length;i++)
			{
				Attribut_Complet a=t.liste_attribut[i];
				NomFeature nom=a.nom;
				boolean rename=false,redefine=false,undefine=false,select=false;
				for(j=0;j<h.rename.length;j++)
				{
					Rename r=h.rename[j];
					if(nom.equals(r.nom_src))
					{
						nom=r.nom_dest;
						rename=true;
						break;
					}
				}
				for(j=0;j<h.undefine.length;j++)
				{
					NomFeature n=h.undefine[j];
					if(nom.equals(n))
					{
						undefine=true;
						break;
					}
				}
				for(j=0;j<h.redefine.length;j++)
				{
					NomFeature n=h.redefine[j];
					if(nom.equals(n))
					{
						redefine=true;
						break;
					}
				}
				for(j=0;j<h.select.length;j++)
				{
					NomFeature n=h.select[j];
					if(nom.equals(n))
					{
						select=true;
						break;
					}
				}
				assert(h!=null);
				ajoute_attribut(a,nom,undefine,redefine,h,
								conversion);
			}
		}
		
		heritage_ajoute=true;
	}
	
	/**
	 * Ajoute les attributs declare dans la feature f
	 * de la classe.
	 * @param f	la feature
	 */
	public void ajoute_feature(Feature f)
	{
		assert(f!=null);
		//assert(feature_ajoute==false);
		int i;
		for(i=0;i<f.liste_nom.length;i++)
		{
			NomFeature n=f.liste_nom[i];
			assert(n!=null);
			Attribut a=new Attribut(n,classe,f);
			ajoute_attribut(a,n,f);
		}
		
		feature_ajoute=true;
	}
	
	protected void ajoute_attribut(Attribut a,NomFeature nom,
									Feature feature)
	{// ajoute un attribut dans la table de symbol
		assert(a!=null);
		Attribut_Complet ac;
		int no=no_attribut(a);
		if(no==-1)
		{
			ac=insert_attribut(a.nom);
		}
		else
			ac=liste_attribut[no];
		ac.ajoute_attribut_directe(a,feature);
		//ac.feature_directe=feature;
		//ac.attribut_directe=a;
	}

	/**
	 * Ajoute l'attribut ac dans la table de symbol avec 
	 * pour nom nom. L'attribut vient d'un ancetre.
	 * @param ac       l'attribut ajoute
	 * @param nom  le nom de l'attribut apres renomage eventuel
	 * @param undefine =true ssi c'est un undefine
	 * @param redefine =true ssi c'est un redefine
	 * @param herit    l'heritage
	 */
	protected void ajoute_attribut(Attribut_Complet ac,NomFeature nom,
				boolean undefine,boolean redefine,Heritage herit,
				Conversion conversion)
	{
		assert(ac!=null);
		assert(nom!=null);
		assert(herit!=null);
		Attribut a;
		Attribut_Complet ac2;
		int no=no_attribut(nom);
		if(no==-1)
			ac2=insert_attribut(nom);
		else
			ac2=liste_attribut[no];
		ac2.ajoute_ancetre(ac,nom,undefine,redefine,herit,conversion);
		/*if(ac.attribut_deferred!=null)
		{
			for(int i=0;i<ac.attribut_deferred.length;i++)
			{
				a=ac.attribut_deferred[i];
				ac2.ajoute_deferred(a);
			}
		}
		if(ac.attribut_reel!=null)
		{
			if(undefine)
			{
				//ac2.redefine=redefine;
				//ac2.undefine=true;
				if(ac.attribut_reel==null)
				{// TODO : a revoir
					//log.erreur(new ErreurVDUS3(classe,));
					assert(false);
				}
				else
				{
					ac2.ajoute_deferred(ac.attribut_reel);
				}
			}
			else if(redefine)
			{
				//ac2.redefine=true;
				//ac2.undefine=undefine;
				ac2.ajoute_deferred(ac.attribut_reel);
			}
			else
				ac2.defini_attribut(ac.attribut_reel);
		}*/
	}

	/**
	 * Cree un attribut et l'ajoute dans la table.
	 * Il ne doit pas exister.
	 * @param nom	le nom de cette attribut
	 * @return
	 */
	protected Attribut_Complet insert_attribut(NomFeature nom)
	{// cree un nouvel Attribut_Complet
		assert(nom!=null);
		Attribut_Complet ac=new Attribut_Complet(nom,this);
		int len;
		if(liste_attribut==null)
			len=0;
		else
			len=liste_attribut.length;
		Attribut_Complet t[]=new Attribut_Complet[len+1];
		int i;
		for(i=0;i<len;i++)
			t[i]=liste_attribut[i];
		t[i]=ac;
		liste_attribut=t;
		return ac;
	}

	/*public void ajoute_attribut_deferred(Attribut a)
	{
		assert(a!=null);
		
	}*/
	
	public void termine_verification()
	{
		int i,j;
		if(heritage!=null)
		{
			for(i=0;i<heritage.length;i++)
			{
				Heritage h=heritage[i];
				assert(h!=null);
				context.type_valide(this,h.type);
			}
		}
		if(liste_attribut!=null)
		{
			boolean deferred=false;
			Attribut_Complet ad=null;
			for(i=0;i<liste_attribut.length;i++)
			{
				Attribut_Complet a=liste_attribut[i];
				//context.feature_courante=a.getFeature();
				context.compiler.env.entre_feature(a.getFeature());
				a.verifie_signature(context);
				if(a.est_deferred())
				{
					deferred=true;
					ad=a;
				}
				context.compiler.env.sort_feature();
			}
			if(deferred&&!classe.deferred)
			{// Erreur VCCH1
				assert(ad!=null);
				context.erreur(new ErreurVCCH1(classe,ad.getFeature()));
			}
			else if(!deferred&&classe.deferred)
			{// Erreur VCCH2
				context.erreur(new ErreurVCCH2(classe));
			}
			/*for(i=0;i<liste_attribut.length;i++)
			{
				Attribut_Complet a=liste_attribut[i];
				/*if(a.redefine&&a.attribut_reel==null)
				{
					log.erreurMsg("L'attribut "+a.nom+" n'a pas ete redefinie dans la classe "+
							classe.nom);
				}*/
			/*	Attribut a2=a.donne_attribut(classe);
				if(a2!=null&&a.attribut_deferred!=null)
				{
					if(classe.nom=="TEST8"&&a2.nom.nom=="Void")
					{
						System.out.println("Trouve");
					}
					a.verifie_signature();
					Type t=a2.feature.type_retour,t2;
					for(j=0;j<a.attribut_deferred.length;j++)
					{
						if(t==null)
						{
							if(a.attribut_deferred[j].feature.type_retour!=null)
							{
								log.erreurMsg("Pas1 le bon type de retour pour l'attribut "+
										a+" dans la classe "+classe.nom);
							}
						}
						else
						{
							t2=a.attribut_deferred[j].feature.type_retour;
							if(t2==null||!context.type_compatible(t,this,t2,this))
							{
								log.erreurMsg("Pas2 le bon type de retour pour l'attribut "+
											a+" dans la classe "+classe.nom+
											" entre "+t+" et "+t2+":"+trouve_vrai_type(t)+
											","+trouve_vrai_type(t2));
							}
						}
					}
				}
			}*/
		}
	}
	
	/**
	 * retrourne true ssi le type t est un generique
	 * @param t
	 * @return
	 */
	public boolean type_generique(Type t)
	{
		assert(t!=null);
		assert(classe!=null);
		Type type,type2;
		int i;
		type=classe.type;
		assert(type!=null);
		//assert(t.nom.compareToIgnoreCase("u")!=0):"type="+t+
		//		";classe.type="+type+";classe="+classe.nom;
		if(t.is_like||type.egaux(t))
			return false;
		if(type.generique!=null&&type.generique.length>0)
		{
			for(i=0;i<type.generique.length;i++)
			{
				if(type.generique[i].egaux(t))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * retourne la contrainte s'il y en a du parametre generique
	 * type
	 * @param type
	 * @return
	 */
	public Type contrainte(Type type)
	{
		assert(type!=null);
		assert(type_generique(type));
		Type type_base;
		type_base=classe.type;
		assert(type_base!=null);
		assert(type_base.generique!=null&&type_base.generique.length>0);
		for(int i=0;i<type_base.generique.length;i++)
		{
			if(type_base.generique[i].egaux(type))
			{
				Type t=type_base.generique[i];
				if(t.generique==null||t.generique.length==0)
				{
					return null;
				}
				else
				{
					assert(t.generique.length==1);
					return t.generique[0];
				}
			}
		}
		assert(false);
		return null;
	}
	
	public Type trouve_vrai_type(Type t)
	{// TODO: prendre en compte like result
		assert(t!=null);
		assert(context!=null);
		if(!t.is_like)
		{
			/*if(type_generique(t))
			{
				Type type=contrainte(t);
				if(type==null)
					return new Type(false,"any",new Vector());
				else
					return type;
			}
			else*/
				return t;
		}
		else
		{
			assert(t.like!=null);
			Expr_Var v=(Expr_Var)t.like;
			if(v.nom.compareToIgnoreCase("Current")==0)
				return classe.type;
			int i;
			i=no_attribut(new NomFeature(v.nom));
			if(i==-1)
			{
				DeclareVar liste[],var;
				Environnement env;
				env=context.compiler.env;
				if(env.dans_feature())
				{
					liste=env.feature.param;
					var=context.cherche_var(v.nom,liste);
					if(var!=null)
					{
						return var.type;
					}
					if(env.feature instanceof FeatureRoutine)
					{
						liste=((FeatureRoutine)env.feature).local;
						var=context.cherche_var(v.nom,liste);
						if(var!=null)
						{
							return var.type;
						}
					}
				}
				log.erreurMsg("Attribut du like "+v.nom+" inconnue");
				return null;
			}
			else
			{
				Type t2=liste_attribut[i].donne_type_retour(context);
				assert(t2!=null);
				if(t2.is_like)
					return null;
				return t2;
			}
		}
		//assert(false):t+"("+t.debut()+")";
		//return null;
	}
	
	protected int no_attribut(Attribut a)
	{
		assert(a!=null);
		if(liste_attribut==null)
			return -1;
		int i;
		for(i=0;i<liste_attribut.length;i++)
		{
			if(liste_attribut[i].meme_attribut(a))
				return i;
		}
		return -1;
	}

	public int no_attribut(NomFeature n)
	{
		assert(n!=null);
		if(liste_attribut==null)
			return -1;
		int i;
		for(i=0;i<liste_attribut.length;i++)
		{
			if(liste_attribut[i].nom.meme_nom(n))
				return i;
		}
		return -1;
	}
	
	public Attribut_Complet donne_attribut(NomFeature nom)
	{
		assert(nom!=null);
		int i;
		i=no_attribut(nom);
		if(i==-1)
			return null;
		return liste_attribut[i];
	}


	public void verifie_valide()
	{
		int i;
		
		if(liste_attribut==null)
			return;
		
		for(i=0;i<liste_attribut.length;i++)
		{
			liste_attribut[i].verifie_valide(context);
		}
	}
	
	public Attribut_Complet trouve_attribut(String nom)
	{
		if(liste_attribut!=null)
		{
			int i;
			Attribut_Complet ac;
			NomFeature nomf;
			for(i=0;i<liste_attribut.length;i++)
			{
				ac=liste_attribut[i];
				nomf=ac.nom;
				if(!nomf.infix&&!nomf.prefix&&
					nomf.nom.compareToIgnoreCase(nom)==0)
				{
					return ac;
				}
			}
		}
		return null;
	}
	
	public String toString()
	{
		String res="Table_Symbol de "+classe.nom+":{\n";
		if(liste_attribut!=null)
		{
			for(int i=0;i<liste_attribut.length;i++)
			{
				res+=liste_attribut[i].toString()+"\n";
			}
		}
		res+="}";
		
		return res;
	}
	
	public Classe classe;
	public Heritage[] heritage;
	public Logging log;
	public Attribut_Complet liste_attribut[];
	public Context context;
	public boolean heritage_ajoute,feature_ajoute;
	public Table_Symbol table_heritage[];
	public Conversion table_conversion[];

}
