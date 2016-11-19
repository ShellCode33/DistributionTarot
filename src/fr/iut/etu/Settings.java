package fr.iut.etu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by shellcode on 11/19/16.
 */
public class Settings extends Scene {

    Controller controller;
    ImageView selectedBackground = null;
    ImageView selectedBackCard = null;

    public Settings(Controller controller) throws IOException {
        super(FXMLLoader.load(controller.getClass().getResource("settings.fxml")), Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        this.controller = controller;

        VBox vbox = (VBox)lookup("#vbox-userinput");
        vbox.setTranslateX((Controller.SCREEN_WIDTH - vbox.getWidth()) / 2);

        HBox backgroundsContainer = (HBox)lookup("#backgrounds-container");
        HBox backcardsContainer = (HBox)lookup("#backcards-container");

        Image image = null;
        ImageView imageView = null;
        DropShadow border = new DropShadow( 30, Color.BLACK );

        int i = 0;

        do {
            image = new Image("file:res/backgrounds/background_board" + i++ + ".jpg");

            if(!image.isError()) {
                imageView = new ImageView(image);

                if(selectedBackground == null) {
                    selectedBackground = imageView;
                    selectedBackground.setEffect(border);
                }

                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                ImageView finalImageView = imageView;
                int finalI = i - 1;
                imageView.setOnMouseClicked(mouseEvent -> {
                    selectedBackground.setEffect(null);
                    selectedBackground = finalImageView;
                    selectedBackground.setEffect(border);
                    vbox.setStyle("-fx-background-image: url(\"file:res/backgrounds/background_board" + finalI + ".jpg\");");
                });

                if(i == 1)
                    selectedBackground = imageView;

                backgroundsContainer.getChildren().add(imageView);
            }

        } while(!image.isError()); //On charge toutes les images du répertoire de 0 à n

        i = 0;

        do {
            image = new Image("file:res/cards/back" + i++ + ".jpg");

            if(!image.isError()) {
                imageView = new ImageView(image);

                if(selectedBackCard == null) {
                    selectedBackCard = imageView;
                    selectedBackCard.setEffect(border);
                }

                imageView.setFitWidth(Controller.CARD_WIDTH);
                imageView.setFitHeight(Controller.CARD_HEIGHT);

                ImageView finalImageView1 = imageView;
                imageView.setOnMouseClicked(mouseEvent -> {
                    selectedBackCard.setEffect(null);
                    selectedBackCard = finalImageView1;
                    selectedBackCard.setEffect(border);
                });

                backcardsContainer.getChildren().add(imageView);
            }

        } while(!image.isError()); //On charge toutes les images du répertoire de 0 à n

        Slider slider = (Slider)lookup("#slider");
        Label sliderLabel = (Label)lookup("#slider-label");

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            sliderLabel.setText("" + (int)slider.getValue() + " %");
        });


        Button saveButton = (Button)lookup("#saveButton");
        Button cancelButton = (Button)lookup("#cancelButton");

        saveButton.setOnAction(actionEvent -> {
            controller.setBoardImage(selectedBackground.getImage());
            controller.setBackCardImage(selectedBackCard.getImage());
            controller.getMusicPlayer().setVolume(slider.getValue() / 100);
            controller.setScene(controller.getMenu());
        });

        cancelButton.setOnAction(actionEvent -> {
            controller.setScene(controller.getMenu());
        });
    }
}
