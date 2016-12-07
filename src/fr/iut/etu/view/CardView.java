package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.layouts.Settings;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Vector2D;
import javafx.animation.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.*;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Group implements Observer, Comparable<CardView> {

    public static double CARD_THICK = 2;
    public static double CARD_WIDTH = 120;
    public static double CARD_HEIGHT = 212;

    private final Card card;
    private Animation flipAnimation;
    private boolean selected = false;

    private static Random random = new Random();
    private ArrayList<ParticleView> myParticles = new ArrayList<>();
    private static ArrayList<ParticleView> allParticles = new ArrayList<>();

    private boolean moving = false;

    public CardView(Card card) {

        this.card = card;
        this.card.addObserver(this);

        ImageView back = new ImageView(Settings.getBackCardImage());
        back.setSmooth(true);
        back.setFitHeight(CARD_HEIGHT);
        back.setFitWidth(CARD_WIDTH);
        back.setTranslateZ(-1.01);

        getChildren().add(back);

        Image imageFace;

        if (card.getType() == Card.Type.FOOL) {
            imageFace = new Image("file:./res/cards/FOOL.png");
        } else {
            imageFace = new Image("file:./res/cards/" + card.getType().toString() + "_" + card.getValue() + ".png");
        }

        ImageView front = new ImageView(imageFace);
        front.setFitHeight(CARD_HEIGHT);
        front.setFitWidth(CARD_WIDTH);

        front.setTranslateZ(-1);

        front.setRotationAxis(Rotate.Y_AXIS);
        front.setRotate(180);

        getChildren().add(front);

        createFlipAnimation();
    }

    public void addParticle() {

        //Particles go to this position slowly (but never reach it because of the lifespan)
        Point2D origin = new Point2D(Controller.SCREEN_WIDTH / 2, Controller.SCREEN_HEIGHT / 2);

        // random location
        Point2D point = getParent().localToParent(localToParent(random.nextDouble() * CARD_WIDTH, random.nextDouble() * CARD_HEIGHT));
        Vector2D location = new Vector2D(point.getX(), point.getY());

        // random velocity
        double vx = random.nextGaussian() * 0.05 * (origin.getX() - point.getX());
        double vy = random.nextGaussian() * 0.05 * (origin.getY() - point.getY());
        Vector2D velocity = new Vector2D(vx, vy);

        ParticleView particle = new ParticleView(location, velocity);
        myParticles.add(particle);
        allParticles.add(particle);
    }

    private void createFlipAnimation() {

        setRotationAxis(Rotate.Y_AXIS);

        TranslateTransition translate1 = new TranslateTransition(Duration.seconds(0.2), this);

        translate1.setByY(-CARD_HEIGHT);

        translate1.setCycleCount(1);

        TranslateTransition translate2 = new TranslateTransition(Duration.seconds(0.3), this);
        translate2.setByZ(-CARD_HEIGHT);
        translate2.setCycleCount(1);

        RotateTransition rotate = new RotateTransition(Duration.seconds(0.3), this);
        rotate.setFromAngle(getRotate());
        rotate.setToAngle(-180);
        rotate.setAxis(Rotate.X_AXIS);
        rotate.setCycleCount(1);

        TranslateTransition translate3 = new TranslateTransition(Duration.seconds(0.2), this);
        translate3.setByZ(CARD_HEIGHT);
        translate3.setCycleCount(1);

        TranslateTransition translate4 = new TranslateTransition(Duration.seconds(0.3), this);
        translate4.setByY(CARD_HEIGHT);

        translate4.setCycleCount(1);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(translate2, rotate);

        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(translate1,pt, translate3, translate4);

        flipAnimation = st;
    }

    public Animation getFlipAnimation() {
        return flipAnimation;
    }

    @Override
    public void update(Observable observable, Object o) {
    }

    @Override
    public int compareTo(CardView cardView) {
        return card.compareTo(cardView.card);
    }

    @Override
    public String toString() {
        return card.toString();
    }

    public void setSelect(boolean value) {
        selected = value;
        setTranslateY(selected ? -40 : 0);
    }

    public boolean isSelected() {
        return selected;
    }

    public Card getCard() {
        return card;
    }

    public static ArrayList<ParticleView> getAllParticles() {
        return allParticles;
    }

    public ArrayList<ParticleView> getParticles() {
        return myParticles;
    }

    public void setMoving(boolean value) {
        moving = value;
    }

    public boolean isMoving() {
        return moving;
    }
}