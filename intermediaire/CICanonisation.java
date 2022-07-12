package tinyeiffel.intermediaire;

import java.util.logging.Logger;

public class CICanonisation {

	private CIProgramme programme;
	private Logger logger;
	
	public CICanonisation(CIProgramme p, Logger logger) {
		assert(p!=null);
		assert(logger!=null);
		programme=p;
		this.logger=logger;
	}

	public CIProgramme simplifie() {
		int i,j;
		CIClasse cl;
		CIAttribut attr;
		if(programme.liste_classe!=null)
		{
			for(i=0;i<programme.liste_classe.length;i++)
			{
				cl=programme.liste_classe[i];
				if(cl.liste_attribut!=null)
				{
					for(j=0;j<cl.liste_attribut.length;j++)
					{
						attr=cl.liste_attribut[j];
						if(attr.routine!=null)
							simplifie_routine(attr.routine);
					}
				}
			}
		}
		return programme;
	}

	private void simplifie_routine(CIRoutine routine) {
		CIListe_Instr liste_instr;
		int i;
		CIInstruction instr;
		if(routine.liste_instruction!=null)
		{
			liste_instr=routine.liste_instruction;
			for(i=0;i<liste_instr.size();i++)
			{
				instr=liste_instr.elt(i);
			}
		}
	}
}
