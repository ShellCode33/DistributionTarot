package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import fr.iut.etu.view.CardView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by shellcode on 11/19/16.
 */
public class Settings extends StackPane {

    private static ImageView selectedBackground = new ImageView(new Image("file:./res/cards/back0.jpg"));
    private static ImageView selectedBackCard = new ImageView(new Image("file:./res/cards/back0.jpg"));
    private static Color particleColor = Color.YELLOW;
    private Rectangle lastRectangleClicked = null;

    public Settings(Controller controller) {

        setAlignment(Pos.CENTER);
        getStylesheets().add("file:res/style.css");
        getStyleClass().add("background-menu");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(50);
        vbox.getStyleClass().add("background-settings");

        vbox.setBackground(new Background(new BackgroundImage(selectedBackground.getImage(), null, null, null, null)));

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
        HBox colorsContainer = new HBox();

        backgroundsContainer.setAlignment(Pos.CENTER);
        backcardsContainer.setAlignment(Pos.CENTER);
        sliderContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setAlignment(Pos.CENTER);
        colorsContainer.setAlignment(Pos.CENTER);

        backgroundsContainer.setSpacing(50);
        backcardsContainer.setSpacing(50);
        sliderContainer.setSpacing(50);
        buttonsContainer.setSpacing(50);
        colorsContainer.setSpacing(20);

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

        Label labelVol = new Label("Music Volume : ");
        labelVol.getStyleClass().add("textMenu");
        labelVol.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        Slider slider = new Slider();
        slider.setValue(50);
        Label sliderLabel = new Label("50 %");
        sliderLabel.getStyleClass().add("textMenu");
        sliderLabel.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        sliderContainer.getChildren().addAll(labelVol, slider, sliderLabel);

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            sliderLabel.setText("" + (int)slider.getValue() + " %");
            controller.getMusicPlayer().setVolume(slider.getValue() / 100);
        });

        Label labelColor = new Label("Choose particles color :");
        labelColor.getStyleClass().add("textMenu");
        labelColor.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");

        Color colors[] = new Color[] {Color.YELLOW, Color.HOTPINK, Color.DARKBLUE, Color.DARKRED, Color.WHITE, Color.BLACK, Color.DARKGREEN, Color.CYAN};

        for(Color color : colors) {
            Rectangle rectangle = new Rectangle(50, 50, color);

            if(color == particleColor) {
                rectangle.setEffect(border);
                lastRectangleClicked = rectangle;
            }

            rectangle.setOnMouseClicked(event -> {
                lastRectangleClicked.setEffect(null);
                rectangle.setEffect(border);
                particleColor = color;
                lastRectangleClicked = rectangle;
            });
            colorsContainer.getChildren().add(rectangle);
        }

        Button okButton = new Button("OK");
        buttonsContainer.getChildren().add(okButton);
        okButton.getStyleClass().add("button");
        okButton.setStyle("-fx-font-size: " + 30 * Controller.SCALE_COEFF + "px;");
        okButton.setPrefWidth(Controller.SCREEN_WIDTH / 10);
        okButton.setMaxWidth(Controller.SCREEN_WIDTH / 10);

        okButton.setOnAction(actionEvent -> {
            controller.setBoardImage(selectedBackground.getImage());
            controller.setLayout(controller.getMenu());
        });


        vbox.getChildren().addAll(label1, backgroundsContainer, label2, backcardsContainer, sliderContainer, labelColor, colorsContainer, buttonsContainer);
        getChildren().add(vbox);
    }

    public static Image getBackgroundImage() {
        return selectedBackground.getImage();
    }

    public static Image getBackCardImage() {
        return selectedBackCard.getImage();
    }

    public static Color getParticleColor() {
        return particleColor;
    }
}
