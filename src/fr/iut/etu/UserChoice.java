package fr.iut.etu;

import fr.iut.etu.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by shellcode on 11/23/16.
 */
public class UserChoice extends Group {

    VBox vbox;
    private Controller controller;

    public UserChoice(Controller controller) {

        this.controller = controller;

        try {
            vbox = FXMLLoader.load(getClass().getResource("user_choice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTranslateZ(-100);
        vbox.setPrefWidth(Controller.SCREEN_WIDTH / 2);
        vbox.setMaxWidth(Controller.SCREEN_WIDTH / 2);
        vbox.setPrefHeight(Controller.SCREEN_HEIGHT / 2);
        vbox.setMaxHeight(Controller.SCREEN_HEIGHT / 2);
        setTranslateX(Controller.SCREEN_WIDTH / 4);
        setTranslateY(Controller.SCREEN_HEIGHT / 4);

        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        Button button1 = (Button)vbox.getChildren().get(1);
        Button button2 = (Button)vbox.getChildren().get(2);
        Button button3 = (Button)vbox.getChildren().get(3);
        Button button4 = (Button)vbox.getChildren().get(4);

        button1.setPrefWidth(buttonWidth);
        button1.setPrefHeight(buttonHeight);
        button1.setMaxWidth(buttonWidth);
        button1.setMaxHeight(buttonHeight);

        button2.setPrefWidth(buttonWidth);
        button2.setPrefHeight(buttonHeight);
        button2.setMaxWidth(buttonWidth);
        button2.setMaxHeight(buttonHeight);

        button3.setPrefWidth(buttonWidth);
        button3.setPrefHeight(buttonHeight);
        button3.setMaxWidth(buttonWidth);
        button3.setMaxHeight(buttonHeight);

        button4.setPrefWidth(buttonWidth);
        button4.setPrefHeight(buttonHeight);
        button4.setMaxWidth(buttonWidth);
        button4.setMaxHeight(buttonHeight);

        Button finalButton = button1;
        Button finalButton1 = button2;
        Button finalButton2 = button3;
        Button finalButton3 = button4;
        button1.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton));
        button2.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton1));
        button3.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton2));
        button4.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton3));

        getChildren().add(vbox);
    }

    public void buttonClicked(Button button) {

        Player.UserChoice choice = null;
        String id = button.getId();

        if(id.equals("button1"))
            choice = Player.UserChoice.TAKE;
        else if(id.equals("button2"))
            choice = Player.UserChoice.KEEP;
        else if(id.equals("button3"))
            choice = Player.UserChoice.KEEP_WITHOUT_DOG;
        else if(id.equals("button4"))
            choice = Player.UserChoice.KEEP_AGAINST_DOG;

        System.out.println("User choose: " + choice.toString());
        controller.processUserChoice(0, choice);
    }
}
