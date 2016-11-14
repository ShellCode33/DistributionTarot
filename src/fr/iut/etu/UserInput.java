package fr.iut.etu;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by shellcode on 11/7/16.
 */
public class UserInput extends Scene {

    private VBox vbox;
    private GridPane gridLayout;
    private TextField userInput;
    private Button submitButton;
    private Image selectedImage = null;

    private Controller controller;

    public UserInput(Controller controller) throws IOException {
        super(FXMLLoader.load(controller.getClass().getResource("user_input.fxml")), Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        this.controller = controller;


        vbox = (VBox)lookup("#vbox-userinput");
        gridLayout = (GridPane) lookup("#gridLayout");
        userInput = (TextField)lookup("#userInput");
        submitButton = (Button)lookup("#submitButton");

        DropShadow border = new DropShadow( 30, Color.BLACK );

        int num_avatar = 1;
        for(int i = 0; i < 4;i++) {
            for(int j = 0; j < 5; j++) {
                Image img = new Image("file:res/avatar" + num_avatar++ + ".png");

                if(selectedImage == null)
                    selectedImage = img;

                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(100);
                imgView.setFitHeight(100);
                imgView.setOnMouseClicked(mouseEvent -> imgView.requestFocus());
                imgView.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) ->
                {
                    if(newValue) {
                        selectedImage = imgView.getImage();
                        System.out.println("selectedImage: " + selectedImage);
                    }

                    imgView.setEffect(newValue ? border : null);
                });

                gridLayout.add(imgView, j, i);
            }
        }

        double buttonWidth = Controller.SCREEN_WIDTH / 5;
        double buttonHeight = Controller.SCREEN_HEIGHT / 12;

        gridLayout.setPrefWidth(gridLayout.getChildren().get(0).getLayoutBounds().getWidth()*5+10*6);
        gridLayout.setMaxWidth(gridLayout.getChildren().get(0).getLayoutBounds().getWidth()*5+10*6);

        submitButton.setPrefWidth(buttonWidth);
        submitButton.setMaxWidth(buttonWidth);
        submitButton.setPrefHeight(buttonHeight);
        submitButton.setMaxHeight(buttonHeight);

        submitButton.setOnAction(e -> buttonClicked(e));

        userInput.setPrefWidth(buttonWidth*2);
        userInput.setMaxWidth(buttonWidth*2);
        userInput.setPrefHeight(buttonHeight/1.5);
        userInput.setMaxHeight(buttonHeight/1.5);

        vbox.setScaleX(Controller.SCALE_COEFF);
        vbox.setScaleY(Controller.SCALE_COEFF);
        vbox.setTranslateX((Controller.SCREEN_WIDTH-buttonWidth*2)/2);
    }

    public void buttonClicked(ActionEvent e)
    {
        if(userInput.getText().isEmpty())
            userInput.setText("User42");

        controller.startGame(userInput.getText(), selectedImage);
    }
}
