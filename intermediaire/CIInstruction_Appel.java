/*
 * Created on 5 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIInstruction_Appel extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Appel(CIExpr_Var cible,CINom_Attribut nom,
			CIExpr_Scalaire param[],CISource source) {
		assert(nom!=null);
		this.cible=cible;
		this.nom=nom;
		this.param=param;
		this.source=source;
	}

	public String toString()
	{
		String res;
		res="Call ";
		if(cible!=null)
			res+=cible+".";
		res+=nom+"(";
		if(param!=null)
		{
			for(int i=0;i<param.length;i++)
			{
				if(i>0)
					res+=",";
				res+=param[i];
			}
		}
		res+=")";
		return res;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<instr_appel>");
		if(cible!=null)
			cible.toXML(out);
		nom.toXML(out);
		if(param!=null)
		{
			for(int i=0;i<param.length;i++)
			{
				param[i].toXML(out);
			}
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</instr_appel>");
	}
	
	public void check_egal(CIInstruction instr)
	{
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Appel);
		CIInstruction_Appel ins=(CIInstruction_Appel)instr;
		assert(nom!=null);
		assert(ins.nom!=null);
		nom.check_egal(ins.nom);
		if(param==null||param.length==0)
			assert(ins.param==null||ins.param.length==0);
		else
		{
			assert(ins.param!=null);
			assert(ins.param.length==param.length):"len("+ins.param.length+")!=len("+param.length+")"+
					";nom="+ins.nom+";";
			for(int i=0;i<param.length;i++)
				param[i].check_egal(ins.param[i]);
		}
		if(cible==null)
			assert(ins.cible==null);
		else
		{
			assert(ins.cible!=null);
			cible.check_egal(ins.cible);
		}
		if(source!=null)
		{
			source.check_egal(ins.source);
		}
		else
		{
			assert(ins.source==null);
		}
	}
	
	public CIExpr_Var cible;
	public CIExpr_Scalaire param[];
	public CINom_Attribut nom;
	public CISource source;

}
