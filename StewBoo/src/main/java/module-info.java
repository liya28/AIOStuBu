module io.serateam.stewboo {
    requires com.jfoenix;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires com.google.gson;
    requires java.desktop;
    requires org.fxmisc.richtext;
    requires org.jsoup;

    opens io.serateam.stewboo.core.services.pomodoro to com.google.gson;
    opens io.serateam.stewboo.core.services.notes to com.google.gson;
    opens io.serateam.stewboo.ui to javafx.fxml;
    opens io.serateam.stewboo.ui.menus to javafx.fxml;
    opens io.serateam.stewboo.ui.menus.pomodoro to javafx.fxml;
    opens io.serateam.stewboo.core.utility to com.google.gson;
    exports io.serateam.stewboo.ui;
    exports io.serateam.stewboo.ui.menus;
    exports io.serateam.stewboo.ui.menus.pomodoro;
}