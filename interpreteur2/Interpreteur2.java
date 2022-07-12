package tinyeiffel.interpreteur2;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

import tinyeiffel.intermediaire.CIAttribut;
import tinyeiffel.intermediaire.CIClasse;
import tinyeiffel.intermediaire.CICreation;
import tinyeiffel.intermediaire.CIExpr;
import tinyeiffel.intermediaire.CIExpr_Appel;
import tinyeiffel.intermediaire.CIExpr_Binaire;
import tinyeiffel.intermediaire.CIExpr_Bool;
import tinyeiffel.intermediaire.CIExpr_Char;
import tinyeiffel.intermediaire.CIExpr_Creation;
import tinyeiffel.intermediaire.CIExpr_Double;
import tinyeiffel.intermediaire.CIExpr_Entier;
import tinyeiffel.intermediaire.CIExpr_Extern;
import tinyeiffel.intermediaire.CIExpr_Real;
import tinyeiffel.intermediaire.CIExpr_Scalaire;
import tinyeiffel.intermediaire.CIExpr_String;
import tinyeiffel.intermediaire.CIExpr_Type;
import tinyeiffel.intermediaire.CIExpr_Unaire;
import tinyeiffel.intermediaire.CIExpr_Var;
import tinyeiffel.intermediaire.CIExpr_Var_Syst;
import tinyeiffel.intermediaire.CIExpr_Void;
import tinyeiffel.intermediaire.CIInstruction;
import tinyeiffel.intermediaire.CIInstruction_Affect;
import tinyeiffel.intermediaire.CIInstruction_Appel;
import tinyeiffel.intermediaire.CIInstruction_Extern;
import tinyeiffel.intermediaire.CIInstruction_Goto;
import tinyeiffel.intermediaire.CIInstruction_Label;
import tinyeiffel.intermediaire.CIInstruction_Raise;
import tinyeiffel.intermediaire.CIInstruction_Return;
import tinyeiffel.intermediaire.CIInstruction_Si;
import tinyeiffel.intermediaire.CIInstruction_Si_Non;
import tinyeiffel.intermediaire.CIListe_Instr;
import tinyeiffel.intermediaire.CINom_Attribut;
import tinyeiffel.intermediaire.CIProgramme;
import tinyeiffel.intermediaire.CIType;
import tinyeiffel.intermediaire.CITypeSimple;

public class Interpreteur2 {

	public Interpreteur2()
	{
		out=System.out;
		err=System.out;
		in=System.in;
		res_trace=new StringBuffer();
	}
	
	public void execution(CIProgramme p, Logger logger,String classpath[])
	{
		CIClasse cl;
		CINom_Attribut methode_racine;
		CIAttribut attr;
		EObjet objet_racine;
		EFrame frame;
		programme=p;
		this.logger=logger;
		this.classpath=classpath;
		chargement_classpath();
		logger.info("Execution du code interprete");
		trace(TRACE_ACTIVE,"Debut de la trace");
		if(p.liste_classe!=null&&p.liste_classe.length>0)
		{
			cl=p.liste_classe[0];
			assert(cl!=null);
			assert(cl.liste_attribut!=null);
			assert(cl.liste_attribut.length>0);
			if(cl.creation!=null&&cl.creation.length>0)
			{
				CICreation creation;
				creation=cl.creation[0];
				assert(creation!=null);
				assert(creation.nom!=null);
				assert(creation.nom.length>0);
				methode_racine=creation.nom[0];
				objet_racine=creation_objet(cl.nom,methode_racine,null,null);
			}
			else
			{
				assert(false);
				//methode_racine=cl.liste_attribut[0].nom;
				//objet_racine=creation_objet(cl.nom,null,null);
			}
			//assert(methode_racine!=null);
		}
		else
		{
			trace(TRACE_ACTIVE,"Il n'y a pas de classe");
		}
		trace(TRACE_ACTIVE,"Fin de la trace");
	}

	private void chargement_classpath() {
		URL chemins[];
		String s;
		if(classpath!=null&&classpath.length>0)
		{
			try {
				chemins=new URL[classpath.length];
				s="liste des classpath:\n";
				for(int i=0;i<classpath.length;i++)
				{
					chemins[i]=new URL("file://"+classpath[i]+"/");
					s+="\t"+classpath[i]+"\n";
				}
				//chemins[0]=new URL("file:/E:/projet/eiffel/test/interpretation/test2/");
				loader = new URLClassLoader(chemins/*,getClass().getClassLoader()*/);
				//loader.
				logger.info(s);
				/*chemins=loader.getURLs();
				s="";
				for(int i=0;i<chemins.length;i++)
				{
					if(i>0)
						s+=",";
					s+=chemins[i];
				}
				logger.info(s);
				loader.loadClass("Test1");
				logger.info("chargement réussi");*/
			} catch (MalformedURLException e) {
				e.printStackTrace();
				assert(false);
			}/* catch (ClassNotFoundException e) {
				e.printStackTrace();
				assert(false);
			}*/
		}
		else
		{
			logger.info("il n'y a pas de classpath");
		}
		//Class c = loader.loadClass("com.developpez.MaClasse");
	}

