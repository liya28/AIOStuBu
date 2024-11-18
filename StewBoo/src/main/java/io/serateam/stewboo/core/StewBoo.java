package io.serateam.stewboo.core;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.services.pomodoro.PomodoroService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.Sample;
import io.serateam.stewboo.core.utility.SharedVariables;

// set up processes go here (config setup, authentication, file integrity checks, etc)
import java.util.ArrayList;
import java.util.List;
public class StewBoo
{
    public static void initializeCore()
    {
        Sample s = new Sample();
        JSONService.serializeAndWriteToFile(SharedVariables.Path.test, s);
        Sample newS = JSONService.deserialize(SharedVariables.Path.test, Sample.class);
        System.out.println(newS);

        // For simplicity, we will be opting to grouping our services into a list
        List<IService> services = new ArrayList<>();
        // TODO: Add more services if need be.
        services.add(PomodoroService.getInstance());

        for(IService service : services)
        {
            System.out.println("Initializing service: " + service.getClass().getSimpleName());
            service.initializeService();
        }

    }
}
