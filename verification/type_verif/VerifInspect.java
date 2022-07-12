/*
 * Created on 8 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

import java.io.PrintStream;
import java.util.*;
import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifInspect extends Verif {

	
	/**
	 * 
	 */
	public VerifInspect(VerifType type_expr,Vector liste_cas,Position pos) {
		assert(type_expr!=null);
		assert(liste_cas!=null);
		assert(pos!=null);
		this.type_expr=type_expr;
		this.liste_cas=liste_cas;
		this.pos=pos;
	}


	public void toXML(PrintStream out)
	{// TODO: a revoir
		int i;
		Expr e;
		assert(out!=null);
		out.println("<inspect>");
		type_expr.toXML(out);
		for(i=0;i<liste_cas.size();i++)
		{
			if(liste_cas.elementAt(i) instanceof Vector)
			{
				//liste_cas.
			}
			else
			{
				e=(Expr)liste_cas.elementAt(i);
				e.toXML(out);
			}
		}
		pos.toXML(out);
		out.println("</inspect>");
	}
	
	public VerifType type_expr;
	/*
	 * Pour l'exemple :
	 * inspect t
	 * when 1,5,8..9 then
	 * when 7 then 
	 * when 9..5 then 
	 * else
	 * end
	 * ce vector doit etre de la forme :
	 * {1,5,{8,9},7,{9,5}}
	 * */
	public Vector liste_cas;
	public Position pos;
	
}
