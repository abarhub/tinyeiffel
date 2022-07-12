package tinyeiffel.compiler;

import java.io.*;
import tinyeiffel.ast.*;
import java.util.*;

public class Lexer extends Lexer_Constantes
{
	public Lexer()
	{
		etatOk=true;
	}

	/*public Lexer(FileInputStream f)
	{
		etatOk=true;
		contenu="";
		nom_fichier=nom;
		init_lexer();
		try{
			BufferedReader entree=new BufferedReader(new FileReader(nom));
			String line;
			do
			{
				line=entree.readLine();
				if(line!=null)
				{
					contenu+=line+"\n";
				}
			} while(line!=null);
			pos=0;
			pos_dernier=0;
			liste_dernier_token=new Token[10];
			nb_token_lu=0;
		} catch(IOException e)
		{
			etatOk=false;
			System.out.println("Exception IO:"+e);
			//System.
			System.exit(0);
		}
	}*/

		/********************* lexer **************************/

	public String str_debug()
	{
		int i;
		String res="etatOk="+etatOk+"\n";
		res+="pos_dernier="+pos_dernier+",nb_token_lu="+nb_token_lu+"\n";
		if(liste_dernier_token!=null)
		{
			for(i=0;i<liste_dernier_token.length;i++)
			{
				res+="token["+i+"]="+toStringToken(liste_dernier_token[i])+"\n";
			}
		}
		return res;
	}

	public void ouvre(String nom) throws FileNotFoundException
	{
		contenu="";
		nom_fichier=nom;
		init_lexer();
		//try{
		BufferedReader entree=new BufferedReader(new FileReader(nom));
		String line;
		try{
			do
			{
				line=entree.readLine();
				if(line!=null)
				{
					contenu+=line+"\n";
				}
			} while(line!=null);
			pos=0;
			pos_dernier=0;
			liste_dernier_token=new Token[10];
			nb_token_lu=0;
		} catch(IOException e)
		{
			etatOk=false;
			System.out.println("Exception IO:"+e);
			//System.
			System.exit(0);
		}
	}

