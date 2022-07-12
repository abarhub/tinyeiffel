package tinyeiffel.ast;

import java.io.PrintStream;

public class FeatureDeferred extends Feature implements ToXML
{
	public FeatureDeferred()
	{
		
	}
	
	public boolean est_routine()
	{
		return true;
	}
		
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureDeferred);
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLCorpsDebut(out);
		out.println("<deferred/>");
		toXMLCorpsFin(out);
	}

	public boolean is_deferred()
	{
		return true;
	}

}