package fr.iut.etu.layouts;

import fr.iut.etu.Controller;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

import java.io.File;

/**
 * Created by shellcode on 12/9/16.
 */
public class Rules extends Region {

    final WebView browser = new WebView();

    public Rules() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        browser.setPrefWidth(bounds.getWidth());
        browser.setPrefHeight(bounds.getHeight());
        browser.setMaxWidth(bounds.getWidth());
        browser.setMaxHeight(bounds.getHeight());
        browser.getEngine().load(String.valueOf(new File("res/rules.html").toURI()));
        getChildren().add(browser);
    }


}
