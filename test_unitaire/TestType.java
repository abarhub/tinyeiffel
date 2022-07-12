/*
 * Created on 23 janv. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * TODO : ne plus utiliser antlr
 */
package tinyeiffel.test_unitaire;

import junit.framework.*;
import tinyeiffel.compiler.*;
import tinyeiffel.ast.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.io.*;
//import antlr.CommonAST;
//import antlr.collections.AST;
//import antlr.DumpASTVisitor;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRecognitionException;
import tinyeiffel.erreur.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.w3c.dom.*;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;


/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestType extends TestCase {

	/**
	 * Constructor for TestType.
	 * @param arg0
	 */
	public TestType(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestType.class);
	}

	protected Logger logger;
	protected FileHandler fh;
	
	protected void setUp()
	{
		logger=Logger.getLogger("tinyeiffel.test.testType");
		if(fh==null)
		{
			try {
				fh = new FileHandler("test_type.log");
				fh.setFormatter(new SimpleFormatter());
				logger.addHandler(fh);
			}
			catch(IOException e)
			{
				System.err.println(e);
				e.printStackTrace();
				assert(false);
			}
		}
		// Request that every detail gets logged.
		logger.setLevel(Level.ALL);
        // Log a simple INFO message.
        logger.info("Demarrage du log");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		logger.info("Tests Finis");
		if(fh!=null)
		{
			logger.removeHandler(fh);
			fh.close();
		}
	}

	public void testAssertion()
	{
		assertTrue(TestAll.assertion_active());
	}
	
	/*public void testType()
	{
		Compiler_Eiffel compiler;
		Table_Symbol t1;
		Type de,ancetre;
		int no;
		//assert(false);
		System.out.println("Test des types");
		compiler=new Compiler_Eiffel(Compiler_Eiffel.apres_context);
		//de=new Type(false,"TEST1",new Vector());
		de=parse_type("(TEST1)",compiler);
		ancetre=new Type(false,"TEST1",new Vector());
		no=compiler.compile("test\\context\\conformite\\generique\\test1.ace");
		assert(no==0):"no="+no;
		t1=compiler.context.donne_table_symbol(de);
		assert(compiler.context.type_compatible(de,t1,ancetre,t1));
		System.out.println("Fin de test des types");
	}*/
	
	protected boolean verifie(String nom,Compiler_Eiffel compiler,
						String de,String ancetre,boolean conforme,
						String classe)
	{
		//Compiler_Eiffel compiler;
		Table_Symbol t1;
		Type type_de,type_ancetre,type_lieu;
		int no;
		Classe cl;
		boolean res;
		assert(nom!=null);
		assert(compiler!=null);
		assert(de!=null);
		assert(ancetre!=null);
		//assert(classe!=null);
		System.out.println("Verif:"+de+"->"+ancetre+"="+conforme+" ("+nom+")");
		//compiler=new Compiler_Eiffel(Compiler_Eiffel.apres_context);
		//de=new Type(false,"TEST1",new Vector());
		if(classe==null)
			classe=de;
		type_de=parse_type("("+de+")",compiler);
		System.out.println("type_de1="+type_de.toString2());
		type_ancetre=parse_type("("+ancetre+")",compiler);
		type_lieu=parse_type("("+classe+")",compiler);
		//no=compiler.compile("test\\context\\conformite\\generique\\test1.ace");
		//assert(no==0):"no="+no;
		t1=compiler.context.donne_table_symbol(type_lieu);
		System.out.println("type_de2="+type_de.toString2());
		cl=compiler.context.cherche_classe(type_lieu);
		compiler.env.entre_classe(cl,t1);
		res=compiler.context.type_compatible(type_de,t1,type_ancetre,t1);
		compiler.env.sort_classe();
		System.out.println("Fin Verif types ("+res+")");
		return res;
	}
	
	protected boolean verifie_expr(String nom,Compiler_Eiffel compiler,
							String de,String ancetre,boolean conforme,
							String classe,String nom_feature,boolean dans_corps)
	{
		//Compiler_Eiffel compiler;
		Table_Symbol t1;
		Type /*type_de,type_ancetre,*/type_lieu;
		int no;
		boolean res;
		NomFeature nfeature=null;
		Expr expr=null;
		Feature f=null;
		Classe cl=null;
		DeclareVar cible;
		assert(nom!=null);
		assert(compiler!=null);
		assert(de!=null);
		assert(ancetre!=null);
		//assert(classe!=null);
		System.out.println("Verif:"+de+"->"+ancetre+"="+conforme+" ("+nom+")");
		//compiler=new Compiler_Eiffel(Compiler_Eiffel.apres_context);
		//de=new Type(false,"TEST1",new Vector());
		if(classe==null)
			classe=de;
		//type_de=parse_type("("+de+")",compiler);
		expr=parse_expr("("+de+")",compiler);
		assert(expr!=null):"expr="+expr;
		System.out.println("type_de1="+expr.toString());
		cible=parse_declare_var(ancetre,compiler);
		assert(cible!=null):"ancetre="+ancetre;
		type_lieu=parse_type("("+classe+")",compiler);
		if(nom_feature!=null)
			nfeature=parse_nom_feature("("+nom_feature+")",compiler);
		expr=parse_expr("("+de+")",compiler);
		//no=compiler.compile("test\\context\\conformite\\generique\\test1.ace");
		//assert(no==0):"no="+no;
		t1=compiler.context.donne_table_symbol(type_lieu);
		cl=compiler.context.cherche_classe(type_lieu);
		if(nfeature!=null)
			f=t1.donne_attribut(nfeature).getFeature();
		System.out.println("type_de2="+expr.toString());
		compiler.env.entre_classe(cl,t1);
		if(f!=null)
			compiler.env.entre_feature(f);
		compiler.verifie_expr(compiler.context,expr,true);
		res=compiler.context.type_compatible(expr,t1,cible,t1);
		if(f!=null)
			compiler.env.sort_feature();
		compiler.env.sort_classe();
		System.out.println("Fin Verif types ("+res+")");
		return res;
	}
	
	public void testType2()
	{
		/*Compiler_Eiffel compiler;
		Table_Symbol t1;
		Type de,ancetre;
		int no;*/
		logger.info("Test type 2");
		Element elt=donne_liste_conformite("test_unitaire\\conforme.xml");
		assert(elt!=null);
		assert(elt.getNodeName() == "liste_testes");
		NodeList liste_conformite = elt.getChildNodes();
		Compiler_Eiffel compiler;
		int i,i0;
		for (i0 = 0; i0 < liste_conformite.getLength(); i0++) {
			Node n0 = liste_conformite.item(i0);
			System.out.println("element=" + n0.getNodeName()+"======================================================");
			if (n0 instanceof Element) {
				if (n0.getNodeName() == "conformite") {
					//System.out.println("heritage");
					//h = creer_heritage((Element) n);
					NamedNodeMap attributs = n0.getAttributes();
					String nom = null;
					int nb_erreur=0;
					for (i = 0; i < attributs.getLength(); i++) {
						Node numéro = attributs.item(i);
						String nomAttribut = numéro.getNodeName();
						String valeurAttribut = numéro.getNodeValue();
						//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
						if (nomAttribut == "nom")
							nom = valeurAttribut;
						else if (nomAttribut == "nb_erreur")
						{
							try{
								nb_erreur = Integer.parseInt(valeurAttribut);
							}
							catch(NumberFormatException e)
							{
								assert(false):"e="+e;
							}
						}
						else
							assert(false):nomAttribut+"="+valeurAttribut;
					}
					compiler=new Compiler_Eiffel(Compiler_Eiffel.apres_context);
					int no=compiler.compile(nom,TestAll.path_test);
					if(no!=nb_erreur)
					{
						compiler.logging.affiche();
						if(no!=nb_erreur)
						{
							logger.severe("no="+no+"!="+nb_erreur+
										"("+compiler.logging+")");
							assertTrue(no==nb_erreur);
						}
						//assert(no==nb_erreur):"no="+no+"!="+nb_erreur+
						//				"("+compiler.logging+")";
					}
					NodeList liste_test=n0.getChildNodes();
					for(int j=0;j<liste_test.getLength();j++)
					{
						Node n=liste_test.item(j);
						if (n instanceof Element) {
							if (n.getNodeName() == "test") {
								//System.out.println("heritage");
								//h = creer_heritage((Element) n);
								NamedNodeMap attr = n.getAttributes();
								String de = null,ancetre=null,classe=null;
								Type nom_classe = null;
								boolean conforme = false,res;
								for (i = 0; i < attr.getLength(); i++) {
									Node numéro = attr.item(i);
									String nomAttribut = numéro.getNodeName();
									String valeurAttribut = numéro.getNodeValue();
									//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
									if (nomAttribut == "de")
										de = valeurAttribut;
									else if (nomAttribut == "ancetre")
										ancetre = valeurAttribut;
									else if (nomAttribut == "conforme")
										conforme = valeurAttribut.compareToIgnoreCase("oui")==0?true:false;
									else if (nomAttribut == "lieux")
										classe = valeurAttribut;
									else
										assert(false):nomAttribut+"="+valeurAttribut;
								}
								res=verifie(nom,compiler,de,ancetre,conforme,classe);
								if(res!=conforme)
								{
									logger.severe("Type "+de+" "+((conforme)?"pas":"")+
										" conforme a "+ancetre+" dans "+nom+
										"("+res+")");
									assertTrue(res==conforme);
								}
								/*assert(res==conforme):
										"Type "+de+" "+((conforme)?"pas":"")+
										" conforme a "+ancetre+" dans "+nom+
										"("+res+")";*/
							}
							else if (n.getNodeName() == "test_expr") {
								//System.out.println("heritage");
								//h = creer_heritage((Element) n);
								NamedNodeMap attr = n.getAttributes();
								String de = null,ancetre=null,classe=null;
								String nom_feature=null;
								Type nom_classe = null;
								boolean conforme = false,res,dans_corps=false;
								for (i = 0; i < attr.getLength(); i++) {
									Node numéro = attr.item(i);
									String nomAttribut = numéro.getNodeName();
									String valeurAttribut = numéro.getNodeValue();
									//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
									if (nomAttribut == "de")
										de = valeurAttribut;
									else if (nomAttribut == "ancetre")
										ancetre = valeurAttribut;
									else if (nomAttribut == "conforme")
										conforme = valeurAttribut.compareToIgnoreCase("oui")==0?true:false;
									else if (nomAttribut == "lieux")
										classe = valeurAttribut;
									else if (nomAttribut == "nom_feature")
										nom_feature = valeurAttribut;
									else if (nomAttribut == "dans_corps")
										dans_corps = valeurAttribut.compareToIgnoreCase("oui")==0?true:false;
									else
										assert(false):nomAttribut+"="+valeurAttribut;
								}
								res=verifie_expr(nom,compiler,de,ancetre,conforme,classe,nom_feature,dans_corps);
								if(res!=conforme)
								{
									logger.severe("Type "+de+" "+((conforme)?"pas":"")+
											" conforme a "+ancetre+" dans "+nom+
											"("+res+")");
									assertTrue(res==conforme);
								}
								/*assert(res==conforme):
										"Type "+de+" "+((conforme)?"pas":"")+
										" conforme a "+ancetre+" dans "+nom+
										"("+res+")";*/
							}
							else
								assert(false);
						} else if (n instanceof Text) {
							String s = n.getNodeValue();
							assert(s == "" || s.matches("[ \t\n]+"));
						} else if (n instanceof Comment) {
							
						}
						else
							assert(false):"n="+n;
					}
				}
				else
					assert(false);
			} else if (n0 instanceof Text) {
						String s = n0.getNodeValue();
						assert(s == "" || s.matches("[ \t\n]+"));
			} else if (n0 instanceof Comment) {
				
			}
			else
				assert(false):"n="+n0;
		}
		logger.info("Fin des Tests type 2");
	}
		
	public Element donne_liste_conformite(String nom)
	{
		try {
			//Récupère une instance de la classe de fabrication
			DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();
			//Récupére une instance de la classe DocumentBuilder (spécifique vendeur)
			DocumentBuilder parser = factory.newDocumentBuilder();
			//effectue le parsing avec récupération du noeud DOM Document
			Document document = parser.parse(nom);
			Element catalogue = document.getDocumentElement();
			return catalogue;
		} catch (FactoryConfigurationError e) {
			// unable to get a document builder factory
			e.printStackTrace();
			System.err.println("Erreur de configuration du factory");
			System.exit(1);
		} catch (ParserConfigurationException e) {
			// parser was unable to be configured
			e.printStackTrace();
			System.err.println("Erreur de configuration du parser");
			System.exit(1);
		} catch (SAXException e) {
			// parsing error
			e.printStackTrace();
			System.err.println("Exception Sax");
			System.exit(1);
		} catch (IOException e) {
			// i/o error
			e.printStackTrace();
			System.err.println("Erreur d'entree sortie");
			System.exit(1);
		}
		return null;
	}
	
	public Type parse_type(String nom_type,Compiler_Eiffel compiler) 
	{
		Type t;
		assert(nom_type!=null);
		assert(compiler!=null);
		//String nom_reel=null;
		//nom_reel = donne_nom(nom_fichier);
		if (nom_type == "") {
			compiler.logging.erreur(new ErreurIO("Fichier " + nom_type + " non trouve",nom_type));
			return null;
		}
		try {
			EiffelLexer lexer =
				//new EiffelLexer(new FileInputStream(nom_reel));
				new EiffelLexer(new StringReader(nom_type));
			lexer.setFilename(nom_type);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(nom_type);
			parser.lexer = lexer;
			parser.logging=compiler.logging;
			//parser.logging=logging;
			// Parse the input expression
			t = parser.type0();
			Vector liste_classe = parser.type_utilisee;
			System.out.println("classe:" + liste_classe);
			if (t == null) {
				return null;
			}
			//compiler.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x,y;
			x=-1;
			y=-1;
			if(e instanceof TokenStreamRecognitionException)
			{
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			compiler.logging.erreur(new ErreurSource("Erreur lexicale:"+
							e.getMessage(),x,y,nom_type));
			return null;
		} catch (RecognitionException e) {
			//erreur("exception: " + e);
			compiler.logging.erreur(new ErreurSource("Erreur de parsing:"+
							e.getMessage(),e.getLine(),e.getColumn(),
							e.getFilename()));
			return null;
		} /*catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			compiler.logging.erreur(new ErreurIO("Fichier "+nom_type+" non trouve",nom_type));
			return null;
		}*/ catch (SecurityException e) {
			compiler.logging.erreur(new ErreurSource("Erreur: erreur de securite",-1,-1,nom_type));
			return null;
		}
		return t;
	}
	
	public DeclareVar parse_declare_var(String nom_type,Compiler_Eiffel compiler) 
	{
		DeclareVar t;
		String n,n2;
		Type t2;
		int i;
		assert(nom_type!=null);
		assert(compiler!=null);
		//String nom_reel=null;
		//nom_reel = donne_nom(nom_fichier);
		if (nom_type == "") {
			compiler.logging.erreur(new ErreurIO("Fichier " + nom_type + " non trouve",nom_type));
			return null;
		}
		try {
			i=nom_type.indexOf(':');
			assert(i>-1);
			n=nom_type.substring(0,i-1);
			n2=nom_type.substring(i+1);
			EiffelLexer lexer =
				//new EiffelLexer(new FileInputStream(nom_reel));
				new EiffelLexer(new StringReader("("+n2+")"));
			lexer.setFilename(nom_type);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(nom_type);
			parser.lexer = lexer;
			parser.logging=compiler.logging;
			//parser.logging=logging;
			// Parse the input expression
			t2 = parser.type0();
			Vector liste_classe = parser.type_utilisee;
			System.out.println("classe:" + liste_classe);
			if (t2 == null) {
				return null;
			}
			t=new DeclareVar(n,t2);
			//compiler.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x,y;
			x=-1;
			y=-1;
			if(e instanceof TokenStreamRecognitionException)
			{
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			compiler.logging.erreur(new ErreurSource("Erreur lexicale:"+
							e.getMessage(),x,y,nom_type));
			return null;
		} catch (RecognitionException e) {
			//erreur("exception: " + e);
			compiler.logging.erreur(new ErreurSource("Erreur de parsing:"+
							e.getMessage(),e.getLine(),e.getColumn(),
							e.getFilename()));
			return null;
		} /*catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			compiler.logging.erreur(new ErreurIO("Fichier "+nom_type+" non trouve",nom_type));
			return null;
		}*/ catch (SecurityException e) {
			compiler.logging.erreur(new ErreurSource("Erreur: erreur de securite",-1,-1,nom_type));
			return null;
		}
		return t;
	}	
	public NomFeature parse_nom_feature(String nom_feature,Compiler_Eiffel compiler) 
	{
		NomFeature t;
		assert(nom_feature!=null);
		assert(compiler!=null);
		//String nom_reel=null;
		//nom_reel = donne_nom(nom_fichier);
		if (nom_feature == "") {
			compiler.logging.erreur(new ErreurIO("Fichier " + nom_feature + " non trouve",nom_feature));
			return null;
		}
		try {
			EiffelLexer lexer =
				//new EiffelLexer(new FileInputStream(nom_reel));
				new EiffelLexer(new StringReader(nom_feature));
			lexer.setFilename(nom_feature);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(nom_feature);
			parser.lexer = lexer;
			parser.logging=compiler.logging;
			//parser.logging=logging;
			// Parse the input expression
			t = parser.nom_feature0();
			Vector liste_classe = parser.type_utilisee;
			System.out.println("classe:" + liste_classe);
			if (t == null) {
				return null;
			}
			//compiler.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x,y;
			x=-1;
			y=-1;
			if(e instanceof TokenStreamRecognitionException)
			{
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			compiler.logging.erreur(new ErreurSource("Erreur lexicale:"+
							e.getMessage(),x,y,nom_feature));
			return null;
		} catch (RecognitionException e) {
			//erreur("exception: " + e);
			compiler.logging.erreur(new ErreurSource("Erreur de parsing:"+
							e.getMessage(),e.getLine(),e.getColumn(),
							e.getFilename()));
			return null;
		} /*catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			compiler.logging.erreur(new ErreurIO("Fichier "+nom_type+" non trouve",nom_type));
			return null;
		}*/ catch (SecurityException e) {
			compiler.logging.erreur(new ErreurSource("Erreur: erreur de securite",-1,-1,nom_feature));
			return null;
		}
		return t;
	}
	
	public Expr parse_expr(String expr,Compiler_Eiffel compiler) 
	{
		Expr t;
		assert(expr!=null);
		assert(compiler!=null);
		//String nom_reel=null;
		//nom_reel = donne_nom(nom_fichier);
		if (expr == "") {
			compiler.logging.erreur(new ErreurIO("Fichier " + expr + " non trouve",expr));
			return null;
		}
		try {
			EiffelLexer lexer =
				//new EiffelLexer(new FileInputStream(nom_reel));
				new EiffelLexer(new StringReader(expr));
			lexer.setFilename(expr);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(expr);
			parser.lexer = lexer;
			parser.logging=compiler.logging;
			//parser.logging=logging;
			// Parse the input expression
			t = parser.expr0();
			Vector liste_classe = parser.type_utilisee;
			System.out.println("classe:" + liste_classe);
			if (t == null) {
				return null;
			}
			//compiler.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x,y;
			x=-1;
			y=-1;
			if(e instanceof TokenStreamRecognitionException)
			{
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			compiler.logging.erreur(new ErreurSource("Erreur lexicale:"+
							e.getMessage(),x,y,expr));
			return null;
		} catch (RecognitionException e) {
			//erreur("exception: " + e);
			compiler.logging.erreur(new ErreurSource("Erreur de parsing:"+
							e.getMessage(),e.getLine(),e.getColumn(),
							e.getFilename()));
			return null;
		} /*catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			compiler.logging.erreur(new ErreurIO("Fichier "+nom_type+" non trouve",nom_type));
			return null;
		}*/ catch (SecurityException e) {
			compiler.logging.erreur(new ErreurSource("Erreur: erreur de securite",-1,-1,expr));
			return null;
		}
		return t;
	}
	
	public void testConversion()
	{
		System.out.println("Test des conversions");
		Compiler_Eiffel compiler;
		String nom,s;
		int no,i,j;
		Classe c,c1=null,b1=null;
		Heritage h;
		Conversion conv;
		Type t,base;
		logger.info("Test de convertion");
		nom="test\\context\\conformite\\generique\\test1.ace"; 
		compiler=new Compiler_Eiffel(Compiler_Eiffel.apres_context);
		no=compiler.compile(nom,TestAll.path_test);
		if(no!=0)
		{
			compiler.logging.affiche();
			if(no!=0)
			{
				logger.severe("no="+no+"("+compiler.logging+")");
				assertTrue(no==0);
			}
			//assert(no==0):"no="+no+"("+compiler.logging+")";
		}
		for(i=0;i<compiler.liste_classe.size();i++)
		{
			c=(Classe)compiler.liste_classe.elementAt(i);
			if(c.nom.compareToIgnoreCase("C1")==0)
			{
				c1=c;
			}
			else if(c.nom.compareToIgnoreCase("B1")==0)
			{
				b1=c;
			}
		}
		assert(c1!=null);
		assert(b1!=null);
		conv=new Conversion(c1.heritage[0],c1.type);
		s="C1[TL,TN]";
		base=parse_type("("+s+")",compiler);
		t=conv.convertie2(base);
		System.out.println("type "+base.toString2()+"->"+t.toString2());
		assertTrue(t.toString2().compareToIgnoreCase("B1[TK,TL,TM]")==0);
		//compiler.liste_classe
		logger.info("Fin de test des conversions");
	}
	public static Test suite()
	{
		return new TestSuite(TestType.class);
	}

}
