package tinyeiffel.interpreteur2;

public abstract class EObjetSpecial extends EObjet {

	public EObjetSpecial(EType t) {
		super(t);
	}

	public abstract EObjet action(String nom,EObjet param[]);
}
