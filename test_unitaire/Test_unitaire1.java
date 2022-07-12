package tinyeiffel.test_unitaire;

import junit.framework.*;
import java.io.*;
import tinyeiffel.compiler.*;
//import ast.*;
import java.util.logging.*;


public class Test_unitaire1 extends TestCase
{
	
	private String liste_fichier[];
	private Fichier_test liste_fichier2[];
	
	protected Logger logger;
	protected FileHandler fh;
	
	protected void setUp()
	{
		File f=new File("test");
		ParserXML p=new ParserXML();
		Fichier_test f2[]=p.parse("test_unitaire\\test.xml");
		assertTrue(f2!=null);
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
		/*liste_fichier=f.list(new FiltreFileName(".e"));
		System.out.println("nb="+liste_fichier.length);*/
		liste_fichier2=f2;
		
		if(logger==null||true)
		{
			logger=Logger.getLogger("tinyeiffel.test.test_unitaire");
			if(fh==null)
			{
				try {
					fh = new FileHandler("test_unitaire.log",true);
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
		}
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
	
	public static Test suite()
	{
//		TestSuite t=new TestSuite();
		//t.addTest(new TestXmlSimple("test_unitaire\\test_class1.xml"));
//		t.addTest(new Test_unitaire1());
//		return t;//new TestSuite(Test_unitaire1.class);
		return new TestSuite(Test_unitaire1.class);
	}

	public void testEgal()
	{
		logger.info("12==12");
		assertEquals(12,12);
	}

	public void testAssertion()
	{
		assertTrue(TestAll.assertion_active());
	}
	
	public void testParser()
	{
		int i;
		Fichier_test fichier;
		Compiler_Eiffel c;
		String nom;
		logger.info("Debut Parser...");
		for(i=0;i<liste_fichier2.length;i++)
		{
			fichier=liste_fichier2[i];
			assertTrue(fichier!=null);
			assert(fichier!=null);
			assertTrue(fichier.nom!=null);
			assertTrue(fichier.nom!="");
			nom="test\\"+fichier.nom;
			File f=new File(nom);
			assertTrue(f.exists());
			assertTrue(!f.isDirectory());
			//assert(Compiler_Eiffel.nb_erreur()==0);
			logger.info("parse de "+nom);
			c=new Compiler_Eiffel(nom,TestAll.path_test);
			if(c.nb_erreur()!=fichier.no_erreur)
			{
				logger.severe("Difference nb erreurs "+nom+":"+
				c.nb_erreur()+"!="+
				fichier.no_erreur);
				c.affiche_erreur();
				assertTrue(nom+":"+c.nb_erreur()+"!="+fichier.no_erreur,false);
			}
			/*assert(c.nb_erreur()==fichier.no_erreur):
					"Difference nb erreurs "+nom+":"+
					c.nb_erreur()+"!="+
					fichier.no_erreur;*/
			assertTrue(nom+":"+c.nb_erreur()+"!="+fichier.no_erreur,c.nb_erreur()==fichier.no_erreur);
            //Compiler_Eiffel.logging=new Logging();
		}
		logger.info("Test Parser Ok");
	}

	public static void main(String arg[])
	{
		//assert(junit.textui.TestRunner!=null);
		//if(arg.length>1)
		//	junit.awtui.TestRunner.run(suite());
		//else
			junit.textui.TestRunner.run(suite());
		//TestResult result= 
		//suite().run(null);
		//TestSuite suite=(TestSuite)suite();
		//TestResult result= suite.run(suite);
                /*AstXML ast=new AstXML();
                Classe c=ast.parse("");
                c.check_egal(c);*/
                System.out.println("Fin des tests");
	}
};

