/*
 * Created on 10 janv. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.lace;

import tinyeiffel.ast.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Grappe {

	/**
	 * 
	 */
	public Grappe(NomGrappe nom,Chaine repertoire) {
		assert(repertoire!=null);
		this.nom=nom;
		this.repertoire=repertoire;
	}

	public String donne_repertoire()
	{
		String s=repertoire.chaine_unique();
		if(s.length()>0)
		{
			if(s.endsWith("\""))
				s=s.substring(0,s.length()-1);
			if(s.startsWith("\""))
				s=s.substring(1);
		}
		return s;
	}

	public String getNom()
	{
		String s;
		if(nom==null)
			return null;
		s=nom.nom.toString();
		if(s.startsWith("\""))
			s=s.substring(1);
		return s;
	}

	NomGrappe nom;
	Chaine repertoire;

}
