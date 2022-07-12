package tinyeiffel.compiler;

import java.util.*;
import tinyeiffel.erreur.*;

public class Logging
{
	public Logging()
	{
		liste=new Vector();
		liste_erreur=new Vector();
	}

	public void erreurMsg(String text)
	{
		liste.addElement(new Log(erreur,text));
	}
	
	public void erreur(Erreur e)
	{
		liste_erreur.addElement(e);
	}
	
	public void warningMsg(String text)
	{
		liste.addElement(new Log(warning,text));
	}
	
	public void infoMsg(String text)
	{
		liste.addElement(new Log(info,text));
	}
	
	public void fatalMsg(String text)
	{
		try
		{
			throw new Exception("fatal");
		}
		catch (Exception e)
		{
			int i;
			StackTraceElement[] s=e.getStackTrace();
			text+="\r\nbacktrace:\r\n";
			for(i=0;i<s.length;i++)
			{
				text+=s[i]+"\r\n";
			}
		}
		liste.addElement(new Log(fatal,text));
	}
	
	public void affiche()
	{
		int i;
		Log log;
		Erreur erreur;
		String nom;

		System.out.println("Liste de tous les messages:");
		for(i=0;i<liste.size();i++)
		{
			System.out.print(i+" ");
			log=(Log)liste.elementAt(i);
			System.out.println(noToString(log.no)+":"+log.text);
		}
		for(i=0;i<liste_erreur.size();i++)
		{
			System.out.print(i+":");
			erreur=(Erreur)liste_erreur.elementAt(i);
			nom=erreur.getClass().getName();
			nom=nom.substring(6+1+6);
			System.out.println(nom+":"+erreur.toMsg());
		}
		System.out.println("Fin de la liste");
	}

	public void affiche(int no)
	{
		int i,j;
		Log log;

		System.out.println("Liste des messages "+noToString(no)+" :");
		j=0;
		for(i=0;i<liste.size();i++)
		{
			log=(Log)liste.elementAt(i);
			if(log.no==no)
			{
				System.out.print(j+" ");
				System.out.println(noToString(log.no)+":"+log.text);
				j++;
			}
		}
		System.out.println("Fin de la liste");
	}

	String noToString(int no)
	{
		switch(no)
		{
			case erreur:
				return "Erreur";
			case warning:
				return "Warning";
			case info:
				return "Info";
			case fatal:
				return "Fatal";
			default:
				return "Inconnue";
		}
	}

	public int nb_erreur()
	{
		int i,nb=0;
		if(liste!=null)
		{
			for(i=0;i<liste.size();i++)
			{
				Log l=(Log)liste.elementAt(i);
				if(l.no==erreur)
					nb++;
			}
		}
		return nb+liste_erreur.size();
	}

	public Erreur[] getListe_erreur()
	{
		int i;
		Erreur err,liste[];
		liste=new Erreur[liste_erreur.size()];
		for(i=0;i<liste.length;i++)
		{
			liste[i]=(Erreur)liste_erreur.elementAt(i);
		}
		return liste;
	}

	public String toString()
	{
		int i,j;
		Log log;
		String res="",nom;
		Erreur erreur;

		for(i=0;i<liste.size();i++)
		{
			res+=i+" ";
			log=(Log)liste.elementAt(i);
			res+=noToString(log.no)+":"+log.text+"\n";
		}
		for(i=0;i<liste_erreur.size();i++)
		{
			System.out.print(i+":");
			erreur=(Erreur)liste_erreur.elementAt(i);
			nom=erreur.getClass().getName();
			nom=nom.substring(6+1+6);
			res+=nom+":"+erreur.toMsg()+"\n";
		}
		return res;
	}
	
	private Vector liste,liste_erreur;

	public final int erreur=1,warning=2,info=3,fatal=4;

}