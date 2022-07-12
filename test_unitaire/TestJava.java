package tinyeiffel.test_unitaire;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import tinyeiffel.compiler.Compiler_Eiffel;

public class TestJava extends TestCase {

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

	public TestJava(String nom)
	{
		super(nom);
	}
	

	public void testAssert()
	{
		assertTrue(TestAll.assertion_active());
		logger.info("Assertions activés");
	}
	
	public void testSuppr() throws IOException
	{
		File f,f2;
		f=new File("resultat");
		if(!f.exists())
			f.mkdir();
		assertTrue(f.exists());
		f2=new File(f,"Test1");
		f2.createNewFile();
		assertTrue(f2.exists());
		assertTrue(f.listFiles()!=null);
		assertTrue(f.listFiles().length>0);
		supprime_tout(f);
		assertTrue(!f2.exists());
		assertTrue(f.listFiles()==null||f.listFiles().length==0);
	}
	
	public void testExecution()
	{
		Compiler_Eiffel comp;
		String tab[][];
		int i;
		File f;
		String source,sortie,sortie_err;
		tab=parseXml("test\\genere_java\\test_exec.xml");
		assertTrue(tab!=null);
		assert(tab!=null);
		assertTrue(tab.length>0);
		for(i=0;i<tab.length;i++)
		{
			System.out.println("coucou{"+tab[i][0]+","+tab[i][1]+","+tab[i][2]+"}");
			source=tab[i][0];
			sortie=tab[i][2];
			sortie_err=tab[i][3];
			f=new File("resultat");
			if(f.exists()&&f.listFiles()!=null&&f.listFiles().length>0)
			{
				supprime_tout(f);
			}
			assert(f.exists());
			assert(f.listFiles()==null||f.listFiles().length==0);
			copy(new File("test/genere_java/lib/Utils.class"),new File("resultat/Utils.class"));
			comp=new Compiler_Eiffel(source,Compiler_Eiffel.genere_java,null);
			if(comp.nb_erreur()!=0)
			{
				logger.severe("Il y a des erreur pour le fichier "+source);
				logger.severe(comp.logging.toString());
				assertTrue("Il y a des erreur pour le fichier "+source+
						"("+comp.logging.toString()+")",
						comp.nb_erreur()==0);
				assert(false);
			}
			else
			{
				System.out.println("Execution...");
				String res,res_ref,res_ref_err;
				String user_dir;
				user_dir=System.getProperty("user.dir");
				System.out.println("user_dir="+user_dir);
				res_ref=lecture_fichier(sortie);
				if(sortie_err==null||sortie_err.equals(""))
					res_ref_err="";
				else
					res_ref_err=lecture_fichier(sortie_err);
				assertNotNull(res_ref);
				ResExec exec;
				exec=execution_programme("java -cp resultat\\ TEST1_Imp");
				System.out.println("res_ref={"+res_ref+"}");
				System.out.println("out={"+exec.out+"}"+exec.code_sortie);
				System.out.println("err={"+exec.err+"}");
				assertEquals(exec.code_sortie,0);
				assertNotNull(res_ref);
				assertNotNull(exec.out);
				assertNotNull(exec.err);
				assertNotNull(res_ref);
				assertEquals(res_ref,exec.out);
				assertEquals(res_ref_err,exec.err);
			}
		}
	}


	private ResExec execution_programme(String commande) {
		ResExec res=null;
		try {
			Process proc;
			StringInputStream out,err;
			proc = Runtime.getRuntime().exec(commande);
			out=new StringInputStream(new BufferedInputStream(proc.getInputStream()));
			err=new StringInputStream(new BufferedInputStream(proc.getErrorStream()));
			proc.waitFor();
			res=new ResExec();
			res.code_sortie=proc.exitValue();
			res.out=out.getText();
			res.err=err.getText();
		} catch (IOException e) {
			e.printStackTrace();
			res=null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			res=null;
		}
		return res;
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

	private void copy(File source, File destination) {
		boolean resultat = false;
		// Declaration des flux
		FileInputStream sourceFile = null;
		FileOutputStream destinationFile = null;
		try {
			// Création du fichier :
			destination.createNewFile();
			// Ouverture des flux
			sourceFile = new FileInputStream(source);
			destinationFile = new FileOutputStream(destination);
			byte buffer[] = new byte[512 * 2];
			int nbLecture;
			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
			}
			// Copie réussie
			resultat = true;
		} catch (java.io.FileNotFoundException f) {
		} catch (java.io.IOException e) {
		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				if (sourceFile != null)
					sourceFile.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (destinationFile != null)
					destinationFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assert (resultat);
	}

	private void supprime_tout(File f) {
		assert(f.exists());
		assert(f.getAbsolutePath().startsWith("E:\\projet\\eiffel\\resultat"));
		if(f.isDirectory())
		{
			File tab[];
			int i;
			tab=f.listFiles();
			if(tab!=null)
			{
				for(i=0;i<tab.length;i++)
				{
					if(tab[i].isDirectory())
					{
						supprime_tout(tab[i]);
						tab[i].delete();
					}
					else
					{
						tab[i].delete();
					}
				}
			}
		}
		else
		{
			f.delete();
		}
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
		String source="",ref="",src_xml="",res[],ref_err="";
		
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

		res=new String[4];
		res[0]=source;
		res[1]=src_xml;
		res[2]=ref;
		res[3]=ref_err;
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
		return new TestSuite(TestJava.class);
	}

}
class ResExec
{
	int code_sortie;
	String out,err;
}
class StringInputStream implements Runnable
{
	private InputStream in;
	private Thread th;
	private String res;
	private boolean erreur;
	
	public StringInputStream(InputStream in)
	{
		this.in=in;
		th = new Thread(this);
        th.start();
	}
	
	public void run() {
		byte buffer[]=new byte[512*1024];
        int nbLecture,i;
        res="";
        try {
			while( (nbLecture = in.read(buffer)) != -1 ) {
			     //destinationFile.write(buffer, 0, nbLecture);
				for(i=0;i<nbLecture;i++)
				{
					res+=(char)buffer[i];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			res="";
			erreur=true;
		} 
	}
	
	public String getText()
	{
		return res;
	}
	
	public boolean getErreur()
	{
		return erreur;
	}
}