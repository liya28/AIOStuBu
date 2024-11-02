package io.serateam.stewboo.core;

import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.Sample;
import io.serateam.stewboo.core.utility.SharedVariables;

// set up processes go here (config setup, authentication, file integrity checks, etc)
public class StewBoo
{
    public static void initializeCore()
    {
        Sample s = new Sample();
        JSONService.serializeAndWriteToFile(SharedVariables.test, s);
        Sample newS = JSONService.deserialize(SharedVariables.test, Sample.class);
        System.out.println(newS);
    }
}
