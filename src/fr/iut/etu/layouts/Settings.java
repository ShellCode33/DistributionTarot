package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import fr.iut.etu.view.CardView;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;

/**
 * Created by shellcode on 11/19/16.
 */
class Settings extends StackPane {

    private ImageView selectedBackground = null;
    private ImageView selectedBackCard = null;

    public Settings(Controller controller) throws IOException {

        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("background-menu");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(50);
        vbox.getStyleClass().add("background-settings");

        Label label1 = new Label("Board background :");
        Label label2 = new Label("Back cards :");
        label1.getStyleClass().add("textMenu");
        label2.getStyleClass().add("textMenu");
        label1.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        label2.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");


        HBox backgroundsContainer = new HBox();
        HBox backcardsContainer = new HBox();
        HBox sliderContainer = new HBox();
        HBox buttonsContainer = new HBox();

        backgroundsContainer.setAlignment(Pos.CENTER);
        backcardsContainer.setAlignment(Pos.CENTER);
        sliderContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setAlignment(Pos.CENTER);

        backgroundsContainer.setSpacing(50);
        backcardsContainer.setSpacing(50);
        sliderContainer.setSpacing(50);
        buttonsContainer.setSpacing(50);

        Image image;
        ImageView imageView;
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

                imageView.setFitWidth(100 * Controller.SCALE_COEFF);
                imageView.setFitHeight(100 * Controller.SCALE_COEFF);

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

                imageView.setFitWidth(CardView.CARD_WIDTH);
                imageView.setFitHeight(CardView.CARD_HEIGHT);

                ImageView finalImageView1 = imageView;
                imageView.setOnMouseClicked(mouseEvent -> {
                    selectedBackCard.setEffect(null);
                    selectedBackCard = finalImageView1;
                    selectedBackCard.setEffect(border);
                });

                backcardsContainer.getChildren().add(imageView);
            }

        } while(!image.isError()); //On charge toutes les images du répertoire de 0 à n

        Slider slider = new Slider();
        slider.setValue(50);
        Label sliderLabel = new Label("50 %");
        sliderLabel.getStyleClass().add("textMenu");
        sliderLabel.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        sliderContainer.getChildren().addAll(slider, sliderLabel);

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            sliderLabel.setText("" + (int)slider.getValue() + " %");
            controller.getMusicPlayer().setVolume(slider.getValue() / 100);
        });


        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        buttonsContainer.getChildren().addAll(saveButton, cancelButton);
        saveButton.getStyleClass().add("button");
        cancelButton.getStyleClass().add("button");
        saveButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        cancelButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");

        saveButton.setOnAction(actionEvent -> {
            controller.setBoardImage(selectedBackground.getImage());
            controller.setBackCardImage(selectedBackCard.getImage());
            controller.setLayout(controller.getMenu());
        });

        cancelButton.setOnAction(actionEvent -> controller.setLayout(controller.getMenu()));

        vbox.getChildren().addAll(label1, backgroundsContainer, label2, backcardsContainer, sliderContainer, buttonsContainer);
        getChildren().addAll(vbox);
    }
}
