package tinyeiffel.interpreteur2;

public class EDouble extends EObjetSpecial {

	public EDouble(EType t,double v) {
		super(t);
		valeur=v;
	}

	public EObjet action(String nom, EObjet[] param) {
		return null;
	}

	public double getValeur() {
		return valeur;
	}

	public void setValeur(double valeur) {
		this.valeur = valeur;
	}
	
	private double valeur;

}
