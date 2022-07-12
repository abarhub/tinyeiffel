/*
 * Created on 18 sept. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.test_unitaire;

import junit.framework.*;
import tinyeiffel.ast.*;
import tinyeiffel.compiler.*;
import java.io.*;

import tinyeiffel.outils.compare_xml.*;
import java.util.logging.*;

import java.util.*;
import antlr.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestXmlSimple extends TestCase {

	/**
	 * 
	 */
	/*public TestXmlSimple(String nom) {
		nom_fichier=nom;
	}*/

	protected void setUp()
	{
		//System.out.println("coucou-1");
		
	}

	public static String[] nom=
		{"test_unitaire\\test_class1.xml","std_classe\\any.e",
		"test\\test1.xml","test\\test1-2.e",
		"test\\test2.xml","test\\test1-3.e",
		"test\\test3.xml","test\\test1-4.e",
		"test\\test4.xml","test\\test1-5.e",
		"test\\test5.xml","test\\test1-6.e",
		"test\\test7.xml","test\\test7.e",
		"test\\parser\\test1\\test1.xml","test\\parser\\test1\\test1.e", // 7
		"test\\parser\\test_classe1\\test1.xml","test\\parser\\test_classe1\\test1.e" // 8
		,"test\\parser\\test_classe1\\test2.xml","test\\parser\\test_classe1\\test2.e"
		,"test\\parser\\test_classe1\\test3.xml","test\\parser\\test_classe1\\test3.e" // 10
		,"test\\parser\\test_classe1\\test4.xml","test\\parser\\test_classe1\\test4.e"
		,"test\\parser\\test_classe1\\test5.xml","test\\parser\\test_classe1\\test5.e"
		,"test\\parser\\test_classe1\\test6.xml","test\\parser\\test_classe1\\test6.e"
		,"test\\parser\\test_classe1\\test7.xml","test\\parser\\test_classe1\\test7.e" // 14
		,"test\\parser\\test_heritage\\test1.xml","test\\parser\\test_heritage\\test1.e"
		,"test\\parser\\test_heritage\\test2.xml","test\\parser\\test_heritage\\test2.e"
		,"test\\parser\\test_heritage\\test3.xml","test\\parser\\test_heritage\\test3.e"
		,"test\\parser\\test_heritage\\test4.xml","test\\parser\\test_heritage\\test4.e"
		,"test\\parser\\test_heritage\\test5.xml","test\\parser\\test_heritage\\test5.e"
		,"test\\parser\\test_heritage\\test6.xml","test\\parser\\test_heritage\\test6.e" // 20
		,"test\\parser\\test_heritage\\test7.xml","test\\parser\\test_heritage\\test7.e"
		,"test\\parser\\test_heritage\\test8.xml","test\\parser\\test_heritage\\test8.e"
		,"test\\parser\\test_heritage\\test9.xml","test\\parser\\test_heritage\\test9.e"
		,"test\\parser\\test_heritage\\test10.xml","test\\parser\\test_heritage\\test10.e"
		,"test\\parser\\test_heritage\\test11.xml","test\\parser\\test_heritage\\test11.e" // 25
		,"test\\parser\\test_heritage\\test12.xml","test\\parser\\test_heritage\\test12.e"
		,"test\\parser\\test_heritage\\test13.xml","test\\parser\\test_heritage\\test13.e"
		,"test\\parser\\test_classe1\\test9.xml","test\\parser\\test_classe1\\test9.e"
		,"test\\parser\\test_classe1\\test10.xml","test\\parser\\test_classe1\\test10.e" // 29
		,"test\\parser\\test_classe1\\test11.xml","test\\parser\\test_classe1\\test11.e" // 30
		,"test\\parser\\test_classe1\\test12.xml","test\\parser\\test_classe1\\test12.e"
		,"test\\parser\\test_classe1\\test13.xml","test\\parser\\test_classe1\\test13.e"
		,"test\\parser\\test_heritage\\test14.xml","test\\parser\\test_heritage\\test14.e"
		,"test\\parser\\test_heritage\\test15.xml","test\\parser\\test_heritage\\test15.e"
		,"test\\parser\\test_attr\\test1.xml","test\\parser\\test_attr\\test1.e"
		,"test\\parser\\test_attr\\test2.xml","test\\parser\\test_attr\\test2.e" // 36
		,"test\\parser\\test_attr\\test3.xml","test\\parser\\test_attr\\test3.e"
		,"test\\parser\\test_attr\\test4.xml","test\\parser\\test_attr\\test4.e"
		,"test\\parser\\test_attr\\test5.xml","test\\parser\\test_attr\\test5.e"
		,"test\\parser\\test_attr\\test6.xml","test\\parser\\test_attr\\test6.e" // 40
		,"test\\parser\\test_instr\\test1.xml","test\\parser\\test_instr\\test1.e"
		,"test\\parser\\test_instr\\test2.xml","test\\parser\\test_instr\\test2.e"
		,"test\\parser\\test_instr\\test4.xml","test\\parser\\test_instr\\test4.e"
		,"test\\parser\\test_instr\\test5.xml","test\\parser\\test_instr\\test5.e"
		,"test\\parser\\test_instr\\test6.xml","test\\parser\\test_instr\\test6.e" // 45
		,"test\\parser\\test_instr\\test7.xml","test\\parser\\test_instr\\test7.e"
		,"test\\parser\\test_instr\\test8.xml","test\\parser\\test_instr\\test8.e"
		,"test\\parser\\test_instr\\test10.xml","test\\parser\\test_instr\\test10.e"
		,"test\\parser\\test_instr\\test11.xml","test\\parser\\test_instr\\test11.e"
		,"test\\parser\\test_instr\\test13.xml","test\\parser\\test_instr\\test13.e" // 50
		,"test\\parser\\test_type\\test1.xml","test\\parser\\test_type\\test1.e"
		,"test\\parser\\test_type\\test2.xml","test\\parser\\test_type\\test2.e"
		,"test\\parser\\test_type\\test3.xml","test\\parser\\test_type\\test3.e"
		,"test\\parser\\test_expr\\test1.xml","test\\parser\\test_expr\\test1.e"
		,"test\\parser\\test_expr\\test2.xml","test\\parser\\test_expr\\test2.e" // 55
		,"test\\parser\\test_expr\\test3.xml","test\\parser\\test_expr\\test3.e"
		,"test\\parser\\test_expr\\test4.xml","test\\parser\\test_expr\\test4.e"
		,"test\\parser\\test_expr\\test6.xml","test\\parser\\test_expr\\test6.e"
		,"test\\parser\\test_expr\\test5.xml","test\\parser\\test_expr\\test5.e"
		,"test\\parser\\test_expr\\test7.xml","test\\parser\\test_expr\\test7.e" // 60
		,"test\\parser\\test_expr\\test8.xml","test\\parser\\test_expr\\test8.e"
		,"test\\parser\\test_classe1\\test8.xml","test\\parser\\test_classe1\\test8.e"
		,"test\\parser\\test_classe1\\test14.xml","test\\parser\\test_classe1\\test14.e"
		,"test\\parser\\test_classe1\\test15.xml","test\\parser\\test_classe1\\test15.e"
		,"test\\parser\\test_classe1\\test16.xml","test\\parser\\test_classe1\\test16.e" // 65
		,"test\\parser\\test_expr\\test9.xml","test\\parser\\test_expr\\test9.e"
		,"test\\parser\\test_instr\\test12.xml","test\\parser\\test_instr\\test12.e"
		,"test\\parser\\test_type\\test5.xml","test\\parser\\test_type\\test5.e"
		,"test\\parser\\test_type\\test4.xml","test\\parser\\test_type\\test4.e"
		,"test\\parser\\test_instr\\test3.xml","test\\parser\\test_instr\\test3.e" // 70
		,"test\\parser\\test_expr\\test10.xml","test\\parser\\test_expr\\test10.e"
		,"test\\parser\\test_expr\\test11.xml","test\\parser\\test_expr\\test11.e"
		,"test\\parser\\test_expr\\test12.xml","test\\parser\\test_expr\\test12.e"
		,"test\\parser\\test_expr\\test13.xml","test\\parser\\test_expr\\test13.e" // 74
		//,"test\\parser\\test1\\test1.xml","test\\parser\\test1\\test1.e"*/
		};

	public static final int no_test=74; // le no de ligne en commencant a 0

	/*********************************************
	 * fichier eiffel avec des pb:
	 * ,"test\\parser\\test_attr\\test7.xml","test\\parser\\test_attr\\test7.e" : les commentaire de feature ne sont pas mis dans le fichier xml
	 * ,"test\\parser\\test_expr\\test10.xml","test\\parser\\test_expr\\test10.e" : expressions dans assert sans ; de séparation
	 * correcte de traité "9.r()" comme "9.;r();" ? (9.46.t est 9.46.t;) (cf test_expr\test12.e)
	 */

	public void test_parser()
	{
		for(int i=0;i<nom.length;i+=2)
		{
			Compiler_Eiffel compiler=new Compiler_Eiffel();
			compiler.configure(nom[i+1],TestAll.path_test);
			Classe c=compiler.parse_file(nom[i+1]);
			assert(c!=null):"nom="+nom[i];
			assert(compiler.nb_erreur()==0):"nom="+nom[i];
			assertTrue(compiler.nb_erreur()==0);
			compiler.termine();
		}
	}

	/**
	 * verifie que le fichier parse est equivalent 
	 * au fichier XML de referrence
	 *
	 */
	public void test_correcte()
	{
		//System.out.println("coucou6");
		assertTrue(nom!=null);
		assertTrue(nom.length%2==0);
		//assertEquals(1,1);
		for(int i=0;i<nom.length;i+=2)
		{
			AstXML ast=new AstXML();
			Classe c=ast.parse(nom[i]);
			c.check_egal(c);
			assertTrue(true);
			Compiler_Eiffel compiler=new Compiler_Eiffel();
			compiler.configure(nom[i+1],TestAll.path_test);
			Classe c2=compiler.parse_file(nom[i+1]);
			assertTrue(c2!=null);
			c2.check_egal(c2);
			assertTrue(true);
			System.out.println("i="+i+";"+nom[i]+";"+nom[i+1]);
			//if(i/2==5)
			//	System.exit(0);
			c.check_egal(c2);
			assertTrue(true);
			c2.check_egal(c);
			assertTrue(true);
			compiler.termine();
		}
		
		//assertTrue(true);
		//System.out.println("coucou2");
	}
	
	/**
	 * verifie que le fichier XML genere est
	 * equivalent a l'original
	 *
	 */
	public void test_correcte2() throws IOException
	{
		String nom_fichier="test\\test0-2.xml";
		//System.out.println("coucou6");
		assertTrue(nom!=null);
		assertTrue(nom.length%2==0);
		//assertEquals(1,1);
		for(int i=0;i<nom.length;i+=2)
		{
			Compiler_Eiffel compiler=new Compiler_Eiffel();
			compiler.configure(nom[i+1],TestAll.path_test);
			Classe c=compiler.parse_file(nom[i+1]);
			c.check_egal(c);
			assertTrue(true);
			try {
				File f=new File(nom_fichier);
				f.delete();
				OutputStream out=new FileOutputStream(nom_fichier);
				c.toXML(new PrintStream(out));
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				assertTrue(false);
			} catch (IOException e) {
				System.out.println("Erreur:"+e);
				e.printStackTrace();
			}
			AstXML ast=new AstXML();
			Classe c2=ast.parse(nom_fichier);
			assertTrue(c2!=null);
			c2.check_egal(c2);
			assertTrue(true);
			c.check_egal(c2);
			assertTrue(true);
			c2.check_egal(c);
			assertTrue(true);
			Compare_XML comp=new Compare_XML(nom_fichier,nom[i]);
			assert(comp.parse()):nom_fichier+"!="+nom[i]+
                "("+nom[i+1]+"):"+comp.erreur();
			compiler.termine();
		}
		
		//assertTrue(true);
		//System.out.println("coucou2");
	}
	
	public void testAssertion()
	{
		//System.out.println("coucou5");
		assertEquals(12,12);
		assertTrue(TestAll.assertion_active());
		//System.out.println("coucou4");
	}
	
	private static Logger logger;
	public void t_estLog(){
		try {
			logger = Logger.getLogger("com.wombat.nose");
			FileHandler fh = new FileHandler("test_log.txt");
			logger.addHandler(fh);
			// Log a FINE tracing message
			logger.fine("doing stuff");
			try{
				int i=0;
				//i=7/i;
			} catch (ArithmeticException ex){
				// Log the error
				logger.log(Level.WARNING,"Erreur d'arithmetique"/*,ex*/);
			} catch (Error ex){
				// Log the error
				logger.log(Level.WARNING,"trouble sneezing",ex);
			}
			logger.fine("done");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static Test suite()
	{
//		TestSuite t=new TestSuite();
		//t.addTest(new TestXmlSimple("test_unitaire\\test_class1.xml"));
//		t.addTest(new Test_unitaire1());
//		return t;//new TestSuite(Test_unitaire1.class);
		//System.out.println("coucou1");
		return new TestSuite(TestXmlSimple.class);
	}
	
	public static void main(String arg[])
	{
		junit.textui.TestRunner.run(suite());
		//genere_xml();
		//vitesse();
	}
	
	public static void genere_xml()
	{
		int i,no;
		String nom_fichier="test\\parser\\test_tmp.xml";
		Compiler_Eiffel compiler=new Compiler_Eiffel();
		no=(no_test)*2;
		compiler.configure(nom[no+1],TestAll.path_test);
		Classe c=compiler.parse_file(nom[no+1]);
		//assert(c!=null):"nom="+nom[no];
		if(compiler.nb_erreur()!=0)
		{
			compiler.affiche_erreur();
			assert(false);
		}
		else
			assertTrue(compiler.nb_erreur()==0);
		try {
			File f=new File(nom_fichier);
			f.delete();
			OutputStream out=new FileOutputStream(nom_fichier);
			c.toXML(new PrintStream(out));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			System.out.println("Erreur:"+e);
			e.printStackTrace();
		}
		compiler.termine();
		System.out.println("Terminer");
		//assert(junit.textui.TestRunner!=null);
		//System.out.println("coucou0");
		//if(arg.length>1)
		//	junit.awtui.TestRunner.run(suite());
		//else
//			junit.textui.TestRunner.run(suite());
		//System.out.println("coucou3");
		//TestResult result= 
		//suite().run(null);
		//TestSuite suite=(TestSuite)suite();
		//TestResult result= suite.run(suite);
				/*AstXML ast=new AstXML();
				Classe c=ast.parse("");
				c.check_egal(c);*/
				//System.out.println("Fin des tests");
	}

	public static void vitesse()
	{
		int i,no,nb_annalyse=100;
		Date date1=new Date(),date2;
		String nom_fichier/*="test\\parser\\test_tmp.xml"*/;
		Compiler_Eiffel compiler=new Compiler_Eiffel();
		boolean test_lexer=false;
		no=1;
		test_lexer=true;
		System.out.println("Test de vitesse");
		//nom_fichier="test\\parser\\test_tmp.xml";
		nom_fichier="E:\\SmallEiffel0\\tutorial\\fibonacci.e";
		//nom_fichier="E:\\SmallEiffel0\\lib\\kernel\\general.e";
		nom_fichier="E:\\SmallEiffel0\\lib\\kernel\\string.e";
		//nom_fichier="E:\\projet\\eiffel\\test\\parser\\test_exemple\\test2.e";
		System.out.println("Fichier:"+nom_fichier+" ("+nb_annalyse+" analyses)");
		compiler.configure(nom_fichier,TestAll.path_test);
		//System.out.println("T1");
		if(test_lexer)
			System.out.println("Lexer");
		else
			System.out.println("Lexer+Parsing");
		for(i=0;i<nb_annalyse;i++)
		{
			if(test_lexer)
			{
				parcourt_lexer(compiler,nom_fichier);
			}
			else
			{
				Classe c=compiler.parse_file(nom_fichier);
				//System.out.println("T2");
				//assert(c!=null):"nom="+nom_fichier;
				if(compiler.nb_erreur()!=0)
				{
					compiler.affiche_erreur();
					assert(false);
				}
				else
				{
					assert(c!=null):"nom="+nom_fichier;
					assertTrue(compiler.nb_erreur()==0);
					//System.out.println("Pas d'erreur");
				}
			}
			
		}
		/*try {
			File f=new File(nom_fichier);
			f.delete();
			OutputStream out=new FileOutputStream(nom_fichier);
			c.toXML(new PrintStream(out));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			System.out.println("Erreur:"+e);
			e.printStackTrace();
		}*/
		date2=new Date();
		long max_mem1,total_mem1,free_mem1;
		long max_mem2,total_mem2,free_mem2,libere2;
		long max_mem3,total_mem3,free_mem3,libere3;
		Runtime runtime;
		runtime=Runtime.getRuntime();
		// avant le GC
		max_mem1=runtime.maxMemory();
		total_mem1=runtime.totalMemory();
		free_mem1=runtime.freeMemory();
		System.out.println("Avant GC:");
		System.out.println("total mem:"+total_mem1+", max mem="+max_mem1+", free mem="+free_mem1);
		// GC
		runtime.gc();
		// apres le GC
		max_mem2=runtime.maxMemory();
		total_mem2=runtime.totalMemory();
		free_mem2=runtime.freeMemory();
		libere2=(total_mem1-free_mem1)-(total_mem2-free_mem2);
		System.out.println("Apres GC:");
		System.out.println("total mem:"+total_mem2+", max mem="+max_mem2+", free mem="+free_mem2+", libere="+(libere2));
		// GC2
		if(compiler!=null)
		{
			compiler=null;
		}
		compiler=null;
		runtime.gc();
		// apres le GC2
		max_mem3=runtime.maxMemory();
		total_mem3=runtime.totalMemory();
		free_mem3=runtime.freeMemory();
		libere3=(total_mem2-free_mem2)-(total_mem3-free_mem3);
		System.out.println("Apres GC2:");
		System.out.println("total mem:"+total_mem3+", max mem="+max_mem3+", free mem="+free_mem3+", libere="+(libere3));
		// Duree
		System.out.println("Durée:");
		System.out.println(date1.toString()+"->"+date2.toString()+"("+nb_annalyse+" analyses)");
		System.out.println("Terminer");
		compiler.termine();
		//assert(junit.textui.TestRunner!=null);
		//System.out.println("coucou0");
		//if(arg.length>1)
		//	junit.awtui.TestRunner.run(suite());
		//else
//			junit.textui.TestRunner.run(suite());
		//System.out.println("coucou3");
		//TestResult result= 
		//suite().run(null);
		//TestSuite suite=(TestSuite)suite();
		//TestResult result= suite.run(suite);
				/*AstXML ast=new AstXML();
				Classe c=ast.parse("");
				c.check_egal(c);*/
				//System.out.println("Fin des tests");
	}
	
	public static void parcourt_lexer(Compiler_Eiffel compiler,String nom_fichier)
	{
		assert(compiler!=null);
		assert(nom!=null);
		Classe c;
		String nom_reel=null;
		EiffelParser parser_tmp=null;
		nom_reel = compiler.donne_nom(nom_fichier);
		if (nom_reel == "") {
			//logging.erreur(new ErreurIO("Fichier " + nom_fichier + " non trouve",nom_fichier));
			System.out.println("Fichier " + nom_fichier + " non trouve");
			assert(false);
			return ;
		}
		try {
			int i=0;
			EiffelLexer lexer;
			lexer=new EiffelLexer(new FileInputStream(nom_reel));
			//lexer=lecture_fichier(nom_reel);
			lexer.setFilename(nom_reel);
			antlr.Token token;
			token=lexer.nextToken();
			assert(token!=null);
			while(token!=null&&token.getType()!=antlr.Token.EOF_TYPE)
			{
				token=lexer.nextToken();
				i++;
			}
			//System.out.println("nb token="+i);
			
			//EiffelParser parser = new EiffelParser(lexer);
			//parser.setASTNodeClass("compiler.ASTDefaut");
			//parser.setFilename(nom_reel);
			//parser.lexer = lexer;
			//parser.logging=logging;
			//parser_tmp=parser;
			//parser.logging=logging;
			// Parse the input expression
			//System.out.println("A1");
			//c = parser.classe();
			//System.out.println("A2");
			/*Vector liste_classe = parser.type_utilisee;
			//System.out.println("classe:" + liste_classe);
			if (c == null) {
				return ;
			}
			if(c.type!=null&&
				c.type.generique!=null&&
				c.type.generique.length>0)
			{// elimination dans liste_classe 
				// des parametres generiques
				for(int i=0;i<c.type.generique.length;i++)
				{
					Type t1,t2;
					t1=c.type.generique[i];
					for(int j=0;j<liste_classe.size();j++)
					{
						t2=(Type)liste_classe.elementAt(j);
						if(t1.egaux(t2))
						{
							liste_classe.remove(j);
							j--;
						}
					}
				}
			}*/
			//c.set_liste_classe(liste_classe);
		} catch (TokenStreamException e) {
			//erreur("exception: " + e);
			int x,y;
	
			x=parser_tmp.lexer.getColumn();
			y=parser_tmp.lexer.getLine();
			//parser_tmp.lexer.getFilename();
			if(e instanceof TokenStreamRecognitionException)
			{
				//x=((TokenStreamRecognitionException)e).;
				//y=((TokenStreamRecognitionException)e);
			}
			System.out.println("Erreur lexicale:"+
							e.getMessage());
			return;
		} /*catch (RecognitionException e) {
			//erreur("exception: " + e);
			System.out.println("Erreur de parsing:"+
							e.getMessage());
			return;
		}*/ catch (FileNotFoundException e) {
			//erreur("Erreur1: fichier " + nom_fichier + " non trouve");
			//erreur("exception: " + e);
			//assert(false);
			System.out.println("Fichier "+nom_fichier+" non trouve");
			return;
		} catch (SecurityException e) {
			System.out.println("Erreur: erreur de securite:"+e);
			return;
		}
	}
	
	public static EiffelLexer lecture_fichier(String nom_fichier) throws IOException
	{// lecture du fichier puis création du lexer
		EiffelLexer lexer;
		String str="",s;
		FileInputStream file;
		byte buf[];
		int nb,nb_car=0,j,len,nb2=0;
		StringBuffer s2,s3;
		file=new FileInputStream(nom_fichier);
		buf=new byte[10000];
		s2=new StringBuffer();
		//assert(nb_car==s2.length()):"err:"+s2.length()+"!="+nb_car;
		do
		{
			nb=file.read(buf);
			if(nb!=-1)
			{
				//s=new String(buf);
				//s3=new StringBuffer(buf,0,nb);
				//s2=new StringBuffer();
				//s2.append(s.substring(0,nb));
				//s2.append(s3);
				for(j=0;j<nb;j++)
				{
					//char c0=;
					s2.append((char)buf[j]);
				}
				//s2.append((char[])buf,0,nb);
				//str+=s.substring(0,nb);
				nb_car=nb_car+nb;
				//assert(nb_car==nb2*buf.length+nb):"nb_car:"+nb_car+"!="+(nb2*buf.length+nb)+"("+nb2+","+nb+")";
				//assert(s2.length()==nb2*buf.length+nb):"s2:"+s2.length()+"!="+(nb2*buf.length+nb)+"("+nb2+","+nb+")";
			}
			//assert(nb_car==s2.length()):"err:"+s2.length()+"!="+nb_car+"("+nb2+","+nb+")";
			nb2++;
		} while(nb!=-1);
		//System.out.println("nombre de caractères:"+nb_car);
		//assert(s2.length()==nb_car):"err:"+s2.length()+"!="+nb_car;
		lexer=new EiffelLexer(new StringReader(s2.toString()));
		return lexer;
	}
	
	public String nom_fichier;

}
