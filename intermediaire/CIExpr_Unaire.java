/*
 * Created on 6 févr. 2004
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
public class CIExpr_Unaire extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Unaire(int op,CIExpr_Scalaire expr,CISource source) {
		assert(expr!=null);
		this.op=op;
		this.expr=expr;
		this.source=source;
	}

	public String toString()
	{
		return nom_op()+" "+expr.toString();
	}

	public void toXML(PrintStream out)
	{
		out.print("<expression_un type=\"");
		switch(op)
		{
			case Plus: out.print("plus");break;
			case Moins: out.print("moins");break;
			case Conv_E2R: out.print("conv_e2r");break;
			case Conv_E2D: out.print("conv_e2d");break;
			case Conv_R2D: out.print("conv_r2d");break;
			case Not: out.print("not");break;
			case Old: out.print("old");break;
			case Dollard: out.print("addr");break;
			default:assert(false):"op="+op;
		}
		out.println("\">");
		expr.toXML(out);
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expression_un>");
	}

	public String nom_op()
	{
		switch(op)
		{
			case Plus: return "+";
			case Moins: return "-";
			case Conv_E2R: return "conv_e2r";
			case Conv_E2D: return "conv_e2d";
			case Conv_R2D: return "conv_r2d";
			case Old: return "old";
			case Not: return "not";
			case Dollard: return "addr";
			case Conv_R2E: return "conv_r2e";
			case Conv_E2C: return "conv_e2c";
			case Conv_D2E: return "conv_d2e";
			case Conv_D2R: return "conv_d2r";
			case Conv_C2E: return "conv_c2e";
		}
		assert(false):op;
		return null;
	}

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Unaire);
		CIExpr_Unaire e2=(CIExpr_Unaire)e;
		assert(expr!=null);
		assert(e2.expr!=null);
		assert(op>=1);
		assert(op==e2.op);
		expr.check_egal(e2.expr);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	

	public static final int Plus=31,Moins=32,Conv_E2R=33,Conv_E2D=34,
							Conv_R2D=35,Not=36,Old=37,Dollard=38,
							Conv_D2R=39,Conv_D2E=40,Conv_R2E=41,
							Conv_E2C=42,Conv_C2E=43;

	public CIExpr_Scalaire expr;
	public int op;
	public CISource source;

}
