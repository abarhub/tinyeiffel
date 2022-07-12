/*
 * Created on 28 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.test_unitaire;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestExtern extends TestCase {

	String liste_fichier[]=
	{"d:\\projet\\eiffel\\test\\extern\\test1\\test1.xml"};
	
	public void testParser()
	{
		
		
	}

	public void testAssertion()
	{
		//System.out.println("coucou5");
		assertEquals(12,12);
		assertTrue(TestAll.assertion_active());
		//System.out.println("coucou4");
	}

	public static Test suite()
	{
		return new TestSuite(TestExtern.class);
	}
	
	public static void main(String arg[])
	{
		junit.textui.TestRunner.run(suite());
	}
	
}
