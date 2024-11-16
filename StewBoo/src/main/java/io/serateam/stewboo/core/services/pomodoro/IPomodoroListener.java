package io.serateam.stewboo.core.services.pomodoro;

public interface IPomodoroListener
{
    /**
     * Updates the listener to the new state of the Pomodoro clock timer.
     * @param newRemainingSeconds contains the state of the Pomodoro clock timer (in seconds).
     */
    void onTimerUpdate(long newRemainingSeconds);
}
