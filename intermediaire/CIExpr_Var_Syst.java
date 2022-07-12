/*
 * Created on 28 mars 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.PrintStream;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIExpr_Var_Syst extends CIExpr_Scalaire {

	public CIExpr_Var_Syst(String nom,CIExpr_Scalaire param[],
			CISource source)
	{
		assert(nom!=null);
		this.nom=nom;
		this.param=param;
		this.source=source;
	}

	public String toString()
	{
		String res="$"+nom;
		if(param!=null)
		{
			int i;
			res+="(";
			for(i=0;i<param.length;i++)
			{
				if(i>0)
					res+=",";
				res+=param[i].toString();
			}
			res+=")";
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see intermediaire.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		out.println("<expression_scalaire type=\"var_syst\" >");
		out.println("<expr_var_syst nom=\""+nom+"\">");
		if(param!=null)
		{
			int i;
			for(i=0;i<param.length;i++)
			{
				param[i].toXML(out);
			}
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expr_var_syst>");
		out.println("</expression_scalaire>");
	}

	/* (non-Javadoc)
	 * @see intermediaire.Expr#check_egal(intermediaire.Expr)
	 */
	public void check_egal(CIExpr e) {
		assert(e!=null);
		assert(e instanceof CIExpr_Var_Syst);
		CIExpr_Var_Syst ev=(CIExpr_Var_Syst)e;
		assert(nom!=null);
		assert(ev.nom!=null);
		assert(nom.equalsIgnoreCase(ev.nom));
		if(param==null||param.length==0)
			assert(ev.param==null||ev.param.length==0);
		else
		{
			int i;
			for(i=0;i<param.length;i++)
			{
				assert(param[i]!=null);
				assert(ev.param[i]!=null);
				param[i].check_egal(ev.param[i]);
			}
		}
		if(source!=null)
		{
			source.check_egal(((CIExpr_Var_Syst)e).source);
		}
		else
		{
			assert(((CIExpr_Var_Syst)e).source==null);
		}
	}

	public String nom;
	public CIExpr_Scalaire param[];
	public CISource source;

}
