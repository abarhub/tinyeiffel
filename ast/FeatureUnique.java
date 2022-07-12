package tinyeiffel.ast;

import java.io.PrintStream;

public class FeatureUnique extends Feature implements ToXML
{
	public FeatureUnique()
	{
		
	}
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureUnique);
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLDebut(out);
		out.println("<corps>");
		out.println("<unique/>");
		out.println("</corps>");
		toXMLFin(out);
	}

	public boolean est_constant()
	{
		return true;
	}

}