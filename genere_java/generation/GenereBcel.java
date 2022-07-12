package tinyeiffel.genere_java.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.FLOAD;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;

import tinyeiffel.genere_java.EnvironementGlobal;
import tinyeiffel.intermediaire.CIAttribut;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CIProgramme;
import tinyeiffel.intermediaire.CIType;
import tinyeiffel.intermediaire.CITypeSimple;


public class GenereBcel implements GenereJvm {

	private EnvironementGlobal env;
	private CIProgramme programme;
	private String nom_classe;
	private ClassGen class_gen;
	private ConstantPoolGen cp;
	private InstructionList liste_instr;
	private String nom_methode;
	private InstructionFactory factory;
	private Type type_retour;
	private Type parametres[];
	private List liste_local_nom;
	private List liste_local_type;
	private List liste_classes;
	private boolean methode_abstraite;
	private InstructionHandle debut,fin;
	private List instr_handle;
	private List instr;
	
	public GenereBcel(CIProgramme programme)
	{
		env=new EnvironementGlobal(programme);
		this.programme=programme;
		liste_classes=new ArrayList();
	}

	public void nouvelle_classe(String nom_classe, String classe_ancetre, String[] liste_interfaces) {
		this.nom_classe=nom_classe;
		class_gen = new ClassGen(nom_classe,classe_ancetre,
				nom_classe+".java", Constants.ACC_PUBLIC | Constants.ACC_SUPER,
				liste_interfaces);
		cp = class_gen.getConstantPool();
		factory = new InstructionFactory(class_gen);
	}

	public void sauve(File f) throws IOException {
		assert(f!=null);
		JavaClass res;
		//File f2;
		//f2=new File(f,)
		//cg.getJavaClass().dump("e:\\projet\\eiffel\\resultat\\"+env.nom_interface(cl)+".class");
		class_gen.getJavaClass().dump(f.getAbsolutePath());
		res=class_gen.getJavaClass();
		class_gen=null;
		cp=null;
		liste_classes.add(res);
	}

	public void nouvelle_interface(String nom_classe, String classe_ancetre, String[] liste_interfaces) {
		this.nom_classe=nom_classe;
		if(classe_ancetre==null)
			classe_ancetre="java.lang.Object";
		class_gen = new ClassGen(nom_classe,classe_ancetre,
				nom_classe+".java", Constants.ACC_PUBLIC | Constants.ACC_INTERFACE
				| Constants.ACC_ABSTRACT,
				liste_interfaces);
		cp = class_gen.getConstantPool();
		factory = new InstructionFactory(class_gen);
	}

	public void ajoute_champs(String nom, CIType type,boolean est_static) {
		Type type_retour;
		FieldGen fg;
		short acces;
		acces=Constants.ACC_PUBLIC;
		if(est_static)
			acces|=Constants.ACC_STATIC;
		type_retour=conv_type(type);
		fg=new FieldGen(acces,
			type_retour,nom,cp);
		class_gen.addField(fg.getField());
	}

	public void nouvelle_methode(String nom,CIType type_retour,
			CIType parametres[],boolean abstraite) {
		assert(nom!=null);
		assert(!nom.isEmpty());
		nom_methode=nom;
		methode_abstraite=abstraite;
		liste_instr = new InstructionList();
		this.type_retour=conv_type(type_retour);
		if(parametres==null||parametres.length==0)
		{
			this.parametres=Type.NO_ARGS;
		}
		else
		{
			this.parametres=new Type[parametres.length];
			for(int i=0;i<parametres.length;i++)
			{
				this.parametres[i]=conv_type(parametres[i]);
			}
		}
		liste_local_nom=new ArrayList();
		liste_local_type=new ArrayList();
		debut=null;
		fin=null;
		instr_handle=new ArrayList();
		instr=new ArrayList();
	}

	public Type conv_type(CIType type) {
		if(type==null)
			return Type.VOID;
		else
			return env.conv_type(type);
	}

