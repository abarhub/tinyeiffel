/*
 * Created on 24 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification;

import java.util.*;
import tinyeiffel.verification.type_verif.*;
import tinyeiffel.compiler.Logging;
import tinyeiffel.erreur.*;
import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 */
public class VerificationsGlobales {

	Map liste_verifications;
	Vector liste_classe_existe;
	Vector liste_attribut_existe;
	Vector liste_attribut_conflit;
	Vector liste_methode;
	Vector liste_methode_creation;
	Vector liste_type_conforme;
	Vector liste_inspect;
	Logging logging;
	
	public VerificationsGlobales(Logging logging)
	{
		assert(logging!=null);
		liste_verifications=new HashMap();
		liste_classe_existe=new Vector();
		liste_attribut_existe=new Vector();
		liste_attribut_conflit=new Vector();
		liste_methode=new Vector();
		liste_methode_creation=new Vector();
		liste_type_conforme=new Vector();
		liste_inspect=new Vector();
		this.logging=logging;
	}
	
	public void ajoute(Verifications v)
	{
		int i;
		Verif verif;
		Vector liste;
		assert(v!=null);
		liste=v.liste_verifications;
		liste_verifications.put(v.nom_classe.nom.toUpperCase(),v);
		for(i=0;i<liste.size();i++)
		{
			verif=(Verif)liste.elementAt(i);
			if(verif instanceof VerifClasseExiste)
			{
				if(!liste_classe_existe.contains(verif))
					liste_classe_existe.addElement(verif);
			}
			else if(verif instanceof VerifAttributExiste)
			{
				if(!liste_attribut_existe.contains(verif))
					liste_attribut_existe.addElement(verif);
			}
			else if(verif instanceof VerifAttributConflit)
			{
				if(!liste_attribut_conflit.contains(verif))
					liste_attribut_conflit.addElement(verif);
			}
			else if(verif instanceof VerifMethode)
			{
				if(!liste_methode.contains(verif))
					liste_methode.addElement(verif);
			}
			else if(verif instanceof VerifTypeConforme)
			{
				if(!liste_type_conforme.contains(verif))
					liste_type_conforme.addElement(verif);
			}
			else if(verif instanceof VerifMethodeCreation)
			{
				if(!liste_methode_creation.contains(verif))
					liste_methode_creation.addElement(verif);
			}
			else if(verif instanceof VerifInspect)
			{
				if(!liste_inspect.contains(verif))
					liste_inspect.addElement(verif);
			}
			else
			{
				assert(false);
			}
		}
	}
	
	public boolean traitement()
	{
		int i;
		Verif v;
		Verifications verif;
		VerifClasseExiste v1;
		VerifAttributExiste v2;
		VerifAttributConflit v3;
		boolean res=true;
		assert(liste_verifications!=null);
		for(i=0;i<liste_classe_existe.size();i++)
		{
			v1=(VerifClasseExiste)liste_classe_existe.elementAt(i);
			verif=cherche_classe(v1.classe);
			if(verif==null)
			{
				logging.erreur(new ErreurVTCT(v1.classe,v1.pos));
				res=false;
				// TODO:donc on supprime les erreurs associés a la classe v1.classe
			}
			else
			{
				v1.set_ok();
			}
		}
		for(i=0;i<liste_attribut_existe.size();i++)
		{
			v2=(VerifAttributExiste)liste_attribut_existe.elementAt(i);
			verif=cherche_classe(v2.classe);
			assert(verif!=null);
			if(!verif.liste_attribut.containsKey(v2.nom_attribut))
			{
				logging.erreur(new ErreurVEEN(v2.classe,v2.nom_attribut,v2.pos));
				res=false;
			}
			else
			{
				v2.set_ok();
			}
		}
		for(i=0;i<liste_attribut_conflit.size();i++)
		{
			v3=(VerifAttributConflit)liste_attribut_conflit.elementAt(i);
			verif=cherche_classe(v3.classe);
			assert(verif!=null);
			if(verif.liste_attribut.containsKey(v3.nom_attribut.toString()))
			{
				VerifAttribut v0=(VerifAttribut)verif.liste_attribut.get(v3.nom_attribut.toString());
				logging.erreur(new ErreurVFFD2(v3.classe,v3.nom_attribut,
						v0.debut(),v3.pos));
				res=false;
			}
			else
			{
				v3.set_ok();
			}
		}
		if(res)
		{
			boolean b;
			b=verification_methodes();
			if(!b)
				res=false;
		}
		return res;
	}
	
