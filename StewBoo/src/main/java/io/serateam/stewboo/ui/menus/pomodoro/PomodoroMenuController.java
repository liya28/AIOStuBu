package io.serateam.stewboo.ui.menus.pomodoro;

import com.jfoenix.controls.JFXButton;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.services.pomodoro.IPomodoroListener;
import io.serateam.stewboo.core.services.pomodoro.PomodoroSessionState;
import io.serateam.stewboo.core.services.pomodoro.PomodoroSettings;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.menus.IMenu;
import io.serateam.stewboo.ui.utility.ControllerAlerter;
import io.serateam.stewboo.ui.utility.MusicPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
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
    @FXML private TextField textField_pomodoroMinutes;
    @FXML private TextField textField_shortBreakMinutes;
    @FXML private TextField textField_longBreakMinutes;
    @FXML private Text txt_errorIncorrectInput;
    @FXML private ProgressBar progressBar_timeUntilFinish;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static PomodoroService service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        service = PomodoroService.getInstance();
        PomodoroSettings settings = service.getConfig();
        textField_pomodoroMinutes.setText(String.valueOf(settings.getWorkMinutes()/60));
        textField_shortBreakMinutes.setText(String.valueOf(settings.getQuickBreakMinutes()/60));
        textField_longBreakMinutes.setText(String.valueOf(settings.getLongBreakMinutes()/60));

        service.addListener(this);
        btn_stopTimer.setDisable(true);

        btn_startTimer.setOnMouseClicked(e -> onStartPomodoroClick());
        btn_stopTimer.setOnMouseClicked(e -> {
            btn_startTimer.setDisable(false);
            btn_stopTimer.setDisable(true);
            onStopPomodoroClick();
        });
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
    public void onTimerUpdate(long remainingSeconds, long initialSeconds)
    {
        System.out.println("Time left: " + remainingSeconds + " seconds");
        txt_timer.setText(formatSecondsToTime(remainingSeconds));

//        progressBar_timeUntilFinish.setProgress((1 - ( (double) remainingSeconds / initialSeconds)));
//        remaining seconds to zero
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar_timeUntilFinish.progressProperty(),
                (1 - ( (double) remainingSeconds / initialSeconds)));
        KeyFrame keyFrame = new KeyFrame(new Duration(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

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
        progressBar_timeUntilFinish.setProgress(0);
    }

    @Override
    public void onNewPomodoroTimeConfig(int workMinutes, int quickBreakMinutes, int longBreakMinutes)
    {
        System.out.printf("Pomodoro Config:\nWork: %d, Quick Break: %d, Long Break %d%n", workMinutes, quickBreakMinutes, longBreakMinutes);
        textField_pomodoroMinutes.setText(String.valueOf(workMinutes/60));
        textField_shortBreakMinutes.setText(String.valueOf(quickBreakMinutes/60));
        textField_longBreakMinutes.setText(String.valueOf(longBreakMinutes/60));
    }

    // endregion

    // region PomodoroService Methods

    private void startPomodoro()
    {
        try
        {
            // ensure our text fields are colored red before we throw some exceptions
            boolean isPomodoroOk = validateNumericalTextField(textField_pomodoroMinutes);
            boolean isQuickBreakOk = validateNumericalTextField(textField_shortBreakMinutes);
            boolean isLongBreakOk = validateNumericalTextField(textField_longBreakMinutes);
            if(!(isPomodoroOk && isQuickBreakOk && isLongBreakOk))
            {
                throw new IOException();
            }

            int pomMinutes;
            int shortMinutes;
            int longMinutes;

            if(textField_pomodoroMinutes.getText().isEmpty())
                pomMinutes = PomodoroService.DEFAULT_POMODORO_MINUTES;
            else
                pomMinutes = Integer.parseInt(textField_pomodoroMinutes.getText())*60;

            if(textField_shortBreakMinutes.getText().isEmpty())
                shortMinutes = PomodoroService.DEFAULT_POMODORO_BREAK_TIME;
            else
                shortMinutes = Integer.parseInt(textField_shortBreakMinutes.getText())*60;

            if(textField_longBreakMinutes.getText().isEmpty())
                longMinutes = PomodoroService.DEFAULT_POMODORO_LONG_BREAK_TIME;
            else
                longMinutes = Integer.parseInt(textField_longBreakMinutes.getText())*60;

            setNewUserPomodoroTime(pomMinutes, shortMinutes, longMinutes);

            System.out.println("Session started...");

            btn_startTimer.setDisable(true);
            btn_stopTimer.setDisable(false);
            service.startPomodoroSession();
        }
        catch(IOException e)
        {
            ControllerAlerter.showError("Error", "Invalid input!", "Input must be an integer greater than 0 and less than 1441.");
            System.err.println("POMODORO: Invalid text fields");
        }

    }
    private void stopTimer()
    {
        System.out.println("Stopping session...");
        service.stopPomodoroSession();
    }

    // endregion

    // region Utility Methods

    private void setNewUserPomodoroTime(int pomodoroMinutes, int shortMinutes, int longMinutes)
    {
        service.saveNewConfigTime(pomodoroMinutes, shortMinutes, longMinutes);
    }

    private boolean validateNumericalTextField(TextField tf)
    {
        String invalidInput_CSS = "-fx-border-color: red; -fx-border-width: 2;";

        if(!tf.getText().isEmpty()
        && (!tf.getText().matches("^(0*[1-9]\\d*)$") || Integer.parseInt(tf.getText()) > 1440))
        {
            tf.setStyle(invalidInput_CSS);
            return false;
        }
        // reset the styles in case the elements had the error styling
        tf.setStyle("");
        return true;
    }

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
            case WORK_SESSION -> "Pomodoro";
            case QUICK_BREAK -> "Quick Break";
            case LONG_BREAK -> "Long Break";
        };
    }

    // endregion
}
