package tinyeiffel.ast;

import java.io.PrintStream;

public class Instr_Affect extends Instr implements ToXML
{
	public Instr_Affect(String nom,Expr expr)
	{
		this.expr=expr;
		this.nom=nom;
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
			assert(instr instanceof Instr_Affect);
			Instr_Affect ins=(Instr_Affect)instr;
			assert(nom.equals(ins.nom));
			expr.check_egal(ins.expr);
			if(var_current==null)
				assert(ins.var_current==null);
			else
				assert(var_current.equalsIgnoreCase(ins.var_current));
		}
	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instruction>");
		if(var_current!=null)
		{
			out.println("<affect current=\""+var_current+"\">");
		}
		else
		{
			out.println("<affect>");
		}
		out.println("<variable nom=\""+nom+"\" />");
		expr.toXML(out);
		out.println("</affect>");
		out.println("</instruction>");
	}

}