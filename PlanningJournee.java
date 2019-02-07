import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.ArrayList;

public class PlanningJournee {

	private List<Chirurgie> listeChirurgies;
	private List<Salle> listeSalles; // Salle classique
    private List<Salle> listeSallesUrgence; // Salle urgence
	private List<Chirurgien> listeChirurgiens;
	private List<Conflit> listeConflits;

	public PlanningJournee(List<Chirurgie> lc,List<Salle> ls, List<Salle> lsu,  List<Chirurgien> lch) {
		this.listeChirurgies = lc;
		this.listeSalles = ls;
		this.listeChirurgiens = lch;
        this.listeSallesUrgence = lsu;
		this.listeConflits = new ArrayList<>();
	}

	public List<Conflit> getListeConflits() {
		return this.listeConflits;
	}

	public void setConflits() {
		// Vider la liste de conflit actuelle
		this.listeConflits.clear();

		Conflit nouveauConflit; // Sauvegarde le nouveau conflit, est null s'il y en a pas

		for (int i = 0; i < this.listeChirurgies.size(); i++) {
			for (int j = i + 1; j < this.listeChirurgies.size(); j++) {
				// Creer un nouveau conflit s'il y a lieu ou retourne null, reinitialisation de
				// nouveauConflit
				nouveauConflit = this.listeChirurgies.get(i).enConflit(this.listeChirurgies.get(j));

				if (nouveauConflit != null) {
					System.out.println(nouveauConflit);
					this.listeConflits.add(nouveauConflit);
				}

			}
		}

	}

	public void montrerConflits() {
		this.listeConflits.stream()
							.forEach(System.out::println);
	}

	public void resoudreConflits() {

		for(Conflit conflitCourant : this.listeConflits) {
                        if(conflitCourant.getPremiereChirurgie().estUrgente()){
                            conflitCourant.resoudreConflit(this.listeChirurgiens, this.listeSallesUrgence);
                        }else{
			conflitCourant.resoudreConflit(this.listeChirurgiens, this.listeSalles);
                        }
                }
	}

	public void visualiserConflits() {
		for (Conflit conflitCourant : this.listeConflits) {
			conflitCourant.visualiser();
		}
	}

	public void visualiser() {
		for (Chirurgie chrg : this.listeChirurgies) {
			System.out.print(chrg + " : ");
			chrg.visualisation();
		}
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();

		strb.append(listeChirurgies.toString());
		strb.append(listeSalles.toString());
		strb.append(listeChirurgiens.toString());

		return strb.toString();
	}
}
