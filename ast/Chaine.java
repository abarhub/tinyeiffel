package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public class Chaine implements ToXML,Traite
{
	public Chaine(Vector liste,Token oper)
	{
          assert(oper!=null);
		this.liste_chaine=new String[liste.size()];
		liste.copyInto(this.liste_chaine);
                this.oper=oper;
	}

	public boolean equals(Object o)
	{
		if(o instanceof Chaine)
		{
			return chaine_unique()==((Chaine)o).chaine_unique();
		}
		return false;
	}

	public String chaine_unique()
	{
		String res="";

		if(liste_chaine.length>0)
		{
			int i;
			res=liste_chaine[0];
			for(i=1;i<liste_chaine.length;i++)
			{
				res=res.substring(1,res.length()-1);
				res+=liste_chaine[i].substring(2);
			}
		}

		return res;
	}

	public String toString()
	{
		return chaine_unique();
	}

	public void check_egal(Chaine c)
	{
		assert(c!=null);
		for(int i=0;i<liste_chaine.length;i++)
		{
			assert(liste_chaine[i].equals(c.liste_chaine[i])):
				"Difference "+i+" : \""+liste_chaine[i]+"\"!=\""+
			c.liste_chaine[i]+"\"";		
		}
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<chaine>");
		for(int i=0;i<liste_chaine.length;i++)
		{
			out.print("<chaine1>");
			out.print(liste_chaine[i]);			
			out.println("</chaine1>");
		}
		out.println("</chaine>");
	}

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}


	// enlève des " avant et apres
	public String simplifie_chaine() {
		String s;
		s=chaine_unique();
		if(s!=null)
		{
			if(s.startsWith("\""))
				s=s.substring(1);
			if(s.endsWith("\""))
				s=s.substring(0,s.length()-1);
		}
		return s;
	}
	
	protected boolean traite=false;

	public String liste_chaine[];
        public final Token oper;
}