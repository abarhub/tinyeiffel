/*
 * Created on 27 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.intermediaire;

import java.io.PrintStream;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CISource {

	public CISource(int ligne,int colonne,String fichier)
	{
		this.ligne=ligne;
		this.colonne=colonne;
		this.fichier=fichier;
	}
	
	public String toString()
	{
		return "source("+ligne+","+colonne+((fichier!=null)?fichier:"")+")";
	}

	/* (non-Javadoc)
	 * @see intermediaire.Expr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		if(fichier!=null)
		{
			out.println("<source ligne="+ligne+
					" colonne="+colonne+" fichier=\""+fichier+"\" />");
		}
		else
		{
			out.println("<source ligne="+ligne+
					" colonne="+colonne+" />");
		}
	}

	public void check_egal(CISource e)
	{
		assert(e!=null);
		assert(e instanceof CISource);
		//CISource e2=(CIExpr_Type)e;
		assert(e.ligne==ligne);
		assert(e.colonne==colonne);
		if(fichier==null)
		{
			assert(e.fichier==null);
		}
		else
		{
			assert(e.fichier.equals(fichier));
		}
	}
	
	public int ligne,colonne;
	public String fichier;
}
