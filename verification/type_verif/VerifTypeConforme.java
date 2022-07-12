/*
 * Created on 5 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

import java.io.PrintStream;

import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifTypeConforme extends Verif {

	// vérifie que type_ancetre est un descendant de type_descendant
	public VerifTypeConforme(VerifType type_ancetre,
			VerifType type_descendant,Type nom_classe)
	{
		assert(type_ancetre!=null);
		assert(type_descendant!=null);
		assert(nom_classe!=null);
		this.type_ancetre=type_ancetre;
		this.type_descendant=type_descendant;
		this.nom_classe=nom_classe;
	}
	
	// verifie que type_descendant n'a pas de type
	public VerifTypeConforme(VerifType type_descendant,Type nom_classe)
	{
		assert(type_descendant!=null);
		assert(nom_classe!=null);
		this.type_ancetre=null;
		this.type_descendant=type_descendant;
		this.nom_classe=nom_classe;
	}
	
	public boolean pret_test()
	{
		return type_ancetre.a_type_reel()&&type_descendant.a_type_reel();
	}
	
	public boolean est_conforme()
	{// TODO: a terminer
		assert(pret_test());
		assert(false);
		return true;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<type_conforme>");
		if(type_ancetre!=null)
		{
			type_ancetre.toXML(out);
		}
		type_descendant.toXML(out);
		nom_classe.toXML(out);
		out.println("</type_conforme>");
	}
	
	public VerifType type_ancetre;
	public VerifType type_descendant;
	protected Type nom_classe;
	
}
