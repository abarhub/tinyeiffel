/*
 * Created on 27 juin 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.interpreteur;

import tinyeiffel.ast.*;
import java.io.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Donnee_Std_File extends Donnee {

	/**
	 * @param nom_classe
	 * @param expanded
	 */
	public Donnee_Std_File() {
		super("STD_FILE", false);
		out=System.out;
	}
	
	public String toString()
	{
		return "STD_FILE";
	}
	
	public void affiche(Donnee_Int i)
	{
		out.println(i.get());
	}
	
	public Donnee fonction_externe(NomFeature nf,Donnee param[])
	{
		assert(nf!=null);
		//assert(nf.nom2!=null):"nom="+nf.nom;
		//assert(nf.nom2.chaine_unique()!=null);
		//if(nf.infix&&nf.nom2.chaine_unique().equalsIgnoreCase("\"+\""))
		if(nf.nom.equalsIgnoreCase("put_integer"))
		{
			assert(param!=null);
			assert(param.length==1);
			assert(param[0]!=null);
			assert(param[0]instanceof Donnee_Int);
			affiche((Donnee_Int)param[0]);
			return null;
		}
		else
		{
			super.fonction_externe(nf,param);
		}
		return null;
	}
	
	protected PrintStream out;
	
}
