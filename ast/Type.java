package tinyeiffel.ast;

//import java.util.*;
import java.io.*;

public abstract class Type implements ToXML,Traite
{
	/*public Type(boolean expanded,String nom,Vector t)
	{
		this.nom=nom;
		this.expanded=expanded;
		if(t!=null)
		{
			generique=new Type[t.size()];
			t.copyInto(generique);
		}
	}*/

	/*public Type(Expr e)
	{
		is_like=true;
		like=e;
	}*/

	public static Type copieType(Type type)
	{
		Type t;
		if(type instanceof TypeSimple)
		{
			t=new TypeSimple(type.expanded,type.nom,null);
		}
		else
		{
			t=new TypeAncre(type.like);
		}
		t.nom=type.nom;
		t.is_like=type.is_like;
		t.like=type.like;
		if(type.generique!=null)
		{
			t.generique=new Type[type.generique.length];
			for(int i=0;i<type.generique.length;i++)
			{
				t.generique[i]=copieType(type.generique[i]);
			}
		}
		t.is_from=type.is_from;
		t.expanded=type.expanded;
		//classe_env=type.classe_env;
		
		t.debut_=type.debut_;
		t.fin=type.fin;
		t.tnom=type.tnom;
		t.tcrochet_ouvr=type.tcrochet_ouvr;
		t.tcrochet_ferm=type.tcrochet_ferm;
		t.tlike=type.tlike;
		t.tfleche=type.tfleche;
		return t;
	}

	public boolean egaux(Type t)
	{
		if(nom==null)
			return false;

		if(t!=null&&t.nom!=null)
			return nom.compareToIgnoreCase(t.nom)==0;
		return false;
	}

	public boolean equals(Object o)
	{
		if(o instanceof Type)
		{
			Type t=(Type)o;
			if(is_like==t.is_like)
			{// TODO:a completer
				return true;
			}
			else
			{
				if(t.is_like)
					return false;
				return nom==t.nom;
			}
		}
		return false;
	}

	public String toString()
	{
		if(is_like)
		{
			if(like instanceof Expr_Var)
				return "like "+((Expr_Var)like).nom;
			else
				return "like "+like.toString();
		}
		assert(nom!=null);
		return ((expanded)?"expanded ":"")+nom;
	}

        public void set_token(Token tnom,Token tcrochet_ouvr,
                              Token tcrochet_ferm)
        {
          this.tnom=tnom;
          this.tcrochet_ouvr=tcrochet_ouvr;
          this.tcrochet_ferm=tcrochet_ferm;
          //debut=tnom.debut();
        }

        public void set_token(Token tlike)
        {
          this.tlike=tlike;
          //debut=tlike.debut();
        }

        public void set_token(Token tnom,Token tfleche)
        {
          this.tnom=tnom;
          this.tfleche=tfleche;
          //debut=tnom.debut();
        }

	public Position debut()
	{
		if(tnom!=null)
			return tnom.debut();
		else
			return tlike.debut();
	}

	public void check_egal(Type t)
	{
		assert(t!=null);
		if(nom==null)
			assert(t.nom==null);
		else
			assert(nom.equals(t.nom)):
				"type non egaux:"+nom+" et "+t.nom;
		if(like==null)
			assert(t.like==null);
		else
			like.check_egal(t.like);	
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		if(is_like)
		{
			out.println("<nom_classe>");
			assert(like!=null);
			out.println("<like>");
			like.toXML(out);
			out.println("</like>");
			out.println("</nom_classe>");
		}
		else
		{
			out.println("<nom_classe nom=\""+nom+"\" "+
					((expanded)?"expanded=\"yes\"":"")+">");
			//out.println("coucou_debut");
			if(generique!=null)
			{
				for(int i=0;i<generique.length;i++)
				{
					generique[i].toXML(out);
				}				
			}
			//out.println("coucou_fin");
			out.println("</nom_classe>");
		}
	}

	public String toString2()
	{
		String res;
		if(is_like)
		{
			res=toString();
		}
		else
		{
			res=nom;
			if(generique!=null&&generique.length>0)
			{
				res+="[";
				for(int i=0;i<generique.length;i++)
				{
					if(i>0)
						res+=",";
					res+=generique[i].toString2();
				}
				res+="]";
			}
		}
		return res;
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
	public boolean is_like;
	public Expr like;
	public Type generique[];
	public boolean is_from;
	public boolean expanded;
	//public Classe classe_env;

        public Position debut_,fin;
        public Token tnom,tcrochet_ouvr,tcrochet_ferm,
            tlike,tfleche;

}