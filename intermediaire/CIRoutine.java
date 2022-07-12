/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIRoutine {

	/**
	 * 
	 */
	public CIRoutine(CIClasse classe,CISource source) {
		assert(classe!=null);
		this.classe=classe;
		this.source=source;
	}

	public CIDeclare_Var ajoute_var_local(String nom,CIType type)
	{
		CIDeclare_Var res,tmp[];
		int i,j;
		assert(nom!=null);
		assert(type!=null);
		res=new CIDeclare_Var(nom,type,null);
		if(local==null)
		{
			//local=new Declare_Var[1];
			//local[0]=res;
			local=new CIListe_Var();
		}
		/*else
		{
			tmp=new Declare_Var[local.length+1];
			for(i=0;i<local.length;i++)
			{
				tmp[i]=local[i];
			}
			tmp[i]=res;
			local=tmp;
		}*/
		local.ajoute(res);
		return res;
	}

	public CIDeclare_Var cherche_var(String nom)
	{
		CIDeclare_Var res;
		CIAttribut attr;
		CINom_Attribut n;
		assert(nom!=null);
		res=cherche_var_routine(nom);
		if(res!=null)
			return res;
		attr=classe.donne_attribut(nom);
		if(nom.equalsIgnoreCase("void"))
			assert(attr!=null):"classe="+classe.nom+";"+classe.liste_attribut;
		if(attr!=null)
		{
			res=new CIDeclare_Var(attr.nom.nom,attr.retour,null);
			return res;
		}
		if(nom.compareToIgnoreCase("current")==0)
		{
			res=new CIDeclare_Var("Current",classe.nom,null);
			return res;
		}
		res=classe.programme.liste_var_global.donne_var(nom);
		return res;
	}

	public CIDeclare_Var cherche_var_routine(String nom)
	{
		int i,j;
		CIDeclare_Var v;
		assert(nom!=null);
		if(local!=null)
		{
			for(i=0;i<local.size();i++)
			{
				v=local.elt(i);
				if(v.nom.compareToIgnoreCase(nom)==0)
					return v;
			}
		}
		if(parametre!=null)
		{
			for(i=0;i<parametre.size();i++)
			{
				v=parametre.elt(i);
				if(v.nom.compareToIgnoreCase(nom)==0)
					return v;
			}
		}
		if(retour!=null)
		{
			if(retour.nom.compareToIgnoreCase(nom)==0)
				return retour;
		}
		return null;
	}

	public void affiche(PrintStream out)
	{
		int i;
		assert(out!=null);
		if(parametre!=null)
		{
			out.print("(");
			for(i=0;i<parametre.size();i++)
			{
				if(i>0)
					out.print(",");
				out.print(parametre.elt(i));
			}
			out.print(")");
		}
		if(retour!=null)
		{
			out.println(":"+retour);
		}
		else
			out.println();
		out.println("local");
		if(local!=null)
		{
			for(i=0;i<local.size();i++)
			{
				out.println(local.elt(i));
			}
		}
		out.println("do");
		if(liste_instruction!=null)
		{
			for(i=0;i<liste_instruction.size();i++)
			{
				liste_instruction.elt(i).affiche(out);
			}
		}
		out.println("end");
	}

	public void toXML(PrintStream out)
	{
		int i,j;
		assert(out!=null);
		out.println("<routine>");
		out.println("<signature>");
		if(parametre!=null)
		{
			/*for(i=0;i<parametre.size();i++)
			{
				parametre.elt(i).toXML(out);
			}*/
			parametre.toXML(out);
		}
		if(retour!=null)
			retour.toXML(out);
		out.println("</signature>");
		if(precondition!=null)
		{
			precondition.toXML(out,"precondition");
		}
		if(local!=null)
		{
			/*for(i=0;i<local.size();i++)
			{
				local.elt(i).toXML(out);
			}*/
			local.toXML(out);
		}
		out.println("<instruction>");
		if(liste_instruction!=null)
		{
			/*for(i=0;i<liste_instruction.size();i++)
			{
				liste_instruction.elt(i).toXML(out);
			}*/
			liste_instruction.toXML(out);
		}
		out.println("</instruction>");
		if(postcondition!=null)
		{
			postcondition.toXML(out,"postcondition");
		}
		if(source!=null)
		{
			source.toXML(out);
		}
		out.println("</routine>");
	}

	public void check_egal(CIRoutine r)
	{
		int i;
		assert(r!=null);
		if(liste_instruction==null||liste_instruction.size()==0)
			assert(r.liste_instruction==null||r.liste_instruction.size()==0);
		else
		{
			/*assert(r.liste_instruction!=null);
			assert(r.liste_instruction.length==liste_instruction.length);
			for(i=0;i<liste_instruction.length;i++)
			{
				liste_instruction[i].check_egal(r.liste_instruction[i]);
			}*/
			liste_instruction.check_egal(r.liste_instruction);
		}
		if(local==null||local.size()==0)
			assert(r.local==null||r.local.size()==0);
		else
			local.check_egal(r.local);
		if(parametre==null||parametre.size()==0)
			assert(r.parametre==null||r.parametre.size()==0);
		else
			parametre.check_egal(r.parametre);
		if(retour==null)
			assert(r.retour==null);
		else
		{
			assert(r.retour!=null);
			retour.check_egal(r.retour);
		}
		if(precondition==null)
			assert(r.precondition==null):"r="+r.precondition.liste_instr.size();
		else
			precondition.check_egal(r.precondition);
		if(postcondition==null)
			assert(r.postcondition==null);
		else
			postcondition.check_egal(r.postcondition);
		if(source!=null)
		{
			source.check_egal(r.source);
		}
		else
		{
			assert(r.source==null);
		}
	}
	
	public CIClasse classe;
	//public Instruction liste_instruction[];
	public CIListe_Instr liste_instruction;
	public CIDeclare_Var retour;//local[],parametre[],
	public CIListe_Var local,parametre;
	public CIAssertion precondition,postcondition;
	public CISource source;

}
