/*
 * Created on 4 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.compiler;

import java.util.Vector;

import tinyeiffel.ast.Chaine;
import tinyeiffel.ast.NomFeature;
import tinyeiffel.ast.Token;
import tinyeiffel.ast.Type;
import tinyeiffel.erreur.ErreurSource;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractParser extends Lexer_Constantes {

	public Vector type_utilisee;
	public Logging logging;
	protected Lexer lexer;

	public void ajoute_type(Type t) {
		int j;
	
		if(t!=null&&!((Type)t).is_like)
		{
			for(j=0;j<type_utilisee.size();j++)
			{
				if(((Type)type_utilisee.elementAt(j)).egaux(t))
				{
					return;
				}
			}
			type_utilisee.addElement(t);
		}
	}

	public void ajoute_type(Vector t) {
		int i,j;
		boolean trouve;
	
		if(t!=null)
		{
			for(i=0;i<t.size();i++)
			{
				if(!((Type)t.elementAt(i)).is_like)
				{
					trouve=false;
					for(j=0;!trouve&&j<type_utilisee.size();j++)
					{
						if(((Type)type_utilisee.elementAt(j)).egaux(
							((Type)t.elementAt(i))))
						{
								trouve=true;
						}
					}
					if(!trouve)
					{
						ajoute_type((Type)t.elementAt(i));
					}
				}
			}
		}
	}

	/********************** parser ***********************/
	public boolean etatOk() {
		return lexer.etatOk;
	}

	protected NomFeature parse_nom_feature() {
		Token t_nom,t_frozen,t_accoladeF,t_id,t_virgule;
		Type type;
		NomFeature n=null;
		boolean erreur=false,est_frozen=false,est_deferred=false;
		Vector liste_creation,liste_nom_feature,liste_type;
		t_frozen=lexer.lit(TK_FROZEN);
		if(t_frozen!=null)
		{
			est_frozen=true;
		}
		t_nom=lexer.lit(ID);
		if(t_nom!=null)
		{
			n=new NomFeature(t_nom.text);
			n.set_token(t_nom,null);
			n.prefix=false;
			n.infix=false;
		}
		else
		{// TODO: infix et prefix
			Chaine ch;
			Token t_infix,t_prefix=null;
			t_infix=lexer.lit(TK_INFIX);
			if(t_infix==null)
			{
				t_prefix=lexer.lit(TK_PREFIX);
			}
			if(t_infix!=null||t_prefix!=null)
			{
				ch=parse_chaine();
				if(ch==null)
				{
					erreur_parse("Il faut une chaine");
					erreur=true;
				}
				else
				{
					n=new NomFeature(ch);
					if(t_infix!=null)
					{
						n.set_token(null,t_infix);
						n.infix=true;
					}
					else
					{
						n.set_token(null,t_prefix);
						n.prefix=true;
					}
				}
			}
		}
		if(n!=null)
		{
			n.frozen=est_frozen;
			n.tfrozen=t_frozen;
		}
		//assert((cl!=null)!=erreur);
		if(erreur||!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans le nom d'attribut");
		}
		return n;
	}

	protected Chaine parse_chaine() {
		Token t_ch,t_debut;
		Chaine ch,res=null;
		Vector liste=new Vector();
		//erreur_parse("chaine1");
		t_ch=lexer.lit(STRING);
		//erreur_parse("chaine2");
		t_debut=t_ch;
		while(t_ch!=null)
		{
			liste.addElement(t_ch.text);
			t_ch=lexer.lit(STRING);
		}
		//erreur_parse("chaine3");
		if(liste.size()>0)
		{
			assert(t_debut!=null);
			res=new Chaine(liste,t_debut);
		}
		if(!etatParserOk()||!etatOk())
		{
			erreur_parse2("Erreur dans la chaine");
		}
		return res;
	}
	
	protected void erreur_parse(String msg) {
		assert(msg!=null);
		if(liste_erreur==null)
		{
			liste_erreur=new Vector();
		}
		liste_erreur.addElement("Erreur Parse ("+lexer.y+","+lexer.x+"):"+msg);
		System.out.println("Erreur Parse ("+lexer.y+","+lexer.x+"):"+msg);
		System.out.print("suivant_deja_lu="+lexer.suivant_deja_lu);
		if(lexer.suivant_deja_lu)
			System.out.print("token="+lexer.dernier_token.text);
		System.out.println();
		System.out.println("Debug lexer :"+lexer.str_debug());
	}

	protected void erreur_parse2(String msg) {
		assert(msg!=null);
		logging.erreur(new ErreurSource(msg,getLine(),
					getColumn(),getFilename()));
	}

	Vector liste_erreur;

	public boolean etatParserOk() {
		if (liste_erreur==null||liste_erreur.size()==0)
			return true;
		else
			return false;
	}

	public String getMsgErreur() {
		assert(!etatParserOk());
		return (String)liste_erreur.elementAt(0);
	}

	public int getColumn() {
		return lexer.getColumn();
	}

	public int getLine() {
		return lexer.getLine();
	}

	public String getFilename() {
		return lexer.nom_fichier;
	}

}
