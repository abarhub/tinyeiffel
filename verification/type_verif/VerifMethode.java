/*
 * Created on 5 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

import java.io.PrintStream;

import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifMethode extends Verif {

	public VerifMethode(VerifType nom_classe,NomFeature nom,
			VerifType param[],VerifType type_retour,Position pos)
	{
		assert(nom_classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.nom_classe=nom_classe;
		this.nom=nom;
		this.param=param;
		this.type_retour=type_retour;
		this.pos=pos;
	}
	
	/*public boolean est_pret()
	{
		
	}*/
	
	public void toXML(PrintStream out)
	{
		int i;
		assert(out!=null);
		out.println("<methode>");
		nom_classe.toXML(out);
		nom.toXML2(out);
		out.println("<param>");
		if(param!=null&&param.length>0)
		{
			for(i=0;i<param.length;i++)
			{
				param[i].toXML(out);
			}
		}
		out.println("</param>");
		if(type_retour!=null)
		{
			type_retour.toXML(out);
		}
		pos.toXML(out);
		out.println("</methode>");
	}
	
	public VerifType nom_classe,param[],type_retour;
	public NomFeature nom;
	public Position pos;
}
