package tinyeiffel.ast;

import java.io.PrintStream;
import java.util.*;

public class Instr_Loop extends Instr implements ToXML
{
	public Instr_Loop(Expr expr,Vector from,Vector loop,
		Vector invariant,Expr variant)
	{
		this.expr=expr;
		this.from=new Instr[from.size()];
		from.copyInto(this.from);
		this.loop=new Instr[loop.size()];
		loop.copyInto(this.loop);
		this.invariant=new Assert[invariant.size()];
		invariant.copyInto(this.invariant);
		this.variant=variant;
	}

        public void set_token(Token tfrom,Token tuntil,Token tinvariant,
                              Token tvariant,Token tloop,Token tend)
        {
          this.tfrom=tfrom;
          this.tuntil=tuntil;
          this.tinvariant=tinvariant;
          this.tvariant=tvariant;
          this.tloop=tloop;
          this.tend=tend;
        }

        public Position debut()
        {
          return tfrom.debut();
        }

	public Expr expr;
	public Instr from[];
	public Instr loop[];
	public Assert invariant[];
	public Expr variant;
        public Token tfrom,tuntil,tinvariant,tvariant,tloop,tend;
	/* (non-Javadoc)
	 * @see ast.Instr#check_egal(ast.Instr)
	 */
	public void check_egal(Instr instr) {
		assert(instr!=null);
		assert(instr instanceof Instr_Loop);
		Instr_Loop ins=(Instr_Loop)instr;
		int i;
		assert(from.length==ins.from.length);
		for(i=0;i<from.length;i++)
		{
			from[i].check_egal(ins.from[i]);
		}
		/******/
		assert(loop.length==ins.loop.length);
		for(i=0;i<loop.length;i++)
		{
			loop[i].check_egal(ins.loop[i]);
		}
		/*****/
		assert(invariant.length==ins.invariant.length);
		for(i=0;i<invariant.length;i++)
		{
			invariant[i].check_egal(ins.invariant[i]);
		}
		expr.check_egal(ins.expr);
		if(variant==null)
			assert(ins.variant==null);
		else
			variant.check_egal(ins.variant);
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		int i;
		out.println("<instruction>");
		out.println("<loop>");
		if(from!=null)
		{
			for(i=0;i<from.length;i++)
				from[i].toXML(out);
		}
		expr.toXML(out);
		if(variant!=null)
		{
			out.println("<variant>");
			variant.toXML(out);
			out.println("</variant>");
		}	
		if(invariant!=null&&invariant.length>0)
		{
			out.println("<invariant>");
			for(i=0;i<invariant.length;i++)
			{
				invariant[i].toXML(out);
			}
			out.println("</invariant>");
		}
		if(loop!=null)
		{
			for(i=0;i<loop.length;i++)
				loop[i].toXML(out);
		}
		out.println("</loop>");
		out.println("</instruction>");
	}

}