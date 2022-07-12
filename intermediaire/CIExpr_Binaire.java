/*
 * Created on 5 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIExpr_Binaire extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Binaire(int op,CIExpr_Scalaire expr1,
			CIExpr_Scalaire expr2,CISource source) {
		assert(expr1!=null);
		assert(expr2!=null);
		this.op=op;
		this.expr1=expr1;
		this.expr2=expr2;
		this.source=source;
	}

	public String toString()
	{
		return expr1.toString()+" "+nom_op()+" "+expr2.toString();
	}

	public String nom_op()
	{
		switch(op)
		{
			case Plus: return "+";
			case Div_entier: return "//";
			case Moins: return "-";
			case Fois: return "*";
			case Div_reel: return "/";
			case Puiss: return "^";
			case Mod: return "mod";
			case Sup: return ">=";
			case SupS: return ">";
			case Egal: return "=";
			case Inf: return "<=";
			case InfS: return "<";
			case Diff: return "/=";
			case And: return "and";
			case Or: return "or";
			case Xor: return "xor";
			case Implies: return "implies";
			case And_Then: return "and then";
			case Or_Else: return "or else";
		}
		assert(false);
		return null;
	}
	
	public void toXML(PrintStream out)
	{
		out.print("<expression_bin type=\"");
		switch(op)
		{
			case Plus: out.print("plus");break;
			case Div_entier: out.print("div_entier");break;
			case Moins: out.print("moins");break;
			case Fois: out.print("fois");break;
			case Div_reel: out.print("div_reel");break;
			case Puiss: out.print("puiss");break;
			case Mod: out.print("mod");break;
			case Sup: out.print("sup");break;
			case SupS: out.print("sups");break;
			case Egal: out.print("egal");break;
			case Inf: out.print("inf");break;
			case InfS: out.print("infs");break;
			case Diff: out.print("diff");break;
			case And: out.print("and");break;
			case Or: out.print("or");break;
			case Xor: out.print("xor");break;
			case Implies: out.print("implies");break;
			case And_Then: out.print("and_then");break;
			case Or_Else: out.print("or_else");break;
			default:assert(false):"op="+op;
		}
		out.println("\">");
		expr1.toXML(out);
		expr2.toXML(out);
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expression_bin>");
	}
	

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Binaire);
		CIExpr_Binaire e2=(CIExpr_Binaire)e;
		assert(expr1!=null);
		assert(expr2!=null);
		assert(e2.expr1!=null);
		assert(e2.expr2!=null);
		assert(op>=1);
		assert(op==e2.op);
		expr1.check_egal(e2.expr1);
		expr2.check_egal(e2.expr2);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public static final int Plus=1,Moins=2,Fois=3,Div_entier=4,
							Div_reel=5,Puiss=6,Mod=7,Sup=8,SupS=9,
							Egal=10,Inf=11,InfS=12,Diff=13,And=14,
							Or=15,Xor=16,Implies=17,And_Then=18,
							Or_Else=19;

	public CIExpr_Scalaire expr1,expr2;
	public int op;
	public CISource source;

}
