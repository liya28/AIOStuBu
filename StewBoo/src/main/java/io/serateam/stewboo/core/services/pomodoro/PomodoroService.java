package io.serateam.stewboo.core.services.pomodoro;

import io.serateam.stewboo.core.services.IService;

// NOTE: PomodoroService is a utility class, hence, it shall be static

/**
 * Utility class. Controls the entire Pomodoro session of the application.
 */
public class PomodoroService implements IService
{
    @Override
    public void initializeService()
    {
        // TODO: remove this method and IService initializeService() since Service classes are utility classes; make all necessary adjustments for other classes implementing IService
    }

    private static final PomodoroClock clock = PomodoroClock.getInstance();
    private static boolean isRunning = false;

    private static void waitUntilComplete() {
        // Busy-wait until the current timer finishes
        // Learn more: https://www.baeldung.com/cs/os-busy-waiting
        while (clock.getRunningState()) {
            try {
                // Busy-waiting is expensive, do this to prevent high CPU usage :)
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
