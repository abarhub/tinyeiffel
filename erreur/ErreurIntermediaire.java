/*
 * Created on 5 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.erreur;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ErreurIntermediaire extends Erreur {

	/**
	 * 
	 */
	public ErreurIntermediaire(String m) {
		assert(m!=null);
		msg=m;
	}

	public String toString()
	{
		return "Erreur dans le code intermediaire : "+msg;
	}
	
	public final String msg;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return toString();
	}
}
