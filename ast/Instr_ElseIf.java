package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_ElseIf extends Instr implements Suite, ToXML
{
	public Instr_ElseIf(Expr e,Vector instr)
	{
		expr=e;
		liste_instr=new Instr[instr.size()];
		instr.copyInto(liste_instr);
	}

        public Position debut()
        {
          return telseif.debut();
        }

	public Expr expr;
	public Instr liste_instr[];
	private Instr suivant;
        public Token telseif,tthen;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_ElseIf);
		Instr_ElseIf ins=(Instr_ElseIf)instr;
		int i;
		assert(liste_instr.length==ins.liste_instr.length);
		for(i=0;i<liste_instr.length;i++)
		{
			liste_instr[i].check_egal(ins.liste_instr[i]);
		}
		expr.check_egal(ins.expr);
		if(suivant==null)
		{
			assert(ins.suivant==null);
		}
		else
		{
			assert(ins.suivant!=null);
			suivant.check_egal(ins.suivant);
		}
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<elseif>");
		expr.toXML(out);
		int i;
		if(liste_instr!=null)
		{
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].toXML(out);
			}
		}
		out.println("</elseif>");
	}

	/* (non-Javadoc)
	 * @see ast.Suite#getSuivant()
	 */
	public Instr getSuivant() {
		return suivant;
	}

	/* (non-Javadoc)
	 * @see ast.Suite#setSuivant(ast.Instr)
	 */
	public void setSuivant(Instr instr) {
		assert(instr!=null);
		suivant=instr;
	}

}