package tinyeiffel.intermediaire;

import java.io.PrintStream;

public class CIInstruction_Extern extends CIInstruction {

	/**
	 * 
	 */
	public CIInstruction_Extern(String langage,String options,
			CIExpr_Var cible,CINom_Attribut nom,
			CIExpr_Scalaire param[],CISource source) {
		assert(nom!=null);
		assert(langage!=null);
		this.langage=langage;
		this.opt=options;
		this.cible=cible;
		this.nom=nom;
		this.param=param;
		this.source=source;
	}

	public String toString()
	{
		String res;
		res="Call extern  lang(\""+langage+"\") ";
		if(opt!=null&&opt.length()>0)
			res+="option=\""+opt+"\" ";
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
		out.println("<instr_extern lang_extern=\""+langage+"\""+
			((opt!=null)?" opt_extern=\""+opt+"\"":"")+">");
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
		out.println("</instr_extern>");
	}
	
	public void check_egal(CIInstruction instr)
	{
		assert(instr!=null);
		assert(instr instanceof CIInstruction_Extern);
		CIInstruction_Extern ins=(CIInstruction_Extern)instr;
		assert(nom!=null);
		assert(ins.nom!=null);
		assert(langage.equals(ins.langage));
		if(opt==null||opt.length()==0)
		{
			assert(ins.opt==null||ins.opt.length()==0);
		}
		else
		{
			assert(ins.opt!=null);
			assert(ins.opt.equals(opt));
		}
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
	public String langage;
	public String opt;

}
