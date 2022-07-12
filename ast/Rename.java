package tinyeiffel.ast;

import java.io.*;

public class Rename implements ToXML,Traite //extends Instr
{
	public Rename(NomFeature nom_src,NomFeature nom_dest)
	{
		this.nom_src=nom_src;
		this.nom_dest=nom_dest;
	}

        public Position debut()
        {
          assert(false);
          return null;
        }

	public void check_egal(Rename r)
	{
		assert(r!=null);
		nom_src.check_egal(r.nom_src);
		nom_dest.check_egal(r.nom_dest);
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<rename>");
		nom_src.toXML(out);
		nom_dest.toXML(out);
		out.println("</rename>");
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

	public NomFeature nom_src,nom_dest;
}