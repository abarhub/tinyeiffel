package tinyeiffel.ast;

import java.io.PrintStream;

public class FeatureExternal extends Feature implements ToXML
{
	public FeatureExternal(Chaine str,Chaine alias)
	{
		this.str=str;
		this.alias=alias;
	}
	
	public boolean est_routine()
	{
		return true;
	}

	public Chaine str,alias;
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureExternal);
		FeatureExternal f2=(FeatureExternal)f;
		str.check_egal(f2.str);
		if(alias==null)
			assert(f2.alias==null);
		else
			alias.check_egal(f2.alias);
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLCorpsDebut(out);
		out.println("<external>");
		str.toXML(out);
		if(alias!=null)
			alias.toXML(out);
		out.println("</external>");
		toXMLCorpsFin(out);
	}

	public boolean est_external()
	{
		return true;
	}

}