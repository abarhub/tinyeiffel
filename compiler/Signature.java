/*
 * Created on 23 nov. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package tinyeiffel.compiler;

import tinyeiffel.ast.*;

/**
 * @author BARRET
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Signature {

	/**
	 * 
	 */
	public Signature(DeclareVar parametre[],Type retour) {
		//assert(retour!=null);
		this.parametre=parametre;
		this.retour=retour;
	}

	public Signature(Signature signature,Conversion conversion)
	{
		assert(signature!=null);
		assert(conversion!=null);
		int i,len;
		Type t;
		if(signature.parametre!=null)
		{
			len=signature.parametre.length;
			parametre=new DeclareVar[len];
			for(i=0;i<len;i++)
			{
				t=signature.parametre[i].type;
				parametre[i]=new DeclareVar(signature.parametre[i].nom,
										conversion.convertie(t));
			}
		}
		if(signature.retour!=null)
		{
			retour=conversion.convertie(signature.retour);
		}
	}

	public int verifie_signature(Signature signature,
								//Attribut_Heritage attribut,
								Context context,
								Table_Symbol table_symbol)
		{
			assert(signature!=null);
			//assert(attribut!=null);
			assert(context!=null);
			assert(table_symbol!=null);
			//Type parametre[];
			//Type type_retour;
			//parametre=signature.parametre;
			//type_retour=signature.retour;
			//assert(false);
			if(signature.parametre==null)
			{
				//assert(attribut.attribut.nb_param>=0);
				if(nb_parametre()!=0)
					return -2;
			}
			else
			{
				if(nb_parametre()!=signature.parametre.length)
								return -2;
				DeclareVar param[];
				param=this.parametre;
				for(int i=0;i<parametre.length;i++)
				{
					//assert(param[i].length==1);
					//assert(parametre[i].length==1);
					if(!context.type_compatible(signature.parametre[i].type,table_symbol,
												param[i].type,table_symbol))
					{
						return i;
					}
				}
			}
			if(signature.retour==null)
			{
				if(this.retour!=null)
				{
					return -3;
				}
			}
			else
			{
				if(this.retour==null)
				{
					return -3;
				}
				else if(!context.type_compatible(signature.retour,
								table_symbol,
								this.retour,
								table_symbol))
				{
					return -3;
				}
			}
			return -1;
		}

	public int nb_parametre()
	{
		if(parametre==null)
			return 0;
		else
			return parametre.length;
	}

	public Type getParametre(int no)
	{
		assert(no>=0);
		assert(no<nb_parametre());
		return parametre[no].type;
	}

	public Type getTypeRetour()
	{
		return retour;
	}

	public String getNom(int no)
	{
		assert(no>=0);
		assert(no<nb_parametre());
		return parametre[no].nom;
	}

	protected DeclareVar parametre[];
	protected Type retour;
	//protected String nom[];

}
