package io.serateam.stewboo.ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Set initial position of slider off-screen
        slider.setTranslateX(-176);

        // Show the slider
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            slide.setToX(0); // Slide into view
            slide.play();

            slide.setOnFinished(e -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        // Hide the slider
        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            slide.setToX(-176); // Slide out of view
            slide.play();

            slide.setOnFinished(e -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });


    }
}
