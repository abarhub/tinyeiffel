package tinyeiffel.compiler;

import java.io.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;
//import sun.reflect.ClassFileAssembler;
import tinyeiffel.ast.*;
import java.util.*;
import tinyeiffel.test_unitaire.*;

public class Eiffel
{
	public static void main(String[] args) {
		if(args!=null&&args.length==1)
		{
			Compiler_Eiffel c=new Compiler_Eiffel(args[0],Compiler_Eiffel.genere_c,null);
			if(c.nb_erreur()>0)
				System.exit(1);
		}
		else
		{
			//test_parser3();
//			test1("compiler.ASTDefaut");
			//teste_classe("test\\test4.e");
			//test_complet();
			//test_parser4();
			//teste_classe("test\\test1-3.e");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\test6.e");
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\test8.e");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\vuex2-2\\test1.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\vtat1\\test1.e");
			//Compiler_Eiffel.prop=new Properties();
			//Compiler_Eiffel.prop.setProperty("new_compatible","yes");;
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\conformite\\ref\\test2.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\conformite\\expand\\test2.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\conformite\\pgene\\test2.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\conformite\\ancre\\test2.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\context\\conformite\\generique\\test1.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\test11.e");
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\test12.e",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interm\\test_external\\test1.ace",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\parser\\test_exemple\\fibonacci.ace");
			//Compiler_Eiffel c=new Compiler_Eiffel("E:\\SmallEiffel0\\lib\\kernel\\character.e");
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interpret\\test1.e",Compiler_Eiffel.exec_prog); 
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\genc\\test1.e",Compiler_Eiffel.genere_c);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\genc\\test1.e",Compiler_Eiffel.convert);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\genc\\test1.e",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interm\\test2\\test1.ace",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\interm\\test_external2\\test1.ace",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interm\\test_attribut2\\test1.ace",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interm\\test_instr1\\test1.ace",Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\smalleiffel\\tutorial\\fibonacci.ace",
					//"D:\\projet\\eiffel\\smalleiffel\\tutorial\\fibonacci.ace",
			//		Compiler_Eiffel.code_interm);
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\simple\\test1\\test1.ace",Compiler_Eiffel.code_interm,true);
			//Compiler_Eiffel c=new Compiler_Eiffel("test\\simple\\test3\\test3.ace",Compiler_Eiffel.code_interm,true);
			Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\genere_java\\test1\\test1.ace",Compiler_Eiffel.genere_java,null);
			//Compiler_Eiffel c=new Compiler_Eiffel(".\\test\\interpretation\\test1\\test1.ace",Compiler_Eiffel.interpretation2,null);
		}
		System.out.println("Fin du programme");
	}

	static boolean test1(String s)
	{
		boolean res=false;
		try {
			Class str= Class.forName(s); // get class def
			res=true;
		}
		catch (Exception e) {
			System.out.println("Erreur de chargement de classe:"+s);
		}
		System.out.println("Chargement réussi de '"+s+"'");
		return res;
	}
	void test_asm()
	{
		//sun.reflect.ClassFileAssembler a;
		
	}

	public static boolean teste_classe(String nom)
	{
		boolean res=true;

		System.out.println("test de la classe "+nom);
		try {
			EiffelLexer lexer = new EiffelLexer(new FileInputStream(nom));
			lexer.setFilename(nom);
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename(nom);
			parser.lexer=lexer;
			//parser.g//nom_fichier=nom;
			// Parse the input expression
			Classe c=parser.classe();
			Vector liste_classe=parser.type_utilisee;
			System.out.println("classe:"+liste_classe);
		}
		catch(TokenStreamException e) {
			System.out.println("exception: "+e);
			res=false;
		}
		catch(RecognitionException e) {
			System.out.println("exception: "+e);
			res=false;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Erreur0: fichier "+nom+" non trouve");
			System.out.println("exception: "+e);
			res=false;
		}
		catch(SecurityException e)
		{
			System.out.println("Erreur: erreur de securite");
			res=false;
		}
		System.out.println("Fin de "+nom);
		System.out.println("----------------------");
		return res;
	}

	public static void test_parser3()
	{//"test\\test1.xml","test\\test1-2.e"
		Compiler_Eiffel compiler=new Compiler_Eiffel();
		Classe c=compiler.parse_file("test\\test1-2.e");
		try {
			
			c.toXML(new PrintStream(new FileOutputStream("test\\test0.xml")));
			c.check_egal(c);
			AstXML ast=new AstXML();
			Classe c2=ast.parse("test\\test1.xml");
			c2.check_egal(c2);
			c2.check_egal(c);
			c.check_egal(c2);
			System.out.println("Ok sans probleme");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//AstXML ast=new AstXML();
		//Classe c2=ast.parse("test\\test1.xml");
		System.out.println("Fini");
	}
	
	public static void test_parser2()
	{
		Compiler_Eiffel compiler=new Compiler_Eiffel();
		Classe c=compiler.parse_file("test\\test1.e");
		try {
			
			c.toXML(new PrintStream(new FileOutputStream("test\\test0.xml")));
			c.check_egal(c);
			AstXML ast=new AstXML();
			Classe c2=ast.parse("test\\test0.xml");
			c2.check_egal(c2);
			c2.check_egal(c);
			c.check_egal(c2);
			System.out.println("Ok sans probleme");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//AstXML ast=new AstXML();
		//Classe c2=ast.parse("test\\test1.xml");
		System.out.println("Fini");
	}

	public static void test_parser()
	{
		String classes_correctes[]={"test1.in","test\\test1.e",
			"test\\test2.e","test\\test3.e","test\\test4.e",
			"test\\test5.e"};
		String classes_incorrectes[]={"toto.in"};
		int i;

		for(i=0;i<classes_correctes.length;i++)
		{
			if(!teste_classe(classes_correctes[i]))
				System.out.println("Erreur avec le fichier "+
						classes_correctes[i]+" *****************************");
		}

		for(i=0;i<classes_incorrectes.length;i++)
		{
			if(teste_classe(classes_incorrectes[i]))
				System.out.println("Erreur avec le fichier "+
						classes_incorrectes[i]+" *****************************");
		}
	}

	public static void test_complet()
	{

		try {
			//if(!test1("ASTDefaut")) return;
			/*CalcLexer lexer = new CalcLexer(new DataInputStream(System.in));
			lexer.setFilename("<stdin>");
			CalcParser parser = new CalcParser(lexer);
			parser.setFilename("<stdin>");
			// Parse the input expression
			parser.expr();
			CommonAST t = (CommonAST)parser.getAST();
			// Print the resulting tree out in LISP notation
			System.out.println(t.toStringTree());
			CalcTreeWalker walker = new CalcTreeWalker();
			// Traverse the tree created by the parser
			float r = walker.expr(t);
			System.out.println("value is "+r);*/
			EiffelLexer lexer = new EiffelLexer(new DataInputStream(System.in));
			lexer.setFilename("<stdin>");
			EiffelParser parser = new EiffelParser(lexer);
			parser.setASTNodeClass("tinyeiffel.compiler.ASTDefaut");
			parser.setFilename("<stdin>");
			parser.lexer=lexer;
			//parser.nom_fichier="<s"
			// Parse the input expression
			Classe c=parser.classe();
/*			System.out.println("Resultat:"+c.nom);
			CommonAST t = (CommonAST)parser.getAST();
			System.out.println("Resultat:");
			System.out.println(t.toStringTree());
			System.out.println("Parcourt de l'arbre:");
			PascalTreeWalker walker = new PascalTreeWalker();
//			walker.programme(t);
			/*System.out.println("Analyse semantique:*******************************");
			PascalCheck check = new PascalCheck();
			check.initialise();
			//t = (CommonAST)parser.getAST();
			check.programme(t);
			System.out.println("table type="+check.getTableType());
			System.out.println("table de symbol="+check.getTableSymbol());
			*/
			//walker.programme_check(t);
//			teste_classe("test\\test3.e");
			test_parser();
			System.out.println("Fin du programme");
		}
		catch(TokenStreamException e) {
			System.err.println("exception: "+e);
		}
		catch(RecognitionException e) {
			System.err.println("exception: "+e);
		}
	}

}