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

//import javax.xml.parsers.SAXParser;
import org.xml.sax.ErrorHandler;
//import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserXML {

	public static void main(String args[]) {
		ParserXML p = new ParserXML();
		Fichier_test[] res = p.parse("test_unitaire\\test.xml");
		if (res == null) {
			System.out.println("Rien de trouve");
		} else {
			System.out.println("Réussi");
		}
	}

	public Fichier_test[] parse(String xmlFile) {
		//String xmlFile = "file:///xerces-2_5_0/data/personal.xml"; 
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			DefaultHandler handler = new TDefaultHandler();

			ErrorHandler error_handler = new TErrorHandler();

			parser.getXMLReader().setErrorHandler(error_handler);
			//parser.setFeature("http://xml.org/sax/features/validation",false);
			//parser.setFeature("http://xml.org/sax/features/namespaces",false);
			System.out.println("Parsing...");
			parser.parse(xmlFile, handler);
			System.out.println("Terminé");
			Fichier_test f[] = ((TDefaultHandler) handler).resultat;
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

class TErrorHandler implements ErrorHandler {
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

class TDefaultHandler extends DefaultHandler {
	private Locator locator;

	public Fichier_test[] resultat;

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/** Start document. */
	public void startDocument() throws SAXException {
		System.out.println("Debut du document");
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
		if (qname.equals("toto:root")) {
			//System.out.println("root");
		} else if (qname.equals("toto:file")) {
			int nb_erreur = -1, i;
			String nom = "";
			for (i = 0; i < attributes.getLength(); i++) {
				if (attributes.getQName(i).equals("nom")) {
					nom = attributes.getValue(i);
					assert(nom != "");
				} else if (attributes.getQName(i).equals("nb_erreur")) {
					nb_erreur = convertie(attributes.getValue(i));
					assert(nb_erreur >= 0);
					//if(nb_e)
				}
			}
			Fichier_test f, res[];

			/*System.out.println("pos("+locator.getLineNumber()+
				","+locator.getColumnNumber()+")="+nom+","+nb_erreur+","+
				attributes.getLength());*/
			//echo 
			assert(nb_erreur >= 0);
			assert(nom != "");
			f = new Fichier_test(nom, nb_erreur);
			if (resultat == null) {
				res = new Fichier_test[1];
			} else {
				res = new Fichier_test[resultat.length + 1];
				for (i = 0; i < resultat.length; i++)
					res[i] = resultat[i];
			}
			res[res.length - 1] = f;
			resultat = res;
			//System.out.println("Ajouter");
		} else {

		}
	}

	/** End element. */
	public void endElement(String uri, String localName, String qname)
		throws SAXException {

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

	}

}