	private EObjet creation_objet(CITypeSimple nom, CINom_Attribut methode,
			EFrame frame,EObjet param[]) {
		EObjet o=null;
		assert(nom!=null);
		if(nom.est_special())
		{
			if(nom.nom.equalsIgnoreCase("$integer"))
			{
				o=new_integer(0);
			}
			else if(nom.nom.equalsIgnoreCase("$boolean"))
			{
				o=new_bool(false);
			}
			else if(nom.nom.equalsIgnoreCase("$string"))
			{
				o=new_string("");
			}
			else if(nom.nom.equalsIgnoreCase("$real"))
			{
				o=new_real(0.0f);
			}
			else if(nom.nom.equalsIgnoreCase("$double"))
			{
				o=new_double(0.0);
			}
			else if(nom.nom.equalsIgnoreCase("$character"))
			{
				o=new_character('\0');
			}
			else
			{
				assert(false);
			}
		}
		else
		{
			String n;
			EType t[];
			n=nom.nom;
			t=convType(nom.generique);
			o=new EObjetNormal(new EType(nom.expanded,n,t),null);
			if(methode!=null)
			{
				CIClasse cl;
				//EFrame frame2;
				//frame2=new EFrame(o,);
				cl=programme.donne_classe(nom);
				assert(cl!=null);
				appel(frame,o,methode,cl,null);
			}
		}
		return o;
	}

	private EFrame appel(EFrame frame, EObjet o, 
			CINom_Attribut methode, CIClasse cl,EObjet param[]) {
		EFrame frame_courante;
		CIAttribut attr;
		assert(cl!=null);
		assert(methode!=null);
		assert(o!=null);
		attr=cl.donne_attribut(methode);
		assert(attr!=null);
		// TODO: faire la recherche de la methode s'il y a héritage dans les ancetres
		// TODO: appeller la précondition et la post condition et l'invariant de classe
		assert(attr.routine!=null);
		frame_courante=new EFrame(o,cl,attr,frame,param);
		trace(TRACE_METHODE,"Entre methode "+cl.nom.nom+"."+methode.nom);
		instruction(frame_courante,attr.routine.liste_instruction);
		trace(TRACE_METHODE,"Sort methode "+cl.nom.nom+"."+methode.nom);
		return frame_courante;
	}

