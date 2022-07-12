package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Debug extends Instr implements ToXML
{
	public Instr_Debug(Vector chaine,Vector instr)
	{
		if(chaine!=null)
		{
			//System.out.println("chaine="+chaine);
			this.chaine=new Chaine[chaine.size()];
			chaine.copyInto(this.chaine);
		}
		if(instr!=null)
		{
			this.instr=new Instr[instr.size()];
			instr.copyInto(this.instr);
		}
	}

        public Position debut()
        {
          return tdebug.debut();
        }

	public Chaine chaine[];
	public Instr instr[];
        public Token tdebug,tend;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Debug);
		Instr_Debug ins=(Instr_Debug)instr;
		int i;
		assert(chaine.length==ins.chaine.length):
				"Taille differente:"+chaine.length+"/="+
				ins.chaine.length;
		for(i=0;i<chaine.length;i++)
		{
			chaine[i].check_egal(ins.chaine[i]);
		}
		assert(this.instr.length==ins.instr.length);
		for(i=0;i<this.instr.length;i++)
		{
			this.instr[i].check_egal(ins.instr[i]);
		}

	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<instruction>");
		out.println("<debug>");
		int i;
		if(chaine!=null)
		{
			for(i=0;i<chaine.length;i++)
				chaine[i].toXML(out);
		}
		if(instr!=null)
		{
			for(i=0;i<instr.length;i++)
				instr[i].toXML(out);
		}
		out.println("</debug>");
		out.println("</instruction>");
	}

}