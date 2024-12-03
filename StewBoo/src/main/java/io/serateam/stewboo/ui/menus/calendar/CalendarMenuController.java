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
                        newEntry.setHidden(entry.isHidden());
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

        // TODO Recurrence cases:
        //  NOTE: CHECK FIRST IF ENTRY IS RECURRENT, IN WHICH CASE, WE REFER TO THE RECURRENCE SOURCE (CHECK IF ENTRY IS RECURRENCE SOURCE)
        //  .
        //  Case 1: Upon changing content info of a recurrent entry, we refer to the recurrence source
        //  Case 2: Upon changing of a recurrent entry to another calendar, we should refer to the recurrence source
        //  Case 3: deletion of entry is taken care of.                                                                 DONE
        //
        //  Do not delete the current entry but delete all other entries: CalendarEvent.ENTRY_RECURRENCE_RULE_CHANGED


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
        // TODO Recurrence case. Do not delete the current entry but delete all other entries

        // Save the calendar after everything is sorted
        if(event.getEventType().getSuperType() == CalendarEvent.ENTRY_CHANGED)
        {

            boolean eventWasDeleted = event.getCalendar() == null;
            saveCalenderAndCalendarEntries(event, eventWasDeleted);
        }
    }

    private void saveCalenderAndCalendarEntries(CalendarEvent event, boolean isDeleteOperation)
    {
        System.out.println("Calendar: Saving calendar.");
        Calendar eventCalendar = (isDeleteOperation) ? event.getOldCalendar() : event.getCalendar();
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
                        entry.setHidden(inputEntry.isHidden());
                        entry.setMinimumDuration(inputEntry.getMinimumDuration());
                        entry.setRecurrent(inputEntry.isRecurrence());
                        if(inputEntry.isRecurrence())
                        {
                            entry.setRecurrenceRule(inputEntry.getRecurrenceRule());
                            entry.setRecurrenceId(inputEntry.getRecurrenceId());
                            entry.setRecurrenceSourceId(inputEntry.getRecurrenceSourceEntry().getId());
                        }
                        else
                        {
                            entry.setRecurrenceRule("");
                            entry.setRecurrenceId("");
                            entry.setRecurrenceSourceId("");
                        }
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
     */
    private void changeCalendar(CalendarEvent event)
    {
        System.out.println("Calendar: Changing calendar...");

        // Note: CalendarFX handles deletion of entries by nullifying its Calendar property!
        // If getCalendar() is null, it means that an entry was deleted in the UI.
        // The CalendarFX developer manual states that assigning null to the
        // Calendar property of the Entry object can count as a deletion.
        // Refer: https://dlsc-software-consulting-gmbh.github.io/CalendarFX/#_calendar
        if(event.getCalendar() == null)
        {
            // Case: If entry is removed in the entire calendar view
            System.out.println("Calendar: Removing calendar...");
            removeEntryInOldCalendarAndSave(event);
        }
        else if(event.getOldCalendar() == null)
        {
            // Case: If the entry is a new entry
            System.out.println("Calendar: Creating calendar...");
            createNewEntryInCalendar(event);
        }
        else
        {
            // Case: If the user transfers the entry from one calendar to another
            System.out.println("Calendar: Changing entry's calendar designation...");
            changeEntryFromOldCalendarToNewCalendar(event);
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
        entry.setHidden(inputEntry.isHidden());
        entry.setMinimumDuration(inputEntry.getMinimumDuration());
        entry.setRecurrent(inputEntry.isRecurrence());
        entry.setRecurrenceRule(inputEntry.getRecurrenceRule());
        entry.setRecurrenceId(inputEntry.isRecurrence() ? inputEntry.getRecurrenceId() : "");
        entry.setRecurrenceSourceId(inputEntry.isRecurrence() ? inputEntry.getRecurrenceSourceEntry().getId() : "");
    }

    private void removeEntryInOldCalendarAndSave(Entry<?> entry, Calendar oldCalendar)
    {
        Entry<?> entry = event.getEntry();
        StubuCalendarEntry stubuEntry = StubuCalendarMapper.toStubuCalendarEntryObject(entry);
        StubuCalendar oldStubuCalendar = removeEntryInOldCalendar(event, stubuEntry);
        calendarService.saveCalendarToFile(oldStubuCalendar);
    }

    private void changeEntryFromOldCalendarToNewCalendar(CalendarEvent event)
    {
        System.out.printf("Calendar: Changing calendar from %s to %s %n",
                event.getOldCalendar().getName(), event.getCalendar().getName());

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
