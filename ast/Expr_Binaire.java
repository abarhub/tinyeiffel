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

public class Expr_Binaire extends Expr implements ToXML {
  public Expr_Binaire(int op,Expr e1,Expr e2,Token oper) {
    this.op=op;
    assert(e1!=null);
    assert(e2!=null);
    assert(oper!=null);
    assert(valide_op(op));
    this.op=op;
    this.expr1=e1;
    this.expr2=e2;
    this.oper=oper;
    free_op=null;
  }
  
	public Expr_Binaire(String op,Expr e1,Expr e2,Token oper)
	{
		assert(op!=null);
		this.op=Free_Op;
  		assert(e1!=null);
		assert(e2!=null);
		assert(oper!=null);
		assert(valide_op(this.op));
		this.expr1=e1;
		this.expr2=e2;
		this.oper=oper;
		this.free_op=op;
	}
	
	public NomFeature donne_nom_feature()
	{// TODO: pas beau
		NomFeature n;
		if(op==Free_Op)
		{
			n=new NomFeature(free_op);
			n.infix=true;
		}
		else
			n=super.donne_nom_feature();
		return n;
	}
	
  public Position debut()
  {
    assert(expr1!=null);
    if(expr1==null)
      return null;//new Position(-1,-1);
    assert(expr1!=null);
    return expr1.debut();
  }

  public boolean valide_op(int op)
  {
    switch(op)
    {
      case Plus:case Moins:
      case Fois:case Div:case Div_entier:case Mod:
      case Point:case Puiss:case Xor:case Or:case And:case And_Then:case Or_Else:case Implies:
      case Egal:case Diff:case Infs:case Inf:case Sup:case Sups:
	  case Free_Op:
        return true;
      default:
        return false;
    }
  }

  public final Token oper;
  public final Expr expr1,expr2;
  public final String free_op;
	/* (non-Javadoc)
	 * @see ast.Expr#check_egal(ast.Expr)
	 */
	public void check_egal(Expr e) {
		assert(e!=null);
		assert(e instanceof Expr_Binaire);
		assert(op==e.op):"Difference:"+op+"!="+e.op;
		Expr_Binaire e2=(Expr_Binaire)e;
		expr1.check_egal(e2.expr1);
		expr2.check_egal(e2.expr2);
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
			case Plus:
				ope="plus";
				break;
			case Moins:
				ope="moins";
				break;
			case Fois:
				ope="fois";
				break;
			case Div:
				ope="div";
				break;
			case Div_entier:
				ope="div_entier";
				break;
			case Mod:
				ope="mod";
				break;
			case Point:
				ope="point";
				break;
			case Or_Else:
				ope="or_else";
				break;
			case And_Then:
				ope="and_then";
				break;
			case Puiss:
				ope="puiss";
				break;
			case Implies:
				ope="implies";
				break;
			case Or:
				ope="or";
				break;
			case And:
				ope="and";
				break;
			case Xor:
				ope="xor";
				break;
			case Sup:
				ope="sup";
				break;
			case Sups:
				ope="sups";
				break;
			case Inf:
				ope="inf";
				break;
			case Infs:
				ope="infs";
				break;
			case Egal:
				ope="egal";
				break;
			case Diff:
				ope="diff";
				break;
			case Free_Op:
				ope="free_op";
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
		assert(ope!=null);
		out.println("<expression_binaire op=\""+ope+"\""+autre+">");
		expr1.toXML(out);
		expr2.toXML(out);
		out.println("</expression_binaire>");
		out.println("</expression>");
	}

	public String toString()
	{
		String nom_op;
		nom_op=donne_nom_feature().nom;
		return expr1.toString()+" "+nom_op+" "+expr2.toString(); 
	}

}