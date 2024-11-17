package io.serateam.stewboo.ui.menus.pomodoro;

import com.jfoenix.controls.JFXButton;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.services.pomodoro.IPomodoroListener;
import io.serateam.stewboo.core.services.pomodoro.PomodoroSessionState;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.menus.IMenu;
import io.serateam.stewboo.ui.utility.MusicPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class PomodoroMenuController implements Initializable, IMenu, IPomodoroListener
{
    @FXML private Text txt_timer;
    @FXML private Text txt_currentSession;
    @FXML private Text txt_pomodoroCollected;
    @FXML private JFXButton btn_startTimer;
    @FXML private JFXButton btn_stopTimer;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static PomodoroService service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        service = PomodoroService.getInstance();
        service.addListener(this);

        btn_startTimer.setOnMouseClicked(e -> onStartPomodoroClick());
        btn_stopTimer.setOnMouseClicked(e -> onStopPomodoroClick());
    }
    private void onStartPomodoroClick()
    {
        startPomodoro();
    }
    private void onStopPomodoroClick()
    {
        stopTimer();
    }

    // region IPomodoroListener Implementation Methods

    @Override
    public void onTimerUpdate(long remainingSeconds)
    {
        System.out.println("Time left: " + remainingSeconds + " seconds");
        txt_timer.setText(formatSecondsToTime(remainingSeconds));
    }

    @Override
    public void onPomodoroCounterUpdate(int newPomodoroCounter)
    {
        System.out.println("+1 Pomodoro! Current number of Pomodoro collected: " + newPomodoroCounter);
        txt_pomodoroCollected.setText("Pomodoro Collected: " + newPomodoroCounter);
    }

    @Override
    public void onSessionComplete()
    {
        MusicPlayer.playMusic(SharedVariables.url_path_alarmMp3);
        System.out.println("Session complete! Time for a break.");
    }

    @Override
    public void onBreakComplete()
    {
        MusicPlayer.playMusic(SharedVariables.url_path_alarmMp3);
        System.out.println("Break complete! Time to focus.");
    }

    @Override
    public void onStateChanged(PomodoroSessionState newState)
    {
        System.out.println("Timer state changed to: " + newState);
        txt_currentSession.setText("Current Session: " + identifySessionState(newState));
    }

    // endregion

    // region PomodoroService Methods

    private void startPomodoro()
    {
        System.out.println("Session started...");
        service.startPomodoroSession();
    }
    private void stopTimer()
    {
        System.out.println("Stopping session...");
        service.stopPomodoroSession();
    }

    // endregion

    // region Utility Methods

    /**
     * @return Time-formatted string using the format {@code HH:MM:ss}.
     */
    private String formatSecondsToTime(long timeInSeconds)
    {
        long hours = timeInSeconds / 3600;
        long minutes = (timeInSeconds % 3600) / 60;
        long seconds = timeInSeconds % 60;

        LocalTime time = LocalTime.of((int) hours, (int) minutes, (int) seconds);
        return TIME_FORMATTER.format(time);
    }

    /**
     * @return Session state including its duration.
     */
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
}
