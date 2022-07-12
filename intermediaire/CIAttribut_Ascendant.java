/*
 * Created on 11 févr. 2004
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
public class CIAttribut_Ascendant {

	/**
	 * 
	 */
	public CIAttribut_Ascendant(int no,CINom_Attribut nom,CISource source)
	{
		assert(no>=0);
		this.no=no;
		this.nom=nom;
		this.source=source;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.print("<ascendant no=\""+no+"\"");
		if(nom!=null)
		{
			out.println(" >");
			nom.toXML(out);
			if(source!=null)
			{
				source.toXML(out);
			}
			out.println("</ascendant>");
		}
		else
		{
			if(source!=null)
			{
				out.println(" >");
				source.toXML(out);
				out.println("</ascendant>");
			}
			else
			{
				out.println(" />");
			}
		}
	}

	public void check_egal(CIAttribut_Ascendant a)
	{
		assert(a!=null);
		assert(no>=0);
		assert(no==a.no);
		if(nom!=null)
		{
			assert(a.nom!=null);
			nom.check_egal(a.nom);
		}
		else
			assert(a.nom==null);
		if(source!=null)
		{
			source.check_egal(a.source);
		}
		else
		{
			assert(a.source==null);
		}
	}
	
	public CIAttribut donne_attribut(CINom_Attribut nom,CIClasse cl,
			CIProgramme p)
	{
		assert(nom!=null);
		assert(cl!=null);
		assert(p!=null);
		CIAttribut attr;
		CITypeSimple t1,t2;
		CIClasse cl2;
		CINom_Attribut n;
		int i;
		assert(cl.heritage!=null);
		assert(cl.heritage.length>0):"classe="+cl.nom.nom;
		assert(cl.heritage.length>no);
		t1=cl.heritage[no];
		cl2=p.donne_classe(t1);
		assert(cl2!=null);
		n=nom;
		attr=cl2.donne_attribut(n);
		assert(attr!=null);
		if(attr.est_descendant())
		{
			assert(attr.attribut_ascendant!=null);
			assert(attr.attribut_ascendant.length==1);
			attr=attr.attribut_ascendant[0].donne_attribut(n,cl2,p);
			assert(attr!=null);
		}
		return attr;
	}
	
	public final int no;
	public final CINom_Attribut nom;
	public CISource source;

}