	public boolean verification_methodes()
	{
		int i,j;
		VerifMethode vm;
		Verifications verif;
		VerifAttribut va;
		VerifTypeConforme vtc1,vtc2;
		VerifType vt1,vt2;
		for(i=0;i<liste_methode.size();i++)
		{
			vm=(VerifMethode)liste_methode.elementAt(i);
			assert(vm.nom_classe!=null);
			if(vm.nom_classe.a_type_reel()&&!vm.get_ok())
			{
				verif=cherche_classe(vm.nom_classe.get_type_reel().nom);
				assert(verif!=null);
				va=cherche_attribut(verif,vm.nom);
				if(va!=null)
				{// TODO: a terminer
					boolean erreur=false;
					if(vm.param==null||vm.param.length==0)
					{
						if(va.param==null||va.param.length==0)
						{
							
						}
						else
						{// Erreur
							erreur=true;
							logging.erreur(new ErreurVUAR1(vm.nom,
									verif.nom_classe(),va.param.length,va.debut(),
									vm.nom_classe.get_type_reel().nom,0,vm.pos));
						}
					}
					else
					{
						if(va.param==null||va.param.length!=vm.param.length)
						{// Erreur
							erreur=true;
							logging.erreur(new ErreurVUAR1(vm.nom,
									verif.nom_classe(),0,va.debut(),
									vm.nom_classe.get_type_reel().nom,vm.param.length,vm.pos));
						}
						else
						{
							for(j=0;j<vm.param.length;j++)
							{
								vm.param[j].set_type_reel(va.param[j].getType());
							}
						}
					}
					if(!erreur)
					{
						if(vm.type_retour==null)
						{
							if(va.type_retour==null)
							{
								
							}
							else
							{// Erreur
								logging.erreur(new ErreurVUAR1(vm.nom,
										verif.nom_classe(),-1,va.debut(),
										vm.nom_classe.get_type_reel().nom,0,vm.pos));
							}
						}
						else
						{
							if(va.type_retour==null)
							{// Erreur
								logging.erreur(new ErreurVUAR1(vm.nom,
										verif.nom_classe(),0,va.debut(),
										vm.nom_classe.get_type_reel().nom,-1,vm.pos));
							}
							else
							{
								//types_compatibles(va.type_retour,vm.type_retour.get_type_reel());
								vm.type_retour.set_type_reel(va.type_retour);
							}
						}
					}
				}
				else
				{
					assert(false):"classe:"+verif.nom_classe()+",methode="+vm.nom+" non trouvée";
				}
				vm.set_ok();
			}
		}
		for(i=0;i<liste_type_conforme.size();i++)
		{
			vtc1=(VerifTypeConforme)liste_type_conforme.elementAt(i);
			vt1=vtc1.type_ancetre;
			vt2=vtc1.type_descendant;
			if(vt1!=null)
			{// Vérifie que vt1 est un ancetre de vt2
				
			}
			else
			{// Vérifie que vt2 n'a pas de type
				
			}
		}
		return true;
	}
	
	boolean heritage[][]=null;
	String liste_type[]=null;
	
	public boolean type_compatible(Type t1,Type t2)
	{
		boolean res;
		assert(t1!=null);
		assert(t2!=null);
		if(heritage==null)
		{
			int len_type,i,j;
			Verifications v,v2;
			Set ens_type;
			String s1,s2;
			len_type=liste_verifications.size();
			ens_type=liste_verifications.keySet();
			liste_type=new String[len_type];
			heritage=new boolean[len_type][len_type];
			liste_type=(String[])ens_type.toArray();
			for(i=0;i<len_type;i++)
			{
				s1=liste_type[i];
				
				for(j=0;j<len_type;j++)
				{
					
				}
			}
		}
		res=true;
		return res;
	}

	int indice(String liste[],String elt)
	{
		int i;
		assert(liste!=null);
		assert(elt!=null);
		for(i=0;i<liste.length;i++)
		{
			if(elt.equalsIgnoreCase(liste[i]))
				return i;
		}
		return -1;
	}
	
	public VerifAttribut cherche_attribut(Verifications classe,NomFeature nom)
	{
		VerifAttribut res,tmp;
		
		assert(classe!=null);
		assert(nom!=null);
		if(classe.liste_attribut.containsKey(nom.toString()))
		{
			res=(VerifAttribut)classe.liste_attribut.get(nom.toString());
		}
		else
		{
			// recherche dans les ancetres
			if(classe.liste_heritage!=null&&
				classe.liste_heritage.length>0)
			{
				int i;
				Verifications cl;
				Heritage h;
				NomFeature n;
				res=null;
				for(i=0;i<classe.liste_heritage.length;i++)
				{
					h=classe.liste_heritage[i];
					n=nom;
					cl=cherche_classe(h.type.nom);
					assert(cl!=null);
					if(h.rename!=null&&h.rename.length>0)
					{// TODO: prendre en compte le renomage
						int j;
						for(j=0;j<h.rename.length;j++)
						{
							if(h.rename[j].nom_dest.equals(n))
							{
								n=h.rename[j].nom_src;
								break;
							}
						}
					}
					tmp=cherche_attribut(cl,n);
					if(tmp!=null)
					{
						res=tmp;
						break;
					}
				}
			}
			else
			{
				if(classe.nom_classe.nom.equalsIgnoreCase("any")||
					classe.nom_classe.nom.equalsIgnoreCase("general")||
					classe.nom_classe.nom.equalsIgnoreCase("plateform"))
				{// pour eviter une recherche circulaire
					res=null;
				}
				else
				{
					Verifications any;
					any=cherche_classe("any");
					assert(any!=null);
					if(any.liste_attribut.containsKey(nom.toString()))
					{
						res=(VerifAttribut)any.liste_attribut.get(nom.toString());
					}
					else
					{
						res=null;
					}
				}
			}
		}
		return res;
	}
	
	public Verifications cherche_classe(String nom)
	{
		assert(liste_verifications!=null);
		assert(nom!=null);
		String s=nom.toUpperCase();
		if(liste_verifications.containsKey(s))
			return (Verifications)liste_verifications.get(s);
		else
			return null;
	}
	
}
