package tinyeiffel.genere_java.generation;

import java.io.File;
import java.io.IOException;

import org.apache.bcel.generic.Type;

import tinyeiffel.genere_java.EnvironementGlobal;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CIType;
import tinyeiffel.intermediaire.CITypeSimple;

public interface GenereJvm {

	public static final int IADD=1,FADD=2,DADD=3,ISUB=4,FSUB=5,DSUB=6,IMUL=7,FMUL=8,DMUL=9,
		IDIV=10,FDIV=11,F2I=12,DDIV=13,D2I=14,IREM=15,ICONST_0=16,ICONST_1=17,FCMPG=18,DCMPG=19,IF_ICMPEQ=20,
		IF_ICMPGE=21,IF_ICMPGT=22,IF_ICMPLE=23,IF_ICMPNE=24,IAND=25,IOR=26,IXOR=27,INEG=28,I2F=29,I2D=30,
		F2D=31,D2F=32,I2C=33,IFEQ=34,IF_ACMPNE=35,IFNE=36,DUP=37,RETURN=38,ARETURN=39,PUSHTHIS=40,ACONSTNULL=41,NOP=42,GOTO=43;
	
	public abstract EnvironementGlobal getEnv();
	public abstract Type conv_type(CIType type);
	
	public abstract void nouvelle_classe(String nom_classe,String classe_ancetre,String liste_interfaces[]);
	public abstract void nouvelle_interface(String nom_classe,String classe_ancetre,String liste_interfaces[]);
	public abstract void sauve(File f) throws IOException;
		
	public abstract void ajoute_champs(String nom,CIType type,boolean est_static);
	
	public abstract void nouvelle_methode(String nom,CIType type_retour,CIType parametres[],boolean abstraite);
	public abstract void fin_methode();
	
	public abstract int instr_invoke_special(String classe,String nom_methode,CIType type_retour,CIType parametres[]);
	public abstract int instr_new(String nom);
	public abstract int instr_aload(int var_local);
	public abstract int instr_put_field(String classe,String nom_champs,CIType type);
	public abstract int instr_get_field(CITypeSimple classe,String nom_champs,CIType type);
	public abstract int instr_invoke_virtual(String classe,String nom_methode,CIType type_retour,CIType parametres[]);
	public abstract int instr_load(CIType type,int no);
	public abstract int instr_invoke_interface(String classe,String nom_methode,CIType type_retour,CIType parametres[]);
	public abstract int instr_return(CIType type);
	public abstract void set_cible(int source,int cible);
	public abstract int instr_expr(int op);
	public abstract int instr_iload(int var_local);
	public abstract int instr_iconst(int i);
	public abstract int instr_dload(int var_local);
	public abstract int instr_dconst(double d);
	public abstract int instr_fload(int var_local);
	public abstract int instr_fconst(float d);
	public abstract int instr_sconst(String s);
	public abstract int instr_cast(CIType src,CIType dest);
	public abstract int instr_instanceof(CIType t);
	public abstract int instr_store(CIType t,int no_local);
	public abstract int instr_invoke_static(String classe,String nom_methode,CIType type_retour,CIType parametres[]);
	
	public abstract int ajoute_var_local(String nom,CIType type);
	public abstract int donne_var_local(String nom);
	
	public abstract void construit_main(CIClasse cl);
	
	public abstract boolean verification();
	
	public abstract boolean contient_attribut(String nom_attribut);
	public abstract String donne_type(String nom_attribut);
}
