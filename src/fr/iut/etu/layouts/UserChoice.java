package fr.iut.etu.layouts;

import fr.iut.etu.Presenter;
import fr.iut.etu.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by shellcode on 11/23/16.
 */
public class UserChoice extends VBox {

    private Presenter presenter;

    Button button1 = new Button("Take");
    Button button2 = new Button("Guard");
    Button button3 = new Button("Guard without the dog");
    Button button4 = new Button("Guard against the dog");

    public UserChoice(Presenter presenter) {
        this.presenter = presenter;

        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("user-choice");

        setPadding(new Insets(20));
        setSpacing(50);
        setTranslateZ(-100);
        setPrefWidth(Presenter.SCREEN_WIDTH / 2);
        setMaxWidth(Presenter.SCREEN_WIDTH / 2);
        setPrefHeight(Presenter.SCREEN_HEIGHT / 2);
        setMaxHeight(Presenter.SCREEN_HEIGHT / 2);
        setTranslateX(Presenter.SCREEN_WIDTH / 4);
        setTranslateY(Presenter.SCREEN_HEIGHT / 4);
        setScaleX(Presenter.SCALE_COEFF);
        setScaleY(Presenter.SCALE_COEFF);

        double buttonWidth = Presenter.SCREEN_WIDTH / 5;
        double buttonHeight = Presenter.SCREEN_HEIGHT / 12;

        Label label = new Label("Make your choice :");
        label.getStyleClass().add("textMenu");
        label.setStyle("-fx-font-size: " + 30 * Presenter.SCALE_COEFF + "px;");

        button1.getStyleClass().add("button");
        button2.getStyleClass().add("button");
        button3.getStyleClass().add("button");
        button4.getStyleClass().add("button");

        button1.setPrefWidth(buttonWidth);
        button1.setPrefHeight(buttonHeight);
        button1.setMaxWidth(buttonWidth);
        button1.setMaxHeight(buttonHeight);
        button1.setStyle("-fx-font-size: " + 30 * Presenter.SCALE_COEFF + "px;");

        button2.setPrefWidth(buttonWidth);
        button2.setPrefHeight(buttonHeight);
        button2.setMaxWidth(buttonWidth);
        button2.setMaxHeight(buttonHeight);
        button2.setStyle("-fx-font-size: " + 30 * Presenter.SCALE_COEFF + "px;");

        button3.setPrefWidth(buttonWidth);
        button3.setPrefHeight(buttonHeight);
        button3.setMaxWidth(buttonWidth);
        button3.setMaxHeight(buttonHeight);
        button3.setStyle("-fx-font-size: " + 30 * Presenter.SCALE_COEFF + "px;");

        button4.setPrefWidth(buttonWidth);
        button4.setPrefHeight(buttonHeight);
        button4.setMaxWidth(buttonWidth);
        button4.setMaxHeight(buttonHeight);
        button4.setStyle("-fx-font-size: " + 30 * Presenter.SCALE_COEFF + "px;");


        button1.setOnMouseClicked(mouseEvent -> buttonClicked(button1));
        button2.setOnMouseClicked(mouseEvent -> buttonClicked(button2));
        button3.setOnMouseClicked(mouseEvent -> buttonClicked(button3));
        button4.setOnMouseClicked(mouseEvent -> buttonClicked(button4));

        getChildren().addAll(label, button1, button2, button3, button4);
    }

    public void buttonClicked(Button button) {

        Player.UserChoice choice = null;

        if(button == button1)
            choice = Player.UserChoice.TAKE;

        else if(button == button2)
            choice = Player.UserChoice.KEEP;

        else if(button == button3)
            choice = Player.UserChoice.KEEP_WITHOUT_DOG;

        else if(button == button4)
            choice = Player.UserChoice.KEEP_AGAINST_DOG;

        System.out.println("User choose: " + choice.toString());
        presenter.processUserChoice(choice);
    }
}
