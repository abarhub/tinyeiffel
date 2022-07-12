/*
 * Created on 23 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Verif {

	protected Verif()
	{
		est_Ok=false;
	}
	
	public boolean get_ok()
	{
		return est_Ok;
	}
	
	public void set_ok()
	{
		est_Ok=true;
	}
	
	protected boolean est_Ok;
}
