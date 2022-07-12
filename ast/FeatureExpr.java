package tinyeiffel.ast;

import java.io.PrintStream;

public class FeatureExpr extends Feature implements ToXML
{
	public FeatureExpr()
	{
		
	}

	public Expr expr;
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureExpr);
		FeatureExpr f2=(FeatureExpr)f;
		expr.check_egal(f2.expr);
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLDebut(out);
		out.println("<corps>");
		expr.toXML(out);
		out.println("</corps>");
		toXMLFin(out);
	}

	public boolean est_constant()
	{
		return true;
	}

}