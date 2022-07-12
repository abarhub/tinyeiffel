package tinyeiffel.ast;

import java.io.PrintStream;

/**
 * <p>Title: l'ast d'eiffel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Barret Alain
 * @version 1.0
 */

public class Expr_Var extends Expr implements ToXML {
  public Expr_Var(String nom,Token oper) {
    this.op=Expr.Var;
    assert(nom!=null);
    assert(oper!=null);
    assert(!nom.equals(""));
    this.nom=nom;
    this.oper=oper;
  }

  public Position debut()
  {
    assert(oper!=null);
    Position p=oper.debut();
    assert(p!=null);
    return p;
  }

  public final String nom;
  public final Token oper;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Var):"different:"+
			this.toString()+"/="+e.toString();
		Expr_Var e2=(Expr_Var)e;
		assert(nom.equals(e2.nom));
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		out.println("<variable nom=\""+nom+"\"/>");
		out.println("</expression>");
	}

	public String toString()
	{
		return nom;
	}

}