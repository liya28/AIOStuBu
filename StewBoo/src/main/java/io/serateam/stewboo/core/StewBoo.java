package io.serateam.stewboo.core;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.core.services.notes.NotesService;
import io.serateam.stewboo.core.services.todolist.TodoListService;
import io.serateam.stewboo.core.services.calendar.CalendarService;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// set up processes go here (config setup, authentication, file integrity checks, etc.)
public class StewBoo
{
    public static void initializeCore()
    {
        if(!new File(SharedVariables.Path.mainUserDirectory).exists())
        {
            try
            {
                Files.createDirectories(Paths.get(SharedVariables.Path.mainUserDirectory));
                System.out.println("Successfully created " + SharedVariables.Path.mainUserDirectory);
            }
            catch (IOException e)
            {
                System.err.println("Failed to create directory: " + SharedVariables.Path.mainUserDirectory);
            }
        }

        // For simplicity, we will be opting to grouping our services into a list
        List<IService> services = new ArrayList<>();

        // TODO: Add more services if need be.
        services.add(NotesService.getInstance());
        services.add(PomodoroService.getInstance());
        services.add(TodoListService.getInstance());
        services.add(CalendarService.getInstance());
        services.add(FlashCardService.getInstance());

        for(IService service : services)
        {
            System.out.println("Initializing service: " + service.getClass().getSimpleName());
            service.initializeService();
        }
    }
}
