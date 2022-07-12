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

public class Expr_Entier extends Expr implements ToXML {

  public Expr_Entier(String entier,Token oper) {
    this.op=Expr.Entier;
    str=entier;
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
    public final String str;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Entier);
		Expr_Entier e2=(Expr_Entier)e;
		assert(str.equals(e2.str));
	}

	public String toString()
	{
		return str;
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		out.println("<entier valeur=\""+str+"\" />");
		out.println("</expression>");
	}

}