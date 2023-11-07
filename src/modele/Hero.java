package modele;

import java.util.List;

import javafx.scene.paint.Color;
import utilitaire.Exportable;

public class Hero implements Exportable{

	public enum CASQUE {CASQUE1, CASQUE2, CASQUE3, CASQUE4, CASQUE5, }
	public enum ARMURE {ARMURE1, ARMURE2, ARMURE3, ARMURE4, ARMURE5, }
	public enum CAPE {CAPE1, CAPE2, CAPE3, CAPE4, CAPE5, }
	public enum BOTTES {BOTTES1, BOTTES2, BOTTES3, BOTTES4, BOTTES5}
	public enum BACKGROUND {BACKGROUND1, BACKGROUND2, BACKGROUND3, BACKGROUND4, BACKGROUND5}
	private CASQUE casqueActuel;
	private ARMURE armureActuelle;
	private CAPE capeActuelle;
	private BOTTES bottesActuelles;
	private BACKGROUND backgroundActuel;
	private String label;
	private Color couleurNom;
	private List<Animal> animals;
	
	private static Hero instance;
	
	
	public static Hero getInstance() {
		if(instance == null) {
		instance = new Hero();
		}
		return instance;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Color getCouleurNom() {
		return couleurNom;
	}
	public void setCouleurNom(Color couleurNom) {
		this.couleurNom = couleurNom;
	}
	public CASQUE getCasqueActuel() {
		return casqueActuel;
	}
	public void setCasqueActuel(CASQUE casqueActuel) {
		this.casqueActuel = casqueActuel;
	}
	public ARMURE getArmureActuelle() {
		return armureActuelle;
	}
	public void setArmureActuelle(ARMURE armureActuelle) {
		this.armureActuelle = armureActuelle;
	}
	public CAPE getCapeActuelle() {
		return capeActuelle;
	}
	public void setCapeActuelle(CAPE capeActuelle) {
		this.capeActuelle = capeActuelle;
	}
	public BOTTES getBottesActuelles() {
		return bottesActuelles;
	}
	public void setBottesActuelles(BOTTES bottesActuelles) {
		this.bottesActuelles = bottesActuelles;
	}
	public BACKGROUND getBackgroundActuel() {
		return backgroundActuel;
	}
	public void setBackgroundActuel(BACKGROUND backgroundActuel) {
		this.backgroundActuel = backgroundActuel;
	}

	public List<Animal> getAnimals() {
		return animals;
	}
	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	public void setAssetActuel(Assets.ASSETS asset, int id) {
		switch(asset) {
		case CASQUE:
			setCasqueActuel(CASQUE.valueOf("CASQUE" + id));
			break;
		case ARMURE:
			setArmureActuelle(ARMURE.valueOf("ARMURE" + id));
			break;
		case CAPE:
			setCapeActuelle(CAPE.valueOf("CAPE" + id));
			break;
		case BOTTES:
			setBottesActuelles(BOTTES.valueOf("BOTTES" + id));
			break;
		case BACKGROUND:
			setBackgroundActuel(BACKGROUND.valueOf("BACKGROUND" + id));
			break;
		}
	}

	public void enleverAssetActuel(Assets.ASSETS asset) {
		switch(asset) {
		case CASQUE:
			setCasqueActuel(null);
			break;
		case ARMURE:
			setArmureActuelle(null);
			break;
		case CAPE:
			setCapeActuelle(null);
			break;
		case BOTTES:
			setBottesActuelles(null);
			break;
		case BACKGROUND:
			setBackgroundActuel(null);
			break;
		}
	}
	@Override
	public String exporterJSON() {
		return null;
	}
	@Override
	public String exporterXML() {
		String xml = "";
		if(label != null)xml+="<label><titre>"+ label +"</titre>";
		if(couleurNom != null)xml+="<couleurNom>"+couleurNom.toString()+"</couleurNom></label>";
		if(couleurNom == null && label!= null )xml+="</label>";
		if(casqueActuel != null)xml+= "<casque>"+casqueActuel.toString()+"</casque>";
		if(armureActuelle != null)xml+="<armure>"+armureActuelle.toString()+"</armure>";
		if(capeActuelle != null)xml+="<cape>"+capeActuelle.toString()+"</cape>";
		if(bottesActuelles != null)xml+="<bottes>"+bottesActuelles.toString()+"</bottes>";
		if(backgroundActuel != null)xml+="<background>"+backgroundActuel.toString()+"</background>";
		if(animals != null) {
		xml+="<animals>";
		for (Animal animal : animals) {
			xml+="<animal>"+animal.toString()+"</animal>";
		}
		xml+="</animals>";
		}
		return xml;
	}
}
