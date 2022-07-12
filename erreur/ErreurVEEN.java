/*
 * Created on 11 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * Erreur VEEN no 3
 * Variable invalide car non déclaré 
 */
public class ErreurVEEN extends Erreur {

	public ErreurVEEN(Classe classe,String nom,Token pos)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.classe=classe;
		this.nom=nom;
		this.pos=pos;
		simple=false;
	}

	public ErreurVEEN(Classe classe,String nom,Position pos)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.classe=classe;
		this.nom=nom;
		this.pos2=pos;
		simple=false;
	}
	
	public ErreurVEEN(String nom_classe,NomFeature nom,Position pos)
	{
		assert(classe!=null);
		assert(nom!=null);
		assert(pos!=null);
		this.nom_classe=nom_classe;
		this.nom_attr=nom;
		this.pos2=pos;
		simple=false;
	}

	public Classe classe;
	public String nom;
	public Token pos;
	public Position pos2;
	boolean simple;
	String nom_classe;
	NomFeature nom_attr;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(simple)
		{
			return "L'attribut "+nom_attr+" n'est pas declare dans la classe "+
				nom_classe+" a la ligne "+pos2;
		}
		else
		{
			return "L'attribut "+nom+" n'est pas declare dans la classe "+
				classe.nom+" a la ligne "+
				((pos!=null)?pos.debut():pos2);
		}
	}

}
