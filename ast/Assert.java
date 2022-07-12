package tinyeiffel.ast;

import java.io.*;

public class Assert implements ToXML,Traite //extends ASTEiffel
{
	public Assert(String nom,Expr expr)
	{
		this.nom=nom;
		this.expr=expr;
	}

	public Assert(Expr expr)
	{
		this.nom="";
		this.expr=expr;
	}

	public void check_egal(Assert a)
	{
		assert(a!=null);
		assert(a instanceof Assert);
		assert(nom.equals(a.nom)):nom+"!="+a.nom;
		expr.check_egal(a.expr);	
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		if(nom==null||nom.equals(""))
			out.println("<assert>");
		else
			out.println("<assert tag=\""+nom+"\">");
		expr.toXML(out);
		out.println("</assert>");
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

	public String nom;
	public Expr expr;
}