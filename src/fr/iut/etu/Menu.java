package fr.iut.etu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Created by shellcode on 11/7/16.
 */
public class Menu extends Scene {

    private Button playButton, settingsButton, exitButton;

    private Controller controller;

    public Menu(Controller controller) throws IOException {
        super(FXMLLoader.load(controller.getClass().getResource("menu.fxml")), Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        this.controller = controller;

        playButton = (Button)lookup("#playButton");
        settingsButton = (Button)lookup("#settingsButton");
        exitButton = (Button)lookup("#exitButton");


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

        playButton.setTranslateX((Controller.SCREEN_WIDTH - buttonWidth) / 2);
        settingsButton.setTranslateX((Controller.SCREEN_WIDTH - buttonWidth) / 2);
        exitButton.setTranslateX((Controller.SCREEN_WIDTH - buttonWidth) / 2);

        playButton.setTranslateY((Controller.SCREEN_HEIGHT - buttonHeight) / 2 - 150);
        settingsButton.setTranslateY((Controller.SCREEN_HEIGHT - buttonHeight) / 2);
        exitButton.setTranslateY((Controller.SCREEN_HEIGHT - buttonHeight) / 2 + 150);

        playButton.setOnAction(e -> buttonClicked(e));
        settingsButton.setOnAction(e -> buttonClicked(e));
        exitButton.setOnAction(e -> buttonClicked(e));
    }

    public void buttonClicked(ActionEvent e)
    {
        Button button = (Button)e.getSource();

        if(button == playButton)
            controller.startGame();

        else if(button == settingsButton)
            settingsButton.setText("Not yet implemented");

        else if(button == exitButton) {
            System.exit(0);
        }
    }
}
