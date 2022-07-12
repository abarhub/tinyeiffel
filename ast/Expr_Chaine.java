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

public class Expr_Chaine extends Expr implements ToXML {
  public Expr_Chaine(Chaine str) {
      this.op=Expr.Chaine;
      assert(str!=null);
      //assert(false);
      assert(str.oper!=null);
      this.str2=str;
      this.oper=str.oper;
      assert(str.oper!=null);
      //this.oper=str.oper;
      assert(this.oper!=null);
    }


     public Position debut()
      {
        if(oper==null)
        {
          System.out.println("op=" + op);
          assert (false);
        }
        if(oper==null)
          return str2.oper.debut();
        else
          return oper.debut();
      }

      public final Token oper;
      public final Chaine str2;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Chaine);
		Expr_Chaine e2=(Expr_Chaine)e;
		str2.check_egal(e2.str2);
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		str2.toXML(out);
		out.println("</expression>");
	}

	public String toString()
	{
		return str2.toString();
	}

	public String simplifie()
	{
		String s;
		s=str2.toString();
		if(s!=null)
		{
			if(s.startsWith("\""))
				s=s.substring(1);
			if(s.endsWith("\""))
				s=s.substring(0,s.length()-1);
		}
		return s;
	}

}