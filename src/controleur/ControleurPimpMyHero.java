package controleur;

import architecture.Controleur;
import architecture.Vue;
import controleur.commande.Commande;
import controleur.commande.CommandeChangerBackground;
import javafx.scene.control.ColorPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;

import com.sun.media.jfxmedia.logging.Logger;
import javafx.geometry.Point2D;
import modele.Animal;
import modele.Assets;
import modele.Assets.ASSETS;
import modele.ElementChargable;
import modele.Hero;
import modele.Hero.ARMURE;
import modele.Hero.BACKGROUND;
import modele.Hero.BOTTES;
import modele.Hero.CAPE;
import modele.Hero.CASQUE;
import utilitaire.Chargeur;
import utilitaire.Exporteur;
import vue.VuePimpMyHero;


public class ControleurPimpMyHero extends Controleur {
	final int NOMBRE_CHOIX = 5;
	private Assets.ASSETS typeChoisi ;
	private Animal.ANIMAL animalChoisi;
	private List<Animal> listeAnimalActuel;
	private boolean isSuppressionActive = false;


    public ControleurPimpMyHero() {
        Logger.logMsg(Logger.INFO, "new ControleurPimpMyHero()");
    }

    public void initialiser() {
        Logger.logMsg(Logger.INFO, "ControleurPimpMyHero.initialiser()");
        listeAnimalActuel = new ArrayList<Animal>();

		Chargeur chargeur = new Chargeur();
		ArrayList<ElementChargable> elements = chargeur.chargerSauvegarde();

		if (elements == null) {
			VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.CASQUE, 1);
			VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.ARMURE, 1);
			VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.CAPE, 1);
			VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.BOTTES, 1);
			VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.BACKGROUND, 2);
			//changerBackgroundAleatoire();
			return;
		}

		for (ElementChargable element : elements) {
            System.out.println("element : " + element);

            ASSETS asset;
			int id = -1;
			String nom = null;
            if (!element.getType().equals("label")) {
                // Séparer le nom de l'id (l'id est un numéro à la fin)
                String[] nomId = element.getType().split("(?<=\\D)(?=\\d)");
                nom = nomId[0];
                id = Integer.parseInt(nomId[1]);
                System.out.println("nom : " + nom + " id : " + id);
                asset = ASSETS.valueOf(nom.toUpperCase());
            } else {
                // C'est un label
                VuePimpMyHero.getInstance().changerTitre(element.getTexte());
                VuePimpMyHero.getInstance().changerCouleurLabel(element.getCouleur());
                Hero.getInstance().setLabel(element.getTexte());
                Hero.getInstance().setCouleurNom(element.getCouleur());
				asset = ASSETS.LABEL;
            }

            if (asset != ASSETS.ANIMAL && asset != ASSETS.LABEL) {
                VuePimpMyHero.getInstance().changerAsset(asset, id);
                Hero.getInstance().setAssetActuel(asset, id);
            } else if (asset == ASSETS.ANIMAL) {
                // Je fais avec le code que j'ai ok c'est pas ma faute
                animalChoisi = Animal.ANIMAL.valueOf(nom.toUpperCase() + id);
                notifierAjoutAnimal(element.getX(), element.getY());
            } else {
                // C'est un label
				VuePimpMyHero.getInstance().changerTitre(element.getTexte());
				VuePimpMyHero.getInstance().changerCouleurLabel(element.getCouleur());

				VuePimpMyHero.getInstance().ecrireTitre(element.getTexte());
				VuePimpMyHero.getInstance().setCouleurSelectionnee(element.getCouleur());

				Hero.getInstance().setLabel(element.getTexte());
				Hero.getInstance().setCouleurNom(element.getCouleur());
            }
        }
    }

	public void changerBackgroundAleatoire() {
		Logger.logMsg(Logger.INFO, "ControleurPimpMyHero.changerBackgroundAleatoire()");
		int random = (int)(Math.random() * 5 + 1);
		VuePimpMyHero.getInstance().changerAsset(Assets.ASSETS.BACKGROUND,random);
		// Schedule the task to run in 3 seconds
		Timer timer = new Timer();
		timer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				changerBackgroundAleatoire();
			}
		}, 3000);
	}

	public Point2D getAssetPosition(Assets.ASSETS asset) {
		return Assets.getAssetPosition(asset);
	}

	public double getAssetSize(Assets.ASSETS asset) {
		return Assets.getAssetSize(asset);
	}
	protected Stack<Commande> historique = new Stack<Commande>();
	
    public void notifierSelectionBouton(int idBouton) {
    	//List<String> boutons = VuePimpMyHero.getInstance().getBoutons();
    	Logger.logMsg(Logger.INFO, "ControleurPimpMyHero.notifierBouton() - " + idBouton);
		String identifiantBouton = VuePimpMyHero.getInstance().getBoutons().get(idBouton);
    	VuePimpMyHero.getInstance().ajouterEffetPush(identifiantBouton);

		if (idBouton != 7) isSuppressionActive = false;

    	switch(idBouton) {
    	case 0:
    		//#bouton-selection-casque
    		gererAffichageItem(Assets.ASSETS.CASQUE);
    		break;
    	case 1:
    		//#bouton-selection-armure
    		gererAffichageItem(Assets.ASSETS.ARMURE);
    		break;
    	case 2:
    		//#bouton-selection-cape
    		gererAffichageItem(Assets.ASSETS.CAPE);
    		break;
    	case 3:
    		//#bouton-selection-bottes
    		gererAffichageItem(Assets.ASSETS.BOTTES);
    		break;
    	case 4:
    		//#bouton-selection-animal
    		gererAffichageItem(Assets.ASSETS.ANIMAL);
    		break;
    	case 5:
    		//#bouton-selection-background
    		gererAffichageItem(Assets.ASSETS.BACKGROUND);
    		break;
    	case 6 :
    		//#bouton-telechargement
    		sauvegarderHero();
    		break;
    	case 7 :
    		//#bouton-supprimer
			isSuppressionActive = !isSuppressionActive;
			if (!isSuppressionActive) VuePimpMyHero.getInstance().resetEffetPush();
    		break;
    	case 8 :
    		//#bouton-annuler
    		historique.pop().annuler();
    		break;
    	case 9 :
    		//#bouton-choix-1
    		changerItemChoisi(typeChoisi,1);
    		break;
    	case 10:
    		//#bouton-choix-2
    		changerItemChoisi(typeChoisi,2);
    		break;
    	case 11:
    		//bouton-choix-3
    		changerItemChoisi(typeChoisi,3);
    		break;
    	case 12:
    		//bouton-choix-4
    		changerItemChoisi(typeChoisi,4);
    		break;
    	case 13:
    		//bouton-choix-5
    		changerItemChoisi(typeChoisi,5);
    		break;
    	default:
    		break;
    	}	
    }


	private void gererAffichageItem(Assets.ASSETS typeItem) {
		Logger.logMsg(Logger.INFO, "ControleurPimpMyHero.gererAffichageItem()");
		typeChoisi = typeItem;
    	String itemChoisi = typeItem.toString().toLowerCase();
    	List<String> items = new ArrayList<String>();
    	
    	for (int i = 1; i <= NOMBRE_CHOIX;i++){
    		items.add(itemChoisi + i +".png");
		}
    	vue.VuePimpMyHero.getInstance().afficherListe(itemChoisi, items);
    }

    private void changerItemChoisi(Assets.ASSETS itemChoisi, int id) {
    	Logger.logMsg(Logger.INFO, "ControleurPimpMyHero.changerItemChoisi()");

    	switch(itemChoisi) {
    	case CASQUE:
    		Hero.getInstance().setCasqueActuel(CASQUE.valueOf("CASQUE" + id));
    		vue.VuePimpMyHero.getInstance().changerAsset(itemChoisi, id);
    		System.out.println("Hero.getInstance().getCasqueActuel() : " + Hero.getInstance().getCasqueActuel().toString());
    		break;
    		
    	case ARMURE:
    		Hero.getInstance().setArmureActuelle(ARMURE.valueOf("ARMURE" + id));
    		vue.VuePimpMyHero.getInstance().changerAsset(itemChoisi, id);
    		System.out.println("Hero.getInstance().getArmureActuelle() : " + Hero.getInstance().getArmureActuelle().toString());
    		break;
    		
    	case CAPE:
    		Hero.getInstance().setCapeActuelle(CAPE.valueOf("CAPE" + id));
    		vue.VuePimpMyHero.getInstance().changerAsset(itemChoisi, id);
    		System.out.println("Hero.getInstance().getCapeActuelle() : " + Hero.getInstance().getCapeActuelle().toString());
    		break;
    		
    	case BOTTES:
    		Hero.getInstance().setBottesActuelles(BOTTES.valueOf("BOTTES" + id));
    		vue.VuePimpMyHero.getInstance().changerAsset(itemChoisi, id);
    		System.out.println("Hero.getInstance().getBottesActuelles() : " + Hero.getInstance().getBottesActuelles().toString());
    		break;
    		
    	case BACKGROUND:
    		CommandeChangerBackground commande = new CommandeChangerBackground(id, itemChoisi);
    		commande.executer();
    		this.historique.add(commande);
    		System.out.println("Hero.getInstance().getBackgroundActuel() : " + Hero.getInstance().getBackgroundActuel().toString());
    		break;
    		
    	case ANIMAL:
    		switch(id) {
	    		case 1:
	    			animalChoisi = Animal.ANIMAL.ANIMAL1;
	    			break;
	    		case 2:
	    			animalChoisi = Animal.ANIMAL.ANIMAL2;
	    			break;
	    		case 3:
	    			animalChoisi = Animal.ANIMAL.ANIMAL3;
	    			break;
	    		case 4:
	    			animalChoisi = Animal.ANIMAL.ANIMAL4;
	    			break;
	    		case 5:
	    			animalChoisi = Animal.ANIMAL.ANIMAL5;
	    			break;	
	    	}
    		System.out.println("animalChoisi : " + animalChoisi);
    		break;
    	}
    }

	public void notifierAjoutAnimal(double x, double y) {
		Logger.logMsg(Logger.INFO, "notifierAjoutAnimal");
		if (animalChoisi != null && !isSuppressionActive) {
			String id = VuePimpMyHero.getInstance().ajouterAnimal(x, y, animalChoisi);
			listeAnimalActuel.add(new Animal(animalChoisi, x, y, id));
			Hero.getInstance().setAnimals(listeAnimalActuel);
		}
	}
    
	public void notifierSelectionColorPicker(ColorPicker cp) {
		Logger.logMsg(Logger.INFO, "notifierSelectionColorPicker");
		VuePimpMyHero.getInstance().changerCouleurLabel(cp.getValue());
		Hero.getInstance().setCouleurNom(cp.getValue());
	}

	public void notifierChangementTitre(String text) {
		Logger.logMsg(Logger.INFO, "notifierChangementTitre");
		VuePimpMyHero.getInstance().changerTitre(text);
		Hero.getInstance().setLabel(text);
	}
	
	public void sauvegarderHero() {
		Logger.logMsg(Logger.INFO, "sauvegarderHero()" );
		Exporteur exporter =  new Exporteur();
		exporter.sauvegarder(Hero.getInstance());

		Timer timer = new Timer();
		timer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				VuePimpMyHero.getInstance().resetEffetPush();
			}
		}, 500);
	}

	public void notifierSuppressionAsset(String idButton){
		Logger.logMsg(Logger.INFO, "notifierSuppressionAsset");
		if (!isSuppressionActive) return;

		// Enlever le "bouton-suppression" du idButton
		String idAsset = idButton.substring(19);
		System.out.println("idAsset : " + idAsset);
		VuePimpMyHero.getInstance().supprimerAsset(idButton);
		VuePimpMyHero.getInstance().supprimerAsset(idAsset);

		String nom = idAsset.split("-")[0];
		if (nom.equals("animal")) {
			listeAnimalActuel.removeIf(animal -> animal.getId().equals(idAsset));
			Hero.getInstance().setAnimals(listeAnimalActuel);
			return;
		}

		Hero.getInstance().enleverAssetActuel(Assets.ASSETS.valueOf(idAsset.toUpperCase()));
	}
}
