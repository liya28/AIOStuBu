package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import io.serateam.stewboo.core.services.calendar.CalendarService;
import io.serateam.stewboo.core.services.calendar.StubuCalendar;
import io.serateam.stewboo.core.services.calendar.StubuCalendarEntry;
import io.serateam.stewboo.core.services.calendar.StubuCalendarList;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar.Style;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

// TODO priorities:
//      saveCalendar()
//      implement changeCalendars()


        /*
         TODO figure out how to send data here to backend.
            ideas:
                - Create a mapper that translates CalendarEvent and Entry into our own
                calendar event and entry classes. Use properties only useful for us to serialize!
                  (raf 11/30; 1:27 AM)
                - Refer to CalendarFX and LogiservStudio projects
                Classes:
                    - StubuCalendarEntry
                    - StubuCalendarEvent
        */

public class CalendarMenuController implements Initializable, IMenu
{
    @FXML AnchorPane root;
    @FXML StackPane rootChildren;

    static CalendarView calendarView;
    List<Calendar> ui_calendarList;
    StubuCalendarList domain_stubuCalendarList;
    CalendarService calendarService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        System.out.println("Calendar Now Initializing...");
        calendarService = CalendarService.getInstance();

        calendarView = new CalendarView();

        // Hello! For simplicity, we'll disable these features in our CalendarView.
        calendarView.setEnableTimeZoneSupport(false);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);

        Calendar meetings = createCalendar("Meetings", "M", Style.STYLE3);
        Calendar deadlines = createCalendar("Deadlines", "D", Style.STYLE4);
        Calendar holidays = createCalendar("Holidays", "H", Style.STYLE7);

        CalendarSource stubuCalendarSource = new CalendarSource("MyStubuUser");
        stubuCalendarSource.getCalendars().addAll(meetings, deadlines, holidays);

        // Let's group these Calendar objects.
        ui_calendarList = new ArrayList<>();
        ui_calendarList.add(meetings);
        ui_calendarList.add(deadlines);
        ui_calendarList.add(holidays);

        domain_stubuCalendarList = StubuCalendarList.getInstance();
        List<StubuCalendar> listOfStubuCalendars = domain_stubuCalendarList.getCalendars();
        // On first boot, the domain object StubuCalendarList is empty.
        if(listOfStubuCalendars.isEmpty())
        {
            for(Calendar calendar : ui_calendarList)
            {
                StubuCalendar newStubuCalendar = StubuCalendarMapper.toStubuCalendarObject(calendar);
                domain_stubuCalendarList.addCalendar(newStubuCalendar);
            }
        }
        else
        {
            System.err.println("Unimplemented case");
            // TODO implement this!
            for(StubuCalendar stubuCalendar : listOfStubuCalendars)
            {
            }
        }

        // TODO Populate calendar
//        populateCalendar();

        // Handle events (e.g. when user creates a new entry).
        EventHandler<CalendarEvent> calendarEventHandler = event -> eventHandler(event);
        for(Calendar calendar : ui_calendarList)
        {
            calendar.addEventHandler(calendarEventHandler);
        }

        calendarView.getCalendarSources().setAll(stubuCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        rootChildren.getChildren().addAll(calendarView);

        Thread updateTimeThread = getThread();
        updateTimeThread.start();
    }

    private void eventHandler(CalendarEvent event)
    {
        // Autosave? hahaha
        System.out.println("Event: " + event.getEventType());
        if(event.getEventType() == CalendarEvent.CALENDAR_CHANGED)
        {
            saveCalender(event);
        }
        if(event.getEventType() == CalendarEvent.ENTRY_CALENDAR_CHANGED)
        {
            changeCalendar(event);
        }


        // Save the calendar after everything is sorted
        if(event.getEventType().getSuperType() == CalendarEvent.ENTRY_CHANGED)
        {
            saveCalender(event);
        }
    }

    private void saveCalender(CalendarEvent event)
    {
        // FIXME Bug when saving
        System.out.println("Calendar: Saving calendar...");
        Calendar eventCalendar = event.getCalendar();
        StubuCalendar stubuCalendar = null;
        for(StubuCalendar calendar : domain_stubuCalendarList.getCalendars())
        {
            if(Objects.equals(calendar.getName(), eventCalendar.getName()))
            {
                stubuCalendar = calendar;
                break;
            }
        }
        if(stubuCalendar == null)
        {
            System.err.println("Unimplemented case");
            stubuCalendar = new StubuCalendar(eventCalendar.getName());
            domain_stubuCalendarList.addCalendar(stubuCalendar);
        }

        calendarService.saveCalendarToFile(stubuCalendar);
    }

    private void changeCalendar(CalendarEvent event)
    {
        // Note: CalendarFX handles deletion of entries by nullifying its Calendar property!
        System.out.println("Calendar: Changing calendar...");
//        System.out.printf("Calendar: %s%n", event.getCalendar());
//        System.out.printf("Calendar Event Type: %s%n", event.getEventType());
//        System.out.printf("Calendar Entry: %s%n", event.getEntry());

        // Calendar is null means that an entry was deleted!
        // CalendarFX developer manual states that assigning null to the
        // Calendar property of the Entry object can count as a deletion.
        // Refer: https://dlsc-software-consulting-gmbh.github.io/CalendarFX/#_calendar
        if(event.getEntry().getCalendar() == null)
        {
            System.err.println("Unimplemented case");
//            StubuCalendarMapper.toStubuCalendarObject(event.getOldCalendar());
        }

        if(event.getOldCalendar() == null)
        {
            // Case: If the entry is a new entry

            // Get current calendar where the entry resides
            Calendar eventCalendar = event.getCalendar();
            StubuCalendar stubuCalendar = null;
            for(StubuCalendar calendar : domain_stubuCalendarList.getCalendars())
            {
                if(Objects.equals(calendar.getName(), eventCalendar.getName()))
                {
                    stubuCalendar = calendar;
                    break;
                }
            }
            // If we did not find calendar in domain_stubuCalendarList
            if(stubuCalendar == null)
            {
                System.err.println("Unimplemented case");
                stubuCalendar = new StubuCalendar(eventCalendar.getName());
                domain_stubuCalendarList.addCalendar(stubuCalendar);
            }

            // Get entry
            Entry<?> entry = event.getEntry();
            StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);

            stubuCalendar.addEntry(stubuEntry);
        }
        else
        {
            // Case: If we transfer the entry from one calendar to another
            System.out.printf("Changing calendar: from %s to %s %n", event.getOldCalendar().getName(), event.getCalendar().getName());
        }
    }

    private void populateCalendar()
    {
        // get list of calendar event list
        // for every event list, get event
        System.err.println("Calendar: Not supported yet.");
    }

    // region DONE

    private Calendar createCalendar(String name, String shortName, Style style)
    {
        Calendar calendar = new Calendar(name);
        calendar.setShortName(shortName);
        calendar.setStyle(style);
        return calendar;
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

    // endregion
}
