package tinyeiffel.ast;

import java.io.*;

public abstract class Instr implements ToXML,Traite //extends ASTEiffel
{
	/*public Instr(int op)
	{
		this.op=op;
	}

	public Expr expr,expr2;
	public int op;
	public static final int Affect=1,Tent_Affect=2,Loop=3,If=4,Creation=5,
		Retry=6,Inspect=7,Check=8,Appel=9,Else=10,ElseIf=11;
	/*public String str;
	public int entier;
	public double reel;
	public String caracter;*/
	/*public Vector parametre;
	public String nom;
	//public Vector tableau;
	public Vector liste_expr;
	public Vector when;
	public Vector liste_instr;
	public Instr suivant;
	public Vector from;
	public Vector loop;
	public String nom2;
	public Type type;*/
	
	public abstract void check_egal(Instr instr);
	public abstract void toXML(PrintStream out);
	
	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	protected boolean traite=false;

        public abstract Position debut();
        //public abstract Position fin();
	//public Instr suivant;
}