/*
 * Created on 26 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ErreurAffect extends Erreur {

	public ErreurAffect(Classe classe,Instr_Affect instr,String nom)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(nom!=null);
		this.classe=classe;
		this.instr1=instr;
		this.nom=nom;
		type=affect;
	}
	
	public ErreurAffect(Classe classe,Instr_TentAffect instr,String nom)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(nom!=null);
		this.classe=classe;
		this.instr2=instr;
		this.nom=nom;
		type=tent_affect;
	}

	public ErreurAffect(Classe classe,Instr_Creation instr,String nom)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(nom!=null);
		this.classe=classe;
		this.instr3=instr;
		this.nom=nom;
		type=creation;
	}

	Classe classe;
	Instr_Affect instr1;
	Instr_TentAffect instr2;
	Instr_Creation instr3;
	String nom;
	int type;
	final int affect=1;
	final int tent_affect=2;
	final int creation=3;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(type==affect)
			return "Dans la classe "+classe.nom+", l'affectation "+
				"n'a pas pour cible une variable a la ligne "+
			instr1.tid.debut();
		else if(type==tent_affect)
			return "Dans la classe "+classe.nom+", la tentative "+
				"d'affectation n'a pas pour cible une variable"+
				" a la ligne "+instr2.tid.debut();
		else
			return "Dans la classe "+classe.nom+", la creation "+
				"n'a pas pour cible une variable"+
				" a la ligne "+instr3.tid.debut();
	}

}
