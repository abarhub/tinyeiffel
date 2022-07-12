/*
 * Created on 24 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification;

import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifAttribut {

	NomFeature nom;
	boolean est_once;
	DeclareVar param[];
	Type type_retour;
	
	public VerifAttribut(NomFeature nom,DeclareVar param[],
			Type type_retour,boolean est_once)
	{
		this.nom=nom;
		this.est_once=est_once;
		this.param=param;
		this.type_retour=type_retour;
	}
	
	public Position debut()
	{
		return nom.debut();
	}
}
