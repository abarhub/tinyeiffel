package tinyeiffel.intermediaire;

import java.io.PrintStream;

public class CIExpr_Double extends CIExpr_Scalaire {

	public CIExpr_Double(double dbl,CISource source) {
		this.dbl=dbl;
		this.source=source;
	}

	public String toString()
	{
		return ""+dbl;
	}
	
	public void toXML(PrintStream out) {
		out.print("<expression_scalaire type=\"double\" text=\""+dbl+"\" ");
		if(source!=null)
		{
			out.println("/>");
			source.toXML(out);
			out.println("</expression_scalaire>");
		}
		else
		{
			out.println("/>");
		}
	}

	public void check_egal(CIExpr e) {

		assert(e!=null);
		assert(e instanceof CIExpr_Double);
		CIExpr_Double e2=(CIExpr_Double)e;
		assert(dbl==e2.dbl);
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}

	public final double dbl;
	public CISource source;
}
