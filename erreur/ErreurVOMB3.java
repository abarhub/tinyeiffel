/*
 * Created on 11 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

import tinyeiffel.ast.*;
import java.util.*;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ErreurVOMB3 extends Erreur {

	public ErreurVOMB3(Classe classe,Instr_Inspect instr,
					Expr valeur1,Expr valeur2)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(valeur1!=null);
		assert(valeur2!=null);
		conflit=conflit_elt_elt;
		this.valeur1=valeur1;
		this.valeur2=valeur2;
		this.instr=instr;
		this.classe=classe;
	}

	public ErreurVOMB3(Classe classe,Instr_Inspect instr,
					Expr valeur1,Vector inter1)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(valeur1!=null);
		assert(inter1!=null);
		assert(inter1.size()==2);
		assert(inter1.elementAt(0) instanceof Expr);
		assert(inter1.elementAt(1) instanceof Expr);
		conflit=conflit_elt_inter;
		this.valeur1=valeur1;
		this.inter1=inter1;
		this.instr=instr;
		this.classe=classe;
	}

	public ErreurVOMB3(Classe classe,Instr_Inspect instr,
					Vector inter1,Vector inter2)
	{
		assert(classe!=null);
		assert(instr!=null);
		assert(inter1!=null);
		assert(inter1.size()==2);
		assert(inter1.elementAt(0) instanceof Expr);
		assert(inter1.elementAt(1) instanceof Expr);
		assert(inter2!=null);
		assert(inter2.size()==2);
		assert(inter2.elementAt(0) instanceof Expr);
		assert(inter2.elementAt(1) instanceof Expr);
		conflit=conflit_inter_inter;
		this.inter1=inter1;
		this.inter2=inter2;
		this.instr=instr;
		this.classe=classe;
	}
	
	Classe classe;
	Instr_Inspect instr;
	int conflit;
	Expr valeur1,valeur2;
	Vector inter1,inter2;

	public static final int conflit_elt_inter=1;
	public static final int conflit_elt_elt=2;
	public static final int conflit_inter_inter=3;
	

	protected String toString(Vector intervalle)
	{
		assert(intervalle!=null);
		assert(intervalle.size()==2);
		assert(intervalle.elementAt(0) instanceof Expr);
		assert(intervalle.elementAt(1) instanceof Expr);
		Expr e1,e2;
		e1=(Expr)intervalle.elementAt(0);
		e2=(Expr)intervalle.elementAt(1);
		return e1+".."+e2;
	}

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		if(conflit==conflit_elt_elt)
			return "Dans la classe "+classe.nom+", il y a conflit"+
					" entre la valeur "+valeur1+" qui est présente"+
					" a la ligne "+valeur1.debut()+" et a la ligne "+
					valeur2.debut();
		else if(conflit==conflit_elt_inter)
			return "Dans la classe "+classe.nom+", il y a conflit"+
					" entre la valeur "+valeur1+" a la ligne "+
					valeur1.debut()+" et l'intervalle "+
					toString(inter1)+" a la ligne "+
					((Expr)inter1.elementAt(0)).debut();
		else if(conflit==conflit_inter_inter)
			return "Dans la classe "+classe.nom+", il y a conflit"+
					" entre les intervalles "+toString(inter1)+
					" a la ligne "+((Expr)inter1.elementAt(0)).debut()+" et "+
					toString(inter2)+" a la ligne "+
					((Expr)inter2.elementAt(0)).debut();
		assert(false);
		return null;
	}

}
