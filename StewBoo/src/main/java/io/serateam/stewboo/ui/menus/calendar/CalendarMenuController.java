package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.view.CalendarView;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


public class CalendarMenuController implements Initializable, IMenu
{
    @FXML AnchorPane root;
    @FXML StackPane rootChildren;

    static CalendarView calendarView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        System.out.println("Calendar Now Initializing...");

        calendarView = new CalendarView();
        calendarView.setEnableTimeZoneSupport(true);

        Calendar task = new Calendar("Task");
        Calendar events = new Calendar("Events");
        Calendar meetings = new Calendar("Meetings");
        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");

        task.setShortName("T");
        events.setShortName("E");
        meetings.setShortName("M");
        birthdays.setShortName("B");
        holidays.setShortName("H");

        task.setStyle(Style.STYLE1);
        events.setStyle(Style.STYLE2);
        meetings.setStyle(Style.STYLE3);
        birthdays.setStyle(Style.STYLE4);
        holidays.setStyle(Style.STYLE7);

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        familyCalendarSource.getCalendars().addAll(birthdays, holidays, task, events, meetings);

        calendarView.getCalendarSources().setAll(familyCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        rootChildren.getChildren().addAll(calendarView);

        Thread updateTimeThread = getThread();
        updateTimeThread.start();
    }

    private static Thread getThread() {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        return updateTimeThread;
    }

    public static void saveSession()
    {
        if(calendarView != null)
        {
            calendarView.getCalendars().
        }
    }
}
