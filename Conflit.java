import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Set;

public abstract class Conflit {
	private Chirurgie premiereChirurgie;
	private Chirurgie secondeChirurgie;

	public abstract boolean persiste();
	public abstract boolean ressourcesSuffisantes(List<Chirurgien> lc, List<Salle> ls);
	public abstract void modifierChirurgie(List<Chirurgien> lc, List<Salle> ls);

	public Conflit(Chirurgie first, Chirurgie second) {
		this.premiereChirurgie = first;
		this.secondeChirurgie = second;
		this.premiereChirurgie.setCorrige();
		this.secondeChirurgie.setCorrige();
	}

	public Chirurgie getPremiereChirurgie() {
		return this.premiereChirurgie;
	}

	public Chirurgie getSecondeChirurgie() {
		return this.secondeChirurgie;
	}

	public void visualiser() {
		System.out.print(this.getClass() + "\n" + this.premiereChirurgie);
		this.premiereChirurgie.visualisation();

		System.out.print(this.secondeChirurgie);
		this.secondeChirurgie.visualisation();

		System.out.println();
	}

	public void reordonner() {
		Chirurgie tmp = null;
		if (! this.getPremiereChirurgie().commenceAvant(this.getSecondeChirurgie())) {
			tmp = this.getPremiereChirurgie();
			this.premiereChirurgie = this.getSecondeChirurgie();
			this.secondeChirurgie = tmp;
		}
	}

	private double tauxSuperposition() {
		double dureeInter = this.getPremiereChirurgie().dureeIntersection(this.getSecondeChirurgie());
		double premierTaux = dureeInter / (double) this.getPremiereChirurgie().duree();
		double deuxiemeTaux = dureeInter / (double) this.getSecondeChirurgie().duree();

		// Prend le minimun des deux taux
		double resultat = (premierTaux > deuxiemeTaux) ? deuxiemeTaux : premierTaux;

		return resultat;
	}

    public void resoudreConflit(List<Chirurgien> lc, List<Salle> ls) {
		if (!this.persiste()) {		// Le conflit a pu etre resolu entre temps
			System.out.println("Le conflit entre les chirurgies " + this.getPremiereChirurgie().getId() + " et " + this.getSecondeChirurgie().getId() + " n'existe plus.");
			this.visualiser();
			return;
		}
		System.out.println("RESOLUTION DU CONFLIT entre " + this.getPremiereChirurgie().getId() + " et " + this.getSecondeChirurgie().getId() + " : ");
		System.out.println("Chirurgiens disponibles : \t" + lc + "\n" + "Salles dispnobles : \t\t" + ls);
		this.visualiser();

		// Nomalisation des deux chirurgies : de sorte a ce qu'elle ne commence plus
		// ou se termine a des horaires suspectes
		Correcteur.normaliserFin(this.getPremiereChirurgie());
		Correcteur.normaliserDebut(this.getSecondeChirurgie());

		// Resolution par modification des ressources
		// Les listes de chirurgiens/salles ne sont pas censees contenir le chirurgien / la salle a modifier !
		if (this.persiste() && this.ressourcesSuffisantes(lc, ls)) {
			System.out.println("----Modification de la ressource est possible");
			this.modifierChirurgie(lc, ls);
			Statistiques.plusModifRessource();
		} else {
			System.out.println("----Pas de modification de ressource envisageable");
		}

		// Resolution par decoupage
		double ts = this.tauxSuperposition();
		if (this.persiste() && this.tauxSuperposition() < 0.8
							&& (this.getPremiereChirurgie().dureeSuspecte() || this.getSecondeChirurgie().dureeSuspecte())
							&& (!this.getPremiereChirurgie().courte() || !this.getSecondeChirurgie().courte())) {
			System.out.println("----Decoupage des chirurgies");
			Correcteur.couperDuree(this.getPremiereChirurgie(), this.getSecondeChirurgie());
			Statistiques.plusDecoupe();
		} else {
			System.out.println("----Pas de decoupage de chirurgies");
		}

		// Resolution par decalage
		if (this.persiste()) {
			System.out.println("----Decalage d'une chirurgie");
			Correcteur.decalageChirurgie(this.getPremiereChirurgie(), this.getSecondeChirurgie());
			Statistiques.plusDecalage();
		} else {
			System.out.println("----Pas de decalage de chirurgie");
		}

		System.out.println("\nVoici le resultat final : ");
		this.visualiser();
		System.out.println();
	}

	@Override
	public String toString() {
		return this.getClass() + " -- " + this.premiereChirurgie + " avec " + this.secondeChirurgie;
	}
}
