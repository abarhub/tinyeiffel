/*
 * Created on 23 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

import java.io.*;

import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifAttributExiste extends Verif {

	public String classe;
	public NomFeature nom_attribut;
	public Position pos;
	
	public VerifAttributExiste(String classe,NomFeature nom_attribut,
			Position pos)
	{
		super();
		this.classe=classe;
		this.nom_attribut=nom_attribut;
		this.pos=pos;
	}

	public boolean equal(Object o)
	{
		if(o==null||!(o instanceof VerifAttributExiste))
			return false;
		VerifAttributExiste v;
		v=(VerifAttributExiste)o;
		return v.classe.equalsIgnoreCase(classe)&&
			v.nom_attribut.equals(nom_attribut);
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<attribut_existe nom_classe=\""+classe+"\">");
		nom_attribut.toXML2(out);
		pos.toXML(out);
		out.println("</attribut_existe>");
	}
}
