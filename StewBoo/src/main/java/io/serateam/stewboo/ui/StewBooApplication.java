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
    private double windowX = 0;
    private double windowY = 0;

    public static void StewBooLaunch() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws NullPointerException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SharedVariables.url_path_dashboardFxml));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            // Enable window dragging
            root.setOnMousePressed(event -> {
                windowX = event.getSceneX();
                windowY = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - windowX);
                primaryStage.setY(event.getScreenY() - windowY);
            });

            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/style.css")).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}