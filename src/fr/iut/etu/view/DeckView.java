package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import fr.iut.etu.model.Deck;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        //super(120, 212, deck.size());
        this.deck = deck;
        deck.addObserver(this);

        //Create the Material
        PhongMaterial mat = new PhongMaterial();
        Image image = new Image("file:./res/deck.jpg");
        mat.setDiffuseMap(image);
        mat.setSpecularColor(Color.BLACK);

        //http://stackoverflow.com/questions/19459012/how-to-create-custom-3d-model-in-javafx-8

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(
                0, 0, 100,      //P0
                100, 0, 100,    //P1
                0, 100, 100,    //P2
                100, 100, 100,  //P3
                0, 0, 0,        //P4
                100, 0, 0,      //P5
                0, 100, 0,      //P6
                100, 100, 0     //P7
        );

        mesh.getTexCoords().addAll(
                0.25f, 0,       //T0
                0.5f, 0,        //T1
                0, 0.25f,       //T2
                0.25f, 0.25f,   //T3
                0.5f, 0.25f,    //T4
                0.75f, 0.25f,   //T5
                1, 0.25f,       //T6
                0, 0.5f,        //T7
                0.25f, 0.5f,    //T8
                0.5f, 0.5f,     //T9
                0.75f, 0.5f,    //T10
                1, 0.5f,        //T11
                0.25f, 0.75f,   //T12
                0.5f, 0.75f     //T13
        );

        mesh.getFaces().addAll(
                5,1,4,0,0,3     //P5,T1 ,P4,T0  ,P0,T3
                ,5,1,0,3,1,4    //P5,T1 ,P0,T3  ,P1,T4
                ,0,3,4,2,6,7    //P0,T3 ,P4,T2  ,P6,T7
                ,0,3,6,7,2,8    //P0,T3 ,P6,T7  ,P2,T8
                ,1,4,0,3,2,8    //P1,T4 ,P0,T3  ,P2,T8
                ,1,4,2,8,3,9    //P1,T4 ,P2,T8  ,P3,T9
                ,5,5,1,4,3,9    //P5,T5 ,P1,T4  ,P3,T9
                ,5,5,3,9,7,10   //P5,T5 ,P3,T9  ,P7,T10
                ,4,6,5,5,7,10   //P4,T6 ,P5,T5  ,P7,T10
                ,4,6,7,10,6,11  //P4,T6 ,P7,T10 ,P6,T11
                ,3,9,2,8,6,12   //P3,T9 ,P2,T8  ,P6,T12
                ,3,9,6,12,7,13  //P3,T9 ,P6,T12 ,P7,T13
        );

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(mat);

        getChildren().add(meshView);

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {
        //setDepth(deck.size());
    }
}
