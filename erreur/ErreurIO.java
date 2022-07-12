/*
 * Created on 27 sept. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.erreur;

/**
 * @author BARRET
 *
 * Erreur générique d'entrée/sortie
 */
public class ErreurIO extends Erreur {

	public ErreurIO(String msg,String fichier)
	{
		this.msg=msg;
		this.fichier=fichier;
	}

	public String toString()
	{
		return "Erreur "+fichier+" : "+msg;
	}

	public final String msg,fichier;

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return toString();
	}

}
