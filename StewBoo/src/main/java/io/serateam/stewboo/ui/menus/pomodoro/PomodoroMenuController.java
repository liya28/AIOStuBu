package io.serateam.stewboo.ui.menus.pomodoro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.services.pomodoro.IPomodoroListener;
import io.serateam.stewboo.core.services.pomodoro.PomodoroSessionState;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PomodoroMenuController implements Initializable, IMenu, IPomodoroListener
{
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML private Text text;
    @FXML private JFXTextArea timerText;
    @FXML private JFXButton btn_startTimer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PomodoroService.addListener(this);

        btn_startTimer.setOnMouseClicked(e -> onStartPomodoroClick());
    }
}
