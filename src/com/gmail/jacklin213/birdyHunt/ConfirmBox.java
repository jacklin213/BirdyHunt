package com.gmail.jacklin213.birdyHunt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox extends Stage {
	
	Stage window;
	
	static final String DEFAULT_TITLE = "Confirm";
	static final String DEFAULT_MESSAGE = "Are you sure you want to do this?";
	
	boolean confirm = false;
	
	public ConfirmBox() {
		this(DEFAULT_TITLE, DEFAULT_MESSAGE);
	}
	
	public ConfirmBox(String message) {
		this(DEFAULT_TITLE, message);
	}
	
	public ConfirmBox(String title, String message) {
		window = this;

		//Item
		Label text = new Label(message);
		Button btnYes = new Button("Yes");
		Button btnNo = new Button("No");
		btnYes.setOnAction(event -> {
			confirm = true;
			window.close();
		});
		btnYes.defaultButtonProperty().bind(btnYes.focusedProperty());
		btnNo.setOnAction(event -> {
			confirm = false;
			window.close();
		});
		btnNo.defaultButtonProperty().bind(btnNo.focusedProperty());
		//Layout
		GridPane layout = new GridPane();
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(25, 25, 25, 25));
		
		layout.add(text, 0, 0, 2, 1);
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);
		hbox.getChildren().addAll(btnNo, btnYes);
		layout.add(hbox, 0, 1, 2, 1);
//		layout.add(btnYes, 1, 1);
//		layout.add(btnNo, 0, 1);
		//Scene
		Scene scene = new Scene(layout);
		//Stage
		this.setTitle(title);
		this.setScene(scene);
		//this.setMinWidth(250);
		this.setResizable(false);
		//AlertBox style bellow
		this.initModality(Modality.APPLICATION_MODAL);
		this.showAndWait();
	}
	
	public boolean getConfirm() {
		return confirm;
	}
}
