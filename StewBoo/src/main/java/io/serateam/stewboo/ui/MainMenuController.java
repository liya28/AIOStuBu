package io.serateam.stewboo.ui;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private ImageView Exit;
    @FXML
    private JFXButton btn_calendar;
    @FXML
    private JFXButton btn_flashcard;
    @FXML
    private JFXButton btn_notes;
    @FXML
    private JFXButton btn_pomodoro;
    @FXML
    private JFXButton btn_todo;
    @FXML
    private JFXButton btn_workbench;
    @FXML
    private Pane pane_mainArea;
    @FXML
    private ImageView imageView_menuClose;
    @FXML
    private ImageView imageView_menuOpen;
    @FXML
    private Pane pane_permanentArea;
    @FXML
    private Pane home_view;
    @FXML
    private AnchorPane anchorPane_nav;
    @FXML
    private TextField textField_search;
    @FXML
    private ImageView imageView_userProfile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane_nav.setTranslateX(-119);
        imageView_menuOpen.setVisible(true);
        imageView_menuClose.setVisible(false);
        pane_mainArea.getChildren().setAll(home_view); // Ensure home_view is the only child initially

        setupMenuAnimation();

        btn_calendar.setOnMouseClicked(event -> loadView(SharedVariables.path_calendarFxml));
        btn_flashcard.setOnMouseClicked(event -> loadView(SharedVariables.path_flashcardsFxml));
        btn_notes.setOnMouseClicked(event -> loadView(SharedVariables.path_notesFxml));
        btn_pomodoro.setOnMouseClicked(event -> loadView(SharedVariables.path_pomodoroFxml));
        btn_todo.setOnMouseClicked(event -> loadView(SharedVariables.path_todolistFxml));
        btn_workbench.setOnMouseClicked(event -> showHomeView());
    }

    @FXML
    private void showHomeView() {
        pane_mainArea.getChildren().setAll(home_view); // Clear others and show home_view
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            pane_mainArea.getChildren().setAll(view); // Clear and add new view OMG THANK YOU ANI
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupMenuAnimation() {
        imageView_menuOpen.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.2), anchorPane_nav);
            slide.setToX(0);
            slide.play();
            slide.setOnFinished(e -> {
                imageView_menuOpen.setVisible(false);
                imageView_menuClose.setVisible(true);
            });
        });

        imageView_menuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.2), anchorPane_nav);
            slide.setToX(-119); // Slide out of view
            slide.play();
            slide.setOnFinished(e -> {
                imageView_menuOpen.setVisible(true);
                imageView_menuClose.setVisible(false);
            });
        });
    }

    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
