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
            populateCalendar(listOfStubuCalendars);
        }

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

    private void populateCalendar(List<StubuCalendar> listOfStubuCalendars)
    {
        System.out.println("Calendar: Populating Calendar...");
        for(StubuCalendar stubuCalendar : listOfStubuCalendars)
        {
            for(Calendar calender : ui_calendarList)
            {
                if(Objects.equals(stubuCalendar.getName(), calender.getName()))
                {
                    for(StubuCalendarEntry entry : stubuCalendar.getEntryList())
                    {
                        Entry<?> newEntry = new Entry<>();
                        newEntry.setTitle(entry.getTitle());
                        newEntry.setId(entry.getId());
                        newEntry.setInterval(entry.getStartDate(), entry.getEndDate());
                        newEntry.setLocation(entry.getLocation());
                        newEntry.setFullDay(entry.isFullDay());
                        newEntry.setRecurrenceRule(entry.getRecurrenceRule());

                        calender.addEntry(newEntry);
                    }
                }
            }
        }
    }

    private void eventHandler(CalendarEvent event)
    {
        System.out.println("Event: " + event.getEventType());
        if(event.getEventType() == CalendarEvent.CALENDAR_CHANGED)
        {
            saveCalenderAndCalendarEntries(event);
        }
        if(event.getEventType() == CalendarEvent.ENTRY_CALENDAR_CHANGED)
        {
            // CalendarEvent.ENTRY_CALENDAR_CHANGED may be raised
            // when an entry is deleted.
            changeCalendar(event);
        }

        // Save the calendar after everything is sorted
        if(event.getEventType().getSuperType() == CalendarEvent.ENTRY_CHANGED)
        {
            saveCalenderAndCalendarEntries(event);
        }
    }

    private void saveCalenderAndCalendarEntries(CalendarEvent event)
    {
        System.out.println("Calendar: Saving calendar...");
        Calendar eventCalendar = event.getCalendar();
        StubuCalendar stubuCalendar = null;
        for(StubuCalendar calendar : domain_stubuCalendarList.getCalendars())
        {
            if(Objects.equals(calendar.getName(), eventCalendar.getName()))
            {
                stubuCalendar = calendar;

                for(StubuCalendarEntry entry : stubuCalendar.getEntryList())
                {
                    // Reassign CalendarFX Entry properties to our domain Entry object with the same ID
                    if(Objects.equals(entry.getId(), event.getEntry().getId()))
                    {
                        Entry<?> inputEntry = event.getEntry();
                        entry.setTitle(inputEntry.getTitle());
                        entry.setLocation(inputEntry.getLocation());
                        entry.setStartDate(inputEntry.getStartAsLocalDateTime());
                        entry.setEndDate(inputEntry.getEndAsLocalDateTime());
                        entry.setFullDay(inputEntry.isFullDay());
                        entry.setRecurrenceRule(inputEntry.getRecurrenceRule());
                        break;
                    }
                }
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

    /**
     * Handles the following cases where an entry's calendar designation is changed:</p>
     * <ul>
     *     <li>A new entry is created by the user
     *     (A new entry is assigned a calendar).</li>
     *     <li>The user changed the entry's calendar designation to another calendar
     *     (An entry's calendar designation is changed).</li>
     *     <li>An entry is deleted
     *     (An entry's calendar designation is made {@code null}).</li>
     * </ul>
     * @param event
     */
    private void changeCalendar(CalendarEvent event)
    {
        // Note: CalendarFX handles deletion of entries by nullifying its Calendar property!
        System.out.println("Calendar: Changing calendar...");

        // If getCalendar() is null, it means that an entry was deleted!
        // CalendarFX developer manual states that assigning null to the
        // Calendar property of the Entry object can count as a deletion.
        // Refer: https://dlsc-software-consulting-gmbh.github.io/CalendarFX/#_calendar
        if(event.getEntry().getCalendar() == null)
        {
            // TODO Case: Entry is removed in the entire calendar view
            System.err.println("Unimplemented case");
//            StubuCalendarMapper.toStubuCalendarObject(event.getOldCalendar());
        }

        if(event.getOldCalendar() == null)
        {
            // Case: If the entry is a new entry
            createNewEntryInCalendar(event);
        }
        else
        {
            // Case: If we transfer the entry from one calendar to another
            changeEntryFromOldCalendarToNewCalendar(event);
        }
    }

    private void changeEntryFromOldCalendarToNewCalendar(CalendarEvent event)
    {
        System.out.printf("Changing calendar: from %s to %s %n", event.getOldCalendar().getName(), event.getCalendar().getName());

        Entry<?> entry = event.getEntry();
        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);

        createNewEntryInCalendar(event);

        StubuCalendar oldStubuCalendar = removeEntryInOldCalendar(event, stubuEntry);
        calendarService.saveCalendarToFile(oldStubuCalendar);
    }

    private void createNewEntryInCalendar(CalendarEvent event)
    {
        Calendar eventCalendar = event.getCalendar();
        StubuCalendar stubuCalendar = findCalendarInListOfCalendarsInDomain(eventCalendar);

        // If we did not find calendar in domain_stubuCalendarList,
        // create a new calendar (that will be the current calendar)
        if(stubuCalendar == null)
        {
            System.err.println("Creating new calendar...");
            stubuCalendar = StubuCalendarMapper.toStubuCalendarObject(eventCalendar);
            domain_stubuCalendarList.addCalendar(stubuCalendar);
        }

        // Get entry and add it to the current calendar
        Entry<?> entry = event.getEntry();
        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);
        stubuCalendar.addEntry(stubuEntry);
    }

    /**
     * Removes the entry in the domain StubuCalendarList by obtaining the old calendar of the entry through
     * {@code CalendarEvent.getOldCalendar()} and removing the entry in the old calendar by the ID of the entry.
     * @param event
     * @param stubuEntry
     * @return old StubuCalendar object reference.
     */
    private StubuCalendar removeEntryInOldCalendar(CalendarEvent event, StubuCalendarEntry stubuEntry)
    {
        Calendar oldEventCalendar = event.getOldCalendar();
        StubuCalendar oldStubuCalendar = findCalendarInListOfCalendarsInDomain(oldEventCalendar);
        // TODO Unhandled exception
        //  Logically, this should not happen! Programmer error.
        if(oldStubuCalendar == null) throw new RuntimeException("Unimplemented case");

        String id = stubuEntry.getId();
        oldStubuCalendar.removeEntryById(id);
        return oldStubuCalendar;
    }

    /**
     * Finds the mirror of CalendarFX Calendar in the domain through its name.
     * @param calendar
     * @return the mirrored copy of CalendarFX in StubuCalendarList; {@code null} if not found.
     */
    private StubuCalendar findCalendarInListOfCalendarsInDomain(Calendar calendar)
    {
        StubuCalendar item = null;
        for(StubuCalendar stubuCalendar : domain_stubuCalendarList.getCalendars())
        {
            if (Objects.equals(stubuCalendar.getName(), calendar.getName()))
            {
                item = stubuCalendar;
                break;
            }
        }
        return item;
    }

    /**
     * Creates a new CalendarFX Calendar.
     * @param name
     * @param shortName
     * @param style
     * @return a new CalendarFX Calendar object.
     */
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
}
