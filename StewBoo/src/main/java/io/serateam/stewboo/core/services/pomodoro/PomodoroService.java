package io.serateam.stewboo.core.services.pomodoro;

import io.serateam.stewboo.core.services.IService;

/**
 * Controls the entire Pomodoro session of the application.
 */
public class PomodoroService implements IService
{
    public static final int DEFAULT_POMODORO_MINUTES = 25*60;
    public static final int DEFAULT_POMODORO_BREAK_TIME = 5*60;
    public static final int DEFAULT_POMODORO_LONG_BREAK_TIME = 15*60;

    private static PomodoroService instance;
    private final PomodoroClock clock;
    private boolean onSession;

    // region Initializers

    private PomodoroService()
    {
        clock = new PomodoroClock();
        this.onSession = false;
    }

    public static PomodoroService getInstance()
    {
        if(instance == null)
        {
            instance = new PomodoroService();
        }
        return instance;
    }

    @Override
    public void initializeService()
    {
        PomodoroSettings userConfig = PomodoroSettings.loadEntityFromFile();
        if(userConfig == null) {
            System.out.println("Pomodoro: No config file found");
            saveDefaultConfigTime();
        }
        else saveNewConfigTime(userConfig.getWorkMinutes(), userConfig.getQuickBreakMinutes(), userConfig.getLongBreakMinutes());
    }

    // endregion

    // region User Configuration Methods

    public void saveDefaultConfigTime()
    {
        clock.setDefaultTime();
    }

    public void saveNewConfigTime(int workMinutes, int quickBreakMinutes, int longBreakMinutes)
    {
        clock.setTime(workMinutes, quickBreakMinutes, longBreakMinutes);
    }

    public PomodoroSettings getConfig()
    {
        return PomodoroSettings.loadEntityFromFile().getSettings();
    }

    // endregion

    // region Pomodoro Session Methods

    /**
     * Continuously runs in a separate thread until {@link PomodoroService#stopPomodoroSession()} is called by the user.
     */
    public void startPomodoroSession()
    {
        resetSession();
        if(clock.getRunningState()) return;

        this.onSession = true;
        new Thread(() -> {
            while(this.onSession)
            {
                clock.startClock();
                waitUntilComplete();

                if(!this.onSession) break;
            }
        }).start();
    }

    private void resetSession()
    {
        clock.resetClock();
    }

    /**
     * Utility method. Waits for the Pomodoro clock to end running.
     */
    private void waitUntilComplete()
    {
        // Busy-wait until the current timer finishes
        // Learn more: https://www.baeldung.com/cs/os-busy-waiting
        while (clock.getRunningState())
        {
            try
            {
                // Busy-waiting is expensive, do this to prevent high CPU usage :)
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stops the session as well as the current running clock.
     */
    public void stopPomodoroSession()
    {
        clock.stopClock();
        onSession = false;
    }

    // endregion

    // region Listener Methods

    public void addListener(IPomodoroListener listener)
    {
        clock.addListener(listener);
    }

    public void removeListener(IPomodoroListener listener)
    {
        clock.removeListener(listener);
    }

    // endregion
}