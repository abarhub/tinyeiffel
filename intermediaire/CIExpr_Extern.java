package tinyeiffel.intermediaire;

import java.io.PrintStream;

public class CIExpr_Extern extends CIExpr {

	/**
	 * 
	 */
	public CIExpr_Extern(CIType type_retour,String langage,String options,
			CIExpr_Var cible,CINom_Attribut nom,
			CIExpr_Scalaire param[],CISource source) {
		assert(nom!=null);
		assert(langage!=null);
		assert(type_retour!=null);
		this.langage=langage;
		this.opt=options;
		this.nom=nom;
		this.cible=cible;
		this.parametre=param;
		this.source=source;
		this.type_retour=type_retour;
	}

	public String toString()
	{
		String res="Call extern  lang(\""+langage+"\") ";
		if(opt!=null&&opt.length()>0)
			res+="option=\""+opt+"\" ";
		if(cible!=null)
			res+=cible+".";
		res+=nom+"(";
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				if(i>0)
					res+=",";
				res+=parametre[i];
			}
		}
		res+=")";
		return res;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<expr_extern lang_extern=\""+langage+"\""+
			((opt!=null)?" opt_extern=\""+opt+"\"":"")+">");
		if(cible!=null)
			cible.toXML(out);
		nom.toXML(out);
		if(parametre!=null)
		{
			for(int i=0;i<parametre.length;i++)
			{
				parametre[i].toXML(out);
			}
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</expr_extern>");
	}
	
	public void check_egal(CIExpr e)
	{
		assert(e!=null);
		assert(e instanceof CIExpr_Extern);
		CIExpr_Extern e2=(CIExpr_Extern)e;
		assert(nom!=null);
		assert(e2.nom!=null);
		nom.check_egal(e2.nom);
		assert(langage.equals(e2.langage));
		assert(type_retour!=null);
		type_retour.check_egal(e2.type_retour);
		if(opt==null||opt.length()==0)
		{
			assert(e2.opt==null||e2.opt.length()==0);
		}
		else
		{
			assert(e2.opt!=null);
			assert(e2.opt.equals(opt));
		}
		if(parametre==null||parametre.length==0)
			assert(e2.parametre==null||e2.parametre.length==0);
		else
		{
			assert(e2.parametre!=null);
			assert(e2.parametre.length==parametre.length);
			for(int i=0;i<parametre.length;i++)
			{
				parametre[i].check_egal(e2.parametre[i]);
			}
		}
		if(cible==null)
			assert(e2.cible==null);
		else
		{
			assert(e2.cible!=null);
			cible.check_egal(e2.cible);
		}
		if(source!=null)
		{
			source.check_egal(e2.source);
		}
		else
		{
			assert(e2.source==null);
		}
	}
	
	public final CIExpr_Var cible;
	public final CIExpr_Scalaire parametre[];
	public final CINom_Attribut nom;
	public CISource source;
	public String langage;
	public String opt;
	public CIType type_retour;

}
