/*
 * Created on 28 sept. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.ast;

import java.io.PrintStream;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Commentaire implements ToXML,Traite {
	
	public Commentaire(String str,Token debut)
	{
		this.str=str;
		this.debut=debut; 
	}
	
	public String str;
	public Token debut;
	/* (non-Javadoc)
	 * @see ast.ToXML#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		// TODO Auto-generated method stub
		out.print("<commentaire>");
		out.print(str);		
		out.println("</commentaire>");
	}

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	protected boolean traite=false;

}
