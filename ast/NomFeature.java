package tinyeiffel.ast;

import java.io.*;

public class NomFeature implements ToXML,Traite
{
	public NomFeature(String nom)
	{
		assert(nom!=null);
		this.nom=nom;
	}

	public NomFeature(Chaine nom)
	{
		assert(nom!=null);
		this.nom2=nom;
	}

	public boolean equals(Object o)
	{
		if(o instanceof NomFeature)
		{
			NomFeature n=(NomFeature)o;
			if(prefix!=n.prefix||infix!=n.infix)
				return false;
			String n1,n2;
			if(nom!=null)
				n1=nom;
			else
				n1=nom2.chaine_unique();
			//assert(n.nom!=null||n.nom2!=null);
			if(n.nom!=null)
				n2=n.nom;
			else
			{
				assert(n.nom2!=null):n;
				n2=n.nom2.chaine_unique();
			}
			if(n1.charAt(0)!='"')
				n1="\""+n1+"\"";
			if(n2.charAt(0)!='"')
				n2="\""+n2+"\"";
			return (n1.compareToIgnoreCase(n2)==0);
		}
		else if(o instanceof Attribut)
		{
			Attribut a=(Attribut)o;
			return a.equals(this);
		}
		else if(o instanceof String)
		{
			if(!prefix&&!infix)
				return nom.compareToIgnoreCase((String)o)==0;
		}
		return false;
	}

	public String toString()
	{
		if(prefix)
		{
			if(nom2!=null)
				return "prefix "+nom2;
			else
				return "prefix "+nom;
		}
		else if(infix)
		{
			if(nom2!=null)
				return "infix "+nom2;
			else
				return "infix "+nom;
		}
		assert(nom!=null);
		return nom;
	}

        public void set_token(Token tnom,Token tpre_in)
        {
            this.tnom=tnom;
            this.tpre_in=tpre_in;
            //this.tfrozen
            if(tpre_in!=null)
              debut=new Position(tpre_in.x,tpre_in.y);
            else
              debut=new Position(tnom.x,tnom.y);
        }

        public Position debut()
        {
          if(tfrozen!=null)
            return tfrozen.debut();
          if(tpre_in!=null)
            return tpre_in.debut();
          if(tnom!=null)
            return tnom.debut();
          //return null;
          assert(false):"("+toString()+")";
          return null;
        }

	public void check_egal(NomFeature n)
	{
		assert(n!=null);
		assert(n instanceof NomFeature);
		assert(prefix==n.prefix);
		assert(infix==n.infix);	
		assert(frozen==n.frozen):"frozen different:"+frozen+","+n.frozen;
		if(nom2==null)
		{
			assert(nom!=null);
			assert(n.nom!=null);
			assert(n.nom2==null);
			assert(nom.equals(n.nom)):"nom different:"+nom+"!="+n.nom+";";
		}
		else
		{
			assert(nom==null);
			assert(n.nom==null);
			assert(n.nom2!=null);
			nom2.check_egal(n.nom2);
		}
	}

	public boolean meme_nom(NomFeature n)
	{
		assert(n!=null);
		return this.equals(n);
	}

	/* (non-Javadoc)
	 * @see ast.Instr#toXML(java.io.PrintStream)
	 */
	public void toXML(PrintStream out) {
		assert(out!=null);
		out.print("<nom_feature ");
		if(nom!=null)
			out.print("nom=\""+nom+"\" ");
		else
			out.print("nom="+nom2.toString()+" ");
		if(frozen)
			out.print("frozen=\"on\" ");
		if(infix)
			out.print("type=\"infix\" ");
		else if(prefix)
			out.print("type=\"prefix\" ");
		out.println("/>");
	}
	
	public void toXML2(PrintStream out) {
		assert(out!=null);
		out.print("<nom_feature ");
		if(nom!=null)
			out.print("nom=\""+nom.replaceAll("\"","")+"\" ");
		else
			out.print("nom="+nom2.toString()+" ");
		if(frozen)
			out.print("frozen=\"on\" ");
		if(infix)
			out.print("type=\"infix\" ");
		else if(prefix)
			out.print("type=\"prefix\" ");
		out.println("/>");
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
	
	public boolean prefix,infix,frozen;
	public String nom;
	public Chaine nom2;

        public Position debut,fin;
        public Token tnom,tpre_in,tfrozen;

}