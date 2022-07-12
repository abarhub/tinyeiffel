/*
 * Created on 5 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.verification.type_verif;

import java.io.PrintStream;

import tinyeiffel.ast.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VerifType {

	public VerifType(String var_type,Type type_reel)
	{
		assert(var_type!=null);
		assert(type_reel!=null);
		this.type_reel=type_reel;
		this.var_type=var_type;
	}

	public VerifType(String var_type)
	{
		assert(var_type!=null);
		this.type_reel=null;
		this.var_type=var_type;
	}
	
	public boolean a_type_reel()
	{
		return type_reel!=null;
	}
	
	public String toString()
	{
		if(type_reel==null)
			return var_type+"=()";
		else
			return var_type+"=("+type_reel+")";
	}
	
	public Type get_type_reel()
	{
		return type_reel;
	}
	
	public void set_type_reel(Type t)
	{
		assert(t!=null);
		assert(type_reel==null);
		type_reel=t;
	}
	
	public String get_var_type()
	{
		return var_type;
	}
	
	public void toXML(PrintStream out)
	{
		assert(out!=null);
		
		if(type_reel!=null)
		{
			out.println("<verif_type var_type=\""+var_type+"\">");
			type_reel.toXML(out);
			out.println("</verif_type>");
		}
		else
		{
			out.println("<verif_type var_type=\""+var_type+"\" />");
		}
	}
	
	protected Type type_reel;
	protected String var_type;
	
}
