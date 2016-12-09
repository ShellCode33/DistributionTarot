package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

import java.io.File;

/**
 * Created by shellcode on 12/9/16.
 */
public class Rules extends Region {

    final WebView browser = new WebView();

    public Rules() {
        browser.setPrefWidth(Controller.SCREEN_WIDTH);
        browser.setPrefHeight(Controller.SCREEN_HEIGHT);
        browser.getEngine().load(String.valueOf(new File("res/rules.html").toURI()));
        getChildren().add(browser);
    }


}
