/*
 * Created on 25 févr. 2004
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
public class CIListe_Var {

	/**
	 * 
	 */
	public CIListe_Var() {
		liste=new Vector();
	}

	public void ajoute(CIDeclare_Var v)
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
			assert(v.elementAt(i) instanceof CIDeclare_Var);
		}
		liste.addAll(v);
	}

	public CIDeclare_Var ajoute(String nom,CIType type)
	{
		CIDeclare_Var v;
		assert(nom!=null);
		assert(type!=null);
		
		v=new CIDeclare_Var(nom,type,null);
		ajoute(v);
		return v;
	}

	public CIDeclare_Var donne_var(String nom)
	{
		int i;
		CIDeclare_Var d;
		for(i=0;i<liste.size();i++)
		{
			d=(CIDeclare_Var)liste.elementAt(i);
			if(d.nom.equalsIgnoreCase(nom))
				return d;
		}
		return null;
	}

	public CIDeclare_Var elt(int i)
	{
		assert(i>=0);
		assert(i<size());
		return (CIDeclare_Var)liste.elementAt(i);
	}

	public int size()
	{
		return liste.size();
	}

	public void toXML(PrintStream out)
	{
		int i;
		assert(out!=null);
		for(i=0;i<size();i++)
		{
			elt(i).toXML(out);
		}
	}

	public void check_egal(CIListe_Var liste_var)
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

	protected Vector liste;

}
