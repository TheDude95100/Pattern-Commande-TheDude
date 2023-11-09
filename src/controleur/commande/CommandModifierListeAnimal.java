package controleur.commande;

import java.util.ArrayList;
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
	List<Animal>listeActuel;

	public CommandModifierListeAnimal(ANIMAL animalChoisi, double x, double y) {
		// TODO Auto-generated constructor stub
		this.animalChoisi = animalChoisi;
		this.x = x;
		this.y = y;
		this.listeActuel = new ArrayList<Animal>();
	}
	@Override
	public void executer() {
		// TODO Auto-generated method stub
		this.id = VuePimpMyHero.getInstance().ajouterAnimal(x, y, animalChoisi);
		Animal animal = new Animal(animalChoisi, x, y, id);
		if (Hero.getInstance().getAnimals()!=null) this.listeActuel = Hero.getInstance().getAnimals();
		listeActuel.add(animal);
		Hero.getInstance().setAnimals(listeActuel);
	}

	@Override
	public void annuler() {
		// TODO Auto-generated method stub
		VuePimpMyHero.getInstance().supprimerAsset(listeActuel.get(listeActuel.size()-1).getId());
		listeActuel.remove(listeActuel.size()-1);
		Hero.getInstance().setAnimals(listeActuel);
		
	}

}
