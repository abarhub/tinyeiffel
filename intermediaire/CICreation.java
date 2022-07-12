/*
 * Created on 3 avr. 2004
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
public class CICreation {

	/**
	 * 
	 */
	public CICreation(CIType type[],CINom_Attribut nom[],
			CISource source)
	{
		assert(nom!=null);
		assert(nom.length>0);
		this.type=type;
		this.nom=nom;
		this.source=source;
	}

	public void toXML(PrintStream out)
	{
		int i;
		assert(out!=null);
		out.println("<creation>");
		if(type!=null)
		{
			for(i=0;i<type.length;i++)
				type[i].toXML(out);
		}
		for(i=0;i<nom.length;i++)
		{
			nom[i].toXML(out);
		}
		//for(i=0;i<liste.size();i++)
		//	liste.elt(i).toXML(out);
		/*if(liste!=null)
			liste.toXML(out);
		out.println("<instruction>");
		if(liste_instr!=null)
			liste_instr.toXML(out);
		//for(i=0;i<liste_instr.length;i++)
		//	liste_instr[i].toXML(out);
		out.println("</instruction>");*/
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</creation>");
	}

	public void check_egal(CICreation c)
	{
		int i;
		assert(c!=null);
		assert(nom!=null);
		assert(nom.length>0);
		assert(c.nom!=null);
		assert(c.nom.length>0);
		//type.check_egal(c.type);
		if(type==null||type.length==0)
			assert(c.type==null||c.type.length==0);
		else
		{
			assert(c.type!=null);
			assert(c.type.length==type.length);
			for(i=0;i<type.length;i++)
			{
				type[i].check_egal(c.type[i]);
			}
		}
		assert(nom.length==c.nom.length);
		for(i=0;i<nom.length;i++)
		{
			nom[i].check_egal(c.nom[i]);
		}
		/*if(liste==null||liste.size()==0)
			assert(a.liste==null||a.liste.size()==0):a.liste.size()+":"+a.liste.elt(0);
		else
		{
			/*assert(a.liste!=null);
			assert(a.liste.size()==liste.size());
			for(i=0;i<liste.size();i++)
			{
				liste.elt(i).check_egal(a.liste.elt(i));
			}*/
/*			liste.check_egal(a.liste);
		}
		if(liste_instr==null||liste_instr.size()==0)
			assert(a.liste_instr==null||a.liste_instr.size()==0);
		else
		{
			/*assert(a.liste_instr!=null);
			assert(a.liste_instr.length==liste_instr.length);
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].check_egal(a.liste_instr[i]);
			}*/
//			liste_instr.check_egal(a.liste_instr);
//		}
		if(source!=null)
		{
			source.check_egal(c.source);
		}
		else
		{
			assert(c.source==null);
		}
	}
	
	public String toString()
	{
		int i;
		String res="";
		if(nom!=null)
		{
			for(i=0;i<nom.length;i++)
			{
				if(i>0)
					res+=",";
				res+=nom[i].toString();
			}
		}
		return res;
	}
	

	public String toString(int no)
	{
		int i;
		String res="";
		if(nom!=null)
		{
			if(no>=0&&no<nom.length)
			{
				res=nom[no].toString();
			}
			else
			{
				res=toString();
			}
		}
		return res;
	}
	
	public CIType type[];
	public CINom_Attribut nom[];
	public CISource source;

}
