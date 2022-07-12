package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public class Export implements ToXML,Traite //extends Instr
{
	public Export(Vector feature,Vector type)
	{
		liste_nom=new NomFeature[feature.size()];
		feature.copyInto(liste_nom);
		if(type!=null)
		{
			liste_type=new Type[type.size()];
			type.copyInto(liste_type);
		}
	}

        public Position debut()
        {
          assert(false);
          return null;
        }

	public void check_egal(Export e)
	{
		assert(e!=null);
		int i;
		assert(liste_nom.length==e.liste_nom.length);
		for(i=0;i<liste_nom.length;i++)
		{
			liste_nom[i].check_egal(e.liste_nom[i]);
		}
		/*****/
		assert(liste_type.length==e.liste_type.length);
		for(i=0;i<liste_type.length;i++)
		{
			liste_type[i].check_egal(e.liste_type[i]);
		}
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<export>");
		int i;
		if(liste_nom!=null)
		{
			for(i=0;i<liste_nom.length;i++)
			{
				liste_nom[i].toXML(out);
			}
		}
		if(liste_type!=null)
		{
			for(i=0;i<liste_type.length;i++)
			{
				liste_type[i].toXML(out);
			}
		}
		out.println("</export>");
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

	public NomFeature liste_nom[];
	public Type liste_type[];
}