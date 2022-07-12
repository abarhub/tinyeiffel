package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public abstract class Expr implements ToXML,Traite//extends ASTEiffel
{
	/*public Expr(int op)
	{
		if(op==0)
		{
			System.out.println("Erreur1");
			System.exit(1);
		}
		this.op=op;
	}

	/*public Expr(int op,Expr e1,Expr e2)
	{
		if(op==0)
		{
			System.out.println("Erreur2");
			System.exit(1);
		}
		this.op=op;
		this.expr1=e1;
		this.expr2=e2;
	}*/

	/*public Expr(int op,Expr e1)
	{
		if(op==0)
		{
			System.out.println("Erreur3");
			try
			{
				throw new Exception("fatal");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			//Compiler_Eiffel.logging.fatalMsg("Erreur3");
			System.exit(1);
		}
		this.op=op;
		this.expr1=e1;
	}

	public Expr(String nom,Vector param)
	{
		this.op=Appel;
		this.parametre=param;
		this.nom=nom;
	}

	public Expr(String nom)
	{
		this.op=Var;
		this.nom=nom;
	}

	public Expr(Chaine str)
	{
	  //assert(false);
		op=Chaine;
		this.str2=str;
                assert(str.oper!=null);
                this.oper=str.oper;
		assert(this.oper!=null);
	}

        public Expr(int i,Token t)
        {
          this.op=Expr.Entier;
          this.oper=t;
          this.entier=i;
        }*/

	/*public String donne_pos()
	{
		if(oper==null)
			return "()";
		else
			return "("+oper.x+","+oper.y+")";
	}*/

	public abstract void check_egal(Expr e);

	public NomFeature donne_nom_feature()
	{
		NomFeature n=null;
		Vector v;

		switch(op)
		{
			case Expr.Plus:
				n=new NomFeature("\"+\"");
				n.infix=true;
				break;
			case Expr.Moins:
				n=new NomFeature("\"-\"");
				n.infix=true;
				break;
			case Expr.Fois:
				n=new NomFeature("\"*\"");
				n.infix=true;
				break;
			case Expr.Div:
				n=new NomFeature("\"/\"");
				n.infix=true;
				break;
			case Expr.Mod:
				n=new NomFeature("\"\\\\\"");
				n.infix=true;
				break;
			case Expr.Not:
				n=new NomFeature("\"not\"");
				n.prefix=true;
				break;
			case Expr.PlusU:
				n=new NomFeature("\"+\"");
				n.prefix=true;
				break;
			case Expr.MoinsU:
				n=new NomFeature("\"-\"");
				n.prefix=true;
				break;
			case Expr.Puiss:
				n=new NomFeature("\"^\"");
				n.infix=true;
				break;
			case Expr.Xor:
				n=new NomFeature("\"xor\"");
				n.infix=true;
				break;
			case Expr.Or:
				n=new NomFeature("\"or\"");
				n.infix=true;
				break;
			case Expr.And:
				n=new NomFeature("\"and\"");
				n.infix=true;
				break;
			case Expr.And_Then:
				n=new NomFeature("\"and then\"");
				n.infix=true;
				break;
			case Expr.Or_Else:
				n=new NomFeature("\"or else\"");
				n.infix=true;
				break;
			case Expr.Implies:
				n=new NomFeature("\"implies\"");
				n.infix=true;
				break;
			case Expr.Egal:
				n=new NomFeature("\"=\"");
				n.infix=true;
				break;
			case Expr.Diff:
				n=new NomFeature("\"/=\"");
				n.infix=true;
				break;
			case Expr.Infs:
				n=new NomFeature("\"<\"");
				n.infix=true;
				break;
			case Expr.Inf:
				n=new NomFeature("\"<=\"");
				n.infix=true;
				break;
			case Expr.Sup:
				n=new NomFeature("\">=\"");
				n.infix=true;
				break;
			case Expr.Sups:
				n=new NomFeature("\">\"");
				n.infix=true;
				break;
			case Expr.Div_entier:
				n=new NomFeature("\"//\"");
				n.infix=true;
				break;
			case Expr.Point:
				n=new NomFeature("\".\"");
				n.infix=true;
				break;
			case Expr.Old:
				n=new NomFeature("old");
				n.prefix=true;
				break;
			case Expr.Dollard:
				n=new NomFeature("$");
				n.prefix=true;
				break;
			default:
				assert(false):"op="+op;
		}
		return n;
	}

        public abstract Position debut();
        /*{
	  //if(oper==null)
	    //return null;
          //assert(oper!=null);
          switch(op)
          {
            case Plus:case Moins:
            case Fois:case Div:case Div_entier:case Mod:
            case Point:case Puiss:case Xor:case Or:case And:case And_Then:case Or_Else:case Implies:
            case Egal:case Diff:case Infs:case Inf:case Sup:case Sups:
	      if(expr1==null)
		return null;//new Position(-1,-1);
	      assert(expr1!=null);
              return expr1.debut();
            /**/
/*            case Chaine:
	      if(oper==null)
		return str2.oper.debut();
	      else
		return oper.debut();
	    /*case Char:
	      if(oper==null)
		return str2.oper.debut();
	      else
		return oper.debut();*/
/*	    case Entier:
            case Reel:case Char:case Vrai:case Faux:
	      if(oper==null)
	      {
                System.out.println("op="+op);
		//assert(false);
                return new Position(-1,-1);
	      }
              return oper.debut();
            /**/
/*            case Old:case Not:case PlusU:case MoinsU:
	      if(oper==null)
		new Position(-1,-1);
              return oper.debut();
            case Appel:
            case Var:
	      if(oper==null)
		return new Position(-1,-1);
              return oper.debut();
            case Tableau:
              assert(false);
              return null;
            default:
              return null;
          }
        }*/

//public Type verifie(Context context)

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

	//public Expr expr1/*,expr2*/;
	public int op;
	public static final int Plus=1,Moins=2,Fois=3,Div=4,Div_entier=5,Chaine=6,Entier=7,
		Reel=8,Char=9,Vrai=10,Faux=11,Mod=12,Old=13,Not=14,PlusU=15,MoinsU=16,
		Point=17,Puiss=18,Xor=19,Or=20,And=21,And_Then=22,Or_Else=23,Implies=24,
		Egal=25,Diff=26,Infs=27,Inf=28,Sup=29,Sups=30,Appel=31,Var=32,Tableau=33,
		Free_Op=34,Free_OpU=35,Dollard=36;
	public Type type;
	public boolean erreur;
	public Classe classe;
	/*public String str;
	public Chaine str2;
	public int entier;
	public double reel;
	public String caracter;
	public Vector parametre;
	public String nom;
	public Vector tableau;
	public Token oper;*/
}
