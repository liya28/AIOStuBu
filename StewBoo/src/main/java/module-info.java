module io.serateam.stewboo {
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;
    requires com.google.gson;
    requires atlantafx.base;
    requires org.controlsfx.controls;

    opens io.serateam.stewboo.core.utility to com.google.gson;
    opens io.serateam.stewboo.core.services.pomodoro to com.google.gson;
    opens io.serateam.stewboo.core.services.todolist to com.google.gson;
    opens io.serateam.stewboo.ui to javafx.fxml;
    opens io.serateam.stewboo.ui.menus to javafx.fxml;
    opens io.serateam.stewboo.ui.menus.todolist to javafx.fxml;
    opens io.serateam.stewboo.ui.menus.pomodoro to javafx.fxml;
    exports io.serateam.stewboo.ui;
    exports io.serateam.stewboo.ui.menus;
    exports io.serateam.stewboo.ui.menus.pomodoro;
    exports io.serateam.stewboo.ui.menus.todolist;
}