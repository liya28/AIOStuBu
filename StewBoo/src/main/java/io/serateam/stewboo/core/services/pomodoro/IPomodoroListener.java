package io.serateam.stewboo.core.services.pomodoro;

public interface IPomodoroListener
{
    /**
     * Updates the listener to the new state of the Pomodoro clock timer.
     *
     * @param newRemainingSeconds contains the state of the Pomodoro clock timer (in seconds).
     * @param initialSeconds
     */
    void onTimerUpdate(long newRemainingSeconds, long initialSeconds);

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

    /**
     * Updates the listener to the new session state.
     * @param newState contains the state of the session.
     * Refer to {@link PomodoroSessionState}.
     */
    void onStateChanged(PomodoroSessionState newState);

    /**
     * Updates the listener on the new Pomodoro configuration
     * for its work, quick break, and long break session durations
     */
    void onNewPomodoroTimeConfig(int workMinutes, int quickBreakMinutes, int longBreakMinutes);
}
