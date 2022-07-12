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
public class VerifClasseExiste extends Verif {

	public String classe;
	public Position pos;
	
	public VerifClasseExiste(String classe, Position pos)
	{
		super();
		this.classe=classe;
		this.pos=pos;
	}
	
	public boolean equal(Object o)
	{
		if(o==null||!(o instanceof VerifClasseExiste))
			return false;
		VerifClasseExiste v;
		v=(VerifClasseExiste)o;
		return v.classe.equalsIgnoreCase(classe);
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<classe_existe nom=\""+classe+"\">");
		pos.toXML(out);
		out.println("</classe_existe>");
	}
}
