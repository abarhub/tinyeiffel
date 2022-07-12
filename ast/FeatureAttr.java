package tinyeiffel.ast;

import java.io.PrintStream;

public class FeatureAttr extends Feature implements ToXML
{
	public FeatureAttr()
	{
		
	}

	public boolean est_variable()
	{
		return true;
	}

	
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureAttr);
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLDebut(out);
		toXMLFin(out);
	}

}