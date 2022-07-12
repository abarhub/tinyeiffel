/*
 * Created on 9 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.test_unitaire;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */


/**
 * <p>Title: </p>
 * <p>Description: Les testes de eiffel</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

//import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
//import ast.*;
import tinyeiffel.compiler.*;
import tinyeiffel.erreur.*;

//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;

//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;

import java.util.*;
import junit.framework.*;
import java.io.*;

public class VerifieContextErreur extends TestCase {


	protected void setUp()
	{
		//System.out.println("coucou-1");
		
	}

	public static Test suite()
	{
//		TestSuite t=new TestSuite();
		//t.addTest(new TestXmlSimple("test_unitaire\\test_class1.xml"));
//		t.addTest(new Test_unitaire1());
//		return t;//new TestSuite(Test_unitaire1.class);
		//System.out.println("coucou1");
		return new TestSuite(VerifieContextErreur.class);
	}
	
	public void test1()
	{
		assertTrue(true);
	}
	
	public void testAssaertion()
	{
		assertTrue(TestAll.assertion_active());
	}
	
	public int no_seul=-1;
	
	public void test2() throws FileNotFoundException
	{
		Classe_Erreur[] liste;
		FileOutputStream file;
		Compiler_Eiffel compiler;
		Classe_Erreur cl;
		Logging log;
		Erreur erreur;
		liste=parse("test\\context\\test_context.xml");
		assert(liste!=null);
		file=new FileOutputStream("test\\context\\test999.xml");
				
		toXML(liste,new PrintStream(file));
		int i=0,j,i_old=-1;
		if(no_seul!=-1)
		{
			i=no_seul;
			i_old=i-1;
		}
		for(;(i<liste.length&&no_seul==-1)||
				(i<no_seul+1&&no_seul!=-1);i++,i_old++)
		{
			assert(i_old+1==i);
			cl=liste[i];
			compiler=new Compiler_Eiffel();
			compiler.logging=new Logging();
			compiler.compile(cl.nom,TestAll.path_test);
			log=compiler.logging;
			Erreur lerreur[]=log.getListe_erreur();
			for(j=0;j<lerreur.length;j++)
			{
				erreur=lerreur[j];
				System.out.println("!"+erreur.toMsg()+"!"/*+"=="
						+((cl.liste_erreur!=null)?cl.liste_erreur[j].text:"null")*/);
				// verification que chaque erreur (erreur) du fichier xml 
				// est trouve par le compilo (cl.liste_erreur)  
				assert(contient(erreur,cl.liste_erreur)):
						i+"Erreur non présente pour "+cl.nom+" (chez compilo):"+erreur.toMsg()+
						"!="+((cl.liste_erreur!=null&&cl.liste_erreur.length>j)?""+cl.liste_erreur[j].text:"null");
			}
			if(cl.liste_erreur==null)
				assert(lerreur.length==0);
			else
			{
				if(lerreur.length!=cl.liste_erreur.length)
				{
					for(int k=0;k<cl.liste_erreur.length;k++)
					{// recherche s'il y a des erreurs dans le compilo (e)
						// qui ne sont pas de le fichier xml (lerreur)
						Err e=cl.liste_erreur[k];
						if(!contient(e,lerreur))
						{
							System.out.println("Erreur non présente (chez XML):"+e.text);
						}
					}
				}
				assert(lerreur.length==cl.liste_erreur.length):
						"Erreur en trop:"+cl.nom+"("+
						lerreur.length+"!="+
						cl.liste_erreur.length+")"+
						"{"+cl.liste_erreur+"}";
			}
			assert(lerreur.length==log.nb_erreur());
			System.gc();
		}
		assert(i==liste.length);
		for(i=0;i<liste.length;i++)
		{
			if(no_seul==-1||no_seul==i)
				System.out.println("Test de "+liste[i].nom);
		}
	}
	
	//public String valeur(Classe_Erreur[])
	
	// retourne true ssi liste contient e
	public boolean contient(Erreur e,Err liste[])
	{
		int i;
		if(liste!=null)
		{
			for(i=0;i<liste.length;i++)
			{
				if(liste[i].same_as(e))
					return true;
			}
		}
		return false;
	}

	public boolean contient(Err e,Erreur liste[])
	{
		int i;
		if(liste!=null)
		{
			for(i=0;i<liste.length;i++)
			{
				if(e.same_as(liste[i]))
					return true;
			}
		}
		return false;
	}
	
	public static void main(String arg[])
	{
		//assert(junit.textui.TestRunner!=null);
		//System.out.println("coucou0");
		//if(arg.length>1)
		//	junit.awtui.TestRunner.run(suite());
		//else
		VerifieContextErreur v=new VerifieContextErreur();
		v.no_seul=15;
		try{
			v.test2();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			assert(false);
		}
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
	
	public void toXML(Classe_Erreur[] liste,PrintStream out)
	{
		assert(liste!=null);
		assert(out!=null);
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE liste_classe SYSTEM \"..\\..\\test_unitaire\\context.dtd\">");
		out.println("<liste_classe>");
		for(int i=0;i<liste.length;i++)
		{
			liste[i].toXML(out);
		}
		out.println("</liste_classe>");
	}

	public Classe_Erreur[] parse(String nom_fichier) {
		assert(nom_fichier!=null);
		
		String xmlFile = "test\\context\test8.xml";
			xmlFile=nom_fichier;
		try {
			DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			//parser.setFeature("http://xml.org/sax/features/validation", true);
			Document document = parser.parse(xmlFile);


			Element catalogue = document.getDocumentElement();

			/*System.out.println(
				"code="
					+ org.w3c.dom.Node.ELEMENT_NODE
					+ ","
					+ org.w3c.dom.Node.ATTRIBUTE_NODE
					+ ","
					+ org.w3c.dom.Node.CDATA_SECTION_NODE
					+ ","
					+ org.w3c.dom.Node.COMMENT_NODE
					+ ","
					+ org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE
					+ ","
					+ org.w3c.dom.Node.DOCUMENT_NODE
					+ ","
					+ org.w3c.dom.Node.DOCUMENT_TYPE_NODE
					+ ","
					+ org.w3c.dom.Node.ENTITY_NODE
					+ ","
					+ org.w3c.dom.Node.ENTITY_REFERENCE_NODE
					+ ","
					+ org.w3c.dom.Node.NOTATION_NODE
					+ ","
					+ org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE
					+ ","
					+ org.w3c.dom.Node.TEXT_NODE
					+ ",");*/
			Classe_Erreur c[] = creer_liste_erreur(catalogue);
			//System.out.println("classe=" + c.toString());
			return c;
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
			System.err.println("Exception Sax:"+e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			// i/o error
			e.printStackTrace();
			System.err.println("Erreur d'entree sortie"+e.getMessage());
			System.exit(1);
		}
		return null;
	}

	public Classe_Erreur[] ajoute_classe(Classe_Erreur[] liste,Classe_Erreur cl)
	{
		assert(cl!=null);
		if(liste==null)
		{
			liste=new Classe_Erreur[1];
			liste[0]=cl;
		}
		else
		{
			int i,len=liste.length;
			Classe_Erreur tmp[]=new Classe_Erreur[len+1];
			for(i=0;i<len;i++)
			{
				tmp[i]=liste[i];
			}
			tmp[i]=cl;
			liste=tmp;
		}
		assert(liste!=null);
		return liste;
	}

	public Classe_Erreur[] creer_liste_erreur(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "liste_classe");
		NamedNodeMap attributs = element.getAttributes();
		Classe_Erreur liste[]=null,cl;

		assert(attributs.getLength()==0);
		//System.out.println("methode 1:");
		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "classe") {
					//System.out.println("heritage");
					cl = new Classe_Erreur((Element) n);
					liste=ajoute_classe(liste,cl);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else if(n instanceof Comment) {
				// commentaire
			} else {
				assert(false);
			}
		}

		assert(liste!=null);

		return liste;
	}

}
class Classe_Erreur
{
	public Classe_Erreur(Element element)
	{
		assert(element!=null);
		creer_classe(element);
	}