	public void fin_methode() {
		assert(nom_methode!=null);
		assert(liste_instr!=null);
		assert(liste_local_nom.size()==liste_local_type.size());
		MethodGen  mg;
		String nom;
		CIType type;
		int acces;
		//InstructionHandle debut,fin;
		acces=Constants.ACC_PUBLIC;
		if(methode_abstraite)
		{
			acces|=Constants.ACC_ABSTRACT;
		}
		mg = new MethodGen(acces, type_retour,
			parametres, null,nom_methode, nom_classe,
			liste_instr, cp);
		//debut=liste_instr.findHandle(pos).getStart();
		//fin=liste_instr.getEnd();
		for(int i=0;i<liste_local_nom.size();i++)
		{
			nom=(String) liste_local_nom.get(i);
			type=(CIType) liste_local_type.get(i);
			mg.addLocalVariable(nom,
				env.conv_type(type),debut,fin);
		}
	    mg.setMaxStack();
	    class_gen.addMethod(mg.getMethod());
	    liste_instr.dispose();
	    nom_methode=null;
		liste_instr=null;
		//factory=null;
		type_retour=null;
		parametres=null;
		liste_local_nom=null;
		liste_local_type=null;
		debut=null;
		fin=null;
		instr_handle=null;
		instr=null;
	}

	private int ajoute_instr(Instruction instr)
	{
		InstructionHandle ins;
		assert(liste_instr!=null);
		if(instr instanceof BranchInstruction)
		{
			ins=liste_instr.append((BranchInstruction)instr);
		}
		else
		{
			ins=liste_instr.append(instr);
		}
		this.instr.add(instr);
		instr_handle.add(ins);
		if(debut==null)
			ins=debut;
		fin=ins;
		return instr_handle.size()-1;
	}
	
	public int instr_invoke_special(String classe,String nom_methode,
			CIType type_retour,CIType parametres[]) {
		return invoke(classe, nom_methode, type_retour, parametres,Constants.INVOKESPECIAL);
	}

	private int invoke(String classe, String nom_methode, CIType type_retour, CIType[] parametres,short type_appel) {
		Type tretour,tparam[];
		tretour=conv_type(type_retour);
		if(parametres==null)
		{
			tparam=new Type[0];
		}
		else
		{
			tparam=new Type[parametres.length];
			if(tparam.length>0)
			{
				for(int i=0;i<tparam.length;i++)
				{
					assert(parametres[i]!=null):classe+"."+nom_methode;
					tparam[i]=env.conv_type(parametres[i]);
				}
			}
		}
		return ajoute_instr(factory.createInvoke(classe,
				nom_methode,tretour,tparam,type_appel));
	}

	public int instr_aload(int i) {
		if(i==0)
		{
			return ajoute_instr(InstructionConstants.ALOAD_0);
		}
		else
		{
			assert(false);
			return ajoute_instr(factory.createConstant(new Integer(i)));
		}
	}

	public int instr_new(String nom) {
		return ajoute_instr(factory.createNew(nom));
	}

	public int instr_put_field(String classe,String nom_champs,CIType type) {
		Type t;
		t=conv_type(type);
		return ajoute_instr(factory.createPutField(classe,
				nom_champs,t));
	}


	public EnvironementGlobal getEnv() {
		return env;
	}

	public int ajoute_var_local(String nom, CIType type) {
		liste_local_nom.add(nom);
		liste_local_type.add(type);
		return liste_local_nom.size()-1;
	}
	

	public int donne_var_local(String nom) {
		for(int i=0;i<liste_local_nom.size();i++)
		{
			if(liste_local_nom.get(i).equals(nom))
			{
				return i;
			}
		}
		//liste_local_nom.add(nom);
		//liste_local_type.add(type);
		//return liste_local_nom.size()-1;
		return -1;
	}

	public int instr_get_field(CITypeSimple classe_appel,
			String nom_champs, CIType type) {
		Type type_classe;
		String nom_classe;
		nom_classe=env.nom_classe(classe_appel);
		//type_classe=new ObjectType(nom_classe);
		return ajoute_instr(factory.createGetField(
				nom_classe,nom_champs,conv_type(type)));
	}

