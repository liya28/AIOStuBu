package io.serateam.stewboo.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class StewBooApplication extends Application {
    private double window_x = 0;
    private double window_y = 0;

    public static void StewBooLaunch() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws NullPointerException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(FxmlVariables.url_path_dashboardFxml));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            // Enable window dragging
            root.setOnMousePressed(event -> {
                window_x = event.getSceneX();
                window_y = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - window_x);
                primaryStage.setY(event.getScreenY() - window_y);
            });

            Scene scene = new Scene(root, 800, 500);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/style.css")).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}