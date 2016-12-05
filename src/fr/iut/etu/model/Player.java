package fr.iut.etu.model;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player extends Hand{

    private String name = "";

    public enum UserChoice {
        TAKE,
        KEEP,
        KEEP_WITHOUT_DOG,
        KEEP_AGAINST_DOG
    }

    private UserChoice choice = null;

    public Player() {
        super();
    }

    public Player(String name) {
        super();
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(Notifications.USERNAME_CHANGED);
    }

    public void setChoice(UserChoice choice) {
        this.choice = choice;
    }

    public UserChoice getChoice() {
        return choice;
    }
}
