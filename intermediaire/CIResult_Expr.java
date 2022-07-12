/*
 * Created on 6 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.intermediaire;

import java.util.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CIResult_Expr {

	/**
	 * 
	 */
	public CIResult_Expr(Vector v,CIExpr e) {
		assert(v!=null);
		assert(e!=null);
		instr=v;
		expr=e;
	}

	public boolean a_instr()
	{
		return instr!=null&&instr.size()>0;
	}

	public Vector instr;
	public CIExpr expr;

}
