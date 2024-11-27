package io.serateam.stewboo.ui;

import java.net.URL;

public class SharedVariables {
    public static final String path_directoryAudio = "audio/";
    public static final String path_directoryControls = "controls/";
    public static final String path_directoryCSS = "css/";
    public static final String path_directoryImages = "images/";

    public static final String path_dashboardFxml = path_directoryControls + "dashboard.fxml";
    public static final String path_calendarFxml = path_directoryControls + "calendar_view.fxml";
    public static final String path_flashcardsFxml = path_directoryControls + "flashcards/flashcards_view.fxml";
    public static final String path_pomodoroFxml = path_directoryControls + "pomodoro_view.fxml";
    public static final String path_todolistFxml = path_directoryControls + "todolist_view.fxml";
    public static final String path_notesFxml = path_directoryControls + "notes_view.fxml";
    public static final String path_flashcardsCardFxml = path_directoryControls + "flashcards/flashcards_cardview.fxml";
    public static final String path_flashcardsDeckFxml = path_directoryControls + "flashcards/flashcards_deckName.fxml";
    public static final String path_flashcardsCardCreationFxml = path_directoryControls + "flashcards/flashcards_cardCreation.fxml";

    public static final URL url_path_dashboardFxml = SharedVariables.class.getResource(path_dashboardFxml);
    public static final URL url_path_calendarFxml = SharedVariables.class.getResource(path_calendarFxml);
    public static final URL url_path_flashcardsFxml = SharedVariables.class.getResource(path_flashcardsFxml);
    public static final URL url_path_pomodoroFxml = SharedVariables.class.getResource(path_pomodoroFxml);
    public static final URL url_path_todolistFxml = SharedVariables.class.getResource(path_todolistFxml);
    public static final URL url_path_notesFxml = SharedVariables.class.getResource(path_notesFxml);
    public static final URL url_path_flashcardsCardFxml = SharedVariables.class.getResource(path_flashcardsCardFxml);
    public static final URL url_path_flashcardsDeckFxml = SharedVariables.class.getResource(path_flashcardsDeckFxml);
    public static final URL url_path_flashcardsCardCreationFxml = SharedVariables.class.getResource(path_flashcardsCardCreationFxml);

    public static final URL url_path_alarmMp3 = SharedVariables.class.getResource(path_directoryAudio + "pomodoro_alarm.mp3");

    public static final String path_loadingLogo = path_directoryImages + "loading_logo.png";
    public static final String path_appIcon = path_directoryImages + "logo.png";
}
