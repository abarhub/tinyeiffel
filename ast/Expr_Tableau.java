package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.Vector;

/**
 * <p>Title: l'ast d'eiffel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Barret Alain
 * @version 1.0
 */

public class Expr_Tableau extends Expr implements ToXML {
  public Expr_Tableau(Vector tableau,Token oper) {
    this.op=Expr.Tableau;
    assert(oper!=null);
    this.tableau=tableau;
    this.oper=oper;
  }

    public Position debut()
    {
      assert(false);
      return null;
    }

    public Vector tableau;
    public Token oper;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Tableau);
		Expr_Tableau e2=(Expr_Tableau)e;
		assert(tableau.size()==e2.tableau.size());
		for(int i=0;i<tableau.size();i++)
		{
			Expr e3=(Expr)tableau.elementAt(i);
			Expr e4=(Expr)e2.tableau.elementAt(i);
			e3.check_egal(e4);		
		}
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");

		out.println("<tableau>");
		if(tableau!=null)
		{
			for(int i=0;i<tableau.size();i++)
			{
				((Expr)tableau.elementAt(i)).toXML(out);
			}
		}
		out.println("</tableau>");
		out.println("</expression>");
	}

	public String toString()
	{
		String res;
		res="<<";
		if(tableau!=null)
		{
			for(int i=0;i<tableau.size();i++)
			{
				if(i>0)
					res+=",";
				res+=tableau.elementAt(i);
			}
		}
		res+=">>";
		return res;
	}

}