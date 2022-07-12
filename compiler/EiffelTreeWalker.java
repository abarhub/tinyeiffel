// $ANTLR 2.7.3: "eiffel.g" -> "EiffelTreeWalker.java"$

package tinyeiffel.compiler;

import tinyeiffel.ast.Classe;
import antlr.NoViableAltException;
import antlr.RecognitionException;
import antlr.collections.AST;


public class EiffelTreeWalker extends antlr.TreeParser       implements EiffelTokenTypes
 {
public EiffelTreeWalker() {
	tokenNames = _tokenNames;
}

	public final void programme(AST _t) throws RecognitionException {
		
		ASTDefaut programme_AST_in = (_t == ASTNULL) ? null : (ASTDefaut)_t;
		ASTDefaut n = null;
		
		try {      // for error handling
			AST __t364 = _t;
			ASTDefaut tmp1_AST_in = (ASTDefaut)_t;
			match(_t,CLASS);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INDEXING:
			{
				index(_t);
				_t = _retTree;
				break;
			}
			case ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			n = (ASTDefaut)_t;
			match(_t,ID);
			_t = _t.getNextSibling();
			ASTDefaut tmp2_AST_in = (ASTDefaut)_t;
			match(_t,FEATURE);
			_t = _t.getNextSibling();
			_t = __t364;
			_t = _t.getNextSibling();
			
						System.out.println("Programme:");
						Classe c=new Classe(false,false,/*#n.getText()*/null,null,null,
									null,null,null,null,null);
						//#programme.ast=c;
					
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		_retTree = _t;
	}
	
	public final void index(AST _t) throws RecognitionException {
		
		ASTDefaut index_AST_in = (_t == ASTNULL) ? null : (ASTDefaut)_t;
		
		try {      // for error handling
			AST __t367 = _t;
			ASTDefaut tmp3_AST_in = (ASTDefaut)_t;
			match(_t,INDEXING);
			_t = _t.getFirstChild();
			{
			int _cnt371=0;
			_loop371:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==ID)) {
					ASTDefaut tmp4_AST_in = (ASTDefaut)_t;
					match(_t,ID);
					_t = _t.getNextSibling();
					ASTDefaut tmp5_AST_in = (ASTDefaut)_t;
					match(_t,DOUBLE_POINT);
					_t = _t.getNextSibling();
					{
					int _cnt370=0;
					_loop370:
					do {
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case ID:
						{
							ASTDefaut tmp6_AST_in = (ASTDefaut)_t;
							match(_t,ID);
							_t = _t.getNextSibling();
							break;
						}
						case STRING:
						{
							ASTDefaut tmp7_AST_in = (ASTDefaut)_t;
							match(_t,STRING);
							_t = _t.getNextSibling();
							break;
						}
						default:
						{
							if ( _cnt370>=1 ) { break _loop370; } else {throw new NoViableAltException(_t);}
						}
						}
						_cnt370++;
					} while (true);
					}
					ASTDefaut tmp8_AST_in = (ASTDefaut)_t;
					match(_t,SEMI);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt371>=1 ) { break _loop371; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt371++;
			} while (true);
			}
			_t = __t367;
			_t = _t.getNextSibling();
			
			
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		_retTree = _t;
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
	
	}
	
