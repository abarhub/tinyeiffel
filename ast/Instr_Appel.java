package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Appel extends Instr implements Suite, ToXML
{
	public Instr_Appel(String nom,Vector param)
	{
		this.nom=nom;
		/*if(param.elementAt(0) instanceof Expr)
			System.out.println("Reussi");
		else
			System.out.println("Loupe");
		if(param.toArray() instanceof Object[])
			System.out.println("Reussi2");
		else
			System.out.println("Loupe2");*/
		this.parametre=new Expr[param.size()];
		param.copyInto(parametre);
		//for(i=0;i<param.size();i++)
		//	parametre[i]=(Expr)param.elementAt(i);
	}

	public Instr_Appel(Expr expr)
	{
		this.expr=expr;
	}

        public Position debut()
        {
          if(expr!=null)
            return expr.debut();
          return tid.debut();
        }

	public Expr parametre[];
	public String nom;
	public Expr expr;
	private Instr suivant;
        public Token tid,tpoint;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Appel);
		Instr_Appel ins=(Instr_Appel)instr;
		assert(nom.equals(ins.nom));
		if(expr==null)
			assert(ins.expr==null);
		else
			expr.check_egal(ins.expr);
		int i;
		if(parametre==null)
			assert(ins.parametre==null);
		else
		{
			assert(ins.parametre.length==parametre.length);
			for(i=0;i<parametre.length;i++)
			{
				parametre[i].check_egal(ins.parametre[i]);
			}
		}
		if(suivant==null)
			assert(ins.suivant==null);
		else
			suivant.check_egal(ins.suivant);
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instruction>");
		out.println("<appel nom=\""+nom+"\">");
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				out.println("<parametre_reel>");
				parametre[i].toXML(out);
				out.println("</parametre_reel>");
			}
		}
		Instr ins=suivant;
		while(ins!=null)
		{
			assert(ins instanceof Instr_Appel);
			Instr_Appel insa=(Instr_Appel)ins;
			out.println("<appel nom=\""+insa.nom+"\">");
			if(insa.parametre!=null)
			{
				for(int i=0;i<insa.parametre.length;i++)
				{
					out.println("<parametre_reel>");
					insa.parametre[i].toXML(out);
					out.println("</parametre_reel>");
				}
			}
			ins=insa.suivant;
		}
		if(suivant!=null)
		{
			ins=suivant;
			while(ins!=null)
			{
				out.println("</appel>");
				ins=((Suite)ins).getSuivant();
			}
		}
		out.println("</appel>");
		out.println("</instruction>");
	}

	/* (non-Javadoc)
	 * @see ast.Suite#getSuivant()
	 */
	public Instr getSuivant() {
		return suivant;
	}

	/* (non-Javadoc)
	 * @see ast.Suite#setSuivant(ast.Instr)
	 */
	public void setSuivant(Instr instr) {
		assert(instr!=null);
		suivant=instr;
	}

}