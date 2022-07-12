package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Creation extends Instr implements ToXML
{
	public Instr_Creation(Type type,String nom,String nom2,Vector param)
	{
		this.type=type;
		this.nom=nom;
		this.nom2=nom2;
		this.parametre=new Expr[param.size()];
		param.copyInto(parametre);
	}

        public Position debut()
        {
          return texcl1.debut();
        }

	public Expr parametre[];
	public String nom;
	public String nom2;
	public Type type;
        public Token texcl1,texcl2,tid,tid2,tpoint;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Creation);
		Instr_Creation ins=(Instr_Creation)instr;
		int i;
		assert(parametre.length==ins.parametre.length);
		for(i=0;i<parametre.length;i++)
		{
			parametre[i].check_egal(ins.parametre[i]);
		}
		assert(nom.equals(ins.nom));
		if(nom2==null)
			assert(ins.nom2==null);
		else
			assert(nom2.equals(ins.nom2));
		if(type==null)
			assert(ins.type==null);
		else
			type.check_egal(ins.type);
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		out.println("<instruction>");
		if(nom2!=null)
		{
			out.println("<creation2 nom=\""+nom+"\">");
		}
		else
		{
			out.println("<creation2>");
		}
		if(type!=null)
			type.toXML(out);
		out.println("<appel nom=\""+((nom2!=null)?nom2:nom)+"\">");
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				out.println("<parametre_reel>");
				parametre[i].toXML(out);
				out.println("</parametre_reel>");
			}
		}
		out.println("</appel>");
		out.println("</creation2>");
		out.println("</instruction>");
	}

}