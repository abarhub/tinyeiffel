package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public class Heritage implements ToXML,Traite
{
	public Heritage(Type type,Vector rename,Vector export,
		Vector undefine,Vector redefine,Vector select)
	{
		this.type=type;
		this.rename=new Rename[rename.size()];
		rename.copyInto(this.rename);
		this.export=new Export[export.size()];
		export.copyInto(this.export);
		this.undefine=new NomFeature[undefine.size()];
		undefine.copyInto(this.undefine);
		this.redefine=new NomFeature[redefine.size()];
		redefine.copyInto(this.redefine);
		this.select=new NomFeature[select.size()];
		select.copyInto(this.select);
	}
        public void set_token(Token therit,Token trename,
                                     Token texport,Token tundefine,
                                     Token tredefine,Token tselect,
                                     Token tend)
        {
          this.therit=therit;
          this.trename=trename;
          this.texport=texport;
          this.tundefine=tundefine;
          this.tredefine=tredefine;
          this.tselect=tselect;
          this.tend=tend;
        }

        public Position debut()
        {
          if(therit!=null)
            return therit.debut();
          return null;
        }

	public void check_egal(Heritage h)
	{
		assert(h!=null);
		type.check_egal(h.type);
		int i;
		/****/
		assert(rename.length==h.rename.length);
		for(i=0;i<rename.length;i++)
		{
			rename[i].check_egal(h.rename[i]);
		}
		/****/
		assert(export.length==h.export.length);
		for(i=0;i<export.length;i++)
		{
			export[i].check_egal(h.export[i]);
		}
		/****/
		assert(undefine.length==h.undefine.length);
		for(i=0;i<undefine.length;i++)
		{
			undefine[i].check_egal(h.undefine[i]);
		}
		/****/
		assert(redefine.length==h.redefine.length);
		for(i=0;i<redefine.length;i++)
		{
			redefine[i].check_egal(h.redefine[i]);
		}
		/****/
		assert(select.length==h.select.length);
		for(i=0;i<select.length;i++)
		{
			select[i].check_egal(h.select[i]);
		}
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<heritage>");
		type.toXML(out);
		int i;
		if(rename!=null)
		{
			for(i=0;i<rename.length;i++)
				rename[i].toXML(out);
		}
		if(export!=null&&export.length>0)
		{
			for(i=0;i<export.length;i++)
				export[i].toXML(out);
		}
		if(undefine!=null&&undefine.length>0)
		{
			out.println("<undefine>");
			for(i=0;i<undefine.length;i++)
			{
				undefine[i].toXML(out);
			}
			out.println("</undefine>");
		}
		if(redefine!=null&&redefine.length>0)
		{
			out.println("<redefine>");
			for(i=0;i<redefine.length;i++)
				redefine[i].toXML(out);
			out.println("</redefine>");
		}
		if(select!=null&&select.length>0)
		{
			out.println("<select>");
			for(i=0;i<select.length;i++)
				select[i].toXML(out);
			out.println("</select>");
		}
		out.println("</heritage>");
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

	public Type type;
	public Rename rename[];
	public Export export[];
	public NomFeature undefine[],redefine[],select[];
        public Position debut,fin;
	public Token therit,trename,texport,tundefine,tredefine,
            tselect,tend;
}