/*
 * Created on 12 févr. 2004
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
public class CIExpr_Creation extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Creation(CIType type,CIExpr_Appel appel,
			CISource source) {
		assert(type!=null);
		//assert(appel!=null);// il peu n'y avoir aucun appel
		this.appel=appel;
		this.type=type;
		this.source=source;
	}
	
	public CIExpr_Creation(CIType type,CIExpr_Scalaire index,
			CISource source) {
		assert(type!=null);
		assert(index!=null);
		this.index=index;
		this.type=type;
		this.source=source;
	}

	public String toString()
	{
		if(appel!=null)
		{
			return "!"+type+"!"+appel;
		}
		else if(index!=null)
		{
			return "!"+type+"!["+index+"]";
		}
		else
		{
			return "!"+type+"!";
		}
	}

	/* (non-Javadoc)
	 * @see intermediaire.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		out.println("<expression_creation>");
		type.toXML(out);
		if(appel!=null)
		{
			appel.toXML(out);
		}
		else if(index!=null)
		{
			index.toXML(out);
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expression_creation>");
	}

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Creation);
		CIExpr_Creation e2=(CIExpr_Creation)e;
		assert(type!=null);
		assert(e2.type!=null);
		type.check_egal(e2.type);
		if(appel!=null)
		{
			assert(e2.appel!=null);
			appel.check_egal(e2.appel);
			assert(e2.index==null);
		}
		else
		{
			assert(e2.appel==null);
			if(index!=null)
			{
				assert(index!=null);
				assert(e2.index!=null);
				index.check_egal(e2.index);
			}
			else
			{
				assert(e2.index==null);
			}
		}
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}

	public CIExpr_Appel appel;
	public CIType type;
	public CIExpr_Scalaire index;
	public CISource source;

}
