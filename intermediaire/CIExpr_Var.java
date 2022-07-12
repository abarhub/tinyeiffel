/*
 * Created on 5 févr. 2004
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
public class CIExpr_Var extends CIExpr_Scalaire {

	/**
	 * 
	 */
	public CIExpr_Var(String nom,CISource source) {
		assert(nom!=null);
		this.nom=nom;
		this.source=source;
	}

	public CIExpr_Var(String nom,CIExpr_Scalaire indice,CISource source) {
		assert(nom!=null);
		assert(indice!=null);
		assert(indice instanceof CIExpr_Entier||
				indice instanceof CIExpr_Var);
		this.nom=nom;
		this.indice=indice;
		this.source=source;
	}
	
	public void set_src(String nom)
	{
		assert(nom!=null);
		src=nom;
	}
	
	public boolean a_indice()
	{
		return indice!=null;
	}
	
	public boolean a_src()
	{
		return src!=null;
	}
	
	public String toString()
	{
		String res;
		
		if(a_src())
			res=src+".";
		else
			res="";
		if(a_indice())
			res+=nom+"["+indice+"]";
		else
			res+=nom;
		
		return res;
	}

	public void toXML(PrintStream out)
	{
		out.println("<expression_scalaire type=\"var\" >");
		if(a_src())
			out.println("<var nom=\""+nom+"\" src=\""+src+"\" >");
		else
			out.println("<var nom=\""+nom+"\" >");
		if(indice!=null)
			indice.toXML(out);
		out.println("</var>");
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expression_scalaire>");
	}

	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Var);
		CIExpr_Var e2=(CIExpr_Var)e;
		assert(nom!=null);
		assert(e2.nom!=null);
		assert(nom.equalsIgnoreCase(e2.nom));
		if(indice!=null)
		{
			assert(e2.indice!=null);
			indice.check_egal(e2.indice);
		}
		else
			assert(e2.indice==null);
		if(src==null)
			assert(e2.src==null);
		else
		{
			assert(e2.src!=null):"src="+src;
			assert(src.equalsIgnoreCase(e2.src));
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
	
	public String nom,src;
	public CIExpr_Scalaire indice;
	public CISource source;

}
