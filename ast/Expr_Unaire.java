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

public class Expr_Unaire extends Expr implements ToXML {
  public Expr_Unaire(int op,Expr e1,Token oper) {
    this.op=op;
    assert(oper!=null);
    assert(valide_op(op));
    assert(e1!=null);
    this.op=op;
    this.expr1=e1;
    this.oper=oper;
    free_op=null;
  }

  public Expr_Unaire(String op,Expr e1,Token oper) {
	  this.op=Free_OpU;
	  assert(op!=null);
	  assert(oper!=null);
	  assert(valide_op(this.op));
	  assert(e1!=null):"op="+op+";token="+oper.debut();
	  this.expr1=e1;
	  this.oper=oper;
	  this.free_op=op;
	}
	
	public NomFeature donne_nom_feature()
	{// TODO: pas beau
		NomFeature n;
		if(op==Free_OpU)
		{
			n=new NomFeature(free_op);
			n.prefix=true;
		}
		else
			n=super.donne_nom_feature();
		return n;
	}
	
    public Position debut()
    {
      assert(oper!=null);
      Position t=oper.debut();
      assert(t!=null);
      return t;
    }

    public boolean valide_op(int op)
    {
      switch(op)
      {
        case Old:
        case Not:
        case PlusU:
        case MoinsU:
		case Free_OpU:
		case Dollard:
          return true;
        default:
          return false;
      }
    }

    public final Token oper;
    public final Expr expr1;
    public final String free_op;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Unaire);
		Expr_Unaire e2=(Expr_Unaire)e;
		assert(op==e2.op):"Difference:"+op+"!="+e2.op;
		expr1.check_egal(e2.expr1);
	}

	/* (non-Javadoc)
	 * @see ast.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<expression>");
		String ope=null,autre="";
		switch(op)
		{
			case PlusU:
				ope="plus";
				break;
			case MoinsU:
				ope="moins";
				break;
			case Old:
				ope="old";
				break;
			case Not:
				ope="not";
				break;
			case Dollard:
				ope="addr";
				break;
			case Free_OpU:
				//ope=free_op;
				ope="free_op";
				//autre=" nom=\""+free_op+"\"";
				if(free_op.startsWith("&"))
				{
					autre=" nom=\""+"&amp;"+free_op.substring(1,free_op.length())+"\"";
				}
				else
				{
					autre=" nom=\""+free_op+"\"";
				}
				break;
			default:
				assert(false);
		}
		out.println("<expression_unaire op=\""+ope+"\""+autre+">");
		expr1.toXML(out);
		out.println("</expression_unaire>");
		out.println("</expression>");
	}

	public String toString()
	{
		String nom_op;
		nom_op=donne_nom_feature().nom;
		return nom_op+" "+expr1.toString(); 
	}

}