	public int instr_invoke_virtual(String classe, String nom_methode,
			CIType type_retour, CIType[] parametres) {
		return invoke(classe, nom_methode, type_retour, parametres,Constants.INVOKEVIRTUAL);
	}
	

	public boolean verification() {
		org.apache.bcel.Repository.clearCache();
		List liste;
		liste=liste_classes;
		if(liste!=null)
		{
			JavaClass j;
			int i;
			String nom_classe;
			int nb_erreur;
			int nb_warning;
			nb_erreur=0;
			nb_warning=0;
			System.out.println("Vérification des classes...");
			for(i=0;i<liste.size();i++)
			{
				j=(JavaClass)liste.get(i);
				org.apache.bcel.Repository.addClass(j);
			}
			for(i=0;i<liste.size();i++)
			{
				j=(JavaClass)liste.get(i);
				nom_classe=j.getClassName();
				Verifier v =VerifierFactory.getVerifier(nom_classe);
				VerificationResult vr;

				System.out.println("classe:"+nom_classe+"***************************");
				vr = v.doPass1();
				System.out.println("Pass 1:\n"+vr);

				vr = v.doPass2();
				System.out.println("Pass 2:\n"+vr);

				if (vr == VerificationResult.VR_OK){
					JavaClass jc = org.apache.bcel.Repository
					    .lookupClass(nom_classe);
					for (int k=0; k<jc.getMethods().length; k++){
						vr = v.doPass3a(k);
						if(vr.getStatus()!=VerificationResult.VERIFIED_OK)
						{
							nb_erreur++;
						}
						System.out.println("Pass 3a, method number "+k+" ['"+jc.getMethods()[k]+"']:\n"+vr);

						vr = v.doPass3b(k);
						System.out.println("Pass 3b, method number "+k+" ['"+jc.getMethods()[k]+"']:\n"+vr);
					}
				}
				else
				{
					nb_erreur++;
				}

				System.out.println("Warnings:");
				String[] warnings = v.getMessages();
				if (warnings.length == 0) System.out.println("<none>");
				else
				{
					for (int k=0; k<warnings.length; k++){
						System.out.println(warnings[k]);
					}
					nb_warning+=warnings.length;
				}

				System.out.println("\n");

				// avoid swapping.
		  	v.flush();
			}
			System.out.println("nb erreurs:"+nb_erreur);
			System.out.println("nb warning:"+nb_warning);
			return nb_erreur==0;
		}
		System.out.println("Fin de la vérification des classes");
		return true;
	}

	public void construit_main(CIClasse cl) {
		String nom_classe;
		ConstantPoolGen cp;
		InstructionList il;
		CIAttribut attr;
		int i;
		InstructionFactory factory;
		InstructionHandle start,end,handler_debut,handler_fin,fin;
		GOTO g1,g2;
		LocalVariableGen lg;
		ObjectType type_exception=new ObjectType("java.io.IOException");
		type_exception=new ObjectType("java.lang.Exception");
		nom_classe=env.nom_classe(cl);
		cp = class_gen.getConstantPool(); // cg creates constant pool
		il = new InstructionList();
		MethodGen  mg = new MethodGen(Constants.ACC_STATIC | Constants.ACC_PUBLIC, // access flags
			Type.VOID,               // return type
			new Type[] {             // argument types
				new ArrayType(Type.STRING, 1) },
			new String[] { "argv" }, // arg names
			"main", nom_classe,    // method, class
			il, cp);
		factory = new InstructionFactory(class_gen);
		start=il.append(new NOP());
		il.append(factory.createNew(nom_classe));
		il.append(new DUP());
		il.append(factory.createInvoke(nom_classe,
				"<init>",Type.VOID,new Type[0],Constants.INVOKESPECIAL));
		il.append(factory.createInvoke(nom_classe,"make",
			Type.VOID,new Type[0],Constants.INVOKEVIRTUAL));
		//il.append(new POP());
		end=il.append(new NOP());
		g1=new GOTO(null);
		il.append(g1);
		lg=mg.addLocalVariable("e",new ObjectType("Exception"),null,null);
		handler_debut=il.append(new NOP());
		il.append(InstructionFactory.createStore(type_exception,lg.getIndex()));
		il.append(factory.createGetStatic("java.lang.System","err",
			new ObjectType("java.io.PrintStream")));
		il.append(InstructionFactory.createLoad(type_exception,lg.getIndex()));
		//il.append(new SWAP());
		il.append(factory.createInvoke("java.io.PrintStream","println",
			Type.VOID,new ObjectType[]{
			new ObjectType("java.lang.Object")},Constants.INVOKEVIRTUAL));
		handler_fin=il.append(new NOP());
		lg.setStart(handler_debut);
		lg.setEnd(handler_fin);
		mg.addExceptionHandler(start,end,handler_debut,type_exception);
		g2=new GOTO(null);
		il.append(g2);
		fin=il.append(InstructionConstants.RETURN);
		g1.setTarget(fin);
		g2.setTarget(fin);
		mg.setMaxStack();
		class_gen.addMethod(mg.getMethod());
		il.dispose();
	}

