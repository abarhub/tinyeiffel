package tinyeiffel.outils.compare_xml;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
//import org.apache.xerces.dom.*;

import org.w3c.dom.*;
//import ast.*;

//import org.w3c.dom.DOMImplementationRegistry;
/*import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.DOMBuilder;*/

//import org.apache.xerces.parsers.DOMParser;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
//import org.w3c.dom.*;

//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;

import java.util.*;


public class Compare_XML 
{
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
		Compare_XML c;
		String nom1="test_unitaire\\test_class1.xml";
		String nom2="test\\test0.xml";
		c=new Compare_XML(nom1,nom2);
		if(c.parse())
		{
			System.out.println(nom1+"="+nom2);
		}
		else
		{
			System.out.println(nom1+"!="+nom2);
		}
		System.out.println("Fin");
	}

	public String erreur()
	{
		return msgErreur;
	}

	private String msgErreur;

	public boolean parse()
	{
		//String xmlFile = "test_unitaire\\test_class1.xml";
		//if(nom_fichier!="")
		//	xmlFile=nom_fichier;
		//"file:///xerces-2_5_0/data/personal.xml";
		try {
			/* jasp */
			//R?cup?re une instance de la classe de fabrication
			DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
			//R?cup?re une instance de la classe DocumentBuilder (sp?cifique vendeur)
			DocumentBuilder parser = factory.newDocumentBuilder();
			//effectue le parsing avec r?cup?ration du noeud DOM Document
			Document document = parser.parse(nom1);
			Document document2 = parser.parse(nom2);

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

			Element catalogue1=document.getDocumentElement();
			Element catalogue2=document2.getDocumentElement();

			msgErreur="Difference Inconnue";
			if(compare(catalogue1,catalogue2))
			{
				msgErreur="Pas de difference";
				return true;
			}
			else
			{
				return false;
			}			
			/*NodeList titres = catalogue.getElementsByTagName("class");
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
			/*titres = catalogue.getElementsByTagName("nom_classe");
			System.out.println(
				"Nombre d'element dans la racine : " + titres.getLength());*/
			//Classe c = creer_classe(catalogue);
			//System.out.println("classe=" + c.toString());
			//return c;
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
		return false;
	}
	
	public int avance(Element element,int i)
	{
		assert(element!=null);
		assert(i>=0);
		int res=-1;
		NodeList fils = element.getChildNodes();
		if(i>=fils.getLength())
		{
			return i;
		}
		while(i<fils.getLength())
		{
			Node n = fils.item(i);
			if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+"));
			} else {
				res=i;
				break;
			}
			i++;
		}
		if(res==-1)
			return i;
		assert(res>=i);
		return res;
	}

	public Compare_XML(String nom1,String nom2)
	{
		this.nom1=nom1;
		this.nom2=nom2;
		ligne1=-1;colonne1=-1;
		ligne2=-1;colonne2=-1;
	}

	public void setError(Element element,Element element2)
	{
		assert(element!=null);
		assert(element2!=null);
		//ligne1=
	}

	public String nom1,nom2;
	public int ligne1=-1,colonne1=-1;
	public int ligne2=-1,colonne2=-1;

	public String diff(Element element,Element element2)
	{
		assert(element!=null);
		assert(element2!=null);
		return element.getNodeName()+" et "+
						element2.getNodeName();
	}

	public String diff(Element element)
	{
		assert(element!=null);// DeferredElementImpl
		//DeferredElementImpl d=(DeferredElementImpl)element;
		//d.
		return element.getNodeName();
	}


	public boolean compare(Element element,Element element2) {
		assert(element != null);
		assert(element2!=null);
		//assert(element.getNodeName() == "expression_unaire");

		if(!element.getNodeName().equals(element2.getNodeName()))
		{
			setError(element,element2);
			msgErreur="Difference entre les nodes "+
				diff(element,element2);
			return false;
		}
		NodeList fils = element.getChildNodes();
		NodeList fils2 = element2.getChildNodes();
		if(fils.getLength()==0)
		{
			if(fils2.getLength()>1)
			{
				setError(element,element2);
				msgErreur="Pas le meme nombre de fils pour "+
						diff(element,element2);
				return false;
			}
			else if(fils2.getLength()==1)
			{
				if(!(fils2.item(0) instanceof Text))
				{
					setError(element,element2);
					msgErreur="Pas le meme nombre de fils pour "+
							diff(element,element2);
					return false;
				}
				else
				{
					String s=fils2.item(0).getNodeValue();
					if(s == "" || s.matches("[ \t\n]+"))
					{
						setError(element,element2);
						msgErreur="Pas le meme nombre de fils pour "+
								diff(element,element2);
						return false;
					}
				}
			}
		}
		else if(fils.getLength()==1&&fils.item(0) instanceof Text)
		{
			if(fils2.getLength()>1)
			{
				setError(element,element2);
				msgErreur="Pas le meme nombre de fils pour "+
						diff(element,element2);
				return false;
			}
			else if(fils2.getLength()==0)
			{
				String s=fils.item(0).getNodeValue();
				if(s != "" && !s.matches("[ \t\n]+"))
				{
					setError(element,element2);
					msgErreur="Pas le meme nombre de fils pour "+
							diff(element,element2);
					return false;
				}
			}
			else
			{
				if(!(fils2.item(0) instanceof Text))
				{
					setError(element,element2);
					msgErreur="L'element fils de "+diff(element2)+
						" n'est pas un text";
					return false;
				}
				String s1,s2;
				s1=fils.item(0).getNodeValue();
				s2=fils2.item(0).getNodeValue();
				if(!s1.equals(s2))
				{
					if(!((s1==""||s1.matches("[ \t\n]+"))&&
						(s2==""||s2.matches("[ \t\n]+"))))
					{
						setError(element,element2);
						msgErreur="Les textes ne sont pas les meme pour "+
							diff(element,element2)+"("+s1+"!="+s2+")";
						return false;
					}
				}
			}
		}
		else
		{
			int i=0,j=0,k;
			while(i<fils.getLength()&&j<fils2.getLength()) {
				i=avance(element,i);
				j=avance(element2,j);
				Node n = fils.item(i);
				Node n2 = fils2.item(j);
				//System.out.println("element=" + n.getNodeName());
				if (n instanceof Element) {
					if(!(n2 instanceof Element))
					{
						setError(element,element2);
						msgErreur=diff(element2)+" n'est pas un element";
						return false;
					}
					boolean r=compare((Element)n,(Element)n2);
					if(!r)
					{
						//setError(element,element2);
						return false;
					}
				} else {
					assert(false);
				}
				i=avance(element,i+1);
				j=avance(element2,j+1);
			}
			if(i<fils.getLength()||j<fils2.getLength())
			{
				setError(element,element2);
				msgErreur="Pas le meme nombre de fils pour "+
						diff(element,element2);
				return false;
			}
		}
		int i;
		//assert(exp1 != null);
		NamedNodeMap attributs = element.getAttributes();
		HashMap table=new HashMap();
		//String valeur = null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node num?ro = attributs.item(i);
			String nomAttribut = num?ro.getNodeName();
			String valeurAttribut = num?ro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			/*if (nomAttribut == "op")
				valeur = valeurAttribut;
			else
				assert(false);*/
			assert(!table.containsKey(nomAttribut));
			table.put(nomAttribut,valeurAttribut);
		}
		int nb=i;
		NamedNodeMap attributs2 = element2.getAttributes();
		for (i = 0; i < attributs2.getLength(); i++) {
			Node num?ro = attributs2.item(i);
			String nomAttribut = num?ro.getNodeName();
			String valeurAttribut = num?ro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			/*if (nomAttribut == "op")
				valeur = valeurAttribut;
			else
				assert(false);*/
			if(!table.containsKey(nomAttribut))
			{
				setError(element,element2);
				msgErreur="La clef "+nomAttribut+" de "+diff(element)+
						" n'est pas dans "+diff(element2);
				return false;
			}
			if(!table.get(nomAttribut).equals(valeurAttribut))
			{
				setError(element,element2);
				msgErreur="Pas le meme contenu pour la clef "+nomAttribut+" pour "+
						diff(element,element2)+" ("+
						table.get(nomAttribut)+"!="+valeurAttribut+")";
				return false;
			}
		}
		if(i!=nb)
		{
			setError(element,element2);
			msgErreur="Pas le meme nombre de clef pour "+
					diff(element,element2)+"("+nb+"!="+i+")";
			return false;
		}
		//assert(valeur != null);

		return true;
	}

/*	public Expr_Unaire creer_expression_unaire(Element element) {
		assert(element != null);
		assert(element.getNodeName() == "expression_unaire");

		NodeList fils = element.getChildNodes();
		Expr exp1 = null;
		int i;
		for (i = 0; i < fils.getLength(); i++) {
			Node n = fils.item(i);
			System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					System.out.println("expression");
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
		String valeur = null;
		for (i = 0; i < attributs.getLength(); i++) {
			Node num?ro = attributs.item(i);
			String nomAttribut = num?ro.getNodeName();
			String valeurAttribut = num?ro.getNodeValue();
			//System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "op")
				valeur = valeurAttribut;
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
		else
			assert(false):"code inconnue:"+valeur;

		return res;
	}
*/
}
