module io.serateam.stewboo {
    requires com.jfoenix;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires atlantafx.base;

    opens io.serateam.stewboo.ui to javafx.fxml;
    exports io.serateam.stewboo.ui;
}