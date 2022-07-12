/*
 * Created on 28 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.extern;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtRoutine {

	public ExtRoutine(String nom,ExtType param[],ExtType retour)
	{
		assert(nom!=null);
		this.nom=nom;
		this.parametre=param;
		this.type_retour=retour;
	}
	
	String nom;
	ExtType parametre[],type_retour;
}
