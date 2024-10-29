package io.serateam.stewboo.ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class NavController implements Initializable
{
    @FXML
    private ImageView imageView_exit;

    @FXML
    private Label label_menu;

    @FXML
    private Label label_menuClose;

    @FXML
    private AnchorPane anchorPane_navPaneSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        imageView_exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Set initial position of slider off-screen
        anchorPane_navPaneSlider.setTranslateX(-176);

        // Show the slider
        label_menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), anchorPane_navPaneSlider);
            slide.setToX(0); // Slide into view
            slide.play();

            slide.setOnFinished(e -> {
                label_menu.setVisible(false);
                label_menuClose.setVisible(true);
            });
        });

        // Hide the slider
        label_menuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), anchorPane_navPaneSlider);
            slide.setToX(-176); // Slide out of view
            slide.play();

            slide.setOnFinished(e -> {
                label_menu.setVisible(true);
                label_menuClose.setVisible(false);
            });
        });


    }
}
