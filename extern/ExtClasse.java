/*
 * Created on 28 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.extern;

import java.util.Vector;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtClasse {

	public ExtClasse(String nom,ExtAttribut attr[])
	{
		assert(nom!=null);
		this.nom=nom;
		liste_attribut=attr;
	}
	
	String nom;
	ExtAttribut liste_attribut[];
}
