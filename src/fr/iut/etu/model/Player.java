package fr.iut.etu.model;

import javafx.scene.image.Image;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player extends Hand{

    private String name = "";
    private Image avatar;
    private static int nb_computers = 0;

    public Player() {
        this("#computer" + nb_computers++, new Image("file:res/avatar_default.png"));
    }

    public Player(String name, Image avatar) {
        super();
        this.name = name;
        this.avatar = avatar;
    }

    public String getName(){
        return name;
    }
    public Image getAvatar() {
        return avatar;
    }
}
