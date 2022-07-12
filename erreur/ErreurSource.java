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
 * Erreur lexicale ou de parsing
 */
public class ErreurSource extends Erreur {

	public ErreurSource(String msg,int ligne,int colonne,
				String fichier)
	{
		this.msg=msg;
		this.ligne=ligne;
		this.colonne=colonne;
		this.fichier=fichier;
	}

	public String toString()
	{
		return "Erreur "+fichier+"("+ligne+","+
				colonne+") : "+msg;
	}
	
	

	public final String msg,fichier;
	public final int ligne,colonne; 

	/* (non-Javadoc)
	 * @see erreur.Erreur#toMsg()
	 */
	public String toMsg() {
		return toString();
	}

}
