package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.view.CalendarView;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.application.Platform;
import javafx.concurrent.Task;
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


public class CalendarMenuController implements Initializable, IMenu
{
    @FXML AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        System.out.println("Omside calemdar");
        CalendarView calendarView = new CalendarView();

        Task<CalendarView> calendarTask = new Task<>() {
            @Override
            protected CalendarView call() throws Exception
            {
                Calendar deadlines = new Calendar("Deadlines");
                deadlines.setShortName("dl");
                deadlines.setStyle(Style.STYLE1);

                CalendarSource multiple = new CalendarSource("Group");
                multiple.getCalendars().addAll(deadlines);

                calendarView.getCalendarSources().setAll(multiple);
                calendarView.setRequestedTime(LocalTime.now());

//                final int count = 1000 - 1;
//                for (int i = 1; i <= count; i++) {
//                    Thread.sleep(10);
//                    updateProgress(i, count);
//                }
                return calendarView;
            }
        };

        calendarTask.setOnSucceeded(evt -> {
            // update ui with results
//            root.getChildren().addAll(calendarView);
            root.getChildren().addAll(calendarView);
        });

        // add progressBar indicator to show progressBar of calendarTask
//        progressBar.progressProperty().bind(calendarTask.progressProperty());
//        StackPane stackProgress = new StackPane(progressBar);
//        stackProgress.setAlignment(Pos.CENTER);
//        root.getChildren().addAll(stackProgress);
//        new Thread(calendarTask).start();

    }




//        System.out.println("In Cakendar");
//        CalendarView calendarView = new CalendarView();
//
//        Calendar birthdays = new Calendar("Birthdays"); // (2)
//        Calendar holidays = new Calendar("Holidays");
//
//        birthdays.setStyle(Style.STYLE1); // (3)
//        holidays.setStyle(Style.STYLE2);
//
//        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
//        myCalendarSource.getCalendars().addAll(birthdays, holidays);
//
//        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)
//
//        calendarView.setRequestedTime(LocalTime.now());
//
//        Thread updateTimeThread = getThread(calendarView);
//        updateTimeThread.start();

    private static Thread getThread(CalendarView calendarView) {
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
}
