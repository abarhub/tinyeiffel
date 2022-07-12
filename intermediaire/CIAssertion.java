/*
 * Created on 25 févr. 2004
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
public class CIAssertion {

	/**
	 * 
	 */
	public CIAssertion(CISource source) {
		liste=new CIListe_Var();
		this.source=source;
	}

	public void affiche(PrintStream out)
	{
		int i;
		assert(out!=null);
	}

	public void toXML(PrintStream out,String nom)
	{
		int i;
		assert(out!=null);
		assert(nom!=null);
		out.println("<"+nom+">");
		//for(i=0;i<liste.size();i++)
		//	liste.elt(i).toXML(out);
		if(liste!=null)
			liste.toXML(out);
		out.println("<instruction>");
		if(liste_instr!=null)
			liste_instr.toXML(out);
		//for(i=0;i<liste_instr.length;i++)
		//	liste_instr[i].toXML(out);
		out.println("</instruction>");
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</"+nom+">");
	}

	public void check_egal(CIAssertion a)
	{
		int i;
		assert(a!=null);
		if(liste==null||liste.size()==0)
			assert(a.liste==null||a.liste.size()==0):a.liste.size()+":"+a.liste.elt(0);
		else
		{
			/*assert(a.liste!=null);
			assert(a.liste.size()==liste.size());
			for(i=0;i<liste.size();i++)
			{
				liste.elt(i).check_egal(a.liste.elt(i));
			}*/
			liste.check_egal(a.liste);
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
			liste_instr.check_egal(a.liste_instr);
		}
		if(source!=null)
		{
			source.check_egal(a.source);
		}
		else
		{
			assert(a.source==null);
		}
	}

	public final CIListe_Var liste;
	public CIListe_Instr liste_instr;
	public CISource source;

}
