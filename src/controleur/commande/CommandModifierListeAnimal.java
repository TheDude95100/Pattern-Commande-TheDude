package controleur.commande;

import java.util.List;

import controleur.ControleurPimpMyHero;
import modele.Animal;
import modele.Assets;
import modele.Hero;
import modele.Animal.ANIMAL;
import vue.VuePimpMyHero;

public class CommandModifierListeAnimal extends Commande{
	ANIMAL animalChoisi;
	double x,y;
	String id;
	ControleurPimpMyHero controlleur;

	public CommandModifierListeAnimal(ANIMAL animalChoisi, double x, double y, ControleurPimpMyHero contoleur) {
		// TODO Auto-generated constructor stub
		this.animalChoisi = animalChoisi;
		this.x = x;
		this.y = y;
		this.controlleur = contoleur;
	}
	@Override
	public void executer() {
		// TODO Auto-generated method stub
		this.id = VuePimpMyHero.getInstance().ajouterAnimal(x, y, animalChoisi);
		Animal animal = new Animal(animalChoisi, x, y, id);
		this.controlleur.ajouterAnimal(animal);
		Hero.getInstance().setAnimals(controlleur.getListeAnimalActuel());
	}

	@Override
	public void annuler() {
		// TODO Auto-generated method stub
		controlleur.notifierSuppressionAsset(id);
	}

}
