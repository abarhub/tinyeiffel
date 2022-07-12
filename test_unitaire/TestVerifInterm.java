/*
 * Created on 11 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.test_unitaire;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import tinyeiffel.intermediaire.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author barret
 *
 * Test la verification de coherence du code intermediaire
 * 
 */
public class TestVerifInterm extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestVerifInterm.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if(logger==null||true)
		{
			logger=Logger.getLogger("tinyeiffel.test.test_verif_interm");
			if(fh==null)
			{
				try {
					fh = new FileHandler("test_verif_interm.log",true);
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

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		logger.info("Tests Finis");
		if(fh!=null)
		{
			logger.removeHandler(fh);
			fh.close();
			logger=null;
			fh=null;
		}
	}

	/**
	 * Constructor for TestVerifInterm.
	 * @param arg0
	 */
	public TestVerifInterm(String arg0) {
		super(arg0);
	}

	public static Test suite()
	{
		return new TestSuite(TestVerifInterm.class);
	}

	public void test_assert()
	{
		logger.info("Vérification assert ...");
		assertTrue(TestAll.assertion_active());
		logger.info("Vérification assert faite");
	}
	
	public void testCIVerification() {
		FichierVerifInterm liste_fichiers[],f;
		CIInputXML inp;
		CIProgramme programme;
		CIVerification verif;
		logger.info("Debut Verification...");
		liste_fichiers=lecture_fichier("test/verif_interm/test.xml");
		assert(liste_fichiers!=null);
		assert(liste_fichiers.length>0);
		int i;
		for(i=0;i<liste_fichiers.length;i++)
		{
			f=liste_fichiers[i];
			assert(f!=null);
			assert(f.nom!=null);
			System.out.println("nom="+f.nom);
			inp=new CIInputXML(f.nom);
			programme=inp.programme;
			assertTrue(programme!=null);
			System.out.println("Fichier XML des erreurs :");
			System.out.println(liste_string(f.MsgErreur));
			verif=new CIVerification(programme);
			if(verif.verification())
			{// Il n' a pas d'erreur
				String s;
				int j;
				s=liste_string(f.MsgErreur,";");
				if(f.erreur())
				{
					message_debug(f, null);
				}
				assert(!f.erreur()):"Il y a des erreurs :"+f.nom+"="+s;
			}
			else
			{// Il y a des erreurs
				if(i==11)
				{
					System.out.println("coucou");
				}
				if(!f.erreur()||!f.equivalent(verif.message_erreur(),"Verification"))
				{
					message_debug(f, verif);
				}
				assert(f.erreur()):"Il n'y a pas d'erreur ("+
					verif.message_erreur()[0]+":"+f.nom+")";
				assert(f.equivalent(verif.message_erreur(),"Verification")):
					f.typeErreur+":"+f.nom;
			}
		}
		logger.info("Il y a eu "+liste_fichiers.length+
				" verifications");
		System.out.println("Il y a eu "+liste_fichiers.length+
				" verifications");
	}

	private void message_debug(FichierVerifInterm f, CIVerification verif) {
		System.out.flush();
		System.err.println("Fichier "+f.nom);
		System.err.println("Erreur XML:");
		System.err.println(liste_string(f.MsgErreur));
		System.err.println("Erreur Verification:");
		if(verif!=null)
		{
			System.err.println(liste_string(verif.message_erreur()));
		}
		else
		{
			System.err.println("Rien");
		}
		System.err.flush();
	}
	
	public static String liste_string(String[] liste)
	{
		return liste_string(liste,"\n");
	}
	
	public static String liste_string(String[] liste,String separation)
	{
		String res="";
		if(liste==null)
		{
		}
		else
		{
			for(int i=0;i<liste.length;i++)
			{
				if(i>0)
					res+=separation;
				res+=liste[i];
			}
		}
		return res;
	}
	
	public FichierVerifInterm[] lecture_fichier(String nom)
	{
		assert(nom!=null);
		FichierVerifInterm[] res=null;
		XMLVerifInterm xml;
		xml=new XMLVerifInterm();
		res=xml.parse(nom);
		return res;
	}
	
	//public static FichierVerifInterm liste_fichier[];
	protected Logger logger;
	protected FileHandler fh;
}
