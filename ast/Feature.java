package tinyeiffel.ast;

import java.util.*;
import java.io.*;

public abstract class Feature implements ToXML,Traite
{
	public NomFeature cherche_nom(NomFeature nom)
	{
		if(nom==null)
			return null;
		for(int i=0;i<liste_nom.length;i++)
		{
			if(nom.equals(liste_nom[i]))
			{
				return liste_nom[i];
			}
		}
		return null;
	}

	public void set_require_ensure(Vector require,Vector ensure,
		Vector rescue,Chaine obsolete,Vector commentaire,
		Vector commentaire2)
	{
		this.require=new Assert[require.size()];
		require.copyInto(this.require);
		this.ensure=new Assert[ensure.size()];
		ensure.copyInto(this.ensure);
		this.rescue=new Instr[rescue.size()];
		rescue.copyInto(this.rescue);
		this.obsolete=obsolete;
		if(commentaire!=null)
		{
			this.commentaire=new Commentaire[commentaire.size()];
			commentaire.copyInto(this.commentaire);
		}
		if(commentaire2!=null)
		{
			this.commentaire2=new Commentaire[commentaire2.size()];
			commentaire2.copyInto(this.commentaire2);
		}
	}

	public void set_nom_param(Vector nom,Vector param,Vector export,Vector comment)
	{
		this.liste_nom=new NomFeature[nom.size()];
		nom.copyInto(this.liste_nom);
		if(param!=null)
		{
			this.param=new DeclareVar[param.size()];
			param.copyInto(this.param);
		}
		if(export!=null)
		{
			this.export=new Type[export.size()];
			export.copyInto(this.export);
		}
		if(comment!=null)
		{
			this.commentaire3=new Commentaire[comment.size()];
			comment.copyInto(this.commentaire3);
		}
	}

        public void set_token(Token tobsolete,Token trequire,
                              Token tensure,Token trescue)
        {
          this.tobsolete=tobsolete;
          this.trequire=trequire;
          this.tensure=tensure;
          this.trescue=trescue;
        }

        public void set_token(Token tunique)
        {
          this.tunique=tunique;
        }

        public Position debut()
        {
          //if(tfeature!=null)
            //return tfeature.debut();
          if(liste_nom!=null)
            return liste_nom[0].debut();
          return null;
        }

	public boolean is_deferred()
	{
		return false;
	}

	public boolean est_routine()
	{// est routine, ou external, ou deferred
		return false;
	}

	public boolean est_constant()
	{
		return false;
	}

	public boolean est_variable()
	{
		return false;
	}

	public boolean est_external()
	{
		return false;
	}

	public void check_egal(Feature f)
	{
		assert(f!=null);
		int i;
		/********/
		if(liste_nom==null)
			assert(f.liste_nom==null);
		else
		{
			assert(f.liste_nom!=null);
			assert(liste_nom.length==f.liste_nom.length);
			for(i=0;i<liste_nom.length;i++)
			{
				liste_nom[i].check_egal(f.liste_nom[i]);		
			}
		}
		/********/
		if(param==null)
			assert(f.param==null);
		else
		{
			assert(f.param!=null);
			if(param.length!=f.param.length)
			{
				System.out.println("Diff:");
				for(i=0;i<param.length;i++)
				{
					System.out.println(i+")"+param[i].toString2());
				}
				System.out.println("/=");
				for(i=0;i<f.param.length;i++)
				{
					System.out.println(i+")"+f.param[i].toString2());
				}
			}
			assert(param.length==f.param.length):
						"difference:"+param.length+"!="+
										f.param.length;
			for(i=0;i<param.length;i++)
			{
				param[i].check_egal(f.param[i]);		
			}
		}
		/********/
		if(require==null||require.length==0)
			assert(f.require==null||f.require.length==0);
		else
		{
			assert(f.require!=null);
			assert(require.length==f.require.length);
			for(i=0;i<require.length;i++)
			{
				require[i].check_egal(f.require[i]);		
			}
		}
		/********/
		if(ensure==null||ensure.length==0)
			assert(f.ensure==null||f.ensure.length==0);
		else
		{
			assert(f.ensure!=null);
			assert(ensure.length==f.ensure.length);
			for(i=0;i<ensure.length;i++)
			{
				ensure[i].check_egal(f.ensure[i]);		
			}
		}
		/********/
		if(rescue==null||rescue.length==0)
			assert(f.rescue==null||f.rescue.length==0);
		else
		{
			assert(f.rescue!=null);
			assert(rescue.length==f.rescue.length);
			for(i=0;i<rescue.length;i++)
			{
				rescue[i].check_egal(f.rescue[i]);		
			}
		}
		/********/
		if(export==null||export.length==0)
			assert(f.export==null||f.export.length==0);
		else
		{
			assert(f.export!=null);
			assert(export.length==f.export.length):
				"Difference:"+export.length+"!="+f.export.length;
			for(i=0;i<export.length;i++)
			{
				export[i].check_egal(f.export[i]);		
			}
		}
		/********/
		if(commentaire==null||commentaire.length==0)
			assert(f.commentaire==null||f.commentaire.length==0):"nb_com="+f.commentaire.length;
		else
		{
			assert(f.commentaire!=null);
			assert(commentaire.length==f.commentaire.length);
			for(i=0;i<commentaire.length;i++)
			{
				assert(commentaire[i]==f.commentaire[i]);		
			}
		}
		if(type_retour==null)
			assert(f.type_retour==null);
		else
			type_retour.check_egal(f.type_retour);
		if(obsolete==null)
			assert(f.obsolete==null);
		else
			obsolete.check_egal(f.obsolete);
	}

