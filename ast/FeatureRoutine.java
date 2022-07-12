package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class FeatureRoutine extends Feature implements ToXML
{
	public FeatureRoutine(boolean once,Vector liste_instr,Vector local)
	{
		this.once=once;
		if(liste_instr!=null)
		{
			this.liste_instr=new Instr[liste_instr.size()];
			liste_instr.copyInto(this.liste_instr);
		}
		if(local!=null)
		{
			this.local=new DeclareVar[local.size()];
			local.copyInto(this.local);
		}
	}
	
	public boolean est_routine()
	{
		return true;
	}

	public boolean once;
	public Instr liste_instr[];
	public DeclareVar local[];
	/* (non-Javadoc)
	 * @see ast.Feature#check_egal(ast.Feature)
	 */
	public void check_egal(Feature f) {
		super.check_egal(f);
		assert(f instanceof FeatureRoutine);
		FeatureRoutine f2=(FeatureRoutine)f;
		assert(once==f2.once):once+"!="+f2.once;
		int i;
		/*****/
		assert(liste_instr.length==f2.liste_instr.length):
				"difference:"+liste_instr.length+"!="+
				f2.liste_instr.length;
		for(i=0;i<liste_instr.length;i++)
		{
			liste_instr[i].check_egal(f2.liste_instr[i]);		
		}
		/*****/
		assert(local.length==f2.local.length);
		for(i=0;i<local.length;i++)
		{
			local[i].check_egal(f2.local[i]);		
		}
	}

	/* (non-Javadoc)
	 * @see ast.Feature#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		toXMLCorpsDebut(out);
		out.println("<code"+((once)?" once=\"yes\"":"")+">");
		int i;
		if(local!=null)
		{
			for(i=0;i<local.length;i++)
			{
				DeclareVar d=local[i];
				out.println("<local nom=\""+d.nom+"\">");
				d.type.toXML(out);
				out.println("</local>");
			}
		}
		if(liste_instr!=null)
		{
			for(i=0;i<liste_instr.length;i++)
			{
				liste_instr[i].toXML(out);
			}
		}
		out.println("</code>");
		toXMLCorpsFin(out);
	}

}