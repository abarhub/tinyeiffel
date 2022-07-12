package tinyeiffel.test_unitaire;

/**
 * <p>Title: </p>
 * <p>Description: Les testes de eiffel</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import tinyeiffel.ast.*;

//import org.w3c.dom.DOMImplementationRegistry;
/*import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.DOMBuilder;*/

//import org.apache.xerces.parsers.DOMParser;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;

import java.util.*;

public class AstXML {
	public Classe parse(String nom_fichier) {
		String xmlFile = "test_unitaire\\test_class1.xml";
		if(nom_fichier!="")
			xmlFile=nom_fichier;
		//"file:///xerces-2_5_0/data/personal.xml";
		try {
			/* jasp */
			//Récupère une instance de la classe de fabrication
			DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			//Récupére une instance de la classe DocumentBuilder (spécifique vendeur)
			DocumentBuilder parser = factory.newDocumentBuilder();
			//effectue le parsing avec récupération du noeud DOM Document
			Document document = parser.parse(xmlFile);

			/*DOMParser parser = new DOMParser();
			parser.parse(xmlFile);
			Document document = parser.getDocument();*/

			/* Jaxt */
			//"org.apache.xerces.dom.CoreDocumentImpl";
			/*DocumentBuilderFactory factory =
			    DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			factory.
			Document document = builder.parse(xmlFile);*/

			/* DOM 3 */
			/*System.setProperty(DOMImplementation.PROPERTY,
			                   "org.apache.xerces.dom.DOMImplementationSourceImpl");
			DOMImplementationRegistry registry =
			    DOMImplementationRegistry.newInstance();
			
			DOMImplementationLS impl =
			    (DOMImplementationLS) registry.getDOMImplementation("LS-Load");
			
			DOMBuilder builder = impl.createDOMBuilder(
			    DOMImplementationLS.MODE_SYNCHRONOUS, null);
			
			Document document = builder.parseURI(xmlFile);*/

			Element catalogue = document.getDocumentElement();

			NodeList titres = catalogue.getElementsByTagName("class");
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
			//assert(org.w3c.dom.Node.ELEMENT_NODE==3);
			System.out.println(
				"nom=" + catalogue.getFirstChild().getNodeType());
			//System.out.println("nom2="+titres.item(0).getFirstChild().getNodeValue());
			/*for (int i=0; i<titres.getLength(); i++) {
			 System.out.println(titres.item(i).getFirstChild().getNodeValue());
			}*/
			titres = catalogue.getElementsByTagName("nom_classe");
			System.out.println(
				"Nombre d'element dans la racine : " + titres.getLength());
			Classe c = creer_classe(catalogue);
			System.out.println("classe=" + c.toString());
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
			System.err.println("Exception Sax");
			System.exit(1);
		} catch (IOException e) {
			// i/o error
			e.printStackTrace();
			System.err.println("Erreur d'entree sortie");
			System.exit(1);
		}
		return null;
	}

