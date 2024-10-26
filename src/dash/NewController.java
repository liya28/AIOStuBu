package dash;

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

public class NewController implements Initializable {
    private static final String FXML_PATH = "/design/";

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
    private Pane main_area;
    @FXML
    private ImageView menu_close;
    @FXML
    private ImageView menu_open;
    @FXML
    private Pane permanent_area;
    @FXML
    private Pane home_view;
    @FXML
    private AnchorPane side_ankerpane;
    @FXML
    private TextField txt_search;
    @FXML
    private ImageView user_profile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        side_ankerpane.setTranslateX(-119);
        menu_open.setVisible(true);
        menu_close.setVisible(false);
        main_area.getChildren().setAll(home_view); // Ensure home_view is the only child initially

        setupMenuAnimation();

        btn_calendar.setOnMouseClicked(event -> loadView("cale_view.fxml"));
        btn_flashcard.setOnMouseClicked(event -> loadView("flash_view.fxml"));
        btn_notes.setOnMouseClicked(event -> loadView("notes_view.fxml"));
        btn_pomodoro.setOnMouseClicked(event -> loadView("pomo_view.fxml"));
        btn_todo.setOnMouseClicked(event -> loadView("todo_view.fxml"));
        btn_workbench.setOnMouseClicked(event -> showHomeView());
    }

    @FXML
    private void showHomeView() {
        main_area.getChildren().setAll(home_view); // Clear others and show home_view
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + fxml));
            Parent view = loader.load();
            main_area.getChildren().setAll(view); // Clear and add new view OMG THANK YOU ANI
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupMenuAnimation() {
        menu_open.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.2), side_ankerpane);
            slide.setToX(0);
            slide.play();
            slide.setOnFinished(e -> {
                menu_open.setVisible(false);
                menu_close.setVisible(true);
            });
        });

        menu_close.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.2), side_ankerpane);
            slide.setToX(-119); // Slide out of view
            slide.play();
            slide.setOnFinished(e -> {
                menu_open.setVisible(true);
                menu_close.setVisible(false);
            });
        });
    }

    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
