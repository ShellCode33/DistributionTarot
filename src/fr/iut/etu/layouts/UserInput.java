package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by shellcode on 11/7/16.
 */
class UserInput extends StackPane {

    private final TextField userInput;
    private ImageView selectedImage = null;

    private final Controller controller;

    public UserInput(Controller controller) {
        this.controller = controller;
        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("background-menu");

        // Les éléments de la VBox auront leur taille callée en fonction de la largeur et la hauteur des boutons
        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        VBox vbox = new VBox();
        vbox.getStyleClass().add("vboxStyle");
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(50);
        vbox.setPadding(new Insets(20));
        vbox.setScaleX(Controller.SCALE_COEFF);
        vbox.setScaleY(Controller.SCALE_COEFF);

        //Par défaut la vbox prend toute la place du StackPack, en définissant ses dimensions à 0 elle va s'adapter à la taille de ses fils
        vbox.setPrefWidth(0);
        vbox.setMaxWidth(0);
        vbox.setPrefHeight(0);
        vbox.setMaxHeight(0);

        GridPane gridLayout = new GridPane();
        gridLayout.setPadding(new Insets(10));
        gridLayout.setVgap(10);
        gridLayout.setHgap(10);

        userInput = new TextField();
        userInput.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        userInput.getStyleClass().add("textField");


        Button submitButton = new Button("Ok");
        submitButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        submitButton.getStyleClass().add("button");

        Label label1 = new Label("Pick an avatar :");
        Label label2 = new Label("Choose a username :");
        label1.getStyleClass().add("textMenu");
        label2.getStyleClass().add("textMenu");
        label1.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        label2.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");

        DropShadow border = new DropShadow( 30, Color.BLACK );

        int num_avatar = 1;
        for(int i = 0; i < 4;i++) {
            for(int j = 0; j < 5; j++) {
                Image img = new Image("file:res/avatars/avatar" + num_avatar++ + ".png");
                ImageView imgView = new ImageView(img);

                if(selectedImage == null) {
                    selectedImage = imgView;
                }

                imgView.setFitWidth(100);
                imgView.setFitHeight(100);
                imgView.setOnMouseClicked(mouseEvent -> {
                    selectedImage.setEffect(null);
                    selectedImage = imgView;
                    selectedImage.setEffect(border);
                });

                gridLayout.add(imgView, j, i);
            }
        }

        gridLayout.setPrefWidth(gridLayout.getChildren().get(0).getLayoutBounds().getWidth()*5+10*6);
        gridLayout.setMaxWidth(gridLayout.getChildren().get(0).getLayoutBounds().getWidth()*5+10*6);

        submitButton.setPrefWidth(buttonWidth);
        submitButton.setMaxWidth(buttonWidth);
        submitButton.setPrefHeight(buttonHeight);
        submitButton.setMaxHeight(buttonHeight);

        submitButton.setOnAction(this::buttonClicked);

        userInput.setPrefWidth(buttonWidth*2);
        userInput.setMaxWidth(buttonWidth*2);
        userInput.setPrefHeight(buttonHeight/1.5);
        userInput.setMaxHeight(buttonHeight/1.5);

        userInput.setOnAction(this::buttonClicked);

        vbox.getChildren().addAll(label1, gridLayout, label2, userInput, submitButton);
        getChildren().add(vbox);
    }

    private void buttonClicked(ActionEvent e)
    {
        if(userInput.getText().isEmpty())
            userInput.setText("User42");
        controller.initPlayersModelAndView(userInput.getText(), selectedImage.getImage());
        controller.prepareDeal();
    }
}
