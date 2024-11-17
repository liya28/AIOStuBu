package io.serateam.stewboo.core.services.pomodoro;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// NOTE: Variables and methods are to be made private or package-private in this class, depending on context

class PomodoroClock
{
    private static final int DEFAULT_POMODORO_MINUTES = 25*60;
    private static final int DEFAULT_POMODORO_BREAK_TIME = 5*60;
    private static final int DEFAULT_POMODORO_LONG_BREAK_TIME = 15*60;

    private PomodoroSessionState currentSessionState = PomodoroSessionState.WORK_SESSION;
    private long remainingSeconds = DEFAULT_POMODORO_MINUTES;
    private int pomodoroCounter = 0;
    private boolean isBreak = false;
    private boolean isRunning = false;
    private Timer timer;
    private final List<IPomodoroListener> pomodoroListeners = new ArrayList<>();

    // region Clock Methods

    /**
     * @return {@code true} if clock is running, {@code false} otherwise.
     */
    boolean getRunningState()
    {
        return isRunning;
    }

    /**
     * @return {@code true} if it is break time, {@code false} otherwise.
     */
    boolean getBreakTimeState()
    {
        return isBreak;
    }

    /**
     * @return {@code true} if it is break time and
     * pomodoro counter is divisible by 4,
     * {@code false} otherwise.
     */
    boolean checkForLongBreak()
    {
        return getBreakTimeState() && (pomodoroCounter % 4 == 0);
    }


    /**
     * Stops the Pomodoro clock and resets the current Pomodoro timer.
     */
    void stopClock()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
        this.isRunning = false;
    }

    /**
     * Starts the Pomodoro clock.
     * <p>
     * Break time duration depends on the count of Pomodoros completed
     * (every 4 Pomodoros translate to a long break time duration).
     * One Pomodoro is added every completed work session.
     * <p>
     * Duration:
     * <ul>
     * <li><b>Work Session</b>: 25 minutes</li>
     * <li><b>Quick Break Session</b>: 5 minutes</li>
     * <li><b>Long Break Session</b>: 15 minutes</li>
     * </ul>
     * <p>
     * Decrements {@code remainingSeconds} every 1 second.
     * Every decrement of {@code remainingSeconds} notifies all listeners
     * of its current status.
     * <p>
     * Once {@code remainingSeconds} is exhausted to 0, the clock is stopped and 1 Pomodoro is added.
     * It also notifies listeners of the current time and status of Pomodoro counter.
     */
    void startClock()
    {
        stopClock();
        this.isRunning = true;

        if(checkForLongBreak())
        {
            this.remainingSeconds = DEFAULT_POMODORO_LONG_BREAK_TIME;
            currentSessionState = PomodoroSessionState.LONG_BREAK;
        }
        else if(getBreakTimeState())
        {
            this.remainingSeconds = DEFAULT_POMODORO_BREAK_TIME;
            currentSessionState = PomodoroSessionState.QUICK_BREAK;
        }
        else
        {
            this.remainingSeconds = DEFAULT_POMODORO_MINUTES;
            currentSessionState = PomodoroSessionState.WORK_SESSION;
        }
        notifyListenersOnStateChanged(currentSessionState);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                if(remainingSeconds > 0)
                {
                    remainingSeconds--;
                    notifyListenersOnTimerUpdate(remainingSeconds);
                }
                else
                {
                    if(currentSessionState == PomodoroSessionState.WORK_SESSION)
                    {
                        pomodoroCounter++;
                    }
                    isBreak = !isBreak;
                    stopClock();

                    if(currentSessionState == PomodoroSessionState.QUICK_BREAK
                            || currentSessionState == PomodoroSessionState.LONG_BREAK)
                    {
                        notifyListenersOnBreakComplete();
                    }
                    else
                    {
                        notifyListenersOnSessionComplete();
                        notifyListenersOnPomodoroCounter(pomodoroCounter);
                    }
                }
            }
        }, 0, 1000);
    }

    // endregion

    // region Update Listeners Methods

    void addListener(IPomodoroListener newListener)
    {
        pomodoroListeners.add(newListener);
    }

    void removeListener(IPomodoroListener listener)
    {
        pomodoroListeners.remove(listener);
    }

    // endregion

    // region Notify Listeners Methods

    private void notifyListenersOnTimerUpdate(long newRemainingSeconds)
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onTimerUpdate(newRemainingSeconds);
        }
    }

    private void notifyListenersOnPomodoroCounter(int newPomodoroCounter)
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onPomodoroCounterUpdate(newPomodoroCounter);
        }
    }

    private void notifyListenersOnSessionComplete()
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onSessionComplete();
        }
    }

    private void notifyListenersOnBreakComplete()
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onBreakComplete();
        }
    }

    private void notifyListenersOnStateChanged(PomodoroSessionState newState)
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onStateChanged(newState);
        }
    }

    // endregion
}