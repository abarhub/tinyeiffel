package tinyeiffel.interpreteur2;

import java.util.HashMap;
import java.util.Map;

import tinyeiffel.intermediaire.CIAttribut;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CIType;

public class EFrame {

	public EFrame(EObjet objet_courant,CIClasse classe,
			CIAttribut attr,EFrame suivant,EObjet param[])
	{
		assert(objet_courant!=null);
		assert(attr!=null);
		assert(classe!=null);
		assert(attr.routine!=null);
		if(attr.routine.parametre==null||attr.routine.parametre.size()==0)
		{
			assert(param==null||param.length==0);
		}
		else
		{
			assert(param.length==attr.routine.parametre.size());
		}
		this.objet_courant=objet_courant;
		this.classe=classe;
		this.attr=attr;
		this.suivant=suivant;
		this.param=param;
		local=new HashMap();
		if(attr.routine.local!=null&&attr.routine.local.size()>0)
		{
			for(int i=0;i<attr.routine.local.size();i++)
			{
				local.put(attr.routine.local.elt(i).nom, null);
			}
		}
		if(attr.routine.retour!=null)
		{
			local.put(attr.routine.retour.nom, null);
		}
	}
	
	public EObjet getObjet_courant() {
		return objet_courant;
	}
	
	public EFrame getSuivant() {
		return suivant;
	}

	public CIClasse getClasse()
	{
		return classe;
	}
	
	public CIAttribut getAttr()
	{
		return attr;
	}
	
	public EObjet getRetour()
	{
		//assert(attr.routine.retour!=null);
		return (EObjet) local.get(attr.routine.retour.nom);
	}
	
	public EObjet getVar(String nom)
	{
		if(nom.equalsIgnoreCase("current"))
		{
			return objet_courant;
		}
		else if(local.containsKey(nom))
		{
			return (EObjet) local.get(nom);
		}
		else if(objet_courant instanceof EObjetNormal)
		{
			EObjetNormal o=(EObjetNormal) objet_courant;
			if(o.contient_attribut(nom))
			{
				return o.getAttribut(nom);
			}
		}
		assert(false);
		return null;
	}
	
	public void setVar(String nom,EObjet valeur)
	{
		if(local.containsKey(nom))
		{
			local.put(nom,valeur);
		}
		else if(objet_courant instanceof EObjetNormal)
		{
			EObjetNormal o=(EObjetNormal) objet_courant;
			if(o.contient_attribut(nom))
			{
				o.setAttribut(nom,valeur);
			}
			else
			{
				assert(false):"variable "+nom+" inconnue";
			}
		}
		else
		{
			assert(false):"variable "+nom+" inconnue";
		}
	}
	
	public CIType getType(String nom)
	{
		if(nom.equalsIgnoreCase("current"))
		{
			return classe.nom;
		}
		else if(local.containsKey(nom))
		{
			//return (EObjet) local.get(nom);
			return attr.routine.local.donne_var(nom).type;
		}
		else if(objet_courant instanceof EObjetNormal)
		{
			EObjetNormal o=(EObjetNormal) objet_courant;
			if(o.contient_attribut(nom))
			{
				//return o.getAttribut(nom);
				//return o.
				CIAttribut attr;
				attr=classe.donne_attribut(nom);
				if(attr.retour!=null)
					return attr.retour;
			}
		}
		assert(false);
		return null;
	}

	public void setNewTentative() {
		assert(exception);
		exception=false;
	}

	public void setException(int code) {
		exception=true;
		code_exception=code;
	}
	
	public boolean isException()
	{
		return exception;
	}
	
	public int getCodeException()
	{
		return code_exception;
	}
	
	private EObjet objet_courant; 
	private CIClasse classe;
	private CIAttribut attr;
	private EFrame suivant;
	private EObjet param[];
	private Map local;
	private boolean exception;
	private int code_exception;
}
