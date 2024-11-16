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
    private static final int DEFAULT_POMODORO_LONG_BREAK_TIME = 10*60;
    private static PomodoroClock instance;

    private final List<IPomodoroListener> pomodoroListeners = new ArrayList<>();
    private Timer timer;
    private int pomodoroCounter;
    private long remainingSeconds;
    private boolean isBreak;
    private boolean isRunning;
    private PomodoroSessionState currentState;

    private PomodoroClock()
    {
        pomodoroCounter = 0;
        remainingSeconds = 0;
        isBreak = false;
        isRunning = false;
        currentState = PomodoroSessionState.WORK_SESSION;
    }
}