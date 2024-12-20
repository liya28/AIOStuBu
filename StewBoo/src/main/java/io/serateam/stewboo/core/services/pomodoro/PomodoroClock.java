package io.serateam.stewboo.core.services.pomodoro;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// NOTE: Variables and methods are to be made private or package-private in this class, depending on context

class PomodoroClock
{
    private PomodoroSessionState currentSessionState = PomodoroSessionState.WORK_SESSION;
    private long remainingSeconds = PomodoroService.DEFAULT_POMODORO_MINUTES;
    private int pomodoroCounter = 0;
    private boolean isBreak = false;
    private boolean isRunning = false;
    private Timer timer;

    private final List<IPomodoroListener> pomodoroListeners = new ArrayList<>();
    private PomodoroSettings userConfig;

    // region Clock Methods

    public void resetClock()
    {
        isRunning = false;
        isBreak = false;
        pomodoroCounter = 0;
    }

    public void setDefaultTime()
    {
        userConfig = new PomodoroSettings(
                PomodoroService.DEFAULT_POMODORO_MINUTES,
                PomodoroService.DEFAULT_POMODORO_BREAK_TIME,
                PomodoroService.DEFAULT_POMODORO_LONG_BREAK_TIME);
        userConfig.saveToFile();
        notifyListenersOnNewTimeConfig(userConfig);
    }

    public void setTime(int workMinutes, int quickBreakMinutes, int longBreakMinutes)
    {
        userConfig = new PomodoroSettings(workMinutes, quickBreakMinutes, longBreakMinutes);
        userConfig.saveToFile();
        notifyListenersOnNewTimeConfig(userConfig);
    }

    /**
     * @return {@code true} if clock is running, {@code false} otherwise.
     */
    public boolean getRunningState()
    {
        return isRunning;
    }

    /**
     * @return {@code true} if it is break time, {@code false} otherwise.
     */
    public boolean getBreakTimeState()
    {
        return isBreak;
    }

    /**
     * @return {@code true} if it is break time and
     * pomodoro counter is divisible by 4,
     * {@code false} otherwise.
     */
    public boolean checkForLongBreak()
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
            this.remainingSeconds = userConfig.getLongBreakMinutes();
            currentSessionState = PomodoroSessionState.LONG_BREAK;
        }
        else if(getBreakTimeState())
        {
            this.remainingSeconds = userConfig.getQuickBreakMinutes();
            currentSessionState = PomodoroSessionState.QUICK_BREAK;
        }
        else
        {
            this.remainingSeconds = userConfig.getWorkMinutes();
            currentSessionState = PomodoroSessionState.WORK_SESSION;
        }
        notifyListenersOnStateChanged(currentSessionState);
        notifyListenersOnPomodoroCounter(pomodoroCounter);
        long initialSeconds = this.remainingSeconds;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                if(remainingSeconds > 0)
                {
                    remainingSeconds--;
                    notifyListenersOnTimerUpdate(remainingSeconds, initialSeconds);
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

    public void addListener(IPomodoroListener newListener)
    {
        pomodoroListeners.add(newListener);
    }

    public void removeListener(IPomodoroListener listener)
    {
        pomodoroListeners.remove(listener);
    }

    // endregion

    // region Notify Listeners Methods

    private void notifyListenersOnTimerUpdate(long newRemainingSeconds, long initialSeconds)
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onTimerUpdate(newRemainingSeconds, initialSeconds);
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

    private void notifyListenersOnNewTimeConfig(PomodoroSettings entity)
    {
        for(IPomodoroListener listener : pomodoroListeners)
        {
            listener.onNewPomodoroTimeConfig(
                    entity.getWorkMinutes(),
                    entity.getQuickBreakMinutes(),
                    entity.getLongBreakMinutes());
        }
    }

    // endregion
}