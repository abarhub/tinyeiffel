/*
 * Created on 18 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import tinyeiffel.compiler.Compiler_Eiffel;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIInputXML {

	/**
	 * 
	 */
	public CIInputXML(String nom) {
		assert(nom!=null);
		assert(!nom.equals(""));
		this.nom=nom;
		parse_XML(nom);
	}

	public void parse_XML(String nom)
	{
		CIProgramme res=null;
		File f;
		int no;
		GestionnaireDErreurs err;
		assert(nom!=null);
		try {
			/* jasp */
			//System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            //	"net.sf.saxon.om.DocumentBuilderFactoryImpl");
			//Récupère une instance de la classe de fabrication
			DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
			//factory.setFeature("http://xml.org/sax/features/validation",true);
			factory.setValidating(true);
			//Récupére une instance de la classe DocumentBuilder (spécifique vendeur)
			DocumentBuilder parser = factory.newDocumentBuilder();
			err=new GestionnaireDErreurs();
			parser.setErrorHandler(err);
			//effectue le parsing avec récupération du noeud DOM Document
			System.out.println("Parsing de "+nom+"...");
			Document document = parser.parse(nom);
			System.out.println("Parsing de "+nom+" fait !"+Compiler_Eiffel.info_mem());
			System.gc();
			System.out.println("Apres GC :"+Compiler_Eiffel.info_mem());
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
			titres=null;
			System.gc();
			System.out.println("Apres GC2 :"+Compiler_Eiffel.info_mem());
			res=creer_programme(catalogue);
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
			//res=null;
		} catch (SAXException e) {
			// parsing error
			System.err.println("Exception Sax:"+e+"("+nom+")");
			e.printStackTrace();
			//System.exit(1);
			res=null;
		} catch (IOException e) {
			// i/o error
			e.printStackTrace();
			System.err.println("Erreur d'entree sortie");
			System.exit(1);
		}
		programme=res;
	}

	public CIProgramme creer_programme(Element element)
	{
		int no,i,len,j;
		CIProgramme res;
		CIClasse cl,liste_classe[];
		Vector v;
		boolean heritage[][],heritage_directe[][];
		CIDeclare_Var dv;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("programme"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		no=0;
		res=new CIProgramme();
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "classe") {
					//System.out.println("heritage");
					cl = creer_classe((Element) n,res);
					v.addElement(cl);
				}
				else if (n.getNodeName() == "system") {
					//System.out.println("heritage");
					ajouter_global((Element) n,res.liste_var_global);
					//dv = creer_declare_var((Element) n);
					//res.liste_var_global.ajoute(dv);
					//v.addElement(cl);
				}
				/*else if (n.getNodeName() == "declare_var") {
					//System.out.println("heritage");
					dv = creer_declare_var((Element) n);
					res.liste_var_global.ajoute(dv);
					//v.addElement(cl);
				}*/
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
		liste_classe=new CIClasse[v.size()];
		v.copyInto(liste_classe);
		res.liste_classe=liste_classe;
		len=liste_classe.length;
		heritage=new boolean[len][len];
		heritage_directe=new boolean[len][len];
		for(i=0;i<len;i++)
		{
			CITypeSimple t1,t2;
			cl=liste_classe[i];
			t1=cl.nom;
			if(cl.heritage!=null)
			{
				for(j=0;j<cl.heritage.length;j++)
				{
					t2=cl.heritage[j];
					if(res.type_existe(t2))
					{
						no=res.no_classe(t2);
						assert(no>=0);
						heritage_directe[i][no]=true;
					}
				}
			}
		}
		res.heritage=calcul_heritage(heritage_directe);
		res.heritage_directe=heritage_directe;
		return res;
	}

	public void ajouter_global(Element element,CIListe_Global liste)
	{
		assert(element!=null);
		assert(liste!=null);
		assert(element.getNodeName().equalsIgnoreCase("system"));
		CIDeclare_Var dv;
		NodeList fils = element.getChildNodes();
		int no = 0,i;
		Vector v;
		v = new Vector();
		for(i = 0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "declare_var") {
					//System.out.println("heritage");
					dv = creer_declare_var((Element) n);
					liste.ajoute(dv);
					//v.addElement(cl);
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
	}
	
	public static boolean[][] calcul_heritage(boolean heritage_directe[][])
	{
		boolean heritage[][],modifier=true;
		int taille,i,j,no,k;

		taille=heritage_directe.length;
		heritage=new boolean[taille][taille];

		for(i=0;i<taille;i++)
			for(j=0;j<taille;j++)
				heritage[i][j]=heritage_directe[i][j];

		for(k=0;k<taille*taille&&modifier;k++)
		{
			modifier=false;
			for(i=0;i<taille;i++)
			{
				for(j=0;j<taille;j++)
				{
					if(heritage[j][i]==true)
					{
						for(k=0;k<taille;k++)
						{
							if(heritage[k][j]&&!heritage[k][i])
							{
								modifier=true;
								heritage[k][i]=true;
							}
						}
					}
				}
			}
		}

		return heritage;
	}

	public CIClasse creer_classe(Element element,CIProgramme p)
	{
		int no,i;
		CIClasse res;
		CIClasse cl,liste_classe[];
		Vector v=null,v2,v3;
		CIType type=null,type2;
		CITypeSimple liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("classe"));
		assert(p!=null);
		NodeList fils = element.getChildNodes();
		CIAttribut attr,liste[];
		CICreation creation,liste_creation[]=null;
		CIAssertion invariant=null;
		CISource source=null;
		assert(fils.getLength()>0);
		no=0;
		//res=new Programme();
		v2=new Vector();
		res=new CIClasse();
		v3=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "hierarchie") {
					assert(v==null);
					liste_heritage=creer_hierarchie((Element) n);
				}
				else if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
				}
				else if (n.getNodeName() == "creation") {
					//System.out.println("heritage");
					creation=creer_creation((Element) n);
					v3.addElement(creation);
					/*if(liste_creation==null)
					{
						liste_creation=new Creation[1];
						liste_creation[1]=creation;
					}
					else
					{
						Creation liste2[];
						liste2=liste_creation;
						liste_creation=new Creation[liste2.length+1];
						for(i=0;i<liste2.length;i++)
							liste_creation[i]=liste2[i];
						liste_creation[i]=creation;
					}*/
				}
				else if (n.getNodeName() == "attribut") {
					//System.out.println("heritage");
					attr = creer_attribut((Element) n,res);
					v2.addElement(attr);
				}
				else if (n.getNodeName() == "invariant") {
					//System.out.println("heritage");
					invariant = creer_assert((Element) n);
					//v2.addElement(attr);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("heritage");
					source = creer_source((Element) n);
					//v2.addElement(attr);
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
		res.nom=(CITypeSimple)type;
		res.programme=p;
		res.heritage=liste_heritage;
		if(v2.size()>0)
		{
			liste=new CIAttribut[v2.size()];
			v2.copyInto(liste);
			res.liste_attribut=liste;
		}
		if(v3.size()>0)
		{
			liste_creation=new CICreation[v3.size()];
			v3.copyInto(liste_creation);
			res.creation=liste_creation;
		}
		res.invariant=invariant;
		res.source=source;
		return res;
	}

	public CITypeSimple[] creer_hierarchie(Element element)
	{
		int no,i;
		CITypeSimple res[]=null;
		Vector v=null;
		CIType type=null;
		CITypeSimple liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("hierarchie"));
		NodeList fils = element.getChildNodes();
		CISource source=null;
		//assert(fils.getLength()>0);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
					assert(type instanceof CITypeSimple);
					v.addElement(type);
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
		if(v!=null)
		{
			res=new CITypeSimple[v.size()];
			v.copyInto(res);
		}
		return res;
	}

	public CICreation creer_creation(Element element)
	{
		int no,i;
		CICreation res=null;
		Vector v=null,v2=null;
		CIType type=null,liste_type[]=null;
		CINom_Attribut nom,liste_nom[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("creation"));
		NodeList fils = element.getChildNodes();
		CISource source=null;
		//assert(fils.getLength()>0);
		v=new Vector();
		v2=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
					v2.addElement(type);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom = creer_nom_attribut((Element) n);
					v.addElement(nom);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("heritage");
					source = creer_source((Element) n);
					//v2.addElement(attr);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(v!=null&&v.size()>0)
		{
			liste_nom=new CINom_Attribut[v.size()];
			v.copyInto(liste_nom);
		}
		if(v2!=null&&v2.size()>0)
		{
			liste_type=new CIType[v2.size()];
			v2.copyInto(liste_type);
		}
		assert(liste_nom!=null);
		res=new CICreation(liste_type,liste_nom,source);
		return res;
	}
	
	public CISource creer_source(Element element)
	{
		int no,i,ligne=-1,colonne=-1;
		CISource res=null;
		Vector v=null;
		CIType type=null,liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("source"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="",fichier=null;//'"('"é('"-('è-"(')
		boolean est_like=false,expended=false;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "ligne")
				ligne = Integer.parseInt(nomAttribut);
			else if (nomAttribut.equals("colonne"))
				colonne = Integer.parseInt(nomAttribut);
			else if (nomAttribut.equals("fichier"))
				fichier = nomAttribut;
			else
				assert(false);
		}
		assert(ligne>=0);
		assert(colonne>=0);
		assert(fils.getLength()==0);
		/*v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
					v.addElement(type);
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
		if(v!=null)
		{
			liste_heritage=new CIType[v.size()];
			v.copyInto(liste_heritage);
		}
		if(est_like)
			res=new CIType(nom);
		else
			res=new CIType(expended,nom,liste_heritage);*/
		res=new CISource(ligne,colonne,fichier);
		return res;
	}
	

	public CITypeSimple creer_contrainte(Element element)
	{
		int no,i;
		CITypeSimple res=null,type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("contrainte"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		boolean est_like=false,expended=false;
		CISource source=null;

		assert(attributs.getLength()==0);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					assert(type==null);
					type = (CITypeSimple)creer_type((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(type!=null);
		res=type;
		return res;
	}
	
	public CIType creer_type(Element element)
	{
		int no,i;
		CIType res=null;
		Vector v=null;
		CIType type=null;
		CITypeSimple contrainte=null;
		CIType liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("type"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		boolean est_like=false,expended=false;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else if (nomAttribut.equals("est_like"))
				est_like=valeurAttribut.equalsIgnoreCase("oui");
			else if (nomAttribut.equals("expanded"))
				expended=valeurAttribut.equalsIgnoreCase("oui");
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
					type = creer_type((Element) n);
					v.addElement(type);
				}
				else if (n.getNodeName() == "contrainte") {
					//System.out.println("source");
					contrainte = creer_contrainte((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(v!=null)
		{
			liste_heritage=new CIType[v.size()];
			v.copyInto(liste_heritage);
		}
		if(est_like)
			res=new CITypeAncre(nom,source);
		else
			res=new CITypeSimple(expended,nom,liste_heritage,contrainte,source);
		return res;
	}

	public CIAttribut creer_attribut(Element element,CIClasse classe)
	{
		int no,i;
		CIAttribut res=null;
		Vector v=null,ascendant,descendant;
		CIType type=null,type_retour=null,liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("attribut"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		assert(classe!=null);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIAttribut_Ascendant attr_asc,liste_asc[];
		CIAttribut_Descendant attr_desc,liste_desc[];
		CIRoutine routine=null;
		CIExpr expr=null;
		boolean est_static=false;
		CISource source=null;
		boolean est_defaut=false;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equals("est_static"))
				est_static=valeurAttribut.equalsIgnoreCase("oui");
			else if (nomAttribut.equals("est_defaut"))
				est_defaut=valeurAttribut.equalsIgnoreCase("oui");
			else
				assert(false);
		}
		
		v=new Vector();
		ascendant=new Vector();
		descendant=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					if(type==null)
					{
						type = creer_type((Element) n);
						assert(type!=null);
					}
					else
					{
						type_retour = creer_type((Element) n);
						assert(type_retour!=null);
					}
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom = creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "routine") {
					//System.out.println("heritage");
					routine=creer_routine((Element) n,classe);
				}
				else if (n.getNodeName() == "expression") {
					//System.out.println("heritage");
					expr=creer_expr((Element) n);
				}
				else if (n.getNodeName() == "ascendant") {
					//System.out.println("heritage");
					attr_asc=creer_ascendant((Element) n);
					assert(attr_asc!=null);
					ascendant.addElement(attr_asc);
				}
				else if (n.getNodeName() == "descendant") {
					//System.out.println("heritage");
					attr_desc=creer_descendant((Element) n);
					assert(attr_desc!=null);
					descendant.addElement(attr_desc);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(nom!=null);
		res=new CIAttribut(nom,est_static,source);
		res.type=(CITypeSimple)type;
		res.retour=type_retour;
		res.attribut_defaut=est_defaut;
		if(ascendant.size()>0)
		{
			liste_asc=new CIAttribut_Ascendant[ascendant.size()];
			ascendant.copyInto(liste_asc);
			res.attribut_ascendant=liste_asc;
		}
		if(descendant.size()>0)
		{
			liste_desc=new CIAttribut_Descendant[descendant.size()];
			descendant.copyInto(liste_desc);
			res.attribut_descendant=liste_desc;
		}
		if(routine!=null)
		{
			res.routine=routine;
		}
		else if(expr!=null)
		{
			res.cst=expr;
		}
		return res;
	}

	public CINom_Attribut creer_nom_attribut(Element element)
	{
		int no,i;
		CINom_Attribut res=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("nom_attribut"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()==0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		boolean infix=false,rien=true;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else if (nomAttribut == "type")
			{
				if(valeurAttribut.equalsIgnoreCase("rien"))
				{
					rien=true;
				}
				else if(valeurAttribut.equalsIgnoreCase("infix"))
				{
					rien=false;
					infix=true;
				}
				else if(valeurAttribut.equalsIgnoreCase("prefix"))
				{
					rien=false;
					infix=false;
				}
				else
					assert(false);
			}
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(rien)
			res=new CINom_Attribut(nom,source);
		else
			res=new CINom_Attribut("\""+nom+"\"",infix,source);
		return res;
	}

	public CIAttribut_Ascendant creer_ascendant(Element element)
	{
		int no=-1,i;
		CIAttribut_Ascendant res=null;
		CIType type=null,liste_heritage[]=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("ascendant"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "no")
			{
				try {
					no = Integer.parseInt(valeurAttribut);
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
					assert(false);
				}
			}
			else
				assert(false);
		}
		assert(no>=0);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom = creer_nom_attribut((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		res=new CIAttribut_Ascendant(no,nom,source);
		return res;
	}

	public CIAttribut_Descendant creer_descendant(Element element)
	{
		int no,i;
		CIAttribut_Descendant res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("descendant"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CISource source=null;
		
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
					assert(type!=null);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom = creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(nom!=null);
		assert(type!=null);
		res=new CIAttribut_Descendant(nom,type,source);
		return res;
	}

	public CIDeclare_Var creer_declare_var(Element element)
	{
		int no,i;
		CIDeclare_Var res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("declare_var"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr_Scalaire es=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom = valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					type = creer_type((Element) n);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					es = creer_expr_scalaire((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(es!=null)
			res=new CIDeclare_Var(nom,type,es,source);
		else
			res=new CIDeclare_Var(nom,type,source);
		return res;
	}

	public CIDeclare_Var[] creer_signature(Element element)
	{
		int no,i;
		CIDeclare_Var res=null,liste[]=null;
		CIType type=null;
		Vector v;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("signature"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";

		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "declare_var") {
					//System.out.println("heritage");
					res = creer_declare_var((Element) n);
					v.addElement(res);
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
			liste=new CIDeclare_Var[v.size()];
			v.copyInto(liste);
		}
		return liste;
	}

	public CIRoutine creer_routine(Element element,CIClasse classe)
	{
		int no,i;
		CIRoutine res=null;
		CIType type=null;
		Vector v, v_local;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("routine"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		assert(classe!=null);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIDeclare_Var signature[]=null,decl;
		CIListe_Instr instr=null;
		CIListe_Var local=null;
		CIAssertion precondition=null,postcondition=null;
		CISource source=null;

		v=new Vector();
		v_local=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "signature") {
					//System.out.println("heritage");
					signature=creer_signature((Element) n);
				}
				else if (n.getNodeName() == "declare_var") {
					//System.out.println("heritage");
					decl = creer_declare_var((Element) n);
					v_local.addElement(decl);
				}
				else if (n.getNodeName() == "instruction") {
					//System.out.println("heritage");
					instr = creer_instruction((Element) n);
					//v.addElement(res);
				}
				else if (n.getNodeName() == "precondition") {
					//System.out.println("heritage");
					precondition = creer_assert((Element) n);
					//v.addElement(res);
				}
				else if (n.getNodeName() == "postcondition") {
					//System.out.println("heritage");
					postcondition = creer_assert((Element) n);
					//v.addElement(res);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(v_local.size()>0)
		{
			//local=new Declare_Var[v_local.size()];
			//v_local.copyInto(local);
			local=new CIListe_Var();
			local.ajoute(v_local);
		}
		res=new CIRoutine(classe,source);
		res.local=local;
		res.parametre=donne_parametre(signature);
		res.retour=donne_type_retour(signature);
		res.liste_instruction=instr;
		res.precondition=precondition;
		res.postcondition=postcondition;
		return res;
	}

	public CIAssertion creer_assert(Element element)
	{
		int no,i;
		CIAssertion res=null,ass;
		CIType type=null;
		Vector v,v_local;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("precondition")||
				element.getNodeName().equalsIgnoreCase("postcondition")||
				element.getNodeName().equalsIgnoreCase("invariant"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIDeclare_Var signature[]=null,local[]=null,decl;
		CIListe_Instr liste_instr=null;
		CISource source=null;

		v=new Vector();
		ass=new CIAssertion(null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "declare_var") {
					//System.out.println("heritage");
					decl=creer_declare_var((Element) n);
					assert(decl!=null);
					ass.liste.ajoute(decl);
				}
				else if (n.getNodeName() == "instruction") {
					assert(liste_instr==null);
					liste_instr = creer_instruction((Element) n);
					//assert(liste_instr!=null);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
					assert(false):"n="+n.getNodeName();
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
		ass.liste_instr=liste_instr;
		if(ass.liste.size()!=0||ass.liste_instr!=null)
			res=ass;
		res.source=source;
		assert(res!=null);
		return res;
	}
	
	public CIListe_Var donne_parametre(CIDeclare_Var sig[])
	{
		int i;
		CIDeclare_Var decl,liste[];
		Vector v;
		if(sig==null)
			return null;
		v=new Vector();
		for(i=0;i<sig.length;i++)
		{
			if(!sig[i].nom.equalsIgnoreCase("result"))
				v.addElement(sig[i]);
		}
		if(v.size()>0)
		{
			CIListe_Var liste2;
			//liste=new Declare_Var[v.size()];
			//v.copyInto(liste);
			//return liste;
			liste2=new CIListe_Var();
			liste2.ajoute(v);
			return liste2;
		}
		else
		{
			return null;
		}
	}

	public CIDeclare_Var donne_type_retour(CIDeclare_Var sig[])
	{
		int i;
		CIDeclare_Var decl,liste[];
		Vector v;
		if(sig==null)
			return null;
		v=new Vector();
		for(i=0;i<sig.length;i++)
		{
			if(sig[i].nom.equalsIgnoreCase("result"))
				return sig[i];
		}
		return null;
	}

	public CIExpr creer_expr(Element element)
	{
		int no,i;
		CIExpr res=null;
		CIType type=null;
		Vector v,v_local;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIDeclare_Var signature[]=null,local[]=null,decl;
		CISource source=null;

		v=new Vector();
		v_local=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_bin") {
					//System.out.println("heritage");
					assert(res==null);
					res=creer_expr_bin((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expression_un") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_un((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_scalaire((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_var((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expr_appel") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_appel((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expression_type") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_type((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expression_creation") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_creat((Element) n);
					assert(res!=null);
				}
				else if (n.getNodeName() == "expression_extern") {
					//System.out.println("heritage");
					assert(res==null);
					res = creer_expr_extern((Element) n);
					assert(res!=null);
				}
				else
					assert(false):"n="+n.getNodeName();
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

	public CIExpr_Binaire creer_expr_bin(Element element)
	{
		int no,i,op=-1;
		CIExpr_Binaire res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_bin"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr_Scalaire expr1=null,expr2=null,e;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "type")
			{
				if(valeurAttribut.equalsIgnoreCase("plus"))
					op=CIExpr_Binaire.Plus;
				else if(valeurAttribut.equalsIgnoreCase("moins"))
					op=CIExpr_Binaire.Moins;
				else if(valeurAttribut.equalsIgnoreCase("fois"))
					op=CIExpr_Binaire.Fois;
				else if(valeurAttribut.equalsIgnoreCase("div_entier"))
					op=CIExpr_Binaire.Div_entier;
				else if(valeurAttribut.equalsIgnoreCase("div_reel"))
					op=CIExpr_Binaire.Div_reel;
				else if(valeurAttribut.equalsIgnoreCase("mod"))
					op=CIExpr_Binaire.Mod;
				else if(valeurAttribut.equalsIgnoreCase("and"))
					op=CIExpr_Binaire.And;
				else if(valeurAttribut.equalsIgnoreCase("or"))
					op=CIExpr_Binaire.Or;
				else if(valeurAttribut.equalsIgnoreCase("puiss"))
					op=CIExpr_Binaire.Puiss;
				else if(valeurAttribut.equalsIgnoreCase("sups"))
					op=CIExpr_Binaire.SupS;
				else if(valeurAttribut.equalsIgnoreCase("sup"))
					op=CIExpr_Binaire.Sup;
				else if(valeurAttribut.equalsIgnoreCase("infs"))
					op=CIExpr_Binaire.InfS;
				else if(valeurAttribut.equalsIgnoreCase("inf"))
					op=CIExpr_Binaire.Inf;
				else if(valeurAttribut.equalsIgnoreCase("egal"))
					op=CIExpr_Binaire.Egal;
				else if(valeurAttribut.equalsIgnoreCase("diff"))
					op=CIExpr_Binaire.Diff;
				else if(valeurAttribut.equalsIgnoreCase("and_then"))
					op=CIExpr_Binaire.And_Then;
				else if(valeurAttribut.equalsIgnoreCase("or_else"))
					op=CIExpr_Binaire.Or_Else;
				else if(valeurAttribut.equalsIgnoreCase("xor"))
					op=CIExpr_Binaire.Xor;
				else if(valeurAttribut.equalsIgnoreCase("implies"))
					op=CIExpr_Binaire.Implies;
				else
					assert(false);
			}
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					e=creer_expr_scalaire((Element) n);
					if(expr1==null)
						expr1=e;
					else
						expr2=e;
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(expr1!=null);
		assert(expr2!=null);
		assert(op!=-1);
		res=new CIExpr_Binaire(op,expr1,expr2,source);
		return res;
	}

	public CIExpr_Unaire creer_expr_un(Element element)
	{
		int no,i,op=-1;
		CIExpr_Unaire res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_un"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr_Scalaire expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "type")
			{
				if(valeurAttribut.equalsIgnoreCase("plus"))
					op=CIExpr_Unaire.Plus;
				else if(valeurAttribut.equalsIgnoreCase("moins"))
					op=CIExpr_Unaire.Moins;
				else if(valeurAttribut.equalsIgnoreCase("not"))
					op=CIExpr_Unaire.Not;
				else if(valeurAttribut.equalsIgnoreCase("old"))
					op=CIExpr_Unaire.Old;
				else if(valeurAttribut.equalsIgnoreCase("conv_e2d"))
					op=CIExpr_Unaire.Conv_E2D;
				else if(valeurAttribut.equalsIgnoreCase("conv_e2r"))
					op=CIExpr_Unaire.Conv_E2R;
				else if(valeurAttribut.equalsIgnoreCase("conv_r2d"))
					op=CIExpr_Unaire.Conv_R2D;
				else if(valeurAttribut.equalsIgnoreCase("conv_d2e"))
					op=CIExpr_Unaire.Conv_D2E;
				else if(valeurAttribut.equalsIgnoreCase("conv_r2e"))
					op=CIExpr_Unaire.Conv_R2E;
				else if(valeurAttribut.equalsIgnoreCase("conv_e2c"))
					op=CIExpr_Unaire.Conv_E2C;
				else if(valeurAttribut.equalsIgnoreCase("conv_c2e"))
					op=CIExpr_Unaire.Conv_C2E;
				else if(valeurAttribut.equalsIgnoreCase("addr"))
					op=CIExpr_Unaire.Dollard;
				else if(valeurAttribut.equalsIgnoreCase("conv_d2r"))
					op=CIExpr_Unaire.Conv_D2R;
				else
					assert(false);
			}
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					assert(expr1==null);
					expr1=creer_expr_scalaire((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(expr1!=null);
		assert(op!=-1);
		res=new CIExpr_Unaire(op,expr1,source);
		return res;
	}

	public CIExpr_Var creer_expr_var(Element element)
	{
		int no,i,op;
		CIExpr_Var res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("var"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="",src=null;
		CIExpr_Scalaire expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom=valeurAttribut;
			else if (nomAttribut == "src")
				src=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					assert(expr1==null);
					expr1=creer_expr_scalaire((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		if(expr1!=null)
			res=new CIExpr_Var(nom,expr1,source);
		else
			res=new CIExpr_Var(nom,source);
		if(src!=null)
			res.set_src(src);
		return res;
	}

	public CIExpr_Var_Syst creer_expr_var_syst(Element element)
	{//&é"'(-è_
		int no,i,op;
		CIExpr_Var_Syst res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expr_var_syst"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr_Scalaire expr1=null,param[];
		Vector liste_param=new Vector();
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "nom")
				nom=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		assert(nom.compareTo("")!=0);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					//assert(expr1==null);
					expr1=creer_expr_scalaire((Element) n);
					liste_param.addElement(expr1);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		/*if(expr1!=null)
			res=new Expr_Var(nom,expr1);
		else
			res=new Expr_Var(nom);*/
		if(liste_param==null||liste_param.size()==0)
		{
			param=null;
		}
		else
		{
			param=new CIExpr_Scalaire[liste_param.size()];
			liste_param.copyInto(param);
		}
		res=new CIExpr_Var_Syst(nom,param,source);
		return res;
	}

	public CIExpr_Type creer_expr_type(Element element)
	{
		int no,i,op;
		CIExpr_Type res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_type"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr_Var var=null;
		CISource source=null;

		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					assert(var==null);
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					assert(type==null);
					type=creer_type((Element) n);
					assert(type!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					assert(var==null);
					CIExpr_Scalaire es;
					es=creer_expr_scalaire((Element) n);
					assert(es!=null);
					assert(es instanceof CIExpr_Var);
					var=(CIExpr_Var)es;
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
					assert(false):"n="+n.getNodeName();
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
		assert(type!=null);
		assert(nom!=null);
		res=new CIExpr_Type(var,type,source);
		return res;
	}

	public CIExpr_Appel creer_expr_appel(Element element)
	{
		int no,i,op;
		CIExpr_Appel res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expr_appel"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[];
		Vector v;
		CISource source=null;

		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					assert(var==null);
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					assert(nom==null);
					nom=creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					expr=creer_expr_scalaire((Element) n);
					assert(expr!=null);
					//v.addElement(expr);
					if(nom!=null)
					{
						v.addElement(expr);
					}
					else
					{
						assert(expr instanceof CIExpr_Var);
						var=(CIExpr_Var)expr;
					}
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		//assert(var!=null);
		assert(nom!=null);
		if(v.size()>0)
		{
			param=new CIExpr_Scalaire[v.size()];
			v.copyInto(param);
		}
		else
		{
			param=null;
		}
		res=new CIExpr_Appel(var,nom,param,source);
		return res;
	}

	public CIExpr_Extern creer_expr_extern(Element element)
	{
		int no,i,op;
		CIExpr_Extern res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_extern"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[];
		Vector v;
		CISource source=null;
		String lang=null,opt=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equalsIgnoreCase("lang_extern"))
			{
				lang=valeurAttribut;
			}
			else if(nomAttribut.equalsIgnoreCase("opt_extern"))
			{
				opt=valeurAttribut;
			}
			else
				assert(false);
		}
		assert(lang!=null);
		assert(lang.length()>0);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					assert(var==null);
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					assert(nom==null);
					nom=creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					expr=creer_expr_scalaire((Element) n);
					assert(expr!=null);
					//v.addElement(expr);
					if(nom!=null)
					{
						v.addElement(expr);
					}
					else
					{
						assert(expr instanceof CIExpr_Var);
						var=(CIExpr_Var)expr;
					}
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else if (n.getNodeName() == "type") {
					//System.out.println("source");
					type = creer_type((Element) n);
				}
				else
					assert(false):n.getNodeName();
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
		assert(type!=null);
		assert(nom!=null);
		if(v.size()>0)
		{
			param=new CIExpr_Scalaire[v.size()];
			v.copyInto(param);
		}
		else
		{
			param=null;
		}
		res=new CIExpr_Extern(type,lang,opt,var,nom,param,source);
		return res;
	}
	
	public CIExpr_Creation creer_expr_creat(Element element)
	{
		int no,i,op;
		CIExpr_Creation res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_creation"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[],es=null;
		Vector v;
		CIExpr_Appel ea=null;
		CISource source=null;

		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "type") {
					//System.out.println("heritage");
					assert(type==null);
					type=creer_type((Element) n);
					assert(type!=null);
				}
				else if (n.getNodeName() == "expr_appel") {
					//System.out.println("heritage");
					assert(ea==null);
					ea=creer_expr_appel((Element) n);
					assert(ea!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					assert(es==null);
					es=creer_expr_scalaire((Element) n);
					assert(es!=null);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(type!=null);
		if(ea!=null)
		{
			assert(es==null);
			res=new CIExpr_Creation(type,ea,source);
		}
		else if(es!=null)
		{
			assert(es!=null);
			res=new CIExpr_Creation(type,es,source);
		}
		else
		{
			res=new CIExpr_Creation(type,(CIExpr_Appel)null,source);
		}
		return res;
	}

	public CIExpr_Scalaire creer_expr_scalaire(Element element)
	{
		int no,i,op;
		CIExpr_Scalaire res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("expression_scalaire"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="",text=null,oper="";
		CIExpr_Scalaire expr1;
		CIExpr_Var var=null;
		CIExpr_Var_Syst vars=null;
		CISource source=null;

		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					assert(var==null);
					assert(vars==null);
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "expr_var_syst") {
					//System.out.println("heritage");
					assert(vars==null);
					assert(var==null);
					vars=creer_expr_var_syst((Element) n);
					assert(vars!=null);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
					assert(false):"nom="+n.getNodeName();
			}
			else if (n instanceof Comment) {
				
			}
			else if (n instanceof Text) {
				String s = n.getNodeValue();
				assert(s == "" || s.matches("[ \t\n]+")):s;
			}
			else
				assert(false);
		}
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if(nomAttribut.equalsIgnoreCase("type"))
				oper=valeurAttribut;
			else if(nomAttribut.equalsIgnoreCase("text"))
				text=valeurAttribut;
			else
				assert(false):"nom="+nomAttribut;
		}
		assert(oper!=null);
		if(oper.equalsIgnoreCase("entier"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			assert(text.matches("^[ ]*[\\-+]?[ ]*[0-9]+[ ]*$")):"text="+text;
			try{
				res=new CIExpr_Entier(Integer.parseInt(text),source);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				assert(false);
			}
		}
		else if(oper.equalsIgnoreCase("reel"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			assert(text.matches("^[ ]*[\\-+]?[ ]*(([0-9]*\\.[0-9]+)|([0-9]+\\.[0-9]*)|([0-9]+))(e[\\-+]*[0-9]+)?[ ]*$")):"text="+text;
			try{
				res=new CIExpr_Real(Double.parseDouble(text),source);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				assert(false);
			}
		}
		else if(oper.equalsIgnoreCase("double"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			assert(text.matches("^[ ]*[\\-+]?[ ]*(([0-9]*\\.[0-9]+)|([0-9]+\\.[0-9]*)|([0-9]+))(e[\\-+]*[0-9]+)?[ ]*$")):"text="+text;
			try{
				res=new CIExpr_Double(Double.parseDouble(text),source);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				assert(false);
			}
		}
		else if(oper.equalsIgnoreCase("chaine"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			//assert(text.matches("^$"));
			res=new CIExpr_String(text,source);
		}
		else if(oper.equalsIgnoreCase("car"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			assert(text.length()==1):"text="+text;
			res=new CIExpr_Char(text.charAt(0),source);
		}
		else if(oper.equalsIgnoreCase("booleen"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text!=null);
			assert(text.matches("^[tT][rR][uU][eE]|[fF][aA][lL][sS][eE]$")):"text="+text;
			res=new CIExpr_Bool(text.equalsIgnoreCase("true"),source);
		}
		else if(oper.equalsIgnoreCase("void"))
		{
			assert(var==null);
			assert(vars==null);
			assert(text==null);
			res=new CIExpr_Void(source);
		}
		else if(oper.equalsIgnoreCase("var"))
		{
			assert(var!=null);
			assert(vars==null);
			assert(text==null);
			res=var;
		}
		else if(oper.equalsIgnoreCase("var_syst"))
		{
			assert(vars!=null);
			assert(var==null);
			assert(text==null);
			res=vars;
		}
		else
			assert(false);
		//res=new Expr_Unaire(op,expr1);
		return res;
	}

	public CIListe_Instr creer_instruction(Element element)
	{
		int no,i,op;
		CIListe_Instr res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instruction"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[];
		Vector v;
		CIExpr_Appel ea=null;
		CIInstruction instr;
		CISource source=null;

		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "instr_affect") {
					//System.out.println("heritage");
					instr=creer_instruction_affect((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_appel") {
					//System.out.println("heritage");
					instr=creer_instruction_appel((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_si") {
					//System.out.println("heritage");
					instr=creer_instruction_si((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_si_non") {
					//System.out.println("heritage");
					instr=creer_instruction_si_non((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_goto") {
					//System.out.println("heritage");
					instr=creer_instruction_goto((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_label") {
					//System.out.println("heritage");
					instr=creer_instruction_label((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_return") {
					//System.out.println("heritage");
					instr=creer_instruction_return((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_raise") {
					//System.out.println("heritage");
					instr=creer_instruction_raise((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "instr_extern") {
					//System.out.println("heritage");
					instr=creer_instruction_extern((Element) n);
					assert(instr!=null);
					v.addElement(instr);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
			res=new CIListe_Instr();
			res.ajoute(v);
		}
		return res;
	}

	public CIInstruction_Appel creer_instruction_appel(Element element)
	{
		int no,i,op;
		CIInstruction_Appel res;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_appel"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[]=null;
		Vector v;
		CIExpr_Appel ea=null;
		CIInstruction instr;
		CISource source=null;

		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom=creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					expr=creer_expr_scalaire((Element) n);
					assert(expr!=null);
					if(nom!=null)
					{
						v.addElement(expr);
					}
					else
					{
						assert(expr instanceof CIExpr_Var);
						var=(CIExpr_Var)expr;
					}
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		//assert(!nom.nom.equalsIgnoreCase("toto")):"v="+v;
		if(v.size()>0)
		{
			param=new CIExpr_Scalaire[v.size()];
			v.copyInto(param);
		}
		res=new CIInstruction_Appel(var,nom,param,source);
		return res;
	}
	
	public CIInstruction_Extern creer_instruction_extern(Element element)
	{
		int no,i,op;
		CIInstruction_Extern res;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_extern"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[]=null;
		Vector v;
		CIInstruction instr;
		CISource source=null;
		String lang=null,opt=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equalsIgnoreCase("lang_extern"))
			{
				lang=valeurAttribut;
			}
			else if(nomAttribut.equalsIgnoreCase("opt_extern"))
			{
				opt=valeurAttribut;
			}
			else
				assert(false);
		}
		assert(lang!=null);
		assert(lang.length()>0);
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "var") {
					//System.out.println("heritage");
					var=creer_expr_var((Element) n);
					assert(var!=null);
				}
				else if (n.getNodeName() == "nom_attribut") {
					//System.out.println("heritage");
					nom=creer_nom_attribut((Element) n);
					assert(nom!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					expr=creer_expr_scalaire((Element) n);
					assert(expr!=null);
					if(nom!=null)
					{
						v.addElement(expr);
					}
					else
					{
						assert(expr instanceof CIExpr_Var);
						var=(CIExpr_Var)expr;
					}
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		//assert(!nom.nom.equalsIgnoreCase("toto")):"v="+v;
		if(v.size()>0)
		{
			param=new CIExpr_Scalaire[v.size()];
			v.copyInto(param);
		}
		res=new CIInstruction_Extern(lang,opt,var,nom,param,source);
		return res;
	}
	
	public CIInstruction_Affect creer_instruction_affect(Element element)
	{
		int no,i,op;
		CIInstruction_Affect res;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_affect"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CINom_Attribut nom=null;
		CIExpr_Var var=null;
		CIExpr_Scalaire expr,param[]=null;
		Vector v;
		CIExpr_Appel ea=null;
		CIInstruction instr;
		CIExpr e=null;
		CISource source=null;
		boolean force=false;
		
		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equalsIgnoreCase("force"))
			{
				assert(valeurAttribut.equalsIgnoreCase("oui")||
					valeurAttribut.equalsIgnoreCase("non"));
				force=valeurAttribut.equalsIgnoreCase("oui");
			}
			else
				assert(false);
		}
		v=new Vector();
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("heritage");
					e=creer_expr((Element) n);
					assert(e!=null);
				}
				else if (n.getNodeName() == "expression_scalaire") {
					//System.out.println("heritage");
					expr=creer_expr_scalaire((Element) n);
					assert(expr!=null);
					assert(expr instanceof CIExpr_Var);
					var=(CIExpr_Var)expr;
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(var!=null);
		assert(e!=null);
		res=new CIInstruction_Affect(var,e,force,source);
		return res;
	}

	public CIInstruction_Si creer_instruction_si(Element element)
	{
		int no,i,op;
		CIInstruction_Si res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_si"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut.equalsIgnoreCase("label"))
				nom=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("heritage");
					assert(expr1==null);
					expr1=creer_expr((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
					assert(false):"n="+n.getNodeName();
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
		assert(expr1!=null);
		assert(nom!=null);
		res=new CIInstruction_Si(expr1,nom,source);
		return res;
	}

	public CIInstruction_Si_Non creer_instruction_si_non(Element element)
	{
		int no,i,op;
		CIInstruction_Si_Non res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_si_non"));
		NodeList fils = element.getChildNodes();
		assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "label")
				nom=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("heritage");
					assert(expr1==null);
					expr1=creer_expr((Element) n);
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
					assert(false):"n="+n.getNodeName();
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
		assert(expr1!=null);
		assert(nom!=null);
		res=new CIInstruction_Si_Non(expr1,nom,source);
		return res;
	}

	public CIInstruction_Goto creer_instruction_goto(Element element)
	{
		int no,i,op;
		CIInstruction_Goto res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_goto"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "label")
				nom=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		//assert(fils.getLength()==0);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
				}
				else
				{
					assert(false);
				}
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
		assert(nom!=null);
		res=new CIInstruction_Goto(nom,source);
		return res;
	}

	public CIInstruction_Label creer_instruction_label(Element element)
	{
		int no,i,op;
		CIInstruction_Label res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_label"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr expr1=null;
		CISource source=null;

		for (i = 0; i < attributs.getLength(); i++) {
			Node numéro = attributs.item(i);
			String nomAttribut = numéro.getNodeName();
			String valeurAttribut = numéro.getNodeValue();
			System.out.println(nomAttribut + " =\"" + valeurAttribut + "\"");
			if (nomAttribut == "label")
				nom=valeurAttribut;
			else
				assert(false);
		}
		assert(nom!=null);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(nom!=null);
		res=new CIInstruction_Label(nom,source);
		return res;
	}

	public CIInstruction_Return creer_instruction_return(Element element)
	{
		int no,i,op;
		CIInstruction_Return res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_return"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		String nom="";
		CIExpr expr1=null;
		CISource source=null;

		assert(attributs.getLength()==0);
		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		res=new CIInstruction_Return(source);
		return res;
	}

	public CIInstruction_Raise creer_instruction_raise(Element element)
	{
		int i,op;
		CIInstruction_Raise res=null;
		CIType type=null;
		assert(element!=null);
		assert(element.getNodeName().equalsIgnoreCase("instr_raise"));
		NodeList fils = element.getChildNodes();
		//assert(fils.getLength()>0);
		NamedNodeMap attributs = element.getAttributes();
		CIExpr no=null,nom=null,e;
		CISource source=null;

		for(i=0;i<fils.getLength();i++)
		{
			Node n = fils.item(i);
			//System.out.println("element=" + n.getNodeName());
			if (n instanceof Element) {
				if (n.getNodeName() == "expression") {
					//System.out.println("heritage");
					e=creer_expr((Element) n);
					assert(e!=null);
					if(no==null)
						no=e;
					else
						nom=e;
				}
				else if (n.getNodeName() == "source") {
					//System.out.println("source");
					source = creer_source((Element) n);
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
		assert(no!=null);
		if(nom==null)
			res=new CIInstruction_Raise(no,source);
		else
			res=new CIInstruction_Raise(no,nom,source);
		return res;
	}

	/*******************************/
	
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

	public String nom;
	public CIProgramme programme;
}

class GestionnaireDErreurs implements ErrorHandler{
	   protected String message(SAXParseException e){
	      String message = "Message : "+e.getMessage()+"\n";
	      message += "Ligne "+e.getLineNumber()+", colonne "+e.getColumnNumber()+"\n";
	      message += "Public id : "+e.getPublicId()+"\n";
	      message += "System id : "+e.getSystemId();
	      return message;
	   }
	   protected void printSAXException(SAXParseException e){
	      System.out.println(message(e));
	      if(e.getException() != null){
	         e.getException().printStackTrace();
	      }
	   }
	   public void warning(SAXParseException exception) throws SAXException{
	      System.out.println("*** Warning ***");
	      printSAXException(exception);
	   }
	   public void error(SAXParseException exception) throws SAXException{
	      //System.out.println("*** Erreur ***");
	      //printSAXException(exception);
		   String message = "*** Erreur ***\n";
		   message += message(exception);
		   System.out.println("message="+message);
		   SAXException se = new SAXException(message, exception);
		   throw se;
	   }
	   public void fatalError(SAXParseException exception) throws SAXException{
	      String message = "*** Erreur fatale ***\n";
	      message += message(exception);
	      SAXException se = new SAXException(message, exception);
	      throw se;
	   }
}