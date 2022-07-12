package tinyeiffel.genere_java;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;

import tinyeiffel.intermediaire.CIAttribut;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CINom_Attribut;
import tinyeiffel.intermediaire.CIProgramme;
import tinyeiffel.intermediaire.CIType;
import tinyeiffel.intermediaire.CITypeSimple;

public class EnvironementGlobal {

	public EnvironementGlobal(CIProgramme programme)
	{
		this.programme=programme;
	}
	

	public boolean est_special(CIType t)
	{
		CITypeSimple t2;
		assert(t!=null);
		t2=simplifie_type(t);
		if(t2.nom.startsWith("$"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Type conv_type(CIType t)
	{
		CITypeSimple t2;
		assert(t!=null);
		t2=simplifie_type(t);
		if(est_special(t2))
		{
			if(t2.nom.equalsIgnoreCase("$integer"))
			{
				return Type.INT;
			}
			else if(t2.nom.equalsIgnoreCase("$boolean"))
			{
				return Type.BOOLEAN;
			}
			else if(t2.nom.equalsIgnoreCase("$character"))
			{
				return Type.CHAR;
			}
			else if(t2.nom.equalsIgnoreCase("$real"))
			{
				return Type.FLOAT;
			}
			else if(t2.nom.equalsIgnoreCase("$double"))
			{
				return Type.DOUBLE;
			}
			else if(t2.nom.equalsIgnoreCase("$string"))
			{
				return new ObjectType("java.lang.String");
			}
			else
			{
				assert(false):"type inconnue:"+t2.nom;
				return null;
			}
		}
		else
		{
			return new ObjectType(nom_interface(t2));
		}
	}
	
	public ReturnInstruction conv_retour(CIType t)
	{
		Instruction inst;
		CITypeSimple t2;
		t2=simplifie_type(t);
		if(est_special(t2))
		{
			if(t2.nom.equalsIgnoreCase("$integer"))
			{
				return InstructionConstants.IRETURN;
			}
			else if(t2.nom.equalsIgnoreCase("$boolean"))
			{
				return InstructionConstants.IRETURN;
			}
			else if(t2.nom.equalsIgnoreCase("$character"))
			{
				return InstructionConstants.IRETURN;
			}
			else if(t2.nom.equalsIgnoreCase("$real"))
			{
				return InstructionConstants.FRETURN;
			}
			else if(t2.nom.equalsIgnoreCase("$double"))
			{
				return InstructionConstants.DRETURN;
			}
			else if(t2.nom.equalsIgnoreCase("$string"))
			{
				return InstructionConstants.ARETURN;
			}
			else
			{
				assert(false):"type inconnue:"+t2.nom;
				return null;
			}
		}
		else
		{
			return InstructionConstants.ARETURN;
		}
	}
	
	
	private static int no_temp=1;
	public String temporaire() {
		String res;
		res="temp"+no_temp;
		no_temp++;
		return res;
	}

	public CITypeSimple simplifie_type(CIType type)
	{
		return (CITypeSimple)type;
	}
	
	public boolean types_egaux(CITypeSimple type, CITypeSimple nom) {
		if(type==null)
			return false;
		if(nom==null)
			return false;
		if(type.nom.equalsIgnoreCase(nom.nom))
			return true;
		else
			return false;
	}

	public String nom_classe(CIClasse cl)
	{
		return nom_classe(cl.nom);
	}
	
	public String nom_interface(CIClasse cl)
	{
		return nom_interface(cl.nom);
	}

	public String nom_classe(CITypeSimple t)
	{
		return t.nom.toUpperCase()+"_Imp";
	}
	
	public String nom_interface(CITypeSimple t)
	{
		return t.nom.toUpperCase();
	}

	public String nom_interface(CIType t)
	{
		return simplifie_type(t).nom.toUpperCase();
	}
	
	/*public Environement nouvelle_environement(CIRoutine r,
		MethodGen  mg,ClassGen cg,
		CIClasse cl,CIAttribut attr)
	{
		return new Environement(this,r,mg,cg,cl,attr);
	}*/
	
	public CIClasse donne_classe(String nom)
	{
		CIClasse cl;
		int i;
		if(programme.liste_classe!=null)
		{
			CITypeSimple t;
			t=new CITypeSimple(false,nom,null,null,null);
			cl=programme.donne_classe(t);
			return cl;
		}
		return null;
	}
	
	public CIClasse donne_classe(CIType type)
	{
		CIClasse cl;
		int i;
		if(programme.liste_classe!=null)
		{
			//CITypeSimple t;
			//t=new CITypeSimple(false,nom,null,null,null);
			cl=programme.donne_classe(type);
			return cl;
		}
		return null;
	}

	public CIAttribut donne_attribut(CIClasse cl, CIAttribut attr) {
		if(attr.est_descendant())
		{
			int i,no_heritage;
			CIAttribut res;
			CINom_Attribut nom;
			CIClasse classe_ancetre;
			no_heritage=attr.attribut_ascendant[0].no;
			if(attr.attribut_ascendant[0].nom!=null)
				nom=attr.attribut_ascendant[0].nom;
			else
				nom=attr.nom;
			classe_ancetre=donne_classe(cl.heritage[no_heritage]);
			res=donne_attribut(classe_ancetre,classe_ancetre.donne_attribut(nom));
			return res;
		}
		return attr;
	}
	
	public CIProgramme programme;
}
