package vue;

import architecture.Vue;

import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;
import controleur.ControleurPimpMyHero;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import modele.Animal.ANIMAL;
import modele.Assets;
import org.w3c.dom.Text;

public class VuePimpMyHero extends Vue {

    protected ControleurPimpMyHero controleur;
    protected static VuePimpMyHero instance = null;
    protected List<String> boutons;
    protected ColorPicker cp;
    protected ImageView pushedBouton;
    protected int compteurAnimaux = 0;

    public static VuePimpMyHero getInstance() {
        if (null == instance) instance = new VuePimpMyHero();
        return VuePimpMyHero.instance;
    }


    private VuePimpMyHero() {
        super("personnage.fxml", VuePimpMyHero.class, 1294, 743);
        super.controleur = this.controleur = new ControleurPimpMyHero();
        Logger.logMsg(Logger.INFO, "new VuePimpMyHero()");
        boutons = new ArrayList<String>();
        boutons.add("#bouton-selection-casque");
        boutons.add("#bouton-selection-armure");
        boutons.add("#bouton-selection-cape");
        boutons.add("#bouton-selection-bottes");
        boutons.add("#bouton-selection-animal");
        boutons.add("#bouton-selection-background");
        boutons.add("#bouton-telechargement");
        boutons.add("#bouton-supprimer");
        boutons.add("#bouton-annuler");
        for (int boutonChoix = 1; boutonChoix < 6; boutonChoix++) {
            boutons.add("#bouton-choix-" + boutonChoix);
        }
        cp = (ColorPicker) lookup("#colorpicker");
    }

    public List<String> getBoutons() {
        return boutons;
    }

