package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Inspect extends Instr implements Suite, ToXML
{
	public Instr_Inspect(Expr expr,Vector when,
		Vector liste_instr)
	{
		this.expr=expr;
		this.when=when;
		this.liste_instr=new Instr[liste_instr.size()];
		liste_instr.copyInto(this.liste_instr);
	}

        public Position debut()
        {
          return tinspect.debut();
        }

	public Expr expr;
	public Vector when;
	public Instr[] liste_instr;
	private Instr suivant;
        public Token tinspect,twhen,tthen;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Inspect);
		Instr_Inspect ins=(Instr_Inspect)instr;
		int i;
		assert(liste_instr.length==ins.liste_instr.length);
		for(i=0;i<liste_instr.length;i++)
		{
			liste_instr[i].check_egal(ins.liste_instr[i]);
		}
		assert(when.size()==ins.when.size());
		for(i=0;i<when.size();i++)
		{
			Vector v1=(Vector)when.elementAt(i);
			Vector u1=(Vector)ins.when.elementAt(i);
			assert(v1.size()==u1.size());
			for(int j=0;j<v1.size();j++)
			{
				assert(v1.elementAt(j) instanceof Expr);
				assert(u1.elementAt(j) instanceof Expr);
				Expr e=(Expr)v1.elementAt(j);
				Expr e2=(Expr)u1.elementAt(j);
				e.check_egal(e2);
			}
		}
		expr.check_egal(ins.expr);
		if(suivant==null)
			assert(ins.suivant==null);
		else
			suivant.check_egal(ins.suivant);
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		out.println("<instruction>");
		out.println("<inspect>");
		expr.toXML(out);
		Instr ins=this;
		Instr_Inspect insi;
		while(ins!=null)
		{
			assert(ins instanceof Instr_Inspect);
			insi=(Instr_Inspect)ins;
			Expr e;
			out.println("<when_then>");
			for(int i=0;i<insi.when.size();i++)
			{
				out.println("<expression_when>");
				assert(insi.when.elementAt(i) instanceof Vector);
				Vector v=(Vector)insi.when.elementAt(i);
				for(int j=0;j<v.size();j++)
				{
					assert(v.elementAt(j) instanceof Expr);
					e=(Expr)v.elementAt(j);
					e.toXML(out);
				}
				out.println("</expression_when>");
			}
			for(int k=0;k<insi.liste_instr.length;k++)
			{
				insi.liste_instr[k].toXML(out);
			}
			out.println("</when_then>");
			ins=((Suite)ins).getSuivant();
		}
		out.println("</inspect>");
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