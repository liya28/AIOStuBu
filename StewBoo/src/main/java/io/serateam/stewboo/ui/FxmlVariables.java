package io.serateam.stewboo.ui;

import java.net.URL;

public class FxmlVariables {
    public static final String path_dashboardFxml = "controls/dashboard.fxml";
    public static final String path_calendarFxml = "controls/calendar_view.fxml";
    public static final String path_flashcardsFxml = "controls/flashcards_view.fxml";
    public static final String path_pomodoroFxml = "controls/pomodoro_view.fxml";
    public static final String path_todolistFxml = "controls/todolist_view.fxml";
    public static final String path_notesFxml = "controls/notes_view.fxml";

    public static final URL url_path_dashboardFxml = FxmlVariables.class.getResource(path_dashboardFxml);
    public static final URL url_path_calendarFxml = FxmlVariables.class.getResource(path_calendarFxml);
    public static final URL url_path_flashcardsFxml = FxmlVariables.class.getResource(path_flashcardsFxml);
    public static final URL url_path_pomodoroFxml = FxmlVariables.class.getResource(path_pomodoroFxml);
    public static final URL url_path_todolistFxml = FxmlVariables.class.getResource(path_todolistFxml);
    public static final URL url_path_notesFxml = FxmlVariables.class.getResource(path_notesFxml);
}
