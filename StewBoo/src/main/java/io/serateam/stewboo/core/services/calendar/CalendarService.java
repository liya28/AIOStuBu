package io.serateam.stewboo.core.services.calendar;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// TODO The calendar hierarchy is as follows: StubuCalendarList -> StubuCalendar -> StubuCalendarEntry

public class CalendarService implements IService
{
    private static CalendarService instance;

    private CalendarService() {}

    public static CalendarService getInstance()
    {
        if(instance == null)
        {
            instance = new CalendarService();
        }
        return instance;
    }

    @Override
    public void initializeService()
    {
        /*
            TODO Load the list from file on first boot.
        */
        if(!new File(SharedVariables.Path.stubuCalendarDirectory).exists())
        {
            try
            {
                Files.createDirectories(Paths.get(SharedVariables.Path.stubuCalendarDirectory));
                System.out.println("Successfully created " + SharedVariables.Path.stubuCalendarDirectory);
            }
            catch (IOException e)
            {
                System.err.println("Failed to create directory: " + SharedVariables.Path.stubuCalendarDirectory);
            }
        }
        else
        {
            // Because the folder exists in the project, we can assume that there can be calendar files here
            StubuCalendarList calendarList = StubuCalendarList.getInstance();

            /*
            NotesList notesList = new NotesList();
        File folder = new File(SharedVariables.Path.notesDirectory);
        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".json")));
        if (files != null)
        {
            for (File file : files)
            {
                Note note = JSONService.deserialize(file.getPath(), Note.class);
                if (note != null)
                {
                    notesList.add(note);
                }
            }
        }
        return notesList;
            *
            * */
        }
    }

    public void saveCalendarToFile(StubuCalendar calendar)
    {
        String path = SharedVariables.Path.stubuCalendarDirectory + calendar.getName() + ".json";
        JSONService.serializeAndWriteToFile(path, calendar);
    }

    public void deleteCalendar(StubuCalendar calendar)
    {
        StubuCalendarList calendarList = StubuCalendarList.getInstance();
        calendarList.removeCalendar(calendar);
    }

    public StubuCalendarList loadCalendarListFromDirectory()
    {
        StubuCalendarList calendarList = StubuCalendarList.getInstance();
        File folder = new File(SharedVariables.Path.stubuCalendarDirectory);
        File[] files = folder.listFiles( (dir, name) -> name.endsWith(".json") );
        if(files != null)
        {
            for(File file : files)
            {
                StubuCalendar calendar = JSONService.deserialize(file.getPath(), StubuCalendar.class);
                if(calendar != null)
                {
                    calendarList.addCalendar(calendar);
                }
            }
        }
        return calendarList;
    }
}
