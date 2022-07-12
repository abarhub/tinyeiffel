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
 * Erreur VFFD no 3
 * Conflit entre un attribut de la classe et d'un des ancetres 
 */
public class ErreurVFFD3 extends Erreur {

	public ErreurVFFD3(Attribut a1,Attribut a2,Classe c)
	{
		assert(false);
		assert(a1!=null);
		assert(a2!=null);
		assert(c!=null);
		//assert(a1.classe.nom.compareToIgnoreCase(c.nom)==0);
		assert(a2.classe.nom.compareToIgnoreCase(c.nom)!=0);
		attribut1=a1;
		attribut2=a2;
		classe=c;
	}

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(attribut1.classe.nom.compareToIgnoreCase(classe.nom)!=0)
			return "Erreur dans la classe "+classe.nom+" : "+
				"L'attribut "+attribut1+" est declaré "+
				"dans les classes ancetres "+attribut1.classe.nom+
				" et "+attribut2.classe.nom;
		else
			return "Erreur dans la classe "+classe.nom+" : "+
				"L'attribut "+attribut1+" est deja declaré "+
				"dans la classe ancetre "+attribut2.classe.nom;
	}

	public Attribut attribut1,attribut2;
	public Classe classe;

}