	public void creer_classe(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "classe");
		NamedNodeMap attributs = element.getAttributes();
		Vector v = new Vector();
		Err err;

		assert(attributs.getLength()==1);
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				this.nom=valeurAttribut;
			else
				assert(false);
		}
		assert(!nom.equals(""));
		//System.out.println("methode 1:");
		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "erreur") {
					//System.out.println("heritage");
					err = new Err((Element) n);
					ajoute_erreur(err);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else if (n instanceof Comment) {
				// commentaire
			} else {
				assert(false);
			}
		}
		assert(nom!=null);
	}
	
	public void ajoute_erreur(Err err)
	{
		assert(err!=null);
		if(liste_erreur==null)
		{
			liste_erreur=new Err[1];
			liste_erreur[0]=err;
		}
		else
		{
			int i,len=liste_erreur.length;
			Err tmp[]=new Err[len+1];
			for(i=0;i<len;i++)
			{
				tmp[i]=liste_erreur[i];
			}
			tmp[i]=err;
			liste_erreur=tmp;
		}
		assert(liste_erreur!=null);
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.println("<classe nom=\""+nom+"\">");
		if(liste_erreur!=null)
		{
			for(int i=0;i<liste_erreur.length;i++)
			{
				liste_erreur[i].toXML(out);
			}
		}
		out.println("</classe>");
	}

	public Err liste_erreur[];
	public String nom;
}
class Err
{
	public Err(Element element)
	{
		assert(element!=null);
		creer_erreur(element);
	}

	public void creer_erreur(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "erreur");
		NamedNodeMap attributs = element.getAttributes();

		assert(attributs.getLength()==1);
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		assert(!nom.equals(""));
		//System.out.println("methode 1:");
		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s != "");
				text=s;
			} else {
				assert(false);
			}
		}
	}

	public boolean same_as(Erreur e)
	{
		assert(e!=null);
		String str=e.getClass().getName();
		if(str.compareToIgnoreCase("tinyeiffel.erreur."+nom)!=0)
			return false;
		String str2=e.toMsg();
		if(text==null)
		{
			System.out.println("Hello");
		}
		assert(text!=null);
		if(text.compareToIgnoreCase(str2)!=0)
			return false;
		return true;
	}

	public void toXML(PrintStream out)
	{
		assert(out!=null);
		out.print("<erreur nom=\""+nom+"\">");
		out.print(text);
		out.println("</erreur>");
	}
	
	public String nom,text;
}
