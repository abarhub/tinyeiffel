package tinyeiffel.genere_c;

public class Table
{
	public Table(String nom_classe) 
	{
		this.nom_classe=nom_classe;
	}

	public String nom_classe;
	public Attribut liste_attribut[];
}
