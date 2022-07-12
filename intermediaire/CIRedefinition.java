/*
 * Created on 1 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIRedefinition {

	/**
	 * 
	 */
	public CIRedefinition(CIAttribut attribut,CIAttribut redefinition[],
			CISource source) {
		assert(attribut!=null);
		assert(redefinition!=null);
		this.attribut=attribut;
		this.redefinition=redefinition;
		this.source=source;
	}

	public final CIAttribut attribut,redefinition[];
	public CISource source; // TODO: est-ce utile ?

}
