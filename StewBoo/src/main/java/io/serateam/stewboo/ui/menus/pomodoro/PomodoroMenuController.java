package io.serateam.stewboo.ui.menus.pomodoro;

import com.jfoenix.controls.JFXButton;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.services.pomodoro.IPomodoroListener;
import io.serateam.stewboo.core.services.pomodoro.PomodoroSessionState;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.media.Media;


public class PomodoroMenuController implements Initializable, IMenu, IPomodoroListener
{
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML private Text txt_timer;
    @FXML private Text txt_currentSession;
    @FXML private Text txt_pomodoroCollected;
    @FXML private JFXButton btn_startTimer;
    @FXML private JFXButton btn_stopTimer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PomodoroService.addListener(this);

        btn_startTimer.setOnMouseClicked(e -> onStartPomodoroClick());
        btn_stopTimer.setOnMouseClicked(e -> onStopPomodoroClick());
    }
    protected void onStartPomodoroClick()
    {
        startPomodoro();
    }
    protected void onStopPomodoroClick()
    {
        stopTimer();
    }
    }

    // region IPomodoroListener Methods
    @Override
    public void onTimerUpdate(long remainingSeconds) {
        System.out.println("Time left: " + remainingSeconds + " seconds");
        timerText.setText(formatSecondsToTime(remainingSeconds));
    }
    static String formatSecondsToTime(long timeInSeconds)
    {
        long hours = timeInSeconds / 3600;
        long minutes = (timeInSeconds % 3600) / 60;
        long seconds = timeInSeconds % 60;

        LocalTime time = LocalTime.of((int) hours, (int) minutes, (int) seconds);
        return TIME_FORMATTER.format(time);
    }

    @Override
    public void onPomodoroCounterUpdate(int newPomodoroCounter) {
        System.out.println("new poms: " + newPomodoroCounter + " yway");
    }

    @Override
    public void onSessionComplete() {
        System.out.println("Session complete! Time for a break.");
    }

    @Override
    public void onBreakComplete() {
        System.out.println("Break complete! Time to focus.");
    }

    @Override
    public void onStateChanged(PomodoroSessionState newState)
    {
        System.out.println("Timer state changed to: " + newState);
        txt_currentSession.setText("Current Session: " + identifySessionState(newState));
    }
    private String identifySessionState(PomodoroSessionState state)
    {
        return switch (state)
        {
            case WORK_SESSION -> "Work Session (25 minutes)";
            case QUICK_BREAK -> "Quick Break (5 minutes)";
            case LONG_BREAK -> "Long Break (15 minutes)";
        };
    }

    // endregion

    // region PomodoroService Methods

    private void startPomodoro() {
        PomodoroService.startPomodoroSession();
    }
    private void stopTimer() {
        PomodoroService.stopPomodoroSession();
    }

    // endregion
}
