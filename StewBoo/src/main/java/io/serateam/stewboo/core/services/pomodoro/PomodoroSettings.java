package io.serateam.stewboo.core.services.pomodoro;

import io.serateam.stewboo.core.utility.ISerializable;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

/**
 * Entity for user-defined Pomodoro settings
 */
public class PomodoroSettings implements ISerializable
{
    private int workMinutes;
    private int quickBreakMinutes;
    private int longBreakMinutes;

    PomodoroSettings(int workMinutes, int quickBreakMinutes, int longBreakMinutes)
    {
        this.workMinutes = workMinutes;
        this.quickBreakMinutes = quickBreakMinutes;
        this.longBreakMinutes = longBreakMinutes;
    }

    public int getWorkMinutes()
    {
        return workMinutes;
    }

    public int getQuickBreakMinutes()
    {
        return quickBreakMinutes;
    }

    public int getLongBreakMinutes()
    {
        return longBreakMinutes;
    }

    public PomodoroSettings getSettings()
    {
        return this;
    }

    void saveToFile()
    {
        JSONService.serializeAndWriteToFile(SharedVariables.Path.pomodoroUserConfig, this);
    }

    static PomodoroSettings loadEntityFromFile()
    {
        return JSONService.deserialize(SharedVariables.Path.pomodoroUserConfig, PomodoroSettings.class);
    }
}