    public void activerControles() {
        super.activerControles();

        for (String i : boutons) {
            activerBouton(boutons.indexOf(i));
        }
        activerCP(cp);

        TextField titre = (TextField) lookup("#titre");
        titre.setOnKeyReleased((EventHandler<KeyEvent>) new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                System.out.println("MAJ titre");
                controleur.notifierChangementTitre(titre.getText());
            }
        });


        ImageView jardin = (ImageView) lookup("#background");
        jardin.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent clic) {
                double x = clic.getX();
                double y = clic.getY();
                System.out.println("Clic pour ajouter un animal effectue aux coordonnée : (" + x + " , " + y + ")");
                controleur.notifierAjoutAnimal(x, y);
            }
        });
    }

    public void changerAsset(Assets.ASSETS asset, int elementId) {
        Logger.logMsg(Logger.INFO, "Changer " + asset.toString().toLowerCase() + ": " + elementId);
        String assetString = asset.toString().toLowerCase();

        //Récupérer l'asset s'il existe
        try {
            ImageView assetImage = (ImageView) lookup("#" + assetString);
            assetImage.setImage(new Image("vue/images/" + assetString + "/" + assetString + elementId + ".png"));
            Logger.logMsg(Logger.INFO, "L'asset a été changé");
        } catch (NullPointerException e) {
            Logger.logMsg(Logger.INFO, "L'asset n'existe pas : ajout");
            ajouterAsset(asset, elementId);
        }
    }

    public void ajouterAsset(Assets.ASSETS asset, int elementId) {
        Logger.logMsg(Logger.INFO, "Ajouter " + asset.toString().toLowerCase() + ": " + elementId);
        String assetString = asset.toString().toLowerCase();

        //Création de l'image
        ImageView assetImage = new ImageView();
        assetImage.setImage(new Image("vue/images/" + assetString + "/" + assetString + elementId + ".png"));

        //Récupérer le conteneur
        AnchorPane conteneur = (AnchorPane) lookup("#terrain-de-creation");

        //Changer la taille de l'image
        assetImage.preserveRatioProperty().set(true);
        assetImage.setFitWidth(controleur.getAssetSize(asset));

        //Taille max disponible en X et Y
        double maxX = conteneur.getWidth() - assetImage.getFitWidth();
        double maxY = conteneur.getHeight() - assetImage.getFitHeight();
        System.out.println("maxX: " + maxX + " maxY: " + maxY);

        double assetPosX = controleur.getAssetPosition(asset).getX();
        double assetPosY = controleur.getAssetPosition(asset).getY();

        if (assetPosX > maxX) assetPosX = maxX;
        if (assetPosY > maxY) assetPosY = maxY;

        //Déplacer l'image
        if (assetPosX < 0) // Valeur négative = centré
            assetImage.xProperty().bind(conteneur.widthProperty().subtract(assetImage.fitWidthProperty()).divide(2));
        else
            assetImage.setX(assetPosX);

        if (assetPosY < 0)
            assetImage.yProperty().bind(conteneur.heightProperty().subtract(assetImage.fitHeightProperty()).divide(2));
        else
            assetImage.setY(assetPosY);

        assetImage.setId(assetString);
        conteneur.getChildren().add(assetImage);

        if (assetString.equals("background")) recouperBackground();

        // Créer un bouton de la taille de l'image
        Button boutonSuppression = creerBoutonSuppression(asset, assetImage.getX(), assetImage.getY());
        conteneur.getChildren().add(boutonSuppression);
        activerBoutonSuppression(boutonSuppression.getId());

        reorganiserLayers();
    }

    public Button creerBoutonSuppression(Assets.ASSETS asset, double posX, double posY) {
        System.out.println("creerBoutonSuppression1 : " + asset);
        String idAsset = asset.toString().toLowerCase();
        return creerBoutonSuppression(idAsset, posX, posY);
    }

    public Button creerBoutonSuppression(String idAsset, double posX, double posY) {
        System.out.println("creerBoutonSuppression2 : " + idAsset);
        // On prend tout ce qui est avant le premier tiret
        String nomAsset = idAsset.split("-")[0];
        Assets.ASSETS asset = Assets.ASSETS.valueOf(nomAsset.toUpperCase());

        Button boutonSuppression = new Button();
        boutonSuppression.setPrefSize(controleur.getAssetSize(asset), controleur.getAssetSize(asset));
        boutonSuppression.setLayoutX(posX);
        boutonSuppression.setLayoutY(posY);
        boutonSuppression.setId("bouton-suppression-" + idAsset);
        boutonSuppression.setStyle("-fx-background-color: transparent;");
        return boutonSuppression;
    }

    public void recouperBackground() {
        ImageView imageView = (ImageView) lookup("#background");
        Pane imagePane = (Pane) lookup("#terrain-de-creation");

        // Calculer la position horizontale pour centrer le viewport
        double viewportX = (imageView.getImage().getWidth() - imagePane.getWidth()) / 2;
        imageView.viewportProperty().setValue(new Rectangle2D(viewportX, 0, imagePane.getWidth(), imagePane.getHeight()));
    }

    public void reorganiserLayers() {
        System.out.println("reorganiserLayers");
        for (Assets.ASSETS asset : Assets.ASSETS.values()) {
            String assetString = asset.toString().toLowerCase();

            //Récupérer l'asset s'il existe
            try {
                if (assetString.equals("label")) {
                    Label label = (Label) lookup("#" + assetString);
                    label.toFront();
                }
                else if (assetString.equals("animal")) {
                    // Récupérer tous les élements aillant pour id "animal-"
                    AnchorPane conteneur = (AnchorPane) lookup("#terrain-de-creation");
                    List<Node> animaux = new ArrayList<Node>();
                    for (Node child : conteneur.getChildren()) {
                        if (child.getId().startsWith("animal-")) {
                            System.out.println("reorganiserLayers/animal : " + child.getId());
                            animaux.add(child);
                        }
                    }
                    for (Node animal : animaux) {
                        animal.toFront();
                        Button bouton = (Button) lookup("#bouton-suppression-" + animal.getId());
                        bouton.toFront();
                    }
                }
                else {
                    ImageView assetImage = (ImageView) lookup("#" + assetString);
                    assetImage.toFront();
                }
                Button bouton = (Button) lookup("#bouton-suppression-" + assetString);
                bouton.toFront();
            } catch (NullPointerException | ClassCastException e) {
                //Logger.logMsg(Logger.INFO, "L'asset n'existe pas");
            }
        }
    }

    public void redimensionnerAsset(Assets.ASSETS asset, double width) {
        Logger.logMsg(Logger.INFO, "Resize " + asset.toString().toLowerCase() + ": " + width);
        String assetString = asset.toString().toLowerCase();

        //Récupérer l'asset s'il existe
        try {
            ImageView assetImage = (ImageView) lookup("#" + assetString);
            assetImage.preserveRatioProperty().set(true);
            assetImage.setFitWidth(width);
            Logger.logMsg(Logger.INFO, "L'asset a été redimensionné");
        } catch (NullPointerException e) {
            Logger.logMsg(Logger.INFO, "L'asset n'existe pas");
        }
    }

    public void supprimerAsset(String id) {
        System.out.println("supprimerAsset : " + id);
        //Récupérer l'asset s'il existe

        AnchorPane conteneur = (AnchorPane) lookup("#terrain-de-creation");
        Node aSupprimer = lookup("#" + id);

        if (aSupprimer == null) {
            System.err.println("supprimerAsset : " + id + " n'a pas été trouvé");
            return;
        }
        else {
            System.out.println("supprimerAsset : " + id + " a été trouvé : " + aSupprimer);
        }

        conteneur.getChildren().remove(aSupprimer);

        aSupprimer = lookup("#" + id);
        if (aSupprimer != null) System.err.println("supprimerAsset : " + id + " n'a pas été supprimé");
        else System.out.println("supprimerAsset : " + id + " a été supprimé");
    }

    private void activerBouton(int idBouton) {
        Button bouton = (Button) lookup(boutons.get(idBouton));
        bouton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("activerBouton : Clic sur " + boutons.get(idBouton));
                controleur.notifierSelectionBouton(idBouton);
            }
        });
    }

    private void activerCP(ColorPicker cp) {
        cp.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Clic sur " + cp.getId());
                controleur.notifierSelectionColorPicker(cp);
                System.out.println("La couleur c'est" + cp.getValue());
            }
        });
    }

    private void activerBoutonSuppression(String idBouton) {
        System.out.println("activerBoutonSuppression : " + idBouton);
        Button bouton = (Button) lookup("#" + idBouton);
        bouton.setOnAction(event -> {
            System.out.println("activerBoutonSuppression : Clic sur " + bouton.getId());
            controleur.notifierSuppressionAsset(idBouton);
        });
    }


    public void afficherListe(String assetString, List<String> items) {
        Logger.logMsg(Logger.INFO, "VuePimpMyHero.afficherListe()");

        for (String i : items) {
            int numeroItem = items.indexOf(i);
            try {
                ImageView assetImage = (ImageView) lookup("#image-choix-" + (numeroItem + 1));
                assetImage.setImage(new Image("vue/images/" + assetString + "/" + items.get(numeroItem)));
                Logger.logMsg(Logger.INFO, "Image affiché");
            } catch (NullPointerException e) {
                Logger.logMsg(Logger.INFO, "Image introuvable");
            }

        }
    }


    public void changerTitre(String text) {
        Label label = (Label)lookup("#label");
        label.setText(text);
    }

    public void changerCouleurLabel(Color value) {
        Label label = (Label)lookup("#label");
        label.setTextFill(value);
    }

	public String ajouterAnimal(double x, double y, ANIMAL animalChoisi) {
		
		ImageView animalAjoute = new ImageView();
		
		switch (animalChoisi) {
			case ANIMAL1:
				animalAjoute.setImage(new Image("vue/images/animal/animal1.png"));
				break;
			case ANIMAL2:
				animalAjoute.setImage(new Image("vue/images/animal/animal2.png"));
				break;
			case ANIMAL3:
				animalAjoute.setImage(new Image("vue/images/animal/animal3.png"));
				break;
			case ANIMAL4:
				animalAjoute.setImage(new Image("vue/images/animal/animal4.png"));
				break;	
			case ANIMAL5:
				animalAjoute.setImage(new Image("vue/images/animal/animal5.png"));
				break;	
		}

		animalAjoute.setPreserveRatio(true);
		animalAjoute.setFitHeight(Assets.getAssetSize(Assets.ASSETS.ANIMAL));
		animalAjoute.setX(x - 10);
		animalAjoute.setY(y - 15);
        animalAjoute.setId("animal-" + compteurAnimaux++);
        System.out.println("animalAjoute.getId() : " + animalAjoute.getId());

		AnchorPane terrain = (AnchorPane)lookup("#terrain-de-creation");
		terrain.getChildren().add(animalAjoute);

        Button boutonSuppression = creerBoutonSuppression(animalAjoute.getId(), animalAjoute.getX(), animalAjoute.getY());
        terrain.getChildren().add(boutonSuppression);
        System.out.println("boutonSuppression.getId() : " + boutonSuppression.getId());
        activerBoutonSuppression(boutonSuppression.getId());

        reorganiserLayers();

        return animalAjoute.getId();
	}

    public void ajouterEffetPush(String id) {
        if (pushedBouton != null) pushedBouton.setOpacity(1);

        Button bouton = (Button)lookup(id);
        Pane parent = (Pane) bouton.getParent();
        List<Node> children = parent.getChildren();
        for (Node child : children) {
            if (child instanceof ImageView imageView) {
                imageView.setOpacity(0.5);
                pushedBouton = imageView;
            }
        }
    }

    public void resetEffetPush() {
        if (pushedBouton != null) pushedBouton.setOpacity(1);
    }

    public void ecrireTitre(String texte) {
        TextField titre = (TextField) lookup("#titre");
        titre.setText(texte);
    }

    public void setCouleurSelectionnee(Color couleur) {
        ColorPicker cp = (ColorPicker) lookup("#colorpicker");
        cp.setValue(couleur);
    }
}


