/*
 * Created on 8 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface Ensemble {

	public int contient(Elt e);
	public int contient(Elt e1,Elt e2);
	public void ajoute(Elt e);
	public void ajoute(Elt e1,Elt e2);

	//public Elt getElt();
	//public 

	public static final int dans_rien=1;
	public static final int dans_elt=2;
	public static final int dans_inter=3;
}
