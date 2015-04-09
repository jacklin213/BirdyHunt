package com.gmail.jacklin213.birdyHunt;
	
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class BirdyHunt extends Application {
	
	final static int WIDTH = 464;
	final static int HEIGHT = 600;
	
	final static Image IMG_BACKGROUND = new Image(BirdyHunt.class.getResource("bg_4.jpg").toString());
	final static Image IMG_BG_1 = new Image(BirdyHunt.class.getResource("bg_4_1.png").toString());
	final static Image IMG_BG_2 = new Image(BirdyHunt.class.getResource("bg_4_2.png").toString());
	final static Image IMG_BG_3 = new Image(BirdyHunt.class.getResource("bg_4_3.png").toString());
	
	final static Image IMG_BIRD_1 = new Image(BirdyHunt.class.getResource("bird_1.png").toString());
	final static Image IMG_BIRD_2 = new Image(BirdyHunt.class.getResource("bird_2.png").toString());
	final static Image IMG_BIRD_3 = new Image(BirdyHunt.class.getResource("bird_3.png").toString());
	
	final static Image IMG_EXPLOSION_1 = new Image(BirdyHunt.class.getResource("explode_1.png").toString());
	final static Image IMG_EXPLOSION_2 = new Image(BirdyHunt.class.getResource("explode_2.png").toString());
	final static Image IMG_EXPLOSION_3 = new Image(BirdyHunt.class.getResource("explode_3.png").toString());
	
	ImageView background = new ImageView(IMG_BACKGROUND);
	ImageView bg_1 = new ImageView(IMG_BG_1);
	ImageView bg_2 = new ImageView(IMG_BG_2);
	ImageView bg_3 = new ImageView(IMG_BG_3);
	
	ImageView bird1 = new ImageView(IMG_BIRD_1);
	ImageView bird2 = new ImageView(IMG_BIRD_2);
	ImageView bird3 = new ImageView(IMG_BIRD_3);
	
	ImageView explosion1 = new ImageView(IMG_EXPLOSION_1);
	ImageView explosion2 = new ImageView(IMG_EXPLOSION_2);
	ImageView explosion3 = new ImageView(IMG_EXPLOSION_3);
	
	private Animation current, hitAnimation;
	private Group bird, waves, explosion;
	private IntegerProperty hitCounter = new SimpleIntegerProperty(this, "hitCounter");
	private IntegerProperty shotCounter = new SimpleIntegerProperty(this, "shotCounter");
	private Random random = new Random();
	private Text hitLabel = new Text();
	private Text shotLabel = new Text();
	private Text missesLabel = new Text();
	
	@Override
	public void start(Stage window) {
		bird = new Group(bird1);
		SequentialTransition sequential = new SequentialTransition();
		sequential.setNode(bird);
			RotateTransition rotate = new RotateTransition();
			rotate.setFromAngle(0);
			rotate.setToAngle(1260);
			rotate.setDuration(Duration.millis(800));
			
			TranslateTransition translate = new TranslateTransition();
			translate.setByY(1000);
			translate.setDuration(Duration.millis(800));
		sequential.getChildren().addAll(rotate, translate);
		hitAnimation = sequential;
		hitAnimation.setOnFinished(event -> startAnimation());
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(200), event -> bird.getChildren().setAll(bird2)),
				new KeyFrame(Duration.millis(300), event -> bird.getChildren().setAll(bird3)),
				new KeyFrame(Duration.millis(500), event -> bird.getChildren().setAll(bird1))
		);
		timeline.play();
		
		waves = new Group(background);
		Timeline wavesTimeline = new Timeline();
		wavesTimeline.setCycleCount(Animation.INDEFINITE);
		wavesTimeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(200), event -> waves.getChildren().setAll(bg_1)),
				new KeyFrame(Duration.millis(400), event -> waves.getChildren().setAll(bg_2)),
				new KeyFrame(Duration.millis(600), event -> waves.getChildren().setAll(bg_3)),
				new KeyFrame(Duration.millis(800), event -> waves.getChildren().setAll(background))
		);
		wavesTimeline.play();
		
		hitLabel.textProperty().bind(Bindings.concat("Hits: ", hitCounter));
		shotLabel.textProperty().bind(Bindings.concat("Shot: ", shotCounter));
		missesLabel.textProperty().bind(Bindings.concat("Misses: ", shotCounter.subtract(hitCounter)));
		VBox hud = new VBox(hitLabel, shotLabel, missesLabel);
		hud.setTranslateX(20);
		hud.setTranslateY(20);
		
		explosion = new Group(explosion1);
		Group layout = new Group(new VBox(getMenuBar(window), waves), bird, hud);
		//VBox layout = new VBox(getMenuBar(), content);
		Scene scene = new Scene(layout, WIDTH, HEIGHT);
		scene.setOnMouseClicked(event -> shotCounter.set(shotCounter.get() + 1));
		
		bird.setOnMouseClicked(event -> {
			current.stop();
			hitAnimation.play();
			getExplosion().play();
			hitCounter.set(hitCounter.get() + 1);
		});
		
		window.setOnCloseRequest(event -> {
			event.consume();
			onDisable();
		});
		window.setTitle("FirstGame");
		window.setScene(scene);
		window.show();
		
		startAnimation();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Timeline getExplosion() {
		Timeline explosionTimeline = new Timeline();
		explosionTimeline.setCycleCount(1);
		explosionTimeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(200), event -> explosion.getChildren().setAll(explosion2)),
				new KeyFrame(Duration.millis(400), event -> explosion.getChildren().setAll(explosion3)),
				new KeyFrame(Duration.millis(600), event -> explosion.getChildren().setAll(explosion1))
		);
		return explosionTimeline;
	}
	
	private void startAnimation() {
		if (current != null) {
			current.stop();
		}
		
		int y0 = random.nextInt(HEIGHT/2) + HEIGHT/4;
		int y1 = random.nextInt(HEIGHT/2) + HEIGHT/4;

		bird.setRotate(0);
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(bird);
		transition.setFromX(-100);
		transition.setToX(WIDTH);
		transition.setFromY(y0);
		transition.setToY(y1);
		transition.setDuration(Duration.seconds(1.5));
		transition.setOnFinished(event -> startAnimation());
		
		current = transition;
		current.play();
	}

	private MenuBar getMenuBar(Stage window) {
		MenuBar menuBar = new MenuBar();
		//menuBar.prefWidthProperty().bind(window.widthProperty());
		
		Menu menuFile = new Menu("File");
		MenuItem itemNew = new MenuItem("New Game");
		itemNew.setAccelerator(KeyCombination.keyCombination("F2"));
		//itemNew.setOnAction(event -> newGame());
		MenuItem itemExit = new MenuItem("Exit");
		itemExit.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
		itemExit.setOnAction(event -> onDisable());
		menuFile.getItems().addAll(itemNew, new SeparatorMenuItem(), itemExit);
		
		menuBar.getMenus().add(menuFile);
		
		return menuBar;
	}
	
	public void onDisable() {
		ConfirmBox confirmBox = new ConfirmBox("Exit", "Are you sure you want to exit?");
		if (confirmBox.getConfirm()) {
			//Do stuff
//			window.close();
			Platform.exit();
		}
	}
}