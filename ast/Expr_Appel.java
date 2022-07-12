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

public class Expr_Appel extends Expr implements ToXML {
  public Expr_Appel(String nom,Vector param,Token oper) {
    this.op=Expr.Appel;
    this.parametre=param;
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

    public String nom;
    public Vector parametre;
    public final Token oper;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Appel);
		Expr_Appel e2=(Expr_Appel)e;
		assert(nom.equals(e2.nom));
		assert(parametre.size()==e2.parametre.size());
		for(int i=0;i<parametre.size();i++)
		{
			Expr e3=(Expr)parametre.elementAt(i);
			Expr e4=(Expr)e2.parametre.elementAt(i);
			e3.check_egal(e4);
		}
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		out.println("<appel nom=\""+nom+"\">");
		if(parametre!=null)
		{
			for(int i=0;i<parametre.size();i++)
			{
				out.println("<parametre_reel>");
				((Expr)parametre.elementAt(i)).toXML(out);
				out.println("</parametre_reel>");
			}
		}
		out.println("</appel>");
		out.println("</expression>");
	}
	
	public String toString()
	{
		String res;
		res=nom+"(";
		if(parametre!=null)
		{
			for(int i=0;i<parametre.size();i++)
			{
				if(i>0)
					res+=",";
				res+=parametre.elementAt(i);
			}
		}
		res+=")";
		return res;
	}
}