	public boolean test_prochain(int tab[])
	{
		int i;
		Token t=null;
		assert(tab!=null);
		assert(tab.length>0);
		if(nb_token_lu>pos_dernier)
		{// on a deja lu au moins un token
			t=liste_dernier_token[pos_dernier];
		}
		else
		{
			//suivant_deja_lu=true;
			//t=getToken();
			if(prefreshNbToken(1))
				t=liste_dernier_token[pos_dernier];
		}
		if(t!=null)
		{
			for(i=0;i<tab.length;i++)
			{
				if(tab[i]==t.get_type_token())
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean test_prochain2(int tab[],int tab2[])
	{
		int i,j;
		Token t=null,t2=null;
		assert(tab!=null);
		assert(tab.length>0);
		if(nb_token_lu>pos_dernier+1)
		{// on a deja lu au moins 2 tokens
			t=liste_dernier_token[pos_dernier];
			t2=liste_dernier_token[pos_dernier+1];
		}
		else
		{
			//suivant_deja_lu=true;
			//t=getToken();
			if(prefreshNbToken(2))
			{
				t=liste_dernier_token[pos_dernier];
				t2=liste_dernier_token[pos_dernier+1];
			}
		}
		if(t!=null&&t2!=null)
		{
			int oldi=0,oldj;
			for(i=0;i<tab.length;i++)
			{
				//System.out.println("i="+i);
				assert(i==0||oldi+1==i);
				if(tab[i]==t.get_type_token())
				{
					oldj=0;
					for(j=0;j<tab2.length;j++)
					{
						//System.out.println("j="+j);
						assert(j==0||oldj+1==j);
						if(tab2[j]==t2.get_type_token())
						{
							return true;
						}
						oldj=j;
					}
				}
				oldi=i;
			}
		}
		return false;
	}

	public int test_prochain3(int tab[])
	{
		int i;
		Token t=null;
		assert(tab!=null);
		assert(tab.length>0);
		if(nb_token_lu>pos_dernier)
		{// on a deja lu au moins un token
			t=liste_dernier_token[pos_dernier];
		}
		else
		{
			//suivant_deja_lu=true;
			//t=getToken();
			if(prefreshNbToken(1))
				t=liste_dernier_token[pos_dernier];
		}
		if(t!=null)
		{
			for(i=0;i<tab.length;i++)
			{
				if(tab[i]==t.get_type_token())
				{
					return i;
				}
			}
		}
		return -1;
	}

	public int test_prochain4(int tab[][])
	{
		int i,nb,j;
		Token t=null;
		assert(tab!=null);
		assert(tab.length>0);
		// recherche du plus grand nombre de token a lire
		nb=0;
		for(i=0;i<tab.length;i++)
		{
			if(tab[i].length>nb)
				nb=tab[i].length;
		}
		if(nb_token_lu>pos_dernier+nb)
		{// on a deja lu au moins un token
			t=liste_dernier_token[pos_dernier];
		}
		else
		{
			//suivant_deja_lu=true;
			//t=getToken();
			if(prefreshNbToken(nb))
				t=liste_dernier_token[pos_dernier];
		}
		if(t!=null)
		{
			for(i=0;i<tab.length;i++)
			{
				if(tab[i][0]==t.get_type_token())
				{
					boolean ok=true;
					for(j=1;j<tab[i].length&&ok==true;j++)
					{
						if(tab[i][j]!=liste_dernier_token[pos_dernier+j].get_type_token())
						{
							ok=false;
						}
					}
					if(ok)
						return i;
				}
			}
		}
		return -1;
	}

	void undo()
	{
		assert(nb_token_lu>0);
		assert(pos_dernier>0);
		pos_dernier--;
	}

	public Token lit(int no)
	{
		Token t;
		boolean lecture_suivant;
		assert(no>0);
		if(nb_token_lu>pos_dernier)
		{// on a deja lu au moins un token
			t=liste_dernier_token[pos_dernier];
			lecture_suivant=false;
		}
		else
		{
			//suivant_deja_lu=true;
			prefreshNbToken(1);
			t=liste_dernier_token[pos_dernier];
			//lecture_suivant=true;
			/*t=getToken();
			assert(nb_token_lu==1);
			assert(pos_dernier==1):pos_dernier+":"+nb_token_lu;*/
		}
		//assert(nb_token_lu>pos_dernier);
		if(t!=null&&no==t.get_type_token())
		{
			//suivant_deja_lu=false;
			//dernier_token=null;
			pos_dernier++;
			return t;
		}
		else
		{
			//suivant_deja_lu=true;
			//dernier_token=t;
			/*if(t!=null&&lecture_suivant&&pos_dernier>0)
			{
				pos_dernier--;
			}*/
			return null;
		}
	}

	public Token lit2()
	{
		if(nb_token_lu>pos_dernier)
		{
			Token t;
			t=liste_dernier_token[pos_dernier];
			//suivant_deja_lu=false;
			//dernier_token=null;
			pos_dernier++;
			return t;
		}
		else
		{
			//suivant_deja_lu=true;
			/*Token t;
			prefreshNbToken(1);
			t=liste_dernier_token[pos_dernier];
			//suivant_deja_lu=false;
			//dernier_token=null;
			pos_dernier++;
			return t;*/
			return getToken();
		}
	}

	void parcourt()
	{
		Token r=null;
		int i=0;
		do
		{
			if(i%3==0)
				prefreshNbToken(2);
			r=getToken();
			if(r!=null)
			{
				System.out.println("token="+r.text+"{"+r.get_type_token()+"}"+r.debut());
			}
			i++;
		}
		while(r!=null);
	}

	String toStringToken(Token r)
	{
		if(r==null)
			return "(null)";
		else
			return "token="+r.text+"{"+r.get_type_token()+"}"+r.debut();
	}

	Token getToken()
	{
		Token res;
		if(nb_token_lu>pos_dernier)
		{// on a deja lu au moins un token
			res=liste_dernier_token[pos_dernier];
		}
		else
		{
			res=getNext();
			pos_dernier=0;
			nb_token_lu=1;
			liste_dernier_token[pos_dernier]=res;
		}
		pos_dernier++;
		return res;
	}

	boolean prefreshNbToken(int nb)
	{
		boolean res;
		if(nb_token_lu>pos_dernier+nb)
		{// on a deja lu au moins nb token => on fait rien
			res=true;
		}
		else
		{
			Token t;
			int i=0;
			res=true;
			if(nb_token_lu==pos_dernier)
			{// reinitialisation pour commencer au debut du tableau
				nb_token_lu=0;
				pos_dernier=0;
			}
			do
			{// TODO: prendre en compte si on depasse le tableau
				t=getNext();
				if(t!=null)
				{
					if(nb_token_lu+i>=liste_dernier_token.length)
					{// on augemnte la taille de la zone tampon
						Token tmp[];
						int j,nb2;
						nb2=liste_dernier_token.length;
						tmp=new Token[nb2*nb2];
						for(j=0;j<nb2;j++)
						{
							tmp[j]=liste_dernier_token[j];
						}
						liste_dernier_token=tmp;
						assert(nb_token_lu+i<liste_dernier_token.length);
					}
					liste_dernier_token[nb_token_lu+i]=t;
					i++;
				}
				else
				{// impossible de precharger nb token
					res=false;
				}
			}
			while(t!=null&&nb_token_lu+i<pos_dernier+nb);
			nb_token_lu+=i;
		}
		return res;
	}

	protected Token getNext()
	{
		int last=-1,i;
		char c;
		boolean trouve;
		String s,s2;
		Token res=null;
		assert(contenu!=null);
		assert(pos>=0);
		while(res==null&&pos<contenu.length())
		{
			last=pos;
			c=contenu.charAt(pos);
			switch(c)
			{
				case ' ':
				case '\t':
					pos++;
					break;
				case '\n':
					pos++;
					x=0;
					y++;
					break;
				case ';':
					res=defaut_token(";",PointVirgule);
					pos++;
					break;
				case '.':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='.')
					{
						res=defaut_token("..",DeuxPoint);
						pos++;
					}
					else if(pos+1<contenu.length()&&
						(Character.isDigit(contenu.charAt(pos+1))||
						contenu.charAt(pos+1)=='_'))
					{
						int deb=pos;
						c=contenu.charAt(deb);
						assert(c=='.');
						deb++;
						for(;deb<contenu.length();deb++)
						{
							c=contenu.charAt(deb);
							if(!(Character.isDigit(c)||c=='_'))
							{
								break;
							}
						}
						c=contenu.charAt(deb);
						if(c=='e')
						{// prendre en compte les + et -
							deb++;
							for(;deb<contenu.length();deb++)
							{
								c=contenu.charAt(deb);
								if(!(Character.isDigit(c)||c=='_'))
								{
									break;
								}
							}
						}
						s=contenu.substring(pos,deb);
						res=defaut_token(s,REAL);
						//System.out.println("t="+s);
						pos+=deb-pos;
					}
					else
						res=defaut_token(".",Point);
					pos++;
					break;
				case '(':
					res=defaut_token("(",ParentO);
					pos++;
					break;
				case ')':
					res=defaut_token(")",ParentF);
					pos++;
					break;
				case '[':
					res=defaut_token("[",CrochetO);
					pos++;
					break;
				case ']':
					res=defaut_token("]",CrochetF);
					pos++;
					break;
				case '+':
					res=defaut_token("+",Plus);
					pos++;
					break;
				case '-':
					//System.out.println("trouve");
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='>')
					{
						res=defaut_token("->",Fleche);
						pos+=2;
						//System.out.println("trouve2");
					}
					else if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='-')
					{
						i=1;
						while(pos+i<contenu.length()&&contenu.charAt(pos+i)!='\n')
							i++;
						//res=defaut_token("->",Fleche);
						pos+=i;
					}
					else
					{
						res=defaut_token("-",Moins);
						pos++;
					}
					//System.out.println("trouve3");
					break;
				case '*':
					res=defaut_token("*",Fois);
					pos++;
					break;
				case '/':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='/')
					{
						res=defaut_token("//",DivEntier);
						pos++;
					}
					else if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='=')
					{
						res=defaut_token("/=",Diff);
						pos++;
					}
					else
						res=defaut_token("/",Div);
					pos++;
					break;
				case '\\':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='\\')
					{
						res=defaut_token("\\\\",Mod);
						pos+=2;
					}
					else
					{// todo: a traiter
						assert(false);
						pos++;
					}
					break;
				case '=':
					//System.out.println("trouve4");
					res=defaut_token("=",Egal);
					pos++;
					break;
				case '<':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='<')
					{
						res=defaut_token("<<",ArrayO);
						pos++;
					}
					else if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='=')
					{
						res=defaut_token("<=",Inf);
						pos++;
					}
					else
						res=defaut_token("<",InfS);
					pos++;
					break;
				case '>':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='>')
					{
						res=defaut_token(">>",ArrayF);
						pos++;
					}
					else if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='=')
					{
						res=defaut_token(">=",Sup);
						pos++;
					}
					else
						res=defaut_token(">",SupS);
					pos++;
					break;
				case ',':
					res=defaut_token(",",Virgule);
					pos++;
					break;
				case '?':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='=')
					{
						res=defaut_token("?=",TentativeAffect);
						pos+=2;
					}
					else
					{// todo: a traiter
						assert(false);
						pos++;
					}
					break;
				case ':':
					if(pos+1<contenu.length()&&contenu.charAt(pos+1)=='=')
					{
						res=defaut_token(":=",Affect);
						pos++;
					}
					else
						res=defaut_token(":",DoublePoint);
					pos++;
					break;
				case '^':
					res=defaut_token("^",Puiss);
					pos++;
					break;
				case '!':
					res=defaut_token("!",Exclamation);
					pos++;
					break;
				case '{':
					res=defaut_token("{",AcoladeO);
					pos++;
					break;
				case '}':
					res=defaut_token("}",AcoladeF);
					pos++;
					break;
				case '$':
					res=defaut_token("$",Dollard);
					pos++;
					break;
				case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
				case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
				case 'o':case 'p':case 'q':case 'r':case 's':case 't':case 'u':
				case 'v':case 'w':case 'x':case 'y':case 'z':
				case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':case 'G':
				case 'H':case 'I':case 'J':case 'K':case 'L':case 'M':case 'N':
				case 'O':case 'P':case 'Q':case 'R':case 'S':case 'T':case 'U':
				case 'V':case 'W':case 'X':case 'Y':case 'Z':
					trouve=false;
					for(i=1;pos+i<contenu.length();i++)
					{
						c=contenu.charAt(pos+i);
						if(!(Character.isLetterOrDigit(c)||c=='_'))
						{
							break;
						}
					}
					s=contenu.substring(pos,pos+i);
					Integer n=(Integer)liste_keyword.get(s.toLowerCase());
					if(n!=null)
					{
						res=defaut_token(s,n.intValue());
					}
					else
					{
						res=defaut_token(s,ID);
					}
					//System.out.println("t="+s);
					pos+=i;
					break;
				case '\'':
					for(i=1;pos+i<contenu.length();i++)
					{
						c=contenu.charAt(pos+i);
						if(c=='\'')
						{
							break;
						}
						else if(c=='%')
						{
							if(pos+i+1<contenu.length())
							{
								i++;
							}
						}

					}
					i++;
					s=contenu.substring(pos,pos+i);
					res=defaut_token(s,CHAR);
					//System.out.println("t="+s);
					pos+=i;
					break;
				case '\"':case '%':
					for(i=1;pos+i<contenu.length();i++)
					{
						c=contenu.charAt(pos+i);
						if(c=='\"')
						{
							break;
						}
						else if(c=='%')
						{
							if(pos+i+1<contenu.length()&&
								(contenu.charAt(pos+i+1)=='\n'||
								contenu.charAt(pos+i+1)=='\t'||
								contenu.charAt(pos+i+1)=='\r'))
							{
								break;
							}
							else if(pos+i+1<contenu.length())
							{
								i++;
							}
						}
					}
					i++;
					s=contenu.substring(pos,pos+i);
					res=defaut_token(s,STRING);
					//System.out.println("t="+s);
					pos+=i;
					break;
				case '0':case '1':case '2':case '3':case '4':case '5':
				case '6':case '7':case '8':case '9'://case '.':
					// todo: prendre en compte le dépassement du buffer
					// todo:prendre en compte un debut avec . et + et -
					int deb=pos;
					boolean flottant=false;
					for(deb=pos;deb<contenu.length();deb++)
					{
						c=contenu.charAt(deb);
						if(!(Character.isDigit(c)||c=='_'))
						{
							break;
						}
					}
					if(deb<contenu.length())
					{
						c=contenu.charAt(deb);
						if(c=='.'&&deb+1<contenu.length()&&
							contenu.charAt(deb+1)!='.')
						{
							deb++;
							flottant=true;
							for(;deb<contenu.length();deb++)
							{
								c=contenu.charAt(deb);
								if(!(Character.isDigit(c)||c=='_'))
								{
									break;
								}
							}
							c=contenu.charAt(deb);
							if(c=='e')
							{// prendre en compte les + et -
								deb++;
								for(;deb<contenu.length();deb++)
								{
									c=contenu.charAt(deb);
									if(!(Character.isDigit(c)||c=='_'))
									{
										break;
									}
								}
							}
						}
					}
					s=contenu.substring(pos,deb);
					if(!flottant)
						res=defaut_token(s,INT);
					else
						res=defaut_token(s,REAL);
					//System.out.println("t="+s);
					pos+=deb-pos;
					break;
				case '@':case '#':case '|':case '&':
					for(i=1;pos+i<contenu.length();i++)
					{
						c=contenu.charAt(pos+i);
						if(!(Character.isLetterOrDigit(c)||c=='_'
							||c=='='||c=='<'||c=='>'||c=='/'||c=='@'
							||c=='#'||c=='|'||c=='&'||c=='\\'||c=='^'
							||c=='*'))
						{
							break;
						}
					}
					s=contenu.substring(pos,pos+i);
					res=defaut_token(s,FreeOp);
					//System.out.println("t="+s);
					pos+=i;
					break;
				default:
					if(Character.isLetter(c))
					{
						
					}
					res=defaut_token(""+c,Autre);
					pos++;
					break;
			}
			assert(pos>last);
			lastx=x;
			if(c=='\t')
				x+=8;
			else
				x+=pos-last;
		}
		return res;
	}

	Token defaut_token(String s,int n)
	{
		Token t=new Token(y,x,s,nom_fichier);
		t.set_type_token(n);
		return t;
	}

	void init_lexer()
	{
		int i;
		this.x=1;
		this.y=1;
		lastx=x;
		suivant_deja_lu=false;
		dernier_token=null;
		if(liste_keyword==null)
		{
			liste_keyword=new HashMap();
			liste_keyword.put("and",new Integer(TK_AND));
			liste_keyword.put("cluster",new Integer(TK_CLUSTER));
			liste_keyword.put("do",new Integer(TK_DO));
			liste_keyword.put("else",new Integer(TK_ELSE));
			liste_keyword.put("end",new Integer(TK_END));
			liste_keyword.put("external",new Integer(TK_EXTERNAL));
			liste_keyword.put("if",new Integer(TK_IF));
			liste_keyword.put("not",new Integer(TK_NOT));
			liste_keyword.put("or",new Integer(TK_OR));
			liste_keyword.put("then",new Integer(TK_THEN));
			liste_keyword.put("xor",new Integer(TK_XOR));
			liste_keyword.put("true",new Integer(TK_TRUE));
			liste_keyword.put("false",new Integer(TK_FALSE));
			liste_keyword.put("generate",new Integer(TK_GENERATE));
			liste_keyword.put("class",new Integer(TK_CLASS));
			liste_keyword.put("feature",new Integer(TK_FEATURE));
			liste_keyword.put("is",new Integer(TK_IS));
			liste_keyword.put("default",new Integer(TK_DEFAULT));
			liste_keyword.put("deferred",new Integer(TK_DEFERRED));
			liste_keyword.put("alias",new Integer(TK_ALIAS));
			liste_keyword.put("all",new Integer(TK_ALL));
			liste_keyword.put("as",new Integer(TK_AS));
			liste_keyword.put("check",new Integer(TK_CHECK));
			liste_keyword.put("creation",new Integer(TK_CREATION));
			liste_keyword.put("debug",new Integer(TK_DEBUG));
			liste_keyword.put("elseif",new Integer(TK_ELSEIF));
			liste_keyword.put("ensure",new Integer(TK_ENSURE));
			liste_keyword.put("expanded",new Integer(TK_EXPANDED));
			liste_keyword.put("export",new Integer(TK_EXPORT));
			liste_keyword.put("external",new Integer(TK_EXTERNAL));
			liste_keyword.put("from",new Integer(TK_FROM));
			liste_keyword.put("frozen",new Integer(TK_FROZEN));
			liste_keyword.put("implies",new Integer(TK_IMPLIES));
			liste_keyword.put("indexing",new Integer(TK_INDEXING));
			liste_keyword.put("infix",new Integer(TK_INFIX));
			liste_keyword.put("inherit",new Integer(TK_INHERIT));
			liste_keyword.put("inspect",new Integer(TK_INSPECT));
			liste_keyword.put("invariant",new Integer(TK_INVARIANT));
			liste_keyword.put("like",new Integer(TK_LIKE));
			liste_keyword.put("local",new Integer(TK_LOCAL));
			liste_keyword.put("loop",new Integer(TK_LOOP));
			liste_keyword.put("obsolete",new Integer(TK_OBSOLETE));
			liste_keyword.put("old",new Integer(TK_OLD));
			liste_keyword.put("once",new Integer(TK_ONCE));
			liste_keyword.put("prefix",new Integer(TK_PREFIX));
			liste_keyword.put("redefine",new Integer(TK_REDEFINE));
			liste_keyword.put("rename",new Integer(TK_RENAME));
			liste_keyword.put("require",new Integer(TK_REQUIRE));
			liste_keyword.put("rescue",new Integer(TK_RESCUE));
			liste_keyword.put("retry",new Integer(TK_RETRY));
			liste_keyword.put("select",new Integer(TK_SELECT));
			liste_keyword.put("separate",new Integer(TK_SEPARATE));
			liste_keyword.put("system",new Integer(TK_SYSTEM));
			liste_keyword.put("undefine",new Integer(TK_UNDEFINE));
			liste_keyword.put("until",new Integer(TK_UNTIL));
			liste_keyword.put("variant",new Integer(TK_VARIANT));
			liste_keyword.put("when",new Integer(TK_WHEN));
			liste_keyword.put("root",new Integer(TK_ROOT));
			liste_keyword.put("strip",new Integer(TK_STRIP));
			liste_keyword.put("unique",new Integer(TK_UNIQUE));
		}
	}

	public int getColumn()
	{
		//return x-(nb_token_lu-pos_dernier);
		//return x-(nb_token_lu-pos_dernier);
		//return lastx-(nb_token_lu-pos_dernier);
		return lastx-1;
	}

	public int getLine()
	{
		return y;
	}

	public String getFilename()
	{
		return nom_fichier;
	}

	public String getMessageErr()
	{
		return msg_erreur;
	}

	public boolean est_Ok()
	{
		return etatOk;
	}
	
	boolean etatOk,suivant_deja_lu;
	String contenu,nom_fichier,msg_erreur;
	int pos,x,y,lastx;
	Map liste_keyword;
	protected Token dernier_token;
	protected Token liste_dernier_token[];
	protected int pos_dernier,nb_token_lu;
	protected boolean seul_token;
}
