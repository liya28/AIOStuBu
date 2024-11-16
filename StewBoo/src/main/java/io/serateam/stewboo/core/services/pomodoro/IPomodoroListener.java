package io.serateam.stewboo.core.services.pomodoro;

public interface IPomodoroListener
{
    /**
     * Updates the listener to the new state of the Pomodoro clock timer.
     * @param newRemainingSeconds contains the state of the Pomodoro clock timer (in seconds).
     */
    void onTimerUpdate(long newRemainingSeconds);

    /**
     * Updates the listener to the new state of the Pomodoro counter.
     * @param newPomodoroCounter contains the state of the Pomodoro counter.
     */
    void onPomodoroCounterUpdate(int newPomodoroCounter);

    /**
     * Updates the listener to the completed session.
     */
    void onSessionComplete();

    /**
     * Updates the listener to the completed break session.
     */
    void onBreakComplete();
}
