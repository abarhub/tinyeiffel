/*
 * Created on 10 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.test_unitaire;

import tinyeiffel.compiler.Compiler_Eiffel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenereClasseTest extends TestCase {

	public void testGenereClasses() {
		GenereClasses c;
		Compiler_Eiffel compiler;
		String chemin,fichier_ace;
		int nb_classe=300,nb_feature=100,
			nb_instr=3,nb_local=2;
		chemin="test\\genere_classe\\test2";
		fichier_ace="test.ace";
		c=new GenereClasses(chemin,
				fichier_ace,nb_classe,nb_feature,
				nb_instr,nb_local);
		c.traitement();
		c=null;
		System.gc();
		compiler=new Compiler_Eiffel(chemin+"\\"+fichier_ace,
				Compiler_Eiffel.code_interm,null);
		assertTrue(compiler.nb_erreur()==0);
	}

	public void testAssertion()
	{
		assertTrue(TestAll.assertion_active());
	}
	
	public static Test suite() 
	{
		TestSuite suite = new TestSuite();
		assert(suite!=null);
		//assert(TestAll.log!=null):"log null";

		suite.addTest(new TestSuite(GenereClasseTest.class));
		//log.info("nombre de suite de testes:"/*+suite.testCount()*/);
		
		return suite;
	}
	
	public static void main(String arg[])
	{
		junit.textui.TestRunner.run(suite());
	}
	
}
