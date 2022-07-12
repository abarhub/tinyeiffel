// $ANTLR 2.7.3: "eiffel.g" -> "EiffelParser.java"$

package tinyeiffel.compiler;

import java.util.Vector;

import tinyeiffel.ast.Assert;
import tinyeiffel.ast.Chaine;
import tinyeiffel.ast.Classe;
import tinyeiffel.ast.Creation;
import tinyeiffel.ast.DeclareVar;
import tinyeiffel.ast.Export;
import tinyeiffel.ast.Expr;
import tinyeiffel.ast.Expr_Appel;
import tinyeiffel.ast.Expr_Binaire;
import tinyeiffel.ast.Expr_Car;
import tinyeiffel.ast.Expr_Chaine;
import tinyeiffel.ast.Expr_Entier;
import tinyeiffel.ast.Expr_Faux;
import tinyeiffel.ast.Expr_Reel;
import tinyeiffel.ast.Expr_Tableau;
import tinyeiffel.ast.Expr_Unaire;
import tinyeiffel.ast.Expr_Var;
import tinyeiffel.ast.Expr_Vrai;
import tinyeiffel.ast.Feature;
import tinyeiffel.ast.FeatureAttr;
import tinyeiffel.ast.FeatureDeferred;
import tinyeiffel.ast.FeatureExpr;
import tinyeiffel.ast.FeatureExternal;
import tinyeiffel.ast.FeatureRoutine;
import tinyeiffel.ast.FeatureUnique;
import tinyeiffel.ast.Heritage;
import tinyeiffel.ast.Indexing;
import tinyeiffel.ast.Instr;
import tinyeiffel.ast.Instr_Affect;
import tinyeiffel.ast.Instr_Appel;
import tinyeiffel.ast.Instr_Check;
import tinyeiffel.ast.Instr_Creation;
import tinyeiffel.ast.Instr_Debug;
import tinyeiffel.ast.Instr_Else;
import tinyeiffel.ast.Instr_ElseIf;
import tinyeiffel.ast.Instr_If;
import tinyeiffel.ast.Instr_Inspect;
import tinyeiffel.ast.Instr_Loop;
import tinyeiffel.ast.Instr_Retry;
import tinyeiffel.ast.Instr_TentAffect;
import tinyeiffel.ast.NomFeature;
import tinyeiffel.ast.Rename;
import tinyeiffel.ast.Suite;
import tinyeiffel.ast.Type;
import tinyeiffel.ast.TypeAncre;
import tinyeiffel.ast.TypeSimple;
import tinyeiffel.erreur.ErreurSource;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public class EiffelParser extends antlr.LLkParser       implements EiffelTokenTypes
 {

	public EiffelLexer lexer=null;
	public int last_feature1=-1,last_feature2=-1;

	public Vector type_utilisee=new Vector();
	public Logging  logging;

	public int dernier_commentaire()
	{
		return lexer.liste_commentaire.size()-1;
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

        //public String nom_fichier;

	public tinyeiffel.ast.Token remplitToken(Token n)
	{
                //assert(n!=null);
		if(n==null)
			return null;
		return new tinyeiffel.ast.Token(n.getLine(),n.getColumn(),n.getText(),getFilename());
	}


protected EiffelParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public EiffelParser(TokenBuffer tokenBuf) {
  this(tokenBuf,4);
}

protected EiffelParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public EiffelParser(TokenStream lexer) {
  this(lexer,4);
}

public EiffelParser(ParserSharedInputState state) {
  super(state,4);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final Classe  classe() throws RecognitionException, TokenStreamException {
		Classe res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut classe_AST = null;
		Token  exp = null;
		tinyeiffel.compiler.ASTDefaut exp_AST = null;
		Token  def = null;
		tinyeiffel.compiler.ASTDefaut def_AST = null;
		Token  c = null;
		tinyeiffel.compiler.ASTDefaut c_AST = null;
		Token  tobs = null;
		tinyeiffel.compiler.ASTDefaut tobs_AST = null;
		Token  f0 = null;
		tinyeiffel.compiler.ASTDefaut f0_AST = null;
		Token  inv = null;
		tinyeiffel.compiler.ASTDefaut inv_AST = null;
		Token  e = null;
		tinyeiffel.compiler.ASTDefaut e_AST = null;
		
		Type n;
		Vector f=new Vector(),h=null,invariant=null,creation=null;
		Feature f1=null;
		NomFeature nom=null;
		Chaine obsolete=null;
		Vector index=null,commentaire=null,export=null;
		boolean expanded=false,deferred=false;
		Type type_exp;
		Token fea=null;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case INDEXING:
			{
				index=indexing();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EXPANDED:
			case DEFERRED:
			case CLASS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case EXPANDED:
			{
				exp = LT(1);
				exp_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(exp);
				astFactory.addASTChild(currentAST, exp_AST);
				match(EXPANDED);
				if ( inputState.guessing==0 ) {
					expanded=true;
				}
				break;
			}
			case DEFERRED:
			{
				def = LT(1);
				def_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(def);
				astFactory.addASTChild(currentAST, def_AST);
				match(DEFERRED);
				if ( inputState.guessing==0 ) {
					deferred=true;
				}
				break;
			}
			case CLASS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			c = LT(1);
			c_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(c);
			astFactory.makeASTRoot(currentAST, c_AST);
			match(CLASS);
			n=nom_class();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case OBSOLETE:
			{
				tobs = LT(1);
				tobs_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(tobs);
				astFactory.addASTChild(currentAST, tobs_AST);
				match(OBSOLETE);
				obsolete=string();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			case INHERIT:
			case CREATION:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case INHERIT:
			{
				h=heritage();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			case CREATION:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case CREATION:
			{
				creation=creation();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			int _cnt18=0;
			_loop18:
			do {
				if ((LA(1)==FEATURE)) {
					if ( inputState.guessing==0 ) {
						fea=null;
					}
					{
					if ( inputState.guessing==0 ) {
						export=null;
					}
					f0 = LT(1);
					f0_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f0);
					astFactory.addASTChild(currentAST, f0_AST);
					match(FEATURE);
					if ( inputState.guessing==0 ) {
						fea=f0;
					}
					{
					switch ( LA(1)) {
					case ACOLADEO:
					{
						tinyeiffel.compiler.ASTDefaut tmp9_AST = null;
						tmp9_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp9_AST);
						match(ACOLADEO);
						if ( inputState.guessing==0 ) {
							export=new Vector();
						}
						{
						switch ( LA(1)) {
						case EXPANDED:
						case ID:
						case LIKE:
						{
							type_exp=type();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								export.addElement(type_exp);
							}
							{
							_loop12:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp10_AST = null;
									tmp10_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp10_AST);
									match(VIRGULE);
									type_exp=type();
									astFactory.addASTChild(currentAST, returnAST);
									if ( inputState.guessing==0 ) {
										export.addElement(type_exp);
									}
								}
								else {
									break _loop12;
								}
								
							} while (true);
							}
							break;
						}
						case ACOLADEF:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						tinyeiffel.compiler.ASTDefaut tmp11_AST = null;
						tmp11_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp11_AST);
						match(ACOLADEF);
						break;
					}
					case FEATURE:
					case SEMI:
					case INVARIANT:
					case END:
					case ID:
					case INFIX:
					case PREFIX:
					case FROZEN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					}
					{
					switch ( LA(1)) {
					case FEATURE:
					case INVARIANT:
					case END:
					case ID:
					case INFIX:
					case PREFIX:
					case FROZEN:
					{
						{
						_loop15:
						do {
							if ((_tokenSet_0.member(LA(1)))) {
								f1=feature(export,fea);
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									f.addElement(f1);export=null;fea=null;/*System.out.println("Suite4");*/
								}
							}
							else {
								break _loop15;
							}
							
						} while (true);
						}
						break;
					}
					case SEMI:
					{
						{
						int _cnt17=0;
						_loop17:
						do {
							if ((LA(1)==SEMI)) {
								tinyeiffel.compiler.ASTDefaut tmp12_AST = null;
								tmp12_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp12_AST);
								match(SEMI);
							}
							else {
								if ( _cnt17>=1 ) { break _loop17; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt17++;
						} while (true);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					if ( _cnt18>=1 ) { break _loop18; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt18++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case INVARIANT:
			{
				inv = LT(1);
				inv_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(inv);
				astFactory.addASTChild(currentAST, inv_AST);
				match(INVARIANT);
				invariant=liste_assert();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case END:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			e = LT(1);
			e_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(e);
			astFactory.addASTChild(currentAST, e_AST);
			match(END);
			if ( inputState.guessing==0 ) {
				
						//System.out.println("classe:"+n.nom);
						//if(c instanceof antlr.CommonToken)
						//	System.out.println("Ok");
						//else
						//	System.out.println("Bad");
						//System.out.println("line="+c.getLine());
						commentaire=lexer.liste_commentaire;
						res=new Classe(deferred,expanded,n,f,h,invariant,creation,
								obsolete,index,commentaire);
						//res.feature=f;
						//res.heritage=h;
						ajoute_type(n);
						tinyeiffel.ast.Token tclasse,texp_defer,tobsolete=null,tinvariant=null,tend;
						tclasse=remplitToken(c);
						if(exp!=null)
							texp_defer=remplitToken(exp);
						else if(def!=null)
							texp_defer=remplitToken(def);
						else
							texp_defer=null;
						tobsolete=remplitToken(tobs);
						tinvariant=remplitToken(inv);
						tend=remplitToken(e);
						res.set_token(tclasse,texp_defer,tobsolete,tinvariant,tend);
					
			}
			classe_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la classe",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = classe_AST;
		return res;
	}
	
	public final Vector  indexing() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut indexing_AST = null;
		Token  n = null;
		tinyeiffel.compiler.ASTDefaut n_AST = null;
		Token  n1 = null;
		tinyeiffel.compiler.ASTDefaut n1_AST = null;
		Token  s1 = null;
		tinyeiffel.compiler.ASTDefaut s1_AST = null;
		Token  n2 = null;
		tinyeiffel.compiler.ASTDefaut n2_AST = null;
		Token  s2 = null;
		tinyeiffel.compiler.ASTDefaut s2_AST = null;
		
		String tmp1="",tmp2="";
		Vector liste=new Vector();
		Indexing index=null;
		
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp13_AST = null;
			tmp13_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp13_AST);
			match(INDEXING);
			{
			int _cnt28=0;
			_loop28:
			do {
				if ((LA(1)==ID)) {
					if ( inputState.guessing==0 ) {
						liste=new Vector();
					}
					n = LT(1);
					n_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n);
					astFactory.addASTChild(currentAST, n_AST);
					match(ID);
					tinyeiffel.compiler.ASTDefaut tmp14_AST = null;
					tmp14_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp14_AST);
					match(DOUBLE_POINT);
					{
					switch ( LA(1)) {
					case ID:
					{
						n1 = LT(1);
						n1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n1);
						astFactory.addASTChild(currentAST, n1_AST);
						match(ID);
						if ( inputState.guessing==0 ) {
							tmp1=n1.getText();
						}
						break;
					}
					case STRING:
					{
						s1 = LT(1);
						s1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s1);
						astFactory.addASTChild(currentAST, s1_AST);
						match(STRING);
						if ( inputState.guessing==0 ) {
							tmp1=s1.getText();
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						liste.addElement(tmp1);
					}
					{
					_loop25:
					do {
						if ((LA(1)==VIRGULE)) {
							match(VIRGULE);
							{
							switch ( LA(1)) {
							case ID:
							{
								n2 = LT(1);
								n2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n2);
								astFactory.addASTChild(currentAST, n2_AST);
								match(ID);
								if ( inputState.guessing==0 ) {
									tmp1=n2.getText();
								}
								break;
							}
							case STRING:
							{
								s2 = LT(1);
								s2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s2);
								astFactory.addASTChild(currentAST, s2_AST);
								match(STRING);
								if ( inputState.guessing==0 ) {
									tmp1=s2.getText();
								}
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							if ( inputState.guessing==0 ) {
								liste.addElement(tmp1);
							}
						}
						else {
							break _loop25;
						}
						
					} while (true);
					}
					{
					int _cnt27=0;
					_loop27:
					do {
						if ((LA(1)==SEMI)) {
							tinyeiffel.compiler.ASTDefaut tmp16_AST = null;
							tmp16_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp16_AST);
							match(SEMI);
						}
						else {
							if ( _cnt27>=1 ) { break _loop27; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt27++;
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						
													index=new Indexing(n.getText(),liste);
													res.addElement(index);
												
					}
				}
				else {
					if ( _cnt28>=1 ) { break _loop28; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt28++;
			} while (true);
			}
			indexing_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'index",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = indexing_AST;
		return res;
	}
	
	public final Type  nom_class() throws RecognitionException, TokenStreamException {
		Type res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut nom_class_AST = null;
		
		Type d=null;
		
		
		try {      // for error handling
			d=decl_type();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				res=d;
			}
			nom_class_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le nom de la classe",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = nom_class_AST;
		return res;
	}
	
	public final Chaine  string() throws RecognitionException, TokenStreamException {
		Chaine res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut string_AST = null;
		Token  s = null;
		tinyeiffel.compiler.ASTDefaut s_AST = null;
		Token  s1 = null;
		tinyeiffel.compiler.ASTDefaut s1_AST = null;
		Token  s2 = null;
		tinyeiffel.compiler.ASTDefaut s2_AST = null;
		Token  s3 = null;
		tinyeiffel.compiler.ASTDefaut s3_AST = null;
		
		Vector tmp=new Vector();
		boolean suite=false;
		String str="";
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING:
			{
				s = LT(1);
				s_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s);
				astFactory.addASTChild(currentAST, s_AST);
				match(STRING);
				if ( inputState.guessing==0 ) {
					
					tmp.addElement(s.getText());
					opera=remplitToken(s);
							res=new Chaine(tmp,opera);
						
				}
				string_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case STRING2:
			{
				s1 = LT(1);
				s1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s1);
				astFactory.addASTChild(currentAST, s1_AST);
				match(STRING2);
				if ( inputState.guessing==0 ) {
					tmp.addElement(s1.getText());opera=remplitToken(s1);
				}
				{
				_loop273:
				do {
					if ((LA(1)==STRING3)) {
						s2 = LT(1);
						s2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s2);
						astFactory.addASTChild(currentAST, s2_AST);
						match(STRING3);
						if ( inputState.guessing==0 ) {
							
											str=s2.getText();
											tmp.addElement(str);
										
						}
					}
					else {
						break _loop273;
					}
					
				} while (true);
				}
				s3 = LT(1);
				s3_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s3);
				astFactory.addASTChild(currentAST, s3_AST);
				match(STRING4);
				if ( inputState.guessing==0 ) {
					tmp.addElement(s3.getText());
				}
				if ( inputState.guessing==0 ) {
					
							res=new Chaine(tmp,opera);
						
				}
				string_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la chaine",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = string_AST;
		return res;
	}
	
	public final Vector  heritage() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut heritage_AST = null;
		Token  in = null;
		tinyeiffel.compiler.ASTDefaut in_AST = null;
		Token  ren = null;
		tinyeiffel.compiler.ASTDefaut ren_AST = null;
		Token  exp2 = null;
		tinyeiffel.compiler.ASTDefaut exp2_AST = null;
		Token  und = null;
		tinyeiffel.compiler.ASTDefaut und_AST = null;
		Token  red = null;
		tinyeiffel.compiler.ASTDefaut red_AST = null;
		Token  sel = null;
		tinyeiffel.compiler.ASTDefaut sel_AST = null;
		Token  end = null;
		tinyeiffel.compiler.ASTDefaut end_AST = null;
		
		Heritage h;
		Type t,t2;
		Vector rename=new Vector(),export=new Vector(),undefine=new Vector(),
			redefine=new Vector(),select=new Vector();
		Rename tmp1=null;
		Export exp=null;
		NomFeature n1,n2;
		Vector liste1=null,liste2=null;
		
		
		try {      // for error handling
			in = LT(1);
			in_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(in);
			astFactory.addASTChild(currentAST, in_AST);
			match(INHERIT);
			{
			int _cnt69=0;
			_loop69:
			do {
				if ((LA(1)==EXPANDED||LA(1)==ID||LA(1)==LIKE)) {
					t=type();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						
										rename=new Vector();export=new Vector();undefine=new Vector();
										redefine=new Vector();select=new Vector();
										liste1=new Vector();liste2=new Vector();
										ajoute_type(t);
									
					}
					{
					switch ( LA(1)) {
					case END:
					case RENAME:
					case EXPORT:
					case UNDEFINE:
					case REDEFINE:
					case SELECT:
					{
						{
						switch ( LA(1)) {
						case RENAME:
						{
							ren = LT(1);
							ren_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(ren);
							astFactory.addASTChild(currentAST, ren_AST);
							match(RENAME);
							{
							switch ( LA(1)) {
							case ID:
							case INFIX:
							case PREFIX:
							{
								n1=nom_feature();
								astFactory.addASTChild(currentAST, returnAST);
								tinyeiffel.compiler.ASTDefaut tmp17_AST = null;
								tmp17_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp17_AST);
								match(AS);
								n2=nom_feature();
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									tmp1=new Rename(n1,n2);rename.addElement(tmp1);
								}
								{
								_loop35:
								do {
									if ((LA(1)==VIRGULE)) {
										tinyeiffel.compiler.ASTDefaut tmp18_AST = null;
										tmp18_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp18_AST);
										match(VIRGULE);
										n1=nom_feature();
										astFactory.addASTChild(currentAST, returnAST);
										tinyeiffel.compiler.ASTDefaut tmp19_AST = null;
										tmp19_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp19_AST);
										match(AS);
										n2=nom_feature();
										astFactory.addASTChild(currentAST, returnAST);
										if ( inputState.guessing==0 ) {
											tmp1=new Rename(n1,n2);rename.addElement(tmp1);
										}
									}
									else {
										break _loop35;
									}
									
								} while (true);
								}
								break;
							}
							case SEMI:
							case END:
							case EXPORT:
							case UNDEFINE:
							case REDEFINE:
							case SELECT:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							{
							_loop37:
							do {
								if ((LA(1)==SEMI)) {
									tinyeiffel.compiler.ASTDefaut tmp20_AST = null;
									tmp20_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp20_AST);
									match(SEMI);
								}
								else {
									break _loop37;
								}
								
							} while (true);
							}
							break;
						}
						case END:
						case EXPORT:
						case UNDEFINE:
						case REDEFINE:
						case SELECT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						switch ( LA(1)) {
						case EXPORT:
						{
							exp2 = LT(1);
							exp2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(exp2);
							astFactory.addASTChild(currentAST, exp2_AST);
							match(EXPORT);
							{
							int _cnt50=0;
							_loop50:
							do {
								if ((LA(1)==ACOLADEO)) {
									{
									tinyeiffel.compiler.ASTDefaut tmp21_AST = null;
									tmp21_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp21_AST);
									match(ACOLADEO);
									if ( inputState.guessing==0 ) {
										liste2=new Vector();
									}
									{
									switch ( LA(1)) {
									case EXPANDED:
									case ID:
									case LIKE:
									{
										t2=type();
										astFactory.addASTChild(currentAST, returnAST);
										if ( inputState.guessing==0 ) {
											liste2.addElement(t2);
										}
										{
										_loop43:
										do {
											if ((LA(1)==VIRGULE)) {
												tinyeiffel.compiler.ASTDefaut tmp22_AST = null;
												tmp22_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
												astFactory.addASTChild(currentAST, tmp22_AST);
												match(VIRGULE);
												t2=type();
												astFactory.addASTChild(currentAST, returnAST);
												if ( inputState.guessing==0 ) {
													liste2.addElement(t2);
												}
											}
											else {
												break _loop43;
											}
											
										} while (true);
										}
										break;
									}
									case ACOLADEF:
									{
										break;
									}
									default:
									{
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
									}
									tinyeiffel.compiler.ASTDefaut tmp23_AST = null;
									tmp23_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp23_AST);
									match(ACOLADEF);
									}
									{
									switch ( LA(1)) {
									case ID:
									case INFIX:
									case PREFIX:
									{
										{
										n1=nom_feature();
										astFactory.addASTChild(currentAST, returnAST);
										if ( inputState.guessing==0 ) {
											liste1=new Vector();liste1.addElement(n1);
										}
										{
										_loop47:
										do {
											if ((LA(1)==VIRGULE)) {
												tinyeiffel.compiler.ASTDefaut tmp24_AST = null;
												tmp24_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
												astFactory.addASTChild(currentAST, tmp24_AST);
												match(VIRGULE);
												n1=nom_feature();
												astFactory.addASTChild(currentAST, returnAST);
												if ( inputState.guessing==0 ) {
													liste1.addElement(n1);
												}
											}
											else {
												break _loop47;
											}
											
										} while (true);
										}
										}
										break;
									}
									case ALL:
									{
										tinyeiffel.compiler.ASTDefaut tmp25_AST = null;
										tmp25_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp25_AST);
										match(ALL);
										if ( inputState.guessing==0 ) {
											liste1=new Vector();
										}
										break;
									}
									default:
									{
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
									}
									if ( inputState.guessing==0 ) {
										
																exp=new Export(liste1,liste2);
																export.addElement(exp);
																ajoute_type(liste2);
															
									}
									{
									_loop49:
									do {
										if ((LA(1)==SEMI)) {
											tinyeiffel.compiler.ASTDefaut tmp26_AST = null;
											tmp26_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
											astFactory.addASTChild(currentAST, tmp26_AST);
											match(SEMI);
										}
										else {
											break _loop49;
										}
										
									} while (true);
									}
								}
								else {
									if ( _cnt50>=1 ) { break _loop50; } else {throw new NoViableAltException(LT(1), getFilename());}
								}
								
								_cnt50++;
							} while (true);
							}
							break;
						}
						case END:
						case UNDEFINE:
						case REDEFINE:
						case SELECT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						switch ( LA(1)) {
						case UNDEFINE:
						{
							und = LT(1);
							und_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(und);
							astFactory.addASTChild(currentAST, und_AST);
							match(UNDEFINE);
							n1=nom_feature();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								undefine.addElement(n1);
							}
							{
							_loop53:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp27_AST = null;
									tmp27_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp27_AST);
									match(VIRGULE);
									n1=nom_feature();
									astFactory.addASTChild(currentAST, returnAST);
									if ( inputState.guessing==0 ) {
										undefine.addElement(n1);
									}
								}
								else {
									break _loop53;
								}
								
							} while (true);
							}
							{
							_loop55:
							do {
								if ((LA(1)==SEMI)) {
									tinyeiffel.compiler.ASTDefaut tmp28_AST = null;
									tmp28_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp28_AST);
									match(SEMI);
								}
								else {
									break _loop55;
								}
								
							} while (true);
							}
							break;
						}
						case END:
						case REDEFINE:
						case SELECT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						switch ( LA(1)) {
						case REDEFINE:
						{
							red = LT(1);
							red_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(red);
							astFactory.addASTChild(currentAST, red_AST);
							match(REDEFINE);
							n1=nom_feature();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								redefine.addElement(n1);
							}
							{
							_loop58:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp29_AST = null;
									tmp29_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp29_AST);
									match(VIRGULE);
									n1=nom_feature();
									astFactory.addASTChild(currentAST, returnAST);
									if ( inputState.guessing==0 ) {
										redefine.addElement(n1);
									}
								}
								else {
									break _loop58;
								}
								
							} while (true);
							}
							{
							_loop60:
							do {
								if ((LA(1)==SEMI)) {
									tinyeiffel.compiler.ASTDefaut tmp30_AST = null;
									tmp30_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp30_AST);
									match(SEMI);
								}
								else {
									break _loop60;
								}
								
							} while (true);
							}
							break;
						}
						case END:
						case SELECT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						switch ( LA(1)) {
						case SELECT:
						{
							sel = LT(1);
							sel_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(sel);
							astFactory.addASTChild(currentAST, sel_AST);
							match(SELECT);
							n1=nom_feature();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								select.addElement(n1);
							}
							{
							_loop63:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp31_AST = null;
									tmp31_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp31_AST);
									match(VIRGULE);
									n1=nom_feature();
									astFactory.addASTChild(currentAST, returnAST);
									if ( inputState.guessing==0 ) {
										select.addElement(n1);
									}
								}
								else {
									break _loop63;
								}
								
							} while (true);
							}
							{
							_loop65:
							do {
								if ((LA(1)==SEMI)) {
									tinyeiffel.compiler.ASTDefaut tmp32_AST = null;
									tmp32_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp32_AST);
									match(SEMI);
								}
								else {
									break _loop65;
								}
								
							} while (true);
							}
							break;
						}
						case END:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						end = LT(1);
						end_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(end);
						astFactory.addASTChild(currentAST, end_AST);
						match(END);
						}
						break;
					}
					case EXPANDED:
					case FEATURE:
					case SEMI:
					case ID:
					case CREATION:
					case LIKE:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						
											h=new Heritage(t,rename,export,undefine,redefine,select);
											tinyeiffel.ast.Token therit,trename,texport,tundefine,tredefine,tselect,
						tend;
											therit=remplitToken(in);
											trename=remplitToken(ren);
											texport=remplitToken(exp2);
											tundefine=remplitToken(und);
											tredefine=remplitToken(red);
											tselect=remplitToken(sel);
											tend=remplitToken(end);
											h.set_token(therit,trename,texport,tundefine,tredefine,
												tselect,tend);
											res.addElement(h);
										
					}
					{
					_loop68:
					do {
						if ((LA(1)==SEMI)) {
							tinyeiffel.compiler.ASTDefaut tmp33_AST = null;
							tmp33_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp33_AST);
							match(SEMI);
						}
						else {
							break _loop68;
						}
						
					} while (true);
					}
				}
				else {
					if ( _cnt69>=1 ) { break _loop69; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt69++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
						//res=new Heritage();
					
			}
			heritage_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'heritage",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = heritage_AST;
		return res;
	}
	
	public final Vector  creation() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut creation_AST = null;
		Token  cre = null;
		tinyeiffel.compiler.ASTDefaut cre_AST = null;
		
		Creation c1;
		NomFeature nom=null;
		Vector creation=new Vector(),type=new Vector();
		Type t;
		boolean pas_export=false;
		
		
		try {      // for error handling
			{
			int _cnt78=0;
			_loop78:
			do {
				if ((LA(1)==CREATION)) {
					cre = LT(1);
					cre_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(cre);
					astFactory.addASTChild(currentAST, cre_AST);
					match(CREATION);
					if ( inputState.guessing==0 ) {
						creation=new Vector();type=new Vector();pas_export=false;
					}
					{
					switch ( LA(1)) {
					case ACOLADEO:
					{
						tinyeiffel.compiler.ASTDefaut tmp34_AST = null;
						tmp34_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp34_AST);
						match(ACOLADEO);
						if ( inputState.guessing==0 ) {
							pas_export=true;
						}
						{
						switch ( LA(1)) {
						case EXPANDED:
						case ID:
						case LIKE:
						{
							t=type();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								type.addElement(t);
							}
							{
							_loop75:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp35_AST = null;
									tmp35_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp35_AST);
									match(VIRGULE);
									t=type();
									astFactory.addASTChild(currentAST, returnAST);
									if ( inputState.guessing==0 ) {
										type.addElement(t);
									}
								}
								else {
									break _loop75;
								}
								
							} while (true);
							}
							break;
						}
						case ACOLADEF:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						tinyeiffel.compiler.ASTDefaut tmp36_AST = null;
						tmp36_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp36_AST);
						match(ACOLADEF);
						break;
					}
					case ID:
					case INFIX:
					case PREFIX:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					nom=nom_feature();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						creation.addElement(nom);
					}
					{
					_loop77:
					do {
						if ((LA(1)==VIRGULE)) {
							tinyeiffel.compiler.ASTDefaut tmp37_AST = null;
							tmp37_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp37_AST);
							match(VIRGULE);
							nom=nom_feature();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								creation.addElement(nom);
							}
						}
						else {
							break _loop77;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						
								if(!pas_export&&type.size()==0)
									type=null;
								c1=new Creation(type,creation);
								tinyeiffel.ast.Token tcreation;
								tcreation=remplitToken(cre);
								c1.set_token(tcreation);
								res.addElement(c1);
								if(type!=null)
									ajoute_type(type);
							
					}
				}
				else {
					if ( _cnt78>=1 ) { break _loop78; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt78++;
			} while (true);
			}
			creation_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la creation",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = creation_AST;
		return res;
	}
	
	public final Type  type() throws RecognitionException, TokenStreamException {
		Type res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut type_AST = null;
		Token  id1 = null;
		tinyeiffel.compiler.ASTDefaut id1_AST = null;
		Token  co = null;
		tinyeiffel.compiler.ASTDefaut co_AST = null;
		Token  cf = null;
		tinyeiffel.compiler.ASTDefaut cf_AST = null;
		Token  like = null;
		tinyeiffel.compiler.ASTDefaut like_AST = null;
		
		Expr e=null;
		Vector t=new Vector();
		Type t2;
		boolean expanded=false;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case EXPANDED:
			case ID:
			{
				{
				switch ( LA(1)) {
				case EXPANDED:
				{
					tinyeiffel.compiler.ASTDefaut tmp38_AST = null;
					tmp38_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp38_AST);
					match(EXPANDED);
					if ( inputState.guessing==0 ) {
						expanded=true;
					}
					break;
				}
				case ID:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				id1 = LT(1);
				id1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(id1);
				astFactory.addASTChild(currentAST, id1_AST);
				match(ID);
				{
				switch ( LA(1)) {
				case CROCHETO:
				{
					co = LT(1);
					co_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(co);
					astFactory.addASTChild(currentAST, co_AST);
					match(CROCHETO);
					{
					switch ( LA(1)) {
					case EXPANDED:
					case ID:
					case LIKE:
					{
						t2=type();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							t.addElement(t2);
						}
						{
						_loop96:
						do {
							if ((LA(1)==VIRGULE)) {
								tinyeiffel.compiler.ASTDefaut tmp39_AST = null;
								tmp39_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp39_AST);
								match(VIRGULE);
								t2=type();
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									t.addElement(t2);
								}
							}
							else {
								break _loop96;
							}
							
						} while (true);
						}
						break;
					}
					case CROCHETF:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					cf = LT(1);
					cf_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(cf);
					astFactory.addASTChild(currentAST, cf_AST);
					match(CROCHETF);
					break;
				}
				case EXPANDED:
				case FEATURE:
				case VIRGULE:
				case ACOLADEF:
				case SEMI:
				case INVARIANT:
				case END:
				case ID:
				case RENAME:
				case EXPORT:
				case UNDEFINE:
				case REDEFINE:
				case SELECT:
				case CREATION:
				case INFIX:
				case PREFIX:
				case RPAREN:
				case CROCHETF:
				case LIKE:
				case FROZEN:
				case IS:
				case DO:
				case ONCE:
				case EXCLAMATION:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					
								res=new TypeSimple(expanded,id1.getText(),t);ajoute_type(res);
								res.set_token(remplitToken(id1),remplitToken(co),remplitToken(cf));
							
				}
				type_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case LIKE:
			{
				like = LT(1);
				like_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(like);
				astFactory.addASTChild(currentAST, like_AST);
				match(LIKE);
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
								res=new TypeAncre(e);
								res.set_token(remplitToken(like));
							
				}
				type_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le type"+ex,ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = type_AST;
		return res;
	}
	
	public final Feature  feature(
		Vector export_debut,Token fea
	) throws RecognitionException, TokenStreamException {
		Feature res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut feature_AST = null;
		Token  fro1 = null;
		tinyeiffel.compiler.ASTDefaut fro1_AST = null;
		Token  fro2 = null;
		tinyeiffel.compiler.ASTDefaut fro2_AST = null;
		
		Vector decl=null,comment=null;
		NomFeature n;
		boolean frozen=false,export_vide=false,premier=true;
		Vector nom=new Vector(),export=new Vector();
		Type t=null,t2=null;
		int debut_commentaire=dernier_commentaire(),fin_commentaire=-1;
		export=export_debut;
		export_vide=true;
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				frozen=false;
			}
			{
			switch ( LA(1)) {
			case FROZEN:
			{
				fro1 = LT(1);
				fro1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(fro1);
				astFactory.addASTChild(currentAST, fro1_AST);
				match(FROZEN);
				if ( inputState.guessing==0 ) {
					frozen=true;
				}
				break;
			}
			case ID:
			case INFIX:
			case PREFIX:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				fin_commentaire=dernier_commentaire();
			}
			n=nom_feature();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				
										n.frozen=frozen;
										nom.addElement(n);
										n.tfrozen=remplitToken(fro1);
									
			}
			{
			_loop102:
			do {
				if ((LA(1)==VIRGULE)) {
					tinyeiffel.compiler.ASTDefaut tmp40_AST = null;
					tmp40_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp40_AST);
					match(VIRGULE);
					if ( inputState.guessing==0 ) {
						frozen=false;
					}
					{
					switch ( LA(1)) {
					case FROZEN:
					{
						fro2 = LT(1);
						fro2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(fro2);
						astFactory.addASTChild(currentAST, fro2_AST);
						match(FROZEN);
						if ( inputState.guessing==0 ) {
							frozen=true;
						}
						break;
					}
					case ID:
					case INFIX:
					case PREFIX:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					n=nom_feature();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						
												n.frozen=frozen;
												nom.addElement(n);
												n.tfrozen=remplitToken(fro2);
											
					}
				}
				else {
					break _loop102;
				}
				
			} while (true);
			}
			{
			{
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				decl=decl_param();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			case SEMI:
			case INVARIANT:
			case END:
			case ID:
			case DOUBLE_POINT:
			case INFIX:
			case PREFIX:
			case FROZEN:
			case IS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case DOUBLE_POINT:
			{
				tinyeiffel.compiler.ASTDefaut tmp41_AST = null;
				tmp41_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp41_AST);
				match(DOUBLE_POINT);
				t=type();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			case SEMI:
			case INVARIANT:
			case END:
			case ID:
			case INFIX:
			case PREFIX:
			case FROZEN:
			case IS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case IS:
			{
				tinyeiffel.compiler.ASTDefaut tmp42_AST = null;
				tmp42_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(IS);
				if ( inputState.guessing==0 ) {
					last_feature1=lexer.donne_dernier();
				}
				res=corps();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FEATURE:
			case SEMI:
			case INVARIANT:
			case END:
			case ID:
			case INFIX:
			case PREFIX:
			case FROZEN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop109:
			do {
				if ((LA(1)==SEMI)) {
					match(SEMI);
				}
				else {
					break _loop109;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
						comment=null;
						//System.out.println("suite2");
						if(res!=null)
						{
							//res=new Feature();
							if(fin_commentaire>debut_commentaire)
							{
								//System.out.println("comment feature:"+debut_commentaire+"<"+fin_commentaire);
								comment=new Vector();
								for(int i=debut_commentaire+1;i<=fin_commentaire;i++)
								{
									//System.out.println(i+")"+((Commentaire)lexer.liste_commentaire.elementAt(i)).str+"!");
									comment.addElement(lexer.liste_commentaire.elementAt(i));
								}
								//System.out.println("fin0");
							}
							res.set_nom_param(nom,decl,export,comment);
							res.type_retour=t;
							if(fea!=null)
								res.tfeature=remplitToken(fea);
							else
								res.tfeature=null;
						}
						else
						{
							if(fin_commentaire>debut_commentaire)
							{
								//System.out.println("comment feature:"+debut_commentaire+"<"+fin_commentaire);
								comment=new Vector();
								for(int i=debut_commentaire+1;i<=fin_commentaire;i++)
								{
									//System.out.println(i+")"+((Commentaire)lexer.liste_commentaire.elementAt(i)).str+"!");
									comment.addElement(lexer.liste_commentaire.elementAt(i));
								}
								//System.out.println("fin2");
							}
							res=new FeatureAttr();
							res.set_nom_param(nom,decl,export,comment);
							res.type_retour=t;
						}
						ajoute_type(export);
						ajoute_type(t);
						last_feature1=-1;last_feature2=-1;
						//System.out.println("suite3");
					
			}
			}
			}
			feature_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'attribut",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = feature_AST;
		return res;
	}
	
	public final Vector  liste_assert() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut liste_assert_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		
		Expr e=null;
		Assert a=null;
		boolean trouve=false;
		
		
		try {      // for error handling
			{
			_loop288:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					if ( inputState.guessing==0 ) {
						trouve=false;
					}
					{
					boolean synPredMatched282 = false;
					if (((LA(1)==ID) && (LA(2)==DOUBLE_POINT))) {
						int _m282 = mark();
						synPredMatched282 = true;
						inputState.guessing++;
						try {
							{
							match(ID);
							match(DOUBLE_POINT);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched282 = false;
						}
						rewind(_m282);
						inputState.guessing--;
					}
					if ( synPredMatched282 ) {
						i = LT(1);
						i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
						astFactory.addASTChild(currentAST, i_AST);
						match(ID);
						tinyeiffel.compiler.ASTDefaut tmp44_AST = null;
						tmp44_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp44_AST);
						match(DOUBLE_POINT);
						if ( inputState.guessing==0 ) {
							trouve=true;
						}
						{
						boolean synPredMatched285 = false;
						if (((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2))) && (_tokenSet_3.member(LA(3))) && (_tokenSet_4.member(LA(4))))) {
							int _m285 = mark();
							synPredMatched285 = true;
							inputState.guessing++;
							try {
								{
								switch ( LA(1)) {
								case ID:
								{
									match(ID);
									break;
								}
								case STRING:
								{
									match(STRING);
									break;
								}
								case LPAREN:
								{
									match(LPAREN);
									break;
								}
								case INT:
								{
									match(INT);
									break;
								}
								case REAL:
								{
									match(REAL);
									break;
								}
								case CHAR:
								{
									match(CHAR);
									break;
								}
								case PLUS:
								{
									match(PLUS);
									break;
								}
								case MOINS:
								{
									match(MOINS);
									break;
								}
								case FREEOP:
								{
									match(FREEOP);
									break;
								}
								case OLD:
								{
									match(OLD);
									break;
								}
								case NOT:
								{
									match(NOT);
									break;
								}
								case DEBUT_ARRAY:
								{
									match(DEBUT_ARRAY);
									break;
								}
								case TRUE:
								{
									match(TRUE);
									break;
								}
								case FALSE:
								{
									match(FALSE);
									break;
								}
								case STRING2:
								{
									match(STRING2);
									break;
								}
								default:
								{
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
								}
							}
							catch (RecognitionException pe) {
								synPredMatched285 = false;
							}
							rewind(_m285);
							inputState.guessing--;
						}
						if ( synPredMatched285 ) {
							e=expr();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else if ((_tokenSet_5.member(LA(1))) && (_tokenSet_6.member(LA(2))) && (_tokenSet_4.member(LA(3))) && (_tokenSet_7.member(LA(4)))) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
					}
					else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
						e=expr();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					{
					_loop287:
					do {
						if ((LA(1)==SEMI)) {
							tinyeiffel.compiler.ASTDefaut tmp45_AST = null;
							tmp45_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp45_AST);
							match(SEMI);
						}
						else {
							break _loop287;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						
									if(trouve)
										a=new Assert(i.getText(),e);
									else
										a=new Assert(e);
									res.addElement(a);
								
					}
				}
				else {
					break _loop288;
				}
				
			} while (true);
			}
			liste_assert_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la liste d'assertion",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = liste_assert_AST;
		return res;
	}
	
	public final NomFeature  nom_feature() throws RecognitionException, TokenStreamException {
		NomFeature res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut nom_feature_AST = null;
		Token  id = null;
		tinyeiffel.compiler.ASTDefaut id_AST = null;
		Token  in = null;
		tinyeiffel.compiler.ASTDefaut in_AST = null;
		Token  pre = null;
		tinyeiffel.compiler.ASTDefaut pre_AST = null;
		
		Chaine s1;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			{
				id = LT(1);
				id_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
				match(ID);
				if ( inputState.guessing==0 ) {
					
													res=new NomFeature(id.getText());
													res.set_token(remplitToken(id),null);
												
				}
				nom_feature_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case INFIX:
			{
				in = LT(1);
				in_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(in);
				astFactory.addASTChild(currentAST, in_AST);
				match(INFIX);
				s1=string();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
													res=new NomFeature(s1);res.infix=true;
													res.set_token(null,remplitToken(in));
												
				}
				nom_feature_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case PREFIX:
			{
				pre = LT(1);
				pre_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(pre);
				astFactory.addASTChild(currentAST, pre_AST);
				match(PREFIX);
				s1=string();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
													res=new NomFeature(s1);res.prefix=true;
													res.set_token(null,remplitToken(pre));
												
				}
				nom_feature_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le nom d'attribut",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = nom_feature_AST;
		return res;
	}
	
	public final Type  decl_type() throws RecognitionException, TokenStreamException {
		Type res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut decl_type_AST = null;
		Token  id = null;
		tinyeiffel.compiler.ASTDefaut id_AST = null;
		Token  co = null;
		tinyeiffel.compiler.ASTDefaut co_AST = null;
		Token  cf = null;
		tinyeiffel.compiler.ASTDefaut cf_AST = null;
		
		Vector t=new Vector();
		Type t2;
		boolean expanded=false;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case EXPANDED:
			{
				tinyeiffel.compiler.ASTDefaut tmp46_AST = null;
				tmp46_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(EXPANDED);
				if ( inputState.guessing==0 ) {
					expanded=true;
				}
				break;
			}
			case ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			id = LT(1);
			id_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			{
			switch ( LA(1)) {
			case CROCHETO:
			{
				co = LT(1);
				co_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(co);
				astFactory.addASTChild(currentAST, co_AST);
				match(CROCHETO);
				{
				switch ( LA(1)) {
				case EXPANDED:
				case ID:
				{
					t2=type2();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						t.addElement(t2);
					}
					{
					_loop87:
					do {
						if ((LA(1)==VIRGULE)) {
							tinyeiffel.compiler.ASTDefaut tmp47_AST = null;
							tmp47_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp47_AST);
							match(VIRGULE);
							t2=type2();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								t.addElement(t2);
							}
						}
						else {
							break _loop87;
						}
						
					} while (true);
					}
					break;
				}
				case CROCHETF:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				cf = LT(1);
				cf_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(cf);
				astFactory.addASTChild(currentAST, cf_AST);
				match(CROCHETF);
				break;
			}
			case OBSOLETE:
			case FEATURE:
			case VIRGULE:
			case INHERIT:
			case CREATION:
			case CROCHETF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
							res=new TypeSimple(expanded,id.getText(),t);
							res.set_token(remplitToken(id),remplitToken(co),remplitToken(cf));
						
			}
			decl_type_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le type declare",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = decl_type_AST;
		return res;
	}
	
	public final NomFeature  nom_feature0() throws RecognitionException, TokenStreamException {
		NomFeature res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut nom_feature0_AST = null;
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp48_AST = null;
			tmp48_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(LPAREN);
			res=nom_feature();
			astFactory.addASTChild(currentAST, returnAST);
			tinyeiffel.compiler.ASTDefaut tmp49_AST = null;
			tmp49_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp49_AST);
			match(RPAREN);
			nom_feature0_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = nom_feature0_AST;
		return res;
	}
	
	public final Type  type2() throws RecognitionException, TokenStreamException {
		Type res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut type2_AST = null;
		Token  id = null;
		tinyeiffel.compiler.ASTDefaut id_AST = null;
		Token  f = null;
		tinyeiffel.compiler.ASTDefaut f_AST = null;
		
		Vector t=new Vector();
		Type t2;
		boolean expanded=false;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case EXPANDED:
			{
				tinyeiffel.compiler.ASTDefaut tmp50_AST = null;
				tmp50_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp50_AST);
				match(EXPANDED);
				if ( inputState.guessing==0 ) {
					expanded=true;
				}
				break;
			}
			case ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			id = LT(1);
			id_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			{
			switch ( LA(1)) {
			case FLECHED:
			{
				f = LT(1);
				f_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f);
				astFactory.addASTChild(currentAST, f_AST);
				match(FLECHED);
				t2=decl_type();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					t.addElement(t2);
				}
				break;
			}
			case VIRGULE:
			case CROCHETF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
							res=new TypeSimple(expanded,id.getText(),t);res.is_from=true;
							res.set_token(remplitToken(id),remplitToken(f));
						
			}
			type2_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le type",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = type2_AST;
		return res;
	}
	
	public final Expr  expr() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr_AST = null;
		
		Expr e1=null;
		
		
		try {      // for error handling
			e1=expr17();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			expr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr_AST;
		return e;
	}
	
	public final Type  type0() throws RecognitionException, TokenStreamException {
		Type res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut type0_AST = null;
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp51_AST = null;
			tmp51_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp51_AST);
			match(LPAREN);
			res=type();
			astFactory.addASTChild(currentAST, returnAST);
			tinyeiffel.compiler.ASTDefaut tmp52_AST = null;
			tmp52_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(RPAREN);
			type0_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = type0_AST;
		return res;
	}
	
	public final Vector  decl_param() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut decl_param_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		Token  j = null;
		tinyeiffel.compiler.ASTDefaut j_AST = null;
		
		DeclareVar p;
		Vector v=new Vector();
		int i0;
		Type t;
		
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp53_AST = null;
			tmp53_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp53_AST);
			match(LPAREN);
			{
			switch ( LA(1)) {
			case ID:
			{
				{
				int _cnt116=0;
				_loop116:
				do {
					if ((LA(1)==ID)) {
						i = LT(1);
						i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
						astFactory.addASTChild(currentAST, i_AST);
						match(ID);
						if ( inputState.guessing==0 ) {
							v=new Vector();v.addElement(i);
						}
						{
						_loop114:
						do {
							if ((LA(1)==VIRGULE)) {
								match(VIRGULE);
								j = LT(1);
								j_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(j);
								astFactory.addASTChild(currentAST, j_AST);
								match(ID);
								if ( inputState.guessing==0 ) {
									v.addElement(j);
								}
							}
							else {
								break _loop114;
							}
							
						} while (true);
						}
						tinyeiffel.compiler.ASTDefaut tmp55_AST = null;
						tmp55_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp55_AST);
						match(DOUBLE_POINT);
						t=type();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							
												for(i0=0;i0<v.size();i0++)
												{
													p=new DeclareVar(((Token)v.elementAt(i0)).getText(),t);
													p.set_token(remplitToken((Token)v.elementAt(i0)));
													res.addElement(p);
												}
												ajoute_type(t);
											
						}
						{
						switch ( LA(1)) {
						case SEMI:
						{
							tinyeiffel.compiler.ASTDefaut tmp56_AST = null;
							tmp56_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp56_AST);
							match(SEMI);
							break;
						}
						case ID:
						case RPAREN:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
					}
					else {
						if ( _cnt116>=1 ) { break _loop116; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt116++;
				} while (true);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tinyeiffel.compiler.ASTDefaut tmp57_AST = null;
			tmp57_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp57_AST);
			match(RPAREN);
			decl_param_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la declaration des parametres",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = decl_param_AST;
		return res;
	}
	
	public final Feature  corps() throws RecognitionException, TokenStreamException {
		Feature res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut corps_AST = null;
		Token  obs = null;
		tinyeiffel.compiler.ASTDefaut obs_AST = null;
		Token  req = null;
		tinyeiffel.compiler.ASTDefaut req_AST = null;
		Token  ens = null;
		tinyeiffel.compiler.ASTDefaut ens_AST = null;
		Token  resc = null;
		tinyeiffel.compiler.ASTDefaut resc_AST = null;
		Token  uni = null;
		tinyeiffel.compiler.ASTDefaut uni_AST = null;
		
		Feature c=null;
		Expr e;
		Vector l1=new Vector(),l2=new Vector(),l3=new Vector(),l4=new Vector();
		Instr inst=null;
		Chaine obsolete=null;
		int i,debut_commentaire=dernier_commentaire(),fin_commentaire=-1;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case DEFERRED:
			case OBSOLETE:
			case REQUIRE:
			case LOCAL:
			case DO:
			case ONCE:
			case EXTERNAL:
			{
				{
				switch ( LA(1)) {
				case OBSOLETE:
				{
					obs = LT(1);
					obs_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(obs);
					astFactory.addASTChild(currentAST, obs_AST);
					match(OBSOLETE);
					if ( inputState.guessing==0 ) {
						
									if(last_feature2==-1) last_feature2=lexer.donne_dernier();
									debut_commentaire=dernier_commentaire();
									
					}
					obsolete=string();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
					}
					break;
				}
				case DEFERRED:
				case REQUIRE:
				case LOCAL:
				case DO:
				case ONCE:
				case EXTERNAL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case REQUIRE:
				{
					req = LT(1);
					req_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(req);
					astFactory.addASTChild(currentAST, req_AST);
					match(REQUIRE);
					{
					switch ( LA(1)) {
					case ELSE:
					{
						tinyeiffel.compiler.ASTDefaut tmp58_AST = null;
						tmp58_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp58_AST);
						match(ELSE);
						break;
					}
					case DEFERRED:
					case ID:
					case STRING:
					case LPAREN:
					case LOCAL:
					case DO:
					case ONCE:
					case EXTERNAL:
					case INT:
					case REAL:
					case CHAR:
					case PLUS:
					case MOINS:
					case FREEOP:
					case OLD:
					case NOT:
					case DOLLARD:
					case DEBUT_ARRAY:
					case TRUE:
					case FALSE:
					case STRING2:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						
									if(last_feature2==-1) last_feature2=lexer.donne_dernier();
									if(fin_commentaire==-1) fin_commentaire=dernier_commentaire();
									
					}
					l1=liste_assert();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case DEFERRED:
				case LOCAL:
				case DO:
				case ONCE:
				case EXTERNAL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					
								if(last_feature2==-1) last_feature2=lexer.donne_dernier();
								if(fin_commentaire==-1) fin_commentaire=dernier_commentaire();
								
				}
				c=corps2();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case ENSURE:
				{
					ens = LT(1);
					ens_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(ens);
					astFactory.addASTChild(currentAST, ens_AST);
					match(ENSURE);
					{
					switch ( LA(1)) {
					case THEN:
					{
						tinyeiffel.compiler.ASTDefaut tmp59_AST = null;
						tmp59_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp59_AST);
						match(THEN);
						break;
					}
					case END:
					case ID:
					case STRING:
					case LPAREN:
					case RESCUE:
					case INT:
					case REAL:
					case CHAR:
					case PLUS:
					case MOINS:
					case FREEOP:
					case OLD:
					case NOT:
					case DOLLARD:
					case DEBUT_ARRAY:
					case TRUE:
					case FALSE:
					case STRING2:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					l2=liste_assert();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case END:
				case RESCUE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case RESCUE:
				{
					resc = LT(1);
					resc_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(resc);
					astFactory.addASTChild(currentAST, resc_AST);
					match(RESCUE);
					{
					_loop127:
					do {
						if ((_tokenSet_9.member(LA(1)))) {
							inst=instr();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								l3.addElement(inst);
							}
							{
							_loop126:
							do {
								if ((LA(1)==SEMI)) {
									match(SEMI);
								}
								else {
									break _loop126;
								}
								
							} while (true);
							}
						}
						else {
							break _loop127;
						}
						
					} while (true);
					}
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				tinyeiffel.compiler.ASTDefaut tmp61_AST = null;
				tmp61_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(END);
				if ( inputState.guessing==0 ) {
					
							res=c;
							for(i=last_feature1+1;i<=last_feature2;i++)
							{
								//System.out.println((i-last_feature1-1)+":"+lexer.liste_commentaire.elementAt(i));
								l4.addElement(lexer.liste_commentaire.elementAt(i));
							}
							Vector l5=null;
							if(fin_commentaire>debut_commentaire)
							{
								//System.out.println("comment:"+debut_commentaire+"<"+fin_commentaire);
								l5=new Vector();
								for(i=debut_commentaire+1;i<=fin_commentaire;i++)
								{
									//System.out.println(i+")"+((Commentaire)lexer.liste_commentaire.elementAt(i)).str+"!");
									l5.addElement(lexer.liste_commentaire.elementAt(i));
								}
								//System.out.println("fin3");
							}
							if(res!=null)
							{
							res.set_require_ensure(l1,l2,l3,obsolete,l4,l5);
							res.set_token(remplitToken(obs),remplitToken(req),
								remplitToken(ens),remplitToken(resc));
							}
							//System.out.println("suite");
						
				}
				corps_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case UNIQUE:
			{
				uni = LT(1);
				uni_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(uni);
				astFactory.addASTChild(currentAST, uni_AST);
				match(UNIQUE);
				if ( inputState.guessing==0 ) {
					
							res=new FeatureUnique();
							res.set_token(remplitToken(uni));
						
				}
				corps_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case ID:
			case STRING:
			case LPAREN:
			case INT:
			case REAL:
			case CHAR:
			case PLUS:
			case MOINS:
			case FREEOP:
			case OLD:
			case NOT:
			case DOLLARD:
			case DEBUT_ARRAY:
			case TRUE:
			case FALSE:
			case STRING2:
			{
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
							res=new FeatureExpr();
							((FeatureExpr)res).expr=e;
						
				}
				corps_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'attribut",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = corps_AST;
		return res;
	}
	
	public final Feature  corps2() throws RecognitionException, TokenStreamException {
		Feature res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut corps2_AST = null;
		Token  i1 = null;
		tinyeiffel.compiler.ASTDefaut i1_AST = null;
		Token  i2 = null;
		tinyeiffel.compiler.ASTDefaut i2_AST = null;
		Token  once2 = null;
		tinyeiffel.compiler.ASTDefaut once2_AST = null;
		Token  once1 = null;
		tinyeiffel.compiler.ASTDefaut once1_AST = null;
		Token  def = null;
		tinyeiffel.compiler.ASTDefaut def_AST = null;
		Token  ext = null;
		tinyeiffel.compiler.ASTDefaut ext_AST = null;
		Token  alias = null;
		tinyeiffel.compiler.ASTDefaut alias_AST = null;
		
		boolean once=false;
		Vector v1,v2,v3,liste_instr;
		DeclareVar p;
		Type t;
		int j;
		v2=new Vector();
		v1=new Vector();
		Instr inst;
		liste_instr=new Vector();
		Chaine c1=null,c2=null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LOCAL:
			case DO:
			case ONCE:
			{
				{
				switch ( LA(1)) {
				case LOCAL:
				{
					tinyeiffel.compiler.ASTDefaut tmp62_AST = null;
					tmp62_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp62_AST);
					match(LOCAL);
					{
					_loop136:
					do {
						if ((LA(1)==ID)) {
							if ( inputState.guessing==0 ) {
								v1=new Vector();
							}
							{
							i1 = LT(1);
							i1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i1);
							astFactory.addASTChild(currentAST, i1_AST);
							match(ID);
							if ( inputState.guessing==0 ) {
								v1.addElement(i1);
							}
							{
							_loop133:
							do {
								if ((LA(1)==VIRGULE)) {
									tinyeiffel.compiler.ASTDefaut tmp63_AST = null;
									tmp63_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp63_AST);
									match(VIRGULE);
									i2 = LT(1);
									i2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i2);
									astFactory.addASTChild(currentAST, i2_AST);
									match(ID);
									if ( inputState.guessing==0 ) {
										v1.addElement(i2);
									}
								}
								else {
									break _loop133;
								}
								
							} while (true);
							}
							}
							tinyeiffel.compiler.ASTDefaut tmp64_AST = null;
							tmp64_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp64_AST);
							match(DOUBLE_POINT);
							t=type();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								
													for(j=0;j<v1.size();j++)
													{
														p=new DeclareVar(((Token)v1.elementAt(j)).getText(),t);
														p.set_token(remplitToken((Token)v1.elementAt(j)));
														v2.addElement(p);
														//System.out.println("Suite17");
													}
													ajoute_type(t);
													//{System.out.println("Suite18");}
												
							}
							{
							_loop135:
							do {
								if ((LA(1)==SEMI)) {
									match(SEMI);
								}
								else {
									break _loop135;
								}
								
							} while (true);
							}
						}
						else {
							break _loop136;
						}
						
					} while (true);
					}
					break;
				}
				case DO:
				case ONCE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case DO:
				{
					once2 = LT(1);
					once2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(once2);
					astFactory.addASTChild(currentAST, once2_AST);
					match(DO);
					if ( inputState.guessing==0 ) {
						once=false;
					}
					break;
				}
				case ONCE:
				{
					once1 = LT(1);
					once1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(once1);
					astFactory.addASTChild(currentAST, once1_AST);
					match(ONCE);
					if ( inputState.guessing==0 ) {
						once=true;
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop141:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						inst=instr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste_instr.addElement(inst);
						}
						{
						_loop140:
						do {
							if ((LA(1)==SEMI)) {
								match(SEMI);
							}
							else {
								break _loop140;
							}
							
						} while (true);
						}
					}
					else {
						break _loop141;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					
							res=new FeatureRoutine(once,liste_instr,v2);
							res.tdo=(once1==null)?remplitToken(once2):remplitToken(once1);
							//((FeatureRoutine)res).once=once;
							//((FeatureRoutine)res).liste_instr=liste_instr;
							//res=new FeatureDeferred();
						
				}
				corps2_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case DEFERRED:
			{
				def = LT(1);
				def_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(def);
				astFactory.addASTChild(currentAST, def_AST);
				match(DEFERRED);
				if ( inputState.guessing==0 ) {
					res=new FeatureDeferred();res.tdeferred=remplitToken(def);
				}
				corps2_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case EXTERNAL:
			{
				ext = LT(1);
				ext_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(ext);
				astFactory.addASTChild(currentAST, ext_AST);
				match(EXTERNAL);
				c1=string();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case ALIAS:
				{
					alias = LT(1);
					alias_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(alias);
					astFactory.addASTChild(currentAST, alias_AST);
					match(ALIAS);
					c2=string();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case END:
				case ENSURE:
				case RESCUE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					
									res=new FeatureExternal(c1,c2);
									res.texternal=remplitToken(ext);
									res.talias=remplitToken(alias);
								
				}
				corps2_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans le corps de l'attribut",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = corps2_AST;
		return res;
	}
	
	public final Instr  instr() throws RecognitionException, TokenStreamException {
		Instr res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut instr_AST = null;
		Token  cur1 = null;
		tinyeiffel.compiler.ASTDefaut cur1_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		Token  aff = null;
		tinyeiffel.compiler.ASTDefaut aff_AST = null;
		Token  cur2 = null;
		tinyeiffel.compiler.ASTDefaut cur2_AST = null;
		Token  j = null;
		tinyeiffel.compiler.ASTDefaut j_AST = null;
		Token  taff = null;
		tinyeiffel.compiler.ASTDefaut taff_AST = null;
		Token  k = null;
		tinyeiffel.compiler.ASTDefaut k_AST = null;
		Token  in = null;
		tinyeiffel.compiler.ASTDefaut in_AST = null;
		Token  r = null;
		tinyeiffel.compiler.ASTDefaut r_AST = null;
		Token  c = null;
		tinyeiffel.compiler.ASTDefaut c_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		Token  l = null;
		tinyeiffel.compiler.ASTDefaut l_AST = null;
		Token  fro = null;
		tinyeiffel.compiler.ASTDefaut fro_AST = null;
		Token  inv = null;
		tinyeiffel.compiler.ASTDefaut inv_AST = null;
		Token  var = null;
		tinyeiffel.compiler.ASTDefaut var_AST = null;
		Token  unt = null;
		tinyeiffel.compiler.ASTDefaut unt_AST = null;
		Token  loop = null;
		tinyeiffel.compiler.ASTDefaut loop_AST = null;
		Token  en3 = null;
		tinyeiffel.compiler.ASTDefaut en3_AST = null;
		Token  if2 = null;
		tinyeiffel.compiler.ASTDefaut if2_AST = null;
		Token  then1 = null;
		tinyeiffel.compiler.ASTDefaut then1_AST = null;
		Token  eif = null;
		tinyeiffel.compiler.ASTDefaut eif_AST = null;
		Token  then2 = null;
		tinyeiffel.compiler.ASTDefaut then2_AST = null;
		Token  else0 = null;
		tinyeiffel.compiler.ASTDefaut else0_AST = null;
		Token  excl = null;
		tinyeiffel.compiler.ASTDefaut excl_AST = null;
		Token  excl2 = null;
		tinyeiffel.compiler.ASTDefaut excl2_AST = null;
		Token  m = null;
		tinyeiffel.compiler.ASTDefaut m_AST = null;
		Token  p2 = null;
		tinyeiffel.compiler.ASTDefaut p2_AST = null;
		Token  n = null;
		tinyeiffel.compiler.ASTDefaut n_AST = null;
		Token  r2 = null;
		tinyeiffel.compiler.ASTDefaut r2_AST = null;
		Token  insp = null;
		tinyeiffel.compiler.ASTDefaut insp_AST = null;
		Token  when = null;
		tinyeiffel.compiler.ASTDefaut when_AST = null;
		Token  then = null;
		tinyeiffel.compiler.ASTDefaut then_AST = null;
		Token  else2 = null;
		tinyeiffel.compiler.ASTDefaut else2_AST = null;
		Token  ch = null;
		tinyeiffel.compiler.ASTDefaut ch_AST = null;
		Token  en2 = null;
		tinyeiffel.compiler.ASTDefaut en2_AST = null;
		Token  d = null;
		tinyeiffel.compiler.ASTDefaut d_AST = null;
		Token  en = null;
		tinyeiffel.compiler.ASTDefaut en_AST = null;
		
		Expr e=null,e2=null;
		Vector liste=new Vector(),liste2=new Vector(),liste3=new Vector();
		Instr inst=null,derniere_instr=null;
		Type t=null;
		//Instr_Appel der_appel=null
		Chaine s1,s2;
		boolean current_present=false;
		//{System.out.println("Suite40");}
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case FROM:
			{
				fro = LT(1);
				fro_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(fro);
				astFactory.addASTChild(currentAST, fro_AST);
				match(FROM);
				{
				_loop167:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						inst=instr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste.addElement(inst);
						}
						{
						_loop166:
						do {
							if ((LA(1)==SEMI)) {
								match(SEMI);
							}
							else {
								break _loop166;
							}
							
						} while (true);
						}
					}
					else {
						break _loop167;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case INVARIANT:
				{
					inv = LT(1);
					inv_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(inv);
					astFactory.addASTChild(currentAST, inv_AST);
					match(INVARIANT);
					liste3=liste_assert();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case VARIANT:
				case UNTIL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case VARIANT:
				{
					var = LT(1);
					var_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(var);
					astFactory.addASTChild(currentAST, var_AST);
					match(VARIANT);
					e2=expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case UNTIL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				unt = LT(1);
				unt_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(unt);
				astFactory.addASTChild(currentAST, unt_AST);
				match(UNTIL);
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				loop = LT(1);
				loop_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(loop);
				astFactory.addASTChild(currentAST, loop_AST);
				match(LOOP);
				{
				_loop173:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						inst=instr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste2.addElement(inst);
						}
						{
						_loop172:
						do {
							if ((LA(1)==SEMI)) {
								match(SEMI);
							}
							else {
								break _loop172;
							}
							
						} while (true);
						}
					}
					else {
						break _loop173;
					}
					
				} while (true);
				}
				en3 = LT(1);
				en3_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(en3);
				astFactory.addASTChild(currentAST, en3_AST);
				match(END);
				if ( inputState.guessing==0 ) {
					
								//{System.out.println("Suite23");}
								res=new Instr_Loop(e,liste,liste2,liste3,e2);
								((Instr_Loop)res).set_token(remplitToken(fro),remplitToken(unt),
									remplitToken(inv),remplitToken(var),remplitToken(loop),
									remplitToken(en3));
							
				}
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case IF:
			{
				if2 = LT(1);
				if2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(if2);
				astFactory.addASTChild(currentAST, if2_AST);
				match(IF);
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				then1 = LT(1);
				then1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(then1);
				astFactory.addASTChild(currentAST, then1_AST);
				match(THEN);
				{
				_loop177:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						inst=instr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste.addElement(inst);
						}
						{
						_loop176:
						do {
							if ((LA(1)==SEMI)) {
								match(SEMI);
							}
							else {
								break _loop176;
							}
							
						} while (true);
						}
					}
					else {
						break _loop177;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					
								//{System.out.println("Suite25");}
								res=new Instr_If(e,liste);
								((Instr_If)res).tif=remplitToken(if2);
								((Instr_If)res).tthen=remplitToken(then1);
								derniere_instr=res;
							
				}
				{
				_loop183:
				do {
					if ((LA(1)==ELSEIF)) {
						eif = LT(1);
						eif_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(eif);
						astFactory.addASTChild(currentAST, eif_AST);
						match(ELSEIF);
						e2=expr();
						astFactory.addASTChild(currentAST, returnAST);
						then2 = LT(1);
						then2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(then2);
						astFactory.addASTChild(currentAST, then2_AST);
						match(THEN);
						if ( inputState.guessing==0 ) {
							liste=new Vector();
						}
						{
						_loop182:
						do {
							if ((_tokenSet_9.member(LA(1)))) {
								inst=instr();
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									liste.addElement(inst);
								}
								{
								_loop181:
								do {
									if ((LA(1)==SEMI)) {
										match(SEMI);
									}
									else {
										break _loop181;
									}
									
								} while (true);
								}
							}
							else {
								break _loop182;
							}
							
						} while (true);
						}
						if ( inputState.guessing==0 ) {
							
										inst=new Instr_ElseIf(e2,liste);
										((Instr_ElseIf)inst).telseif=remplitToken(eif);
										((Instr_ElseIf)inst).tthen=remplitToken(then2);
										((Suite)derniere_instr).setSuivant(inst);
										derniere_instr=inst;
									
						}
					}
					else {
						break _loop183;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case ELSE:
				{
					else0 = LT(1);
					else0_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(else0);
					astFactory.addASTChild(currentAST, else0_AST);
					match(ELSE);
					if ( inputState.guessing==0 ) {
						liste=new Vector();
					}
					{
					_loop188:
					do {
						if ((_tokenSet_9.member(LA(1)))) {
							inst=instr();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								liste.addElement(inst);
							}
							{
							_loop187:
							do {
								if ((LA(1)==SEMI)) {
									match(SEMI);
								}
								else {
									break _loop187;
								}
								
							} while (true);
							}
						}
						else {
							break _loop188;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						
									inst=new Instr_Else(liste);
									((Instr_Else)inst).telse=remplitToken(else0);
									((Suite)derniere_instr).setSuivant(inst);
									derniere_instr=inst;
								
					}
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				tinyeiffel.compiler.ASTDefaut tmp72_AST = null;
				tmp72_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp72_AST);
				match(END);
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case EXCLAMATION:
			{
				excl = LT(1);
				excl_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(excl);
				astFactory.addASTChild(currentAST, excl_AST);
				match(EXCLAMATION);
				{
				switch ( LA(1)) {
				case EXPANDED:
				case ID:
				case LIKE:
				{
					t=type();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case EXCLAMATION:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				excl2 = LT(1);
				excl2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(excl2);
				astFactory.addASTChild(currentAST, excl2_AST);
				match(EXCLAMATION);
				m = LT(1);
				m_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(m);
				astFactory.addASTChild(currentAST, m_AST);
				match(ID);
				{
				switch ( LA(1)) {
				case POINT:
				{
					p2 = LT(1);
					p2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p2);
					astFactory.addASTChild(currentAST, p2_AST);
					match(POINT);
					n = LT(1);
					n_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n);
					astFactory.addASTChild(currentAST, n_AST);
					match(ID);
					{
					switch ( LA(1)) {
					case LPAREN:
					{
						liste=exec_param();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case SEMI:
					case INVARIANT:
					case END:
					case ID:
					case STRING:
					case ELSE:
					case ENSURE:
					case RESCUE:
					case LPARENT:
					case INT:
					case REAL:
					case CHAR:
					case FROM:
					case VARIANT:
					case UNTIL:
					case IF:
					case ELSEIF:
					case EXCLAMATION:
					case RETRY:
					case INSPECT:
					case WHEN:
					case CHECK:
					case DEBUG:
					case STRING2:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case SEMI:
				case INVARIANT:
				case END:
				case ID:
				case STRING:
				case ELSE:
				case ENSURE:
				case RESCUE:
				case LPARENT:
				case INT:
				case REAL:
				case CHAR:
				case FROM:
				case VARIANT:
				case UNTIL:
				case IF:
				case ELSEIF:
				case EXCLAMATION:
				case RETRY:
				case INSPECT:
				case WHEN:
				case CHECK:
				case DEBUG:
				case STRING2:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					
								if(n!=null)
									res=new Instr_Creation(t,m.getText(),n.getText(),liste);
								else
									res=new Instr_Creation(t,m.getText(),null,liste);
								ajoute_type(t);
								((Instr_Creation)res).texcl1=remplitToken(excl);
								((Instr_Creation)res).texcl2=remplitToken(excl2);
								((Instr_Creation)res).tid=remplitToken(m);
								((Instr_Creation)res).tid2=remplitToken(n);
								((Instr_Creation)res).tpoint=remplitToken(p2);
							
				}
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case RETRY:
			{
				r2 = LT(1);
				r2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(r2);
				astFactory.addASTChild(currentAST, r2_AST);
				match(RETRY);
				if ( inputState.guessing==0 ) {
					
								res=new Instr_Retry();
								((Instr_Retry)res).tretry=remplitToken(r2);
							
				}
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case INSPECT:
			{
				insp = LT(1);
				insp_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(insp);
				astFactory.addASTChild(currentAST, insp_AST);
				match(INSPECT);
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				int _cnt199=0;
				_loop199:
				do {
					if ((LA(1)==WHEN)) {
						when = LT(1);
						when_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(when);
						astFactory.addASTChild(currentAST, when_AST);
						match(WHEN);
						if ( inputState.guessing==0 ) {
							liste=new Vector();liste3=new Vector();
						}
						liste2=expr_when();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste.addElement(liste2);
						}
						{
						_loop194:
						do {
							if ((LA(1)==VIRGULE)) {
								tinyeiffel.compiler.ASTDefaut tmp73_AST = null;
								tmp73_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp73_AST);
								match(VIRGULE);
								liste2=expr_when();
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									liste.addElement(liste2);
								}
							}
							else {
								break _loop194;
							}
							
						} while (true);
						}
						then = LT(1);
						then_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(then);
						astFactory.addASTChild(currentAST, then_AST);
						match(THEN);
						{
						_loop198:
						do {
							if ((_tokenSet_9.member(LA(1)))) {
								inst=instr();
								astFactory.addASTChild(currentAST, returnAST);
								if ( inputState.guessing==0 ) {
									liste3.addElement(inst);
								}
								{
								_loop197:
								do {
									if ((LA(1)==SEMI)) {
										match(SEMI);
									}
									else {
										break _loop197;
									}
									
								} while (true);
								}
							}
							else {
								break _loop198;
							}
							
						} while (true);
						}
						if ( inputState.guessing==0 ) {
							
										inst=new Instr_Inspect(e,liste,liste3);
										((Instr_Inspect)inst).tinspect=remplitToken(insp);
										((Instr_Inspect)inst).twhen=remplitToken(when);
										((Instr_Inspect)inst).tthen=remplitToken(then);
										if(derniere_instr==null)
										{
											derniere_instr=res=inst;
										}
										else
										{
											((Suite)derniere_instr).setSuivant(inst);
											derniere_instr=inst;
										}
									
						}
					}
					else {
						if ( _cnt199>=1 ) { break _loop199; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt199++;
				} while (true);
				}
				{
				switch ( LA(1)) {
				case ELSE:
				{
					else2 = LT(1);
					else2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(else2);
					astFactory.addASTChild(currentAST, else2_AST);
					match(ELSE);
					if ( inputState.guessing==0 ) {
						liste=new Vector();liste3=new Vector();
					}
					{
					_loop204:
					do {
						if ((_tokenSet_9.member(LA(1)))) {
							inst=instr();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								liste3.addElement(inst);
							}
							{
							_loop203:
							do {
								if ((LA(1)==SEMI)) {
									match(SEMI);
								}
								else {
									break _loop203;
								}
								
							} while (true);
							}
						}
						else {
							break _loop204;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						
									inst=new Instr_Inspect(e,liste,liste3);
									((Instr_Inspect)inst).tinspect=remplitToken(insp);
									((Instr_Inspect)inst).twhen=remplitToken(else2);
									//assert(derniere_instr!=null)
									((Suite)derniere_instr).setSuivant(inst);
									derniere_instr=inst;
								
					}
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				tinyeiffel.compiler.ASTDefaut tmp76_AST = null;
				tmp76_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(END);
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case CHECK:
			{
				ch = LT(1);
				ch_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(ch);
				astFactory.addASTChild(currentAST, ch_AST);
				match(CHECK);
				liste=liste_assert();
				astFactory.addASTChild(currentAST, returnAST);
				en2 = LT(1);
				en2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(en2);
				astFactory.addASTChild(currentAST, en2_AST);
				match(END);
				if ( inputState.guessing==0 ) {
					
								res=new Instr_Check(liste);
								((Instr_Check)res).tcheck=remplitToken(ch);
								((Instr_Check)res).tend=remplitToken(en2);
							
				}
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case DEBUG:
			{
				d = LT(1);
				d_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d);
				astFactory.addASTChild(currentAST, d_AST);
				match(DEBUG);
				{
				switch ( LA(1)) {
				case LPAREN:
				{
					tinyeiffel.compiler.ASTDefaut tmp77_AST = null;
					tmp77_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp77_AST);
					match(LPAREN);
					s1=string();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						liste2.addElement(s1);
					}
					{
					_loop207:
					do {
						if ((LA(1)==VIRGULE)) {
							tinyeiffel.compiler.ASTDefaut tmp78_AST = null;
							tmp78_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp78_AST);
							match(VIRGULE);
							s2=string();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								liste2.addElement(s2);
							}
						}
						else {
							break _loop207;
						}
						
					} while (true);
					}
					tinyeiffel.compiler.ASTDefaut tmp79_AST = null;
					tmp79_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp79_AST);
					match(RPAREN);
					break;
				}
				case END:
				case ID:
				case STRING:
				case LPARENT:
				case INT:
				case REAL:
				case CHAR:
				case FROM:
				case IF:
				case EXCLAMATION:
				case RETRY:
				case INSPECT:
				case CHECK:
				case DEBUG:
				case STRING2:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop211:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						inst=instr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							liste.addElement(inst);
						}
						{
						_loop210:
						do {
							if ((LA(1)==SEMI)) {
								match(SEMI);
							}
							else {
								break _loop210;
							}
							
						} while (true);
						}
					}
					else {
						break _loop211;
					}
					
				} while (true);
				}
				en = LT(1);
				en_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(en);
				astFactory.addASTChild(currentAST, en_AST);
				match(END);
				if ( inputState.guessing==0 ) {
					
								res=new Instr_Debug(liste2,liste);
								((Instr_Debug)res).tdebug=remplitToken(d);
								((Instr_Debug)res).tend=remplitToken(en);
							
				}
				instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched153 = false;
				if (((LA(1)==ID) && (LA(2)==POINT||LA(2)==AFFECT) && (_tokenSet_1.member(LA(3))) && (_tokenSet_10.member(LA(4))))) {
					int _m153 = mark();
					synPredMatched153 = true;
					inputState.guessing++;
					try {
						{
						{
						if ((LA(1)==ID) && (LA(2)==POINT)) {
							match(ID);
							match(POINT);
						}
						else if ((LA(1)==ID) && (LA(2)==AFFECT)) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						match(ID);
						match(AFFECT);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched153 = false;
					}
					rewind(_m153);
					inputState.guessing--;
				}
				if ( synPredMatched153 ) {
					if ( inputState.guessing==0 ) {
						current_present=false;
					}
					{
					if ((LA(1)==ID) && (LA(2)==POINT)) {
						cur1 = LT(1);
						cur1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(cur1);
						astFactory.addASTChild(currentAST, cur1_AST);
						match(ID);
						tinyeiffel.compiler.ASTDefaut tmp81_AST = null;
						tmp81_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp81_AST);
						match(POINT);
						if ( inputState.guessing==0 ) {
							current_present=true;
						}
					}
					else if ((LA(1)==ID) && (LA(2)==AFFECT)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					i = LT(1);
					i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
					astFactory.addASTChild(currentAST, i_AST);
					match(ID);
					aff = LT(1);
					aff_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(aff);
					astFactory.makeASTRoot(currentAST, aff_AST);
					match(AFFECT);
					e=expr();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						
										//{System.out.println("Suite22");}
										res=new Instr_Affect(i.getText(),e);
										((Instr_Affect)res).set_token(remplitToken(i),remplitToken(aff));
										if(current_present)
										{
											( (Instr_Affect)res).tcurrent=remplitToken(cur1);
											((Instr_Affect)res).var_current=cur1.getText();
										}
									
					}
					instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				}
				else {
					boolean synPredMatched157 = false;
					if (((LA(1)==ID) && (LA(2)==POINT||LA(2)==TENTATIVE_AFFECT) && (_tokenSet_1.member(LA(3))) && (_tokenSet_11.member(LA(4))))) {
						int _m157 = mark();
						synPredMatched157 = true;
						inputState.guessing++;
						try {
							{
							{
							if ((LA(1)==ID) && (LA(2)==POINT)) {
								match(ID);
								match(POINT);
							}
							else if ((LA(1)==ID) && (LA(2)==TENTATIVE_AFFECT)) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
							
							}
							match(ID);
							match(TENTATIVE_AFFECT);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched157 = false;
						}
						rewind(_m157);
						inputState.guessing--;
					}
					if ( synPredMatched157 ) {
						if ( inputState.guessing==0 ) {
							current_present=false;
						}
						{
						if ((LA(1)==ID) && (LA(2)==POINT)) {
							cur2 = LT(1);
							cur2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(cur2);
							astFactory.addASTChild(currentAST, cur2_AST);
							match(ID);
							tinyeiffel.compiler.ASTDefaut tmp82_AST = null;
							tmp82_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp82_AST);
							match(POINT);
							if ( inputState.guessing==0 ) {
								current_present=true;
							}
						}
						else if ((LA(1)==ID) && (LA(2)==TENTATIVE_AFFECT)) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						j = LT(1);
						j_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(j);
						astFactory.addASTChild(currentAST, j_AST);
						match(ID);
						taff = LT(1);
						taff_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(taff);
						astFactory.addASTChild(currentAST, taff_AST);
						match(TENTATIVE_AFFECT);
						e2=expr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							
											res=new Instr_TentAffect(j.getText(),e2);
											((Instr_TentAffect)res).set_token(remplitToken(j),remplitToken(taff));
											if(current_present)
											{
												((Instr_TentAffect)res).tcurrent=remplitToken(cur2);
												((Instr_TentAffect)res).var_current=cur2.getText();
											}
										
						}
						instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
					}
					else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_14.member(LA(3))) && (_tokenSet_15.member(LA(4)))) {
						{
						switch ( LA(1)) {
						case ID:
						{
							k = LT(1);
							k_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(k);
							astFactory.addASTChild(currentAST, k_AST);
							match(ID);
							if ( inputState.guessing==0 ) {
								liste=new Vector();
							}
							{
							switch ( LA(1)) {
							case LPAREN:
							{
								liste=exec_param();
								astFactory.addASTChild(currentAST, returnAST);
								break;
							}
							case SEMI:
							case INVARIANT:
							case END:
							case ID:
							case STRING:
							case ELSE:
							case ENSURE:
							case RESCUE:
							case POINT:
							case LPARENT:
							case INT:
							case REAL:
							case CHAR:
							case FROM:
							case VARIANT:
							case UNTIL:
							case IF:
							case ELSEIF:
							case EXCLAMATION:
							case RETRY:
							case INSPECT:
							case WHEN:
							case CHECK:
							case DEBUG:
							case STRING2:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							if ( inputState.guessing==0 ) {
								
											res=new Instr_Appel(k.getText(),liste);
											((Instr_Appel)res).tid=remplitToken(k);
											derniere_instr=res;
										
							}
							break;
						}
						case LPARENT:
						{
							tinyeiffel.compiler.ASTDefaut tmp83_AST = null;
							tmp83_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp83_AST);
							match(LPARENT);
							e=expr();
							astFactory.addASTChild(currentAST, returnAST);
							tinyeiffel.compiler.ASTDefaut tmp84_AST = null;
							tmp84_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp84_AST);
							match(RPARENT);
							if ( inputState.guessing==0 ) {
								
											res=new Instr_Appel(e);
											derniere_instr=res;
										
							}
							break;
						}
						case INT:
						{
							in = LT(1);
							in_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(in);
							astFactory.addASTChild(currentAST, in_AST);
							match(INT);
							if ( inputState.guessing==0 ) {
								
											e=new Expr_Entier(in.getText(),remplitToken(in));
											res=new Instr_Appel(e);
											derniere_instr=res;
										
							}
							break;
						}
						case STRING:
						case STRING2:
						{
							s1=string();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								
											e=new Expr_Chaine(s1);
											res=new Instr_Appel(e);
											derniere_instr=res;
										
							}
							break;
						}
						case REAL:
						{
							r = LT(1);
							r_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(r);
							astFactory.addASTChild(currentAST, r_AST);
							match(REAL);
							if ( inputState.guessing==0 ) {
								
											e=new Expr_Reel(r.getText(),remplitToken(r));
											res=new Instr_Appel(e);
											derniere_instr=res;
										
							}
							break;
						}
						case CHAR:
						{
							c = LT(1);
							c_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(c);
							astFactory.addASTChild(currentAST, c_AST);
							match(CHAR);
							if ( inputState.guessing==0 ) {
								
											e=new Expr_Car(c.getText(),remplitToken(c));
											res=new Instr_Appel(e);
											derniere_instr=res;
										
							}
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						{
						_loop163:
						do {
							if ((LA(1)==POINT)) {
								p = LT(1);
								p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
								astFactory.addASTChild(currentAST, p_AST);
								match(POINT);
								l = LT(1);
								l_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(l);
								astFactory.addASTChild(currentAST, l_AST);
								match(ID);
								if ( inputState.guessing==0 ) {
									liste=new Vector();
								}
								{
								switch ( LA(1)) {
								case LPAREN:
								{
									liste=exec_param();
									astFactory.addASTChild(currentAST, returnAST);
									break;
								}
								case SEMI:
								case INVARIANT:
								case END:
								case ID:
								case STRING:
								case ELSE:
								case ENSURE:
								case RESCUE:
								case POINT:
								case LPARENT:
								case INT:
								case REAL:
								case CHAR:
								case FROM:
								case VARIANT:
								case UNTIL:
								case IF:
								case ELSEIF:
								case EXCLAMATION:
								case RETRY:
								case INSPECT:
								case WHEN:
								case CHECK:
								case DEBUG:
								case STRING2:
								{
									break;
								}
								default:
								{
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
								}
								if ( inputState.guessing==0 ) {
									
													inst=new Instr_Appel(l.getText(),liste);
													((Instr_Appel)inst).tid=remplitToken(l);
													((Instr_Appel)inst).tpoint=remplitToken(p);
													((Suite)derniere_instr).setSuivant(inst);
													derniere_instr=inst;
												
								}
							}
							else {
								break _loop163;
							}
							
						} while (true);
						}
						instr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					
							logging.erreur(new ErreurSource("Erreur dans l'instruction"/*+ex*/,ex.getLine(),
										ex.getColumn(),ex.getFilename()));
						
				} else {
					throw ex;
				}
			}
			returnAST = instr_AST;
			return res;
		}
		
	public final Vector  liste_expr() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut liste_expr_AST = null;
		
		Expr e1,e2;
		
		
		try {      // for error handling
			e1=expr();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				res.addElement(e1);
			}
			{
			int _cnt145=0;
			_loop145:
			do {
				if ((LA(1)==SEMI)) {
					match(SEMI);
				}
				else {
					if ( _cnt145>=1 ) { break _loop145; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt145++;
			} while (true);
			}
			{
			_loop149:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					e2=expr();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						res.addElement(e2);
					}
					{
					int _cnt148=0;
					_loop148:
					do {
						if ((LA(1)==SEMI)) {
							match(SEMI);
						}
						else {
							if ( _cnt148>=1 ) { break _loop148; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt148++;
					} while (true);
					}
				}
				else {
					break _loop149;
				}
				
			} while (true);
			}
			liste_expr_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans la liste d'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = liste_expr_AST;
		return res;
	}
	
	public final Vector  exec_param() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut exec_param_AST = null;
		
		Expr e1=null,e2=null;
		
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp87_AST = null;
			tmp87_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp87_AST);
			match(LPAREN);
			{
			switch ( LA(1)) {
			case ID:
			case STRING:
			case LPAREN:
			case INT:
			case REAL:
			case CHAR:
			case PLUS:
			case MOINS:
			case FREEOP:
			case OLD:
			case NOT:
			case DOLLARD:
			case DEBUT_ARRAY:
			case TRUE:
			case FALSE:
			case STRING2:
			{
				e1=expr();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					res.addElement(e1);
				}
				{
				_loop277:
				do {
					if ((LA(1)==VIRGULE)) {
						tinyeiffel.compiler.ASTDefaut tmp88_AST = null;
						tmp88_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp88_AST);
						match(VIRGULE);
						e2=expr();
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							res.addElement(e2);
						}
					}
					else {
						break _loop277;
					}
					
				} while (true);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tinyeiffel.compiler.ASTDefaut tmp89_AST = null;
			tmp89_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp89_AST);
			match(RPAREN);
			exec_param_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans les parametres",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = exec_param_AST;
		return res;
	}
	
	public final Vector  expr_when() throws RecognitionException, TokenStreamException {
		Vector res=new Vector();
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr_when_AST = null;
		Token  d = null;
		tinyeiffel.compiler.ASTDefaut d_AST = null;
		
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				res.addElement(e1);
			}
			{
			switch ( LA(1)) {
			case DEUX_POINT:
			{
				d = LT(1);
				d_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d);
				astFactory.addASTChild(currentAST, d_AST);
				match(DEUX_POINT);
				e2=expr();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
									res.addElement(e2);
									opera=remplitToken(d);
									//res.oper=opera;
								
				}
				break;
			}
			case VIRGULE:
			case THEN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expr_when_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr_when_AST;
		return res;
	}
	
	public final Expr  expr0() throws RecognitionException, TokenStreamException {
		Expr res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr0_AST = null;
		
		try {      // for error handling
			tinyeiffel.compiler.ASTDefaut tmp90_AST = null;
			tmp90_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp90_AST);
			match(LPAREN);
			res=expr();
			astFactory.addASTChild(currentAST, returnAST);
			tinyeiffel.compiler.ASTDefaut tmp91_AST = null;
			tmp91_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp91_AST);
			match(RPAREN);
			expr0_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = expr0_AST;
		return res;
	}
	
	public final Expr  expr17() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr17_AST = null;
		Token  x = null;
		tinyeiffel.compiler.ASTDefaut x_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr18();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;/* System.out.println("Regle Xor");*/
			}
			{
			_loop219:
			do {
				if ((LA(1)==XOR||LA(1)==IMPLIES)) {
					{
					switch ( LA(1)) {
					case XOR:
					{
						x = LT(1);
						x_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(x);
						astFactory.makeASTRoot(currentAST, x_AST);
						match(XOR);
						if ( inputState.guessing==0 ) {
							op=Expr.Xor;opera=remplitToken(x);
						}
						break;
					}
					case IMPLIES:
					{
						i = LT(1);
						i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
						astFactory.makeASTRoot(currentAST, i_AST);
						match(IMPLIES);
						if ( inputState.guessing==0 ) {
							op=Expr.Implies;opera=remplitToken(i);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e2=expr18();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);
											e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop219;
				}
				
			} while (true);
			}
			expr17_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr17_AST;
		return e;
	}
	
	public final Expr  expr18() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr18_AST = null;
		Token  o = null;
		tinyeiffel.compiler.ASTDefaut o_AST = null;
		Token  e3 = null;
		tinyeiffel.compiler.ASTDefaut e3_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr19();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;/*System.out.println("regle Ou");*/
			}
			{
			_loop223:
			do {
				if ((LA(1)==OR)) {
					o = LT(1);
					o_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(o);
					astFactory.makeASTRoot(currentAST, o_AST);
					match(OR);
					if ( inputState.guessing==0 ) {
						op=Expr.Or;opera=remplitToken(o);
					}
					{
					switch ( LA(1)) {
					case ELSE:
					{
						e3 = LT(1);
						e3_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(e3);
						astFactory.addASTChild(currentAST, e3_AST);
						match(ELSE);
						if ( inputState.guessing==0 ) {
							op=Expr.Or_Else;opera=remplitToken(e3);
						}
						break;
					}
					case ID:
					case STRING:
					case LPAREN:
					case INT:
					case REAL:
					case CHAR:
					case PLUS:
					case MOINS:
					case FREEOP:
					case OLD:
					case NOT:
					case DOLLARD:
					case DEBUT_ARRAY:
					case TRUE:
					case FALSE:
					case STRING2:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e2=expr19();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop223;
				}
				
			} while (true);
			}
			expr18_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr18_AST;
		return e;
	}
	
	public final Expr  expr19() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr19_AST = null;
		Token  a = null;
		tinyeiffel.compiler.ASTDefaut a_AST = null;
		Token  a2 = null;
		tinyeiffel.compiler.ASTDefaut a2_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr20();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;/*System.out.println("Regle et");*/
			}
			{
			_loop229:
			do {
				if ((LA(1)==AND)) {
					{
					boolean synPredMatched228 = false;
					if (((LA(1)==AND) && (LA(2)==THEN))) {
						int _m228 = mark();
						synPredMatched228 = true;
						inputState.guessing++;
						try {
							{
							match(AND);
							match(THEN);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched228 = false;
						}
						rewind(_m228);
						inputState.guessing--;
					}
					if ( synPredMatched228 ) {
						a = LT(1);
						a_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(a);
						astFactory.addASTChild(currentAST, a_AST);
						match(AND);
						tinyeiffel.compiler.ASTDefaut tmp92_AST = null;
						tmp92_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp92_AST);
						match(THEN);
						if ( inputState.guessing==0 ) {
							op=Expr.And_Then;opera=remplitToken(a);
						}
					}
					else if ((LA(1)==AND) && (_tokenSet_1.member(LA(2)))) {
						a2 = LT(1);
						a2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(a2);
						astFactory.makeASTRoot(currentAST, a2_AST);
						match(AND);
						if ( inputState.guessing==0 ) {
							op=Expr.And;opera=remplitToken(a2);
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					e2=expr20();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop229;
				}
				
			} while (true);
			}
			expr19_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr19_AST;
		return e;
	}
	
	public final Expr  expr20() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr20_AST = null;
		Token  e3 = null;
		tinyeiffel.compiler.ASTDefaut e3_AST = null;
		Token  d = null;
		tinyeiffel.compiler.ASTDefaut d_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		Token  i2 = null;
		tinyeiffel.compiler.ASTDefaut i2_AST = null;
		Token  s = null;
		tinyeiffel.compiler.ASTDefaut s_AST = null;
		Token  s2 = null;
		tinyeiffel.compiler.ASTDefaut s2_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr21();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			_loop233:
			do {
				if (((LA(1) >= EGAL && LA(1) <= SUP))) {
					{
					switch ( LA(1)) {
					case EGAL:
					{
						e3 = LT(1);
						e3_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(e3);
						astFactory.makeASTRoot(currentAST, e3_AST);
						match(EGAL);
						if ( inputState.guessing==0 ) {
							op=Expr.Egal;opera=remplitToken(e3);
						}
						break;
					}
					case DIFF:
					{
						d = LT(1);
						d_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d);
						astFactory.makeASTRoot(currentAST, d_AST);
						match(DIFF);
						if ( inputState.guessing==0 ) {
							op=Expr.Diff;opera=remplitToken(d);
						}
						break;
					}
					case INFS:
					{
						i = LT(1);
						i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
						astFactory.makeASTRoot(currentAST, i_AST);
						match(INFS);
						if ( inputState.guessing==0 ) {
							op=Expr.Infs;opera=remplitToken(i);
						}
						break;
					}
					case INF:
					{
						i2 = LT(1);
						i2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i2);
						astFactory.makeASTRoot(currentAST, i2_AST);
						match(INF);
						if ( inputState.guessing==0 ) {
							op=Expr.Inf;opera=remplitToken(i2);
						}
						break;
					}
					case SUPS:
					{
						s = LT(1);
						s_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s);
						astFactory.makeASTRoot(currentAST, s_AST);
						match(SUPS);
						if ( inputState.guessing==0 ) {
							op=Expr.Sups;opera=remplitToken(s);
						}
						break;
					}
					case SUP:
					{
						s2 = LT(1);
						s2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(s2);
						astFactory.makeASTRoot(currentAST, s2_AST);
						match(SUP);
						if ( inputState.guessing==0 ) {
							op=Expr.Sup;opera=remplitToken(s2);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e2=expr21();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop233;
				}
				
			} while (true);
			}
			expr20_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr20_AST;
		return e;
	}
	
	public final Expr  expr21() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr21_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		Token  m = null;
		tinyeiffel.compiler.ASTDefaut m_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr22();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			_loop237:
			do {
				if ((LA(1)==PLUS||LA(1)==MOINS) && (_tokenSet_1.member(LA(2))) && (_tokenSet_16.member(LA(3))) && (_tokenSet_17.member(LA(4)))) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						p = LT(1);
						p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
						astFactory.makeASTRoot(currentAST, p_AST);
						match(PLUS);
						if ( inputState.guessing==0 ) {
							op=Expr.Plus;opera=remplitToken(p);
						}
						break;
					}
					case MOINS:
					{
						m = LT(1);
						m_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(m);
						astFactory.makeASTRoot(currentAST, m_AST);
						match(MOINS);
						if ( inputState.guessing==0 ) {
							op=Expr.Moins;opera=remplitToken(m);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e2=expr22();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop237;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
						//if(e2!=null)
					
			}
			expr21_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr21_AST;
		return e;
	}
	
	public final Expr  expr22() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr22_AST = null;
		Token  f = null;
		tinyeiffel.compiler.ASTDefaut f_AST = null;
		Token  d1 = null;
		tinyeiffel.compiler.ASTDefaut d1_AST = null;
		Token  d2 = null;
		tinyeiffel.compiler.ASTDefaut d2_AST = null;
		Token  m = null;
		tinyeiffel.compiler.ASTDefaut m_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr23();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			_loop246:
			do {
				if (((LA(1) >= FOIS && LA(1) <= MOD))) {
					{
					switch ( LA(1)) {
					case FOIS:
					{
						f = LT(1);
						f_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f);
						astFactory.makeASTRoot(currentAST, f_AST);
						match(FOIS);
						if ( inputState.guessing==0 ) {
							op=Expr.Fois;opera=remplitToken(f);
						}
						break;
					}
					case DIV:
					{
						d1 = LT(1);
						d1_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d1);
						astFactory.makeASTRoot(currentAST, d1_AST);
						match(DIV);
						if ( inputState.guessing==0 ) {
							op=Expr.Div;opera=remplitToken(d1);
						}
						break;
					}
					case DIV_ENTIER:
					{
						d2 = LT(1);
						d2_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d2);
						astFactory.makeASTRoot(currentAST, d2_AST);
						match(DIV_ENTIER);
						if ( inputState.guessing==0 ) {
							op=Expr.Div_entier;opera=remplitToken(d2);
						}
						break;
					}
					case MOD:
					{
						m = LT(1);
						m_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(m);
						astFactory.makeASTRoot(currentAST, m_AST);
						match(MOD);
						if ( inputState.guessing==0 ) {
							op=Expr.Mod;opera=remplitToken(m);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e2=expr23();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						/*e=new Expr(op,e,e2);e.oper=opera;*/
						e=new Expr_Binaire(op,e,e2,opera);
					}
				}
				else {
					break _loop246;
				}
				
			} while (true);
			}
			expr22_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr22_AST;
		return e;
	}
	
	public final Expr  expr21_b(
		Expr entree
	) throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr21_b_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		Token  m = null;
		tinyeiffel.compiler.ASTDefaut m_AST = null;
		
		int op=0;
		Expr e1=entree,e2=null;
		tinyeiffel.ast.Token opera=null;
		e=entree;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case PLUS:
			case MOINS:
			{
				{
				switch ( LA(1)) {
				case PLUS:
				{
					p = LT(1);
					p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
					astFactory.makeASTRoot(currentAST, p_AST);
					match(PLUS);
					if ( inputState.guessing==0 ) {
						op=Expr.Plus;opera=remplitToken(p);
					}
					break;
				}
				case MOINS:
				{
					m = LT(1);
					m_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(m);
					astFactory.makeASTRoot(currentAST, m_AST);
					match(MOINS);
					if ( inputState.guessing==0 ) {
						op=Expr.Moins;opera=remplitToken(m);
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				e2=expr22();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
									e=new Expr_Binaire(op,e1,e2,opera);
									e1=e;
								
				}
				e=expr21_b(e1);
				astFactory.addASTChild(currentAST, returnAST);
				expr21_b_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case EOF:
			{
				expr21_b_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = expr21_b_AST;
		return e;
	}
	
	public final Expr  expr23() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr23_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr24();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			switch ( LA(1)) {
			case PUISS:
			{
				p = LT(1);
				p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
				astFactory.makeASTRoot(currentAST, p_AST);
				match(PUISS);
				e2=expr23();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					
									/*e=new Expr(Expr.Puiss,e,e2);
									opera=remplitToken(p);
									e.oper=opera;*/
					e=new Expr_Binaire(Expr.Puiss,e,e2,remplitToken(p));
								
				}
				break;
			}
			case EOF:
			case EXPANDED:
			case DEFERRED:
			case FEATURE:
			case VIRGULE:
			case ACOLADEF:
			case SEMI:
			case INVARIANT:
			case END:
			case ID:
			case STRING:
			case RENAME:
			case EXPORT:
			case UNDEFINE:
			case REDEFINE:
			case SELECT:
			case CREATION:
			case INFIX:
			case PREFIX:
			case LPAREN:
			case RPAREN:
			case CROCHETF:
			case LIKE:
			case FROZEN:
			case IS:
			case ELSE:
			case ENSURE:
			case THEN:
			case RESCUE:
			case LOCAL:
			case DO:
			case ONCE:
			case EXTERNAL:
			case LPARENT:
			case RPARENT:
			case INT:
			case REAL:
			case CHAR:
			case FROM:
			case VARIANT:
			case UNTIL:
			case LOOP:
			case IF:
			case ELSEIF:
			case EXCLAMATION:
			case RETRY:
			case INSPECT:
			case WHEN:
			case CHECK:
			case DEBUG:
			case DEUX_POINT:
			case XOR:
			case IMPLIES:
			case OR:
			case AND:
			case EGAL:
			case DIFF:
			case INFS:
			case INF:
			case SUPS:
			case SUP:
			case PLUS:
			case MOINS:
			case FOIS:
			case DIV:
			case DIV_ENTIER:
			case MOD:
			case FREEOP:
			case OLD:
			case NOT:
			case DOLLARD:
			case DEBUT_ARRAY:
			case FIN_ARRAY:
			case TRUE:
			case FALSE:
			case STRING2:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expr23_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr23_AST;
		return e;
	}
	
	public final Expr  expr24() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr24_AST = null;
		Token  f = null;
		tinyeiffel.compiler.ASTDefaut f_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		
		
		try {      // for error handling
			e1=expr24_2();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			_loop253:
			do {
				boolean synPredMatched252 = false;
				if (((LA(1)==FREEOP) && (_tokenSet_1.member(LA(2))) && (_tokenSet_18.member(LA(3))) && (_tokenSet_17.member(LA(4))))) {
					int _m252 = mark();
					synPredMatched252 = true;
					inputState.guessing++;
					try {
						{
						match(FREEOP);
						expr24_2();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched252 = false;
					}
					rewind(_m252);
					inputState.guessing--;
				}
				if ( synPredMatched252 ) {
					f = LT(1);
					f_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f);
					astFactory.addASTChild(currentAST, f_AST);
					match(FREEOP);
					e2=expr24_2();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						e=new Expr_Binaire(f.getText(),e,e2,remplitToken(f));
					}
				}
				else {
					break _loop253;
				}
				
			} while (true);
			}
			expr24_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr24_AST;
		return e;
	}
	
	public final Expr  expr24_2() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr24_2_AST = null;
		Token  f = null;
		tinyeiffel.compiler.ASTDefaut f_AST = null;
		
		int op=0,i;
		boolean operation=false;
		Expr e1=null;
		tinyeiffel.ast.Token opera=null;
		Vector v1=new Vector();
		
		
		try {      // for error handling
			{
			_loop256:
			do {
				if ((LA(1)==FREEOP)) {
					f = LT(1);
					f_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f);
					astFactory.addASTChild(currentAST, f_AST);
					match(FREEOP);
					if ( inputState.guessing==0 ) {
						operation=true;opera=remplitToken(f);v1.addElement(opera);
					}
				}
				else {
					break _loop256;
				}
				
			} while (true);
			}
			e1=expr25();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				// TODO: Erreur si plusieurs fois free_op (ex: @ @ @ a -> @ a)
							if(operation)
							{
								e=e1;
								for(i=v1.size()-1;i>=0;i--)
								{
									opera=(tinyeiffel.ast.Token)v1.elementAt(i);
									e=new Expr_Unaire(/*f.getText()*/opera.text,e,opera);
								}
							}
							else
							{
								e=e1;
							}
						
			}
			expr24_2_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr24_2_AST;
		return e;
	}
	
	public final Expr  expr25() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr25_AST = null;
		Token  o = null;
		tinyeiffel.compiler.ASTDefaut o_AST = null;
		Token  n = null;
		tinyeiffel.compiler.ASTDefaut n_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		Token  m = null;
		tinyeiffel.compiler.ASTDefaut m_AST = null;
		Token  d = null;
		tinyeiffel.compiler.ASTDefaut d_AST = null;
		
		int op=0,i;
		Expr e1=null;
		tinyeiffel.ast.Token opera=null;
		Vector v1=new Vector(),v2=new Vector();
		// TODO: Erreur si plusieurs fois operation (ex: not - + a -> + a)
		
		
		try {      // for error handling
			{
			_loop259:
			do {
				switch ( LA(1)) {
				case OLD:
				{
					o = LT(1);
					o_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(o);
					astFactory.makeASTRoot(currentAST, o_AST);
					match(OLD);
					if ( inputState.guessing==0 ) {
						op=Expr.Old;opera=remplitToken(o);
								v1.addElement(new Integer(op));v2.addElement(opera);
					}
					break;
				}
				case NOT:
				{
					n = LT(1);
					n_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n);
					astFactory.makeASTRoot(currentAST, n_AST);
					match(NOT);
					if ( inputState.guessing==0 ) {
						op=Expr.Not;opera=remplitToken(n);
								v1.addElement(new Integer(op));v2.addElement(opera);
					}
					break;
				}
				case PLUS:
				{
					p = LT(1);
					p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
					astFactory.makeASTRoot(currentAST, p_AST);
					match(PLUS);
					if ( inputState.guessing==0 ) {
						op=Expr.PlusU;opera=remplitToken(p);
								v1.addElement(new Integer(op));v2.addElement(opera);
					}
					break;
				}
				case MOINS:
				{
					m = LT(1);
					m_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(m);
					astFactory.makeASTRoot(currentAST, m_AST);
					match(MOINS);
					if ( inputState.guessing==0 ) {
						op=Expr.MoinsU;opera=remplitToken(m);
								v1.addElement(new Integer(op));v2.addElement(opera);
					}
					break;
				}
				case DOLLARD:
				{
					d = LT(1);
					d_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d);
					astFactory.makeASTRoot(currentAST, d_AST);
					match(DOLLARD);
					if ( inputState.guessing==0 ) {
						op=Expr.Dollard;opera=remplitToken(d);
								v1.addElement(new Integer(op));v2.addElement(opera);
								ajoute_type(new TypeSimple(false,"POINTER",new Vector()));
					}
					break;
				}
				default:
				{
					break _loop259;
				}
				}
			} while (true);
			}
			e1=expr26();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				
						  assert(v1.size()==v2.size());
				if(op==0)
				{
						      assert(v1.size()==0);
				e=e1;//e.oper=opera;
				}
				else
				{
						      e=e1;
						      assert(v1.size()>0);
						      assert(e!=null):"opera="+opera.text+";"+opera.x+","+opera.y+";"+opera.file;
						      for(i=v1.size()-1;i>=0;i--)
						      {
						         Integer integ;
							 integ=(Integer)v1.elementAt(i);
							 opera=(tinyeiffel.ast.Token)v2.elementAt(i);
				e=new Expr_Unaire(integ.intValue(),e,opera);
						      }
				}
				
			}
			expr25_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr25_AST;
		return e;
	}
	
	public final Expr  expr26() throws RecognitionException, TokenStreamException {
		Expr e=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr26_AST = null;
		Token  p = null;
		tinyeiffel.compiler.ASTDefaut p_AST = null;
		
		int op=0;
		Expr e1=null,e2=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			e1=expr27();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e=e1;
			}
			{
			_loop262:
			do {
				if ((LA(1)==POINT)) {
					p = LT(1);
					p_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(p);
					astFactory.makeASTRoot(currentAST, p_AST);
					match(POINT);
					e2=expr27();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						
										/*e=new Expr(Expr.Point,e,e2);
										opera=remplitToken(p);
										e.oper=opera;*/
						e=new Expr_Binaire(Expr.Point,e,e2,remplitToken(p));
									
					}
				}
				else {
					break _loop262;
				}
				
			} while (true);
			}
			expr26_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr26_AST;
		return e;
	}
	
	public final Expr  expr27() throws RecognitionException, TokenStreamException {
		Expr res=null;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		tinyeiffel.compiler.ASTDefaut expr27_AST = null;
		Token  n = null;
		tinyeiffel.compiler.ASTDefaut n_AST = null;
		Token  i = null;
		tinyeiffel.compiler.ASTDefaut i_AST = null;
		Token  r = null;
		tinyeiffel.compiler.ASTDefaut r_AST = null;
		Token  c = null;
		tinyeiffel.compiler.ASTDefaut c_AST = null;
		Token  d = null;
		tinyeiffel.compiler.ASTDefaut d_AST = null;
		Token  t = null;
		tinyeiffel.compiler.ASTDefaut t_AST = null;
		Token  f = null;
		tinyeiffel.compiler.ASTDefaut f_AST = null;
		
		Expr e=null,e1=null,e2=null;
		Vector param=null;
		Chaine str=null;
		tinyeiffel.ast.Token opera=null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			{
				n = LT(1);
				n_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(n);
				astFactory.addASTChild(currentAST, n_AST);
				match(ID);
				{
				if ((LA(1)==LPAREN) && (_tokenSet_19.member(LA(2))) && (_tokenSet_18.member(LA(3))) && (_tokenSet_17.member(LA(4)))) {
					param=exec_param();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_17.member(LA(2))) && (_tokenSet_21.member(LA(3))) && (_tokenSet_21.member(LA(4)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				if ( inputState.guessing==0 ) {
					
								if(param!=null)
					{// /*(LPARENT) =>*/
									//res=new Expr(n.getText(),param);
					res=new Expr_Appel(n.getText(),param,remplitToken(n));
					}
								else
					{
									//res=new Expr(n.getText());
					res=new Expr_Var(n.getText(),remplitToken(n));
					}
								//res.oper=remplitToken(n);
							
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case INT:
			{
				i = LT(1);
				i_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(i);
				astFactory.addASTChild(currentAST, i_AST);
				match(INT);
				if ( inputState.guessing==0 ) {
					
											/*res=new Expr(Expr.Entier);
											res.str=i.getText();
											res.oper=remplitToken(i);*/
					res=new Expr_Entier(i.getText(),remplitToken(i));
										
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case STRING:
			case STRING2:
			{
				str=string();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					res=new Expr_Chaine(str);
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case REAL:
			{
				r = LT(1);
				r_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(r);
				astFactory.addASTChild(currentAST, r_AST);
				match(REAL);
				if ( inputState.guessing==0 ) {
					
											/*res=new Expr(Expr.Reel);
											res.str=r.getText();
											res.oper=remplitToken(r);*/
					res=new Expr_Reel(r.getText(),remplitToken(r));
										
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case CHAR:
			{
				c = LT(1);
				c_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(c);
				astFactory.addASTChild(currentAST, c_AST);
				match(CHAR);
				if ( inputState.guessing==0 ) {
					
											res=new Expr_Car(c.getText(),remplitToken(c));
											//res.str=c.getText();
											//res.oper=remplitToken(c);
					//assert(res.oper!=null);
										
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case LPAREN:
			{
				tinyeiffel.compiler.ASTDefaut tmp93_AST = null;
				tmp93_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp93_AST);
				match(LPAREN);
				e=expr();
				astFactory.addASTChild(currentAST, returnAST);
				tinyeiffel.compiler.ASTDefaut tmp94_AST = null;
				tmp94_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp94_AST);
				match(RPAREN);
				if ( inputState.guessing==0 ) {
					res=e;
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case DEBUT_ARRAY:
			{
				d = LT(1);
				d_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(d);
				astFactory.addASTChild(currentAST, d_AST);
				match(DEBUT_ARRAY);
				if ( inputState.guessing==0 ) {
					param=new Vector();
				}
				{
				switch ( LA(1)) {
				case ID:
				case STRING:
				case LPAREN:
				case INT:
				case REAL:
				case CHAR:
				case PLUS:
				case MOINS:
				case FREEOP:
				case OLD:
				case NOT:
				case DOLLARD:
				case DEBUT_ARRAY:
				case TRUE:
				case FALSE:
				case STRING2:
				{
					e1=expr();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						param.addElement(e1);
					}
					{
					_loop270:
					do {
						if ((LA(1)==VIRGULE)) {
							tinyeiffel.compiler.ASTDefaut tmp95_AST = null;
							tmp95_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp95_AST);
							match(VIRGULE);
							e2=expr();
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								param.addElement(e2);
							}
						}
						else {
							break _loop270;
						}
						
					} while (true);
					}
					break;
				}
				case FIN_ARRAY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				tinyeiffel.compiler.ASTDefaut tmp96_AST = null;
				tmp96_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp96_AST);
				match(FIN_ARRAY);
				if ( inputState.guessing==0 ) {
					/*res=new Expr(Expr.Tableau);res.tableau=param;*/res=new Expr_Tableau(param,remplitToken(d));
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case TRUE:
			{
				t = LT(1);
				t_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(t);
				astFactory.addASTChild(currentAST, t_AST);
				match(TRUE);
				if ( inputState.guessing==0 ) {
					/*res=new Expr(Expr.Vrai);res.oper=remplitToken(t);*/res=new Expr_Vrai(remplitToken(t));
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			case FALSE:
			{
				f = LT(1);
				f_AST = (tinyeiffel.compiler.ASTDefaut)astFactory.create(f);
				astFactory.addASTChild(currentAST, f_AST);
				match(FALSE);
				if ( inputState.guessing==0 ) {
					/*res=new Expr(Expr.Faux);res.oper=remplitToken(f);*/res=new Expr_Faux(remplitToken(f));
				}
				expr27_AST = (tinyeiffel.compiler.ASTDefaut)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
						logging.erreur(new ErreurSource("Erreur dans l'expression",ex.getLine(),
									ex.getColumn(),ex.getFilename()));
					
			} else {
				throw ex;
			}
		}
		returnAST = expr27_AST;
		return res;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"expanded\"",
		"\"deferred\"",
		"\"class\"",
		"\"obsolete\"",
		"\"feature\"",
		"ACOLADEO",
		"VIRGULE",
		"ACOLADEF",
		"SEMI",
		"\"invariant\"",
		"\"end\"",
		"\"indexing\"",
		"ID",
		"DOUBLE_POINT",
		"STRING",
		"\"inherit\"",
		"\"rename\"",
		"\"as\"",
		"\"export\"",
		"\"all\"",
		"\"undefine\"",
		"\"redefine\"",
		"\"select\"",
		"\"creation\"",
		"\"infix\"",
		"\"prefix\"",
		"LPAREN",
		"RPAREN",
		"CROCHETO",
		"CROCHETF",
		"FLECHED",
		"\"like\"",
		"\"frozen\"",
		"\"is\"",
		"\"require\"",
		"\"else\"",
		"\"ensure\"",
		"\"then\"",
		"\"rescue\"",
		"\"unique\"",
		"\"local\"",
		"\"do\"",
		"\"once\"",
		"\"external\"",
		"\"alias\"",
		"POINT",
		"AFFECT",
		"TENTATIVE_AFFECT",
		"LPARENT",
		"RPARENT",
		"INT",
		"REAL",
		"CHAR",
		"\"from\"",
		"\"variant\"",
		"\"until\"",
		"\"loop\"",
		"\"if\"",
		"\"elseif\"",
		"EXCLAMATION",
		"\"retry\"",
		"\"inspect\"",
		"\"when\"",
		"\"check\"",
		"\"debug\"",
		"DEUX_POINT",
		"\"xor\"",
		"\"implies\"",
		"\"or\"",
		"\"and\"",
		"EGAL",
		"DIFF",
		"INFS",
		"INF",
		"SUPS",
		"SUP",
		"PLUS",
		"MOINS",
		"FOIS",
		"DIV",
		"DIV_ENTIER",
		"MOD",
		"PUISS",
		"FREEOP",
		"\"old\"",
		"\"not\"",
		"DOLLARD",
		"DEBUT_ARRAY",
		"FIN_ARRAY",
		"\"true\"",
		"\"false\"",
		"STRING2",
		"STRING3",
		"STRING4",
		"\"separate\"",
		"\"strip\"",
		"WS",
		"COMMENTAIRE",
		"DIGIT",
		"EXPOSANT"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 69524848640L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 126100790640443392L, 4018339840L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 991623149886197792L, 17179869120L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { -1165597151661165278L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { -9016300558387406L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 991060199932776480L, 4018339840L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -1165599352831905502L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { -26037747790L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { -6642809450371153920L, 2147483675L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { -1164737405240643584L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { -1163611505333800960L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 130604389194072064L, 2147483648L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { -1165863305147486208L, 16903241759L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { -1153476101116956400L, 17179869151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { -554454776056046L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { -3668267154178768L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { -281492159234126L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { -3668267154178766L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 126100792787927040L, 4018339840L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { -3668267154178766L, 4294967295L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { -17180426318L, 17179869183L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	
	}
