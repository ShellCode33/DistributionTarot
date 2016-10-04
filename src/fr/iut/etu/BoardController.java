package fr.iut.etu;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by shellcode on 9/27/16.
 */
public class BoardController implements Initializable {

    private Board model;

    public BoardController() {
        model = new Board();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
