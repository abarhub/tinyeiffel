/*
 * Created on 26 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.util.*;
import java.io.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIListe_Instr {

	/**
	 * 
	 */
	public CIListe_Instr() {
		liste=new Vector();
	}

	public void ajoute(CIInstruction v)
	{
		assert(v!=null);
		liste.addElement(v);
	}

	public void ajoute(Vector v)
	{
		int i;
		assert(v!=null);
		for(i=0;i<v.size();i++)
		{
			assert(v.elementAt(i) instanceof CIInstruction);
		}
		liste.addAll(v);
	}

	public CIInstruction elt(int i)
	{
		assert(i>=0);
		assert(i<size());
		return (CIInstruction)liste.elementAt(i);
	}

	public int size()
	{
		return liste.size();
	}

	public void toXML(PrintStream out)
	{
		int i;
		assert(out!=null);
		//out.println("<instruction>");
		for(i=0;i<size();i++)
		{
			elt(i).toXML(out);
		}
		//out.println("</instruction>");
	}

	public void check_egal(CIListe_Instr liste_var)
	{
		assert(liste_var!=null);
		if(size()==0)
			assert(liste_var==null||liste_var.size()==0);
		else
		{
			assert(liste_var!=null);
			assert(liste_var.size()==size());
			for(int i=0;i<size();i++)
			{
				elt(i).check_egal(liste_var.elt(i));
			}
		}
	}

	public int getLabel(String nom)
	{
		if(liste!=null)
		{
			for(int i=0;i<liste.size();i++)
			{
				if(liste.get(i) instanceof CIInstruction_Label)
				{
					CIInstruction_Label ins=(CIInstruction_Label) liste.get(i);
					if(ins.nom.equalsIgnoreCase(nom))
						return i;
				}
			}
		}
		return -1;
	}
	
	protected Vector liste;

}
