package tinyeiffel.ast;

import java.io.*;
import java.util.*;

public class Classe implements ToXML,Traite //extends ASTEiffel
{
	public Classe(boolean deferred,boolean expanded,
		Type type,Vector feature,Vector heritage,
		Vector invariant,Vector creation,Chaine obsolete,
		Vector index,Vector comment)
	{
		this.type=type;
		nom=type.nom;
		this.deferred=deferred;
		this.expanded=expanded;
		if(feature!=null)
		{
			Vector v=new Vector();
			for(int i=0;i<feature.size();i++)
			{
				Object o=feature.elementAt(i);
				if(o!=null)
					v.addElement(o);
			}
			this.feature=new Feature[v.size()];
			v.copyInto(this.feature);
			for(int i=0;i<this.feature.length;i++)
				this.feature[i].classe=this;
		}
		if(heritage!=null)
		{
			this.heritage=new Heritage[heritage.size()];
			heritage.copyInto(this.heritage);
		}
		if(invariant!=null)
		{
			this.invariant=new Assert[invariant.size()];
			invariant.copyInto(this.invariant);
		}
		if(creation!=null)
		{
			this.creation=new Creation[creation.size()];
			creation.copyInto(this.creation);
		}
		this.obsolete=obsolete;
		if(index!=null)
		{
			this.index=new Indexing[index.size()];
			index.copyInto(this.index);
		}
		if(comment!=null)
		{
			this.commentaire=new Commentaire[comment.size()];
			comment.copyInto(this.commentaire);
		}
	}

	public void set_liste_classe(Vector liste)
	{
		this.liste_classe=new Type[liste.size()];
		liste.copyInto(this.liste_classe);
	}

        public void set_token(Token tclass,Token texp_defer,
                              Token tobsolete,Token tinvariant,
                              Token tend)
        {
          this.tclass=tclass;
          this.texp_defer=texp_defer;
          this.tobsolete=tobsolete;
          this.tinvariant=tinvariant;
          this.tend=tend;
          this.debut=new Position(tclass.x,tclass.y);
          this.fin=new Position(tend.x,tend.y);
        }

        public boolean equals(Object o)
	{
		if(o instanceof Classe)
		{
			Classe c=(Classe)o;
			if(nom.compareToIgnoreCase(c.nom)==0)
				return true;
		}
		return false;
	}

	public boolean deType(Type t)
	{
          if(t==null||t.nom==null)
            return false;

          if(t.nom.compareToIgnoreCase(nom)==0)
	    return true;
	  return false;
	}

	public void check_egal(Classe c)
	{
		assert(c!=null);
		assert(nom.equals(c.nom)):nom+"!="+c.nom;
		type.check_egal(c.type);
		assert(deferred==c.deferred);
		assert(expanded==c.expanded):"difference "+nom+":"+expanded+
					"!="+c.expanded;
		if(obsolete==null)
			assert(c.obsolete==null);
		else
			obsolete.check_egal(c.obsolete);
		int i;
		if(feature==null)
			assert(c.feature==null);
		else
		{
			assert(feature.length==c.feature.length):
				"Pas le meme nombre de feature:"+feature.length+
				"!="+c.feature.length;
			for(i=0;i<feature.length;i++)
			{
				feature[i].check_egal(c.feature[i]);		
			}
		}
		/***********/
		if(heritage==null)
			assert(c.heritage==null);
		else
		{
			assert(c.heritage!=null);
			assert(heritage.length==c.heritage.length):
				"Difference du nombre d'heritage:"+
				heritage.length+"!="+c.heritage.length;
			for(i=0;i<heritage.length;i++)
			{
				heritage[i].check_egal(c.heritage[i]);		
			}
		}
		/***********/
		assert(c!=null);
		if(invariant==null)
			assert(c.invariant==null);
		else
		{
			assert(c!=null);
			assert(c.invariant!=null);
			assert(invariant.length==c.invariant.length):"nom="+nom+";"+invariant.length+"!="+c.invariant.length+";"+
				c.invariant[2].expr;
			for(i=0;i<invariant.length;i++)
			{
				invariant[i].check_egal(c.invariant[i]);		
			}
		}
		/***********/
		if(creation==null||creation.length==0)
			assert(c.creation==null||c.creation.length==0);
		else
		{
			assert(creation.length==c.creation.length):creation.length+"!="+c.creation.length;
			for(i=0;i<creation.length;i++)
			{
				creation[i].check_egal(c.creation[i]);		
			}
		}
		/***********/
		/*if(index==null)
			assert(c.index==null);
		else
		{
			assert(index.length==c.index.length);
			for(i=0;i<index.length;i++)
			{
				index[i].check_egal(c.index[i]);		
			}
		}*/
	}

	public void toXML(PrintStream out2)
	{
		assert(out2!=null);
		//PrintStream out2=new PrintStream(out);
		out2.println("<?xml version=\"1.0\"?>");
		out2.println("<!DOCTYPE class SYSTEM \"..\\test_unitaire\\classe.dtd\">");
		out2.println("<class "+
				((expanded)?"type=\"expanded\"":"")+" "+
				((deferred)?"type=\"deferred\"":"")+">");
		type.toXML(out2);
		int i;
		if(obsolete!=null)
		{
			out2.println("<obsolete>");
			obsolete.toXML(out2);
			out2.println("</obsolete>");
		}
		if(heritage!=null)
		{
			for(i=0;i<heritage.length;i++)
				heritage[i].toXML(out2);
		}
		if(creation!=null)
		{
			for(i=0;i<creation.length;i++)
				creation[i].toXML(out2);
		}
		if(feature!=null)
		{
			for(i=0;i<feature.length;i++)
				feature[i].toXML(out2);
		}
		if(invariant!=null)
		{
			out2.println("<invariant>");
			for(i=0;i<invariant.length;i++)
				invariant[i].toXML(out2);
			out2.println("</invariant>");
		}
		out2.println("</class>");
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
	public Feature feature[];
	public Type type;
	public Heritage heritage[];
	public Assert invariant[];
	public Creation creation[];
	public Chaine obsolete;
	public Indexing index[];
	public Type liste_classe[];
	public boolean attributs_traite;
	//public Vector attributs,attributs_ancetre;
	public Position debut,fin;
	public boolean expanded,deferred;
	public Token tclass,texp_defer,tobsolete,tinvariant,tend;
	public Commentaire commentaire[];

        //public Position token_classe,
}