	private void instruction(EFrame frame, 
		CIListe_Instr liste_instruction) {
		int i;
		CIInstruction ins;
		boolean retour=false;
		//boolean raise=false;
		// TODO: géré les exceptions
		assert(frame!=null);
		assert(!frame.isException());
		if(liste_instruction!=null&&liste_instruction.size()>0)
		{
			for(i=0;i<liste_instruction.size();i++)
			{
				ins=liste_instruction.elt(i);
				trace(TRACE_INSTRUCTION,i+":"+ins.toString());
				if(ins instanceof CIInstruction_Affect)
				{
					CIInstruction_Affect instr=(CIInstruction_Affect)ins;
					CIExpr_Var e;
					e=instr.nom;
					if(e.src==null)
						frame.setVar(e.nom, evalue(frame, instr.expr));
					else
					{
						EObjetNormal o;
						assert(frame.getVar(e.src) instanceof EObjetNormal);
						o=(EObjetNormal) frame.getVar(e.src);
						assert(o!=null);
						o.setAttribut(e.nom, evalue(frame, instr.expr));
					}
				}
				else if(ins instanceof CIInstruction_Appel)
				{
					CIInstruction_Appel instr=(CIInstruction_Appel)ins;
					EObjetNormal o;
					EObjet param[];
					CIClasse cl;
					CIAttribut attr;
					if(instr.cible!=null)
					{
						CIType t;
						assert(instr.cible.src==null);
						o=(EObjetNormal) frame.getVar(instr.cible.nom);
						t=frame.getType(instr.cible.nom);
						cl=programme.donne_classe(t);
					}
					else
					{
						o=(EObjetNormal) frame.getObjet_courant();
						cl=frame.getClasse();
					}
					if(instr.param!=null&&instr.param.length>0)
					{
						param=new EObjet[instr.param.length];
						for(int j=0;j<param.length;j++)
						{
							param[j]=evalue(frame, instr.param[j]);
						}
					}
					else
					{
						param=null;
					}
					appel(frame,o,instr.nom,cl,param);
				}
				else if(ins instanceof CIInstruction_Extern)
				{
					CIInstruction_Extern instr=(CIInstruction_Extern)ins;
					String ext,s,classe,methode;
					Object param[];
					Object o;
					EObjet o2,o3;
					Class signature[];
					CIType t;
					Method m;
					Class classe2=null;
					assert(instr.langage!=null);
					assert(instr.nom!=null);
					appel_extern(frame,instr.cible,instr.nom.nom,instr.param,instr.langage);
					/*s=instr.langage.trim();
					try {
					if(s.toUpperCase().startsWith("JVM:"))
					{
						int pos;
						assert(instr.cible==null):"Type de methode non implementé";
						pos=s.indexOf(":",4);
						if(pos>=0)
						{
							classe=s.substring(4,pos);
						}
						else
						{
							classe=s.substring(4);
						}
						//classe2=Class.forName(classe);
						classe2=loader.loadClass(classe);
						assert(classe2!=null);
						assert(s.endsWith(":static")):"Type de methode non implementé";
						/*if(instr.opt!=null&&instr.opt.length()>0)
						{
							methode=instr.opt;
						}
						else
						{
							methode=frame.getAttr().nom.nom;
						}* /
						methode=instr.nom.nom;
						assert(methode!=null);
						if(instr.param!=null&&instr.param.length>0)
						{
							param=new Object[instr.param.length];
							signature=new Class[instr.param.length];
							for(int j=0;j<param.length;j++)
							{
								o2=evalue(frame, instr.param[j]);
								//t=frame.getType(nom)
								if(o2==null)
								{
									o=null;
									signature[j]=Object.class;
								}
								else if(o2 instanceof EInteger)
								{
									EInteger v=(EInteger) o2;
									o=new Integer(v.getValeur());
									signature[j]=Integer.TYPE;
								}
								else if(o2 instanceof EBool)
								{
									EBool v=(EBool) o2;
									o=new Boolean(v.getValeur());
									signature[j]=Boolean.TYPE;
								}
								else if(o2 instanceof EString)
								{
									EString v=(EString) o2;
									o=v.getValeur();
									signature[j]=String.class;
								}
								else if(o2 instanceof EReal)
								{
									EReal v=(EReal) o2;
									o=new Float(v.getValeur());
									signature[j]=Float.TYPE;
								}
								else if(o2 instanceof EDouble)
								{
									EDouble v=(EDouble) o2;
									o=new Double(v.getValeur());
									signature[j]=Double.TYPE;
								}
								else
								{
									o=null;
									assert(false);
								}
								param[j]=o;
							}
						}
						else
						{
							param=null;
							signature=null;
						}
						m=classe2.getMethod(methode, signature);
						PrintStream out1,err1;
						out1=System.out;
						err1=System.err;
						System.setOut(out);
						System.setErr(err);
						m.invoke(null, param);
						System.setOut(out1);
						System.setErr(err1);
					}
					else
					{
						assert(false):instr.langage;
					}
					} catch (SecurityException e) {
						e.printStackTrace();
						assert(false);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						assert(false);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						assert(false);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						assert(false);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						assert(false);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
						assert(false);
					}*/
				}
				else if(ins instanceof CIInstruction_Goto)
				{
					CIInstruction_Goto instr=(CIInstruction_Goto)ins;
					int no;
					no=liste_instruction.getLabel(instr.nom);
					assert(no>=0);
					i=no-1;
				}
				else if(ins instanceof CIInstruction_Label)
				{
					CIInstruction_Label instr=(CIInstruction_Label)ins;
					int j;
					if(frame.isException())
					{
						for(j=0;j<i;j++)
						{
							if(liste_instruction.elt(i) instanceof CIInstruction_Label)
							{
								break;
							}
						}
						if(j==i)
						{// c'est une récuperation d'une exception
							frame.setNewTentative();
						}
					}
				}
				else if(ins instanceof CIInstruction_Raise)
				{
					CIInstruction_Raise instr=(CIInstruction_Raise)ins;
					EObjet o;
					int no;
					o=evalue(frame, instr.no);
					frame.setException(((EInteger)o).getValeur());
					no=liste_instruction.getLabel("debut_rescue");
					if(no!=-1)
					{// il y a un gestionnaire d'exception
						i=no-1;
					}
					else
					{// il n'y a pas de gestionnaire d'exception
						break;
					}
				}
				else if(ins instanceof CIInstruction_Return)
				{
					CIInstruction_Return instr=(CIInstruction_Return)ins;
					retour=true;
					break;
				}
				else if(ins instanceof CIInstruction_Si)
				{
					CIInstruction_Si instr=(CIInstruction_Si)ins;
					int no;
					EObjet o;
					o=evalue(frame,instr.expr);
					assert(o!=null);
					assert(o instanceof EBool);
					if(((EBool)o).getValeur())
					{
						no=liste_instruction.getLabel(instr.label);
						assert(no>=0);
						i=no-1;
					}
				}
				else if(ins instanceof CIInstruction_Si_Non)
				{
					CIInstruction_Si_Non instr=(CIInstruction_Si_Non)ins;
					int no;
					EObjet o;
					o=evalue(frame,instr.expr);
					assert(o!=null);
					assert(o instanceof EBool);
					if(!((EBool)o).getValeur())
					{
						no=liste_instruction.getLabel(instr.label);
						assert(no>=0);
						i=no-1;
					}
				}
				else
				{
					assert(false);
				}
			}
		}
		assert(retour||frame.isException());
	}

