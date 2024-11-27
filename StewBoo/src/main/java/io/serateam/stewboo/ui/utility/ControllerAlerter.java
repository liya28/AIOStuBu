package io.serateam.stewboo.ui.utility;

import javafx.scene.control.Alert;

public class ControllerAlerter
{
    public static void showError(String title, String headerText, String contentText)
    {
        showAlert(Alert.AlertType.ERROR, title, headerText, contentText);
    }

    public static void showInfo(String title, String headerText, String contentText)
    {
        showAlert(Alert.AlertType.INFORMATION, title, headerText, contentText);
    }

    public static void showWarning(String title, String headerText, String contentText)
    {
        showAlert(Alert.AlertType.WARNING, title, headerText, contentText);
    }

    public static void showConfirmation(String title, String headerText, String contentText)
    {
        showAlert(Alert.AlertType.CONFIRMATION, title, headerText, contentText);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
