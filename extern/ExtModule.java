/*
 * Created on 28 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.extern;

import java.util.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtModule {

	public ExtModule(String nom)
	{
		assert(nom!=null);
		this.nom=nom;
	}
	
	public void ajoute_routine(ExtRoutine r)
	{
		assert(r!=null);
	}
	
	public void ajoute_classe(ExtClasse c)
	{
		assert(c!=null);
	}
	
	String nom;
	Vector liste_routine;
	Vector liste_classe;
}
