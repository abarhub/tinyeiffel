/*
 * Created on 23 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.ast;

import java.util.Vector;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TypeSimple extends Type {

	public TypeSimple(boolean expanded,String nom,Vector t)
	{
		this.nom=nom;
		this.expanded=expanded;
		if(t!=null)
		{
			generique=new Type[t.size()];
			t.copyInto(generique);
		}
	}
}
