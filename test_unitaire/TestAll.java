/*
 * Created on 18 sept. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.test_unitaire;

import junit.framework.*;

//import java.io.IOException;
import java.util.logging.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestAll {

	public static Logger log;
	public static String[] path_test={ "", ".\\std_classe_test\\", ".\\"};
	
	public static boolean assertion_active()
	{
		boolean trouve=false;
		assert(trouve=true);
		return trouve;
	}

	public static void main(String[] args) 
	{
		String log_file="log_erreur.txt";
		try {
			/*FileHandler fh = new FileHandler(log_file);
			log=Logger.getLogger("TestAll");
			log.addHandler(fh);
			log.setLevel(Level.ALL);
			log.info("Ensemble des testes");*/
			junit.textui.TestRunner.run(suite());
			//junit.swingui.TestRunner.run(suite()); //swingui
			//log.info("Fin de tous les tests");
		} catch (SecurityException e) {
			System.out.println("Erreur de securite"+e.getMessage());
			e.printStackTrace();
		} /*catch (IOException e) {
			System.out.println("Erreur pour creer le fichier "+
					log_file+e.getMessage());
			e.printStackTrace();
		}*/
	}
	
	public static Test suite() 
	{
		TestSuite suite = new TestSuite();
		assert(suite!=null);
		//assert(TestAll.log!=null):"log null";

		suite.addTest(TestXmlSimple.suite());
		suite.addTest(Test_unitaire1.suite());
		suite.addTest(VerifieContextErreur.suite());
		suite.addTest(TestType.suite());
		suite.addTest(TestVerifInterm.suite());
		suite.addTest(TestInterm.suite());
		//suite.addTest(TestJava.suite());
		suite.addTest(TestInterpretationIntermediaire.suite());
		//log.info("nombre de suite de testes:"/*+suite.testCount()*/);
		
		return suite;
	}

}
