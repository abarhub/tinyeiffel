package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Else extends Instr implements Suite, ToXML
{
	public Instr_Else(Vector instr)
	{
		liste_instr=new Instr[instr.size()];
		instr.copyInto(liste_instr);
	}

        public Position debut()
        {
          return telse.debut();
        }

	public Instr liste_instr[];
        public Token telse;
        private Instr suivant;
        
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Else);
		Instr_Else ins=(Instr_Else)instr;
		int i;
		assert(liste_instr.length==ins.liste_instr.length);
		for(i=0;i<liste_instr.length;i++)
		{
			liste_instr[i].check_egal(ins.liste_instr[i]);
		}
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.println("<else>");
		int i;
		if(liste_instr!=null)
		{
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].toXML(out);
			}
		}
		out.println("</else>");
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