	public int instr_invoke_interface(String classe, String nom_methode, CIType type_retour, CIType[] parametres) {
		return invoke(classe, nom_methode, type_retour, parametres,Constants.INVOKEINTERFACE);
	}

	public int instr_load(CIType type, int no) {
		ajoute_instr(InstructionFactory.createLoad(env.conv_type(type),no));
		return liste_instr.getLength()-1;
	}

	public int instr_return(CIType type) {
		return ajoute_instr(env.conv_retour(type));
	}

	public void set_cible(int source, int cible) {
		Instruction tab[];
		BranchInstruction goto1;
		InstructionHandle cible1;
		assert(source>=0);
		assert(cible>=0);
		//tab=liste_instr.getInstructions();
		assert(instr!=null);
		assert(source<instr.size()):source+">="+instr.size();
		assert(cible<instr_handle.size()):cible+">="+instr_handle.size();
		//goto1=(BranchInstruction) instr_handle.get(source);
		goto1=(BranchInstruction) instr.get(source);
		//cible1=liste_instr.getInstructionHandles()[cible];
		cible1= (InstructionHandle) instr_handle.get(cible);
		goto1.setTarget(cible1);
	}

	public int instr_expr(int op) {
		switch(op)
		{
		case IADD:
			return ajoute_instr(InstructionConstants.IADD);
		case FADD:
			return ajoute_instr(InstructionConstants.FADD);
		case DADD:
			return ajoute_instr(InstructionConstants.DADD);
		case ISUB:
			return ajoute_instr(InstructionConstants.ISUB);
		case FSUB:
			return ajoute_instr(InstructionConstants.FSUB);
		case DSUB:
			return ajoute_instr(InstructionConstants.DSUB);
		case IMUL:
			return ajoute_instr(InstructionConstants.IMUL);
		case FMUL:
			return ajoute_instr(InstructionConstants.FMUL);
		case DMUL:
			return ajoute_instr(InstructionConstants.DMUL);
		case IDIV:
			return ajoute_instr(InstructionConstants.IDIV);
		case FDIV:
			return ajoute_instr(InstructionConstants.FDIV);
		case F2I:
			return ajoute_instr(InstructionConstants.F2I);
		case DDIV:
			return ajoute_instr(InstructionConstants.DDIV);
		case D2I:
			return ajoute_instr(InstructionConstants.D2I);
		case IREM:
			return ajoute_instr(InstructionConstants.IREM);
		case ICONST_0:
			return ajoute_instr(InstructionConstants.ICONST_0);
		case ICONST_1:
			return ajoute_instr(InstructionConstants.ICONST_1);
		case FCMPG:
			return ajoute_instr(InstructionConstants.FCMPG);
		case DCMPG:
			return ajoute_instr(InstructionConstants.DCMPG);
		case IF_ICMPEQ:
			return ajoute_instr(new IF_ICMPEQ(null));
		case IF_ICMPGE:
			return ajoute_instr(new IF_ICMPGE(null));
		case IF_ICMPGT:
			return ajoute_instr(new IF_ICMPGT(null));
		case IF_ICMPLE:
			return ajoute_instr(new IF_ICMPLE(null));
		case IF_ICMPNE:
			return ajoute_instr(new IF_ICMPNE(null));
		case IAND:
			return ajoute_instr(InstructionConstants.IAND);
		case IOR:
			return ajoute_instr(InstructionConstants.IOR);
		case IXOR:
			return ajoute_instr(InstructionConstants.IXOR);
		case I2D:
			return ajoute_instr(InstructionConstants.I2D);
		case INEG:
			return ajoute_instr(InstructionConstants.INEG);
		case I2F:
			return ajoute_instr(InstructionConstants.I2F);
		case F2D:
			return ajoute_instr(InstructionConstants.F2D);
		case I2C:
			return ajoute_instr(InstructionConstants.I2C);
		case IFEQ:
			return ajoute_instr(new IFEQ(null));
		case IF_ACMPNE:
			return ajoute_instr(new IF_ACMPNE(null));
		case IFNE:
			return ajoute_instr(new IFNE(null));
		case DUP:
			return ajoute_instr(InstructionConstants.DUP);
		case RETURN:
			return ajoute_instr(InstructionConstants.RETURN);
		case ARETURN:
			return ajoute_instr(InstructionConstants.ARETURN);
		case PUSHTHIS:
			return ajoute_instr(InstructionConstants.THIS);
		case ACONSTNULL:
			return ajoute_instr(InstructionConstants.ACONST_NULL);
		case NOP:
			return ajoute_instr(InstructionConstants.NOP);
		case GOTO:
			return ajoute_instr(new GOTO(null));
		}
		return -1;
	}

