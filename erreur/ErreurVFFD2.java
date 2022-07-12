/*
 * Created on 6 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;
//import compiler.*;

/**
 * @author BARRET
 *
 * Erreur VFFD no 2
 * Des attributs d'une meme classe ont des noms identiques
 */
public class ErreurVFFD2 extends Erreur {

	public ErreurVFFD2(Attribut a1,Attribut a2)
	{
		assert(a1!=null);
		assert(a2!=null);
		assert(a1.nom.equals(a2.nom));
		assert(a1.classe.nom.compareToIgnoreCase(a2.classe.nom)==0);
		//assert(a1.nom.debut().avant(a2.nom.debut()));
		attribut1=a1;
		attribut2=a2;
		simple=false;
	}

	public ErreurVFFD2(String nom_classe,NomFeature nom,
			Position pos1,Position pos2)
	{
		assert(nom_classe!=null);
		assert(nom!=null);
		assert(pos1!=null);
		assert(pos2!=null);
		simple=true;
		this.nom_classe=nom_classe;
		this.nom=nom;
		this.pos1=pos1;
		this.pos2=pos2;
	}

	Attribut attribut1,attribut2;
	boolean simple;
	String nom_classe;
	NomFeature nom;
	Position pos1, pos2;

	public String toString()
	{
		return toMsg();
	}

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		String res;
		if(simple)
		{
			res="Dans la classe "+nom_classe+", "+
				"l'attribut "+nom+" est defini plusieurs fois ("+
				pos1+" et "+pos2+")";
		}
		else
		{
			res="Dans la classe "+attribut1.classe.nom+", "+
				"l'attribut "+attribut1+" est defini plusieurs fois ("+
				attribut1.nom.debut()+" et "+attribut2.nom.debut()+")";
		}
		return res;
	}

}
