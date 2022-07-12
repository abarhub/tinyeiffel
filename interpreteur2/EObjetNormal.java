package tinyeiffel.interpreteur2;

import java.util.HashMap;
import java.util.Map;

public class EObjetNormal extends EObjet {

	public EObjetNormal(EType t,Map attr) {
		super(t);
		attributs=new HashMap();
		if(attr!=null)
			attributs.putAll(attr);
	}

	public boolean contient_attribut(String nom)
	{
		return attributs.containsKey(nom);
	}

	public EObjet getAttribut(String nom)
	{
		return (EObjet) attributs.get(nom);
	}

	public void setAttribut(String nom,EObjet valeur)
	{
		attributs.put(nom,valeur);
	}
	
	private Map attributs;
}