	private EObjet appel_extern(EFrame frame,CIExpr_Var var,String nom_methode,
		CIExpr_Scalaire parametres[],String langage)
	{
		
		String ext,s,classe,methode;
		Object param[];
		Object o;
		EObjet o2,o3;
		Class signature[];
		CIType t;
		Method m;
		EObjet res=null;
		Class classe2=null;
		assert(langage!=null);
		assert(nom_methode!=null);
		s=langage.trim();
		try {
		if(s.toUpperCase().startsWith("JVM:"))
		{
			int pos;
			assert(var==null):"Type de methode non implementé";
			pos=s.indexOf(":",4);
			if(pos>=0)
			{
				classe=s.substring(4,pos);
			}
			else
			{
				classe=s.substring(4);
			}
			//classe2=Class.forName(classe);
			classe2=loader.loadClass(classe);
			assert(classe2!=null);
			assert(s.endsWith(":static")):"Type de methode non implementé";
			/*if(instr.opt!=null&&instr.opt.length()>0)
			{
				methode=instr.opt;
			}
			else
			{
				methode=frame.getAttr().nom.nom;
			}*/
			methode=nom_methode;
			assert(methode!=null);
			if(parametres!=null&&parametres.length>0)
			{
				param=new Object[parametres.length];
				signature=new Class[parametres.length];
				for(int j=0;j<param.length;j++)
				{
					o2=evalue(frame, parametres[j]);
					//t=frame.getType(nom)
					if(o2==null)
					{
						o=null;
						signature[j]=Object.class;
					}
					else if(o2 instanceof EInteger)
					{
						EInteger v=(EInteger) o2;
						o=new Integer(v.getValeur());
						signature[j]=Integer.TYPE;
					}
					else if(o2 instanceof EBool)
					{
						EBool v=(EBool) o2;
						o=new Boolean(v.getValeur());
						signature[j]=Boolean.TYPE;
					}
					else if(o2 instanceof EString)
					{
						EString v=(EString) o2;
						o=v.getValeur();
						signature[j]=String.class;
					}
					else if(o2 instanceof EReal)
					{
						EReal v=(EReal) o2;
						o=new Float(v.getValeur());
						signature[j]=Float.TYPE;
					}
					else if(o2 instanceof EDouble)
					{
						EDouble v=(EDouble) o2;
						o=new Double(v.getValeur());
						signature[j]=Double.TYPE;
					}
					else if(o2 instanceof ECharacter)
					{
						ECharacter v=(ECharacter) o2;
						o=new Character(v.getValeur());
						signature[j]=Character.TYPE;
					}
					else
					{
						o=null;
						assert(false):"parametre no "+j+" invalide :"+o2;
					}
					param[j]=o;
				}
			}
			else
			{
				param=null;
				signature=null;
			}
			m=classe2.getMethod(methode, signature);
			PrintStream out1,err1;
			out1=System.out;
			err1=System.err;
			System.setOut(out);
			System.setErr(err);
			o=m.invoke(null, param);
			if(o==null)
			{
				res=null;
			}
			else if(o instanceof Integer)
			{
				res=new_integer(((Integer)o).intValue());
			}
			else if(o instanceof Boolean)
			{
				res=new_bool(((Boolean)o).booleanValue());
			}
			else if(o instanceof Float)
			{
				res=new_real(((Float)o).floatValue());
			}
			else if(o instanceof Double)
			{
				res=new_double(((Double)o).doubleValue());
			}
			else if(o instanceof Character)
			{
				res=new_character(((Character)o).charValue());
			}
			else if(o instanceof String)
			{
				res=new_string((String)o);
			}
			else
			{
				res=null;
				assert(false);
			}
			System.setOut(out1);
			System.setErr(err1);
		}
		else
		{
			assert(false):langage;
		}
		} catch (SecurityException e) {
			e.printStackTrace();
			assert(false);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			assert(false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			assert(false);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			assert(false);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			assert(false);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			assert(false);
		}
		return res;
	}
	
	private EObjet evalue(EFrame frame,CIExpr expr) {
		if(expr instanceof CIExpr_Appel)
		{
			CIExpr_Appel e=(CIExpr_Appel) expr;
			EObjetNormal o;
			EObjet param[];
			CIClasse cl;
			CIAttribut attr;
			EFrame f;
			if(e.cible!=null)
			{
				CIType t;
				assert(e.cible.src==null);
				o=(EObjetNormal) frame.getVar(e.cible.nom);
				t=frame.getType(e.cible.nom);
				cl=programme.donne_classe(t);
			}
			else
			{
				o=(EObjetNormal) frame.getObjet_courant();
				cl=frame.getClasse();
			}
			if(e.parametre!=null&&e.parametre.length>0)
			{
				param=new EObjet[e.parametre.length];
				for(int j=0;j<param.length;j++)
				{
					param[j]=evalue(frame, e.parametre[j]);
				}
			}
			else
			{
				param=null;
			}
			f=appel(frame,o,e.nom,cl,param);
			return f.getRetour();
		}
		else if(expr instanceof CIExpr_Binaire)
		{
			CIExpr_Binaire e=(CIExpr_Binaire) expr;
			EObjet e1,e2;
			EObjet o=null;
			assert(e.expr1!=null);
			e1=evalue(frame,e.expr1);
			assert(e1!=null);
			assert(e.expr2!=null);
			e2=evalue(frame,e.expr2);
			assert(e2!=null);
			if(e1 instanceof EBool)
			{
				boolean v1,v2,r=false;
				assert(e2 instanceof EBool);
				v1=((EBool)e1).getValeur();
				v2=((EBool)e2).getValeur();
				switch(e.op)
				{
				case CIExpr_Binaire.And:
				case CIExpr_Binaire.And_Then:
					r=v1&&v2;
					break;
				case CIExpr_Binaire.Or:
				case CIExpr_Binaire.Or_Else:
					r=v1||v2;
					break;
				case CIExpr_Binaire.Egal:
					r=v1==v2;
					break;
				case CIExpr_Binaire.Diff:
				case CIExpr_Binaire.Xor:
					r=v1!=v2;
					break;
				case CIExpr_Binaire.Implies:
					r=!v1||v1&&v2;
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
				o=new_bool(r);
			}
			else if(e1 instanceof ECharacter)
			{
				char v1,v2;
				boolean r=false;
				assert(e2 instanceof ECharacter);
				v1=((ECharacter)e1).getValeur();
				v2=((ECharacter)e2).getValeur();
				switch(e.op)
				{
				case CIExpr_Binaire.SupS:
					r=v1>v2;
					break;
				case CIExpr_Binaire.Sup:
					r=v1>=v2;
					break;
				case CIExpr_Binaire.Egal:
					r=v1==v2;
					break;
				case CIExpr_Binaire.Inf:
					r=v1<=v2;
					break;
				case CIExpr_Binaire.InfS:
					r=v1<v2;
					break;
				case CIExpr_Binaire.Diff:
					r=v1!=v2;
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
				o=new_bool(r);
			}
			else if(e1 instanceof EInteger)
			{
				switch(e.op)
				{
				case CIExpr_Binaire.SupS:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()>((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Sup:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()>=((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Egal:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()==((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Inf:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()<=((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.InfS:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()<((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Diff:
					assert(e2 instanceof EInteger);
					o=new_bool(((EInteger)e1).getValeur()!=((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Plus:
					assert(e2 instanceof EInteger);
					o=new_integer(((EInteger)e1).getValeur()+((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Moins:
					assert(e2 instanceof EInteger);
					o=new_integer(((EInteger)e1).getValeur()-((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Fois:
					assert(e2 instanceof EInteger);
					o=new_integer(((EInteger)e1).getValeur()*((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Div_entier:
					assert(e2 instanceof EInteger);
					o=new_integer(((EInteger)e1).getValeur()/((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Div_reel:
					assert(e2 instanceof EInteger);
					o=new_double((double)((EInteger)e1).getValeur()/(double)((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Mod:
					assert(e2 instanceof EInteger);
					o=new_integer(((EInteger)e1).getValeur()%((EInteger)e2).getValeur());
					break;
				case CIExpr_Binaire.Puiss:
					assert(e2 instanceof EInteger);
					o=new_integer((int)Math.pow(((EInteger)e1).getValeur(),((EInteger)e2).getValeur()));
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof EReal)
			{
				switch(e.op)
				{
				case CIExpr_Binaire.SupS:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()>((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Sup:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()>=((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Egal:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()==((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Inf:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()<=((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.InfS:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()<((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Diff:
					assert(e2 instanceof EReal);
					o=new_bool(((EReal)e1).getValeur()!=((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Plus:
					assert(e2 instanceof EReal);
					o=new_real(((EReal)e1).getValeur()+((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Moins:
					assert(e2 instanceof EReal);
					o=new_real(((EReal)e1).getValeur()-((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Fois:
					assert(e2 instanceof EReal);
					o=new_real(((EReal)e1).getValeur()*((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Div_reel:
					assert(e2 instanceof EReal);
					o=new_real(((EReal)e1).getValeur()/((EReal)e2).getValeur());
					break;
				case CIExpr_Binaire.Puiss:
					assert(e2 instanceof EReal);
					o=new_real((float)Math.pow(((EReal)e1).getValeur(),((EReal)e2).getValeur()));
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof EDouble)
			{
				switch(e.op)
				{
				case CIExpr_Binaire.SupS:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()>((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Sup:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()>=((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Egal:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()==((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Inf:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()<=((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.InfS:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()<((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Diff:
					assert(e2 instanceof EDouble);
					o=new_bool(((EDouble)e1).getValeur()!=((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Plus:
					assert(e2 instanceof EDouble);
					o=new_double(((EDouble)e1).getValeur()+((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Moins:
					assert(e2 instanceof EDouble);
					o=new_double(((EDouble)e1).getValeur()-((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Fois:
					assert(e2 instanceof EDouble);
					o=new_double(((EDouble)e1).getValeur()*((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Div_reel:
					assert(e2 instanceof EDouble);
					o=new_double(((EDouble)e1).getValeur()/((EDouble)e2).getValeur());
					break;
				case CIExpr_Binaire.Puiss:
					assert(e2 instanceof EDouble);
					o=new_double(Math.pow(((EDouble)e1).getValeur(),((EDouble)e2).getValeur()));
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else
			{
				assert(false):"types d'opérandes invalides";
			}
			return o;
		}
		else if(expr instanceof CIExpr_Bool)
		{
			CIExpr_Bool e=(CIExpr_Bool) expr;
			EBool o;
			o=new_bool(e.bool);
			return o;
		}
		else if(expr instanceof CIExpr_Char)
		{
			CIExpr_Char e=(CIExpr_Char) expr;
			ECharacter o;
			o=new_character(e.car);
			return o;
		}
		else if(expr instanceof CIExpr_Creation)
		{
			CIExpr_Creation e=(CIExpr_Creation) expr;
			EObjetNormal o;
			EObjet param[];
			CINom_Attribut nom;
			assert(e.appel!=null);
			assert(e.appel.cible==null);
			//assert(e.appel.)
			nom=e.appel.nom;
			if(e.appel.parametre==null||e.appel.parametre.length==0)
			{
				param=null;
			}
			else
			{
				param=new EObjet[e.appel.parametre.length];
				for(int i=0;i<e.appel.parametre.length;i++)
				{
					param[i]=evalue(frame, e.appel.parametre[i]);
				}
			}
			o=(EObjetNormal) creation_objet((CITypeSimple) e.type, nom, frame,param);
			return o;
		}
		else if(expr instanceof CIExpr_Double)
		{
			CIExpr_Double e=(CIExpr_Double) expr;
			EDouble o;
			o=new_double(e.dbl);
			return o;
		}
		else if(expr instanceof CIExpr_Entier)
		{
			CIExpr_Entier e=(CIExpr_Entier) expr;
			EInteger o;
			o=new_integer(e.entier);
			return o;
		}
		else if(expr instanceof CIExpr_Extern)
		{
			CIExpr_Extern e=(CIExpr_Extern) expr;
			String ext,s,classe,methode;
			Object param[];
			Object o,o5;
			EObjet o2,o3,o4;
			Class signature[];
			CIType t;
			Method m;
			Class classe2=null;
			assert(e.langage!=null);
			assert(e.nom!=null);
			o4=appel_extern(frame, e.cible, e.nom.nom, e.parametre, e.langage);
			/*s=e.langage.trim();
			try {
			if(s.toUpperCase().startsWith("JVM:"))
			{
				int pos;
				assert(e.cible==null):"Type de methode non implementé";
				pos=s.indexOf(":",4);
				if(pos>=0)
				{
					classe=s.substring(4,pos);
				}
				else
				{
					classe=s.substring(4);
				}
				//classe2=Class.forName(classe);
				classe2=loader.loadClass(classe);
				assert(classe2!=null);
				assert(s.endsWith(":static")):"Type de methode non implementé";
				/*if(instr.opt!=null&&instr.opt.length()>0)
				{
					methode=instr.opt;
				}
				else
				{
					methode=frame.getAttr().nom.nom;
				}* /
				methode=e.nom.nom;
				assert(methode!=null);
				if(e.parametre!=null&&e.parametre.length>0)
				{
					param=new Object[e.parametre.length];
					signature=new Class[e.parametre.length];
					for(int j=0;j<param.length;j++)
					{
						o2=evalue(frame, e.parametre[j]);
						//t=frame.getType(nom)
						if(o2==null)
						{
							o=null;
							signature[j]=Object.class;
						}
						else if(o2 instanceof EInteger)
						{
							EInteger v=(EInteger) o2;
							o=new Integer(v.getValeur());
							signature[j]=Integer.TYPE;
						}
						else if(o2 instanceof EBool)
						{
							EBool v=(EBool) o2;
							o=new Boolean(v.getValeur());
							signature[j]=Boolean.TYPE;
						}
						else if(o2 instanceof EString)
						{
							EString v=(EString) o2;
							o=v.getValeur();
							signature[j]=String.class;
						}
						else if(o2 instanceof EReal)
						{
							EReal v=(EReal) o2;
							o=new Float(v.getValeur());
							signature[j]=Float.TYPE;
						}
						else if(o2 instanceof EDouble)
						{
							EDouble v=(EDouble) o2;
							o=new Double(v.getValeur());
							signature[j]=Double.TYPE;
						}
						else
						{
							o=null;
							assert(false);
						}
						param[j]=o;
					}
				}
				else
				{
					param=null;
					signature=null;
				}
				m=classe2.getMethod(methode, signature);
				PrintStream out1,err1;
				out1=System.out;
				err1=System.err;
				System.setOut(out);
				System.setErr(this.err);
				o5=m.invoke(null, param);
				System.setOut(out1);
				System.setErr(err1);
				if(o5==null)
				{
					o4=null;
				}
				else if(o5 instanceof Integer)
				{
					o4=new_integer(((Integer)o5).intValue());
				}
				else if(o5 instanceof Boolean)
				{
					o4=new_bool(((Boolean)o5).booleanValue());
				}
				else if(o5 instanceof Float)
				{
					o4=new_real(((Float)o5).floatValue());
				}
				else if(o5 instanceof Double)
				{
					o4=new_double(((Double)o5).doubleValue());
				}
				else if(o5 instanceof Character)
				{
					o4=new_character(((Character)o5).charValue());
				}
				else if(o5 instanceof String)
				{
					o4=new_string((String)o5);
				}
				else
				{
					o4=null;
					assert(false);
				}
				return o4;
			}
			else
			{
				assert(false):e.langage;
			}
			} catch (SecurityException ex) {
				ex.printStackTrace();
				assert(false);
			} catch (NoSuchMethodException ex) {
				ex.printStackTrace();
				assert(false);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				assert(false);
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
				assert(false);
			} catch (InvocationTargetException ex) {
				ex.printStackTrace();
				assert(false);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				assert(false);
			}*/
			return o4;
		}
		else if(expr instanceof CIExpr_Real)
		{
			CIExpr_Real e=(CIExpr_Real) expr;
			EReal o;
			o=new_real((float)e.real);
			return o;
		}
		else if(expr instanceof CIExpr_String)
		{
			CIExpr_String e=(CIExpr_String) expr;
			EString o;
			o=new_string(e.str);
			return o;
		}
		else if(expr instanceof CIExpr_Unaire)
		{
			CIExpr_Unaire e=(CIExpr_Unaire) expr;
			EObjet e1;
			EObjet o=null;
			assert(e.expr!=null);
			e1=evalue(frame,e.expr);
			assert(e1!=null);
			if(e1 instanceof EBool)
			{
				boolean b;
				b=((EBool)e1).getValeur();
				switch(e.op)
				{
				case CIExpr_Unaire.Not:
					o=new_bool(!b);
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof ECharacter)
			{
				char c;
				c=((ECharacter)e1).getValeur();
				switch(e.op)
				{
				case CIExpr_Unaire.Conv_C2E:
					o=new_integer(c);
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof EInteger)
			{
				int n;
				n=((EInteger)e1).getValeur();
				switch(e.op)
				{
				case CIExpr_Unaire.Conv_E2C:
					o=new_character((char)n);
					break;
				case CIExpr_Unaire.Conv_E2D:
					o=new_double(n);
					break;
				case CIExpr_Unaire.Conv_E2R:
					o=new_real(n);
					break;
				case CIExpr_Unaire.Moins:
					o=new_integer(-n);
					break;
				case CIExpr_Unaire.Plus:
					o=new_integer(n);
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof EReal)
			{
				float f;
				f=((EReal)e1).getValeur();
				switch(e.op)
				{
				case CIExpr_Unaire.Conv_R2D:
					o=new_double(f);
					break;
				case CIExpr_Unaire.Conv_R2E:
					o=new_integer((int)f);
					break;
				case CIExpr_Unaire.Moins:
					o=new_real(-f);
					break;
				case CIExpr_Unaire.Plus:
					o=new_real(f);
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else if(e1 instanceof EDouble)
			{
				double d=((EDouble)e1).getValeur();
				switch(e.op)
				{
				case CIExpr_Unaire.Conv_D2E:
					o=new_integer((int) d);
					break;
				case CIExpr_Unaire.Conv_D2R:
					o=new_real((float)d);
					break;
				case CIExpr_Unaire.Moins:
					o=new_double(-d);
					break;
				case CIExpr_Unaire.Plus:
					o=new_double(d);
					break;
				default:
					assert(false):"Type d'operande invalide";
					break;
				}
			}
			else
			{// TODO : old,dollard ne sont pas implémenté
				assert(false):"Type d'opérande invalide";
			}
			return o;
		}
		else if(expr instanceof CIExpr_Type)
		{
			CIExpr_Type e=(CIExpr_Type) expr;
		}
		else if(expr instanceof CIExpr_Var)
		{
			CIExpr_Var e=(CIExpr_Var) expr;
			EObjet o;
			if(e.src==null)
				o=frame.getVar(e.nom);
			else
			{
				o=frame.getVar(e.src);
				assert(o instanceof EObjetNormal);
				EObjetNormal o2=(EObjetNormal) o;
				assert(o2.contient_attribut(e.nom));
				o=o2.getAttribut(e.nom);
			}
			return o;
		}
		else if(expr instanceof CIExpr_Var_Syst)
		{
			CIExpr_Var_Syst e=(CIExpr_Var_Syst) expr;
			assert(false);
		}
		else if(expr instanceof CIExpr_Void)
		{
			CIExpr_Void e=(CIExpr_Void) expr;
			return null;
		}
		else
		{
			assert(false);
			return null;
		}
		return null;
	}

	private EInteger new_integer(int n)
	{
		return new EInteger(new EType(true,"$integer",null),n);
	}

	private ECharacter new_character(char c)
	{
		return new ECharacter(new EType(true,"$character",null),c);
	}

	private EBool new_bool(boolean b)
	{
		return new EBool(new EType(true,"$boolean",null),b);
	}

	private EReal new_real(float f)
	{
		return new EReal(new EType(true,"$real",null),f);
	}

	private EDouble new_double(double d)
	{
		return new EDouble(new EType(true,"$double",null),d);
	}

	private EString new_string(String s)
	{
		return new EString(new EType(true,"$string",null),s);
	}
	
	private EType[] convType(CIType[] generique) {
		EType[] res=null;
		if(generique!=null&&generique.length>0)
		{
			CITypeSimple t;
			EType[] tmp;
			res=new EType[generique.length];
			for(int i=0;i<generique.length;i++)
			{
				t=(CITypeSimple)generique[i];
				if(t.generique!=null&&t.generique.length>0)
				{
					tmp=convType(t.generique);
				}
				else
				{
					tmp=null;
				}
				res[i]=new EType(t.expanded,t.nom,tmp);
			}
		}
		return res;
	}

	private void trace(int niveau,String msg) {
		if(trace>=niveau)
		{
			res_trace.append(msg);
			if(!msg.endsWith("\n"))
				res_trace.append("\n");
			logger.info(msg);
		}
	}
	
	public boolean erreur()
	{
		return erreur;
	}
	
	public PrintStream getOut()
	{
		return out;
	}

	public void setOut(PrintStream out)
	{
		assert(out!=null);
		this.out=out;
	}
	
	public PrintStream getErr()
	{
		return err;
	}
	
	public void setErr(PrintStream err)
	{
		this.err=err;
	}
	
	public InputStream getIn()
	{
		return in;
	}
	
	public void setIn(InputStream in)
	{
		this.in=in;
	}
	
	public void setTrace(int trace)
	{
		assert(trace>=DEBUT);
		assert(trace<=FIN);
		this.trace=trace;
	}
	
	public String getTrace()
	{
		return res_trace.toString();
	}
	
	private PrintStream out;
	private PrintStream err;
	private InputStream in;
	private boolean erreur;
	private int trace=AUCUNE_TRACE;
	public static final int AUCUNE_TRACE=0,TRACE_METHODE=1,TRACE_INSTRUCTION=2;
	private static final int DEBUT=AUCUNE_TRACE,FIN=TRACE_INSTRUCTION,TRACE_ACTIVE=TRACE_METHODE;
	private StringBuffer res_trace;
	private CIProgramme programme;
	private Logger logger;
	private String classpath[];
	private URLClassLoader loader;
}
