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

public class Expr_Vrai extends Expr implements ToXML {
  public Expr_Vrai(Token oper) {
    this.op=Expr.Vrai;
    this.oper=oper;
  }

  public Position debut()
  {
    if(oper==null)
    {
      System.out.println("op="+op);
      assert(false);
      //return new Position(-1,-1);
    }
    return oper.debut();
  }

  public final Token oper;

	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Vrai);
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		out.println("<vrai />");
		out.println("</expression>");
	}

	public String toString()
	{
		return "true";
	}

}