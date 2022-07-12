/*
 * Created on 28 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.extern;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtParseXML {

	public ExtParseXML(String nom) {
		assert(nom!=null);
		this.nom=nom;
		parse_XML(nom);
	}
	
	public void parse_XML(String nom)
	{
		ExtModule res=null;
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
			res=creer_module(catalogue);
			assert(res!=null);
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
			System.err.println("Exception Sax:"+e+"("+nom+")");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// i/o error
			e.printStackTrace();
			System.err.println("Erreur d'entree sortie");
			System.exit(1);
		}
		module=res;
	}

	public ExtModule creer_module(Element element)
	{
		int i;
		ExtModule res;
		ExtRoutine r;
		ExtClasse cl;
		Vector v;
		String nom=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("module"));
		NodeList fils = element.getChildNodes();
		NamedNodeMap attributs = element.getAttributes();

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = nomAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		res=new ExtModule(nom);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "routine") {
					//System.out.println("heritage");
					r = creer_routine((Element) n);
					res.ajoute_routine(r);
				}
				else if (n.getNodeName() == "classe") {
					//System.out.println("heritage");
					cl = creer_classe((Element) n);
					res.ajoute_classe(cl);
				}
				else
					assert(false);
			}
			else if (n instanceof Comment) {
				
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			}
			else
				assert(false);
		}
		return res;
	}


	public ExtRoutine creer_routine(Element element)
	{
		int i;
		ExtRoutine res;
		Vector v;
		String nom=null;
		ExtType t,liste_type[];
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("routine"));
		NodeList fils = element.getChildNodes();
		NamedNodeMap attributs = element.getAttributes();
		boolean fonction=false;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = nomAttribut;
			if (nomAttribut == "fonction")
				fonction=nomAttribut.equalsIgnoreCase("oui");
			else
				assert(false);
		}
		assert(nom!=null);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					t = creer_type((Element) n);
					v.addElement(t);
				}
				else
					assert(false);
			}
			else if (n instanceof Comment) {
				
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			}
			else
				assert(false);
		}
		if(fonction)
		{
			assert(v.size()>0);
			i=v.size()-1;
			t=(ExtType)v.elementAt(i);
			v.removeElementAt(i);
		}
		else
		{
			t=null;
		}
		if(v.size()>0)
		{
			liste_type=new ExtType[v.size()];
			v.copyInto(liste_type);
		}
		else
		{
			liste_type=null;
		}
		res=new ExtRoutine(nom,liste_type,t);
		return res;
	}

	public ExtType creer_type(Element element)
	{
		int no,i,len,j;
		ExtType res;
		Vector v;
		String nom=null;
		ExtType t,liste_type[];
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("type"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		//String nom="",fichier=null;//'"('"é('"-('è-"(')
		boolean fonction=false;
		no=0;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = nomAttribut;
			if (nomAttribut == "fonction")
				fonction=nomAttribut.equalsIgnoreCase("oui");
			else
				assert(false);
		}
		assert(nom!=null);
		assert(fils.getLength()==0);
		res=new ExtType(nom);
		return res;
	}

	public ExtClasse creer_classe(Element element)
	{
		int no,i,len,j;
		ExtClasse res;
		Vector v;
		String nom=null;
		//boolean heritage[][],heritage_directe[][];
		ExtType t,liste_type[];
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("classe"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		//String nom="",fichier=null;//'"('"é('"-('è-"(')
		boolean fonction=false;
		ExtAttribut liste_attribut[], a;
		no=0;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = nomAttribut;
			if (nomAttribut == "fonction")
				fonction=nomAttribut.equalsIgnoreCase("oui");
			else
				assert(false);
		}
		assert(nom!=null);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "attribut") {
					//System.out.println("heritage");
					a = creer_attribut((Element) n);
					v.addElement(a);
				}
				else
					assert(false);
			}
			else if (n instanceof Comment) {
				
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			}
			else
				assert(false);
		}
		if(v.size()>0)
		{
			liste_attribut=new ExtAttribut[v.size()];
			v.copyInto(liste_attribut);
		}
		else
		{
			liste_attribut=null;
		}
		res=new ExtClasse(nom,liste_attribut);
		return res;
	}

	public ExtAttribut creer_attribut(Element element)
	{
		int i;
		ExtAttribut res;
		Vector v;
		String nom=null;
		ExtType t,liste_type[];
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("attribut"));
		NodeList fils = element.getChildNodes();
		NamedNodeMap attributs = element.getAttributes();
		boolean fonction=false;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = nomAttribut;
			if (nomAttribut == "fonction")
				fonction=nomAttribut.equalsIgnoreCase("oui");
			else
				assert(false);
		}
		assert(nom!=null);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					t = creer_type((Element) n);
					v.addElement(t);
				}
				else
					assert(false);
			}
			else if (n instanceof Comment) {
				
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			}
			else
				assert(false);
		}
		if(fonction)
		{
			//int i;
			assert(v.size()>0);
			i=v.size()-1;
			t=(ExtType)v.elementAt(i);
			v.removeElementAt(i);
		}
		else
		{
			t=null;
		}
		if(v.size()>0)
		{
			liste_type=new ExtType[v.size()];
			v.copyInto(liste_type);
		}
		else
		{
			liste_type=null;
		}
		res=new ExtAttribut(nom,liste_type,t);
		return res;
	}

	
	String nom;
	public ExtModule module;
	
}
