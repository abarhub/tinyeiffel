package tinyeiffel.test_unitaire;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

//import javax.xml.parsers.SAXParser;
import org.xml.sax.ErrorHandler;
//import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLVerifInterm {

	public static void main(String args[]) {
		XMLVerifInterm p = new XMLVerifInterm();
		FichierVerifInterm[] res = p.parse("test\\verif_interm\\test.xml");
		if (res == null) {
			System.out.println("Rien de trouve");
		} else {
			System.out.println("Réussi");
		}
	}

	public FichierVerifInterm[] parse(String xmlFile) {
		//String xmlFile = "file:///xerces-2_5_0/data/personal.xml"; 
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			SAXParser parser = factory.newSAXParser();
			DefaultHandler handler = new TDefaultHandler2();
			XMLReader reader = parser.getXMLReader();
			
			ErrorHandler error_handler = new TErrorHandler2();

			reader.setErrorHandler(error_handler);
			/*parser.
			parser.setProperty("http.proxyHost","192.168.0.1");
			parser.setProperty("http.proxyPort","3128");*/
			reader.setFeature("http://xml.org/sax/features/validation",true);
			//parser.setFeature("http://xml.org/sax/features/validation",true);
			//parser.setFeature("http://xml.org/sax/features/namespaces",false);
			System.out.println("Parsing...");
			parser.parse(xmlFile, handler);
			System.out.println("Terminé");
			FichierVerifInterm f[] = ((TDefaultHandler2) handler).resultat;
			if (f == null)
				System.out.println("null");
			else
				System.out.println("length:" + f.length);
			return f;
		} catch (FactoryConfigurationError e) {
			// unable to get a document builder factory
			System.out.println(
				"Erreur: pb avec le document builder factory("
					+ e.getMessage()
					+ ")");
		} catch (ParserConfigurationException e) {
			// parser was unable to be configured
			System.out.println(
				"Erreur: pb avec la configuration(" + e.getMessage() + ")");
		} catch (SAXException e) {
			// parsing error
			System.out.println(
				"Erreur: erreur de parsing(" + e.getMessage() + ")");
		} catch (IOException e) {
			// i/o error
			System.out.println(
				"Erreur: erreur d'entrée sortie(" + e.getMessage() + ")");
		}
		return null;
	}

}

class TErrorHandler2 implements ErrorHandler {
	public void warning(SAXParseException e) throws SAXException {
		System.err.println(
			"[warning] \n"
				+ " Ligne   : "
				+ e.getLineNumber()
				+ "\n"
				+ " URI     : "
				+ e.getSystemId()
				+ "\n"
				+ " Message : "
				+ e.getMessage());
		throw new SAXException("un avertissement a eu lieu");
	}

	public void error(SAXParseException e) throws SAXException {
		System.err.println(
			"[error] \n"
				+ " Ligne   : "
				+ e.getLineNumber()
				+ "\n"
				+ " URI     : "
				+ e.getSystemId()
				+ "\n"
				+ " Message : "
				+ e.getMessage());
		throw new SAXException("une erreur a eu lieu");
	}

	public void fatalError(SAXParseException e) throws SAXException {
		System.err.println(
			"[fatal error] \n"
				+ " Ligne   : "
				+ e.getLineNumber()
				+ "\n"
				+ " URI     : "
				+ e.getSystemId()
				+ "\n"
				+ " Message : "
				+ e.getMessage());
		throw new SAXException("une erreur fatale a eu lieu");
	}
}

class TDefaultHandler2 extends DefaultHandler {
	protected Locator locator;
	protected boolean dans_erreur=false;

	public FichierVerifInterm[] resultat;

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/** Start document. */
	public void startDocument() throws SAXException {
		System.out.println("Debut du document");
		dans_erreur=false;
	}

	/** Processing instruction. */
	public void processingInstruction(String target, String data)
		throws SAXException {

	}

	/** End document. */
	public void endDocument() throws SAXException {
		System.out.println("Fin du document");
	}

	/** Start prefix mapping. */
	public void startPrefixMapping(String prefix, String uri)
		throws SAXException {

	}

	/** End prefix mapping. */
	public void endPrefixMapping(String prefix) throws SAXException {

	}

	public int convertie(String str) {
		try {
			int i = Integer.parseInt(str, 10);
			return i;
		} catch (NumberFormatException e) {

		}
		return -1;
	}

	/** Start element. */
	public void startElement(
		String uri,
		String localName,
		String qname,
		Attributes attributes)
		throws SAXException {
		//System.out.println("Element:"+localName+"!"+qname);
		if (qname.equals("root")) {
			//System.out.println("root");
		} else if (qname.equals("test")) {
			int nb_erreur = 0, i;
			String nom = "";
			//dans_erreur=true;
			for (i = 0; i < attributes.getLength(); i++) {
				if (attributes.getQName(i).equals("nom")) {
					nom = attributes.getValue(i);
					assert(nom != "");
				} else if (attributes.getQName(i).equals("nb_erreur")) {
					nb_erreur = convertie(attributes.getValue(i));
					assert(nb_erreur >= 0);
					//if(nb_e)
				}
				else
				{
					assert(false):"attribut inconnue:"+attributes.getValue(i);
				}
			}
			FichierVerifInterm f, res[];

			/*System.out.println("pos("+locator.getLineNumber()+
				","+locator.getColumnNumber()+")="+nom+","+nb_erreur+","+
				attributes.getLength());*/
			//echo 
			assert(nb_erreur >= 0);
			assert(nom != "");
			f = new FichierVerifInterm(nom);
			if (resultat == null) {
				res = new FichierVerifInterm[1];
			} else {
				res = new FichierVerifInterm[resultat.length + 1];
				for (i = 0; i < resultat.length; i++)
					res[i] = resultat[i];
			}
			res[res.length - 1] = f;
			resultat = res;
			//System.out.println("Ajouter");
		} else if (qname.equals("erreur")) {
			dans_erreur=true;
			assert(attributes.getLength()==0);
		} else {

		}
	}

	/** End element. */
	public void endElement(String uri, String localName, String qname)
		throws SAXException {
		if(qname.equals("erreur"))
		{
			dans_erreur=false;
		}
	}

	/** Skipped entity. */
	public void skippedEntity(String name) throws SAXException {

	}

	/** Ignorable whitespace. */
	public void ignorableWhitespace(char[] ch, int offset, int length)
		throws SAXException {

	}

	/** Characters. */
	public void characters(char[] ch, int offset, int length)
		throws SAXException {
		if(dans_erreur)
		{
			String s=new String(ch,offset,length),tab[];
			FichierVerifInterm f;
			int i;
			f=resultat[resultat.length-1];
			if(f.MsgErreur==null)
			{
				tab=new String[1];
				tab[0]=s;
				f.MsgErreur=tab;
			}
			else
			{
				tab=new String[f.MsgErreur.length+1];
				for(i=0;i<f.MsgErreur.length;i++)
				{
					tab[i]=f.MsgErreur[i];
				}
				tab[i]=s;
				f.MsgErreur=tab;
			}
			//f.MsgErreur=s;
		}
	}

}