package tinyeiffel.ast;

import java.io.*;

public class Position implements Traite
{
	public Position(int x,int y)
	{
		this.x=x;
		this.y=y;
	}

        public Position(String fichier,int x,int y)
        {
                 this.x=x;
                 this.y=y;
                 this.fichier=fichier;
        }

        public String toString()
        {
          return "("+x+","+y+","+fichier+")";
        }

	public boolean avant(Position p)
	{
		assert(p!=null);
		return ((x<p.x)||((x==p.x)&&(y<p.y)));
	}

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	protected boolean traite=false;

	public String NomClasse()
	{
		int debut,fin;
		debut=fichier.lastIndexOf('\\');
		fin=fichier.lastIndexOf('.');
		if(debut!=-1&&fin!=-1&&debut<fin)
		{
			return fichier.substring(debut+1,fin);
		}
		else
		{
			return fichier;
		}
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<position nom_fichier=\""+fichier+
				"\" x=\""+x+"\" y=\""+y+"\" />");
	}
	
	public String fichier,texte;
	/**
	 * x est la ligne (>=1)
	 * y est la colonne (>=1)
	 */
	public int x,y;

}