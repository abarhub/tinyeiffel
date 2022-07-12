package tinyeiffel.ast;

import java.io.PrintStream;

public class Instr_Retry extends Instr implements ToXML
{
	public Instr_Retry()
	{

	}

        public Position debut()
        {
          return tretry.debut();
        }

        public Token tretry;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Retry);
		Instr_Retry ins=(Instr_Retry)instr;

	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instruction>");
		out.println("<retry/>");
		out.println("</instruction>");
	}

}