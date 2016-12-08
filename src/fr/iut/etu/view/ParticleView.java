package fr.iut.etu.view;

import fr.iut.etu.layouts.Settings;
import fr.iut.etu.model.Vector2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;

/**
 * Created by shellcode on 11/29/16.
 */
public class ParticleView extends Region {

    Vector2D location;
    Vector2D velocity;

    double maxSpeed = 5;

    static int width = 4;
    static int height = 4;

    static int lifeSpanMax = 30;
    int lifeSpan = lifeSpanMax;

    static Image particleImages[] = null;

    public ParticleView(Vector2D location, Vector2D velocity) {
        this.location = location;
        this.velocity = velocity;

        setPrefSize(width, height);
    }

    public static void createTexture() {

        System.out.println("Creating particles textures");

        particleImages = new Image[lifeSpanMax];

        Circle circle = new Circle(width / 2);
        circle.setFill(Settings.getParticleColor());

        // create images
        for(int i = 0; i < lifeSpanMax; i++) {
            circle.setOpacity((double)i / (lifeSpanMax-1));

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage wi = new WritableImage(width, height);
            circle.snapshot(parameters, wi);
            particleImages[i] = wi;
        }
    }

    public void draw(GraphicsContext gc) {
        if(lifeSpan > 0)
            gc.drawImage(particleImages[lifeSpan-1], location.x - width / 2, location.y - height / 2);
    }

    public void move() {
        // limit velocity to max speed
        velocity.limit(maxSpeed);

        // change location depending on velocity
        location.add(velocity);
    }

    public boolean isDead() {
        return lifeSpan <= 0;
    }

    public void decreaseLifeSpan() {
        lifeSpan--;
    }

}
