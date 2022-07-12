/*
 * Created on 8 févr. 2004
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
public class CIExpr_Appel extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Appel(CIExpr_Var cible,CINom_Attribut nom,
			CIExpr_Scalaire param[],CISource source) {
		assert(nom!=null);
		this.nom=nom;
		this.cible=cible;
		this.parametre=param;
		this.source=source;
	}

	public String toString()
	{
		String res="Call ";
		if(cible!=null)
			res+=cible+".";
		res+=nom+"(";
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				if(i>0)
					res+=",";
				res+=parametre[i];
			}
		}
		res+=")";
		return res;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<expr_appel>");
		if(cible!=null)
			cible.toXML(out);
		nom.toXML(out);
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				parametre[i].toXML(out);
			}
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expr_appel>");
	}
	
	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Appel);
		CIExpr_Appel e2=(CIExpr_Appel)e;
		assert(nom!=null);
		assert(e2.nom!=null);
		nom.check_egal(e2.nom);
		if(parametre==null||parametre.length==0)
			assert(e2.parametre==null||e2.parametre.length==0);
		else
		{
			assert(e2.parametre!=null);
			assert(e2.parametre.length==parametre.length);
			for(int i=0;i<parametre.length;i++)
			{
				parametre[i].check_egal(e2.parametre[i]);
			}
		}
		if(cible==null)
			assert(e2.cible==null);
		else
		{
			assert(e2.cible!=null);
			cible.check_egal(e2.cible);
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
	
	public final CIExpr_Var cible;
	public final CIExpr_Scalaire parametre[];
	public final CINom_Attribut nom;
	public CISource source;

}