	public int instr_dconst(double d) {
		// factory.createConstant(new Double(e.dbl))
		return ajoute_instr(factory.createConstant(new Double(d)));
	}

	public int instr_dload(int var_local) {
		return ajoute_instr(new DLOAD(var_local));
	}

	public int instr_fconst(float f) {
		return ajoute_instr(factory.createConstant(new Float(f)));
	}

	public int instr_fload(int var_local) {
		return ajoute_instr(new FLOAD(var_local));
	}

	public int instr_iconst(int i) {
		return ajoute_instr(factory.createConstant(new Integer(i)));
	}

	public int instr_iload(int var_local) {
		return ajoute_instr(new ILOAD(var_local));
	}

	public int instr_sconst(String s) {
		return ajoute_instr(factory.createConstant(s));
	}

	public int instr_cast(CIType src, CIType dest) {
		return ajoute_instr(factory.createCast(conv_type(src),conv_type(dest)));
	}

	public int instr_instanceof(CIType t) {
		return ajoute_instr(factory.createInstanceOf(
				(ObjectType)(env.conv_type(t))));
	}

	public boolean contient_attribut(String nom_attribut) {
		Field f,tab[];
		tab=class_gen.getFields();
		if(tab!=null)
		{
			for(int i=0;i<tab.length;i++)
			{
				if(tab[i].getName().equals(nom_attribut))
					return true;
			}
		}
		return false;
	}

	public int instr_store(CIType t, int no_local) {
		return ajoute_instr(InstructionFactory.createStore(env.conv_type(t),no_local)); 
	}

	public int instr_invoke_static(String classe, String nom_methode, CIType type_retour, CIType[] parametres) {
		return invoke(classe, nom_methode, type_retour, parametres,Constants.INVOKESTATIC);
	}

	public String donne_type(String nom_attribut) {
		Field f,tab[];
		tab=class_gen.getFields();
		if(tab!=null)
		{
			for(int i=0;i<tab.length;i++)
			{
				if(tab[i].getName().equals(nom_attribut))
					return ((ObjectType)tab[i].getType()).getClassName();
			}
		}
		return null;
	}


}
