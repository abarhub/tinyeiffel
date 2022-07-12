package tinyeiffel.test_unitaire;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import tinyeiffel.intermediaire.CIInputXML;
import tinyeiffel.intermediaire.CIProgramme;
import tinyeiffel.intermediaire.CIVerification;
import tinyeiffel.interpreteur2.Interpreteur2;

public class TestInterpretationIntermediaire extends TestCase {

	protected Logger logger;
	protected FileHandler fh;
	
	protected void setUp() throws Exception {
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

	public void testAssert()
	{
		assertTrue(TestAll.assertion_active());
		logger.info("Assertions activés");
	}
	
	public void test1()
	{
		String tab[][];
		int i;
		File f,classpath;
		String source,sortie,sortie_err;
		CIInputXML inp;
		CIProgramme programme;
		String sortie_ref,sortie_err_ref;
		boolean erreur;
		Interpreteur2 interp;
		String trace,trace_ref;
		tab=parseXml("test\\interpretation\\test_exec.xml");
		assertTrue(tab!=null);
		assert(tab!=null);
		assertTrue(tab.length>0);
		for(i=0;i<tab.length;i++)
		{
			erreur=false;
			System.out.println("coucou{"+tab[i][0]+","+tab[i][1]+","+tab[i][2]+"}");
			source=tab[i][0];
			sortie=tab[i][2];
			sortie_err=tab[i][3];
			trace=tab[i][4];
			{// lecture du fichier XML
				f=new File(source);
				assertTrue(f.exists());
				inp=new CIInputXML(f.getAbsolutePath());
				programme=inp.programme;
				assertTrue(programme!=null);
				classpath=f.getParentFile();
				assertTrue(classpath!=null);
				classpath=new File(f.getParentFile(),"lib");
				assertTrue(classpath!=null);
			}
			{// lecture des fichier de reference
				f=new File(sortie);
				assertTrue("Fichier "+f.getAbsolutePath()+" inconnu",f.exists());
				sortie_ref=lecture_fichier(f.getAbsolutePath());
				assertTrue(sortie_ref!=null);
				if(sortie_err!=null&&sortie_err.length()>0)
				{
					f=new File(sortie_err);
					assertTrue("Fichier "+f.getAbsolutePath()+" inconnu("+sortie_err+")",f.exists());
					sortie_err_ref=lecture_fichier(f.getAbsolutePath());
					assertTrue(sortie_err_ref!=null);
				}
				else
				{
					sortie_err_ref="";
				}
				if(trace!=null&&trace.length()>0)
				{
					f=new File(trace);
					assertTrue("Fichier "+f.getAbsolutePath()+" inconnu("+trace+")",f.exists());
					trace_ref=lecture_fichier(f.getAbsolutePath());
					assertTrue(trace_ref!=null);
				}
				else
				{
					trace_ref="";
				}
			}
			f=null;
			{// verification du code intermediaire
				CIVerification v;
				v=new CIVerification(programme);
				if(v.verification())
				{
					logger.info("Code intermediaire Ok");
					System.out.println("Code intermediaire Ok");
				}
				else
				{
					logger.info("Erreur dans le code intermediaire !!!");
					System.out.println("Erreur dans le code intermediaire !!!");
					erreur=true;
				}
			}
			assertFalse(erreur);
			{// execution et verification du résultat
				ByteArrayOutputStream out2,err2;
				interp=new Interpreteur2();
				out2=new ByteArrayOutputStream();
				err2=new ByteArrayOutputStream();
				interp.setOut(new PrintStream(out2));
				interp.setErr(new PrintStream(err2));
				interp.setTrace(Interpreteur2.TRACE_INSTRUCTION);
				interp.execution(programme, logger,new String[]{classpath.getPath()});
				assertTrue(!interp.erreur());
				logger.info("interpretation ok!");
				compare(source+"(n°"+i+") trace",sortie_ref,out2.toString());
				compare(source+"(n°"+i+") trace",sortie_err_ref,err2.toString());
				compare(source+"(n°"+i+") trace",trace_ref,interp.getTrace());
				//assertEquals(source+"(n°"+i+") out\n",sortie_ref, out2.toString());
				//assertEquals(source+"(n°"+i+") err\n",sortie_err_ref, err2.toString());
				//logger.info("ref="+trace_ref);
				//logger.info("reel="+interp.getTrace());
				//assertEquals(source+"(n°"+i+") trace\n",trace_ref.trim(), interp.getTrace().trim());
			}
		}
	}
	

	private void compare(String msg,String ref, String sortie) {
		char c1,c2;
		if(ref==null||sortie==null)
			return;
		ref=ref.replaceAll("\\r\\n", "\n");
		sortie=sortie.replaceAll("\\r\\n", "\n");
		for(int i=0;i<ref.length();i++)
		{
			assertTrue(msg+"\nLa sorite est plus petite\n"+ref+"\n========\n"+sortie,i<sortie.length());
			c1=ref.charAt(i);
			c2=sortie.charAt(i);
			if(c1!=c2)
			{
				assertEquals(msg+"\nLes caratères ASCII "+((int)c1)+" et "+((int)c2)+
						" ne sont pas pareille à la position "+i,ref, sortie);
			}
			assertTrue(msg+"\nLes caratères ASCII "+((int)c1)+" et "+((int)c2)+
				" ne sont pas pareille à la position "+i+"\n"+ref+"\n========\n"+sortie,c1==c2);
		}
		assertEquals(msg+"\nLa référence est plus petite",ref,sortie);
		assertTrue(msg+"\nLa référence est plus petite\n"+ref+"\n========\n"+sortie,sortie.length()==ref.length());
	}

	private String lecture_fichier(String sortie) {
		FileInputStream sourceFile=null;
		String res=null;
		try {
			sourceFile = new java.io.FileInputStream(sortie);
			byte buffer[]=new byte[512*2];
            int nbLecture,i;
            res="";
            while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
            	for(i=0;i<nbLecture;i++)
            	{
                    res+=(char)buffer[i];
            	}
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			res=null;
		} catch (IOException e) {
			e.printStackTrace();
			res=null;
		}
		assert(res!=null);
		return res;
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
						//f=new File(res[no][0]);
						//res[no][1]=f.getParent()+"\\resultat.xml";
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
		String source="",ref="",src_xml="",res[],ref_err="",trace="";
		
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
			else if (nomAttribut == "reference_erreur")
				ref_err = valeurAttribut;
			else if (nomAttribut == "trace")
				trace = valeurAttribut;
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

		res=new String[5];
		res[0]=source;
		res[1]=src_xml;
		res[2]=ref;
		res[3]=ref_err;
		res[4]=trace;
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
		return new TestSuite(TestInterpretationIntermediaire.class);
	}
	
	public static void main(String arg[])
	{
		ByteArrayOutputStream out,err;
		String fichier="test\\interpretation\\test8\\resultat.xml";
		File f,classpath;
		CIInputXML inp;
		CIProgramme programme;
		Interpreteur2 interp;
		Logger logger;
		f=new File(fichier);
		assert(f.exists()):"fichier inexistant:"+f.getAbsolutePath();
		classpath=new File(f.getParentFile().getParentFile(),"lib");
		inp=new CIInputXML(f.getAbsolutePath());
		programme=inp.programme;
		interp=new Interpreteur2();
		out=new ByteArrayOutputStream();
		err=new ByteArrayOutputStream();
		interp.setOut(new PrintStream(out));
		interp.setErr(new PrintStream(err));
		logger=init_log();
		interp.setTrace(Interpreteur2.TRACE_INSTRUCTION);
		interp.execution(programme, logger,new String[]{classpath.getAbsolutePath()});
		assert(!interp.erreur());
		logger.info("out={"+out.toString()+"}");
		logger.info("err={"+err.toString()+"}");
		logger.info("trace={"+interp.getTrace()+"}");
	}

	private static Logger init_log() {
		Logger logger;
		FileHandler fh;
		logger=Logger.getLogger("tinyeiffel.test.testInterm");
		//if(fh==null)
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
		return logger;
	}
}