	public Classe creer_classe(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "class");
		NamedNodeMap attributs = element.getAttributes();
		String type="";
		Type nom_classe = null;
		Heritage h;
		Vector v = new Vector(), creation = new Vector();
		Vector invariant = null, v2 = new Vector();
		Chaine obsolete = null;

		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "type")
				type = valeurAttribut;
			else
				assert(false);
		}
		assert(type.equals("")||type.equals("expanded")||
				type.equals("deferred"));
		//System.out.println("methode 1:");
		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "heritage") {
					//System.out.println("heritage");
					h = creer_heritage((Element) n);
					v.addElement(h);
				} else if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom_classe = creer_nom_classe((Element) n);
				} else if (n.getNodeName() == "creation") {
					//System.out.println("creation");
					Creation c = creer_creation((Element) n);
					creation.addElement(c);
				} else if (n.getNodeName() == "obsolete") {
					//System.out.println("obsolete");
					obsolete = creer_obsolete((Element) n);
				} else if (n.getNodeName() == "invariant") {
					//System.out.println("invariant");
					invariant = creer_invariant((Element) n);
				} else if (n.getNodeName() == "feature") {
					//System.out.println("feature");
					Feature f = creer_feature((Element) n);
					v2.addElement(f);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}

		/*System.out.println("methode 2:");
		Node node=element.getFirstChild();
		while(node!=null)
		{
		  Node n=node;
		  System.out.println("element="+n.getNodeName());
		  node=node.getNextSibling();
		}*/
		assert(nom_classe != null);

		return new Classe(type.equals("deferred"),type.equals("expanded"),
			nom_classe,
			v2,
			(v.size()==0)?null:v,
			invariant,
			creation,
			obsolete,
			new Vector(),
			new Vector());
	}

	public Chaine creer_obsolete(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "obsolete");
		NamedNodeMap attributs = element.getAttributes();
		Chaine res = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "chaine") {
					//System.out.println("chaine");
					assert(res == null);
					res = creer_chaine((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Vector creer_invariant(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "invariant");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "assert") {
					//System.out.println("assert");
					Assert e = creer_assert((Element) n);
					res.addElement(e);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Vector creer_require(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "require");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "assert") {
					//System.out.println("assert");
					Assert e = creer_assert((Element) n);
					res.addElement(e);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}
	public Vector creer_ensure(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "ensure");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "assert") {
					//System.out.println("assert");
					Assert e = creer_assert((Element) n);
					res.addElement(e);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Assert creer_assert(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "assert");
		NamedNodeMap attributs = element.getAttributes();
		Assert res;
		String tag=null;

		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "tag")
				tag = valeurAttribut;
			else
				assert(false);
		}
		assert(tag==null||!tag.equals(""));

		NodeList fils = element.getChildNodes();
		int i;
		Expr expr = null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					expr = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if(tag==null)
			res = new Assert(expr);
		else
			res=new Assert(tag,expr);

		return res;
	}

	public Feature creer_feature(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "feature");
		NamedNodeMap attributs = element.getAttributes();
		Feature res = null;
		Vector liste_export = null, liste_nom = new Vector();
		Type nom_classe = null;
		Vector param = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "liste_export") {
					//System.out.println("liste_export");
					liste_export = creer_liste_export((Element) n);
				} else if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					NomFeature f = creer_nom_feature((Element) n);
					liste_nom.addElement(f);
				} else if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom_classe = creer_nom_classe((Element) n);
				} // creer_parametre_formel
				else if (n.getNodeName() == "corps") {
					//System.out.println("corps");
					res = creer_corps((Element) n);
				} else if (n.getNodeName() == "parametre_formel") {
					//System.out.println("parametre_formel");
					DeclareVar d = creer_parametre_formel((Element) n);
					if(param==null)
						param=new Vector();
					param.addElement(d);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if (res == null) {
			res = new FeatureAttr();
			res.set_nom_param(liste_nom, param, liste_export,new Vector());
			res.type_retour = nom_classe;
		} else {
			res.set_nom_param(liste_nom, param, liste_export,new Vector());
			res.type_retour = nom_classe;
		}

		return res;
	}

	public Vector creer_liste_export(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "liste_export");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();
		Type nom;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom = creer_nom_classe((Element) n);
					res.addElement(nom);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public DeclareVar creer_parametre_formel(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "parametre_formel");
		DeclareVar res = null;

		int i;
		NamedNodeMap attributs = element.getAttributes();
		String nom = null;
		Type nom_classe = null;
		boolean frozen = false;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength() == 0);
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					assert(nom_classe == null);
					nom_classe = creer_nom_classe((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(nom != null);
		assert(nom_classe != null);
		res = new DeclareVar(nom, nom_classe);
		assert(res != null);

		return res;
	}

	public DeclareVar creer_local(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "local");
		DeclareVar res = null;

		int i;
		NamedNodeMap attributs = element.getAttributes();
		String nom = null;
		Type nom_classe = null;
		boolean frozen = false;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength() == 0);
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					assert(nom_classe == null);
					nom_classe = creer_nom_classe((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(nom != null);
		assert(nom_classe != null);
		res = new DeclareVar(nom, nom_classe);
		assert(res != null);

		return res;
	}

	public Feature creer_corps(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "corps");
		NamedNodeMap attributs = element.getAttributes();
		Feature res = null;
		Vector require = new Vector(),
			ensure = new Vector(),
			rescue = new Vector();
		Chaine obsolete = null;
		Expr e = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "require") {
					//System.out.println("require");
					assert(res == null);
					require = creer_require((Element) n);
				} else if (n.getNodeName() == "ensure") {
					//System.out.println("ensure");
					ensure = creer_ensure((Element) n);
				} else if (n.getNodeName() == "rescue") {
					//System.out.println("rescue");
					rescue = creer_rescue((Element) n);
				} else if (n.getNodeName() == "corps2") {
					//System.out.println("corps2");
					assert(res == null);
					res = creer_corps2((Element) n);
					assert(res!=null);
				} else if (n.getNodeName() == "obsolete") {
					//System.out.println("obsolete");
					assert(res == null);
					obsolete = creer_obsolete((Element) n);
				} else if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(res == null);
					e = creer_expression((Element) n);
					assert(e!=null);
				} else if (n.getNodeName() == "unique") {
					//System.out.println("unique");
					assert(res == null);
				} else {
					assert(false):
						"element inconnue:"+n.getNodeName();
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if (res != null)
			res.set_require_ensure(require, ensure, rescue, obsolete, new Vector(),new Vector());
		else if (e != null) {
			res = new FeatureExpr();
			((FeatureExpr) res).expr = e;
		} else {
			res = new FeatureUnique();
		}

		return res;
	}

	public Vector creer_rescue(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "rescue");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr ins = creer_instruction((Element) n);
					res.addElement(ins);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Feature creer_corps2(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "corps2");
		NamedNodeMap attributs = element.getAttributes();
		Feature res = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "code") {
					//System.out.println("code");
					assert(res == null);
					res = creer_code((Element) n);
				} else if (n.getNodeName() == "external") {
					//System.out.println("external");
					assert(res == null);
					res = creer_external((Element) n);
				} else if (n.getNodeName() == "deferred") {
					//System.out.println("deferred");
					assert(res == null);
					res = new FeatureDeferred();
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public FeatureExternal creer_external(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "external");
		NamedNodeMap attributs = element.getAttributes();
		FeatureExternal res = null;
		Chaine ch1 = null, ch2 = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "chaine") {
					//System.out.println("code");
					if (ch1 == null)
						ch1 = creer_chaine((Element) n);
					else
						ch2 = creer_chaine((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new FeatureExternal(ch1, ch2);

		return res;
	}

	public FeatureRoutine creer_code(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "code");
		FeatureRoutine res = null;
		Vector local = new Vector(), instr = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "local") {
					//System.out.println("local");
					DeclareVar l = creer_local((Element) n);
					local.addElement(l);
				} else if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr l = creer_instruction((Element) n);
					instr.addElement(l);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		NamedNodeMap attributs = element.getAttributes();
		boolean once = false;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "once")
				once = true;
			else
				assert(false);
		}

		res = new FeatureRoutine(once, instr, local);

		return res;
	}

	public Instr creer_instruction(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "instruction");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "if") {
					//System.out.println("if");
					assert(res == null);
					res = creer_if((Element) n);
				} else if (n.getNodeName() == "inspect") {
					//System.out.println("inspect");
					assert(res == null);
					res = creer_inspect((Element) n);
				} else if (n.getNodeName() == "debug") {
					//System.out.println("debug");
					assert(res == null);
					res = creer_debug((Element) n);
				} else if (n.getNodeName() == "creation2") {
					//System.out.println("creation2");
					assert(res == null);
					res = creer_creation2((Element) n);
				} else if (n.getNodeName() == "affect") {
					//System.out.println("affect");
					assert(res == null);
					res = creer_affect((Element) n);
				} else if (n.getNodeName() == "tentative_affect") {
					//System.out.println("tentative_affect");
					assert(res == null);
					res = creer_tentative_affect((Element) n);
				} else if (n.getNodeName() == "loop") {
					//System.out.println("loop");
					assert(res == null);
					res = creer_loop((Element) n);
				} else if (n.getNodeName() == "check") {
					//System.out.println("check");
					assert(res == null);
					res = creer_check((Element) n);
				} else if (n.getNodeName() == "appel") {
					//System.out.println("appel");
					assert(res == null);
					res = creer_appel2((Element) n);
				} else if (n.getNodeName() == "retry") {
					//System.out.println("check");
					assert(res == null);
					res = creer_retry((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Instr_If creer_if(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "if");
		NamedNodeMap attributs = element.getAttributes();
		Instr_If res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Expr e = null;
		Vector v = new Vector();
		Instr_ElseIf elseif_d = null, elseif_f = null;
		Instr_Else else_d = null, else_f = null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					e = creer_expression((Element) n);
				} else if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr ins = creer_instruction((Element) n);
					v.addElement(ins);
				} else if (n.getNodeName() == "elseif") {
					//System.out.println("elseif");
					assert(res == null);
					Instr_ElseIf elseif = creer_elseif((Element) n);
					if (elseif_d == null)
						elseif_d = elseif_f = elseif;
					else {
						elseif_f.setSuivant(elseif);
						elseif_f = elseif;
					}
				} else if (n.getNodeName() == "else") {
					//System.out.println("else");
					Instr_Else e3 = creer_else((Element) n);
					assert(else_d==null);
					if (else_d == null)
						else_d = else_f = e3;
					if(elseif_f!=null)
						elseif_f.setSuivant(else_d);
					/*else {
						//else_f.suivant = e3;
						else_f = e3;
					}*/
				} else {
					assert(false):"type="+n.getNodeName();
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new Instr_If(e, v);
		if(elseif_d!=null)
			res.setSuivant(elseif_d);
		else if (else_d != null)
			res.setSuivant(else_d);

		return res;
	}

	public Instr_ElseIf creer_elseif(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "elseif");
		NamedNodeMap attributs = element.getAttributes();
		Instr_ElseIf res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Expr e = null;
		Vector v = new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					e = creer_expression((Element) n);
				} else if (n.getNodeName() == "instruction") {
					System.out.println("instruction");
					Instr ins = creer_instruction((Element) n);
					v.addElement(ins);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new Instr_ElseIf(e, v);

		return res;
	}

	public Instr_Else creer_else(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "else");
		NamedNodeMap attributs = element.getAttributes();
		Instr_Else res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Vector v = new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr ins = creer_instruction((Element) n);
					v.addElement(ins);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new Instr_Else(v);

		return res;
	}

	public Instr_Inspect creer_inspect(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "inspect");
		NamedNodeMap attributs = element.getAttributes();
		Instr_Inspect res = null;
		Instr_Inspect last=null;

		NodeList fils = element.getChildNodes();
		int i;
		Expr expr=null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(expr==null);
					expr = creer_expression((Element) n);
				} else if (n.getNodeName() == "when_then") {
					//System.out.println("when_then");
					assert(expr!=null);
					Vector v = creer_when_then((Element) n);
					assert(v!=null);
					assert(v.elementAt(0) instanceof Vector);
					assert(v.elementAt(1) instanceof Vector);
					Vector ins=(Vector)v.elementAt(1);
					Vector when=(Vector)v.elementAt(0);
					Instr_Inspect instr=new Instr_Inspect(expr,when,ins);
					if(res==null)
						res=last=instr;
					else
					{
						last.setSuivant(instr);
						last=instr;
					}
				} else {
					assert(false):"type inconnue:"+n.getNodeName();
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Vector creer_when_then(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "when_then");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Vector when=new Vector();
		Vector instr=new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_when") {
					//System.out.println("expression_when");
					Vector v = creer_expression_when((Element) n);
					when.addElement(v);
				} else if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr ins = creer_instruction((Element) n);
					assert(ins!=null);
					instr.addElement(ins);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res=new Vector();
		res.addElement(when);
		res.addElement(instr);

		return res;
	}

	public Vector creer_expression_when(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "expression_when");
		NamedNodeMap attributs = element.getAttributes();
		Vector res = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					Expr e = creer_expression((Element) n);
					res.addElement(e);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res.size()>0&&res.size()<3);

		return res;
	}
	
	public Instr creer_debug(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "debug");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Vector v = new Vector(), v2 = new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "chaine") {
					//System.out.println("chaine");
					assert(res == null);
					Chaine c = creer_chaine((Element) n);
					v.addElement(c);
				} else if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					assert(res == null);
					Instr c = creer_instruction((Element) n);
					v2.addElement(c);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new Instr_Debug(v, v2);

		return res;
	}

	public Instr creer_creation2(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "creation2");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;
		
		String nom_init = null;
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom_init=valeurAttribut;
			else
				assert(false);
		}
		
		NodeList fils = element.getChildNodes();
		int i;
		Type nom_classe = null;
		Expr_Appel var  = null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom_classe = creer_nom_classe((Element) n);
				} else if (n.getNodeName() == "appel") {
					//System.out.println("appel");
						var = creer_appel((Element) n);
				} else {
					assert(false):"type incorrecte:"+n.getNodeName();
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		String nom,nom2=null;
		Vector param=new Vector();
		nom=var.nom;
		if(var.parametre!=null)
			param=var.parametre;
		if(nom_init!=null)
		{
			nom2=nom;
			nom=nom_init;
		}
		res=new Instr_Creation(nom_classe,nom,nom2,param);

		return res;
	}

	public Instr creer_affect(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "affect");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		String nom_current = null;
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "current")
				nom_current=valeurAttribut;
			else
				assert(false);
		}
		
		NodeList fils = element.getChildNodes();
		int i;
		Expr expr=null;
		String nom=null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "variable") {
					//System.out.println("variable");
					assert(nom == null);
					Expr_Var v=creer_variable((Element) n);
					nom=v.nom;
				} 
				else if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(expr==null);
					expr = creer_expression((Element) n);
				}
				else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res=new Instr_Affect(nom,expr);
		((Instr_Affect)res).var_current=nom_current;

		return res;
	}

	public Instr creer_tentative_affect(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "tentative_affect");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		String nom_current = null;
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "current")
				nom_current=valeurAttribut;
			else
				assert(false);
		}
		
		NodeList fils = element.getChildNodes();
		int i;
		Expr expr=null;
		String nom=null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "variable") {
					//System.out.println("variable");
					assert(nom == null);
					Expr_Var v=creer_variable((Element) n);
					nom=v.nom;
				} 
				else if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(expr==null);
					expr = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res=new Instr_TentAffect(nom,expr);
		((Instr_TentAffect)res).var_current=nom_current;

		return res;
	}

	public Instr creer_loop(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "loop");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Vector instr1=new Vector(),instr2=new Vector();
		Vector invariant=null;
		Expr expr=null,expr2=null;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "instruction") {
					//System.out.println("instruction");
					Instr ins= creer_instruction((Element) n);
					if(expr==null)
						instr1.addElement(ins);
					else
						instr2.addElement(ins);
				} else if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					expr= creer_expression((Element) n);
				} else if (n.getNodeName() == "invariant") {
					//System.out.println("invariant");
					invariant= creer_invariant((Element) n);
				} else if (n.getNodeName() == "variant") {
					//System.out.println("variant");
					expr2= creer_variant((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(expr != null);
		res=new Instr_Loop(expr,instr1,instr2,
				(invariant==null)?new Vector():invariant,expr2);

		return res;
	}

	public Expr creer_variant(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "variant");
		NamedNodeMap attributs = element.getAttributes();
		Expr res = null;

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					res= creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);

		return res;
	}

	public Instr creer_check(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "check");
		NamedNodeMap attributs = element.getAttributes();
		Instr res = null;

		NodeList fils = element.getChildNodes();
		int i;
		Vector v = new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "assert") {
					//System.out.println("assert");
					Assert a = creer_assert((Element) n);
					v.addElement(a);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		res = new Instr_Check(v);

		return res;
	}

	public Instr creer_appel2(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "appel2"
				||element.getNodeName() == "appel");
		Instr_Appel res = null,last=null;

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		String nom = null;
		Instr appel=null;
		Expr expr=null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		//assert(nom != null);

		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "parametre_reel") {
					//System.out.println("parametre_reel");
					Expr nom1 = creer_parametre_reel((Element) n);
					v1.addElement(nom1);
				} else if (n.getNodeName() == "appel") {
					//System.out.println("appel");
					appel = creer_appel2((Element) n);
				} else if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					expr = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if(expr!=null)
			res=new Instr_Appel(expr);
		else
			res=new Instr_Appel(nom, v1);
		assert(res != null);
		if(appel!=null)
			res.setSuivant(appel);

		return res;
	}

	public Heritage creer_heritage(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "heritage");
		NamedNodeMap attributs = element.getAttributes();
		Type nom = null;
		Vector rename = new Vector(),
			export = new Vector(),
			undefine = new Vector(),
			redefine = new Vector(),
			select = new Vector();

		NodeList fils = element.getChildNodes();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom = creer_nom_classe((Element) n);
				} else if (n.getNodeName() == "rename") {
					Rename r;
					//System.out.println("nom_classe");
					r = creer_rename((Element) n);
					rename.addElement(r);
				} else if (n.getNodeName() == "export") {
					Export r;
					//System.out.println("nom_classe");
					r = creer_export((Element) n);
					export.addElement(r);
				} else if (n.getNodeName() == "undefine") {
					//Export r;
					//System.out.println("nom_classe");
					undefine = creer_undefine((Element) n);
					//undefine.addElement(r);
				} else if (n.getNodeName() == "redefine") {
					//Export r;
					//System.out.println("nom_classe");
					redefine = creer_redefine((Element) n);
					//undefine.addElement(r);
				} else if (n.getNodeName() == "select") {
					//Export r;
					//System.out.println("nom_classe");
					select = creer_select((Element) n);
					//undefine.addElement(r);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}

		return new Heritage(nom, rename, export, undefine, redefine, select);
	}

	public Rename creer_rename(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "rename");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null, nom2 = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					if (nom1 == null)
						nom1 = creer_nom_feature((Element) n);
					else
						nom2 = creer_nom_feature((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return new Rename(nom1, nom2);
	}

	public Export creer_export(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "export");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null;
		Type nom2 = null;
		Vector v1 = new Vector(), v2 = new Vector();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					nom1 = creer_nom_feature((Element) n);
					v1.addElement(nom1);
				} else if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					nom2 = creer_nom_classe((Element) n);
					v2.addElement(nom2);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if(v2!=null&&v2.size()>0)
			assert(v2.elementAt(0) instanceof Type);
		return new Export(v1, v2);
	}

	public Vector creer_undefine(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "undefine");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null;
		Vector v = new Vector();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					nom1 = creer_nom_feature((Element) n);
					v.addElement(nom1);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return v;
	}

	public Vector creer_redefine(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "redefine");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null;
		Vector v = new Vector();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					nom1 = creer_nom_feature((Element) n);
					v.addElement(nom1);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return v;
	}

	public Vector creer_select(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "select");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null;
		Vector v = new Vector();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					nom1 = creer_nom_feature((Element) n);
					v.addElement(nom1);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return v;
	}

	public Creation creer_creation(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "creation");

		Creation res=null;
		NodeList fils = element.getChildNodes();
		NamedNodeMap attributs = element.getAttributes();
		boolean export_none = false;
		//NomFeature nom1 = null, nom2 = null;
		Vector v1 = new Vector(), v2 = new Vector();
		int i;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equalsIgnoreCase("export_none"))
				export_none=valeurAttribut.equalsIgnoreCase("true");
			else
				assert(false);
		}
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_feature") {
					//System.out.println("nom_feature");
					NomFeature nom1 = creer_nom_feature((Element) n);
					v1.addElement(nom1);
				} else if (n.getNodeName() == "nom_classe") {
					//System.out.println("nom_classe");
					Type nom2 = creer_nom_classe((Element) n);
					v2.addElement(nom2);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		if(export_none)
		{
			assert(v2.size()==0);
			res=new Creation(v2, v1);
		}
		else if(v2.size()==0)
		{
			res=new Creation(null, v1);
		}
		else
		{
			res=new Creation(v2, v1);
		}
		assert(res!=null);
		return res;
	}

	public Expr creer_expression(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "expression");

		NodeList fils = element.getChildNodes();
		//NomFeature nom1 = null, nom2 = null;
		Vector v1 = new Vector(), v2 = new Vector();
		Expr res = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "entier") {
					//System.out.println("entier");
					res = creer_entier((Element) n);
				} else if (n.getNodeName() == "variable") {
					//System.out.println("variable");
					res = creer_variable((Element) n);
				} else if (n.getNodeName() == "reel") {
					//System.out.println("reel");
					res = creer_reel((Element) n);
				} else if (n.getNodeName() == "chaine") {
					//System.out.println("chaine");
					res = new Expr_Chaine(creer_chaine((Element) n));
				} else if (n.getNodeName() == "appel") {
					//System.out.println("appel");
					res = creer_appel((Element) n);
				} else if (n.getNodeName() == "tableau") {
					//System.out.println("tableau");
					res = creer_tableau((Element) n);
				} else if (n.getNodeName() == "expression_binaire") {
					//System.out.println("expression_binaire");
					res = creer_expression_binaire((Element) n);
				} else if (n.getNodeName() == "expression_unaire") {
					//System.out.println("expression_unaire");
					res = creer_expression_unaire((Element) n);
				} else if (n.getNodeName() == "car") {
					//System.out.println("car");
					res = creer_car((Element) n);
				} else if (n.getNodeName() == "vrai") {
					//System.out.println("vrai");
					res = creer_vrai((Element) n);
				} else if (n.getNodeName() == "faux") {
					//System.out.println("faux");
					res = creer_faux((Element) n);
				} else {
					assert(false):"expr op="+n.getNodeName();
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return res;
	}

	public Expr_Var creer_variable(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "variable");

		NamedNodeMap attributs = element.getAttributes();
		String nom = null;
		boolean frozen = false;
		for (int i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		assert(nom != null);

		/*NodeList fils = element.getChildNodes();
		//NomFeature nom1 = null, nom2 = null;
		Vector v1=new Vector(),v2=new Vector();
		int i;
		for (i = 0; i < fils.getLength(); i++) {
		  Node n = fils.item(i);
		  System.out.println("element=" + n.getNodeName());
		  if (n instanceof Element) {
		    if (n.getNodeName() == "nom_feature") {
		      System.out.println("nom_feature");
		      NomFeature nom1 = creer_nom_feature( (Element) n);
		      v1.addElement(nom1);
		    }
		    else if (n.getNodeName() == "nom_classe") {
		      System.out.println("nom_classe");
		      Type nom2 = creer_nom_classe( (Element) n);
		      v2.addElement(nom2);
		    }
		    else {
		      assert (false);
		    }
		  }
		  else if (n instanceof Text) {
		    String s = n.getNodeValue();
		    assert (s == "" || s.matches("[ \t\n]+"));
		  }
		  else {
		    assert (false);
		  }
		}*/
		return new Expr_Var(nom, new Token(-1,-1,"",""));
	}

	public Expr_Appel creer_appel(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "appel");

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		String nom = null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		assert(nom != null);

		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "parametre_reel") {
					//System.out.println("parametre_reel");
					Expr nom1 = creer_parametre_reel((Element) n);
					v1.addElement(nom1);
				} else if (n.getNodeName() == "appel") {
					//System.out.println("appel");
					Expr appel = creer_appel((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return new Expr_Appel(nom, v1, null);
	}

	public Expr_Tableau creer_tableau(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "tableau");

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		assert(attributs.getLength()==0);

		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					Expr expr = creer_expression((Element) n);
					v1.addElement(expr);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		return new Expr_Tableau(v1, new Token(-1,-1,"",""));
	}

	public Expr creer_parametre_reel(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "parametre_reel");

		NodeList fils = element.getChildNodes();
		Expr res = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(res == null);
					res = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(res != null);
		return res;
	}

	public Expr creer_car(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "car");

		NodeList fils = element.getChildNodes();
		Expr res = null;
		String text=null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Text) {
				assert(text==null);
				String s = n.getNodeValue();
				assert(s != "");
				text=s;
			} else {
				assert(false);
			}
		}
		assert(text!=null);
		res=new Expr_Car(text,new Token(-1,-1,"",""));
		return res;
	}

	public Instr creer_retry(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "retry");

		NodeList fils = element.getChildNodes();
		Instr res = null;
		String text=null;
		int i;
		/*for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Text) {
				assert(text==null);
				String s = n.getNodeValue();
				assert(s != "");
				text=s;
			} else {
				assert(false);
			}
		}
		assert(text!=null);
		res=new Expr_Car(text,new Token(-1,-1,"",""));*/
		res=new Instr_Retry();
		return res;
	}
	
	public Expr_Vrai creer_vrai(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "vrai");

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		assert(attributs.getLength()==0);
		assert(fils.getLength() == 0);

		return new Expr_Vrai(new Token(-1,-1,"",""));
	}
	
	public Expr_Faux creer_faux(Element element) {
		assert(element != null);
		assert(element.getNodeName().equals("faux"));

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		assert(attributs.getLength()==0);
		assert(fils.getLength() == 0);

		return new Expr_Faux(new Token(-1,-1,"",""));
	}

	public Expr_Entier creer_entier(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "entier");

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		String valeur = null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "valeur")
				valeur = valeurAttribut;
			else
				assert(false);
		}
		assert(valeur != null);
		assert(fils.getLength() == 0);

		return new Expr_Entier(valeur, null);
	}

	public Expr_Reel creer_reel(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "reel");

		NodeList fils = element.getChildNodes();
		Vector v1 = new Vector();
		int i;
		NamedNodeMap attributs = element.getAttributes();
		//assert(attributs.getLength()==0);
		//boolean frozen = false;
		String valeur = null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "valeur")
				valeur = valeurAttribut;
			else
				assert(false);
		}
		assert(valeur != null);
		assert(fils.getLength() == 0);
		/*assert(fils.getLength() == 1);
		Node n = fils.item(0);
		assert(n instanceof Text);
		String valeur = n.getNodeValue();*/

		return new Expr_Reel(valeur, null);
	}

	public Expr_Binaire creer_expression_binaire(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "expression_binaire");

		NodeList fils = element.getChildNodes();
		Expr exp1 = null, exp2 = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(exp1 == null || exp2 == null);
					if (exp1 == null)
						exp1 = creer_expression((Element) n);
					else
						exp2 = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(exp1 != null);
		assert(exp2 != null);
		NamedNodeMap attributs = element.getAttributes();
		String valeur = null,nom_free_op=null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "op")
				valeur = valeurAttribut;
			else if (nomAttribut == "nom")
				nom_free_op = valeurAttribut;
			else
				assert(false);
		}
		assert(valeur != null);

		Expr_Binaire res = null;
		if (valeur.equals("plus"))
			res = new Expr_Binaire(Expr.Plus, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("moins"))
			res = new Expr_Binaire(Expr.Moins, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("fois"))
			res = new Expr_Binaire(Expr.Fois, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("div"))
			res = new Expr_Binaire(Expr.Div, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("div_entier"))
			res = new Expr_Binaire(Expr.Div_entier, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("mod"))
			res = new Expr_Binaire(Expr.Mod, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("point"))
			res = new Expr_Binaire(Expr.Point, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("puiss"))
			res = new Expr_Binaire(Expr.Puiss, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("xor"))
			res = new Expr_Binaire(Expr.Xor, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("or"))
			res = new Expr_Binaire(Expr.Or, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("and"))
			res = new Expr_Binaire(Expr.And, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("and_then"))
			res = new Expr_Binaire(Expr.And_Then, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("or_else"))
			res = new Expr_Binaire(Expr.Or_Else, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("implies"))
			res = new Expr_Binaire(Expr.Implies, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("egal"))
			res = new Expr_Binaire(Expr.Egal, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("diff"))
			res = new Expr_Binaire(Expr.Diff, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("infs"))
			res = new Expr_Binaire(Expr.Infs, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("inf"))
			res = new Expr_Binaire(Expr.Inf, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("sup"))
			res = new Expr_Binaire(Expr.Sup, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("sups"))
			res = new Expr_Binaire(Expr.Sups, exp1, exp2, new Token(-1,-1,"",""));
		else if (valeur.equals("free_op"))
			res = new Expr_Binaire(nom_free_op, exp1, exp2, new Token(-1,-1,"",""));
		else
		{
			assert(valeur.equals("plus")):"valeur="+valeur+";";
			assert(false):"valeur="+valeur+";";
		}

		return res;
	}

	public Expr_Unaire creer_expression_unaire(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "expression_unaire");

		NodeList fils = element.getChildNodes();
		Expr exp1 = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("expression");
					assert(exp1 == null);
					exp1 = creer_expression((Element) n);
				} else {
					assert(false);
				}
			} else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}
		assert(exp1 != null);
		NamedNodeMap attributs = element.getAttributes();
		String valeur = null,nom_free_op=null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "op")
				valeur = valeurAttribut;
			else if (nomAttribut == "nom")
				nom_free_op = valeurAttribut;
			else
				assert(false);
		}
		assert(valeur != null);

		Expr_Unaire res = null;
		if (valeur.equals("plus"))
			res = new Expr_Unaire(Expr.PlusU, exp1, new Token(-1,-1,"",""));
		else if (valeur.equals("moins"))
			res = new Expr_Unaire(Expr.MoinsU, exp1, new Token(-1,-1,"",""));
		else if (valeur.equals("old"))
			res = new Expr_Unaire(Expr.Old, exp1, new Token(-1,-1,"",""));
		else if (valeur.equals("not"))
			res = new Expr_Unaire(Expr.Not, exp1, new Token(-1,-1,"",""));
		else if (valeur.equals("free_op"))
			res = new Expr_Unaire(nom_free_op, exp1, new Token(-1,-1,"",""));
		else if (valeur.equals("addr"))
			res = new Expr_Unaire(Expr.Dollard, exp1, new Token(-1,-1,"",""));
		else
			assert(false):"code inconnue:"+valeur;

		return res;
	}

	public Chaine creer_chaine(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "chaine");

		NodeList fils = element.getChildNodes();
		int i;

		Vector v = new Vector();
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if(n instanceof Element)
			{
				if (n.getNodeName() == "chaine1") {
					//System.out.println("chaine1");
					String s = creer_chaine1((Element) n);
					v.addElement(s);
				} else {
					assert(false);
				}
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				assert(false);
			}
		}

		return new Chaine(v, new Token(-1,-1,"",""));
	}

	public String creer_chaine1(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "chaine1");

		NodeList fils = element.getChildNodes();
		int i;
		String text = null;

		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Text) {
				assert(text==null);
				text = n.getNodeValue();
			} else {
				assert(false);
			}
		}
		assert(text != null);

		return text;
	}

	public NomFeature creer_nom_feature(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "nom_feature");

		NodeList fils = element.getChildNodes();
		NomFeature nom1 = null, nom2 = null;
		assert(fils.getLength() == 0);
		int i;
		String nom = null, type = null;

		NamedNodeMap attributs = element.getAttributes();
		boolean frozen = false;
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "type")
				type = valeurAttribut;
			else if (nomAttribut == "frozen")
				frozen = true;
			else if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		assert(nom != null);

		NomFeature nomf = null;
		Vector v;
		if (type == null)
			nomf = new NomFeature(nom);
		else if (type.equals("infix")) {
			v = new Vector();
			v.addElement("\""+nom+"\"");
			nomf = new NomFeature(new Chaine(v, new Token(-1,-1,"","")));
			nomf.infix = true;
		} else if (type.equals("prefix")) {
			v = new Vector();
			v.addElement("\""+nom+"\"");
			nomf = new NomFeature(new Chaine(v, new Token(-1,-1,"","")));
			nomf.prefix = true;
		} else
			assert(false):"type="+type;

		nomf.frozen = frozen;

		return nomf;
	}

	public Type creer_nom_classe(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "nom_classe");
		NamedNodeMap attributs = element.getAttributes();
		String nom = "";
		Type res = null;
		boolean expanded=true;

		//assert(attributs.getLength() <= 2);
		for(int i=0;i<attributs.getLength();i++)
		{
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else if(nomAttribut=="expanded")
				expanded=true;
			else
				assert(false);
		}
		
		//assert(nom!="");
		NodeList fils = element.getChildNodes();
		if (fils.getLength() == 0) {
			assert(!nom.equals(""));
			res = new TypeSimple(expanded,nom, new Vector());
		} else if (nom==""/*&& fils.item(0).getNodeName() == "like"*/) {
			int i;
			Expr exp=null;
			for (i = 0; i < fils.getLength(); i++) {
				Node n = fils.item(i);
				//System.out.println("element=" + n.getNodeName());
				if (n instanceof Element) {
					if (n.getNodeName() == "like") {
						//System.out.println("like");
						NodeList fils2 = n.getChildNodes();
						int j;
						for (j = 0; j < fils2.getLength(); j++) {
							Node n2=fils2.item(j);
							if(n2 instanceof Element)
							{
								if(n2.getNodeName()=="expression")
								{
									assert(exp==null);
									exp = creer_expression((Element) n2);
								}
								else
									assert(false);
							}
							else if(n2 instanceof Text) {
								String s = n2.getNodeValue();
								assert(s == "" || s.matches("[ \t\n]+"));
							} else {
								assert(false);
							}
						}
					} else if (n instanceof Text) {
						String s = n.getNodeValue();
						assert(s == "" || s.matches("[ \t\n]+"));
					} else {
						assert(false);
					}
				}
			}
			//Element elt = (Element) fils.item(0).getFirstChild();
			//Expr e = creer_expression(elt);
			assert(exp!=null);
			res = new TypeAncre(exp);
		} else {
			int i;
			assert(!nom.equals(""));
			for (i = 0; i < fils.getLength(); i++) {
				Node n = fils.item(i);
				//System.out.println("element=" + n.getNodeName());
				if (n instanceof Element) {
					if (n.getNodeName() == "nom_classe") {
						//System.out.println("nom_classe");
						//nom = creer_nom_classe( (Element) n);
						Vector v = new Vector();
						NodeList fils2 = n.getChildNodes();
						int j;
						for (j = 0; j < fils2.getLength(); j++) {
							Node n2=fils2.item(j);
							if(n2 instanceof Element)
							{
								if(n2.getNodeName()=="nom_classe")
								{
									Type t = creer_nom_classe((Element) n2);
									v.addElement(t);
								}
								else
									assert(false);
							}
							else if(n2 instanceof Text) {
								String s = n2.getNodeValue();
								assert(s == "" || s.matches("[ \t\n]+"));
							} else {
								assert(false);
							}
						}
						res = new TypeSimple(expanded,nom, v);
					} else {
						assert(false);
					}
				} else if (n instanceof Text) {
					String s = n.getNodeValue();
					assert(s == "" || s.matches("[ \t\n]+"));
				} else {
					assert(false);
				}
			}
			if(res==null)
				res=new TypeSimple(expanded,nom,new Vector());
		}
		//res=new Type(nom,v);
		assert(res != null);

		return res;
	}

	/*public Expr creer_expression(Element element)
	{
	  return null;
	}*/
}