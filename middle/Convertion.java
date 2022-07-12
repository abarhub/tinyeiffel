/*
 * Created on 2 janv. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.middle;

import java.util.Vector;
import tinyeiffel.ast.*;
import tinyeiffel.compiler.*;

/**
 * @author Barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Convertion {

	Compiler_Eiffel compiler;
	
	public Convertion(Compiler_Eiffel compiler)
	{
		System.out.println("Coucou conv");
		this.compiler=compiler;
		initialise();
		traitement();
	}
	
	/**
	 * 
	 */
	public void initialise() {
		liste_classe=new Vector();
		table_symbol=new Table_Symbol_Interm();
	}

	public void traitement()
	{
		Vector liste;
		int i;
		Classe_Interm cl;
		Classe c;
		liste=compiler.liste_classe;
		for(i=0;i<liste.size();i++)
		{
			c=(Classe)liste.elementAt(i);
			cl=new Classe_Interm(c.nom);
			liste_classe.addElement(cl);
		}
		for(i=0;i<liste_classe.size();i++)
		{
			cl=(Classe_Interm)liste_classe.elementAt(i);
			System.out.println(cl.nom);
		}
	}

	public Vector liste_classe;
	public Table_Symbol_Interm table_symbol;
}
