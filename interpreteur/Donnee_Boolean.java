/*
 * Created on 26 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Donnee_Boolean extends Donnee {

	/**
	 * @param nom_classe
	 */
	public Donnee_Boolean() {
		super("BOOLEAN",true);
		type_special=true;
		valeur=false;
	}

	public String toString()
	{
		return ""+valeur;
	}

	public boolean valeur;
}