	public abstract void toXML(PrintStream out);

	public void toXMLDebut(PrintStream out)
	{
		assert(out!=null);
		out.println("<feature>");
		int i;
		if(export!=null/*&&export.length>0*/)
		{
			out.println("<liste_export>");
			for(i=0;i<export.length;i++)
				export[i].toXML(out);
			out.println("</liste_export>");
		}
		if(liste_nom!=null&&liste_nom.length>0)
		{
			for(i=0;i<liste_nom.length;i++)
			{
				liste_nom[i].toXML(out);
			}
		}
		if(param!=null&&param.length>0)
		{
			for(i=0;i<param.length;i++)
			{
				DeclareVar v=param[i];
				out.println("<parametre_formel nom=\""+v.nom+"\">");
				v.type.toXML(out);
				out.println("</parametre_formel>");
			}
		}
		if(type_retour!=null)
		{
			type_retour.toXML(out);
		}
	}

	public void toXMLCorpsDebut(PrintStream out)
	{
		assert(out!=null);
		int i;
		toXMLDebut(out);
		out.println("<corps>");
		if(obsolete!=null)
		{
			out.println("<obsolete>");
			obsolete.toXML(out);
			out.println("</obsolete>");
		}
		if(require!=null&&require.length>0)
		{
			out.println("<require>");
			for(i=0;i<require.length;i++)
			{
				require[i].toXML(out);
			}
			out.println("</require>");
		}
		out.println("<corps2>");
	}
	
	public void toXMLFin(PrintStream out)
	{
		assert(out!=null);
		out.println("</feature>");
	}

	public void toXMLCorpsFin(PrintStream out)
	{
		assert(out!=null);
		int i;
		out.println("</corps2>");
		if(ensure!=null&&ensure.length>0)
		{
			out.println("<ensure>");
			for(i=0;i<ensure.length;i++)
			{
				ensure[i].toXML(out);
			}
			out.println("</ensure>");
		}
		if(rescue!=null&&rescue.length>0)
		{
			out.println("<rescue>");
			for(i=0;i<rescue.length;i++)
			{
				rescue[i].toXML(out);
			}
			out.println("</rescue>");
		}
		out.println("</corps>");
		toXMLFin(out);
	}
	
	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	public String toString()
	{
		int i;
		String s="";
		for(i=0;i<liste_nom.length;i++)
		{
			if(i>0)
				s+=",";
			s+=liste_nom[i].toString();
		}
		return s;
	}
	
	protected boolean traite=false;

	public NomFeature liste_nom[];
	public DeclareVar param[];
	public Assert[] require,ensure;
	public Type type_retour;
	public Instr rescue[];
	public Type export[];
	public Commentaire commentaire[],commentaire2[],commentaire3[];
	public Chaine obsolete;
	public Classe classe;
        public Token tfeature,tobsolete,trequire,tensure,
            trescue,tunique,tdeferred,texternal,talias,tdo;
}
