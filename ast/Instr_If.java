package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_If extends Instr implements Suite, ToXML
{
	public Instr_If(Expr expr,Vector instr)
	{
		this.expr=expr;
		this.liste_instr=new Instr[instr.size()];
		instr.copyInto(this.liste_instr);
	}

        public Position debut()
        {
          return tif.debut();
        }

	public Expr expr;
	public Instr liste_instr[];
	private Instr suivant;
        public Token tif,tthen;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_If);
		Instr_If ins=(Instr_If)instr;
		int i;
		if(liste_instr==null)
			assert(ins.liste_instr==null);
		else
		{
			assert(liste_instr.length==ins.liste_instr.length);
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].check_egal(ins.liste_instr[i]);
			}
		}
		expr.check_egal(ins.expr);
		if(suivant==null)
			assert(ins.suivant==null);
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
		out.println("<instruction>");
		out.println("<if>");
		expr.toXML(out);
		int i;
		if(liste_instr!=null)
		{
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].toXML(out);
			}
		}
		Instr ins=suivant;
		while(ins!=null)
		{
			ins.toXML(out);
			ins=((Suite)ins).getSuivant();
		}
		out.println("</if>");
		out.println("</instruction>");
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