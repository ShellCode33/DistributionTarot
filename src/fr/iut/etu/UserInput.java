package fr.iut.etu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by shellcode on 11/7/16.
 */
public class UserInput extends Scene {

    private GridPane gridLayout;
    private TextField userInput;
    private Button submitButton;

    private Controller controller;

    public UserInput(Controller controller) throws IOException {
        super(FXMLLoader.load(controller.getClass().getResource("user_input.fxml")), Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        this.controller = controller;

        gridLayout = (GridPane) lookup("#gridLayout");
        userInput = (TextField)lookup("#userInput");
        submitButton = (Button)lookup("#submitButton");

        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        submitButton.setPrefWidth(buttonWidth);
        submitButton.setPrefHeight(buttonHeight);
        submitButton.setMaxWidth(buttonWidth);
        submitButton.setMaxHeight(buttonHeight);

        submitButton.setTranslateX((Controller.SCREEN_WIDTH / 1.75));
        submitButton.setTranslateY(Controller.Y_SCREEN_START + (Controller.SCREEN_HEIGHT - buttonHeight) / 2);

        submitButton.setOnAction(e -> buttonClicked(e));

        userInput.setPrefWidth(buttonWidth*2);
        userInput.setMaxWidth(buttonWidth*2);
        userInput.setPrefHeight(buttonHeight);
        userInput.setMaxHeight(buttonHeight);
        userInput.setTranslateY(Controller.Y_SCREEN_START + (Controller.SCREEN_HEIGHT - buttonHeight) / 2);
        userInput.setTranslateX(Controller.SCREEN_WIDTH/2 - buttonWidth*1.75);


    }

    public void buttonClicked(ActionEvent e)
    {
        if(userInput.getText().isEmpty())
            userInput.setText("Computer42");

        controller.startGame(userInput.getText());
    }
}
