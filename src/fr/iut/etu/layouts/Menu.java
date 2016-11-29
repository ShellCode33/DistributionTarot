package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;

/**
 * Created by shellcode on 11/7/16.
 */
public class Menu extends StackPane {

    private Button playButton, settingsButton, exitButton;

    private Controller controller;

    public Menu(Controller controller) throws IOException {
        this.controller = controller;

        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("background-menu");

        playButton = new Button("PLAY");
        settingsButton = new Button("SETTINGS");
        exitButton = new Button("EXIT");

        Font font = new Font(30 * Controller.SCALE_COEFF);
        playButton.setFont(font);
        settingsButton.setFont(font);
        exitButton.setFont(font);


        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        playButton.setPrefWidth(buttonWidth);
        playButton.setPrefHeight(buttonHeight);
        playButton.setMaxWidth(buttonWidth);
        playButton.setMaxHeight(buttonHeight);

        settingsButton.setPrefWidth(buttonWidth);
        settingsButton.setPrefHeight(buttonHeight);
        settingsButton.setMaxWidth(buttonWidth);
        settingsButton.setMaxHeight(buttonHeight);

        exitButton.setPrefWidth(buttonWidth);
        exitButton.setPrefHeight(buttonHeight);
        exitButton.setMaxWidth(buttonWidth);
        exitButton.setMaxHeight(buttonHeight);

        playButton.setOnAction(this::buttonClicked);
        settingsButton.setOnAction(this::buttonClicked);
        exitButton.setOnAction(this::buttonClicked);

        VBox hbox = new VBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50);
        hbox.getChildren().addAll(playButton, settingsButton, exitButton);
        getChildren().add(hbox);
    }

    private void buttonClicked(ActionEvent e) {
        Button button = (Button)e.getSource();

        try {
            if (button == playButton) {
                controller.setLayout(new UserInput(controller));

            }

            else if (button == settingsButton)
                controller.setLayout(new Settings(controller));

            else if (button == exitButton) {
                System.exit(0);
            }
        }

        catch(IOException e1) {
            e1.printStackTrace();
        }
    }
}
