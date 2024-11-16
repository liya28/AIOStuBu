package io.serateam.stewboo.ui;

import java.net.URL;

public class SharedVariables {
    public static final String path_directoryAudio = "audio/";
    public static final String path_directoryControls = "controls/";
    public static final String path_directoryCSS = "css/";
    public static final String path_directoryImages = "images/";

    public static final String path_dashboardFxml = path_directoryControls + "dashboard.fxml";
    public static final String path_calendarFxml = path_directoryControls + "calendar_view.fxml";
    public static final String path_flashcardsFxml = path_directoryControls + "flashcards_view.fxml";
    public static final String path_pomodoroFxml = path_directoryControls + "pomodoro_view.fxml";
    public static final String path_todolistFxml = path_directoryControls + "todolist_view.fxml";
    public static final String path_notesFxml = path_directoryControls + "notes_view.fxml";

    public static final URL url_path_dashboardFxml = SharedVariables.class.getResource(path_dashboardFxml);
    public static final URL url_path_calendarFxml = SharedVariables.class.getResource(path_calendarFxml);
    public static final URL url_path_flashcardsFxml = SharedVariables.class.getResource(path_flashcardsFxml);
    public static final URL url_path_pomodoroFxml = SharedVariables.class.getResource(path_pomodoroFxml);
    public static final URL url_path_todolistFxml = SharedVariables.class.getResource(path_todolistFxml);
    public static final URL url_path_notesFxml = SharedVariables.class.getResource(path_notesFxml);

    public static final URL url_path_alarmMp3 = SharedVariables.class.getResource(path_directoryAudio + "pomodoro_alarm.mp3");
}
