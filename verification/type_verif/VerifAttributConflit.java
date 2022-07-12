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
public class VerifAttributConflit extends Verif {

	public String classe;
	public NomFeature nom_attribut;
	public Position pos;
	
	public VerifAttributConflit(String classe,NomFeature nom_attribut,
			Position pos)
	{
		super();
		this.classe=classe;
		this.nom_attribut=nom_attribut;
		this.pos=pos;
	}

	public boolean equal(Object o)
	{
		if(o==null||!(o instanceof VerifAttributConflit))
			return false;
		VerifAttributConflit v;
		v=(VerifAttributConflit)o;
		return v.classe.equalsIgnoreCase(classe)&&
			v.nom_attribut.equals(nom_attribut);
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<attribut_conflit nom_classe=\""+classe+"\">");
		nom_attribut.toXML2(out);
		pos.toXML(out);
		out.println("</attribut_conflit>");
	}
	
}
