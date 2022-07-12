package tinyeiffel.interpreteur2;

public class ECharacter extends EObjetSpecial {

	public ECharacter(EType t,char v) {
		super(t);
		valeur=v;
	}

	public EObjet action(String nom, EObjet[] param) {
		// TODO Auto-generated method stub
		return null;
	}

	public char getValeur() {
		return valeur;
	}

	public void setValeur(char valeur) {
		this.valeur = valeur;
	}
	
	private char valeur;

}
