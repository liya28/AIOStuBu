module io.serateam.stewboo {
    requires com.jfoenix;
    requires javafx.fxml;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires com.google.gson;
    requires java.desktop;

    opens io.serateam.stewboo.core.services.pomodoro to com.google.gson;
    opens io.serateam.stewboo.core.services.flashcard to com.google.gson;
    opens io.serateam.stewboo.ui to javafx.fxml;
    opens io.serateam.stewboo.ui.menus to javafx.fxml;
    opens io.serateam.stewboo.ui.menus.flashcards to javafx.fxml;
    opens io.serateam.stewboo.ui.menus.pomodoro to javafx.fxml;
    opens io.serateam.stewboo.core.utility to com.google.gson;
    exports io.serateam.stewboo.ui;
    exports io.serateam.stewboo.ui.menus;
    exports io.serateam.stewboo.ui.menus.pomodoro;
    exports io.serateam.stewboo.ui.menus.flashcards;
}