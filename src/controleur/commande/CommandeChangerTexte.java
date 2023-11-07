package controleur.commande;

import javafx.scene.paint.Color;
import modele.Hero;
import vue.VuePimpMyHero;

public class CommandeChangerTexte extends Commande{

	private Color ancienneCouleur;
	private Color nouvelleCouleur;
	
	
	public CommandeChangerTexte(Color couleur) {
		this.nouvelleCouleur = couleur;
		if(Hero.getInstance().getCouleurNom() != null) {
			this.ancienneCouleur = Hero.getInstance().getCouleurNom();
		}
	}
	@Override
	public void executer() {
		// TODO Auto-generated method stub
		VuePimpMyHero.getInstance().changerCouleurLabel(this.nouvelleCouleur);
		Hero.getInstance().setCouleurNom(this.nouvelleCouleur);
	}

	@Override
	public void annuler() {
		// TODO Auto-generated method stub
		if(ancienneCouleur != null) {
		VuePimpMyHero.getInstance().changerCouleurLabel(this.ancienneCouleur);
		Hero.getInstance().setCouleurNom(this.ancienneCouleur);	
		}

	}

}
