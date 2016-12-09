package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by shellcode on 11/7/16.
 */
public class Menu extends StackPane {

    private Button playButton, rulesButton, settingsButton, aboutButton, exitButton;

    private final Controller controller;

    private Settings settings = null;
    private UserInput userInput = null;
    private Rules rules = null;
    private About about = new About();

    public Menu(Controller controller) throws IOException {
        this.controller = controller;

        settings = new Settings(controller);
        userInput = new UserInput(controller);
        rules = new Rules();

        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("background-menu");

        playButton = new Button("PLAY");
        rulesButton = new Button("RULES");
        settingsButton = new Button("SETTINGS");
        aboutButton = new Button("ABOUT");
        exitButton = new Button("EXIT");

        playButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        rulesButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        settingsButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        aboutButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        exitButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");


        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        playButton.setPrefWidth(buttonWidth);
        playButton.setPrefHeight(buttonHeight);
        playButton.setMaxWidth(buttonWidth);
        playButton.setMaxHeight(buttonHeight);

        rulesButton.setPrefWidth(buttonWidth);
        rulesButton.setPrefHeight(buttonHeight);
        rulesButton.setMaxWidth(buttonWidth);
        rulesButton.setMaxHeight(buttonHeight);

        settingsButton.setPrefWidth(buttonWidth);
        settingsButton.setPrefHeight(buttonHeight);
        settingsButton.setMaxWidth(buttonWidth);
        settingsButton.setMaxHeight(buttonHeight);

        aboutButton.setPrefWidth(buttonWidth);
        aboutButton.setPrefHeight(buttonHeight);
        aboutButton.setMaxWidth(buttonWidth);
        aboutButton.setMaxHeight(buttonHeight);

        exitButton.setPrefWidth(buttonWidth);
        exitButton.setPrefHeight(buttonHeight);
        exitButton.setMaxWidth(buttonWidth);
        exitButton.setMaxHeight(buttonHeight);

        playButton.setOnAction(this::buttonClicked);
        rulesButton.setOnAction(this::buttonClicked);
        settingsButton.setOnAction(this::buttonClicked);
        aboutButton.setOnAction(this::buttonClicked);
        exitButton.setOnAction(this::buttonClicked);

        VBox hbox = new VBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50);
        hbox.getChildren().addAll(playButton, rulesButton, settingsButton, aboutButton, exitButton);
        getChildren().add(hbox);
    }

    private void buttonClicked(ActionEvent e) {
        Button button = (Button)e.getSource();

        about.getBackgroundPsychedelic().stop(); //On stop l'animation quoi qu'il arrive si jamais elle est encore lancée après être allé dans le about

        if (button == playButton)
            controller.setLayout(userInput);

        else if(button == rulesButton)
            controller.setLayout(rules);

        else if (button == settingsButton)
            controller.setLayout(settings);

        else if(button == aboutButton) {
            about.getBackgroundPsychedelic().start();
            controller.setLayout(about);
        }

        else if (button == exitButton)
            System.exit(0);
    }
}
