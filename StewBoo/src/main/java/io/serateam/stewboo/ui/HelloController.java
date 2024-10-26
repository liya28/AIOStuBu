package io.serateam.stewboo.ui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}