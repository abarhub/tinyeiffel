/*
 * Created on 10 janv. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.lace;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import tinyeiffel.ast.NomFeature;
import tinyeiffel.ast.Type;
/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Ace {

	/**
	 * Constructeur
	 * @param generation 
	 * @param extern 
	 * @param chemin_racine 
	 */
	public Ace(NomSysteme nom,Type nom_classe,
				NomGrappe grappe_racine,
				NomFeature procedure_racine,
				Vector liste_grappe, 
				Map extern, Map generation, String chemin_racine) {
		assert(nom!=null);
		assert(nom_classe!=null);
		this.nom=nom;
		this.nom_classe=nom_classe;
		this.grappe_racine=grappe_racine;
		this.procedure_racine=procedure_racine;
		if(liste_grappe!=null)
		{
			this.liste_grappe=new Grappe[liste_grappe.size()];
			liste_grappe.copyInto(this.liste_grappe);
		}
		if(extern==null)
		{
			extern=new HashMap();
		}
		if(generation==null)
		{
			generation=new HashMap();
		}
		this.extern=extern;
		this.generation=generation;
		if(!extern.containsKey(external_kernel_path))
		{
			extern.put(external_kernel_path,"intern/kernel.xml");
		}
		else
		{
			extern.put(external_kernel_path,extern.get(external_kernel_path));
		}
		String tmp;
		tmp=(String)extern.get(external_kernel_path);
		if(tmp!=null)
		{
			File f;
			f=new File(tmp);
			if(!f.exists())
			{
				f=new File(chemin_racine,tmp);
				if(f.exists())
				{
					extern.put(external_kernel_path,f.getAbsolutePath());
				}
				else
				{
					assert(false):"fichier "+tmp+" inconnue("+chemin_racine+","+f.getAbsolutePath()+")";
				}
			}
		}
	}

	public Grappe donne_grappe(NomGrappe nom)
	{
		int i;
		NomGrappe n;
		assert(nom!=null);
		if(liste_grappe!=null)
		{
			for(i=0;i<liste_grappe.length;i++)
			{
				n=liste_grappe[i].nom;
				if(n!=null&&n.equals(nom))
					return liste_grappe[i];
			}
		}
		return null;
	}

	public Grappe donne_grappe_racine()
	{
		Grappe g=null;
		if(grappe_racine!=null)
			g=donne_grappe(grappe_racine);
		if(g==null)
		{
			assert(liste_grappe!=null);
			assert(liste_grappe.length>0);
			g=liste_grappe[0];
		}
		assert(g!=null);
		return g;
	}

	public String classe_racine()
	{
		return nom_classe.nom;
	}

	public String routine_racine()
	{
		if(procedure_racine==null)
			return null;
		else
			return procedure_racine.nom;
	}

	public String extern_kernel()
	{
		String chemin;
		chemin=(String)extern.get(external_kernel_path);
		return chemin;
	}
	
	public NomSysteme nom;
	public Type nom_classe;
	public NomGrappe grappe_racine;
	public NomFeature procedure_racine;
	public Grappe liste_grappe[];
	public Map extern;
	public Map generation;
	public String chemin_racine;
	
	private static final String external_kernel_path = "external_kernel_path";
	
}
