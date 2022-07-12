package tinyeiffel.ast;

import java.io.PrintStream;

public class Instr_TentAffect extends Instr implements ToXML
{
	public Instr_TentAffect(String nom,Expr expr)
	{
		this.nom=nom;
		this.expr=expr;
	}

        public void set_token(Token tid,Token tegal)
        {
           this.tid=tid;
           this.tegal=tegal;
        }

        public Position debut()
        {
        	if(tcurrent!=null)
        		return tcurrent.debut();
        	else
          		return tid.debut();
        }

	public Expr expr;
	public String nom,var_current;
        public Token tid,tegal,tcurrent;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_TentAffect);
		Instr_TentAffect ins=(Instr_TentAffect)instr;
		assert(nom.equals(ins.nom));
		expr.check_egal(ins.expr);
		if(var_current==null)
		{
			assert(ins.var_current==null);
		}
		else
		{
			assert(var_current.equalsIgnoreCase(ins.var_current));
		}
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instruction>");
		if(var_current!=null)
		{
			out.println("<tentative_affect current=\""+var_current+"\">");
		}
		else
		{
			out.println("<tentative_affect>");
		}
		out.println("<variable nom=\""+nom+"\" />");
		expr.toXML(out);
		out.println("</tentative_affect>");
		out.println("</instruction>");
	}

}