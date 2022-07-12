/*
 * Created on 8 mai 2005
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
public class VerifMethodeCreation extends Verif {

	/**
	 * 
	 */
	public VerifMethodeCreation(VerifType nom_classe,
			NomFeature nom,Position pos) {
		assert(nom_classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.nom_classe=nom_classe;
		this.nom=nom;
		this.pos=pos;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<methode_creation>");
		nom_classe.toXML(out);
		nom.toXML2(out);
		pos.toXML(out);
		out.println("</methode_creation>");
	}
	
	public VerifType nom_classe;
	public NomFeature nom;
	public Position pos;
	
}
