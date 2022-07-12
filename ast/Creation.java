package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public class Creation implements ToXML,Traite
{
  public Creation(Vector type, Vector nom_fonction) {
    if (type != null) {
      liste_type = new Type[type.size()];
      type.copyInto(liste_type);
    }
    this.nom_fonction = new NomFeature[nom_fonction.size()];
    nom_fonction.copyInto(this.nom_fonction);
  }
        public void set_token(Token tcreation)
        {
             this.tcreation=tcreation;
        }

        public Position debut()
        {
          return tcreation.debut();
        }

	public void check_egal(Creation c)
	{
		assert(c!=null);
		assert(c instanceof Creation);
		int	i;
		/*********/
		if(liste_type==null)
		{
			assert(c.liste_type==null);
		}
		else
		{
			assert(liste_type.length==c.liste_type.length);
			for(i=0;i<liste_type.length;i++)
			{
				liste_type[i].check_egal(c.liste_type[i]);		
			}
		}
		/*********/
		assert(nom_fonction.length==c.nom_fonction.length);
		for(i=0;i<nom_fonction.length;i++)
		{
			nom_fonction[i].check_egal(c.nom_fonction[i]);		
		}
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		if(liste_type!=null&&liste_type.length==0)
		{
			out.println("<creation export_none=\"true\">");
		}
		else
		{
			out.println("<creation>");
		}
		int i;
		if(liste_type!=null)
		{
			for(i=0;i<liste_type.length;i++)
				liste_type[i].toXML(out);
		}
		if(nom_fonction!=null)
		{
			for(i=0;i<nom_fonction.length;i++)
				nom_fonction[i].toXML(out);
		}
		out.println("</creation>");
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

	public Type liste_type[];
        public NomFeature nom_fonction[];
	public Position debut,fin;
        public Token tcreation;
}