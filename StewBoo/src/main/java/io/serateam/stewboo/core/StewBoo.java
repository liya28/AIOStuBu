package io.serateam.stewboo.core;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.services.calendar.CalendarService;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.Sample;
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

        // Test only!
        Sample s = new Sample();
        JSONService.serializeAndWriteToFile(SharedVariables.Path.test, s);
        Sample newS = JSONService.deserialize(SharedVariables.Path.test, Sample.class);
        System.out.println(newS);

        // For simplicity, we will be opting to grouping our services into a list
        List<IService> services = new ArrayList<>();
        // TODO: Add more services if need be.
        services.add(PomodoroService.getInstance());
        services.add(CalendarService.getInstance());

        for(IService service : services)
        {
            System.out.println("Initializing service: " + service.getClass().getSimpleName());
            service.initializeService();
        }

    }
}
