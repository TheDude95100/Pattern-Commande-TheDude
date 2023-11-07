package controleur.commande;

import modele.*;
import modele.Hero.BACKGROUND;
import vue.VuePimpMyHero;

public class CommandeChangerBackground extends Commande {

	private BACKGROUND ancienBackground;
	private BACKGROUND nouveauBackground;
	private Assets.ASSETS itemChoisi;
	private int id;
	
	public CommandeChangerBackground(int id, Assets.ASSETS itemChoisi) {
		this.ancienBackground = Hero.getInstance().getBackgroundActuel();
		this.nouveauBackground = BACKGROUND.valueOf("BACKGROUND" + id);
		this.itemChoisi = itemChoisi;
		this.id = id;
	}
	
	@Override
	public void executer() {
		Hero.getInstance().setBackgroundActuel(nouveauBackground);
		vue.VuePimpMyHero.getInstance().changerAsset(this.itemChoisi,this.id);
		System.out.println("Hero.getInstance().getBackgroundActuel() : " + Hero.getInstance().getBackgroundActuel().toString());
	}
	
	@Override
	public void annuler() {
		Hero.getInstance().setBackgroundActuel(ancienBackground);
		VuePimpMyHero.getInstance().changerAsset(this.itemChoisi, Integer.parseInt(ancienBackground.toString().substring(10)));
		nouveauBackground = ancienBackground;
	}
}
