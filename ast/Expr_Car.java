package tinyeiffel.ast;

import java.io.PrintStream;

/**
 * <p>Title: l'ast d'eiffel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Expr_Car extends Expr implements ToXML
{
  public Expr_Car(String car,Token oper) {
    this.op=Expr.Char;
    this.car=car;
    this.oper=oper;
  }

  public Position debut()
  {
    if(oper==null)
    {
      System.out.println("op=" + op);
      assert (false);
    }
    return oper.debut();
  }

  public final String car;
  public final Token oper;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Car);
		Expr_Car e2=(Expr_Car)e;
		assert(car.equals(e2.car)):"Difference:\""+car+
					"\"!=\""+e2.car+"\"";
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		out.print("<car>");
		out.print(car);
		out.println("</car>");
		out.println("</expression>");
	}

	public String toString()
	{
		return car;
	}

}