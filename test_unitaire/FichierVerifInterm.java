/*
 * Created on 11 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tinyeiffel.test_unitaire;

/**
 * @author barret
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FichierVerifInterm {

	/**
	 * 
	 */
	public FichierVerifInterm(String nom) {
		assert(nom!=null);
		this.nom=nom;
		this.MsgErreur=null;
	}

	public FichierVerifInterm(String nom,String erreur[]) {
		assert(nom!=null);
		assert(erreur!=null);
		this.nom=nom;
		this.MsgErreur=erreur;
	}
	
	public String nom;
	public String MsgErreur[];
	public String typeErreur;
	/**
	 * @return
	 */
	public boolean erreur() {
		return MsgErreur!=null;
	}
	
	public String liste_message()
	{
		if(MsgErreur==null)
		{
			return "";
		}
		else
		{
			int i;
			String res="";
			for(i=0;i<MsgErreur.length;i++)
			{
				if(i>0)
					res+="\n";
				res+=MsgErreur[i];
			}
			return res;
		}
	}

	public boolean equivalent(String liste_Msg[],String type_parametre)
	{
		assert(type_parametre!=null);
		if(MsgErreur==null||MsgErreur.length==0)
		{
			if(liste_Msg!=null&&liste_Msg.length>0)
			{
				typeErreur="Erreurs dans "+type_parametre+" (no 0) en trop :"+liste_Msg[0];
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			if(liste_Msg==null||liste_Msg.length==0)
			{
				typeErreur="Erreurs dans fichier (no 0) en trop :"+MsgErreur[0];
				return false;
			}
			else
			{
				boolean tab[],trouve;
				int i,j;
				tab=new boolean[liste_Msg.length];
				for(i=0;i<MsgErreur.length;i++)
				{
					trouve=false;
					for(j=0;j<liste_Msg.length;j++)
					{
						if(!tab[j])
						{
							if(MsgErreur[i].equals(liste_Msg[j]))
							{
								tab[j]=true;
								trouve=true;
							}
						}
					}
					if(!trouve)
					{
						typeErreur="Erreurs dans fichier (no "+i+") en trop :"+MsgErreur[i];
						return false;
					}
				}
				for(i=0;i<tab.length;i++)
				{
					if(!tab[i])
					{
						typeErreur="Erreurs dans "+type_parametre+"(no "+i+") en trop :"+MsgErreur[i];
						return false;
					}
				}
				return true;
			}
		}
	}
}
