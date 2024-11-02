module io.serateam.stewboo {
    requires com.jfoenix;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires com.google.gson;

    opens io.serateam.stewboo.ui to javafx.fxml;
    opens io.serateam.stewboo.core.utility to com.google.gson;
    exports io.serateam.stewboo.ui;
}