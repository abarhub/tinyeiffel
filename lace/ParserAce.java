/*
 * Created on 4 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.lace;

import java.io.*;
import java.util.*;

import tinyeiffel.ast.*;
import tinyeiffel.erreur.*;
import tinyeiffel.compiler.*;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParserAce extends AbstractParser {

	private String repertoire_racine;
	
	/**
	 * 
	 */
	public ParserAce() {
		
	}

	public ParserAce(Lexer l)
	{
		assert(l!=null);
		type_utilisee=new Vector();
		lexer=l;
	}
	
	public ParserAce(String nom) throws FileNotFoundException
	{
		File f;
		type_utilisee=new Vector();
		lexer=new Lexer();
		f=new File(nom);
		if(f.exists())
		{
			repertoire_racine=f.getParentFile().getAbsolutePath();
		}
		else
		{
			//f=new File(nom);
			assert(false):"Impossible de trouver le fichier "+nom;
		}
		lexer.ouvre(nom);
	}
	
	public void ajoute_type(Type t)
	{
		int j;

		if(t!=null&&!((Type)t).is_like)
		{
			for(j=0;j<type_utilisee.size();j++)
			{
				if(((Type)type_utilisee.elementAt(j)).egaux(t))
				{
					return;
				}
			}
			type_utilisee.addElement(t);
		}
	}
	public void ajoute_type(Vector t)
	{
		int i,j;
		boolean trouve;

		if(t!=null)
		{
			for(i=0;i<t.size();i++)
			{
				if(!((Type)t.elementAt(i)).is_like)
				{
					trouve=false;
					for(j=0;!trouve&&j<type_utilisee.size();j++)
					{
						if(((Type)type_utilisee.elementAt(j)).egaux(
							((Type)t.elementAt(i))))
						{
								trouve=true;
						}
					}
					if(!trouve)
					{
						ajoute_type((Type)t.elementAt(i));
					}
				}
			}
		}
	}
	
	/*************************************************/
	
	/********************** parser ***********************/

	public void parse()
	{
		assert(etatOk());
		//lexer.parcourt();
		parse_ace();
	}
	
	public Ace parse_ace()
	{
		NomSysteme sys=null;
		Type nom;
		NomGrappe nomg=null;
		NomFeature nomp=null;
		Vector liste_grappe=null;
		Ace res=null;
		Token t_system,t_nom_system,t_root;
		boolean erreur=false;
		Type type_racine;
		Map extern,generation;
		t_system=lexer.lit2();
		if(t_system==null||t_system.get_type_token()!=TK_SYSTEM)
		{
			erreur_parse("Il faut un system");
			erreur=true;
			assert(false);
		}
		else
		{
			t_nom_system=lexer.lit(ID);
			if(t_nom_system==null)
			{
				erreur_parse("Il faut un nom de system");
				erreur=true;
				assert(false);
			}
			else
			{
				sys=new NomSysteme(t_nom_system.text,t_nom_system);
				t_root=lexer.lit(TK_ROOT);
				if(t_root==null)
				{
					erreur_parse("Il faut une classe racine");
					erreur=true;
					assert(false);
				}
				else
				{
					nom=parse_type();
					if(nom==null)
					{
						erreur=true;
					}
					else
					{
						if(lexer.lit(ParentO)!=null)
						{
							nomg=parse_nom_grappe();
							if(nomg==null)
							{
								erreur_parse("Il faut un nom de grappe racine");
								erreur=true;
								assert(false);
							}
							else
							{
								if(lexer.lit(ParentF)==null)
								{
									erreur_parse("Il faut une parenthese fermante");
									erreur=true;
									assert(false);
								}
							}
						}
						else
						{
							nomg=null;
						}
						if(lexer.lit(DoublePoint)!=null)
						{
							nomp=parse_nom_feature();
							if(nomp==null)
							{
								erreur_parse("Il faut un nom de feature");
								erreur=true;
								assert(false);
							}
						}
						parse_default();
						liste_grappe=parse_grappe();
						extern=parse_extern();
						generation=parse_generation();
						res=new Ace(sys,nom,nomg,nomp,
								liste_grappe,extern,generation,
								this.repertoire_racine);
						if(res.extern_kernel()==null)
						{
							erreur_parse("le fichier kernel n'est pas renseigné");
							erreur=true;
							assert(false);
						}
						else
						{
							File f;
							f=new File(res.extern_kernel());
							if(!f.exists())
							{
								erreur_parse("le fichier kernel \""+res.extern_kernel()+"\" n'existe pas");
								erreur=true;
								assert(false):"le fichier kernel \""+res.extern_kernel()+"\" n'existe pas";
							}
						}
					}
				}
			}
		}
		return res;
	}
	
		
	/**
	 * 
	 */
	private Map parse_generation() {
		Token t_default;
		Map res=new HashMap();
		t_default=lexer.lit(TK_GENERATE);
		return res;
	}

	/**
	 * 
	 */
	private Map parse_extern() {
		Token t_default,ident,text;
		Map res=new HashMap();
		int suivant[]={TK_GENERATE,TK_END};
		int suivant2[]={ParentO,DoublePoint};
		int i,n;
		Chaine ch;
		String s,id;
		Vector v;
		t_default=lexer.lit(TK_EXTERNAL);
		if(t_default!=null)
		{
			while(!lexer.test_prochain(suivant))
			{
				ident=lexer.lit(ID);
				if(ident!=null)
				{
					id=ident.text;
					n=lexer.test_prochain3(suivant2);
					if(n==0)
					{// parentaise
						lexer.lit(ParentO);
						text=lexer.lit(ID);
						if(text==null)
						{
							erreur_parse("Il faut un identifiant");
							break;
						}
						else
						{
							if(lexer.lit(ParentF)==null)
							{
								erreur_parse("Il faut une parentaise fermante");
								break;
							}
							else
							{
								if(res.containsKey(id))
								{
									v=(Vector)res.get(id);
								}
								else
								{
									v=new Vector();
									res.put(id,v);
								}
								v.add(text.text);
							}
						}
					}
					else if(n==1)
					{// :
						lexer.lit(DoublePoint);
						ch=parse_chaine();
						if(ch!=null)
						{
							//s=ch.chaine_unique();
							s=ch.simplifie_chaine();
						}
						else
						{
							s="";
						}
						assert(!res.containsKey(id));
						res.put(id,s);
					}
					lexer.lit(PointVirgule);
				}
				else
				{
					erreur_parse("Il faut un identifiant");
					break;
				}
			}
		}
		return res;
	}

	/**
	 * @return une liste de cluster
	 */
	private Vector parse_grappe() {
		Token t_cluster;
		Vector res;
		Chaine s,s2;
		NomGrappe n;
		Grappe g;
		boolean fin=false;
		t_cluster=lexer.lit(TK_CLUSTER);
		res=new Vector();
		n=null;
		while(!fin)
		{
			s=parse_chaine();
			if(s==null)
			{
				fin=true;
			}
			else
			{
				if(lexer.lit(DoublePoint)!=null)
				{
					s2=parse_chaine();
					if(s2==null)
					{
						fin=true;
					}
					else
					{
						n=new NomGrappe(s);
						g=new Grappe(n,s2);
						res.addElement(g);
					}
				}
				else
				{
					g=new Grappe(n,s);
					res.addElement(g);
				}
			}
			lexer.lit(PointVirgule);
		}
		return res;
	}

	/**
	 * 
	 */
	private void parse_default() {
		Token t_default;
		t_default=lexer.lit(TK_DEFAULT);
	}

	/**
	 * @return
	 */
	private NomGrappe parse_nom_grappe() {
		Chaine nom_grappe;
		NomGrappe nom;
		nom_grappe=parse_chaine();
		if(nom_grappe==null)
		{
			erreur_parse("Il faut un nom de grappe");
			nom=null;
		}
		else
		{
			nom=new NomGrappe(nom_grappe);
		}
		return nom;
	}

	// parse un type
	Type parse_type()
	{
		Token t_type,t_expand,t_like;
		Type type=null,type2,type3;
		boolean erreur=false,expand=false;
		Token crochetO=null,crochetF=null,vers;
		t_expand=lexer.lit(TK_EXPANDED);
		if(t_expand!=null)
		{
			expand=true;
		}
		t_type=lexer.lit(ID);
		if(t_type==null)
		{
			erreur_parse("Il faut un type");
			erreur=true;
		}
		else
		{
			Token virgule;
			Vector param=new Vector();
			boolean fin=false;
			crochetO=lexer.lit(CrochetO);
			if(crochetO!=null)
			{
				crochetF=lexer.lit(CrochetF);
				if(crochetF==null)
				{
					int i=0;
					while(!fin)
					{
						//erreur_parse("no="+i);
						type2=parse_type();
						if(type2!=null)
						{
							param.addElement(type2);
							virgule=lexer.lit(Virgule);
							if(virgule==null)
							{
								fin=true;
							}
							else
							{
	
							}
						}
						else
						{
							fin=true;
						}
						i++;
					}
					crochetF=lexer.lit(CrochetF);
					if(crochetF==null)
					{
						erreur_parse("Il faut ]");
						erreur=true;
					}
				}
			}
			type=new TypeSimple(expand,t_type.text,param);
			type.set_token(t_type,crochetO,crochetF);
			ajoute_type(type);
		}
		assert((type!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans le type");
		}
		return type;
	}
	/***************************************************************/
	/*public Vector type_utilisee;
	public Logging  logging;
    //public String nom_fichier;

	public tinyeiffel.ast.Token remplitToken(Token n)
	{
                //assert(n!=null);
		if(n==null)
			return null;
		return new tinyeiffel.ast.Token(n.getLine(),n.getColumn(),n.getText(),getFilename());
	}*/
	
	//public AceLexer lexer=null;
	//public int last_feature1=-1,last_feature2=-1;

	/*public Vector type_utilisee=new Vector();
	public Logging logging;
	protected Lexer lexer;*/
	
}
