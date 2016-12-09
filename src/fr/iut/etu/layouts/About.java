package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

/**
 * Created by shellcode on 12/9/16.
 */
public class About extends VBox {

    Random random = new Random();
    AnimationTimer backgroundPsychedelic;

    public About() {
        setAlignment(Pos.CENTER);

        Label label = new Label("PROUDLY");
        Label label1 = new Label("Designed By Sylvain Dupouy & Clément Fleury");
        Label label2 = new Label("Enjoy the power of this application");
        Label label3 = new Label("Peace");
        Label label4 = new Label("Press Esc to go back in the menu");
        Label label5 = new Label("Can you see me ?");
        Label label6 = new Label("No you can't");
        Label label7 = new Label("Nice try !");

        label.setFont(new Font(70 * Controller.SCALE_COEFF));
        label1.setFont(new Font(50 * Controller.SCALE_COEFF));
        label2.setFont(new Font(40 * Controller.SCALE_COEFF));
        label3.setFont(new Font(30 * Controller.SCALE_COEFF));
        label4.setFont(new Font(20 * Controller.SCALE_COEFF));
        label5.setFont(new Font(10 * Controller.SCALE_COEFF));
        label6.setFont(new Font(7 * Controller.SCALE_COEFF));
        label7.setFont(new Font(4 * Controller.SCALE_COEFF));


        getChildren().addAll(label, label1, label2, label3, label4, label5, label6, label7);
        psychedelicBackground();
    }

    private void psychedelicBackground() {

        backgroundPsychedelic = new AnimationTimer() {
            @Override
            public void handle(long l) {
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                setBackground(new Background(new BackgroundFill(Color.rgb(r,g,b), CornerRadii.EMPTY, Insets.EMPTY)));

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public AnimationTimer getBackgroundPsychedelic() {
        return backgroundPsychedelic;
    }
}
