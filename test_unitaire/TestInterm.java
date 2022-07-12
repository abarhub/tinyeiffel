/*
 * Created on 17 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.test_unitaire;

import junit.framework.*;
import tinyeiffel.outils.compare_xml.*;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import tinyeiffel.compiler.*;
//import java.util.*;
import tinyeiffel.intermediaire.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestInterm extends TestCase {

	protected Logger logger;
	protected FileHandler fh;
	
	protected void setUp()
	{
		logger=Logger.getLogger("tinyeiffel.test.testInterm");
		if(fh==null)
		{
			try {
				fh = new FileHandler("test_interm.log",true);
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
	
	/**
	 * Constructor for TestInterm.
	 * @param arg0
	 */
	public TestInterm(String arg0) {
		super(arg0);
	}

	public void testAssert()
	{
		assertTrue(TestAll.assertion_active());
		logger.info("Assertions activés");
	}
	
	/**
	 * Vérifie que les fichiers de référence en XML sont correctes
	 *
	 */
	public void testVerif()
	{
		Compare_XML c;
		Compiler_Eiffel comp;
		CIProgramme p,p2;
		CIInputXML inp;
		CIVerification verif;
		int i,nb=0;
		String tab[][],source,ref,src_xml;
		boolean res;
		logger.info("test verif");
		tab=parseXml("test\\interm\\test_all.xml");
		assertTrue(tab!=null);
		assertTrue(tab.length>0);
		for(i=0;i<tab.length;i++)
		{
			source=tab[i][0];
			src_xml=tab[i][1];
			ref=tab[i][2];
			System.out.println("i="+i);
			inp=new CIInputXML(ref);
			p=inp.programme;
			verif=new CIVerification(p);
			res=verif.verification();
			if(!res)
			{
				String s,tab2[];
				int j;
				logger.severe("Il y a des erreur pour le fichier "+source);
				tab2=verif.message_erreur();
				s="";
				for(j=0;j<tab2.length;j++)
				{
					if(j>0)
						s+="\n";
					s+=tab2[j];
				}
				logger.severe("********\n"+s+"***************\n");
				assertTrue("("+source+"-"+i+")"+s,res);
			}
			/*comp=new Compiler_Eiffel(source,Compiler_Eiffel.code_interm);
			if(comp.nb_erreur()!=0)
			{
				logger.severe("Il y a des erreur pour le fichier "+source);
				logger.severe(comp.logging.toString());
				assertTrue(comp.nb_erreur()==0);
			}
			c=new Compare_XML(src_xml,ref);
			if(!c.parse())
			{
				logger.severe("Erreur("+src_xml+"):"+c.erreur());
			}
			//assert(c.parse()):"Erreur("+src_xml+"):"+c.erreur();
			//assert(false);
			//&é"'_è('-((-
			p=comp.programme;
			assertTrue("Erreur("+src_xml+"):"+c.erreur(),p!=null);
			inp=new CIInputXML(src_xml);
			p2=inp.programme;
			System.out.println("Analyse 1 ...");
			p.check_egal(p);
			System.out.println("Analyse 2 ...");
			p2.check_egal(p2);
			System.out.println("Analyse 3 ...");
			p2.check_egal(p);
			System.out.println("Analyse 4 ...");
			p.check_egal(p2);*/
			nb++;
		}
		assertTrue(nb==tab.length);
		System.out.println("nb testes:"+nb);
		//assert(false);
		logger.info("test correcte Ok ("+nb+")");
	}
	
	/**
	 * Comparaison des sources avec les fichiers de reference en XML 
	 *
	 */
	public void testCorrect()
	{
		Compare_XML c;
		Compiler_Eiffel comp;
		CIProgramme p,p2;
		CIInputXML inp;
		int i,nb=0;
		String tab[][],source,ref,src_xml;
		logger.info("test correcte");
		tab=parseXml("test\\interm\\test_all.xml");
		assertTrue(tab!=null);
		assertTrue(tab.length>0);
		for(i=0;i<tab.length;i++)
		{
			source=tab[i][0];
			src_xml=tab[i][1];
			ref=tab[i][2];
			comp=new Compiler_Eiffel(source,Compiler_Eiffel.code_interm,TestAll.path_test);
			if(comp.nb_erreur()!=0)
			{
				logger.severe("Il y a des erreur pour le fichier "+source);
				logger.severe(comp.logging.toString());
				assertTrue("Il y a des erreur pour le fichier "+source+
						"("+comp.logging.toString()+")",
						comp.nb_erreur()==0);
			}
			c=new Compare_XML(src_xml,ref);
			if(!c.parse())
			{
				logger.severe("Erreur("+src_xml+"):"+c.erreur());
			}
			//assert(c.parse()):"Erreur("+src_xml+"):"+c.erreur();
			//assert(false);
			//&é"'_è('-((-
			p=comp.programme;
			assertTrue("Erreur("+src_xml+"):"+c.erreur(),p!=null);
			inp=new CIInputXML(src_xml);
			p2=inp.programme;
			System.out.println("Analyse 1 ...");
			p.check_egal(p);
			System.out.println("Analyse 2 ...");
			p2.check_egal(p2);
			System.out.println("Analyse 3 ...");
			p2.check_egal(p);
			System.out.println("Analyse 4 ...");
			p.check_egal(p2);
			nb++;
		}
		assertTrue(nb==tab.length);
		System.out.println("nb testes:"+nb);
		//assert(false);
		logger.info("test correcte Ok ("+nb+")");
	}
	
	public String[][] parseXml(String nom)
	{
		String res[][]=null;
		File f;
		int no;
		assert(nom!=null);
		try {
			/* jasp */
			//Récupère une instance de la classe de fabrication
			DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
			//Récupére une instance de la classe DocumentBuilder (spécifique vendeur)
			DocumentBuilder parser = factory.newDocumentBuilder();
			//effectue le parsing avec récupération du noeud DOM Document
			Document document = parser.parse(nom);

			Element catalogue = document.getDocumentElement();

			NodeList titres = catalogue.getElementsByTagName("test");
			System.out.println(
				"Nombre d'element dans la racine : " + titres.getLength());
			System.out.println("nom=" + catalogue.getNodeName());
			System.out.println("nom=" + catalogue.getFirstChild());
			System.out.println(
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
					+ ",");
			res=new String[titres.getLength()][];
			NodeList fils = catalogue.getChildNodes();//titres/*.getChildNodes()*/;
			assert(fils.getLength()>0);
			no=0;
			for(int i=0;i<fils.getLength();i++)
			{
				Node n = fils.item(i);
				//System.out.println("element=" + n.getNodeName());
				if (n instanceof Element) {
					if (n.getNodeName() == "test") {
						//System.out.println("heritage");
						res[no] = creer_test_elt((Element) n);
						f=new File(res[no][0]);
						res[no][1]=f.getParent()+"\\resultat.xml";
						no++;
					}
					else
						assert(false);
				}
				else if (n instanceof Text) {
					String s = n.getNodeValue();
					assert(s == "" || s.matches("[ \t\n]+"));
				}
				else if (n instanceof Comment) {
					//String s = n.getNodeValue();
					//assert(s == "" || s.matches("[ \t\n]+"));
				}
				else
					assert(false);
			}
			return res;
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
		/*res=new String[1][3];
		res[0][0]="test\\interm\\test1\\test1.ace";
		//res[0][1]="test\\interm\\test1\\resultat.xml";
		res[0][2]="test\\interm\\test1\\valide.xml";
		f=new File(res[0][0]);
		res[0][1]=f.getParent()+"\\resultat.xml";*/
		return res;
	}

	public String[] creer_test_elt(Element element)
	{
		assert(element != null);
		assert(element.getNodeName() == "test");
		NamedNodeMap attributs = element.getAttributes();
		String source="",ref="",src_xml="",res[];
		
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "source")
				source = valeurAttribut;
			else if (nomAttribut == "source")
				src_xml = valeurAttribut;
			else if (nomAttribut == "reference")
				ref = valeurAttribut;
			else
				assert(false);
		}
		assert(source!=null);
		assert(ref!=null);
		//System.out.println("methode 1:");
		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			 if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}

		res=new String[3];
		res[0]=source;
		res[1]=src_xml;
		res[2]=ref;
		/*System.out.println("methode 2:");
		Node node=element.getFirstChild();
		while(node!=null)
		{
		  Node n=node;
		  System.out.println("element="+n.getNodeName());
		  node=node.getNextSibling();
		}*/
		assert(ref!=null);

		return res;
	}

	public static Test suite()
	{
		return new TestSuite(TestInterm.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestInterm.class);
	}

}
