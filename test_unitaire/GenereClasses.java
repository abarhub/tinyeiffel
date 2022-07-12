/*
 * Created on 9 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.test_unitaire;

import java.io.*;
import java.util.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenereClasses {

	String chemin, nom_ace;
	int nb_classe, nb_feature, nb_instr, nb_local;
	File racine;
	Vector liste_classes;
	
	public GenereClasses(String chemin,String nom_ace,
			int nb_classe,int nb_feature,int nb_instr,int nb_local)
	{
		assert(chemin!=null&&!chemin.equals(""));
		assert(nom_ace!=null&&!nom_ace.equals(""));
		assert(nb_classe>=1);
		assert(nb_feature>=0);
		assert(nb_instr>=0);
		assert(nb_local>=0);
		this.chemin=chemin;
		this.nom_ace=nom_ace;
		this.nb_classe=nb_classe;
		this.nb_feature=nb_feature;
		this.nb_instr=nb_instr;
		this.nb_local=nb_local;
		supprime(chemin);
		racine=new File(chemin);
		racine.mkdirs();
		assert(racine.exists());
		liste_classes=new Vector();
	}
	
	public void traitement()
	{
		init_classes();
		genere();
	}
	
	protected void init_classes()
	{
		int i,j,n,n2,k;
		ClasseTest cl;
		String nom;
		Random r=new Random();
		AttributTest attr,lattr[];
		cl=new ClasseTest();
		cl.nom="ANY";
		liste_classes.add(cl);
		for(i=0;i<nb_classe;i++)
		{
			cl=new ClasseTest();
			cl.nom="A"+i;
			n=r.nextInt(this.nb_feature)+1;
			assert(n>0);
			lattr=new AttributTest[n];
			for(j=0;j<n;j++)
			{
				attr=new AttributTest();
				attr.nom=cl.nom+"_"+j;
				n2=r.nextInt(5);
				if(n2>1)
				{
					attr.type=new String[n2-1];
					for(k=0;k<n2-1;k++)
					{
						attr.type[k]="ANY";
					}
				}
				lattr[j]=attr;
			}
			cl.liste_attribut=lattr;
			liste_classes.add(cl);
		}
	}

	protected void genere()
	{
		int i,j,k;
		ClasseTest cl;
		String nom;
		Random r=new Random();
		AttributTest attr,lattr[];
		FileOutputStream fo;
		PrintWriter out;
		File f;
		for(i=0;i<liste_classes.size();i++)
		{
			cl=(ClasseTest)liste_classes.elementAt(i);
			try {
				f=new File(racine,cl.nom.toLowerCase()+".e");
				fo=new FileOutputStream(f);
				out=new PrintWriter(fo);
				out.println("class "+cl.nom.toUpperCase());
				out.println();
				out.println("feature");
				out.println();
				lattr=cl.liste_attribut;
				System.out.println("coucou="+lattr);
				if(lattr!=null&&lattr.length>0)
				{
					for(j=0;j<lattr.length;j++)
					{
						System.out.println("coucou");
						attr=lattr[j];
						out.print("\t"+attr.nom);
						if(attr.type!=null&&attr.type.length>0)
						{
							out.print("(");
							for(k=0;k<attr.type.length;k++)
							{
								if(k>0)
								{
									out.print(";");
								}
								out.print("p"+(k+1)+":"+attr.type[k]);
							}
							out.print(")");
						}
						if(attr.type_retour!=null)
						{
							out.print(":"+attr.type_retour);
						}
						if(attr.type!=null&&attr.type.length>0)
						{
							out.println(" is");
							out.println("\tdo");
							out.println("\tend;");
						}
						else
						{
							out.println(";");
						}
						out.println();
					}
				}
				out.println("end");
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println(e);
				e.printStackTrace();
			}
		}
		try {
			f=new File(racine,"test1.e");
			fo=new FileOutputStream(f);
			out=new PrintWriter(fo);
			out.println("class TEST1");
			out.println();
			out.println("feature");
			out.println();
			for(i=0;i<liste_classes.size();i++)
			{
				cl=(ClasseTest)liste_classes.elementAt(i);
				out.println("\tattr"+i+":"+cl.nom+";");
			}
			out.println();
			out.println("end");
			out.close();
			f=new File(racine,nom_ace);
			fo=new FileOutputStream(f);
			out=new PrintWriter(fo);
			out.println("system test1");
			out.println();
			out.println("root test1");
			out.println();
			out.println("cluster");
			out.println();
			out.println("\t\""+racine+"\\\";");
			out.println("\t\".\\\";");
			out.println();
			out.println("end");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * supprime le fichier chemin ou le repertoire chemin et son contenu
	 * si chemin="toto\tata\titi", le repertoire titi sera supprimé, 
	 * mais pas toto, ni tata
	 * @param chemin
	 */
	public static void supprime(String chemin)
	{
		File f=new File(chemin);
		supprime(f);
	}
	
	public static void supprime(File f)
	{
		if(f.exists())
		{
			if(f.isDirectory())
			{
				File liste[];
				int i;
				liste=f.listFiles();
				for(i=0;i<liste.length;i++)
				{
					supprime(liste[i]);
				}
				f.delete();
			}
			else
			{
				f.delete();
			}
		}
	}
	
	public static void main(String[] args) {
		GenereClasses c=new GenereClasses("test\\genere_classe\\test1",
				"test.ace",2,20,3,2);
		c.traitement();
		//supprime("test\\genere_classe\\test1");
	}
}

class ClasseTest
{
	String nom;
	AttributTest liste_attribut[];
}
class AttributTest
{
	String nom;
	String type[];
	String type_retour; 
}