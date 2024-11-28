package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDateTime;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Calendar Now Initializing...");

        calendarView = new CalendarView();
        calendarView.setEnableTimeZoneSupport(true);

        Calendar task = createCalendar("Task", "T", Style.STYLE1);
        Calendar events = createCalendar("Events", "E", Style.STYLE2);
        Calendar meetings = createCalendar("Meetings", "M", Style.STYLE3);
        Calendar birthdays = createCalendar("Birthdays", "B", Style.STYLE4);
        Calendar holidays = createCalendar("Holidays", "H", Style.STYLE7);

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        familyCalendarSource.getCalendars().addAll(task, events, meetings, birthdays, holidays);

        calendarView.getCalendarSources().setAll(familyCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        rootChildren.getChildren().addAll(calendarView);

        // Add event handlers to each calendar
        addCalendarEventHandlers(task);
        addCalendarEventHandlers(events);
        addCalendarEventHandlers(meetings);
        addCalendarEventHandlers(birthdays);
        addCalendarEventHandlers(holidays);

        Thread updateTimeThread = getThread();
        updateTimeThread.start();
    }

    private static Thread getThread()
    {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run()
            {
                while (true)
                {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try
                    {
                        // update every 10 seconds
                        sleep(10000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        return updateTimeThread;
    }

    private Calendar createCalendar(String name, String shortName, Style style) {
        Calendar calendar = new Calendar(name);
        calendar.setShortName(shortName);
        calendar.setStyle(style);
        return calendar;
    }

    private void addCalendarEventHandlers(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalStateException("Calendar instance is null!");
        }
        EventHandler<CalendarEvent> handler = evt -> {
            System.out.println("Event fired: " + evt.getEventType());
            System.out.println("Associated entry: " + evt.getEntry());
        };

        calendar.addEventHandler(CalendarEvent.ENTRY_INTERVAL_CHANGED, event -> {
            Entry<?> entry = event.getEntry();
            System.out.println("New or modified entry: " + entry.getTitle());

            // Save the entry details
            saveEntryToStorage(entry);
        });
    }


    private void saveEntryToStorage(Entry<?> entry) {
        // Example of saving data - you can adapt this to your needs
        String title = entry.getTitle();
        LocalDateTime start = entry.getStartAsLocalDateTime();
        LocalDateTime end = entry.getEndAsLocalDateTime();
        boolean isFullDay = entry.isFullDay();

        System.out.println("Saving entry:");
        System.out.println("Title: " + title);
        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("Full Day: " + isFullDay);

        // TODO Implement your saving logic (e.g., save to a database or file)
    }
}
