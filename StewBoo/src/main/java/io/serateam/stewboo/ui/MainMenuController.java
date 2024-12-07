package io.serateam.stewboo.ui;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML private ImageView Exit;
    @FXML private JFXButton btn_calendar;
    @FXML private JFXButton btn_flashcard;
    @FXML private JFXButton btn_notes;
    @FXML private JFXButton btn_pomodoro;
    @FXML private JFXButton btn_todo;
    @FXML private JFXButton btn_workbench;
    @FXML private AnchorPane pane_mainArea;
    @FXML private ImageView imageView_menuClose;
    @FXML private ImageView imageView_menuOpen;
    @FXML private Pane pane_permanentArea;
    @FXML private AnchorPane home_view;
    @FXML private AnchorPane slideShow_Holder;
    @FXML private AnchorPane anchorPane_nav;
    @FXML private Pane vision_Pane;
    @FXML private Pane title_Pane;
    @FXML private Pane member_Pane;
    @FXML private HBox hBox_subPageArea;
    @FXML private ImageView img_Splashscreen;

    private final String[] imagePaths = {
            "/io/serateam/stewboo/ui/images/slide1_quote.png",
            "/io/serateam/stewboo/ui/images/slide2_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide3_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide4_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide5_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide6_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide7_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide8_quote.png",
            "/io/serateam/stewboo/ui/images/slide9_quote.png",
            "/io/serateam/stewboo/ui/images/slide10_quote.jpg",
            "/io/serateam/stewboo/ui/images/slide11_quote.jpg"
    };
    private int currentImageIndex = 0;
    private final Image[] preloadedImages = new Image[imagePaths.length];

    // Store loaded views in this hashmap; O(1) lookup so long as we are careful!
    private final Map<String, AnchorPane> loadedViewCache = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        hBox_subPageArea.getChildren().setAll(home_view); // Ensure home_view is the only child initially
        preloadImages();
        setSlideShow();

        btn_workbench.setOnMouseClicked(event -> showHomeView());
        btn_pomodoro.setOnMouseClicked(event -> loadView(SharedVariables.path_pomodoroFxml));
        btn_flashcard.setOnMouseClicked(event -> loadView(SharedVariables.path_flashcardsFxml));
        btn_calendar.setOnMouseClicked(event -> loadView(SharedVariables.path_calendarFxml));
        btn_todo.setOnMouseClicked(event -> loadView(SharedVariables.path_todolistFxml));
        btn_notes.setOnMouseClicked(event -> loadView(SharedVariables.path_notesFxml));

        ShowSplashScreen();
    }

    @FXML
    private void showHomeView()
    {
        hBox_subPageArea.getChildren().setAll(home_view); // Clear others and show home_view
    }

    private void loadView(String fxml)
    {
        try
        {
            if (!loadedViewCache.containsKey(fxml))
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                AnchorPane view = loader.load();

                // Store the loaded view in the cache
                loadedViewCache.put(fxml, view);
            }
            // Use the cached view
            AnchorPane newPage = loadedViewCache.get(fxml);
            hBox_subPageArea.getChildren().setAll(newPage);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    private void setupMenuAnimation()
    {
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

    public void handleExitClick(MouseEvent mouseEvent)
    {
        System.exit(0);
    }

    private void setSlideShow()
    {
        if (imagePaths.length == 0) return; // No images to show


        ImageView img_Holder = new ImageView(preloadedImages[currentImageIndex]);
        img_Holder.setPreserveRatio(false);
        img_Holder.fitWidthProperty().bind(slideShow_Holder.widthProperty());
        img_Holder.fitHeightProperty().bind(slideShow_Holder.heightProperty());

        slideShow_Holder.getChildren().add(img_Holder);

        Timeline slideshow = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), img_Holder);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                img_Holder.setImage(preloadedImages[currentImageIndex]);
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), img_Holder);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        }));

        slideshow.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely
        slideshow.play();
    }

    private void preloadImages()
    {
        for (int i = 0; i < imagePaths.length; i++)
        {
            try
            {
                preloadedImages[i] = new Image(Objects.requireNonNull(getClass().getResource(imagePaths[i])).toString());
            }
            catch (Exception e)
            {
                // TODO please handle this error properly. What to do if the images were not preloaded?
                preloadedImages[i] = new Image(Objects.requireNonNull(getClass().getResource("/io/serateam/stewboo/ui/images/placeholder.jpg")).toString());
            }
        }
    }

}
