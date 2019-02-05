import java.time.Duration;
import java.util.List;

public class Ubiquite extends Conflit {

	

	public Ubiquite(Chirurgie first, Chirurgie second) {
		super(first, second);
  	}

	@Override
	public void resoudreConflit( List<Chirurgien> lc, List<Salle> ls) {
		//LocalDate ld = super.getPremiereChirurgie().getDatesOperation().getDateDebut().toLocalDate();
		Chirurgien tmpChirurgien = null;
		if(lc.size() == 1) {
			//Deplace les horaires
			long duree = this.getSecondeChirurgie().duree();
			long dureeChevauchement = Duration.between(this.getPremiereChirurgie().getDatesOperation().getDateFin(), this.getSecondeChirurgie().getDatesOperation().getDateDebut()).toMinutes();
			// heure fin first - heure debut seconde
			Correcteur.translater(getSecondeChirurgie(), dureeChevauchement + 15);
			
		}else {
			//Change de chirurgien
			for(Chirurgien c : lc) {
				if(!this.getPremiereChirurgie().getChirurgien().equals(c)) {
					tmpChirurgien = c;
					break;
				}
			}
			Correcteur.changerChirurgien(getSecondeChirurgie(), tmpChirurgien);
		}
		
	}

	
	
	
	/**
	 * Modifier le chirurgien
	 * Modifier l'heure
	 */
}
