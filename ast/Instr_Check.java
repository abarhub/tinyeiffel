package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Check extends Instr implements ToXML
{
	public Instr_Check(Vector expr)
	{
		liste_expr=new Assert[expr.size()];
		expr.copyInto(liste_expr);
	}

        public Position debut()
        {
          return tcheck.debut();
        }

	public Assert liste_expr[];
        public Token tcheck,tend;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Check);
		Instr_Check ins=(Instr_Check)instr;
		int i;
		assert(liste_expr.length==ins.liste_expr.length);
		for(i=0;i<liste_expr.length;i++)
		{
			liste_expr[i].check_egal(ins.liste_expr[i]);
		}
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		out.println("<instruction>");
		out.println("<check>");
		if(liste_expr!=null)
		{
			for(int i=0;i<liste_expr.length;i++)
			{
				liste_expr[i].toXML(out);
			}
		}
		out.println("</check>");
		out.println("</instruction>");
	}

}