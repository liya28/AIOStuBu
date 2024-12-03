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
        System.out.println("Calendar: Study Buddy Calendar is now initializing.");
        calendarService = CalendarService.getInstance();

        calendarView = new CalendarView();

        // Hello! For simplicity, we'll disable these features in our CalendarView.
        calendarView.setEnableTimeZoneSupport(false);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);

        /*
            CalendarFX CSS source for Calendar.Style:
                -style1-color: rgba(119.0, 192.0, 75.0, 0.9);
                -style2-color: rgba(65.0, 143.0, 203.0, 0.9);
                -style3-color: rgba(247.0, 209.0, 91.0, 0.9);
                -style4-color: rgba(157.0, 91.0, 159.0, 0.9);
                -style5-color: rgba(208.0, 82.0, 95.0, 0.9);
                -style6-color: rgba(249.0, 132.0, 75.0, 0.9);
                -style7-color: rgba(174.0, 102.0, 62.0, 0.9);
        */
        Calendar meetings = createCalendar("Meetings", "M", Style.STYLE3);
        Calendar deadlines = createCalendar("Deadlines", "D", Style.STYLE5);
        Calendar holidays = createCalendar("Holidays", "H", Style.STYLE2);

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
        System.out.println("Calendar: Populating CalendarView with calendar entries.");
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
                        newEntry.setMinimumDuration(entry.getMinimumDuration());

                        calender.addEntry(newEntry);
                    }
                }
            }
        }
    }

    // region Calendar Event Methods

    private void eventHandler(CalendarEvent event)
    {
        System.out.println("Calendar: Event Thrown [" + event.getEventType() + "]");

        if(event.getEventType() == CalendarEvent.CALENDAR_CHANGED)
        {
            saveCalenderAndCalendarEntries(event, false);
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
            boolean wasEventDeleted = event.getCalendar() == null;
            saveCalenderAndCalendarEntries(event, wasEventDeleted);
        }
    }

    private void saveCalenderAndCalendarEntries(CalendarEvent event, boolean isDeleteOperation)
    {
        System.out.println("Calendar: Saving calendar.");

        Entry<?> inputEntry = event.getEntry().isRecurrence()
                ? event.getEntry().getRecurrenceSourceEntry()
                : event.getEntry();

        Calendar eventCalendar = (isDeleteOperation) ? event.getOldCalendar() : event.getCalendar();
        StubuCalendar stubuCalendar = null;

        // Iterate over the calendars
        try
        {
            for(StubuCalendar calendar : domain_stubuCalendarList.getCalendars())
            {
                if(Objects.equals(calendar.getName(), eventCalendar.getName()))
                {
                    stubuCalendar = calendar;
                    break;
                }
            }
            if(stubuCalendar == null)
                throw new NullPointerException("Calendar: Could not find calendar with name "
                        + eventCalendar.getName() + " in domain_stubuCalendarList.");
        }
        catch(NullPointerException e)
        {
            System.err.println(e.getMessage());
            System.out.println("Creating calendar in domain_stubuCalendarList with name " + eventCalendar.getName());
            stubuCalendar = new StubuCalendar(eventCalendar.getName());
            domain_stubuCalendarList.addCalendar(stubuCalendar);
            createNewEntryInCalendar(event.getEntry(), eventCalendar);
        }

        // Iterate over the entries
        for(StubuCalendarEntry entry : stubuCalendar.getEntryList())
        {
            // Reassign CalendarFX Entry properties to our domain Entry object with the same ID
            if(Objects.equals(entry.getId(), event.getEntry().getId()))
            {
                updateStubuCalendarEntry(inputEntry, entry);
                break;
            }
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
     */
    private void changeCalendar(CalendarEvent event)
    {
        System.out.println("Calendar: Changing calendar.");

        // Note: CalendarFX handles deletion of entries by nullifying its Calendar property!
        // If getCalendar() is null, it means that an entry was deleted in the UI.
        // The CalendarFX developer manual states that assigning null to the
        // Calendar property of the Entry object can count as a deletion.
        // Refer: https://dlsc-software-consulting-gmbh.github.io/CalendarFX/#_calendar
        if(event.getCalendar() == null)
        {
            // Case: If entry is removed in the entire calendar view
            System.out.println("Calendar: Removing calendar.");
            Entry<?> entry = event.getEntry();
            Calendar oldCalendar = event.getOldCalendar();
            removeEntryInOldCalendarAndSave(entry, oldCalendar);
        }
        else if(event.getOldCalendar() == null)
        {
            // Case: If the entry is a new entry
            System.out.println("Calendar: Creating calendar.");
            Entry<?> entry = event.getEntry();
            Calendar newCalendar = entry.getCalendar();
            createNewEntryInCalendar(entry, newCalendar);
        }
        else
        {
            // Case: If the user transfers the entry from one calendar to another
            System.out.println("Calendar: Changing entry's calendar designation.");
            System.out.printf("Calendar: Changing calendar from %s to %s %n",
                    event.getOldCalendar().getName(), event.getCalendar().getName());

            Entry<?> entry = event.getEntry();
            Calendar oldCalendar = event.getOldCalendar();
            changeEntryFromOldCalendarToNewCalendar(entry, oldCalendar);
        }
    }

    // endregion

    private static void updateStubuCalendarEntry(Entry<?> inputEntry, StubuCalendarEntry entry)
    {
        entry.setTitle(inputEntry.getTitle());
        entry.setLocation(inputEntry.getLocation());
        entry.setStartDate(inputEntry.getStartAsLocalDateTime());
        entry.setEndDate(inputEntry.getEndAsLocalDateTime());
        entry.setFullDay(inputEntry.isFullDay());
        entry.setMinimumDuration(inputEntry.getMinimumDuration());
        entry.setRecurrenceRule(inputEntry.getRecurrenceRule());
    }

    private void removeEntryInOldCalendarAndSave(Entry<?> entry, Calendar oldCalendar)
    {
        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);
        StubuCalendar oldStubuCalendar = removeEntryInOldCalendar(oldCalendar, stubuEntry);
        calendarService.saveCalendarToFile(oldStubuCalendar);
    }

    private void changeEntryFromOldCalendarToNewCalendar(Entry<?> entry, Calendar oldCalendar)
    {
        entry = (entry.isRecurrence()) ? entry.getRecurrenceSourceEntry() : entry;

        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);

        createNewEntryInCalendar(entry, entry.getCalendar());

        StubuCalendar oldStubuCalendar = removeEntryInOldCalendar(oldCalendar, stubuEntry);
        calendarService.saveCalendarToFile(oldStubuCalendar);
    }

    private void createNewEntryInCalendar(Entry<?> entry, Calendar newCalendar)
    {
        StubuCalendar stubuCalendar = findCalendarInListOfCalendarsInDomain(newCalendar);

        // If we did not find calendar in domain_stubuCalendarList,
        // create a new calendar (that will be the current calendar)
        if(stubuCalendar == null)
        {
            System.err.println("Creating new calendar...");
            stubuCalendar = StubuCalendarMapper.toStubuCalendarObject(newCalendar);
            domain_stubuCalendarList.addCalendar(stubuCalendar);
        }

        // Get entry and add it to the current calendar
        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);
        stubuCalendar.addEntry(stubuEntry);
    }

    /**
     * Removes the entry in the domain StubuCalendarList by obtaining the old calendar of the entry through
     * {@code CalendarEvent.getOldCalendar()} and removing the entry in the old calendar by the ID of the entry.
     * @return old StubuCalendar object reference.
     */
    private StubuCalendar removeEntryInOldCalendar(Calendar oldEventCalendar, StubuCalendarEntry stubuEntry)
    {
        StubuCalendar oldStubuCalendar = findCalendarInListOfCalendarsInDomain(oldEventCalendar);

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